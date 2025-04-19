<template>
    <div class="login">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <div class="logo-header">
        <img :src="logo" class="logo-img" />
        <div class="logo-text-group">
          <div class="logo-text">Codura</div>
          <div class="logo-subtext">让团队协作更高效，让代码更可信</div>
        </div>
      </div>
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            placeholder="账号"
          >
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter.native="handleLogin"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="code" v-if="captchaEnabled">
          <el-input
            v-model="loginForm.code"
            auto-complete="off"
            placeholder="验证码"
            style="width: 63%"
            @keyup.enter.native="handleLogin"
          >
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code">
            <img :src="codeUrl" @click="getCode" class="login-code-img" />
          </div>
        </el-form-item>
        <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
        <el-form-item style="width:100%;">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
          <div style="float: left;">
            <div class="link-type" @click="$router.push('/code-chat')">
              免费使用
            </div>
          </div>
          <div style="float: right;" v-if="register">
            <router-link class="link-type" :to="'/register'">立即注册</router-link>
          </div>
        </el-form-item>
      </el-form>
      <!--  底部  -->
      <div class="el-login-footer">
        <span>Copyright © {{ new Date().getFullYear() }} Devassist All Rights Reserved.</span>
      </div>
    </div>
  </template>
  
  <script>
  import { getCodeImg } from "@/api/login";
  import Cookies from "js-cookie";
  import { encrypt, decrypt } from '@/utils/jsencrypt'
  import logoImg from '@/assets/logo/logo.png'
  import ide_events from "../chat/js/events";
  import utils from "../chat/js/utils";

  import {EVENT_TYPES} from '../chat/js/events'
  export default {
    name: "Login",
    mixins:[ide_events,utils],
    data() {
      return {
        logo: logoImg,
        codeUrl: "",
        loginForm: {
          username: "admin",
          password: "admin123",
          rememberMe: false,
          code: "",
          uuid: ""
        },
        loginRules: {
          username: [
            { required: true, trigger: "blur", message: "请输入您的账号" }
          ],
          password: [
            { required: true, trigger: "blur", message: "请输入您的密码" }
          ],
          code: [{ required: true, trigger: "change", message: "请输入验证码" }]
        },
        loading: false,
        // 验证码开关
        captchaEnabled: true,
        // 注册开关
        register: false,
        redirect: undefined
      };
    },
    watch: {
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect;
        },
        immediate: true
      }
    },
    created() {
      this.getCode();
      this.getCookie();
      this.addEventHandler(EVENT_TYPES.FlashUserInfo,this.handleLoginSuccess)
      this.addEventHandler(EVENT_TYPES.LoginFail,this.handleLoginFail)
    },
    methods: {
      getCode() {
        getCodeImg().then(res => {
          this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
          if (this.captchaEnabled) {
            this.codeUrl = "data:image/gif;base64," + res.img;
            this.loginForm.uuid = res.uuid;
          }
        });
      },
      getCookie() {
        const username = Cookies.get("username");
        const password = Cookies.get("password");
        const rememberMe = Cookies.get('rememberMe')
        this.loginForm = {
          username: username === undefined ? this.loginForm.username : username,
          password: password === undefined ? this.loginForm.password : decrypt(password),
          rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
        };
      },
      // 发送登录事件
      sendLoginMessage(loginParams){
        this.postMessage(
            {
                type:EVENT_TYPES.Login,
                data:loginParams
            }
        )
      },
      handleLoginFail(data){
          this.loading = false;
          if (this.captchaEnabled) {
            this.getCode();
          }
          if(data && data.value && data.value.errorMessage){
              this.$modal.msgError(data.value.errorMessage)
          }
      },
      handleLoginSuccess(data){
        // 将用户信息记录在vuex里面
        if(data.type==EVENT_TYPES.FlashUserInfo){
          const result = data.value
          if (!result.error){
            // 更新用户信息
            let username=result.data.name
            let id=result.data.id
            let token=result.data.token
            let avatar=result.data.avatar
            let permissions=result.data.permissions
            let roles=result.data.roles
            this.$store.dispatch("FlashUserInfo",{username,id,token,avatar,permissions,roles}).then(res=>{
              this.$modal.msgSuccess(`登录成功`)
              this.$router.push({ path: '/code-chat' }).catch(() => { });
            }).catch(e=>{
              this.$modal.msgError(`用户信息保存异常，请联系管理员`)
            }).finally(()=>{
                this.loading = false;
            })
          }else{
            this.$modal.msgError(`登录失败：${result.errorMessage}`)
            this.loading = false;
          }
        }else{
          this.$modal.msgError(`服务异常，请稍后重试`)
        }
      },
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true;
            if (this.loginForm.rememberMe) {
              Cookies.set("username", this.loginForm.username, { expires: 30 });
              Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
              Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
            } else {
              Cookies.remove("username");
              Cookies.remove("password");
              Cookies.remove('rememberMe');
            }
            this.sendLoginMessage(this.loginForm)
          }
        });
      }
    }
  };
  </script>
  
  <style rel="stylesheet/scss" lang="scss">
  .login {
    display: flex;
    position: relative;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    height: 100%;
    background-image: url("../../../../assets/images/login-background.png");
    background-size: cover;
    width: 100%;
  }
  .top-header{
    position: absolute;
    width: 100px;
    height: 100px;
    display: flex;
    top: 0;
    padding: 5px;
    width: 100%;
  }
  
  
  .login-form {
    border-radius: 10px; /* 更圆的边角 */
    background: rgba(255, 255, 255, 0.7); /* 透明背景 */
    backdrop-filter: blur(10px); /* 毛玻璃效果 */
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); /* 阴影 */
    width: 400px;
    padding: 25px 25px 5px 25px;
    transition: transform 0.3s, box-shadow 0.3s; /* 动画效果 */
  
    &:hover {
      box-shadow: 0 8px 40px rgba(0, 0, 0, 0.2); /* 增强阴影 */
    }
    
    .el-input {
      height: 38px;
      input {
        height: 38px;
      }
    }
  
    .input-icon {
      height: 39px;
      width: 14px;
      margin-left: 2px;
    }
  }
  
  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }
  
  .login-code {
    width: 33%;
    height: 38px;
    float: right;
    
    img {
      cursor: pointer;
      vertical-align: middle;
    }
  }
  
  .el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
  }
  
  .login-code-img {
    height: 38px;
  }


.logo-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  gap: 10px;
  animation: fadeInUp 0.6s ease-out;

  .logo-img {
    width: 40px;
    height: 40px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    transition: transform 0.3s ease;
  }

  .logo-text-group {
    display: flex;
    flex-direction: column;
    line-height: 1.2;

    .logo-text {
      font-size: 22px;
      font-weight: 600;
      color: #1f2d3d;
      font-family: "Poppins", "Helvetica Neue", "Segoe UI", sans-serif;
      letter-spacing: 0.5px;
      transition: color 0.3s ease;
    }

    .logo-subtext {
      font-size: 13px;
      color: #666;
      font-weight: 400;
      font-family: "PingFang SC", "Helvetica Neue", sans-serif;
    }
  }

  &:hover {
    .logo-img {
      transform: scale(1.06);
    }

    .logo-text {
      color: #409EFF;
    }
  }
}

  </style>
  
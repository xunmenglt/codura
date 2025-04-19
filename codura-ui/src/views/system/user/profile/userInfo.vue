<template>
    <div class="info-container">
        <el-card class="avatar-card" shadow="hover">
            <div class="avatar"  @click="dialogCropper=true">
                <div class="avatar-bottom">
                    <img :src="user.avatar">
                </div>
                <div class="avatar-top">
                    <img :src="user.avatar">
                </div>
            </div>
        </el-card>
        <el-card shadow="never">
            <el-form label-width="80px" :model="form" ref="form" :rules="rules" size="mini" >
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="form.userName" disabled autocomplete="off" style="width: 400px"></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="nickName">
                    <el-input v-model="form.nickName" autocomplete="off" style="width: 400px"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form.email" autocomplete="off" style="width: 400px"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input type="textarea" resize="none" :rows="3" clear="bz" v-model="form.remark" autocomplete="off" style="width: 400px"></el-input>
                </el-form-item>
                <el-form-item label="所属角色">
                    <el-tag
                        v-for="item in roles"
                        :key="item.roleName"
                        effect="plain" style="margin-right:5px">
                        {{ item.roleZhName }}
                    </el-tag>
                </el-form-item>
                <el-form-item>
                    <el-button size="mini" type="primary" @click="save">保 存</el-button>
                    <el-button size="mini" type="danger" @click="close">关闭</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <el-dialog :visible.sync="dialogCropper">
            <PicCropper @cropperCancel="cropperCancelChange" @cropperConfirm="cropperConfirmChange"></PicCropper>
        </el-dialog>
    </div>
</template>

<script>
import {getUserProfile, updateUserProfile, uploadAvatar} from '@/api/system/user'
import PicCropper from '@/components/PicCropper'
export default {
    components:{PicCropper},
    name:'UserInfo',
    data() {
        return {
            dialogCropper:false,
            form:{
                userName:undefined,
                nickName:undefined,
                email:undefined,
                remark:undefined
            },
            user:{},
            roles:[],
            rules: {
                nickName: [
                    { required: true, message: "用户昵称不能为空", trigger: "blur" }
                ],
                email: [
                    { required: true, message: "邮箱地址不能为空", trigger: "blur" },
                    {
                        type: "email",
                        message: "请输入正确的邮箱地址",
                        trigger: ["blur", "change"]
                    }
                ],
            }
        }
    },
    created(){
        this.getUser()
    },
    methods:{
        cropperCancelChange(){
            this.dialogCropper=false
        },
        async cropperConfirmChange(e){
            if(e){
                const formData = new FormData();
                formData.append('avatarfile', e);
                const res =await uploadAvatar(formData)
                if(res&&res.code&&res.code===200){
                    this.getUser()
                    this.$store.dispatch('GetInfo')
                }
            }
            this.dialogCropper=false
        },
        getUser(){
            getUserProfile().then((res)=>{
                if(res && res.code===200){
                    this.user=res.user
                    this.roles=res.roles
                    if(this.user.avatar){
                        this.user.avatar='/api'+this.user.avatar
                    }else{
                        this.user.avatar=require("@/assets/images/profile.png")
                    }
                    this.form.userName=this.user.userName
                    this.form.nickName=this.user.nickName
                    this.form.email=this.user.email
                    this.form.remark=this.user.remark
                }
            }).catch((e)=>{
                this.$modal.msgError("获取用户信息失败")
            })
        },
        save(){
            this.$refs["form"].validate(valid=>{
                if (valid) {
                    updateUserProfile(this.form).then(response => {
                        this.$modal.msgSuccess("修改成功");
                        this.getUser()
                        this.$store.dispatch('GetInfo')
                    });
                }
            })
        },
        close() {
            this.$tab.closePage();
        }
    }
}
</script>

<style lang="less" scoped>
.info-container{
    display: flex;
    box-sizing: border-box;
}
.avatar-card{
    overflow:visible;
    margin-right: 5px;   
}

.avatar{
    width: 300px;
    height: 300px;
    position: relative;
    cursor: pointer;
    .avatar-bottom,.avatar-top{
        width: inherit;
        height: inherit;
        border-radius: 5px;
        position: absolute;
        background: #fff;
        img{
            height: 100%;
            border-radius: 5px;
        }
    }
}

.avatar-bottom::after{
    content: "+";
    position: absolute;
    border-radius: 5px;
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #eee;
    background: rgba(0, 0, 0, 0.5);
    font-size: 30px;
    font-style: normal;
}

.avatar-top{
    clip-path: circle(70%);
    transition: 1.5s;
}

.avatar-top:hover{
    clip-path: circle(0%);
}


/** .avatar:hover:after {
    content: '+';
    position: absolute;
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    bottom: 0;
    color: #eee;
    background: rgba(0, 0, 0, 0.5);
    font-size: 24px;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    cursor: pointer;
    line-height: 110px;
    border-radius: 5px;
}
**/
.bz {
    .el-textarea__inner {
        resize: none;
    }
}
</style>
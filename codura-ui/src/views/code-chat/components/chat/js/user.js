import { mapGetters } from 'vuex'
import {EVENT_TYPES} from './events'
export default{
    data() {
        return {
            userInfo:{
                username:"",
            }
        }
    },
    computed:{
        ...mapGetters([
            "programmername",
            "programmertoken",
            "programmeravatar"
        ])
    },
    created() {
        this.initUserInfo()
        // 添加更新用户的信息
        this.addEventHandler(EVENT_TYPES.FlashUserInfo,this.updateUserInfo)
        this.addEventHandler(EVENT_TYPES.LogoutFail,this.handleLogoutFail)
        this.addEventHandler(EVENT_TYPES.LogoutSuccess,this.handleLogoutSuccess)

    },
    methods: {
        initUserInfo(){
            this.getUserInfo()
        },
        getUserInfo(){
            this.postMessage(
                {
                    type:EVENT_TYPES.GetUserInfo,
                }
            )
        },
        updateUserInfo(data){
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
              }).catch(e=>{
                this.$modal.msgError(`用户信息初始化异常，请联系管理员`)
              })
            }else{
                if(result.errorMessage){
                    this.$modal.msgError(`登录失败：${result.errorMessage}`)
                }
            }
          }else{
            this.$modal.msgError(`服务异常，请稍后重试`)
          }
        },
        toLogout(){
            // 请求服务删除用户信息
            this.postMessage({
                type:EVENT_TYPES.Logout
            })
        },
        handleLogoutFail(data){
            const result = data.value
            if (result.errorMessage){
                this.$modal.msgError(`退出失败：${result.errorMessage}`)
            }
        },
        handleLogoutSuccess(data){
            this.$modal.msgSuccess(`退出成功`)
            this.$store.dispatch("FedLogOut")
        }
    },
    
}
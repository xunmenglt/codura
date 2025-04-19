<template>
  <div class="app-container">
    <el-card>
      <el-tabs>
        <el-tab-pane label="个人信息">
          <user-info></user-info>
        </el-tab-pane>
        <el-tab-pane label="修改密码">
          <reset-pwd></reset-pwd>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import UserInfo from './userInfo.vue';
import ResetPwd from './resetPwd.vue'
export default {
    components:{UserInfo,ResetPwd}

}
</script>

<style>

</style>
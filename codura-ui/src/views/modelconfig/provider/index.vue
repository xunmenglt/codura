<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <el-divider content-position="left"><span style="font-size: 18px;">模型服务配置</span></el-divider>

    <el-tabs v-model="activeTab" type="card">
      <!-- 聊天模型配置 -->
      <el-tab-pane label="聊天模型配置" name="chat">
        <div v-loading="isLoading" element-loading-text="加载中...">
        <el-form :model="chatConfig" label-width="120px" >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="服务名称">
                <el-input v-model="chatConfig.name" placeholder="请输入服务名称" />
              </el-form-item>
              <el-form-item label="OpenAPI 类型">
                <el-select v-model="chatConfig.openaiType" placeholder="请选择">
                  <el-option label="openai" value="openai" />
                  <el-option label="azure" value="azure" />
                  <el-option label="custom" value="custom" />
                </el-select>
              </el-form-item>

              <el-form-item label="模型名称">
                <el-input v-model="chatConfig.modelName" placeholder="请输入模型名称" />
              </el-form-item>
              <el-form-item label="请求路径">
                <el-input v-model="chatConfig.path" placeholder="/v1/chat/completions" />
              </el-form-item>
              <el-form-item label="API KEY">
                <el-input
                    v-model="chatConfig.apiKey"
                    :type="showChatKey ? 'text' : 'password'"
                    placeholder="请输入API密钥"
                >
                  <template slot="append">
                    <i
                        class="el-icon-view"
                        @click="showChatKey = !showChatKey"
                        :style="{ color: showChatKey ? '#409EFF' : '#aaa', transform: showChatKey ? 'none' : 'scaleX(-1)' }"
                        style="cursor: pointer"
                    />

                  </template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        </div>
      </el-tab-pane>

      <!-- 补全模型配置 -->
      <el-tab-pane label="补全模型配置" name="fim">
        <div v-loading="isLoading" element-loading-text="加载中...">
        <el-form :model="fimConfig" label-width="120px" v-loading="isLoading">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="服务名称">
                <el-input v-model="fimConfig.name" placeholder="请输入服务名称" />
              </el-form-item>
              <el-form-item label="OpenAPI 类型">
                <el-select v-model="fimConfig.openaiType" placeholder="请选择">
                  <el-option label="openai" value="openai" />
                  <el-option label="azure" value="azure" />
                  <el-option label="custom" value="custom" />
                </el-select>
              </el-form-item>
              <el-form-item label="填充类型">
                <el-input v-model="fimConfig.fillType" placeholder="fillcode-use-chat" />
              </el-form-item>
              <el-form-item label="模型名称">
                <el-input v-model="fimConfig.modelName" placeholder="deepseek-coder" />
              </el-form-item>
              <el-form-item label="请求路径">
                <el-input v-model="fimConfig.path" />
              </el-form-item>
              <el-form-item label="API KEY">
                <el-input
                    v-model="fimConfig.apiKey"
                    :type="showFimKey ? 'text' : 'password'"
                    placeholder="请输入API密钥"
                >
                  <template slot="append">
                    <i
                        class="el-icon-view"
                        @click="showFimKey = !showFimKey"
                        :style="{ color: showFimKey ? '#409EFF' : '#aaa', transform: showFimKey ? 'none' : 'scaleX(-1)' }"
                        style="cursor: pointer"
                    />

                  </template>
                </el-input>
              </el-form-item>
            </el-col>

          </el-row>
        </el-form>
          </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 按钮 -->
    <el-divider />
    <div class="text-center">
<!--      <el-button @click="reset" icon="el-icon-refresh">重置</el-button>-->
      <el-button type="warning" @click="loadDefault" icon="el-icon-refresh-left">默认配置</el-button>
      <el-button type="primary" @click="save" icon="el-icon-check">保存</el-button>
    </div>
  </div>
</template>

<script>
import { getConfigByUserName,addConfig,updateConfig } from '@/api/config/modelconfig';
import {parseTime} from "@/utils/xunmeng";
export default {
  data() {
    return {

      isLoading: false,
      activeTab: 'chat',
      showChatKey: false,
      showFimKey: false,
      chatConfig: {
        name: undefined,
        openaiType: undefined,
        businessType: 0,
        modelName: undefined,
        path: undefined,
        apiKey: undefined
      },
      fimConfig: {
        name: undefined,
        openaiType:undefined,
        businessType: 1,
        fillType: undefined,
        modelName: undefined,
        path:undefined,
        apiKey: undefined
      }
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.isLoading = true;
      const userName = this.$store.state.user.name;
      getConfigByUserName(userName).then(res => {
        const data = res.data || [];
        if (data.length === 0) {
          // 当前用户无配置时，查 admin 配置作为默认
          this.getUserConfig('admin');
        } else {
          this.applyConfigList(data);
        }
      }).catch(() => {
        this.getUserConfig('admin');
      }).finally(() => {
        this.isLoading = false;
      });
    },

    applyConfigList(data) {
      data.forEach(item => {
        if (item.businessType === 0) {
          this.chatConfig = { ...this.chatConfig, ...item };
        } else if (item.businessType === 1) {
          this.fimConfig = { ...this.fimConfig, ...item };
        }
      });
    },

    getUserConfig(userName) {
      getConfigByUserName(userName).then(res => {
        const data = res.data || [];
        if (userName === 'admin') {
          // 是 admin 配置：clone 一份给当前用户用
          const cloned = data.map(item => {
            return {
              ...item,
              id: null, // 保证保存时会 insert
              userName: this.$store.state.user.name // 属于当前用户
            };
          });
          this.applyConfigList(cloned);
        } else {
          this.applyConfigList(data);
        }
      });
    },

    save() {
      const now = parseTime(new Date());
      const chatConfig = { ...this.chatConfig, updateTime: now };
      const fimConfig = { ...this.fimConfig, updateTime: now };
      const saveChat = this.chatConfig.id==null
          ? addConfig(chatConfig)
          : updateConfig(chatConfig);

      const saveFim = this.fimConfig.id==null
          ? addConfig(fimConfig)
          : updateConfig(fimConfig);

      // 使用 Promise.all 并用 .then/.catch 链式处理
      Promise.all([saveChat, saveFim])
          .then(() => {
            this.$message.success('配置已保存！');
          })
          .catch(error => {
            this.$message.error('配置保存失败，请重试');
          });
    },

    loadDefault() {
      this.getUserConfig('admin');
      this.$message.success('恢复系统默认');
    }
  }
};
</script>

<style scoped>
.app-container {
  background: #fff;
  padding: 24px;
  border-radius: 4px;
}
/* 可选：全屏遮罩时加强背景层级 */
.el-loading-mask {
  z-index: 9999 !important;
}

</style>

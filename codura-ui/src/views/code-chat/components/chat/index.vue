<template>
  <div class="container">
    <!-- 渲染聊天窗口标题信息 -->
    <div class="header">
      <div class="title">
        {{ conversation.title || "新对话" }}
      </div>
      <div class="username">
        <el-dropdown>
          <span class="el-dropdown-link">
            <span v-if="programmertoken">{{ programmername }}</span>
            <span v-else>未登录</span>
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <div @click="toLogin" v-if="!programmertoken">登录</div>
              <div @click="toLogout" v-else>退出登录</div>
            </el-dropdown-item>
            <el-dropdown-item>
              <div @click="toTutorial">使用教程</div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <!-- 消息体 -->
    <div class="conversation-container" id="conversation-container">
      <div
        class="message-container"
        v-if="
          conversation &&
          conversation.messages &&
          conversation.messages.length > 0
        "
      >
        <div
          :class="[
            'message-item',
            item.role === ASSISTANT ? 'assistant-message' : 'user-message',
          ]"
          v-for="(item, index) in conversation.messages"
          :key="index"
        >
          <div class="avatar-container">
            <span class="avatar">
              <img v-if="item.role === ASSISTANT" :src="CodealienImage" />
              <img v-else :src="UserImage" />
            </span>
            <span class="role-name">
              <span v-if="item.role === ASSISTANT">{{ $app_name }}</span>
              <span v-else> YOU </span>
            </span>
          </div>
          <div class="content-message">
            <div
              v-html="convert2Markdown(item.content)"
              class="markdown-content"
            ></div>
          </div>
        </div>
        <div v-if="completion && completion.content" class="assistant-message">
          <div class="avatar-container">
            <span class="avatar">
              <img :src="CodealienImage" />
            </span>
            <span class="role-name">
              <span>DevAssist</span>
            </span>
          </div>
          <div class="content-message">
            <div
              v-html="convert2Markdown(completion.content)"
              class="markdown-content"
            ></div>
          </div>
        </div>
      </div>
      <div class="welcome-container" v-else>
        <img :src="CodealienImage" style="width: 50%" />
        <div
          class="welcome-container-text"
          style="font-weight: 600; font-size: larger"
        >
          欢迎使用{{ $app_name }}代码编程助手
        </div>
        <div class="welcome-container-text" style="font-size: 10px">
          让编程如此简单
        </div>
      </div>
    </div>
    <div class="bottom">
      <!-- 工具栏 -->
      <div class="tool-contianer">
        <div class="right">
          <div class="tool-item scroll-botom-control" @click="scrollToBottom">
            <img class="need-invert" :src="ScrollBotomImage" />
          </div>
          <div class="tool-item">
            <div>{{ selectedTextLength }}</div>
          </div>
        </div>
        <div class="left">
          <el-tooltip
            class="item"
            effect="dark"
            content="Stop Generate"
            placement="top-end"
          >
            <div
              v-if="generating"
              class="tool-item"
              @click="handleStopGeneration"
            >
              <img :src="StopImage" />
            </div>
          </el-tooltip>
        </div>
      </div>
      <!-- 发送窗口 -->
      <div class="sendinput-container">
        <!--                   <el-input @keyup.native="onKeyUpSend" ref="chatInputRef" :disabled="generating" v-model="inputText" :placeholder="generating?'回复中...':'请输入问题 ctrl + enter 发送'">&ndash;&gt;-->
        <!--                       <el-button v-if="generating" slot="append" icon="el-icon-loading"></el-button>-->
        <!--                       <el-button v-else slot="append" icon="el-icon-s-promotion" @click="handleSubmitForm"></el-button>-->
        <!--                    </el-input> -->

        <el-input
          id="input"
          type="textarea"
          @keyup.enter.native="onKeyUpSend"
          @compositionstart="onCompositionStart"
          @compositionend="onCompositionEnd"
          ref="chatInputRef"
          :disabled="generating"
          v-model="inputText"
          :placeholder="generating ? '回复中...' : '请输入问题 按Enter键发送'"
          :autosize="{ minRows: 1, maxRows: 4 }"
        >
        </el-input>
        <el-button
          v-if="generating"
          icon="el-icon-loading"
          style="line-height: 0.7px"
        ></el-button>
        <el-button
          v-else
          icon="el-icon-s-promotion"
          @click="handleSubmitForm"
          style="line-height: 0.7px"
        ></el-button>
      </div>
    </div>
  </div>
</template>
  
  <script>
import mainJS from "./js/main";
export default {
  ...mainJS,
};
</script>
  
<style lang="less">
  @import "./css/index.less";
</style>
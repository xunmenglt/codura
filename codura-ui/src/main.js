import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Cookies from 'js-cookie'
import directive from './directive' // directive


import plugins from './plugins' // plugins

// elui
import ElementUI from 'element-ui';
import './assets/styles/element-variables.scss'

// 弹窗工具
import Toast from "vue-toastification";
import "vue-toastification/dist/index.css";

// 引入图标
import '@mdi/font/css/materialdesignicons.css'

import './assets/icons'

import './permission' // permission control

// 自定义css
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css

// 阿里巴巴矢量图
import '@/assets/styles/icon/iconfont.js'
import '@/assets/styles/icon/iconfont.css'

import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from "@/utils/xunmeng.js";

Vue.config.productionTip = false



Vue.use(ElementUI,{
  size: Cookies.get('size') || 'medium'
});


Vue.use(Toast, {
  transition: "Vue-Toastification__bounce",
  maxToasts: 3,
  newestOnTop: false
});

Vue.use(directive)
Vue.use(plugins)

Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.handleTree = handleTree

Vue.prototype.$app_name = process.env.VUE_APP_NAME


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import FastClick from 'fastclick'
import store from './store'
import router from './router'
import App from './App'

//  引入API文件
import api from './api/index.js'

//  引入插件
import notify from './utils/notice'

import { AlertPlugin, LoadingPlugin, Confirm, WechatPlugin, AjaxPlugin  } from 'vux'

Vue.use(AlertPlugin)
Vue.use(LoadingPlugin)
Vue.use(Confirm)
Vue.use(WechatPlugin)
Vue.use(AjaxPlugin)

//  将API方法绑定到全局
Vue.prototype.$api = api

Vue.prototype.$notify = notify

FastClick.attach(document.body)

Vue.config.productionTip = false

Vue.config.devtools = true;

/* eslint-disable */
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app-box')

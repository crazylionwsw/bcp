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
import wechatAuth from './utils/wechatAuth'//微信登录插件

import { AlertPlugin, LoadingPlugin, Confirm } from 'vux'

Vue.use(AlertPlugin)
Vue.use(LoadingPlugin)
Vue.use(Confirm)
Vue.use(wechatAuth, {appid: 1000006});

//  将API方法绑定到全局
Vue.prototype.$api = api

Vue.prototype.$notify = notify

/**
 *  公用组件
 */
Vue.prototype.updateTitle = function (value) {
  this.$store.commit('updateTitle', value)
}

FastClick.attach(document.body)

Vue.config.productionTip = false

Vue.config.devtools = true;

/* eslint-disable */
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app-box')

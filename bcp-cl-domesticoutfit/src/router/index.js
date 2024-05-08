import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

import App from '../App'
import Layout from '../views/layout/Layout'

Vue.use(VueRouter)

export const constantRouterMap = [
  { path: '/404', component: resolve => require(['../views/errorPage/404'], resolve), hidden: true },
  { path: '/401', component: resolve => require(['../views/errorPage/401'], resolve), hidden: true },
  {
    path: '',
    component: Layout,
    redirect: 'domesticoutfit/list',
    children: [
      {
        path: 'domesticoutfit/list',
        component: resolve => require(['../views/domesticoutfit/list'], resolve),
        name: 'list',
        meta: { title: 'list', icon: 'list', noCache: true }
      },
      {
        path: 'domesticoutfit/create',
        component: resolve => require(['../views/domesticoutfit/create'], resolve),
        name: 'create',
        meta: { title: 'create' }
      },
      {
        path: 'domesticoutfit/edit/:id',
        component: resolve => require(['../views/domesticoutfit/edit'], resolve),
        name: 'edit',
        meta: { title: 'edit' }
      },
      {
        path: 'domesticoutfit/history',
        component: resolve => require(['../views/domesticoutfit/history'], resolve),
        name: 'history',
        meta: { title: 'history', icon: 'history', noCache: true }
      },
    ]
  }
]

// const router = new VueRouter({
//   base:__dirname,
//   linkActiveClass:'link-active',
//   routes: [
//     {
//       path: '',
//       name: 'list',
//       component: resolve => require(['../views/domesticoutfit/list'],resolve),
//       children: [
//         {
//           path: '/entity',
//           name: 'entity',
//           component:resolve => require(['../views/domesticoutfit/entity'],resolve),
//           meta:{title:'添加'}
//         },
//         {
//           path:'/my',
//           name:'my',
//           component:resolve => require(['../components/my'],resolve),
//           meta:{title:'我的'}
//         }
//       ]
//     },
//
//   ]
// });

const router = new VueRouter({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

router.beforeEach(function (to, from, next) {
  store.dispatch('UpdateLoading', {loading: true})
  store.dispatch('UpdateShowBack', {showBack: true})

  console.log('to.name:' + to.name + 'to.query:' + to.query)
  if (to.name === '/' && to.query.code !== undefined && to.query.code !== null) {
    this.$store.dispatch('GetUserInfoByCode', to.query.code).then(() => {
      this.$router.push({ path: '/' })
    }).catch(() => {
    })
  }

  next()

  /*if (store.state.user.loginStatus == 0) {
    //微信未授权登录跳转到授权登录页面
    wechatAuth.redirect_uri = window.location.href
    store.dispatch('SET_LOGIN_STATUS', 1)
    window.location.href = wechatAuth.authUrl
  } else if (store.state.user.loginStatus == 1) {
    try {
      wechatAuth.returnFromWechat(to.fullPath);
    } catch (err) {
      store.dispatch('SET_LOGIN_STATUS', 0)
      next()
    }
    store.dispatch('LoginWeChatAuth', wechatAuth.code).then((res) => {
      if (res.status == 1) {
        store.dispatch('SET_LOGIN_STATUS', 2)
      } else {
        store.dispatch('SET_LOGIN_STATUS', 0)
      }
      next()
    }).catch((err) => {
      next()
    })
  }else {
    next()
  }*/
})

router.afterEach(function (to) {
  store.dispatch('UpdateLoading', {loading: false})
})

/**
 *  路由出口
 */
export default router

export const asyncRouterMap = [
  {
    path: '/error',
    component: App,
    redirect: 'noredirect',
    name: 'errorPages',
    meta: {
      title: 'errorPages',
      icon: '404'
    },
    children: [
      { path: '401', component: resolve => require(['../views/errorPage/401'], resolve), name: 'page401', meta: { title: 'page401', noCache: true }},
      { path: '404', component: resolve => require(['../views/errorPage/404'], resolve), name: 'page404', meta: { title: 'page404', noCache: true }}
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]

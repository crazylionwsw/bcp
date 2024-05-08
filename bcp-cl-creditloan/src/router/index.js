import Vue from 'vue'
import VueRouter from 'vue-router'

import App from '../App'
import Layout from '../views/layout/Layout'

Vue.use(VueRouter)

export const constantRouterMap = [
  { path: '/404', component: resolve => require(['../views/errorPage/404'], resolve), hidden: true },
  { path: '/401', component: resolve => require(['../views/errorPage/401'], resolve), hidden: true },
  {
    path: '/creditloan',
    component: Layout,
    redirect: '/creditloan/active',
    children: [
      {
        path: 'active',
        component: resolve => require(['../views/creditloan/active'], resolve),
        name: 'active',
        meta: { title: 'active' }
      },
      {
        path: 'create',
        component: resolve => require(['../views/creditloan/create'], resolve),
        name: 'create',
        meta: { title: 'create' }
      },
      {
        path: 'detail',
        component: resolve => require(['../views/creditloan/detail'], resolve),
        name: 'detail',
        meta: { title: 'detail' }
      }
    ]
  },
  {
    path: '/employee',
    component: Layout,
    redirect: '/employee/auth',
    children: [
      {
        path: 'auth',
        component: resolve => require(['../views/employee/auth'], resolve),
        name: 'employeeAuth',
        meta: { title: 'employeeAuth' }
      },
      {
        path: 'bind',
        component: resolve => require(['../views/employee/bind'], resolve),
        name: 'employeeBind',
        meta: { title: 'employeeBind' }
      }
    ]
  },
  {
    path: '',
    component: Layout,
    redirect: 'auth',
    children: [
      {
        path: '/auth',
        component: resolve => require(['../views/auth/index'], resolve),
        name: 'auth',
        meta: { title: 'auth' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/auth',
    children: [
      {
        path: 'auth',
        component: resolve => require(['../views/user/auth'], resolve),
        name: 'userAuth',
        meta: { title: 'userAuth' }
      },
      {
        path: 'info',
        component: resolve => require(['../views/user/info'], resolve),
        name: 'info',
        meta: { title: 'info' }
      },
      {
        path: 'bind',
        component: resolve => require(['../views/user/bind'], resolve),
        name: 'bind',
        meta: { title: 'bind' }
      },
      {
        path: 'login',
        component: resolve => require(['../views/user/login'], resolve),
        name: 'login',
        meta: { title: 'login' }
      },
      {
        path: 'reg',
        component: resolve => require(['../views/user/register'], resolve),
        name: 'reg',
        meta: { title: 'reg' }
      }
    ]
  },
  { path: '/attention', component: resolve => require(['../views/auth/attention'], resolve), hidden: true, name: 'attention' }
]

const router = new VueRouter({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

router.beforeEach(function (to, from, next) {
  next()
})

router.afterEach(function (to) {
})

/**
 *  路由出口
 */
export default router

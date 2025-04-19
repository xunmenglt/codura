import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layout'
import { createWebHistory } from 'vue-router';
Vue.use(VueRouter)

export const constantRoutes=[
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon:'user' }
      }
    ]
  },
  {
    path: '/code-chat',
    component: () => import('@/views/code-chat'),
    hidden: true,
    redirect: '/code-chat/chat',
    children:[
      {
        path: 'chat',
        component: () => import('@/views/code-chat/components/chat'),
        name: 'chat',
      },
      {
        path: 'login',
        component: () => import('@/views/code-chat/components/login'),
        name: 'login',
      },
      {
        path: 'tutorial',
        component: () => import('@/views/code-chat/components/tutorial'),
        name: 'tutorial',
      }
    ]
  }
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  {
    path: '/system/notice-alter',
    component: Layout,
    hidden: true,
    permissions: ['system:notice:edit','system:notice:add'],
    children: [
      {
        path: 'notice/:noticeId(\\d+)',
        component: () => import('@/views/system/notice/alter'),
        name: 'NoticeAlter',
        meta: { title: '公告管理', activeMenu: '/system/notice' }
      }
    ]
  }
]



// 防止连续点击多次路由报错
let routerPush = VueRouter.prototype.push;
let routerReplace = VueRouter.prototype.replace;
// push
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
VueRouter.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}


const router = new VueRouter({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes:constantRoutes
})

export default router

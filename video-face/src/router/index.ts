import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    children: [
      {
        path: '',
        name: 'home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'trending',
        name: 'trending',
        component: () => import('@/views/Trending.vue'),
        meta: { title: '热门' }
      },
      {
        path: 'subscriptions',
        name: 'subscriptions',
        component: () => import('@/views/Subscriptions.vue'),
        meta: { title: '订阅' }
      },
      {
        path: 'library',
        name: 'library',
        component: () => import('@/views/Library.vue'),
        meta: { title: '媒体库' }
      }
    ]
  },
  {
    path: '/watch/:id',
    name: 'watch',
    component: () => import('@/views/Watch.vue'),
    meta: { title: '观看' }
  },
  {
    path: '/channel/:id',
    name: 'channel',
    component: () => import('@/views/Channel.vue'),
    meta: { title: '频道' }
  },
  {
    path: '/search',
    name: 'search',
    component: () => import('@/views/Search.vue'),
    meta: { title: '搜索' }
  },
  {
    path: '/studio',
    component: () => import('@/layouts/StudioLayout.vue'),
    children: [
      {
        path: '',
        name: 'studio',
        component: () => import('@/views/studio/Dashboard.vue'),
        meta: { title: '创作者工作室' }
      },
      {
        path: 'content',
        name: 'studio-content',
        component: () => import('@/views/studio/Content.vue'),
        meta: { title: '内容管理' }
      },
      {
        path: 'analytics',
        name: 'studio-analytics',
        component: () => import('@/views/studio/Analytics.vue'),
        meta: { title: '数据分析' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title} - Video Platform`
  next()
})

export default router 
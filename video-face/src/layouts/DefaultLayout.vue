<template>
  <div class="layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-left">
        <el-button class="menu-btn" icon="Menu" @click="toggleSidebar" />
        <router-link to="/" class="logo">
          <img src="@/assets/logo.png" alt="Logo" />
          <span>Video Platform</span>
        </router-link>
      </div>
      
      <div class="header-center">
        <div class="search-box">
          <el-input
            v-model="searchQuery"
            placeholder="搜索"
            @keyup.enter="handleSearch"
          >
            <template #suffix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>
      
      <div class="header-right">
        <el-button v-if="!isLoggedIn" @click="handleLogin">登录</el-button>
        <template v-else>
          <el-button @click="goToStudio">上传</el-button>
          <el-dropdown>
            <el-avatar :src="userAvatar" />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goToChannel">我的频道</el-dropdown-item>
                <el-dropdown-item @click="goToStudio">创作者工作室</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </div>
    </header>

    <div class="main-container">
      <!-- 侧边栏 -->
      <aside class="sidebar" :class="{ collapsed: !sidebarOpen }">
        <el-menu :collapse="!sidebarOpen">
          <el-menu-item index="home" @click="$router.push('/')">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="trending" @click="$router.push('/trending')">
            <el-icon><TrendCharts /></el-icon>
            <span>热门</span>
          </el-menu-item>
          <el-menu-item index="subscriptions" @click="$router.push('/subscriptions')">
            <el-icon><Collection /></el-icon>
            <span>订阅</span>
          </el-menu-item>
          <el-menu-item index="library" @click="$router.push('/library')">
            <el-icon><VideoPlay /></el-icon>
            <span>媒体库</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <!-- 主要内容区域 -->
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const searchQuery = ref('')
const sidebarOpen = ref(true)
const isLoggedIn = ref(false) // 这里应该从store中获取
const userAvatar = ref('https://example.com/default-avatar.png')

const toggleSidebar = () => {
  sidebarOpen.value = !sidebarOpen.value
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({
      path: '/search',
      query: { q: searchQuery.value }
    })
  }
}

const handleLogin = () => {
  // 实现登录逻辑
}

const handleLogout = () => {
  // 实现登出逻辑
}

const goToStudio = () => {
  router.push('/studio')
}

const goToChannel = () => {
  router.push(`/channel/${userStore.userId}`)
}
</script>

<style scoped lang="scss">
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 56px;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-bottom: 1px solid #e5e5e5;
  z-index: 100;

  &-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .logo {
      display: flex;
      align-items: center;
      gap: 8px;
      text-decoration: none;
      color: #000;

      img {
        height: 24px;
      }
    }
  }

  &-center {
    flex: 1;
    max-width: 640px;
    margin: 0 40px;

    .search-box {
      width: 100%;
    }
  }

  &-right {
    display: flex;
    align-items: center;
    gap: 16px;
  }
}

.main-container {
  display: flex;
  margin-top: 56px;
  flex: 1;
}

.sidebar {
  width: 240px;
  height: calc(100vh - 56px);
  position: fixed;
  left: 0;
  overflow-y: auto;
  background: #fff;
  transition: width 0.3s;

  &.collapsed {
    width: 72px;
  }
}

.main-content {
  flex: 1;
  margin-left: 240px;
  padding: 24px;
  transition: margin-left 0.3s;

  .sidebar.collapsed + & {
    margin-left: 72px;
  }
}
</style> 
<template>
  <div class="studio-layout">
    <!-- 顶部导航栏 -->
    <header class="studio-header">
      <div class="header-left">
        <router-link to="/studio" class="logo">
          <img src="@/assets/logo.png" alt="Logo" />
          <span>创作者工作室</span>
        </router-link>
      </div>
      
      <div class="header-right">
        <el-dropdown>
          <el-avatar :src="userAvatar" />
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToChannel">我的频道</el-dropdown-item>
              <el-dropdown-item @click="goToHome">返回首页</el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="studio-container">
      <!-- 侧边栏 -->
      <aside class="studio-sidebar">
        <el-menu default-active="dashboard">
          <el-menu-item index="dashboard" @click="$router.push('/studio')">
            <el-icon><DataBoard /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          <el-menu-item index="content" @click="$router.push('/studio/content')">
            <el-icon><VideoCamera /></el-icon>
            <span>内容管理</span>
          </el-menu-item>
          <el-menu-item index="analytics" @click="$router.push('/studio/analytics')">
            <el-icon><TrendCharts /></el-icon>
            <span>数据分析</span>
          </el-menu-item>
          <el-menu-item index="comments">
            <el-icon><ChatDotRound /></el-icon>
            <span>评论管理</span>
          </el-menu-item>
          <el-menu-item index="settings">
            <el-icon><Setting /></el-icon>
            <span>频道设置</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <!-- 主要内容区域 -->
      <main class="studio-content">
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

const userAvatar = ref('https://example.com/default-avatar.png')

const goToChannel = () => {
  router.push(`/channel/${userStore.userId}`)
}

const goToHome = () => {
  router.push('/')
}

const handleLogout = () => {
  // 实现登出逻辑
}
</script>

<style scoped lang="scss">
.studio-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.studio-header {
  height: 64px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #1a1a1a;
  color: #fff;

  .header-left {
    .logo {
      display: flex;
      align-items: center;
      gap: 12px;
      text-decoration: none;
      color: #fff;

      img {
        height: 32px;
      }

      span {
        font-size: 18px;
        font-weight: 500;
      }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 16px;
  }
}

.studio-container {
  display: flex;
  flex: 1;
}

.studio-sidebar {
  width: 240px;
  height: calc(100vh - 64px);
  background: #fff;
  border-right: 1px solid #e5e5e5;

  .el-menu {
    border-right: none;
  }
}

.studio-content {
  flex: 1;
  padding: 24px;
  background: #f9f9f9;
  min-height: calc(100vh - 64px);
}
</style> 
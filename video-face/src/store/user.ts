import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types/user'
import { login as apiLogin, logout as apiLogout } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const user = ref<User | null>(null)
  const isLoggedIn = ref<boolean>(!!token.value)

  // 登录
  const login = async (username: string, password: string) => {
    try {
      const { token: newToken, user: userData } = await apiLogin(username, password)
      token.value = newToken
      user.value = userData
      isLoggedIn.value = true
      localStorage.setItem('token', newToken)
      return true
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  // 登出
  const logout = async () => {
    try {
      await apiLogout()
      token.value = ''
      user.value = null
      isLoggedIn.value = false
      localStorage.removeItem('token')
      return true
    } catch (error) {
      console.error('Logout failed:', error)
      return false
    }
  }

  // 更新用户信息
  const updateUserInfo = (newUserInfo: Partial<User>) => {
    if (user.value) {
      user.value = { ...user.value, ...newUserInfo }
    }
  }

  // 获取用户ID
  const getUserId = () => user.value?.id

  return {
    token,
    user,
    isLoggedIn,
    login,
    logout,
    updateUserInfo,
    getUserId
  }
}) 
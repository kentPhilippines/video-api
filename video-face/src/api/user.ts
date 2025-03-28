import request from '@/utils/request'
import type {
  User,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  UpdateUserRequest,
  ChangePasswordRequest
} from '@/types/user'

export const login = (data: LoginRequest): Promise<LoginResponse> => {
  return request({
    url: '/api/user/login',
    method: 'post',
    data
  })
}

export const register = (data: RegisterRequest): Promise<User> => {
  return request({
    url: '/api/user/register',
    method: 'post',
    data
  })
}

export const logout = () => {
  return request({
    url: '/api/user/logout',
    method: 'post'
  })
}

export const getUserInfo = (): Promise<User> => {
  return request({
    url: '/api/user/info',
    method: 'get'
  })
}

export const updateUserInfo = (data: UpdateUserRequest): Promise<User> => {
  return request({
    url: '/api/user/info',
    method: 'put',
    data
  })
}

export const changePassword = (data: ChangePasswordRequest): Promise<void> => {
  return request({
    url: '/api/user/password',
    method: 'put',
    data
  })
}

export const resetPassword = (email: string): Promise<void> => {
  return request({
    url: '/api/user/reset-password',
    method: 'post',
    data: { email }
  })
}

export const checkUsername = (username: string): Promise<boolean> => {
  return request({
    url: '/api/user/check-username',
    method: 'get',
    params: { username }
  })
}

export const checkEmail = (email: string): Promise<boolean> => {
  return request({
    url: '/api/user/check-email',
    method: 'get',
    params: { email }
  })
} 
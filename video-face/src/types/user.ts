export interface User {
  id: number
  username: string
  email: string
  phone?: string
  avatar: string
  vipStatus: number
  vipExpireTime?: string
  status: number
  lastLoginTime?: string
  lastLoginIp?: string
  createdAt: string
  updatedAt: string
}

export interface LoginResponse {
  token: string
  user: User
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
  phone?: string
}

export interface UpdateUserRequest {
  avatar?: string
  phone?: string
  email?: string
}

export interface ChangePasswordRequest {
  oldPassword: string
  newPassword: string
} 
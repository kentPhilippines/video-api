# 认证模块API文档

## 1. 账号密码认证

### 1.1 用户注册

- **接口地址**：`POST /api/v1/auth/register`
- **请求参数**：
  ```json
  {
    "username": "string",  // 用户名，4-20个字符
    "password": "string",  // 密码，6-20个字符
    "email": "string",     // 邮箱
    "code": "string",      // 邮箱验证码
    "phone": "string",     // 手机号（可选）
    "avatar": "string"     // 头像URL（可选）
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "userId": "long",
      "username": "string",
      "email": "string",
      "phone": "string",
      "avatar": "string"
    }
  }
  ```

### 1.2 账号密码登录

- **接口地址**：`POST /api/v1/auth/login`
- **请求参数**：
  ```json
  {
    "account": "string",    // 账号（用户名/邮箱/手机号）
    "password": "string",   // 密码
    "clientType": "int",    // 客户端类型：1-Web 2-iOS 3-Android
    "clientId": "string"    // 客户端设备ID
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "accessToken": "string",
      "refreshToken": "string",
      "expiresIn": "long",
      "user": {
        "id": "long",
        "username": "string",
        "email": "string",
        "phone": "string",
        "avatar": "string",
        "status": "int",
        "vipStatus": "int",
        "vipExpireTime": "datetime"
      }
    }
  }
  ```

### 1.3 退出登录

- **接口地址**：`POST /api/v1/auth/logout`
- **请求头**：
  ```
  Authorization: Bearer {accessToken}
  ```
- **请求参数**：无
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success"
  }
  ```

## 2. 验证码认证

### 2.1 发送邮箱验证码

- **接口地址**：`POST /api/v1/auth/email/code`
- **请求参数**：
  ```json
  {
    "email": "string",   // 邮箱地址
    "type": "int"        // 验证码类型：1-注册 2-登录 3-重置密码
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success"
  }
  ```

### 2.2 发送手机验证码

- **接口地址**：`POST /api/v1/auth/sms/code`
- **请求参数**：
  ```json
  {
    "phone": "string",   // 手机号
    "type": "int"        // 验证码类型：1-注册 2-登录 3-重置密码
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success"
  }
  ```

### 2.3 验证码登录

- **接口地址**：`POST /api/v1/auth/code/login`
- **请求参数**：
  ```json
  {
    "account": "string",    // 账号（邮箱/手机号）
    "code": "string",       // 验证码
    "clientType": "int",    // 客户端类型：1-Web 2-iOS 3-Android
    "clientId": "string"    // 客户端设备ID
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "accessToken": "string",
      "refreshToken": "string",
      "expiresIn": "long",
      "user": {
        "id": "long",
        "username": "string",
        "email": "string",
        "phone": "string",
        "avatar": "string",
        "status": "int",
        "vipStatus": "int",
        "vipExpireTime": "datetime"
      }
    }
  }
  ```

## 3. OAuth认证

### 3.1 获取OAuth授权URL

- **接口地址**：`GET /api/v1/auth/oauth/url`
- **请求参数**：
  ```
  type: int           // OAuth类型：1-GitHub 2-Google 3-微信
  redirectUri: string // 回调地址
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "authorizeUrl": "string"
    }
  }
  ```

### 3.2 OAuth登录回调

- **接口地址**：`POST /api/v1/auth/oauth/callback`
- **请求参数**：
  ```json
  {
    "type": "int",         // OAuth类型：1-GitHub 2-Google 3-微信
    "code": "string",      // 授权码
    "clientType": "int",   // 客户端类型：1-Web 2-iOS 3-Android
    "clientId": "string"   // 客户端设备ID
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "accessToken": "string",
      "refreshToken": "string",
      "expiresIn": "long",
      "user": {
        "id": "long",
        "username": "string",
        "email": "string",
        "phone": "string",
        "avatar": "string",
        "status": "int",
        "vipStatus": "int",
        "vipExpireTime": "datetime"
      }
    }
  }
  ```

## 4. Token管理

### 4.1 刷新Token

- **接口地址**：`POST /api/v1/auth/token/refresh`
- **请求参数**：
  ```json
  {
    "refreshToken": "string"
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "accessToken": "string",
      "refreshToken": "string",
      "expiresIn": "long"
    }
  }
  ```

### 4.2 检查Token有效性

- **接口地址**：`GET /api/v1/auth/token/check`
- **请求头**：
  ```
  Authorization: Bearer {accessToken}
  ```
- **请求参数**：无
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "valid": "boolean",
      "expiresIn": "long"
    }
  }
  ``` 
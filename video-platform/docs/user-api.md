# 用户模块API文档

## 基础说明

- 基础路径: `/api`
- 请求头: 需要认证的接口必须在请求头中携带 `Authorization: Bearer {token}`
- 响应格式:
```json
{
    "code": 0,          // 状态码：0-成功，其他-失败
    "message": "成功",   // 响应消息
    "data": {}          // 响应数据
}
```

## 1. 用户管理接口

### 1.1 创建用户

- **接口地址**：`POST /api/v1/users`
- **请求参数**：
  ```json
  {
    "username": "string", // 用户名，4-20个字符
    "password": "string", // 密码，6-20个字符
    "email": "string",    // 邮箱（可选）
    "phone": "string",    // 手机号（可选）
    "avatar": "string"    // 头像URL（可选）
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "userId": "long"    // 用户ID
    }
  }
  ```

### 1.2 获取用户信息

- **接口地址**：`GET /api/v1/users/{userId}`
- **请求参数**：无
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "id": "long",                  // 用户ID
      "username": "string",          // 用户名
      "email": "string",            // 邮箱
      "phone": "string",            // 手机号（脱敏）
      "avatar": "string",           // 头像URL
      "status": "int",              // 用户状态：0-禁用 1-正常
      "vipStatus": "int",           // VIP状态：0-普通用户 1-VIP用户
      "vipExpireTime": "datetime",  // VIP到期时间
      "lastLoginTime": "datetime",  // 最后登录时间
      "lastLoginIp": "string",      // 最后登录IP
      "createdAt": "datetime",      // 创建时间
      "updatedAt": "datetime"       // 更新时间
    }
  }
  ```

### 1.3 更新用户信息

- **接口地址**：`PUT /api/v1/users/{userId}`
- **请求参数**：
  ```json
  {
    "email": "string",    // 邮箱（可选）
    "phone": "string",    // 手机号（可选）
    "avatar": "string",   // 头像URL（可选）
    "password": "string"  // 新密码（可选）
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success"
  }
  ```

## 2. 用户关注接口

### 2.1 关注用户

- **接口地址**：`POST /api/v1/users/{userId}/follow`
- **请求参数**：无
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success"
  }
  ```

### 2.2 取消关注

- **接口地址**：`DELETE /api/v1/users/{userId}/follow`
- **请求参数**：无
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success"
  }
  ```

### 2.3 获取关注列表

- **接口地址**：`GET /api/v1/users/{userId}/following`
- **请求参数**：
  ```
  page: int       // 页码，从1开始
  size: int       // 每页数量
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": "long",
      "list": [
        {
          "userId": "long",
          "username": "string",
          "avatar": "string",
          "followTime": "datetime"
        }
      ]
    }
  }
  ```

### 2.4 获取粉丝列表

- **接口地址**：`GET /api/v1/users/{userId}/followers`
- **请求参数**：
  ```
  page: int       // 页码，从1开始
  size: int       // 每页数量
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": "long",
      "list": [
        {
          "userId": "long",
          "username": "string",
          "avatar": "string",
          "followTime": "datetime"
        }
      ]
    }
  }
  ```

## 3. 用户收藏接口

### 3.1 创建收藏夹

- **接口地址**：`POST /api/v1/users/favorites/folders`
- **请求参数**：
  ```json
  {
    "name": "string",         // 收藏夹名称
    "description": "string",  // 收藏夹描述
    "isPublic": "int"        // 是否公开：0-私密 1-公开
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "folderId": "long"
    }
  }
  ```

### 3.2 收藏视频

- **接口地址**：`POST /api/v1/users/favorites`
- **请求参数**：
  ```json
  {
    "videoId": "long",    // 视频ID
    "folderId": "long"    // 收藏夹ID
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success"
  }
  ```

### 3.3 获取收藏夹列表

- **接口地址**：`GET /api/v1/users/{userId}/favorites/folders`
- **请求参数**：无
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "list": [
        {
          "id": "long",
          "name": "string",
          "description": "string",
          "isPublic": "int",
          "count": "int",
          "createdAt": "datetime",
          "updatedAt": "datetime"
        }
      ]
    }
  }
  ```

## 4. 用户历史记录接口

### 4.1 添加观看历史

- **接口地址**：`POST /api/v1/users/history`
- **请求参数**：
  ```json
  {
    "videoId": "long",     // 视频ID
    "progress": "int",     // 观看进度（秒）
    "duration": "int",     // 视频总时长（秒）
    "finished": "int"      // 是否看完：0-未完成 1-已完成
  }
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success"
  }
  ```

### 4.2 获取观看历史

- **接口地址**：`GET /api/v1/users/history`
- **请求参数**：
  ```
  page: int       // 页码，从1开始
  size: int       // 每页数量
  ```
- **响应结果**：
  ```json
  {
    "code": 0,
    "message": "success",
    "data": {
      "total": "long",
      "list": [
        {
          "videoId": "long",
          "title": "string",
          "cover": "string",
          "progress": "int",
          "duration": "int",
          "finished": "int",
          "watchedAt": "datetime"
        }
      ]
    }
  }
  ```

## 5. 通知相关接口

### 5.1 获取通知列表

- 请求路径：`/notification/list`
- 请求方法：GET
- 请求参数：
  - `type`: int，通知类型，可选
    - `1`: 关注通知
    - `2`: 点赞通知
    - `3`: 评论通知
    - `4`: 回复通知
    - `5`: 系统通知
  - `pageNum`: int，页码，默认1
  - `pageSize`: int，每页数量，默认20
- 响应数据：
```json
{
    "code": 0,
    "message": "success",
    "data": {
        "records": [
            {
                "id": 1,
                "type": 1,
                "content": "xxx关注了你",
                "fromUser": {
                    "id": 1,
                    "username": "用户名",
                    "avatar": "头像URL"
                },
                "resourceId": 1,
                "isRead": 0,
                "createdAt": "2024-03-20T10:00:00"
            }
        ],
        "total": 100,
        "size": 20,
        "current": 1,
        "pages": 5
    }
}
```

### 5.2 标记通知为已读

- 请求路径：`/notification/read`
- 请求方法：POST
- 请求参数：
```json
{
    "ids": [1, 2, 3]       // 通知ID列表，必填
}
```
- 响应数据：
```json
{
    "code": 0,
    "message": "标记成功",
    "data": null
}
```

### 5.3 获取未读通知数量

- 请求路径：`/notification/unread/count`
- 请求方法：GET
- 响应数据：
```json
{
    "code": 0,
    "message": "success",
    "data": {
        "total": 100,
        "follow": 10,
        "like": 20,
        "comment": 30,
        "reply": 25,
        "system": 15
    }
}
``` 
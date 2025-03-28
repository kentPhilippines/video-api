package com.video.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.auth.entity.UserLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

    /**
     * 获取用户最近一次登录记录
     */
    @Select("SELECT * FROM user_login_log WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT 1")
    UserLoginLog getLastLoginLog(Long userId);

    /**
     * 获取用户登录失败次数（指定时间内）
     */
    @Select("SELECT COUNT(*) FROM user_login_log WHERE user_id = #{userId} AND login_status = 0 AND created_at > #{startTime}")
    Integer getLoginFailCount(Long userId, String startTime);
} 
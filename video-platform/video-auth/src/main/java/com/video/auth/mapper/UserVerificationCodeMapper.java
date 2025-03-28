package com.video.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.auth.entity.UserVerificationCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserVerificationCodeMapper extends BaseMapper<UserVerificationCode> {

    /**
     * 获取最新的未验证验证码
     */
    @Select("SELECT * FROM user_verification_code WHERE (email = #{account} OR phone = #{account}) " +
            "AND type = #{type} AND verified = 0 AND expired_at > NOW() " +
            "ORDER BY created_at DESC LIMIT 1")
    UserVerificationCode getLatestValidCode(String account, Integer type);

    /**
     * 标记验证码为已验证
     */
    @Update("UPDATE user_verification_code SET verified = 1 WHERE id = #{id}")
    void markAsVerified(Long id);

    /**
     * 获取指定时间内发送的验证码数量
     */
    @Select("SELECT COUNT(*) FROM user_verification_code WHERE (email = #{account} OR phone = #{account}) " +
            "AND created_at > #{startTime}")
    Integer getCodeCount(String account, String startTime);
} 
package com.video.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.auth.entity.UserToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserTokenMapper extends BaseMapper<UserToken> {

    /**
     * 根据访问令牌获取token信息
     */
    @Select("SELECT * FROM user_token WHERE access_token = #{accessToken} AND deleted = 0")
    UserToken getByAccessToken(String accessToken);

    /**
     * 根据刷新令牌获取token信息
     */
    @Select("SELECT * FROM user_token WHERE refresh_token = #{refreshToken} AND deleted = 0")
    UserToken getByRefreshToken(String refreshToken);

    /**
     * 获取用户在指定客户端的token
     */
    @Select("SELECT * FROM user_token WHERE user_id = #{userId} AND client_type = #{clientType} " +
            "AND client_id = #{clientId} AND deleted = 0")
    UserToken getByUserAndClient(Long userId, Integer clientType, String clientId);

    /**
     * 使token失效
     */
    @Update("UPDATE user_token SET deleted = 1 WHERE id = #{id}")
    void invalidateToken(Long id);

    /**
     * 清除用户所有token
     */
    @Update("UPDATE user_token SET deleted = 1 WHERE user_id = #{userId}")
    void clearUserTokens(Long userId);
} 
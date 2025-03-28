package com.video.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.auth.entity.UserOAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserOAuthMapper extends BaseMapper<UserOAuth> {

    /**
     * 根据OAuth类型和平台用户ID查询绑定信息
     */
    @Select("SELECT * FROM user_oauth WHERE oauth_type = #{oauthType} AND oauth_id = #{oauthId} AND deleted = 0")
    UserOAuth getByTypeAndOAuthId(Integer oauthType, String oauthId);

    /**
     * 获取用户绑定的指定类型的OAuth账号
     */
    @Select("SELECT * FROM user_oauth WHERE user_id = #{userId} AND oauth_type = #{oauthType} AND deleted = 0")
    UserOAuth getByUserIdAndType(Long userId, Integer oauthType);

    /**
     * 检查用户是否已绑定指定类型的OAuth账号
     */
    @Select("SELECT COUNT(*) FROM user_oauth WHERE user_id = #{userId} AND oauth_type = #{oauthType} AND deleted = 0")
    Integer checkOAuthBound(Long userId, Integer oauthType);
} 
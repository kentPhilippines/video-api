package com.video.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.user.dto.FollowUserDTO;
import com.video.user.entity.User;
import com.video.user.entity.UserFollow;
import com.video.user.mapper.UserFollowMapper;
import com.video.user.mapper.UserMapper;
import com.video.user.service.UserFollowService;
import com.video.user.vo.FollowUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void followUser(Long userId, FollowUserDTO followUserDTO) {
        // 检查目标用户是否存在
        User targetUser = userMapper.selectById(followUserDTO.getFollowedId());
        if (targetUser == null) {
            throw new RuntimeException("目标用户不存在");
        }

        // 不能关注自己
        if (userId.equals(followUserDTO.getFollowedId())) {
            throw new RuntimeException("不能关注自己");
        }

        // 检查是否已经关注
        LambdaQueryWrapper<UserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollow::getFollowerId, userId)
                .eq(UserFollow::getFollowedId, followUserDTO.getFollowedId())
                .eq(UserFollow::getDeleted, 0);
        UserFollow existingFollow = this.getOne(queryWrapper);

        if (existingFollow != null) {
            if (existingFollow.getStatus() == 1) {
                throw new RuntimeException("已经关注该用户");
            }
            // 如果之前取消关注过，则重新关注
            existingFollow.setStatus(1);
            existingFollow.setUpdatedAt(LocalDateTime.now());
            this.updateById(existingFollow);
        } else {
            // 创建新的关注记录
            UserFollow userFollow = new UserFollow();
            userFollow.setFollowerId(userId);
            userFollow.setFollowedId(followUserDTO.getFollowedId());
            userFollow.setStatus(1);
            userFollow.setCreatedAt(LocalDateTime.now());
            userFollow.setUpdatedAt(LocalDateTime.now());
            this.save(userFollow);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfollowUser(Long userId, Long targetUserId) {
        LambdaQueryWrapper<UserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollow::getFollowerId, userId)
                .eq(UserFollow::getFollowedId, targetUserId)
                .eq(UserFollow::getStatus, 1)
                .eq(UserFollow::getDeleted, 0);

        UserFollow userFollow = this.getOne(queryWrapper);
        if (userFollow == null) {
            throw new RuntimeException("未关注该用户");
        }

        userFollow.setStatus(0);
        userFollow.setUpdatedAt(LocalDateTime.now());
        this.updateById(userFollow);
    }

    @Override
    public Page<FollowUserVO> getFollowingList(Long userId, Integer pageNum, Integer pageSize) {
        // 分页查询关注列表
        Page<UserFollow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollow::getFollowerId, userId)
                .eq(UserFollow::getStatus, 1)
                .eq(UserFollow::getDeleted, 0)
                .orderByDesc(UserFollow::getCreatedAt);

        Page<UserFollow> followPage = this.page(page, queryWrapper);
        
        // 转换为VO
        return convertToFollowUserVOPage(followPage, userId, true);
    }

    @Override
    public Page<FollowUserVO> getFollowerList(Long userId, Integer pageNum, Integer pageSize) {
        // 分页查询粉丝列表
        Page<UserFollow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollow::getFollowedId, userId)
                .eq(UserFollow::getStatus, 1)
                .eq(UserFollow::getDeleted, 0)
                .orderByDesc(UserFollow::getCreatedAt);

        Page<UserFollow> followerPage = this.page(page, queryWrapper);
        
        // 转换为VO
        return convertToFollowUserVOPage(followerPage, userId, false);
    }

    @Override
    public int getFollowingCount(Long userId) {
        return baseMapper.getFollowingCount(userId);
    }

    @Override
    public int getFollowerCount(Long userId) {
        return baseMapper.getFollowerCount(userId);
    }

    @Override
    public boolean isFollowing(Long userId, Long targetUserId) {
        LambdaQueryWrapper<UserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollow::getFollowerId, userId)
                .eq(UserFollow::getFollowedId, targetUserId)
                .eq(UserFollow::getStatus, 1)
                .eq(UserFollow::getDeleted, 0);
        return this.count(queryWrapper) > 0;
    }

    private Page<FollowUserVO> convertToFollowUserVOPage(Page<UserFollow> followPage, Long userId, boolean isFollowing) {
        Page<FollowUserVO> voPage = new Page<>(followPage.getCurrent(), followPage.getSize(), followPage.getTotal());
        
        if (followPage.getRecords().isEmpty()) {
            voPage.setRecords(new ArrayList<>());
            return voPage;
        }

        // 获取关注/粉丝用户ID列表
        List<Long> userIds = followPage.getRecords().stream()
                .map(follow -> isFollowing ? follow.getFollowedId() : follow.getFollowerId())
                .collect(Collectors.toList());

        // 批量查询用户信息
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 转换为VO
        List<FollowUserVO> voList = followPage.getRecords().stream().map(follow -> {
            FollowUserVO vo = new FollowUserVO();
            Long targetUserId = isFollowing ? follow.getFollowedId() : follow.getFollowerId();
            User user = userMap.get(targetUserId);
            
            if (user != null) {
                vo.setUserId(user.getId());
                vo.setUsername(user.getUsername());
                vo.setAvatar(user.getAvatar());
                vo.setFollowTime(follow.getCreatedAt());
                vo.setIsFollowEachOther(baseMapper.checkFollowEachOther(userId, targetUserId));
            }
            
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }
} 
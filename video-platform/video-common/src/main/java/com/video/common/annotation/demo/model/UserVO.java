package com.video.common.annotation.demo.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserVO implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private boolean vip;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 
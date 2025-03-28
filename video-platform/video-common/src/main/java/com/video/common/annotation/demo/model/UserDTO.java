package com.video.common.annotation.demo.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private Boolean vip;
} 
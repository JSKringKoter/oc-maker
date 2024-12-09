package com.ocmaker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserInfo {
    private int userUid;
    private String username;
    private String password;
    private LocalDateTime createTime;
}

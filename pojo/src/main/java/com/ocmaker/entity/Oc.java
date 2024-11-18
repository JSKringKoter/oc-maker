package com.ocmaker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oc {
    private Integer ocId;          // oc唯一标识符
    private String name;           // 名称
    private String gender;         // 性别
    private Integer age;           // 年龄
    private Integer height;        // 身高
    private Integer weight;        // 体重
    private String skinColor;      // 肤色
    private String hair;           // 发型
    private String hairColor;     // 发色
    private String eyesColor;      // 瞳色
    private String body;           // 身体特征
    private String face;           // 面部特征
    private String personality;    // 性格特点
    private String skill;          // 特长
    private String hobby;          // 爱好
    private LocalDateTime createTime;   //创建时间
    private LocalDateTime updateTime;   //更新时间
    private Integer userUid;       // 隶属于用户的id
}

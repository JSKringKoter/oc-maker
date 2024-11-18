package com.ocmaker.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OcDetailVO {
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
}

package com.ocmaker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    /**
     * 获取所有AI有关的变量值
     * @param oc
     * @return
     * @throws IllegalAccessException
     */
    public static String getAiGenerateInfo(Oc oc) throws IllegalAccessException {
        Field[] fields = oc.getClass().getDeclaredFields();
        StringBuilder info = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);
            //检查name是否在排除的字段内
            String name = field.getName();
            if (!name.equals("ocId") && !name.equals("name") && !name.equals("skill") && !name.equals("hobby") && !name.equals("createTime")
                    && !name.equals("updateTime") && !name.equals("userUid") && !name.equals("personality")) {
                Object value = field.get(oc);
                //处理一些额外的逻辑
//                if (name.equals("gender")) {
//                    if (value != "男" && value != "女")
//                        continue;
//                }
//                if (name.equals("age")) {
//                    info.append(value);
//                    info.append("岁");
//                    info.append(", ");
//                    continue;
//                }
//                if (name.equals("height")) {
//                    info.append(value);
//                    info.append("cm");
//                    info.append(", ");
//                    continue;
//                }
//                if (name.equals("weight")) {
//                    info.append(value);
//                    info.append("kg");
//                    info.append(", ");
//                    continue;
//                }
                info.append(name);
                info.append(":");
                info.append(value);
                info.append(", ");
            }
        }
        return info.toString();
    }

}

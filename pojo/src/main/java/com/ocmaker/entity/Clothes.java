package com.ocmaker.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clothes {
    private Integer clothesId;
    private String name;
    private String describe;
    private String imgUrl;
    private String abbImgUrl;
    private String hat;
    private String faceDecorate;
    private String uppers;
    private String belt;
    private String bottoms;
    private String legDecorate;
    private String shoes;
    private String otherDecorate;
    private boolean isCollect;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer clothesOcId;

    /**
     * 获取所有AI有关的变量值
     * @param clothes
     * @return
     * @throws IllegalAccessException
     */
    public static String getAiGenerateInfo(Clothes clothes) throws IllegalAccessException {
        Field[] fields = clothes.getClass().getDeclaredFields();
        StringBuilder info = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);
            //检查name是否在排除的字段内
            String name = field.getName();
            if (!name.equals("clothesId") && !name.equals("name") && !name.equals("describe") && !name.equals("imgUrl") && !name.equals("createTime")
                    && !name.equals("updateTime") && !name.equals("clothesOcId")) {
                Object value = field.get(clothes);
                info.append(name);
                info.append(":");
                info.append(value);
                info.append(", ");
            }
        }
        return info.toString();
    }
}

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
public class Clothes {
    private Integer clothesId;
    private String name;
    private String describe;
    private String imgUrl;
    private String hat;
    private String faceDecorate;
    private String uppers;
    private String belt;
    private String bottoms;
    private String legDecorate;
    private String shoes;
    private String otherDecorate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer clothesOcId;
}

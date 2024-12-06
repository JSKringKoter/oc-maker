package com.ocmaker.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClothesDetailVO {
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
    private boolean isCollect;
    private Integer clothesOcId;
}

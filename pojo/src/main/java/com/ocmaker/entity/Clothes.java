package com.ocmaker.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clothes {
    private Integer clothesId;
    private String imgUrl;
    private String hat;
    private String faceDecorate;
    private String bottoms;
    private String legDecorate;
    private String shoes;
    private String otherDecorate;
    private Integer clothesOcId;
}

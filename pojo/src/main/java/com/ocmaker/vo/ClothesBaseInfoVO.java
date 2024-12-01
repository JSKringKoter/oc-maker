package com.ocmaker.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClothesBaseInfoVO {
    Integer clothesId;
    Integer clothesOcId;
    String imgUrl;
    String abbImgUrl;
    String name;
    String describe;
}

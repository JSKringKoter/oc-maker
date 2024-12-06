package com.ocmaker.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectClothesVO {
    private Integer clothesOcId;
    private Integer clothesId;
    private boolean isCollect;
}

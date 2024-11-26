package com.ocmaker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageGenerateInfo {
    Integer clothesOcId;
    Integer clothesId;
    String apiKey;
}

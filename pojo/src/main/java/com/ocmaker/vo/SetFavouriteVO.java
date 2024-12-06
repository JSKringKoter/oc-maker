package com.ocmaker.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetFavouriteVO {
    Integer ocId;
    Integer clothesId;
}

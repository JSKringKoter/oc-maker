package com.ocmaker.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OcBaseInfoVO {
    private Integer ocId;
    private String name;
    private String gender;
    private int age;
}

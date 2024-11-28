package com.ocmaker.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiGenerateConfig {
    private Integer Style;          //画风
    private Integer expression;     //表情
    private Integer proximity;      //人物所占比例
    private Integer background;     //背景

    public static final String STYLE1 = "artist:hiten_(hitenkei), {{artist:chen_bin}}, [[artist:jyt]], [[artist:ciloranko]], [[[artist:kedama milk]]], [artist_pan_(mimi)], artist:hoshi (snacherubi), ";
    public static final String STYLE2 = "[artist:kedama milk],[artist:ask (askzy)],artist:wanke, artist:wlop,[artist:ATDAN],artist:ciloranko,[[artist:rhasta]],[artist:tidsean],[artist:ke-ta],{{chiaroscuro}},[artist:as109]],[artist:sho (sho lwlw)], ";
    public static final String STYLE3 = "{ningen mame,{ciloranko},ke-ta,kedama milk,wlop,rhasta},tianliang duohe fangdongye,{chen_bin,haneru},{{shiny_skin}}, ";

}

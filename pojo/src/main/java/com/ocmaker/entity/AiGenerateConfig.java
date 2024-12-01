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
    private Integer style;          //画风
    private Integer expression;     //表情
    private Integer proximity;      //人物所占比例
    private Integer background;     //背景

    public static final String STYLE1 = ", ";
    public static final String STYLE2 = "artist:hiten_(hitenkei), {{artist:chen_bin}}, [[artist:jyt]], [[artist:ciloranko]], [[[artist:kedama milk]]], [artist_pan_(mimi)], artist:hoshi (snacherubi), ";
    public static final String STYLE3 = "[artist:kedama milk],[artist:ask (askzy)],artist:wanke, artist:wlop,[artist:ATDAN],artist:ciloranko,[[artist:rhasta]],[artist:tidsean],[artist:ke-ta],{{chiaroscuro}},[artist:as109]],[artist:sho (sho lwlw)], ";
    public static final String STYLE4 = "{ningen mame,{ciloranko},ke-ta,kedama milk,wlop,rhasta},tianliang duohe fangdongye,{chen_bin,haneru},{{shiny_skin}}, ";

    private static final String EXPRESSION1 = ", light smile, ";
    private static final String EXPRESSION2 = ", laugh, open mouth, ";
    private static final String EXPRESSION3 = ", crying, tears, ";
    private static final String EXPRESSION4 = ", expressionless, ";
    private static final String EXPRESSION5 = ", angry, ";

    private static final String PROXIMITY1 = ", {{full body}}, ";
    private static final String PROXIMITY2 = ", {{upper body}}, ";
    private static final String PROXIMITY3 = ", {{cowboy shot}}, ";
    private static final String PROXIMITY4 = ", {{lower body}}, ";

    public static final String BACKGROUND1 = ", white background, simple background, solo-focus, ";
    public static final String BACKGROUND2 = ", pink background, simple background, solo-focus, ";
    public static final String BACKGROUND3 = ", grey background, simple background, solo-focus, ";
    public static final String BACKGROUND4 = ", outdoors, street, blue sky, tree, building, solo-focus, ";
    public static final String BACKGROUND5 = ", indoors, table, sofa, flower vase, window, blue sky, solo-focus, ";

    /**
     * 获得风格串
     * @return String
     */
    public String getStyle() {
        return switch (style) {
            case 1 -> STYLE1;
            case 2 -> STYLE2;
            case 3 -> STYLE3;
            default -> STYLE4;
        };
    }

    /**
     * 获得背景串
     * @return String
     */
    public String getBackground() {
        return switch(background) {
            case 2 -> BACKGROUND2;
            case 3 -> BACKGROUND3;
            case 4 -> BACKGROUND4;
            case 5 -> BACKGROUND5;
            default -> BACKGROUND1;
        };
    }

    /**
     * 获得表情串
     * @return String
     */
    public String getExpression() {
        return switch(expression) {
            case 2 -> EXPRESSION2;
            case 3 -> EXPRESSION3;
            case 4 -> EXPRESSION4;
            case 5 -> EXPRESSION5;
            default -> EXPRESSION1;
        };
    }

    /**
     * 获得画幅串
     * @return String
     */
    public String getProximity() {
        return switch(proximity) {
            case 1 -> PROXIMITY1;
            case 2 -> PROXIMITY2;
            case 4 -> PROXIMITY4;
            default -> PROXIMITY3;
        };
    }

}

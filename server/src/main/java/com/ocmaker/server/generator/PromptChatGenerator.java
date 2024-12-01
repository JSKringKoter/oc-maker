package com.ocmaker.server.generator;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ocmaker.entity.Clothes;
import com.ocmaker.entity.ImageGenerateInfo;
import com.ocmaker.entity.Oc;
import com.ocmaker.server.exception.GenerateFailException;
import com.ocmaker.server.mapper.ClothesMapper;
import com.ocmaker.server.mapper.OcMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PromptChatGenerator {
    @Autowired
    private OcMapper ocMapper;
    @Autowired
    private ClothesMapper clothesMapper;
    private final String apiKey = "sk-908f93bf655241398bb813677e77745d";

    public String pushToChat(String userPrompt) {

        String systemPrompt = "下面将会给出非常高级的图片描述，丰富这段文本的表现力并转化为保留较多关键信息的关键词组，按照画面质量描述与风格词汇、主体描述、细节描述的顺序以逗号分隔。文本以特征:特征值组成，请将它们转化成tag，每个tag应该在一到两个词汇左右，并精准地描绘一项特征。将年龄转化为描述年龄的tag，将体重、身高等数值转化为描述身材的tag，总体输出小于100个词汇，且不要太长，英文小写输出，合并为一行。tag中不能有-或_。如果出现不是人类的种族，请描绘其特征（例如猫娘会有cat ear, cat tail):";
        String requestBody = String.format("""
                {
                     "model": "deepseek-chat",
                     "messages": [
                         {"role": "system", "content": "%s"},
                         {"role": "user", "content": "%s"}
                     ],
                          "stream": false
                }
                """, systemPrompt, userPrompt);
        //向deepseek发送请求
        HttpResponse response = HttpRequest.post("https://api.deepseek.com/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .execute();
        //获得请求的json数据
        String resultString = response.body();
        JSONObject resultJson = JSONUtil.parseObj(resultString);
        //通过json解析获得最终的result数据
        String result = resultJson.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getStr("content");
        //获得http状态码
        int status = response.getStatus();
        if (status == 200) {
            log.info("chat返回结果为：{}", result);
            return result;
        } else {
            throw new GenerateFailException("Chat服务器出错，请联系管理员");
        }
    }

    public String getPromptById(ImageGenerateInfo info) throws IllegalAccessException {

        Oc oc = ocMapper.selectOcDetail(info.getClothesOcId());
        Clothes clothes = clothesMapper.selectClothesByClothesId(info.getClothesId());
        String temp = Oc.getAiGenerateInfo(oc) + Clothes.getAiGenerateInfo(clothes);
        String prompt = temp.replaceAll("\n", "");

        log.info("原始prompt为：{}", prompt);
        return pushToChat(prompt);
    }

//    @Autowired
//    private OcMapper ocMapper;
//    @Autowired
//    private ClothesMapper clothesMapper;
//
//    private final WebClient webClient;
//    private String apiKey = "sk-908f93bf655241398bb813677e77745d";
//
//    public PromptChatGenerator() {
//        webClient = WebClient.builder()
//                .baseUrl("https://api.deepseek.com")
//                .defaultHeader("Authorization", "Bearer " + apiKey)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//
//    }
//
//    /**
//     * 获取提示词
//     * @param info
//     * @return
//     */
//    public Mono<String> getPromptById(ImageGenerateInfo info) {
//
//        Oc oc = ocMapper.selectOcDetail(info.getClothesOcId());
//        Clothes clothes = clothesMapper.selectClothesByClothesId(info.getClothesId());
//        String prompt = "oc.toString() + clothes.toString()";
//
//        return pushToChat(prompt);
//    }
//
//    public Mono<String> pushToChat(String userPrompt) {
//        String systemPrompt = "请根据以下的内容生成一段AI生成用的tag：";
//        String requestBody = String.format("""
//                {
//                    "model": "deepseek-chat",
//                    "messages": [
//                      {"role": "system", "content": "%s"},
//                      {"role": "user", "content": "%s"}
//                    ],
//                    "stream": false
//                }
//                """, systemPrompt, userPrompt);
//
//        RuntimeException e = new RuntimeException("服务器出错");
//        return webClient.post()
//                .bodyValue(requestBody)
//                .exchangeToMono(response -> {
//                    HttpStatusCode httpCode = response.statusCode();
//                    int code = httpCode.value();
//                    if (code == 400) {
//                        log.error("请求格式错误");
//                        return Mono.error(e);
//                    }
//                    if (code == 401) {
//                        log.error("API Key错误");
//                        return Mono.error(e);
//                    }
//                    if (code == 500) {
//                        log.error("Deepseek服务器出错");
//                        return Mono.error(e);
//                    }
//
//                    return response.bodyToMono(String.class);
//                });
//
//    }
}

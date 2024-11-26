package com.ocmaker.server.service;

import com.ocmaker.entity.Clothes;
import com.ocmaker.entity.ImageGenerateInfo;
import com.ocmaker.entity.Oc;
import com.ocmaker.server.mapper.ClothesMapper;
import com.ocmaker.server.mapper.OcMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Service
@Slf4j
public class ImageGenerateService {

    private final WebClient webClient;

    @Autowired
    private OcMapper ocMapper;
    @Autowired
    private ClothesMapper clothesMapper;

    public String getPromptById(ImageGenerateInfo info) {

        Oc oc = ocMapper.selectOcDetail(info.getClothesOcId());
        Clothes clothes = clothesMapper.selectClothesByClothesId(info.getClothesId());
        String prompt = "oc.toString() + clothes.toString()";

        return prompt;
    }

    public ImageGenerateService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://image.novelai.net") // NovelAI API 的基础地址
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024)) // 设置为 10MB)
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create()
                                .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                                .address(new InetSocketAddress("127.0.0.1", 7890))) // 替换为你的代理地址和端口
                        )
                )
                .build();
    }

    public Mono<byte[]> generateImage(String apiKey, String prompt) {

        Long seed = ThreadLocalRandom.current().nextLong(100, 100000);
        // 请求体
        String requestBody = String.format("""
                {
                    "input": "%s",
                    "model": "nai-diffusion-3",
                    "action": "generate",
                    "parameters": {
                        "width": 832,
                        "height": 1216,
                        "scale": 5,
                        "sampler": "k_euler_ancestral",
                        "steps": 20,
                        "seed": %s,
                        "n_samples": 1,
                        "ucPreset": 0,
                        "uc": "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry",
                        "cfg_rescale": 0,
                        "negative_prompt": "{{nsfw}}, nude"
                    }
                }
                """, prompt, seed);

        return webClient.post()
                .uri("/ai/generate-image") // NovelAI 的生成 API 路径
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    log.info("响应状态码: {}", status.value());

                    if (status.equals(HttpStatus.UNAUTHORIZED)) {
                        log.error("认证失败: 401 Unauthorized");
                        return Mono.error(new RuntimeException("API认证失败"));
                    }

                    if (status.is4xxClientError() || status.is5xxServerError()) {
                        return response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("请求失败: " + status.value() + ", 错误信息: " + error)));
                    }

                    return response.bodyToMono(byte[].class);
                });
    }

}

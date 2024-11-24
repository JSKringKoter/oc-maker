package com.ocmaker.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Service
@Slf4j
public class ImageGenerateService {

    private final WebClient webClient;

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
                        "steps": 5,
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
                .retrieve()
                .bodyToMono(byte[].class)
                .doOnError(error -> log.error("图片生成失败: ", error));
    }

}

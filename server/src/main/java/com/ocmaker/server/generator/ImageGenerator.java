package com.ocmaker.server.generator;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ocmaker.common.utils.ZipUtils;
import com.ocmaker.server.exception.GenerateFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class ImageGenerator {

    private final String url = "https://image.novelai.net/ai/generate-image";
    private final String checkUrl = "https://api.novelai.net/user/data";

    /**
     * 图像生成
     * @param apiKey
     * @param prompt
     * @return
     * @throws IOException
     */
    public String generateImage(String apiKey, String prompt) throws IOException {

        Long seed = ThreadLocalRandom.current().nextLong(100, 100000);
        // 请求体
        String requestBody = String.format("""
                {
                    "input": "solo, straight-on, %s",
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
                        "negative_prompt": "{{nsfw}}, nude, {{{mini_person}}}, {{{mini_girl}}}, cat, animal, pov hand, "
                    }
                }
                """, prompt, seed);

        //拿到返回的响应体
        HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .setProxy(new Proxy(Proxy.Type.HTTP,  new InetSocketAddress("127.0.0.1", 7890)))
                .execute();
        //拿到状态码和zipBytes
        int status = response.getStatus();
        if (status == 401) {
            log.info("用户提供的apikey无效");
            throw new GenerateFailException("提供的apiKey无效，请检查apiKey是否过期。");
        }
        if (status == 429) {
            log.info("并行错误");
            throw new GenerateFailException("图片生成正在被占用，请稍后再试。");
        }
        byte[] zipBytes = response.bodyBytes();

        if (status == 200) {
            String fileName = UUID.randomUUID().toString() + ".png";
            String savePath = "D:/images/" + fileName;

            File directory = new File("D:/images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            //解压获得的zip文件
            byte[] imageBytes = ZipUtils.unZip(zipBytes);
            Path path = Paths.get(savePath);
            Files.write(path, imageBytes);

            return fileName;
        } else {
            throw new GenerateFailException("NovelAI服务器出错，请联系管理员");
        }
    }


    /**
     * 检查apiKey是否有效
     * @param apiKey
     */
    public void checkApiKey(String apiKey) {
        HttpResponse response = HttpRequest.get(checkUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .setProxy(new Proxy(Proxy.Type.HTTP,  new InetSocketAddress("127.0.0.1", 7890)))
                .execute();
        if (response.getStatus() != 200) {
            throw new GenerateFailException("提供的apiKey无效，请检查apiKey是否过期。");
        }
    }
//    这里是原来使用WebClient构造请求的代码
//    private final WebClient webClient;
//
//    /**
//     * 无参构造器，为webclient设置代理和响应体最大大小
//     */
//    public ImageGenerator() {
//        webClient = WebClient.builder()
//                .baseUrl("https://image.novelai.net")
//                //设置响应体大小
//                .codecs(configurer -> configurer
//                        .defaultCodecs()
//                        .maxInMemorySize(10 * 1024 * 1024)) // 设置为 10MB)
//                //设置代理
//                .clientConnector(
//                        new ReactorClientHttpConnector(
//                                HttpClient.create()
//                                        .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
//                                                .address(new InetSocketAddress("127.0.0.1", 7890))) // 替换为你的代理地址和端口
//                        )
//                )
//                .build();
//    }
//
//    /**
//     * 构建提示词并生成图片
//     * @param apiKey
//     * @param prompt
//     * @return
//     */
//    public Mono<byte[]> generateImage(String apiKey, String prompt) {
//
//        Long seed = ThreadLocalRandom.current().nextLong(100, 100000);
//        // 请求体
//        String requestBody = String.format("""
//                {
//                    "input": "%s",
//                    "model": "nai-diffusion-3",
//                    "action": "generate",
//                    "parameters": {
//                        "width": 832,
//                        "height": 1216,
//                        "scale": 5,
//                        "sampler": "k_euler_ancestral",
//                        "steps": 20,
//                        "seed": %s,
//                        "n_samples": 1,
//                        "ucPreset": 0,
//                        "uc": "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry",
//                        "cfg_rescale": 0,
//                        "negative_prompt": "{{nsfw}}, nude"
//                    }
//                }
//                """, prompt, seed);
//
//        return webClient.post()
//                .uri("/ai/generate-image") // NovelAI 的生成 API 路径
//                .header("Authorization", "Bearer " + apiKey)
//                .header("Content-Type", "application/json")
//                .bodyValue(requestBody)
//                .exchangeToMono(response -> {
//                    HttpStatusCode status = response.statusCode();
//                    log.info("响应状态码: {}", status.value());
//
//                    if (status.equals(HttpStatus.UNAUTHORIZED)) {
//                        log.error("认证失败: 401 Unauthorized");
//                        return Mono.error(new RuntimeException("API认证失败"));
//                    }
//
//                    if (status.is4xxClientError() || status.is5xxServerError()) {
//                        return response.bodyToMono(String.class)
//                                .flatMap(error -> Mono.error(new RuntimeException("请求失败: " + status.value() + ", 错误信息: " + error)));
//                    }
//
//                    return response.bodyToMono(byte[].class);
//                });

}

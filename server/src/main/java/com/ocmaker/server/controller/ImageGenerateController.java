package com.ocmaker.server.controller;

import com.ocmaker.common.result.Result;
import com.ocmaker.common.utils.ZipUtils;
import com.ocmaker.entity.ImageGenerateInfo;
import com.ocmaker.server.service.ImageGenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/generate-image")
@Slf4j
public class ImageGenerateController {

    @Autowired
    private ImageGenerateService generateService;


    @PostMapping("")
    public Mono<ResponseEntity<String>> generateImage(@RequestBody ImageGenerateInfo info) {

        String containPrompt = generateService.getPromptById(info);
        String apiKey = info.getApiKey();

        //String apiKey = "pst-rmpI2pBTrTH6oZEvjsFENJwEvk7nT5A3VTmYv2zKt2euQzLK7ej6RRLtiA1cK9Rp";
        String stylePrompt = "artist:hiten_(hitenkei), {{artist:chen_bin}}, [[artist:jyt]], [[artist:ciloranko]], [[[artist:kedama milk]]], [artist_pan_(mimi)], artist:hoshi (snacherubi), 1girl, ";

        String prompt = stylePrompt + containPrompt;
        String savePath = "D:/images/" + UUID.randomUUID().toString() + ".png";

        return generateService.generateImage(apiKey, prompt)
                .flatMap(zipBytes -> {
                    try {
                        File directory = new File("D:/images");
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }
                        //解压获得的zip文件
                        byte[] imageBytes = ZipUtils.unZip(zipBytes);
                        Files.write(Paths.get(savePath), imageBytes);

                        return Mono.just(ResponseEntity.ok()
                                .body("图片已保存至: " + savePath));
                    } catch (IOException e) {
                        log.error("保存图片失败: ", e);
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("保存图片失败: " + e.getMessage()));
                    }
                });
    }
}

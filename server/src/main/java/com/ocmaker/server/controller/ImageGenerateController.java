package com.ocmaker.server.controller;

import com.ocmaker.common.result.Result;
import com.ocmaker.common.utils.ZipUtils;
import com.ocmaker.entity.ImageGenerateInfo;
import com.ocmaker.server.exception.FileDeleteFailException;
import com.ocmaker.server.service.ClothesService;
import com.ocmaker.server.service.ImageGenerateService;
import com.ocmaker.server.oss.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
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
    @Autowired
    private ClothesService clothesService;


    /**
     * 根据用户提供的信息生成一张图片，并保存至阿里云oss
     * @param info
     * @return
     */
    @PostMapping("")
    public Mono<ResponseEntity<String>> generateImage(@RequestBody ImageGenerateInfo info) {


        String containPrompt = generateService.getPromptById(info);
        String apiKey = info.getApiKey();

        //String apiKey = "pst-rmpI2pBTrTH6oZEvjsFENJwEvk7nT5A3VTmYv2zKt2euQzLK7ej6RRLtiA1cK9Rp";
        String stylePrompt = "artist:hiten_(hitenkei), {{artist:chen_bin}}, [[artist:jyt]], [[artist:ciloranko]], [[[artist:kedama milk]]], [artist_pan_(mimi)], artist:hoshi (snacherubi),,, 1girl, {{{:3}}}, {{{yellow eyes}}}, cat girl, white cat ear, white cat tail, white hair, long hair, ahoge, cowboy shot, blue shirt, short sleeves, pleated skirt, short skirt, plaid skirt, black skirt, outdoors, blue sky, hair tie, from above, light smile, flower field, colorful flowers, grassland, from above, , best quality, amazing quality, very aesthetic, absurdres";
        String prompt = stylePrompt + containPrompt;
        String fileName = UUID.randomUUID().toString() + ".png";
        String savePath = "D:/images/" + fileName;

        return generateService.generateImage(apiKey, prompt)
                .flatMap(zipBytes -> {
                    try {
                        File directory = new File("D:/images");
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }
                        //解压获得的zip文件
                        byte[] imageBytes = ZipUtils.unZip(zipBytes);
                        Path path = Paths.get(savePath);
                        Files.write(path, imageBytes);

                        //将文件上传至阿里云oss，并将下载地址放在数据库中
                        String url = OssUtils.uploadFile(fileName);
                        clothesService.updateUrl(url, info.getClothesId());
                        //删除本地文件
                        Files.delete(path);

                        return Mono.just(ResponseEntity.ok()
                                .body("图片保存成功"));
                    } catch (IOException e) {
                        log.error("服务器无法获取到图片: ", e);
                        return Mono.just(ResponseEntity.status(500)
                                .body("服务器无法获取到图片: " + e.getMessage()));
                    } catch (Exception e) {
                        log.error("图片上传失败：", e);
                        return Mono.just(ResponseEntity.status(500)
                                .body("图片上传失败: " + e.getMessage()));
                    }
                });
    }


    /**
     * 获取clothesId后获取url，然后删除服装的图片
     * @param info
     * @return
     * @throws Exception
     */
    @PostMapping("/delete")
    public Result<?> deleteImage(@RequestBody ImageGenerateInfo info) throws Exception {
        String url = clothesService.selectImgUrlByClothesId(info.getClothesId());
        //删除阿里云oss中的存档
        OssUtils.deleteFile(url);
        //删除数据库中的条目
        clothesService.makeImgUrlNullByClothesId(info.getClothesId());
        return Result.success();
    }
}

package com.ocmaker.server.controller;

import com.aliyuncs.exceptions.ClientException;
import com.ocmaker.common.result.Result;
import com.ocmaker.entity.ImageGenerateInfo;
import com.ocmaker.server.service.ClothesService;
import com.ocmaker.server.generator.ImageGenerator;
import com.ocmaker.server.oss.OssUtils;
import com.ocmaker.server.generator.PromptChatGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;

@RestController
@RequestMapping("/generate-image")
@Slf4j
public class ImageGenerateController {

    @Autowired
    private PromptChatGenerator promptChatGenerator;
    @Autowired
    private ClothesService clothesService;
    @Autowired
    private ImageGenerator generator;

    /**
     * 根据用户提供的信息生成一张图片，并保存至阿里云oss
     * @param info
     * @return
     */
    @PostMapping("")
    public Result<?> generateImage(@RequestBody ImageGenerateInfo info) throws IOException, ClientException, IllegalAccessException {

        //获取图像生成参数
        String apiKey = info.getApiKey();
        //向AI请求prompt
        String containPrompt = promptChatGenerator.getPromptById(info);
        String stylePrompt = "artist:hiten_(hitenkei), {{artist:chen_bin}}, [[artist:jyt]], [[artist:ciloranko]], [[[artist:kedama milk]]], [artist_pan_(mimi)], artist:hoshi (snacherubi), ";
        String figure = "standing, arms at sides, cowboy shot, ";
        String prompt = stylePrompt + containPrompt + figure;
        //图像生成成功，获取生成的文件名
        String fileName = generator.generateImage(apiKey, prompt);
        //将文件上传到oss，并获取返回的url
        String url = OssUtils.uploadFile(fileName);
        //删除本地的文件
        OssUtils.deleteLocalFile(fileName);
        //将url保存到服务器
        clothesService.updateUrl(url, info.getClothesId());
        return Result.success();

//        String fileName = UUID.randomUUID().toString() + ".png";
//        String savePath = "D:/images/" + fileName;

//        return promptChatGenerateService.getPromptById(info).flatMap(cPrompt -> {
//            log.info("返回的prompt为：{}", cPrompt);
//            String prompt = stylePrompt + cPrompt;

//            return generateService.generateImage(apiKey, prompt)
//                    .flatMap(zipBytes -> {
//                        try {
//                            File directory = new File("D:/images");
//                            if (!directory.exists()) {
//                                directory.mkdirs();
//                            }
//                            //解压获得的zip文件
//                            byte[] imageBytes = ZipUtils.unZip(zipBytes);
//                            Path path = Paths.get(savePath);
//                            Files.write(path, imageBytes);
//
//                            //将文件上传至阿里云oss，并将下载地址放在数据库中
//                            String url = OssUtils.uploadFile(fileName);
//                            clothesService.updateUrl(url, info.getClothesId());
//                            //删除本地文件
//                            Files.delete(path);
//
//                            return Mono.just(ResponseEntity.ok()
//                                    .body("图片保存成功"));
//                        } catch (IOException e) {
//                            log.error("服务器无法获取到图片: ", e);
//                            return Mono.just(ResponseEntity.status(500)
//                                    .body("服务器无法获取到图片: " + e.getMessage()));
//                        } catch (Exception e) {
//                            log.error("图片上传失败：", e);
//                            return Mono.just(ResponseEntity.status(500)
//                                    .body("图片上传失败: " + e.getMessage()));
//                        }
//                    });
        //});
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

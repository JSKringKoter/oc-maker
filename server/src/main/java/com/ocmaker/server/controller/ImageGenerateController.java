package com.ocmaker.server.controller;

import com.aliyuncs.exceptions.ClientException;
import com.ocmaker.common.result.Result;
import com.ocmaker.common.utils.ImageUtils;
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
import java.util.Objects;

@RestController
@RequestMapping("/generate-image")
@Slf4j
public class ImageGenerateController {

    @Autowired
    private PromptChatGenerator promptChatGenerator;
    @Autowired
    private ClothesService clothesService;
    @Autowired
    private ImageGenerator imageGenerator;

    /**
     * 根据用户提供的信息生成一张图片，并保存至阿里云oss
     * @param info
     * @return
     */
    @PostMapping("")
    public Result<?> generateImage(@RequestBody ImageGenerateInfo info) throws IOException, ClientException, IllegalAccessException {

        //获取图像生成参数
        String apiKey = info.getApiKey();
        if (Objects.equals(apiKey, "catisland cafe")) {
            apiKey = "pst-rmpI2pBTrTH6oZEvjsFENJwEvk7nT5A3VTmYv2zKt2euQzLK7ej6RRLtiA1cK9Rp";
        }
        //检查apiKey是否有效
        imageGenerator.checkApiKey(apiKey);
        //向AI请求prompt
        String containPrompt = promptChatGenerator.getPromptById(info);
        String stylePrompt = info.getConfig().getStyle();
        String backgroundPrompt = info.getConfig().getBackground();
        String proximityPrompt = info.getConfig().getProximity();
        String expressionPrompt = info.getConfig().getExpression();
        String prompt = stylePrompt + containPrompt + proximityPrompt + expressionPrompt + backgroundPrompt;
        //图像生成成功，获取生成的文件名
        String fileName = imageGenerator.generateImage(apiKey, prompt);
        //将图片压缩成略缩图，并获得略缩图路径
        String abbImageName = ImageUtils.pictureCropping(fileName);
        //将文件和略缩图上传到oss，并获取返回的url
        String url = OssUtils.uploadFile(fileName);
        String abbUrl = OssUtils.uploadFile(abbImageName);
        //删除本地的文件
        OssUtils.deleteLocalFile(abbImageName);
        OssUtils.deleteLocalFile(fileName);
        //将url保存到服务器
        clothesService.updateUrl(url, info.getClothesId());
        clothesService.updateAbbImgUrl(abbUrl, info.getClothesId());
        return Result.success();

//        这里是原来使用WebClient构造请求的代码
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
        String abbUrl = clothesService.selectImgUrlByClothesId(info.getClothesId());
        //删除阿里云oss中的存档
        OssUtils.deleteFile(url);
        OssUtils.deleteFile(abbUrl);
        //删除数据库中的条目
        clothesService.makeImgUrlNullByClothesId(info.getClothesId());
        clothesService.makeAbbImgUrlNullByClothesId(info.getClothesId());
        return Result.success();
    }
}

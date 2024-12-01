package com.ocmaker.common.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.IOException;

public class ImageUtils {
    public static String pictureCropping(String imageName) throws IOException {
        String imagePath = "D:\\images\\" + imageName;
        String abbreviatedImagePath = "D:\\images\\" + "abb_" +  imageName;
        Thumbnails.of(imagePath)
                .sourceRegion(Positions.TOP_CENTER, 832, 832)
                .size(400, 400)
                .keepAspectRatio(false)
                .toFile(abbreviatedImagePath);
        return "abb_" + imageName;
    }
}

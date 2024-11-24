package com.ocmaker.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    public static byte[] unZip(byte[] zipBytes) throws IOException {
        byte[] imageBytes;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(zipBytes);
            ZipInputStream zis = new ZipInputStream(bis)) {
                zis.getNextEntry();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
                }
                imageBytes = bos.toByteArray();
        }
        return imageBytes;
    }
}

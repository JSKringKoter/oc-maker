package com.ocmaker.server.oss;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import com.ocmaker.server.exception.FileDeleteFailException;
import com.ocmaker.server.exception.FileUploadFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class OssUtils {

    private static final String endpoint = "https://oss-cn-nanjing.aliyuncs.com";
    private static final String bucketName = "oc-maker";
    private static final String region = "cn-nanjing";

    /**
     * 将指定文件名的文件上传到oss
     * @param fileName
     * @return
     * @throws ClientException
     * @throws FileUploadFailException
     */
    public static String uploadFile(String fileName) throws ClientException, FileUploadFailException {

        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        String objectName = "image/" + fileName;
        String filePath= "D:\\images\\" + fileName;

        // 创建OSSClient实例。
        try {
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
            OSS ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(region)
                    .build();

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(filePath));
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            String url = "https://oc-maker.oss-cn-nanjing.aliyuncs.com/image/" + fileName;
            ossClient.shutdown();

            return url;
        } catch (FileUploadFailException e) {
            log.info("文件上传出错");
        }
            return null;
    }

    /**
     * 将oss上指定url的文件删除
     * @param url
     * @throws Exception
     * @throws FileDeleteFailException
     */
    public static void deleteFile(String url) throws Exception, FileDeleteFailException {

        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        String fileName = url.replaceAll("https://oc-maker.oss-cn-nanjing.aliyuncs.com/image/", "");
        String objectName = "image/" + fileName;

        // 创建OSSClient实例。
        try {
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
            OSS ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(region)
                    .build();

            ossClient.deleteObject(bucketName, objectName);
        } catch(FileDeleteFailException e) {
            log.info("文件删除出错");
        }
    }

    /**
     * 删除本地指定路径的文件
     * @param fileName
     * @throws IOException
     */
    public static void deleteLocalFile(String fileName) throws IOException {
        String filePath= "D:\\images\\" + fileName;
        Files.deleteIfExists(Path.of(filePath));
    }
}

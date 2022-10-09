package com.evilorlive.aliossupload.plugin;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.evilorlive.aliossupload.config.AliyunOssConfig;
import com.evilorlive.aliossupload.entity.PluginConfig;
import com.evilorlive.aliossupload.vo.FileUploadResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component("aliyunFileUploadPlugin")
public class AliyunFileUploadPlugin extends FileUploadPlugin{

    private OSS ossClient;

    @Autowired
    AliyunOssConfig aliyunOssConfig;

    /**
     * 获取地域节点
     *
     * @return endpoint
     */
    private String getEndpoint() {
        return getAttribute("endpoint");
    }

    /**
     * 获取AccessKeyId
     *
     * @return AccessKeyId
     */
    private String getAccessKeyId() {
        return getAttribute("accessKeyId");
    }

    /**
     * 获取AccessKeySecret
     *
     * @return AccessKeySecret
     */
    private String getAccessKeySecret() {
        return getAttribute("accessKeySecret");
    }

    /**
     * @description: 获取存储桶名称
     * @return bucketName
     */
    private String getBucketName() {
        return getAttribute("bucketName");
    }

    /**
     * @description: 获取图片格式后缀
     * @return image
     */
    private String[] getImageSuffix() {
        return getAttribute("imageSuffix").split(",");
    }

    /**
     * @description: 获取文件格式后缀
     * @return image
     */
    private String[] getFileSuffix() {
        return getAttribute("fileSuffix").split(",");
    }


    /**
     * @description: 简单文件上传
     * @param uploadFile: 上传文件
     * @param type: 文件类型
     * @return FileUploadResult
     */
    @Override
    public FileUploadResult simpleUpload(MultipartFile uploadFile, FileUploadPlugin.Type type) {
        // 检测文件格式
        String originalFilename = uploadFile.getOriginalFilename();
        String filePath = getFilePath(originalFilename, type);
        Boolean isLegal = checkType(originalFilename, type);
        if (!isLegal) {
            return FileUploadResult.failIllegal();
        }

        // 上传文件至OSS
        try {
            InputStream inputStream = uploadFile.getInputStream();
            getOssClient().putObject(getBucketName(), filePath, inputStream);
        } catch (OSSException oe) {
            return FileUploadResult.failOss(oe.getMessage());
        } catch (ClientException ce) {
            return FileUploadResult.failClient(ce.getMessage());
        }  catch (IOException ie) {
            return FileUploadResult.failRead(ie.getMessage());
        } finally {
            if (getOssClient() != null) {
                getOssClient().shutdown();
            }
        }

        return FileUploadResult.ok(getFileUrl(filePath));
    }

    /**
     * @description: 获取配置信息
     * @return PluginConfig
     */
    @Override
    public PluginConfig getPluginConfig() {
        PluginConfig pluginConfig = new PluginConfig();
        Map<String,String> map = new HashMap<>();
        map.put("endpoint",aliyunOssConfig.getEndpoint());
        map.put("accessKeyId",aliyunOssConfig.getAccessKeyId());
        map.put("accessKeySecret",aliyunOssConfig.getAccessKeySecret());
        map.put("bucketName",aliyunOssConfig.getBucketName());
        map.put("imageSuffix",aliyunOssConfig.getImageSuffix());
        map.put("fileSuffix",aliyunOssConfig.getFileSuffix());
        pluginConfig.setAttributes(map);

        return pluginConfig;
    }

    /**
     * @description: 检查文件后缀是否合法
     * @param fileName: 文件名
     * @param type: 文件类型
     * @return java.lang.Boolean
     */
    private Boolean checkType(String fileName, Type type){
        // 获取文件允许的后缀
        String[] fileType;
        switch (type) {
            case IMAGE:
                fileType = getImageSuffix();
                break;
            case FILE:
                fileType = getFileSuffix();
                break;
            default:
                fileType = null;
        }
        // 检查上传文件是否合法
        for (String s : fileType) {
            if (StringUtils.endsWithIgnoreCase(fileName, s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @description: 获取上传文件路径
     * @param sourceFileName: 文件名
     * @param type: 文件类型
     * @return 上传文件路径
     */
    private String getFilePath(String sourceFileName, Type type) {
        DateTime dateTime = new DateTime();
        String prefix;
        switch (type) {
            case IMAGE:
                prefix = "images/";
                break;
            case FILE:
                prefix = "files/";
                break;
            default:
                prefix = "other/";
        }
        return prefix + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }

    /**
     * @description: 获取上传文件url
     * @param filePath: 文件路径
     * @return 文件url
     */
    private String getFileUrl(String filePath) {
        return "https://" + getBucketName() + "." + getEndpoint() + "/" + filePath;
    }

    /**
     * @description: 获取阿里云连接
     * @return com.aliyun.oss.OSS
     */
    private OSS getOssClient() {
        if (this.ossClient == null){
            return new OSSClientBuilder().build(getEndpoint(), getAccessKeyId(), getAccessKeySecret());
        }else {
            return this.ossClient;
        }
    }
}

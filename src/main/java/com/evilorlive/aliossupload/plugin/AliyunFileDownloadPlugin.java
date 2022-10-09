package com.evilorlive.aliossupload.plugin;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.evilorlive.aliossupload.config.AliyunOssConfig;
import com.evilorlive.aliossupload.entity.PluginConfig;
import com.evilorlive.aliossupload.vo.FileDownloadResult;
import com.evilorlive.aliossupload.vo.FileUploadResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component("aliyunFileDownloadPlugin")
public class AliyunFileDownloadPlugin extends FileDownloadPlugin{

    private OSS ossClient;

    @Autowired
    private AliyunOssConfig aliyunOssConfig;

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


    @Override
    public FileDownloadResult simpleDownload(OutputStream outputStream, String fileUrl) throws IOException {

        try {
            if (!checkExist(fileUrl)){
                return FileDownloadResult.failExist();
            }
            GetObjectRequest getObjectRequest = new GetObjectRequest(getBucketName(), getFilePath(fileUrl));
            OSSObject ossObject = getOssClient().getObject(getObjectRequest);

            BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
            BufferedOutputStream out = new BufferedOutputStream(outputStream);

            byte[] buffer = new byte[1024];
            int lenght = 0;
            while ((lenght = in.read(buffer)) != -1) {
                out.write(buffer, 0, lenght);
            }

            out.flush();
            out.close();
            in.close();
        } catch (OSSException oe) {
            FileDownloadResult.failOss(oe.getMessage());
        } catch (ClientException ce) {
            FileDownloadResult.failClient(ce.getMessage());
        } catch (IOException ie) {
            FileUploadResult.failRead(ie.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return FileDownloadResult.ok(fileUrl);
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
        pluginConfig.setAttributes(map);

        return pluginConfig;
    }


    /**
     * @description: 检测文件是否存在
     * @return Boolean
     */
    @Override
    public Boolean checkExist(String fileUrl) {
        try {
            // 判断文件是否存在。如果返回值为true，则文件存在，否则存储空间或者文件不存在。
            // 设置是否进行重定向或者镜像回源。默认值为true，表示忽略302重定向和镜像回源；如果设置isINoss为false，则进行302重定向或者镜像回源。
            //boolean isINoss = true;
            boolean found = getOssClient().doesObjectExist(getBucketName(), getFilePath(fileUrl));
            //boolean found = ossClient.doesObjectExist(bucketName, objectName, isINoss);
            return found;
        } catch (OSSException oe) {
            FileDownloadResult.failOss(oe.getMessage());
        } catch (ClientException ce) {
            FileDownloadResult.failClient(ce.getMessage());
        } finally {
            if (getOssClient() != null) {
                getOssClient().shutdown();
            }
        }

        return false;
    }

    /**
     * @description: 获取下载文件路径
     * @param fileUrl: 文件url
     * @return 下载文件路径
     */
    private String getFilePath(String fileUrl) {
        return StringUtils.substringAfterLast(fileUrl, getEndpoint() + "/");
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

package com.evilorlive.aliossupload.plugin;

import com.evilorlive.aliossupload.entity.PluginConfig;
import com.evilorlive.aliossupload.vo.FileDownloadResult;
import com.evilorlive.aliossupload.vo.FileUploadResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

public abstract class FileDownloadPlugin {


    public abstract FileDownloadResult simpleDownload(OutputStream outputStream, String fileUrl) throws IOException;


    /**
     * 获取ID
     *
     * @return ID
     */
    public String getId() {
        return getClass().getAnnotation(Component.class).value();
    }

    /**
     * 获取属性值
     *
     * @param name 属性名称
     * @return 属性值
     */
    public String getAttribute(String name) {
        PluginConfig pluginConfig = getPluginConfig();
        return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
    }

    /**
     * 获取插件配置
     *
     * @return 插件配置
     */
    public PluginConfig getPluginConfig() {
        return new PluginConfig();
    }

    /**
     * 获取文件名
     *
     * @return 文件名
     */
    public String getFileName(String fileUrl) {
        return StringUtils.substringAfterLast(fileUrl, "/");
    }

    /**
     * 检测文件是否存在
     *
     * @return Boolean
     */
    public abstract Boolean checkExist(String fileUrl);

}

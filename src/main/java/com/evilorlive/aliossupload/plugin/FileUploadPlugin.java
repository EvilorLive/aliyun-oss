package com.evilorlive.aliossupload.plugin;

import com.evilorlive.aliossupload.entity.PluginConfig;
import com.evilorlive.aliossupload.vo.FileUploadResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

public abstract class FileUploadPlugin {

    /**
     * 类型
     */
    public enum Type {

        /**
         * 图片
         */
        IMAGE,

        /**
         * 文件
         */
        FILE
    }


    public abstract FileUploadResult simpleUpload(MultipartFile uploadFile, FileUploadPlugin.Type type);


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

}

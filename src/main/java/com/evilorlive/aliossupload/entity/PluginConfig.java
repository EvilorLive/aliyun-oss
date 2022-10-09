package com.evilorlive.aliossupload.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PluginConfig {

    /**
     * 插件ID
     */
    private String pluginId;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 属性
     */
    private Map<String, String> attributes = new HashMap<>();

    /**
     * 获取属性值
     *
     * @param name 属性名称
     * @return 属性值
     */
    public String getAttribute(String name) {
        return attributes.getOrDefault(name,null);
    }

}

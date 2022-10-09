package com.evilorlive.aliossupload.controller;

import com.evilorlive.aliossupload.plugin.FileUploadPlugin;
import com.evilorlive.aliossupload.vo.FileUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {

    @Autowired
    private Map<String, FileUploadPlugin> fileUploadPluginMap = new HashMap<>();

    @PostMapping("/upload")
    public FileUploadResult uploadOSS(@RequestParam(value = "file") MultipartFile file){

        FileUploadPlugin fileUploadPlugin = fileUploadPluginMap.get("aliyunFileUploadPlugin");

        FileUploadResult fileUploadResult = fileUploadPlugin.simpleUpload(file, FileUploadPlugin.Type.IMAGE);

        return fileUploadResult;
    }

}

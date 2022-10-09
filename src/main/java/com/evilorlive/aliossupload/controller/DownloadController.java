package com.evilorlive.aliossupload.controller;

import com.evilorlive.aliossupload.plugin.FileDownloadPlugin;
import com.evilorlive.aliossupload.plugin.FileUploadPlugin;
import com.evilorlive.aliossupload.vo.FileDownloadResult;
import com.evilorlive.aliossupload.vo.FileUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DownloadController {

    @Autowired
    private Map<String, FileDownloadPlugin> fileDownloadPluginMap = new HashMap<>();

    @GetMapping("/download")
    public FileDownloadResult uploadOSS(@RequestParam(value = "fileUrl") String fileUrl, HttpServletResponse response) throws IOException {

        FileDownloadPlugin fileDownloadPlugin = fileDownloadPluginMap.get("aliyunFileDownloadPlugin");

        // 浏览器以附件形式下载
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileDownloadPlugin.getFileName(fileUrl).getBytes(), "ISO-8859-1"));

        FileDownloadResult fileDownloadResult = fileDownloadPlugin.simpleDownload(response.getOutputStream(), fileUrl);

        return fileDownloadResult;
    }

}

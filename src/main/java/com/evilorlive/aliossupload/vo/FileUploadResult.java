package com.evilorlive.aliossupload.vo;

import lombok.Data;

@Data
public class FileUploadResult {

    // 上传成功
    private static final Integer STATUS_OK = 200;

    // 文件后缀不合法
    private static final Integer STATUS_FAIL_ILLEGAL = 500;

    // OSS 请求报错
    private static final Integer STATUS_FAIL_OSS = 501;

    // OSS 连接异常
    private static final Integer STATUS_FAIL_CLIENT = 502;

    // 文件读取异常
    private static final Integer STATUS_FAIL_READ = 503;

    // 状态码
    private Integer status;

    // 状态信息
    private String message;

    // 文件url
    private String url;

    public FileUploadResult(Integer status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }

    public static FileUploadResult ok(String url) {
        return new FileUploadResult(STATUS_OK, "上传成功！", url);
    }

    public static FileUploadResult failIllegal() {
        return new FileUploadResult(STATUS_FAIL_ILLEGAL, "文件格式不正确，请重试！",null);
    }

    public static FileUploadResult failOss(String msg) {
        return new FileUploadResult(STATUS_FAIL_OSS, msg,null);
    }

    public static FileUploadResult failClient(String msg) {
        return new FileUploadResult(STATUS_FAIL_CLIENT, msg,null);
    }

    public static FileUploadResult failRead(String msg) {
        return new FileUploadResult(STATUS_FAIL_READ, msg,null);
    }

}

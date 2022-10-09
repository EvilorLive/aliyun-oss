package com.evilorlive.aliossupload.vo;

import lombok.Data;

@Data
public class FileDownloadResult {

    // 下载成功
    private static final Integer STATUS_OK = 200;

    // OSS 请求报错
    private static final Integer STATUS_FAIL_OSS = 501;

    // OSS 连接异常
    private static final Integer STATUS_FAIL_CLIENT = 502;

    // 文件读取异常
    private static final Integer STATUS_FAIL_READ = 503;

    // 文件读取异常
    private static final Integer STATUS_FAIL_EXIST = 504;

    // 状态码
    private Integer status;

    // 状态信息
    private String message;

    // 文件url
    private String url;

    public FileDownloadResult(Integer status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }

    public static FileDownloadResult ok(String url) {
        return new FileDownloadResult(STATUS_OK, "文件下载成功！", url);
    }

    public static FileDownloadResult failOss(String msg) {
        return new FileDownloadResult(STATUS_FAIL_OSS, msg,null);
    }

    public static FileDownloadResult failClient(String msg) {return new FileDownloadResult(STATUS_FAIL_CLIENT, msg,null);}

    public static FileDownloadResult failRead(String msg) {
        return new FileDownloadResult(STATUS_FAIL_READ, msg,null);
    }

    public static FileDownloadResult failExist() {
        return new FileDownloadResult(STATUS_FAIL_EXIST, "文件不存在，请重试！",null);
    }

}

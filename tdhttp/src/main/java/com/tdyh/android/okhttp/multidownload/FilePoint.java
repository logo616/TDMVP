package com.tdyh.android.okhttp.multidownload;



public class FilePoint {
    private String fileName;//文件名
    private String url;//下载地址
    private String fileDir;//下载目录

    public FilePoint(String url) {
        this.url = url;
    }

    public FilePoint(String fileDir, String url) {
        this.fileDir = fileDir;
        this.url = url;
    }

    public FilePoint(String url, String fileDir, String fileName) {
        this.url = url;
        this.fileDir = fileDir;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

}

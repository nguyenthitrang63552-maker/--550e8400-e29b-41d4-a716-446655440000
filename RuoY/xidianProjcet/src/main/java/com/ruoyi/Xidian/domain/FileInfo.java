package com.ruoyi.Xidian.domain;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.net.URLEncoder;

/**
 * 文件信息实体类
 * 用于封装文件的名称、大小、MIME类型、访问链接等核心属性
 */
public class FileInfo {
    // 文件名称
    private String name;
    // 文件大小（字节）
    private long size;
    // MIME类型（如application/json、image/png）
    private String mimeType;
    // 文件类型（可根据业务定义，如文档、图片、视频）
    private String fileType;
    // 文件内容访问URL
    private String contentUrl;

    public FileInfo() {}

    public FileInfo(Path path, String mime, String type, String reqPath) throws IOException {
        this.name = path.getFileName().toString();
        this.size = Files.size(path);
        this.mimeType = mime;
        this.fileType = type;
        this.contentUrl = "/api/file/content?path=" + URLEncoder.encode(reqPath, "UTF-8");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}

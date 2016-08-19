/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.vo;

import com.tcci.fc.entity.content.TcApplicationdata;

/**
 *
 * @author Jason.Yu
 */
public class AttachmentVO {
    private String fileName;
    private String contentType;
    private byte[] content;
    private long size;
    private TcApplicationdata applicationdata;
    private int index;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public TcApplicationdata getApplicationdata() {
        return applicationdata;
    }

    public void setApplicationdata(TcApplicationdata applicationdata) {
        this.applicationdata = applicationdata;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
}

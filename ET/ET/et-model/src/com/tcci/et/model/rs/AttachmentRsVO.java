/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rs;

import java.io.Serializable;

/**
 *
 * @author Kyle.Cheng
 */
public class AttachmentRsVO extends BaseResponseVO implements Serializable {
    private Long appId;
    private String fileName;
    private String contentType;
    private int index;
    private byte[] content;
    private String url;
    
    /**
     *  genUrl(http://192.168.203.51/et-web", "/services/tender/file", tid, appId)
     * @param urlRoot
     * @param path
     * @param tid
     * @param appId
     * @return 
     */
    public String genUrl(String urlRoot, String path, Long tid, Long appId){
//        if( savename==null ){
//            return null;
//        }
        StringBuilder sb = new StringBuilder();
        sb.append(urlRoot).append(path);

        StringBuilder qstr = new StringBuilder();
        qstr.append("/").append(tid)
                .append("/").append(appId);
        
        sb.append(qstr);
        
        return sb.toString();
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

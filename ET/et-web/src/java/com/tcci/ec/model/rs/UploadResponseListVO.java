/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class UploadResponseListVO {
    List<UploadResponseVO> files;

    public List<UploadResponseVO> getFiles() {
        return files;
    }

    public void setFiles(List<UploadResponseVO> files) {
        this.files = files;
    }
}

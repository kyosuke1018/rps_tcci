/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.util;

import org.primefaces.model.UploadedFile;

/**
 *
 * @author Jimmy.Lee
 */
public interface AttachmentEventListener {

    public boolean uploadVerify(UploadedFile uploadFile);

}

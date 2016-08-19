/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.doc;

import com.tcci.fc.entity.repository.TcFolder;

/**
 *
 * @author Jason.Yu
 */
public class FolderVO {
    TcFolder folder;

    public TcFolder getFolder() {
        return folder;
    }

    public void setFolder(TcFolder folder) {
        this.folder = folder;
    }
 
    public String toString(){
        return folder.getName();
    }
}

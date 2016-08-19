/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.event.content;

import com.tcci.fc.entity.content.ContentHolder;

/**
 *
 * @author nEO.Fu
 */

public class ContentHolderEvent {
    public static final int CREATE_EVENT = 0;
    public static final int EDIT_EVENT = 1;
    public static final int DESTROY_EVENT = 2;
    public static final int DOWNLOAD_EVENT = 3;
    private int action;
    private ContentHolder contentHolder;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public ContentHolder getContentHolder() {
        return contentHolder;
    }

    public void setContentHolder(ContentHolder contentHolder) {
        this.contentHolder = contentHolder;
    }
}

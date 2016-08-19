/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.content;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Gilbert.Lin
 */
public enum ContentRole {

    PRIMARY("P"),
    SECONDARY("S");
    private String contentRole;

    private ContentRole(String contentRole) {
        this.contentRole = contentRole;
    }

    @Override
    public String toString() {
        return contentRole;
    }

    public Character toCharacter() {
        return contentRole.charAt(0);
    }
    
    public static ContentRole fromCharacter(Character c) throws Exception {
        for(ContentRole contentRole: ContentRole.values()) {
            if(contentRole.toCharacter().equals(c)) {
                return contentRole;
            }
        }
        throw new Exception ("wrong ContentRole character");
    }

    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }
}

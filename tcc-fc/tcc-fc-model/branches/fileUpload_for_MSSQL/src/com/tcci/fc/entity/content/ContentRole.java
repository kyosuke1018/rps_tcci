/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.entity.content;

/**
 *
 * @author Gilbert.Lin
 */
public enum ContentRole {
    PRIMARY ( "P" ), 
    SECONDARY ( "S" );

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
}

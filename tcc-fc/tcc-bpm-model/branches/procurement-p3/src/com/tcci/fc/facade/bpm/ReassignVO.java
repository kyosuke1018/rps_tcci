/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.org.TcUser;

/**
 *
 * @author Jimmy.Lee
 */
public class ReassignVO {
    private TcUser newOwner;
    private String comments;

    // c'tor
    public ReassignVO(TcUser newOwner, String comments) {
        this.newOwner = newOwner;
        this.comments = comments;
    }
    
    // getter, setter
    public TcUser getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(TcUser newOwner) {
        this.newOwner = newOwner;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

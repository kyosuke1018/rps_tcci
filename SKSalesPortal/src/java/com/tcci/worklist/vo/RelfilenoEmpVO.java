/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.vo;

import com.tcci.fc.entity.org.TcUser;

/**
 *
 * @author nEO.Fu
 */
public class RelfilenoEmpVO {

    private String bname;
    private TcUser tcUser;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public TcUser getTcUser() {
        return tcUser;
    }

    public void setTcUser(TcUser tcUser) {
        this.tcUser = tcUser;
    }

    public String getDisplayIdentifier() {
        return bname + "-" + tcUser.getDisplayIdentifier();
    }
    
}

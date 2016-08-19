/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.event.content;

import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvvault;
import java.util.List;

/**
 *
 * @author nEO
 */

public class TcApplicationdataEvent {
    public static final int CREATE_EVENT = 0;
    public static final int EDIT_EVENT = 1;
    public static final int DESTROY_EVENT = 2;
    public static final int DOWNLOAD_EVENT = 3;
    private int action;
    private TcApplicationdata applicationdata;
    private List<TcFvvault> tcFvvaultList; // 要處裡的實體檔案存放位置 (null 值表 TcDomain 關聯的全部 TcFvvault)

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public TcApplicationdata getApplicationdata() {
        return applicationdata;
    }

    public void setApplicationdata(TcApplicationdata applicationdata) {
        this.applicationdata = applicationdata;
    }

    public List<TcFvvault> getTcFvvaultList() {
        return tcFvvaultList;
    }

    public void setTcFvvaultList(List<TcFvvault> tcFvvaultList) {
        this.tcFvvaultList = tcFvvaultList;
    }

}

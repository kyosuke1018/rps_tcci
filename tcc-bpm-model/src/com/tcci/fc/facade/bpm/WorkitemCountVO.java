/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

/**
 *
 * @author Jimmy.Lee
 */
public class WorkitemCountVO {

    private String groupName;
    private int count;

    // c'tor
    public WorkitemCountVO() {
    }

    public WorkitemCountVO(String groupName, int count) {
        this.groupName = groupName;
        this.count = count;
    }

    // getter, setter
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}

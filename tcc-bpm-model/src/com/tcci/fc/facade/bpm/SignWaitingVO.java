/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Oliver.chen
 */
public class SignWaitingVO {

    private String email;
    private List<SignWaitingDetailVO> list;

    public SignWaitingVO() {}
    
    public SignWaitingVO(String email)
    {
        super();
        this.email = email;
    }

    public List<SignWaitingDetailVO> getList() {
        return list;
    }

    public void setList(List<SignWaitingDetailVO> list) {
        this.list = list;
    }    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setDetail(String appName, LinkedList<String> columnNames, LinkedList<LinkedList<String>> columnValues, String workListUrl) {
        SignWaitingDetailVO detail = new SignWaitingDetailVO();
        detail.setAppName(appName);
        detail.setColumnNames(columnNames);
        detail.setColumnValues(columnValues);
        detail.setWorkListUrl(workListUrl);
        if (this.list == null) {
            this.list = new LinkedList<SignWaitingDetailVO>();
        }
        this.list.add(detail);
    }
    
    public void addDetail(List<SignWaitingDetailVO> list)
    {
        if(list == null || list.size() == 0)
            return;
        if(this.list == null)
            this.list = new LinkedList<SignWaitingDetailVO>();
        this.list.addAll(list);
    }

    public class SignWaitingDetailVO {

        private String appName;
        private LinkedList<String> columnNames;
        private LinkedList<LinkedList<String>> columnValues;
        private String workListUrl;

        public LinkedList<String> getColumnNames() {
            return columnNames;
        }

        public void setColumnNames(LinkedList<String> columnNames) {
            this.columnNames = columnNames;
        }

        public LinkedList<LinkedList<String>> getColumnValues() {
            return columnValues;
        }

        public void setColumnValues(LinkedList<LinkedList<String>> columnValues) {
            this.columnValues = columnValues;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getWorkListUrl() {
            return workListUrl;
        }

        public void setWorkListUrl(String workListUrl) {
            this.workListUrl = workListUrl;
        }
        
    }
}
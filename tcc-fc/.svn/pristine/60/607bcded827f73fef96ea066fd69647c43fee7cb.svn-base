/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.controller.dialog.pickuser;

import java.io.Serializable;

/**
 *
 * @author Greg.Chou
 */
public class GroupUIComponent implements Serializable{
    private static final String GROUP_ID_DELIMITER = "_DELIM_";

    private String name;
    private String treeDisplayName;
    private String companyCode = "";
    private String depCode = "";

    public GroupUIComponent() {
    }
    
    public GroupUIComponent(String name, String treeDisplayName, String companyCode, String depCode/*, GroupUIComponent parent, String companyCode*/) {
        this.name = name;
        this.treeDisplayName = treeDisplayName;
        this.companyCode = companyCode;
        this.depCode = depCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setCode(String code) {
        this.depCode = code;
    }

    public String getTreeDisplayName() {
        return treeDisplayName;
    }

    public void setTreeDisplayName(String treeDisplayName) {
        this.treeDisplayName = treeDisplayName;
    }
    
    public String getGroupId() {
        return createGroupId(companyCode, depCode);
    }

    public boolean isMatch(String groupId) {
        String compareCompanyCode = null;
        String compareDepCode = null;
        String[] tokens = groupId.split(GROUP_ID_DELIMITER);
        compareCompanyCode = tokens[0];
        compareDepCode = tokens[1];
        
        if (compareDepCode.equals(compareCompanyCode)) {
            return getCompanyCode().equals(compareCompanyCode);
        } else {
            return this.getGroupId().equals(createGroupId(compareCompanyCode, compareDepCode));
        }
    }

    private static String createGroupId(String companyCode, String depCode) {
        return companyCode + GROUP_ID_DELIMITER + depCode;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.controller.dialog.pickuser;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Greg.Chou
 */
@ManagedBean
@ApplicationScoped
public class OrgUIComponentFactory {
    private List<UserUIComponent> userList = new ArrayList<UserUIComponent>();
    private List<GroupUIComponent> groupList = new ArrayList<GroupUIComponent>();

    private static final String GROUP_IDENT = "&nbsp;&nbsp;&nbsp;&nbsp;";
    private static final String GROUP_PREFIX = "-";

    @ManagedProperty(value="#{hepTreeFactory}")
    private TreeFactory treeFactory;
    public void setTreeFactory(TreeFactory treeFactory) {
        this.treeFactory = treeFactory;
    }    

    @PostConstruct
    private void init() {
        for (TreeNode company: treeFactory.getRoot().getChildren()) {
            final String companyCode = company.getCode();
            final String companyName = company.getName();
                
            createUIComponent(company, companyCode, companyName, 1);
        }
    }

    private void createUIComponent(TreeNode group, String topNodeCode, String topNodeName, int level) {
        int displayLevel = level;
        
        GroupUIComponent depObj = new GroupUIComponent(group.getName(), getPrefix(displayLevel)+group.getName(), topNodeCode, group.getCode());
        groupList.add(depObj);        
        
        displayLevel++;
        for (TreeNode child : group.getChildren()) {
            if (child instanceof TreeLeaf) {
                UserUIComponent user = new UserUIComponent(child.getCode(), child.getName(), ((TreeLeaf)child).getEmail(), topNodeName, depObj);
                userList.add(user);                                
            }else {
                createUIComponent(child, topNodeCode, topNodeName, displayLevel);
            }
        }        
    }

    private static String getPrefix(int level) {
        String prefix = "";

        for (int i = 0; i < level; i++) {
            prefix += GROUP_IDENT;
        }

        prefix += GROUP_PREFIX;

        return prefix;
    }

    public List<UserUIComponent> getUserList() {
        return this.userList;
    }

    public List<GroupUIComponent> getGroupList() {
        return this.groupList;
    }
}

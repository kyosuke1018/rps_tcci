/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.dialog.pickuser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Greg.Chou
 */
public class TreeNode {
    private String name;
    private String code;
    private List<TreeNode> children = new ArrayList<TreeNode>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getChildren() {
        return this.children;
    }
    
    public void addChild(TreeNode node) {
        this.children.add(node);
    }
}

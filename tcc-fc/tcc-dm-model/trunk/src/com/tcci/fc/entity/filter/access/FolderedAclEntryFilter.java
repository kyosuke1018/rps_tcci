package com.tcci.fc.entity.filter.access;

import java.util.Set;

/**
 *
 * @author Neo.Fu
 */
public class FolderedAclEntryFilter {
    private String aclTargetClassName;
    private Long aclTargetId;
    private String aclPrincipalClassName;
    private Long aclPrincipalId;
    private String permissionmask;
    private Set principals;

    public String getAclTargetClassName() {
        return aclTargetClassName;
    }

    public void setAclTargetClassName(String aclTargetClassName) {
        this.aclTargetClassName = aclTargetClassName;
    }

    public Long getAclTargetId() {
        return aclTargetId;
    }

    public void setAclTargetId(Long aclTargetId) {
        this.aclTargetId = aclTargetId;
    }

    public String getAclPrincipalClassName() {
        return aclPrincipalClassName;
    }

    public void setAclPrincipalClassName(String aclPrincipalClassName) {
        this.aclPrincipalClassName = aclPrincipalClassName;
    }

    public Long getAclPrincipalId() {
        return aclPrincipalId;
    }

    public void setAclPrincipalId(Long aclPrincipalId) {
        this.aclPrincipalId = aclPrincipalId;
    }

    public String getPermissionmask() {
        return permissionmask;
    }

    public void setPermissionmask(String permissionmask) {
        this.permissionmask = permissionmask;
    }

    public Set getPrincipals() {
        return principals;
    }

    public void setPrincipals(Set principals) {
        this.principals = principals;
    }
}

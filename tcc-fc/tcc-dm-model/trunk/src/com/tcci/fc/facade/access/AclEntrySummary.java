package com.tcci.fc.facade.access;

/**
 *
 * @author nEO.Fu
 */
public class AclEntrySummary {
    private String permissionmask;
    private String inheritancemask;
    private String aclTargetClassName;
    private long aclTargetId;
    private String aclPrincipalClassName;
    private long aclPrincipalId;
    private int aclcount = 0;
    
    public AclEntrySummary () {
    }

    public String getPermissionmask() {
        return permissionmask;
    }

    public void setPermissionmask(String permissionmask) {
        this.permissionmask = permissionmask;
    }

    public String getInheritancemask() {
        return inheritancemask;
    }

    public void setInheritancemask(String inheritancemask) {
        this.inheritancemask = inheritancemask;
    }

    public boolean isCreatable() {
        return (getPermissionmask()!=null && 
                     getPermissionmask().length()>PermissionUtils.CREATE_PERMISSION && 
                     getPermissionmask().charAt(PermissionUtils.CREATE_PERMISSION)=='1');
    }    
    public boolean isReadable() {
        return (getPermissionmask()!=null && 
                     getPermissionmask().length()>PermissionUtils.READ_PERMISSION && 
                     getPermissionmask().charAt(PermissionUtils.READ_PERMISSION)=='1');
    }    
    public boolean isUpdatable() {
        return (getPermissionmask()!=null && 
                     getPermissionmask().length()>PermissionUtils.UPDATE_PERMISSION && 
                     getPermissionmask().charAt(PermissionUtils.UPDATE_PERMISSION)=='1');
    }    
    public boolean isDeletable() {
        return (getPermissionmask()!=null && 
                     getPermissionmask().length()>PermissionUtils.DELETE_PERMISSION && 
                     getPermissionmask().charAt(PermissionUtils.DELETE_PERMISSION)=='1');
    }
    public void addAclCount(int count) {
        aclcount += count;
    }
    public int getAclCount() {
        return aclcount;
    }

     private String replaceAt(String mask, int location, String c) {
         StringBuffer sb = new StringBuffer(mask);
         sb.replace(location, location+1, c);
         return sb.toString();
     }

     public void setCreate(boolean flag) {
         String mask = getPermissionmask();
         if (mask==null || mask.length()!=PermissionUtils.ACCESS_POLICY_DEFAULT.length()) {
             mask = PermissionUtils.ACCESS_POLICY_DEFAULT;
         }
         mask = replaceAt(mask, PermissionUtils.CREATE_PERMISSION, ((flag) ? "1" : "0"));
         setPermissionmask(mask);
         return;
     }     
     public void setRead(boolean flag) {
         String mask = getPermissionmask();
         if (mask==null || mask.length()!=PermissionUtils.ACCESS_POLICY_DEFAULT.length()) {
             mask = PermissionUtils.ACCESS_POLICY_DEFAULT;
         }
         mask = replaceAt(mask, PermissionUtils.READ_PERMISSION, ((flag) ? "1" : "0"));
         setPermissionmask(mask);
         return;
     }
     public void setUpdate(boolean flag) {
         String mask = getPermissionmask();
         if (mask==null || mask.length()!=PermissionUtils.ACCESS_POLICY_DEFAULT.length()) {
             mask = PermissionUtils.ACCESS_POLICY_DEFAULT;
         }
         mask = replaceAt(mask, PermissionUtils.UPDATE_PERMISSION, ((flag) ? "1" : "0"));
         setPermissionmask(mask);
         return;
     }
     public void setDelete(boolean flag) {
         String mask = getPermissionmask();
         if (mask==null || mask.length()!=PermissionUtils.ACCESS_POLICY_DEFAULT.length()) {
             mask = PermissionUtils.ACCESS_POLICY_DEFAULT;
         }
         mask = replaceAt(mask, PermissionUtils.DELETE_PERMISSION, ((flag) ? "1" : "0"));
         setPermissionmask(mask);
         return;
     }     

    public String getAclTargetClassName() {
        return aclTargetClassName;
    }

    public void setAclTargetClassName(String aclTargetClassName) {
        this.aclTargetClassName = aclTargetClassName;
    }


    public long getAclTargetId() {
        return aclTargetId;
    }

    public void setAclTargetId(long aclTargetId) {
        this.aclTargetId = aclTargetId;
    }


    public String getAclPrincipalClassName() {
        return aclPrincipalClassName;
    }

    public void setAclPrincipalClassName(String aclPrincipalClassName) {
        this.aclPrincipalClassName = aclPrincipalClassName;
    }


    public long getAclPrincipalId() {
        return aclPrincipalId;
    }

    public void setAclPrincipalId(long aclPrincipalId) {
        this.aclPrincipalId = aclPrincipalId;
    }
    
}

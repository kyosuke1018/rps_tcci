package com.tcci.fc.facade.access;

import com.tcci.fc.entity.access.FolderedAccessControlled;
import com.tcci.fc.entity.access.TcFolderedAclEntry;
import com.tcci.fc.entity.org.TcPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
public class PermissionUtils {

    private static final Logger logger = LoggerFactory.getLogger(PermissionUtils.class);
    public static final String ACCESS_POLICY_DEFAULT = "0000000";
    public static final String ACCESS_POLICY_ALL = "1111111";
    public static final int CREATE_PERMISSION = 0;
    public static final int READ_PERMISSION = 1;
    public static final int UPDATE_PERMISSION = 2;
    public static final int DELETE_PERMISSION = 3;
    public static final int MANAGEMENT_PERMISSION = 4;

    private static String replaceAt(String mask, int location, String c) {
        StringBuffer sb = new StringBuffer(mask);
        sb.replace(location, location + 1, c);
        return sb.toString();
    }

    public static boolean getAclFlag(String mask, int location) {
        boolean flag = false;
        if (mask != null && mask.length() == PermissionUtils.ACCESS_POLICY_DEFAULT.length()) {
            if ("1".equals(mask.substring(location, location + 1))) {
                flag = true;
            }
        }
        return flag;
    }

    public static String setAclFlag(String mask, int location, boolean flag) {
        String maskout = mask;
        if (maskout == null || maskout.length() != PermissionUtils.ACCESS_POLICY_DEFAULT.length()) {
            maskout = PermissionUtils.ACCESS_POLICY_DEFAULT;
        }
        maskout = replaceAt(maskout, location, ((flag) ? "1" : "0"));
        return maskout;
    }

    public static void setAclFlag(TcFolderedAclEntry aclentry, int location, boolean flag) {
        String mask = aclentry.getPermissionmask();
        aclentry.setPermissionmask(setAclFlag(mask, location, flag));
    }

    public static void setCreate(TcFolderedAclEntry aclentry, boolean flag) {
        setAclFlag(aclentry, PermissionUtils.CREATE_PERMISSION, flag);
    }

    public static void setRead(TcFolderedAclEntry aclentry, boolean flag) {
        setAclFlag(aclentry, PermissionUtils.READ_PERMISSION, flag);
    }

    public static void setUpdate(TcFolderedAclEntry aclentry, boolean flag) {
        setAclFlag(aclentry, PermissionUtils.UPDATE_PERMISSION, flag);
    }

    public static void setDelete(TcFolderedAclEntry aclentry, boolean flag) {
        setAclFlag(aclentry, PermissionUtils.DELETE_PERMISSION, flag);
    }

    public static void setManagement(TcFolderedAclEntry aclentry, boolean flag) {
        setAclFlag(aclentry, PermissionUtils.MANAGEMENT_PERMISSION, flag);
    }

    /**
     * for create TcFolderedAclEntry.
     *
     * @param summary source permission summary
     * @param source merge permission mask.
     * @return new permission mask.
     */
    public static AclEntrySummary mergePermission(AclEntrySummary summary, Object source) {
        String sourcePermissionmask = ACCESS_POLICY_DEFAULT;
        String sourceInheritancemask = ACCESS_POLICY_DEFAULT;
        if (source instanceof TcFolderedAclEntry) {
            TcFolderedAclEntry entry = (TcFolderedAclEntry) source;
            sourcePermissionmask = entry.getPermissionmask();
            sourceInheritancemask = entry.getInheritancemask();
        } else if (source instanceof AclEntrySummary) {
            AclEntrySummary entry = (AclEntrySummary) source;
            sourcePermissionmask = entry.getPermissionmask();
            sourceInheritancemask = entry.getInheritancemask();
        }
        String pstring = "";
        for (int i = 0; i < sourcePermissionmask.length(); i++) {
            boolean b1 = (sourcePermissionmask.charAt(i) == '1') ? true : false;
            boolean b2 = (summary.getPermissionmask().charAt(i) == '1') ? true : false;
            pstring += (b1 || b2) ? "1" : "0";
        }
        summary.setPermissionmask(pstring);
        String istring = "";
        for (int i = 0; i < sourceInheritancemask.length(); i++) {
            boolean b1 = (sourceInheritancemask.charAt(i) == '1') ? true : false;
            boolean b2 = (summary.getInheritancemask().charAt(i) == '1') ? true : false;
            istring += (b1 || b2) ? "1" : "0";
        }
        summary.setInheritancemask(istring);
        return summary;
    }

    /**
     * for update TcFolderedAclEntry.
     *
     * @param summary original permission mask
     * @param source remove permission mask.
     * @return new permission mask summary
     */
    public static AclEntrySummary updatePermission(AclEntrySummary summary, Object source) {
        String sourcePermissionmask = ACCESS_POLICY_DEFAULT;
        String sourceInheritancemask = ACCESS_POLICY_DEFAULT;
        if (source instanceof TcFolderedAclEntry) {
            TcFolderedAclEntry entry = (TcFolderedAclEntry) source;
            sourcePermissionmask = entry.getPermissionmask();
            sourceInheritancemask = entry.getInheritancemask();
        } else if (source instanceof AclEntrySummary) {
            AclEntrySummary entry = (AclEntrySummary) source;
            sourcePermissionmask = entry.getPermissionmask();
            sourceInheritancemask = entry.getInheritancemask();
        }
        String pstring = "";
        for (int i = 0; i < sourcePermissionmask.length(); i++) {
            if (sourcePermissionmask.charAt(i) == 'X') {
                pstring += summary.getPermissionmask().charAt(i);
            } else {
                pstring += sourcePermissionmask.charAt(i);
            }
        }
        summary.setPermissionmask(pstring);
        summary.setInheritancemask(sourceInheritancemask);
        return summary;
    }

    /**
     * for remove TcFolderedAclEntry.
     *
     * @param summary original permission mask
     * @param source remove permission mask.
     * @return
     */
    public static AclEntrySummary minusPermission(AclEntrySummary summary, Object source) {
        String sourcePermissionmask = ACCESS_POLICY_DEFAULT;
        String sourceInheritancemask = ACCESS_POLICY_DEFAULT;
        if (source instanceof TcFolderedAclEntry) {
            TcFolderedAclEntry entry = (TcFolderedAclEntry) source;
            sourcePermissionmask = entry.getPermissionmask();
            sourceInheritancemask = entry.getInheritancemask();
        } else if (source instanceof AclEntrySummary) {
            AclEntrySummary entry = (AclEntrySummary) source;
            sourcePermissionmask = entry.getPermissionmask();
            sourceInheritancemask = entry.getInheritancemask();
        }
        String pstring = "";
        for (int i = 0; i < sourcePermissionmask.length(); i++) {
            boolean b1 = (sourcePermissionmask.charAt(i) == '1') ? true : false;
            boolean b2 = (summary.getPermissionmask().charAt(i) == '1') ? true : false;
            if (b1) {
                pstring += "0";
            } else {
                pstring += (b2) ? "1" : "0";
            }
        }
        summary.setPermissionmask(pstring);
        summary.setInheritancemask(sourceInheritancemask);
        return summary;
    }

    public static AclEntrySummary newAclEntrySummary(FolderedAccessControlled folderedAccessControlled, TcPrincipal principal) {
        AclEntrySummary aclSummary = new AclEntrySummary();
        aclSummary.setPermissionmask(PermissionUtils.ACCESS_POLICY_DEFAULT);
        aclSummary.setInheritancemask(PermissionUtils.ACCESS_POLICY_DEFAULT);
        if (principal != null) {
            aclSummary.setAclPrincipalClassName(principal.getClass().getCanonicalName());
            aclSummary.setAclPrincipalId(principal.getId().longValue());
        }
        if (folderedAccessControlled != null) {
            aclSummary.setAclTargetClassName(folderedAccessControlled.getClass().getCanonicalName());
            aclSummary.setAclTargetId(folderedAccessControlled.getId());
        }
        return aclSummary;
    }

    public static AclEntrySummary diff(TcFolderedAclEntry source, TcFolderedAclEntry target) {
        AclEntrySummary summary = newAclEntrySummary(null, null);
        String sourcePermissionmask = source.getPermissionmask();
        String targetPermissionmask = target.getPermissionmask();
        String pstring = "";
        for (int i = 0; i < sourcePermissionmask.length(); i++) {
            if (sourcePermissionmask.charAt(i) == targetPermissionmask.charAt(i)) {
                pstring += "X";
            } else {
                pstring += targetPermissionmask.charAt(i);
            }
        }
        summary.setPermissionmask(pstring);
        return summary;
    }
}

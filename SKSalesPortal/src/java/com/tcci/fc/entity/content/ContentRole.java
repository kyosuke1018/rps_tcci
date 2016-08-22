package com.tcci.fc.entity.content;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Neo.Fu
 */
public enum ContentRole {
    PRIMARY("1"),
    SECONDARY("2"),
    PICTURE("P"),
    LICENSE("L"),
    CATEGORY("C"),
    BE("B");
    private String contentRole;

    private ContentRole(String contentRole) {
        this.contentRole = contentRole;
    }

    @Override
    public String toString() {
        return contentRole;
    }

    public Character toCharacter() {
        return contentRole.charAt(0);
    }
    
    public static ContentRole fromCharacter(Character c) throws Exception {
        for(ContentRole contentRole: ContentRole.values()) {
            if(contentRole.toCharacter().equals(c)) {
                return contentRole;
            }
        }
        throw new Exception ("wrong ContentRole character");
    }

    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }
}

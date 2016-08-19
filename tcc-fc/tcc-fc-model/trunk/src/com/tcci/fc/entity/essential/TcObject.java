/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.essential;

import com.tcci.fc.entity.essential.DisplayIdentity;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;

/**
 *
 * @author Jason.Yu
 */
public interface TcObject extends Persistable, DisplayIdentity {
    
    public abstract Date getCreatetimestamp();

    public abstract void setCreatetimestamp(Date timestamp);

    public abstract Date getModifytimestamp();

    public abstract void setModifytimestamp(Date timestamp);

    public abstract TcUser getCreator();

    public abstract void setCreator(TcUser user);

    public abstract TcUser getModifier();

    public abstract void setModifier(TcUser user);
    
}

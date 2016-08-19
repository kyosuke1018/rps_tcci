/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.content;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;

/**
 *
 * @author Gilbert.Lin
 */
public interface ContentItem extends Persistable {
    public String getContainerclassname();
    public void setContainerclassname(String containerClassName);
    public Long getContainerid();
    public void setContainerid(Long containerId);
    public Date getCreatetimestamp();
    public void setCreatetimestamp(Date createTimestamp);
    public Character getContentrole();
    public void setContentrole(Character role);
    public TcUser getCreator();
    public void setCreator(TcUser creator);
    public String getDescription();
    public void setDescription(String description);

}

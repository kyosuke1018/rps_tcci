package com.tcci.fc.entity.repository;

import com.tcci.fc.entity.essential.TcObject;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;

/**
 *
 * @author Wayne_Hu
 */
public interface Foldered extends TcObject {

    TcFolder folder = null;

    String getDisplayIdentifier();

    Long getId();

    Date getCreatetimestamp();

    void setCreatetimestamp(Date createTimestamp);

    Date getModifytimestamp();

    void setModifytimestamp(Date modifyTimestamp);

    TcUser getCreator();

    void setCreator(TcUser creator);

    TcUser getModifier();

    void setModifier(TcUser modifier);

    TcFolder getFolder();

    void setFolder(TcFolder folder);

    public Boolean getIsremoved();

    public void setIsremoved(Boolean isremoved);
}

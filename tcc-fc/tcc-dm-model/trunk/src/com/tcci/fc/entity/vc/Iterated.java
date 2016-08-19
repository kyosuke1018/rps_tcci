package com.tcci.fc.entity.vc;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.essential.TcObject;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;

public interface Iterated<E extends Mastered> extends Cloneable, Persistable, TcObject {

    public String getIterationnumber();

    public void setIterationnumber(String iterationNumber);

    public Boolean getIslatestiteration();

    public void setIslatestiteration(Boolean isLatestIteration);

    public Date getCreatetimestamp();

    public void setCreatetimestamp(Date createTimestamp);

    public Date getModifytimestamp();

    public void setModifytimestamp(Date modifyTimestamp);

    public TcUser getModifier();

    public void setModifier(TcUser modifier);

    public TcUser getCreator();

    public void setCreator(TcUser creator);

    public E getMaster();

    public void setMaster(E mastered);

    public Iterated clone();
}


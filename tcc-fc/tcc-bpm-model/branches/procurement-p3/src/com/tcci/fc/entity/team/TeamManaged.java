package com.tcci.fc.entity.team;

import com.tcci.fc.entity.essential.Persistable;


/**
 *
 * @author Richard_Tsai
 */
public interface TeamManaged extends Persistable {

    public TcTeam getTeam();

    public void setTeam(TcTeam team);

}

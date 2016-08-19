/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

import com.tcci.fc.entity.essential.DisplayIdentity;
import com.tcci.fc.entity.essential.Persistable;

/**
 *
 * @author Jason.Yu
 */
public interface TcPrincipal extends Persistable, DisplayIdentity{
    
    public Long getId();
 
    public void setId(Long id);
    
    public String getName();

    public void setName(String name);

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.essential;

import com.tcci.fc.entity.essential.Persistable;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Wayne.Hu
 */
@Stateless
@Named
public class EssentialFacade {
        @PersistenceContext(unitName="Model")
    private EntityManager em;

     public Persistable getObject(String entityClassName, Long primaryKey) throws Exception {

        Persistable obj = null;
        try {
            obj = (Persistable) em.find(Class.forName(entityClassName), primaryKey);
        } catch (Exception e) {
            e.printStackTrace();
        }    
        return obj;
     }   
        
      public Persistable getObject(String oid) throws Exception {
        int index = oid.indexOf(":");
        if (index < 0) {
            throw new Exception("Incorrect oid format");
        }

        Persistable obj = null;
        try {
            String entityClassName = oid.substring(0, index);
            String primaryKey = oid.substring(index + 1);
            Class entityClass = Class.forName(entityClassName);
            obj = (Persistable) em.find(entityClass, Long.valueOf(primaryKey));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
}

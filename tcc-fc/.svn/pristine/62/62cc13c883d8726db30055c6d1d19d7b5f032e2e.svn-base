/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.essential.EssentialFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.Pan
 */
@Stateless
@Named
public class TcApplicationdataFacade extends AbstractFacade<TcApplicationdata> {
    private static final Logger logger = LoggerFactory.getLogger(TcApplicationdataFacade.class);
    
    @Inject
    EssentialFacade essentialFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcApplicationdataFacade() {
        super(TcApplicationdata.class);
    }

    /**
     * 移除所有無 ContentHolder 關聯的 TcApplicationdata
     */
    public void removeNoRefData(){
        List<TcApplicationdata> tcApplicationdataList = findAll();
        
        for(TcApplicationdata tcApplicationdata : tcApplicationdataList){
            String classname = tcApplicationdata.getContainerclassname();
            Long id = tcApplicationdata.getContainerid();
            try {
                Persistable contentHolder = essentialFacade.getObject(classname, id);
                if( contentHolder==null ){// 無關聯
                    logger.error("TcApplicationdataFacade => contentHolder==null => classname =" + classname + ": id=" + id);
                    em.remove(tcApplicationdata);
                }
            } catch (Exception ex) {
                logger.error("TcApplicationdataFacade => removeNoRefData Exception !", ex);
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.event.content;

import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.content.TcFvitemFacade;
import com.tcci.fc.facade.content.TcFvvaultFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TcDomain Event Handler
 * @author Peter.pan
 */
@Named
@Stateless
public class TcDomainEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(TcDomainEventHandler.class);
    
    @Inject
    TcFvitemFacade tcFvitemFacade;
    
    public void TcDomainEventHandler(@Observes(during = TransactionPhase.AFTER_COMPLETION) TcDomainEvent tcDomainEvent) {
        logger.debug("TcDomainEvent fire event");
        TcDomain tcDomain = tcDomainEvent.getTcDomain();
        List<TcFvvault> tcFvvaultList = tcDomainEvent.getTcFvvaultList(); // 要處裡的實體檔案存放位置
        
        int action = tcDomainEvent.getAction();
        logger.debug("action={}",action);
        switch (action) {
            case TcDomainEvent.DESTROY_EVENT:
                tcFvitemFacade.destroyTcFvitemNoRef(tcDomain, tcFvvaultList);
                break;
            default:
                break;
        }
    }

}

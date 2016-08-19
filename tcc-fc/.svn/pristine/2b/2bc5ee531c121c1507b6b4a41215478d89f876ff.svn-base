/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.event.content;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.facade.content.ContentFacade;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@Named
@Stateless
public class ContentHolderEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(ContentHolderEventHandler.class);
    @Inject
    ContentFacade contentFacade;
    
    public void ContentHolderEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) ContentHolderEvent contentHolderEvent) throws Exception{
        logger.debug("ContentHolderEvent fire event");
        ContentHolder contentHolder = contentHolderEvent.getContentHolder();
        
        int action = contentHolderEvent.getAction();
        logger.debug("action={}",action);
        switch (action) {
            case TcApplicationdataEvent.DESTROY_EVENT:
                contentFacade.removeContent(contentHolder);
                break;
            default:
                throw new Exception("Not support yet!");
        }
    }
}

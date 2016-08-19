/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.event.content;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.event.ObjectEvent;
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

    public void ContentHolderEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) ObjectEvent objectEvent) throws Exception {
        logger.debug("ContentHolderEvent fire event");
        if (objectEvent.getObject() instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder)objectEvent.getObject();

            int action = objectEvent.getAction();
            logger.debug("action={}", action);
            switch (action) {
                case ObjectEvent.DESTROY_EVENT:
                    contentFacade.removeContent(contentHolder);
                    break;
                default:
                    throw new Exception("Not support yet!");
            }
        }
    }
}

package com.tcci.fc.event.content;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Alternative;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@Named
@Alternative
@Stateless
public class TcApplicationdataEventHandlerDefaultImpl implements TcApplicationdataEventHandler {

    Logger logger = LoggerFactory.getLogger(TcApplicationdataEventHandlerDefaultImpl.class);

    public void TcApplicationdataEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) TcApplicationdataEvent event) {
        logger.debug("default implementation");
        //do nothing.
    }
;
}

package com.tcci.fc.event.content;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;

/**
 *
 * @author nEO.Fu
 */
public interface TcApplicationdataEventHandler {

    public void TcApplicationdataEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) TcApplicationdataEvent event);
}

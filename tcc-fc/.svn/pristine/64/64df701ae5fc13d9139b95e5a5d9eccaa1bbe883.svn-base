/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.event.content;

import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.facade.content.ContentFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Named
@Alternative
@Stateless
public class TcApplicationdataEventHandlerPurgeImpl implements TcApplicationdataEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(TcApplicationdataEventHandlerPurgeImpl.class);
    @Inject
    ContentFacade contentFacade;

    @Override
    public void TcApplicationdataEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) TcApplicationdataEvent applicationdataEvent) {
        logger.debug("purge implementation");
        TcApplicationdata applicationdata = applicationdataEvent.getApplicationdata();
        List<TcFvvault> tcFvvaultList = applicationdataEvent.getTcFvvaultList(); // 要處裡的實體檔案存放位置
        boolean backupFileFlag = applicationdataEvent.isBackupFileFlag();   //是否要備份檔案

        int action = applicationdataEvent.getAction();
        logger.debug("action={}", action);
        switch (action) {
            case TcApplicationdataEvent.DESTROY_EVENT:
                contentFacade.destroyFvitems(applicationdata, tcFvvaultList, backupFileFlag);
                break;
            default:
                break;
        }
    }
}

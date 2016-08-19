/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.event.content;

import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.log.TcDownloadLog;
import com.tcci.fc.entity.log.TcDownloadLogDetail;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.log.TcDownloadLogDetailFacade;
import com.tcci.fc.facade.log.TcDownloadLogFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
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
 * @author Neo.Fu
 */
@Named
@Alternative
@Stateless
public class TcApplicationdataEventHandlerDownloadLogImpl implements TcApplicationdataEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(TcApplicationdataEventHandlerDownloadLogImpl.class);
    @Inject
    TcDownloadLogFacade downloadLogFacade;
    @Inject
    TcDownloadLogDetailFacade downloadLogDetailFacade;
    @Inject
    TcUserFacade userFacade;
    @Resource
    SessionContext sessionContext;
    
    @Override
    public void TcApplicationdataEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION)TcApplicationdataEvent event) {
        logger.debug("download log implementation");
        logger.debug("fire event");
        TcApplicationdata applicationdata = event.getApplicationdata();
        int action = event.getAction();
        logger.debug("action={}", action);
        switch (action) {
            case TcApplicationdataEvent.DESTROY_EVENT:
                destroyDownloadLog(applicationdata);
                break;
            case TcApplicationdataEvent.DOWNLOAD_EVENT:
                createDownloadLog(applicationdata);
                break;
            default:
                break;
        }
    }

    private void destroyDownloadLog(TcApplicationdata applicationdata) {
        TcDownloadLog downloadLog = downloadLogFacade.findByApplicationdata(applicationdata);
        if (downloadLog != null) {
            for (TcDownloadLogDetail detail : downloadLog.getDownloadLogDetailCollection()) {
                downloadLogDetailFacade.remove(detail);
            }
            downloadLog.setDownloadLogDetailCollection(null);
            downloadLog.setApplicationdata(null);
            downloadLogFacade.remove(downloadLog);
        }
    }

    private void createDownloadLog(TcApplicationdata applicationdata) {
        TcDownloadLog downloadLog = downloadLogFacade.findByApplicationdata(applicationdata);
        List<TcDownloadLogDetail> downloadLogDetails;
        Principal principal = sessionContext.getCallerPrincipal();
        TcUser tcUser = null;
        String loginAccount = null;
        if (principal != null) {
            loginAccount = principal.getName();
        }
        if (loginAccount != null) {
            tcUser = userFacade.findUserByLoginAccount(loginAccount);
        }
        logger.debug("tcUser={}", tcUser);
        Date now = new Date();
        if (null == downloadLog) {
            downloadLog = new TcDownloadLog();
            downloadLog.setApplicationdata(applicationdata);
            downloadLog.setTotalCount(0);
            downloadLog.setCreator(tcUser);
            downloadLog.setCreatetimestamp(now);
            downloadLogDetails = new ArrayList<TcDownloadLogDetail>();
            downloadLog.setDownloadLogDetailCollection(downloadLogDetails);
        }
        //create download detail.
        TcDownloadLogDetail detail = new TcDownloadLogDetail();
        detail.setDownloadLog(downloadLog);
        detail.setCreator(tcUser);
        detail.setCreatetimestamp(now);
        downloadLogDetailFacade.create(detail);

        //update download log.
        int totalCount = downloadLog.getTotalCount();
        downloadLog.setTotalCount(++totalCount);
        downloadLog.setModifier(tcUser);
        downloadLog.setModifytimestamp(now);
        downloadLog.getDownloadLogDetailCollection().add(detail);
        downloadLogFacade.save(downloadLog);
    }
}

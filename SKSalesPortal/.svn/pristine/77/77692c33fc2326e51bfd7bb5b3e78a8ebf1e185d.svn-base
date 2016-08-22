package com.tcci.sksp.event.quotation;

import com.tcci.sksp.entity.enums.QuotationStatusEnum;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.notification.SkQuotationCreateNotifier;
import com.tcci.sksp.notification.SkQuotationRejectNotifier;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class SkQuotationMasterEventHandler {

    Logger logger = LoggerFactory.getLogger(SkQuotationMasterEventHandler.class);
    @Inject
    SkQuotationRejectNotifier rejectNotifier;
    @Inject
    SkQuotationCreateNotifier createNotifier;

    public void SkQuotationMasterEvent(@Observes(during = TransactionPhase.AFTER_COMPLETION) SkQuotationMasterEvent event) {
        logger.debug("SkQuotationMasterEvent.fire()");
        SkQuotationMaster quotationMaster = event.getQuotationMaster();
        //申請通知
        if(QuotationStatusEnum.OPEN.equals(quotationMaster.getStatus())) {
            createNotifier.notify(quotationMaster);
        }
        //核退通知
        if (QuotationStatusEnum.REJECT.equals(quotationMaster.getStatus())) {
            rejectNotifier.notify(quotationMaster);
        }
    }
}

package com.tcci.sksp.notification;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.VelocityMail;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.entity.quotation.SkQuotationReviewHistory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Neo.Fu
 */
public class SkQuotationRejectNotifier {

    @Resource(mappedName = "jndi/sk.config")
    private Properties jndiConfig;

    public void notify(SkQuotationMaster quotation) {

        String subject = "報價單核退通知, 單號:" + quotation.getId();
        HashMap<String, Object> mailBean = new HashMap<String, Object>();
        SkQuotationReviewHistory latestReviewHistory = getLatestReviewHistory(quotation);
        List<TcUser> reviewerList = getReviewers(quotation);
        String receivers = "";
        for (TcUser reviewer : reviewerList) {
            if (receivers.length() > 0) {
                receivers += ",";
            }
            if (StringUtils.isNotEmpty(reviewer.getEmail())) {
                receivers += reviewer.getEmail();
            }
        }
        mailBean.put(VelocityMail.SUBJECT, subject);
        String to = "";
        if (StringUtils.isNotEmpty(quotation.getCreator().getEmail())) {
            to = quotation.getCreator().getEmail();
        } else {
            to = jndiConfig.getProperty("quotation.admin.mail");
        }
        mailBean.put(VelocityMail.TO, to);
        mailBean.put(VelocityMail.CC, receivers);
        mailBean.put("customer", quotation.getCustomer());
        mailBean.put("id", quotation.getId());
        mailBean.put("reviewer", latestReviewHistory.getReviewer());
        mailBean.put("remark", latestReviewHistory.getRemark());
        VelocityMail.sendMail(mailBean, "quotationRejectMail.vm");
    }

    private SkQuotationReviewHistory getLatestReviewHistory(SkQuotationMaster quotation) {
        List<SkQuotationReviewHistory> historyList = new ArrayList();
        historyList.addAll(quotation.getReviewHistoryCollection());
        Collections.sort(historyList, new Comparator<SkQuotationReviewHistory>() {
            @Override
            public int compare(SkQuotationReviewHistory h1, SkQuotationReviewHistory h2) {
                return h2.getReviewDate().compareTo(h1.getReviewDate());
            }
        });
        return historyList.get(0);
    }

    private List<TcUser> getReviewers(SkQuotationMaster quotation) {
        List<TcUser> reviewerList = new ArrayList();
        for (SkQuotationReviewHistory reviewHistory : quotation.getReviewHistoryCollection()) {
            reviewerList.add(reviewHistory.getReviewer());
        }
        return reviewerList;
    }
}

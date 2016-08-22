package com.tcci.sksp.notification;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.VelocityMail;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroup;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroupUser;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.facade.SkQuotationMailGroupFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@ManagedBean(name = "quotationCreateNotifier")
@ViewScoped
public class SkQuotationCreateNotifier {

    Logger logger = LoggerFactory.getLogger(SkQuotationCreateNotifier.class);
    @Resource(mappedName = "jndi/sk.config")
    private Properties jndiConfig;
    @EJB
    private SkSalesMemberFacade memberFacade;
    @EJB
    private SkQuotationMailGroupFacade mailGroupFacade;

    public void notify(SkQuotationMaster quotation) {
        logger.debug("notify");
        HashMap<String, Object> mailBean = new HashMap<String, Object>();
        //主旨
        String subject = quotation.getCustomer().getSapid() + "區已下單";
        mailBean.put(VelocityMail.SUBJECT, subject);

        //收件者
        String receivers = "";
        //creator
        if (StringUtils.isNotEmpty(quotation.getCreator().getEmail())) {
            receivers = quotation.getCreator().getEmail();
        } else {
            receivers = jndiConfig.getProperty("quotation.admin.mail");
        }
        //設定的收件者
        SkSalesMember salesMember = memberFacade.findByCode(quotation.getCustomer().getSapid());
        List<SkQuotationMailGroup> mailGroupList = mailGroupFacade.findByMember(salesMember);
        Set<String> emails = new HashSet();
        for (SkQuotationMailGroup mailGroup : mailGroupList) {
            for (SkQuotationMailGroupUser mailGroupUser : mailGroup.getUserCollection()) {
                TcUser receiver = mailGroupUser.getUser();
                //帳號已 disable 不寄送通知.
                if (!receiver.getDisabled()) {
                    if (StringUtils.isNotEmpty(mailGroupUser.getUser().getEmail())) {
                        emails.add(mailGroupUser.getUser().getEmail());
                    }
                }
            }
        }
        for (String email : emails) {
            if (receivers.length() > 0) {
                receivers += ",";
            }
            receivers += email;
        }
        logger.debug("receivers={}", receivers);
        mailBean.put(VelocityMail.TO, receivers);
        mailBean.put("sapid", quotation.getCustomer().getSapid());
        VelocityMail.sendMail(mailBean, "quotationCreateMail.vm");
    }
}

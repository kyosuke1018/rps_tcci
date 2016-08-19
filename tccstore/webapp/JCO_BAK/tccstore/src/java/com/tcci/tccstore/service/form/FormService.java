/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.form;

import com.tcci.tccstore.entity.EcForm;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.facade.form.EcFormFacade;
import com.tcci.tccstore.service.ServiceBase;
import com.tcci.tccstore.service.ServiceException;
import com.tcci.tccstore.util.MailNotify;
import java.text.MessageFormat;
import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Path("form")
public class FormService extends ServiceBase {
    
    private final static Logger logger = LoggerFactory.getLogger(FormService.class);

    @EJB
    private EcFormFacade ecFormFacade;

    @POST
    @Path("member")
    @Produces(MediaType.TEXT_PLAIN)
    public String formMember(
            @FormParam(PARAM_NAME) String name,
            @FormParam(PARAM_EMAIL) String email,
            @FormParam(PARAM_PHONE) String phone
    ) {
        name = StringUtils.trimToNull(name);
        email = StringUtils.trimToNull(email);
        phone = StringUtils.trimToNull(phone);
        if (null==name || null==email || null==phone) {
            String error = "name, email, phone都是必填!";
            logger.error(error);
            throw new ServiceException(error);
        }
        email = email.toLowerCase().trim();
        if (ecMemberFacade.findByLoginAccount(email) != null) {
            String error = MessageFormat.format("帐号({0})已存在!", email);
            logger.error(error);
            throw new ServiceException(error);
        }
        // jimmy.lee 20151028 改成直接註冊並發送email
        ecFormFacade.createFormMember(name, email, phone); // 保留記錄
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(email)) {
            String error = MessageFormat.format("EMail({0})格式不正确!", email);
            logger.error(error);
            throw new ServiceException(error);
        }
        
        EcMember ecMember = new EcMember(email, name, phone);
        String newPassword = RandomStringUtils.randomAlphanumeric(6);
        ecMember.setPassword(DigestUtils.sha256Hex(newPassword));
        ecMemberFacade.save(ecMember);
        MailNotify.memberRegister(email, newPassword);
        logger.warn("member:{} created!", email);
        return "OK";
    }

    @POST
    @Path("customer")
    @Produces(MediaType.TEXT_PLAIN)
    public String formCustomer(
            @FormParam(PARAM_NAME) String name,
            @FormParam(PARAM_EMAIL) String email,
            @FormParam(PARAM_PHONE) String phone,
            @FormParam(PARAM_PROVINCE) String province_idx
    ) {
        EcMember loginMember = getAuthMember();
        String province = "1".equals(province_idx) ? "广东省" :
                          "2".equals(province_idx) ? "广西壮族自治区" :
                          "3".equals(province_idx) ? "其它" : province_idx;
        EcForm ecForm = ecFormFacade.createFormCustomer(name, email, phone, loginMember, province);
        String contact = ecFormFacade.findContact(province);
        MailNotify.formCustomerApply(ecForm, province, contact);
        return "OK";
    }

}

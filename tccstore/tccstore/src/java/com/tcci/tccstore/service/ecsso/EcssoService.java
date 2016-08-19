/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.ecsso;

import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.facade.member.AccountNotExistException;
import com.tcci.tccstore.facade.member.PasswordWrongException;
import com.tcci.tccstore.service.ServiceBase;
import com.tcci.tccstore.service.ServiceException;
import com.tcci.tccstore.util.MailNotify;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jimmy.Lee
 */
@Path("ecsso")
public class EcssoService extends ServiceBase {

    @POST
    @Path("resetPassword")
    @Produces(MediaType.TEXT_PLAIN)
    public String resetPassword(@FormParam(PARAM_OLD_PASSWORD) String old_password, @FormParam(PARAM_NEW_PASSWORD) String new_password) {
        EcMember loginMember = getAuthMember();
        try {
            ecMemberFacade.resetPassword(loginMember, old_password, new_password);
        } catch (PasswordWrongException ex) {
            throw new ServiceException("旧密码不正确!");
        }
        return "OK";
    }

    @POST
    @Path("forgotPassword")
    @Produces(MediaType.TEXT_PLAIN)
    public String forgotPassword(@FormParam(PARAM_ACCOUNT) String account) {
        try {
            String newPassword = ecMemberFacade.forgotPassword(account);
            MailNotify.forgotPassword(account, newPassword);
        } catch (AccountNotExistException ex) {
            throw new ServiceException(account + " 帐号不存在或已停用!");
        }
        return "OK";
    }

}

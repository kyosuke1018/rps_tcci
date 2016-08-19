/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.crm;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.facade.member.EcMemberFacade;
import com.tcci.tccstore.facade.member.PasswordWrongException;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "changePassword")
@ViewScoped
public class ChangePassword {

    private String oldPassword;
    private String newPassword;
    private String newPassword2;

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    @EJB
    private EcMemberFacade ecMemberFacade;

    private ResourceBundle rb = ResourceBundle.getBundle("msgApp",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());

    // action
    public void submit() {
        try {
            if (StringUtils.isEmpty(oldPassword)) {
                JsfUtil.addErrorMessage(rb.getString("app.error.oldPasswordIsRequired"));
                return;
            }
            if (StringUtils.isEmpty(newPassword)) {
                JsfUtil.addErrorMessage(rb.getString("app.error.newPasswordIsRequired"));
                return;
            }
            if (!newPassword.equals(newPassword2)) {
                JsfUtil.addErrorMessage(rb.getString("app.error.passwordMismatch"));
                return;
            }
            ecMemberFacade.resetPassword(userSession.getEcMember(), oldPassword, newPassword);
            JsfUtil.addSuccessMessage(rb.getString("app.message.changePasswordSuccess")); // 密碼變更成功!
            // redirect to home
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.getExternalContext().redirect("index.xhtml");
            context.responseComplete();
        } catch (PasswordWrongException ex) {
            JsfUtil.addErrorMessage(rb.getString("app.error.oldPasswordWrong")); // 舊密碼不正確!
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "exception!");
        }
    }

    // getter, setter
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public EcMemberFacade getEcMemberFacade() {
        return ecMemberFacade;
    }

    public void setEcMemberFacade(EcMemberFacade ecMemberFacade) {
        this.ecMemberFacade = ecMemberFacade;
    }

}

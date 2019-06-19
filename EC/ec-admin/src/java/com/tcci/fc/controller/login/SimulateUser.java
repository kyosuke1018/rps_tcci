/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.login;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.controller.global.TcSessionController;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.util.ssoclient.SSOClient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class SimulateUser extends SessionAwareController implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(SimulateUser.class);

    private SuFormVO lastForm;
    private String systemCode;
    private String emuAccount;
    private String suguardUrl;

    //@ManagedProperty(value = "#{tcSessionController}")
    //protected TcSessionController userSession;

    @Resource(mappedName = "jndi/global.config")
    private Properties jndiGlobalConfig;

    private SSOClient ssoClient = SSOClient.getInstance();
    private String loginAccount;

    @PostConstruct
    private void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRequest();
            systemCode = request.getContextPath().substring(1).toUpperCase();
            loginAccount = sessionController.getOrigUser().getLoginAccount();
            suguardUrl = jndiGlobalConfig.getProperty("SUGUARD_URL");
            /*if (GlobalConstant.DEBUG_MODE) {
                suguardUrl = "http://192.168.203.81/suguard"; // for test
            }*/
            if (null == suguardUrl) { // 未設定suguardUrl則可任意切換(原登入者需有ADMINISTRATORS role)
                return;
            }
            Map<String, String> params = new HashMap<>();
            params.put("systemCode", systemCode);
            params.put("loginAccount", loginAccount);
            lastForm = ssoClient.executeGetService(suguardUrl + "/service/suform/last", params, SuFormVO.class);
            Date now = new Date();
            if (lastForm.getStartTime() != null && now.before(lastForm.getStartTime())) {
                lastForm.setExpired(true);
            } else if (lastForm.getEndTime() != null && now.after(lastForm.getEndTime())) {
                lastForm.setExpired(true);
            }
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }

    // action
    public String doSimulate() {
        try {
            if (null == emuAccount) {
                throw new Exception("模擬帳號必填!");
            }
            if (suguardUrl != null) {
                Map<String, String> params = new HashMap<>();
                params.put("systemCode", systemCode);
                params.put("loginAccount", loginAccount);
                params.put("emuAccount", emuAccount);
                //驗證及log
                ssoClient.executeGetService(suguardUrl + "/service/suform/simulate", params, String.class);
            }
            sessionController.setNewUserAccount(emuAccount);
            sessionController.doSimulateUser();

            return "/index.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            logger.error("doSimulateUser exception!", ex);
            JsfUtils.addErrorMessage(ex.getMessage());
        }

        return "#";
    }

    // helper
    public boolean canDo(String action) {
        if ("CREATE".equals(action)) {
//            return (suguardUrl != null) && ((null == lastForm) || lastForm.getStatus().matches("REJECT|CANCEL") || lastForm.isExpired());
            return (suguardUrl != null);
        } else if ("EDIT".equals(action)) {
            return (null != lastForm && lastForm.getStatus().matches("DRAFT"));
        } else if ("SIMULATE".equals(action)) {
            return (null == suguardUrl) || (null != lastForm && lastForm.getStatus().matches("APPROVE") && !lastForm.isExpired());
        }
        return false;
    }

    // getter, setter
    public SuFormVO getLastForm() {
        return lastForm;
    }

    public void setLastForm(SuFormVO lastForm) {
        this.lastForm = lastForm;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getEmuAccount() {
        return emuAccount;
    }

    public void setEmuAccount(String emuAccount) {
        this.emuAccount = emuAccount;
    }

    public String getSuguardUrl() {
        return suguardUrl;
    }

    public void setSuguardUrl(String suguardUrl) {
        this.suguardUrl = suguardUrl;
    }

}

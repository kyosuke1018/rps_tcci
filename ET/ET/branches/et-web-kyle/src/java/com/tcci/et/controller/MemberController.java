/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.entity.EtMember;
import com.tcci.et.facade.EtMemberFacade;
import com.tcci.et.facade.rs.AuthREST;
import com.tcci.et.facade.rs.VenderREST;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.rs.BaseResponseVO;
import com.tcci.et.model.rs.SubmitVO;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.security.SecurityConstants;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "memberController")
@ViewScoped
public class MemberController extends SessionAwareController implements Serializable {
    
    @Inject
    private IdentityStoreHandler identityStoreHandler;
    @Inject
    private AuthREST authREST;
    @Inject
    private VenderREST venderREST;
    @EJB private EtMemberFacade memberFacade;
    
    private EtMember member;
    private MemberVO memberVO;
    private String test = "stestsetete";
    private SubmitVO formVO;
    
    @PostConstruct
    private void init(){
        logger.info("init ...");
        try{
            member = this.getLoginUser();
//            HttpServletRequest request = JsfUtils.getRequest();
//            HttpSession session = request.getSession(false);
//            CallerPrincipal caller = (CallerPrincipal)session.getAttribute(SecurityConstants.CALLER_ATTR);
//            if(caller!=null){
//                logger.debug("session caller...:"+caller.getName());
//                logger.debug("session GROUPS_ATTR...:"+session.getAttribute(SecurityConstants.GROUPS_ATTR));
//            }
            if(member==null){
                logger.warn("member ..."+member);
//                FacesContext context = FacesContext.getCurrentInstance();
//                String url = context.getExternalContext().getRequestContextPath();
//                context.getExternalContext().redirect(url);
//                context.responseComplete();
            }else{
                logger.debug("memberId ...:"+member.getId());
                memberVO = memberFacade.findById(member.getId(), false, GlobalConstant.DEF_LOCALE.getLocale());
            }
            formVO = new SubmitVO();
            
        } catch (Exception e) {
            // ignore;
            logger.debug("logout exception :\n", e);
        }

    }
    
    protected void submit() throws Exception {
//        if (form.getStatus() == FormStatusEnum.DRAFT) {
//            reloadApprovers(true);
//            if (!hasApprover()) {
//                throw new Exception("無簽核人，請聯絡系統管理員!");
//            }
//            form.setStatus(FormStatusEnum.SIGNING);
//            formFacade.save(form);
////            TcProcess process = bpmEngine.createProcess(form.getCreator(), "PHASE1", roleApprovers, "", form);
//            TcProcess process = bpmEngine.createProcess(form.getCreator(), "BC", roleApprovers, "", form);
//            form.setProcess(process);
//            formFacade.save(form);
//        } else {
//            throw new Exception("目前表單狀態無法送出!");
//        }
    }
    
    // 登入
//    public String login(HttpServletRequest request, HttpServletResponse response, 
//            HttpMessageContext context) {
//    public String login(HttpServletRequest request, HttpServletResponse response) {
    public String login() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = JsfUtils.getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            String name = "kyle.cheng";
            String password = "abcd1234";
            CredentialValidationResult result = identityStoreHandler.validate(
                    new UsernamePasswordCredential(name, password));
            logger.debug("validateRequest result.getStatus() ="+result.getStatus());
            String url = "";
            if( result.getStatus() == VALID ){
                CallerPrincipal caller = result.getCallerPrincipal();
                        if( caller!=null ){
                            Principal p = request.getUserPrincipal();
                            logger.debug("*** validateRequest caller {}",caller.getName());
                            logger.debug("*** validateRequest CallerGroups {}",result.getCallerGroups().size());
//                            for(String group:result.getCallerGroups()){
//                                logger.debug("*** validateRequest Groups {}",group);
//                            }
                        }
                
                
                HttpSession session = request.getSession(false);
                session.setAttribute(SecurityConstants.CALLER_ATTR, result.getCallerPrincipal());// CallerPrincipal
                session.setAttribute(SecurityConstants.GROUPS_ATTR, result.getCallerGroups());
//                url = "/faces/member/member.xhtml";
            } else {
//                response.sendRedirect("index.xhtml");
//                url = "/faces/index.xhtml";
            }
//            response.sendRedirect("index.xhtml");
//            String url = context.getExternalContext().getRequestContextPath();
//            logger.debug("login Redirect url:{}", url);
//            JsfUtils.redirect(url);
        } catch (Exception e) {
            // ignore;
            logger.debug("login exception :\n", e);
        }
//        return "?faces-redirect=true";
        return "";
    }

    public String login2() {
        try {
            String name = "kyle.cheng";
            String password = "abcd1234";
//            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = JsfUtils.getRequest();
            formVO = new SubmitVO();
            formVO.setLoginAccount(name);
            formVO.setPwd(password);
                    
            
//            Response res = authREST.login(request, formVO);
            Response res = authREST.login(request, name, password);
            logger.debug("res = "+res);
            int status = res.getStatus();
//            String result = this.getResult(res);
//            res.getEntity();
//            AuthVO resVO = (AuthVO)res.getEntity();
//            String msg = res.readEntity(String.class);
            logger.debug("status = "+status);
//            logger.debug("msg = "+result);
        } catch (Exception e) {
            // ignore;
            logger.debug("login exception :\n", e);
        }
        
//        return "?faces-redirect=true";
        return "";
    }
    
    public void apply() {
        logger.debug("apply = "+formVO.getApplyVenderCode());
        try{
            if(StringUtils.isBlank(formVO.getApplyVenderCode()) || StringUtils.isBlank(formVO.getApplyVenderName())){
                JsfUtils.addErrorMessage("無供應商資訊");
                return;
            }
            HttpServletRequest request = JsfUtils.getRequest();
            Response res = venderREST.apply(request, formVO);
            logger.debug("res = "+res);
            
            int status = res.getStatus();
            logger.debug("status = "+status);
            if(status == 200){
                String msg = res.readEntity(String.class);
                logger.debug("msg = "+msg);
                JsfUtils.addSuccessMessage(String.format("已提交申請!", formVO.getId()));
            }else{
                JsfUtils.addErrorMessage(String.format("申請失敗!", formVO.getId()));
            }
        } catch (Exception e) {
            // ignore;
            logger.debug("apply exception :\n", e);
        }
    }
    
    public EtMember getMember() {
        return member;
    }

    public String getTest() {
        return test;
    }

    public MemberVO getMemberVO() {
        return memberVO;
    }

    public SubmitVO getFormVO() {
        return formVO;
    }

    public void setFormVO(SubmitVO formVO) {
        this.formVO = formVO;
    }

}

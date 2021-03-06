/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.MemberVenderVO;
import com.tcci.et.entity.EtVcForm;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.facade.EtVcFormFacade;
import com.tcci.fc.controller.util.AttachmentBean;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.bpm.ProcessActivityVO;
import com.tcci.fc.facade.content.ContentFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "vcFormSign")
@ViewScoped
public class VcFormSignController extends SessionAwareController implements Serializable {
    protected AttachmentBean attachmentBean;
    @EJB
    protected ContentFacade contentFacade;
    @EJB UserFacade userFacade;
    @EJB
    protected EtVcFormFacade formFacade;
    @Inject
    protected IBPMEngine bpmEngine;
    
    protected EtVcForm form;
    private String comment;
    private MemberVenderVO formVO;
    protected List<ProcessActivityVO> activities;
    protected int activeIndex; // tabview:
    private boolean initSuccess;
    private TcUser reassignUser;
    private String reassignComment;
    protected List<TcUser> allUsers;
    
    private TcWorkitem workitem;
    private Set<String> signedAccounts = new HashSet<>();
    
    @PostConstruct
    private void init(){
        try {
            // Get view Id
            viewId = null;//不檢查
            
            initValidate();
            loadForm();
            FormStatusEnum status = form.getStatus();
            Long wid = getLongParam("workitem");
            workitem = bpmEngine.findWorkitem(wid);
            if (null == workitem) {
                throw new Exception("無此簽核項目(" + wid + ")!");
            }
            if (!workitem.getOwner().equals(this.getLoginUser())) {
                throw new Exception("非此簽核項目(" + wid + ")簽核人!");
            }
            if (FormStatusEnum.SIGNING != status) {
                throw new Exception("申請單(" + form.getId() + ")並非在簽核中!");
            }
            if (ExecutionStateEnum.RUNNING != workitem.getExecutionstate()) {
                throw new Exception("簽核項目(" + workitem.getActivityname() + ")並非在簽核中!");
            }
            if (FormStatusEnum.SIGNING == status) {
                findSignedAccount(activities);
            }
            attachmentBean.setReadOnly(false); // 允許簽核人上傳附件
            initSuccess = true;
        } catch (Exception ex) {
            logger.error("init excetion!", ex);
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    protected void initValidate() throws Exception {
        if(this.getLoginUser()==null){
            throw new Exception("無登入者資料，請聯絡系統管理員!");
        }
        attachmentBean = new AttachmentBean(contentFacade).readOnly(true);
    }
    
    protected static Long getLongParam(String key) throws Exception {
        String value = JsfUtils.getRequestParameter(key);
        if (value == null) {
            throw new Exception(key + "參數不得為空!");
        }
        try {
            return Long.parseLong(value);
        } catch (Exception ex) {
            throw new Exception(key + "參數必需為數值!");
        }
    }
    
    protected void loadForm() throws Exception {
        Long id = getLongParam("id");
        form = formFacade.find(id);
        if (null == form) {
            throw new Exception("無此申請單(" + id + ")!");
        }
        formVO = formFacade.findById(id, Boolean.TRUE);
        logger.info("loadForm ProcessId:"+formVO.getProcessId());
        if (form.getProcess() != null) {
            //排除沒有簽核人員的關卡
            List<ProcessActivityVO> list = new ArrayList<>();
            List<ProcessActivityVO> result = bpmEngine.findProcessActivitiesFlow(form.getProcess(), true);
            if (CollectionUtils.isNotEmpty(result)) {
                logger.info("loadForm activities:"+result.size());
                for(ProcessActivityVO processActivityVO:result){
                    if (CollectionUtils.isNotEmpty(processActivityVO.getOwner())) {
                        list.add(processActivityVO);
                    }
                }
            }
            activities = list;
            logger.info("loadForm activities:"+activities.size());
        }
        FormStatusEnum status = form.getStatus();
        if (activities != null && !activities.isEmpty()) {
            activeIndex = 1;
        } else {
            activeIndex = 0;
        }
    }
    
    private void findSignedAccount(List<ProcessActivityVO> list) {
        if (list != null && !list.isEmpty()) {
            for (ProcessActivityVO vo : list) {
                TcWorkitem w = vo.getWorkitem();
                if (w != null && (ExecutionStateEnum.COMPLETED == w.getExecutionstate() || ExecutionStateEnum.RUNNING == w.getExecutionstate())) {
                    signedAccounts.add(w.getOwner().getLoginAccount());
                }
            }
        }
    }
    
    // action
    public void actionApprove() {
        try {
            verifyStatus();
            contentFacade.saveContent(form, attachmentBean.getAttachmentList(form), this.getLoginUser());
            approve(workitem, comment, form.getId());
            JsfUtils.addSuccessMessage(String.format("申請單(%d)已核准!", form.getId()));
//            redirect("formView.xhtml?id=" + form.getId());
            redirect("formTodo.xhtml");
        } catch (Exception ex) {
            logger.error("actionApprove exception, form id:{}", form.getId(), ex);
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    public void actionReject() {
        try {
            verifyStatus();
            form.setStatus(FormStatusEnum.REJECT);
            formFacade.save(form, this.getLoginUser(), Boolean.FALSE);
            contentFacade.saveContent(form, attachmentBean.getAttachmentList(form), this.getLoginUser());
            reject(workitem, comment, form.getId());
            JsfUtils.addSuccessMessage(String.format("申請差單(%d)已駁回!", form.getId()));
//            redirect("formView.xhtml?id=" + form.getId());
            redirect("formTodo.xhtml");
        } catch (Exception ex) {
            logger.error("actionReject exception, form id:{}", form.getId(), ex);
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    public void actionReassign() {
        try {
            if (null == reassignUser) {
                JsfUtils.addErrorMessage("未選擇改派人員!");
                return;
            }
            if (reassignComment != null && reassignComment.length()>200) {
                throw new Exception("備註勿超過200字!");
            }
            verifyStatus();
            contentFacade.saveContent(form, attachmentBean.getAttachmentList(form), this.getLoginUser());
            reassign(workitem, reassignUser, reassignComment);
            JsfUtils.addSuccessMessage(String.format("申請單(%d)已改派!", form.getId()));
//            redirect("formView.xhtml?id=" + form.getId());
            redirect("formTodo.xhtml");
        } catch (Exception ex) {
            logger.error("actionReassign exception, form id:{}", form.getId(), ex);
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    public void actionClose() {
        redirect("formTodo.xhtml");
    }
    
    protected void verifyStatus() throws Exception {
        if (isStatusChanged()) {
            throw new Exception(String.format("申請單(%d)狀態已變更，請重新整理再試!", form.getId()));
        }
    }
    
    protected boolean isStatusChanged() {
        return form.getId() == null ? false : formFacade.isStatusChanged(form);
    }
    
    public void reject(TcWorkitem workitem, String comment, Long formId) throws Exception {
        if (StringUtils.isBlank(comment)) {
            throw new Exception("駁回需填簽核意見!");
        }
        if (comment.length()>200) {
            throw new Exception("簽核意見勿超過200字!");
        }
        if (!workitem.getActivityid().getProcessid().getPrimaryobjectid().equals(formId)) {
            throw new Exception("資料不正確!");
        }
        bpmEngine.completeWorkitem(workitem, "reject", "reject", comment, this.getLoginUser(), true);
    }
    
    public void approve(TcWorkitem workitem, String comment, Long formId) throws Exception {
        if (comment != null && comment.length()>200) {
            throw new Exception("簽核意見勿超過200字!");
        }
        if (!workitem.getActivityid().getProcessid().getPrimaryobjectid().equals(formId)) {
            throw new Exception("");
        }
        bpmEngine.completeWorkitem(workitem, "approve", "approve", comment, this.getLoginUser(), true);
    }
    
    public void reassign(TcWorkitem workitem, TcUser newOwner, String comment) {
        bpmEngine.reassign(workitem, newOwner, comment, workitem.getOwner(), true);
    }
    
    public List<TcUser> completeActiveUser(String query) {
        return completeUser(query, true, null);
    }
    
    public List<TcUser> completeUser(String query, boolean activeOnly, Set<String> skipAccounts) {
        if (null == allUsers) {
            allUsers = activeOnly ? userFacade.findAllActiveUser() : userFacade.findAll();
        }
        List<TcUser> result = new ArrayList<>();
        String tmp = StringUtils.trimToNull(query);
        int count = 0;
        for (TcUser u : allUsers) {
            if (skipAccounts != null && skipAccounts.contains(u.getLoginAccount())) {
                continue;
            }
            if (null == tmp
                    || StringUtils.containsIgnoreCase(u.getLoginAccount(), tmp)
                    || StringUtils.containsIgnoreCase(u.getCname(), tmp)) {
                result.add(u);
                count++;
                if (count >= GlobalConstant.COMPLETE_USER_MAX) {
                    break;
                }
            }
        }
        return result;
    }
    
    // helper
    public String getPageTitle() {
        return null == form ? "簽核申請單"
                : null == form.getId() ? "簽核申請單" : "簽核申請單(" + form.getId() + ")";
    }
    
    public int getAttachmentSize() {
        return null == form ? 0 : attachmentBean.getAttachmentList(form).size();
    }
    
    public String getFormStatus() {
        try {
            return null == form ? null : msgApp.getString("status." + form.getStatus().toString());
        } catch (Exception ex) {
            return form.getStatus().toString();
        }
    }
    
    // getter, setter
    public EtVcForm getForm() {
        return form;
    }

    public MemberVenderVO getFormVO() {
        return formVO;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public boolean isInitSuccess() {
        return initSuccess;
    }
    
    public List<ProcessActivityVO> getActivities() {
        return activities;
    }

    public AttachmentBean getAttachmentBean() {
        return attachmentBean;
    }

    public void setAttachmentBean(AttachmentBean attachmentBean) {
        this.attachmentBean = attachmentBean;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TcUser getReassignUser() {
        return reassignUser;
    }

    public void setReassignUser(TcUser reassignUser) {
        this.reassignUser = reassignUser;
    }

    public String getReassignComment() {
        return reassignComment;
    }

    public void setReassignComment(String reassignComment) {
        this.reassignComment = reassignComment;
    }
    
}

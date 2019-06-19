/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.entity.EcOption;
import com.tcci.ec.facade.EcOptionFacade;
import com.tcci.ec.model.VenderVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.rs.LongOptionVO;
import static com.tcci.et.controller.VcFormQueryController.FUNC_OPTION;
import com.tcci.et.entity.EtVcForm;
import com.tcci.et.enums.BpmRoleEnum;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.facade.EtVcFormFacade;
import com.tcci.et.facade.EtVenderCategoryFacade;
import com.tcci.fc.controller.util.AttachmentBean;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "vcFormEdit")
@ViewScoped
public class VcFormEditController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 58;
    
    protected AttachmentBean attachmentBean;
    @EJB
    protected ContentFacade contentFacade;
    @EJB UserFacade userFacade;
    @EJB
    protected EtVcFormFacade formFacade;
    @Inject
    protected IBPMEngine bpmEngine;
    @EJB private EtVenderCategoryFacade etVenderCategoryFacade;
    @EJB private EcOptionFacade ecOptionFacade;
    
    private VenderVO editVO;
    private List<LongOptionVO> categoryOptionList;
    private List<Long> selectedCategorys;
    private Map<String, Object> roleApprovers;//簽核人員
    
    private boolean initSuccess;
    protected int activeIndex; // tabview:
    protected EtVcForm form;
    
    @PostConstruct
    private void init(){
//        if( functionDenied ){ return; }
        viewId = null;//不檢查
        try {
            initValidate();
            loadForm();
            
            categoryOptionList = new ArrayList<>();
            fetchCategoryOptionList();
            
            attachmentBean.setReadOnly(false); // 允許簽核人上傳附件
            initSuccess = true;
        } catch (Exception ex) {
            logger.error("init excetion!", ex);
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    protected void initValidate() throws Exception {
        if(this.getLoginUser()==null){
//        if (!userSession.isValid()) {
            throw new Exception("無登入者資料，請聯絡系統管理員!");
        }
        attachmentBean = new AttachmentBean(contentFacade).readOnly(true);
    }
    
    protected void loadForm() throws Exception {
        RequestContext context;
        String mandt = JsfUtils.getRequestParameter("mandt");
        String code = JsfUtils.getRequestParameter("code");
        if (mandt == null || code == null) {
            throw new Exception("參數不得為空!");
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setType(mandt);
        criteriaVO.setCode(code);
        List<VenderVO> resList = etVenderCategoryFacade.findByCriteria(criteriaVO);
        if (CollectionUtils.isNotEmpty(resList)) {
            editVO = resList.get(0);
        }else{
//            context = JsfUtils.buildErrorCallback();
//            context.addCallbackParam("msg", "供應商參數有誤!");
//            return;
            throw new Exception("供應商參數有誤!");
        }
        boolean block = formFacade.findRunningByVenderVO(editVO);
        if(block){
//            context = JsfUtils.buildErrorCallback();
//            context.addCallbackParam("msg", "該供應商已有申請審核中!");
//            return;
            throw new Exception("該供應商已有申請審核中!");
        }
        
        //申請廠別
        BaseCriteriaVO criteriaVO2 = new BaseCriteriaVO();
        criteriaVO2.setDisabled(Boolean.FALSE);//排除黑名單
        criteriaVO2.setType(editVO.getMandt());
        criteriaVO2.setCode(editVO.getVenderCode());
        CmFactory factory = etVenderCategoryFacade.findApplyFactory(criteriaVO2);
        if(factory==null){
//            context = JsfUtils.buildErrorCallback();
//            context.addCallbackParam("msg", "查無簽核人員!");
//            return;
            throw new Exception("查無簽核人員!");
        }
        
        Long factoryId = factory.getId();
        roleApprovers = reloadApprovers(factory);//size 2
        if(roleApprovers.isEmpty()){
//            context = JsfUtils.buildErrorCallback();
//            context.addCallbackParam("msg", "廠別("+factory.getCode()+") 查無簽核人員!");
//            return;
            throw new Exception("廠別("+factory.getCode()+") 查無簽核人員!");
        }
        
        editVO.setApplicantName(this.getLoginUser().getCname());
        editVO.setApplicantAd(this.getLoginUser().getLoginAccount());
        editVO.setFactoryName(factory.getName());
        
        form = new EtVcForm();
        form.setFactoryId(factoryId);
        form.setMandt(editVO.getMandt());
        form.setVenderCode(editVO.getVenderCode());
        form.setCname(editVO.getCname());
//                form.setCids(cids);
//                form.setCnames(cnames);
        form.setStatus(FormStatusEnum.SIGNING);

    }
    
    //簽核人員 1:HZ_MM or HQ_MM
    public Map<String, Object> reloadApprovers(CmFactory factory){
        Map<String, Object> roleApprovers = new HashMap<>();
        boolean isHZ = cmFactorygroupFacade.isSubFactory("1", "HZCN", factory.getCode());
        List<TcUser> approvers = new ArrayList<>();
        if(isHZ){
            approvers = permissionFacade.findUsersByRole(BpmRoleEnum.HZ_MM.name());
            if(CollectionUtils.isNotEmpty(approvers)){
                roleApprovers.put(BpmRoleEnum.HZ_MM.name(), approvers);
            }
        }else{//TCDCN
            approvers = permissionFacade.findUsersByRole(BpmRoleEnum.HQ_MM.name());
            if(CollectionUtils.isNotEmpty(approvers)){
                roleApprovers.put(BpmRoleEnum.HQ_MM.name(), approvers);
            }
        }
        
        return roleApprovers;
    }
    
    /**
     * 已選取 分類
     * @return 
     */
    public List<LongOptionVO> getSelectedCategoryList(){
        if( categoryOptionList==null ){
            return null;
        }
        List<LongOptionVO> selectedList = new ArrayList<>();
        for(LongOptionVO vo:categoryOptionList){
            if( selectedCategorys!=null && selectedCategorys.contains(vo.getValue()) ){
                selectedList.add(vo);
            }
        }
        return selectedList;
    }
    
    /**
     * 分類下拉選單
     */
    public void fetchCategoryOptionList(){
        categoryOptionList = new ArrayList<>();
        categoryOptionList = ecOptionFacade.findByTypeOptions("tenderCategory", "C");
        prepareSelectedCategoryInfo(editVO);
    }
    
    /**
     * 物料群組選取狀態
     * @param vo
     */
    public void prepareSelectedCategoryInfo(VenderVO vo){
        selectedCategorys = new ArrayList<>();
        if(StringUtils.isNotBlank(vo.getCids())){
            String[] ids = vo.getCids().split(",");
            for(String idStr:ids){
                selectedCategorys.add(Long.parseLong(idStr));
            }
        }
     }
    
    /**
     * 提交申請 
     */
    public void apply() {
        logger.info("=== apply === ");
        RequestContext context;
        try{
            String cids = "";
            String cnames = "";
            if (CollectionUtils.isNotEmpty(selectedCategorys)) {
                logger.info("=== apply === selectedCategorys:"+selectedCategorys.size());
                cids = StringUtils.longlistToString(selectedCategorys,",");
                StringBuilder sb = new StringBuilder();
                boolean exists = false;
                for(Long cid : selectedCategorys){
                    EcOption ecOption = ecOptionFacade.find(cid);
                    if( ecOption!=null ){
                        if( exists ){
                            sb.append(",").append(ecOption.getCname());
                        }else{
                            sb.append(ecOption.getCname());
                        }
                        exists = true;
                    }
//                    return sb.toString();
                }
                cnames = sb.toString();
            }
            //有異動
            if(!cids.equals(editVO.getCids())){
                form.setCids(cids);
                form.setCnames(cnames);
                
                formFacade.save(form, this.getLoginUser(), Boolean.FALSE);
                logger.info("apply... vcformID = " + form.getId());
                contentFacade.saveContent(form, attachmentBean.getAttachmentList(form), this.getLoginUser());
                formFacade.startProcess(form, this.getLoginUser(), roleApprovers);
            }else{
                context = JsfUtils.buildErrorCallback();
                context.addCallbackParam("msg", "供應商類別無異動 申請未提交!");
                return;
            }
            
            context = JsfUtils.buildErrorCallback();
            context.addCallbackParam("msg", "已提交申請!");
            
            redirect("formQuery.xhtml");
        }catch(Exception e){
            logger.error("apply Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
    
    public void actionClose() {
        redirect("formQuery.xhtml");
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }
    
    public int getAttachmentSize() {
        return null == form ? 0 : attachmentBean.getAttachmentList(form).size();
    }
    
    // getter, setter

    public VenderVO getEditVO() {
        return editVO;
    }

    public void setEditVO(VenderVO editVO) {
        this.editVO = editVO;
    }
    
    public EtVcForm getForm() {
        return form;
    }

    public boolean isInitSuccess() {
        return initSuccess;
    }
    
    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }
    
    public AttachmentBean getAttachmentBean() {
        return attachmentBean;
    }

    public void setAttachmentBean(AttachmentBean attachmentBean) {
        this.attachmentBean = attachmentBean;
    }
    
    public List<LongOptionVO> getCategoryOptionList() {
        return categoryOptionList;
    }

    public List<Long> getSelectedCategorys() {
        return selectedCategorys;
    }

    public void setSelectedCategorys(List<Long> selectedCategorys) {
        this.selectedCategorys = selectedCategorys;
    }
    
    
}

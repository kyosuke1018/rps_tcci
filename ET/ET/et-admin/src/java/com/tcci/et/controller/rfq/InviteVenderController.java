/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.rfq;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.entity.EtRfqVender;
import com.tcci.et.enums.NoticeTypeEnum;
import com.tcci.et.enums.TenderMethodEnum;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.et.facade.EtMemberFormFacade;
import com.tcci.et.facade.EtNoticeLogFacade;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.EtVenderFacade;
import com.tcci.et.facade.EtMemberVenderFacade;
import com.tcci.et.facade.rfq.EtRfqEkkoFacade;
import com.tcci.et.facade.rfq.EtRfqVenderFacade;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.NoticeVO;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.MemberVenderVO;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.criteria.VenderCriteriaVO;
import com.tcci.et.model.rfq.RfqEkkoVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "inviteVenderController")
@ViewScoped
public class InviteVenderController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 23;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private EtTenderFacade etTenderFacade;
    @EJB private EtVenderFacade etVenderFacade;
    @EJB private EtRfqEkkoFacade etRfqEkkoFacade;
    @EJB private EtNoticeLogFacade etNoticeLogFacade;
    @EJB private EtMemberVenderFacade etMemberVenderFacade;
    @EJB private EtMemberFormFacade etMemberFormFacade;
    @EJB private EtRfqVenderFacade etRfqVenderFacade;
    
    // 查詢條件
//    private BaseCriteriaVO criteriaVO;
    
    private boolean isAdmin;
    private TenderVO tenderVO;
    private List<TenderVO> allTenderList; // 可選取全部案件 
    private String selectedTenderTxt;
    private List<VenderAllVO> allVenderList; // 可選取全部供應商
    private List<Long> selectedVenders;
    private List<VenderAllVO> selectedVenderList;
    private RfqEkkoVO rfqVO;
    
    
    @PostConstruct
    private void init() {
        tenderVO = new TenderVO();
        allVenderList = new ArrayList<>();
        
        getInputParams();// 取得輸入參數

        isAdmin = sessionController.isUserInRole("ADMINISTRATORS");
        
        List<String> types = new ArrayList<>();
        types.add(TenderMethodEnum.PRIVATE.getCode());
        types.add(TenderMethodEnum.COMBI.getCode());
        List<String> statusList = new ArrayList<>();
        statusList.add(TenderStatusEnum.ON_TENDER.getCode());
        TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
        criteriaVO.setTypes(types);
//        criteriaVO.setStatusList(statusList);
        criteriaVO.setOrderBy("S.ID DESC");
        allTenderList = etTenderFacade.findByCriteria(criteriaVO);
        
    }
    
    /**
     * 取得輸入參數
     */
    private void getInputParams() {
        String idStr = JsfUtils.getRequestParameter("tenderId");
        if (idStr != null) {
            try {
                Long tenderId = Long.parseLong(idStr);
//                criteriaVO.setTenderId(tenderId);
                
                tenderVO = etTenderFacade.findById(tenderId, false);
                if(tenderVO!=null){
                    selectedTenderTxt = etTenderFacade.getTenderDisplayIdentifier(tenderVO);
                    
                    //rfq
                    rfqVO = etRfqEkkoFacade.findByTenderId(tenderVO.getId());
                    
                    initVenderOption();
                }
            } catch (Exception e) {
                logger.error("getInputParams exception:\n", e);
            }
        }
    }
    
    private void initVenderOption(){
        logger.info("initVenderOption CategoryId:"+tenderVO.getCategoryId());
        if(tenderVO.getCategoryId() == null){
            allVenderList = new ArrayList<>();
            return;
        }
        VenderCriteriaVO criteriaVO = new VenderCriteriaVO();
        criteriaVO.setCategoryId(tenderVO.getCategoryId());
        
        allVenderList = etVenderFacade.findByCriteria(criteriaVO);
//        if (CollectionUtils.isNotEmpty(allVenderList)) {
//            logger.info("initVenderOption CategoryId:"+allVenderList.size());
//        }
        
    }
    
    /**
     * 選取 autoComplete 案件列表
     * @param intxt
     * @return 
     */
    public List<String> autoCompleteTenderOptions(String intxt){
        List<String> resList = new ArrayList<String>();
        
        for(TenderVO tender : allTenderList){// 有權選取的所有User
            String txt = etTenderFacade.getTenderDisplayIdentifier(tender);
            if( txt.toUpperCase().indexOf(intxt.toUpperCase()) >= 0 ){// 符合輸入
                resList.add(txt);
            }
        }
        
        return resList;
    }
    
//    private RfqEkkoVO findRfqByTenderId() {
//        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
//        criteriaVO.setTenderId(tenderVO.getId());
//        
//        List<RfqEkkoVO> list = etRfqEkkoFacade.findByCriteria(criteriaVO);
//        return (list!=null && !list.isEmpty())? list.get(0):null;
//    }
    
    public void conformTender(){
        logger.info("conform :"+selectedTenderTxt);
        try{
            tenderVO = etTenderFacade.findTenderByTxt(allTenderList, selectedTenderTxt);
            if(tenderVO!=null){
                //rfq
                rfqVO = etRfqEkkoFacade.findByTenderId(tenderVO.getId());

                initVenderOption();
            }
        } catch (Exception e) {
            logger.error("conformTender exception:\n", e);
        }
    }
    
    public void conformVender(){
        logger.info("conformVender :"+selectedVenders);
        selectedVenderList = new ArrayList<>();
        for(Long id:selectedVenders){
            VenderAllVO vo = etVenderFacade.findById(id, false);
            selectedVenderList.add(vo);
        }
        
    }
    
    public void notice(){
        logger.info("notice :"+selectedVenders);
        NoticeTypeEnum type = NoticeTypeEnum.INVITE;
        if(rfqVO==null){
            JsfUtils.addErrorMessage("此標案尚未建立對應的詢價單!");
            return;
        }
        
        try {
            if (CollectionUtils.isNotEmpty(selectedVenderList)) {
                RfqCriteriaVO rfqCriteriaVO = new RfqCriteriaVO();
                rfqCriteriaVO.setTenderId(tenderVO.getId());
                
                for(VenderAllVO venderAllVO:selectedVenderList){
                    if(venderAllVO.getApplyId()==null){
                        rfqCriteriaVO.setVenderId(venderAllVO.getId());
                        //供應商對多個會員
                        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
                        criteriaVO.setType(venderAllVO.getMandt());
                        criteriaVO.setCode(venderAllVO.getLifnrUi());
                        List<MemberVenderVO> list = etMemberVenderFacade.findByCriteria(criteriaVO);
                        if (CollectionUtils.isNotEmpty(list)) {
                            for(MemberVenderVO venderVO:list){
                                String email = venderVO.getLoginAccount();
                                //TODO ADD RfqVender
                                //TODO link url
                                //notice log
                                NoticeVO noticeVO = new NoticeVO();
                                noticeVO.setEmail(email);
                                noticeVO.setType(type.getCode());
                                noticeVO.setMemberId(venderVO.getMainId());
                                noticeVO.setVenderId(venderAllVO.getId());
                                noticeVO.setTenderId(tenderVO.getId());
                                //邀標 以投標日期為報價期間
                                String dateFromTo = tenderVO.getTenderDateStr();
                                noticeVO.setMemo("報價期間:"+dateFromTo);
                                
                                etNoticeLogFacade.quotationNotice(noticeVO, this.getLoginUser());
                            }
                            //new EtRfqVender
                            if (CollectionUtils.isNotEmpty(etRfqVenderFacade.findByCriteria(rfqCriteriaVO))) {//已存在
//                            }else{
                            }else if(rfqVO!=null){
                                EtRfqVender entity = new EtRfqVender();
                                entity.setTenderId(tenderVO.getId());
                                entity.setRfqId(rfqVO.getId());
                                entity.setVenderId(venderAllVO.getId());
                                entity.setMandt(venderAllVO.getMandt());
                                entity.setEbeln(rfqVO.getRfqNo());
                                etRfqVenderFacade.save(entity, this.getLoginUser(), Boolean.FALSE);
                            }
                            
                        }else{//供應商 無會員綁定
                            //log
                            NoticeVO noticeVO = new NoticeVO();
                            noticeVO.setType(type.getCode());
                            noticeVO.setVenderId(venderAllVO.getId());
                            noticeVO.setTenderId(tenderVO.getId());
                            noticeVO.setMemo("供應商 ["+venderAllVO.getMandt()+"_"+venderAllVO.getLifnrUi()+"] 無會員綁定!");
                            noticeVO.setResult(false);
                            etNoticeLogFacade.saveVO(noticeVO, this.getLoginUser(), false);
                        }
                    }else{
                        //新商申請對單一會員
                        MemberVO memberVO = etMemberFormFacade.findById(venderAllVO.getApplyId(), false, locale);
                        String email = memberVO.getEmail();
                        //TODO ADD RfqVender
                        //TODO link url
                        //notice log
                        NoticeVO noticeVO = new NoticeVO();
                        noticeVO.setEmail(email);
                        noticeVO.setType(type.getCode());
                        noticeVO.setMemberId(memberVO.getMemberId());
                        noticeVO.setVenderId(venderAllVO.getId());
                        noticeVO.setTenderId(tenderVO.getId());
                        //邀標 以投標日期為報價期間
                        String dateFromTo = tenderVO.getTenderDateStr();
                        noticeVO.setMemo("報價期間:"+tenderVO.getTenderDateStr());
                        etNoticeLogFacade.quotationNotice(noticeVO, this.getLoginUser());
                        
                        //new EtRfqVender
                        if (CollectionUtils.isNotEmpty(etRfqVenderFacade.findByCriteria(rfqCriteriaVO))) {//已存在
//                      }else{
                        }else if(rfqVO!=null){
                            EtRfqVender entity = new EtRfqVender();
                            entity.setTenderId(tenderVO.getId());
                            entity.setRfqId(rfqVO.getId());
                            entity.setVenderId(venderAllVO.getId());
                            entity.setMandt(venderAllVO.getMandt());
                            entity.setEbeln(rfqVO.getRfqNo());
                            etRfqVenderFacade.save(entity, this.getLoginUser(), Boolean.FALSE);
                        }
                    }
                }
            }else{
                JsfUtils.addErrorMessage("請選擇供應商!");
                return;
            }
            
            JsfUtils.addSuccessMessage("通知已寄出");
//            redirect("quotationMgn.xhtml?tenderId="+tenderVO.getId());
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "notice", e, false);
        }
    }
    
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    } 
    
    
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">

    public String getSelectedTenderTxt() {
        return selectedTenderTxt;
    }

    public void setSelectedTenderTxt(String selectedTenderTxt) {
        this.selectedTenderTxt = selectedTenderTxt;
    }

    public List<VenderAllVO> getAllVenderList() {
        return allVenderList;
    }
    
    public List<Long> getSelectedVenders() {
        return selectedVenders;
    }

    public void setSelectedVenders(List<Long> selectedVenders) {
        this.selectedVenders = selectedVenders;
    }
    
    public List<VenderAllVO> getSelectedVenderList() {
        return selectedVenderList;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }
    //</editor-fold>
    
}

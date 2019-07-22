/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.rfq;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.exception.CmCustomException;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.entity.EtBargain;
import com.tcci.et.enums.NoticeTypeEnum;
import com.tcci.et.enums.TenderMethodEnum;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.et.facade.EtBargainFacade;
import com.tcci.et.facade.EtMemberFormFacade;
import com.tcci.et.facade.EtNoticeLogFacade;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.EtVenderAllFacade;
import com.tcci.et.facade.EtVenderFacade;
import com.tcci.et.facade.rfq.EtRfqEkkoFacade;
import com.tcci.et.facade.rfq.EtRfqVenderFacade;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.NoticeVO;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.VenderAllVO;
import com.tcci.et.model.VenderVO;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.rfq.RfqEkkoVO;
import com.tcci.et.model.rfq.RfqVenderVO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
@ManagedBean(name = "quotationMgnController")
@ViewScoped
public class QuotationMgnController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 25;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private EtTenderFacade etTenderFacade;
    @EJB private EtVenderAllFacade etVenderAllFacade;
    @EJB private EtRfqEkkoFacade etRfqEkkoFacade;
    @EJB private EtRfqVenderFacade etRfqVenderFacade;
    @EJB private EtNoticeLogFacade etNoticeLogFacade;
    @EJB private EtVenderFacade etVenderFacade;
    @EJB private EtMemberFormFacade etMemberFormFacade;
    @EJB private EtBargainFacade etBargainFacade;
    
    private boolean isAdmin;
    private TenderVO tenderVO;
    private List<TenderVO> allTenderList; // 可選取全部案件 
    private String selectedTenderTxt;
    private RfqEkkoVO rfqVO;
    private List<RfqVenderVO> rfqVenderList;
    private List<VenderAllVO> allVenderList; // 可選取全部供應商
    private List<Long> selectedVenders;
    private List<VenderAllVO> selectedVenderList;
    private EtBargain lastBargain;
    private EtBargain editBargain;
    
    @PostConstruct
    private void init() {
        tenderVO = new TenderVO();
        
        getInputParams();// 取得輸入參數
        
        isAdmin = sessionController.isUserInRole("ADMINISTRATORS");
        
        List<String> types = new ArrayList<>();
        types.add(TenderMethodEnum.PRIVATE.getCode());
        types.add(TenderMethodEnum.COMBI.getCode());
        List<String> statusList = new ArrayList<>();
        statusList.add(TenderStatusEnum.ON_TENDER.getCode());
        TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
//        criteriaVO.setTypes(types);
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
                    
                    initBargain();//議價紀錄
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
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderVO.getId());
//        criteriaVO.setRfqId(rfqVO.getId());

        rfqVenderList = etRfqVenderFacade.findByCriteria(criteriaVO);
        allVenderList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rfqVenderList)) {
            for(RfqVenderVO rfqVenderVO:rfqVenderList){
                VenderAllVO vo = etVenderAllFacade.findById(rfqVenderVO.getVenderId(), false);
                allVenderList.add(vo);
                
                //TODO find quotation
                
            }
        }
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
    
    public void conformTender(){
        logger.info("conform :"+selectedTenderTxt);
        tenderVO = etTenderFacade.findTenderByTxt(allTenderList, selectedTenderTxt);
        if(tenderVO!=null){
            //rfq
            rfqVO = etRfqEkkoFacade.findByTenderId(tenderVO.getId());
            
            initBargain();//議價紀錄
            initVenderOption();
        }
    }
    
    public void conformVender(){
        logger.info("conformVender :"+selectedVenders);
        selectedVenderList = new ArrayList<>();
        for(Long id:selectedVenders){
            VenderAllVO vo = etVenderAllFacade.findById(id, false);
            selectedVenderList.add(vo);
        }
        
    }
    
    public void notice(){
        logger.info("notice :"+selectedVenders);
        NoticeTypeEnum type = NoticeTypeEnum.QUOTE;
        if(rfqVO==null){
            JsfUtils.addErrorMessage("此標案尚未建立對應的詢價單!");
            return;
        }
        if(editBargain==null || editBargain.getSdate()==null || editBargain.getEdate()==null
                ||  editBargain.getSdate().compareTo(editBargain.getEdate()) > 0){
            JsfUtils.addErrorMessage("輸入[報價期間]錯誤!(起日需小於等於迄日)");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstant.FORMAT_DATE);
            String dateFromTo = sdf.format(editBargain.getSdate())+"~"+sdf.format(editBargain.getEdate());
            if (CollectionUtils.isNotEmpty(selectedVenderList)) {
                for(VenderAllVO venderAllVO:selectedVenderList){
                    if(venderAllVO.getApplyId()==null){
                        //供應商對多個會員
                        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
                        criteriaVO.setType(venderAllVO.getMandt());
                        criteriaVO.setCode(venderAllVO.getLifnrUi());
                        List<VenderVO> list = etVenderFacade.findByCriteria(criteriaVO);
                        if (CollectionUtils.isNotEmpty(list)) {
                            for(VenderVO venderVO:list){
                                String email = venderVO.getLoginAccount();
                                //TODO link url
                                //notice log
                                NoticeVO noticeVO = new NoticeVO();
                                noticeVO.setType(type.getCode());
                                noticeVO.setMemberId(venderVO.getMainId());
                                noticeVO.setVenderId(venderAllVO.getId());
                                noticeVO.setTenderId(tenderVO.getId());
                                //議價 輸入報價期間
                                noticeVO.setMemo("報價期間:"+dateFromTo);
                                etNoticeLogFacade.quotationNotice(noticeVO, this.getLoginUser());
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
                        //TODO link url
                        //notice log
                        NoticeVO noticeVO = new NoticeVO();
                        noticeVO.setType(type.getCode());
                        noticeVO.setMemberId(memberVO.getMemberId());
                        noticeVO.setVenderId(venderAllVO.getId());
                        noticeVO.setTenderId(tenderVO.getId());
                        //議價 輸入報價期間
                        noticeVO.setMemo("報價期間:"+dateFromTo);
                        etNoticeLogFacade.quotationNotice(noticeVO, this.getLoginUser());
                    }
                }
            }else{
                JsfUtils.addErrorMessage("請選擇供應商!");
                return;
            }
            //add ET_BARGAIN
            etBargainFacade.save(editBargain, this.getLoginUser(), false);
            
            initBargain();//reload 議價紀錄
            
            JsfUtils.addSuccessMessage("通知已寄出");
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "notice", e, false);
        }
    }
    
    private void initBargain(){
        if(rfqVO==null){
            lastBargain = null;
            editBargain = null;
            return;
        }
        //EtBargain
        List<EtBargain> list = etBargainFacade.findByTenderId(tenderVO.getId());
        if(list!=null && !list.isEmpty()){
            lastBargain = list.get(0);
            editBargain = new EtBargain();
            editBargain.setTenderId(tenderVO.getId());
            editBargain.setRfqId(rfqVO.getId());
            editBargain.setTimes(lastBargain.getTimes()+1);
        }else{
            lastBargain = null;
            editBargain = new EtBargain();
            editBargain.setTenderId(tenderVO.getId());
            editBargain.setRfqId(rfqVO.getId());
            editBargain.setTimes(1);
        }
    }
    
    /**
     * 可否邀標
     * @return
     */
    public boolean canInviteVender() {
        if(tenderVO!=null){
            if(TenderMethodEnum.OPEN.getCode().equals(tenderVO.getType())){
                return false;
            }
            //可邀標的狀態 待定義
            if(TenderStatusEnum.VERIFY.getCode().equals(tenderVO.getType())
                    || TenderStatusEnum.END.getCode().equals(tenderVO.getType())){
                return false;
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * 可否再次報價
     * @return
     */
    public boolean canNotice() {
        if(tenderVO!=null){
            //可再次報價的狀態 待定義
            if(TenderStatusEnum.VERIFY.getCode().equals(tenderVO.getType())
                    || TenderStatusEnum.END.getCode().equals(tenderVO.getType())){
                return false;
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * link 邀標
     *
     * @param vo
     */
    public void redirectInviteVender() {
        logger.debug("redirectInviteVender ...");
        try {
            redirect("inviteVender.xhtml?tenderId="+tenderVO.getId());
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "redirectRfq", e, false);
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

    public List<Long> getSelectedVenders() {
        return selectedVenders;
    }

    public void setSelectedVenders(List<Long> selectedVenders) {
        this.selectedVenders = selectedVenders;
    }

    public List<VenderAllVO> getAllVenderList() {
        return allVenderList;
    }

    public List<VenderAllVO> getSelectedVenderList() {
        return selectedVenderList;
    }

    public List<RfqVenderVO> getRfqVenderList() {
        return rfqVenderList;
    }

    public EtBargain getLastBargain() {
        return lastBargain;
    }

    public EtBargain getEditBargain() {
        return editBargain;
    }

    public void setEditBargain(EtBargain editBargain) {
        this.editBargain = editBargain;
    }
    
    public boolean isIsAdmin() {
        return isAdmin;
    }
    //</editor-fold>
    
}

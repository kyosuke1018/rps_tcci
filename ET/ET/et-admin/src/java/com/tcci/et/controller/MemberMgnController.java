/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.entity.EtMember;
import com.tcci.et.facade.EtMemberFacade;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.VenderVO;
import com.tcci.et.model.criteria.MemberCriteriaVO;
import com.tcci.et.model.rs.LongOptionVO;
import com.tcci.et.facade.EtVenderFacade;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.fc.util.StringUtils;
import com.tcci.security.AESPasswordHash;
import com.tcci.security.AESPasswordHashImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "memberController")
@ViewScoped
public class MemberMgnController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 65;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private EtMemberFacade etMemberFacade;
    @EJB private EtVenderFacade etVenderFacade;
//    @EJB private EtMemberFormFacade memberFormFacade;
    
    // 查詢條件
    private MemberCriteriaVO criteriaVO;
    
    // 結果
    private BaseLazyDataModel<MemberVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<MemberVO> filterResultList; // datatable filter 後的結果
    
    // 編輯
//    private TcUser editUsers;
    private MemberVO editVO;
//    private List<String> selectedVenderList;
    private boolean editMode = false;
    
//    private List<EtVender> etVenderList;
    private String selectedVenderTxt;
    private List<VenderVO> allLfa1List; // 可選取全部供應商 
    private List<VenderVO> addVenderList;
    
    // 匯入
    private String importTxt;
    
    private boolean isAdmin;
    private StreamedContent downloadFile;
    
    @PostConstruct
    private void init(){
        if( functionDenied ){ return; }
        
        criteriaVO = new MemberCriteriaVO();
        criteriaVO.setActive(Boolean.FALSE);
        criteriaVO.setDisabled(Boolean.TRUE);
        editVO = new MemberVO();
//        selectedVenderList = new ArrayList<>();
        
        // Get view Id
        viewId = JsfUtils.getViewId();
        
        fetchInputParameters();
        
        isAdmin = sessionController.isUserInRole("ADMINISTRATORS");
        if(isAdmin){
            
        }
        
        initVenderOption();//跑很久 改在init
        
        //
        BaseCriteriaVO defCriteria = new BaseCriteriaVO();
        defCriteria.setDisabled(Boolean.FALSE);
        allLfa1List = etVenderFacade.findLfa1ByCriteria(defCriteria);
        
        // default query
        defQuery();
    }
    
    /**
     * 取得輸入參數
     */
    private void fetchInputParameters() {
        // 回饋
        String loginAccount = JsfUtils.getRequestParameter("loginAccount");
        logger.debug("loginAccount = " + loginAccount);
        
        if( loginAccount!=null ){
            try{
                criteriaVO.setKeyword(loginAccount);
            }catch(NumberFormatException e){
                // ignore
            }
        }
    }
    
    /**
     * default query
     */
    public void defQuery(){
        // criteriaVO.setGroup(GlobalConstant.UG_ADMINISTRATORS_ID); // ADMINISTRATORS
        doQuery();
    }
    
    /**
     * 查詢參數檢核
     * @return 
     */
    public boolean doCheck(){
        if( criteriaVO.getKeyword()!=null ){
            criteriaVO.setKeyword(criteriaVO.getKeyword().trim());
        }
        return true;
    }
    
    /**
     * 查詢
     */
    public void doQuery(){
        logger.debug("doQuery ...");
        if( !doCheck() ){
            return;
        }
        
        resetDataTable();
        criteriaVO.setSetMaxResultsSize(GlobalConstant.DEF_MAX_RESULT_SIZE);//設定最大回傳筆數

        try {
            logger.info("doQuery ...Active:"+criteriaVO.getActive());
            //排除已刪除
            if(!criteriaVO.getActive()){
                criteriaVO.setActive(null);
            }
            logger.info("doQuery ...Disabled:"+criteriaVO.getDisabled());
            if(criteriaVO.getDisabled()){
                criteriaVO.setDisabled(null);
            }
            
            List<MemberVO> resList = etMemberFacade.findByCriteria(criteriaVO, GlobalConstant.DEF_LOCALE.getLocale());
            lazyModel = new BaseLazyDataModel<MemberVO>(resList);
        }catch(Exception e){
            String msg = ExceptionHandlerUtils.getSimpleMessage("查詢失敗:", e);
            logger.error(msg);
            JsfUtils.addErrorMessage(msg);
        }
    }
    
    /**
     * 重設表單、結果
     */
    public void doReset(){
        logger.debug("doReset ...");
        if( lazyModel!=null ){
            lazyModel.reset();
        }
        
        // 清除條件
        criteriaVO = new MemberCriteriaVO();
        criteriaVO.reset();
        resetDataTable();
    }
    
    /**
     * 移除 datatable 目前排序、filter 效果
     */
    public void resetDataTable(){
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }
    
    /**
     * 開始編輯
     * @param vo 
     */
    public void edit(MemberVO vo){
        logger.debug("=== edit ===");
        
//        selectedVenderList = new ArrayList<>();
        initVenderOption();

        // 取出原 USER 資料
//        editVO = vo;
        editVO = etMemberFacade.findById(vo.getMemberId(), false, GlobalConstant.DEF_LOCALE.getLocale());
        logger.debug("=== edit === MemberId:"+editVO.getMemberId());
        logger.debug("=== edit === detailId:"+editVO.getId());

//        fetchGroupList(editUsers);// user group

        editMode = true;
    }
    
    public void changeDisabled(){
        logger.debug("changeDisabled..."+editVO.getDisabled());
    }
    
    /**
     * 開始新增
     */
    public void add(){
        logger.debug("=== add === ");
//        editUsers = new TcUser();
        editVO = new MemberVO();
//        selectedVenderList = new ArrayList<>();
        initVenderOption();

        // 新增 user 預設為 CSRC_USER
//        TcGroup tcGroup = tcGroupFacade.findGroupByCode(GlobalConstant.UG_CSRC_USER);
//        if( tcGroup!=null ){
//            selectedGroups.add(tcGroup.getId().toString());// 預設勾選
//        }
        
//        fetchGroupList(editUsers);// user group
        
        editMode = true;
//        defPlantUser = true;
    }
    
    /**
     * 儲存 
     */
    //public void save(ActionEvent event) {    
    public void save() {
        logger.info("=== save === ");
        ActivityLogEnum acEnum = (editVO.getMemberId()==null ? ActivityLogEnum.A_MEMBER : ActivityLogEnum.U_MEMBER);
        RequestContext context;
        
        if( editVO==null ){
            context = JsfUtils.buildErrorCallback();
            context.addCallbackParam("msg", "editVO is null!");
            return;
        }
        
        try{
            editVO.setEmail(editVO.getLoginAccount());
            editVO.setAddVenderList(addVenderList);
            if( editVO.getMemberId()==null ){// new
                // default password
                AESPasswordHash aes = new AESPasswordHashImpl();
                String encrypted = aes.encrypt(GlobalConstant.DEF_PWD);
                editVO.setPwd(encrypted);
            }
            
            etMemberFacade.saveVO(editVO, this.getLoginUser(), GlobalConstant.DEF_LOCALE.getLocale(), false);
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(),
                editVO.getMemberId(), true, this.getLoginUser(), this.isSimulated());
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(),
                editVO.getMemberId(), false, this.getLoginUser(), this.isSimulated());
            logger.error("save Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
    
    /**
     * 已選取 Group
     * @return 
     */
//    public List<TcGroup> getSelectedVenderList(){
//        if( tcGroupList==null ){
//            return null;
//        }
//        List<TcGroup> selectedList = new ArrayList<TcGroup>();
//        for(TcGroup tcGroup:tcGroupList){
//            if( selectedGroups!=null && selectedGroups.contains(tcGroup.getId().toString()) ){
//                selectedList.add(tcGroup);
//            }
//        }
//        return selectedList;
//    }
    
    
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    } 
    
    //<editor-fold defaultstate="collapsed" desc="for vender">
    private void initVenderOption(){
        selectedVenderTxt = "";
        addVenderList = new ArrayList<>();
//        allLfa1List = etVenderFacade.findLfa1ByCriteria(new BaseCriteriaVO());
    }
    
    /**
     * 選取 autoComplete 供應商列表
     * @param intxt
     * @return 
     */
    public List<String> autoCompleteVenderOptions(String intxt){
        List<String> resList = new ArrayList<String>();
        
        for(VenderVO vender : allLfa1List){// 有權選取的所有User
            String txt = vender.getLabel();
            if( txt.toUpperCase().indexOf(intxt.toUpperCase()) >= 0 ){// 符合輸入
                resList.add(txt);
            }
        }
        
        return resList;
    }  
    
    public void addVender(){
        LongOptionVO addVenerVO = new LongOptionVO();
        VenderVO selectedVender = null;
        for(VenderVO vender : allLfa1List){// 有權選取的所有User
            String txt = vender.getLabel();
            if( txt.toUpperCase().indexOf(selectedVenderTxt.toUpperCase()) >= 0 ){// 符合輸入
                addVenerVO.setValue(Long.parseLong("0"));
                addVenerVO.setLabel(txt);
                selectedVender = vender;
            }
        }
//        initVenderOption();
//        selectedVenderTxt = "";
        
        // check
        List<LongOptionVO> list = editVO.getVenders();
        if(CollectionUtils.isNotEmpty(list)){
            for(LongOptionVO vo : list){
                if(vo.getLabel().equals(addVenerVO.getLabel())){
                    return;
                }
            }
        }else{
            list = new ArrayList<>();
        }
        list.add(addVenerVO);
        if(selectedVender!=null){
            addVenderList.add(selectedVender);
        }
        
        editVO.setVenders(list);
    }
    
    public void removeVender(LongOptionVO venerVO){
        List<LongOptionVO> list = editVO.getVenders();
        list.remove(venerVO);
        editVO.setVenders(list);
    }
    
    /**
     * 重設密碼 send mail
     *
     * @param vo
     */
    public void resetPassword(MemberVO vo) {
        logger.debug("resetPassword ...");
        ActivityLogEnum acEnum = ActivityLogEnum.RESET_PWD;
        try {
            EtMember member = etMemberFacade.find(vo.getMemberId());
            
            // 產生重設密碼 a ~ z
            String plaintext = StringUtils.genRandomString(97, 122, 6);// 六碼小寫英文
            AESPasswordHash aes = new AESPasswordHashImpl();
            String encrypted = aes.encrypt(plaintext);
            
            //send mail
            etMemberFacade.sendPwdMail(member, plaintext);
            
            // save DB
            member.setPassword(encrypted);
            etMemberFacade.save(member, this.getLoginUser(), false);

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getMemberId(),
                    acEnum.getCode(), acEnum.getName()+"("+plaintext+")", true, this.getLoginUser(), this.isSimulated());
            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "resetPassword", e, false);

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getMemberId(),
                    acEnum.getCode(), acEnum.getName(), false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public BaseLazyDataModel<MemberVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<MemberVO> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    public List<MemberVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<MemberVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public MemberCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(MemberCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }
    
    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }
    
    public MemberVO getEditVO() {
        return editVO;
    }

    public void setEditVO(MemberVO editVO) {
        this.editVO = editVO;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getSelectedVenderTxt() {
        return selectedVenderTxt;
    }

    public void setSelectedVenderTxt(String selectedVenderTxt) {
        this.selectedVenderTxt = selectedVenderTxt;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }
    //</editor-fold>
}

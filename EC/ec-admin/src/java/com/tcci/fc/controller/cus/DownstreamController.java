/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.fc.controller.cus;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.enums.ActivityLogEnum;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcMemberFactory;
import com.tcci.ec.entity.EcTccDealerDs;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.facade.EcMemberFacade;
import com.tcci.ec.facade.EcMemberFactoryFacade;
import com.tcci.ec.facade.EcTccDealerDsFacade;
import com.tcci.ec.facade.MessageFacade;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.criteria.MemberCriteriaVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.security.AESPasswordHash;
import com.tcci.security.AESPasswordHashImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * A11.經銷商管理 (參閱 [匯入台泥經銷商功能] )
 * @author Peter.pan
 */
@ManagedBean(name = "downstream")
@ViewScoped
public class DownstreamController extends SessionAwareController implements Serializable {
    private static final long FUNC_OPTION = 23;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    private static final int MAX_REL_DEALER = 3;// 關聯經銷商一次最多新增筆數
    
    private @EJB EcMemberFacade memberFacade;
    private @EJB EcTccDealerDsFacade tccDealerDsFacade;
    private @EJB EcMemberFactoryFacade memberFactoryFacade;
    private @EJB MessageFacade message;
    
    private MemberCriteriaVO criteriaVO;
    
    //private List<SelectItem> sysOptions; // 巡檢系統別
    
    // 查詢結果
    private List<MemberVO> resultList;
    private BaseLazyDataModel<MemberVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<MemberVO> filterResultList;
    
    // 綁定供應商
    private List<MemberVO> dealers;// for select dealers
    private List<Long> selDealers;
    private String selDealerLabel;
    private List<MemberVO> existedDealers;
    private List<MemberVO> addDealers;
    private boolean dealerEditMode;
    
    // 編輯
    //private List<MemberVO> addList;
    private MemberVO editVO;
    
    @PostConstruct
    private void init() {
        // 權限檢核
        if( this.functionDenied ){
            logger.debug(this.getClass().getSimpleName()+" init functionDenied = "+functionDenied);
            return;
        }
        resetCriteria();
        
        prepareAdd();// 準備新增資料
    }
    
    /**
     * 功能標題
     * @return
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }
    
    public void resetCriteria(){
        criteriaVO = new MemberCriteriaVO();
        
        if( !this.isAdministrators(this.getLoginUser()) ){// 系統管理員不限制(可查到未設定廠別的客戶)
            if( !sys.isEmpty(factoryListPermission) ){
                List<Long> list = new ArrayList<Long>();
                for(CmFactory factory : factoryListPermission){
                    list.add(factory.getId());
                }
                criteriaVO.setFactoryList(list);
            }
        }
    }
    
    public boolean checkParams(BaseCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            // 未輸入查詢條件!
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt001"));
            return false;
        }
        if( !this.isAdministrators(this.getLoginUser()) && sys.isEmpty(criteriaVO.getFactoryList()) ){
            // 無廠別權限!
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt015"));
            return false;
        }
        
        return true;
    }
    
    /**
     * 查詢
     */
    public void doQuery(){
        logger.debug("doQuery ...");
        try {
            if( !checkParams(criteriaVO) ){
                return;
            }
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);
            filterResultList = null; // filterValue 初始化
            
            criteriaVO.setTccDs(true);
            resultList = memberFacade.findByCriteria(criteriaVO, JsfUtils.getRequestLocale());
            
            lazyModel = new BaseLazyDataModel<MemberVO>(resultList);
        } catch (Exception e) {
            sys.processUnknowException(this.getLoginUser(), "doQuery", e);
        }
    }
    
    /**
     * 清除
     */
    public void doReset() {
        logger.debug("doReset ...");
        try {
            // 清除條件
            resetCriteria();
            
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);
            filterResultList = null; // filterValue 初始化
            resultList = null;
            lazyModel = new BaseLazyDataModel<MemberVO>();
        } catch (Exception e) {
            sys.processUnknowException(this.getLoginUser(), "doReset", e);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="for CRUD">
    /**
     * 準備修改
     */
    public void prepareAdd(){
        logger.debug("prepareAdd ...");
        try{
            this.editVO = new MemberVO();
            this.selDealers = new ArrayList<Long>();
            this.selDealerLabel = null;
            this.existedDealers = new ArrayList<MemberVO>();
            // 新增綁定經銷商
            initAddDealsForm();
            dealerEditMode = true;
            //findDealers();// 抓取有效經銷商// for select dealers
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            sys.processUnknowException(this.getLoginUser(), "prepareAdd", e);
        }
    }
    
    /**
     * 準備編輯
     * @param vo
     */
    public void prepareEdit(MemberVO vo){
        logger.debug("prepareEdit ...");
        try{
            editVO = new MemberVO();
            ExtBeanUtils.copyProperties(editVO, vo);
            editVO.setEditMode(true); // 編輯模式
            //findDealers();// 抓取有效經銷商 // for select dealers
            findDsDealers(editVO.getMemberId());// 抓取現有綁定經銷商
            //prepareDsDealers();// for select dealers
            // 新增綁定經銷商
            initAddDealsForm();
            dealerEditMode = false;
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            sys.processUnknowException(this.getLoginUser(), "prepareEdit", e);
        }
    }
    
    /**
     * 非正在編輯列
     * @param vo
     * @return
     */
    public boolean notEditRow(MemberVO vo){
        return editVO!=null && !editVO.equals(vo);
    }
    
    /**
     * 刪除
     * @param vo
     */
    public void deleteMember(MemberVO vo){
        logger.debug("deleteMember ...");
        try{
            // 不刪除，只改 Disabled
            // memberFacade.remove(vo);
            EcMember entity = memberFacade.find(vo.getMemberId());
            if( entity!=null ){
                entity.setDisabled(Boolean.TRUE);
                memberFacade.save(entity, this.getLoginMember(), false);
                logger.info("removeMember save id = "+vo.getMemberId());
                
                activityLogFacade.logActiveCommon(ActivityLogEnum.D_DEALER, viewId, vo.toString(),
                        true, this.getLoginUser());// 異動紀錄
                this.doQuery();
                prepareAdd();// 準備新增資料
                JsfUtils.buildSuccessCallback();
                return;
            }else{
                logger.error("removeMember not exists id = "+vo.getMemberId());
            }
        }catch(Exception e){
            sys.processUnknowException(this.getLoginUser(), "deleteMember", e);
        }
        activityLogFacade.logActiveCommon(ActivityLogEnum.D_DEALER, viewId, vo.toString(),
                false, this.getLoginUser());// 異動紀錄
        // 刪除失敗!
        JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt003"));
    }
    
    /**
     * 儲存前檢查
     * @param vo
     * @return
     */
    public boolean checkBeforeSave(MemberVO vo){
        List<String> errors = new ArrayList<String>();
        if( !memberFacade.checkInput(vo, this.getLoginUser(), JsfUtils.getRequestLocale(), errors) ){// 輸入檢查
            logger.error("memberFacade.checkInput errors="+errors.size());
            for(String err : errors){
                JsfUtils.addErrorMessage(err);
            }
            return false;
        }
        
        if( this.addDealers!=null ){// 新增綁定經銷商檢查
            for(MemberVO memberVO : addDealers){
                if( !memberVO.isError() ){// 錯誤的忽略
                    if( StringUtils.isNotBlank(memberVO.getIdCode()) ){// 有輸入
                        if( StringUtils.isBlank(memberVO.getName()) ){// 未執行檢查
                            // 請先執行[綁定經銷商]的輸入檢查，確保該經銷商存在!
                            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt018"));
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * 儲存修改
     */
    public void saveEdit(){
        logger.debug("saveEdit ...");
        MemberVO vo = this.editVO;
        try{
            MemberVO memVO = memberFacade.fundById(vo.getMemberId(), locale);
            //*統一社會信用代碼 *公司名稱 *公司簡稱 "*會員帳號(台泥電商賬號)"
            //*手機 E-mail 公司負責人 資本額(元) 年收入(元)
            //產業別 所在區域 創立時間
            memVO.setIdCode(vo.getIdCode());
            memVO.setCname(vo.getCname());
            memVO.setNickname(vo.getNickname());
            memVO.setLoginAccount(vo.getLoginAccount());
            memVO.setPhone(vo.getPhone());
            memVO.setEmail(vo.getEmail());
            memVO.setOwner1(vo.getOwner1());
            memVO.setCapital(vo.getCapital());
            memVO.setYearIncome(vo.getYearIncome());
            memVO.setBrief(vo.getBrief());// 產業別也放在簡介欄
            memVO.setAddr1(vo.getAddr1());// 區域別也放到地址欄
            memVO.setStartAt(vo.getStartAt());
            
            if( !checkBeforeSave(memVO) ){
                return;
            }
            
            memberFacade.saveVO(memVO, this.getLoginMember(), JsfUtils.getRequestLocale(), false);
            saveDealerDsRel(memVO.getMemberId());// 儲存綁定經銷商
            saveRelFactory(memVO);// 資料維護廠別
            
            activityLogFacade.logActiveCommon(ActivityLogEnum.U_DEALER, viewId, vo.toString(),
                    true, this.getLoginUser());// 異動紀錄
            
            //vo.setEditMode(false);
            this.criteriaVO.setOrderBy("NVL(S.MODIFYTIME, S.CREATETIME) DESC");
            this.doQuery();
            prepareAdd();// 準備新增資料
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            activityLogFacade.logActiveCommon(ActivityLogEnum.U_DEALER, viewId, vo.toString(),
                    false, this.getLoginUser());// 異動紀錄
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt003"));// 儲存失敗!
            sys.processUnknowException(this.getLoginUser(), "saveEdit", e);
        }
    }
    
    /**
     * 儲存新增
     */
    public void saveAdd(){
        logger.debug("saveAdd ...");
        try{
            MemberVO memVO = new MemberVO();
            ExtBeanUtils.copyProperties(memVO, editVO);
            // 暫用 verifyCode 辨別同批匯入
            String verifyCode = DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR);
            memVO.setName(editVO.getNickname());// 使用公司簡稱當會員名稱
            
            memVO.setActive(Boolean.TRUE);
            memVO.setAdminUser(Boolean.FALSE);
            memVO.setMemberType(MemberTypeEnum.COMPANY.getCode());// 直接成為公司戶
            memVO.setDisabled(Boolean.FALSE);
            memVO.setTccDealer(Boolean.FALSE);// 台泥經銷商
            memVO.setTccDs(Boolean.TRUE);// 下游客戶
            
            memVO.setVerifyCode(verifyCode);
            memVO.setApplytime(new Date());
            memVO.setApprovetime(new Date());
            // for EC_COMPAMY
            memVO.setTel1(memVO.getPhone());
            memVO.setEmail1(memVO.getEmail());
            memVO.setType(CompanyTypeEnum.MEMBER.getCode());
            
            if( !checkBeforeSave(memVO) ){
                return;
            }
            memberFacade.saveVO(memVO, this.getLoginMember(), JsfUtils.getRequestLocale(), false);
            saveDealerDsRel(memVO.getMemberId());// 儲存綁定經銷商
            saveRelFactory(memVO);// 資料維護廠別
            
            logger.info("saveAdd with new store ... "+memVO.getMemberId());
            
            activityLogFacade.logActiveCommon(ActivityLogEnum.C_DEALER, viewId, editVO.toString(),
                    true, this.getLoginUser());// 異動紀錄
            
            this.criteriaVO.setOrderBy("NVL(S.MODIFYTIME, S.CREATETIME) DESC");
            this.doQuery();
            prepareAdd();// 準備新增資料
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            activityLogFacade.logActiveCommon(ActivityLogEnum.C_DEALER, viewId, editVO.toString(),
                    false, this.getLoginUser());// 異動紀錄
            // 新增失敗!
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt005"));
            sys.processUnknowException(this.getLoginUser(), "saveAdd", e);
        }
    }
    
    /**
     * 取消修改
     */
    public void cancelEdit(){
        logger.debug("cancelEdit ...");
        try{
            this.prepareAdd();
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            sys.processUnknowException(this.getLoginUser(), "cancelEdit", e);
        }
    }
    
    /**
     * 發送密碼簡訊
     */
    public boolean canSendSMS(){
        return this.isAdministrator() && editVO!=null && sys.isValidId(editVO.getMemberId());
    }
    public void sendSMS(){
        if( this.editVO==null || this.editVO.getMemberId()==null ) {
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt009"));//未指定發送對象!
            JsfUtils.buildErrorCallback();
            return;
        }
        EcMember member = memberFacade.find(this.editVO.getMemberId());
        if( member==null ) {
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt010"));//沒有此發送對象!
            JsfUtils.buildErrorCallback();
            return;
        }
        
        if( sys.isBlank(this.editVO.getPhone()) ) {
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt011"));//未輸入電話(手機號碼)!
            JsfUtils.buildErrorCallback();
            return;
        }
        
        // 產生重設密碼 a ~ z
        String plaintext = StringUtils.genRandomString(97, 122, 6);// 六碼小寫英文
        AESPasswordHash aes = new AESPasswordHashImpl();
        String encrypted = aes.encrypt(plaintext);
        // SMS
        logger.info("sendSMS before sendMsg ...");
        logger.debug("sendSMS loginAccount="+member.getLoginAccount()+", plaintext = "+plaintext);
        if( message.resetPassword(member, plaintext, member, locale) ){
            logger.info("sendSMS before save DB ...");
            // save DB
            member.setPassword(encrypted);
            member.setResetPwd(encrypted);
            member.setResetPwdExpired(DateUtils.addDate(new Date(), 3));
            memberFacade.save(member, member, false);
        }
    }
    
    // 可否刪除
    public boolean canDelete(MemberVO vo){
        if( this.editVO==null || !this.editVO.isEditMode() ){
            if( vo.getModifierId()!=null ){
                return vo.getModifierId().equals(this.getLoginMemberId());
            }else{
                if( vo.getCreatorId()!=null ){
                    return vo.getCreatorId().equals(this.getLoginMemberId());
                }
            }
        }
        return false;
    }
    
    /**
     * 資料維護廠別
     * @param vo
     */
    public void saveRelFactory(MemberVO vo){
        // 資料維護廠別
        if( vo.getFactoryId()!=null ){
            EcMemberFactory entity = memberFactoryFacade.findByKey(vo.getMemberId(), vo.getFactoryId());
            memberFactoryFacade.save(entity, this.getLoginUser(), false);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for 綁定經銷商 (輸入 IC_CODE 方式)">
    /**
     * 初始新增綁定經銷商表單
     */
    public void initAddDealsForm(){
        // 新增綁定經銷商
        this.addDealers = new ArrayList<MemberVO>();
        for(int i=0; i<MAX_REL_DEALER; i++){
            addDealers.add(new MemberVO());
        }
    }
    
    /**
     * 儲存綁定經銷商
     * @param dsId
     */
    public void saveDealerDsRel(Long dsId){
        logger.debug("saveDealerDsRel ...");
        List<EcTccDealerDs> list = tccDealerDsFacade.findByDs(dsId);
        
        // 不用選取的，改用輸入 IC_CODE /////////////
        selDealers = new ArrayList<Long>();
        if( this.existedDealers!=null ){// 原有且未刪除
            for(MemberVO vo : existedDealers){
                if( !vo.isDeleted() ){
                    selDealers.add(vo.getMemberId());
                }
            }
        }
        if( this.addDealers!=null ){// 新增
            for(MemberVO vo : addDealers){
                if( !vo.isError() && StringUtils.isNotBlank(vo.getIdCode()) && vo.getMemberId()!=null ){
                    selDealers.add(vo.getMemberId());
                }
            }
        }
        //////////////////////////////
        
        List<Long> oriList = new ArrayList<Long>();
        if( list!=null ){
            for(EcTccDealerDs vo : list){
                oriList.add(vo.getDealerId());
                if( this.selDealers==null || !this.selDealers.contains(vo.getDealerId()) ){
                    logger.debug("saveDealerDsRel remove dealerId = "+vo.getDealerId()+", dsId = "+dsId);
                    tccDealerDsFacade.remove(vo);
                }
            }
        }
        if( selDealers!=null ){
            for(Long dealerId : selDealers){
                if( !oriList.contains(dealerId) ){
                    logger.debug("saveDealerDsRel saveByKey dealerId = "+dealerId+", dsId = "+dsId);
                    tccDealerDsFacade.saveByKey(dealerId, dsId, this.getLoginMember(), false);
                }
            }
        }
    }
    
    /**
     * 抓取現有綁定經銷商
     * @param dsId
     */
    public void findDsDealers(Long dsId){
        logger.debug("findDsDealers ...");
        MemberCriteriaVO pCriteriaVO = new MemberCriteriaVO();
        pCriteriaVO.setDsId(dsId);
        pCriteriaVO.setTccDealer(true);
        List<MemberVO> oriDealers = memberFacade.findByCriteria(pCriteriaVO, locale);
        
        StringBuilder sb = new StringBuilder();
        existedDealers = new ArrayList<MemberVO>();
        selDealers = new ArrayList<Long>();
        if( oriDealers!=null ){
            for(MemberVO vo : oriDealers){
                selDealers.add(vo.getMemberId());
                existedDealers.add(vo);
                if( sb.toString().isEmpty() ){
                    sb.append(vo.getDealerLabel());
                }else{
                    sb.append("、").append(vo.getDealerLabel());
                }
            }
        }
        this.selDealerLabel = sb.toString();
        logger.debug("findDsDealers selDealers = "+sys.size(selDealers));
    }
    
    /**
     * 編輯綁定經銷商
     */
    public void prepareEditRelDealers(){
        logger.debug("prepareEditRelDealers ...");
        try{
            dealerEditMode = true;
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "prepareEditRelDealers", e, true);
        }
    }
    
    /**
     * 刪除綁定經銷商
     * @param vo
     */
    public void delRelDealer(MemberVO vo){
        logger.debug("delRelDealer vo = "+vo);
        vo.setDeleted(true);
        JsfUtils.buildSuccessCallback();
    }
    
    /**
     * 檢查輸入綁定供應商
     */
    public void checkRelDealer(){
        logger.debug("delRelDealer ...");
        
        List<String> codes = new ArrayList<String>();
        for(MemberVO vo : addDealers){
            logger.debug("checkRelDealer idCode = "+vo.getIdCode());
            String idCode = vo.getIdCode().trim().toUpperCase();
            vo.setMemberId(null);
            vo.setName(null);
            vo.setError(false);
            
            if( StringUtils.isNotBlank(idCode) ){
                if( codes.contains(idCode) ){
                    // 重複輸入!
                    vo.setName(JsfUtils.getResourceTxt("msg.txt019"));// for UI display
                    vo.setError(true);
                }else{
                    codes.add(idCode);
                    if( existedDealers!=null ){
                        for(MemberVO existed : existedDealers){
                            if( existed.getIdCode()!=null && idCode.equals(existed.getIdCode().toUpperCase()) ){
                                // 已設定!
                                vo.setName(JsfUtils.getResourceTxt("msg.txt020"));// for UI display
                                vo.setError(true);
                                break;
                            }
                        }
                    }
                }
                
                if( vo.isError()){
                    continue;
                }
                
                // 依 IC_CODD 查經銷商名稱
                MemberCriteriaVO mCriteriaVO = new MemberCriteriaVO();
                mCriteriaVO.setTccDealer(Boolean.TRUE);// tcc dealer
                mCriteriaVO.setSellerApprove(Boolean.TRUE);// 賣家
                mCriteriaVO.setDisabled(Boolean.FALSE);
                mCriteriaVO.setActive(Boolean.TRUE);
                mCriteriaVO.setFullData(false);
                mCriteriaVO.setIdCode(vo.getIdCode());
                
                List<MemberVO> list = memberFacade.findByCriteria(mCriteriaVO, locale);
                if( !sys.isEmpty(list) ){
                    ExtBeanUtils.copyProperties(vo, list.get(0));
                }else{
                    // 查無此經銷商!
                    vo.setName(JsfUtils.getResourceTxt("msg.txt021"));// for UI display
                    vo.setError(true);
                }
            }
        }
    }
    
    /**
     *  儲存綁定經銷商
     */
    public void saveDsDealers(){
        logger.debug("saveDsDealers ...");
        try{
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            sys.processUnknowException(this.getLoginUser(), "saveDsDealers", e);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for 綁定經銷商 (勾選方式，目前不用)">
    /**
     * 抓取有效經銷商 // for select dealers
     */
    public void findDealers(){
        MemberCriteriaVO pCriteriaVO = new MemberCriteriaVO();
        pCriteriaVO.setTccDealer(true);
        dealers = memberFacade.findByCriteria(pCriteriaVO, locale);
    }
    
    /**
     * 顯示現有綁定經銷商// for select dealers
     */
    public void prepareDsDealers(){
        StringBuilder sb = new StringBuilder();
        if( dealers!=null && selDealers!=null ){
            for(MemberVO vo : dealers){
                if( selDealers.contains(vo.getMemberId()) ){
                    if( sb.toString().isEmpty() ){
                        sb.append(vo.getDealerLabel());
                    }else{
                        sb.append("、").append(vo.getDealerLabel());
                    }
                }
            }
        }
        this.selDealerLabel = sb.toString();
    }
    
    /**
     * 開啟綁定經銷商對話框// for select dealers
     */
    public void openSelectDealerDlg(){
        logger.debug("openSelectDealerDlg ...");
        try{
            if( dealers!=null && selDealers!=null ){
                for(MemberVO vo : dealers){
                    if( selDealers.contains(vo.getMemberId()) ){
                        vo.setSelected(true);
                    }
                }
            }
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            sys.processUnknowException(this.getLoginUser(), "openSelectDealerDlg", e);
        }
    }
    
    /**
     * 選取綁定經銷商// for select dealers
     */
    public void selectDealers(){
        logger.debug("selectDealers ...");
        try{
            selDealers = new ArrayList<Long>();
            if( dealers!=null ){
                for(MemberVO vo : dealers){
                    if( vo.isSelected() ){
                        selDealers.add(vo.getMemberId());
                    }
                }
            }
            logger.debug("selectDealers selDealers = "+sys.size(selDealers));
            prepareDsDealers();
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            sys.processUnknowException(this.getLoginUser(), "selectDealers", e);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter & setter">
    public MemberCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }
    
    public void setCriteriaVO(MemberCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }
    
    public List<MemberVO> getAddDealers() {
        return addDealers;
    }
    
    public void setAddDealers(List<MemberVO> addDealers) {
        this.addDealers = addDealers;
    }
    
    public List<Long> getSelDealers() {
        return selDealers;
    }
    
    public void setSelDealers(List<Long> selDealers) {
        this.selDealers = selDealers;
    }
    
    public List<MemberVO> getResultList() {
        return resultList;
    }
    
    public void setResultList(List<MemberVO> resultList) {
        this.resultList = resultList;
    }
    
    public BaseLazyDataModel<MemberVO> getLazyModel() {
        return lazyModel;
    }
    
    public void setLazyModel(BaseLazyDataModel<MemberVO> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    public MemberVO getEditVO() {
        return editVO;
    }
    
    public void setEditVO(MemberVO editVO) {
        this.editVO = editVO;
    }
    
    public List<MemberVO> getFilterResultList() {
        return filterResultList;
    }
    
    public void setFilterResultList(List<MemberVO> filterResultList) {
        this.filterResultList = filterResultList;
    }
    
    public List<MemberVO> getExistedDealers() {
        return existedDealers;
    }
    
    public void setExistedDealers(List<MemberVO> existedDealers) {
        this.existedDealers = existedDealers;
    }
    
    public List<MemberVO> getDealers() {
        return dealers;
    }
    
    public void setDealers(List<MemberVO> dealers) {
        this.dealers = dealers;
    }
    
    public boolean isDealerEditMode() {
        return dealerEditMode;
    }
    
    public void setDealerEditMode(boolean dealerEditMode) {
        this.dealerEditMode = dealerEditMode;
    }
    
    public String getSelDealerLabel() {
        return selDealerLabel;
    }
    
    public void setSelDealerLabel(String selDealerLabel) {
        this.selDealerLabel = selDealerLabel;
    }
    //</editor-fold>
}

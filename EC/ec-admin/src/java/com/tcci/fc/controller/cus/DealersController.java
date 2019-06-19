/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.cus;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.enums.ActivityLogEnum;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.facade.EcMemberFacade;
import com.tcci.ec.facade.EcStoreFacade;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.StoreVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.criteria.MemberCriteriaVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.http.impl.cookie.DateUtils;

/**
 * A11.經銷商管理 (參閱 [匯入台泥經銷商功能] )
 * @author Peter.pan
 */
@ManagedBean(name = "dealers")
@ViewScoped
public class DealersController extends SessionAwareController implements Serializable {
    private static final long FUNC_OPTION = 22;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    private @EJB EcMemberFacade memberFacade;
    private @EJB EcStoreFacade storeFacade;
    
    private MemberCriteriaVO criteriaVO;

    //private List<SelectItem> sysOptions; // 巡檢系統別
    
    // 查詢結果
    private List<MemberVO> resultList;
    private BaseLazyDataModel<MemberVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<MemberVO> filterResultList;
    
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
    
    /*public String getFunctionGroup(){
        return FuncGroupEnum.EVENT.getDisplayName(); // II.事件處理
    }
    public String getFunctionName(){
        return FunctionEnum.ONDUTY_REMIND.getDisplayName();//　1.巡检注意事项
    }*/
   
    public void resetCriteria(){
        criteriaVO = new MemberCriteriaVO();
        //criteriaVO.setPlantId(this.getPlantIdStr());
        //criteriaVO.setAvailableOnly(true);// 預設 排除禁用与过期讯息
        changeFactoryId();
    }
    
    public void changeFactoryId(){
        logger.debug("changeFactoryId ... "+criteriaVO.getFactoryId());
        //sysOptions = commonUIFacade.buildPatrolSystemOptions(criteriaVO.getPlantIdValue(), true);
    }
    
    public boolean checkParams(BaseCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            // 未輸入查詢條件!
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt001"));
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

            //criteriaVO.setFactoryId(FUNC_OPTION);
            criteriaVO.setTccDealer(true);
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
     * 準備修改
     */
    public void prepareAdd(){
        logger.debug("prepareAdd ...");
        try{
            this.editVO = new MemberVO();
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            sys.processUnknowException(this.getLoginUser(), "prepareAdd", e);
        }
    }
    
    /**
     * 儲存新增 
     */
    public void saveAdd(){
        logger.debug("saveAdd ...");
        try{
            // 檢查STORE是否存在 for 可多人管理STORE
            if( editVO.getIdCode()==null ){
                logger.error("saveDealers vo.getIdCode()==null !");
                JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("msg.txt004"));// [统一社会信用代码]必填!
                return;
            }
            
            StoreVO storeVO = storeFacade.findByIdCode(editVO.getIdCode());

            MemberVO memVO = new MemberVO();
            logger.debug("saveAdd 1 memVO.getMemberId = "+memVO.getMemberId());
            ExtBeanUtils.copyProperties(memVO, editVO);
            logger.debug("saveAdd 2 memVO.getMemberId = "+memVO.getMemberId());
            // 暫用 verifyCode 辨別同批匯入
            String verifyCode = DateUtils.formatDate(new Date(), GlobalConstant.FORMAT_DATETIME_STR);
            if( storeVO==null ){// STORE 不存在，正常建立 EC_MEMBER、EC_SELLER、EC_STORE、EC_COMPANY*2、EC_STORE_USER
                // for EC_MEMBER
                memVO.setName(editVO.getNickname());// 使用公司簡稱當會員名稱
                
                memVO.setActive(Boolean.TRUE);
                memVO.setAdminUser(Boolean.FALSE);
                memVO.setMemberType(MemberTypeEnum.COMPANY.getCode());// 直接成為公司戶
                memVO.setDisabled(Boolean.FALSE);
                memVO.setTccDealer(Boolean.TRUE);// 台泥經銷商
                memVO.setTccDs(Boolean.FALSE);
                // 預設皆有權限
                memVO.setStoreOwner(Boolean.TRUE);// 第一筆帳號預設為 owner
                memVO.setFiUser(Boolean.TRUE);
                
                memVO.setVerifyCode(verifyCode);
                memVO.setApplytime(new Date());
                memVO.setApprovetime(new Date());
                memVO.setSellerApply(Boolean.TRUE);// 直接成為賣家
                memVO.setSellerApprove(Boolean.TRUE);
                // for EC_COMPAMY
                memVO.setTel1(memVO.getPhone());
                memVO.setEmail1(memVO.getEmail());
                memVO.setType(CompanyTypeEnum.MEMBER.getCode());

                if( !checkBeforeSave(memVO) ){
                    return;
                }
                memberFacade.saveVO(memVO, this.getLoginMember(), JsfUtils.getRequestLocale(), false);
                logger.info("saveAdd with new store ... "+memVO.getMemberId());
            }else{
                // STORE 已存在，只能存 EC_MEMBER、EC_COMPANY、EC_STORE_USER
                memVO.setStoreId(storeVO.getId());// storeId
                // for EC_MEMBER
                memVO.setName(editVO.getNickname());// 使用公司簡稱當會員名稱
                
                memVO.setActive(Boolean.TRUE);
                memVO.setAdminUser(Boolean.FALSE);
                memVO.setMemberType(MemberTypeEnum.COMPANY.getCode());
                memVO.setDisabled(Boolean.FALSE);
                memVO.setTccDealer(Boolean.TRUE);// 台泥經銷商
                memVO.setTccDs(Boolean.FALSE);
                // 預設皆有權限
                memVO.setStoreOwner(Boolean.TRUE);// 第一筆帳號預設為 owner
                memVO.setFiUser(Boolean.TRUE);
                //memVO.setStoreOwner(sys.isTrue(vo.getStoreOwner()));
                //memVO.setFiUser(sys.isTrue(vo.getFinUser()));
                memVO.setVerifyCode(verifyCode);

                // for EC_COMPAMY
                //memVO.setCname(memVO.getName());// 公司名稱
                memVO.setTel1(memVO.getPhone());
                memVO.setEmail1(memVO.getEmail());
                //memVO.setAddr1(vo.getStateName());// 區域別也放到地址欄
                //memVO.setBrief(vo.getCategoryName());// 產業別也放在簡介欄
                memVO.setType(CompanyTypeEnum.MEMBER.getCode());

                logger.debug("saveAdd 3 memVO.getMemberId = "+memVO.getMemberId());
                if( !checkBeforeSave(memVO) ){
                    return;
                }
                logger.debug("saveAdd 4 memVO.getMemberId = "+memVO.getMemberId());
                memberFacade.saveVO(memVO, this.getLoginMember(), JsfUtils.getRequestLocale(), false);
                logger.info("saveAdd with existed store ... "+memVO.getMemberId());
            }
            // 控制每家 store 唯一 StoreOwner
            // 1. 無 StoreOwner，取所有管理員第一筆為 Owner
            // 2. 多 StoreOwner，除第一筆以外，改為非 StoreOwner
            if( memVO.getStoreId()!=null ){
                memberFacade.decideStoreOwner(memVO.getStoreId(), this.getLoginMember(), locale, false);
            }

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
    
    //<editor-fold defaultstate="collapsed" desc="getter & setter">   
    public MemberCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(MemberCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
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

    //</editor-fold>
}

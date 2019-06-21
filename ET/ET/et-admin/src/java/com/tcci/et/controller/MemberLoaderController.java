/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.controller.admin.AttachmentController;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.model.admin.UserLoaderVO;
import com.tcci.et.entity.EtMember;
import com.tcci.et.enums.MemberTypeEnum;
import com.tcci.et.facade.EtMemberFacade;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.VenderVO;
import com.tcci.et.model.criteria.MemberCriteriaVO;
import com.tcci.et.facade.EtVenderFacade;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.util.ExcelParserIntegerAccount;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fc.util.EntityComparator;
import com.tcci.fc.util.StringUtils;
import com.tcci.security.AESPasswordHash;
import com.tcci.security.AESPasswordHashImpl;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jason.yu
 */
@ManagedBean(name = "memberLoadController")
@ViewScoped
public class MemberLoaderController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 9;
    public static final String DATATABLE_RESULT = "dataExportForm:dtResult";
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @EJB private UserFacade userFacade;
    @EJB private TcGroupFacade groupFacade;
    @EJB private EtMemberFacade etMemberFacade;
    @EJB private EtVenderFacade etVenderFacade;
    
    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;//上傳Excel
    public void setAttachmentController(AttachmentController attachmentController) {
        this.attachmentController = attachmentController;
    }
    //</editor-fold>
    
    private final static String NULL_STR = "NA";
    private final static String USER_INFO = "USER_INFO";
    private final static  String YES = "YES";
    private final static  String NO = "NO";
    private List<MemberVO> loaderVOList;//查詢結果
    private List<MemberVO> importResultList;//匯入結果
    private List<MemberVO> importList;//待匯入清單
    private boolean finished = false;//是否完成下載
    private boolean isError = false;
    private static final boolean readonly = false;
    private static final boolean onlyUploadOnce = true;
    
    // 查詢條件
    MemberCriteriaVO criteriaVO;
    boolean includeDisabledUser;// 是否包含已刪除使用者(預設不勾選)
    MemberVO loaderVO;
    
    
    // keep datatable filter model
    private List<MemberVO> filterResultList;
//    private List<UserLoaderVO> filterUserLoaderVOList = null;
    private int countAfterFilter = 0; // 結果筆數(filter 後隨之異動)
    
    @PostConstruct
    private void init() {
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // 初始查詢條件
        loaderVO = new MemberVO();
        criteriaVO = new MemberCriteriaVO();
        includeDisabledUser = false;
        
        // 重設匯入區塊
        resetImportBlock();
    }
    public void triggerPostConstruct(){}
    public void reset() {
        resetImportBlock();
        importResultList = null;
    }
    
    private void resetImportBlock() {
        attachmentController.init(null, readonly, onlyUploadOnce);
        this.setFinished(false);
        importList = null;
    }
    
    /**
     * 重設查詢條件及結果
     */
    public void resetExport() {
        // 條件
        loaderVO = new MemberVO();
        criteriaVO = new MemberCriteriaVO();
        includeDisabledUser = false;
        
        // 結果
        loaderVOList = new ArrayList<>();
        filterResultList = new ArrayList<>();
        countAfterFilter = 0;
        
        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }
    
    /**
     * 清空錯誤訊息
     */
    public void clearMessages() {
        JsfUtils.clearMessageForComponent("messages");
    }
    
    /**
     * 判斷可否進行匯入
     * @return
     */
    public boolean enableFullImport(){
        boolean enableFullImportBtn = true;
        List<AttachmentVO> attachmentList = attachmentController.getAttachmentVOList();
        if(CollectionUtils.isEmpty(attachmentList)){
            return !enableFullImportBtn;
        }else{
            if(isFinished()){
                return !enableFullImportBtn;
            }else{
                return enableFullImportBtn;
            }
        }
    }
    
    /**
     * EXCEL 匯入
     */
    public void doFullImport() {
        List<AttachmentVO> list = attachmentController.getAttachmentVOList();
        
        if( list==null ){
            logger.error("doFullImport error list = null !");
        }
        
        for (AttachmentVO attachmentVO : list) {
            logger.debug("doFullImport attachmentVO = "+attachmentVO.getFileName());
            InputStream is = new ByteArrayInputStream(attachmentVO.getContent());
            
            ExcelParserIntegerAccount parser = new ExcelParserIntegerAccount();
            parser.setInputStream(is);
            Vector vector = null;
            try {
                vector = parser.parse();
            } catch (Exception ex) {
                String msg = "讀取Excel失敗!" + ex.getMessage();
                JsfUtils.addErrorMessage(msg);
            }
            importResultList = new ArrayList<>();
            importList = new ArrayList<>();
            try {
                addImportList(vector);
                
                for( MemberVO vo : importList){
                    handleVO(vo);
                    importResultList.add(vo);
                }
                
                String msg = "匯入Excel成功!" + "共"+ importList.size() +"筆,成功"+ importResultList.size() +"筆";
                JsfUtils.addSuccessMessage(msg);
                logger.info(msg);
            } catch (Exception ex) {
                String msg = "匯入Excel失敗!" + "共"+ importList.size() +"筆,成功"+ importResultList.size() +"筆! (請確定資料格式是否正確，無資料欄位請填入NA。)";
                JsfUtils.addErrorMessage(msg);
                logger.error("UserDataLoaderController.java => doFullImport", ex);
            }
            this.setFinished(true);
            resetImportBlock();
            break;
        }
    }
    
    /**
     * 多筆處理
     * @param vector
     */
    private void addImportList(Vector vector){
        logger.info("addImportList ...");
        if (vector.isEmpty()) {
            String msg = "No data in file!";
            JsfUtils.addSuccessMessage(msg);
            logger.info(msg);
            return;
        }
        
        logger.debug("=== vector == " + vector);
        for (int i = 0; i < vector.size(); i++) {
            Vector dataVector = (Vector) vector.get(i);
            addUserLoaderVO(dataVector);
        }
    }
    /**
     * 單筆處理
     * @param vector
     */
    private void addUserLoaderVO(Vector vector) {
        if (vector.isEmpty()) {
            String msg = "No data in row!";
            //FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, msg);
            logger.info(msg);
            return;
        }
        
        String key = (String) vector.get(0);
        if (USER_INFO.equals(key)) {
            MemberVO vo = convertMemberInfo(vector);
            importList.add(vo);
        }
    }
    
    /**
     * 轉換物件
     * @param vector
     * @return
     */
    private MemberVO convertMemberInfo(Vector vector) {
        MemberVO vo = new MemberVO();
        int i = 1;
        if (vector.size() >= i) { // 會員帳號
//            vo.setLoginAccount((String) vector.get(i++));
//            vo.setLoginAccount(vo.getLoginAccount().toLowerCase());
            String email = (String) vector.get(i++);
            vo.setLoginAccount(email.toLowerCase());
            vo.setEmail(email.toLowerCase());
        }
        if (vector.size() >= i) { // 姓名
            vo.setName((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 電話
//            vo.setPhone((String) vector.get(i++));
            String phone = (String) vector.get(i++);
            logger.info("phone:"+phone);
            vo.setPhone(phone);
        }
        if (vector.size() >= i) { //供應商代碼
//            vo.setVenderCodes((String) vector.get(i++));
            String venderCodes = (String) vector.get(i++);
            vo.setVenderCodes(venderCodes);
            vo.setApplyVenderCode(venderCodes);
        }
//        if (vector.size() >= i) { //供應商名稱
//            vo.setApplyVenderName((String) vector.get(i++));
//        }
        return vo;
    }
    
    /**
     * 維護以下資料
     * 1.EtMember
     * 2.vender
     * @param vo
     * @return
     */
    private void handleVO(MemberVO vo) {
        EtMember member = etMemberFacade.findByLoginAccount(vo.getLoginAccount());
        if(member == null){
//            member = new EtMember();
//            member.setType(MemberTypeEnum.COMPANY.getCode());//def
//            member.setDisabled(false);
        }else{
            vo.setMemberId(member.getId());
        }
        if(member!=null){
            if(!NULL_STR.equalsIgnoreCase(vo.getName())){
                member.setName(vo.getName());
            }
            if(!NULL_STR.equalsIgnoreCase(vo.getPhone())){
                member.setPhone(vo.getPhone());
            }
            member.setEmail(vo.getLoginAccount());
            if(!NULL_STR.equalsIgnoreCase(vo.getApplyVenderCode())){
                member.setApplyVenderCode(vo.getApplyVenderCode());
            }
            if(!NULL_STR.equalsIgnoreCase(vo.getApplyVenderName())){
                member.setApplyVenderName(vo.getApplyVenderName());
            }
        }
        
        etMemberFacade.save(member, this.getLoginUser(), this.isSimulated());
        
        vo.setType(MemberTypeEnum.COMPANY.getCode());//def
        
        // 供應商
        List<VenderVO> venderSelected = new ArrayList<>();
        String venderCodes = vo.getVenderCodes();
        logger.info("handleMember venderCodes:"+venderCodes);
        if(StringUtils.isBlank(venderCodes)){
            //do noting
        }else if(NULL_STR.equalsIgnoreCase(venderCodes)){
            //do noting
        }else{
            String[] codes = venderCodes.split(",");
            String mandt = criteriaVO.getType();
            if (codes != null && !NULL_STR.equalsIgnoreCase(mandt)) {
                for (String code : codes) {
                    BaseCriteriaVO criteriaLfa1 = new BaseCriteriaVO();
                    criteriaLfa1.setType(mandt);
                    criteriaLfa1.setCode(code);
//                    criteriaLfa1.setMaxResults(1);
                    List<VenderVO> result = etVenderFacade.findLfa1ByCriteria(criteriaLfa1);
                    logger.info("handleMember result:"+result.size());
                    if(CollectionUtils.isNotEmpty(result)){//lfa1 存在
                        if(member == null){
                            venderSelected.add(result.get(0));
                        }else{
                            criteriaLfa1.setMemberId(member.getId());
                            List<VenderVO> result2 = etVenderFacade.findByCriteria(criteriaLfa1);
                            logger.info("handleMember result2:"+result2.size());
                            if(CollectionUtils.isEmpty(result2)){//et_vender 不存在 該會員未設定此供應商
                                venderSelected.add(result.get(0));
                            }
                        }
                    }else{
                        String msg = "供應商不存在!供應商代碼=" + code + ",sapclient=" + mandt + "";
                        JsfUtils.addErrorMessage(msg);
                        throw new EJBException(msg);
                    }//end of else
                }//end of for
            }
        }
        logger.info("saveVO venderSelected:"+venderSelected.size());
        vo.setAddVenderList(venderSelected);
        String pwd = "";
        if( vo.getMemberId()==null ){// new
            // default password
//            pwd = GlobalConstant.DEF_PWD;
            //randon password
            pwd = StringUtils.genRandomString(97, 122, 6);// 六碼小寫英文
            
            AESPasswordHash aes = new AESPasswordHashImpl();
//            String encrypted = aes.encrypt(GlobalConstant.DEF_PWD);
            String encrypted = aes.encrypt(pwd);
            vo.setPwd(encrypted);
            
            //send mail
            etMemberFacade.sendPwdMail(member, pwd);
        }
        etMemberFacade.saveVO(vo, this.getLoginUser(), GlobalConstant.DEF_LOCALE.getLocale(), this.isSimulated());
        
//        cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_MEMBER_IMPORT, viewId, JsfUtils.getClientIP(),
//                vo.getMemberId(), true, this.getLoginUser(), this.isSimulated());
        //log紀錄密碼?!
        cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_MEMBER_IMPORT, viewId, JsfUtils.getClientIP(),
                vo.getMemberId(), ActivityLogEnum.U_MEMBER_IMPORT.getCode(), ActivityLogEnum.U_MEMBER_IMPORT.getName()+"("+pwd+")", true, this.getLoginUser(), this.isSimulated());
    }
    
    /**
     * query
     */
    public void query() {
        try {
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(":tabView:dataExportForm:tbl");
            this.filterResultList = null; // filterValue 初始化
            
            criteriaVO.setSetMaxResultsSize(GlobalConstant.DEF_MAX_RESULT_SIZE);//設定最大回傳筆數
            
            loaderVOList = etMemberFacade.findByCriteria(criteriaVO, GlobalConstant.DEF_LOCALE.getLocale());
            countAfterFilter = loaderVOList.size(); // before do filter set default value
            
            Collections.sort(loaderVOList, new EntityComparator<>("loginAccount"));
        } catch (Exception e) {
            JsfUtils.addErrorMessage("系統查詢發生錯誤!");
            logger.error("query exception :\n", e);
        }
        //printUserExportData();
    }
    
    //</editor-fold>
    
    
    /**
     * 處理 datatable 的 filter event
     * @param event
     */
    public void onFilter(AjaxBehaviorEvent event) {
        countAfterFilter = (filterResultList==null)? 0:filterResultList.size();
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
    public List<MemberVO> getLoaderVOList() {
        return loaderVOList;
    }

    public void setLoaderVOList(List<MemberVO> loaderVOList) {
        this.loaderVOList = loaderVOList;
    }
    
    public List<MemberVO> getImportResultList() {
        return importResultList;
    }
    
    public void setImportResultList(List<MemberVO> importResultList) {
        this.importResultList = importResultList;
    }
    public boolean isFinished() {
        return finished;
    }
    
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    public boolean isIsError() {
        return isError;
    }
    
    public void setIsError(boolean isError) {
        this.isError = isError;
    }
    public List<MemberVO> getImportList() {
        return importList;
    }
    
    public void setImportList(List<MemberVO> importList) {
        this.importList = importList;
    }
    
    public boolean isIncludeDisabledUser() {
        return includeDisabledUser;
    }
    
    public void setIncludeDisabledUser(boolean includeDisabledUser) {
        this.includeDisabledUser = includeDisabledUser;
    }
    
    public List<MemberVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<MemberVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public MemberVO getLoaderVO() {
        return loaderVO;
    }

    public void setLoaderVO(MemberVO loaderVO) {
        this.loaderVO = loaderVO;
    }

    public MemberCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(MemberCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }
    
    public int getCountAfterFilter() {
        return countAfterFilter;
    }
    
    public void setCountAfterFilter(int countAfterFilter) {
        this.countAfterFilter = countAfterFilter;
    }
    //</editor-fold>
}

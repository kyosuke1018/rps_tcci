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
import com.tcci.et.entity.EtOption;
import com.tcci.et.enums.MemberTypeEnum;
import com.tcci.et.facade.EtMemberFacade;
import com.tcci.et.facade.EtOptionFacade;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.VenderVO;
import com.tcci.et.model.criteria.MemberCriteriaVO;
import com.tcci.et.facade.EtVenderCategoryFacade;
import com.tcci.et.facade.EtVenderFacade;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.util.ExcelParserIntegerAccount;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fc.util.EntityComparator;
import com.tcci.security.AESPasswordHash;
import com.tcci.security.AESPasswordHashImpl;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jason.yu
 */
@ManagedBean(name = "venderCategoryLoadController")
@ViewScoped
public class VenderCategoryLoaderController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 8;
    public static final String DATATABLE_RESULT = "dataExportForm:dtResult";
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @EJB private EtOptionFacade etOptionFacade;
    @EJB private EtVenderFacade etVenderFacade;
    @EJB private EtVenderCategoryFacade etVenderCategoryFacade;
    
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
    private List<VenderVO> loaderVOList;//查詢結果
    private List<VenderVO> importResultList;//匯入結果
    private List<VenderVO> importList;//待匯入清單
    private boolean finished = false;//是否完成下載
    private boolean isError = false;
    private static final boolean readonly = false;
    private static final boolean onlyUploadOnce = true;
    
    // 查詢條件
    BaseCriteriaVO criteriaVO;
    boolean includeDisabledUser;// 是否包含已刪除使用者(預設不勾選)
    VenderVO loaderVO;
    
    
    // keep datatable filter model
    private List<VenderVO> filterResultList;
    private int countAfterFilter = 0; // 結果筆數(filter 後隨之異動)
    
    @PostConstruct
    private void init() {
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // 初始查詢條件
        loaderVO = new VenderVO();
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
        loaderVO = new VenderVO();
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
                
                for( VenderVO vo : importList){
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
            addLoaderVO(dataVector);
        }
    }
    /**
     * 單筆處理
     * @param vector
     */
    private void addLoaderVO(Vector vector) {
        if (vector.isEmpty()) {
            String msg = "No data in row!";
            //FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, msg);
            logger.info(msg);
            return;
        }
        
        String key = (String) vector.get(0);
        if (USER_INFO.equals(key)) {
            VenderVO vo = convertVenderVO(vector);
            importList.add(vo);
        }
    }
    
    /**
     * 轉換物件
     * @param vector
     * @return
     */
    private VenderVO convertVenderVO(Vector vector) {
        VenderVO vo = new VenderVO();
        int i = 1;
        if (vector.size() >= i) { // sap client
            vo.setMandt((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 代碼
            vo.setVenderCode((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 分類ID
            vo.setCids((String) vector.get(i++));
        }
        return vo;
    }
    
    /**
     * 維護以下資料
     * 1.EtMember
     * 2.vender
     * @param vo
     * @return
     */
    private void handleVO(VenderVO vo) {
        BaseCriteriaVO criteriaLfa1 = new BaseCriteriaVO();
        criteriaLfa1.setType(vo.getMandt());
        criteriaLfa1.setCode(vo.getVenderCode());
        List<VenderVO> result = etVenderFacade.findLfa1ByCriteria(criteriaLfa1);
        if(CollectionUtils.isNotEmpty(result)){//lfa1 存在
            // 供應商類別
            List<Long> selectedCategorys = new ArrayList<>();
            String cids = vo.getCids();
            if(StringUtils.isNotBlank(cids)){
                String[] cidStrs = cids.split(",");
                for (String cidStr : cidStrs) {
                    Long cid = Long.parseLong(cidStr);
                    EtOption entity = etOptionFacade.find(cid);
                    if(entity!=null && "tenderCategory".equals(entity.getType())){
                        selectedCategorys.add(cid);
                    }
                }
            }
            logger.info("saveVO selectedCategorys:"+selectedCategorys.size());
            if(CollectionUtils.isNotEmpty(selectedCategorys)){
                etVenderCategoryFacade.saveCategory(vo, selectedCategorys, this.getLoginUser());
            }
        }
    }
    
    /**
     * query
     */
    public void query() {
        try {
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(":tabView:dataExportForm:tbl");
            this.filterResultList = null; // filterValue 初始化
            
            criteriaVO.setActive(Boolean.TRUE);
            criteriaVO.setSetMaxResultsSize(GlobalConstant.DEF_MAX_RESULT_SIZE);//設定最大回傳筆數
            
            loaderVOList = etVenderCategoryFacade.findByCriteria(criteriaVO);
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
    public List<VenderVO> getLoaderVOList() {
        return loaderVOList;
    }

    public void setLoaderVOList(List<VenderVO> loaderVOList) {
        this.loaderVOList = loaderVOList;
    }
    
    public List<VenderVO> getImportResultList() {
        return importResultList;
    }
    
    public void setImportResultList(List<VenderVO> importResultList) {
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
    public List<VenderVO> getImportList() {
        return importList;
    }
    
    public void setImportList(List<VenderVO> importList) {
        this.importList = importList;
    }
    
    public boolean isIncludeDisabledUser() {
        return includeDisabledUser;
    }
    
    public void setIncludeDisabledUser(boolean includeDisabledUser) {
        this.includeDisabledUser = includeDisabledUser;
    }
    
    public List<VenderVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<VenderVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public VenderVO getLoaderVO() {
        return loaderVO;
    }

    public void setLoaderVO(VenderVO loaderVO) {
        this.loaderVO = loaderVO;
    }

    public BaseCriteriaVO getCriteriaVO() {
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

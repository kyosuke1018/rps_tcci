/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.model.admin.UserLoaderVO;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.util.ExcelParserIntegerAccount;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fc.util.EntityComparator;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author jason.yu
 */
@ManagedBean
@ViewScoped
public class UserDataLoaderController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 7;
    public static final String DATATABLE_RESULT = "dataExportForm:dtResult";
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @EJB private UserFacade userFacade;
    @EJB private TcGroupFacade groupFacade;
    
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
    private List<UserLoaderVO> userLoaderVOList;//查詢結果
    private List<UserLoaderVO> importResultList;//匯入結果
    private List<UserLoaderVO> importList;//待匯入清單
    private boolean finished = false;//是否完成下載
    private boolean isError = false;
    private static final boolean readonly = false;
    private static final boolean onlyUploadOnce = true;
    
    // 查詢條件
    UserLoaderVO userLoaderVO;  // 廠別、工號、姓名(繁簡)、AD帳號
    boolean includeDisabledUser;// 是否包含已刪除使用者(預設不勾選)
    
    // keep datatable filter model
    private List<UserLoaderVO> filterUserLoaderVOList = null;
    private int countAfterFilter = 0; // 結果筆數(filter 後隨之異動)
    
    @PostConstruct
    private void init() {
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // 初始查詢條件
        userLoaderVO = new UserLoaderVO();
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
        userLoaderVO = new UserLoaderVO();
        includeDisabledUser = false;
        
        // 結果
        userLoaderVOList = new ArrayList<UserLoaderVO>();
        filterUserLoaderVOList = new ArrayList<UserLoaderVO>();
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
            importResultList = new ArrayList<UserLoaderVO>();
            importList = new ArrayList<UserLoaderVO>();
            try {
                addImportList(vector);
                
                for( UserLoaderVO vo : importList){
                    TcUser user = handleTCUser(vo);
                    vo.setIsImported(true);
                    importResultList.add(vo);
                    // USER相關系統自動設定
                    //if( user.getId()>0 ){
                    //    usermgmtFacade.autoSetting(user, sessionController.getLoginTcUser());
                    //}
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
            UserLoaderVO vo = convertUserInfo(vector);
            importList.add(vo);
        }
    }
    
    /**
     * 轉換物件
     * @param vector
     * @return
     */
    private UserLoaderVO convertUserInfo(Vector vector) {
        UserLoaderVO vo = new UserLoaderVO();
        int i = 1;
        /*if (vector.size() >= i) { // 1:廠別
        vo.setFactoryFullName((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 2:部門
        vo.setDepartmentFullName((String) vector.get(i++));
        }*/
        if (vector.size() >= i) { // 3:工號
            vo.setEmployeeId((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 4:姓名
            vo.setEmployeeName((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 5:AD帳號
            vo.setLoginAccount((String) vector.get(i++));
            vo.setLoginAccount(vo.getLoginAccount().toLowerCase());
        }
        if (vector.size() >= i) { // 6:郵件帳號
            vo.setEmailAddress((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 7:群組代碼
            vo.setGroupNames((String) vector.get(i++));
        }
        /*if (vector.size() >= i) { // 8:廠別代碼
            vo.setFactorycodes((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 9:公司權限
            vo.setCompanies((String) vector.get(i++));
        }
        if (vector.size() >= i) { // 10:工廠群組
            vo.setFactoryGroups((String) vector.get(i++));
        }*/
        return vo;
    }
    
    /**
     * 維護以下資料
     * 1.TcUser
     * 2.UserInfo
     * 3.TcUsergroup
     * 4.UserFactory
     * @param vo
     * @return
     */
    private TcUser handleTCUser(UserLoaderVO vo) {
        TcUser user = userFacade.findUserByLoginAccount(vo.getLoginAccount());
        if (user == null) {
            user = new TcUser();
            user.setLoginAccount(vo.getLoginAccount());
            user.setDisabled(false);
        }
        
        if(!NULL_STR.equalsIgnoreCase(vo.getEmployeeId())){
            user.setEmpId(vo.getEmployeeId());
        }
        if(!NULL_STR.equalsIgnoreCase(vo.getEmployeeName())){
            user.setCname(vo.getEmployeeName());
        }
        if(!NULL_STR.equalsIgnoreCase(vo.getEmailAddress())){
            user.setEmail(vo.getEmailAddress());
        }
        
        userFacade.save(user, this.getLoginUser(), this.isSimulated());// TC_USER DB儲存
        
        // 群組 ADMINISTRATORS,...
        List<String> groupSelected = new ArrayList<String>();
        String groupNames = vo.getGroupNames();
        if(StringUtils.isEmpty(groupNames)){
            //do noting
        }else if(NULL_STR.equalsIgnoreCase(groupNames)){
            //do noting
        }else{
            String[] groupCodes = groupNames.split(",");
            if (groupCodes != null) {
                for (String groupCode : groupCodes) {
                    TcGroup group = groupFacade.findGroupByCode(groupCode);
                    if (group == null) {
                        vo.setIsError(true);
                        String msg = "3.群組代碼不存在!群組代碼=" + groupCode + ",AD帳號=" + vo.getLoginAccount() + "";
                        JsfUtils.addErrorMessage(msg);
                        throw new EJBException(msg);
                    } else {
                        groupSelected.add(group.getId().toString());
                    }//end of else
                }//end of for
            }
        }
        
        // 公司權限 tcc,tcc_cn,...
        List<String> companySelected = new ArrayList<String>();
        /*String companies = vo.getCompanies();
        if(StringUtils.isEmpty(companies)){
            //do noting
        }else if(NULL_STR.equalsIgnoreCase(companies)){
            //do noting
        }else{
            String[] companyCodes = companies.split(",");
            if (companyCodes != null) {
                companySelected.addAll(Arrays.asList(companyCodes));
            }
        }*/
        
        // 工廠 1021,1022,....
        List<String> factorySelected = new ArrayList<String>();
        /*if(NULL_STR.equalsIgnoreCase(vo.getFactorycodes())){
            //do noting
        }else if("ALL".equalsIgnoreCase(vo.getFactorycodes())){
            //TcUser editUser = userFactoryFacade.findUserByLoginAccount(vo.getLoginAccount());
            List<CmFactory> factoryList = cmFactoryFacade.findAll();
            for (CmFactory cmFactory : factoryList) {
                Long gid = cmFactory.getId();
                factorySelected.add(String.valueOf(gid));
            }
        }else{
            String[] factorycodes = vo.getFactorycodes().split(",");
            
            for (String factorycode : factorycodes) {
                CmFactory cmFactory = cmFactoryFacade.getByCode(factorycode);
                if(null == cmFactory){
                    vo.setIsError(true);
                    String msg = "4.廠別代碼不存在!廠別代碼=" + factorycode + ",AD帳號=" + vo.getLoginAccount() + "";
                    JsfUtils.addErrorMessage(msg);
                    throw new EJBException(msg);
                }
                Long gid = cmFactory.getId();
                factorySelected.add(String.valueOf(gid));
            }
        }*/
        
        // 工廠群組 (1)群組一,(2)群組二,....
        List<String> factoryGroupSelected = new ArrayList<String>();
        /*String factoryGroups = vo.getFactoryGroups();
        if(StringUtils.isEmpty(companies)){
            //do noting
        }else if(NULL_STR.equalsIgnoreCase(companies)){
            //do noting
        }else{
            String[] factoryGroupIds = factoryGroups.split(",");
            if (factoryGroupIds != null) {
                for(int i=0; i<factoryGroupIds.length; i++){
                    String id = factoryGroupIds[i];
                    int s = id.indexOf("(");
                    int e = id.indexOf(")");
                    if( s>=0 && e>=0 && e>s ){
                        id = id.substring(s+1, e);
                        factoryGroupSelected.add(id);
                    }
                }
            }
        }*/
        
        permissionFacade.update(user, groupSelected, companySelected,
                factorySelected, factoryGroupSelected, 0, sessionController.getLoginTcUser());// 權限DB儲存
        
        cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_USER_IMPORT, viewId, JsfUtils.getClientIP(),
                user.getId(), user.getLoginAccount(), user.getName(), true, this.getLoginUser(), this.isSimulated());
        
        return user;
    }
    
    /**
     * query
     */
    public void query() {
        try {
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(":tabView:dataExportForm:tbl");
            this.filterUserLoaderVOList = null; // filterValue 初始化
            
            userLoaderVOList = permissionFacade.findUserLoaderVOList(userLoaderVO, includeDisabledUser);
            countAfterFilter = userLoaderVOList.size(); // before do filter set default value
            
            Collections.sort(userLoaderVOList, new EntityComparator<UserLoaderVO>("loginAccount"));
        } catch (Exception e) {
            JsfUtils.addErrorMessage("系統查詢發生錯誤!");
            logger.error("query exception :\n", e);
        }
        //printUserExportData();
    }
    
    private void setUserLoaderVO(UserLoaderVO vo, TcUser user) {
        if(StringUtils.isEmpty(vo.getEmployeeName())){
            vo.setEmployeeName(NULL_STR);
        }
        if(StringUtils.isEmpty(vo.getEmployeeId())){
            vo.setEmployeeId(NULL_STR);
        }
        vo.setDisableAccount((user.getDisabled() != null && user.getDisabled().booleanValue() == true) ? YES : NO);
    }
    
    /**
     * 設定角色代碼
     * @param vo
     * @param user
     */
    private void setGroupCodes(UserLoaderVO vo,TcUser user) {
        String groupCodes = null;
        StringBuilder sb = new StringBuilder();
        if (user.getTcUsergroupCollection() != null) {
            for (TcUsergroup ug : user.getTcUsergroupCollection()) {
                TcGroup group = ug.getGroupId();
                boolean append = false;
                if (sb.length() != 0) {
                    append = true;
                }
                sb.append(group.getCode());
                sb.append(",");
            }
            groupCodes = sb.toString();
            
            if (!StringUtils.isEmpty(groupCodes) && groupCodes.lastIndexOf(",") == (groupCodes.length() - 1)) {
                groupCodes = groupCodes.substring(0, groupCodes.length() - 1);
            }
        }
        
        if (groupCodes.equals("")) {
            groupCodes = NULL_STR;
        }
        
        vo.setGroupNames(groupCodes);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="not used">
    /**
     * not used
     * @param document
     */
    public void postProcessXLS(Object document) {
        //System.out.println("postProcessXLS start");
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        String[] titles = {"userloader.key",
            "factory.name.number",
            "userloader.department.name.number",
            "userloader.employee.id",
            "userloader.employee.name",
            "userloader.user.ad.account",
            "userloader.user.email.address",
            "userloader.user.group.name",
            "userloader.is.booking.notify.bebooked.user",
            "userloader.is.booking.notify.booking.user",
            "userloader.is.disable.user"};
        int columnIndex = 0;
        for (String title : titles) {
            HSSFCell cell = row.getCell(columnIndex);
            if (cell == null) {
                cell = row.createCell(columnIndex);
            }
            cell.setCellValue(JsfUtils.getResourceTxt(title));
            columnIndex++;
        }
    }
    /**
     * not used
     * @param vector
     * @return
     */
    private String getRowData(Vector vector) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vector.size(); i++) {
            sb.append((String) vector.get(i));
            sb.append(",");
        }
        return sb.toString();
    }
    /**
     * not used
     */
    private void printUserExportData() {
        if (userLoaderVOList != null) {
            for (UserLoaderVO vo : userLoaderVOList) {
                logger.debug("USER_INFO,"
                        //+ vo.getFactoryFullName() + ","
                        //+ vo.getDepartmentFullName() + ","
                        + vo.getEmployeeId() + ","
                        + vo.getEmployeeName() + ","
                        + vo.getLoginAccount() + ","
                        + vo.getEmailAddress() + ","
                        + vo.getGroupNames() + ","
                        //+ vo.getBeBookedNotify() + ","
                        //+ vo.getBookingNotify() + ","
                        + vo.getDisableAccount());
            }
        }
    }
    
    /**
     * not used
     * replace by dataExporter
     * @return
     */
    /*public String exportPermissionAction(){
    TCCJasperReport report =new TCCJasperReport();
    try {
    //List factoryCodeList = (List)userLoaderVOList;
    String rootPath="/report/material";
    //String rootPath="";
    String fileName="pp-permission";
    Map<String,Object> params= new HashMap<String,Object>();
    HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
    ExportFormatEnum exportFormatEnum = ExportFormatEnum.XLS;
    OutputStream outputStream = response.getOutputStream();
    report.execute( userLoaderVOList, rootPath, fileName,fileName, params, response, exportFormatEnum, outputStream);
    FacesContext.getCurrentInstance().responseComplete();
    } catch (JRException e) {
    String msg = " Error:" + e.getLocalizedMessage();
    FacesMessage fMsg =new FacesMessage(FacesMessage.SEVERITY_ERROR ,msg,msg);
    FacesContext.getCurrentInstance().addMessage(null, fMsg);
    } catch (Exception e) {
    String msg = " Error:" + e.getLocalizedMessage();
    FacesMessage fMsg =new FacesMessage(FacesMessage.SEVERITY_ERROR ,msg,msg);
    FacesContext.getCurrentInstance().addMessage(null, fMsg);
    }
    return null;*/
    /*
    * String reportName = "pp-permission.jasper";
    * java.io.InputStream is = UserDataLoaderController.class.getClassLoader().getResourceAsStream(reportName);
    * HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
    * Map parameters = new HashMap();
    * //parameters.put("ReportTitle", " Title ");
    * try {
    * JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
    * JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(userLoaderVOList));
    *
    * OutputStream out = response.getOutputStream();
    * ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    * JasperExportManager.exportReportToXmlStream(jasperPrint, byteArrayOutputStream);
    * response.setHeader("Cache-Control", "max-age=0");
    * response.setHeader("Content-Disposition", "attachment; filename=pp-permission.xls");
    * response.setContentType("application/vnd.ms-excel");
    * out.write(byteArrayOutputStream.toByteArray());
    * out.flush();
    * out.close();
    * FacesContext.getCurrentInstance().responseComplete();
    *
    * } catch (JRException e) {
    * e.printStackTrace();
    * String msg = " Error:" + e.getLocalizedMessage();
    * FacesMessage fMsg =new FacesMessage(FacesMessage.SEVERITY_ERROR ,msg,msg);
    * FacesContext.getCurrentInstance().addMessage(null, fMsg);
    * } catch (Exception e) {
    * e.printStackTrace();
    * String msg = " Error:" + e.getLocalizedMessage();
    * FacesMessage fMsg =new FacesMessage(FacesMessage.SEVERITY_ERROR ,msg,msg);
    * FacesContext.getCurrentInstance().addMessage(null, fMsg);
    * }
    * return null;
    *
    */
    //}
    //</editor-fold>
    
    /**
     * 處理 datatable 的 filter event
     * @param event
     */
    public void onFilter(AjaxBehaviorEvent event) {
        countAfterFilter = (filterUserLoaderVOList==null)? 0:filterUserLoaderVOList.size();
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
    public List<UserLoaderVO> getUserLoaderVOList() {
        return userLoaderVOList;
    }
    
    public void setUserLoaderVOList(List<UserLoaderVO> userLoaderVOList) {
        this.userLoaderVOList = userLoaderVOList;
    }
    public List<UserLoaderVO> getImportResultList() {
        return importResultList;
    }
    
    public void setImportResultList(List<UserLoaderVO> importResultList) {
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
    public List<UserLoaderVO> getImportList() {
        return importList;
    }
    
    public void setImportList(List<UserLoaderVO> importList) {
        this.importList = importList;
    }
    
    public UserLoaderVO getUserLoaderVO() {
        return userLoaderVO;
    }
    
    public void setUserLoaderVO(UserLoaderVO userLoaderVO) {
        this.userLoaderVO = userLoaderVO;
    }
    
    public boolean isIncludeDisabledUser() {
        return includeDisabledUser;
    }
    
    public void setIncludeDisabledUser(boolean includeDisabledUser) {
        this.includeDisabledUser = includeDisabledUser;
    }
    
    public List<UserLoaderVO> getFilterUserLoaderVOList() {
        return filterUserLoaderVOList;
    }
    
    public void setFilterUserLoaderVOList(List<UserLoaderVO> filterUserLoaderVOList) {
        this.filterUserLoaderVOList = filterUserLoaderVOList;
    }
    
    public int getCountAfterFilter() {
        return countAfterFilter;
    }
    
    public void setCountAfterFilter(int countAfterFilter) {
        this.countAfterFilter = countAfterFilter;
    }
    //</editor-fold>
}

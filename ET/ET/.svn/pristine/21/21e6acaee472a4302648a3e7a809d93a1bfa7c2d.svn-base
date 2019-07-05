/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.controller.global;

import com.tcci.cm.enums.ActionEnum;
import com.tcci.cm.enums.BooleanTypeEnum;
import com.tcci.cm.enums.OrgTypeEnum;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.enums.OptionEnum;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcGroupComparator;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.util.StringUtils;
import com.tcci.et.enums.AuthLevelEnum;
import com.tcci.et.enums.ContentStatusEnum;
import com.tcci.et.enums.DataTypeEnum;
import com.tcci.et.enums.CriteriaDateTypeEnum;
import com.tcci.et.enums.ImageSizeEnum;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.PhotoGalleryEnum;
import com.tcci.et.enums.PublicationEnum;
import com.tcci.et.enums.VenderSrcEnum;
import com.tcci.et.enums.VideoLibraryEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
@ApplicationScoped
@Named("ui")
public class CommonUIController {
    private static final Logger logger = LoggerFactory.getLogger(CommonUIController.class);

    @EJB TcGroupFacade tcGroupFacade;
    @EJB SysResourcesFacade sysResourcesFacade;
    
    private List<SelectItem> cmOrgMultiSelect;// 系統組織多選
    private List<SelectItem> userOrgOptions;//user所屬組織選單 
    private List<SelectItem> companyOrgOptions;//系統組織 公司(無parent)
    private List<SelectItem> orgTypeOtions;
    
    private List<SelectItem> boolOptions; // 有/無選單
    
    private List<SelectItem> activityLogOptions; // 操作類別
    private List<SelectItem> monthOptions; // 月份選單
    
    private List<SelectItem> contentStatusOptions; // 狀態別選單
    private List<SelectItem> publicationOptions; // 刊物型態選單
    private List<SelectItem> dataTypeOptions; // 資料型態選單
    private List<SelectItem> criteriaDateTypeOptions; // 資料型態選單
    private List<SelectItem> sysOptions; // 資料別選單
    private List<SelectItem> VenderSrcOptions;// 供應商來源
    
    private List<SelectItem> langWebSiteOptions; // 網站語系

    private List<SelectItem> authLevelOptions;// 權限階層
       
    @PostConstruct
    public void init(){
        logger.debug("CommonUIController init ...");
        // 月份
        monthOptions = buildMonthOptions();
        // 組織類型
        orgTypeOtions = buildOrgTypeOptions();
        // 有/無選單
        boolOptions = buildBoolOptions();
        // 操作類別
        activityLogOptions = buildActivityLogOptions();
        
        // 狀態別選單
        contentStatusOptions = buildContentStatusOptions();
        // 刊物型態選單
        publicationOptions = buildPublicationOptions();
        // 資料型態選單
        dataTypeOptions = buildDataTypeOptions();
        // 日期時間條件類型
        criteriaDateTypeOptions = buildCriteriaDateTypeOptions();
        
        // 網站語系
        langWebSiteOptions = buildLangWebSiteOptions();
        // 權限階層
        authLevelOptions = buildAuthLevelOptions();
        
        // 資料別選單
        sysOptions = buildSysOptions(1);
        
        // 供應商來源
        VenderSrcOptions = buildVenderSrcOptions();
    }

    public void refresh(){
        init();
        JsfUtils.buildSuccessCallback();
    }
    
    public String getContextPath(){
        return JsfUtils.getContextPath();
    }

    //<editor-fold defaultstate="collapsed" desc="for url">    
    /**
     * GET URL for UI
     * @param relUrl
     * @param imgType
     * @return 
     */
    public String genWebImgUrl(String relUrl, String imgType){// 產生外網圖片連結URL
        return sysResourcesFacade.genWebImgUrl(relUrl, imgType);
    }
    public String genAdminImgUrl(String relUrl, String imgType){// 產生管理介面圖片連結URL
        return sysResourcesFacade.genAdminImgUrl(relUrl, imgType);
    }
    public String genWebFileUrl(String relUrl){// 產生外網檔案連結URL
        return sysResourcesFacade.genWebFileUrl(relUrl);
    }
    public String genAdminFileUrl(String relUrl){// 產生管理介面檔案連結URL
        return sysResourcesFacade.genAdminFileUrl(relUrl);
    }
    
    public String genWebImgUrlS(String relUrl){
        return sysResourcesFacade.genWebImgUrl(relUrl, "S");
    }
    public String genWebImgUrlB(String relUrl){
        return sysResourcesFacade.genWebImgUrl(relUrl, "B");
    }
    public String genWebImgUrlO(String relUrl){
        return sysResourcesFacade.genWebImgUrl(relUrl, "O");
    }
    
    public String genAdminImgUrlS(String relUrl){
        return sysResourcesFacade.genAdminImgUrl(relUrl, "S");
    }
    public String genAdminImgUrlB(String relUrl){
        return sysResourcesFacade.genAdminImgUrl(relUrl, "B");
    }
    public String genAdminImgUrlO(String relUrl){
        return sysResourcesFacade.genAdminImgUrl(relUrl, "O");
    }
    
    public String genJsfImgUrlS(String relUrl){
        String url = sysResourcesFacade.genJsfImgUrl(relUrl, "S");
        logger.debug("genJsfImgUrlS url = "+url);
        return url;
    }
    public String genJsfImgUrlB(String relUrl){
        return sysResourcesFacade.genJsfImgUrl(relUrl, "B");
    }
    public String genJsfImgUrlO(String relUrl){
        return sysResourcesFacade.genJsfImgUrl(relUrl, "O");
    }
    //</editor-fold>
 
    //<editor-fold defaultstate="collapsed" desc="UI get ImageSizeEnum code">
    public String getOriginalImgCode(){// 原圖
        return ImageSizeEnum.ORIGINAL.getCode();
    }
    public String getBigImgCode(){// 大縮圖
        return ImageSizeEnum.BIG.getCode();
    }
    public String getSmallImgCode(){// 小縮圖
        return ImageSizeEnum.SMALL.getCode();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="UI get ActionEnum code">
    public String getQueryCode(){// 查詢
        return ActionEnum.QUERY.getCode();
    }
    public String getViewCode(){// 檢視
        return ActionEnum.VIEW.getCode();
    }
    public String getAddCode(){// 新增
        return ActionEnum.ADD.getCode();
    }
    public String getModifyCode(){// 修改
        return ActionEnum.MODIFY.getCode();
    }
    public String getDeleteCode(){// 刪除
        return ActionEnum.DELETE.getCode();
    }
    public String getAddSubCode(){// 建立子項目
        return ActionEnum.ADDSUB.getCode();
    }
    public String getUploadSubCode(){// 上傳
        return ActionEnum.UPLOAD.getCode();
    }
    public String getChoiceCode(){// 選取
        return ActionEnum.CHOICE.getCode();
    }
    //</editor-fold>
 
    //<editor-fold defaultstate="collapsed" desc="UI get PhotoGalleryEnum code">
    public String getCustomImgCode(){// 自訂相簿/圖庫
        return PhotoGalleryEnum.CUSTOM.getCode();
    }
    public String getDocImgCode(){// 文章插圖
        return PhotoGalleryEnum.DOC.getCode();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="UI get VideoLibraryEnum code">
    public String getCustomCode(){// 自訂影片庫
        return VideoLibraryEnum.CUSTOM.getCode();
    }
    public String getDocCode(){// 文章穿插檔案
        return VideoLibraryEnum.DOC.getCode();
    }
    public String getVideoCode(){// 影片
        return VideoLibraryEnum.VIDEO.getCode();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="UI get PublicationEnum code">
    public String getNewsCode(){// 最新消息
        return PublicationEnum.NEWS.getCode();
    }
    public String getUploadImageCode(){// 圖片
        return PublicationEnum.IMAGE.getCode();
    }
    public String getUploadDocCode(){// 文件
        return PublicationEnum.DOC.getCode();
    }
    public String getUploadVideoCode(){// 影片
        return PublicationEnum.VIDEO.getCode();
    }
    public String getTempCode(){// 系統暫存
        return PublicationEnum.TEMP.getCode();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UI get DataTypeEnum code">
    public String getHtmlCode(){// 自製網頁
        return DataTypeEnum.HTML.getCode();
    }
    public String getUploadCode(){// 上傳檔案
        return DataTypeEnum.UPLOAD.getCode();
    }
    public String getLinkCode(){// 連結
        return DataTypeEnum.LINK.getCode();
    }
    //public String getAlbumCode(){// 相簿
    //    return DataTypeEnum.ALBUM.getCode();
    //}
    public String getFolderCode(){// 資料夾
        return DataTypeEnum.FOLDER.getCode();
    }
    //</editor-fold>
    
    /**
     * 供應商來源選單
     * @return 
     */
    List<SelectItem> buildVenderSrcOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(VenderSrcEnum item : VenderSrcEnum.values()){
             options.add(new SelectItem(item.getCode(), item.getDisplayName()));
        }
        return options;
    }
    
    /**
     * 權限階層
     * @return 
     */
    List<SelectItem> buildAuthLevelOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(AuthLevelEnum item : AuthLevelEnum.values()){
            options.add(new SelectItem(item.getCode(), item.getDisplayName()));
        }
        return options;
    }
    
    /**
     * 選項資料別選單
     * @return 
     */
    List<SelectItem> buildSysOptions(int category){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(OptionEnum item : OptionEnum.values()){
            if( item.getCategory()==category ){
                options.add(new SelectItem(item.getCode(), item.getDisplayName()));
            }
        }
        return options;
    }
               
    /**
     * 網站語系
     * @return 
     */
    List<SelectItem> buildLangWebSiteOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(LanguageEnum item : LanguageEnum.values()){
            if( item.isWebSite() ){
                // 使用 ShortCode
                options.add(new SelectItem(item.getShortCode(), item.getDisplayName()));
            }
        }
        return options;
    }
               
    /**
     * 刊物型態選單
     * @return 
     */
    List<SelectItem> buildPublicationOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(PublicationEnum item : PublicationEnum.richContentList()){
            options.add(new SelectItem(item.getCode(), item.getDisplayName()));
        }
        return options;
    }
    
    /**
     * 狀態別選單
     */
    List<SelectItem> buildCriteriaDateTypeOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(CriteriaDateTypeEnum item : CriteriaDateTypeEnum.values()){
            options.add(new SelectItem(item.getCode(), item.getDisplayName()));
        }
        return options;
    }
    
    /**
     * 狀態別選單
     */
    List<SelectItem> buildDataTypeOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(DataTypeEnum item : DataTypeEnum.values()){
            options.add(new SelectItem(item.getCode(), item.getDisplayName()));
        }
        return options;
    }
    
    /**
     * 狀態別選單
     */
    List<SelectItem> buildContentStatusOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(ContentStatusEnum item : ContentStatusEnum.values()){
            options.add(new SelectItem(item.getCode(), item.getDisplayName()));
        }
        return options;
    }
    
    // Y/N
    public String getYes(){
        return BooleanTypeEnum.YES.getYn();
    }
    public String getNo(){
        return BooleanTypeEnum.NO.getYn();
    }
    
    // 狀態顯示顏色
    public String getContentStatusEnumColor(String statusCode){
        ContentStatusEnum enum1 = ContentStatusEnum.getFromCode(statusCode);
        return (enum1==null? ContentStatusEnum.DRAFT.getColor():enum1.getColor());
    }

    //<editor-fold defaultstate="collapsed" desc="跨系統通用選單">    
    /**
     * 月份選單
     * @return 
     */
    private  List<SelectItem> buildMonthOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(int i=1; i<=12; i++){
            options.add((new SelectItem(Integer.toString(i), Integer.toString(i))));
        }
        return options;
    }
    
    /**
     * 有/無選單
     * @return 
     */
    private List<SelectItem> buildBoolOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        options.add(new SelectItem(Boolean.FALSE, "無"));
        options.add(new SelectItem(Boolean.TRUE, "有"));
        
        return options;
    }
    
    /**
     * 操作類別
     * @return 
     */
    private List<SelectItem> buildActivityLogOptions() {
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(ActivityLogEnum item : ActivityLogEnum.values()){
            options.add(new SelectItem(item.getCode(), item.getCode() + "-" + item.getDisplayName()));
        }
        return options;
    }    
        
    /**
     * 系統角色群組選單
     * @param includeNoSelect
     * @return 
     */
    private List<SelectItem> buildGroupOptions(boolean includeNoSelect){
        List<SelectItem> options = new ArrayList<SelectItem>();
        if( includeNoSelect ){
            options.add(new SelectItem(Long.valueOf(0), "---"));
        }
        
        List<TcGroup> groupList = tcGroupFacade.findAll();
        // 排序 by code
        TcGroupComparator tcGroupComparator = new TcGroupComparator();
        Collections.sort(groupList, tcGroupComparator);
        
        if( groupList!=null ){
            for (TcGroup group : groupList) {
                if (includeNoSelect) {
                    options.add(new SelectItem(group.getId(), group.getCode() + "-" + group.getName()));
                } else {
                    options.add(new SelectItem(group.getId(), group.getCode() + "-" + group.getName()));
                }
            }
        }
        return options;
    }
    
    /**
     * 組織類別選單
     */
    private List<SelectItem> buildOrgTypeOptions(){
        List<SelectItem> options = new ArrayList<SelectItem>();
        for(OrgTypeEnum item : OrgTypeEnum.values()){
            options.add(new SelectItem(item.getCode(), item.getDisplayName()));
        }
        return options;
    }
    //</editor-fold>
    
    /**
     * 最多顯示字串長度
     * @param ori
     * @return 
     */
    public String showMaxStr(String ori){
        return StringUtils.showMaxTxt(ori, GlobalConstant.STR_MAX_SHOW_LEN);
    }
    public String showMaxTxt(String ori){
        return StringUtils.showMaxTxt(ori, GlobalConstant.TXT_MAX_SHOW_LEN);
    }
    public String showMaxMsg(String ori){
        return StringUtils.showMaxTxt(ori, GlobalConstant.MSG_MAX_SHOW_LEN);
    }
    public String showMaxTxt(String ori, int len){
        return StringUtils.showMaxTxt(ori, len);
    }
    
    //<editor-fold defaultstate="collapsed" desc="for GlobalConstant">    
    public int getMaxContactsCriteriaNum(){
        return GlobalConstant.MAX_CONTACTS_CRITERIA_NUM;
    }

    public int getMaxDateCriteriaNum(){
        return GlobalConstant.MAX_DATE_CRITERIA_NUM;
    }
    
    public int getImageAdminWidth(){
        return GlobalConstant.WIDTH_ADMIN_IMG;
    }
    
    public int getImageAdminHeight(){
        return GlobalConstant.HEIGHT_ADMIN_IMG;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">    
    public TcGroupFacade getTcGroupFacade() {
        return tcGroupFacade;
    }

    public void setTcGroupFacade(TcGroupFacade tcGroupFacade) {
        this.tcGroupFacade = tcGroupFacade;
    }

    public List<SelectItem> getVenderSrcOptions() {
        return VenderSrcOptions;
    }

    public void setVenderSrcOptions(List<SelectItem> VenderSrcOptions) {
        this.VenderSrcOptions = VenderSrcOptions;
    }

    public List<SelectItem> getAuthLevelOptions() {
        return authLevelOptions;
    }

    public void setAuthLevelOptions(List<SelectItem> authLevelOptions) {
        this.authLevelOptions = authLevelOptions;
    }

    public List<SelectItem> getLangWebSiteOptions() {
        return langWebSiteOptions;
    }

    public void setLangWebSiteOptions(List<SelectItem> langWebSiteOptions) {
        this.langWebSiteOptions = langWebSiteOptions;
    }

    public List<SelectItem> getPublicationOptions() {
        return publicationOptions;
    }

    public void setPublicationOptions(List<SelectItem> publicationOptions) {
        this.publicationOptions = publicationOptions;
    }

    public List<SelectItem> getCriteriaDateTypeOptions() {
        return criteriaDateTypeOptions;
    }

    public void setCriteriaDateTypeOptions(List<SelectItem> criteriaDateTypeOptions) {
        this.criteriaDateTypeOptions = criteriaDateTypeOptions;
    }

    public List<SelectItem> getContentStatusOptions() {
        return contentStatusOptions;
    }

    public void setContentStatusOptions(List<SelectItem> contentStatusOptions) {
        this.contentStatusOptions = contentStatusOptions;
    }

    public List<SelectItem> getMonthOptions() {
        return monthOptions;
    }

    public void setMonthOptions(List<SelectItem> monthOptions) {
        this.monthOptions = monthOptions;
    }

    public List<SelectItem> getActivityLogOptions() {
        return activityLogOptions;
    }

    public void setActivityLogOptions(List<SelectItem> activityLogOptions) {
        this.activityLogOptions = activityLogOptions;
    }

    public List<SelectItem> getBoolOptions() {
        return boolOptions;
    }

    public void setBoolOptions(List<SelectItem> boolOptions) {
        this.boolOptions = boolOptions;
    }

    public List<SelectItem> getOrgTypeOtions() {
        return orgTypeOtions;
    }

    public void setOrgTypeOtions(List<SelectItem> orgTypeOtions) {
        this.orgTypeOtions = orgTypeOtions;
    }
    
    public List<SelectItem> getCmOrgMultiSelect() {
        return cmOrgMultiSelect;
    }

    public void setCmOrgMultiSelect(List<SelectItem> cmOrgMultiSelect) {
        this.cmOrgMultiSelect = cmOrgMultiSelect;
    }
    
    public List<SelectItem> getUserOrgOptions() {
        return userOrgOptions;
    }

    public void setUserOrgOptions(List<SelectItem> userOrgOptions) {
        this.userOrgOptions = userOrgOptions;
    }

    public List<SelectItem> getDataTypeOptions() {
        return dataTypeOptions;
    }

    public void setDataTypeOptions(List<SelectItem> dataTypeOptions) {
        this.dataTypeOptions = dataTypeOptions;
    }

    public List<SelectItem> getSysOptions() {
        return sysOptions;
    }

    public void setSysOptions(List<SelectItem> sysOptions) {
        this.sysOptions = sysOptions;
    }
    
    public List<SelectItem> getCompanyOrgOptions() {
        return companyOrgOptions;
    }

    public void setCompanyOrgOptions(List<SelectItem> companyOrgOptions) {
        this.companyOrgOptions = companyOrgOptions;
    }
    //</editor-fold>

}

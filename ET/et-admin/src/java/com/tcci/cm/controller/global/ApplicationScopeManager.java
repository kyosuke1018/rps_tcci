/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.global;

import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.facade.doc.CmDocumentFacade;
import com.tcci.cm.model.admin.OnlineUsers;
import com.tcci.cm.util.ConfigUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.util.WebUtils;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.content.TcApplicationdataFacade;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.FileUtils;
import com.tcci.et.facade.KbPublicationFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * AP Scoped 資訊管理
 * @author Peter
 */
@ManagedBean(name = "app")
@ApplicationScoped
public class ApplicationScopeManager extends AbstractController {   
    @EJB SysResourcesFacade sysResourcesFacade;
    @EJB CmDocumentFacade cmDocumentFacade;
    @EJB TcApplicationdataFacade applicationdataFacade;
    @EJB KbPublicationFacade publicationFacade;
    
    private boolean announcement;// 公告
    private Date announcementFileTime; // 最近公告檔時間
    private StreamedContent downloadFile;// 公告download

    @PostConstruct
    public void init() {
        OnlineUsers.action(OnlineUsers.ACTION_GET);// call first
    }
    
    public String getCallBackErrorMsg(){
        return "網頁已過期，請按[F5]鍵重新整理後再試。";
    }
    public String getExceptionMsg(){
        return "系統發生錯誤，請 Email 此畫面給系統管理員，並告知執行動作，以利盡速為您排除問題。";
    }
    public String getErrorMsg(){
        return "(重新整理後仍無法執行，請 Email 此畫面給系統管理員，並告知執行動作，以利儘速為您排除問題，謝謝。)";
    }
    
    public String getDebugHitMsg(){
        StringBuilder sb = new StringBuilder();
        sb.append("【執行主機】:").append(WebUtils.getHostAddress());
        sb.append("，");
        sb.append("【執行時間】:").append(DateUtils.format(new Date()));
        
        return sb.toString();
    }

    /**
     * 顯示部分字串
     * @param ori
     * @param len
     * @return 
     */
    public String showPartialStr(String ori, int len){
        if( ori==null || ori.length()<=len ){
            return ori;
        }
        
        return ori.substring(0, len) + "...";
    }
    
    /**
     * getHostURL
     * @return http://serverName:ServerPort
     */
    public String getHostURL() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String url = request.getContextPath();
        return url;
    }        

    /**
     * 取得Server IP Address
     * @return 
     */
    public String getServerIpAddress(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getLocalAddr();
    }
    
    /**
     * 取得系統時間
     * @return 
     */
    public String getSystime(){
        DateFormat df = new SimpleDateFormat(GlobalConstant.FORMAT_DATETIME);
        Date now = new Date();        
        return df.format(now);
    }
    
    /**
     * 取得系統時間 String
     * @return 
     */
    public String getSystimeStr(){
        DateFormat df = new SimpleDateFormat(GlobalConstant.FORMAT_DATETIME_STR);
        Date now = new Date();        
        return df.format(now);
    }    
    
    /**
     * Get system default format string
     * @return 
     */
    public String getDateFormat(){
        return GlobalConstant.FORMAT_DATE;
    }
    public String getTimeFormat(){
        return GlobalConstant.FORMAT_TIME;
    }
    public String getDateTimeFormat(){
        return GlobalConstant.FORMAT_DATETIME;
    }
    public String getIntFormat(){
        return GlobalConstant.FORMAT_INTEGER;
    }
    
    public String getDateTimeString(){
        return DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR);
    }
    
    public String formatDateTime(Date inDate){
        return DateUtils.formatDateString(inDate, GlobalConstant.FORMAT_DATETIME);
    }
    
    public String formatDate(Date inDate){
        return DateUtils.formatDateString(inDate, GlobalConstant.FORMAT_DATE);
    }
    
    /**
     * 線上使用者
     * @return 
     */
    public List<TcUser> getOnlineUsers(){
        return OnlineUsers.action(OnlineUsers.ACTION_VIEW);
    }

    //<editor-fold defaultstate="collapsed" desc="for Announcement">
    /**
     * 變更是否公告
     */
    public void changeAnnouncement(){
        logger.info("changeAnnouncement announcement = "+announcement);
        if( announcement ){
            if( announcementFileTime == null ){
                announcement = false;
                logger.error("changeAnnouncement error : no announcement file !");
                JsfUtils.addErrorMessage("無公告檔!");
            }
        }
    }
    
    /**
     * 取得公告檔資訊
     * @return 
     */
    public File getAnnouncementInfo(){
        String root = cmDocumentFacade.getDefFilePath();// 儲存根目錄
        root = (root.endsWith(File.separator))? root:root+File.separator;
        String path = root + GlobalConstant.ANNOUNCEMENT_FOLDER;
        
        File dir = new File(path);
        if( dir.exists() && dir.isDirectory() ){
            File[] files = dir.listFiles();
            announcementFileTime = null;
            int idx = -1;
            for(int i=0; files!=null && i<files.length; i++){
                if( files[i]!=null && files[i].isFile() && files[i].canRead() ){
                    Date lastModified = new Date(files[i].lastModified());
                    logger.debug(files[i].getName() + " " + DateUtils.format(lastModified));
                    if( announcementFileTime==null ){
                        announcementFileTime = lastModified;
                        idx = i;
                        logger.debug("announcementFileTime = " + DateUtils.format(announcementFileTime));
                    }else{
                        if( announcementFileTime.before(lastModified) ){
                            announcementFileTime = lastModified;
                            idx = i;
                            logger.debug("announcementFileTime = " + DateUtils.format(announcementFileTime));
                        }
                    }
                }
            }
            if( idx>-1 ){
                return files[idx];
            } 
        }else{
            FileUtils.toDir(path); // 建立目錄
        }
        
        return null;
    }
    
    /**
     * 下載公告檔
     */
    public void downloadAnnouncementFile(){
        logger.debug("downloadAnnouncementFile ...");
        File theFile = getAnnouncementInfo();
        if( theFile==null ){
            return; 
        }

        String fileType = "application/octet-stream";
        String fileExt = FileUtils.getExtFileName(theFile);
        
        if( theFile.exists() && theFile.isFile() ){
            try{
                String outFileName = URLEncoder.encode("ICS系統公告"+fileExt, "UTF-8"); //匯出中文檔名編碼處理
                
                downloadFile = new DefaultStreamedContent(new FileInputStream(theFile), fileType, outFileName);
            }catch(FileNotFoundException e){
                logger.error("downloadAnnouncementFile FileNotFoundException:\n", e);
            } catch (UnsupportedEncodingException e) {
                logger.error("downloadAnnouncementFile UnsupportedEncodingException:\n", e);
            }
        }else{
            logger.error("downloadAnnouncementFile file not exists : "+ theFile.getName());
        }
    }
    
    /**
     * 上傳公告檔處理
     *
     * @param event
     */
    public void handleAnnouncementUpload(FileUploadEvent event) {
        logger.debug("handleAnnouncementUpload ...");
        
        try {
            if (event != null && event.getFile() != null) {
                String root = cmDocumentFacade.getDefFilePath();// 儲存根目錄
                root = (root.endsWith(File.separator))? root:root+File.separator;
                String path = root + GlobalConstant.ANNOUNCEMENT_FOLDER;
                String filename = path + File.separator + "Announcement" + Long.toString(System.currentTimeMillis());
                
                InputStream input = null;
                OutputStream output = null;
                try {
                    String srcName = event.getFile().getFileName();
                    String fileExt = event.getFile().getFileName().substring(srcName.lastIndexOf("."));
                    filename = filename + fileExt;
                    
                    input = event.getFile().getInputstream();
                    output = new FileOutputStream(new File(filename));
                    IOUtils.copy(input, output);
                    
                    getAnnouncementInfo();
                } catch(IOException e){
                    logger.error("handleAnnouncementUpload exception ...", e);
                } finally {
                    if( input!=null ){ IOUtils.closeQuietly(input); }
                    if( output!=null ){ IOUtils.closeQuietly(output); }
                }
            }
        } catch (Exception e) {
            logger.error("handleAnnouncementUpload Exception \n", e);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for URL">
    /**
     * 依 view Id 取的索引字串
     * @param viewId
     * @return 
     */
    public String getKeyFromViewId(String viewId){
        int i = viewId.lastIndexOf("/");
        int j = viewId.lastIndexOf(".");
        
        if( i>=0 ){
            if( j>0 ){
                return viewId.substring(i+1, j);
            }else{
                return viewId.substring(i+1);
            }
        }
        return "";
    }
    
    /**
     * 外部 AP URL 前置網址 
     * @param apName
     * @return 
     */
    public String getUrlPrefix(String apName){
        String name = "url.prefix." + apName;
        return ConfigUtils.getPropFromJNDI(GlobalConstant.JNDI_NAME_PRIVATE, name, "");
    }
    //</editor-fold>
 
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public boolean isAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(boolean announcement) {
        this.announcement = announcement;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public Date getAnnouncementFileTime() {
        return announcementFileTime;
    }

    public void setAnnouncementFileTime(Date announcementFileTime) {
        this.announcementFileTime = announcementFileTime;
    }

    //</editor-fold>
}

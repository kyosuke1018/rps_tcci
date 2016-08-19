/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.web.controller;

import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.model.QueryBuilder;
import com.tcci.solr.client.model.TcQueryResponse;
import com.tcci.solr.client.model.TcSolrDocument;
import com.tcci.solr.client.model.TcSolrSource;
import com.tcci.solr.client.proxy.TcSolrQueryProxy;
import com.tcci.solr.client.proxy.TcSolrUpdateProxy;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
@ManagedBean(name = "indexController")
@ViewScoped
public class IndexController implements Serializable {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ManagedProperty(value = "#{fileController}")
    private FileController fileController;
    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }
    
    private List<TcSolrDocument> resultList; 
    private String keyword;
    private boolean firstTime;
    private long foundNum;
    private double qtime;
    private String[] defKeyWords = {"采购", " 電腦", "資訊", "水泥"};
    
    private String filename;
    private String title;
    private String fileAbsolutePath;
    
    @PostConstruct
    public void init(){
        logger.debug("IndexController init ...");
        resultList = new ArrayList<TcSolrDocument>();
        keyword = "";
        firstTime = true;
        foundNum = 0;
        qtime = 0;
    }

    /**
     *  查詢
     */
    public void doQuery(){
        firstTime = false;
        try{
            run2StepSolrQuery(keyword);
        }catch(Exception e){
             showError("查詢失敗!");
             logger.error("doQuery exception:\n", e);
        }
    }

    /**
     * Keyword Auto Complete 
     * @param txt
     * @return 
     */
    public List<String> autoCompleteKeyword(String txt){
        List<String> list = new ArrayList<String>();
        
        for(String word : defKeyWords){
            if( txt==null || txt.isEmpty() || word.contains(txt) ){
                list.add(word);
            }
        }
        
        return list;
    }
    
    /**
     * 兩階段查詢 (文件授權資訊)
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public void run2StepSolrQuery(String keyword) throws SolrProxyException, SolrServerException{
        Calendar calS = Calendar.getInstance();
        // Get TcSolrQueryProxy Instance
        TcSolrQueryProxy solrQueryProxy = new TcSolrQueryProxy();
        // 關鍵字查詢條件
        QueryBuilder queryBuilder = QueryBuilder.newInstanceByKeyword(keyword);
        
        // Solr 第一次查詢 : 條件 = AP代碼與關鍵字
        List<Long> idList = solrQueryProxy.customIdQuery(queryBuilder); // solr query only return cid field
        logger.info("idList.size = "+((idList!=null)?idList.size():0));

        if( idList==null || idList.isEmpty() ){
            return;
        }
        
        /**
         * Insert your code there 1:
         * 取 idList 與各別AP "文件授權" 與 "DB其他條件" 交集的 ID
         */
        /*idList.remove(2);
        idList.remove(5);
        idList.remove(9);*/
        
        /**
         * Insert your code there 2:
         * 承上，取目前頁面要顯示資料ID
         */
        /*idList.remove(11);
        idList.remove(12);*/
        
         if( idList.isEmpty() ){
            showMsg("您有權檢視的文件中，無符合查詢條件的項目!");
            return;
        }
         
        // Solr 第二次查詢: 條件 = AP代碼與cid 比對 (原條件要保留，為了 highlight)
        QueryBuilder qbNew = queryBuilder.addCIdCritiria(idList);
        
        TcQueryResponse queryResponse = solrQueryProxy.simpleQuery(qbNew);
        // logger.debug(queryResponse.toString()); // for debug
        // SolrUtils.dumpQueryResponse(queryResponse); // for debug
        if( queryResponse!=null ){
            if( queryResponse.getStatus() == TcSolrConfig.RESPONSE_SOLR_OK ){
                resultList = queryResponse.getTcSolrDocumentList();
                
                foundNum = (resultList!=null)?resultList.size():0;// 筆數
            }else{
                showError("test2StepQuery error ==> queryResponse.getStatus() = "+queryResponse.getStatus());
            }
        }else{
            showError("test2StepQuery error ==> queryResponse = null");
        }
        
        Calendar calE = Calendar.getInstance();
        qtime = (calE.getTimeInMillis()-calS.getTimeInMillis())/1000.0;// 查詢時間
    }
    
    /**
     * 將上傳檔匯入 Solr 
     */
    public void runSolrLazyExtract() throws IOException, SolrProxyException{
        TcSolrUpdateProxy solrUpdateProxy = new TcSolrUpdateProxy();
        
        TcSolrSource tcSolrSource = new TcSolrSource();
        // 來源AP代號 (用 contextPath)
        // tcSolrSource.setSource("PMIS"); // Set in TCApplicationListener contextInitialized()
        // 客製 ID
        tcSolrSource.setCid(System.currentTimeMillis());
        // 標題
        tcSolrSource.setTitle(title);
        // 檔名
        tcSolrSource.setFilename(fileController.getServerFileName());
        // 相對路徑
        tcSolrSource.setPath(fileController.getServerRelDir());

        // 匯入 Solr 
        solrUpdateProxy.add(tcSolrSource);
    }
    
    
    /**
     * HighlightSnippet 自訂顯示格式
     * @param tcSolrDocument 
     */
    public String formatHighlightSnippet(TcSolrDocument tcSolrDocument){
        return tcSolrDocument.formatHighlightSnippet("<B>", "</B>");
    }  
    
    /**
     * 自 Solr 刪除
     * @param tcSolrDocument 
     */
    public void removeFromSolr(TcSolrDocument tcSolrDocument){
        TcSolrUpdateProxy solrUpdateProxy = new TcSolrUpdateProxy();
        TcSolrSource tcSolrSource = new TcSolrSource();
        // 客製 ID
        tcSolrSource.setCid(tcSolrDocument.getCid());
        try {
            solrUpdateProxy.deleteByCid(tcSolrSource); // 刪除
            
            resultList.remove(tcSolrDocument);// 自結果列表移除
            
            showMsg("刪除完成!");
        } catch (Exception ex) {
            logger.error("removeFromSolr exception: \n", ex);
            showError("刪除失敗("+ex.toString()+")!");
        }
    }
    
    /**
     * 開啟上傳視窗
     */
    public void openUploadDlg(){
        filename = null;
        fileAbsolutePath = null;
        title = null;
    }
    
    /**
     * 上傳處理
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {       
        logger.debug("handleFileUpload event = " + event);
        
        try {
            if( event!=null && event.getFile()!=null ){
                // 記錄上傳檔
                fileController.handleFileUpload(event);
                
                filename = event.getFile().getFileName();

                logger.debug("handleImpFileUpload fileAbsolutePath = " + fileAbsolutePath);
            }
        } catch (Exception e) {
            showError("上傳失敗!");
            logger.error("handleImpFileUpload exception:\n", e);
        }
    }
    
    /**
     * 匯入 Solr
     */
    public void saveDocument(){
        logger.debug("saveDocument ...");
        if( title==null || title.isEmpty() ){
            title = fileController.getFile().getFileName();
        }
        
        fileController.saveDocument();
        
        fileAbsolutePath = fileController.getFileAbsolutePath();
        logger.debug("handleImpFileUpload fileAbsolutePath = " + fileAbsolutePath);
        try {
            // 匯入 Solr Server
            runSolrLazyExtract();
            
            showMsg("檔案已上傳。("+fileAbsolutePath+")");
        } catch (Exception ex) {
            showError("上傳 Solr 失敗!");
            logger.error("saveDocument exception:\n", ex);
        }
    }
    
    /**
     * 顯示錯誤訊息
     * @param msg 
     */
    public void showError(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, "錯誤訊息"));
        logger.error(msg);
    }
    
    /**
     * 顯示錯誤訊息
     * @param msg 
     */
    public void showMsg(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "提示訊息"));
        logger.info(msg);
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getFoundNum() {
        return foundNum;
    }

    public void setFoundNum(long foundNum) {
        this.foundNum = foundNum;
    }

    public double getQtime() {
        return qtime;
    }

    public void setQtime(double qtime) {
        this.qtime = qtime;
    }

    public List<TcSolrDocument> getResultList() {
        return resultList;
    }

    public void setResultList(List<TcSolrDocument> resultList) {
        this.resultList = resultList;
    }    

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }
    //</editor-fold>
}

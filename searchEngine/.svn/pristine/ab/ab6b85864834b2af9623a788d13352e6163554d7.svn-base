package com.tcci.solr.client.proxy;

import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.enums.SolrTransactionEnum;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.model.QueryBuilder;
import com.tcci.solr.client.model.QueryField;
import com.tcci.solr.client.model.TcSolrDocument;
import com.tcci.solr.client.model.TcSolrSource;
import com.tcci.solr.client.util.SolrUtils;
import com.tcci.sso.client.SSOClient;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.SSOHttpSolrServer;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 執行 Solr Update 代理程式
 * @author Peter
 */
public class TcSolrUpdateProxy {
    private static final Logger logger = LoggerFactory.getLogger(TcSolrUpdateProxy.class);
    
    private String solrServiceUrl = TcSolrConfig.getSolrURL();
    private String casserver = TcSolrConfig.getSsoTicketURL();
    private String username = TcSolrConfig.getRestUser();
    private String password = TcSolrConfig.getRestPwd();
    private boolean extractOnly = false;
    
    public TcSolrUpdateProxy(){
    }

    public TcSolrUpdateProxy(String url){
        solrServiceUrl = url;
    }
    
    /**
     * 新增文件 (背景執行)
     * @return 
     */
    public int add(TcSolrSource tcSolrSource) throws IOException, SolrProxyException{
        return upload(solrServiceUrl, TcSolrConfig.SOLR_URL_UPLOAD, SolrTransactionEnum.CREATE, tcSolrSource);
    }
    
    /**
     * 變更文件 (背景執行)
     * @return 
     */
    public int update(TcSolrSource tcSolrSource) throws IOException, SolrProxyException{
        return upload(solrServiceUrl, TcSolrConfig.SOLR_URL_UPLOAD, SolrTransactionEnum.UPDATE, tcSolrSource);
    }
    
    /**
     * 異動 Solr 文件  ==> Call lazyExtract Servlet
     * @param solrServiceUrl
     * @param actionURL
     * @param operation
     * @param tcSolrSource 
     */
    private int upload(String solrServiceUrl, String actionURL, SolrTransactionEnum operation, TcSolrSource tcSolrSource) 
            throws IOException, SolrProxyException{
        // 輸入檢核
        checkUpdateParams(tcSolrSource);
        
        StringBuilder sb = new StringBuilder()
                .append(solrServiceUrl).append(actionURL)
                .append("?op=").append(operation.getCode())
                .append("&source=").append(tcSolrSource.getSource())
                .append("&cid=").append(tcSolrSource.getCid());
        
        if( tcSolrSource.getTitle()!=null ){
            sb.append("&title=").append(URLEncoder.encode(tcSolrSource.getTitle(), "UTF-8"));
        }
        
        if( tcSolrSource.getDescription()!=null ){
            sb.append("&description=").append(URLEncoder.encode(tcSolrSource.getDescription(), "UTF-8"));
        }
        
        if( tcSolrSource.getFilename()!=null ){
            sb.append("&filename=").append(URLEncoder.encode(tcSolrSource.getFilename(), "UTF-8"));
        }
        if( tcSolrSource.getPath()!=null ){
            sb.append("&path=").append(URLEncoder.encode(tcSolrSource.getPath(), "UTF-8"));
        }
        
        String serviceURL = sb.toString();
        logger.debug("serviceURL = "+serviceURL);
        // call solr lazy extract by SSOClient 
        //return remoteSolrCall(serviceURL);
        return remoteSolrCall(serviceURL);// for post
    }
    
    /**
     * Extract content from a rich document (not support SSO)
     * 若 Solr Server 有 SSO 保護，此 Method 只能在 loacl 執行
     * @param tcSolrDocument
     * @return
     * @throws IOException
     * @throws SolrServerException 
     */
    public int extract(TcSolrSource tcSolrSource) throws IOException, SolrServerException, SolrProxyException{
        // 輸入檢核
        checkUpdateParams(tcSolrSource);
        
        // 取得 SolrServer，設定 Extract URL
        SolrServer server = new HttpSolrServer(solrServiceUrl);
        ContentStreamUpdateRequest req = new ContentStreamUpdateRequest(TcSolrConfig.SOLR_URL_EXTRACT);
        
        TcSolrDocument tcSolrDocument = new TcSolrDocument(tcSolrSource);
        // 取得上傳檔資訊
        String filename = tcSolrDocument.getFilename();
        String path = tcSolrDocument.getPath();
        String fullfilename = path + File.separator + filename;
        
        File fs = new File(fullfilename);
        long size = fs.length();
        // Get File Content Type
        // need JDK7
        //Path inf = Paths.get(fullfilename);
        //String probeContentType = Files.probeContentType(inf);
        //logger.info("probeContentType = "+probeContentType);
        // use activation-x.jar &　META-INF\mime.types
        String contentType = new MimetypesFileTypeMap().getContentType(fs);
        
        // 加入檔案 至 ContentStreamUpdateRequest
        req.addFile(fs, contentType);
        
        // 加入參數 至 ContentStreamUpdateRequest
        // unique key
        req.setParam("literal.id", tcSolrDocument.getId());
        // source AP ccustom info
        req.setParam("literal.source", tcSolrDocument.getSource());// source AP
        req.setParam("literal.cid", new Long(tcSolrDocument.getCid()).toString());// customized PK id
        req.setParam("literal.title", tcSolrDocument.getTitle());
        req.setParam("literal.description", tcSolrDocument.getDescription());
        // 
        req.setParam("literal.filename", filename);
        req.setParam("literal.path", path);
        req.setParam("literal.size", new Long(size).toString());
        //req.setParam("literal.extracted", Boolean.valueOf(tcSolrDocument.isExtracted()).toString());
        
        // 萃取文件內容原則
        req.setParam("captureAttr", "false");
        req.setParam("uprefix", "ignored_");// ignored no user data
        req.setParam("fmap.content", "ignored_content"); // ignore manay no use meta content
        String tag = getCaptureTagByContentType(contentType); // 依 ContentType 決定要攫取的 Tag 內容
        req.setParam("capture", tag);// capture content within tag
        req.setParam("fmap."+tag, "content");// map tag to content field

        if( extractOnly ){
            req.setParam("extractOnly", "true");// for test show extraction result
        }
        
        // call solr extract service
        logger.info("solr extract fullfilename = "+fullfilename);
        req.setMethod(SolrRequest.METHOD.POST);// 此 Method 只能在 loacl 執行，不可於SSO環境使用

        NamedList<Object> result = server.request(req);
        server.commit();
        
        if( extractOnly ){
            SolrUtils.dumpResultNamedList(result);// for test when extractOnly
            return TcSolrConfig.RESPONSE_SOLR_OK;
        }
        
        UpdateResponse updateResponse = new UpdateResponse();
        updateResponse.setResponse(result);
        int status = updateResponse.getStatus();

        if( status==TcSolrConfig.RESPONSE_SOLR_OK ){
            logger.info("solr extract success: status = "+status+" fullfilename = "+fullfilename);
        }else{
            logger.error("solr extract fail: status = "+status+" fullfilename = "+fullfilename);
        }
        
        return status;
    }
    
    /**
     * Client Side : 刪除 By Id
     */
    public int deleteByCid(TcSolrSource tcSolrSource) throws IOException, SolrProxyException{
        // 輸入檢核
        checkDeleteByCidParams(tcSolrSource);
        
        StringBuilder sb = new StringBuilder()
                .append(solrServiceUrl).append(TcSolrConfig.SOLR_URL_DELETE)
                .append("?op=").append(SolrTransactionEnum.DELETE.getCode())
                .append("&source=").append(tcSolrSource.getSource())
                .append("&cid=").append(tcSolrSource.getCid());
        
        String serviceURL = sb.toString();
        logger.debug("serviceURL = "+serviceURL);
        
        // call solr deleteByQuery by SSOClient 
        return remoteSolrCall(serviceURL);
    }
    
    /**
     * 遠端呼叫 Solr (需SSO)
     * @param serviceURL
     * @return 
     */
    public int remoteSolrCall(String serviceURL, List<NameValuePair> pairList) throws IOException{
        HttpResponse response;
        if( TcSolrConfig.SSO_ENABLED ){
            if( TcSolrConfig.SSO_SUPPORT_POST ){
                // response = SSOClient.callServiceByPost(casserver, username, password, serviceURL, pairList); // for http post
            }else{
                response = SSOClient.callService(casserver, username, password, serviceURL);
            }
        }else{
            response = SolrUtils.callService(serviceURL);
        }
        
        int statusCode = HttpStatus.SC_NOT_FOUND;
        if (response != null) {
            String responseString = EntityUtils.toString(response.getEntity());
            statusCode = response.getStatusLine().getStatusCode();
            logger.info("statusCode = "+statusCode);            
            logger.info("responseString = "+responseString.substring(0, Math.min(1024, responseString.length())));
        }
        
        return statusCode;
    }
    
    
    public int remoteSolrCall(String serviceURL) throws IOException{
        return remoteSolrCall(serviceURL, null);
    }
    
    /**
     * Server Side : 刪除 (目前針對CID)
     */
    public int deleteByQuery(TcSolrSource tcSolrSource){
        int status = TcSolrConfig.RESPONSE_SOLR_FAIL;
        try{
            QueryBuilder qb = new QueryBuilder();
            qb.append(new QueryField("cid", Long.valueOf(tcSolrSource.getCid()).toString()));
            String deleteQueryStr = qb.toTcSolrQuery(tcSolrSource.getSource());
            logger.info("deleteByQuery qb.toTcSolrQuery() = " + deleteQueryStr);
                    
            SolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());
            UpdateResponse updateResponse = server.deleteByQuery(deleteQueryStr);
            server.commit();
            
            status = updateResponse.getStatus();
        }catch(Exception e){
            logger.error("deleteByQuery Exception:", e);
        }
        return status;
    } 
    
    /**
     * 檢查 Update Request 參數
     */
    public void checkUpdateParams(TcSolrSource tcSolrSource) throws SolrProxyException{
        if( tcSolrSource==null ){
            logger.error(SolrProxyException.ERROR_NO_UPDATE_PARAM);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_PARAM);
        }
        // 來源系統
        if( tcSolrSource.getSource()==null || tcSolrSource.getSource().isEmpty() ){
            // 預設 Source 代碼 (一般為 contextPath)
            if( TcSolrConfig.getSourceAP()!=null && !TcSolrConfig.getSourceAP().isEmpty() ){
                tcSolrSource.setSource(TcSolrConfig.getSourceAP());
            }else{
                logger.error(SolrProxyException.ERROR_NO_UPDATE_SOURCE);
                throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_SOURCE);
            }
        }
        // 客製ID
        if( tcSolrSource.getCid()==0 ){
            logger.error(SolrProxyException.ERROR_NO_UPDATE_CID);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_CID);
        }
        // 標題
        if( tcSolrSource.getTitle()==null || tcSolrSource.getTitle().isEmpty() ){
            logger.error(SolrProxyException.ERROR_NO_UPDATE_TITLE);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_TITLE);
        }
        // 檔名
        if( tcSolrSource.getFilename()==null || tcSolrSource.getFilename().isEmpty() ){
            logger.error(SolrProxyException.ERROR_NO_UPDATE_FILENAME);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_FILENAME);
        }
        // 實體檔案路徑
        if( tcSolrSource.getPath()==null || tcSolrSource.getPath().isEmpty() ){
            logger.error(SolrProxyException.ERROR_NO_UPDATE_PATH);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_PATH);
        }
    }
    
    /**
     * 檢查 deleteByCid Request 參數
     */
    public void checkDeleteByCidParams(TcSolrSource tcSolrSource) throws SolrProxyException{
        if( tcSolrSource==null ){
            logger.error(SolrProxyException.ERROR_NO_UPDATE_PARAM);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_PARAM);
        }
        // 來源系統
        if( tcSolrSource.getSource()==null || tcSolrSource.getSource().isEmpty() ){
            // 預設 Source 代碼 (一般為 contextPath)
            if( TcSolrConfig.getSourceAP()!=null && !TcSolrConfig.getSourceAP().isEmpty() ){
                tcSolrSource.setSource(TcSolrConfig.getSourceAP());
            }else{
                logger.error(SolrProxyException.ERROR_NO_UPDATE_SOURCE);
                throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_SOURCE);
            }
        }
        // 客製ID
        if( tcSolrSource.getCid()==0 ){
            logger.error(SolrProxyException.ERROR_NO_UPDATE_CID);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_UPDATE_CID);
        }
    }
    
    /**
     * 依 ContentType 決定要攫取的 Tag 內容
     * @return 
     */
    public String getCaptureTagByContentType(String contentType){
        String tag;
        
        if( contentType.equals(TcSolrConfig.CONTENT_TYPE_PDF) 
                || contentType.equals(TcSolrConfig.CONTENT_TYPE_DOC) 
                || contentType.equals(TcSolrConfig.CONTENT_TYPE_DOCX)
                || contentType.equals(TcSolrConfig.CONTENT_TYPE_PPT)
                || contentType.equals(TcSolrConfig.CONTENT_TYPE_PPTX) ){// for word、pdf、ppt
            tag = "p";
        }else if( contentType.equals(TcSolrConfig.CONTENT_TYPE_XLS)
                || contentType.equals(TcSolrConfig.CONTENT_TYPE_XLSX) ){// for excel (or only capture 'td' if need sheet name)
            tag = "div";
        }else{
            tag = "body";
        }
        
        return tag;
    }
    
    /**
     * 取得 SSOHttpSolrServer
     * @return 
     */
    public SolrServer getHttpSolrServer(){
        if( TcSolrConfig.SSO_ENABLED ){
            SSOHttpSolrServer server = new SSOHttpSolrServer(solrServiceUrl);
            return server;
        }else{
            HttpSolrServer server = new HttpSolrServer(solrServiceUrl);
            server.setConnectionTimeout(TcSolrConfig.DEF_CONNECT_TIMEOUT); // 5 sec
            server.setDefaultMaxConnectionsPerHost(TcSolrConfig.DEF_MAX_CONNECT_PER_HOST);
            server.setMaxTotalConnections(TcSolrConfig.DEF_MAX_TOTAL_CONNECT);
            return server;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter"> 
    public String getSolrServiceUrl() {
        return solrServiceUrl;
    }

    public void setSolrServiceUrl(String solrServiceUrl) {
        this.solrServiceUrl = solrServiceUrl;
    }

    public boolean isExtractOnly() {
        return extractOnly;
    }

    public void setExtractOnly(boolean extractOnly) {
        this.extractOnly = extractOnly;
    }

    public String getCasserver() {
        return casserver;
    }

    public void setCasserver(String casserver) {
        this.casserver = casserver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //</editor-fold> 

}

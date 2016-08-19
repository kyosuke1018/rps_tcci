package com.tcci.solr.client.proxy;

import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.enums.QueryDefTypeEnum;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.model.QueryBuilder;
import com.tcci.solr.client.model.TcQueryResponse;
import com.tcci.solr.client.model.TcSolrDocument;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.SSOHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SimpleParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 執行 Solr Query 代理程式
 * @author Peter
 */
public class TcSolrQueryProxy extends SolrQuery {
    private static final Logger logger = LoggerFactory.getLogger(TcSolrQueryProxy.class);
    
    private String solrServiceUrl = TcSolrConfig.getSolrURL();
    
    public TcSolrQueryProxy(){
    }
    
    public TcSolrQueryProxy(String url){
        solrServiceUrl = url;
    }
    
    /**
     * 取得 SSOHttpSolrServer
     * @return 
     */
    //public HttpSolrServer getHttpSolrServer(){
    //    HttpSolrServer server = new HttpSolrServer(solrServiceUrl);
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
    
    /**
     * 預設查詢參數設定
     */
    private void prepareDefQueryParams(QueryBuilder queryBuilder){
        prepareBasicQueryParams(queryBuilder);// 準備基本查詢參數
        
        // 設定預設完整資訊最大回傳筆數
        if( this.getRows()==null ){
            this.setRows(TcSolrConfig.DEF_MAX_RESULT_ROWS);
        }
                
        // fl: field list (所有欄位及比對得分)
        if( this.getFields()!=null ){
            //this.add("fl", TcSolrConfig.FIELD_LIST_RESULT);
            this.set(CommonParams.FL, TcSolrConfig.FIELD_LIST_RESULT);
        }
        
        // Highlighting defaults
        this.setHighlight(true);
        this.setHighlightSnippets(TcSolrConfig.DEF_HIGHLIGHT_SNIPPET_NUM);
        this.setHighlightFragsize(TcSolrConfig.DEF_HIGHLIGHT_FRAG_SIZE);
        this.setHighlightSimplePre(TcSolrConfig.DEF_HIGHLIGHT_PER);
        this.setHighlightSimplePost(TcSolrConfig.DEF_HIGHLIGHT_POST);
        for(int i=0; TcSolrConfig.DEF_HIGHLIGHT_FIELDS!=null && i<TcSolrConfig.DEF_HIGHLIGHT_FIELDS.length; i++){
            this.addHighlightField(TcSolrConfig.DEF_HIGHLIGHT_FIELDS[i]);
        }
    }
    
    /**
     * 只回傳 custom ID 的查詢
     * @param queryBuilder 
     */
    private void prepareCidQueryParams(QueryBuilder queryBuilder){
        prepareBasicQueryParams(queryBuilder);// 準備基本查詢參數
        // 設定預設完整資訊最大回傳筆數
        if( this.getRows()==null ){
            this.setRows(TcSolrConfig.DEF_MAX_CID_ROWS);
        }
        if( this.getSortField()==null ){
            this.setSort(TcSolrConfig.FIELD_CUSTOM_ID, SolrQuery.ORDER.desc);// 取較新的資料
        }
        // fl: field list (所有欄位及比對得分)
        //this.add("fl", TcSolrConfig.FIELD_CUSTOM_ID);
        this.set(CommonParams.FL, TcSolrConfig.FIELD_CUSTOM_ID);
    }
    
    /**
     * 準備基本查詢參數
     * @param queryBuilder 
     */
    private void prepareBasicQueryParams(QueryBuilder queryBuilder){
        if( queryBuilder.isSingleQueryValue() ){// 單一關鍵字字串比對
            // 單一關鍵字字串比對，使用 edismax 查詢模式
            //this.add(QueryParsing.DEFTYPE, QueryDefTypeEnum.EDISMAX.getCode());
            this.add(TcSolrConfig.FIELD_QUERY_DEFINE_TYPE, QueryDefTypeEnum.EDISMAX.getCode());
            this.add(CommonParams.Q, queryBuilder.getQueryValue());
            this.add(SimpleParams.QF, queryBuilder.toSolrQuery());
        }else{
            // 不同欄位有不同關鍵字字串比對，使用標準查詢模式
            this.add(CommonParams.Q, queryBuilder.toTcSolrQuery());
        }

        if( this.getSortField()==null || this.getSortField().isEmpty() ){
            this.setSort(TcSolrConfig.FIELD_CUSTOM_ID, SolrQuery.ORDER.desc);// 最新資料在前
        }
    }

    /**
     * 搭配客製文件授權查詢
     * @param keyword
     * @param compList
     * @param accessible
     * @return
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public TcQueryResponse customAuthQuery(String keyword, List<Long> compList, boolean accessible) throws SolrProxyException, SolrServerException{
        QueryBuilder queryBuilder = QueryBuilder.newInstanceByKeyword(keyword);
        
        return customAuthQuery(queryBuilder, compList, accessible);
    }
    
    /**
     * 搭配客製文件授權查詢
     * @param queryBuilder
     * @param compList
     * @param accessible
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public TcQueryResponse customAuthQuery(QueryBuilder queryBuilder, List<Long> compList, boolean accessible) throws SolrProxyException, SolrServerException{
        if( (accessible && (compList==null || compList.isEmpty())) ){
            logger.info("customAuthQuery 無文件授權資訊!");
            return new TcQueryResponse();
        }
        
        // Solr 第一次查詢 : 條件 = AP代碼與關鍵字
        List<Long> idList = customIdQuery(queryBuilder); // solr query only return cid field
        if( idList==null || idList.isEmpty() ){
            logger.info("customAuthQuery 無符合條件的文件!");
            return new TcQueryResponse();
        }
        
        if( accessible ){// 可否存取
            idList.retainAll(compList);// 交集
        }else{
            idList.removeAll(compList);// 差集
        }
        
        if( idList.isEmpty() ){
            logger.info("customAuthQuery 可量文件授權後，無符合條件的文件!");
            return new TcQueryResponse();
        }

        // Solr 第二次查詢: 條件 = AP代碼與cid 比對 (原條件要保留，為了 highlight)
        QueryBuilder qbNew = queryBuilder.addCIdCritiria(idList);
        
        // 完整資訊回傳查詢
        return simpleQuery(qbNew);
    }
    
    /**
     * solr query only return cid field
     * @param queryBuilder
     * @return
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public List<Long> customIdQuery(String keyword) throws SolrProxyException, SolrServerException{
        QueryBuilder queryBuilder = QueryBuilder.newInstanceByKeyword(keyword);
        
        return customIdQuery(queryBuilder);
    }
    
    /**
     * solr query only return cid field
     * @param queryBuilder
     * @return
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public List<Long> customIdQuery(QueryBuilder queryBuilder) throws SolrProxyException, SolrServerException{
        logger.debug("customIdQuery queryBuilder = \n"+((queryBuilder!=null)?queryBuilder.toTcSolrQuery():null));
                
        checkQueryBuilder(queryBuilder);// 檢查 QueryBuilder
        
        prepareCidQueryParams(queryBuilder);// 預設查詢參數設定
        
        TcQueryResponse tcQueryResponse = new TcQueryResponse(executeQuery());
        
        List<Long> resList = new ArrayList<Long>();
        if( tcQueryResponse.getStatus() == TcSolrConfig.RESPONSE_SOLR_OK ){
            if( tcQueryResponse.getTcSolrDocumentList()!=null && !tcQueryResponse.getTcSolrDocumentList().isEmpty() ){
                for(TcSolrDocument tcSolrDocument : tcQueryResponse.getTcSolrDocumentList() ){
                    long cid = tcSolrDocument.getCid();
                    if( cid>0 ){
                        resList.add(cid);
                    }
                }
            }else{
                logger.error("customIdQuery 無符合條件的文件!");
            }
        }else{
            logger.error("customIdQuery tcQueryResponse.getStatus() = "+tcQueryResponse.getStatus());
        }
        
        return resList;
    }
    
    /**
     * 只依客製ID查詢
     * @param source
     * @param ids
     * @return 
     */
    public TcQueryResponse queryByCId(List<Long> idList) throws SolrProxyException, SolrServerException{
        // 只有ID條件
        QueryBuilder qbNew = new QueryBuilder().addCIdCritiria(idList);
        // 查詢
        return simpleQuery(qbNew);
    }
    public TcQueryResponse queryByCId(long cid) throws SolrProxyException, SolrServerException{
        List<Long> idList = new ArrayList<Long>();
        idList.add(Long.valueOf(cid));
        // 只有ID條件
        QueryBuilder qbNew = new QueryBuilder().addCIdCritiria(idList);
        // 查詢
        return simpleQuery(qbNew);
    }
    
    /**
     * 只依客製ID查詢 - 使否存在
     * @param source
     * @param ids
     * @return 
     */
    public boolean existsByCId(List<Long> idList) throws SolrProxyException, SolrServerException{
        // 只有ID條件
        QueryBuilder qbNew = new QueryBuilder().addCIdCritiria(idList);
        
        this.checkQueryBuilder(qbNew);// 檢查 QueryBuilder
        this.prepareCidQueryParams(qbNew);// 只回傳 ID
        
        // 查詢
        TcQueryResponse tcQueryResponse = new TcQueryResponse(executeQuery());
        
        return tcQueryResponse.getStatus()==TcSolrConfig.RESPONSE_SOLR_OK 
                && tcQueryResponse.getNumFound()>=1;
    }
    
    /**
     * 簡易Solr查詢
     * @param queryBuilder
     * @return
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public TcQueryResponse simpleQuery(QueryBuilder queryBuilder) throws SolrProxyException, SolrServerException{
        logger.debug("simpleQuery queryBuilder = \n"+((queryBuilder!=null)?queryBuilder.toTcSolrQuery():null));
        
        checkQueryBuilder(queryBuilder);// 檢查 QueryBuilder
        
        prepareDefQueryParams(queryBuilder);// 預設查詢參數設定
        
        QueryResponse queryResponse = executeQuery();
        return new TcQueryResponse(queryResponse);
    }
    
    /**
     * 檢查 queryBuilder 內容
     * @param queryBuilder
     * @return 
     */
    public void checkQueryBuilder(QueryBuilder queryBuilder) throws SolrProxyException{
        if( queryBuilder==null ){
            logger.error(SolrProxyException.ERROR_NO_QUERY_PARAM);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_QUERY_PARAM);
        }

        if( queryBuilder.toSolrQuery().isEmpty() ){// 無查詢條件 (不含 source 欄位)
            logger.error(SolrProxyException.ERROR_NO_QUERY_PARAM);
            throw new SolrProxyException(SolrProxyException.ERROR_NO_QUERY_PARAM);
        }
        
        if( queryBuilder.isSingleQueryValue() ){// 單一關鍵字字串比對
            if( queryBuilder.getQueryValue()==null || queryBuilder.getQueryValue().isEmpty() ){
                logger.error(SolrProxyException.ERROR_NO_KEYWORD);
                throw new SolrProxyException(SolrProxyException.ERROR_NO_KEYWORD);
            }
        }
        
        if( queryBuilder.isSingleQueryValue() ){// 目前應用一定要加 source 欄位限制及 cid 比對，所以不適用 SingleQueryValue
            logger.error(SolrProxyException.ERROR_SOURCE_LIMIT);
            throw new SolrProxyException(SolrProxyException.ERROR_SOURCE_LIMIT);
        }
    }
    
    /**
     * Solr Query
     * @return
     * @throws SolrServerException 
     */
    public QueryResponse executeQuery() throws SolrServerException {
        SolrServer server = getHttpSolrServer();
        QueryResponse queryResponse;
        if( TcSolrConfig.SSO_SUPPORT_POST ){
            queryResponse = server.query(this, SolrRequest.METHOD.POST); // for SSO 只能用GET
        }else{
            queryResponse = server.query(this);
        }
        server.shutdown();
        return queryResponse;
    }
    
    /**
     * Solr Query by input ModifiableSolrParams
     * @param solrParams
     * @return
     * @throws SolrServerException 
     */
    public QueryResponse executeQuery(ModifiableSolrParams solrParams) throws SolrServerException {       
        SolrServer server = getHttpSolrServer();
   
        QueryResponse queryResponse = server.query(solrParams);
        server.shutdown();
        return queryResponse;
    }
    
    /**
     * Solr Query by input SolrQuery
     * @param solrQuery
     * @return
     * @throws SolrServerException 
     */
    public QueryResponse executeQuery(SolrQuery solrQuery) throws SolrServerException {
        SolrServer server = getHttpSolrServer();
        
        QueryResponse queryResponse = server.query(solrQuery);
        server.shutdown();
        return queryResponse;
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getSolrServiceUrl() {
        return solrServiceUrl;
    }

    public void setSolrServiceUrl(String solrServiceUrl) {
        this.solrServiceUrl = solrServiceUrl;
    }
    //</editor-fold>
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.server.util;

import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.model.TcQueryResponse;
import com.tcci.solr.client.model.TcSolrDocument;
import com.tcci.solr.client.model.TcSolrSource;
import com.tcci.solr.client.proxy.TcSolrQueryProxy;
import com.tcci.solr.client.proxy.TcSolrUpdateProxy;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Peter
 */
public class SolrServerUtils {
    private final static Logger logger = LoggerFactory.getLogger(SolrServerUtils.class);
    
    /**
     * 執行 Solr delete
     * @param tcSolrSource
     * @return 
     */
    public static String runSolrDelete(TcSolrSource tcSolrSource){
        String resMsg;
        try{
            // Run Solr deleteByQuery
            TcSolrUpdateProxy proxy = new TcSolrUpdateProxy();
            int status = proxy.deleteByQuery(tcSolrSource);
            logger.info("runSolrDelete status = "+status);

            if( status == TcSolrConfig.RESPONSE_SOLR_OK ){
                resMsg = TcSolrConfig.RESPONSE_SUCCESS;
            }else{
                resMsg = TcSolrConfig.RESPONSE_FAIL;
            }
        }catch(Exception e){
            logger.error("processRequest exception\n", e);
            resMsg = TcSolrConfig.RESPONSE_FAIL;
        }
        
        return resMsg;
    }

    /**
     * 重建索引 (全部)
     */
    public static long reIndexAll(int onceNum) throws SolrProxyException, SolrServerException, IOException{
        int rows = 0;
        long foundNum = 0;
        boolean first = true;
        
        TcSolrQueryProxy queryProxy = new TcSolrQueryProxy();
        // 查詢條件
        queryProxy.setQuery(TcSolrConfig.SOLR_QUERY_ALL);
        // 依 ID 排序
        queryProxy.setSort(TcSolrConfig.FIELD_SOLR_KEY, SolrQuery.ORDER.asc);
        
        while( first || rows<foundNum ){
            logger.info("reIndexAll 處理第 " + rows + " 到 "+ (rows+onceNum-1) + " 筆..."+new Date());
            // 限制每次處理筆數
            queryProxy.setStart(rows);
            queryProxy.setRows(onceNum);
            // 查詢取得資料
            TcQueryResponse tcQueryResponse = new TcQueryResponse(queryProxy.executeQuery());
            
            // 總筆數
            if( first ){
                first = false;
                foundNum = tcQueryResponse.getNumFound();
            }
            
            // Update & ReIndex
           reIndexByQueryResponse(tcQueryResponse);
            
            // 下一批
            rows = rows + onceNum;
        }
        return foundNum;
    }

    /**
     * 重建索引 (指定分批)
     */
    public static long reIndexPeriod(TcSolrQueryProxy queryProxy) throws SolrProxyException, SolrServerException, IOException{
        if( queryProxy==null ){
            return 0;
        }
        
        logger.info("reIndexAll 處理第 " + queryProxy.getStart() + " 到 "+ (queryProxy.getStart()+queryProxy.getRows()-1) + " 筆..."+new Date());
        
        // 查詢取得資料
        TcQueryResponse tcQueryResponse = new TcQueryResponse(queryProxy.executeQuery());

        // 總筆數
        long foundNum = tcQueryResponse.getNumFound();

        // Update & ReIndex
        reIndexByQueryResponse(tcQueryResponse);
        
        return (foundNum>queryProxy.getRows())?queryProxy.getRows():foundNum;
    }
    
    /**
     * 重建索引 (by source AP)
     * @param source 
     */
    public static long reIndexBySource(String source, int onceNum) throws SolrProxyException, SolrServerException, IOException{
        int rows = 0;
        long foundNum = 0;
        boolean first = true;
        TcSolrQueryProxy queryProxy = new TcSolrQueryProxy();
        // 查詢條件
        queryProxy.setQuery(TcSolrConfig.FIELD_SOURCE_AP+TcSolrConfig.OP_SOLR_EQUALS+source);
        // 依 CID 排序
        queryProxy.setSort(TcSolrConfig.FIELD_CUSTOM_ID, SolrQuery.ORDER.asc);
        
        while( first || rows<foundNum ){
            logger.info("reIndexAll 處理第 " + rows + " 到 "+ (rows+onceNum-1) + " 筆..."+new Date());
            // 限制每次處理筆數
            queryProxy.setStart(rows);
            queryProxy.setRows(onceNum);
            // 查詢取得資料
            TcQueryResponse tcQueryResponse = new TcQueryResponse(queryProxy.executeQuery());
            
            // 總筆數
            if( first ){
                first = false;
                foundNum = tcQueryResponse.getNumFound();
            }
            
            // Update & ReIndex
            reIndexByQueryResponse(tcQueryResponse);
            
            // 下一批
            rows = rows + onceNum;
        }
        
        return foundNum;
    }

    /**
     * 重建索引 (by TcQueryResponse)
     * @param source 
     */
    public static void reIndexByQueryResponse(TcQueryResponse tcQueryResponse) throws IOException, SolrServerException, SolrProxyException {
        if( tcQueryResponse==null ){
            logger.warn("reIndexByQueryResponse is null !");
            return;
        }
        
        // Update & ReIndex
        List<TcSolrDocument> list = tcQueryResponse.getTcSolrDocumentList();
        if( list!=null ){
            for(TcSolrDocument tcSolrDocument : list){
                TcSolrSource tcSolrSource = tcSolrDocument;
                
                if( checkFileExisted(tcSolrSource) ){// 檢查 TcSolrSource 實體檔案是否存在
                    TcSolrUpdateProxy updateProxy = new TcSolrUpdateProxy();
                    // 因 QueueExtractBean 已轉換路徑為 Solr Server 實際路徑，所以[Solr Server本機]可直接使用 extract，即可讀到檔案
                    updateProxy.extract(tcSolrSource);
                }
            }
        }
    }
    
    /**
     * 檢查 TcSolrSource 實體檔案是否存在
     * @param tcSolrSource
     * @return 
     */
    public static boolean checkFileExisted(TcSolrSource tcSolrSource){
        if( tcSolrSource!=null && tcSolrSource.getFilename()!=null && tcSolrSource.getPath()!=null ){
            String filename = tcSolrSource.getPath() + File.separator + tcSolrSource.getFilename();
            
            File fs = new File(filename);
            if( fs.exists() && fs.isFile() && fs.canRead() ){
                return true;
            }else{
                logger.info("checkFileExisted file error : tcSolrSource = "+tcSolrSource);
            }
        }else{
            logger.info("checkFileExisted error : tcSolrSource = "+tcSolrSource);
        }
        return false;
    }
    
}

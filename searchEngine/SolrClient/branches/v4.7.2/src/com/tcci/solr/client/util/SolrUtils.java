package com.tcci.solr.client.util;

import com.tcci.fc.util.zhcoder.Zhcoder;
import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.model.TcQueryResponse;
import com.tcci.solr.client.model.TcSolrDocument;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Peter
 */
public class SolrUtils {
    private static final Logger logger = LoggerFactory.getLogger(SolrUtils.class);
    private static Zhcoder zhcoder = new Zhcoder();

    /**
     * 以 content path name 為來源AP的單號
     * @param contentPath
     * @return
     */
    public static String getSourceByContentPath(String contentPath){
        return contentPath.replaceAll("/", "");
    }
    
    /**
     * 以 UUID 為 solr id
     * @return 
     */
    public static String getSolrId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    
    /**
     * solr phrase
     * @param phrase
     * @return 
     */
    public static String phraseEscape(String phrase){
        return "\"" + phrase + "\"";
    }
    
    /**
     * escape SOLR_QUERY_OPERATORS
     * @param keyword
     * @return 
     */
    public static String escape(String keyword){
        for (String item : TcSolrConfig.SOLR_QUERY_OPERATORS) {
            keyword = keyword.replaceAll(item, "\\" + item);
        }
        
        return keyword;
    }
    
    /**
     * 取得簡繁體陣列
     * @param value 簡體或繁體
     * @return 簡繁體陣列 or null
     */
    public static String[] converterZhCode(String value){
        if ( value==null || value.trim().isEmpty() ) {
            return null;
        }
        String[] materialNames = zhcoder.converterResult(value);
        return materialNames;
    }   

    /**
     * dump QueryResponse
     * @param queryResponse 
     */
    public static void dumpQueryResponse(TcQueryResponse queryResponse){
        logger.info("status="+queryResponse.getStatus());
        logger.info("QTime="+queryResponse.getQTime());
        logger.info("numFound="+queryResponse.getNumFound());
        logger.info("start="+queryResponse.getStart());
        logger.info("maxScope="+queryResponse.getMaxScore());
        
        List<TcSolrDocument> solrDocumentList = queryResponse.getTcSolrDocumentList();
        
        if( solrDocumentList!=null ){// 0
            for(TcSolrDocument tcSolrDocument : solrDocumentList){// 1
                logger.info("id="+tcSolrDocument.getId());
                logger.info("source="+tcSolrDocument.getSource());
                logger.info("cid="+tcSolrDocument.getCid());
                logger.info("title="+tcSolrDocument.getTitle());
                logger.info("description="+tcSolrDocument.getDescription());
                logger.info("filename="+tcSolrDocument.getFilename());
                logger.info("path="+tcSolrDocument.getPath());
                logger.info("contentType="+tcSolrDocument.getContentType());
                logger.info("size="+tcSolrDocument.getSize());
                // logger.debug("content="+tcSolrDocument.getContent());

                // Then to get back the highlight results you need
                dumpHighlightSnippets(tcSolrDocument.getHighlightSnippets());
            }// 1
        }// 0
    }
    
    /**
     * dump highlightSnippets
     * @param highlightSnippets 
     */
    public static void dumpHighlightSnippets(Map<String, List<String>> highlightSnippets){
        if( highlightSnippets != null) {
            for(String key : highlightSnippets.keySet()){
                List<String> snippets = highlightSnippets.get(key);
                if( snippets!=null ){
                    logger.info("highlightSnippets : key ="+key);
                    for(String snippet:snippets){
                        logger.info("highlightSnippets : snippet="+snippet.trim());
                    }
                }
            }
        }
    }

    /**
     * dump Result NamedList
     * @param result 
     */
    public static void dumpResultNamedList(NamedList<Object> result){
        Iterator<Map.Entry<String, Object>> it = result.iterator();
        
        while( it.hasNext() ){
            Map.Entry<String, Object> entry = it.next();
            
            if(entry.getValue() instanceof NamedList){
                NamedList<Object> subres = (NamedList<Object>) (entry.getValue());
                logger.info(entry.getKey() + " = [");
                dumpResultNamedList(subres);
                logger.info("]");
            }else{
                logger.info(entry.getKey() + " = " + entry.getValue());
            }
        }
    }
    
    /**
     * MultiValued Field to String List
     * @param contentList
     * @return 
     */
    public static String convertMultiValued(List<String> contentList){ 
        StringBuilder contentSB = new StringBuilder();
        //List<String> contentList = (List<String>) solrDocument.getFieldValue("content");
        if( contentList!=null ){
            for(String contentTxt : contentList){
                if( contentSB.toString().isEmpty() ){
                    contentSB.append(contentTxt);
                }else{
                    contentSB.append(",").append(contentTxt);
                }
            }
        }
        
        return contentSB.toString();
    }

    /**
     * call service without SSO
     * @param server
     * @param scTicket
     * @param service
     * @return 
     */
    public static HttpResponse callService(String service) throws IOException {
        HttpPost httpPost = new HttpPost(service);
        HttpClient client = new DefaultHttpClient();
        return client.execute(httpPost);
    }

}

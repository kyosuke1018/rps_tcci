package com.tcci.solr.client.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.util.SolrUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Customized Solr Document
 * @author Peter
 */
public class TcSolrDocument extends TcSolrSource implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(TcSolrDocument.class);
    
    // output attributes (system generated, query result)
    private long size;// file size
    private Date lastModified;// file last modified time
    private String author;// author
    private String contentType;// file content type
    private String content;// file content
    //private boolean extracted;// has extracted
    
    private double score;// system output    private long size;// file size

    private Map<String, List<String>> highlightSnippets; // hight light 片段

    private Map<String, Object> customizeProps; // 其他客制參數
            
    public TcSolrDocument(){
        super();
    }

    public TcSolrDocument(TcSolrSource tcSolrSource){
        this.setId(tcSolrSource.getId());
        this.setSource(tcSolrSource.getSource());
        this.setCid(tcSolrSource.getCid());
        this.setTitle(tcSolrSource.getTitle());
        this.setDescription(tcSolrSource.getDescription());
        this.setFilename(tcSolrSource.getFilename());
        this.setPath(tcSolrSource.getPath());
    }
    
    /**
     * construct by SolrDocument with customized properties
     * @param solrDocument
     * @param custProps 
     */
    public TcSolrDocument(SolrDocument solrDocument, Map<String, Map<String, List<String>>> highlightSnippetsMap, Map<String, Object> custProps){
        convertSolrDocument(solrDocument, highlightSnippetsMap);
        customizeProps.putAll(custProps);
    }
    
    /**
     * construct by SolrDocument
     * @param solrDocument 
     */
    public TcSolrDocument(SolrDocument solrDocument, Map<String, Map<String, List<String>>> highlightSnippetsMap){
        convertSolrDocument(solrDocument, highlightSnippetsMap);
    }
    
    /**
     * HighlightSnippet 自訂顯示格式
     * @return 
     */
    public String formatHighlightSnippet(String preTag, String postTag){
        String concatStr = concatHighlightSnippet();
        
        concatStr = StringEscapeUtils.escapeHtml(concatStr);// 取代前先 escapeHtml，UI則要設為 escape=false
        
        concatStr = concatStr.replaceAll(TcSolrConfig.DEF_HIGHLIGHT_PER, preTag);
        concatStr = concatStr.replaceAll(TcSolrConfig.DEF_HIGHLIGHT_POST, postTag);
        
        logger.info("formatHighlightSnippet concatStr = \n"+concatStr);
        
        return concatStr;
    }
    
    /**
     * 合併 Highlight Snippets 
     * @return 
     */
    public String concatHighlightSnippet(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; TcSolrConfig.DEF_HIGHLIGHT_FIELDS!=null && i<TcSolrConfig.DEF_HIGHLIGHT_FIELDS.length; i++){
            List<String> list = highlightSnippets.get(TcSolrConfig.DEF_HIGHLIGHT_FIELDS[i]);

            if( list!=null ){
                for(String snippet : list){
                    sb.append(snippet).append(TcSolrConfig.HIGHLIGHT_CONCAT_STR);
                }
            }
        }
        
        return sb.toString();
    }

    /**
     * 由 QueryResponse 的 SolrDocument 轉換至 TcSolrDocument
     * @param solrDocument 
     */
    private void convertSolrDocument(SolrDocument solrDocument, Map<String, Map<String, List<String>>> highlightSnippetsMap) {
        // Solr Unique Key
        this.setId(setByFieldValue(String.class, solrDocument.getFieldValue("id"), null));

        // 來源IP自訂資訊
        this.setSource(setByFieldValue(String.class, solrDocument.getFieldValue(TcSolrConfig.FIELD_SOURCE_AP), null));
        this.setCid(setByFieldValue(Long.class, solrDocument.getFieldValue(TcSolrConfig.FIELD_CUSTOM_ID), new Long(0)));
        this.setTitle(setByFieldValue(String.class, solrDocument.getFieldValue("title"), null));
        this.setDescription(setByFieldValue(String.class, solrDocument.getFieldValue("description"), null));
        
        // 上傳檔資訊
        this.setFilename(setByFieldValue(String.class, solrDocument.getFieldValue("filename"), null));
        this.setPath(setByFieldValue(String.class, solrDocument.getFieldValue("path"), null));
        this.setSize(setByFieldValue(Long.class, solrDocument.getFieldValue("size"), new Long(0)));
        this.setLastModified(setByFieldValue(Date.class, solrDocument.getFieldValue("last_modified"), null));

        // 是否已 Extract
        //this.setExtracted(setByFieldValue(Boolean.class, solrDocument.getFieldValue("extracted"), true));
        
        // 結構多筆，實際應用一筆 author、content、contentType，合併為單一字串
        // solr schema.xml: author、ontent、contentType field 都為 multiValued="true" 
        this.setAuthor(SolrUtils.convertMultiValued((List<String>) solrDocument.getFieldValue("author")));
        this.setContent(SolrUtils.convertMultiValued((List<String>) solrDocument.getFieldValue("content")));
        this.setContentType(SolrUtils.convertMultiValued((List<String>) solrDocument.getFieldValue("content_type")));

        // 得分
        this.setScore(setByFieldValue(Float.class, solrDocument.getFieldValue(TcSolrConfig.FIELD_BOOTS_SCORE), new Float(0)).doubleValue());
        
        // get the doc highlight snippets
        if (highlightSnippetsMap!=null && highlightSnippetsMap.get(this.id) != null) {
            highlightSnippets = new HashMap<String, List<String>>();
            for(int i=0; TcSolrConfig.DEF_HIGHLIGHT_FIELDS!=null && i<TcSolrConfig.DEF_HIGHLIGHT_FIELDS.length; i++){
                List<String> list = highlightSnippetsMap.get(this.id).get(TcSolrConfig.DEF_HIGHLIGHT_FIELDS[i]);
                highlightSnippets.put(TcSolrConfig.DEF_HIGHLIGHT_FIELDS[i], list);
            }
        }
    }
 
    /**
     * Solr 單一欄位值轉換
     * @param <T>
     * @param clazz
     * @param value
     * @param defValue
     * @return 
     */
    private <T> T setByFieldValue(Class<T> clazz, Object value, Object defValue){
        try{
            if( value!=null ){
                return clazz.cast(value);
            }
        }catch(Exception e){
            logger.error("setByFieldValue exception :", e);
        }
        
        return clazz.cast(defValue);
    }

    @Override
    public boolean equals(Object obj){
        if( obj==null || !(obj instanceof TcSolrDocument) ){
            return false;
        }
        
        TcSolrDocument other = ((TcSolrDocument)obj);
                
        if( this.id!=null && other.getId()!=null
                && this.id.equals(other.getId()) ){
            return true;
        }
        
        if( this.getSource()!=null && other.getSource()!=null
            && this.getCid()>0 && other.getCid()>0 ){
            if( this.getSource().equals(other.getSource()) && this.getCid()==other.getCid() ){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    /*public boolean isExtracted() {
        return extracted;
    }

    public void setExtracted(boolean extracted) {
        this.extracted = extracted;
    }*/

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Map<String, List<String>> getHighlightSnippets() {
        return highlightSnippets;
    }

    public void setHighlightSnippets(Map<String, List<String>> highlightSnippets) {
        this.highlightSnippets = highlightSnippets;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getCustomizeProps() {
        return customizeProps;
    }

    public void setCustomizeProps(Map<String, Object> customizeProps) {
        this.customizeProps = customizeProps;
    }
    //</editor-fold>

}

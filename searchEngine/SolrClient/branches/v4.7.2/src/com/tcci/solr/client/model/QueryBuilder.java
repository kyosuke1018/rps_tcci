package com.tcci.solr.client.model;

import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.util.SolrUtils;
import java.util.List;
import org.apache.solr.common.params.SimpleParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * boost query : 
 *      q=media:DVD^2 media:BLU-RAY^1.5
 *  For the standard request handler, "boost" the clause on the title field:
 *      q=title:superman^2 subject:superman
 *  Using the dismax request handler, one can specify boosts on fields in parameters such as qf:
 *      q=superman&qf=title^2 subject
 * 
 * @author Peter
 */
public class QueryBuilder {
    private static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
    
    private StringBuilder result = new StringBuilder();
    private boolean singleQueryValue;// Using the dismax request handler
    private String queryValue;// use when singleQueryValue=true
    private boolean isFirst = true;// 用來判斷後續附加字串，是否先加一個空白字元
    
    public QueryBuilder(){
        singleQueryValue = false; // 目前應用一定要加 source 欄位限制及 cid 比對，所以不適用 SingleQueryValue
    }
    
    public QueryBuilder(boolean isSingleQueryValue){
        singleQueryValue = isSingleQueryValue;
    }
    
    /**
     * 需檢查是否先加一個空白字元
     */
    private void checkFirst(){
        if( !isFirst ){
            result.append(" ");
        }
        isFirst = false;
    }
    
    public QueryBuilder openParen(){
        checkFirst();
        result.append("(");
        isFirst = true;
        return this;
    }
    
    public QueryBuilder closeParen(){
        result.append(")");
        isFirst = false;
        return this;
    }
    
   /**
     * 附加 +
     * @return 
     */
    public QueryBuilder must(){
        checkFirst();
        result.append("+");
        isFirst = true;
        return this;
    }
    
   /**
     * 附加 AND
     * @return 
     */
    public QueryBuilder and(){
        checkFirst();
        result.append(SimpleParams.AND_OPERATOR); //.append(" ");
        this.isFirst = false;
        return this;
    }

   /**
     * 附加 OR
     * @return 
     */
    public QueryBuilder or(){
        checkFirst();
        result.append(SimpleParams.OR_OPERATOR); //.append(" ");
        this.isFirst = false;
        return this;
    }
    
    /**
     * 附加 NOT
     * @return 
     */
    public QueryBuilder not(){
        checkFirst();
        result.append(SimpleParams.NOT_OPERATOR); //.append(" ");
        this.isFirst = false;
        return this;
    }
    
    /**
     * 附加查詢字串
     * @param subQ
     * @return 
     */
    public QueryBuilder append(String subQ){
        checkFirst();
        result.append(subQ);
        this.isFirst = false;
        return this;
    }
    
    /**
     * 附加查詢欄位條件
     * ex. (+(field1:foo^5 OR field2:bar^10) AND (field1:bar^5 OR field2:bar^10))
     * @param queryField
     * @return 
     */
    public QueryBuilder append(QueryField queryField) throws SolrProxyException{
        checkFirst();
        
        if( singleQueryValue ){// 共用單一關鍵字 (目前用不到，因有 source, cid 欄位要比對)
            if( queryField.getBoost()==1 ){
                result.append(queryField.getName());
            }else{
                result.append(queryField.getName()).append(TcSolrConfig.OP_SOLR_BOOTS).append(queryField.getBoost());
            }
        }else{// 每個欄位有各自關鍵字
            String value = paresKeywords(queryField);
            /*if( queryField.getBoost()==1 ){
                result.append(queryField.getName()).append(TcSolrConfig.OP_SOLR_EQUALS).append(value);
            }else{
                result.append(queryField.getName()).append(TcSolrConfig.OP_SOLR_EQUALS).append(value)
                      .append(TcSolrConfig.OP_SOLR_BOOTS).append(queryField.getBoost());
            }*/
            result.append(value);
        }
        
        return this;
    }
    
    /**
     * 解析輸入關鍵字，轉換成SOLR查詢條件
     * ex. 
     * 輸入     台泥+專案-SAP 
     * 轉換為   (text:台泥 台泥) AND (text:專案 专案) NOT (text:SAP) 
     * @param value
     * @return 
     */
    public String paresKeywords(QueryField queryField) throws SolrProxyException{
        StringBuilder sb = new StringBuilder();
        
        String postStr = queryField.getValue();
        int i = 0;
        int j = 0;
        boolean firstItem = true;
        String op = null;
        
        while( !postStr.isEmpty() ){
            i = postStr.indexOf(TcSolrConfig.KEYWORD_OP_AND);// 自訂AND符號 "+"
            j = postStr.indexOf(TcSolrConfig.KEYWORD_OP_NOT);// 自訂NOT符號 "-"
            
            if( !firstItem && op!=null ){
                sb.append(" ").append(op).append(" ");
            }
                
            if( i>-1 && (j<0 || i<j) ){// 先有+號
                String preStr = postStr.substring(0, i).trim();
                preStr = (queryField.isZhcode())? prepareQueryValue(preStr):preStr;// 繁簡轉換
                sb.append("(").append(buildBoostStr(queryField.getName(), preStr, queryField.getBoost())).append(")");
                
                op = SimpleParams.AND_OPERATOR;
                
                postStr = postStr.substring(i+1).trim();
            }else if( j>-1 && (i<0 || j<i) ){// 先有-號
                String preStr = postStr.substring(0, j).trim();
                preStr = (queryField.isZhcode())? prepareQueryValue(preStr):preStr;// 繁簡轉換

                sb.append("(").append(buildBoostStr(queryField.getName(), preStr, queryField.getBoost())).append(")");
                
                op = SimpleParams.NOT_OPERATOR;
                
                postStr = postStr.substring(j+1).trim();
            }else{
                if( !postStr.isEmpty() ){
                    postStr = (queryField.isZhcode())? prepareQueryValue(postStr):postStr;// 繁簡轉換

                    sb.append("(").append(buildBoostStr(queryField.getName(), postStr, queryField.getBoost())).append(")");
                }
                break;
            }
            
            firstItem = false;
        }
        
        return sb.toString();
    }
    
    /**
     * 建立含比重條件 (value 僅可輸入無 operator 的字串)
     * @param value
     * @return 
     */
    public String buildBoostStr(String name, String value, double boost){
        StringBuilder sb = new StringBuilder();
        if( boost==1 ){
            sb.append(name).append(TcSolrConfig.OP_SOLR_EQUALS).append(value);
        }else{
            sb.append(name).append(TcSolrConfig.OP_SOLR_EQUALS).append(value).append(TcSolrConfig.OP_SOLR_BOOTS).append(boost);
        }
        return sb.toString();
    }
    
    /**
     * 考慮 繁簡轉換
     * @param ori
     * @return 
     */
    public String prepareQueryValue(String ori){
        String value = ori;
        if( TcSolrConfig.ZHCODER_ENABLED ){// 繁簡轉換
            String[] values = SolrUtils.converterZhCode(ori);
            StringBuilder sb = new StringBuilder();
            for(int i=0; values!=null && i<values.length; i++){
                sb.append(values[i]).append(" ");
            }
            value = sb.toString().trim();
        }
        return value;
    }

    /**
     * 傳換查詢條件字串
     * 
     * @return 
     */   
    public String toSolrQuery(){
        return (result==null)?"":result.toString();
    }

    /**
     * 傳換查詢條件字串(加入 SOURCE_AP 限制) use for Solr Client
     * 
     * @return 
     */   
    public String toTcSolrQuery(){
        return toTcSolrQuery(TcSolrConfig.getSourceAP());
    }
    
    /**
     * 傳換查詢條件字串(加入 SOURCE_AP 限制) use for Solr Server
     * @param source (client source AP)
     * @return 
     */
    public String toTcSolrQuery(String source){
        StringBuilder sb = new StringBuilder();
        sb.append("+(").append(TcSolrConfig.FIELD_SOURCE_AP).append(TcSolrConfig.OP_SOLR_EQUALS).append(source).append(")")
          .append(" ").append(SimpleParams.AND_OPERATOR).append(" ")
          .append("(").append(toSolrQuery()).append(")");
        
        return sb.toString();
    }

    /**
     * 整合 cid 欄位比對條件
     * @param queryBuilder
     * @param idList
     * @return 
     */
    public QueryBuilder addCIdCritiria(List<Long> idList) throws SolrProxyException{
        String oriQ = this.toSolrQuery();// 原有其他條件 (不含 source 代號)
        boolean hasOriQ = false;
        if( oriQ!=null && !oriQ.isEmpty() ){
            hasOriQ = true;
        }

        if( hasOriQ && idList!=null && !idList.isEmpty() ){
            this.and().openParen();
        }

        // 以 "OR" 方式連結各ID條件
        boolean firstId = true;
        for(Long cid : idList){
            QueryField qf = new QueryField(TcSolrConfig.FIELD_CUSTOM_ID, cid.toString(), false);
            if( firstId ){
                this.append(qf);
                firstId = false;
            }else{
                this.or().append(qf);
            }
        }
        
        if( hasOriQ && idList!=null && !idList.isEmpty() ){
            this.closeParen();
        }
        
        return this;
    }
    
    //<editor-fold defaultstate="collapsed" desc="static method">
    /**
     * 一般關鍵字查詢方式建立 Instance
     * @param keyword
     * @return 
     */
    public static QueryBuilder newInstanceByKeyword(String keyword) throws SolrProxyException{
        QueryBuilder queryBuilder = new QueryBuilder(false);
        QueryField queryField = new QueryField(TcSolrConfig.FIELD_GLOBAL_QUERY, keyword);
        queryBuilder.append(queryField);
        
        return queryBuilder;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public StringBuilder getResult() {
        return result;
    }

    public void setResult(StringBuilder result) {
        this.result = result;
    }

    public boolean isSingleQueryValue() {
        return singleQueryValue;
    }

    public void setSingleQueryValue(boolean singleQueryValue) {
        this.singleQueryValue = singleQueryValue;
    }

    public String getQueryValue() {
        //return queryValue;
        return prepareQueryValue(queryValue);// 考慮 繁簡轉換
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public boolean isIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }
    //</editor-fold>
}

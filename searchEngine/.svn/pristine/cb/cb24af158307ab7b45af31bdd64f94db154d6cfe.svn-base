package com.tcci.solr.client.model;


import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Customized Solr Query Response
 * @author Peter
 */
public class TcQueryResponse implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(TcQueryResponse.class);
    
    int status;// query status
    int QTime;// query time
    
    long numFound;// query number found
    long start; // count form 0
    long rows; // result rows
    Float maxScore;// max query scope
    
    List<TcSolrDocument> tcSolrDocumentList;// query results
    Map<String, LinkedHashMap<String, Integer>> facetResults;// facet query results
    
    public TcQueryResponse(){
        status = TcSolrConfig.RESPONSE_SOLR_OK;
        numFound = 0;
    }
    
    public TcQueryResponse(QueryResponse queryResponse) throws SolrProxyException{
        convert(queryResponse, null, 0, 0);
    }

    public TcQueryResponse(QueryResponse queryResponse, List<String> groupFields, int pageNum, int pageSize) throws SolrProxyException{
        convert(queryResponse, groupFields, pageNum, pageSize);
    }
    
    /**
     * 將 QueryResponse 轉換回 TcQueryResponse
     * @param solrDocument 
     * @pageNum: return page number
     * @pageSize: Page Size
     */
    private void convert(QueryResponse queryResponse, List<String> groupFields, int pageNum, int pageSize) throws SolrProxyException{
        if( queryResponse==null ){
            throw new SolrProxyException(SolrProxyException.ERROR_RESPONSE_NULL);
        }

        status = queryResponse.getStatus();// query status
        QTime = queryResponse.getQTime();// query time
        
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        if( solrDocumentList==null || solrDocumentList.isEmpty() ){
            return;
        }
        
        numFound = solrDocumentList.getNumFound();
        start = solrDocumentList.getStart();
        rows = solrDocumentList.size();
        maxScore = solrDocumentList.getMaxScore();
        
        // get docs and highlight snippets
        // logger.info("queryResponse.getHighlighting() = "+queryResponse.getHighlighting());
        tcSolrDocumentList = new ArrayList<TcSolrDocument>();
        Iterator<SolrDocument> iter = solrDocumentList.iterator();
        while (iter.hasNext()) {
            SolrDocument resultDoc = iter.next();
            TcSolrDocument tcSolrDocument = new TcSolrDocument(resultDoc, queryResponse.getHighlighting());
            
            tcSolrDocumentList.add(tcSolrDocument);
        }// end of while
        
        // facet query
        if( groupFields!=null && !groupFields.isEmpty()){
            facetResults = new HashMap<String, LinkedHashMap<String, Integer>>();

            for(String groupField : groupFields){
                List<FacetField.Count> countList = queryResponse.getFacetField(groupField).getValues();
                List<FacetField.Count> returnList;
                LinkedHashMap<String, Integer> rmap = new LinkedHashMap<String, Integer>();
                
                if (pageNum * pageSize < countList.size()) {
                    returnList = countList.subList((pageNum - 1) * pageSize, pageNum * pageSize);
                } else {
                    returnList = countList.subList((pageNum - 1) * pageSize, countList.size() - 1);
                }

                for (FacetField.Count count : returnList) {
                    if (count.getCount() > 0) {
                        rmap.put(count.getName(), (int) count.getCount());
                    }
                }
                
                facetResults.put(groupField, rmap);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getQTime() {
        return QTime;
    }

    public void setQTime(int QTime) {
        this.QTime = QTime;
    }

    public long getNumFound() {
        return numFound;
    }

    public void setNumFound(long numFound) {
        this.numFound = numFound;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public Float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Float maxScore) {
        this.maxScore = maxScore;
    }

    public List<TcSolrDocument> getTcSolrDocumentList() {
        return tcSolrDocumentList;
    }

    public void setTcSolrDocumentList(List<TcSolrDocument> tcSolrDocumentList) {
        this.tcSolrDocumentList = tcSolrDocumentList;
    }

    public Map<String, LinkedHashMap<String, Integer>> getFacetResults() {
        return facetResults;
    }

    public void setFacetResults(Map<String, LinkedHashMap<String, Integer>> facetResults) {
        this.facetResults = facetResults;
    }
    //</editor-fold>
}

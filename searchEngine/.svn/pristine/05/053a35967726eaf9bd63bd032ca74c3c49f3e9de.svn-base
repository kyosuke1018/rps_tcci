import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.model.QueryBuilder;
import com.tcci.solr.client.model.QueryField;
import com.tcci.solr.client.model.TcQueryResponse;
import com.tcci.solr.client.model.TcSolrDocument;
import com.tcci.solr.client.model.TcSolrSource;
import com.tcci.solr.client.proxy.TcSolrQueryProxy;
import com.tcci.solr.client.proxy.TcSolrUpdateProxy;
import com.tcci.solr.client.util.SolrUtils;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class TestSolrClient {
    private static final Logger logger = LoggerFactory.getLogger(TestSolrClient.class);
    private static final boolean extractOnly = true;
    
    public static void main(String[] args) throws SolrServerException, SolrProxyException, IOException {
        //try {
            //testDeleteByCid();
        
            //testSolrQuery();// 直接使用 SolrQuery 做查詢
            //testSolrQueryProxy2(); // 簡易關鍵字查詢測試
            //test2StepQuery(); // 兩階段查詢 (文件授權資訊)
            //test2StepQuery2(); // 已知 文件授權資訊 查詢
            
            clearAll();
            //testExtractDir("D:\\BAK\\solr"); // 目錄內檔案匯入
            //testExtractDir("C:\\temp\\台泥英德\\MM"); // 目錄內檔案匯入
            testLazyExtract(); // 測試 Lazy Extract
            
            //<editor-fold defaultstate="collapsed" desc="test single file extract"> 
            //testSolrUpdateProxy("D:\\BAK\\solr\\looping.pdf", "PMIS", 10022, "");
            //testSolrUpdateProxy("D:\\BAK\\ERP.xls", "PMIS", 10009, "");
            //testSolrUpdateProxy("D:\\BAK\\ERP.xlsx", "PMIS", 10010, "");
            //testSolrUpdateProxy("D:\\BAK\\Weekly Report-PeterPan-20140516.doc", "PMIS", 10011, "");
            //testSolrUpdateProxy("D:\\BAK\\TCJCoServer.docx", "PMIS", 10011, "");
            //testSolrUpdateProxy("D:\\BAK\\敘永及納溪ERP導入專案kickoff meeting-201401113.ppt", "PMIS", 10011, ""); // x
            //testSolrUpdateProxy("D:\\BAK\\Android 開發經驗分享.pptx", "PMIS", 10011, "");
            //testSolrUpdateProxy("D:\\BAK\\ipod_video.xml", "PMIS", 10008, "");

            //testSolrUpdateProxy("D:\\BAK\\敘永及納溪ERP導入專案-part1.ppt", "PMIS", 10013, ""); // x
            //testSolrUpdateProxy("D:\\BAK\\敘永及納溪ERP導入專案-part1.pptx", "PMIS", 10011, ""); // V
            //testSolrUpdateProxy("D:\\BAK\\SAP_RT_SYNC.ppt", "PMIS", 10011, ""); // V
            //testSolrUpdateProxy("D:\\BAK\\Apache Solr 介紹.ppt", "PMIS", 10014, ""); // V
            //</editor-fold>
        //} catch (SolrServerException e) {
        //    logger.error("SolrServerException Exception :", e);
        //}
    }
    
    /**
     * 測試 TcSolrUpdateProxy DeleteByCid
     */
    public static void testDeleteByCid() throws IOException, SolrProxyException{
        TcSolrUpdateProxy proxy = new TcSolrUpdateProxy();
        TcSolrSource tcSolrSource = new TcSolrSource();
        tcSolrSource.setSource("PMIS");
        tcSolrSource.setCid(1401267594785L);
        
        proxy.deleteByCid(tcSolrSource);
    }
    
    /**
     * 測試 Lazy Extract
     */
    public static void testLazyExtract() throws IOException, SolrProxyException{
        TcSolrUpdateProxy proxy = new TcSolrUpdateProxy();
        TcSolrSource tcSolrSource = new TcSolrSource();
        tcSolrSource.setSource("SolrWebDemo");
        tcSolrSource.setCid(20001);
        tcSolrSource.setTitle("eSMS安裝手冊WIN");
        StringBuilder sb = new StringBuilder().append("111111111111111");
        for(int i=0; i<50; i++){// 40 ok, 50 error
            sb.append(i).append("測試很長的輸入一二三");
        }
        
        tcSolrSource.setDescription(sb.toString());
        tcSolrSource.setFilename("eSMS安裝手冊WIN.pdf");
        //tcSolrSource.setPath("D:\\BAK");
        tcSolrSource.setPath("D:/BAK/solr/SolrWebDemo");
        
        proxy.add(tcSolrSource);
    }
    
    /**
     * 兩階段查詢 (文件授權資訊)
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public static void test2StepQuery() throws SolrProxyException, SolrServerException{
        TcSolrQueryProxy solrQueryProxy = new TcSolrQueryProxy(TcSolrConfig.getSolrURL());
        // 關鍵字查詢條件
        QueryBuilder queryBuilder = QueryBuilder.newInstanceByKeyword("采购");
        
        // Solr 第一次查詢 : 條件 = AP代碼與關鍵字
        List<Long> idList = solrQueryProxy.customIdQuery(queryBuilder); // solr query only return cid field
        logger.info("idList.size = "+((idList!=null)?idList.size():0));

        if( idList==null || idList.isEmpty() ){
            return;
        }
        
        // 取 idList 與各別AP "文件授權" 與 "DB其他條件" 交集的 ID
        /*idList.remove(2);
        idList.remove(5);
        idList.remove(9);*/
        // 承上，取目前頁面要顯示資料ID
        /*idList.remove(11);
        idList.remove(12);*/
        
        // Solr 第二次查詢: 條件 = AP代碼與cid 比對 (原條件要保留，為了 highlight)
        QueryBuilder qbNew = queryBuilder.addCIdCritiria(idList);
        
        TcQueryResponse queryResponse = solrQueryProxy.simpleQuery(qbNew);
        // logger.debug(queryResponse.toString());
        SolrUtils.dumpQueryResponse(queryResponse);
    }
    
    /**
     * 已知 文件授權資訊 查詢
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public static void test2StepQuery2() throws SolrProxyException, SolrServerException{
        TcSolrQueryProxy solrQueryProxy = new TcSolrQueryProxy(TcSolrConfig.getSolrURL());

        // 文件授權資訊與DB其他條件
        List<Long> compList = new ArrayList<Long>();
        for(long i=1; i<=20; i++){
            compList.add(i);
        }
        boolean accessible = true;
        
        // 加入客製授權考量查詢
        String keyword = "系統";
        TcQueryResponse queryResponse = solrQueryProxy.customAuthQuery(keyword, compList, accessible);
        // logger.debug(queryResponse.toString());
        SolrUtils.dumpQueryResponse(queryResponse);
    }
    
    /**
     * 簡易關鍵字查詢測試
     * @throws SolrProxyException
     * @throws SolrServerException 
     */
    public static void testSolrQueryProxy2() throws SolrProxyException, SolrServerException{
        logger.debug("testSolrQueryProxy2 ...");
        TcSolrQueryProxy solrQueryProxy = new TcSolrQueryProxy(TcSolrConfig.getSolrURL());
        QueryBuilder queryBuilder = new QueryBuilder(false);
        
        QueryField queryField1 = new QueryField("text", "電腦", true);
        queryBuilder.append(queryField1); // the same 1 
        //queryBuilder.append(queryField1).or().append(queryField2);

        TcQueryResponse queryResponse = solrQueryProxy.simpleQuery(queryBuilder);
        
        logger.debug(queryResponse.toString());
        SolrUtils.dumpQueryResponse(queryResponse);
    }
    
    /**
     * 指定檔案夾內文件建立索引
     * @param path
     * @throws IOException
     * @throws SolrServerException 
     */
    public static void testExtractDir(String path) throws IOException, SolrServerException, SolrProxyException{
        File dir = new File(path);
        if( dir.isDirectory() ){
            File[] files = dir.listFiles();
            for(int i=0; files!=null && i<files.length; i++){
                if( files[i].isFile() ){
                    logger.info("read file = "+files[i].getAbsolutePath());
                    String filename = files[i].getAbsolutePath();
                    long cid = 10000+i;
                    String title = "["+(cid)+"]"+filename;
                    //String id = (new Integer(i)).toString();
                    //String cid = "C"+("000000"+new Integer(i).toString()).substring((new Integer(i)).toString().length());
                    // testSolrCell(filename, id, cid);
                    testSolrUpdateProxy(filename, TcSolrConfig.getSourceAP(), cid, title);
                }
            }
        }
    }
    
    /**
     * 清除現有全部索引
     * @throws SolrServerException
     * @throws IOException 
     */
    public static void clearAll() throws SolrServerException, IOException{
        SolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());
        //server.deleteByQuery("*:*");
        server.deleteByQuery("source:SolrWebDemo AND title:eSMS安裝手冊WIN");
        // SolrWebDemo
        server.commit();
    }
    
    /**
     * test SolrUpdateProxy
     * @param filename
     * @param cid 
     */
    public static void testSolrUpdateProxy(String fullfilename, String source, long cid, String title) 
            throws IOException, SolrServerException, SolrProxyException{
        logger.info("BEGIN: testSolrUpdateProxy filename:"+fullfilename+" => cid:"+cid);
        
        int i = fullfilename.lastIndexOf(File.separator);
        if( i<0 ){
            logger.error("fullfilename:"+fullfilename);
            return;
        }
    
        String filename = fullfilename.substring(i+1);
        String path = fullfilename.substring(0, i);
        if( title==null || title.isEmpty() ){
            title = filename;
        }

        File fs = new File(filename); // "D:/BAK/solr/solr-word.pdf"
        long size = fs.length();
                
        TcSolrDocument tcSolrDocument = new TcSolrDocument();
        tcSolrDocument.setId(SolrUtils.getSolrId());
        // tcSolrDocument.setId("0e96b464-4819-42eb-bd36-f682b3703dcb");// for test update by id

        tcSolrDocument.setSource(source);
        tcSolrDocument.setCid(cid);
        tcSolrDocument.setTitle(title);
        
        //tcSolrDocument.setContentType(contentType);
        tcSolrDocument.setFilename(filename);
        tcSolrDocument.setLastModified(new Date(fs.lastModified()));
        tcSolrDocument.setPath(path);
        tcSolrDocument.setSize(size);
        
        //tcSolrDocument.setExtracted(true);// 直接上傳檔案 Extract
        
        TcSolrUpdateProxy proxy = new TcSolrUpdateProxy();
        //proxy.setExtractOnly(extractOnly);// for test
        int status = proxy.extract(tcSolrDocument);
        logger.info("END: testSolrUpdateProxy fullfilename:"+fullfilename+"=> status:"+status);
    }
    
    /**
     * 建立輸入檔案的索引
     * Sending Documents to Solr with Solr Cell and SolrJ
     */
    public static void testSolrCell(String filename, String id, long cid) throws SolrServerException, IOException {
        SolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());
        // server.ping()
        
        int i = filename.lastIndexOf(File.separator);
        if( i<0 ){
            logger.error("filename: " + filename);
            return;
        }
        String fname = filename.substring(i+1);
        String path = filename.substring(0, i);
        String title = fname + " title ...";
        
        /*CommonsHttpSolrServer s = new CommonsHttpSolrServer( url );
        server.setConnectionTimeout(100); // 1/10th sec
        server.setDefaultMaxConnectionsPerHost(100);
        server.setMaxTotalConnections(100);*/
      
        ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
        File fs = new File(filename); // "D:/BAK/solr/solr-word.pdf"
        long size = fs.length();
        req.addFile(fs, "application/octet-stream");
        
        req.setParam("literal.id", id);
        req.setParam("literal.cid", new Long(cid).toString());
        req.setParam("literal.filename", fname);
        req.setParam("literal.path", path);
        req.setParam("literal.title", title);
        req.setParam("literal.size", new Long(size).toString());
       
        // uprefix=attr_&fmap.content=attr_content
        req.setParam("uprefix", "ignored_");
        //req.setParam("uprefix", "attr_"); // for debuge to view all attributes
        req.setParam("captureAttr", "false");
        //req.setParam("fmap.ignored_filename", "filename");
        req.setParam("fmap.content", "ignored_content"); // ignore manay no use meta
        req.setParam("fmap.content_type", "content_type"); // ignore manay no use meta
        req.setParam("capture", "div");// capture content within div tag
        req.setParam("fmap.div", "content");// map div to content field
        //req.setParam("extractOnly", "true");

        NamedList<Object> result = server.request(req);
        
        server.commit();
        // server.shutdown();
        logger.info("Result: " + result);
        SolrUtils.dumpResultNamedList(result);
        
        UpdateResponse updateResponse = new UpdateResponse();
        updateResponse.setResponse(result);
        logger.info("updateResponse.getQTime();: " + updateResponse.getQTime());
        logger.info("updateResponse.getStatus();: " + updateResponse.getStatus());
    }
        
    /**
     * 測試 Solr 查詢
     * @throws SolrServerException 
     */
    public static void testSolrQuery() throws SolrServerException, IOException {
        SolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());
        // boost query : 
        //      q=media:DVD^2 media:BLU-RAY^1.5
        //  For the standard request handler, "boost" the clause on the title field:
        //      q=title:superman^2 subject:superman
        //  Using the dismax request handler, one can specify boosts on fields in parameters such as qf:
        //      q=superman&qf=title^2 subject
        
        // http://localhost:8983/solr/select/?q=video&indent=on&hl=true&hl.fl=name,features&facet=true&facet.field=cat&facet.mincount=1&wt=json
        // faceted query
        SolrQuery solrQuery = new SolrQuery()
                .setQuery("案")
                .setFacet(true)
                .setFacetMinCount(1)
                .setFacetLimit(8)
                //.setFacetSort(null)
                //.addFacetField("category")
                //.addDateRangeFacet(field, start, end, gap)
                //.addNumericRangeFacet(field, start, end, gap)
                .addFacetField("author")
                .setHighlight(true)
                .setHighlightSnippets(1)
                .setParam("hl.fl", "subject,content")
                //.setShowDebugInfo(true)
                .setSort(TcSolrConfig.FIELD_CUSTOM_ID, SolrQuery.ORDER.desc);
    
        /*query = new SolrQuery("*:*");
        query.addFacetQuery("price:[* TO 2]");
        query.addFacetQuery("price:[2 TO 4]");
        query.addFacetQuery("price:[5 TO *]");
        query.addFacetField("inStock");
        query.addFacetField("price");
        query.addFacetField("timestamp");
        query.removeFilterQuery("inStock:true*/
        //query2.addFilterQuery("inStock:true");
    
        QueryResponse queryResponse = server.query(solrQuery);
        
        showQueryResponse(queryResponse);
    }

    /**
     * 顯示 Solr Query Response 內容
     * @param queryResponse 
     */
    public static void showQueryResponse(QueryResponse queryResponse){
        int status = queryResponse.getStatus();
        logger.debug("status="+status);
        logger.debug("QTime="+queryResponse.getQTime());

        // Then to get back the highlight results you need something like this
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        Iterator<SolrDocument> iter = solrDocumentList.iterator();
        logger.info("numFound="+solrDocumentList.getNumFound());
        logger.info("start="+solrDocumentList.getStart());
        logger.info("maxScope="+solrDocumentList.getMaxScore());
        
        while (iter.hasNext()) {
            SolrDocument resultDoc = iter.next();

            String id = (String) resultDoc.getFieldValue("id"); //id is the uniqueKey field
            String content = (String) resultDoc.getFieldValue("content");
            logger.info("content = \n"+content);
            /*List<String> contentList = (List<String>) resultDoc.getFieldValue("content");

            logger.info("id="+id);
            if( contentList!=null ){
                for(String content : contentList){
                    logger.info("content="+content);
                }
            }*/
            
            if( queryResponse.getHighlighting()!=null ){
                if (queryResponse.getHighlighting().get(id) != null) {
                    List<String> highlightSnippets = queryResponse.getHighlighting().get(id).get("content");
                    if( highlightSnippets!=null ){
                        for(String txt : highlightSnippets){
                            logger.debug("highlightSnippets : content="+txt);
                        }
                    }
                }
            }
        }// end of while
    }
   
}

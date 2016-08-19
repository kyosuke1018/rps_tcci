
import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import com.tcci.solr.client.model.TcQueryResponse;
import com.tcci.solr.client.model.TcSolrDocument;
import com.tcci.solr.client.model.TcSolrSource;
import com.tcci.solr.client.proxy.TcSolrQueryProxy;
import com.tcci.solr.client.proxy.TcSolrUpdateProxy;
import com.tcci.solr.server.util.SolrServerUtils;
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
public class testMain {
    private static final Logger logger = LoggerFactory.getLogger(testMain.class);
    
    public static void main(String[] args) throws SolrServerException, SolrProxyException, IOException {
        // testUpdatePartialField();
        // SolrServerUtils.reIndexBySource("SolrWebDemo", 20);
        // SolrServerUtils.reIndexAll(40);
        
        testReIndexPeriod("SolrWebDemo");
    }
    
    /**
     * 變更欄位值 & Reindex
     * @throws SolrProxyException
     * @throws SolrServerException
     * @throws IOException 
     */
    public static void testUpdatePartialField() throws SolrProxyException, SolrServerException, IOException{
        long testCid = 10111;
        
        TcSolrQueryProxy queryProxy = new TcSolrQueryProxy();
        TcQueryResponse tcQueryResponse = queryProxy.queryByCId(testCid);

        TcSolrDocument tcSolrDocument = null;
        String id = null;
        if( tcQueryResponse.getNumFound()>0 ){
            List<TcSolrDocument> list = tcQueryResponse.getTcSolrDocumentList();
            if( list!=null && !list.isEmpty() ){
                tcSolrDocument = list.get(0);
                id = tcSolrDocument.getId();
                logger.info("id = "+id);
            }
        }
        
        if( tcSolrDocument!=null && id!=null ){           
            TcSolrSource tcSolrSource = tcSolrDocument;
            tcSolrSource.setTitle(tcSolrDocument.getTitle()+" Updated: "+new Date());
            
            TcSolrUpdateProxy updateProxy = new TcSolrUpdateProxy();
            // 因 QueueExtractBean 已轉換路徑為 Solr Server 實際路徑，所以[Solr Server本機]可直接使用 extract，即可讀到檔案
            updateProxy.extract(tcSolrSource);
            
            /*
            System.setProperty("solr.solr.home", "D:\\Server\\solr-4.7.2\\root\\solr");
            CoreContainer initializer = new CoreContainer();
            CoreContainer coreContainer = new CoreContainer();
            coreContainer.load();
            EmbeddedSolrServer server = new EmbeddedSolrServer(coreContainer, "");
            */
            /*
            SolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());
            SolrInputDocument doc = new SolrInputDocument();
            doc.setField("id", id);
            doc.setField("title", tcSolrDocument.getTitle());
            
            server.add(doc);
            server.commit();
            */
        }
    }
    
    public static void testReIndexPeriod(String source) throws SolrProxyException, SolrServerException, IOException{
        TcSolrQueryProxy queryProxy = new TcSolrQueryProxy();
        // 限制每次處理筆數
        queryProxy.setStart(0);
        queryProxy.setRows(10);

        long num = 0;
        if( source.isEmpty() ){
            // 查詢條件
            queryProxy.setQuery(TcSolrConfig.SOLR_QUERY_ALL);// TcSolrConfig.SOLR_QUERY_ALL or TcSolrConfig.FIELD_SOURCE_AP+TcSolrConfig.OP_SOLR_EQUALS+source
            // 依 ID 排序
            queryProxy.setSort(TcSolrConfig.FIELD_SOLR_KEY, SolrQuery.ORDER.asc);
            // num = SolrServerUtils.reIndexAll(ConfigManager.DEF_INDEX_NUM);
        }else{
            // 查詢條件
            queryProxy.setQuery(TcSolrConfig.FIELD_SOURCE_AP+TcSolrConfig.OP_SOLR_EQUALS+source);
            // 依 ID 排序
            queryProxy.setSort(TcSolrConfig.FIELD_CUSTOM_ID, SolrQuery.ORDER.asc);
            // num = SolrServerUtils.reIndexBySource(source, ConfigManager.DEF_INDEX_NUM);
        }

        num = SolrServerUtils.reIndexPeriod(queryProxy);

        logger.info("共處理 "+num+" 筆資料");
    }
    
}

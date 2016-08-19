import com.tcci.solr.client.conf.TcSolrConfig;
import com.tcci.solr.client.exception.SolrProxyException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.FacetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter
 */
public class SolrJExample2 {
    private static final Logger logger = LoggerFactory.getLogger(TestSolrClient.class);

    public static void main(String[] args) throws SolrServerException, SolrProxyException, IOException {

    }
    
    /**
     * adding documents to solr
     */
    public static void addDocToSolr() throws SolrServerException, IOException {
        SolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());
        // SolrServer server = new EmbeddedSolrServer();

        // Construct a document
        SolrInputDocument doc1 = new SolrInputDocument();
        doc1.addField("id", "id1", 1.0f);
        doc1.addField("name", "doc1", 1.0f);
        doc1.addField("price", 10);

        // Construct another document. Each document can be independently be added 
        // but it is more efficient to do a batch update. 
        // Every call to SolrServer is an Http Call (This is not true for EmbeddedSolrServer).
        SolrInputDocument doc2 = new SolrInputDocument();
        doc2.addField("id", "id2", 1.0f);
        doc2.addField("name", "doc2", 1.0f);
        doc2.addField("price", 20);

        // Create a collection of documents
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        docs.add(doc1);
        docs.add(doc2);

        // Add the documents to Solr
        server.add(docs);

        // Do a commit
        server.commit();

        // To immediately commit after adding documents, you could use:
        UpdateRequest req = new UpdateRequest();
        req.setAction(UpdateRequest.ACTION.COMMIT, false, false);
        req.add(docs);
        UpdateResponse rsp = req.process(server);
    }

    /**
     * This is the most optimal way of updating all your docs in one http
     * request.
     */
    public static void batchUpdate() throws SolrServerException, IOException {
        HttpSolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());

        Iterator<SolrInputDocument> iter;

        iter = new Iterator<SolrInputDocument>() {
            @Override
            public boolean hasNext() {
                boolean result = false;
                // set the result to true false to say if you have more documensts
                return result;
            }

            @Override
            public SolrInputDocument next() {
                SolrInputDocument result = null;
                // construct a new document here and set it to result
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        server.add(iter);
    }

    /**
     * Directly adding POJOs to Solr
     */
    public static void addPOJO() throws IOException, SolrServerException {
        HttpSolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());

        // Create the bean instances
        Item item = new Item();
        item.id = "one";
        item.categories = new String[]{"aaa", "bbb", "ccc"};
        // Add to Solr
        server.addBean(item);

        // Adding multiple beans together
        List<Item> beans = new ArrayList<Item>();
        //add Item objects to the list
        server.addBeans(beans);
    }

    /**
     * Takes an SQL ResultSet and adds the documents to solr. Does it in batches
     * of fetchSize.
     *
     * @param rs A ResultSet from the database.
     * @return The number of documents added to solr.
     * @throws SQLException
     * @throws SolrServerException
     * @throws IOException
     */
    public static long addResultSet(ResultSet rs) throws SQLException,
            SolrServerException, IOException {
        int fetchSize = 1000;
        HttpSolrServer solrCore = new HttpSolrServer(TcSolrConfig.getSolrURL());

        long count = 0;
        int innerCount = 0;
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        ResultSetMetaData rsm = rs.getMetaData();
        int numColumns = rsm.getColumnCount();
        String[] colNames = new String[numColumns + 1];

        /**
         * JDBC numbers the columns starting at 1, so the normal java convention
         * of starting at zero won't work.
         */
        for (int i = 1; i < (numColumns + 1); i++) {
            colNames[i] = rsm.getColumnName(i);
            /**
             * If there are fields that you want to handle manually, check for
             * them here and change that entry in colNames to null. This will
             * cause the loop in the next section to skip that database column.
             */
            // //Example:
            // if (rsm.getColumnName(i) == "db_id")
            // {
            // colNames[i] = null;
            // }
        }

        while (rs.next()) {
            count++;
            innerCount++;

            SolrInputDocument doc = new SolrInputDocument();

            /**
             * At this point, take care of manual document field assignments for
             * which you previously assigned the colNames entry to null.
             */
            // //Example:
            // doc.addField("solr_db_id", rs.getLong("db_id"));
            for (int j = 1; j < (numColumns + 1); j++) {
                if (colNames[j] != null) {
                    Object f;
                    switch (rsm.getColumnType(j)) {
                        case Types.BIGINT: {
                            f = rs.getLong(j);
                            break;
                        }
                        case Types.INTEGER: {
                            f = rs.getInt(j);
                            break;
                        }
                        case Types.DATE: {
                            f = rs.getDate(j);
                            break;
                        }
                        case Types.FLOAT: {
                            f = rs.getFloat(j);
                            break;
                        }
                        case Types.DOUBLE: {
                            f = rs.getDouble(j);
                            break;
                        }
                        case Types.TIME: {
                            f = rs.getDate(j);
                            break;
                        }
                        case Types.BOOLEAN: {
                            f = rs.getBoolean(j);
                            break;
                        }
                        default: {
                            f = rs.getString(j);
                        }
                    }
                    doc.addField(colNames[j], f);
                }
            }
            docs.add(doc);

            /**
             * When we reach fetchSize, index the documents and reset the inner
             * counter.
             */
            if (innerCount == fetchSize) {
                solrCore.add(docs);
                docs.clear();
                innerCount = 0;
            }
        }

        /**
         * If the outer loop ended before the inner loop reset, index the
         * remaining documents.
         */
        if (innerCount != 0) {
            solrCore.add(docs);
        }
        return count;
    }

    /**
     * Reading Data from Solr
     */
    public static void readSolr() throws SolrServerException {
        // Get an instance of server first
        HttpSolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());

        // Construct a SolrQuery
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addSortField("price", SolrQuery.ORDER.asc);

        // Query the server
        QueryResponse rsp = server.query(query);

        // Get the results
        SolrDocumentList docs = rsp.getResults();

        // To read Documents as beans, the bean must be annotated as given in the example.
        List<Item> beans = rsp.getBeans(Item.class);
    }

    /**
     * Load Balance Solr Server
     * LBHttpSolrServer
     */
    public static void testLBHttpSolr() throws MalformedURLException {
        LBHttpSolrServer lbHttpSolrServer = new LBHttpSolrServer("http://host1:8080/solr/", "http://host2:8080/solr", "http://host3:8080/solr");
        //or if you wish to pass the HttpClient do as follows
        //HttpClient httpClient =  new HttpClient() {};
        //SolrServer lbHttpSolrServer = new LBHttpSolrServer(httpClient, "http://host1:8080/solr/","http://host2:8080/solr","http://host3:8080/solr");

        // LBHttpSolrServer does not keep pinging the servers to know if they are alive. 
        // If a request to a server fails by an Exception then the host is taken off the list 
        // of live servers and moved to a 'dead server list' and the request is resent to the next live server. 
        // This process is continued till it tries all the live servers. 
        // If atleast one server is alive the request succeeds , and if not it fails.
        // LBHttpSolrServer keeps pinging the dead servers once a minute (default value) to find 
        // if it is alive. The interval can be changed using
        lbHttpSolrServer.setAliveCheckInterval(60 * 1000); //time in milliseconds
        //remove one 
        lbHttpSolrServer.removeSolrServer("http://host2:8080/solr");
        //and add another
        lbHttpSolrServer.addSolrServer("http://host4:8080/solr");
    }

    /**
     * Using with SolrCloud : SolrJ includes a 'smart' client for SolrCloud,
     * which is ZooKeeper aware. This means that your Java application only
     * needs to know about your Zookeeper instances, and not where your Solr
     * instances are, as this can be derived from ZooKeeper.
     */
    public static void testCloudSolr() throws MalformedURLException, SolrServerException, IOException {
        CloudSolrServer server = new CloudSolrServer("localhost:9983");
        server.setDefaultCollection("collection1");
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "1234");
        doc.addField("name", "A lovely summer holiday");
        server.add(doc);
        server.commit();
    }


    /**
     * Method to index all types of files into Solr.
     *
     * @param fileName
     * @param solrId
     * @throws IOException
     * @throws SolrServerException
     */
    public static void indexFilesSolrCell(String fileName, String solrId)
            throws IOException, SolrServerException {

        String urlString = TcSolrConfig.getSolrURL();
        SolrServer solr = new HttpSolrServer(urlString); // new CommonsHttpSolrServer(urlString);

        ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");

        up.addFile(new File(fileName), "application/octet-stream");
        
        // up.setParam("literal.id", solrId);
        up.setParam("uprefix", "attr_");
        up.setParam("fmap.content", "attr_content");

        up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);

        solr.request(up);

        QueryResponse rsp = solr.query(new SolrQuery("*:*"));

        logger.debug(rsp.toString());
    }
    
    /**
     * Solr Facet Test
     * @param qStr
     * @param groupField
     * @param sortField
     * @param asc
     * @param pageSize
     * @param pageNum
     * @return 
     */
    public static Map<String, Integer> queryByGroup(String qStr, String groupField, String sortField, boolean asc, Integer pageSize, Integer pageNum) {
        Map<String, Integer> rmap = new LinkedHashMap<String, Integer>();
        try {
            SolrServer server = new HttpSolrServer(TcSolrConfig.getSolrURL());
            SolrQuery query = new SolrQuery();
            if (qStr != null && qStr.length() > 0) {
                query.setQuery(qStr);
            } else {
                query.setQuery("*:*");//如果没有查询语句，必须这么写，否则会报异常
            }
            query.setIncludeScore(false);//是否按每组数量高低排序
            query.setFacet(true);//是否分组查询
            query.setRows(0);//设置返回结果条数，如果你时分组查询，你就设置为0
            query.addFacetField(groupField);//增加分组字段
            query.setFacetSort(true);//分组是否排序
            query.setFacetLimit(pageSize);//限制每次返回结果数
            query.setSortField(sortField, asc ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);//分组排序字段
            query.set(FacetParams.FACET_OFFSET, (pageNum - 1) * pageSize);//当前结果起始位置
            QueryResponse rsp = server.query(query);

            List<FacetField.Count> countList = rsp.getFacetField(groupField).getValues();
            List<FacetField.Count> returnList;
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
        } catch (SolrServerException e) {
            logger.debug("SolrServerException Exception:", e);
        }
        return rmap;
    }
}

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple main to illustrate how to execute a request using SolrJ
 *
 */
public class SolrJExampleMain {
    private static final Logger logger = LoggerFactory.getLogger(SolrJExampleMain.class);

    private static final String myServer = "http://localhost:8080/constellio/app";
    private static final String myCollection = "test";
    // Can be set to 'on', 'off' or 'constellio' to include Constellio's facets
    private static final String facet = "constellio";
    // q=...
    private static final String query = "open source";
    private static final int start = 0;
    private static final int nbDocuments = 11;

    public static void main(String[] args) throws MalformedURLException,
            SolrServerException {

        // Prepare the SolrServer. Right now, the default SolrJ's ResponseParser
        // isn't supported by Constellio.
        HttpSolrServer server = new HttpSolrServer(myServer);
        server.setParser(new XMLResponseParser());

        // Do the same query three times using three different method
        logger.debug("= = = = = = = = = = = = = = = = = First way to execute a query = = = = = = = = = = = = = = = = =");
        //print(doFirstQuery(server));
        logger.debug("= = = = = = = = = = = = = = = = = Second way to execute a query = = = = = = = = = = = = = = = = =");
        print(doSecondQuery(server));
        logger.debug("= = = = = = = = = = = = = = = = = Third way to execute query = = = = = = = = = = = = = = = = =");
        print(doThirdQuery(server));
        logger.debug("= = = = = = = = = = = = = = = = = Using SpellChecker = = = = = = = = = = = = = = = = =");
        print(spellCheck(server, "opn sorce source"));
    }

    /**
     * Do the query using a StringBuffer
     */
    /*public static QueryResponse doFirstQuery(SolrServer server)
            throws SolrServerException {
        StringBuilder request = new StringBuilder();
        request.append("collectionName=" + myCollection);
        request.append("&username=" + "admin");
        request.append("&password=" + "password");
        request.append("&facet=" + facet);
        request.append("&q=" + query);
        request.append("&start=").append(start);
        request.append("&rows=").append(nbDocuments);
        SolrParams solrParams = SolrRequestParsers.parseQueryString(request.toString());

        return server.query(solrParams);
    }*/

    /**
     * Do the query using a ModifiableSolrParams
     */
    public static QueryResponse doSecondQuery(SolrServer server)
            throws SolrServerException {
        ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("collectionName", myCollection);
        solrParams.set("username", "admin");
        solrParams.set("password", "password");
        solrParams.set("facet", facet);
        solrParams.set("q", query);
        solrParams.set("start", start);
        solrParams.set("rows", nbDocuments);
        return server.query(solrParams);
    }

    /**
     * Do the query using a SolrQuery
     */
    public static QueryResponse doThirdQuery(SolrServer server)
            throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        solrQuery.set("collectionName", myCollection);
        solrQuery.set("username", "admin");
        solrQuery.set("password", "password");
        solrQuery.set("facet", facet);
        solrQuery.setStart(start);
        solrQuery.setRows(nbDocuments);
        return server.query(solrQuery);
    }

    /**
     * Do the query using a SolrQuery
     */
    public static QueryResponse spellCheck(SolrServer server, String badQuery)
            throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(badQuery);
        solrQuery.set("collectionName", myCollection);

        // qt=spellcheck || qt=spellchecker
        solrQuery.setQueryType("spellcheck");
        return server.query(solrQuery);
    }

    /**
     * Print documents and facets
     *
     * @param response
     */
    @SuppressWarnings("unchecked")
    public static void print(QueryResponse response) {
        SolrDocumentList docs = response.getResults();
        if (docs != null) {
            System.out.println(docs.getNumFound() + " documents found, "
                    + docs.size() + " returned : ");
            for (int i = 0; i < docs.size(); i++) {
                SolrDocument doc = docs.get(i);
                logger.debug("\t" + doc.toString());
            }
        }

        List<FacetField> fieldFacets = response.getFacetFields();
        if (fieldFacets != null && fieldFacets.isEmpty()) {
            logger.debug("\nField Facets : ");
            for (FacetField fieldFacet : fieldFacets) {
                System.out.print("\t" + fieldFacet.getName() + " :\t");
                if (fieldFacet.getValueCount() > 0) {
                    for (Count count : fieldFacet.getValues()) {
                        System.out.print(count.getName() + "["
                                + count.getCount() + "]\t");
                    }
                }
                logger.debug("");
            }
        }

        Map<String, Integer> queryFacets = response.getFacetQuery();
        if (queryFacets != null && !queryFacets.isEmpty()) {
            logger.debug("\nQuery facets : ");
            for (String queryFacet : queryFacets.keySet()) {
                logger.debug("\t" + queryFacet + "\t["
                        + queryFacets.get(queryFacet) + "]");
            }
            logger.debug("");
        }

        NamedList<NamedList<Object>> spellCheckResponse = (NamedList<NamedList<Object>>) response
                .getResponse().get("spellcheck");

        if (spellCheckResponse != null) {
            Iterator<Entry<String, NamedList<Object>>> wordsIterator = spellCheckResponse
                    .iterator();

            while (wordsIterator.hasNext()) {
                Entry<String, NamedList<Object>> entry = wordsIterator.next();
                String word = entry.getKey();
                NamedList<Object> spellCheckWordResponse = entry.getValue();
                boolean correct = spellCheckWordResponse.get("frequency")
                        .equals(1);
                logger.debug("Word: " + word + ",\tCorrect?: " + correct);
                NamedList<Integer> suggestions = (NamedList<Integer>) spellCheckWordResponse
                        .get("suggestions");
                if (suggestions != null && suggestions.size() > 0) {
                    logger.debug("Suggestions : ");
                    Iterator<Entry<String, Integer>> suggestionsIterator = suggestions
                            .iterator();
                    while (suggestionsIterator.hasNext()) {
                        logger.debug("\t"
                                + suggestionsIterator.next().getKey());
                    }
                }
                logger.debug("");
            }
        }
    }

    /**
     * 依特定 KEY 自 Solr Query Response 取值
     * 
     * @param result
     * @param paths
     * @return 
     */
    public static Object getResponseByKey(NamedList<Object> result, List<String> paths){
        if( result==null || paths==null || result.size()==0 || paths.isEmpty() ){
            return null;
        }
        
        Iterator<Entry<String, Object>> it = result.iterator();
        
        String key = paths.get(0);
        while( it.hasNext() ){
            Entry<String, Object> entry = it.next();
            
            if( key.equals((String)entry.getKey()) ){
                logger.debug("key="+key+"; "+entry.getKey() + " = " + entry.getValue());
                if( paths.size()==1 ){
                    return entry.getValue();
                }else{
                    paths.remove(key);
                    if(entry.getValue() instanceof NamedList){
                        return getResponseByKey((NamedList<Object>)entry.getValue(), paths);
                    }else{
                        return null;
                    }
                }
            }
        }
        
        return null;
    }
}

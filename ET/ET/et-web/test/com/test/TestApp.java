/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author peter.pan
 */
public class TestApp {
    private final static Logger logger = LoggerFactory.getLogger(TestApp.class);
    private final static String URL_BASE = "http://localhost:8080/et-web/services";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        testTendereds();
    }
    
    public static void testTendereds(){
        Map<String, Object> resHeaders = new HashMap<String, Object>();
        String jsonParams = "{\n"
                + " \"memberId\":2 \n"
                + "}";
        String res = callService("POST", "/rfq/tendered/count", jsonParams, null, resHeaders);
        
        res = callService("POST", "/rfq/tendered/list", jsonParams, null, resHeaders);
    }
    
    /**
     * 呼叫 Service
     * @param method
     * @param strUrl
     * @param jsonParams
     * @param reqHeaders
     * @param resHeaders
     * @return
     */
    public static String callService(String method,
            String strUrl, String jsonParams,
            Map<String, Object> reqHeaders,
            Map<String, Object> resHeaders) {
        String result = "";
        try {
            String url = URL_BASE + strUrl;
            logger.debug("callService url = "+url);
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 5000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            
            HttpResponse response = null;
            if( "POST".equals(method.toUpperCase()) ){
                HttpPost postRequest = new HttpPost(url);
                StringEntity input = new StringEntity(jsonParams, "UTF-8");
                input.setContentType("application/json");
                
                // 透過 Header 輸入資訊時
                if( reqHeaders!=null ){
                    setReqHeaders(postRequest, reqHeaders);
                }
                postRequest.setEntity(input);
                response = httpClient.execute(postRequest);
            }else if( "GET".equals(method.toUpperCase()) ){
                HttpGet getRequest = new HttpGet(url);
                // 透過 Header 輸入資訊時
                if( reqHeaders!=null ){
                    setReqHeaders(getRequest, reqHeaders);
                }
                response = httpClient.execute(getRequest);
            }
            
            if( response!=null && response.getStatusLine()!=null ){
                int httpStatus = response.getStatusLine().getStatusCode();
                logger.debug("response.getStatusLine().getStatusCode() = "+response.getStatusLine().getStatusCode());
                /*
                logger.debug("response.getEntity().getContentLength() = "+response.getEntity().getContentLength());
                logger.debug("response.getEntity().getContentType() = "+response.getEntity().getContentType());
                logger.debug("response.getEntity().getContentEncoding() = "+response.getEntity().getContentEncoding());
                logger.debug("response.getEntity() = "+response.getEntity());
                */
                if( httpStatus==200 ){
                    result = getResult(response);
                    logger.debug("result = "+result);
                    if( resHeaders!=null ){
                        // Header 有回傳需要資訊時
                        Header[] headers = response.getAllHeaders();
                        if( headers!=null ){
                            for(Header header : headers){
                                resHeaders.put(header.getName(), header.getValue());
                            }
                        }
                    }
                }else if( httpStatus==401 ){
                    logger.error("!!! Unauthorized !!!");
                }
            }else{
                logger.error("response==null || response.getStatusLine()==null");
            }
            
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            logger.error("callService Exception:\n", e);
        }
        
        return result;
    }
    
    private static void setReqHeaders(HttpRequest request, Map<String, Object> reqHeaders){
        // 透過 Header 輸入資訊時
        if( reqHeaders!=null ){
            for(String key : reqHeaders.keySet()){
                request.setHeader(key, (String)reqHeaders.get(key));
            }
        }
    }
    
    private static String getResult(HttpResponse response) throws IllegalStateException, IOException {
        String encoding = (response.getEntity().getContentEncoding()!=null)
                ?response.getEntity().getContentEncoding().getValue():"UTF-8";
        return streamToString(response.getEntity().getContent(), encoding, response.getEntity().getContentLength());
    }
    
    private static String streamToString(InputStream is, String encoding, long len) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[(len>1024 || len<=0)?1024:(int)len];
        int length;
        while(( length = is.read(buffer)) != -1 ){
            result.write(buffer, 0, length);
        }
        return result.toString(encoding);
    }
    
    /**
     * 上傳圖片
     * @param strUrl
     * @param id
     * @param parent
     * @param files
     * @param subjectList
     * @param statusList
     * @param reqHeaders
     */
    public static void uploadImage(String strUrl, 
            Long id, 
            Long parent, 
            List<String> files, 
            List<String> subjectList, 
            List<String> statusList, 
            Map<String, Object> reqHeaders)
    {
        String url = URL_BASE + strUrl;
        // the file we want to upload
        // List<File> filesList = new ArrayList<File>();
        List<FileInputStream> fisList = new ArrayList<FileInputStream>();
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            
            // server back-end URL
            HttpPost httppost = new HttpPost(url);
            // 透過 Header 輸入資訊時
            if( reqHeaders!=null ){
                setReqHeaders(httppost, reqHeaders);
            }
            
            //MultipartEntity entity = new MultipartEntity();
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName("UTF-8"));
            // for 植物照片 KB_ACCESSION.ID & 往來人員照片 KB_CONTACTS.ID
            if( id!=null ){
                entity.addPart("id", new StringBody(id.toString(), Charset.forName("UTF-8")));
            }
            // for 自訂圖庫 KB_PHOTO_GALLERY.ID )
            if( parent!=null ){
                entity.addPart("parent", new StringBody(parent.toString(), Charset.forName("UTF-8")));
            }
            
            if( files!=null && !files.isEmpty() ){
                int i = 0;
                for(String filename : files){
                    File file = new File(filename);
                    // set the file input stream and file name as arguments
                    FileInputStream fis = new FileInputStream(file);
                    // 檔名: 防止文件名亂碼
                    entity.addPart("filenames", new StringBody(file.getName(), Charset.forName("UTF-8")));
                    // 檔案內容
                    entity.addPart("files", new InputStreamBody(fis, file.getName()));
                    // 圖片說明
                    if( subjectList!=null && subjectList.get(i)!=null ){
                        entity.addPart("subjects", new StringBody(subjectList.get(i), Charset.forName("UTF-8")));
                    }
                    // 狀態
                    if( statusList!=null && statusList.get(i)!=null ){
                        entity.addPart("status", new StringBody(statusList.get(i), Charset.forName("UTF-8")));
                    }else{
                        entity.addPart("status", new StringBody("D", Charset.forName("UTF-8")));// ContentStatusEnum
                    }
                    fisList.add(fis);// for close finally
                }
            }
            
            httppost.setEntity(entity);
            //httppost.setHeader("Content-Charset", "UTF-8");
            //httppost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            
            // execute the request
            HttpResponse response = httpclient.execute(httppost);
            
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity, "UTF-8");
            
            logger.info("[" + statusCode + "] " + responseString);
        } catch (ClientProtocolException e) {
            logger.error("uploadImage ClientProtocolException:\n", e);
        } catch (IOException e) {
            logger.error("uploadImage IOException:\n", e);
        } finally {
            try {
                if( !fisList.isEmpty() ){
                    for(FileInputStream fis : fisList){
                        if(fis != null) fis.close();
                    }
                }
            } catch (IOException e) {
                logger.error("uploadImage finally IOException :"+e.toString());
            }
        }
    }
    
}

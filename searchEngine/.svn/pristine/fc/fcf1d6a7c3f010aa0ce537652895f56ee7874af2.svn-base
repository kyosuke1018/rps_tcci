package com.tcci.solr.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: peter.pan
 * Date: 9/5/12
 */
@ManagedBean(name = "fileController")
@ViewScoped
public class FileController implements Serializable {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String SOURCE_DOC_ROOT = "D:\\BAK\\solr";
    public static String SOURCE_DOC_AP_DIR = "\\SolrWebDemo";
    
    private UploadedFile file;
    private StreamedContent downloadFile;
    
    private String fileAbsolutePath;// 完正檔名路徑
    private String serverRelDir;// 相對路徑資料夾
    private String serverFileName;// 儲存檔名
    
    public FileController() {
    }
    
    public UploadedFile getFile() {
        return file;
    }
    
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    /**
     * Handle File Upload
     * @param event 
     */
    public void handleFileUpload(FileUploadEvent event) {
        logger.debug("handleFileUpload ... event.getFile()="+event.getFile());
        file = event.getFile();
    }
    
    /**
     * 上傳檔暫存至 User Temp (實際請使用 Fundation libs 放至 TC_FVVAULT 指定位置)
     */
    public void saveDocument(){
    	try{
            // 實際儲存檔案
            File tempFile = saveRealFile();
            
            // 取得檔案位置資訊
            fileAbsolutePath = tempFile.getAbsolutePath();
            int i = fileAbsolutePath.lastIndexOf(File.separator);
            serverRelDir = fileAbsolutePath.substring(0, i); //.substring(SOURCE_DOC_ROOT.length()); // 改用JNDI設對應表
            serverFileName = fileAbsolutePath.substring(i+1);
    	}catch(Exception e){
            e.printStackTrace();
    	}        
    }
    
    /**
     * 實際儲存檔案
     * @return
     * @throws IOException 
     */
    public File saveRealFile() throws IOException{
        //create a temp file
        File tempFile = File.createTempFile("TEMP_", "_"+file.getFileName(), new File(SOURCE_DOC_ROOT+SOURCE_DOC_AP_DIR));

        FileOutputStream fop = new FileOutputStream(tempFile);

        // get the content in bytes
        byte[] contentInBytes = file.getContents();

        fop.write(contentInBytes);
        fop.flush();
        fop.close();
        
        return tempFile;
    }
    
    public StreamedContent getDownloadFile() {
        return downloadFile;
    }
    
    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public String getServerRelDir() {
        return serverRelDir;
    }

    public void setServerRelDir(String serverRelDir) {
        this.serverRelDir = serverRelDir;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }    
}
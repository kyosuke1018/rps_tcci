package com.tcci.fc.util;

import com.tcci.cm.model.global.GlobalConstant;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 檔案處理工具
 * 
 * @pdOid 9a421c5b-2565-4a59-8aff-a2bd69df6c8e
 * @author jackson
 */
public class FileUtils extends org.apache.commons.io.FileUtils{

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    
    /**
     * 取得檔名、附檔名
     * @param fileName
     * @return 
     */
    public static String[] getFileExtension(String fileName){
        int i = fileName.lastIndexOf(".");
        String name = "";
        String fext = "";
        if( i>-1 ){
            name = fileName.substring(0, i);
            fext = fileName.substring(i+1);// 取附檔名
        }
        
        return new String[]{name, fext};
    }

    /**
     * 取得附檔名.
     * 
     * @param file
     * @return String
     */
    public static String getExtFileName(final File file) {
        return file.getName().substring(file.getName().lastIndexOf("."));
    }

    /**
     * 取得檔名.
     * 
     * @param file
     * @return String
     */
    public static String getFileName(final File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }

    /**
     * 將來源檔案移到 destDir 目錄中。
     * 
     * @param srcFile 來源檔案
     * @param destFile 目的目錄
     * @return destFile 移動後的目錄
     */
    public final static File renameTo(File srcFile, File destFile) {
        logger.debug(" Rename from ["
                + srcFile.getPath()
                + "] to ["
                + destFile.getPath()
                + "] : "
                + srcFile.renameTo(destFile));

        return destFile;
    }


    /**
     * 根據目錄名稱，取得目錄的 {@link File} 物件，如果不存在，產生一個新的.
     * @param dirName
     * @return file
     */
    public static File toDir(String dirName) {
        return toDir(dirName, true);
    }

    /**
     * 根據目錄名稱，取得目錄的 {@link File} 物件。如果不存在，依傳入參數決定是否要產生一個新的.
     * 
     * @param dirName
     * @param create
     * @return dirFile
     */
    public static File toDir(String dirName, boolean create) {
        File dirFile = new File(dirName);
        if (create) {
            if (!dirFile.exists()) {
                logger.debug("Parent : " + dirFile.getParent());

                if (dirFile.getParent() != null) {
                    toDir(dirFile.getParent(), create);
                }

                logger.debug("Mkdir " + dirFile.getPath() + ": " + dirFile.mkdir());
            }
        }
        return dirFile;
    }

    public final static boolean isFileExist(String fileName) {
        return new File(fileName).exists();
    }

    /**
     * 檢查檔名是否存在，如果不存在，產生一個新的檔案.
     * 
     * @param fileName
     * @return File
     */
    public static File createFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            logger.error("系統錯誤: 編號 ["+System.currentTimeMillis()+"]", e);
        }
        return file;
    }
    
    /**
     * 讀取檔案到 Byte Array (使用 NIO)
     * @param filefullname
     * @return 
     */
    public static byte[] file2Bytes(String filefullname){
        // File f = new File("c:\\wscp.script");
        File f = new File(filefullname);
        
        if( !f.exists() || !f.isFile() || !f.canRead() ){
            logger.error("FileUtils.java => file2Bytes file no ready : filefullname = " + filefullname);
            return null;
        }
        
        FileInputStream fin = null;
        FileChannel ch = null;
        byte[] bytes = null;
        
        try {
            fin = new FileInputStream(f);
            ch = fin.getChannel();
            int size = (int) ch.size();
            MappedByteBuffer buf = ch.map(MapMode.READ_ONLY, 0, size);
            bytes = new byte[size];
            buf.get(bytes);

        } catch (IOException e) {
            logger.error("系統錯誤: 編號 ["+System.currentTimeMillis()+"]", e);
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (ch != null) {
                    ch.close();
                }
            } catch (IOException e) {
                logger.error("系統錯誤: 編號 ["+System.currentTimeMillis()+"]", e);
            }
        }
        
        return bytes;
    }
    
    public static String getTempPath() throws IOException{
        File temp = File.createTempFile("temp-file-name", ".tmp"); 

        //Get tempropary file path
        String absolutePath = temp.getAbsolutePath();
        String tempFilePath = absolutePath.
            substring(0,absolutePath.lastIndexOf(GlobalConstant.FILE_SEPARATOR));
        
        return tempFilePath;
    }
}
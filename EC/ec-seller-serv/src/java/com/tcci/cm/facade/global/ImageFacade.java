/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.global;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.ImageSizeEnum;
import com.tcci.ec.model.rs.ImageVO;
import com.tcci.fc.util.FileUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class ImageFacade {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getFullPath(String imgSrc, String imgType, Long storeId){
        FileEnum srcEnum = FileEnum.getFromCode(imgSrc);
        ImageSizeEnum sizeEnum = ImageSizeEnum.getFromCode(imgType);
        
        if( srcEnum==null ){
            logger.error("getFullFileName srcEnum = null, imgSrc = "+imgSrc);
            return null;
        }
        sizeEnum = (sizeEnum==null)?ImageSizeEnum.ORIGINAL:sizeEnum;
        
        StringBuilder sb = new StringBuilder();
        sb.append(srcEnum.getRootDir())
          .append(srcEnum.isByStore()?storeId:"")
          .append(sizeEnum.getFolder());
        
        return sb.toString();
    }

    public String getFullFileName(String imgSrc, String imgType, Long storeId, String fileName){
        String path = getFullPath(imgSrc, imgType, storeId);
        if( path!=null ){ 
            StringBuilder sb = new StringBuilder();
            sb.append(path)
              .append(GlobalConstant.FILE_SEPARATOR)
              .append(fileName);
            return sb.toString();
        }
        
        return null;
    }
    
    public String getFullFileNameSafe(String imgSrc, String imgType, Long storeId, String fileName){
        ImageSizeEnum sizeEnum = ImageSizeEnum.getFromCode(imgType);
        if( ImageSizeEnum.ORIGINAL == sizeEnum ){
            return getFullFileName(imgSrc, imgType, storeId, fileName);
        }else if( ImageSizeEnum.BIG == sizeEnum ){
            String filename = getFullFileName(imgSrc, imgType, storeId, fileName);
            File f = new File(filename);
            if( f.isFile() && f.exists() && f.canRead() ){
                return filename;
            }else{
                return getFullFileName(imgSrc, ImageSizeEnum.ORIGINAL.getCode(), storeId, fileName);
            }
        }else if( ImageSizeEnum.SMALL == sizeEnum ){
            String filename = getFullFileName(imgSrc, imgType, storeId, fileName);
            File f = new File(filename);
            if( f.isFile() && f.exists() && f.canRead() ){
                return filename;
            }else{
                filename = getFullFileName(imgSrc, ImageSizeEnum.BIG.getCode(), storeId, fileName);
                f = new File(filename);
                if( f.isFile() && f.exists() && f.canRead() ){
                    return filename;
                }else{
                    return getFullFileName(imgSrc, ImageSizeEnum.ORIGINAL.getCode(), storeId, fileName);
                }
            }
        }
        logger.error(MessageFormat.format("getFullFileNameSafe imgSrc={0}, imgType={1}, storeId={2}, fileName={3}", 
                imgSrc, imgType, storeId, fileName));
        return "";
    }

    
    /**
     * 取得Image寬高
     * @param fullFilename
     * @param retImgVO
     * @return 
     */
    public String getImageInfo(String fullFilename, ImageVO retImgVO){
        String errMsg = null;
        
        InputStream in = null;
        File f = new File(fullFilename);
        if( !f.isFile() || !f.exists() || !f.canRead() ){
            errMsg = "輸入圖片檔不存在!";
            return errMsg;
        }
        
        try{
            in = Files.newInputStream(Paths.get(fullFilename));// use NIO
            BufferedImage bi = ImageIO.read(in);// use NIO
            if( bi!=null ){
                logger.debug("compressImage Width = " + bi.getWidth());
                logger.debug("compressImage Height = " + bi.getHeight());

                retImgVO.setWidth(bi.getWidth());// 取得圖片資訊
                retImgVO.setHeight(bi.getHeight());// 取得圖片資訊
                retImgVO.setSize(f.length());

                //if( bi.getPropertyNames()!=null ){
                //    for(String name : bi.getPropertyNames()){
                //        logger.debug("compressImage " + name + " = " + bi.getProperty(name));
                //    }
                //}
            }
        }catch(IOException e){
            errMsg = "讀取檔案發生錯誤!";
            logger.error("compressImage IOException:\n", e);
            return errMsg;
        }finally{
            if( in!=null ){
                try{
                    in.close();
                }catch(Exception e){
                    logger.error("compressImage InputStream close Exception:\n", e);
                }
            }
        }
        
        return errMsg;
    }

    //<editor-fold defaultstate="collapsed" desc="for 縮圖">
    /**
     * 縮圖 (並取得圖片資訊)
     * @param holderEntity
     * @param force 強制重新產生
     * @param retImgVO 若關聯多圖檔，只傳最後一個圖檔資訊
     * @return 
     */
    /*public String compressImageByHolder(ContentHolder holderEntity, boolean force, ImageVO retImgVO){
        if( holderEntity==null || retImgVO==null ){
            return "未輸入圖片資訊!";
        }
        List<AttachmentVO> attachments = attachmentFacade.loadContent(holderEntity);
        retImgVO.setId(holderEntity.getId());
        
        StringBuilder retMsg = new StringBuilder();
        if( attachments!=null ){
            for(AttachmentVO vo : attachments){
                String msg = compressImage(vo, force, retImgVO);
                if( msg!=null ){
                    retMsg.append(msg).append("\n");
                }
            }
        }
        
        return retMsg.toString();
    }
    public String compressImageByHolder(ContentHolder holderEntity, ImageVO retImgVO){
        return compressImageByHolder(holderEntity, true, retImgVO);
    }*/
    
    /**
     * 指定資料夾內圖片縮檔
     * 儲存路徑資訊參考 JNDI
     * @param dir
     * @param force 強制重新產生
     * @return 
     */
    public String compressImagesInFolder(String dir, boolean force){
        StringBuilder errMsgSB = new StringBuilder();
        
        File dirF = new File(dir);
        if( !dirF.exists() || !dirF.isDirectory() ){
            return errMsgSB.append(dir).append(": 輸入檔案路徑錯誤!").toString();
        }
        
        int counts = 0;
        if( dirF.listFiles()!=null ){
            for(File file : dirF.listFiles(new ImageFileFilter())){
                if( dirF.isFile() && dirF.canRead() ){
                    ImageVO retImgVO = new ImageVO();
                    String userFilename = file.getName();
                    String fvFilename = userFilename;
                    String fullFilename = file.getAbsolutePath();
                    
                    StringBuilder retMsg = new StringBuilder();
                    retMsg.append(userFilename);

                    logger.debug("compressImagesInFolder process "+fullFilename);
                    // 取得Image寬高
                    String errMsg1 = getImageInfo(fullFilename, retImgVO);
                    if( errMsg1!=null ){
                        errMsgSB.append(":").append(errMsg1).append("\n").toString();
                    }else{
                        // 縮圖 (縮圖需寬高資訊)
                        String errMsg2 = compressImageFile(userFilename, fvFilename, fullFilename, force, retImgVO);
                        if( errMsg2!=null ){
                            errMsgSB.append(":").append(errMsg2).append("\n").toString();
                        }
                    }
                    counts++;
                }
            }
        }
        
        logger.info("compressImagesInFolder dir = "+dir+": counts = "+counts);
        return errMsgSB.toString();
    }
    
    /**
     * 縮圖 (並取得圖片資訊)
     * @param root 儲存位置
     * @param saveFilename 儲存檔名
     * @param oriFileanme 待壓縮原始檔 完整路徑
     * @param force 強制重新產生
     * @param retImgVO 
     * @return  
     */
    public String compressImage(String root, String saveFilename, String oriFileanme, boolean force, ImageVO retImgVO){
        /*if( attachmentVO==null || attachmentVO.getApplicationdata()==null 
         || attachmentVO.getApplicationdata().getFvitem()==null 
         || attachmentVO.getApplicationdata().getFvitem().getFilename()==null ){
            logger.debug("compressImage error attachmentVO==null ");
            String errMsg = "輸入圖片資訊錯誤!";
            return errMsg;
        }
        
        String userFilename = attachmentVO.getFileName();
        String fvFilename = attachmentVO.getApplicationdata().getFvitem().getFilename();
        String fullFilename = attachmentFacade.getFullFileName(attachmentVO);
        Long itemId = attachmentVO.getApplicationdata().getFvitem().getId();
        logger.debug("compressImage fullFilename = "+fullFilename);*/
        
        StringBuilder retMsg = new StringBuilder();
        retMsg.append(saveFilename);//.append("[").append(itemId).append("]");
        
        // 取得Image寬高
        String errMsg1 = getImageInfo(oriFileanme, retImgVO);
        if( errMsg1!=null ){
            return retMsg.append(":").append(errMsg1).toString();
        }else{
            // 縮圖 (縮圖需寬高資訊)
            //String errMsg2 = compressImageFile(userFilename, fvFilename, fullFilename, force, retImgVO);
            String errMsg2 = compressImageFile(root, saveFilename, oriFileanme, force, retImgVO);
            if( errMsg2!=null ){
                return retMsg.append(":").append(errMsg2).toString();
            }
        }
        return null;
    }
    
    /**
     * 縮圖 (縮圖需寬高資訊)
     * 儲存路徑資訊參考 JNDI
     * @param root 儲存路徑
     * @param saveFilename 儲存檔名(無路徑)
     * @param oriFileanme 完整檔名
     * @param force 強制重新產生
     * @param retImgVO Image寬高資訊
     * @return 
     */
    //public String compressImageFile(String userFilename, String fvFilename, String fullFilename, boolean force, ImageVO retImgVO){
    public String compressImageFile(String root, String saveFilename, String oriFileanme, boolean force, ImageVO retImgVO){
        String errMsg = null;
        if( root==null ){
            return "未輸入儲存路徑!";
        }
        root = root.endsWith(GlobalConstant.FILE_SEPARATOR)?root.substring(0, root.length()-1):root;
        
        try{
            File dirfile = new File(root + ImageSizeEnum.BIG.getFolder());
            dirfile.mkdirs(); //for several levels, without the "s" for one level
            // 大圖長、寬不大於 1600 X 1600
            String bigFilename = root + ImageSizeEnum.BIG.getFolder() + GlobalConstant.FILE_SEPARATOR + saveFilename;
            if( force || !FileUtils.isFileExist(bigFilename) ){
                double wRateB = retImgVO.getWidth()/GlobalConstant.WIDTH_BIG_IMG;
                double hRateB = retImgVO.getHeight()/GlobalConstant.HEIGHT_BIG_IMG;

                if( wRateB>1 || hRateB>1 ){// 大圖不大於 1600 X 1600
                    logger.debug("compressImage has bigFilename = "+bigFilename);
                    if( wRateB<hRateB ){
                        Thumbnails.of(new File(oriFileanme)).height(GlobalConstant.HEIGHT_BIG_IMG).toFile(new File(bigFilename));
                    }else{
                        Thumbnails.of(new File(oriFileanme)).width(GlobalConstant.WIDTH_BIG_IMG).toFile(new File(bigFilename));
                    }
                }
            }
        }catch(IOException e){
            errMsg = "大圖壓縮發生錯誤!";
            logger.error("compressImage IOException:\n", e);
            return errMsg;
        }

        try{
            File dirfile = new File(root + ImageSizeEnum.SMALL.getFolder());
            dirfile.mkdirs(); //for several levels, without the "s" for one level
            // 小圖長、寬不小於 300 X 300
            String smallFilename = root + ImageSizeEnum.SMALL.getFolder() + GlobalConstant.FILE_SEPARATOR + saveFilename;
            if( force || !FileUtils.isFileExist(smallFilename) ){
                double wRateS = retImgVO.getWidth()/GlobalConstant.WIDTH_SMALL_IMG;
                double hRateS = retImgVO.getHeight()/GlobalConstant.HEIGHT_SMALL_IMG;

                if( wRateS>1 && wRateS>1 ){// 小圖不小於 300 X 300
                    logger.debug("compressImage has smallFilename = "+smallFilename);
                    if( wRateS<hRateS ){
                        Thumbnails.of(new File(oriFileanme)).width(GlobalConstant.WIDTH_SMALL_IMG).toFile(new File(smallFilename));
                    }else{
                        Thumbnails.of(new File(oriFileanme)).height(GlobalConstant.HEIGHT_SMALL_IMG).toFile(new File(smallFilename));
                    }
                }
            }
        }catch(IOException e){
            errMsg = "小圖壓縮發生錯誤!";
            logger.error("compressImage IOException:\n", e);
            return errMsg;
        }
        
        if( errMsg==null ){
            retImgVO.setCompressed(true);// 取得圖片資訊
        }
        
        return errMsg;
    }
    //</editor-fold>
}

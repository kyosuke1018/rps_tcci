/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.global;

import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.model.global.ImageVO;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.facade.content.TcFvvaultFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.util.FileUtils;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.enums.ImageSizeEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.enums.FileEnum;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    
    @EJB private TcDomainFacade domainFacade;
    @EJB private AttachmentFacade attachmentFacade;
    @EJB private TcFvvaultFacade tcFvvaultFacade;
    @EJB private SysResourcesFacade sysResourcesFacade;
    
    private Map<String, TcDomain> domainMap;// 效能考量，先 Cache
    
    @PostConstruct
    public void init(){
        logger.debug("ImageFacade init ...");
        logger.debug("ImageFacade init ..."+GlobalConstant.DOMAIN_NAME_DOC_IMG);
        logger.debug("ImageFacade init ..."+domainFacade.findByName(GlobalConstant.DOMAIN_NAME_DOC_IMG).get(0));
        logger.debug("ImageFacade init ..."+domainFacade.getDomainByName(GlobalConstant.DOMAIN_NAME_DOC_IMG));
        domainMap = new HashMap<>();
        domainMap.put(GlobalConstant.DOMAIN_NAME_DOC_IMG, domainFacade.getDomainByName(GlobalConstant.DOMAIN_NAME_DOC_IMG));
        domainMap.put(GlobalConstant.DOMAIN_NAME_CUSTOM_IMG, domainFacade.getDomainByName(GlobalConstant.DOMAIN_NAME_CUSTOM_IMG));
    }
    
    public TcDomain getDomain(String domainName){
        return domainMap.get(domainName);
    }
    
    /**
     * 取得要顯示的圖 (小圖、大圖、原圖)
     * @param imgType
     * @param domainName
     * @param fileName
     * @return 
     */
    public String getDisplayImage(String imgType, String domainName, String fileName){
        String fullFileanme = null;
        File f = null;
        if( imgType==null ){
            imgType = ImageSizeEnum.SMALL.getCode();// 預設小圖
        }
        // 未指定或指定小圖
        if( imgType.equals(ImageSizeEnum.SMALL.getCode()) ){
            fullFileanme = new StringBuilder().append(sysResourcesFacade.getSmallImgaeDir()).append(File.separator).append(fileName).toString();
            f = new File(fullFileanme);
        }
        
        // 指定大圖或指定小圖不存在
        if( imgType.equals(ImageSizeEnum.BIG.getCode()) || (imgType.equals(ImageSizeEnum.SMALL.getCode()) && !(f!=null && f.isFile() && f.exists() && f.canRead())) ){
            fullFileanme = new StringBuilder().append(sysResourcesFacade.getBigImgaeDir()).append(File.separator).append(fileName).toString();
            f = new File(fullFileanme);
        }
        
        // 指定原圖或指定大、小圖不存在
        if( imgType.equals(ImageSizeEnum.ORIGINAL.getCode()) || (!(f!=null && f.isFile() && f.exists() && f.canRead())) ){
            TcDomain domain = this.getDomain(domainName);
            TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, GlobalConstant.getFvVaultHost());

            if( tcFvvault==null || tcFvvault.getLocation()==null ){
                logger.error("outputImagePublic tcFvvault==null || tcFvvault.getLocation()==null, domainName ="+domainName);
                return "NA";
            }
            String path = tcFvvault.getLocation();

            fullFileanme = (path.endsWith(File.separator))? 
                    new StringBuilder().append(path).append(fileName).toString() : 
                    new StringBuilder().append(path).append(File.separator).append(fileName).toString();
        }
        
        return fullFileanme;
    }
    
    /**
     * 產生至 map 存取圖用的 key
     * @param attachmentVO
     * @return 
     */
    public String genMappingKey(AttachmentVO attachmentVO){
        return TcApplicationdata.class.getName()+"_"+Long.toString(attachmentVO.getApplicationdata().getId());
    }
    
    /**
     * 準備顯示圖片 Private URL 資訊 
     * @param domainName
     * @param attachmentVO
     * @param showImgKey
     * @return 
     * @throws java.io.UnsupportedEncodingException 
     */
    public String preparePrivateUrl(String domainName, AttachmentVO attachmentVO, String showImgKey) throws UnsupportedEncodingException{
        String contentType = attachmentVO.getContentType();
        //String fullFileName = attachmentFacade.getFullFileName(attachmentVO);// 完整實體名稱
        String mappingKey = genMappingKey(attachmentVO);
            
        StringBuilder url = new StringBuilder().append(GlobalConstant.URL_GET_IMAGE);
        //String fileExt = fullFileName.lastIndexOf(".")>=0? fullFileName.substring(fullFileName.lastIndexOf(".")+1):"";
        //url.append("?fileName=").append(URLEncoder.encode(fullFileName, GlobalConstant.ENCODING_DEF));
        url.append("?mappingKey=").append(URLEncoder.encode(mappingKey, GlobalConstant.ENCODING_DEF));
        url.append("&contentType=").append(URLEncoder.encode(contentType, GlobalConstant.ENCODING_DEF));
        //url.append("&fileExt=").append(URLEncoder.encode(fileExt, GlobalConstant.ENCODING_DEF));
        url.append("&showImgKey=").append(URLEncoder.encode(showImgKey, GlobalConstant.ENCODING_DEF));
        url.append("&domain=").append(domainName);
        url.append("&publicImg=N");
        logger.debug("preparePrivateUrl url = "+url.toString());
        
        return url.toString();
    }
    
    /**
     * 準備顯示圖片 Public URL 資訊 
     * @param domainName
     * @param attachmentVO
     * @return 
     * @throws java.io.UnsupportedEncodingException 
     */
    public String preparePublicUrl(String domainName, AttachmentVO attachmentVO) throws UnsupportedEncodingException{
        String contentType = attachmentVO.getContentType();
        String sysFilename = attachmentVO.getApplicationdata().getFvitem().getFilename();// 直接放 filename
        return preparePublicUrl(domainName, sysFilename, contentType);
    }
    public String preparePublicUrl(String domainName, String sysFilename, String contentType) throws UnsupportedEncodingException{
        if( domainName==null || sysFilename==null ){
            logger.error("preparePublicUrl error domainName==null || sysFilename==null ");
            return null;
        }

        String mappingKey = sysFilename;// 直接放 filename

        StringBuilder url = new StringBuilder().append(GlobalConstant.URL_GET_IMAGE);
        url.append("?mappingKey=").append(URLEncoder.encode(mappingKey, GlobalConstant.ENCODING_DEF));
        if( contentType!=null ){
            url.append("&contentType=").append(URLEncoder.encode(contentType, GlobalConstant.ENCODING_DEF));
        }
        url.append("&domain=").append(domainName);
        //url.append("&publicImg=Y");// 預設公開網址
        logger.debug("preparePublicUrl url = "+url.toString());
        
        return url.toString();
    }
        
    /**
     * 產生圖片 URL 
     * @param attachmentVO
     * @param domainName
     * @param isPublic
     * @param showImgKey
     * @param contextPath
     * @return 
     */
    public String genImageUrl(String domainName, AttachmentVO attachmentVO, boolean isPublic, String showImgKey, String contextPath){
        try{
            String url = (isPublic)?preparePublicUrl(domainName, attachmentVO):preparePrivateUrl(domainName, attachmentVO, showImgKey);
            url = (contextPath!=null)? contextPath+url:url;
            
            return url;
        }catch(Exception e){
            logger.error("genImageUrl Exception:\n", e);
        }
        return "";
    }

    /**
     * 外網 HTML 使用圖片網址 (尚不含 hostname)
     * @param domainName
     * @param contextPath
     * @param attachmentVO
     * @return 
     */
    public String genWebImageUrl(String domainName, String contextPath, AttachmentVO attachmentVO){
        return genImageUrl(domainName, attachmentVO, true, "", contextPath);
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
    
    //<editor-fold defaultstate="collapsed" desc="for 縮圖">
    /**
     * 縮圖 (並取得圖片資訊)
     * @param holderEntity
     * @param force 強制重新產生
     * @param retImgVO 若關聯多圖檔，只傳最後一個圖檔資訊
     * @return 
     */
    public String compressImageByHolder(ContentHolder holderEntity, boolean force, ImageVO retImgVO){
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
    }
    
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
     * @param attachmentVO 
     * @param force 強制重新產生
     * @param retImgVO 
     * @return  
     */
    public String compressImage(AttachmentVO attachmentVO, boolean force, ImageVO retImgVO){
        if( attachmentVO==null || attachmentVO.getApplicationdata()==null 
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
        logger.debug("compressImage fullFilename = "+fullFilename);
        
        StringBuilder retMsg = new StringBuilder();
        retMsg.append(userFilename).append("[").append(itemId).append("]");
        
        // 取得Image寬高
        String errMsg1 = getImageInfo(fullFilename, retImgVO);
        if( errMsg1!=null ){
            return retMsg.append(":").append(errMsg1).toString();
        }else{
            // 縮圖 (縮圖需寬高資訊)
            String errMsg2 = compressImageFile(userFilename, fvFilename, fullFilename, force, retImgVO);
            if( errMsg2!=null ){
                return retMsg.append(":").append(errMsg2).toString();
            }
        }
        return null;
    }
    
    /**
     * 縮圖 (縮圖需寬高資訊)
     * 儲存路徑資訊參考 JNDI
     * @param userFilename 原檔名(無路徑)
     * @param fvFilename 儲存檔名(無路徑)
     * @param fullFilename 完整檔名
     * @param force 強制重新產生
     * @param retImgVO Image寬高資訊
     * @return 
     */
    public String compressImageFile(String userFilename, String fvFilename, String fullFilename, boolean force, ImageVO retImgVO){
        String errMsg = null;

        try{
            // 大圖長、寬不大於 1600 X 1600
            String bigFilename = sysResourcesFacade.getBigImgaeDir() + File.separator + fvFilename;
            if( force || !FileUtils.isFileExist(bigFilename) ){
                double wRateB = retImgVO.getWidth()/GlobalConstant.WIDTH_BIG_IMG;
                double hRateB = retImgVO.getHeight()/GlobalConstant.HEIGHT_BIG_IMG;

                if( wRateB>1 || hRateB>1 ){// 大圖不大於 1600 X 1600
                    logger.debug("compressImage has bigFilename = "+bigFilename);
                    if( wRateB<hRateB ){
                        Thumbnails.of(new File(fullFilename)).height(GlobalConstant.HEIGHT_BIG_IMG).toFile(new File(bigFilename));
                    }else{
                        Thumbnails.of(new File(fullFilename)).width(GlobalConstant.WIDTH_BIG_IMG).toFile(new File(bigFilename));
                    }
                }
            }
        }catch(IOException e){
            errMsg = "大圖壓縮發生錯誤!";
            logger.error("compressImage IOException:\n", e);
            return errMsg;
        }

        try{
            // 小圖長、寬不小於 300 X 300
            String smallFilename = sysResourcesFacade.getSmallImgaeDir() + File.separator + fvFilename;
            if( force || !FileUtils.isFileExist(smallFilename) ){
                double wRateS = retImgVO.getWidth()/GlobalConstant.WIDTH_SMALL_IMG;
                double hRateS = retImgVO.getHeight()/GlobalConstant.HEIGHT_SMALL_IMG;

                if( wRateS>1 && wRateS>1 ){// 小圖不小於 300 X 300
                    logger.debug("compressImage has smallFilename = "+smallFilename);
                    if( wRateS<hRateS ){
                        Thumbnails.of(new File(fullFilename)).width(GlobalConstant.WIDTH_SMALL_IMG).toFile(new File(smallFilename));
                    }else{
                        Thumbnails.of(new File(fullFilename)).height(GlobalConstant.HEIGHT_SMALL_IMG).toFile(new File(smallFilename));
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

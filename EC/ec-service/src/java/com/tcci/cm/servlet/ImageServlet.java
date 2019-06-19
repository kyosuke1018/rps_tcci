/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.servlet;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.facade.global.ImageFacade;
import com.tcci.fc.util.FileUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 顯示圖片 Servlet
 *  /ImageServlet?publicImg=Y&storeId=1&contentType=image/jpg&mappingKey=6e20be55-6972-4a98-b35f-6c5a11887e12.jpg&imgType=O
 * @author Peter.pan
 */
@WebServlet(name = "ImageServlet", urlPatterns = {"/ImageServlet"})
public class ImageServlet extends HttpServlet {
    public final Logger logger = LoggerFactory.getLogger(ImageServlet.class);
    private final int CACHE_MAX_AGE_DEF = 60; // second
    
    @EJB ImageFacade imageFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = request.getParameter("contentType");// 檔案類型
        String publicImg = request.getParameter("publicImg");// 開放 Image URL
        String mappingKey = request.getParameter("mappingKey");// private:傳對應KEY，不傳實體名稱; public:fvitem檔名
        String showImgKey = request.getParameter("showImgKey");// 安全檢核碼
        String imgType = request.getParameter("imgType");// 小圖、大圖、原圖 for public only
        String imgSrc = request.getParameter("imgSrc");// FileEnum
        String storeId = request.getParameter("storeId");

        logParams(request);

        // 開放 Image URL
        if( publicImg==null || publicImg.equals("Y") ){// 預設公開網址
            outputImagePublic(response, storeId, mappingKey, contentType, imgType, imgSrc);
        }else{
            // 有 Session 安全檢核
            outputImagePrivate(request, response, showImgKey, mappingKey, contentType);
        }
    }
    
    private void logParams(HttpServletRequest request){
        if( request!=null && request.getParameterNames()!=null ){
            Enumeration<String> list = request.getParameterNames();
            int i = 0;
            while(list.hasMoreElements()){
                i++;
                String name = list.nextElement();
                logger.debug("ImageServlet logParams {} = {}", name, request.getParameter(name));
                if( i>20 ){
                    return;
                }
            }
        }
    }
    
    /**
     * 有 Session 安全檢核 Image URL
     * @param request
     * @param response 
     */
    private void outputImagePrivate(HttpServletRequest request, HttpServletResponse response
        , String showImgKey, String mappingKey, String contentType)
    {
        HttpSession session = request.getSession();
        if (session == null) {
            logger.debug("outputImagePrivate not support while session is null !");
            return;
        }
        if( session.getAttribute(showImgKey) != null 
           && ((Boolean)session.getAttribute(showImgKey)) 
           && session.getAttribute(mappingKey) != null ){
            logger.debug("outputImagePrivate session.getLastAccessedTime() = " + session.getLastAccessedTime());
            String fileName = (String)session.getAttribute(mappingKey);
            logger.debug("outputImagePrivate ... fileName = " + fileName);
            outputImage(fileName, contentType, response);
        }else{
            logger.error("outputImagePrivate showImgKey = "+showImgKey);
            logger.error("outputImagePrivate session.getAttribute(showImgKey) = "+session.getAttribute(showImgKey));
            logger.error("outputImagePrivate showImgKey = "+mappingKey);
            logger.error("outputImagePrivate session.getAttribute(mappingKey) = "+session.getAttribute(mappingKey));
        }
    }
    
    /**
     * 開放 Image URL
     * @param domain
     * @param fileName
     * @param fileExt
     * @param contentType 
     */
    private void outputImagePublic(HttpServletResponse response, String storeId, String fileName
            , String contentType, String imgType, String imgSrc){
        // 取得要顯示的圖 (小圖、大圖、原圖)
        String fullFilename = imageFacade.getFullFileNameSafe(imgSrc, imgType, Long.parseLong(storeId), fileName);
        logger.debug("outputImagePublic fullFilename = "+fullFilename);
        
        outputImage(fullFilename, contentType, response);
    }
    
    /**
     * 輸出圖片內容
     * @param domain
     * @param fileName
     * @param fileExt
     * @param contentType 
     */
    private void outputImage(String fileName, String contentType, HttpServletResponse response){
        InputStream in = null;
        OutputStream out = null;
                
        File f = new File(fileName);
        //BufferedImage bi = ImageIO.read(f);
        try {            
            if( f.isFile() && f.exists() && f.canRead() ){
                String fileExt = FileUtils.getExtFileName(f);
                fileExt = fileExt.startsWith(".")?fileExt.substring(1):fileExt;// 去掉分隔號，避免造成無法顯示
                logger.debug("outputImage fileExt = "+fileExt);
                
                in = Files.newInputStream(Paths.get(fileName));// use NIO
                BufferedImage bi = ImageIO.read(in);// use NIO
                out = response.getOutputStream();
                
                String displayFileName = URLEncoder.encode(f.getName(), GlobalConstant.ENCODING_DEF);
                response.setContentType(contentType);
                //response.addHeader("Cache-Control", "max-age="+Integer.toString(CACHE_MAX_AGE_DEF));
                response.addHeader("Content-Disposition", "inline; filename = \""+displayFileName+"\"");
                
                ImageIO.write(bi, fileExt, out);// 檔案輸出至 OutputStream
            }
        } catch (Exception e) {
            logger.error("outputImage exception:\n", e);
        } finally {
            if (in != null) {
                try{
                    in.close();
                }catch(Exception e){
                    logger.error("outputImage in.close() exception:\n", e);
                }
            }
            if (out != null) {
                try{
                    out.close();
                }catch(Exception e){
                    logger.error("outputImage out.close() exception:\n", e);
                }
            }
        }
    }
}

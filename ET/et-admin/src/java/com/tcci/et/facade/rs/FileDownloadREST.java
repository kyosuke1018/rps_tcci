/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.entity.KbPublication;
import com.tcci.et.enums.PublicationEnum;
import com.tcci.et.facade.KbPublicationFacade;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Peter.pan
 */
@Path("/download")
public class FileDownloadREST {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int CACHE_MAX_AGE_DEF = 60; // second
    
    @EJB KbPublicationFacade publicationFacade;
    @EJB AttachmentFacade attachmentFacade;
    
    /**
     * 自 FVault 下載檔案
     * /resources/download/fv/{cid}/{ctype}/{appid}/{fn}
     * @param cid : content holder id
     * @param ctype : content type (目前只有PublicationEnum.DOC.getCode())
     * @param appid : application id
     * @param fn : fvitem fileanme
     * @return 
     */
    @GET
    @Path("/fv/{cid}/{ctype}/{appid}/{fn}")
    public Response downloadFileFromFVault(
            @PathParam("cid")Long cid, 
            @PathParam("ctype")String ctype, 
            @PathParam("appid")Long appid, 
            @PathParam("fn")String fn){
        if( cid==null || ctype==null || appid==null || fn==null ){
            throw new WebApplicationException("Input parameters error !!");
        }
        if( !PublicationEnum.DOC.getCode().equals(ctype) ){
            throw new WebApplicationException("Not support type !!");
        }

        AttachmentVO attachmentVO = null;
        KbPublication publication = publicationFacade.find(cid);
        if( publication!=null ){
            List<AttachmentVO> attachments = attachmentFacade.loadContent(publication);
            for(AttachmentVO vo : attachments){
                if( vo.getApplicationdata().getId().equals(appid) ){// 比對 app Id
                    if( vo.getApplicationdata().getFvitem().getFilename().startsWith(fn) ){// 比對檔名
                        attachmentVO = vo;
                        break;
                    }
                }
            }

            if( attachmentVO!=null ){
                try{
                    String displayFileName = URLEncoder.encode(attachmentVO.getApplicationdata().getFvitem().getName(), GlobalConstant.ENCODING_DEF);
                    String fullFileName = attachmentFacade.getFullFileName(attachmentVO);
                    
                    return genResponseStream(fullFileName, attachmentVO.getContentType(), displayFileName);
                }catch(Exception e){
                    logger.error("downloadFileFromFVault Exception :\n", e);
                }
            }
        }
        
        throw new WebApplicationException("File Not Found !!");
    }
    
    /**
     * 產生回應資料流
     * @param fullFileName
     * @param displayFileName
     * @return 
     */
    private Response genResponseStream(final String fullFileName, String contentType, String displayFileName){
        StreamingOutput fileStream =  new StreamingOutput(){
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException 
            {
                try
                {
                    java.nio.file.Path path = Paths.get(fullFileName);
                    byte[] data = Files.readAllBytes(path);
                    output.write(data);
                    output.flush();
                }catch (Exception e) {
                    logger.error("genResponseStream exception fullFileName="+fullFileName+":\n", e);
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };
        
        contentType = (contentType!=null && !contentType.isEmpty())? contentType:MediaType.APPLICATION_OCTET_STREAM;
        CacheControl cc = new CacheControl();
        cc.setMaxAge(CACHE_MAX_AGE_DEF);
        cc.setPrivate(true);

        return Response
                .ok(fileStream, contentType)
                .header("Content-Disposition", "inline; filename = \""+displayFileName+"\"")
                //.header("content-disposition","attachment; filename = \""+displayFileName+"\"")
                //.cacheControl(cc).build();
                .build();
    }
}

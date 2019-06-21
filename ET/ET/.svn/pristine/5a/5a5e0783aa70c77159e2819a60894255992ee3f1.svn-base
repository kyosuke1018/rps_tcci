/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs.utils;

import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.enums.DataTypeEnum;
import com.tcci.et.facade.KbPublicationFacade;
import com.tcci.et.facade.rs.enums.ResStatusEnum;
import com.tcci.et.facade.rs.model.ResponseVO;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.facade.rs.model.PublicationListRsVO;
import com.tcci.et.facade.rs.model.PublicationRsVO;
import com.tcci.et.model.PublicationVO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST 專用資料轉換程式
 * @author Peter.pan
 */
@Stateless
public class RestDataFacade {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int CACHE_MAX_AGE_DEF = 60; // second
    
    @EJB SysResourcesFacade sysResourcesFacade;
    @EJB ImageFacade imageFacade;
    @EJB AttachmentFacade attachmentFacade;
    @EJB KbPublicationFacade publicationFacade;
    
    /**
     * Build OK Response width Cache
     * @param res
     * @param maxAge
     * @return 
     */
    public Response buildResponseOK(Object res, int maxAge){
        CacheControl cc = new CacheControl();
        cc.setMaxAge(maxAge);
        cc.setPrivate(true);
        
        //return Response.ok(res, MediaType.APPLICATION_JSON).cacheControl(cc).build();
        return Response.ok(res, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    public Response buildResponseOK(Object res){
        return buildResponseOK(res, CACHE_MAX_AGE_DEF);
    }
    
    /**
     * 回應資料筆數
     * @param totalRows
     * @return 
     */
    public ResponseVO genCountResponseVO(int totalRows){
        ResponseVO res = new ResponseVO();
        res.setStatus(ResStatusEnum.SUCCESS.getCode());
        res.setTotalRows(totalRows);
        return res;
    }
    
    /**
     * 產生語系條件:
     * ex. RESTful 的 shortCode 輸入'E'(英文版)，應可顯示[跨語系]及[英文版]的資料，故後端實際查詢條件為 'AE'
     * @param shortCode
     * @return 
     */
    public String genLangCriteria(String shortCode){
        LanguageEnum langEnum = LanguageEnum.getFromShortCode(shortCode);
        if( langEnum!=null ){
            return LanguageEnum.ALL.getShortCode()+shortCode;
        }
        return null;
    }

    //<editor-fold defaultstate="collapsed" desc="for Publication">
    /**
     * 轉換至輸出格式
     * @param vo
     * @param lang
     * @param widthRes
     * @return 
     */
    public PublicationRsVO toPublicationRsVO(PublicationVO vo, LanguageEnum lang, boolean widthRes){
        PublicationRsVO rsVO = new PublicationRsVO();
        if( vo==null || vo.getId()==null ){
            return rsVO;
        }
        ExtBeanUtils.copyProperties(rsVO, vo);
            
        // BEGIN : 特殊欄位處理
        if( vo.getDataTypeEnum()==DataTypeEnum.UPLOAD ){
            vo.setDocs(publicationFacade.findFiles(vo));
            
            if( vo.getDocs()!=null && !vo.getDocs().isEmpty() ){
                List<String> urls = new ArrayList<String>();
                for(AttachmentVO attachmentVO : vo.getDocs()){
                    String url = sysResourcesFacade.genDocUrl(attachmentVO, true, false);
                    url = sysResourcesFacade.genWebFileUrl(url);// 外網可存取完整網址
                    logger.debug("toPublicationRsVO url = \n"+url);
                    
                    urls.add(url);
                }
                if( urls.size()>0 ){
                    if( urls.size()>1 ){
                        rsVO.setUrls(urls);// 多筆
                    }else{
                        rsVO.setUrl(urls.get(0));// 單筆
                    }
                }
            }
        }
        
        // 封面圖示
        if( vo.getTypeEnum().getHasCoverImg() && vo.getCoverUrl()!=null ){
            rsVO.setCoverUrl(sysResourcesFacade.getServiceUrlPrefix()+rsVO.getCoverUrl());
        }
        // END : 特殊欄位處理
        
        if( widthRes ){
            ResponseVO resVO = new ResponseVO();
            resVO.setStatus(ResStatusEnum.SUCCESS.getCode());
            resVO.setTotalRows(1);
            rsVO.setRes(resVO);
        }
        
        return rsVO;
    }
    
    /**
     * 轉換至輸出列表格式
     * @param list
     * @param lang
     * @return 
     */
    public PublicationListRsVO toPublicationListRsVO(List<PublicationVO> list, LanguageEnum lang){
        logger.debug("toPublicationListRsVO list="+(list!=null?list.size():0)+", lang="+lang);
        PublicationListRsVO res = new PublicationListRsVO();
        if( list!=null ){
            List<PublicationRsVO> resList = new ArrayList<PublicationRsVO>();
            for(PublicationVO vo : list){
                PublicationRsVO rsVO = toPublicationRsVO(vo, lang, false);
                resList.add(rsVO);
            }
            res.setTotalCount(resList.size());
            res.setList(resList);
        }else{
            res.setTotalCount(0);
        }
        
        ResponseVO resVO = new ResponseVO();
        resVO.setStatus(ResStatusEnum.SUCCESS.getCode());
        res.setRes(resVO);

        return res;
    }
    //</editor-fold>

}

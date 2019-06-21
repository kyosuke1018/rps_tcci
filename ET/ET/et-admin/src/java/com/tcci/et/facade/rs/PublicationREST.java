/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.et.facade.rs;

import com.tcci.et.enums.ContentStatusEnum;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.PublicationEnum;
import com.tcci.et.facade.KbPhotoGalleryFacade;
import com.tcci.et.facade.KbPublicationFacade;
import com.tcci.et.facade.KbVideoFacade;
import com.tcci.et.facade.rs.model.PublicationListRsVO;
import com.tcci.et.facade.rs.model.PublicationRsVO;
import com.tcci.et.facade.rs.model.ResponseVO;
import com.tcci.et.model.PublicationVO;
import com.tcci.et.model.criteria.PublicationCriteriaVO;
import com.tcci.cm.model.global.GlobalConstant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Peter.pan
 */
@Path("/pubs")
public class PublicationREST extends AbstractWebREST {
    @EJB KbPublicationFacade publicationFacade;
    @EJB KbPhotoGalleryFacade photoGalleryFacade;
    @EJB KbVideoFacade videoFacade;
    
    private final Map<String, String> sortFieldMap;
    private final Map<String, String> sortOrderMap;
    
    public PublicationREST(){
        logger.debug("PublicationREST init ...");
        // for 支援排序
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("dataDateStr", "S.DATADATE");// 編號
        sortFieldMap.put("title",  "S.TITLE");// 屬名
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("1", "");
        sortOrderMap.put("-1", "DESC");
    }
    
    /**
     * 單一筆資訊
     * http://localhost:8080/csrcweb/resources/pubs/{1}?lang=C
     * @param id
     * @param lang
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id")Long id, 
            @QueryParam("lang")String lang){
        logger.debug("findById id = "+id);
        try{
            PublicationVO vo = publicationFacade.findById(id, true);
            PublicationRsVO res = restDataFacade.toPublicationRsVO(vo, null, true);
            
            return restDataFacade.buildResponseOK(res);
        }catch(Exception e){
            processUnknowException(null, "findByCriteria", e);
            return Response.serverError().build();
        }
    }
    
    /**
     * 特殊代碼查詢 (for 固定網頁文章，及暫存文章預覽)
     * @param pubType
     * @param code
     * @return
     */
    @GET
    @Path("{pubType}/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByCode(@PathParam("pubType")String pubType, @PathParam("code")String code){
        logger.debug("findByCode pubType = "+pubType+", code = "+code);
        try{
            PublicationVO vo = null;
            if( PublicationEnum.FIXEDPAGE.getCode().equals(pubType) ){
                vo = publicationFacade.fundHtmlPageByCode(pubType, code, ContentStatusEnum.PUBLISH.getCode());
            }else if( PublicationEnum.TEMP.getCode().equals(pubType) ){
                vo = publicationFacade.fundHtmlPageByCode(pubType, code, null);
                publicationFacade.deleteDoc(vo, null, false);// 預覽後刪除暫存資料
            }
            
            if( vo!=null ){
                PublicationRsVO res = restDataFacade.toPublicationRsVO(vo, null, true);
                return restDataFacade.buildResponseOK(res);
            }else{
                logger.error("findByCode error pubType="+pubType+", code="+code);
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
        }catch(Exception e){
            processUnknowException(null, "findByCode", e);
            return Response.serverError().build();
        }
    }
    
    /**
     *  依文章類別查詢
     * http://localhost:8080/csrcweb/resources/pubs/pubsInType/N
     * @param pubType
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @param lang
     * @return
     */
    @GET
    @Path("/pubsInType/{pubType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByType(@PathParam("pubType")String pubType,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder,
            @QueryParam("lang")String lang
    ){
        logger.debug("findByType pubType = "+pubType+", offset = "+offset+", rows = "+rows
                +", sortField = "+sortField+", sortOrder = "+sortOrder+", lang = "+lang);
        try{
            String langCode = restDataFacade.genLangCriteria(lang);// 產生語系條件
            if( langCode==null ){// 語系條件必要
                logger.error("findByType langCode==null !");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            // 最新消息
            //if( PublicationEnum.NEWS.getCode().equals(pubType) ){
            //    return findByNews(offset, rows, sortField, sortOrder, lang);
            //}
            
            // check type
            if( !PublicationEnum.validDocType(pubType) ){
                logger.error("findByType pubType not valid !");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
            criteriaVO.setType(pubType);
            criteriaVO.setDocOnly(Boolean.TRUE);
            criteriaVO.setStatus(ContentStatusEnum.PUBLISH.getCode());
            // ex. RESTful 的 shortCode 輸入'E'(英文版)，應可顯示[跨語系]及[英文版]的資料，故後端實際查詢條件為 'AE'
            criteriaVO.setLang(langCode);
            criteriaVO.setFirstResult(offset);
            // 限制回傳筆數
            criteriaVO.setMaxResults((rows!=null && rows<GlobalConstant.RS_RESULT_MAX_ROWS)? rows:GlobalConstant.RS_RESULT_MAX_ROWS);
            if( sortFieldMap.get(sortField)!=null ){
                criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            }else{
                criteriaVO.setOrderBy("S.DATADATE", "DESC");// 預設資料日期反序排序
            }
            List<PublicationVO> list = publicationFacade.findByCriteria(criteriaVO);// 子分類
            PublicationListRsVO res = restDataFacade.toPublicationListRsVO(list, LanguageEnum.getFromShortCode(lang));
            
            return restDataFacade.buildResponseOK(res);
        }catch(Exception e){
            processUnknowException(null, "findByType", e);
            return Response.serverError().build();
        }
    }
    /**
     * 依文章類別統計筆數
     * http://localhost:8080/csrcweb/resources/pubs/countInType/R
     * @param pubType
     * @param lang
     * @return
     */
    @GET
    @Path("/countInType/{pubType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countByType(@PathParam("pubType")String pubType, @QueryParam("lang")String lang){
        logger.debug("countInType pubType = "+pubType);
        try{
            String langCode = restDataFacade.genLangCriteria(lang);// 產生語系條件
            if( langCode==null ){// 語系條件必要
                logger.error("countInType langCode==null !");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            //if( PublicationEnum.NEWS.getCode().equals(pubType) ){// 最新消息
            //    return countByNews(lang);
            //}
            
            // check type
            if( !PublicationEnum.validDocType(pubType) ){
                logger.error("countInType pubType not valid !");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
            criteriaVO.setType(pubType);
            criteriaVO.setDocOnly(Boolean.TRUE);
            criteriaVO.setStatus(ContentStatusEnum.PUBLISH.getCode());
            // ex. RESTful 的 shortCode 輸入'E'(英文版)，應可顯示[跨語系]及[英文版]的資料，故後端實際查詢條件為 'AE'
            criteriaVO.setLang(langCode);
            int totalRows = publicationFacade.countByCriteria(criteriaVO);// 子分類
            // 限制回傳筆數
            totalRows = ((totalRows<GlobalConstant.RS_RESULT_MAX_ROWS)? totalRows:GlobalConstant.RS_RESULT_MAX_ROWS);
            ResponseVO res = restDataFacade.genCountResponseVO(totalRows);
            
            return restDataFacade.buildResponseOK(res);
        }catch(Exception e){
            processUnknowException(null, "countInType", e);
            return Response.serverError().build();
        }
    }
    
    /**
     * 最新消息 (首頁使用)
     * http://localhost:8080/csrcweb/resources/pubs/news
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @param lang
     * @return
     */
    @GET
    @Path("/news")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByNews(@QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder,
            @QueryParam("lang")String lang
    ){
        logger.debug("findByType offset = "+offset+", rows = "+rows
                +", sortField = "+sortField+", sortOrder = "+sortOrder+", lang = "+lang);
        
        try{
            String langCode = restDataFacade.genLangCriteria(lang);// 產生語系條件
            if( langCode==null ){// 語系條件必要
                logger.error("findByNews langCode==null !");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            offset = (offset==null)?0:offset;
            rows = (rows==null)?1:rows;
            
            PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
            criteriaVO.setNews(Boolean.TRUE);
            criteriaVO.setDocOnly(Boolean.TRUE);
            criteriaVO.setStatus(ContentStatusEnum.PUBLISH.getCode());
            // ex. RESTful 的 shortCode 輸入'E'(英文版)，應可顯示[跨語系]及[英文版]的資料，故後端實際查詢條件為 'AE'
            criteriaVO.setLang(langCode);
            criteriaVO.setFirstResult(offset);
            // 限制回傳筆數
            criteriaVO.setMaxResults((rows<GlobalConstant.RS_RESULT_MAX_ROWS)? rows:GlobalConstant.RS_RESULT_MAX_ROWS);
            if( sortFieldMap.get(sortField)!=null ){
                criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            }else{
                criteriaVO.setOrderBy("S.DATADATE", "DESC");// 預設資料日期反序排序
            }
            List<PublicationVO> list = publicationFacade.findByCriteria(criteriaVO);// 子分類
            PublicationListRsVO res = restDataFacade.toPublicationListRsVO(list, LanguageEnum.getFromShortCode(lang));
            
            return restDataFacade.buildResponseOK(res);
        }catch(Exception e){
            processUnknowException(null, "findByNews", e);
            return Response.serverError().build();
        }
    }
    private Response countByNews(String lang){// 最新消息總筆數
        logger.debug("countByNews ...");
        try{
            String langCode = restDataFacade.genLangCriteria(lang);// 產生語系條件
            if( langCode==null ){// 語系條件必要
                logger.error("findByNews langCode==null !");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
            criteriaVO.setNews(Boolean.TRUE);
            criteriaVO.setDocOnly(Boolean.TRUE);
            criteriaVO.setStatus(ContentStatusEnum.PUBLISH.getCode());
            // ex. RESTful 的 shortCode 輸入'E'(英文版)，應可顯示[跨語系]及[英文版]的資料，故後端實際查詢條件為 'AE'
            criteriaVO.setLang(langCode);
            
            int totalRows = publicationFacade.countByCriteria(criteriaVO);// 子分類
            // 限制回傳筆數
            totalRows = ((totalRows<GlobalConstant.RS_RESULT_MAX_ROWS)? totalRows:GlobalConstant.RS_RESULT_MAX_ROWS);
            ResponseVO res = restDataFacade.genCountResponseVO(totalRows);
            
            return restDataFacade.buildResponseOK(res);
        }catch(Exception e){
            processUnknowException(null, "countByNews", e);
            return Response.serverError().build();
        }
    }

}

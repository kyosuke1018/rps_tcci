/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcUseLog;
import com.tcci.ec.enums.LogCategoryEnum;
import com.tcci.ec.enums.LogTypeEnum;
import com.tcci.ec.facade.DeliveryFacade;
import com.tcci.ec.facade.EcAdFacade;
import com.tcci.ec.facade.EcBulletinFacade;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.facade.EcUseLogFacade;
import com.tcci.ec.facade.TccArticleFacade;
import com.tcci.ec.model.AdVO;
import com.tcci.ec.model.BulletinVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import com.tcci.ec.vo.TccArticle;
import com.tcci.ec.vo.UseLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("sys")
public class SystemService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(SystemService.class);
    @EJB EcBulletinFacade bulletinFacade;
    @EJB EcAdFacade adFacade;
    @EJB EcFileFacade fileFacade;
    @EJB EcUseLogFacade useLogFacade;
    @EJB private TccArticleFacade tccArticleFacade;
    @EJB private DeliveryFacade deliveryFacade;
    
    protected Map<String, String> sortFieldMap;
    protected Map<String, String> sortOrderMap;
    
    public SystemService(){
        logger.debug("SystemService init ...");
        // for 支援排序
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<>();
        sortFieldMap.put("id", "S.ID");// ID
        sortFieldMap.put("starttime", "S.STARTTIME");
        sortFieldMap.put("endtime", "S.ENDTIME");
        sortFieldMap.put("createtime", "S.CREATETIME");
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<>();
        sortOrderMap.put("-1", "");
        sortOrderMap.put("1", "DESC");
    }
    
    @GET
    @Path("/ad/now")
    @Produces("application/json; charset=UTF-8;")
    public List<AdVO> findAdsNow(@Context HttpServletRequest request, 
            @QueryParam(value = "type") String type){
        logger.debug("findAdsNow type:"+type);
        try{
            if (StringUtils.isBlank(type)) {
                return null;
            }
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            criteriaVO.setType(type);
//            criteriaVO.setActive(active==null?true:active);// 預設傳目前生效項目
            criteriaVO.setActive(true);// 預設傳目前生效項目
            criteriaVO.setFullData(true);
            criteriaVO.setOrderBy("S.SORTNUM, S.STARTTIME DESC");
            List<AdVO> list = adFacade.findByCriteria(criteriaVO);
            return list;
        }catch(Exception e){
            logger.error("findAdsNow Exception:\n", e);
        }
        return null;
    }
    
    @GET
    @Path("/bulletin/list")
    @Produces("application/json; charset=UTF-8;")
    public List<BulletinVO> findBulletins(@Context HttpServletRequest request, 
//            SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder){
        logger.info("findBulletins offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
//        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);
        try{
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            criteriaVO.setFirstResult(offset);
//            criteriaVO.setMaxResults(rows);//不限制筆數
            criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<BulletinVO> list = bulletinFacade.findByCriteria(criteriaVO);
            return list;
        }catch(Exception e){
            logger.error("findAdsNow Exception:\n", e);
        }
        return null;
    }
    
    @POST
    @Path("/saveUseLog")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public String saveUseLog(@Context HttpServletRequest request, UseLog log){
        logger.info("saveUseLog category = "+log.getCategory());//LOGIN_APP, ORDER_CREATE,ORDER_PAY
        EcMember member = getAuthMember();
        try{
            if(member==null){
                return "FAIL";
            }
            EcUseLog entity = new EcUseLog();
            entity.setSrc("APP");
            if(LogTypeEnum.LOGIN_APP.name().equals(log.getCategory())){
                entity.setType(LogTypeEnum.LOGIN_APP.getCode());
//                entity.setCategory(category);
            }else if(LogCategoryEnum.ORDER_CREATE.getCode().equals(log.getCategory()) 
                    || LogCategoryEnum.ORDER_PAY.getCode().equals(log.getCategory())
                    || LogCategoryEnum.ORDER_SHIP.getCode().equals(log.getCategory())
                    ){
                entity.setType(LogTypeEnum.FUNC_APP.getCode());
                entity.setCategory(log.getCategory());
            }else{
                return "FAIL";
            }
            entity.setClientInfo(log.getClientInfo());
            entity.setPatrolLatitude(log.getPatrolLatitude());
            entity.setPatrolLongitude(log.getPatrolLongitude());
            useLogFacade.save(entity, member);
        }catch(Exception e){
            e.printStackTrace();
            logger.error("saveUseLog Exception:\n", e);
            return "FAIL";
        }
        return "SUCCESS";
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="EC 1.5">
    //EC1.0 台泥動態
    @GET
    @Path("1.5/tccArticle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public List<TccArticle> tccArticle(@Context HttpServletRequest request) {
        try{
            List<TccArticle> list = new ArrayList<>();
            List<TccArticle> articles = tccArticleFacade.findByCriteria();
            if (CollectionUtils.isNotEmpty(articles)) {
                for (TccArticle article : articles) {
                    if (article.getLink() == null) {
                        article.setLink("/tccstore/service/article/view/" + article.getId()); // 手機自行補 host
                    }
                    list.add(article);
                }
                return list;
            }
        }catch(Exception e){
            logger.error("tccArticle Exception:\n", e);
        }
        return null;
    }
    
    

    //</editor-fold>
    
}

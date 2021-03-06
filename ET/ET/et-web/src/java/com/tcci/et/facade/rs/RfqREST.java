/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtMember;
import com.tcci.et.facade.rfq.EtRfqVenderFacade;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.et.model.rfq.TenderingVO;
import com.tcci.et.model.rs.BaseResponseVO;
import com.tcci.et.model.rs.SubmitVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javax.ws.rs.core.Response;

/**
 * 詢價/報價
 * 
 * @author Peter.pan
 */
@Path("/rfq")
public class RfqREST extends AbstractWebREST {
    private boolean DEBUG_MODE = true;
    
    private @EJB EtRfqVenderFacade rfqVenderFacade;

    /**
     * 已投標標案 查詢 - 先抓總筆數
     * http://localhost:8080/et-web/services/rfq/tendered/count
     * 
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/tendered/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countTendereds(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countTendereds ...");
        List<String> errors = new ArrayList<String>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊

            RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
            //criteriaVO.setMemberId(memberId);
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setFullData(false);
            // for DEMO
            if( DEBUG_MODE ){
                criteriaVO.setMemberId(2L);
            }           
            int totalRows = rfqVenderFacade.countByCriteria(criteriaVO);
            logger.info("countTendereds totalRows = "+totalRows);
            BaseResponseVO resVO = genCountSuccessRepsone(request, totalRows);
            
            return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * 已投標標案 查詢
     * http://localhost:8080/et-web/services/rfq/tendered/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @POST
    @Path("/tendered/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findTendereds(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findTendereds offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);

        List<String> errors = new ArrayList<String>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊

            RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
            //criteriaVO.setMemberId(memberId);
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setFullData(false);
            // for DEMO
            if( DEBUG_MODE ){
                criteriaVO.setMemberId(2L);
            }
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            
            //criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<TenderingVO> list = rfqVenderFacade.findTendered(criteriaVO);// 已投標標案
            logger.info("findTendereds list = "+sys.size(list));
            
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

}

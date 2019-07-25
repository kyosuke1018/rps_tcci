/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.entity.EtMember;
import com.tcci.et.enums.rs.ResStatusEnum;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.TenderConformVO;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.MemberVenderVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.rs.AttachmentRsVO;
import com.tcci.et.model.rs.HomeTenderVO;
import com.tcci.et.model.rs.LongOptionVO;
import com.tcci.et.model.rs.SubmitVO;
import com.tcci.et.model.rs.TenderRsVO;
import com.tcci.et.entity.EtTender;
import com.tcci.et.entity.EtTenderConform;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.et.facade.EtTenderConformFacade;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Kyle.Cheng
 */
@Path("/tender")
public class TenderREST extends AbstractWebREST {
    
    @EJB private EtTenderConformFacade tenderConformFacade;
    @EJB AttachmentFacade attachmentFacade;
    
    private final int CACHE_MAX_AGE_DEF = 60; // second
    
    /**
     * /services/tender/home
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/home")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response homeList(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("find homeList ...");
        EtMember member = null;
        List<HomeTenderVO> result = new ArrayList<>();
        try {
            if(formVO==null){
                TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
                List<TenderVO> list;
                List<TenderRsVO> rsList;
                HomeTenderVO homeVO;
                //1.招標中
//                criteriaVO.setClosed(Boolean.FALSE);
                criteriaVO.setActive(Boolean.TRUE);
                criteriaVO.setNotDraft(Boolean.TRUE);
//                criteriaVO.setStatus(TenderStatusEnum.ON_TENDER.getCode());
                List<String> statusList = new ArrayList<>();
                statusList.add(TenderStatusEnum.ON_SALE.getCode());
                statusList.add(TenderStatusEnum.ON_TENDER.getCode());
                statusList.add(TenderStatusEnum.VERIFY.getCode());
                criteriaVO.setStatusList(statusList);
                
                criteriaVO.setOrderBy("S.DATADATE", "DESC");
                criteriaVO.setMaxResults(5);
                list = etTenderFacade.findByCriteria(criteriaVO);
                rsList = new ArrayList<>();
                homeVO = new HomeTenderVO();
                homeVO.setType("STATUS");
                homeVO.setValue("T");
                homeVO.setName("投標中");
                if (CollectionUtils.isNotEmpty(list)) {
                    for(TenderVO vo : list){
                        TenderRsVO rsVO = restDataFacade.toTenderRsVO(vo, null, false);
                        rsList.add(rsVO);
                    }
                    homeVO.setList(rsList);
                }
                result.add(homeVO);
                
                
                //2.已決標
                criteriaVO = new TenderCriteriaVO();
                criteriaVO.setClosed(Boolean.TRUE);////排除2個月前決標案件
                criteriaVO.setStatus(TenderStatusEnum.END.getCode());
                criteriaVO.setOrderBy("S.DATADATE", "DESC");
                criteriaVO.setMaxResults(5);
                list = etTenderFacade.findByCriteria(criteriaVO);
                rsList = new ArrayList<>();
                homeVO = new HomeTenderVO();
                homeVO.setType("STATUS");
                homeVO.setValue("E");
                homeVO.setName("已決標");
                if (CollectionUtils.isNotEmpty(list)) {
                    for(TenderVO vo : list){
                        TenderRsVO rsVO = restDataFacade.toTenderRsVO(vo, null, false);
                        rsList.add(rsVO);
                    }
                    homeVO.setList(rsList);
                }
                result.add(homeVO);
                
                
                //3.地區
                List<LongOptionVO> areaOption = etOptionFacade.findByTypeOptions("area", "C");
                for(LongOptionVO area:areaOption){
                    Long areaId = area.getValue();
                    criteriaVO = new TenderCriteriaVO();
                    criteriaVO.setAreaId(areaId);
                    criteriaVO.setClosed(Boolean.TRUE);////排除2個月前決標案件
                    criteriaVO.setActive(Boolean.TRUE);
                    criteriaVO.setNotDraft(Boolean.TRUE);
//                    criteriaVO.setStatus(TenderStatusEnum.ON_TENDER.getCode());
                    criteriaVO.setOrderBy("S.DATADATE", "DESC");
                    criteriaVO.setMaxResults(5);
                    list = etTenderFacade.findByCriteria(criteriaVO);
                    rsList = new ArrayList<>();
                    homeVO = new HomeTenderVO();
                    homeVO.setType("AREA");
                    homeVO.setValue(areaId.toString());
                    homeVO.setName(area.getLabel());
                    if (CollectionUtils.isNotEmpty(list)) {
                        for(TenderVO vo : list){
                            TenderRsVO rsVO = restDataFacade.toTenderRsVO(vo, null, false);
                            rsList.add(rsVO);
                        }
                        homeVO.setList(rsList);
                    }
                    result.add(homeVO);
                }
                
                return this.genSuccessRepsoneWithList(request, result);// 
            }
        } catch (Exception e) {
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/tender/count
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response count(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("find list ...");
        EtMember member = null;
        List<TenderRsVO> result = new ArrayList<>();
        try {
            if(formVO!=null){
                logger.info("find list Type:"+formVO.getType());
                TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
                if("STATUS".equals(formVO.getType())){
                    String statusCode = formVO.getStatus();
                    if(TenderStatusEnum.END.getCode().equals(statusCode)){
                        criteriaVO.setClosed(Boolean.TRUE);
                    }else{
                        criteriaVO.setStatus(statusCode);
                    }
                }else if("AREA".equals(formVO.getType())){
                    Long areaId = formVO.getAreaId();
                    criteriaVO.setAreaId(areaId);
                    criteriaVO.setClosed(Boolean.TRUE);////排除2個月前決標案件
                }
                
                int totalRows = etTenderFacade.countByCriteria(criteriaVO);
                return this.genSuccessRepsoneWithCount(request, totalRows);
            }else{
                TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
                List<String> statusList = new ArrayList<>();
                statusList.add(TenderStatusEnum.ON_SALE.getCode());
                statusList.add(TenderStatusEnum.ON_TENDER.getCode());
                statusList.add(TenderStatusEnum.VERIFY.getCode());
                statusList.add(TenderStatusEnum.END.getCode());
                criteriaVO.setStatusList(statusList);
                
                //排除2個月前決標案件
                criteriaVO.setClosed(Boolean.TRUE);////排除2個月前決標案件
                
                int totalRows = etTenderFacade.countByCriteria(criteriaVO);
                logger.debug("count tender total totalRows = "+totalRows);
                return this.genSuccessRepsoneWithCount(request, totalRows);
            }
        } catch (Exception e) {
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/tender/list
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("find list ...");
        EtMember member = null;
        List<TenderRsVO> result = new ArrayList<>();
        try {
            if(formVO!=null){
                
                logger.info("find list Type:"+formVO.getType());
                TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
                criteriaVO.setOrderBy("S.DATADATE", "DESC");
                if("STATUS".equals(formVO.getType())){
                    String statusCode = formVO.getStatus();
                    logger.info("find list statusCode:"+statusCode);
                    if(TenderStatusEnum.END.getCode().equals(statusCode)){
                        criteriaVO.setClosed(Boolean.TRUE);
                    }
                    criteriaVO.setStatus(statusCode);
                }else if("AREA".equals(formVO.getType())){
                    Long areaId = formVO.getAreaId();
                    logger.info("find list areaId:"+areaId);
                    criteriaVO.setAreaId(areaId);
                    criteriaVO.setClosed(Boolean.TRUE);////排除2個月前決標案件
                }
                
                List<TenderVO> list = etTenderFacade.findByCriteria(criteriaVO);
                if (CollectionUtils.isNotEmpty(list)) {
                    for(TenderVO vo : list){
                        TenderRsVO rsVO = restDataFacade.toTenderRsVO(vo, null, false);
                        result.add(rsVO);
                    }
                }
                return this.genSuccessRepsoneWithList(request, result);// 
            }
        } catch (Exception e) {
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/members/full/{id}
     * @param request
     * @param id
     * @return 
     */
    @GET
    @Path("/full/{id}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
//    @JWTTokenNeeded
    public Response findTenderFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findTenderFullInfo ...");
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
//            Long memberId = admin?id:member.getId();// 賣家只看自己資料
            logger.info("findTenderFullInfo id = "+id);
            TenderVO vo = etTenderFacade.findById(id, false);
            //檢查供應商類別權限
//            if( !checkVenderPermissions(methodName, member, vo, admin) ){
//                return genUnauthorizedResponse();
//            }

            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
//            e.printStackTrace();
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    @POST
    @Path("/findConformLog")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findConformLog(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findConformLog ...");
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            if(formVO==null || formVO.getTenderId()==null){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, null);
            }
            
            member = getReqUser(request);
            Locale locale = getLocale(request);
            if(member==null){
                member = memberFacade.find(formVO.getMemberId());
//                return this.genFailRepsone(request);
            }
            Long memberId = member.getId();
            Long tenderId = formVO.getTenderId();
            
            logger.info("findConformLog memberId = "+memberId);
            logger.info("findConformLog tenderId = "+tenderId);
            //find by member & tender
            BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
            criteriaVO.setMemberId(memberId);
            criteriaVO.setTenderId(tenderId);
            criteriaVO.setOrderBy("S.CREATETIME", "DESC");
            List<TenderConformVO> result = tenderConformFacade.findByCriteria(criteriaVO);
            if(CollectionUtils.isNotEmpty(result)){
                return this.genSuccessRepsone(request, result.get(0));
            }

            return this.genSuccessRepsone(request);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    @POST
    @Path("/conform")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response conform(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("conform ...");
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            if(formVO==null || formVO.getTenderId()==null || formVO.getVenderId()==null){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, null);
            }
            
            member = getReqUser(request);
            Locale locale = getLocale(request);
            if(member==null){
                member = memberFacade.find(formVO.getMemberId());
//                return this.genFailRepsone(request);
            }
            Long memberId = member.getId();
            Long tenderId = formVO.getTenderId();
            Long venderId = formVO.getVenderId();
            BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
            criteriaVO.setId(venderId);
            List<MemberVenderVO> list = etMemberVenderFacade.findByCriteria(criteriaVO);
            MemberVenderVO venderVO = null;
            if(CollectionUtils.isNotEmpty(list)){
                venderVO = list.get(0);
            }
            
            if(venderVO==null){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            
            // 賣家、管理員共用 RESTful
//            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
//                return genUnauthorizedResponse();
//            }
            logger.info("conform memberId = "+memberId);
            EtTenderConform entity = new EtTenderConform();
            entity.setMemberId(memberId);
            entity.setTenderId(tenderId);
            entity.setMandt(venderVO.getMandt());
            entity.setVenderCode(venderVO.getVenderCode());
            entity.setVenderName(venderVO.getCname());
            
            TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
            tenderConformFacade.save(entity, admin, false);
            
            return findConformLog(request, formVO);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    @GET
    @Path("/file/{tid}/{appid}")
    public Response downloadFile(@Context HttpServletRequest request,
            @Context HttpServletResponse response, 
            @PathParam("tid")Long tid, 
            @PathParam("appid")Long appid) throws IOException{
        EtMember member = getReqUser(request);
        if(member!=null){
            return this.downloadFileFromFVault(request, response, tid, appid, member.getId());
        }else{
//            try {
//                FacesContext context = FacesContext.getCurrentInstance();
//                String url = context.getExternalContext().getRequestContextPath();
//                logger.debug("login Redirect url:{}", url);
//                JsfUtils.redirect(url);
//            } catch (Exception e) {
//                logger.debug("login exception :\n", e);
//            }
            return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
        }
//        return this.downloadFileFromFVault(request, tid, appid, null);
    }
    
    @GET
    @Path("/file/{tid}/{appid}/{mid}")
    public Response downloadFileFromFVault(@Context HttpServletRequest request,
            @Context HttpServletResponse response, 
            @PathParam("tid")Long tid, 
            @PathParam("appid")Long appid,
            @PathParam("mid")Long mid ) throws IOException{
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("downloadFileFromFVault ...");
        if( tid==null || mid==null || appid==null){
            return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, null);
        }
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
//            if(true){
//                response.sendRedirect("../../../../index.xhtml");//導頁到某個url
//            }
            
            member = getReqUser(request);
            Locale locale = getLocale(request);
            if(member==null){
                member = memberFacade.find(mid);
//                return this.genFailRepsone(request);
            }
            Long memberId = member.getId();
            Long tenderId = tid;
            EtTender etTender = etTenderFacade.find(tenderId);
            List<MemberVO> list = memberFacade.checkMappingMemberAndCategoryId(etTender.getCategoryId(), memberId);
            if (CollectionUtils.isEmpty(list)) {
                return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
            }
            AttachmentVO attachmentVO = null;
            List<AttachmentVO> attachments = etTenderFacade.findFiles(etTender);
            for(AttachmentVO vo : attachments){
                if( vo.getApplicationdata().getId().equals(appid) ){// 比對 app Id
                    attachmentVO = vo;
                    break;
                }
            }
            if( attachmentVO!=null ){
                String displayFileName = URLEncoder.encode(attachmentVO.getApplicationdata().getFvitem().getName(), GlobalConstant.ENCODING_DEF);
                String fullFileName = attachmentFacade.getFullFileName(attachmentVO);
                return genResponseStream(fullFileName, attachmentVO.getContentType(), displayFileName);
                
                /*
                AttachmentRsVO result = new AttachmentRsVO();
                ExtBeanUtils.copyProperties(result, attachmentVO);
                result.setAppId(attachmentVO.getApplicationdata().getId());
                try{
                    byte[] bytArray = attachmentVO.getContent();
                    if (bytArray != null) {
                        logger.info("getContentStream bytArray = "+(bytArray!=null?bytArray.length:null));
                        result.setContent(attachmentVO.getContent());
                    }else{
                        logger.info("getContentStream bytArray is null ");
                        InputStream is = attachmentFacade.getContentStream(attachmentVO);
                        bytArray = IOUtils.toByteArray(is);
                        result.setContent(bytArray);
                    }
                }catch(Exception e){
                    logger.error("getContentStream Exception: ", e);
                }
                
                return this.genSuccessRepsone(request, result);
//                return genResponseStream(fullFileName, attachmentVO.getContentType(), displayFileName);
                 */

            }
            
            return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
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
    
    @POST
    @Path("/getFile")
    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenNeeded
    public Response getFile(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("getFile ...");
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            if(formVO==null || formVO.getTenderId()==null || formVO.getAppId()==null){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, null);
            }
            
            member = getReqUser(request);
            Locale locale = getLocale(request);
            if(member==null){
                member = memberFacade.find(formVO.getMemberId());
//                return this.genFailRepsone(request);
            }
            Long memberId = member.getId();
            Long tenderId = formVO.getTenderId();
            Long appId = formVO.getAppId();
            EtTender etTender = etTenderFacade.find(tenderId);
            List<MemberVO> list = memberFacade.checkMappingMemberAndCategoryId(etTender.getCategoryId(), memberId);
            if (CollectionUtils.isEmpty(list)) {
                return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
            }
            AttachmentVO attachmentVO = null;
            List<AttachmentVO> attachments = etTenderFacade.findFiles(etTender);
            for(AttachmentVO vo : attachments){
                if( vo.getApplicationdata().getId().equals(appId) ){// 比對 app Id
                    attachmentVO = vo;
                    break;
                }
            }
            if( attachmentVO!=null ){
                AttachmentRsVO result = new AttachmentRsVO();
                ExtBeanUtils.copyProperties(result, attachmentVO);
                result.setAppId(attachmentVO.getApplicationdata().getId());
                try{
                    byte[] bytArray = attachmentVO.getContent();
                    if (bytArray != null) {
                        logger.info("getFile bytArray = "+(bytArray!=null?bytArray.length:null));
                        result.setContent(attachmentVO.getContent());
                    }else{
                        logger.info("getFile bytArray is null ");
                        InputStream is = attachmentFacade.getContentStream(attachmentVO);
                        bytArray = IOUtils.toByteArray(is);
                        result.setContent(bytArray);
                    }
                }catch(Exception e){
                    logger.error("getFile Exception: ", e);
                }
                
                return this.genSuccessRepsone(request, result);
            }
            
            return this.genFailRepsone(request);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    @GET
    @Path("/batchUpdateTenderStatus")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response batchUpdateTenderStatus(@Context HttpServletRequest request) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("find homeList ...");
        try {
            etTenderFacade.batchUpdateTenderStatus(null);
        } catch (Exception e) {
            e.printStackTrace();
        }   
        return this.genSuccessRepsone(request);
    }
}

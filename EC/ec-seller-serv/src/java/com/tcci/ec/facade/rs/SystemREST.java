/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcAd;
import com.tcci.ec.entity.EcBulletin;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.AdEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcAdFacade;
import com.tcci.ec.facade.EcBulletinFacade;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.model.AdVO;
import com.tcci.ec.model.BulletinVO;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import com.tcci.ec.model.rs.SubmitVO;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 *
 * @author Peter.pan
 */
@Path("/sys")
public class SystemREST extends AbstractWebREST {
    @EJB EcBulletinFacade bulletinFacade;
    @EJB EcAdFacade adFacade;
    @EJB EcFileFacade fileFacade;
    
    public SystemREST(){
        logger.debug("SystemREST init ...");
        // for 支援排序
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("id", "S.ID");// ID
        sortFieldMap.put("starttime", "S.STARTTIME");
        sortFieldMap.put("endtime", "S.ENDTIME");
        sortFieldMap.put("createtime", "S.CREATETIME");
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Bulletin">
    /**
     * 查詢 - 先抓總筆數
     * /services/sys/bulletin/count
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/bulletin/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countBulletins(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countBulletins ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            
            int totalRows = bulletinFacade.countByCriteria(criteriaVO);
            logger.debug("countBulletins totalRows = "+totalRows);
            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/sys/bulletin/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return
     */
    @POST
    @Path("/bulletin/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findBulletins(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findBulletins offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);
        
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<BulletinVO> list = bulletinFacade.findByCriteria(criteriaVO);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/sys/bulletin/save
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/bulletin/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveBulletin(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("{} ...", methodName);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
           
            if( formVO==null ){
                logger.error("{} formVO==null", methodName);
                return Response.notAcceptable(null).build();
            }
            EcBulletin entity = (formVO.getId()==null)? new EcBulletin():bulletinFacade.find(formVO.getId());
            if( entity==null ){
                logger.error("{} entity==null, id={}", methodName, formVO.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            entity.setDisabled(formVO.getActive()!=null?!formVO.getActive():false);
            entity.setStarttime(formVO.getStarttime());
            entity.setEndtime(formVO.getEndtime());
            entity.setContent(formVO.getContent());
            
            if( bulletinFacade.checkInput(entity, member, locale, errors) ){
                bulletinFacade.save(entity, member, false);
                BulletinVO vo = bulletinFacade.findById(entity.getId());
                return this.genSuccessRepsone(request, vo);
            }else{
                logger.error("{} checkInput fail, id={}", methodName, formVO.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Hot Product">
    /**
     * 查詢 - 先抓總筆數
     * /services/sys/ad/count
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/ad/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countAds(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countAds ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            
            int totalRows = adFacade.countByCriteria(criteriaVO);
            logger.debug("countAds totalRows = "+totalRows);
            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            logger.error("countAds Exception:\n", e);
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/sys/ad/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return
     */
    @POST
    @Path("/ad/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findAds(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findAds offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
        offset = (offset==null)?0:offset;
        rows = (rows==null)?1:((rows>GlobalConstant.RS_RESULT_MAX_ROWS)?GlobalConstant.RS_RESULT_MAX_ROWS:rows);
        
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setFullData(true); // for img url
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<AdVO> list = adFacade.findByCriteria(criteriaVO);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/sys/ad/{type}/{id}
     * @param request
     * @param type
     * @param id
     * @param lang 控制非中文類的語系，來源DB的選項都先顯示 ENAME
     * @return
     */
    @GET
    @Path("/ad/full/{type}/{id}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findAdFullInfo(@Context HttpServletRequest request, @PathParam("type")String type, 
            @PathParam("id")Long id, @QueryParam("lang")String lang){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findAdFullInfo ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            logger.info("findAdFullInfo id = "+id);
            
            AdVO adVO = adFacade.findById(AdEnum.getFromCode(type), id, true, locale, lang);
            return this.genSuccessRepsone(request, adVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    
    /**
     * 目前生效項目
     * /services/sys/ad/now
     * @param request
     * @param type
     * @param active
     * @return
     */
    @GET
    @Path("/ad/now")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findAdsNow(@Context HttpServletRequest request
            , @QueryParam("type")String type
            , @QueryParam("active")Boolean active){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findAdsNow ... ");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            criteriaVO.setType(type);
            criteriaVO.setActive(active==null?true:active);// 預設傳目前生效項目
            criteriaVO.setFullData(true);
            criteriaVO.setOrderBy("S.SORTNUM, S.STARTTIME DESC");
            List<AdVO> list = adFacade.findByCriteria(criteriaVO);
            
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/sys/ad/save
     * @param request
     * @param multiPart
     * @return
     */
    @Path("/ad/save")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadAd(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadAd ...");
        String root = GlobalConstant.DIR_ADMIN_IMG;
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            Long id = this.getMultiPartLong(multiPart, "id");
            logger.debug("uploadAd id = {}", id);
            String type = this.getMultiPartValue(multiPart, "type");
            logger.debug("uploadAd type = {}", type);
            AdEnum typeEnum = AdEnum.getFromCode(type);
            Long prdId = this.getMultiPartLong(multiPart, "prdId");
            logger.debug("uploadAd prdId = {}", prdId);
            Long storeId = this.getMultiPartLong(multiPart, "storeId");
            logger.debug("uploadAd storeId = {}", storeId);
            String fileName = this.getMultiPartValue(multiPart, "filename");
            logger.info("uploadAd fileName = "+fileName);
            String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");
            logger.debug("uploadAd fileContentType = {}", fileContentType);
            Date starttime = this.getMultiPartDate(multiPart, "starttime");
            logger.debug("uploadAd starttime = {}", starttime);
            Date endtime = this.getMultiPartDate(multiPart, "endtime");
            logger.debug("uploadAd endtime = {}", endtime);
            String message = this.getMultiPartValue(multiPart, "message");
            logger.debug("uploadAd message = {}", message);
            Boolean approve = getMultiPartBoolean(multiPart, "approve", false);
            logger.debug("uploadAd approve = {}", approve);
            
            // 先儲存主檔
            EcAd entity = (id!=null)?adFacade.find(id):new EcAd();
            if( entity==null ){
                entity = new EcAd();
            }
            entity.setType(type);
            entity.setStoreId(storeId);
            entity.setPrdId(prdId);
            entity.setMessage(message);
            entity.setStarttime(starttime);
            entity.setEndtime(endtime);
            
            if( adFacade.checkInput(entity, member, locale, errors) ){
                if( approve && entity.getApproveTime()==null ){
                    entity.setApproveTime(new Date());
                    entity.setApproveUser(member.getId());
                }else if( !approve && entity.getApproveTime()!=null ){
                    entity.setApproveTime(null);
                    entity.setApproveUser(null);
                }
                adFacade.save(entity, member, false);
            }else{
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            
            if( entity.getId()==null ){
                return this.genFailRepsone(request, ResStatusEnum.FAIL, errors);
            }
            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            
            if( bodyParts!=null && !bodyParts.isEmpty() ){
                // 上傳圖片
                byte[] content  = null;
                for (int i = 0; i < bodyParts.size(); i++) {
                    //fileName = bodyParts.get(i).getContentDisposition().getFileName();
                    //logger.info("uploadAd fileName = "+fileName);
                    BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                    //fileContentType = (fileContentType==null)?bodyParts.get(i).getContentDisposition().getType():fileContentType;
                    //logger.debug("uploadAd file ContentType = " + fileContentType);
                    content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                    logger.debug("uploadAd content size = "+((content!=null)?content.length:0));
                    break; // 只支援一個圖檔
                }
                
                if( fileName==null || content==null ){
                    logger.error("uploadAd error fileName==null || content==null");
                    return Response.notAcceptable(null).build(); // HTTP STATUS 406
                }
                
                FileEnum fileEnum = FileEnum.AD;
                // 儲存實體檔案
                FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, true, null);
                logger.info("uploadStorePicture write real file finish.");
                
                /*
                // 取附檔名
                String[] fs = FileUtils.getFileExtension(fileName);
                String name = fs[0];
                String fext = fs[1];
                
                // 儲存實體檔案
                String dir = img.getFullPath(FileEnum.AD.getCode(), ImageSizeEnum.ORIGINAL.getCode(), null);
                String saveFileName = UUID.randomUUID().toString() + "." +fext;
                String saveFileNameFull = img.getFullFileName(
                        FileEnum.AD.getCode(), ImageSizeEnum.ORIGINAL.getCode(), null, saveFileName);
                
                File file = new File(saveFileNameFull);
                FileUtils.writeByteArrayToFile(file, content);
                logger.info("uploadAd saveFileName = "+saveFileName);
            
                // 縮圖
                ImageVO retImgVO = new ImageVO();
                // 取得Image寬高
                String errMsg1 = img.getImageInfo(saveFileNameFull, retImgVO);
                if( errMsg1!=null ){
                    logger.error("uploadAd getImageInfo ... "+errMsg1);
                }else{
                    // 縮圖 (縮圖需寬高資訊)
                    String errMsg2 = img.compressImageFile(dir, saveFileName, saveFileNameFull, true, retImgVO);
                    if( errMsg2!=null ){
                        logger.error("uploadAd compressImageFile ... "+errMsg1);
                    }
                }
                logger.info("uploadAd compress image finish.");
                */
                // Save EcFile
                EcFile fileEntity = (id!=null)?fileFacade.findEntityByPrimary(EcAd.class.getName(), entity.getId()):new EcFile();
                if( fileEntity==null ){
                    fileEntity = new EcFile();
                }
                
                fileEntity.setStoreId(null);
                fileEntity.setPrimaryType(fileEnum.getPrimaryType());
                fileEntity.setPrimaryId(entity.getId());// 此處可能為 null 或 0，後續再 update
                fileEntity.setDescription(fileEnum.toString());
                //fileEntity.setFilename(fileName);
                //fileEntity.setName(name);
                //fileEntity.setSavedir(saveFileNameFull.replaceAll(saveFileName, ""));
                //fileEntity.setSavename(saveFileName);
                fileEntity.setContentType(fileContentType);

                fileEntity.setFilename(fileVO.getFilename());
                fileEntity.setName(fileVO.getName());
                fileEntity.setSavedir(fileVO.getSavedir());
                fileEntity.setSavename(fileVO.getSavename());
                fileEntity.setFileSize(fileVO.getFileSize());
                fileFacade.save(fileEntity, member, false);
            }
            
            AdVO resVO = adFacade.findById(typeEnum, entity.getId(), true, locale, null);
            return this.genSuccessRepsone(request, resVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/sys/ad/sort/save
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/ad/sort/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveAdSort(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveAdSort ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            if( formVO==null || formVO.getAdList()==null ){
                logger.error("saveAdSort formVO==null");
                return Response.notAcceptable(null).build();
            }
            String type = formVO.getType();
            logger.debug("uploadAd type = {}", type);
            AdEnum typeEnum = AdEnum.getFromCode(type);
            
            int sortnum = 0;
            for(AdVO vo : formVO.getAdList()){
                if( vo.isClientModified() ){
                    sortnum++;
                    EcAd entity = vo.getId()!=null? adFacade.find(vo.getId()):null;
                    if( entity!=null ){
                        entity.setSortnum(sortnum);
                        adFacade.save(entity, member, false);
                        logger.info("saveAdSort save EcAd = "+entity.getId());
                    }
                }
            }
            
            return findAdsNow(request, type, true);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/sys/ad/remove
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/ad/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeAd(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeAd ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 平台管理員專屬 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, true) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null ){
                logger.error("removeAd formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long id = formVO.getId();
            String type = formVO.getType();
            logger.info("removeAd id = "+id+", type = "+type);
            EcAd entity = adFacade.find(id);
            if( entity!=null && entity.getType()!=null && entity.getType().equals(type) ){
                entity.setDisabled(Boolean.TRUE);
                adFacade.save(entity, member, false);
            }else{
                logger.error("removeAd not exists id = "+id);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }

            return this.genSuccessRepsoneWithId(request, id);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Download File">
    /**
     * 下載匯出檔
     * @param request
     * @param filename
     * @return 
     */
    @GET
    @Path("/exp/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/vnd.ms-excel")
    public Response getExcelFile(@Context HttpServletRequest request, @QueryParam("filename")String filename){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("getExcelFile ...");
        File file = new File(GlobalConstant.DIR_EXPORT + "/" + filename);
        //String expfilename = "export-" + DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR)+".xlsx";
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename="+filename);
        return response.build();
    }
    //</editor-fold>
}

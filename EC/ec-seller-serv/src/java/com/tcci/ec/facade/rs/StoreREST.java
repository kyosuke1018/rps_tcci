/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPayment;
import com.tcci.ec.entity.EcShipping;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.entity.EcStoreArea;
import com.tcci.ec.entity.EcStoreUser;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.StoreRoleEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.facade.EcPaymentFacade;
import com.tcci.ec.facade.EcShippingFacade;
import com.tcci.ec.facade.EcStoreAreaFacade;
import com.tcci.ec.facade.EcStoreUserFacade;
import com.tcci.ec.model.PaymentVO;
import com.tcci.ec.model.ShippingVO;
import com.tcci.ec.model.criteria.StoreCriteriaVO;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.StoreAreaVO;
import com.tcci.ec.model.StoreVO;
import com.tcci.ec.model.rs.StrOptionVO;
import com.tcci.ec.model.rs.BaseResponseVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.ArrayList;
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
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 *
 * @author Peter.pan
 */
@Path("/stores")
public class StoreREST extends AbstractWebREST {
    @EJB EcFileFacade fileFacade;
    @EJB EcPaymentFacade paymentFacade;
    @EJB EcShippingFacade shippingFacade;
    @EJB EcStoreAreaFacade storeAreaFacade;
    @EJB EcStoreUserFacade storeUserFacade;
    
    public StoreREST(){
        logger.debug("StoreREST init ...");
        // for 支援排序
        // Store
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("storeId", "S.ID");
        sortFieldMap.put("cname", "C.CNAME");
        sortFieldMap.put("ename", "C.ENAME");
        sortFieldMap.put("opened", "S.OPENED");
        sortFieldMap.put("loginAccount", "M.LOGIN_ACCOUNT");
        sortFieldMap.put("name", "M.NAME");
        sortFieldMap.put("idCode", "C.ID_CODE");
        sortFieldMap.put("tel1", "C.TEL1");
        sortFieldMap.put("email1", "C.EMAIL1");
        sortFieldMap.put("addr1", "S.ADDR1");

        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Store Main">
    /**
     * 查詢 - 先抓總筆數
     * /services/stores/count
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countStores(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countStores ...");
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

            StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setFullData(false);
            
            int totalRows = storeFacade.countByCriteria(criteriaVO);
            logger.debug("countStores totalRows = "+totalRows);
            BaseResponseVO resVO = genCountSuccessRepsone(request, totalRows);
            
            return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/stores/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return 
     */
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findStores(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findStores offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
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

            StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setFullData(true);
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<StoreVO> list = storeFacade.findByCriteria(criteriaVO);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/stores/full/{id}
     * @param request
     * @param id
     * @return 
     */
    @GET
    @Path("/full/{id}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findStoreFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findStoreFullInfo ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            Long storeId = admin?id:(store!=null?store.getId():null);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( storeId == null ){
                logger.error("findStoreFullInfo storeId==null ");
                return this.genFailRepsone(request); 
            }
            
            StoreVO vo = storeFacade.findById(storeId, true);

            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 新增店家
     * /services/stores/add
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response addStore(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("addStore ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            MemberVO memberVO = memberFacade.findById(member.getId(), true, locale);
            if( memberVO==null || !memberVO.getActive() || memberVO.getDisabled() || memberVO.getSellerId()==null ){
                logger.error("addStore memberVO==null");
                return genUnauthorizedResponse();
            }
            
            // 限制單一賣家最大商店數
            int num = storeFacade.countMemberStore(memberVO.getId());
            if( num >= GlobalConstant.MAX_STORE_NUM ){
                return genFailRepsone(request, ResStatusEnum.IN_ERROR_LIMIT, errors);
            }
            
            StoreVO vo = new StoreVO();
            vo.setCname(formVO.getCname());
            if( storeFacade.checkInput(vo, member, locale, errors) ){// 輸入檢查
                EcStore storeNew = storeFacade.createNewStore(memberVO, vo, member, locale, false, false, false);// defStore, forTccDealer, simulated
                
                vo = storeFacade.findById(storeNew.getId(), false);
                return this.genSuccessRepsone(request, vo);
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    
    /**
     * 儲存商店
     * /services/stores/save
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveStore(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveStore ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?(formVO!=null?formVO.getStoreId():null):(store!=null?store.getId():null);
            if( storeId==null || formVO==null ){
                logger.error("saveStore storeId==null || formVO==null");
                return Response.notAcceptable(null).build();
            }
            formVO.setStoreId(storeId);

            StoreVO vo = storeFacade.findById(storeId, false);
            if( vo==null ){
                logger.error("saveStore vo==null");
                return Response.notAcceptable(null).build();
            }
            Long companyId = vo.getId();// 預存 companyId, 避免被 SubmitVO.id 覆蓋
            Boolean opened = vo.getOpened();
            ExtBeanUtils.copyProperties(vo, formVO);
            vo.setId(companyId);// 回復 companyId
            vo.setOpened(opened);
            vo.setDisabled(Boolean.FALSE);

            if( storeFacade.checkInput(vo, member, locale, errors) ){// 輸入檢查
                storeFacade.saveVO(vo, member, false);
                
                //return this.genSuccessRepsoneWithId(request, vo.getStoreId());
                return findStoreFullInfo(request, vo.getStoreId());
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
  
    /**
     * 設定賣家預設商店
     * /services/stores/default
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/default")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response setDefaultStore(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("setDefaultStore ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?(formVO!=null?formVO.getStoreId():null):(store!=null?store.getId():null);
            if( formVO==null || storeId==null || formVO.getActive()==null ){
                logger.error("setDefaultStore formVO==null");
                return Response.notAcceptable(null).build();
            }

            logger.error("setDefaultStore storeId = "+storeId);
            EcStore entity = storeFacade.find(storeId);
            MemberVO memberVO = memberFacade.findById(member.getId(), false, locale);
            if( memberVO!=null && entity!=null && storeId.equals(entity.getId()) ){
                storeFacade.setDefaultStore(memberVO.getSellerId(), storeId, member, false); 
                //entity.setDefStore(true);
                //storeFacade.save(entity, member, false);
            }else{
                logger.error("setDefaultStore invalid storeId="+storeId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            return findStoreFullInfo(request, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * /services/stores/open
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/open")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response openStore(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("openStore ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = (store!=null?store.getId():null);
            if( formVO==null || storeId==null || formVO.getOpened()==null ){
                logger.error("openStore formVO==null");
                return Response.notAcceptable(null).build();
            }

            logger.error("openStore storeId = "+storeId);
            EcStore entity = storeFacade.find(storeId);
            if( entity!=null ){
                entity.setOpened(formVO.getOpened());
                storeFacade.save(entity, member, false);
            }else{
                logger.error("openStore invalid storeId="+storeId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            return findStoreFullInfo(request, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * /services/stores/active
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/active")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response activeStore(@Context HttpServletRequest request, SubmitVO formVO) {
        logger.debug("removeStore ...");
        boolean disabled = (formVO.getActive()==null) || (!formVO.getActive());
        return disableStore(request, formVO, disabled);
    }

    /**
     * /services/stores/remove
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeStore(@Context HttpServletRequest request, SubmitVO formVO) {
        logger.debug("removeStore ...");
        return disableStore(request, formVO, true);
    }

    public Response disableStore(@Context HttpServletRequest request, SubmitVO formVO, boolean disabled) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("disableStore ...");
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
            
            Long storeId = admin?(formVO!=null?formVO.getStoreId():null):(store!=null?store.getId():null);
            if( formVO==null || storeId==null ){
                logger.error("disableStore formVO==null");
                return Response.notAcceptable(null).build();
            }

            logger.error("disableStore storeId = "+storeId);
            EcStore entity = storeFacade.find(storeId);
            if( entity!=null ){
                entity.setDisabled(disabled);
                storeFacade.save(entity, member, false);
            }else{
                logger.error("disableStore invalid storeId="+storeId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            return this.genSuccessRepsoneWithId(request, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * /services/stores/defAttrs
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/defAttrs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response genDefAttrs(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("genDefAttrs ...");
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
            
            Long storeId = formVO==null?null:formVO.getStoreId();
            if( storeId==null ){
                logger.error("genDefAttrs storeId==null");
                return Response.notAcceptable(null).build();
            }

            logger.error("genDefAttrs storeId = "+storeId);
            StoreVO vo = storeFacade.findById(storeId, false);
            if( vo!=null ){
                boolean forTccDealer = vo.getTccDealer()!=null || vo.getTccDealer();
                storeFacade.autoGenStoreConfig(storeId, false, member, locale, forTccDealer, false);
                vo = storeFacade.findById(storeId, true);
            }else{
                logger.error("genDefAttrs invalid storeId="+storeId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            return this.genSuccessRepsone(request, vo);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Store Pictures">
    @Path("/logo/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadStoreLogo(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadStoreLogo ...");
        return uploadStorePicture(request, FileEnum.STORE_LOGO.getCode(), multiPart);
    }
    
    @Path("/banner/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadStoreBanner(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadStoreBanner ...");
        return uploadStorePicture(request, FileEnum.STORE_BANNER.getCode(), multiPart);
    }
    
    /**
     * 
     * @param request
     * @param imgSrc
     * @param multiPart
     * @return 
     */
    @Path("/picture/{imgSrc}/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadStorePicture(@Context HttpServletRequest request, @PathParam("imgSrc")String imgSrc, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadStorePicture ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            FileEnum fileEnum = FileEnum.getFromCode(imgSrc);
            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            if( bodyParts==null || bodyParts.isEmpty() || fileEnum==null ){
                logger.error("uploadStorePicture error bodyParts==null || bodyParts.isEmpty() || fileEnum==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            Long storeId = this.getMultiPartLong(multiPart, "storeId");
            storeId = admin?storeId:(store!=null?store.getId():null);
            if( storeId == null ){
                logger.error("uploadStorePicture storeId==null ");
                return this.genFailRepsone(request); 
            }
            
            Long id = this.getMultiPartLong(multiPart, "id");
            String fileName = this.getMultiPartValue(multiPart, "filename");
            logger.info("uploadStorePicture fileName = "+fileName);
            String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");

            byte[] content  = null;
            for (int i = 0; i < bodyParts.size(); i++) {
                //fileName = bodyParts.get(i).getContentDisposition().getFileName();// 中文會亂碼
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                fileContentType = (fileContentType==null)?bodyParts.get(i).getContentDisposition().getType():fileContentType;
                //logger.debug("uploadStoreLogo file ContentType = " + fileContentType);
                content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                logger.debug("uploadStorePicture content size = "+((content!=null)?content.length:0));
                break; // 暫時段落，只支援一個圖檔
            }
            
            if( fileName==null || content==null ){
                logger.error("uploadStorePicture error fileName==null || content==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            // 儲存實體檔案
            FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, true, storeId);
            logger.info("uploadStorePicture write real file finish.");
            
            // Save EcFile
            EcFile fileEntity = (id!=null)?fileFacade.find(id):new EcFile();
            if( fileEntity!=null && fileEntity.getStoreId()!=null && !fileEntity.getStoreId().equals(storeId) ){
                logger.error("uploadStorePicture error : {} : {}", fileEntity.getStoreId(), storeId);
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }else if( fileEntity==null ){
                fileEntity = new EcFile();
            }
            
            fileEntity.setStoreId(storeId);
            fileEntity.setPrimaryType(fileEnum.getPrimaryType());
            fileEntity.setPrimaryId(storeId);// 此處可能為 null 或 0，後續再 update
            fileEntity.setDescription("logo image");
            fileEntity.setContentType(fileContentType);
            
            fileEntity.setFilename(fileVO.getFilename());
            fileEntity.setName(fileVO.getName());
            fileEntity.setSavedir(fileVO.getSavedir());
            fileEntity.setSavename(fileVO.getSavename());
            fileEntity.setFileSize(fileVO.getFileSize());
            fileFacade.save(fileEntity, member, false);
            logger.info("uploadStorePicture save db finish.");
            
            FileVO resVO = fileFacade.findSingleByPrimary(storeId, imgSrc, storeId);
            return this.genSuccessRepsone(request, resVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/stores/logo/remove
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/picture/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeStorePicture(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeStorePicture ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?formVO.getStoreId():(store!=null?store.getId():null);
            EcFile entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 ){
                entity = fileFacade.find(formVO.getId());
            }else{
                logger.error("removeStorePicture entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            if( storeId == null ){
                logger.error("removeStorePicture storeId==null ");
                return this.genFailRepsone(request); 
            }
            
            if( entity==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }else{
                // 商店ID驗證
                if( !storeId.equals(entity.getStoreId()) ){
                    logger.error("removeStorePicture storeId error : "+storeId+" != "+entity.getStoreId());
                    return Response.notAcceptable(null).build();
                }
                // 不刪實體檔，避免有共用狀況(排程刪除即可)
                fileFacade.remove(entity, false);
                return this.genSuccessRepsoneWithId(request, formVO.getId());
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Payment">
    /**
     * /services/stores/payment/list
     * @param request
     * @param storeId
     * @return 
     */
    @GET
    @Path("/payment/list")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findStorePayments(@Context HttpServletRequest request, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findStorePayments ... storeId = "+storeId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("findStorePayments storeId = {}", storeId);
            if( storeId == null ){
                logger.error("findStorePayments storeId==null ");
                return this.genFailRepsone(request); 
            }
            List<PaymentVO> list = paymentFacade.findByStore(storeId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            logger.error("findStorePayments Exception:\n", e);
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 挑選方式 (分類別)
     * /services/stores/payment/save
     * @param request
     * @param formVO
     * @param storeId
     * @return 
     */
    @POST
    @Path("/payment/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveStorePayment(@Context HttpServletRequest request, SubmitVO formVO, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveStorePayment ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("saveStorePayment storeId = {}", storeId);
            logInputs("saveStorePayment", formVO, member);// log 輸入資訊
            if( formVO==null || storeId==null || formVO.getPayMethods()==null ){
                logger.error("saveStorePayment formVO==null");
                return Response.notAcceptable(null).build();
            }

            // List<PaymentVO> oriList = paymentFacade.findByStore(storeId);
            List<EcPayment> oriList = paymentFacade.findStorePayments(storeId);
            // 移除
            if( oriList!=null ){
                for(EcPayment ori:oriList){
                    boolean exists = false;
                    for(StrOptionVO vo : formVO.getPayMethods()){
                        if( vo.getValue().equals(ori.getCode()) ){
                            exists = true;
                            break;
                        }
                    }
                    if( !exists ){
                        // 非真實刪除
                        //paymentFacade.remove(ori.getId(), false);
                        ori.setDisabled(true);
                        paymentFacade.save(ori, member, false);
                    }
                }
            }
            
            // 新增
            for(StrOptionVO vo : formVO.getPayMethods()){
                boolean exists = false;
                if( oriList!=null ){
                    for(EcPayment ori:oriList){
                        if( vo.getValue().equals(ori.getCode()) ){
                            exists = true;
                            break;
                        }
                    }
                }
                
                if( !exists ){
                    EcPayment entity = paymentFacade.genByEnumCode(storeId, vo.getValue(), 1, locale);
                    if( entity!=null ){
                        if( paymentFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                            paymentFacade.save(entity, member, false); 
                        }else{
                            return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        }
                    }else{
                        return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                    }
                }
            }
            
            return findStorePayments(request, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 舊方式 (自行輸入，不分類別)
     * /services/stores/payment/save
     * @param request
     * @param formVO
     * @param storeId
     * @return 
     */
    /*
    @POST
    @Path("/payment/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveStorePayment(@Context HttpServletRequest request, SubmitVO formVO, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveStorePayment ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("saveStorePayment storeId = {}", storeId);
            logInputs("saveStorePayment", formVO, member);// log 輸入資訊
            if( formVO==null || storeId==null || formVO.getPaymentList()==null ){
                logger.error("saveStorePayment formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            int sortnum = 0;
            for(PaymentVO vo : formVO.getPaymentList()){
                if( vo.isClientModified() ){
                    sortnum++;
                    EcPayment entity = vo.getId()!=null? paymentFacade.find(vo.getId()):new EcPayment();
                    if( entity!=null ){
                        entity.setTitle(vo.getTitle());
                        entity.setStoreId(storeId);
                        entity.setActive(true);
                        entity.setDisabled(false);
                        entity.setSortnum(sortnum);
                        
                        if( paymentFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                            paymentFacade.save(entity, member, false); 
                        }else{
                            return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        }
                    }
                }
            }
            
            return findStorePayments(request, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    */
    
    /**
     * /services/stores/payment/remove
     * @param request
     * @param formVO
     * @param storeId
     * @return 
     */
    @POST
    @Path("/payment/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeStorePayment(@Context HttpServletRequest request, SubmitVO formVO, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeStorePayment ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("removeStorePayment storeId = {}", storeId);
            if( storeId == null ){
                logger.error("removeStorePayment storeId==null ");
                return this.genFailRepsone(request); 
            }
            EcPayment entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 ){
                entity = paymentFacade.find(formVO.getId());
            }else{
                logger.error("removeStorePayment entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            
            BaseResponseVO resVO = null;
            if( entity==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }else{
                // 商店ID驗證
                if( !storeId.equals(entity.getStoreId()) ){
                    logger.error("removeStorePayment storeId error : "+storeId+" != "+entity.getStoreId());
                    return Response.notAcceptable(null).build();
                }
                
                entity.setDisabled(Boolean.TRUE);// 非真實刪除
                paymentFacade.save(entity, member, false);
                
                //return this.genSuccessRepsoneWithId(request, formVO.getId());
                return findStorePayments(request, storeId);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Shipping">
    /**
     * /services/stores/shipping/list
     * @param request
     * @param storeId
     * @return 
     */
    @GET
    @Path("/shipping/list")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findStoreShippings(@Context HttpServletRequest request, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findStoreShippings ... storeId = "+storeId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("findStoreShippings storeId = {}", storeId);
            if( storeId == null ){
                logger.error("findStoreShippings storeId==null ");
                return this.genFailRepsone(request); 
            }
            List<ShippingVO> list = shippingFacade.findByStore(storeId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 挑選方式 (分類別)
     * /services/stores/shipping/save
     * @param request
     * @param formVO
     * @param storeId
     * @return 
     */
    @POST
    @Path("/shipping/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveStoreShipping(@Context HttpServletRequest request, SubmitVO formVO, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveStoreShipping ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("saveStoreShipping storeId = {}", storeId);
            logInputs("saveStoreShipping", formVO, member);// log 輸入資訊
            if( formVO==null || storeId==null || formVO.getShipMethods()==null ){
                logger.error("saveStoreShipping formVO==null");
                return Response.notAcceptable(null).build();
            }

            //List<ShippingVO> oriList = shippingFacade.findByStore(storeId);
            List<EcShipping> oriList = shippingFacade.findStoreShippings(storeId);
            
            // 移除
            if( oriList!=null ){
                for(EcShipping ori:oriList){
                    boolean exists = false;
                    for(StrOptionVO vo : formVO.getShipMethods()){
                        if( vo.getValue().equals(ori.getCode()) ){
                            exists = true;
                            break;
                        }
                    }
                    if( !exists ){
                        // 非真實刪除
                        //shippingFacade.remove(ori.getId(), false);
                        ori.setDisabled(true);
                        shippingFacade.save(ori, member, false);
                    }
                }
            }
            
            // 新增
            for(StrOptionVO vo : formVO.getShipMethods()){
                boolean exists = false;
                if( oriList!=null ){
                    for(EcShipping ori:oriList){
                        if( vo.getValue().equals(ori.getCode()) ){
                            exists = true;
                            break;
                        }
                    }
                }
                
                if( !exists ){
                    EcShipping entity = shippingFacade.genByEnumCode(storeId, vo.getValue(), 1, locale);
                    if( entity!=null ){
                        if( shippingFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                            shippingFacade.save(entity, member, false); 
                        }else{
                            return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        }
                    }else{
                        return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                    }
                }
            }
            
            return findStoreShippings(request, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 舊方式 (自行輸入，不分類別)
     * /services/stores/shipping/save
     * @param request
     * @param formVO
     * @param storeId
     * @return 
     */
    /*
    @POST
    @Path("/shipping/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveStoreShipping(@Context HttpServletRequest request, SubmitVO formVO, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveStoreShipping ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("saveStoreShipping storeId = {}", storeId);
            if( storeId == null ){
                logger.error("saveStoreShipping storeId==null ");
                return this.genFailRepsone(request); 
            }
            logInputs("saveStoreShipping", formVO, member);// log 輸入資訊

            if( formVO==null || formVO.getShippingList()==null ){
                logger.error("saveStoreShipping formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            int sortnum = 0;
            for(ShippingVO vo : formVO.getShippingList()){
                if( vo.isClientModified() ){
                    sortnum++;
                    EcShipping entity = vo.getId()!=null? shippingFacade.find(vo.getId()):new EcShipping();
                    if( entity!=null ){
                        entity.setTitle(vo.getTitle());
                        entity.setStoreId(storeId);
                        entity.setActive(true);
                        entity.setDisabled(false);
                        entity.setSortnum(sortnum);

                        if( shippingFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                            shippingFacade.save(entity, member, false); 
                        }else{
                            return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        }
                    }
                }
            }
            
            return findStoreShippings(request, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    */
    
    /**
     * /services/stores/shipping/remove
     * @param request
     * @param formVO
     * @param storeId
     * @return 
     */
    @POST
    @Path("/shipping/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeStoreShipping(@Context HttpServletRequest request, SubmitVO formVO, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeStoreShipping ...");
         List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("removeStoreShipping storeId = {}", storeId);
            if( storeId == null ){
                logger.error("removeStoreShipping storeId==null ");
                return this.genFailRepsone(request); 
            }
            EcShipping entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 ){
                entity = shippingFacade.find(formVO.getId());
            }else{
                logger.error("removeStoreShipping entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            
            BaseResponseVO resVO = null;
            if( entity==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }else{
                // 商店ID驗證
                if( !storeId.equals(entity.getStoreId()) ){
                    logger.error("removeStoreShipping storeId error : "+storeId+" != "+entity.getStoreId());
                    return Response.notAcceptable(null).build();
                }
                
                entity.setDisabled(Boolean.TRUE);// 非真實刪除
                shippingFacade.save(entity, member, false);
                //return this.genSuccessRepsoneWithId(request, formVO.getId());
                return findStoreShippings(request, storeId);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Store Area">
    /**
     * /services/stores/salesArea/list
     * @param request
     * @param storeId
     * @return 
     */
    @GET
    @Path("/salesArea/list")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findStoreSalesArea(@Context HttpServletRequest request, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findStoreSalesArea ... storeId = "+storeId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            storeId = admin?storeId:(store!=null?store.getId():null);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            logger.info("findStoreSalesArea storeId = {}", storeId);
            if( storeId == null ){
                logger.error("findStoreSalesArea storeId==null ");
                return this.genFailRepsone(request); 
            }
            List<StoreAreaVO> list = storeAreaFacade.findByStore(storeId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 挑選方式 (分類別)
     * /services/stores/salesArea/save
     * @param request
     * @param formVO
     * @param storeId
     * @return 
     */
    @POST
    @Path("/salesArea/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveStoreSalesArea(@Context HttpServletRequest request, SubmitVO formVO, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveStoreSalesArea ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:(store!=null?store.getId():null);
            logger.info("saveStoreSalesArea storeId = {}", storeId);

            if( formVO==null || storeId==null || formVO.getStoreAreas()==null ){
                logger.error("saveStoreSalesArea formVO==null");
                return Response.notAcceptable(null).build();
            }

            List<EcStoreArea> oriList = storeAreaFacade.findStoreAreas(storeId);
            
            // 移除
            if( oriList!=null ){
                for(EcStoreArea ori:oriList){
                    boolean exists = false;
                    for(LongOptionVO vo : formVO.getStoreAreas()){
                        if( vo.getValue().equals(ori.getAreaId()) ){// 此處用 areaId
                            exists = true;
                            break;
                        }
                    }
                    if( !exists ){
                        // 真實刪除關連
                        storeAreaFacade.remove(ori.getId(), false);
                    }
                }
            }
            
            // 新增
            for(LongOptionVO vo : formVO.getStoreAreas()){
                boolean exists = false;
                if( oriList!=null ){
                    for(EcStoreArea ori:oriList){
                        if( vo.getValue().equals(ori.getAreaId()) ){
                            exists = true;
                            break;
                        }
                    }
                }
                
                if( !exists ){
                    EcStoreArea entity = new EcStoreArea(storeId, vo.getValue());
                    if( storeAreaFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                        storeAreaFacade.save(entity, member, false); 
                    }else{
                        return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                    }
                }
            }
            
            return findStoreSalesArea(request, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Store Manager">
    /**
     * 新增商店管理員
     * /services/stores/manager/add
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/manager/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response addStoreManager(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("addStoreManager ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            //boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = (store!=null?store.getId():null);
            logger.info("addStoreManager storeId = {}", storeId);

            if( formVO==null || storeId==null || StringUtils.isBlank(formVO.getLoginAccount()) || StringUtils.isBlank(formVO.getName()) ){
                logger.error("addStoreManager formVO==null || storeId==null ");
                return Response.notAcceptable(null).build();
            }

            // 非會員
            MemberVO vo = memberFacade.findVOByLoginAccount(formVO.getLoginAccount(), locale);
            if( vo==null ){
                logger.error("addStoreManager error not a member!");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST_MEM, errors);
            }
            // 原註冊帳號
            if( member.getId().equals(vo.getMemberId()) ){
                logger.error("addStoreManager error is original registry member!");
                errors.add(getResourceMsg(locale, "is.registry.member"));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EXIST, errors);
            }
            // 名稱錯誤
            if( !formVO.getName().equals(vo.getName()) ){
                logger.error("addStoreManager error member name!");
                errors.add(getResourceMsg(locale, "error.mem.name"));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            //MemberVO existedVO = memberFacade.findByStoreManager(storeId, formVO.getLoginAccount(), formVO.getName(), locale);
            EcStoreUser entity = storeUserFacade.findByKey(storeId, vo.getMemberId());
            // 已存在商店管理員
            if( entity!=null && Boolean.FALSE.equals(entity.getDisabled()) ){
                logger.error("addStoreManager error existed manager!");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EXIST, errors);
            }
            if( entity==null ){
                entity = new EcStoreUser();
                entity.setMemo("new");
            }else{
                entity.setMemo("update");
            }
            entity.setDisabled(Boolean.FALSE);
            entity.setOwner(Boolean.FALSE);
            entity.setMemberId(vo.getMemberId());
            entity.setStoreId(storeId);
            storeUserFacade.save(entity, member, false);
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * 刪除商店管理員
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/manager/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeStoreManager(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeStoreManager ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            //boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = (store!=null?store.getId():null);
            logger.info("removeStoreManager storeId = {}", storeId);
            
            if( formVO==null || storeId==null || formVO.getId()==null ){
                logger.error("addStoreManager formVO==null || storeId==null ");
                return Response.notAcceptable(null).build();
            }
            
            EcStoreUser entity = storeUserFacade.find(formVO.getId());
            if( entity==null || !storeId.equals(entity.getStoreId()) || Boolean.TRUE.equals(entity.getDisabled()) ){
                logger.error("addStoreManager entity==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            // 不實際刪除，已備稽核
            entity.setDisabled(Boolean.TRUE);
            storeUserFacade.save(entity, member, false);
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 設定商店管理員角色
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/manager/set")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response setStoreManagerRole(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("setStoreManagerRole ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            //boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = (store!=null?store.getId():null);
            logger.info("setStoreManagerRole storeId = {}", storeId);
            
            if( formVO==null || storeId==null || formVO.getId()==null ){
                logger.error("setStoreManagerRole formVO==null || storeId==null ");
                return Response.notAcceptable(null).build();
            }
            
            // 只開放最高管理員可設定
            MemberVO operator = memberFacade.findById(member.getId(), false, locale);
            if( operator==null || !sys.isTrue(operator.getStoreOwner()) ){
                logger.error("setStoreManagerRole !sys.isTrue(operator.getStoreOwner()) ");
                return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, errors);
            }
            
            Long id = formVO.getId();
            Long memberId = formVO.getMemberId();
            StoreRoleEnum roleEnum = StoreRoleEnum.getFromCode(formVO.getRoleCode());
            Boolean enable = formVO.getEnable();
            
            EcStoreUser entity = storeUserFacade.find(id);
            if( entity==null || !storeId.equals(entity.getStoreId()) || sys.isTrue(entity.getDisabled())
               || !entity.getMemberId().equals(memberId) ){
                logger.error("setStoreManagerRole entity==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            // 不可改自己的角色權限
            if( entity.getMemberId().equals(member.getId()) ){
                logger.error("setStoreManagerRole entity.getMemberId().equals(member.getId()) ");
                return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, errors);
            }
            
            if( StoreRoleEnum.OWNER == roleEnum ){
                entity.setOwner(enable);
            }else if( StoreRoleEnum.FI_USER == roleEnum ){
                entity.setFiUser(enable);
            }
            storeUserFacade.save(entity, member, false);
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
}

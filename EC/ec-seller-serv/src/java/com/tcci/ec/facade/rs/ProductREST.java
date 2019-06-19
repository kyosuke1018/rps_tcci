/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.ec.facade.rs;

import com.tcci.cm.annotation.util.AnnotationExportUtils;
import com.tcci.cm.enums.NumericPatternEnum;
import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExportUtils;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdAttr;
import com.tcci.ec.entity.EcPrdAttrVal;
import com.tcci.ec.entity.EcPrdDetail;
import com.tcci.ec.entity.EcPrdIntro;
import com.tcci.ec.entity.EcPrdPayment;
import com.tcci.ec.entity.EcPrdShipping;
import com.tcci.ec.entity.EcPrdType;
import com.tcci.ec.entity.EcPrdVarOption;
import com.tcci.ec.entity.EcPrdVariant;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.entity.EcStockLog;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.entity.EcTccProduct;
import com.tcci.ec.enums.BrandEnum;
import com.tcci.ec.enums.DataTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.ProductUnitEnum;
import com.tcci.ec.enums.ProductVariantEnum;
import com.tcci.ec.enums.StockEnum;
import com.tcci.ec.enums.VendorEnum;
import com.tcci.ec.facade.EcPrdAttrFacade;
import com.tcci.ec.facade.EcPrdIntroFacade;
import com.tcci.ec.facade.EcPrdTypeFacade;
import com.tcci.ec.facade.EcProductFacade;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.facade.EcOptionFacade;
import com.tcci.ec.facade.EcPrdAttrValFacade;
import com.tcci.ec.facade.EcPrdDetailFacade;
import com.tcci.ec.facade.EcPrdPaymentFacade;
import com.tcci.ec.facade.EcPrdShippingFacade;
import com.tcci.ec.facade.EcPrdVarOptionFacade;
import com.tcci.ec.facade.EcPrdVariantFacade;
import com.tcci.ec.facade.EcStockLogFacade;
import com.tcci.ec.facade.EcTccProductFacade;
import com.tcci.ec.facade.EcVendorFacade;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.OptionVO;
import com.tcci.ec.model.rs.ImportProductVO;
import com.tcci.ec.model.rs.ImportResultVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.PrdAttrVO;
import com.tcci.ec.model.PrdAttrValVO;
import com.tcci.ec.model.PrdDetailVO;
import com.tcci.ec.model.PrdIntroVO;
import com.tcci.ec.model.PrdPaymentVO;
import com.tcci.ec.model.PrdShippingVO;
import com.tcci.ec.model.PrdTypeTreeVO;
import com.tcci.ec.model.PrdTypeVO;
import com.tcci.ec.model.PrdVariantVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import com.tcci.ec.model.ProductVO;
import com.tcci.ec.model.StockLogVO;
import com.tcci.ec.model.TccProductVO;
import com.tcci.ec.model.VendorVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.rs.BaseListResponseVO;
import com.tcci.ec.model.rs.BaseResponseVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.ec.model.rs.UploadResponseListVO;
import com.tcci.ec.model.rs.UploadResponseVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.ResourceBundleUtils;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 *
 * @author Peter.pan
 */
@Path("/products")
public class ProductREST extends AbstractWebREST {
    @EJB EcProductFacade productFacade;
    @EJB EcPrdTypeFacade prdTypeFacade;
    @EJB EcPrdAttrFacade prdAttrFacade;
    @EJB EcPrdIntroFacade prdIntroFacade;
    @EJB EcPrdVarOptionFacade prdVarOptionFacade;
    @EJB EcPrdVariantFacade prdVariantFacade;
    @EJB EcPrdShippingFacade prdShippingFacade;
    @EJB EcPrdPaymentFacade prdPaymentFacade;
    @EJB EcPrdAttrValFacade prdAttrValFacade;
    @EJB EcPrdDetailFacade prdDetailFacade;
    @EJB EcStockLogFacade stockLogFacade;
    @EJB EcFileFacade fileFacade;
    @EJB EcOptionFacade optionFacade;
    @EJB EcVendorFacade vendorFacade;
    @EJB EcTccProductFacade tccProductFacade;
    
    protected Map<String, String> sortTccFieldMap;
    
    public ProductREST(){
        logger.debug("ProductREST init ...");
        // for 支援排序
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("id", "S.ID");// ID
        sortFieldMap.put("storeName", "ST.CNAME");// 商店
        sortFieldMap.put("cname", "S.CNAME");// 名稱
        sortFieldMap.put("code", "S.CODE");// 編號
        sortFieldMap.put("statusName", "S.STATUS");//  狀態
        sortFieldMap.put("vendorName", "V.CNAME");// 供應商
        sortFieldMap.put("typeName", "T.TYPENAME");// 類別
        sortFieldMap.put("price", "S.PRICE");// 售價
        sortFieldMap.put("stock", "NVL(ST.STOCK, 0)");// 庫存
        sortFieldMap.put("favCount", "NVL(FP.CC, 0)");
        sortFieldMap.put("createtime", "S.CREATETIME");// 建立時間
        sortFieldMap.put("modifytime", "S.MODIFYTIME");// 最近修改時間
        sortFieldMap.put("publishTime", "S.PUBLISH_TIME");// 發佈時間
        
        sortTccFieldMap = new HashMap<String, String>();
        sortTccFieldMap.put("id", "S.ID");// ID
        sortTccFieldMap.put("code", "S.CODE");// 編號
        sortTccFieldMap.put("cname", "S.NAME");// 名稱
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
    }
    
    /**
     * 若有需要重新送審處理
     * @param prdId 
     */
    private void reapplyProcess(Long prdId, EcMember operator){
        EcProduct entity = productFacade.find(prdId);
        ProductStatusEnum statusEnum = ProductStatusEnum.getFromCode(entity.getStatus());
        if( statusEnum!=null && statusEnum.isReapprove() ){
            entity.setStatus(ProductStatusEnum.APPLY.getCode());
            productFacade.save(entity, operator, false);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Product Type">
    /**
     * 商品分類
     * /services/products/type/tree
     * @param request
     * @param lang 控制非中文類的語系，來源DB的選項都先顯示 ENAME
     * @return
     */
    @GET
    @Path("/type/tree")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findPrdTypeTree(@Context HttpServletRequest request, @QueryParam("lang")String lang){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findPrdTypeTree ... lang = "+lang);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?GlobalConstant.SHARE_STORE_ID:store.getId();
            if( storeId==null ){
                logger.error("findPrdTypeTree storeId==null");
                return Response.notAcceptable(null).build();
            }
            PrdTypeTreeVO tree = prdTypeFacade.findPrdTypeTree(storeId, false, lang);
            return this.genSuccessRepsone(request, tree);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/type/save
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/type/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdType(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdType ...");
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
            Long storeId = GlobalConstant.SHARE_PRD_TYPE?GlobalConstant.SHARE_STORE_ID:(store!=null?store.getId():null);
            if( storeId == null ){
                logger.error("savePrdType storeId==null");
                return Response.notAcceptable(null).build();
            }
            
            boolean isNew = (formVO.getId()==null || formVO.getId()<=0);
            EcPrdType entity = isNew? new EcPrdType():prdTypeFacade.find(formVO.getId());
            
            entity.setLeaf(formVO.getLevelnum()==GlobalConstant.PRD_TYPE_LEVEL);
            entity.setStoreId(storeId);
            entity.setCname(formVO.getCname());
            entity.setParent(zeroToNull(formVO.getTypeL2())!=null? formVO.getTypeL2():(Long)zeroToNull(formVO.getTypeL1()));
            logger.debug("savePrdType parent = "+entity.getParent());
            entity.setLevelnum(formVO.getLevelnum());
            entity.setSortnum(formVO.getSortnum());
            
            if( prdTypeFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                prdTypeFacade.save(entity, member, false);
                
                return this.genSuccessRepsoneWithId(request, entity.getId());
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/type/remove
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/type/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdType(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removePrdType ...");
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
            Long storeId = GlobalConstant.SHARE_PRD_TYPE?GlobalConstant.SHARE_STORE_ID:(store!=null?store.getId():null);
            if( storeId == null ){
                logger.error("removePrdType storeId==null");
                return Response.notAcceptable(null).build();
            }
            
            EcPrdType entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 ){
                entity = prdTypeFacade.find(formVO.getId());
            }else{
                logger.error("removePrdType entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            
            if( entity==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }else{
                // 商店ID驗證
                if( !storeId.equals(entity.getStoreId()) ){
                    logger.error("removePrdType storeId error : "+storeId+" != "+entity.getStoreId());
                    return Response.notAcceptable(null).build();
                }
                
                entity.setDisabled(Boolean.TRUE);// 非真實刪除
                prdTypeFacade.save(entity, member, false);
                return this.genSuccessRepsoneWithId(request, formVO.getId());
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
       
    private Object zeroToNull(Object obj){
        if( obj==null ){
            return null;
        }
        if(obj instanceof Integer){
            if( (Integer)obj==0 ){
                return null;
            }
        }else if(obj instanceof Long){
            if( (Long)obj==0 ){
                return null;
            }
        }else if(obj instanceof Double){
            
        }else if(obj instanceof BigDecimal){
            
        }
        return obj;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Type Attributes">
    /**
     * /services/products/type/{typeId}/attr/list
     * @param request
     * @param typeId
     * @return
     */
    @GET
    @Path("/type/{typeId}/attr/list")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findPrdTypeAttrs(@Context HttpServletRequest request, @PathParam("typeId")Long typeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findPrdTypeAttrs ... typeId = "+typeId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = GlobalConstant.SHARE_PRD_TYPE?GlobalConstant.SHARE_STORE_ID:(store!=null?store.getId():null);
            if( storeId == null ){
                logger.error("findPrdTypeAttrs storeId==null");
                return Response.notAcceptable(null).build();
            }
            List<PrdAttrVO> list = prdAttrFacade.findByPrdType(storeId, typeId);
            
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/type/{typeId}/attr/save
     * @param request
     * @param formVO
     * @param typeId
     * @return
     */
    @POST
    @Path("/type/{typeId}/attr/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdTypeAttr(@Context HttpServletRequest request, SubmitVO formVO, @PathParam("typeId")Long typeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdTypeAttr ...");
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
            
            Long storeId = GlobalConstant.SHARE_PRD_TYPE?GlobalConstant.SHARE_STORE_ID:(store!=null?store.getId():null);
            if( storeId == null ){
                logger.error("savePrdTypeAttr storeId==null");
                return Response.notAcceptable(null).build();
            }
            if( formVO==null || formVO.getAttrs()==null ){
                logger.error("savePrdTypeAttr formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            for(PrdAttrVO vo : formVO.getAttrs()){
                if( vo.getClientModified() ){
                    EcPrdAttr entity = vo.getId()!=null? prdAttrFacade.find(vo.getId()):new EcPrdAttr();
                    if( entity!=null ){
                        ExtBeanUtils.copyProperties(entity, vo);
                        entity.setStoreId(storeId);
                        entity.setDisabled(Boolean.FALSE);
                        entity.setTypeId(typeId);
                        entity.setDataType(DataTypeEnum.STRING.getCode());
                        
                        if( prdAttrFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                            prdAttrFacade.save(entity, member, false);
                        }else{
                            return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        }
                    }
                }
            }
            
            return findPrdTypeAttrs(request, formVO.getTypeId());
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/type/{typeId}/attr/remove
     * @param request
     * @param formVO
     * @param typeId
     * @return
     */
    @POST
    @Path("/type/{typeId}/attr/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdTypeAttr(@Context HttpServletRequest request, SubmitVO formVO, @PathParam("typeId")Long typeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removePrdTypeAttr ...");
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
            
            Long storeId = GlobalConstant.SHARE_PRD_TYPE?GlobalConstant.SHARE_STORE_ID:(store!=null?store.getId():null);
            if( storeId == null ){
                logger.error("removePrdTypeAttr storeId==null");
                return Response.notAcceptable(null).build();
            }
            EcPrdAttr entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 ){
                entity = prdAttrFacade.find(formVO.getId());
            }else{
                logger.error("removePrdTypeAttr entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            
            BaseResponseVO resVO = null;
            if( entity==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }else{
                // 商店ID驗證
                if( !storeId.equals(entity.getStoreId()) ){
                    logger.error("removePrdTypeAttr storeId error : "+storeId+" != "+entity.getStoreId());
                    return Response.notAcceptable(null).build();
                }
                
                entity.setDisabled(Boolean.TRUE);// 非真實刪除
                prdAttrFacade.save(entity, member, false);
                
                return this.genSuccessRepsoneWithId(request, formVO.getId());
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Export">
    @POST
    @Path("/exp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response expProducts(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("expProducts ...");
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
            
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(storeId);
            List<ProductVO> list = productFacade.findByCriteria(criteriaVO, locale);

            String outputFileName = member.getLoginAccount() + "-" + DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR)
                    + "." + GlobalConstant.EXCEL_FILEEXT;
            String outputFileFullName = GlobalConstant.DIR_EXPORT + "/" + outputFileName;

            List<String> headers = formVO.getHeaders();
            Workbook wb = AnnotationExportUtils.gereateExcelReport(ProductVO.class, list, null, headers);
            // format excel
            Map<Integer, Integer> colsWidth = new HashMap<Integer, Integer>();
            colsWidth.put(0, 4);
            if( !admin ){
                colsWidth.put(1, 0);// 隱藏欄位 storeName
            }
            int[] numCols = new int[]{7};//new int[]{7, 9, 10};// count from 0, ProductVO exportIndex
            Map<String, String> numericPatterns = new HashMap<String, String>();
            // price, stock, favCount
            for(int col : numCols){
                numericPatterns.put(Integer.toString(col), NumericPatternEnum.Digits3.getCode());
            }
            ExportUtils.formatExcel(wb, 0, colsWidth, null, null, null, numCols, numericPatterns, false, 1);

            // 儲存
            ExportUtils.saveWorkbook(wb, outputFileFullName);
            
            FileVO fileVO = new FileVO();
            fileVO.setFilename(outputFileName);
            return this.genSuccessRepsone(request, fileVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    /*
    @GET
    @Path("/exp/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/vnd.ms-excel")
    public Response getExcelFile(@Context HttpServletRequest request, @QueryParam("filename")String filename){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("getExcelFile ...");
        File file = new File(GlobalConstant.DIR_EXPORT + "/" + filename);
        //String expfilename = "export-" + DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR)+".xlsx";
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename="+filename);
        return response.build();
    }
    */
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Main">
    /**
     * 商品查詢 - 先抓總筆數
     * /services/products/count
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countProducts(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countProducts ...");
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
            Long storeId = admin?formVO.getStoreId():store.getId();
                       
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(storeId);
            
            int totalRows = productFacade.countByCriteria(criteriaVO);
            logger.debug("countProducts totalRows = "+totalRows);
            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/list
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
    public Response findProducts(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findProducts offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
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
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(storeId);
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<ProductVO> list = productFacade.findByCriteria(criteriaVO, locale);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/full/{id}
     * @param request
     * @param id
     * @param storeId
     * @return
     */
    @GET
    @Path("/full/{id}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findProductFullInfo(@Context HttpServletRequest request, 
            @PathParam("id")Long id, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findProductFullInfo ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:store.getId();
            
            ProductVO prdVO = productFacade.findById(storeId, id, true, locale);
            return this.genSuccessRepsone(request, prdVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/save
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdBasic(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdBasic ...");
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
            if( formVO==null ){
                logger.error("savePrdBasic formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            Long storeId = admin?formVO.getStoreId():store.getId();
            formVO.setStoreId(storeId);
            
            ProductVO prdVO = (formVO.getId()==null || formVO.getId()==0)?
                    new ProductVO():productFacade.findById(storeId, formVO.getId(), false, locale);
            String status = prdVO.getStatus();
            
            if( sys.isTrue(prdVO.getTccPrd()) ){// 匯入台泥商品，目前只能改價格
                prdVO.setPrice(formVO.getPrice());
            }else{
                // 保留非功能維護的欄位值
                Long coverPicId = prdVO.getCoverPicId();
                status = (status==null)?ProductStatusEnum.DRAF.getCode():status;
                ExtBeanUtils.copyProperties(prdVO, formVO);
                // 回復
                prdVO.setCoverPicId(coverPicId);
                prdVO.setStatus(status);
            }
            
            if( productFacade.checkInput(prdVO, member, locale, errors) ){// 輸入檢查
                if( !sys.isTrue(prdVO.getTccPrd()) ){// 匯入台泥商品，目前只能改價格
                    logger.debug("savePrdBasic prdVO.getStatusOri() = "+prdVO.getStatusOri());
                    ProductStatusEnum statusEnum = ProductStatusEnum.getFromCode(status);
                    // 原本為[上架]或[預約上架]，修改了其他欄位資料須重新送審。若改為其他狀態則不用
                    if( statusEnum!=null && statusEnum.isReapprove() ){
                        prdVO.setStatus(ProductStatusEnum.APPLY.getCode());
                    }
                }
                productFacade.saveVO(prdVO, member, false);
                
                prdVO = productFacade.findById(storeId, prdVO.getId(), true, locale);
                return this.genSuccessRepsone(request, prdVO);
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    
    /**
     * 狀態變更
     * /services/products/status/change
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/status/change")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response changePrdStatus(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("changePrdStatus ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || formVO.getId()==null || formVO.getStatus()==null ){
                logger.error("changePrdStatus formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long storeId = admin?formVO.getStoreId():store.getId();
            ProductStatusEnum status = ProductStatusEnum.getFromCode(formVO.getStatus());
            logger.debug("changePrdStatus status = "+status);
            
            ProductVO prdVO = productFacade.findById(storeId, formVO.getId(), false, locale);
            if( prdVO==null ){
                logger.error("changePrdStatus forDealer prdVO = "+prdVO);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            
            ProductStatusEnum statusOri = ProductStatusEnum.getFromCode(prdVO.getStatusOri());
            logger.debug("changePrdStatus statusOri = "+statusOri);
            prdVO.setStatus(formVO.getStatus());
            
            // 台泥匯入商品?
            boolean tccPrd = tccDealer
                            && (formVO.getForDealer()!=null && formVO.getForDealer()) 
                            && (prdVO.getTccPrd()!=null && prdVO.getTccPrd());
            
            if( tccPrd ){// EC1.5 (只有上架 or 下架，無其他狀態)
                // 上架 or 下架
                if( status!=ProductStatusEnum.PUBLISH && status!=ProductStatusEnum.REMOVE ){
                    logger.error("changePrdStatus forDealer status = "+status+", statusOri = "+statusOri);
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, null);
                }
                if( status==ProductStatusEnum.PUBLISH ){
                    prdVO.setPublishTime(new Date());
                }
            }else{// EC2.0
                if( status==null 
                    || !status.isForSeller() // 非提供賣家選擇狀態
                    || (ProductStatusEnum.RESERVED==status && formVO.getPublishTime()==null) // 預約上架
                    || (ProductStatusEnum.RESERVED==status && !statusOri.isCanPublish()) 
                ){
                    logger.error("changePrdStatus status = "+status+", statusOri = "+statusOri);
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, null);
                }
                if( ProductStatusEnum.RESERVED==status ){// 預約上架
                    prdVO.setPublishTime(formVO.getPublishTime());
                }else if( ProductStatusEnum.APPLY == status  ){// 送審
                    prdVO.setApplicant(member.getId());
                    prdVO.setApplyTime(new Date());
                }
            }
            
            productFacade.saveVO(prdVO, member, false);

            prdVO = productFacade.findById(storeId, prdVO.getId(), true, locale);
            return this.genSuccessRepsone(request, prdVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    
    /**
     * 審核通過
     * /services/products/approve
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response approveProduct(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("approveProduct ...");
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
            if( formVO==null || formVO.getPass()==null || formVO.getStoreId()==null || formVO.getId()==null ){
                logger.error("approveProduct formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long storeId = formVO.getStoreId();
            ProductVO prdVO = productFacade.findById(storeId, formVO.getId(), false, locale);
            if( !ProductStatusEnum.APPLY.getCode().equals(prdVO.getStatus()) ){// 申請上架審核
                logger.error("approveProduct status not APPLY !");
                return Response.notAcceptable(null).build();
            }
            
            ProductStatusEnum status = formVO.getPass()?ProductStatusEnum.PASS:ProductStatusEnum.REJECT;
            prdVO.setStatus(status.getCode());
            productFacade.saveVO(prdVO, member, false);
            
            prdVO = productFacade.findById(storeId, prdVO.getId(), true, locale);
            
            return this.genSuccessRepsone(request, prdVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    
    /**
     * 商品上架
     * /services/products/publish
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/publish")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response publishProduct(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("publishProduct ...");
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
            if( formVO==null || formVO.getId()==null ){
                logger.error("publishProduct formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long storeId = admin?formVO.getStoreId():store.getId();
            ProductVO prdVO = productFacade.findById(storeId, formVO.getId(), false, locale);
            if( !ProductStatusEnum.PASS.getCode().equals(prdVO.getStatus()) 
             && !ProductStatusEnum.RESERVED.getCode().equals(prdVO.getStatus()) ){// 審核通過 or 預約上架
                logger.error("publishProduct status not PASS !");
                return Response.notAcceptable(null).build();
            }
            
            prdVO.setStatus(ProductStatusEnum.PUBLISH.getCode());
            productFacade.saveVO(prdVO, member, false);
            
            prdVO = productFacade.findById(storeId, prdVO.getId(), true, locale);
            
            return this.genSuccessRepsone(request, prdVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    
    /**
     * 刪除商品
     * /services/products/remove
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeProduct(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeProduct ...");
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
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcProduct entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 ){
                entity = productFacade.find(formVO.getId());
            }else{
                logger.error("removeProduct entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            if( entity==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }else{
                // 商店ID驗證
                if( !storeId.equals(entity.getStoreId()) ){
                    logger.error("removeProduct storeId error : "+storeId+" != "+entity.getStoreId());
                    return Response.notAcceptable(null).build();
                }
                
                entity.setDisabled(Boolean.TRUE);// 非真實刪除
                productFacade.save(entity, member, false);
                return this.genSuccessRepsoneWithId(request, formVO.getId());
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Multi Select Operation">
    /**
     * 預約上架/取消
     * /services/products/reserve/save
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/reserve/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdReserve(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdReserve ...");
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
            if( formVO==null || formVO.getActive()==null || formVO.getPrdList()==null ){
                logger.error("savePrdReserve formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            int count = 0;
            for(Long prdId : formVO.getPrdList()){
                EcProduct entity = productFacade.find(prdId);
                if( entity.getStoreId()!=null && entity.getStoreId().equals(storeId) ){
                    ProductStatusEnum status = ProductStatusEnum.getFromCode(entity.getStatus());
                    if( formVO.getActive() && formVO.getStartAt()!=null 
                            && status!=null && status.isCanPublish()){// 預約
                        entity.setStatus(ProductStatusEnum.RESERVED.getCode());
                        entity.setPublishTime(formVO.getStartAt());
                        productFacade.save(entity, member, false);
                        count++;
                    }else if( !formVO.getActive() && ProductStatusEnum.RESERVED == status ){// 取消
                        entity.setStatus(ProductStatusEnum.DRAF.getCode());
                        entity.setPublishTime(null);
                        productFacade.save(entity, member, false);
                        count++;
                    }
                }
            }
            
            return this.genSuccessRepsoneWithCount(request, count);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * 送審、下架
     * /services/products/status/save
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/status/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdStatus(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdStatus ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            //if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
            //    return genUnauthorizedResponse();
            //}
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            
            ProductStatusEnum status = ProductStatusEnum.getFromCode(formVO!=null?formVO.getStatus():null);
            if( formVO==null || formVO.getPrdList()==null || status==null ){
                logger.error("savePrdStatus formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long storeId = admin?formVO.getStoreId():store.getId();
            int count = 0;
            
            if( !admin ){// for seller
                for(Long prdId : formVO.getPrdList()){
                    EcProduct entity = productFacade.find(prdId);
                    if( entity!=null && entity.getStoreId()!=null && entity.getStoreId().equals(storeId) ){
                        if( ProductStatusEnum.APPLY == status 
                         || ProductStatusEnum.REMOVE == status ){// 不需額外檢查的狀態
                            entity.setStatus(status.getCode());
                            if( ProductStatusEnum.APPLY == status  ){// 送審
                                entity.setApplicant(member.getId());
                                entity.setApplyTime(new Date());
                            }
                            productFacade.save(entity, member, false);
                            count++;
                        }else if( ProductStatusEnum.PUBLISH == status ){// 上架
                            if( ProductStatusEnum.PASS.getCode().equals(entity.getStatus()) ){// 需已通過審核
                                entity.setStatus(status.getCode());
                                entity.setPublishTime(new Date());
                                productFacade.save(entity, member, false);
                                count++;
                            }
                        }
                    }
                }
            }else{// for admin
                for(Long prdId : formVO.getPrdList()){
                    EcProduct entity = productFacade.find(prdId);
                    if( entity!=null 
                     && ProductStatusEnum.getFromCode(entity.getStatus())==ProductStatusEnum.APPLY // 審核
                     && (status==ProductStatusEnum.PASS || status==ProductStatusEnum.REJECT)// 通過 or 拒絕
                    ){
                        entity.setStatus(status.getCode());
                        productFacade.save(entity, member, false);
                        count++;
                    }
                }
            }
            
            return this.genSuccessRepsoneWithCount(request, count);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Variant Options (Color & Size)">
    /**
     * /services/products/{prdId}/varOptions/{code}
     * @param request
     * @param prdId
     * @param code
     * @param storeId
     * @return
     */
    @GET
    @Path("/{prdId}/varOption/{code}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findPrdVarOption(@Context HttpServletRequest request, 
            @PathParam("prdId")Long prdId, @PathParam("code")String code, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findPrdVarOption ... prdId = "+prdId+", code="+code);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:store.getId();
            List<LongOptionVO> list = prdVarOptionFacade.findVarOptions(storeId, prdId, ProductVariantEnum.getFromCode(code));
            
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 儲存顏色型別選項
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/color/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdColorOption(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        formVO.setType(ProductVariantEnum.COLOR.getCode());
        return savePrdVarOption(request, prdId, formVO);
    }
    
    /**
     * 儲存大小型別選項
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/size/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdSizeOption(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        formVO.setType(ProductVariantEnum.SIZE.getCode());
        return savePrdVarOption(request, prdId, formVO);
    }
    
    /**
     * 刪除顏色型別選項
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/color/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdColorOption(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        formVO.setType(ProductVariantEnum.COLOR.getCode());
        return disablePrdVarOption(request, prdId, formVO);
    }
    
    /**
     * 刪除大小型別選項
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/size/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdSizeOption(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        formVO.setType(ProductVariantEnum.SIZE.getCode());
        return disablePrdVarOption(request, prdId, formVO);
    }
    
    /**
     * 儲存型別選項
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    public Response savePrdVarOption(@Context HttpServletRequest request, Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdVarOption ...");
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
            if( formVO==null || formVO.getType()==null
                    || formVO.getCname()==null || formVO.getCname().trim().isEmpty() ){
                logger.error("savePrdVarOption formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcPrdVarOption entity = new EcPrdVarOption();
            entity.setStoreId(storeId);
            entity.setPrdId(prdId);
            entity.setType(formVO.getType());
            entity.setCname(formVO.getCname().trim());
            entity.setDisabled(Boolean.FALSE);
            //entity.setDisabled(formVO.getDisabled()==null? false:formVO.getDisabled());
            
            if( prdVarOptionFacade.checkInput(entity, member, locale, errors) ){
                prdVarOptionFacade.save(entity, member, false);
                logger.info("savePrdVarOption entity.getId() = "+entity.getId());
                reapplyProcess(prdId, member); // 若有需要重新送審處理 
                return findPrdVarOption(request, prdId, formVO.getType(), storeId);
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    
    /**
     * 刪除型別選項
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    public Response disablePrdVarOption(@Context HttpServletRequest request, Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("disablePrdVarOption ...");
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
            if( formVO==null || formVO.getType()==null || formVO.getId()==null || prdId==null ){
                logger.error("disablePrdVarOption formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcPrdVarOption entity = prdVarOptionFacade.find(formVO.getId());
            if( entity!=null
                    && formVO.getType().equals(entity.getType())
                    && storeId.equals(entity.getStoreId())
                    && prdId.equals(entity.getPrdId()) ){
                
                entity.setDisabled(true);
                prdVarOptionFacade.save(entity, member, false);
                logger.debug("disablePrdVarOption entity.getId() = "+entity.getId());
                
                return findPrdVarOption(request, prdId, formVO.getType(), storeId);
            }else{
                logger.error("disablePrdVarOption invalid id = "+formVO.getId()+", type="+formVO.getType()+", storeId="+storeId+", prdId="+prdId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Variants">
    /**
     * /services/products/{prdId}/varOptions/{code}
     * @param request
     * @param prdId
     * @param storeId
     * @return
     */
    @GET
    @Path("/{prdId}/variants")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findPrdVariants(@Context HttpServletRequest request, 
            @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findPrdVariants ... prdId = "+prdId);
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:store.getId();
            List<PrdVariantVO> list = prdVariantFacade.findByPrd(storeId, prdId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 儲存顏色型別選項
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/variant/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdVariant(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdVariant ...");
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
            
            if( formVO==null ){
                logger.error("savePrdVariant formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            boolean isNew = (formVO.getId()==null || formVO.getId()==0);
            EcPrdVariant entity = null;
            if( isNew ){
                entity = new EcPrdVariant();
                ExtBeanUtils.copyProperties(entity, formVO);
            }else{
                entity = prdVariantFacade.find(formVO.getId());
                if( entity!=null
                        && storeId.equals(entity.getStoreId())
                        && prdId.equals(entity.getPrdId()) ){
                    ExtBeanUtils.copyProperties(entity, formVO);
                }else{
                    logger.error("savePrdVariant invalid id = "+formVO.getId()+", storeId="+storeId+", prdId="+prdId);
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
                }
            }
            
            entity.setStoreId(storeId);
            entity.setPrdId(prdId);
            //entity.setDisabled(formVO.getDisabled()==null? false:formVO.getDisabled());
            entity.setDisabled(Boolean.FALSE);
            if( prdVariantFacade.checkInput(entity, member, locale, errors) ){
                prdVariantFacade.genVariantName(entity, formVO, locale);
                prdVariantFacade.save(entity, member, false);
                logger.debug("savePrdVariant entity.getId() = "+entity.getId());
                reapplyProcess(prdId, member); // 若有需要重新送審處理 
                return findPrdVariants(request, prdId, storeId);
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    
    /**
     * 刪除顏色型別選項
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/variant/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdVariant(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removePrdVariant ...");
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
            
            if( formVO==null || formVO.getId()==null || prdId==null ){
                logger.error("removePrdVariant formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcPrdVariant entity = prdVariantFacade.find(formVO.getId());
            if( entity!=null
                    && storeId.equals(entity.getStoreId())
                    && prdId.equals(entity.getPrdId()) ){
                
                entity.setDisabled(true);
                prdVariantFacade.save(entity, member, false);
                logger.debug("removePrdVariant entity.getId() = "+entity.getId());
                
                return findPrdVariants(request, prdId, storeId);
            }else{
                logger.error("removePrdVariant invalid id = "+formVO.getId()+", type="+formVO.getType()+", storeId="+storeId+", prdId="+prdId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        
        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Product Stock">
    /**
     * /services/products/stock/count
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/stock/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countStockLogs(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countStockLogs ...");
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
            Long storeId = admin?formVO.getStoreId():store.getId();
                       
            BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
            criteriaVO.setStoreId(storeId);
            criteriaVO.setPrdId(prdId);
            
            int totalRows = stockLogFacade.countByCriteria(criteriaVO);
            logger.debug("countStockLogs totalRows = "+totalRows);
            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/stock/logs
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return
     */
    @POST
    @Path("/{prdId}/stock/logs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findStockLogs(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findStockLogs offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
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
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            BaseCriteriaVO criteriaVO = new ProductCriteriaVO();
            criteriaVO.setStoreId(storeId);
            criteriaVO.setPrdId(prdId);
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            //criteriaVO.setOrderBy(sortFieldMap.get(sortField), sortOrderMap.get(sortOrder));
            List<StockLogVO> list = stockLogFacade.findByCriteria(criteriaVO, locale);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/{prdId}/stock/save
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/stock/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveStockLog(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveStockLog ... prdId = "+prdId);
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
            
            if( formVO==null || prdId==null || formVO.getType()==null || formVO.getQuantity()==null ){
                logger.error("saveStockLog formVO==null");
                return Response.notAcceptable(null).build();
            }
            StockEnum stockEnum = StockEnum.getFromCode(formVO.getType());
            if( formVO.getType()==null || stockEnum==null || formVO.getQuantity()==null ){
                logger.error("saveStockLog formVO.getType()==null || formVO.getQuantity()==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, null);
            }
            
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcStockLog entity = new EcStockLog();
            entity.setType(formVO.getType());
            entity.setQuantity(stockEnum.isPostive()?formVO.getQuantity():BigDecimal.valueOf(formVO.getQuantity().doubleValue()*-1));
            entity.setMemo(formVO.getMemo());
            entity.setClosed(false);
            entity.setDataTime(new Date());
            entity.setDisabled(false);
            entity.setPrdId(prdId);
            entity.setStoreId(storeId);
            
            if( stockLogFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                stockLogFacade.save(entity, member, false);
                logger.info("saveStockLog save {}", entity.getId());
                ProductVO prdVO = productFacade.findById(storeId, prdId, true, locale);
                return this.genSuccessRepsone(request, prdVO);
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Introductions">
    /**
     * /services/products/{prdId}/intro
     * @param request
     * @param prdId
     * @param storeId
     * @return
     */
    @GET
    @Path("/{prdId}/intro")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findPrdIntros(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findPrdIntros ... prdId = "+prdId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:store.getId();
            List<PrdIntroVO> list = prdIntroFacade.findByPrd(storeId, prdId);
            List<String> textList = new ArrayList<String>();
            for(PrdIntroVO vo : list){
                textList.add(vo.getText());
            }
            
            BaseListResponseVO resVO = genSuccessListRepsone(request, textList);
            
            return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/{prdId}/intro/save
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/intro/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdIntros(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdIntros ... prdId = "+prdId);
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
            if( formVO==null || prdId==null || formVO.getIntroductions()==null ){
                logger.error("savePrdIntros formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            // 先移除
            prdIntroFacade.batchRemove(storeId, prdId, null, false);
            // 後新增
            Integer sortnum = 1;
            for(String text : formVO.getIntroductions()){
                EcPrdIntro entity = new EcPrdIntro();
                entity.setStoreId(storeId);
                entity.setPrdId(prdId);
                entity.setSortnum(sortnum++);
                entity.setText(text);
                
                if( prdIntroFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                    prdIntroFacade.save(entity, member, false);
                    reapplyProcess(prdId, member); // 若有需要重新送審處理 
                }else{
                    return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
            }
            
            return findPrdIntros(request, prdId, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Pictures">
    /**
     * http://localhost:8080/ec-seller-serv/products/{prdId}/picture/list
     * 產品照片列表
     * @param request
     * @param prdId
     * @param storeId
     * @return
     */
    @Path("/{prdId}/picture/list")
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findPrdPicture(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findPrdPicture ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);

            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:store.getId();
            
            List<FileVO> list = fileFacade.findByPrimary(storeId, FileEnum.PRD_PIC.getPrimaryType(), prdId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return Response.ok(this.genBaseFailRepsone(request, ResStatusEnum.FAIL), MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * 上傳產品照片
     * @param request
     * @param prdId
     * @param storeId
     * @param multiPart
     * @return
     */
    @Path("/{prdId}/picture/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadPrdPicture(@Context HttpServletRequest request, 
            @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadPrdPicture ...");
        FileEnum fileEnum = FileEnum.PRD_PIC;
        String root = fileEnum.getRootDir();
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
            storeId = admin?storeId:store.getId();
            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            if( bodyParts==null || bodyParts.isEmpty() ){
                logger.error("uploadPrdPicture error bodyParts.isEmpty()");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            //String fileName = this.getMultiPartValue(multiPart, "filename");
            //logger.info("uploadPrdPicture fileName = "+fileName);
            //String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");
            
            byte[] content  = null;
            List<Long> idList = new ArrayList<Long>();
            for (int i = 0; i < bodyParts.size(); i++) {
                String fileName = bodyParts.get(i).getContentDisposition().getFileName();
                fileName = URLDecoder.decode(fileName, "UTF-8");// for jquery.fileupload.js
                logger.info("uploadPrdPicture fileName = "+fileName);
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                String fileContentType = bodyParts.get(i).getContentDisposition().getType();
                logger.debug("uploadPrdPicture file ContentType = " + fileContentType);
                content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                logger.debug("uploadPrdPicture content size = "+((content!=null)?content.length:0));
                // 支援多檔
                if( fileName!=null && content!=null ){
                    // 儲存實體檔案
                    FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, true, storeId);
                    logger.info("uploadPrdPicture write real file finish.");
                    /*
                    String[] fs = FileUtils.getFileExtension(fileName);
                    String name = fs[0];
                    String fext = fs[1];
                    
                    // 儲存實體檔案
                    String dir = root + store.getId();// 依店家區分路徑
                    File dirfile = new File(dir);
                    dirfile.mkdirs(); //for several levels, without the "s" for one level
                    String saveFileName = UUID.randomUUID().toString() + "." +fext;
                    String saveFileNameFull = dir + GlobalConstant.FILE_SEPARATOR + saveFileName;
                    File file = new File(saveFileNameFull);
                    FileUtils.writeByteArrayToFile(file, content);
                    logger.info("uploadPrdPicture saveFileName = "+saveFileName);
                    
                    // 縮圖
                    ImageVO retImgVO = new ImageVO();
                    // 取得Image寬高
                    String errMsg1 = img.getImageInfo(saveFileNameFull, retImgVO);
                    if( errMsg1!=null ){
                        logger.error("uploadPrdPicture getImageInfo ... "+errMsg1);
                    }else{
                        // 縮圖 (縮圖需寬高資訊)
                        String errMsg2 = img.compressImageFile(dir, saveFileName, saveFileNameFull, true, retImgVO);
                        if( errMsg2!=null ){
                            logger.error("uploadPrdPicture compressImageFile ... "+errMsg1);
                        }
                    }
                    logger.info("uploadPrdPicture compress image finish.");
                    */
                    // Save EcFile
                    EcFile fileEntity = new EcFile();
                    fileEntity.setStoreId(store.getId());
                    fileEntity.setPrimaryType(fileEnum.getPrimaryType());
                    fileEntity.setPrimaryId(prdId);
                    fileEntity.setDescription(fileName);// 預設檔名，後續可修改
                    //fileEntity.setFilename(fileName);
                    //fileEntity.setName(name);
                    //fileEntity.setSavedir(dir);
                    //fileEntity.setSavename(saveFileName);
                    //fileEntity.setFileSize(content.length);
                    fileEntity.setContentType(fileContentType);

                    fileEntity.setFilename(fileVO.getFilename());
                    fileEntity.setName(fileVO.getName());
                    fileEntity.setSavedir(fileVO.getSavedir());
                    fileEntity.setSavename(fileVO.getSavename());
                    fileEntity.setFileSize(fileVO.getFileSize());

                    if( fileFacade.checkInput(fileEntity, member, locale, errors) ){// 輸入檢查
                        fileFacade.save(fileEntity, member, false);
                        idList.add(fileEntity.getId());
                    }else{
                        return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                    }
                }
            }
            
            // 封面圖處理
            productFacade.autoSetCoverPicture(storeId, prdId);
            //resetPrdCoverPic(storeId, prdId, null, member);
            reapplyProcess(prdId, member); // 若有需要重新送審處理 
            
            List<FileVO> list = fileFacade.findByIds(store.getId(), FileEnum.PRD_PIC.getPrimaryType(), prdId, idList);
            // 需傳回 jQuery File Upload PlugIn 的接收格式
            //BaseListResponseVO res = this.genSuccessListRepsone(request, list);
            String urlPrefix = sys.getRestUrlPrefix();//WebUtils.getUrlPrefix(request);
            if( list!=null ){
                List<UploadResponseVO> pictures = new ArrayList<UploadResponseVO>();
                for(FileVO fileVO : list){
                    UploadResponseVO vo = new UploadResponseVO();
                    vo.setName(fileVO.getFilename());
                    vo.setSize(fileVO.getFileSize());
                    vo.setUrl(urlPrefix + fileVO.getUrl());
                    vo.setThumbnailUrl(urlPrefix + fileVO.getUrl());// 預覽圖片網址
                    vo.setDeleteUrl(urlPrefix + "/services/products/" + fileVO.getPrimaryId() + "/picture/delete/"+fileVO.getId());
                    vo.setDeleteType("GET");
                    
                    pictures.add(vo);
                }
                UploadResponseListVO res = new UploadResponseListVO();
                res.setFiles(pictures);
                return Response.ok(res, MediaType.APPLICATION_JSON).build();
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return Response.ok(this.genBaseFailRepsone(request, ResStatusEnum.FAIL), MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * /services/products/{prdId}/picture/save
     * @param request
     * @param formVO
     * @param prdId
     * @return
     */
    @POST
    @Path("/{prdId}/picture/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdPicDesc(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdPicDesc ...");
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
            
            if( formVO==null || prdId==null || formVO.getId()==null ){
                logger.error("savePrdPicDesc formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcProduct prdEntity = productFacade.find(prdId);
            EcFile entity = fileFacade.find(formVO.getId());
            if( entity!=null && prdEntity!=null ){
                logger.info("savePrdPicDesc EcFile : "+formVO.getId());
                entity.setDescription(formVO.getDescription());
                
                if( fileFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                    fileFacade.save(entity, member, false);
                    // 封面圖處理
                    if( formVO.getCoverPicId()!=null && !formVO.getCoverPicId().equals(prdEntity.getCoverPicId()) ){
                        logger.info("savePrdPicDesc set CoverPicId prdId="+prdId+", coverPicId="+formVO.getCoverPicId());
                        prdEntity.setCoverPicId(entity.getId());
                        productFacade.save(prdEntity, member, false);
                    }
                    productFacade.autoSetCoverPicture(storeId, prdId);
                    //resetPrdCoverPic(storeId, prdId, null, member);
                    reapplyProcess(prdId, member); // 若有需要重新送審處理 
                }else{
                    return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
            }else{
                logger.error("savePrdPicDesc invalid id = "+formVO.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            
            return findPrdPicture(request, prdId, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/{prdId}/picture/remove
     * @param request
     * @param formVO
     * @param prdId
     * @return
     */
    @POST
    @Path("/{prdId}/picture/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdPic(@Context HttpServletRequest request, SubmitVO formVO, @PathParam("prdId")Long prdId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removePrdPic ...");
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
            if( formVO==null || prdId==null || formVO.getId()==null ){
                logger.error("removePrdPic formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcFile entity = fileFacade.find(formVO.getId());
            if( entity!=null ){
                logger.info("removePrdPic "+entity);
                // 不刪實體檔，避免有共用狀況(排程刪除即可)
                fileFacade.remove(entity, false);
                // 封面圖處理
                productFacade.autoSetCoverPicture(storeId, prdId);
                //resetPrdCoverPic(storeId, prdId, formVO.getId(), member);
            }else{
                logger.error("removePrdPic invalid id = "+formVO.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            
            return findPrdPicture(request, prdId, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * for 前端 Thumbnail 元件使用
     * @param request
     * @param prdId
     * @param id
     * @param storeId
     * @return 
     */
    @GET
    @Path("/{prdId}/picture/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdPic(@Context HttpServletRequest request, 
            @PathParam("prdId")Long prdId, @PathParam("id")Long id, @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        SubmitVO formVO = new SubmitVO();
        formVO.setId(id);
        formVO.setStoreId(storeId);
        return removePrdPic(request, formVO, prdId);
    }
    
    /**
     * 封面圖處理
     * @param storeId
     * @param prdId
     * @param deleteId
     * @param operator 
     */
    public void resetPrdCoverPic(Long storeId, Long prdId, Long deleteId, EcMember operator){
        EcProduct prdEntity = productFacade.find(prdId);
        if( prdEntity!=null && (deleteId==null || deleteId.equals(prdEntity.getCoverPicId())) ){
            // 依上傳順序
            FileVO fileVO = fileFacade.findSingleByPrimary(storeId, FileEnum.PRD_PIC.getPrimaryType(), prdId);
            if( fileVO!=null ){
                if( prdEntity.getCoverPicId()==null // 未設定
                 || prdEntity.getCoverPicId().equals(deleteId) ){// 正好刪除
                    prdEntity.setCoverPicId(fileVO.getId());
                    productFacade.save(prdEntity, operator, false);
                    logger.info("resetPrdCoverPic EcProduct : "+prdId);
                }
            }else{
                if( prdEntity.getCoverPicId()!=null ){// 正好全刪除
                    prdEntity.setCoverPicId(null);
                    productFacade.save(prdEntity, operator, false);
                    logger.info("resetPrdCoverPic EcProduct : "+prdId);
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Shippings">
    /**
     * /services/products/{prdId}/shippings
     * @param request
     * @param prdId
     * @param storeId
     * @return
     */
    @GET
    @Path("/{prdId}/shippings")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findPrdShippings(@Context HttpServletRequest request, 
            @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findPrdShippings ... prdId = "+prdId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }

            storeId = admin?storeId:store.getId();

            List<PrdShippingVO> list = prdShippingFacade.findByPrd(storeId, prdId);
            List<LongOptionVO> resList = new ArrayList<LongOptionVO>();
            for(PrdShippingVO vo : list){
                resList.add(new LongOptionVO(vo.getShipId(), vo.getTitle()));
            }
            
            return this.genSuccessRepsoneWithList(request, resList);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/{prdId}/intro/save
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/shipping/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdShippings(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdShippings ...");
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
            
            if( formVO==null || prdId==null || formVO.getShippings()==null ){
                logger.error("savePrdShippings formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            // 先移除
            prdShippingFacade.batchRemove(storeId, prdId, null, false);
            // 後新增
            //Integer sortnum = 1;
            for(LongOptionVO vo : formVO.getShippings()){
                EcPrdShipping entity = new EcPrdShipping();
                entity.setStoreId(storeId);
                entity.setPrdId(prdId);
                //entity.setSortnum(sortnum++);
                entity.setShipId(vo.getValue());
                if( prdShippingFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                    prdShippingFacade.save(entity, member, false);
                    reapplyProcess(prdId, member); // 若有需要重新送審處理 
                }else{
                    return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
            }

            return findPrdShippings(request, prdId, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Payments">
    /**
     * /services/products/{prdId}/payments
     * @param request
     * @param prdId
     * @param storeId
     * @return
     */
    @GET
    @Path("/{prdId}/payments")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findPrdPayments(@Context HttpServletRequest request, 
            @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findPrdPayments ... prdId = "+prdId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:store.getId();

            List<PrdPaymentVO> list = prdPaymentFacade.findByPrd(storeId, prdId);
            List<LongOptionVO> resList = new ArrayList<LongOptionVO>();
            for(PrdPaymentVO vo : list){
                resList.add(new LongOptionVO(vo.getPayId(), vo.getTitle()));
            }
            
            return this.genSuccessRepsoneWithList(request, resList);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/{prdId}/intro/save
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @POST
    @Path("/{prdId}/payment/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdPayments(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdPayments ...");
        EcMember member = getReqUser(request);
        EcStore store = getStore(request);
        Locale locale = getLocale(request);
        List<String> errors = new ArrayList<String>();
        try{
            logInputs("savePrdPayments", formVO, member);// log 輸入資訊
            
            if( formVO==null || prdId==null || formVO.getPayments()==null ){
                logger.error("savePrdPayments formVO==null");
                return Response.notAcceptable(null).build();
            }
            boolean admin = hasAdminRights(request, member);
            // 賣家專屬 RESTful
            if( !checkPermissions(methodName, member, store, false, true, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            // 先移除
            prdPaymentFacade.batchRemove(storeId, prdId, null, false);
            // 後新增
            //Integer sortnum = 1;
            for(LongOptionVO vo : formVO.getPayments()){
                EcPrdPayment entity = new EcPrdPayment();
                entity.setStoreId(storeId);
                entity.setPrdId(prdId);
                //entity.setSortnum(sortnum++);
                entity.setPayId(vo.getValue());
                if( prdPaymentFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                    prdPaymentFacade.save(entity, member, false);
                    reapplyProcess(prdId, member); // 若有需要重新送審處理 
                }else{
                    return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
            }
            
            return findPrdPayments(request, prdId, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Details">
    /**
     *
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @Path("/{prdId}/detail/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdDetail(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("savePrdDetail ... prdId = "+prdId);
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

            Long storeId = admin?formVO.getStoreId():store.getId();
            
            Long sid = formVO.getId();
            EcPrdDetail entity = (sid!=null && sid>0)? prdDetailFacade.find(sid):new EcPrdDetail();
            // check storeId, prdId
            if( entity==null 
                || (entity.getStoreId()!=null && !entity.getStoreId().equals(storeId)) 
                || (entity.getPrdId()!=null && !entity.getPrdId().equals(prdId))  ){
                logger.error("savePrdDetail sid = "+sid);
                return genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            entity.setContentTxt(formVO.getContentTxt());
            entity.setContentType(formVO.getContentType());
            entity.setStoreId(storeId);
            entity.setPrdId(prdId);
            
            if( prdDetailFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                if( entity.getId()==null || entity.getId()<=0 ){// 新增
                    Integer sortnum = formVO.getSortnum()==null? 1:formVO.getSortnum();
                    entity.setSortnum(sortnum);
                    // 先變更排序
                    if( formVO.getSortnum()!=null ){
                        prdDetailFacade.updateSortnum(storeId, prdId, sortnum, true, member, false);
                    }
                }
                prdDetailFacade.save(entity, member, false);
                reapplyProcess(prdId, member); // 若有需要重新送審處理 
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            
            List<PrdDetailVO> list = prdDetailFacade.findByPrd(storeId, prdId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     *
     * @param request
     * @param prdId
     * @param multiPart
     * @return
     */
    @Path("/{prdId}/detail/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadPrdDetailFiles(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadPrdDetailFiles ...");
        FileEnum fileEnum = FileEnum.PRD_DETAIL;
        String root = fileEnum.getRootDir();
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
            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            if( bodyParts==null || bodyParts.isEmpty() ){
                logger.error("uploadPrdDetailFiles error bodyParts.isEmpty()");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            String fileName = this.getMultiPartValue(multiPart, "filename");
            logger.info("uploadPrdDetailFiles fileName = "+fileName);
            String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");
            Long storeId = this.getMultiPartLong(multiPart, "storeId");
            storeId = admin?storeId:store.getId();
            logger.info("uploadPrdDetailFiles storeId = "+storeId);
            
            byte[] content  = null;
            for (int i = 0; i < bodyParts.size(); i++) {
                //fileName = bodyParts.get(i).getContentDisposition().getFileName();
                //logger.info("uploadPrdDetailFiles fileName = "+fileName);
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                fileContentType = (fileContentType==null)?bodyParts.get(i).getContentDisposition().getType():fileContentType;
                //logger.debug("uploadPrdDetailFiles file ContentType = " + fileContentType);
                content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                logger.debug("uploadPrdDetailFiles content size = "+((content!=null)?content.length:0));
                break; // 暫時段落，只支援一個圖檔
            }
            
            if( fileName==null || content==null ){
                logger.error("uploadPrdDetailFiles error fileName==null || content==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            // 其他欄位
            String contentType = getMultiPartValue(multiPart, "contentType");
            String contentTxt = getMultiPartValue(multiPart, "contentTxt");
            String idStr = getMultiPartValue(multiPart, "id");
            Long id = (idStr!=null)?Long.parseLong(idStr):null;
            String sortnumStr = getMultiPartValue(multiPart, "sortnum");
            Integer sortnum = (sortnumStr!=null)?Integer.parseInt(sortnumStr):null;
            
            // 儲存實體檔案
            FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, true, storeId);
            logger.info("uploadPrdDetailFiles write real file finish.");
            /*
            // 取附檔名
            String[] fs = FileUtils.getFileExtension(fileName);
            String name = fs[0];
            String fext = fs[1];
            
            // 儲存實體檔案
            String dir = root + store.getId();
            File dirfile = new File(dir);
            dirfile.mkdirs(); //for several levels, without the "s" for one level
            String saveFileName = UUID.randomUUID().toString() + "." +fext;
            String saveFileNameFull = dir + GlobalConstant.FILE_SEPARATOR + saveFileName;
            File file = new File(saveFileNameFull);
            FileUtils.writeByteArrayToFile(file, content);
            logger.info("uploadPrdDetailFiles saveFileName = "+saveFileName);
            
            // 縮圖
            ImageVO retImgVO = new ImageVO();
            // 取得Image寬高
            String errMsg1 = img.getImageInfo(saveFileNameFull, retImgVO);
            if( errMsg1!=null ){
                logger.error("uploadPrdDetailFiles getImageInfo ... "+errMsg1);
            }else{
                // 縮圖 (縮圖需寬高資訊)
                String errMsg2 = img.compressImageFile(dir, saveFileName, saveFileNameFull, true, retImgVO);
                if( errMsg2!=null ){
                    logger.error("uploadPrdDetailFiles compressImageFile ... "+errMsg1);
                }
            }
            logger.info("uploadPrdDetailFiles compress image finish.");
            */
            
            // Save EcFile
            EcFile fileEntity = new EcFile();
            fileEntity.setStoreId(store.getId());
            fileEntity.setPrimaryType(fileEnum.getPrimaryType());
            fileEntity.setPrimaryId(id);// 此處可能為 null 或 0，後續再 update
            fileEntity.setDescription(contentTxt);
            //fileEntity.setFilename(fileName);
            //fileEntity.setName(name);
            //fileEntity.setSavedir(dir);
            //fileEntity.setSavename(saveFileName);
            //fileEntity.setFileSize(content.length);
            fileEntity.setContentType(fileContentType);
            
            fileEntity.setFilename(fileVO.getFilename());
            fileEntity.setName(fileVO.getName());
            fileEntity.setSavedir(fileVO.getSavedir());
            fileEntity.setSavename(fileVO.getSavename());
            fileEntity.setFileSize(fileVO.getFileSize());
            
            if( fileFacade.checkInput(fileEntity, member, locale, errors) ){// 輸入檢查
                fileFacade.save(fileEntity, member, false);
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            
            // Save EcPrdDetail
            EcPrdDetail entity = (id!=null && id>0)? prdDetailFacade.find(id):new EcPrdDetail();
            entity.setContentType(contentType);
            entity.setContentTxt(contentTxt);
            entity.setContentImg(fileEntity.getId());
            entity.setStoreId(store.getId());
            entity.setPrdId(prdId);
            if( entity.getId()==null || entity.getId()<=0 ){
                entity.setSortnum(sortnum);
                // 先變更排序
                if( sortnum!=null ){
                    prdDetailFacade.updateSortnum(store.getId(), prdId, sortnum, true, member, false);
                }
            }
            // 再儲存
            if( prdDetailFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                prdDetailFacade.save(entity, member, false);
            }else{
                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            
            // 更新 EcFile.PrimaryId
            fileEntity.setPrimaryId(entity.getId());
            fileFacade.edit(fileEntity, false);
            reapplyProcess(prdId, member); // 若有需要重新送審處理 
            
            List<PrdDetailVO> list = prdDetailFacade.findByPrd(store.getId(), prdId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     *
     * @param request
     * @param prdId
     * @param formVO
     * @return
     */
    @Path("/{prdId}/detail/remove")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdDetail(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("savePrdDetail ...");
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
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            Long sid = formVO.getId();
            EcPrdDetail entity = (sid!=null && sid>0)? prdDetailFacade.find(sid):new EcPrdDetail();
            // TODO check storeId, prdId
            logger.debug("savePrdDetail entity = "+entity);
            // 先變更排序
            Integer sortnum = entity.getSortnum();
            if( sortnum!=null ){
                prdDetailFacade.updateSortnum(storeId, prdId, sortnum, false, member, false);
            }
            // 再移除
            prdDetailFacade.remove(entity, false);
            
            List<PrdDetailVO> list = prdDetailFacade.findByPrd(storeId, prdId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Attrs">
    /**
     * /services/products/{prdId}/attrs
     * @param request
     * @param prdId
     * @param storeId
     * @return
     */
    @GET
    @Path("/{prdId}/attrs")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findPrdAttrs(@Context HttpServletRequest request, 
            @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findPrdAttrs ... prdId = "+prdId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            storeId = admin?storeId:store.getId();
            
            List<PrdAttrValVO> list = prdAttrValFacade.findByPrd(storeId, prdId);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/{prdId}/attr/save
     * @param request
     * @param formVO
     * @param prdId
     * @return
     */
    @POST
    @Path("/{prdId}/attr/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response savePrdAttr(@Context HttpServletRequest request, SubmitVO formVO, @PathParam("prdId")Long prdId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("savePrdAttr ...");
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
            
            if( formVO==null || prdId==null || formVO.getAttrVals()==null ){
                logger.error("savePrdAttr formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            int sortnum = 0;
            for(PrdAttrValVO vo : formVO.getAttrVals()){
                if( vo.isClientModified() ){
                    sortnum++;
                    EcPrdAttrVal entity = vo.getId()!=null? prdAttrValFacade.find(vo.getId()):new EcPrdAttrVal();
                    if( entity!=null ){
                        if( "T".equals(vo.getAttrScope()) ){
                            entity.setAttrId(vo.getAttrId());
                        }else{
                            // 商品專屬屬性。需先新增 EC_PRD_ATTR
                            EcPrdAttr entityAttr = new EcPrdAttr();
                            entityAttr.setCname(vo.getAttrName());
                            entityAttr.setDataType("S");
                            entityAttr.setDisabled(Boolean.FALSE);
                            entityAttr.setPrdId(prdId);
                            entityAttr.setStoreId(storeId);
                            entityAttr.setSortnum(sortnum);
                            
                            if( prdAttrFacade.checkInput(entityAttr, member, locale, errors) ){// 輸入檢查
                                prdAttrFacade.create(entityAttr, false);
                            }else{
                                return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                            }
                            logger.info("savePrdAttr new PrdAttr = "+entityAttr.getId());
                            
                            entity.setAttrId(entityAttr.getId());
                        }
                        entity.setSortnum(sortnum);
                        entity.setAttrValue(vo.getAttrValue());
                        entity.setStoreId(storeId);
                        entity.setPrdId(prdId);
                        if( prdAttrValFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                            prdAttrValFacade.save(entity, member, false);
                        }else{
                            return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        }
                        logger.info("savePrdAttr save PrdAttrVal = "+entity.getId());
                    }
                }
            }
            reapplyProcess(prdId, member); // 若有需要重新送審處理 
            
            return findPrdAttrs(request, prdId, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/{prdId}/attr/remove
     * @param request
     * @param formVO
     * @param prdId
     * @return
     */
    @POST
    @Path("/{prdId}/attr/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removePrdAttr(@Context HttpServletRequest request, SubmitVO formVO, @PathParam("prdId")Long prdId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removePrdAttr ...");
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

            Long storeId = admin?formVO.getStoreId():store.getId();
            PrdAttrValVO vo = null;
            if( formVO.getId()!=null && formVO.getId()>0 ){
                vo = prdAttrValFacade.findById(storeId, prdId, formVO.getId());
            }else{
                logger.error("removePrdAttr entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            
            if( vo==null ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }else{
                EcPrdAttrVal entity = prdAttrValFacade.find(vo.getId());
                // 商店ID驗證
                //if( !store.getId().equals(vo.getStoreId()) ){
                //    logger.error("removePrdAttr storeId error : "+store.getId()+" != "+entity.getStoreId());
                //    return Response.notAcceptable(null).build();
                //}
                if( entity!=null ){
                    //entity.setDisabled(Boolean.TRUE);// 非真實刪除
                    //prdAttrFacade.save(entity, member, false);
                    prdAttrValFacade.remove(entity, false);// 真實刪除
                    if( "T".equals(vo.getAttrScope()) ){
                        EcPrdAttr prdAttrEntity = prdAttrFacade.find(vo.getAttrId());
                        prdAttrFacade.remove(prdAttrEntity, false);// 真實刪除
                    }
                }else{
                    logger.error("removePrdAttr invalid id = "+formVO.getId());
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
                }
                
                return this.genSuccessRepsoneWithId(request, formVO.getId());
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Product Import">
    /**
     * 上傳匯入檔
     * @param request
     * @param multiPart
     * @return
     */
    @Path("/import/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadImport(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadImport ...");
        String root = GlobalConstant.DIR_IMPORT;
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
            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            if( bodyParts==null || bodyParts.isEmpty() ){
                logger.error("uploadImport error bodyParts.isEmpty()");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            String fileName = this.getMultiPartValue(multiPart, "filename");
            logger.info("uploadImport fileName = "+fileName);
            String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");
            
            byte[] content  = null;
            for (int i = 0; i < bodyParts.size(); i++) {
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                logger.debug("uploadImport content size = "+((content!=null)?content.length:0));
                break; // 只支援一個檔
            }
            
            if( fileName==null || content==null ){
                logger.error("uploadImport error fileName==null || content==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            FileEnum fileEnum = FileEnum.IMP_PRD;
            // 儲存實體檔案
            FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, false, store.getId());
            logger.info("uploadImport write real file finish.");
            /*
            // 取附檔名
            String[] fs = FileUtils.getFileExtension(fileName);
            //String name = fs[0];
            String fext = fs[1];
            
            // 儲存實體檔案
            String dir = root + store.getId();
            File dirfile = new File(dir);
            dirfile.mkdirs(); //for several levels, without the "s" for one level
            String saveFileName = UUID.randomUUID().toString() + "." +fext;
            String saveFileNameFull = dir + GlobalConstant.FILE_SEPARATOR + saveFileName;
            File file = new File(saveFileNameFull);
            FileUtils.writeByteArrayToFile(file, content);
            logger.info("uploadImport saveFileName = "+saveFileName);
            */
            // 資料匯入處理
            boolean autoAddClass = false;
            ImportResultVO resVO = new ImportResultVO();
            productFacade.importProducts(store.getId(), fileVO.getSaveFileNameFull(), fileContentType, autoAddClass, locale, resVO);
            
            return this.genSuccessRepsone(request, resVO);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/import/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveImport(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveImport ...");
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
            
            if( formVO==null || formVO.getImportPrdList()==null ){
                logger.error("saveImport formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            for(ImportProductVO vo : formVO.getImportPrdList()){
                ProductVO prdVO = new ProductVO();
                ExtBeanUtils.copyProperties(prdVO, vo);
                prdVO.setDisabled(Boolean.FALSE);
                prdVO.setStatus(ProductStatusEnum.DRAF.getCode());// 預設
                prdVO.setStoreId(store.getId());
                
                productFacade.saveVO(prdVO, member, false);
                logger.info("saveImport ... "+prdVO.getId());
                vo.setId(prdVO.getId());

                // EC_STOCK_LOG 初始庫存
                EcStockLog entity = new EcStockLog();
                entity.setType(StockEnum.FIRST.getCode());
                entity.setQuantity(prdVO.getStock());
                entity.setMemo(StockEnum.FIRST.getDisplayName(locale));
                entity.setClosed(false);
                entity.setDataTime(new Date());
                entity.setDisabled(false);
                entity.setPrdId(prdVO.getId());
                entity.setStoreId(store.getId());
                stockLogFacade.save(entity, member, false);
                logger.error("saveImport saveStockLog save {}", entity.getId());
            }
            
            return this.genSuccessRepsoneWithList(request, formVO.getImportPrdList());
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for TCC Product">
    /**
     * TCC 商品查詢 - 先抓總筆數
     * /services/products/count
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/tcc/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response countTccProducts(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("countTccProducts ...");
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
            Long storeId = admin?formVO.getStoreId():store.getId();
                       
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(storeId);
            
            int totalRows = tccProductFacade.countTccByCriteria(criteriaVO);
            logger.debug("countTccProducts totalRows = "+totalRows);
            return this.genSuccessRepsoneWithCount(request, totalRows);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/products/tcc/list
     * @param request
     * @param formVO
     * @param offset
     * @param rows
     * @param sortField
     * @param sortOrder
     * @return
     */
    @POST
    @Path("/tcc/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response findTccProducts(@Context HttpServletRequest request, SubmitVO formVO,
            @QueryParam("offset")Integer offset, @QueryParam("rows")Integer rows,
            @QueryParam("sortField")String sortField, @QueryParam("sortOrder")String sortOrder
    ){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("findTccProducts offset = "+offset+", rows = "+rows+", sortField = "+sortField+", sortOrder = "+sortOrder);
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
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
            ExtBeanUtils.copyProperties(criteriaVO, formVO);
            criteriaVO.setStoreId(storeId);
            
            criteriaVO.setFirstResult(offset);
            criteriaVO.setMaxResults(rows);
            criteriaVO.setOrderBy(sortTccFieldMap.get(sortField), sortTccFieldMap.get(sortOrder));
            List<TccProductVO> list = tccProductFacade.findTccByCriteria(criteriaVO, locale);
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * 商家匯入台泥商品
     * 
     * /services/products/tcc/import
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/tcc/import")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response importTccProducts(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("importTccProducts ...");
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
            if( formVO==null || formVO.getTccPrdList()==null ){
                logger.error("importTccProducts formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            int count = 0;
            // 匯入台泥經銷商時，為各經銷商的商店預設建立的台泥專用屬性
            PrdTypeVO typeVO = prdTypeFacade.findTccPrdType();
            OptionVO unit = optionFacade.findByCode(storeId, OptionEnum.PRD_UNIT, ProductUnitEnum.METRIC_TON.getCode());
            OptionVO brand = optionFacade.findByCode(storeId, OptionEnum.PRD_BRAND, BrandEnum.TCC.getCode());
            VendorVO vendor = vendorFacade.findByCode(storeId, VendorEnum.TCC.getCode(), false);
            
            if( typeVO==null ){// 類別必要(手機分類查詢才能顯示)
                logger.error("importTccProducts typeVO==null !!!");
                return this.genFailRepsone(request);
            }
            for(TccProductVO vo : formVO.getTccPrdList()){
                // 商品代碼不存在則加入
                if( vo!=null && vo.getCode()!=null && !productFacade.existsTccProduct(storeId, vo) ){
                    vo.setTypeId(typeVO.getId());
                    vo.setPriceUnit(unit!=null?unit.getId():null);
                    vo.setBrandId(brand!=null?brand.getId():null);
                    vo.setVendorId(vendor!=null?vendor.getVendorId():null);
                    productFacade.createTccProduct(storeId, vo, member);
                    count++;
                }
            }
            
            return this.genSuccessRepsoneWithCount(request, count);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * 新增 TCC 商品
     * @param request
     * @param formVO
     * @return 
     */
    @Path("/tcc/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveTccProduct(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("saveTccProduct ...");
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
            
            if( formVO==null || formVO.getCode()==null || formVO.getName()==null ){
                logger.error("saveTccProduct error formVO==null || formVO.getCode()==null || formVO.getName()==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            Long id = formVO.getId();
            String code = formVO.getCode().trim();
            String name = formVO.getName().trim();
            
            // 輸入檢查
            if( StringUtils.isBlank(code) || StringUtils.isBlank(name) ){
                logger.error("saveTccProduct error IN_ERROR_EMPTY");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, errors);
            }
            EcTccProduct entityC = tccProductFacade.findByCode(code, true);
            if( entityC!=null && !entityC.getId().equals(id) ){
                // [商品編號]已存在!
                errors.add(getResourceMsg(locale, "prd.code.exists"));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EXIST, errors);
            }
            EcTccProduct entityN = tccProductFacade.findByName(name, true);
            if( entityN!=null && !entityN.getId().equals(id) ){
                // [商品名稱]已存在!
                errors.add(getResourceMsg(locale, "prd.name.exists"));
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EXIST, errors);
            }
            
            EcTccProduct entity = (id!=null)?tccProductFacade.find(id):new EcTccProduct();
            if( entity==null ){
                // ID 有誤
                logger.error("saveTccProduct error entity==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            entity.setCode(code);
            entity.setName(name);
            entity.setActive(true);
            // 儲存
            tccProductFacade.save(entity, member, false);
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * active TCC 商品
     * @param request
     * @param formVO
     * @return 
     */
    @Path("/tcc/active")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response activeTccProduct(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("activeTccProduct ...");
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
            
            if( formVO==null || formVO.getId()==null || formVO.getActive()==null ){
                logger.error("activeTccProduct formVO==null || formVO.getId()==null || formVO.getActive()==null ");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            EcTccProduct entity = tccProductFacade.find(formVO.getId());
            if( entity==null ){
                logger.error("activeTccProduct entity==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            entity.setActive(formVO.getActive());// 非真實刪除
            tccProductFacade.save(entity, member, false);
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/products/tcc/pic/upload
     * @param request
     * @param multiPart
     * @return
     */
    @Path("/tcc/pic/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response uploadTccPrdPic(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("uploadTccPrdPic ...");
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
            
            Long id = this.getMultiPartLong(multiPart, "id");// EC_TCC_PRODUCT.ID
            logger.debug("uploadTccPrdPic id = {}", id);
            String fileName = this.getMultiPartValue(multiPart, "filename");
            logger.info("uploadTccPrdPic fileName = "+fileName);
            String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");
            logger.debug("uploadTccPrdPic fileContentType = {}", fileContentType);
            String message = this.getMultiPartValue(multiPart, "message");
            logger.debug("uploadTccPrdPic message = {}", message);
            
            if( id==null ){
                logger.error("uploadTccPrdPic error id==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            
            if( bodyParts!=null && !bodyParts.isEmpty() ){
                // 上傳圖片
                byte[] content  = null;
                for (int i = 0; i < bodyParts.size(); i++) {
                    fileName = bodyParts.get(i).getContentDisposition().getFileName();
                    logger.info("uploadTccPrdPic ContentDisposition fileName = "+fileName);
                    BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                    fileContentType = (fileContentType==null)?bodyParts.get(i).getContentDisposition().getType():fileContentType;
                    logger.debug("uploadTccPrdPic ContentDisposition file ContentType = " + fileContentType);
                    content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                    logger.debug("uploadTccPrdPic content size = "+((content!=null)?content.length:0));
                    break; // 只支援一個圖檔
                }
                
                if( fileName==null || content==null ){
                    logger.error("uploadTccPrdPic error fileName==null || content==null");
                    return Response.notAcceptable(null).build(); // HTTP STATUS 406
                }
                
                FileEnum fileEnum = FileEnum.TCC_PRD_PIC;
                // 儲存實體檔案
                FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, true, null);
                logger.info("uploadTccPrdPic write real file finish.");
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
                logger.info("uploadTccPrdPic saveFileName = "+saveFileName);
            
                // 縮圖
                ImageVO retImgVO = new ImageVO();
                // 取得Image寬高
                String errMsg1 = img.getImageInfo(saveFileNameFull, retImgVO);
                if( errMsg1!=null ){
                    logger.error("uploadTccPrdPic getImageInfo ... "+errMsg1);
                }else{
                    // 縮圖 (縮圖需寬高資訊)
                    String errMsg2 = img.compressImageFile(dir, saveFileName, saveFileNameFull, true, retImgVO);
                    if( errMsg2!=null ){
                        logger.error("uploadTccPrdPic compressImageFile ... "+errMsg1);
                    }
                }
                logger.info("uploadTccPrdPic compress image finish.");
                */
                // Save EcFile
                EcFile fileEntity = fileFacade.findEntityByPrimary(FileEnum.TCC_PRD_PIC.getPrimaryType(), id);
                if( fileEntity==null ){
                    fileEntity = new EcFile();
                }
                
                fileEntity.setStoreId(null);
                fileEntity.setPrimaryType(fileEnum.getPrimaryType());
                fileEntity.setPrimaryId(id);
                fileEntity.setDescription(fileEnum.toString());
                //fileEntity.setFilename(fileName);
                //fileEntity.setName(name);
                //fileEntity.setSavedir(saveFileNameFull.replaceAll(saveFileName, ""));
                //fileEntity.setSavename(saveFileName);
                //fileEntity.setFileSize(content.length);
                fileEntity.setContentType(fileContentType);

                fileEntity.setFilename(fileVO.getFilename());
                fileEntity.setName(fileVO.getName());
                fileEntity.setSavedir(fileVO.getSavedir());
                fileEntity.setSavename(fileVO.getSavename());
                fileEntity.setFileSize(fileVO.getFileSize());
                fileFacade.save(fileEntity, member, false);
            }
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 刪除圖片
     * @param request
     * @param formVO
     * @return 
     */
    @Path("/tcc/pic/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response deleteTccPrdPic(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("deleteTccPrdPic ...");
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
            
            if( formVO==null || formVO.getId()==null ){
                logger.error("deleteTccPrdPic formVO==null || formVO.getId()==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            EcFile fileEntity = fileFacade.findEntityByPrimary(FileEnum.TCC_PRD_PIC.getPrimaryType(), formVO.getId());
            if( fileEntity==null ){
                logger.error("deleteTccPrdPic fileEntity==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }
            // 不刪實體檔，避免有共用狀況(排程刪除即可)
            fileFacade.remove(fileEntity, false);
            
            return this.genSuccessRepsone(request);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>
}

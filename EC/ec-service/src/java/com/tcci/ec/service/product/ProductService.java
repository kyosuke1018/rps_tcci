/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.product;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcFavoritePrd;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdType;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.facade.member.EcFavoritePrdFacade;
import com.tcci.ec.facade.product.EcPrdTypeFacade;
import com.tcci.ec.facade.product.EcProductFacade;
import com.tcci.ec.facade.store.EcStoreFacade;
import com.tcci.ec.facade.util.ProductFilter;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.ProductVO;
import com.tcci.ec.service.ServiceBase;
import com.tcci.ec.vo.PrdType;
import com.tcci.ec.vo.Product;
import com.tcci.ec.vo.ProductQuery;
import com.tcci.fc.util.FileUtils;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
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
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("product")
public class ProductService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);
    @EJB
    private EcProductFacade ecProductFacade;
    @EJB
    private EcPrdTypeFacade ecPrdTypeFacade;
    @EJB 
    protected EcFavoritePrdFacade favoritePrdFacade;
    @EJB 
    protected EcFileFacade fileFacade;
    @EJB
    private EcStoreFacade ecStoreFacade;
    
    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public ProductQuery list(@Context HttpServletRequest request,
            ProductFilter filter) {
        logger.debug("product list start");
        ProductQuery productQuery = new ProductQuery();
        try{
            if(filter==null){
                filter = new ProductFilter();
            }else{
                if( CollectionUtils.isNotEmpty(filter.getAreaList()) ){//指定銷售地區 多選
                    List<Long> storeIdList = ecStoreFacade.findIdListByArea(filter.getAreaList());//符合商店
                    if(CollectionUtils.isEmpty(storeIdList)){//沒有符合查詢區域的商店
                        return productQuery;
                    }
                    logger.debug("product storeIdList:"+storeIdList);
                    filter.setStoreIdList(storeIdList);
                }
            }
            List<EcProduct> list = ecProductFacade.findByCriteria(filter);
            
//            String url = jndiConfig.getProperty("url.prefix");
            //篩選類別及parent類別符合的商品
            logger.debug("product list TypeId:"+filter.getTypeId());
            if(CollectionUtils.isNotEmpty(list)){
                logger.debug("product list :"+list.size());
                if (filter.getTypeId() != null) {
                    list = this.processParentType(list, filter.getTypeId());
                    logger.debug("after processParentType product list :"+list.size());
                    EcPrdType ecPrdType = ecPrdTypeFacade.find(filter.getTypeId());
                    if(ecPrdType != null){
                        List<EcPrdType> list2 = ecPrdTypeFacade.findByParent(ecPrdType);
                        List<PrdType> result2 = new ArrayList<>();
                        
                        if(CollectionUtils.isNotEmpty(list2)){
                            for(EcPrdType entity:list2){
                                result2.add(entityTransforVO.prdTypeTransfor(entity));
                            }
                            productQuery.setNextTypeList(result2);
                        }
                    }
                }
            }
            
            if(CollectionUtils.isNotEmpty(list)){
                List<Product> result = new ArrayList<>();
                //喜愛商品
                List<Long> favPrdList = new ArrayList<>();
                logger.debug("product list guest:"+request.getParameter("guest"));
                if(request.getParameter("guest") == null){//非訪客
                    EcMember member = getAuthMember();
                    if(member!=null){
                        logger.debug("product list member..."+member.getLoginAccount());
                    }
                    List<EcFavoritePrd> prdList = favoritePrdFacade.findByMember(member);
                    if(CollectionUtils.isNotEmpty(prdList)){
                        for(EcFavoritePrd entity:prdList){
                            favPrdList.add(entity.getProduct().getId());
                        }
                    }
                }
                
                //處理查詢筆數的問題
                if (filter.getStartResult()>0 && filter.getMaxResult()>0) {
                    List<EcProduct> temp = new ArrayList<>();
                    for(int i = filter.getStartResult();i<filter.getMaxResult();i++){
                        if(i<list.size()){
                            EcProduct entity = list.get(i);
                            if(entity!=null){
                                Product vo = entityTransforVO.productTransfor(entity);
                                //喜愛商品
                                if(CollectionUtils.isNotEmpty(favPrdList)){
                                    vo.setFavorite(favPrdList.contains(entity.getId()));
                                }
                                
                                result.add(vo);
                            }
                        }else{
                            break;
                        }
                    }
                }else{
                    for(EcProduct entity:list){
                        Product vo = entityTransforVO.productTransfor(entity);
                        //喜愛商品
                        if(CollectionUtils.isNotEmpty(favPrdList)){
                            vo.setFavorite(favPrdList.contains(entity.getId()));
                        }
                        
                        result.add(vo);
                    }
                }
                
                productQuery.setProductList(result);
            }
            
        }catch(Exception e){
            logger.error("Exception:"+e);
            e.printStackTrace();
        }
        return productQuery;
    }
    
    private List<EcProduct> processParentType(List<EcProduct> list, Long typeId){
        List<EcProduct> result = new ArrayList<>();
        for(EcProduct entity:list){
            EcPrdType type = entity.getType();
            if(type!=null){
                List<Long> parentList = this.findParentList(type);
                if(CollectionUtils.isNotEmpty(parentList)){
//                    logger.debug("processParentType parentList size:"+parentList.size());
//                    if(!parentList.contains(typeId)){
//                        list.remove(entity);//java.util.ConcurrentModificationException
//                    }
                    if(parentList.contains(typeId)){
                        result.add(entity);
                    }
                }
            }
        }
        
        return result;
    }
    
    private List<Long> findParentList(EcPrdType type){
        List<Long> result = new ArrayList<>();
        EcPrdType entity = type;
        for(int i = 0;i<type.getLevelnum();i++){
//            logger.debug("findParentList:"+entity.getId());
            result.add(entity.getId());
            if(entity.getParent() == null){
                break;
            }
            entity = entity.getParent();
        }
        
        return result;
    }
    
    @GET
    @Path("list2")
    @Produces("application/json; charset=UTF-8;")
    public List<Product> list(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId) {
        logger.debug("product list2 storeId:"+storeId);
        ProductFilter Filter = new ProductFilter();
        Filter.setStoreId(storeId);
        List<EcProduct> list = ecProductFacade.findByCriteria(Filter);
        List<Product> result = new ArrayList<>();
        try{
            if(CollectionUtils.isNotEmpty(list)){
                for(EcProduct entity:list){
                    result.add(entityTransforVO.productTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @GET
    @Path("typeList")
    @Produces("application/json; charset=UTF-8;")
    public List<PrdType> typeList(@Context HttpServletRequest request) {
        logger.debug("product typeList:");
        List<EcPrdType> list = ecPrdTypeFacade.findAllActive();
        List<PrdType> result = new ArrayList<>();
        try{
            if(CollectionUtils.isNotEmpty(list)){
                for(EcPrdType entity:list){
                    result.add(entityTransforVO.prdTypeTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @GET
    @Path("typeLevel1")
    @Produces("application/json; charset=UTF-8;")
    public List<PrdType> typeLevel1(@Context HttpServletRequest request) {
        logger.debug("product typeLevel1:");
        List<EcPrdType> list = ecPrdTypeFacade.findLevel1();
        List<PrdType> result = new ArrayList<>();
        try{
            if(CollectionUtils.isNotEmpty(list)){
                for(EcPrdType entity:list){
                    result.add(entityTransforVO.prdTypeTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @GET
    @Path("typeByParent")
    @Produces("application/json; charset=UTF-8;")
    public List<PrdType> typeByParent(@Context HttpServletRequest request, 
            @QueryParam(value = "prdTypeId") Long prdTypeId) {
        logger.debug("product typeByParent prdTypeId:"+prdTypeId);
        List<PrdType> result = new ArrayList<>();
        try{
            if(prdTypeId!=null){
                EcPrdType ecPrdType = ecPrdTypeFacade.find(prdTypeId);
                List<EcPrdType> list = ecPrdTypeFacade.findByParent(ecPrdType);
                if(CollectionUtils.isNotEmpty(list)){
                    for(EcPrdType entity:list){
                        result.add(entityTransforVO.prdTypeTransfor(entity));
                    }
                }
            }else{
                return this.typeLevel1(request);
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    /**
     * /services/products/full/{id}
     * @param request
     * @param productId
     * @param id
     * @return 
     */
//    @GET
//    @Path("/full/{id}")
//    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
//    public Response findProductFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
//        logger.debug("findProductFullInfo ...");
//        EcMember member = getReqUser(request);
//        EcStore store = getStore(request);
//        try{
//            logger.info("findProductFullInfo id = "+id);
//            
//            ProductVO prdVO = productFacade.findById(store.getId(), id, true);
//            return this.genSuccessRepsone(request, prdVO);
//        }catch(Exception e){
//            logger.error("findProductFullInfo Exception:\n", e);
//        }
//        return this.genFailRepsone(request);
//    }
    @GET
//    @Path("/full/{id}")
    @Path("full")
    @Produces("application/json; charset=UTF-8;")
//    public ProductVO findProductFullInfo(@Context HttpServletRequest request, @PathParam("id")Long id){
    public ProductVO findProductFullInfo(@Context HttpServletRequest request, 
            @QueryParam(value = "productId") Long productId){
        logger.debug("findProductFullInfo ...");
//        EcMember member = getReqUser(request);
//        EcStore store = getStore(request);
        try{
            logger.info("findProductFullInfo id = "+productId);
            EcProduct ecProduct = ecProductFacade.find(productId);
            Locale locale = getLocale(request);
            if(ecProduct != null && productId != null){
                ProductVO prdVO = ecProductFacade.findById(ecProduct.getStore().getId(), productId, true, locale);
                return prdVO;
            }
        }catch(Exception e){
            logger.error("findProductFullInfo Exception:\n", e);
        }
        return null;
    }
    
    @GET
    @Path("favorite")
    @Produces("application/json; charset=UTF-8;")
    public String favorite(@Context HttpServletRequest request,
            @QueryParam(value = "productId") Long productId,
            @QueryParam(value = "flag") boolean flag) {
        logger.debug("favorite productId..."+productId);
        try{
            EcMember member = getAuthMember();
            if(member==null || productId==null){
//                return "查無會員及商品資訊!";
                return "FAIL";
            }
            EcProduct product = ecProductFacade.find(productId);
            if(product!=null){
                List<EcFavoritePrd> productList = favoritePrdFacade.findByPrimary(member, product);
                if(flag && CollectionUtils.isEmpty(productList)){
                    EcFavoritePrd entity = new EcFavoritePrd(member, product);
                    entity.setCreator(member);
                    favoritePrdFacade.save(entity);
                }else if(!flag && CollectionUtils.isNotEmpty(productList)){
                    for(EcFavoritePrd entity:productList){
                        favoritePrdFacade.remove(entity);
                    }
                }
            }else{
//                return "商品不存在! productId..."+productId;
                return "FAIL";
            }
            return "SUCCESS";
        }catch(Exception e){
            logger.error("Exception:"+e);
            return "FAIL";
        }
    }
    
    @GET
    @Path("changeStatus")
    @Produces("application/json; charset=UTF-8;")
    public String changeStatus(@Context HttpServletRequest request,
            @QueryParam(value = "productId") Long productId,
            @QueryParam(value = "status") String status) {
        logger.debug("changeStatus productId:"+productId);
        logger.debug("changeStatus status:"+status);
        
        EcMember member = getAuthMember();
        EcProduct product = ecProductFacade.find(productId);
        ProductStatusEnum statusEnum = ProductStatusEnum.getFromCode(status);
        if(member==null || product==null || statusEnum==null){
            return "FAIL";
        }
        
        product.setStatus(status);
        ecProductFacade.save(product);
        return "SUCCESS";
    }
    
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public ProductVO savePrdBasic(@Context HttpServletRequest request,
            ProductVO prdVO) {
        logger.debug("savePrdBasic ...");
        EcMember member = getAuthMember();
        EcStore store = ecStoreFacade.find(prdVO.getStoreId());
        if(member==null || store==null){
            return null;
        }
        
        Locale locale = getLocale(request);
        List<String> errors = new ArrayList<>();
        try{
            if( ecProductFacade.checkInput(prdVO, member, locale, errors) ){// 輸入檢查
                ecProductFacade.saveVO(prdVO, member);
                prdVO = ecProductFacade.findById(prdVO.getStoreId(), prdVO.getId(), true, locale);
                
                return prdVO;
            }else{
                for(String err:errors){
                    logger.error("savePrdBasic err:", err);
                }
                return null;
            }
        }catch(Exception e){
            logger.error("savePrdBasic Exception:\n", e);
        }
        return null;
    }
    
    @GET
    @Path("findFavorite")
    @Produces("application/json; charset=UTF-8;")
    public List<Product> findFavorite() {
        logger.debug("findFavorite ...");
        EcMember member = getAuthMember();
        if(member!=null){
            logger.debug("findFavorite member..."+member.getLoginAccount());
        }else{
            return null;
        }
        List<EcFavoritePrd> prdList = favoritePrdFacade.findByMember(member);
        List<Product> list = new ArrayList<>();
        String url = jndiConfig.getProperty("url.prefix");
        if(CollectionUtils.isNotEmpty(prdList)){
            for(EcFavoritePrd entity:prdList){
                list.add(entityTransforVO.productTransfor(entity.getProduct()));
            }
        }
        return list;
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Product Pictures">
    /**
     * http://localhost:8080/ec-seller-serv/products/{prdId}/picture/list
     * 產品照片列表
     * @param request
     * @param prdId
     * @return
     */
//    @Path("/{prdId}/picture/list")
    @Path("/picture/list")
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
//    public List<FileVO> findPrdPicture(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId) {
    public List<FileVO> findPrdPicture(@Context HttpServletRequest request, @QueryParam("productId")Long productId) {
        logger.info("findPrdPicture ...");
        String root = GlobalConstant.DIR_STORE_IMG;
        try{
            EcMember member = getAuthMember();
            EcProduct product = ecProductFacade.find(productId);
            if(member==null || product==null){
//                return "查無會員及商品資訊!";
                return null;
            }
            
            List<FileVO> list = fileFacade.findByPrimary(product.getStore().getId(), EcProduct.class.getName(), productId);
            return list;
        }catch(Exception e){
            logger.error("findPrdPicture Exception:\n", e);
        }
        return null;
    }
    
    /**
     * 上傳產品照片
     * @param request
     * @param prdId
     * @param storeId
     * @param multiPart
     * @return
     */
//    @Path("/{prdId}/picture/upload")
    @Path("/picture/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
//    public Response uploadPrdPicture(@Context HttpServletRequest request, 
//            @PathParam("prdId")Long prdId, @QueryParam("storeId")Long storeId, final FormDataMultiPart multiPart) {
    public String uploadPrdPicture(@Context HttpServletRequest request, @QueryParam("productId")Long productId, final FormDataMultiPart multiPart) {
        logger.info("uploadPrdPicture ...");
        String root = GlobalConstant.DIR_STORE_IMG;
        try{
            
            EcMember member = getAuthMember();
            EcProduct product = ecProductFacade.find(productId);
            if(member==null || product==null){
                return "FAIL";
            }
            EcStore store = product.getStore();
            
            Locale locale = getLocale(request);
            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            if( bodyParts==null || bodyParts.isEmpty() ){
                logger.error("uploadPrdPicture error bodyParts.isEmpty()");
//                return Response.notAcceptable(null).build(); // HTTP STATUS 406
                return "FAIL";
            }
            
            //String fileName = this.getMultiPartValue(multiPart, "filename");
            //logger.info("uploadPrdPicture fileName = "+fileName);
            //String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");
            
            byte[] content  = null;
            List<Long> idList = new ArrayList<>();
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
                    
                    // Save EcFile
                    EcFile fileEntity = new EcFile();
                    fileEntity.setStoreId(store.getId());
                    fileEntity.setPrimaryType(FileEnum.PRD_PIC.getPrimaryType());
                    fileEntity.setPrimaryId(productId);
                    fileEntity.setDescription(fileName);// 預設檔名，後續可修改
                    fileEntity.setFilename(fileName);
                    fileEntity.setName(name);
                    fileEntity.setSavedir(dir);
                    fileEntity.setSavename(saveFileName);
                    fileEntity.setContentType(fileContentType);
                    fileEntity.setFileSize(content.length);
                    
                    List<String> errors = new ArrayList<>();
                    if( fileFacade.checkInput(fileEntity, member, locale, errors) ){// 輸入檢查
//                        fileFacade.save(fileEntity, member, false);
                        fileFacade.save(fileEntity, member);
                        idList.add(fileEntity.getId());
                    }else{
//                        return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        logger.error("uploadPrdPicture error: ("+ResStatusEnum.IN_ERROR+") "+errors);
                        return "FAIL";
                    }
                }
            }
            return "SUCCESS";
            /**
            List<FileVO> list = fileFacade.findByIds(store.getId(), EcProduct.class.getName(), productId, idList);
            // 需傳回 jQuery File Upload PlugIn 的接收格式
            //BaseListResponseVO res = this.genSuccessListRepsone(request, list);
            String urlPrefix = WebUtils.getUrlPrefix(request);
            if( list!=null ){
                List<UploadResponseVO> pictures = new ArrayList<>();
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
//                return Response.ok(res, MediaType.APPLICATION_JSON).build();
            }*/
        }catch(Exception e){
            logger.error("uploadPrdPicture Exception:\n", e);
        }
//        return Response.ok(this.genBaseFailRepsone(request, ResStatusEnum.FAIL), MediaType.APPLICATION_JSON).build();
        return "SUCCESS";
    }
    
    /**
     * /services/products/{prdId}/picture/save
     * @param request
     * @param formVO
     * @param prdId
     * @return
     */
    /**@POST
    @Path("/{prdId}/picture/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePrdPicDesc(@Context HttpServletRequest request, @PathParam("prdId")Long prdId, SubmitVO formVO) {
        logger.debug("savePrdPicDesc ...");
        EcMember member = getReqUser(request);
        EcStore store = getStore(request);
        Locale locale = getLocale(request);
        List<String> errors = new ArrayList<String>();
        try{
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            logInputs(methodName, formVO, member);// log 輸入資訊
            
            if( formVO==null || prdId==null || formVO.getId()==null ){
                logger.error("savePrdPicDesc formVO==null");
                return Response.notAcceptable(null).build();
            }
            boolean admin = hasAdminRights(request, member);
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcFile entity = fileFacade.find(formVO.getId());
            if( entity!=null ){
                logger.info("savePrdPicDesc "+entity);
                entity.setDescription(formVO.getDescription());
                
                if( fileFacade.checkInput(entity, member, locale, errors) ){// 輸入檢查
                    fileFacade.save(entity, member, false);
                }else{
                    return genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                }
            }else{
                logger.error("savePrdPicDesc invalid id = "+formVO.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            
            return findPrdPicture(request, prdId, storeId);
        }catch(Exception e){
            logger.error("savePrdPicDesc Exception:\n", e);
        }
        return this.genFailRepsone(request);
    }*/
    
    /**
     * /services/products/{prdId}/picture/remove
     * @param request
     * @param formVO
     * @param prdId
     * @return
     */
    /**@POST
//    @Path("/{prdId}/picture/remove")
    @Path("/picture/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePrdPic(@Context HttpServletRequest request, SubmitVO formVO, @PathParam("prdId")Long prdId) {
        logger.debug("removePrdPic ...");
        EcMember member = getReqUser(request);
        EcStore store = getStore(request);
        try{
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            logInputs(methodName, formVO, member);// log 輸入資訊
            
            if( formVO==null || prdId==null || formVO.getId()==null ){
                logger.error("removePrdPic formVO==null");
                return Response.notAcceptable(null).build();
            }
            boolean admin = hasAdminRights(request, member);
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            EcFile entity = fileFacade.find(formVO.getId());
            if( entity!=null ){
                logger.info("removePrdPic "+entity);
                fileFacade.remove(entity, false);
            }else{
                logger.error("removePrdPic invalid id = "+formVO.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            
            return findPrdPicture(request, prdId, storeId);
        }catch(Exception e){
            logger.error("removePrdPic Exception:\n", e);
        }
        return this.genFailRepsone(request);
    }*/
    
    /**@GET
    @Path("/{prdId}/picture/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePrdPic(@Context HttpServletRequest request, 
            @PathParam("prdId")Long prdId, @PathParam("id")Long id, @QueryParam("storeId")Long storeId) {
        SubmitVO formVO = new SubmitVO();
        formVO.setId(id);
        formVO.setStoreId(storeId);
        return removePrdPic(request, formVO, prdId);
    }*/
    //</editor-fold>
}

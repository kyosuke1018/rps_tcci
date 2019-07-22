package com.tcci.et.facade.rs;

import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.dw.facade.MmDwFacade;
import com.tcci.et.entity.EtMember;
import com.tcci.et.entity.EtOption;
import com.tcci.et.enums.OptionEnum;
import com.tcci.et.enums.rs.ResStatusEnum;
import com.tcci.et.facade.EtOptionFacade;
import com.tcci.et.model.rs.IntOptionVO;
import com.tcci.et.model.rs.LongOptionVO;
import com.tcci.et.model.OptionVO;
import com.tcci.cm.model.global.StrOptionVO;
import com.tcci.et.model.rs.BaseResponseVO;
import com.tcci.et.model.rs.OptionsMapVO;
import com.tcci.et.model.rs.SubmitVO;
import com.tcci.et.model.criteria.PrCriteriaVO;
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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 取得系統各種選單選項
 * @author Peter.pan
 */
@Path("/ops")
public class OptionsREST extends AbstractWebREST {
    @EJB EtOptionFacade optionFacade;
    @EJB MmDwFacade mmDwFacade;
    
    public OptionsREST(){
        logger.debug("OptionsREST init ...");
        // for 支援排序
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        //sortFieldMap.put("code", "S.CODE");// 編號
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<String, String>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
    }
    
    //<editor-fold defaultstate="collapsed" desc="for 系統各類簡單選單選項">
    /**
     *  系統各類簡單選單選項
     * /services/ops/get?keys=prdStatus
     * @param request
     * @param keys
     * @param memberId
     * @param lang 控制非中文類的語系，來源DB的選項都先顯示 ENAME
     * @return 
     */
    @GET
    @Path("/get")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findOptions(@Context HttpServletRequest request, @QueryParam("keys")String keys
            , @QueryParam("memberId")Long memberId, @QueryParam("lang")String lang){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOptions keys = "+keys+", memberId = "+memberId);
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
//            EcStore store = getStore(request);
            Locale locale = getLocale(request);
//            boolean admin = hasAdminRights(request, member);
//            storeId = admin?storeId:store.getId();
            // 賣家、管理員共用 RESTful
//            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
//                return genUnauthorizedResponse();
//            }
            //Map resMap = new HashMap();
            OptionsMapVO mapVO = new OptionsMapVO();
            String[] keyAry = keys!=null?keys.split(","):null;
            if( keyAry!=null ){
                for(String key : keyAry){
                    OptionEnum opEnum = OptionEnum.getFromCode(key);
                    logger.debug("findOptions opEnum = "+opEnum);
                    if( opEnum==null ){
                        continue;
                    }
                    // ET_OPTION
                    if( "ET_OPTION".equals(opEnum.getTable()) ){
//                        List<LongOptionVO> list = optionFacade.findByTypeOptions(opEnum.isStore()?storeId:0, key, lang);
                        List<LongOptionVO> list = optionFacade.findByTypeOptions(key, lang, true);
//                        if( OptionEnum.INDUSTRY == opEnum ){
//                            list = optionFacade.findByTypeOptions(key, lang);
//                        }
                        ExtBeanUtils.copyProperty(mapVO, opEnum.getCode(), list==null?new ArrayList():list);
                    // 有階層選項
//                    }else if( OptionEnum.PRD_TYPE_TREE == opEnum ){
//                        PrdTypeTreeVO tree = prdTypeFacade.findPrdTypeTree(storeId, true, lang);
//                        mapVO.setPrdTypeTree(tree);
                    // 獨立 TABLE
                    }else if( OptionEnum.FACTORYS == opEnum ){
                        List<CmFactory> result = cmFactoryFacade.findByAreaCode(null, null, null);
                        List<LongOptionVO> list = new ArrayList<>();
                        if (result != null ) {
                            logger.debug("buildFactoryOptions options:"+result.size());
                            for (CmFactory g : result) {
                                list.add(new LongOptionVO(g.getId(), g.getCode()+"-"+g.getName()));
                            }
                        }
                        mapVO.setFactorys(list);
//                    }else if( OptionEnum.STORES == opEnum ){
//                        List<LongOptionVO> list = storeFacade.findOnlineStoreOps(memberId, lang);
//                        mapVO.setStores(list==null?new ArrayList():list);
//                    }else if( OptionEnum.TAX_TYPE == opEnum ){
                        // NA
                    // ENUM (新增時記得 genStrOptions() 要加入此 enum)
                    // 部分 ENUM ITEMS
                    
                    }else if( OptionEnum.COUNTRY == opEnum ){
                        PrCriteriaVO criteriaVO = new PrCriteriaVO();
                        if("C".equals(lang)){
                            criteriaVO.setCode("tcc_cn");
                        }else{
                            criteriaVO.setCode("tcc");
                        }
                        List<StrOptionVO> list = mmDwFacade.findCountryOptionsByCriteria(criteriaVO);
                        ExtBeanUtils.copyProperty(mapVO, opEnum.getCode(), list==null?new ArrayList():list);
                    }else if( OptionEnum.CURRENCY == opEnum ){
                        PrCriteriaVO criteriaVO = new PrCriteriaVO();
                        if("C".equals(lang)){
                            criteriaVO.setCode("tcc_cn");
                        }else{
                            criteriaVO.setCode("tcc");
                        }
                        List<StrOptionVO> list = mmDwFacade.findCurrencyOptionsByCriteria(criteriaVO);
                        ExtBeanUtils.copyProperty(mapVO, opEnum.getCode(), list==null?new ArrayList():list);
                    }else{
                    // 所有 ENUM ITEMS
                        List list = genOptions(key, locale, null);
                        ExtBeanUtils.setProperty(mapVO, key, list==null?new ArrayList():list);
                        logger.debug("findOptions list = "+(list!=null?list.size():0));
                    }
                }
            }
            
            return this.genSuccessRepsone(request, mapVO);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    public List genOptions(String key, Locale locale, Boolean adminUser){
        OptionEnum opEnum = OptionEnum.getFromCode(key);
        if( opEnum.getEnumClass()!=null ){
            return genStrOptions(opEnum.getEnumClass(), locale, adminUser);
        }
        
        logger.error("genOptions error key = "+key);
        return null;
    }
    public List<StrOptionVO> genStrOptions(Class enumClass, Locale locale, Boolean adminUser){
        List<StrOptionVO> list = new ArrayList<>();
//        if(enumClass == ProductStatusEnum.class){
//            if( adminUser==null ){
//                for(ProductStatusEnum item : ProductStatusEnum.values()){
//                    list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
//                }
//            }
//        }else{
//            logger.error("genStrOptions not supported enumClass = "+enumClass);
//        }

        return list;
    }
    public List<LongOptionVO> genLongOptions(String key){
        List<LongOptionVO> list = new ArrayList<LongOptionVO>();

        return list;
    }
    public List<IntOptionVO> genIntOptions(Class enumClass){
        List<IntOptionVO> list = new ArrayList<IntOptionVO>();

        return list;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for ET_OPTION">
    /**
     * /services/ops/list
     * @param request
     * @param type
     * @param storeId
     * @return 
     */
    @GET
    @Path("/list")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findOptionsByType(@Context HttpServletRequest request
            , @QueryParam("type")String type
            , @QueryParam("storeId")Long storeId){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOptionsByType ... type = "+type+", storeId = "+storeId);
        List<String> errors = new ArrayList<String>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( storeId == null ){
                logger.error("findOptionsByType storeId==null ");
                return this.genFailRepsone(request); 
            }
            List<OptionVO> list = optionFacade.findByType(type);
            
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/ops/save
     * @param request
     * @param formVO
     * @param type
     * @param storeId
     * @return 
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response saveOptions(@Context HttpServletRequest request, SubmitVO formVO
            , @QueryParam("type")String type
            , @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("saveOptions ... type = "+type+", storeId = "+storeId);
        List<String> errors = new ArrayList<String>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( formVO==null || storeId==null || type==null || formVO.getOptionList()==null ){
                logger.error("saveOptions formVO==null");
                return Response.notAcceptable(null).build();
            }
            
            int sortnum = 0;
            for(OptionVO vo : formVO.getOptionList()){
                if( vo.isClientModified() ){
                    sortnum++;
                    EtOption entity = vo.getId()!=null? optionFacade.find(vo.getId()):new EtOption();
                    if( entity!=null ){
                        entity.setDisabled(entity.getDisabled()==null?false:entity.getDisabled());
                        entity.setSortnum(sortnum);
                        entity.setCname(vo.getCname());
                        entity.setType(type);
                        
                        if( optionFacade.checkInput(entity, member, locale, errors) ){
//                            optionFacade.save(entity, member, false); 
                        }else{
                            return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        }
                    }
                }
            }
            
            return findOptionsByType(request, type, storeId);
            //return findOptions(request, type, storeId);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/ops/remove
     * @param request
     * @param formVO
     * @param type
     * @param storeId
     * @return 
     */
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response removeOption(@Context HttpServletRequest request, SubmitVO formVO
            , @QueryParam("type")String type
            , @QueryParam("storeId")Long storeId) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("removeOption ... type = "+type+", storeId = "+storeId);
        List<String> errors = new ArrayList<String>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            EtOption entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 && type!=null && storeId!=null){
                entity = optionFacade.find(formVO.getId());
            }else{
                logger.error("removeOption entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            
            BaseResponseVO resVO = null;
            if( entity!=null ){
                if( !type.equals(entity.getType()) ){
                    logger.error("removeOption type error : "+type+" != "+entity.getType());
                    return Response.notAcceptable(null).build();
                }
                
                entity.setDisabled(Boolean.TRUE);// 非真實刪除
//                optionFacade.save(entity, member, false);
                
                //return this.genSuccessRepsoneWithId(request, formVO.getId());
                return findOptionsByType(request, type, storeId);
            }else{
                logger.error("removeOption not exists id = "+formVO.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>

}

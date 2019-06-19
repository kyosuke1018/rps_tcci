package com.tcci.ec.facade.rs;

import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOption;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.CusCreditEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.enums.OptionEC10Enum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.OrderSrcEnum;
import com.tcci.ec.enums.PayMethodEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.ProductVariantEnum;
import com.tcci.ec.enums.RfqStatusEnum;
import com.tcci.ec.enums.ShipMethodEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import com.tcci.ec.enums.StockEnum;
import com.tcci.ec.enums.StoreTypeEnum;
import com.tcci.ec.enums.ec10.ShipMethodEC10Enum;
import com.tcci.ec.enums.ec10.TranModeEC10Enum;
import com.tcci.ec.enums.ec10.TranTypeEC10Enum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcCurrencyFacade;
import com.tcci.ec.facade.EcCusAddrFacade;
import com.tcci.ec.facade.EcOptionFacade;
import com.tcci.ec.facade.EcOrderFacade;
import com.tcci.ec.facade.EcPaymentFacade;
import com.tcci.ec.facade.EcPrdTypeFacade;
import com.tcci.ec.facade.EcPrdVarOptionFacade;
import com.tcci.ec.facade.EcProductFacade;
import com.tcci.ec.facade.EcShippingFacade;
import com.tcci.ec.facade.EcTccOrderFacade;
import com.tcci.ec.facade.EcVendorFacade;
import com.tcci.ec.model.rs.IntOptionVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.OptionVO;
import com.tcci.ec.model.OrderVO;
import com.tcci.ec.model.PrdTypeTreeVO;
import com.tcci.ec.model.ProductVO;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
import com.tcci.ec.model.e10.ContractProductVO;
import com.tcci.ec.model.e10.SalesAreaE10VO;
import com.tcci.ec.model.rs.StrOptionVO;
import com.tcci.ec.model.rs.BaseResponseVO;
import com.tcci.ec.model.rs.OptionsMapVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import java.math.BigDecimal;
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
    @EJB EcPrdTypeFacade prdTypeFacade;
    @EJB EcVendorFacade vendorFacade;
    @EJB EcOptionFacade optionFacade;
    @EJB EcPrdVarOptionFacade prdVarOptionFacade;
    @EJB EcShippingFacade shippingFacade;
    @EJB EcPaymentFacade paymentFacade;
    @EJB EcCurrencyFacade currencyFacade;
    @EJB EcProductFacade productFacade;
    @EJB EcOrderFacade orderFacade;
    @EJB EcTccOrderFacade tccOrderFacade;
    @EJB EcCusAddrFacade cusAddrFacade;
    
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
     * @param storeId
     * @param prdId
     * @param lang 控制非中文類的語系，來源DB的選項都先顯示 ENAME
     * @return 
     */
    @GET
    @Path("/get")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findOptions(@Context HttpServletRequest request, @QueryParam("keys")String keys
            , @QueryParam("storeId")Long storeId, @QueryParam("prdId")Long prdId
            , @QueryParam("memberId")Long memberId, @QueryParam("lang")String lang){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOptions keys = "+keys+", storeId = "+storeId+", prdId = "+prdId+", memberId = "+memberId);
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            storeId = admin?storeId:store.getId();
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
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
                    // EC_OPTION
                    if( "EC_OPTION".equals(opEnum.getTable()) ){
                        List<LongOptionVO> list = optionFacade.findByTypeOptions(opEnum.isStore()?storeId:0, key, lang);
                        ExtBeanUtils.copyProperty(mapVO, opEnum.getCode(), list==null?new ArrayList():list);
                    // 有階層選項
                    }else if( OptionEnum.PRD_TYPE_TREE == opEnum ){
                        PrdTypeTreeVO tree = prdTypeFacade.findPrdTypeTree(storeId, true, lang);
                        //resMap.put(key, tree);
                        mapVO.setPrdTypeTree(tree);
                    // 獨立 TABLE
                    }else if( OptionEnum.STORES == opEnum ){
                        List<LongOptionVO> list = storeFacade.findOnlineStoreOps(memberId, lang);
                        mapVO.setStores(list==null?new ArrayList():list);
                    }else if( OptionEnum.PRODUCTS == opEnum ){
                        List<LongOptionVO> list = productFacade.findOnlinePrdOps(storeId, locale, lang);
                        mapVO.setProducts(list==null?new ArrayList():list);
                    }else if( OptionEnum.PRD_COLOR == opEnum ){
                        List<LongOptionVO> list = prdVarOptionFacade.findVarOptions(storeId, prdId, ProductVariantEnum.COLOR);
                        mapVO.setPrdColor(list==null?new ArrayList():list);
                    }else if( OptionEnum.PRD_SIZE == opEnum ){
                        List<LongOptionVO> list = prdVarOptionFacade.findVarOptions(storeId, prdId, ProductVariantEnum.SIZE);
                        mapVO.setPrdSize(list==null?new ArrayList():list);
                    }else if( OptionEnum.SHIPPING == opEnum ){
                        List<LongOptionVO> list = shippingFacade.findShippingOptions(storeId, locale);
                        mapVO.setShipping(list==null?new ArrayList():list);
                    }else if( OptionEnum.PAYMENT == opEnum ){
                        List<LongOptionVO> list = paymentFacade.findPaymentOptions(storeId, locale);
                        mapVO.setPayment(list==null?new ArrayList():list);
                    }else if( OptionEnum.CURRENCY == opEnum ){
                        List<LongOptionVO> list = currencyFacade.findCurrencyOptions(storeId, lang);
                        mapVO.setPayment(list==null?new ArrayList():list);
                    }else if( OptionEnum.TAX_TYPE == opEnum ){
                        // NA
                    }else if( OptionEnum.SELLER_VENDOR == opEnum ){
                        List<LongOptionVO> list = vendorFacade.findVendorOptions(storeId, lang);
                        mapVO.setSellerVendor(list==null?new ArrayList():list);
                    }else if( OptionEnum.DEALERS == opEnum ){// 台泥經銷商(只取是賣家的帳號)
                        List<LongOptionVO> list = memberFacade.findTccDealerOptions(locale, lang);
                        mapVO.setDealers(list==null?new ArrayList():list);
                    }else if( OptionEnum.FAV_CARNO == opEnum ){
                        List<StrOptionVO> list = cusAddrFacade.findCarNoOptions(memberId, storeId);
                        mapVO.setFavCars(list==null?new ArrayList():list);
                    // ENUM (新增時記得 genStrOptions() 要加入此 enum)
                    // 部分 ENUM ITEMS
                    }else if( !admin && OptionEnum.PRD_STATUS_SELLER == opEnum ){
                        List list = genOptions(key, locale, admin);
                        ExtBeanUtils.setProperty(mapVO, key, list==null?new ArrayList():list);
                    }else if( admin && OptionEnum.PRD_STATUS_ADMIN == opEnum ){
                        List list = genOptions(key, locale, admin);
                        ExtBeanUtils.setProperty(mapVO, key, list==null?new ArrayList():list);
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
            sys.processUnknowException(member, methodName, e);
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
        List<StrOptionVO> list = new ArrayList<StrOptionVO>();
        if(enumClass == ProductStatusEnum.class){
            if( adminUser==null ){
                for(ProductStatusEnum item : ProductStatusEnum.values()){
                    list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
                }
            }else if( adminUser ){
                for(ProductStatusEnum item : ProductStatusEnum.values()){
                    if( item.isForAdmin() ){
                        list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
                    }
                }
            }else{
                for(ProductStatusEnum item : ProductStatusEnum.values()){
                    if( item.isForSeller() ){
                        list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
                    }
                }
            }
        }else if(enumClass == PayStatusEnum.class){
            for(PayStatusEnum item : PayStatusEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == ShipStatusEnum.class){
            for(ShipStatusEnum item : ShipStatusEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == OrderStatusEnum.class){
            for(OrderStatusEnum item : OrderStatusEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == RfqStatusEnum.class){
            for(RfqStatusEnum item : RfqStatusEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == MemberTypeEnum.class){
            for(MemberTypeEnum item : MemberTypeEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == StoreTypeEnum.class){
            for(StoreTypeEnum item : StoreTypeEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == OrderSrcEnum.class){
            for(OrderSrcEnum item : OrderSrcEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == StockEnum.class){
            for(StockEnum item : StockEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == ShipMethodEnum.class){
            for(ShipMethodEnum item : ShipMethodEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == PayMethodEnum.class){
            for(PayMethodEnum item : PayMethodEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == CusCreditEnum.class){
            for(CusCreditEnum item : CusCreditEnum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        // for EC1.0
        }else if(enumClass == TranTypeEC10Enum.class){
            for(TranTypeEC10Enum item : TranTypeEC10Enum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else if(enumClass == ShipMethodEC10Enum.class){
            for(ShipMethodEC10Enum item : ShipMethodEC10Enum.values()){
                list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
            }
        }else{
            logger.error("genStrOptions not supported enumClass = "+enumClass);
        }

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

    //<editor-fold defaultstate="collapsed" desc="for 轉EC1.0訂單-系統各類選單選項">
    /**
     * /services/ops/ec10
     * @param request
     * @param formVO
     * @return 
     */
    @POST
    @Path("/ec10")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response findOptionsEC10(@Context HttpServletRequest request, SubmitVO formVO){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("findOptionsEC10 ..");
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
            if( formVO==null || formVO.getKeys()==null ){
                logger.error("findOptionsEC10 formVO==null");
                return Response.notAcceptable(null).build();
            }
            Long storeId = admin?formVO.getStoreId():store.getId();
            
            String keys = formVO.getKeys();
            OptionsMapVO mapVO = new OptionsMapVO();
            String[] keyAry = keys!=null?keys.split(","):null;
            
            Long orderId = formVO.getOrderId();
            ProductVO prdVO = orderId!=null?productFacade.findProductByOrderEC10(storeId, orderId):null;
            OrderVO orderVO = orderFacade.findById(storeId, orderId, true, locale, sys.isTrue(member.getTccDealer()));// 訂單共用資訊
            //DeliveryPlacesEC10VO deliveryPlacesVO = (orderVO!=null)?tccOrderFacade.findDeliveryPlaceById(orderVO.getDeliveryId()):null;
            
            if( keyAry!=null ){
                // === for 併單取得總量, 與訂單共用資訊 =========
                TranModeEC10Enum tranMode = TranModeEC10Enum.getFromCode(formVO.getTranMode());
                if( TranModeEC10Enum.COMBINE == tranMode ){// for 併單取得總量, 與訂單共用資訊
                    List<Long> orderList = formVO.getOrderList();
                    if( formVO.getOrderList()==null ){
                        logger.error("findOptionsEC10 formVO.getOrderList()");
                        return Response.notAcceptable(null).build();
                    }
                    BigDecimal total = BigDecimal.ZERO;
                    List<OrderVO> list = orderFacade.findByIds(storeId, orderList, locale, sys.isTrue(member.getTccDealer()));
                    if( sys.isEmpty(list) ){
                        logger.error("findOptionsEC10 error list==null");
                        return Response.notAcceptable(null).build();
                    }else{
                        for(OrderVO vo : list){
                            if( vo.getQuantity()!=null ){
                                logger.info("findOptionsEC10 vo.getQuantity() = "+vo.getQuantity());
                                total = total.add(vo.getQuantity());
                            }
                        }
                    }
                   
                    orderVO.setQuantity(total);// 總量
                    logger.info("findOptionsEC10 Quantity = "+orderVO.getQuantity());
                }
                mapVO.setSelectedOrder(orderVO);
                // ==========================================
                
                for(String key : keyAry){
                    OptionEC10Enum opEnum = OptionEC10Enum.getFromCode(key);
                    logger.debug("findOptionsEC10 opEnum = "+opEnum);
                    if( opEnum==null ){
                        continue;
                    }
                    
                    String productCode = prdVO!=null?prdVO.getCode():null;
                    logger.info("findOptionsEC10 productCode = "+productCode);
                    if( opEnum!=OptionEC10Enum.COMBINE_OP && productCode==null){
                        logger.error("findOptionsEC10 productCode==null");
                        return Response.notAcceptable(null).build();
                    }
                    
                    if( opEnum==OptionEC10Enum.DELIVERY_DATE ){
                        List<StrOptionVO> list = new ArrayList<StrOptionVO>();
                        String d1 = DateUtils.formatDate(DateUtils.getToday());
                        String d2 = DateUtils.formatDate(DateUtils.addDate(DateUtils.getToday(), 1));
                        list.add(new StrOptionVO(d1, d1));// 今日
                        list.add(new StrOptionVO(d2, d2));// 明日
                        ExtBeanUtils.setProperty(mapVO, key+"EC10", list);
                        logger.debug("findOptionsEC10 key="+key+", list = "+list.size());
                    }else if( opEnum==OptionEC10Enum.COMBINE_OP ){
                        List<StrOptionVO> list = tccOrderFacade.findCanCombineOptions(storeId);
                        ExtBeanUtils.setProperty(mapVO, key+"EC10", list);
                        logger.debug("findOptionsEC10 key="+key+", list = "+list.size());
                    // 所有 ENUM ITEMS
                    }else if( opEnum.getEnumClass()!=null ){
                        List<StrOptionVO> list = null;
                        if( OptionEC10Enum.SHIP_METHOD == opEnum && sys.isValidId(formVO.getContractId()) ){// 提貨方式
                            // 合約限制提貨方式
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setContractId(formVO.getContractId());
                            List<ContractProductVO> cpList = tccOrderFacade.findContractProductInfos(criteriaVO);
                            List<String> methods = new ArrayList<String>();
                            if( cpList!=null ){
                                for(ContractProductVO cpVO : cpList){
                                    if( StringUtils.isNotBlank(cpVO.getMethod()) ){
                                        methods.add(cpVO.getMethod());
                                    }
                                }
                            }
                            list = new ArrayList<StrOptionVO>();
                            for(ShipMethodEC10Enum item : ShipMethodEC10Enum.values()){
                                if( methods.contains(item.getCode()) ){
                                    list.add(new StrOptionVO(item.getCode(), item.getDisplayName(locale)));
                                }
                            }
                        }else{
                            list = genOptionsEC10(key, locale, null);
                        }
                        ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        logger.debug("findOptionsEC10 key="+key+", list = "+(list!=null?list.size():0));
                    // 獨立 TABLE
                    }else if( OptionEC10Enum.CUSTOMER == opEnum ){// 客戶
                        if( member!=null && member.getLoginAccount()!=null ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setLoginAccount(member.getLoginAccount());
                            List<LongOptionVO> list = tccOrderFacade.findCustomerOptions(member.getLoginAccount());
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }
                    }else if( OptionEC10Enum.CONTRACT == opEnum ){// 合約
                        if( formVO.getCustomerId()!=null && productCode!=null ){
                            List<LongOptionVO> list = tccOrderFacade.findContractOptions(formVO.getCustomerId(), productCode);
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }
                    }else if( OptionEC10Enum.PROVINCE == opEnum ){// 省
                        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                        List<StrOptionVO> list = tccOrderFacade.findProvinceOptions(criteriaVO);
                        ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                    }else if( OptionEC10Enum.CITY == opEnum ){// 市
                        if( StringUtils.isNotBlank(formVO.getProvince()) ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setProvince(formVO.getProvince());
                            List<StrOptionVO> list = tccOrderFacade.findCityOptions(criteriaVO);
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }else if( orderVO!=null ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setProvince(orderVO.getProvince());
                            List<StrOptionVO> list = tccOrderFacade.findCityOptions(criteriaVO);
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }
                    }else if( OptionEC10Enum.DISTRICT == opEnum ){// 區
                        if( StringUtils.isNotBlank(formVO.getProvince()) && StringUtils.isNotBlank(formVO.getCity()) ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setProvince(formVO.getProvince());
                            criteriaVO.setCity(formVO.getCity());
                            List<StrOptionVO> list = tccOrderFacade.findDistrictOptions(criteriaVO);
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }else if( orderVO!=null ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setProvince(orderVO.getProvince());
                            criteriaVO.setCity(orderVO.getCity());
                            List<StrOptionVO> list = tccOrderFacade.findDistrictOptions(criteriaVO);
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }
                    }else if( OptionEC10Enum.TOWN == opEnum ){// 鎮
                        if( StringUtils.isNotBlank(formVO.getProvince()) && StringUtils.isNotBlank(formVO.getCity()) && StringUtils.isNotBlank(formVO.getDistrict()) ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setProvince(formVO.getProvince());
                            criteriaVO.setCity(formVO.getCity());
                            criteriaVO.setDistrict(formVO.getDistrict());
                            List<StrOptionVO> list = tccOrderFacade.findTownOptions(criteriaVO);
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }else if( orderVO!=null ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setProvince(orderVO.getProvince());
                            criteriaVO.setCity(orderVO.getCity());
                            criteriaVO.setDistrict(orderVO.getDistrict());
                            List<StrOptionVO> list = tccOrderFacade.findTownOptions(criteriaVO);
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }
                    }else if( OptionEC10Enum.SALES_AREA == opEnum ){
                        // for 一般採購
                        /*if( StringUtils.isNotBlank(formVO.getProvince()) && StringUtils.isNotBlank(formVO.getCity()) && StringUtils.isNotBlank(formVO.getDistrict()) && StringUtils.isNotBlank(formVO.getTown()) ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            criteriaVO.setProvince(formVO.getProvince());
                            criteriaVO.setCity(formVO.getCity());
                            criteriaVO.setDistrict(formVO.getDistrict());
                            SalesAreaE10VO vo = tccOrderFacade.findSalesAreaByDeliveryPlace(criteriaVO);
                            List list = null;
                            if( vo!=null ){
                                list = new ArrayList();
                                list.add(vo);
                            }
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }*/
                    }else if( OptionEC10Enum.PLANT == opEnum ){// 出貨廠
                        SalesAreaE10VO vo = null;
                        if( StringUtils.isNotBlank(formVO.getProvince()) && StringUtils.isNotBlank(formVO.getCity()) 
                         && StringUtils.isNotBlank(formVO.getDistrict()) && StringUtils.isNotBlank(formVO.getTown()) ){
                            OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
                            ExtBeanUtils.copyProperties(criteriaVO, formVO);
                            vo = tccOrderFacade.findSalesArea(criteriaVO);
                        }else if( orderVO!=null ){
                            vo = tccOrderFacade.findSalesAreaById(orderVO.getSalesareaId());
                        }
                        if( vo!=null ){
                            // for 一般採購
                            if( productCode!=null && formVO.getContractId()==null ){
                                logger.debug("findOptionsEC10 PLANT SalesAreaE10VO "+vo.getId()+":"+vo.getCode());
                                List<LongOptionVO> list = tccOrderFacade.findPlantOptions(productCode, vo.getId(), null);
                                ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                            // for 合約採購
                            }else if( productCode!=null && formVO.getContractId()!=null ){
                                logger.debug("findOptionsEC10 ProductCode="+productCode+", ContractId="+formVO.getContractId());
                                List<LongOptionVO> list = tccOrderFacade.findPlantOptions(productCode, vo.getId(), formVO.getContractId());
                                ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                            }
                        }
                    }else if( OptionEC10Enum.SALES == opEnum ){
                        if( formVO.getCustomerId()!=null ){
                            List<LongOptionVO> list = tccOrderFacade.findSalesOptions(formVO.getCustomerId());
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        } 
                    }else if( OptionEC10Enum.CUS_ADDR == opEnum ){
                        if( member!=null && storeId!=null ){
                            List<LongOptionVO> list = cusAddrFacade.findCusAddrOptions(member.getId(), storeId);
                            ExtBeanUtils.setProperty(mapVO, key+"EC10", list==null?new ArrayList():list);
                        }
                    }
                }// end of for

                return this.genSuccessRepsone(request, mapVO);
            }// end of if
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    public List genOptionsEC10(String key, Locale locale, Boolean adminUser){
        OptionEC10Enum opEnum = OptionEC10Enum.getFromCode(key);
        if( opEnum.getEnumClass()!=null ){
            return genStrOptions(opEnum.getEnumClass(), locale, adminUser);
        }
        
        logger.error("genOptionsEC10 error key = "+key);
        return null;
    }
    //<editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for EC_OPTION">
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
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            boolean admin = hasAdminRights(request, member);
            storeId = admin?storeId:(store!=null?store.getId():null);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if( storeId == null ){
                logger.error("findOptionsByType storeId==null ");
                return this.genFailRepsone(request); 
            }
            List<OptionVO> list = optionFacade.findByType(storeId, type);
            
            return this.genSuccessRepsoneWithList(request, list);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
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
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            storeId = admin?storeId:(store!=null?store.getId():null);
            // 賣家、管理員共用 RESTful
            if( !checkPermissions(methodName, member, store, admin, false, false) ){// hasAdminRights, forSellerOnly, forAdminOnly
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
                    EcOption entity = vo.getId()!=null? optionFacade.find(vo.getId()):new EcOption();
                    if( entity!=null ){
                        entity.setDisabled(entity.getDisabled()==null?false:entity.getDisabled());
                        entity.setSortnum(sortnum);
                        entity.setCname(vo.getCname());
                        entity.setStoreId(storeId);
                        entity.setType(type);
                        
                        if( optionFacade.checkInput(entity, member, locale, errors) ){
                            optionFacade.save(entity, member, false); 
                        }else{
                            return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
                        }
                    }
                }
            }
            
            return findOptionsByType(request, type, storeId);
            //return findOptions(request, type, storeId);
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
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
        EcMember member = null;
        try{
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            boolean admin = hasAdminRights(request, member);
            storeId = admin?storeId:(store!=null?store.getId():null);
            EcOption entity = null;
            if( formVO.getId()!=null && formVO.getId()>0 && type!=null && storeId!=null){
                entity = optionFacade.find(formVO.getId());
            }else{
                logger.error("removeOption entity==null, id = "+formVO.getId());
                return Response.notAcceptable(null).build();
            }
            
            BaseResponseVO resVO = null;
            if( entity!=null ){
                // 商店ID驗證
                if( !storeId.equals(entity.getStoreId()) ){
                    logger.error("removeOption storeId error : "+storeId+" != "+entity.getStoreId());
                    return Response.notAcceptable(null).build();
                }
                if( !type.equals(entity.getType()) ){
                    logger.error("removeOption type error : "+type+" != "+entity.getType());
                    return Response.notAcceptable(null).build();
                }
                
                entity.setDisabled(Boolean.TRUE);// 非真實刪除
                optionFacade.save(entity, member, false);
                
                //return this.genSuccessRepsoneWithId(request, formVO.getId());
                return findOptionsByType(request, type, storeId);
            }else{
                logger.error("removeOption not exists id = "+formVO.getId());
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
        }catch(Exception e){
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    //</editor-fold>

}

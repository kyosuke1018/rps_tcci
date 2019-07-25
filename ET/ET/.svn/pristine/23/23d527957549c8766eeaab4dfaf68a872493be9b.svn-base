/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.enums.LocaleEnum;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.facade.admin.CmFactorygroupFacade;
import com.tcci.cm.facade.admin.CmUserfactoryFacade;
import com.tcci.cm.facade.admin.PermissionFacade;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.entity.EtMember;
import com.tcci.et.enums.rs.ResStatusEnum;
import com.tcci.et.facade.EtMemberFacade;
import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.util.WebUtils;
import com.tcci.et.facade.EtOptionFacade;
//import com.tcci.ec.facade.global.SysResourcesFacade;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.MemberVenderVO;
import com.tcci.et.model.rs.AuthVO;
import com.tcci.et.model.rs.BaseListResponseVO;
import com.tcci.et.model.rs.BaseResponseVO;
import com.tcci.et.model.rs.ResponseVO;
import com.tcci.et.model.rs.SubmitVO;
import com.tcci.et.controller.EtSessionController;
import com.tcci.et.enums.BpmRoleEnum;
import com.tcci.et.facade.EtMemberFormFacade;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.et.facade.EtUseLogFacade;
import com.tcci.et.facade.EtVenderCategoryFacade;
import com.tcci.et.facade.EtMemberVenderFacade;
import com.tcci.et.facade.rs.RestDataFacade;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.security.SecurityConstants;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
//import com.tcci.security.TokenProvider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.faces.bean.ManagedProperty;
import javax.security.enterprise.CallerPrincipal;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public abstract class AbstractWebREST {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    //@Inject 
    //protected KeyGenerator keyGenerator;
    //@Inject 
    //protected SecurityContext securityContext;
//    @Inject
//    protected TokenProvider tokenProvider;
    
    @ManagedProperty(value="#{etSessionController}")
    protected EtSessionController sessionController;
    public EtSessionController getSessionController() {
        return sessionController;
    }
    
    public void setSessionController(EtSessionController sessionController) {
        this.sessionController = sessionController;
    }
   
    protected @EJB SysResourcesFacade sys;
    protected @EJB ImageFacade img;
    protected @EJB TcUserFacade userFacade;
    protected @EJB EtUseLogFacade useLogFacade;
    protected @EJB PermissionFacade permissionFacade;
    protected @EJB CmFactoryFacade cmFactoryFacade;
    protected @EJB CmUserfactoryFacade cmUserfactoryFacade;
    protected @EJB CmFactorygroupFacade cmFactorygroupFacade;

    protected @EJB EtMemberFacade memberFacade;
    protected @EJB EtMemberFormFacade memberFormFacade;
    protected @EJB RestDataFacade restDataFacade;
    protected @EJB EtOptionFacade etOptionFacade;
    protected @EJB EtTenderFacade etTenderFacade;
    protected @EJB EtMemberVenderFacade etMemberVenderFacade;
    protected @EJB EtVenderCategoryFacade etVenderCategoryFacade;

    protected String CLIENT_LANGUAGE_CUS = "Custom-Language";
    protected String CLIENT_LANGUAGE = "Accept-Language";
    
    protected String EMPTY_LIST = "[]";
    protected String EMPTY_OBJECT = "{}";
    
    protected Map<String, String> sortFieldMap;
    protected Map<String, String> sortOrderMap;
    
    public boolean checkVenderPermissions(String method, EtMember member
            , TenderVO tender, boolean hasAdminRights){
        // 無登入者資訊
        if( member==null || member.getId()==null ){
            logger.error("{} checkVenderPermissions error 1", method);
            return false;
        }
        
        List<MemberVO> list = memberFacade.checkMappingMemberAndCategoryId(tender.getCategoryId(), member.getId());
        if (CollectionUtils.isNotEmpty(list)) {
            
        }else{
            return false;
        }
        
        // 無管理員權限
//        if( !hasAdminRights){
//            logger.error("{} checkVenderPermissions error 2", method);
//            return false;
//        }
        return true;
    }
    
    public boolean checkPermissions(String method, EtMember member
            , boolean hasAdminRights, boolean forSellerOnly, boolean forAdminOnly){
        // 無登入者資訊
        if( member==null || member.getId()==null ){
            logger.error("{} checkPermissions error 1", method);
            return false;
        }
        // 無管理員權限
        if( !hasAdminRights){
            logger.error("{} checkPermissions error 2", method);
            return false;
        }
        // 管理員專屬功能，卻無管理員權限
        if( forAdminOnly && !hasAdminRights ){
            logger.error("{} checkPermissions error 4", method);
            return false;
        }
        return true;
    }
    
    public AuthVO genAuthVO(HttpServletRequest request, MemberVO member, String token, Boolean adminUser
            , Boolean tccDealer, Boolean storeOwner, Boolean fiUser){
        AuthVO resVO = new AuthVO();
//        if( member==null || member.getMemberId()==null || token==null || token.isEmpty() ){
        if( member==null || member.getMemberId()==null ){
            BaseResponseVO res = this.genBaseFailRepsone(request, ResStatusEnum.IN_ERROR_LOGIN);
            resVO.setRes(res.getRes());
            resVO.setAdminUser(adminUser);
            resVO.setTccDealer(tccDealer);
            resVO.setLogin(Boolean.FALSE);
        }else{
            BaseResponseVO res = genBaseSuccessRepsone(request);
            resVO.setRes(res.getRes());
            resVO.setLoginAccount(member.getLoginAccount());
            resVO.setMemberId(member.getMemberId());
            resVO.setSellerId(member.getSellerId());
            resVO.setStoreId(member.getStoreId());
            resVO.setStores(member.getStores());
            resVO.setManager(member.isManager());// 目前是否為商店管理員身分(對應 storeId)
            resVO.setAdminUser(adminUser);
            resVO.setTccDealer(tccDealer);
            resVO.setStoreOwner(storeOwner);
            resVO.setFiUser(fiUser);
            resVO.setToken(token);// js XMLHttpRequest 無法取的部分 response header 此處要回傳
            resVO.setLogin(Boolean.TRUE);
        }
        return resVO;
    }
    
    public String getResourceTxt(HttpServletRequest request, String key){
        ResourceBundle bundle = ResourceBundle.getBundle(GlobalConstant.DEF_RESOURCE_BUNDLE, getLocale(request));
        String value = bundle.getString(key);
        return value;
    }
    
    public Locale getLocale(HttpServletRequest request){
        LocaleEnum localeEnum = LocaleEnum.SIMPLIFIED_CHINESE;
        
        String language = request.getHeader(CLIENT_LANGUAGE_CUS);
        if( language==null ){
            language = request.getHeader(CLIENT_LANGUAGE);
            if( language!=null && language.indexOf(",")>0 ){
                language = language.substring(0, language.indexOf(",")).trim();
            }
        }
        logger.info("getLocale language = {}", language);
        localeEnum = LocaleEnum.getFromCode(language, LocaleEnum.SIMPLIFIED_CHINESE);
        logger.info("getLocale localeEnum = {}", localeEnum);
        return localeEnum!=null?localeEnum.getLocale():LocaleEnum.SIMPLIFIED_CHINESE.getLocale();
    }
    
    public EtMember getReqUser(HttpServletRequest request){// 取得執行人資訊
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        logger.info("getReqUser authHeader = {}", authHeader);
//        if( authHeader!=null && authHeader.startsWith(TokenProvider.BEARER) ){
//            String jwt = authHeader.substring(TokenProvider.BEARER.length());
//            Long memberId = (Long)tokenProvider.getClaim(jwt, TokenProvider.MEMBER_ID);
//            logger.debug("getReqUser memberId = "+memberId);
//            return memberFacade.find(memberId);
//        }
//        return null;
//        return this.sessionController.getLoginMember();// sessionController is null
        HttpSession session = request.getSession(false);
        if( isLogin(session) ){
            CallerPrincipal caller = (CallerPrincipal)session.getAttribute(SecurityConstants.CALLER_ATTR);// CallerPrincipal
            if(caller!=null){
                return memberFacade.findByLoginAccount(caller.getName());
            }
        }
        return null;
    }
    
    private boolean isLogin(HttpSession session){
        return session!=null 
            && session.getAttribute(SecurityConstants.CALLER_ATTR)!=null;
    }
   
    /**
     * [從管理者後台登入] 且 [有系統管理員權限]
     * @param request
     * @param member
     * @return 
     */
    public boolean hasAdminRights(HttpServletRequest request, EtMember member){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if( member!=null && authHeader!=null && authHeader.startsWith(TokenProvider.BEARER) ){
//            String jwt = authHeader.substring(TokenProvider.BEARER.length());
//            Object obj = tokenProvider.getClaim(jwt, TokenProvider.ADMIN_USER);// 從管理者後台登入
//            if( obj!=null ){
//                Boolean forAdmin = (Boolean)obj;
////                Boolean adminUser = member.getAdminUser();// 有系統管理員權限
////                logger.debug("hasAdminRights forAdmin = "+forAdmin+", member.getAdminUser = "+member.getAdminUser());
//                Boolean adminUser = Boolean.TRUE;
//
//                if( forAdmin && adminUser!=null && adminUser ){
//                    return true;
//                }
//            }
//        }
        return false;
    }

    /**
     * 是否是台泥經銷商
     * @param request
     * @param member
     * @return 
     */
    public boolean isTccDealer(HttpServletRequest request, EtMember member){
        return false;
    }
    
    /**
     * log 輸入資訊
     * @param methodName
     * @param vo 
     * @param operator 
     */
    public void logInputs(String methodName, SubmitVO vo, EtMember operator){
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== BEGIN : ").append(methodName).append("======\n");
        if( operator!=null ){
            sb.append("=== OPERATOR : memberId = ").append(operator.getId()).append("　===").append("\n");
        }
        sb.append(serialize(vo, operator));
        sb.append("\n=== END : ").append(methodName).append("======");
        logger.info(sb.toString());
    }
    /**
     * 
     * @param vo
     * @param operator
     * @return 
     */
    public String serialize(SubmitVO vo, EtMember operator){
        if( vo==null ){
            return EMPTY_OBJECT;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleDateFormat df = new SimpleDateFormat(GlobalConstant.FORMAT_DATETIME);
        mapper.setDateFormat(df);
        
        String jsonInString = null;
        try{
            if( mapper.canSerialize(SubmitVO.class) ){
                jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
            }
        }catch(JsonProcessingException e){
//            sys.processUnknowException(operator, "serialize", e);
        }
        return jsonInString;
    }
    
    public String getHomePage(HttpServletRequest request){
        return GlobalConstant.URL_HOME_PAGE;
    }
    
    //簽核人員 1:PLANT_MM, 2:HZ_MM or HQ_MM
    public Map<String, Object> reloadApprovers(CmFactory factory){
        Map<String, Object> roleApprovers = new HashMap<>();
        
        boolean isHZ = cmFactorygroupFacade.isSubFactory("1", "3010", factory.getCode()) || cmFactorygroupFacade.isSubFactory("1", "HZCN", factory.getCode());
        boolean isHQ = cmFactorygroupFacade.isSubFactory("1", "1010", factory.getCode()) || cmFactorygroupFacade.isSubFactory("1", "TCDCN", factory.getCode());
        if(!isHZ && !isHQ){
            return null;
        }
        
        List<TcUser> plantMM = permissionFacade.findUsersByRole(BpmRoleEnum.PLANT_MM.name());//PLANT_MM
        if(CollectionUtils.isNotEmpty(plantMM)){
            List<TcUser> approvers = new ArrayList<>();
            for(TcUser user : plantMM){
                user = userFacade.findUserByLoginAccount(user.getLoginAccount());//不這樣再查一次 取不到TC_USER的CmUsercompany、CmUserfactory
                List<CmFactory> owenerfactoryList = cmUserfactoryFacade.findUserFactoryPermission(user);
                if(CollectionUtils.isNotEmpty(owenerfactoryList) && owenerfactoryList.contains(factory)){
                    logger.info("Factory approvers:"+user.getDisplayIdentifier());
                    approvers.add(user);
                }
            }
            if(CollectionUtils.isNotEmpty(approvers)){
                roleApprovers.put(BpmRoleEnum.PLANT_MM.name(), approvers);
            }
//            roleApprovers.put(BpmRoleEnum.PLANT_MM.name(), plantMM);
        }  
//        boolean isHZ = cmFactorygroupFacade.isSubFactory("1", "HZCN", factory.getCode());
        List<TcUser> approvers = new ArrayList<>();
        if(isHZ){
            approvers = permissionFacade.findUsersByRole(BpmRoleEnum.HZ_MM.name());
            if(CollectionUtils.isNotEmpty(approvers)){
                roleApprovers.put(BpmRoleEnum.HZ_MM.name(), approvers);
            }
        }else{//TCDCN
            approvers = permissionFacade.findUsersByRole(BpmRoleEnum.HQ_MM.name());
            if(CollectionUtils.isNotEmpty(approvers)){
                roleApprovers.put(BpmRoleEnum.HQ_MM.name(), approvers);
            }
        }
        
        return roleApprovers;
    }
    
    //<editor-fold defaultstate="collapsed" desc="for MultiPart">
    /**
     * Get MultiPart parameter value
     * @param multiPart
     * @param key
     * @return 
     */
    public String getMultiPartValue(FormDataMultiPart multiPart, String key){
        FormDataBodyPart part = multiPart.getField(key);
        String value = part!=null?part.getValue():null;
        logger.debug("getMultiPartValue key = "+key+", value = "+value);
        return value;
    }
    public Long getMultiPartLong(FormDataMultiPart multiPart, String key){
        FormDataBodyPart idIn = multiPart.getField(key);
        return (idIn!=null)?Long.parseLong(idIn.getValue()):null;
    }
    public Date getMultiPartDate(FormDataMultiPart multiPart, String key){
        FormDataBodyPart dtIn = multiPart.getField(key);
        return (dtIn!=null)? DateUtils.getCommonDateTime(dtIn.getValue(), " "):null;
    }
    public Boolean getMultiPartBoolean(FormDataMultiPart multiPart, String key, Boolean def){
        FormDataBodyPart boolIn = multiPart.getField(key);
        return (boolIn!=null)?boolIn.getValueAs(Boolean.class):def;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Repsone">
    public Response genUnauthorizedResponse(){
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    
    public Response genFailRepsone(HttpServletRequest request){
        return Response.ok(genBaseFailRepsone(request, ResStatusEnum.FAIL), MediaType.APPLICATION_JSON).build();
    }
    public Response genFailRepsone(HttpServletRequest request, ResStatusEnum failEnum, List<String> errors){
        return Response.ok(this.genFailRepsoneVO(request, failEnum, errors), MediaType.APPLICATION_JSON).build();
    }
    
    public Response genSuccessRepsone(HttpServletRequest request){
        return Response.ok(genBaseSuccessRepsone(request), MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    public Response genSuccessRepsone(HttpServletRequest request, BaseResponseVO res){// for return extends BaseResponseVO object
        res.setRes(this.genSuccessRepsoneVO(request));
        return Response.ok(res, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    public Response genSuccessRepsoneWithId(HttpServletRequest request, Long id){
        return Response.ok(this.genSuccessRepsoneVO(request, id), MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    public Response genSuccessRepsoneWithList(HttpServletRequest request, List list){
        return Response.ok(this.genSuccessListRepsone(request, list), MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    public Response genSuccessRepsoneWithCount(HttpServletRequest request, int totalRows){
        return Response.ok(this.genCountSuccessRepsone(request, totalRows), MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    public Response genSuccessRepsoneWithTotal(HttpServletRequest request, Double total){
        BaseResponseVO vo = genBaseSuccessRepsone(request);
        vo.getRes().setTotal(total);
        return Response.ok(vo, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    
    public ResponseVO genRepsoneVO(HttpServletRequest request, ResStatusEnum status, 
            Long id, Integer totalRows,
            String msg, List<String> errors){
        ResponseVO res = new ResponseVO();
        res.setHost(WebUtils.getHostAddress());
        res.setStatus(status.getCode());
        res.setId(id);
        res.setTotalRows(totalRows);
        res.setMsg(msg);
        res.setErrors(errors);
        res.setUrlPrefix(sys.getRestUrlPrefix());//WebUtils.getUrlPrefix(request);
        return res;
    }
    
    public ResponseVO genSuccessRepsoneVO(HttpServletRequest request){
        return genRepsoneVO(request, ResStatusEnum.SUCCESS, null, null, null, null);
    }
    
    public BaseResponseVO genBaseSuccessRepsone(HttpServletRequest request){
        BaseResponseVO vo = new BaseResponseVO();
        vo.setRes(genSuccessRepsoneVO(request));
        return vo;
    }
    
    public BaseResponseVO genSuccessRepsoneVO(HttpServletRequest request, Long id){
        BaseResponseVO vo = genBaseSuccessRepsone(request);
        vo.getRes().setId(id);
        return vo;
    }
    
    public BaseListResponseVO genSuccessListRepsone(HttpServletRequest request, List list){
        BaseListResponseVO vo = new BaseListResponseVO();
        vo.setRes(genSuccessRepsoneVO(request));
        vo.setList(list);
        vo.setRowNum((list!=null)?list.size():0);
        return vo;
    }
    
    public BaseResponseVO genCountSuccessRepsone(HttpServletRequest request, int totalRows){
        BaseResponseVO vo = genBaseSuccessRepsone(request);
        vo.getRes().setTotalRows(totalRows);
        
        return vo;
    }
    
    public BaseResponseVO genBaseFailRepsone(HttpServletRequest request, ResStatusEnum status){
        BaseResponseVO vo = new BaseResponseVO();
        ResponseVO res = new ResponseVO();
        res.setHost(WebUtils.getHostAddress());
        res.setStatus(status.getCode());
        res.setMsg(status.getDisplayName(getLocale(request)));
        vo.setRes(res);

        return vo;
    }
    
    public BaseResponseVO genFailRepsoneVO(HttpServletRequest request, ResStatusEnum status, List<String> errors){
        BaseResponseVO vo = genBaseFailRepsone(request, status);
        vo.getRes().setErrors(errors);

        return vo;
    }
    //</editor-fold>
}

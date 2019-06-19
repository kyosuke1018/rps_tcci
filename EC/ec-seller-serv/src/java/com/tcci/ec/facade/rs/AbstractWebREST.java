/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.cm.enums.LocaleEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcMemberFacade;
import com.tcci.ec.facade.EcStoreFacade;
import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.util.WebUtils;
import com.tcci.ec.facade.EcUseLogFacade;
import com.tcci.ec.facade.global.MessageFacade;
import com.tcci.ec.facade.global.SysResourcesFacade;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.rs.AuthVO;
import com.tcci.ec.model.rs.BaseListResponseVO;
import com.tcci.ec.model.rs.BaseResponseVO;
import com.tcci.ec.model.rs.ResponseVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.ResourceBundleUtils;
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
import com.tcci.security.TokenProvider;
import java.text.SimpleDateFormat;
import javax.ws.rs.core.HttpHeaders;
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
    @Inject
    protected TokenProvider tokenProvider;
   
    protected @EJB SysResourcesFacade sys;
    protected @EJB MessageFacade msg;
    protected @EJB ImageFacade img;
    protected @EJB EcUseLogFacade useLogFacade;

    protected @EJB EcMemberFacade memberFacade;
    protected @EJB EcStoreFacade storeFacade;

    protected String CLIENT_LANGUAGE_CUS = "Custom-Language";
    protected String CLIENT_LANGUAGE = "Accept-Language";
    
    protected String EMPTY_LIST = "[]";
    protected String EMPTY_OBJECT = "{}";
    
    protected Map<String, String> sortFieldMap;
    protected Map<String, String> sortOrderMap;
    
    public boolean checkPermissions(String method, EcMember member, EcStore store
            , boolean hasAdminRights, boolean forSellerOnly, boolean forAdminOnly){
        // 無登入者資訊
        if( member==null || member.getId()==null ){
            logger.error("{} checkPermissions error 1", method);
            return false;
        }
        // 無管理員權限，也無商店資訊
        if( !hasAdminRights && (store==null || store.getId()==null) ){
            logger.error("{} checkPermissions error 2", method);
            return false;
        }
        // 賣家專屬功能，卻無商店資訊
        if( forSellerOnly && (store==null || store.getId()==null) ){
            logger.error("{} checkPermissions error 3", method);
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
        if( member==null || member.getMemberId()==null || token==null || token.isEmpty() ){
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
    // 因UI可切換語系
    public String getResourceMsg(Locale locale, String key){
        return ResourceBundleUtils.getMessage(locale, key);
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
    
    public String getJWT(HttpServletRequest request){// 取得執行JWT
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if( authHeader!=null && authHeader.startsWith(TokenProvider.BEARER) ){
            String jwt = authHeader.substring(TokenProvider.BEARER.length());
            return jwt;
        }
        return null;
    }
    
    public EcMember getReqUser(HttpServletRequest request){// 取得執行人資訊
        String jwt = getJWT(request);
        if( jwt!=null ){
            Long memberId = (Long)tokenProvider.getClaim(jwt, TokenProvider.MEMBER_ID);
            logger.debug("getReqUser memberId = "+memberId);
            return memberFacade.find(memberId);
        }
        return null;
    }
   
    public EcStore getStore(HttpServletRequest request){// 取得店家
        String jwt = getJWT(request);
        if( jwt!=null ){
            Long storeId = (Long)tokenProvider.getClaim(jwt, TokenProvider.STORE_ID);
            logger.debug("getStore storeId = "+storeId);
            return storeFacade.find(storeId);
        }
        return null;
    }

    /**
     * [從管理者後台登入] 且 [有系統管理員權限]
     * @param request
     * @param member
     * @return 
     */
    public boolean hasAdminRights(HttpServletRequest request, EcMember member){
        String jwt = getJWT(request);
        if( member!=null && jwt!=null ){
            Object obj = tokenProvider.getClaim(jwt, TokenProvider.ADMIN_USER);// 從管理者後台登入
            if( obj!=null ){
                Boolean forAdmin = (Boolean)obj;
                Boolean adminUser = member.getAdminUser();// 有系統管理員權限
                logger.debug("hasAdminRights forAdmin = "+forAdmin+", member.getAdminUser = "+member.getAdminUser());

                if( forAdmin && adminUser!=null && adminUser ){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是台泥經銷商
     * @param request
     * @param member
     * @return 
     */
    public boolean isTccDealer(HttpServletRequest request, EcMember member){
        String jwt = getJWT(request);
        if( member!=null && jwt!=null ){
            Object obj = tokenProvider.getClaim(jwt, TokenProvider.TCC_DEALER);// 從台泥經銷商後台登入
            if( obj!=null ){
                Boolean forDealer = (Boolean)obj;
                Boolean tccDealer = member.getTccDealer();// 台泥經銷商
                logger.debug("isTccDealer forDealer = "+forDealer+", member.getTccDealer = "+member.getTccDealer());

                if( forDealer && tccDealer!=null && tccDealer ){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * log 輸入資訊
     * @param methodName
     * @param vo 
     * @param operator 
     */
    public void logInputs(String methodName, SubmitVO vo, EcMember operator){
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
    public String serialize(Object vo, EcMember operator){
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
            sys.processUnknowException(operator, "serialize", e);
        }
        return jsonInString;
    }
    
    public String getHomePage(HttpServletRequest request){
        return GlobalConstant.URL_HOME_PAGE;
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
    public Response genFailMsgRepsone(HttpServletRequest request, ResStatusEnum failEnum, String msg){
        return Response.ok(this.genFailRepsoneMsg(request, failEnum, msg), MediaType.APPLICATION_JSON).build();
    }
    
    public Response genSuccessRepsone(HttpServletRequest request){
        return Response.ok(genBaseSuccessRepsone(request), MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    public Response genSuccessRepsone(HttpServletRequest request, BaseResponseVO res){// for return extends BaseResponseVO object
        res.setRes(this.genSuccessRepsoneVO(request));
        return Response.ok(res, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
    }
    public Response genSuccessRepsoneWithMsg(HttpServletRequest request, String resmsg){
        BaseResponseVO resVO = this.genBaseSuccessRepsone(request);
        resVO.getRes().setMsg(resmsg);
        return Response.ok(resVO, MediaType.APPLICATION_JSON).encoding("UTF-8").build();
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
    
    public BaseResponseVO genFailRepsoneMsg(HttpServletRequest request, ResStatusEnum status, String msg){
        BaseResponseVO vo = genBaseFailRepsone(request, status);
        vo.getRes().setMsg(msg);

        return vo;
    }
    //</editor-fold>
}

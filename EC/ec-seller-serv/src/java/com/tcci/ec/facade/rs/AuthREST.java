/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.enums.SysRoleEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.rs.AuthVO;
import com.tcci.ec.model.rs.SubmitVO;
import java.util.HashSet;
import java.util.Set;
import com.tcci.security.AESPasswordHash;
import com.tcci.security.AESPasswordHashImpl;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.tcci.cm.util.AesUtil;
import com.tcci.cm.util.WebUtils;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.entity.EcUseLog;
import com.tcci.ec.enums.LogCategoryEnum;
import com.tcci.ec.enums.LogTypeEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.facade.EcCustomerFacade;
import com.tcci.ec.facade.EcSessionFacade;
import com.tcci.ec.facade.EcStoreUserFacade;
import com.tcci.ec.facade.global.AuthFacade;
import com.tcci.ec.facade.global.MessageFacade;
import com.tcci.ec.model.StoreVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.fc.util.StringUtils;
import java.util.Date;
import java.util.Locale;
import javax.ejb.EJB;
import com.tcci.security.TokenProvider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Asynchronous;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author Peter.pan
 */
@Path("/auth")
public class AuthREST extends AbstractWebREST {

    @EJB AuthFacade auth;
    @EJB MessageFacade message;
    @EJB EcCustomerFacade customerFacade;
    @EJB EcStoreUserFacade storeUserFacade;
    @EJB EcSessionFacade sessionFacade;

    /**
     * 登入
     * /services/auth/login
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        MemberVO member = null;
        try {
            logger.info("loginFrom loginAccount = " + formVO.getLoginAccount());
            logger.debug("loginFrom pwd = " + formVO.getPwd());
            Locale locale = getLocale(request);

            // 密碼處理
            String encrypted = genEncryptedPassword(formVO);
            // 會員資訊 & 可管理商店資訊
            if( GlobalConstant.SYNC_PASSWORD_EC10 ){
                memberFacade.syncPasswordFromEc10(formVO.getLoginAccount());// 同步 EC1.0 密碼
            }
            member = memberFacade.findForLogin(formVO.getLoginAccount(), encrypted, locale);
            if (member == null) {
                logger.info("loginFrom error member==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_LOGIN, null);
            }
            
            boolean forAdmin = (formVO.getForAdmin() != null && formVO.getForAdmin());
            // 依 E_MEMBER.TCC_DEALER 設定決定使用系統 EC1.5 or EC2.0
            boolean forDealer = (member.getTccDealer() != null && member.getTccDealer());

            AuthVO resVO = genAuthVOByMember(request, member, null, forAdmin, forDealer);
            if (resVO != null) {
                // 登入紀錄
                fireUseLogForLogin(request, member, forAdmin ? LogTypeEnum.LOGIN_ADMIN : LogTypeEnum.LOGIN_SELLER);
                logger.info("loginFrom success loginAccount = " + formVO.getLoginAccount());
                return this.genSuccessRepsone(request, resVO);
            } else {
                logger.info("loginFrom fail loginAccount = " + formVO.getLoginAccount());
                return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
            }
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        logger.error("loginFrom error loginAccount = " + formVO.getLoginAccount());
        return this.genFailRepsone(request);
    }

    /**
     * 供手機已登入賣家平台時 可轉址登入WEB /services/auth/loginFromExt
     *
     * ex. result url
     * http://localhost:8383/ec-seller/home.html?from=android&token=eyJqa3UiOiJodHRwOlwvXC93d3cudGFpd2FuY2VtZW50LmNvbSIsImtpZCI6IjE1Mzk5MzMzMDE2NDQiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MDEiLCJhZG1pblVzZXIiOmZhbHNlLCJyb2xlcyI6IlNFTExFUl9PV05FUixNRU1CRVIiLCJpc3MiOiJodHRwOlwvXC93d3cudGFpd2FuY2VtZW50LmNvbSIsInN0b3JlSWQiOjEsImV4cCI6MTU0MDAxOTcwMSwiaWF0IjoxNTM5OTMzMzAxLCJtZW1iZXJJZCI6NX0.9qxK0U1F3-CUB0wQFt4M3u2_fs9GIOHgLGnwBBkbjCw&loginAccount=test01
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/loginFromExt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginFromExt(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        MemberVO member = null;
        String loginAccount = null;
        try {
            logInputs(methodName, formVO, null);// log 輸入資訊
            if (formVO == null || formVO.getLoginAccount() == null || formVO.getFrom() == null || formVO.getTo() == null) {
                logger.error("loginFromExt formVO==null");
                return Response.notAcceptable(null).build();
            }
            Locale locale = getLocale(request);

            String token = formVO.getToken();
            loginAccount = formVO.getLoginAccount().trim();
            // 待傳入資訊
            Long storeId = formVO.getStoreId();
            String language = formVO.getLanguage();
            language = (language==null)? locale.toString():language;
            logger.info("loginFromExt storeId="+storeId+", language = "+language+", formVO.getLanguage() = "+formVO.getLanguage());
            
            // 呼叫 Modile Proxy 認證是否已登入
            String res = callMobileProxy(token, loginAccount, storeId, formVO.getFrom());
            logger.info("loginFromExt proxy res = " + res);

            String sellerWebUrl = sys.getSellerWebUrl(); // "http://localhost:8383/ec-seller";
            String redirectUrl = sellerWebUrl;
            member = memberFacade.findVOByLoginAccount(loginAccount, locale);

            //boolean forAdmin = (formVO.getForAdmin()!=null && formVO.getForAdmin());
            // 依 E_MEMBER.TCC_DEALER 設定決定使用系統 EC1.5 or EC2.0
            boolean forDealer = (member != null && sys.isTrue(member.getTccDealer()));

            boolean success = false;
            if (member == null) {
                logger.error("loginFromExt error member==null");
                //}else if( member.getSellerApprove()==null || !member.getSellerApprove() || member.getStoreId()==null ){
                //    logger.error("loginFromExt error member is not valid seller !");
            } else if (res == null || !res.trim().equals("SUCCESS")) {
                logger.error("loginFromExt error proxy result FAIL !");
            } else {
                boolean manager = false;// 目前是否為商店管理員身分(對應 storeId)
                Long manageStore = null;
                if (storeId != null) {
                    StoreVO storeVO = storeFacade.findById(storeId, false);
                    if (!member.getId().equals(storeVO.getMemberId())) {
                        List<Long> managers = storeUserFacade.findManagerIds(storeId);
                        if (managers.contains(member.getId())) {
                            manager = true;
                            manageStore = storeId;
                        }
                    }
                }
                member.setManager(manager);

                //boolean tccDealer = (member.getTccDealer()!=null && member.getTccDealer());
                //logger.info("loginFromExt forDealer = "+forDealer+", tccDealer = "+tccDealer);
                //if( forDealer == tccDealer ){
                AuthVO resVO = genAuthVOByMember(request, member, manageStore, false, forDealer);
                if (resVO != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(StringUtils.isNotBlank(formVO.getTo())? redirectUrl + formVO.getTo() : redirectUrl);
                    sb.append("?from=android&token=").append(resVO.getToken());
                    sb.append("&loginAccount=").append(resVO.getLoginAccount());
                    sb.append("&tccDealer=").append(forDealer);
                    if( storeId!=null ){
                        sb.append("&storeId=").append(storeId);
                    }
                    if( language!=null ){
                        sb.append("&language=").append(language);
                    }
                    if( resVO.getStoreOwner()!=null ){
                        sb.append("&storeOwner=").append(resVO.getStoreOwner());
                    }
                    if( resVO.getFiUser()!=null ){
                        sb.append("&fiUser=").append(resVO.getFiUser());
                    }
                    redirectUrl = sb.toString();
                    success = true;
                } else {
                    logger.error("loginFromExt error resVO==null");
                }
            }
            if (!success) {
                // 直接導至 http://localhost:8383/ec-seller/login.html
                String loginURL = forDealer ? GlobalConstant.URL_DEALER_LOGIN_PAGE : GlobalConstant.URL_SELLER_LOGIN_PAGE;
                redirectUrl = sellerWebUrl + loginURL + "?from=android&loginAccount=" + loginAccount;
            }

            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("url", redirectUrl);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = objectMapper.writeValueAsString(resMap);

            logger.info("loginFromExt loginAccount="+loginAccount+", redirectUrl="+redirectUrl);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        logger.error("loginFromExt error loginAccount="+loginAccount);
        return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
    }
    
    /**
     * 內部登入 for ec-amin link to ec-seller 
     * /services/auth/internalLogin
     * 
     * @param request
     * @param code
     * @return
     */
    @POST
    @Path("/internalLogin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkInternalLogin(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        MemberVO member = null;
        try {
            String loginAccount = formVO.getLoginAccount();
            String sessionKey = formVO.getToken();
            logger.info("checkInternalLogin loginAccount = " + loginAccount + ", sessionKey = "+sessionKey);
            Locale locale = getLocale(request);

            if(loginAccount!=null && sessionKey!=null) {
                member = memberFacade.findForInternalLogin(loginAccount, sessionKey, locale);
                if (member == null ){
                    logger.error("checkInternalLogin error member=" + member);
                    return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
                }

                Boolean forAdmin = true;
                Boolean forDealer = false;
                AuthVO resVO = genAuthVOByMember(request, member, null, forAdmin, forDealer);
                if (resVO != null) {
                    // 登入紀錄
                    fireUseLogForLogin(request, member, forAdmin ? LogTypeEnum.LOGIN_ADMIN : LogTypeEnum.LOGIN_SELLER);
                    logger.info("checkInternalLogin success loginAccount = " + loginAccount);
                    return this.genSuccessRepsone(request, resVO);
                } else {
                    logger.info("checkInternalLogin fail loginAccount = " + loginAccount);
                    return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
                }
            }
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * /auth/reset 重設密碼
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgetPassword(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        EcMember member = null;
        try {
            Locale locale = getLocale(request);
            if (formVO == null || formVO.getLoginAccount() == null) {
                logger.error("forgetPassword formVO==null");
                return Response.notAcceptable(null).build();
            }
            logInputs(methodName, formVO, null);// log 輸入資訊

            // 帳號資訊
            member = memberFacade.findByLoginAccount(formVO.getLoginAccount());
            if (member == null || member.getPhone() == null) {
                logger.error("forgetPassword member==null || member.getPhone()==null !");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }

            // 產生重設密碼 a ~ z
            String plaintext = StringUtils.genRandomString(97, 122, 6);// 六碼小寫英文
            AESPasswordHash aes = new AESPasswordHashImpl();
            String encrypted = aes.encrypt(plaintext);

            // SMS
            logger.info("forgetPassword before sendMsg ...");
            if( message.resetPassword(member, plaintext, member, locale) ){
                logger.info("forgetPassword before save DB ...");
                // save DB
                member.setPassword(encrypted);
                member.setResetPwd(encrypted);
                member.setResetPwdExpired(DateUtils.addDate(new Date(), 3));
                memberFacade.save(member, member, false);

                return this.genSuccessRepsone(request);
            } else {
                return this.genFailRepsone(request);
            }
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /auth/switchStore 切換管理商店
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/switchStore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response switchStore(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        logger.debug("switchStore ...");
        List<String> errors = new ArrayList<String>();
        EcMember member = null;
        try {
            member = getReqUser(request);
            EcStore store = getStore(request);
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            //boolean admin = hasAdminRights(request, member);
            boolean tccDealer = isTccDealer(request, member);
            // 賣家專屬 RESTful
            if (!checkPermissions(methodName, member, store, false, true, false)) {// hasAdminRights, forSellerOnly, forAdminOnly
                return genUnauthorizedResponse();
            }
            if (formVO == null || formVO.getStoreId() == null) {
                logger.error("switchStore formVO==null");
                return Response.notAcceptable(null).build();
            }

            Long selStoreId = formVO.getStoreId();
            StoreVO storeVO = storeFacade.findById(selStoreId, false);
            if (storeVO == null || selStoreId.equals(store.getId()) // 相同不需切換
                    ) {
                logger.error("switchStore input error selStoreId = " + selStoreId);
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, null);
            }

            MemberVO memberVO = memberFacade.findById(member.getId(), true, locale);

            boolean manager = false;// 目前是否為商店管理員身分(對應 storeId)
            Long manageStore = null;
            if (!member.getId().equals(storeVO.getMemberId())) {
                List<Long> managers = storeUserFacade.findManagerIds(storeVO.getStoreId());
                if (managers.contains(member.getId())) {
                    manager = true;
                    manageStore = selStoreId;
                }
            }

            memberVO.setStoreId(selStoreId);// 切換 storeId
            memberVO.setManager(manager);
            AuthVO resVO = genAuthVOByMember(request, memberVO, manageStore, false, tccDealer);// forAdmin=false

            if (resVO != null) {
                return this.genSuccessRepsone(request, resVO);
            } else {
                return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
            }
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }

    /**
     * 登入檢查
     * /services/auth/checkLogin
     *
     * @param request
     * @param code
     * @return
     */
    @GET
    @Path("/checkLogin")
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response checkLogin(@Context HttpServletRequest request, @QueryParam("code") String code) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        MemberVO member = null;
        try {
            logger.debug("checkLogin code = " + code);
            Locale locale = getLocale(request);

            //HttpSession session = request.getSession();
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith(TokenProvider.BEARER)) {
                logger.debug("checkLogin ...");
                //String jwt = (String)session.getAttribute("JWT");
                String jwt = authHeader.substring(TokenProvider.BEARER.length());
                String loginAccount = tokenProvider.getSubject(jwt);
                String roles = (String) tokenProvider.getClaim(jwt, TokenProvider.AUTH_ROLES);
                Long memberId = (Long) tokenProvider.getClaim(jwt, TokenProvider.MEMBER_ID);
                Long storeId = (Long) tokenProvider.getClaim(jwt, TokenProvider.STORE_ID);
                Boolean forAdmin = (Boolean) tokenProvider.getClaim(jwt, TokenProvider.ADMIN_USER);
                Boolean forDealer = (Boolean) tokenProvider.getClaim(jwt, TokenProvider.TCC_DEALER);
                Boolean storeOwner = (Boolean) tokenProvider.getClaim(jwt, TokenProvider.STORE_OWNER);
                Boolean fiUser = (Boolean) tokenProvider.getClaim(jwt, TokenProvider.FI_USER);
                logger.debug("checkLogin loginAccount = " + loginAccount);
                logger.debug("checkLogin roles = " + roles);
                logger.debug("checkLogin memberId = " + memberId);
                logger.debug("checkLogin storeId = " + storeId);
                logger.debug("checkLogin forAdmin = " + forAdmin);
                logger.debug("checkLogin forDealer = " + forDealer);
                logger.debug("checkLogin storeOwner = " + storeOwner);
                logger.debug("checkLogin fiUser = " + fiUser);

                member = memberFacade.findById(memberId, true, locale);
                if (member == null || (forAdmin && (member.getAdminUser() == null || !member.getAdminUser())) //|| (forDealer && (member.getTccDealer()==null || !member.getTccDealer()))  
                        ) {
                    logger.error("checkLogin error member=" + member + ", forAdmin=" + forAdmin + ", forDealer=" + forDealer);
                    return this.genFailRepsone(request, ResStatusEnum.NO_PERMISSION, null);
                }

                AuthVO resVO = genAuthVO(request, member, jwt, forAdmin, forDealer, storeOwner, fiUser);

                boolean manager = false;// 目前是否為商店管理員身分(對應 storeId)
                Long manageStore = null;
                if (resVO.getLogin() != null || resVO.getLogin()) {
                    if (storeId != null) {// do switch store
                        logger.debug("checkLogin switch storeId = " + storeId);
                        member.setStoreId(storeId);
                        resVO.setStoreId(storeId);

                        List<Long> managers = storeUserFacade.findManagerIds(storeId);
                        if (managers.contains(memberId)) {
                            manager = true;
                            manageStore = storeId;
                        }
                        member.setManager(manager);
                        resVO.setManager(manager);
                    }
                    logger.info("checkLogin memberId=" + memberId + ",  storeId=" + storeId + ", manager=" + manager + ", manageStore=" + manageStore);

                    // Refresh JWT
                    if (request.getAttribute(GlobalConstant.JWT_REFRESH_FLAG) != null
                            && ((Boolean) request.getAttribute(GlobalConstant.JWT_REFRESH_FLAG))) {
                        logger.info("checkLogin Refresh JWT ...");
                        resVO = genAuthVOByMember(request, member, manageStore, forAdmin, forDealer);
                    }

                    // 功能使用記錄
                    fireUseLogForFunc(request, member, code);

                    return this.genSuccessRepsone(request, resVO);
                }
            }
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }

        return this.genFailRepsone(request);
    }
 
    /**
     * 
     * /services/upload/checkCORS
     *
     * @return
     */
    @HEAD
    @Path("/checkCORS")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkCORS() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        logger.debug("checkCORS ...");
        try {
            return Response.ok(GlobalConstant.RS_RESULT_SUCCESS, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            sys.processUnknowException(null, methodName, e);
        }
        return Response.ok(GlobalConstant.RS_RESULT_FAIL).build();
    }

    /**
     * 產生指定人員、商店 JWT
     *
     * @param request
     * @param member
     * @param forAdmin
     * @return
     */
    private AuthVO genAuthVOByMember(HttpServletRequest request, MemberVO member, Long manageStore, Boolean forAdmin, Boolean forDealer) {
        boolean tccDealer = false;
        // 角色資訊
        Set<String> groups = new HashSet<String>();
        groups.add(SysRoleEnum.MEMBER.getCode());
        Boolean admin = false;

        if (forAdmin != null && forAdmin) {// 使用管理員後台
            if (member.getAdminUser() == null || !member.getAdminUser()) {// 無管理員權限
                logger.error("genAuthVOByMember not admin : " + member.getId());
                return null;
            } else {
                admin = true;
                groups.add(SysRoleEnum.ADMIN.getCode());
                member.setStoreId(0L);// 不需 storeId
                logger.info("genAuthVOByMembert is admin");
            }
        } else if (member.getSellerApprove() != null && member.getSellerApprove() && member.getStoreId() != null) {// 有效賣家
            groups.add(SysRoleEnum.SELLER.getCode());
            // 有[訂單]的會員，加入商店客戶關聯主檔
            autoAddCustomerByOrder(member, new EcMember(member.getMemberId()));

            // 台泥經銷商
            forDealer = (forDealer != null && forDealer);
            tccDealer = (member.getTccDealer() != null && member.getTccDealer());
            if (forDealer == tccDealer) {
                if (tccDealer) {
                    groups.add(SysRoleEnum.TCC_DEALER.getCode());
                    tccDealer = true;
                    logger.info("genAuthVOByMembert is tccDealer");
                }
            } else {
                logger.error("genAuthVOByMember forDealer = " + forDealer + ", tccDealer = " + tccDealer);
                return null;
            }
        } else if( member.isManager() && !sys.isEmpty(member.getStores()) ){// 目前為商店管理員身分(對應 storeId)
            groups.add(SysRoleEnum.MANAGER.getCode());// 商店管理人員
            // 管理商店 // TODO 可指定 // 預設抓第一筆 // 最近設定在前面
            Long storeId = (manageStore == null) ? member.getStores().get(0).getValue() : manageStore;
            member.setStoreId(storeId);
            // by 管理商店需變更的設定
            tccDealer = storeFacade.isTccDealer(storeId);// 台泥經銷商的商店
            if( tccDealer ){
                groups.add(SysRoleEnum.TCC_DEALER.getCode());
            }
            logger.info("genAuthVOByMembert is Manager, storeId=" + storeId + ", tccDealer=" + tccDealer);
        } else {// 非有效賣家，非使用管理員後台、商店管理員
            logger.error("genAuthVOByMember not valid user memberId = " + member.getId());
            return null;
        }
        if (groups.size() == 1) {// 只是一般會員
            logger.error("genAuthVOByMember just a member only : " + member.getId());
            return null;
        }

        String fromIP = WebUtils.getClientIP(request);
        logger.info("genAuthVOByMember member="+member.getLoginAccount()+", fromIP = " + fromIP);
        String session = "";
        if( GlobalConstant.SESSION_KEY_ENABLED ){
            session = sessionFacade.newSession(member.getMemberId());
            sessionFacade.deleteExpired(member.getMemberId());// 同時刪除已過期的 KEY
        }
        String jwt = tokenProvider.createToken(member.getLoginAccount(), groups, member.getMemberId(),
                member.getStoreId(), admin, tccDealer, member.getStoreOwner(), member.getFiUser(), fromIP, session);
        AuthVO resVO = genAuthVO(request, member, jwt, admin, tccDealer, member.getStoreOwner(), member.getFiUser());

        return resVO;
    }

    /**
     * 註冊 (目前無用)
     * /services/auth/register
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        EcMember member = null;
        List<String> errors = new ArrayList<String>();
        try {
            Locale locale = getLocale(request);
            if (formVO == null || formVO.getLoginAccount() == null 
             || formVO.getName() == null || formVO.getEmail() == null) {
                logger.error("register formVO==null");
                return Response.notAcceptable(null).build();
            }
            logInputs(methodName, formVO, null);// log 輸入資訊

            // 輸入檢查
            String loginAccount = formVO.getLoginAccount().trim();
            String name = formVO.getName().trim();
            String email = formVO.getEmail().trim();
            boolean matched = loginAccount.matches(GlobalConstant.PATTEN_MEM_ACC);// 帳號檢查
            if (!matched) {
                // [會員帳號]只能包含英文大小寫、數字、底線、減號和英文句點，且長度須介於6~20。
                errors.add(getResourceMsg(locale, "member.check.msg1"));
                logger.error("register loginAccount not matched!");
            } else {
                matched = name.matches(GlobalConstant.PATTEN_MEM_NAME);// 名稱檢查
                if( !matched ){
                    // [會員名稱]長度須介於2~60。
                    errors.add(getResourceMsg(locale, "member.check.msg2"));
                    logger.error("register name not matched!");
                }else{
                    matched = email.matches(GlobalConstant.PATTEN_MEM_EMAIL);// E-mail檢查
                    if( !matched ){
                        // [E-mail]格式錯誤!
                        errors.add(getResourceMsg(locale, "member.check.msg3"));
                        logger.error("register email not matched!");
                    }
                }
            }
            if( !matched ){
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }

            // 已存在
            member = memberFacade.findByLoginAccount(loginAccount);
            if (member != null) {
                logger.error("register member!=null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EXIST_MEM, errors);
            }

            MemberVO vo = new MemberVO();
            vo.setLoginAccount(loginAccount);
            vo.setName(name);
            vo.setActive(false);// 待驗證後才啟動
            vo.setAdminUser(false);
            vo.setCname(name);
            vo.setEmail(email);
            vo.setDisabled(false);
            vo.setMemberType(MemberTypeEnum.PERSON.getCode());
            vo.setPhone(loginAccount);// 電話為帳號
            vo.setTel1(loginAccount);
            vo.setEmail1(email);

            if( memberFacade.checkInput(vo, null, locale, errors) ){
                String verifyCode = sys.genVerifyCode(GlobalConstant.VERIFY_CODE_LEN);
                Calendar verifyCodeExpired = Calendar.getInstance();
                // 過期時間
                verifyCodeExpired.add(Calendar.MINUTE, GlobalConstant.VERIFY_CODE_EXPIRED);
                vo.setVerifyCode(verifyCode);
                vo.setVerifyCodeExpired(verifyCodeExpired.getTime());
                memberFacade.saveVO(vo, null, locale, false);
                logger.info("register member add, vo.getMemberId() = "+vo.getMemberId());
                
                message.verifyRegistration(loginAccount, verifyCode, new EcMember(vo.getMemberId()), locale);
                return this.genSuccessRepsoneWithId(request, vo.getMemberId());// 回傳會員ID
            }

            return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 驗證碼 (目前無用)
     * /services/auth/verify
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verify(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        EcMember member = null;
        List<String> errors = new ArrayList<String>();
        try {
            Locale locale = getLocale(request);
            if (formVO == null || formVO.getLoginAccount() == null || formVO.getVerifyCode()== null) {
                logger.error("verify formVO==null");
                return Response.notAcceptable(null).build();
            }
            logInputs(methodName, formVO, null);// log 輸入資訊

            // 輸入檢查
            String loginAccount = formVO.getLoginAccount().trim();
            String verifyCode = formVO.getVerifyCode().trim();
            
            member = memberFacade.findByLoginAccount(loginAccount);
            if( member!=null ){
                if( member.getVerifyCode()!=null && verifyCode.equals(member.getVerifyCode()) ){
                    Calendar now = Calendar.getInstance();
                    logger.info("verify now ="+DateUtils.format(now.getTime()));
                    logger.info("verify member.getVerifyCodeExpired() ="+DateUtils.format(member.getVerifyCodeExpired()));
                    logger.info("verify now.before(member.getVerifyCodeExpired()) ="+now.before(member.getVerifyCodeExpired()));
                    if( member.getVerifyCodeExpired()!=null && now.getTimeInMillis()<=member.getVerifyCodeExpired().getTime() ){
                        member.setActive(Boolean.TRUE);
                        memberFacade.save(member, member, false);
                        logger.info("verify success member ="+member.getLoginAccount());
                        return this.genSuccessRepsoneWithId(request, member.getId());// 回傳會員ID
                    }
                }
            }
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * 呼叫 Modile Proxy 認證是否已登入
     * @param token
     * @param loginAccount
     * @param storeId
     * @param from
     * @return 
     */
    private String callMobileProxy(String token, String loginAccount, Long storeId, String from){
        String jsonParams = "{}";
        Map<String, Object> reqHeaders = new HashMap<String, Object>();
        String res;
        Map<String, Object> resHeaders = new HashMap<String, Object>();
        
        String mobileProxyUrl = sys.getMobileProxyUrl();// "http://localhost:8080/ec-seller-serv/servlet/executeservice";
        String checkMemberService = sys.getCheckMemberService() + loginAccount; // "/ec/service/member/checkMember?loginAccount="+formVO.getLoginAccount();
        
        reqHeaders.put("from", from);
        reqHeaders.put("storeId", storeId);
        reqHeaders.put(TokenProvider.HEADER_NAME_AUTH, TokenProvider.BEARER + token);
        reqHeaders.put("service", checkMemberService);
        logger.info("callMobileProxy before callService {}", mobileProxyUrl);
        res = WebUtils.callService("GET", mobileProxyUrl, jsonParams, reqHeaders, resHeaders);
        logger.info("callMobileProxy proxy res = " + res);
        
        return res;
    }

    /**
     * 輸入密碼(可解密) 轉至 DB儲存密碼(不可解密)
     * @param formVO
     * @return 
     */
    private String genEncryptedPassword(SubmitVO formVO) {
        //String passphrase = "the quick brown fox jumps over the lazy dog";
        String passphrase = formVO.getPassPhrase();
        //int iterationCount = GlobalConstant.ITERATION_COUNT;
        int iterationCount = formVO.getIterationCount();
        int keySize = GlobalConstant.KEY_SIZE;
        //String salt = GlobalConstant.SALT;
        //String iv = GlobalConstant.IV;
        String salt = formVO.getSalt();
        String iv = formVO.getIv();
        String ciphertext = formVO.getPwd();

        AesUtil aesUtil = new AesUtil(keySize, iterationCount);
        String plaintext = aesUtil.decrypt(salt, iv, passphrase, ciphertext);
        logger.debug("loginFrom plaintext psw = " + plaintext);

        AESPasswordHash aes = new AESPasswordHashImpl();
        String encrypted = aes.encrypt(plaintext);

        return encrypted;
    }

    //<editor-fold defaultstate="collapsed" desc="for Auto Execute After Login">
    /**
     *
     * 有核准PO的會員，加入商店客戶關聯主檔
     *
     *
     */
    private void autoAddCustomerByOrder(MemberVO member, EcMember operator) {
        try {
            customerFacade.addCustomerByOrder(member.getStoreId());

            if( GlobalConstant.TCC_DEALER_ENABLED ){
                // 依 EC_TCC_DEALER_DS 設定，將下游廠商加入對應經銷商的客戶中 
                customerFacade.addCustomerByDealer(member.getMemberId(), operator);
            }
        } catch (Exception e) {
            sys.processUnknowException(member, "autoAddCustomerByOrder", e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Use Logs">
    @Asynchronous
    private void fireUseLogForLogin(HttpServletRequest request, MemberVO member, LogTypeEnum typeEnum) {
        logger.info("fireUseLogEvent typeEnum = " + typeEnum);
        // 勿影響正常功能運作
        try {
            String src = "WEB";
            if (typeEnum == null || member == null || member.getMemberId() == null) {
                logger.error("fireUseLogEvent input null");
                return;
            }
            String type = typeEnum.getCode();
            // 計算前次登入使用時間
            //useLogFacade.calUsePeriods(member.getMemberId(), type, src);
            // 本次使用功能
            EcMember operator = new EcMember(member.getMemberId());
            EcUseLog useLog = new EcUseLog();
            useLog.setType(type);
            //useLog.setCategory(null);
            //useLog.setClientInfo(code);
            useLog.setClientIp(WebUtils.getClientIP(request));
            useLog.setMemberId(member.getMemberId());
            //useLog.setMemo(null);
            //useLog.setPeriod(null);
            useLog.setSrc(src);

            useLogFacade.save(useLog, operator, false);
        } catch (Exception e) {
            sys.processUnknowException(member, "fireUseLogEvent", e);
        }
    }

    @Asynchronous
    private void fireUseLogForFunc(HttpServletRequest request, MemberVO member, String code) {
        logger.info("fireUseLogEvent code = " + code);
        // 勿影響正常功能運作
        try {
            String src = "WEB";
            LogCategoryEnum catEnum = LogCategoryEnum.getFromUrl(code);
            if (catEnum == null || member == null || member.getMemberId() == null) {
                logger.error("fireUseLogEvent input null, code = " + code);
                return;
            }
            String type = catEnum.getType().getCode();
            // 計算前一功能使用時間
            useLogFacade.calUsePeriods(member.getMemberId(), type, src);
            // 本次使用功能
            EcMember operator = new EcMember(member.getMemberId());
            EcUseLog useLog = new EcUseLog();
            useLog.setType(type);
            useLog.setCategory(catEnum.getCode());
            //useLog.setClientInfo(code);
            useLog.setClientIp(WebUtils.getClientIP(request));
            useLog.setMemberId(member.getMemberId());
            //useLog.setMemo(null);
            //useLog.setPeriod(null);
            useLog.setSrc(src);

            useLogFacade.save(useLog, operator, false);
        } catch (Exception e) {
            sys.processUnknowException(member, "fireUseLogEvent", e);
        }
    }
    //</editor-fold>
}

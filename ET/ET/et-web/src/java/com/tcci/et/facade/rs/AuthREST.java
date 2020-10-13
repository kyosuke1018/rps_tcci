/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
//import com.tcci.cm.facade.rs.filter.JWTTokenNeeded;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.et.entity.EtMember;
import com.tcci.et.enums.SysRoleEnum;
import com.tcci.et.enums.rs.ResStatusEnum;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.rs.AuthVO;
import com.tcci.et.model.rs.SubmitVO;
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
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.WebUtils;
import com.tcci.et.entity.EtUseLog;
import com.tcci.et.enums.LogCategoryEnum;
import com.tcci.et.enums.LogTypeEnum;
import com.tcci.et.facade.global.AuthFacade;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.fc.util.StringUtils;
import java.util.Date;
import java.util.Locale;
import javax.ejb.EJB;
//import com.tcci.security.TokenProvider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Asynchronous;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author Peter.pan
 */
@Path("/auth")
public class AuthREST extends AbstractWebREST {

    @EJB
    AuthFacade auth;
    @Inject
    private TcUserFacade userFacade;

    /**
     * /services/auth/register
     *
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
        EtMember member = null;
        List<String> errors = new ArrayList<>();
        try {
            Locale locale = getLocale(request);
            if (formVO == null || formVO.getLoginAccount() == null 
             || formVO.getName() == null || formVO.getPhone() == null) {
                logger.error("register formVO==null");
                return Response.notAcceptable(null).build();
            }
            logInputs(methodName, formVO, null);// log 輸入資訊

            // 輸入檢查
            String loginAccount = formVO.getLoginAccount().trim();
            String name = formVO.getName().trim();
            String email = loginAccount;
            String phone = formVO.getPhone().trim();
//            String pwd = formVO.getPwd().trim();
//            boolean matched = loginAccount.matches(GlobalConstant.PATTEN_MEM_ACC);// 帳號檢查
//            boolean matched = loginAccount.matches(GlobalConstant.PATTEN_MEM_EMAIL);
             boolean matched = true;
            if (!matched) {
                // [會員帳號]只能包含英文大小寫、數字、底線、減號和英文句點，且長度須介於6~20。
//                errors.add(ResourceBundleUtils.getMessage(locale, "member.check.msg1"));
                // [E-mail]格式錯誤!
                errors.add(ResourceBundleUtils.getMessage(locale, "member.check.msg3"));
                logger.error("register loginAccount not matched!");
            } else {
                matched = name.matches(GlobalConstant.PATTEN_MEM_NAME);// 名稱檢查
                if( !matched ){
                    // [會員名稱]長度須介於2~60。
                    errors.add(ResourceBundleUtils.getMessage(locale, "member.check.msg2"));
                    logger.error("register name not matched!");
                }else{
//                    matched = email.matches(GlobalConstant.PATTEN_MEM_EMAIL);// E-mail檢查
                    matched = phone.matches(GlobalConstant.PATTEN_MEM_PHONE);// phone檢查
                    if( !matched ){
                        // [E-mail]格式錯誤!
//                        errors.add(ResourceBundleUtils.getMessage(locale, "member.check.msg3"));
//                        logger.error("register email not matched!");
                        // [電話]只能包含英文大小寫、數字、底線、減號和英文句點，且長度須介於6~20。
//                        errors.add(ResourceBundleUtils.getMessage(locale, "member.check.msg1"));
                        errors.add("[電話]格式錯誤!");
                        logger.error("register phone not matched!");
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
//            vo.setActive(false);// 待驗證後才啟動
            vo.setAdminUser(false);
            vo.setCname(name);
            vo.setEmail(email);
            vo.setDisabled(false);
//            vo.setMemberType(MemberTypeEnum.PERSON.getCode());
//            vo.setPhone(loginAccount);// 電話為帳號
//            vo.setTel1(loginAccount);
//            vo.setEmail1(email);
            
            // email為帳號 註冊不驗證
//            vo.setEmail(loginAccount);
//            vo.setMemberType(MemberTypeEnum.COMPANY.getCode());//def in facade
            vo.setActive(true);//註冊不驗證
            vo.setPhone(phone);
//            vo.setTel1(phone);
//            vo.setEmail1(loginAccount);
            vo.setApplytime(new Date());
            
            String pwd = StringUtils.genRandomString(97, 122, 6);// 六碼小寫英文
            AESPasswordHash aes = new AESPasswordHashImpl();
//            String encrypted = aes.encrypt(GlobalConstant.DEF_PWD);
            String encrypted = aes.encrypt(pwd);
            vo.setPwd(encrypted);
            
            /*if( memberFacade.checkInput(vo, null, locale, errors) ){
                String verifyCode = sys.genVerifyCode(GlobalConstant.VERIFY_CODE_LEN);
                Calendar verifyCodeExpired = Calendar.getInstance();
                // 過期時間
                verifyCodeExpired.add(Calendar.MINUTE, GlobalConstant.VERIFY_CODE_EXPIRED);
                vo.setVerifyCode(verifyCode);
                vo.setVerifyCodeExpired(verifyCodeExpired.getTime());
                memberFacade.saveVO(vo, null, locale, false);
                logger.info("register member add, vo.getMemberId() = "+vo.getMemberId());
                
                message.verifyRegistration(loginAccount, verifyCode, new EtMember(vo.getMemberId()), locale);
                return this.genSuccessRepsoneWithId(request, vo.getMemberId());// 回傳會員ID
            }*/
            if( memberFacade.checkInput(vo, null, locale, errors) ){
                //註冊 以系統管理員執行
                TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
                memberFacade.saveVO(vo, admin, locale, false);
                
                member = memberFacade.find(vo.getMemberId());
                if(member!=null){
                    //send mail
                    memberFacade.sendPwdMail(member, pwd);
                }
                
                logger.info("register member add, vo.getMemberId() = "+vo.getMemberId());
                return this.genSuccessRepsoneWithId(request, vo.getMemberId());// 回傳會員ID
            }

            return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
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
        EtMember member = null;
        List<String> errors = new ArrayList<>();
        try {
            Locale locale = getLocale(request);
            if (formVO == null || formVO.getLoginAccount() == null || formVO.getVerifyCode()== null) {
                logger.error("verify formVO==null");
                return Response.notAcceptable(null).build();
            }
            logInputs(methodName, formVO, null);// log 輸入資訊

            // 輸入檢查
            String loginAccount = formVO.getLoginAccount().trim();
//            String verifyCode = formVO.getVerifyCode().trim();
            
            member = memberFacade.findByLoginAccount(loginAccount);
            if( member!=null ){
                member.setActive(Boolean.TRUE);
//                memberFacade.save(member, member, false);
//                memberFacade.save(member, false);
                return this.genSuccessRepsoneWithId(request, member.getId());// 回傳會員ID
            }
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
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
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        MemberVO member = null;
        try {
            logger.info("loginFrom loginAccount = " + formVO.getLoginAccount());
            logger.debug("loginFrom pwd = " + formVO.getPwd());
            Locale locale = getLocale(request);

            // 密碼處理
//            String encrypted = genEncryptedPassword(formVO);
            String encrypted = genEncryptedPassword(formVO.getPwd());//未加密
            // 會員資訊 & 可管理商店資訊
//            if( GlobalConstant.SYNC_PASSWORD_EC10 ){
//                memberFacade.syncPasswordFromEc10(formVO.getLoginAccount());// 同步 EC1.0 密碼
//            }
            member = memberFacade.findForLogin(formVO.getLoginAccount(), encrypted, locale);
            if (member == null) {
                logger.info("loginFrom error member==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_LOGIN, null);
            }
            
//            boolean forAdmin = (formVO.getForAdmin() != null && formVO.getForAdmin());
            // 依 E_MEMBER.TCC_DEALER 設定決定使用系統 EC1.5 or EC2.0
//            boolean forDealer = (member.getTccDealer() != null && member.getTccDealer());

//            AuthVO resVO = genAuthVOByMember(request, member, null, forAdmin, forDealer);
            AuthVO resVO = genAuthVOByMember(request, member);
            if (resVO != null) {
                // 登入紀錄
//                fireUseLogForLogin(request, member, forAdmin ? LogTypeEnum.LOGIN_ADMIN : LogTypeEnum.LOGIN_SELLER);
                fireUseLogForLogin(request, member, LogTypeEnum.LOGIN_ADMIN);
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
    @GET
    @Path("/login/{name}/{pwd}")
    @Produces(MediaType.TEXT_PLAIN+";charset=utf-8")
    @JWTTokenNeeded
    public Response login(@Context HttpServletRequest request
            , @PathParam("name")String name, @PathParam("pwd")String pwd){
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        MemberVO member = null;
        SubmitVO formVO = new SubmitVO();
        formVO.setLoginAccount(name);
        formVO.setPwd(pwd);
        try {
            logger.info("loginFrom loginAccount = " + formVO.getLoginAccount());
            logger.debug("loginFrom pwd = " + formVO.getPwd());
            Locale locale = getLocale(request);
            // 密碼處理
//            String encrypted = genEncryptedPassword2(formVO);
            String encrypted = genEncryptedPassword(formVO.getPwd());//未加密
            member = memberFacade.findForLogin(formVO.getLoginAccount(), encrypted, locale);
            if (member == null) {
                logger.info("loginFrom error member==null");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_LOGIN, null);
            }
            
//            if (resVO != null) {
            if (member != null) {
                // 登入紀錄
//                fireUseLogForLogin(request, member, forAdmin ? LogTypeEnum.LOGIN_ADMIN : LogTypeEnum.LOGIN_SELLER);
                fireUseLogForLogin(request, member, LogTypeEnum.LOGIN_ADMIN);
                logger.info("loginFrom success loginAccount = " + formVO.getLoginAccount());
//                return this.genSuccessRepsone(request, resVO);
                return this.genSuccessRepsone(request, member);
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
    
    /**
     * /auth/reset 重設密碼 (寄發預設密碼)
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
        EtMember member = null;
        try {
            Locale locale = getLocale(request);
            if (formVO == null || formVO.getLoginAccount() == null) {
                logger.error("forgetPassword formVO==null");
                return Response.notAcceptable(null).build();
            }
            logInputs(methodName, formVO, null);// log 輸入資訊

            // 帳號資訊
            member = memberFacade.findByLoginAccount(formVO.getLoginAccount());
            if (member == null || member.getEmail() == null) {
                logger.error("forgetPassword member==null || member.getEmail()==null !");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }

            // 產生重設密碼 a ~ z
            String plaintext = StringUtils.genRandomString(97, 122, 6);// 六碼小寫英文
            AESPasswordHash aes = new AESPasswordHashImpl();
            String encrypted = aes.encrypt(plaintext);

            // SMS
            /*
            logger.info("forgetPassword before sendMsg ...");
            if( message.resetPassword(member, plaintext, member, locale) ){
                logger.info("forgetPassword before save DB ...");
                // save DB
                member.setPassword(encrypted);
                //重設密碼 以系統管理員執行
                TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
                memberFacade.save(member, admin, false);
//                memberFacade.save(member, false);

                return this.genSuccessRepsone(request);
            } else {
                return this.genFailRepsone(request);
            }*/
            
            logger.info("forgetPassword before save DB ...plaintext:"+plaintext);
            MemberVO memberVO = new MemberVO();
            memberVO.setMemberId(member.getId());
            ExtBeanUtils.copyProperties(memberVO, member);
            fireUseLogForFunc(request, memberVO, LogCategoryEnum.RESET_PWD.getCode());
            
            //send mail
            memberFacade.sendPwdMail(member, plaintext);
            
            // save DB
            member.setPassword(encrypted);
            //重設密碼 以系統管理員執行
            TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
            memberFacade.save(member, admin, false);
            
            return this.genSuccessRepsone(request);
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /auth/reset 變更密碼 要登入
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/changePwd")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        EtMember member = null;
        try {
            // 帳號資訊
            member = getReqUser(request);
            if(member == null){
                member = memberFacade.find(formVO.getMemberId());
            }
//            member = memberFacade.findByLoginAccount(formVO.getLoginAccount());
            if (member == null || member.getEmail() == null) {
                logger.error("changePwd member==null || member.getEmail()==null !");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, null);
            }
            Locale locale = getLocale(request);
            if (formVO == null || formVO.getLoginAccount() == null) {
                logger.error("changePwd formVO==null");
                return Response.notAcceptable(null).build();
            }
            logInputs(methodName, formVO, null);// log 輸入資訊
            
            if(StringUtils.isBlank(formVO.getLoginAccount()) 
                    || StringUtils.isBlank(formVO.getPwd())
                    || !member.getLoginAccount().equals(formVO.getLoginAccount()) ){
                logger.info("loginFrom error 帳號/密碼輸入錯誤!");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_LOGIN, null);
            }
            

            String plaintext = formVO.getPwd();
            AESPasswordHash aes = new AESPasswordHashImpl();
            String encrypted = aes.encrypt(plaintext);

            // SMS
            /*
            logger.info("forgetPassword before sendMsg ...");
            if( message.resetPassword(member, plaintext, member, locale) ){
                logger.info("forgetPassword before save DB ...");
                // save DB
                member.setPassword(encrypted);
                //重設密碼 以系統管理員執行
                TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
                memberFacade.save(member, admin, false);
//                memberFacade.save(member, false);

                return this.genSuccessRepsone(request);
            } else {
                return this.genFailRepsone(request);
            }*/
            
            logger.info("changePwd before save DB ...plaintext:"+plaintext);
            MemberVO memberVO = new MemberVO();
            memberVO.setMemberId(member.getId());
            ExtBeanUtils.copyProperties(memberVO, member);
            fireUseLogForFunc(request, memberVO, LogCategoryEnum.CHANGE_PWD.getCode());
            
            //send mail
            memberFacade.sendPwdMail(member, null);
            
            // save DB
            member.setPassword(encrypted);
            //重設密碼 以系統管理員執行
            TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
            memberFacade.save(member, admin, false);
            
            return this.genSuccessRepsone(request);
        } catch (Exception e) {
            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }

    /**
     * /services/auth/checkLogin
     *
     * @param request
     * @param code
     * @return
     */

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
//            sys.processUnknowException(null, methodName, e);
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
        Set<String> groups = new HashSet<>();
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
//        String jwt = tokenProvider.createToken(member.getLoginAccount(), groups, member.getMemberId(),
//                member.getStoreId(), admin, fromIP);
        String jwt = "";
        AuthVO resVO = genAuthVO(request, member, jwt, admin, tccDealer, member.getStoreOwner(), member.getFiUser());

        return resVO;
    }
    private AuthVO genAuthVOByMember(HttpServletRequest request, MemberVO member) {
        boolean tccDealer = false;
        // 角色資訊
        Set<String> groups = new HashSet<>();
        groups.add(SysRoleEnum.MEMBER.getCode());
//        Boolean admin = false;
        Boolean admin = true;
        groups.add(SysRoleEnum.ADMIN.getCode());
        
        String fromIP = WebUtils.getClientIP(request);
        logger.info("genAuthVOByMember member="+member.getLoginAccount()+", fromIP = " + fromIP);
//        String jwt = tokenProvider.createToken(member.getLoginAccount(), groups, member.getMemberId(),
//                member.getStoreId(), admin, fromIP);
        String jwt = "";
        AuthVO resVO = genAuthVO(request, member, jwt, admin, tccDealer, member.getStoreOwner(), member.getFiUser());

        return resVO;
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
        logger.info("loginFrom plaintext psw = " + plaintext);

        AESPasswordHash aes = new AESPasswordHashImpl();
        String encrypted = aes.encrypt(plaintext);

        return encrypted;
    }
    private String genEncryptedPassword(String ciphertext) {
        AESPasswordHash aes = new AESPasswordHashImpl();
        String encrypted = aes.encrypt(ciphertext);

        return encrypted;
    }

    //<editor-fold defaultstate="collapsed" desc="for Auto Execute After Login">
    /**
     *
     * 有核准PO的會員，加入商店客戶關聯主檔
     *
     *
     */
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
            EtMember operator = new EtMember(member.getMemberId());
            EtUseLog useLog = new EtUseLog();
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
//            sys.processUnknowException(member, "fireUseLogEvent", e);
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
            EtMember operator = new EtMember(member.getMemberId());
            EtUseLog useLog = new EtUseLog();
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
//            sys.processUnknowException(member, "fireUseLogEvent", e);
        }
    }
    //</editor-fold>
}
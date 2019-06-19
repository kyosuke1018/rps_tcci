/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.member;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcCompany;
import com.tcci.ec.entity.EcCusAddr;
import com.tcci.ec.entity.EcFavoritePrd;
import com.tcci.ec.entity.EcFavoriteStore;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcMemberMsg;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.entity.EcPushLog;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.enums.MsgTypeEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.rs.ResStatusEnum;
import com.tcci.ec.facade.EcCompanyFacade;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.facade.EcOptionFacade;
import com.tcci.ec.facade.EcTccDealerDsFacade;
import com.tcci.ec.facade.customer.EcCusAddrFacade;
import com.tcci.ec.facade.member.AccountNotExistException;
import com.tcci.ec.facade.member.EcFavoritePrdFacade;
import com.tcci.ec.facade.member.EcFavoriteStoreFacade;
import com.tcci.ec.facade.member.EcMemberMsgFacade;
import com.tcci.ec.facade.member.PasswordWrongException;
import com.tcci.ec.facade.product.EcProductFacade;
import com.tcci.ec.facade.push.EcPushLogFacade;
import com.tcci.ec.facade.store.EcStoreFacade;
import com.tcci.ec.model.LongOptionVO;
import com.tcci.ec.service.ServiceBase;
import com.tcci.ec.service.sms.SmsService;
import com.tcci.ec.vo.Company;
import com.tcci.ec.vo.CusAddr;
import com.tcci.ec.vo.Member;
import com.tcci.ec.vo.MemberInfo;
import com.tcci.ec.vo.MemberMsg;
import com.tcci.ec.vo.MsgBoard;
import com.tcci.ec.vo.Product;
import com.tcci.ec.vo.Store;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.FileUtils;
import java.io.File;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.AESPasswordHash;
import javax.security.enterprise.identitystore.AESPasswordHashImpl;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("member")
public class MemberService extends ServiceBase {
    private final static Logger logger = LoggerFactory.getLogger(MemberService.class);
    
    protected final static String PARAM_OLD_PASSWORD = "old_password";
    protected final static String PARAM_NEW_PASSWORD = "new_password";
    protected final static String PARAM_ACCOUNT = "account";
    
    @EJB
    private EcCusAddrFacade ecCusAddrFacade;
    @EJB
    private EcProductFacade ecProductFacade;
    @EJB
    private EcStoreFacade ecStoreFacade;
    @EJB
    private EcMemberMsgFacade ecMemberMsgFacade;
    @EJB 
    protected EcFavoriteStoreFacade favoriteStoreFacade;
    @EJB 
    protected EcFavoritePrdFacade favoritePrdFacade;
    @EJB 
    protected EcFileFacade fileFacade;
    @EJB 
    protected EcOptionFacade ecOptionFacade;
    @EJB 
    protected EcCompanyFacade ecCompanyFacade;
    @EJB 
    protected EcTccDealerDsFacade ecTccDealerDsFacade;
    @Inject
    private SmsService smsService;
    @EJB
    private EcPushLogFacade pushLogFacade;//log sms
    
    @GET
    @Path("checkMember")
    @Produces("application/json; charset=UTF-8;")
    public String checkMember(@Context HttpServletRequest request,
            @QueryParam(value = "loginAccount") String loginAccount) {
        logger.debug("checkMember loginAccount:"+loginAccount);
        EcMember member = getAuthMember();
        if(member!=null && member.getLoginAccount().equals(loginAccount)){
            logger.debug("checkMember member..."+member.getLoginAccount());
            return "SUCCESS";
        }
        return "FAIL";
    }
    
    @GET
    @Path("memberInfo")
    @Produces("application/json; charset=UTF-8;")
    public MemberInfo memberInfo() {
        logger.debug("memberInfo ...");
        EcMember member = getAuthMember();
        if(member!=null){
            logger.debug("memberInfo member..."+member.getLoginAccount());
        }
        MemberInfo result = new MemberInfo();
        try{
            if(member!=null){
                result.setMember(entityTransforVO.memberTransfor(member));
                
                List<EcCusAddr> addrList = ecCusAddrFacade.findByMember(member);
                if(CollectionUtils.isNotEmpty(addrList)){
                    List<CusAddr> list = new ArrayList<>();
                    for(EcCusAddr entity:addrList){
                        list.add(entityTransforVO.cusAddrTransfor(entity));
                    }
                    result.setAddrList(list);
                }
                
                List<EcFavoritePrd> prdList = favoritePrdFacade.findByMember(member);
                if(CollectionUtils.isNotEmpty(prdList)){
                    List<Product> list = new ArrayList<>();
                    int i = 0;
                    for(EcFavoritePrd entity:prdList){
                        if(i>10){
                            break;
                        }
                        list.add(entityTransforVO.productTransfor(entity.getProduct()));
                        i++;
                    }
                    result.setFacoritePrd(list);
                }
                List<EcFavoriteStore> storeList = favoriteStoreFacade.findByMember(member);
                if(CollectionUtils.isNotEmpty(storeList)){
                    List<Store> list = new ArrayList<>();
                    int i = 0;
                    for(EcFavoriteStore entity:storeList){
                        if(i>10){
                            break;
                        }
                        list.add(entityTransforVO.storeTransfor(entity.getStore()));
                        i++;
                    }
                    result.setFacoriteStore(list);
                }
                
                //v1.5
                //台泥經銷商 賣家商店
                logger.debug("memberInfo TccDs:"+member.getTccDs());
                if(member.getTccDs()){//台泥DS買家
                    List<Long> stores = ecTccDealerDsFacade.findDealerStoreIds(member.getId());
                    logger.debug("memberInfo stores:"+stores);
                    if(CollectionUtils.isNotEmpty(stores)){
                        List<Store> list = new ArrayList<>();
//                        int i = 0;
                        for(Long storeId : stores){
//                            if(i>10){
//                                break;
//                            }
//                            EcStore store = ecStoreFacade.find(storeId);
                            list.add(entityTransforVO.storeTransfor(ecStoreFacade.find(storeId)));
//                            i++;
                        }
                        result.setDealerStoreList(list);
                    }
                }
            }
            
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @GET
    @Path("list")
//    @PermitAll
//    @Produces(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public List<Member> list() {
        logger.debug("list ...");
//        return ecMemberFacade.findAllActive();
        List<EcMember> list = ecMemberFacade.findAllActive();
        List<Member> result = new ArrayList<>();
        try{
            if(CollectionUtils.isNotEmpty(list)){
                for(EcMember entity:list){
                    result.add(entityTransforVO.memberTransfor(entity));
                }
            }
        }catch(Exception e){
            logger.error("Exception:"+e);
        }
        return result;
    }
    
    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public String register(Member member) {
        if(StringUtils.isNotEmpty(member.getLoginAccount())) {
            EcMember ecMember = ecMemberFacade.findByLoginAccount(member.getLoginAccount());
            if(ecMember!=null){
                return "註冊失敗! 帳號已存在";
            }
        }
        
        if(StringUtils.isNotEmpty(member.getPassword())) {
            EcMember ecMember = ecMemberFacade.registerMember(member);
            if(ecMember!=null){
                return "註冊完成! 請再次登入";
            }
        }
        return "註冊失敗! 請輸入密碼";
    }
    
    @GET
    @Path("register2")
    @Produces("application/json; charset=UTF-8;")
    public String register(@Context HttpServletRequest request,
            @QueryParam(value = "name") String name,
            @QueryParam(value = "email") String email,
            @QueryParam(value = "phone") String phone,
            @QueryParam(value = "loginAccount") String loginAccount,
            @QueryParam(value = "password") String password) {
        if(StringUtils.isNotEmpty(loginAccount)) {
            EcMember ecMember = ecMemberFacade.findByLoginAccount(loginAccount);
            if(ecMember!=null){
                return "註冊失敗! 帳號已存在";
            }
        }
        
        if(StringUtils.isNotEmpty(password)) {
            EcMember ecMember = ecMemberFacade.registerMember(name, email, phone, loginAccount, password);
            if(ecMember!=null){
                return "註冊完成! 請再次登入";
            }
        }
        return "註冊失敗! 請輸入密碼";
    }
    
    /*
    @POST
    @Path("resetPassword")
    @Produces(MediaType.TEXT_PLAIN)
    public String resetPassword(@FormParam(PARAM_OLD_PASSWORD) String old_password, @FormParam(PARAM_NEW_PASSWORD) String new_password) {
        logger.debug("old_password = "+old_password);
        logger.debug("new_password = "+new_password);
        EcMember loginMember = getAuthMember();
        try {
            if(loginMember.isSellerApprove()){//賣家
                logger.error("賣家身分不可變更密碼!");
                return "FAIL";
            }
            
            ecMemberFacade.resetPassword(loginMember, old_password, new_password);
        } catch (PasswordWrongException ex) {
//            throw new ServiceException("旧密码不正确!");
            logger.error("旧密码不正确!");
            return "FAIL";
        }
//        return "OK";
        return "SUCCESS";
    }
    */
    @GET
    @Path("resetPassword")
    @Produces("application/json; charset=UTF-8;")
    public String resetPassword(@QueryParam("PARAM_OLD_PASSWORD") String old_password, @QueryParam("PARAM_NEW_PASSWORD") String new_password) {
        logger.debug("old_password = "+old_password);
        logger.debug("new_password = "+new_password);
//        EcMember loginMember = getAuthMember();
        EcMember member = getAuthMember();
        if(member!=null){
            logger.debug("memberInfo member..."+member.getLoginAccount());
        }
        try {
            if(member.isSellerApprove()){//賣家
                logger.error("賣家身分不可變更密碼!");
                return "FAIL";
            }
            
            ecMemberFacade.resetPassword(member, old_password, new_password);
        } catch (PasswordWrongException ex) {
//            throw new ServiceException("旧密码不正确!");
            logger.error("旧密码不正确!");
            return "FAIL";
        }
//        return "OK";
        return "SUCCESS";
    }
    
    /*@POST
    @Path("forgotPassword")
    @Produces(MediaType.TEXT_PLAIN)
    public String forgotPassword(@FormParam(PARAM_ACCOUNT) String account) {
        try {
            String newPassword = ecMemberFacade.forgotPassword(account);
            MailNotify.forgotPassword(account, newPassword);
        } catch (AccountNotExistException ex) {
            throw new ServiceException(account + " 帐号不存在或已停用!");
        }
        return "OK";
    }*/
    @GET
    @Path("forgotPassword")
    @Produces("application/json; charset=UTF-8;")
    public String forgotPassword(@QueryParam("account") String account) {
//        if(request.getParameter("guest") == null){//非訪客
//        }
        try {
            EcMember member = ecMemberFacade.findByLoginAccount(account);
            Locale locale = getLocale(request);
            if(member!=null){
                if(member.isSellerApprove()){
                    return "请至台泥行動商城进行变更!"; 
                }
            }else{
                return "帐号不存在或已停用!"; 
            }
            
            //限制次數 (一天10次) check EC_SMS_LOG
            logger.debug("EC_SMS_LOG countBySms:"+pushLogFacade.countBySms(member));
            if(pushLogFacade.countBySms(member).compareTo(BigDecimal.TEN)>0){
                return "重设密码次数已超过当日限制, 请联络系统管理员!"; 
            }
            EcPushLog pushLog = new EcPushLog("sms", "alias", "forgetPassword", member.getPhone(), member.getLoginAccount());
            // 產生重設密碼
            String plaintext = smsService.genRandomString(97, 122, 6);// 六碼小寫英文
            AESPasswordHash aes = new AESPasswordHashImpl();
//            String encrypted = aes.encrypt(plaintext);
            String encrypted = DigestUtils.sha256Hex(plaintext);
            
            boolean result = smsService.resetPassword(member, plaintext, locale);
//            boolean result = true;
            if(result){
                logger.debug("EC_SMS_LOG start");
                member.setPassword(encrypted);
                member.setResetPwd(encrypted);
                member.setResetPwdExpired(DateUtils.addDate(new Date(), 1));
                member.setModifytime(new Date());
                ecMemberFacade.save(member);
                logger.debug("smsService.resetPassword OK!");
                
                pushLog.setSuccess(true);
                logger.debug("EC_SMS_LOG end");
            }
            try {
                pushLog.setSuccess(result);
                pushLog.setPushResult(plaintext);
                pushLogFacade.save(pushLog);
            }catch(Exception e){
                logger.error("pushLogFacade Exception:"+e);
            }
//            String newPassword = ecMemberFacade.forgotPassword(account);
//            MailNotify.forgotPassword(account, newPassword);
        }catch(Exception e){
            logger.error("forgotPassword Exception:"+e);
//            throw new ServiceException(account + " 帐号不存在或已停用!");
            return "請通知系統管理員! Exception:"+e.getMessage(); 
        }
//        return "SUCCESS";
        return "讯息已发送 请稍后!";
    }
    
    @GET
    @Path("sellerApply")
    @Produces("application/json; charset=UTF-8;")
    public String sellerApply() {
        logger.debug("sellerApply ...");
        String result = "";
        try{
            EcMember member = getAuthMember();
            if(member==null || member.isSellerApply()){
                logger.debug("sellerApply member..."+member.getLoginAccount());
                return "已經申請過了!";
            }
            member.setSellerApply(true);
            member.setApplytime(new Date());
            ecMemberFacade.save(member);
            
            result = "SUCCESS";
        }catch(Exception e){
            logger.error("Exception:"+e);
            result = "Exception:"+e;
        }
        return result;
    }
    
    @GET
    @Path("messageCreate")
    @Produces("application/json; charset=UTF-8;")
    public String messageCreate(@Context HttpServletRequest request,
            @QueryParam(value = "type") String type,
            @QueryParam(value = "id") Long id,
            @QueryParam(value = "parentId") Long parentId,
            @QueryParam(value = "message") String message) {
        logger.debug("messageCreate type:"+type);
        logger.debug("messageCreate id:"+id);
        logger.debug("messageCreate message:"+message);
        
        EcMember member = getAuthMember();
        EcMemberMsg entity = new EcMemberMsg();
        
        if(type==null || id==null || member==null || message==null){
            return "FAIL";
        }else if(MsgTypeEnum.P.equals(MsgTypeEnum.fromCode(type))){
            entity.setType(MsgTypeEnum.P);
            EcProduct product = ecProductFacade.find(id);
            entity.setProduct(product);
            entity.setStore(product.getStore());
        }else if(MsgTypeEnum.S.equals(MsgTypeEnum.fromCode(type))){
            entity.setType(MsgTypeEnum.S);
            EcStore store = ecStoreFacade.find(id);
            entity.setStore(store);
        }
        entity.setMember(member);
        entity.setMessage(message);
        entity.setCreator(member);
        if(parentId!=null){
            entity.setParent(parentId);
        }
        
        ecMemberMsgFacade.save(entity);
        
        return "SUCCESS";
    }
    
    @GET
    @Path("findMessage")
    @Produces("application/json; charset=UTF-8;")
    /**
    public List<MemberMsg> findMessage(@Context HttpServletRequest request,
            @QueryParam(value = "type") String type,
            @QueryParam(value = "id") Long id) {
        logger.debug("findMessage type:"+type);
        logger.debug("findMessage id:"+id);
        
//        EcMember member = getAuthMember();
        List<EcMemberMsg> msgList = new ArrayList<>();
        if(type==null || id==null){
            return null;
        }else if(MsgTypeEnum.P.equals(MsgTypeEnum.fromCode(type))){
            EcProduct entity = ecProductFacade.find(id);
            msgList = ecMemberMsgFacade.findByPrd(entity);
        }else if(MsgTypeEnum.S.equals(MsgTypeEnum.fromCode(type))){
            EcStore entity = ecStoreFacade.find(id);
            msgList = ecMemberMsgFacade.findByStore(entity);
        }
        
        List<MemberMsg> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(msgList)){
            for(EcMemberMsg message:msgList){
                result.add(entityTransforVO.memberMsgTransfor(message));
            }
        }
        
        return result;
    }*/
    public List<MsgBoard> findMessage(@Context HttpServletRequest request,
            @QueryParam(value = "type") String type,
            @QueryParam(value = "id") Long id) {
        logger.debug("findMessage type:"+type);
        logger.debug("findMessage id:"+id);
        
//        EcMember member = getAuthMember();
        List<EcMemberMsg> msgList = new ArrayList<>();
        if(type==null || id==null){
            return null;
        }else if(MsgTypeEnum.P.equals(MsgTypeEnum.fromCode(type))){
            EcProduct entity = ecProductFacade.find(id);
            msgList = ecMemberMsgFacade.findRootByPrd(entity);
            
        }else if(MsgTypeEnum.S.equals(MsgTypeEnum.fromCode(type))){
            EcStore entity = ecStoreFacade.find(id);
            msgList = ecMemberMsgFacade.findRootByStore(entity);
        }
        
        List<MsgBoard> resultList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(msgList)){
            for(EcMemberMsg message:msgList){
                MsgBoard msgBoard = new MsgBoard();
                if(message!=null){
                    msgBoard.setParentId(message.getId());
                    msgBoard.setType(message.getType());
                    if(MsgTypeEnum.P.equals(message.getType())){
                        msgBoard.setTitle(message.getProduct().getCname());
                        msgBoard.setPrdId(message.getProduct().getId());
                        msgBoard.setStoreId(message.getStore().getId());
                        msgBoard.setStoreTitle(message.getStore().getCname());
                    }else if(MsgTypeEnum.S.equals(message.getType())){
                        msgBoard.setTitle(message.getStore().getCname());
                        msgBoard.setStoreId(message.getStore().getId());
                    }
                    
                    List<MemberMsg> result = new ArrayList<>();
                    result.add(entityTransforVO.memberMsgTransfor(message));
                    List<EcMemberMsg> list = ecMemberMsgFacade.findByParent(message.getId());
                    if(CollectionUtils.isNotEmpty(list)){
                        for(EcMemberMsg msg:list){
                            result.add(entityTransforVO.memberMsgTransfor(msg));
                        }
                    }
                    msgBoard.setMsgList(result);
                }
                
                resultList.add(msgBoard);
            }
        }
        
        return resultList;
    }
    
    @GET
    @Path("findMessageByMember")
    @Produces("application/json; charset=UTF-8;")
    public List<MsgBoard> findMessageByMember(@Context HttpServletRequest request) {
        logger.debug("findMessageByMember");
        EcMember member = getAuthMember();
        List<MsgBoard> resultList = new ArrayList<>();
        if(member!=null){
            List<Long> idList = ecMemberMsgFacade.findParentByMember(member);
//            logger.debug("findMessageByMember idList:"+idList);
//            logger.debug("findMessageByMember idList:"+idList.size());
            if(CollectionUtils.isNotEmpty(idList)){
                for(Long parentId:idList){
                    MsgBoard msgBoard = new MsgBoard();
                    EcMemberMsg message = ecMemberMsgFacade.find(parentId);
                    if(message!=null){
                        msgBoard.setParentId(parentId);
                        msgBoard.setType(message.getType());
                        if(MsgTypeEnum.P.equals(message.getType())){
                            msgBoard.setTitle(message.getProduct().getCname());
                            msgBoard.setPrdId(message.getProduct().getId());
                            msgBoard.setStoreId(message.getStore().getId());
                            msgBoard.setStoreTitle(message.getStore().getCname());
                        }else if(MsgTypeEnum.S.equals(message.getType())){
                            msgBoard.setTitle(message.getStore().getCname());
                            msgBoard.setStoreId(message.getStore().getId());
                        }
                        
                        List<MemberMsg> result = new ArrayList<>();
                        result.add(entityTransforVO.memberMsgTransfor(message));
                        List<EcMemberMsg> list = ecMemberMsgFacade.findByParent(parentId);
                        if(CollectionUtils.isNotEmpty(list)){
                            for(EcMemberMsg msg:list){
                                result.add(entityTransforVO.memberMsgTransfor(msg));
                            }
                        }
                        msgBoard.setMsgList(result);
                    }
                    
                    resultList.add(msgBoard);
                }
            }
        }
        return resultList;
    }
    
    @GET
    @Path("findMessageByStore")
    @Produces("application/json; charset=UTF-8;")
    public List<MsgBoard> findMessageByStore(@Context HttpServletRequest request,
            @QueryParam(value = "storeId") Long storeId) {
        logger.debug("findMessageByStore storeId"+storeId);
        EcMember member = getAuthMember();
        EcStore store = ecStoreFacade.find(storeId);
        if(member==null || storeId==null){
            logger.error("findMessageByStore 查無會員及賣場資訊!");
            return null;
        }
        List<MsgBoard> resultList = new ArrayList<>();
        if(store.getSeller().getMember().equals(member)){//會員負責商店
            List<Long> idList = ecMemberMsgFacade.findParentByStore(store);
            if(CollectionUtils.isNotEmpty(idList)){
                for(Long parentId:idList){
                    MsgBoard msgBoard = new MsgBoard();
                    EcMemberMsg message = ecMemberMsgFacade.find(parentId);
                    if(message!=null){
                        msgBoard.setParentId(parentId);
                        msgBoard.setType(message.getType());
                        if(MsgTypeEnum.P.equals(message.getType())){
                            msgBoard.setTitle(message.getProduct().getCname());
                            msgBoard.setPrdId(message.getProduct().getId());
                            msgBoard.setStoreId(message.getStore().getId());
                            msgBoard.setStoreTitle(message.getStore().getCname());
                        }else if(MsgTypeEnum.S.equals(message.getType())){
                            msgBoard.setTitle(message.getStore().getCname());
                            msgBoard.setStoreId(message.getStore().getId());
                        }
                        
                        List<MemberMsg> result = new ArrayList<>();
                        result.add(entityTransforVO.memberMsgTransfor(message));
                        List<EcMemberMsg> list = ecMemberMsgFacade.findByParent(parentId);
                        if(CollectionUtils.isNotEmpty(list)){
                            for(EcMemberMsg msg:list){
                                result.add(entityTransforVO.memberMsgTransfor(msg));
                            }
                        }
                        msgBoard.setMsgList(result);
                    }
                    
                    resultList.add(msgBoard);
                }
            }
        }
        return resultList;
    }
    
    @POST
    @Path("addr/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public CusAddr addrCreate(@Context HttpServletRequest request, CusAddr cusAddr) {
        EcMember member = getAuthMember();
        try{
            EcCusAddr entity = new EcCusAddr();
            entity.setMember(member);
            entity.setAlias(cusAddr.getAlias());
            entity.setAddress( cusAddr.getAddress());
            entity.setPhone(cusAddr.getPhone());
            entity.setPrimary(cusAddr.isPrimary());
            entity.setCarNo(cusAddr.getCarNo());
            entity.setPatrolLatitude(cusAddr.getPatrolLatitude());
            entity.setPatrolLongitude(cusAddr.getPatrolLongitude());
            entity.setDeliveryId(cusAddr.getDeliveryId());
            entity = ecCusAddrFacade.save(entity);
            return entityTransforVO.cusAddrTransfor(entity);
        }catch(Exception e){
            logger.error("addrCreate Exception:\n", e);
            return null;
        }
    }
    
    @POST
    @Path("addr/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public CusAddr addrEdit(@Context HttpServletRequest request, CusAddr cusAddr) {
        EcMember member = getAuthMember();
        try{
            if(member==null || cusAddr ==null || cusAddr.getId()==null){
                return null;
            }
            
            EcCusAddr entity = ecCusAddrFacade.find(cusAddr.getId());
            entity.setMember(member);
            entity.setAlias(cusAddr.getAlias());
            entity.setAddress( cusAddr.getAddress());
            entity.setPhone(cusAddr.getPhone());
            entity.setPrimary(cusAddr.isPrimary());
            entity.setCarNo(cusAddr.getCarNo());
            entity.setPatrolLatitude(cusAddr.getPatrolLatitude());
            entity.setPatrolLongitude(cusAddr.getPatrolLongitude());
            entity.setDeliveryId(cusAddr.getDeliveryId());
            entity = ecCusAddrFacade.save(entity);
            return entityTransforVO.cusAddrTransfor(entity);
        }catch(Exception e){
            logger.error("addrCreate Exception:\n", e);
            return null;
        }
    }
    
    @GET
    @Path("addr/remove")
    @Produces("application/json; charset=UTF-8;")
    public String addrRemove(@Context HttpServletRequest request,
            @QueryParam(value = "id") Long id) {
        EcCusAddr entity = ecCusAddrFacade.find(id);
        if(id==null || entity==null){
            return "FAIL";
        }
        ecCusAddrFacade.remove(entity);
        return "SUCCESS";
    }
    
    // 所在區域選單
    @GET
    @Path("salesAreaOption")
    @Produces("application/json; charset=UTF-8;")
    public List<LongOptionVO> salesAreaOption(@Context HttpServletRequest request) {
//        logger.debug("salesAreaOption");
        try{
            return ecOptionFacade.findByTypeOptions(Long.parseLong("0"), OptionEnum.SALES_AREA.getCode());
        }catch(Exception e){
            logger.error("Exception:"+e);
            return null;
        }
    }
    
    // 產業別選單
    @GET
    @Path("industryOption")
    @Produces("application/json; charset=UTF-8;")
    public List<LongOptionVO> industryOption(@Context HttpServletRequest request) {
//        logger.debug("salesAreaOption");
        try{
            return ecOptionFacade.findByTypeOptions(Long.parseLong("0"), OptionEnum.INDUSTRY.getCode());
        }catch(Exception e){
            logger.error("Exception:"+e);
            return null;
        }
    }
    
    //升級為企業用戶
    @POST
    @Path("updateToCompany")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json; charset=UTF-8;")
    public Company updateToCompany(@Context HttpServletRequest request, Company company) {
        EcMember member = getAuthMember();
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstant.FORMAT_DATETIME);
        try{
            if(member==null){
                return null;
            }
            EcCompany entity;
            List<EcCompany> list = ecCompanyFacade.findByMainId(member.getId(), "M");
            if(CollectionUtils.isNotEmpty(list)){
                entity = list.get(0);
            }else{
                member.setType(MemberTypeEnum.COMPANY.getCode());
                ecMemberFacade.save(member);
                
                entity = new EcCompany();
                entity.setType("M");
                entity.setMainId(member.getId());
                entity.setCreator(member);
                entity.setCreatetime(new Date());
            }
            entity.setCname(company.getCname());
            entity.setIdCode(company.getIdCode());
            entity.setTel1(company.getTel1());
            entity.setOwner1(company.getOwner1());
            entity.setState(company.getState());
            entity.setCategory(company.getCategory());
            entity.setStartAt(sdf.parse(company.getStartAt()));
            entity.setCapital(company.getCapital());
            entity.setYearIncome(company.getYearIncome());
            entity.setModifier(member);
            entity.setModifytime(new Date());
            
            entity = ecCompanyFacade.save(entity);
            return entityTransforVO.companyTransfor(entity);
        }catch(Exception e){
            logger.error("addrCreate Exception:\n", e);
            return null;
        }
    }
    
    /**
     * 上傳產品照片
     * @param request
     * @param multiPart
     * @return
     */
    @Path("/picture/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadPicture(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        logger.info("uploadPrdPicture ...");
//        String root = GlobalConstant.DIR_STORE_IMG;
        String root = FileEnum.MEMBER_PIC.getRootDir();
        try{
            EcMember member = getAuthMember();
            if(member==null){
                return "FAIL";
            }
            
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
//                    String dir = root + store.getId();// 依店家區分路徑
                    String dir = root;//member
                    File dirfile = new File(dir);
                    dirfile.mkdirs(); //for several levels, without the "s" for one level
                    String saveFileName = UUID.randomUUID().toString() + "." +fext;
                    String saveFileNameFull = dir + GlobalConstant.FILE_SEPARATOR + saveFileName;
                    File file = new File(saveFileNameFull);
                    FileUtils.writeByteArrayToFile(file, content);
                    
                    // Save EcFile
                    EcFile fileEntity = new EcFile();
                    //member
                    fileEntity.setStoreId(null);
                    fileEntity.setPrimaryType(FileEnum.MEMBER_PIC.getPrimaryType());
                    fileEntity.setPrimaryId(member.getId());
                    fileEntity.setDescription("member picture");
                    
//                    fileEntity.setDescription(fileName);// 預設檔名，後續可修改
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
        return "SUCCESS";
    }
    
    @GET
    @Path("/picture/delete")
    @Produces("application/json; charset=UTF-8;")
    public String deletePicture(@Context HttpServletRequest request,
            @QueryParam(value = "oid") Long oid) {
        try {
            fileFacade.remove(oid);
        } catch (Exception ex) {
            logger.error("deletePicture ex:"+ex);
            return "FAIL";
        }
        return "SUCCESS";
    }
    
}

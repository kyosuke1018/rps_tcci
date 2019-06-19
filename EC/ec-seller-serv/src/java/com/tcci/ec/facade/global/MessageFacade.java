/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.global;

import cn.jsms.api.common.model.SMSPayload;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.SmsUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.enums.MessageCategoryEnum;
import com.tcci.ec.enums.MessageTypeEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PayStatusEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.RfqStatusEnum;
import com.tcci.ec.enums.ShipStatusEnum;
import com.tcci.ec.enums.SmsContentEnum;
import com.tcci.ec.enums.SmsProviderEnum;
import com.tcci.ec.model.OrderVO;
import com.tcci.ec.model.ProductVO;
import com.tcci.ec.model.rs.SmsVO;
import com.tcci.ec.service.push.PushService;
import com.tcci.fc.util.ResourceBundleUtils;
import java.text.MessageFormat;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author peter.pan
 */
@Stateless
public class MessageFacade  {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @EJB 
    private SysResourcesFacade sys;
   
    @Inject
    private PushService pushService;
    
    //<editor-fold defaultstate="collapsed" desc="for Push">
    // 賣家發送 Push 訊息
    // 賣家最後量價確認通知 // 20190321 
    public boolean confirmOrderFinal(OrderVO order, EcMember operator, Locale locale){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            if( order==null || order.getId()==null ){
                logger.error("confirmOrderFinal order==null || order.getId()==null !");
                return false;
            }
            
            String account = order.getLoginAccount();
            String category = MessageCategoryEnum.ORDER.getCode();
            String type = MessageTypeEnum.ORDER.getCode();

            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.013");// 您有一筆訂單最終量、價確認!
            // 您有一筆訂單最終量、價確認：訂單編號[{0}]
            String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.014"), order.getOrderNumber());
            pushService.sendPushAlias(category, order.getId().toString(), type, RfqStatusEnum.Quotation.getCode(), title, msg, account);
            return true;
        }catch(Exception e){
            sys.processUnknowException(operator, methodName, e);
        }
        return false;
    }
    
    // RFQ - 報價
    public boolean quotePrice(OrderVO order, EcMember operator, Locale locale){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            if( order==null || order.getId()==null ){
                logger.error("quotePrice order==null || order.getId()==null !");
                return false;
            }
            
            String account = order.getLoginAccount();
            String category = MessageCategoryEnum.ORDER.getCode();
            String type = MessageTypeEnum.ORDER.getCode();
            // 您有一筆訂單報價!
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.001");// 您有一筆訂單報價!
            // 您有一筆訂單報價! 訂單編號[{0}]
            String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.002"), order.getOrderNumber());
            pushService.sendPushAlias(category, order.getId().toString(), type, RfqStatusEnum.Quotation.getCode(), title, msg, account);
            return true;
        }catch(Exception e){
            sys.processUnknowException(operator, methodName, e);
        }
        return false;
    }
    
    // RFQ - 回覆訊息
    public boolean replyOrderMsg(OrderVO order, String message, EcMember operator, Locale locale){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            if( order==null || order.getId()==null ){
                logger.error("replyOrderMsg order==null || order.getId()==null !");
                return false;
            }

            String account = order.getLoginAccount();
            String category = MessageCategoryEnum.ORDER.getCode();
            // 您有一則訂單留言!
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.003");
            pushService.sendPushAlias(category, order.getId().toString(), title, message, account);
            return true;
        }catch(Exception e){
            sys.processUnknowException(operator, methodName, e);
        }
        return false;
    }
    
    // PO - 賣家變更量、價通知
    public boolean changeOrderBySeller(OrderVO order, String type, String status, EcMember operator, Locale locale){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            if( order==null || order.getId()==null ){
                logger.error("changeOrderStatusBySeller order==null || order.getId()==null !");
                return false;
            }
            String account = order.getLoginAccount();
            String category = MessageCategoryEnum.ORDER.getCode();
            // 訂單變更
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.015");
            // 您有一筆訂單量、價變更待確認：訂單編號[{0}]
            String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.016"), order.getOrderNumber());
            pushService.sendPushAlias(category, order.getId().toString(), type, status, title, msg, account);
            
            return true;
        }catch(Exception e){
            logger.error("order="+order+", type = "+type+", status="+status+", e = "+e.getMessage());
            sys.processUnknowException(operator, methodName, e);
        }
        return false;
    }
    
    // PO - 賣家變更狀態通知
    public boolean changeOrderStatusBySeller(OrderVO order, String type, String status, EcMember operator, Locale locale){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            if( order==null || order.getId()==null ){
                logger.error("changeOrderStatusBySeller order==null || order.getId()==null !");
                return false;
            }
            String account = order.getLoginAccount();
            String category = MessageCategoryEnum.ORDER.getCode();
            String message = "";
            if(MessageTypeEnum.ORDER.getCode().equals(type)){
                OrderStatusEnum enums = OrderStatusEnum.getFromCode(status);
                // 狀態變更
                message = ResourceBundleUtils.getMessage(locale, "notify.msg.010")+":["+enums.getDisplayName(locale)+"]";
            }else if(MessageTypeEnum.PAYMENT.getCode().equals(type)){
                PayStatusEnum enums = PayStatusEnum.getFromCode(status);
                // 收款狀態變更
                message = ResourceBundleUtils.getMessage(locale, "notify.msg.011")+":["+enums.getDisplayName(locale)+"]";
            }else if(MessageTypeEnum.SHIPPING.getCode().equals(type)){
                ShipStatusEnum enums = ShipStatusEnum.getFromCode(status);
                // 出貨狀態變更
                message = ResourceBundleUtils.getMessage(locale, "notify.msg.012")+":["+enums.getDisplayName(locale)+"]";
            }
            
            // 您的訂單狀態已變更! type : order, pay, ship
            String title = ResourceBundleUtils.getMessage(locale, "notify.msg.004");
            // 訂單編號[{0}], {1}
            String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.005"), order.getOrderNumber(), message);
            pushService.sendPushAlias(category, order.getId().toString(), type, status, title, msg, account);
            
            return true;
        }catch(Exception e){
            logger.error("order="+order+", type = "+type+", status="+status+", e = "+e.getMessage());
            sys.processUnknowException(operator, methodName, e);
        }
        return false;
    }
    
    // 管理員發送 Push 訊息
    // PRD - 審核結果通知
    public boolean applyShelvesResult(ProductVO vo, EcMember operator, Locale locale){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            if( vo==null || vo.getId()==null ){
                logger.error("applyShelvesResult vo==null || vo.getId()==null !");
                return false;
            }
            ProductStatusEnum statusEnum = ProductStatusEnum.getFromCode(vo.getStatus());
            String account = vo.getApplicantAccount();
            String category = MessageCategoryEnum.PRODUCT.getCode();
            String type = MessageTypeEnum.PRD_APPROVE.getCode();
            if( statusEnum==ProductStatusEnum.PASS ){
                // 商品上架申請已通過審核!
                String title = ResourceBundleUtils.getMessage(locale, "notify.msg.006");
                // 您的商品 [{0}] 上架申請已通過審核!
                String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.007"), vo.getCname());
                pushService.sendPushAlias(category, vo.getId().toString(), type, vo.getStatus(), title, msg, account);
            }else if( statusEnum==ProductStatusEnum.REJECT ){
                // 商品上架申請未通過審核!
                String title = ResourceBundleUtils.getMessage(locale, "notify.msg.008");
                // 您的商品[{0}]上架申請未通過審核，請修正不合宜的內容!
                String msg = MessageFormat.format(ResourceBundleUtils.getMessage(locale, "notify.msg.009"), vo.getCname());
                pushService.sendPushAlias(category, vo.getId().toString(), type, vo.getStatus(), title, msg, account);
            }else{
                logger.info("applyShelvesResult no need send message!");
            }
            
            return true;
        }catch(Exception e){
            sys.processUnknowException(operator, methodName, e);
        }
        return false;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for SMS">
    // 管理員發送 SMS 訊息
    // 註冊驗證碼通知
    public boolean verifyRegistration(String phone, String verifyCode, EcMember operator, Locale locale){
        SmsProviderEnum provider = GlobalConstant.SMS_PROVIDER;
        if( !GlobalConstant.SMS_ENABLED ){
            logger.info("verifyRegistration not support SMS!");
            return false;
        }
        if( provider != SmsProviderEnum.EVERY8D ){
            logger.error("verifyRegistration only support EVERY8D SMS!");
            return false;
        }
        
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            if( sys.isBlank(phone) ){
                logger.error("verifyRegistration phone==null !");
                return false;
            }
            
            SmsVO sms = sys.getSmsInfo(provider);
            if( sys.canUseSms(sms) ){
                sms.setContent(MessageFormat.format(SmsContentEnum.REGISTRY.getDisplayName(locale), verifyCode));
                sms.setReceiver(phone.trim());
                logger.info("verifyRegistration before sendByEvery8D ...");
                if( SmsUtils.sendByEvery8D(sms) ){// 發送簡訊
                    return true;
                }else{
                    logger.error("verifyRegistration sendByEvery8D result = "+sms.getResMsg());
                }
            }else{
                logger.error("verifyRegistration no SMS info !");
            }
        }catch(Exception e){
            sys.processUnknowException(operator, methodName, e);
        }
        return false;
    }
    
    // 密碼重設通知
    public boolean resetPassword(EcMember vo, String plaintext, EcMember operator, Locale locale){
        SmsProviderEnum provider = GlobalConstant.SMS_PROVIDER;
        if( !GlobalConstant.SMS_ENABLED ){
            logger.info("resetPassword not support SMS!");
            return false;
        }
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            if( vo==null || vo.getId()==null ){
                logger.error("resetPassword vo==null || vo.getId()==null !");
                return false;
            }
            
            if( provider == SmsProviderEnum.EVERY8D ){
                SmsVO sms = sys.getSmsInfo(provider);
                if( sys.canUseSms(sms) ){
                    sms.setContent(MessageFormat.format(SmsContentEnum.RESET_PASSWORD.getDisplayName(locale), plaintext));
                    sms.setReceiver(vo.getPhone());
                    logger.info("resetPassword before sendByEvery8D ...");
                    if( SmsUtils.sendByEvery8D(sms) ){// 發送簡訊
                        return true;
                    }else{
                        logger.error("resetPassword sendByEvery8D result = "+sms.getResMsg());
                    }
                }
            }else if( provider == SmsProviderEnum.JIGUANG ){
                SmsVO sms = sys.getSmsInfo(provider);
                if( sys.canUseSms(sms) ){
                    SMSPayload payload = SMSPayload.newBuilder()
                            .setMobileNumber(vo.getPhone()) // 18824419900, peter 15821144674
                            .setTempId(sys.getSmsPwdTempIdCn())
                            //.setSignId(sys.getSmsSingIdCn())
                            .addTempPara("code", plaintext)
                            .build();
                    if( SmsUtils.sendByJiguang(sms.getAccount(), sms.getPassword(), payload) ){// 發送簡訊
                        return true;
                    }else{
                        logger.error("resetPassword sendByJiguang result = "+sms.getResMsg());
                    }
                }
            }
        }catch(Exception e){
            sys.processUnknowException(operator, methodName, e);
        }
        return false;
    }
    //</editor-fold>
}

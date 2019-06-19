/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import cn.jsms.api.common.model.SMSPayload;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.SmsUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.enums.SmsContentEnum;
import com.tcci.ec.enums.SmsProviderEnum;
import com.tcci.ec.model.SmsVO;
import java.text.MessageFormat;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class MessageFacade  {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @EJB 
    private SysResourcesFacade sys;
    
    //<editor-fold defaultstate="collapsed" desc="for SMS">
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
            
            /*if( provider == SmsProviderEnum.EVERY8D ){
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
            }else*/ 
            if( provider == SmsProviderEnum.JIGUANG ){
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

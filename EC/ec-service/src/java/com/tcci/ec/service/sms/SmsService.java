/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.service.sms;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.enums.SmsProviderEnum;
import com.tcci.ec.model.rs.SmsVO;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Path("sms")
public class SmsService {
    private final static Logger logger = LoggerFactory.getLogger(SmsService.class);
    
    @Resource(mappedName = "jndi/ec.config")
    protected Properties jndiConfig;
    @Resource(mappedName = "jndi/ec-seller-serv.config")
    protected Properties servJndiConfig;
    
    private static SMSClient client = null;
    
     @GET
    @Path("test")
    @Produces("application/json; charset=UTF-8;")
//    @Produces(MediaType.TEXT_PLAIN)
    public String test(@Context HttpServletRequest request
            , @QueryParam("phone") String phone){
        EcMember member = new EcMember();
        member.setId(Long.parseLong("1313"));
        member.setPhone(phone);
        this.resetPassword(member, "abcd1234",  Locale.SIMPLIFIED_CHINESE);
        
        return "n/a";
    }
    
    // 密碼重設通知
//    public boolean resetPassword(EcMember vo, String plaintext, EcMember operator, Locale locale){
    public boolean resetPassword(EcMember vo, String plaintext, Locale locale){
        SmsProviderEnum provider = GlobalConstant.SMS_PROVIDER;
        if( !GlobalConstant.SMS_ENABLED ){
            logger.info("resetPassword not support SMS!");
            return false;
        }
        try{
            if( vo==null || vo.getId()==null ){
                logger.error("resetPassword vo==null || vo.getId()==null !");
                return false;
            }
            if( provider == SmsProviderEnum.JIGUANG ){
                SmsVO sms = this.getSmsInfo(provider);
                if( this.canUseSms(sms) ){
                    SMSPayload payload = SMSPayload.newBuilder()
                            .setMobileNumber(vo.getPhone()) // 18824419900, peter 15821144674
                            .setTempId(this.getSmsPwdTempIdCn())
//                            .setTempId(161022)
                            //.setSignId(sys.getSmsSingIdCn())
                            .addTempPara("code", plaintext)
                            .build();
                    if( this.sendByJiguang(sms.getAccount(), sms.getPassword(), payload) ){// 發送簡訊
                        return true;
                    }else{
                        logger.error("resetPassword sendByJiguang result = "+sms.getResMsg());
                    }
                }
            }
        }catch(Exception e){
            logger.error("SmsService resetPassword Exception:"+e);
        }
        return false;
    }
    
    //<editor-fold defaultstate="collapsed" desc="for SMS">
    public SmsVO getSmsInfo(SmsProviderEnum provider){
        SmsVO smsVO = new SmsVO();
        smsVO.setProvider(provider);
        if( provider==SmsProviderEnum.JIGUANG ){
//            Boolean enabledSMS = getSmsEnabledCn();
//            String accountSMS = getSmsAccountCn();
//            String passwordSMS = getSmsPasswordCn();
            Boolean enabledSMS = GlobalConstant.SMS_ENABLED;
            final String masterSecret = jndiConfig.getProperty("jpush.master");
            final String appKey = jndiConfig.getProperty("jpush.appkey");
            String accountSMS = masterSecret;
            String passwordSMS = appKey;
            
            smsVO.setEnabled(enabledSMS!=null?enabledSMS:false);
            smsVO.setAccount(accountSMS);
            smsVO.setPassword(passwordSMS);
        }
        return smsVO;
    }
    
    public boolean canUseSms(SmsVO smsVO){
        SmsProviderEnum provider = smsVO.getProvider();
        if( provider==SmsProviderEnum.JIGUANG ){
            if( !smsVO.isEnabled() ){
                logger.error("SMS not enabled !");
                return false;
            }
            if( StringUtils.isBlank(smsVO.getAccount()) ){
                logger.error("SMS not account !");
                return false;
            }
            if( StringUtils.isBlank(smsVO.getPassword()) ){
                logger.error("SMS not password !");
                return false;
            }
//            if( StringUtils.isBlank(smsVO.getSendSmsUrl()) ){
//                logger.error("SMS not SendSmsUrl !");
//                return false;
//            }
//            if( StringUtils.isBlank(smsVO.getGetCreditUrl()) ){
//                logger.error("SMS not GetCreditUrl !");
//                return false;
//            }
            
            return true;
        }
        
        logger.error("canUseSms error smsVO.getProvider() = "+smsVO.getProvider());
        return false;
    }
    
    public boolean sendByJiguang(String master, String appkey, SMSPayload payload) 
            throws APIConnectionException, APIRequestException {
        if( client==null ){
            client = new SMSClient(master, appkey);
        }
        //try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            logger.info("sendByJiguang res = "+res.toString());
            logger.info(res.isResultOK()?"sendSmsByJiguang SUCCESS":"sendSmsByJiguang FAIL");
            return res.isResultOK();
        /*
        }catch(APIConnectionException e){
            logger.error("sendByJiguang APIConnectionException : \n", e);
        }catch(APIRequestException e){
            logger.error("sendByJiguang APIRequestException : \n", e);
            logger.error("sendByJiguang HTTP Status: " + e.getStatus());
            logger.error("sendByJiguang Error Message: " + e.getMessage());
        }catch(Exception e){
            logger.error("sendByJiguang Exception: \n" + e.getMessage());
        }
        return false;
        */
    }
    
    public int getSmsPwdTempIdCn(){
        String txt = servJndiConfig.getProperty("SmsPwdTempIdCn");
        try{
            return Integer.parseInt(txt);
        }catch(Exception e){
            logger.error("getSmsPwdTempIdCn Exception:"+e);
//            processUnknowException(null, "getSmsPwdTempIdCn", e);
        }
        return 0;
    }
    //</editor-fold>
    
    /**
     *  
     *   int leftLimit = 97; // letter 'a'
     *   int rightLimit = 122; // letter 'z'
     *   int targetStringLength = 6;
     * @param leftLimit
     * @param rightLimit
     * @param targetStringLength
     * @return 
     */
    public static String genRandomString(int leftLimit, int rightLimit, int targetStringLength){
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) 
              (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        logger.debug("genRandomString = "+generatedString);
        return generatedString;
    }
}

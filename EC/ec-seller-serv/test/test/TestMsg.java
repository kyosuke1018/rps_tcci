/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package test;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.Notification;
import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;
import cn.jpush.api.report.ReceivedsResult;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.tcci.cm.util.SMSHttpService;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Aaron
 */
public class TestMsg {
    private final static Logger logger = LoggerFactory.getLogger(TestMsg.class);
    // EC2.0
    //private final static String MASTER_SECRET = "da7c7386f45ca642a9843c8e";
    //private final static String APP_KEY = "542a821cd3ae576d72809a9f";
    // EC1.5
    public static final String APP_KEY = "9141712fa3b3a2f645897109";
    public static final String MASTER_SECRET = "cb761f9ed1c356f69101319e";
    
    public static final int APP_SIGN_ID = 7528;// 簽名
    public static final int APP_TEMP_PWD_ID = 161022;// 预设登入密码通知 

    private static SMSClient client = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // jiguang jpush
        //testJPush();
        // jiguang sms
        sendSmsByJiguang3();
        // Every8D sms
        //sendSmsByEvery8D();
    }
    public static void sendSmsByJiguang3(){
        client = new SMSClient(MASTER_SECRET, APP_KEY);
        ArrayList<String> phones = new ArrayList<String>();
/**
13530668238 
13823688808 
13802244588 
 */        
        phones.add("13530668238");
        phones.add("13823688808");
        phones.add("13802244588");
        
        ArrayList<String> pwds = new ArrayList<String>();
        pwds.add("xzrjli");
        pwds.add("ilvfgi");
        pwds.add("qukkfi");
        for(int i=0; i<phones.size(); i++){
            SMSPayload payload = SMSPayload.newBuilder()
                    //.setMobileNumber("14714621548")
                    .setMobileNumber(phones.get(i)) // 14714621548 / 18824419900 / 15821144674 peter
                    .setTempId(APP_TEMP_PWD_ID)
                    //.setSignId(APP_SIGN_ID)
                    .addTempPara("code", pwds.get(i))
                    //.addTempPara("code", "qukkfi")
                    .build();
            try {
                SendSMSResult res = client.sendTemplateSMS(payload);
                logger.info(phones.get(i)+":"+pwds.get(i)+":"+res.toString());
                logger.info(res.isResultOK()?"sendSmsByJiguang SUCCESS":"sendSmsByJiguang FAIL");
            } catch (APIConnectionException e) {
                logger.error("APIConnectionException : \n", e);
            } catch (APIRequestException e) {
                logger.error("APIRequestException : \n", e);
                logger.info("HTTP Status: " + e.getStatus());
                logger.info("Error Message: " + e.getMessage());
            }
        }
    }

    /*
    076085163303   中山市建宏新型建材有限公司
    076085166300	 中山市众成新型墙体材料有限公司
    12750139797 	深圳市高茂环保建材有限公司

    中山市建宏新型建材有限公司电话：13631190512
    中山市众成新型墙体材料有限公司电话：18933415281
    深圳市高茂环保建材有限公司：电话：13750139797
    */
    public static void sendSmsByJiguang2(){
        client = new SMSClient(MASTER_SECRET, APP_KEY);
        ArrayList<String> phones = new ArrayList<String>();
        phones.add("13631190512");
        phones.add("18933415281");
        phones.add("13750139797");
        
        ArrayList<String> pwds = new ArrayList<String>();
        pwds.add("vtgpkp");
        pwds.add("vkvmcu");
        pwds.add("itgmob");
        for(int i=0; i<phones.size(); i++){
            SMSPayload payload = SMSPayload.newBuilder()
                    //.setMobileNumber("14714621548")
                    .setMobileNumber(phones.get(i)) // 14714621548 / 18824419900 / 15821144674 peter
                    .setTempId(APP_TEMP_PWD_ID)
                    //.setSignId(APP_SIGN_ID)
                    .addTempPara("code", pwds.get(i))
                    //.addTempPara("code", "qukkfi")
                    .build();
            try {
                SendSMSResult res = client.sendTemplateSMS(payload);
                logger.info(phones.get(i)+":"+pwds.get(i)+":"+res.toString());
                logger.info(res.isResultOK()?"sendSmsByJiguang SUCCESS":"sendSmsByJiguang FAIL");
            } catch (APIConnectionException e) {
                logger.error("APIConnectionException : \n", e);
            } catch (APIRequestException e) {
                logger.error("APIRequestException : \n", e);
                logger.info("HTTP Status: " + e.getStatus());
                logger.info("Error Message: " + e.getMessage());
            }
        }
    }
    
    // {"msg_id":"710899671572"}
    /**
     * 極光簡訊發送
     */
    public static void sendSmsByJiguang(){
        client = new SMSClient(MASTER_SECRET, APP_KEY);
        ArrayList<String> phones = new ArrayList<String>();
        phones.add("13878576322");
        phones.add("13660239221");
        phones.add("18078561281");
        phones.add("13809262775");
        phones.add("13928929433");
        phones.add("13928968820");
        phones.add("076085163303");
        phones.add("076085166300");
        phones.add("15986928060");
        phones.add("13926026548");
        phones.add("15807648257");
        phones.add("15876320212");
        phones.add("13750139802");
        phones.add("13750139808");
        phones.add("12750139797");
        phones.add("13823612931");
        phones.add("17329891102");
        phones.add("13929475877");
        phones.add("13530668238");
        phones.add("13823688808");
        phones.add("13802244588");

        ArrayList<String> pwds = new ArrayList<String>();
        pwds.add("hlpyeg");
        pwds.add("hyqtdx");
        pwds.add("xvjacc");
        pwds.add("dzhchx");
        pwds.add("ywlikd");
        pwds.add("dsdlkf");
        pwds.add("vtgpkp");
        pwds.add("vkvmcu");
        pwds.add("dvkekm");
        pwds.add("uvmlrq");
        pwds.add("ndvkaq");
        pwds.add("ujvnod");
        pwds.add("jdjyym");
        pwds.add("scnfia");
        pwds.add("itgmob");
        pwds.add("phmahx");
        pwds.add("xfclra");
        pwds.add("hyzjxt");
        pwds.add("xzrjli");
        pwds.add("ilvfgi");
        pwds.add("qukkfi");
        
        //for(int i=0; i<phones.size(); i++){
            SMSPayload payload = SMSPayload.newBuilder()
                    .setMobileNumber("14714621548")
                    //.setMobileNumber(phones.get(i)) // 14714621548 / 18824419900 / 15821144674 peter
                    .setTempId(APP_TEMP_PWD_ID)
                    //.setSignId(APP_SIGN_ID)
                    //.addTempPara("code", pwds.get(i))
                    .addTempPara("code", "qukkfi")
                    .build();
            try {
                SendSMSResult res = client.sendTemplateSMS(payload);
                //logger.info(phones.get(i)+":"+pwds.get(i)+":"+res.toString());
                logger.info(res.isResultOK()?"sendSmsByJiguang SUCCESS":"sendSmsByJiguang FAIL");
            } catch (APIConnectionException e) {
                logger.error("APIConnectionException : \n", e);
            } catch (APIRequestException e) {
                logger.error("APIRequestException : \n", e);
                logger.info("HTTP Status: " + e.getStatus());
                logger.info("Error Message: " + e.getMessage());
            }
        //}
    }
    
    /**
     * 傳送 Every8D 簡訊
     */
    public static void sendSmsByEvery8D(){
        SMSHttpService sms = new SMSHttpService();
        String userID = "0919953198";	//帳號
        String password = "ecsms121";	//密碼
        String subject = "EC2.0";	//簡訊主旨，主旨不會隨著簡訊內容發送出去。用以註記本次發送之用途。可傳入空字串。
        String content = "TCC-EC 台泥電商新會員註冊驗證碼通知，\n您的驗證碼為 [34526] 五碼數字。";//簡訊發送內容
        //String content = "TCC-EC忘記密碼通知，您的密碼已重設為 [wfad32fs] 八碼字元，您可登入系統再自行修改。";//簡訊發送內容
        String mobile = "0919953198";	//接收人之手機號碼。格式為: +886912345678或09123456789。多筆接收人時，請以半形逗點隔開( , )，如0912345678,0922333444。
        String sendTime = "";	//簡訊預定發送時間。-立即發送：請傳入空字串。-預約發送：請傳入預計發送時間，若傳送時間小於系統接單時間，將不予傳送。格式為YYYYMMDDhhmnss；例如:預約2009/01/31 15:30:00發送，則傳入20090131153000。若傳遞時間已逾現在之時間，將立即發送。
        
        if (sms.getCredit(userID, password)) {
            logger.info(new StringBuffer("取得餘額成功，餘額：").append(String.valueOf(sms.getCreditValue())).toString());
        } else {
            logger.info(new StringBuffer("取得餘額失敗，失敗原因：").append(sms.getProcessMsg()).toString());
        }
        if (sms.sendSMS(userID, password, subject, content, mobile, sendTime)) {
            logger.info(new StringBuffer("發送簡訊成功，餘額：").append(String.valueOf(sms.getCreditValue())).append("，簡訊批號：").append(sms.getBatchID()).toString());
        } else {
            logger.info(new StringBuffer("發送簡訊失敗，失敗原因：").append(sms.getProcessMsg()).toString());
        }
    }
    
    /**
     * 極光推播發送
     */
    public static void testJPush(){
        Long msgId = sendPush(MASTER_SECRET, APP_KEY);
        if( msgId!=null ){
            String msgIds = msgId.toString();
            runReceivedsResult(MASTER_SECRET, APP_KEY, msgIds);
        }
    }
    
    public static JPushClient customPushClient(String masterSecret, String appKey){
        ClientConfig config = ClientConfig.getInstance();
        config.setMaxRetryTimes(5);
        config.setConnectionTimeout(10 * 1000);	// 10 seconds
        config.setSSLVersion("TLSv1.1");// JPush server supports SSLv3, TLSv1, TLSv1.1, TLSv1.2
        
        config.setApnsProduction(false);// development env
        config.setTimeToLive(60 * 60 * 24); // one day
        //config.setGlobalPushSetting(false, 60 * 60 * 24); // development env, one day
        return new JPushClient(masterSecret, appKey, null, config); 	// JPush client
        //return new JPushClient(masterSecret, appKey);
    }
    
    public static Long sendPush(String masterSecret, String appKey){
        JPushClient jpushClient = customPushClient(masterSecret, appKey);
        
        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert();
        try {
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Got result - " + result);
            Thread.sleep(5000);
            // 请求结束后，调用 NettyHttpClient 中的 close 方法，否则进程不会退出。
            jpushClient.close();

            if( result.getResponseCode()==200 && result.statusCode==0 ){
                return result.msg_id;
            }
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            logger.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            logger.error("Should review the error, and fix the request", e);
            logger.error("HTTP Status: " + e.getStatus());
            logger.error("Error Code: " + e.getErrorCode());
            logger.error("Error Message: " + e.getErrorMessage());
        } catch (InterruptedException ex) {
            logger.error("InterruptedException: \n", ex);
        }
        return null;
    }

    // 快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知。
    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll(ALERT);
    }
    
    // 构建推送对象：所有平台，推送目标是别名为 "alias1"，通知内容为 ALERT。
    public static PushPayload buildPushObject_all_alias_alert() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("alias1"))
                .setNotification(Notification.alert(ALERT))
                .build();
    }

    // 构建推送对象：平台是 Andorid 与 iOS，
    // 推送目标是 （"tag1" 与 "tag2" 的并集）交（"alias1" 与 "alias2" 的并集），
    // 推送内容是 - 内容为 MSG_CONTENT 的消息，并且附加字段 from = JPush。
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent("JPush TEST tag & alias")
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }

    // 构建推送对象：推送内容包含SMS信息
    public static void testSendWithSMS(String masterSecret, String appKey){
        JPushClient jpushClient = customPushClient(masterSecret, appKey);
        try {
            SMS sms = SMS.content("Test SMS", 10);
            PushResult result = jpushClient.sendAndroidMessageWithAlias("Test SMS", "test sms", sms, "alias1");
            logger.info("Got result - " + result);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            logger.error("Error response from JPush server. Should review and fix it. ", e);
            logger.error("HTTP Status: " + e.getStatus());
            logger.error("Error Code: " + e.getErrorCode());
            logger.error("Error Message: " + e.getErrorMessage());
        }
    }    
    
    public static void runReceivedsResult(String masterSecret, String appKey, String msgIds){
        logger.info("runReceivedsResult msgIds = "+msgIds);
        JPushClient jpushClient = customPushClient(masterSecret, appKey);
        try {
            ReceivedsResult result = jpushClient.getReportReceiveds(msgIds);
            logger.info("Got result - " + result);
            Thread.sleep(5000);
            // 请求结束后，调用 NettyHttpClient 中的 close 方法，否则进程不会退出。
            jpushClient.close();
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            logger.error("Connection error, should retry later \n", e);
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            logger.error("Should review the error, and fix the request \n", e);
            logger.error("HTTP Status: \n" + e.getStatus());
            logger.error("Error Code: \n" + e.getErrorCode());
            logger.error("Error Message: \n" + e.getErrorMessage());
        } catch (InterruptedException ex) {
            logger.error("InterruptedException: \n", ex);
        }
    }
}

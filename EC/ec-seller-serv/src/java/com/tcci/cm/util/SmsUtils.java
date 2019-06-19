/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.tcci.ec.model.rs.SmsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class SmsUtils {
    private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);
    
    private static SMSClient client = null;
    
    /**
     * Every8D SMS
     * @param vo
     * @return 
     */
    public static boolean sendByEvery8D(SmsVO vo) {
        logger.info("sendByEvery8D SmsVO "+vo.getProvider());
        SMSHttpService sms = new SMSHttpService(vo.getSendSmsUrl(), vo.getGetCreditUrl());
        //String userID = "0919******";	//帳號
        //String password = "ecs**1**";	//密碼
        //String subject = "EC2.0";	//簡訊主旨，主旨不會隨著簡訊內容發送出去。用以註記本次發送之用途。可傳入空字串。
        //String content = "TCC-EC 台泥電商新會員註冊驗證碼通知，\n您的驗證碼為 [34526] 五碼數字。";//簡訊發送內容
        //String content = "TCC-EC忘記密碼通知，您的密碼已重設為 [wfad32fs] 八碼字元，您可登入系統再自行修改。";//簡訊發送內容
        //String mobile = "0919953198";	//接收人之手機號碼。格式為: +886912345678或09123456789。多筆接收人時，請以半形逗點隔開( , )，如0912345678,0922333444。
        //String sendTime = "";	//簡訊預定發送時間。-立即發送：請傳入空字串。-預約發送：請傳入預計發送時間，若傳送時間小於系統接單時間，將不予傳送。格式為YYYYMMDDhhmnss；例如:預約2009/01/31 15:30:00發送，則傳入20090131153000。若傳遞時間已逾現在之時間，將立即發送。
        /*if (sms.getCredit(vo.getAccount(), vo.getPassword())) {
            vo.setResMsg(new StringBuffer("取得餘額成功，餘額：").append(String.valueOf(sms.getCreditValue())).toString());
        } else {
            vo.setResMsg(new StringBuffer("取得餘額失敗，失敗原因：").append(sms.getProcessMsg()).toString());
            return false;
        }*/
        if( sms.sendSMS(vo.getAccount(), vo.getPassword(), vo.getSubject(), vo.getContent(), vo.getReceiver(), "")) {
            vo.setResMsg(new StringBuffer("發送簡訊成功，餘額：").append(String.valueOf(sms.getCreditValue())).append("，簡訊批號：").append(sms.getBatchID()).toString());
            return true;
        } else {
            vo.setResMsg(new StringBuffer("發送簡訊失敗，失敗原因：").append(sms.getProcessMsg()).toString());
            return false;
        }
    }

    /*****
     * Jiguang SMS
     * 
        *HTTP 验证
        使用 HTTP Basic Authentication 的方式做访问授权。这样整个 API 请求可以使用常见的 HTTP 工具来完成，比如：curl，浏览器插件等；

        *HTTP Header（头）里加一个字段（Key/Value对）：
        Authorization: Basic base64_auth_string
        其中 base64_auth_string 的生成算法为：base64(appKey:masterSecret)，即:对 appKey 加上冒号，加上 masterSecret 拼装起来的字符串，
        再做 base64 转换。appKey、masterSecret 可以在控制台应用设置中查看。

        * 发送单条模板短信 API
        *调用地址
        POST https://api.sms.jpush.cn/v1/messages
        *请求示例
        curl --insecure -X POST -v https://api.sms.jpush.cn/v1/messages -H "Content-Type: application/json" -u "7d431e42dfa6a6d693ac2d04:5e987ac6d2e04d95a9d8f0d1" \
        -d '{"mobile":"xxxxxxxxxxxxxx","sign_id":*,"temp_id":1,"temp_para":{"xxxx":"xxxx"}}'

        * 参数
        KEY	REQUIRE	DESCRIPTION
        mobile	TRUE	手机号码
        sign_id	FALSE	签名ID，该字段为空则使用应用默认签名
        temp_id	TRUE	模板ID
        temp_para	FALSE	模板参数,需要替换的参数名和 value 的键值对

        * 返回示例
        发送成功
        {"msg_id": 288193860302}
        发送失败
        {
            "error": {
                "code": *****,
                "message": "******"
            }
        }
    ****/
    public static boolean sendByJiguang(String master, String appkey, SMSPayload payload) 
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
}

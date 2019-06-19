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
import com.tcci.ec.model.SmsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
public class SmsUtils {
    private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);
    
    private static SMSClient client = null;

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

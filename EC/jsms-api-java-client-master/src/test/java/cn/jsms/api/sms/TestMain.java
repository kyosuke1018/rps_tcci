/*
 * Copyright 2018 penpl.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jsms.api.sms;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.BaseTest;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.google.gson.JsonObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author penpl
 */
public class TestMain {
    private static Logger LOG = LoggerFactory.getLogger(TestMain.class);
    private static SMSClient client = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        client = new SMSClient(BaseTest.MASTER_SECRET, BaseTest.APP_KEY);
        testSendTempSMS();
    }

    public static void testSendTempSMS() {
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("+919953198")
                .setTempId(BaseTest.APP_TEMP_PWD_ID)
                //.setSignId(BaseTest.APP_SIGN_ID)
                .addTempPara("code", "2fty67")
                .build();
        /*
        JsonObject json = new JsonObject();
        json.addProperty("mobile", "13800138000");
        json.addProperty("temp_id", BaseTest.APP_TEMP_PWD_ID);
        
        JsonObject tempJson = new JsonObject();
        tempJson.addProperty("code", "2fty67");
        json.add("temp_para", tempJson);
        
        assertEquals(payload.toJSON(), json);
        */
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            assertTrue(res.isResultOK());
            LOG.info(res.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. \n", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. \n", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

}

/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.cm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Every8D
 * @author Aaron
 */
public class SMSHttpService {
    
    private String sendSMSUrl = "http://api.every8d.com/API21/HTTP/sendSMS.ashx";
    private String getCreditUrl = "http://api.every8d.com/API21/HTTP/getCredit.ashx";
    private String processMsg = "";
    private String batchID = "";
    private double credit = 0;
    
    public SMSHttpService(){}

    public SMSHttpService(String sendSMSUrl, String getCreditUrl) {
        this.sendSMSUrl = sendSMSUrl;
        this.getCreditUrl = getCreditUrl;
    }
    
    /// <summary>
    /// 取得帳號餘額
    /// </summary>
    /// <param name="userID">帳號</param>
    /// <param name="password">密碼</param>
    /// <returns>true:取得成功；false:取得失敗</returns>
    public boolean getCredit(String userID, String password) {
        boolean success = false;
        try {
            StringBuilder postDataSb = new StringBuilder();
            postDataSb.append("UID=").append(userID);
            postDataSb.append("&PWD=").append(password);
            
            String resultString = this.httpPost(this.getCreditUrl, postDataSb.toString());
            if (!resultString.startsWith("-")) {
                this.credit = Double.parseDouble(resultString);
                success = true;
            } else {
                this.processMsg = resultString;
            }
        } catch (Exception ex) {
            this.processMsg = ex.getMessage();
        }
        return success;
    }
    
    /// <summary>
    /// 傳送簡訊
    /// </summary>
    /// <param name="userID">帳號</param>
    /// <param name="password">密碼</param>
    /// <param name="subject">簡訊主旨，主旨不會隨著簡訊內容發送出去。用以註記本次發送之用途。可傳入空字串。</param>
    /// <param name="content">簡訊發送內容</param>
    /// <param name="mobile">接收人之手機號碼。格式為: +886912345678或09123456789。多筆接收人時，請以半形逗點隔開( , )，如0912345678,0922333444。</param>
    /// <param name="sendTime">簡訊預定發送時間。-立即發送：請傳入空字串。-預約發送：請傳入預計發送時間，若傳送時間小於系統接單時間，將不予傳送。格式為YYYYMMDDhhmnss；例如:預約2009/01/31 15:30:00發送，則傳入20090131153000。若傳遞時間已逾現在之時間，將立即發送。</param>
    /// <returns>true:傳送成功；false:傳送失敗</returns>
    public boolean sendSMS(String userID, String password, String subject, String content, String mobile, String sendTime) {
        boolean success = false;
        try {
            StringBuilder postDataSb = new StringBuilder();
            postDataSb.append("UID=").append(userID);
            postDataSb.append("&PWD=").append(password);
            postDataSb.append("&SB=").append(subject);
            postDataSb.append("&MSG=").append(content);
            postDataSb.append("&DEST=").append(mobile);
            postDataSb.append("&ST=").append(sendTime);
            
            String resultString = this.httpPost(this.sendSMSUrl, postDataSb.toString());
            if (!resultString.startsWith("-")) {
                /*
                * 傳送成功 回傳字串內容格式為：CREDIT,SENDED,COST,UNSEND,BATCH_ID，各值中間以逗號分隔。
                * CREDIT：發送後剩餘點數。負值代表發送失敗，系統無法處理該命令
                * SENDED：發送通數。
                * COST：本次發送扣除點數
                * UNSEND：無額度時發送的通數，當該值大於0而剩餘點數等於0時表示有部份的簡訊因無額度而無法被發送。
                * BATCH_ID：批次識別代碼。為一唯一識別碼，可藉由本識別碼查詢發送狀態。格式範例：220478cc-8506-49b2-93b7-2505f651c12e
                */
                String[] split = resultString.split(",");
                this.credit = Double.parseDouble(split[0]);
                this.batchID = split[4];
                success = true;
            } else {
                //傳送失敗
                this.processMsg = resultString;
            }
            
        } catch (Exception ex) {
            this.processMsg = ex.getMessage();
        }
        return success;
    }
    
    private String httpPost(String url, String postData) {
        String result = "";
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            BufferedWriter osw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            
            osw.write(postData);
            osw.flush();
            osw.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder sb = new StringBuilder();
            for (line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
            conn.disconnect();
            result = sb.toString();
        } catch (Exception ex) {
            this.processMsg = ex.getMessage();
        }
        return result;
    }
    
    public String getProcessMsg() {
        return processMsg;
    }
    
    public String getBatchID() {
        return batchID;
    }
    
    public double getCreditValue() {
        return credit;
    }

    public String getSendSMSUrl() {
        return sendSMSUrl;
    }

    public void setSendSMSUrl(String sendSMSUrl) {
        this.sendSMSUrl = sendSMSUrl;
    }

    public String getGetCreditUrl() {
        return getCreditUrl;
    }

    public void setGetCreditUrl(String getCreditUrl) {
        this.getCreditUrl = getCreditUrl;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }
}

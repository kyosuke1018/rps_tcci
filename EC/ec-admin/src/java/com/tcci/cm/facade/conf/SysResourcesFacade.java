/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.conf;

import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.model.interfaces.IOperator;
import com.tcci.cm.util.AES;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.NetworkUtils;
import com.tcci.cm.util.NotificationUtils;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.SmsProviderEnum;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.SmsVO;
import com.tcci.ec.model.rs.ImageVO;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server Resources (JNDI, ...)
 * @author peter.pan
 */
@Stateless
public class SysResourcesFacade {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected @EJB ImageFacade img;
    
    @Resource(mappedName = "jndi/ec-admin.config")
    protected Properties jndiConfig;
    
    @Resource(mappedName = "jndi/global.config")
    private Properties jndiGlobalConfig;

    public String getJndiValue(String key){
        String txt = null;
        try{
            txt = jndiConfig.getProperty(key);
        }catch(Exception e){
            logger.error("getJndiValue exception: key = "+key+" \n", e);
        }
        return txt;
    }
    public Boolean getJndiBoolean(String key){
        try{
            return Boolean.parseBoolean(getJndiValue(key));
        }catch(Exception e){
            logger.error("getJndiBoolean exception: key = "+key+" \n", e);
        }
        return null;
    }
    public Integer getJndiInt(String key){
        try{
            return Integer.parseInt(getJndiValue(key));
        }catch(Exception e){
            logger.error("getJndiInt exception: key = "+key+" \n", e);
        }
        return null;
    }
    
    public String getNotifyAdmins(){
        return getJndiValue(GlobalConstant.JNDI_ADMIN_EMAIL);
    }
    
    public String getRestUrlPrefix(){
        String txt = getJndiValue("restUrlPrefix");
        return txt;
    }
    
    public boolean isValidId(Long id){
        return (id!=null && id>0);
    }
    
    public boolean isTrue(Boolean flag){
        return (flag!=null && flag);
    }
    
    public boolean isFalse(Boolean flag){
        return (flag!=null && !flag);
    }
    
    public boolean isEmpty(List list){
        return (list==null || list.isEmpty());
    }
    
    public boolean isBlank(String str){
        return (str==null || str.trim().isEmpty());
    }
    
    public int size(List list){
        return isEmpty(list)?0:list.size();
    }

    /**
     * 文管系統
     * @return 
     */
    public String getDocPubUrl(){
        String url = "";
        try{
            url = jndiGlobalConfig.getProperty(GlobalConstant.JNDI_DOCPUB_URL);
        }catch(Exception e){
            logger.error("getDocPubUrl exception:\n", e);
        }
        return url;
    }
    
    //<editor-fold defaultstate="collapsed" desc="for 密碼">
    /**
     * 密碼編碼
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public String encryptPwdForTansfer(String password, String securityKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        AES aes = new AES();
        String encrypted = aes.encrypt(password, securityKey);
        return encrypted;
    }
    public String decryptPwdForTansfer(String encrypted, String securityKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, GeneralSecurityException, KeyException, IOException {
        AES aes = new AES();
        String infos = aes.decrypt(encrypted, securityKey);
        return infos;
    }
    
    /**
     * 單向加密(信息摘要) for 手機登入傳遞
     * @param info
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public byte[] encrypt(String info) throws NoSuchAlgorithmException
    {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] srcBytes = info.getBytes();
        // 使用 srcBytes 更新摘要
        sha.update(srcBytes);
        // 完成哈希計算，得到 result
        byte[] resultBytes = sha.digest();
        return resultBytes;
    }
    public String toEncryptHexString(byte[] b) {
        final char[] hexChar = {
               '0', '1', '2', '3', '4', '5', '6', '7',
               '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
           };
 
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }
    public String getEncryptString(String info) throws NoSuchAlgorithmException{
        return toEncryptHexString(encrypt(info));
    }
    //</editor-fold>
       
    //<editor-fold defaultstate="collapsed" desc="for Mobile Proxy">
    public String getMobileProxyUrl(){
        String txt = getJndiValue("mobileProxyUrl");
        return txt;
    }
    public String getCheckMemberService(){
        String txt = getJndiValue("checkMemberService");
        return txt;
    }
    public String getSellerWebUrl(){
        String txt = getJndiValue("sellerWebUrl");
        return txt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for JClient Push">
    public String getJPushMaster(){
        String txt = getJndiValue("jpush.master");
        return txt;
    }
    public String getJPushAppkey(){
        String txt = getJndiValue("jpush.appkey");
        return txt;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for SMS TW JNDI">
    public Boolean getSmsEnabledTw(){
        return getJndiBoolean("SmsEnabledTw");
    }
    public String getSmsAccountTw(){
        String txt = getJndiValue("SmsAccountTw");
        return txt;
    }
    public String getSmsPasswordTw(){
        String txt = getJndiValue("SmsPasswordTw");
        return txt;
    }
    public String getSmsSubjectTw(){
        String txt = getJndiValue("SmsSubjectTw");
        return txt;
    }
    public String getSmsSendSMSUrlTw(){
        String txt = getJndiValue("SmsSendSMSUrlTw");
        return txt;
    }
    public String getSmsGetCreditUrlTw(){
        String txt = getJndiValue("SmsGetCreditUrlTw");
        return txt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for SMS CN JNDI">
    public Boolean getSmsEnabledCn(){
        return getJndiBoolean("SmsEnabledCn");
    }
    public String getSmsAccountCn(){// Jiguang MasterSecert
        String txt = getJndiValue("SmsAccountCn");
        return txt;
    }
    public String getSmsPasswordCn(){// Jiguang AppKey
        String txt = getJndiValue("SmsPasswordCn");
        return txt;
    }
    public int getSmsSingIdCn(){
        String txt = getJndiValue("SmsSingIdCn");
        try{
            return Integer.parseInt(txt);
        }catch(Exception e){
            processUnknowException(null, "getSmsSingIdCn", e);
        }
        return 0;
    }
    public int getSmsPwdTempIdCn(){
        String txt = getJndiValue("SmsPwdTempIdCn");
        try{
            return Integer.parseInt(txt);
        }catch(Exception e){
            processUnknowException(null, "getSmsPwdTempIdCn", e);
        }
        return 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for SMS">
    public SmsVO getSmsInfo(SmsProviderEnum provider){
        SmsVO smsVO = new SmsVO();
        smsVO.setProvider(provider);
        if( provider==SmsProviderEnum.JIGUANG ){
            Boolean enabledSMS = getSmsEnabledCn();
            String accountSMS = getSmsAccountCn();
            String passwordSMS = getSmsPasswordCn();
            
            smsVO.setEnabled(enabledSMS!=null?enabledSMS:false);
            smsVO.setAccount(accountSMS);
            smsVO.setPassword(passwordSMS);
        }else if( provider==SmsProviderEnum.EVERY8D ){
            Boolean enabledSMS = getSmsEnabledTw();
            String accountSMS = getSmsAccountTw();
            String passwordSMS = getSmsPasswordTw();
            String sendSmsUrlSMS = getSmsSendSMSUrlTw();
            String getCreditUrlSMS = getSmsGetCreditUrlTw();
            
            smsVO.setEnabled(enabledSMS!=null?enabledSMS:false);
            smsVO.setAccount(accountSMS);
            smsVO.setPassword(passwordSMS);
            smsVO.setSendSmsUrl(sendSmsUrlSMS);
            smsVO.setGetCreditUrl(getCreditUrlSMS);
        }
        return smsVO;
    }
    
    public boolean canUseSms(SmsVO smsVO){
        SmsProviderEnum provider = smsVO.getProvider();
        if( provider==SmsProviderEnum.EVERY8D ){
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
            if( StringUtils.isBlank(smsVO.getSendSmsUrl()) ){
                logger.error("SMS not SendSmsUrl !");
                return false;
            }
            if( StringUtils.isBlank(smsVO.getGetCreditUrl()) ){
                logger.error("SMS not GetCreditUrl !");
                return false;
            }
            
            return true;
        }else if( provider==SmsProviderEnum.JIGUANG ){
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
            
            return true;
        }
        
        logger.error("canUseSms error smsVO.getProvider() = "+smsVO.getProvider());
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Exception">
    /**
     * 未知例外處理
     * @param operator
     * @param methodName
     * @param e 
     */
    public void processUnknowException(IOperator operator, String methodName, Exception e){
        UUID uuid = UUID.randomUUID();
        String constraintViolations = "";
        if(e instanceof EJBException){
            constraintViolations = ExceptionHandlerUtils.printConstraintViolationException((EJBException)e);
        }
        processUnknowException(operator, methodName, e, uuid.toString(), constraintViolations);
    }
    public void processUnknowException(IOperator operator, String methodName, Exception e, String errorCode, String constraintViolations){
        String hostname = NetworkUtils.getHostIP(); // WebUtils.getHostName();
        String datetime = DateUtils.format(new Date());
        logger.error("processUnknowException ["+datetime+"]["+hostname+"]["+methodName+"] ("+errorCode+") Exception:\n", e);

        NotificationUtils sender = new NotificationUtils();
        sender.notifyOnException(getNotifyAdmins(), (operator!=null)?operator.getLabel():null, methodName, e, errorCode, constraintViolations);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Ec File Process">
    /**
     * 寫入實體檔案
     * @param fileEnum
     * @param storeId
     * @param content
     * @param fext
     * @return
     * @throws IOException 
     */
    public FileVO writeRealFile(FileEnum fileEnum, String oriFileName, byte[] content, boolean isImage, Long keyId) throws IOException{
        FileVO fileVO = new FileVO();
        // 取附檔名
        String[] fs = FileUtils.getFileExtension(oriFileName);
        String name = fs[0];
        String fext = fs[1];
        // 儲存實體檔案
        String root = fileEnum.getRootDir();
        String dir = root + (keyId!=null?keyId:"");
        File dirfile = new File(dir);
        dirfile.mkdirs(); //for several levels, without the "s" for one level
        String saveFileName = UUID.randomUUID().toString() + "." +fext;
        String saveFileNameFull = dir + GlobalConstant.FILE_SEPARATOR + saveFileName;
        File file = new File(saveFileNameFull);
        FileUtils.writeByteArrayToFile(file, content);
        logger.info("writeRealFile save file finish. oriFileName="+oriFileName+", saveFileNameFull = \n"+saveFileNameFull);
        
        if( isImage ){
            // 縮圖
            ImageVO retImgVO = new ImageVO();
            // 取得Image寬高
            String errMsg1 = img.getImageInfo(saveFileNameFull, retImgVO);
            if( errMsg1!=null ){
                logger.error("writeRealFile getImageInfo ... "+errMsg1);
            }else{
                // 縮圖 (縮圖需寬高資訊)
                //String errMsg2 = img.compressImageFile(dir, saveFileName, saveFileNameFull, true, retImgVO);
                //if( errMsg2!=null ){
                //    logger.error("writeRealFile compressImageFile ... "+errMsg1);
                //}
            }
            logger.info("writeRealFile compress image finish.");
        }
        
        fileVO.setFileSize(content.length);
        fileVO.setFilename(oriFileName);
        fileVO.setName(name);
        fileVO.setSavedir(dir);
        fileVO.setSavename(saveFileName);
        fileVO.setSaveFileNameFull(saveFileNameFull);
        return fileVO;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for Verify Code">
    public String genVerifyCode(int num) {
        String all = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        while(num>0){
            int idx = getRandomNumberInRange(0, all.length()-1);
            if( idx==all.length()-1 ){
                sb.append(all.substring(idx));
            }else{
                sb.append(all.substring(idx, idx+1));
            }
            num--;
        }
        
        return sb.toString();
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    //</editor-fold>
}

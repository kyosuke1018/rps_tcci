/***********************************************************************
 * Module:  StringUtils.java
 * Author:  jackson
 * Purpose: Defines the Class StringUtils
 ***********************************************************************/
package com.tcci.fc.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 字串處理工具
 * 
 * @pdOid 92947af2-7e5b-4dc9-a163-526682a2937a */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    private final static Logger logger = LoggerFactory.getLogger(StringUtils.class);
    private final static int DESCRIPTION_BYTE_LEN = 4000;
    private final static String DESCRIPTION_ENCODING = "UTF-8";

    protected StringUtils() {
        // hide from public access
    }

    /**
     * <p>Case insensitive check if a String starts with a specified prefix.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case insensitive.</p>
     *
     * <pre>
     * StringUtils.startsWithIgnoreCase(null, null)      = true
     * StringUtils.startsWithIgnoreCase(null, "abcdef")  = false
     * StringUtils.startsWithIgnoreCase("abc", null)     = false
     * StringUtils.startsWithIgnoreCase("abc", "abcdef") = true
     * StringUtils.startsWithIgnoreCase("abc", "ABCDEF") = true
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     * @param str  the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case insensitive, or
     *  both <code>null</code>
     * @since 2.4
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    /**
     * <p>Check if a String starts with a specified prefix.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * <pre>
     * StringUtils.startsWith(null, null)      = true
     * StringUtils.startsWith(null, "abcdef")  = false
     * StringUtils.startsWith("abc", null)     = false
     * StringUtils.startsWith("abc", "abcdef") = true
     * StringUtils.startsWith("abc", "ABCDEF") = false
     * </pre>
     *
     * @see java.lang.String#startsWith(String)
     * @param str  the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @return <code>true</code> if the String starts with the prefix, case sensitive, or
     *  both <code>null</code>
     * @since 2.4
     */
    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    /**
     * <p>Check if a String starts with a specified prefix (optionally case insensitive).</p>
     *
     * @see java.lang.String#startsWith(String)
     * @param str  the String to check, may be null
     * @param prefix the prefix to find, may be null
     * @param ignoreCase inidicates whether the compare should ignore case
     *  (case insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or
     *  both <code>null</code>
     */
    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
    }

    /**
     * 計算字串長度
     * @param str 字串
     * @return 字串的長度
     */
    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    /**
     * StringUtils.getTokens(null, *) = [] StringUtils.getTokens("", *) = []
     * StringUtils.getTokens("abc def", -1) = []
     * StringUtils.getTokens("abc def", 0) = []
     * StringUtils.getTokens("abc  def", 2) = ["ab", "c ", "de", "f"]
     * 
     * @param sData
     *            原始字串
     * @param iLength
     *            切割的長度
     * @return
     */
    public static String[] getTokens(String sData, int iLength) {

        if (null == sData || iLength < 1) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        List<String> tokens = new ArrayList<String>();

        int iLeft = 0;

        int iDataLen = sData.length();

        while (iLeft < iDataLen) {

            int iRight = (iLeft + iLength) > iDataLen ? iDataLen : iLeft
                    + iLength;

            String sToken = sData.substring(iLeft, iRight);

            iLeft += iLength;

            tokens.add(sToken);
        }

        return (String[]) tokens.toArray(new String[tokens.size()]);
    }

    /**
     * StringUtils.getTokens(null, *) = [] StringUtils.getTokens("", *) = []
     * StringUtils.getTokens("abc def", null) = ["abc", "def"]
     * StringUtils.getTokens("abc def", " ") = ["abc", "def"]
     * StringUtils.getTokens("abc  def", " ") = ["abc", "def"]
     * StringUtils.getTokens("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * StringUtils.getTokens("ab:cd:ef:", ":") = ["ab", "cd", "ef", ""]
     * 
     * @param sData
     * @param sDelim
     * @return
     */
    public static String[] getTokens(String sData, String sDelim) {

        if (null == sData) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        List<String> tokens = new ArrayList<String>();

        int iDataLen = sData.length();
        int iDelimLen = sDelim.length();

        int iLeft = 0;
        int iRight = sData.indexOf(sDelim);

        while (iRight >= 0) {

            String sToken = sData.substring(iLeft, iRight).trim();
            tokens.add(sToken);
            iLeft = iRight + iDelimLen;
            iRight = sData.indexOf(sDelim, iLeft);
        }

        if (iLeft < iDataLen) {
            String sToken = sData.substring(iLeft, iDataLen);
            tokens.add(sToken);
        }

        // 取最後一個token，如果為delim則加入一個空白("")token
        if (iDataLen >= iDelimLen) {
            String sLastToken = sData.substring(iDataLen - iDelimLen, iDataLen);

            if (sLastToken.equals(sDelim)) {
                tokens.add("");
            }
        }
        return (String[]) tokens.toArray(new String[tokens.size()]);

    }

    /**
     * 
     * StringUtils.replaceDelim(null, null, null) = null
     * StringUtils.replaceDelim("", null, null) = ""
     * StringUtils.replaceDelim("a*b*c*, "*", [])           = "a*b*c*"
     * StringUtils.replaceDelim("a*b*c*", "*", null) = "a*b*c*"
     * StringUtils.replaceDelim("a*b*c*", "*", [1]) = "a*b*c*"
     * StringUtils.replaceDelim("a*b*c*", "*", [1, 2, 3]) = "a1b2c3"
     * StringUtils.replaceDelim("a*b*c*", "&", [1, 2, 3]) = "a*b*c*"
     * 
     * @param sSource
     *            string to seach and replace in, may be null
     * @param sDelim
     *            the string to search for, may be null
     * @param with
     *            the strings to replace with
     * @return
     */
    public static String replaceDelim(String sSource, String sDelim,
            String with[]) {

        if (sSource == null || sDelim == null || with == null
                || sDelim.length() == 0 || with.length == 0) {
            return sSource;
        }

        String[] tokens = StringUtils.getTokens(sSource, sDelim);

        // token number - 1 == with number
        if ((tokens.length - 1) != with.length) {
            return sSource;
        }

        StringBuffer sb = new StringBuffer("");

        for (int i = 0; i < with.length; i++) {

            sb.append(tokens[i]);

            sb.append(with[i]);
        }

        // last token
        sb.append(tokens[tokens.length - 1]);

        return sb.toString();

    }

    public static String replaceDelim(String sSource, String sDelim,
            List<?> with) {

        String[] array = (String[]) with.toArray(new String[with.size()]);

        return replaceDelim(sSource, sDelim, array);

    }

    /**
     * 
     * @param sValue
     * @return
     */
    public static int str2Int(String sValue, int iDefaultValue) {
        int iValue = iDefaultValue;
        try {
            iValue = Integer.parseInt(sValue);
        } catch (Exception e) {
            iValue = iDefaultValue;
        }

        return iValue;
    }

    public static int str2Int(String sValue) {
        return StringUtils.str2Int(sValue, 0);
    }

    /**
     * StringUtils.getMoneyStr(null) = "" StringUtils.getMoneyStr(" ") = ""
     * StringUtils.getMoneyStr("1234567.89") = "1,234,567.89"
     * StringUtils.getMoneyStr("1234567.00") = "1,234,567";
     * StringUtils.getMoneyStr("1234567.0100") = "1,234,567.01";
     * 
     * @param sMoney
     * @return
     */
    public static String getMoneyStr(String sMoney) {

        if (isBlank(sMoney)) {
            return "";
        }

        sMoney = sMoney.trim();

        StringBuffer sb = new StringBuffer();
        int iLen = sMoney.length();

        if (sMoney.startsWith("+") || sMoney.startsWith("-")) {

            String sSign = substring(sMoney, 0, 1);
            // '-' 放回去, '+' 濾除
            sb.append(sSign.equals("-") ? sSign : "");
            sMoney = substring(sMoney, 1);
        }

        // 整數
        String sInt = "";

        // 小數
        String sDecimal = "";

        // 小數點
        String sDot = "";

        int index = sMoney.indexOf(".");

        if (index >= 0) {
            sDot = ".";
            sInt = sMoney.substring(0, index);
            if ((index + 1) < iLen) {
                sDecimal = sMoney.substring(index + 1, sMoney.length());
            }
        } else {
            sInt = sMoney;
        }

        // 整數
        sInt = getIntMoneyStr(sInt);

        // 小數
        sDecimal = trimRightZero(sDecimal);

        // 有整數部分
        if (sInt.length() > 0) {
            sb.append(sInt);

            if (sDecimal.length() > 0) {
                sb.append(sDot).append(sDecimal);
            }
        } // 沒有整數部分
        else {
            if (sDecimal.length() > 0) {
                sb.append("0.").append(sDecimal);
            } else {
                sb.append("0");
            }
        }

        return sb.toString();
    }

    /**
     * StringUtils.getMoneyStr(null, 1) = "" StringUtils.getMoneyStr(" ", 2) =
     * "" StringUtils.getMoneyStr("1234567.89", 2) = "1,234,567.89"
     * StringUtils.getMoneyStr("1234567.00", 1) = "1,234,567.0";
     * StringUtils.getMoneyStr("1234567.0100", 2) = "1,234,567.01";
     * 
     * @param sMoney
     * @param iScale
     * @return
     */
    public static String getMoneyStr(String sMoney, int iScale) {

        if (isBlank(sMoney)) {
            return "";
        }

        sMoney = sMoney.trim();

        StringBuffer sb = new StringBuffer();
        int iLen = sMoney.length();

        if (sMoney.startsWith("+") || sMoney.startsWith("-")) {

            String sSign = substring(sMoney, 0, 1);
            // '-' 放回去, '+' 濾除
            sb.append(sSign.equals("-") ? sSign : "");
            sMoney = substring(sMoney, 1);
        }

        // 整數
        String sInt = "";

        // 小數
        String sDecimal = "";

        // 小數點
        String sDot = "";

        int index = sMoney.indexOf(".");

        if (index >= 0) {
            sDot = ".";
            sInt = sMoney.substring(0, index);
            if ((index + 1) < iLen) {
                sDecimal = sMoney.substring(index + 1, sMoney.length());
            }
        } else {
            sInt = sMoney;
        }

        // 整數
        sInt = getIntMoneyStr(sInt);

        // 小數
        sDecimal = substring(sDecimal, 0, iScale);

        // 有整數部分
        if (sInt.length() > 0) {
            sb.append(sInt);

            if (sDecimal.length() > 0) {
                sb.append(sDot).append(rightPad(sDecimal, iScale, "0"));
            } else {
                if (iScale > 0) {
                    sb.append(".").append(rightPad(sDecimal, iScale, "0"));
                }
            }
        } // 沒有整數部分
        else {
            if (sDecimal.length() > 0) {
                sb.append("0.").append(rightPad(sDecimal, iScale, "0"));
            } else {
                if (iScale > 0) {
                    sb.append("0.").append(rightPad(sDecimal, iScale, "0"));
                } else {
                    sb.append("0");
                }
            }
        }

        return sb.toString();
    }

    /**
     * StringUtils.getIntMoneyStr("001234567") = "1,234,567"
     * 
     * @param sInt
     * @return
     */
    public static String getIntMoneyStr(String sInt) {

        if (isBlank(sInt)) {
            return "";
        }

        sInt = sInt.trim();

        //        
        // long lAmountInteger = Long.parseLong(sInt);
        //
        // String sPattern = "###,###";
        //
        // DecimalFormat df = new DecimalFormat(sPattern);
        //
        // return df.format(lAmountInteger);

        StringBuffer sb = new StringBuffer();

        sInt = trimLeftZero(sInt);
        int iLen = sInt.length();

        for (int i = 0; i < iLen; i++) {

            char ch = sInt.charAt(i);

            sb.append(ch);
            // 剩餘長度
            int iRemainLen = iLen - i - 1;

            if ((iRemainLen > 0) && (iRemainLen % 3 == 0)) {
                sb.append(",");
            }

        }
        return sb.toString();

    }

    /**
     * 將數字字串依照指定的小數位數輸出成浮點數字串<br>
     * parseFloat("12345678", 2) -> "123456.78"<br>
     * parseFloat("12345678", 6) -> "12.345678"<br>
     * parseFloat("12345678", 10) -> "0.0012345678"<br>
     * parseFloat("12345678", 0) -> "12345678"<br>
     * parseFloat("12345678", -1) -> "12345678"<br>
     * parseFloat("a12s5w6dd", 3) -> ""<br>
     * parseFloat("", 3) -> ""<br>
     * parseFloat(null, 3) -> ""<br>
     * 
     * @param sSourceStr
     *            原始字串
     * @param iScale
     *            小數位數
     * @return
     */
    public static String parseFloat(String sSourceStr, int iScale) {

        if (!NumberUtils.isDigits(sSourceStr)) {
            return "";
        }

        if (StringUtils.isBlank(sSourceStr) || iScale < 1) {
            return StringUtils.defaultString(sSourceStr);
        }

        String sInt = "";

        String sDecimal = "";

        int iLength = sSourceStr.length();

        if (iLength > iScale) {
            // 整數長度
            int iIntLength = iLength - iScale;

            sInt = sSourceStr.substring(0, iIntLength);

            sDecimal = sSourceStr.substring(iIntLength);
        } else {

            sInt = "0";

            sDecimal = StringUtils.leftPad(sSourceStr, iScale, "0");

        }

        return sInt + "." + sDecimal;
    }

    /**
     * StringUtils.trimLeftZero("0012345600") = "12345600"
     * 
     * @param sSource
     * @return
     */
    public static String trimLeftZero(String sSource) {

        if (sSource == null) {
            return "";
        }

        int iLen = sSource.length();
        int index = -1;
        for (int i = 0; (i < iLen && index < 0); i++) {
            char ch = sSource.charAt(i);

            if (ch != '0') {
                index = i;
            }
        }
        String s = "";
        // 發現非0之數字
        if (index >= 0) {
            s = sSource.substring(index, iLen);
        }

        return s;

    }

    /**
     * StringUtils.trimLeft("   345600") = "345600"<br>
     * StringUtils.trimLeft("   345600  ") = "345600  "
     * 
     * @param sSource
     * @return
     */
    public static String trimLeft(String sSource) {

        if (sSource == null) {
            return "";
        }

        int iLen = sSource.length();
        int index = -1;
        for (int i = 0; (i < iLen && index < 0); i++) {
            char ch = sSource.charAt(i);

            if (ch != ' ') {
                index = i;
            }
        }
        String s = "";

        if (index >= 0) {
            s = sSource.substring(index, iLen);
        }

        return s;

    }

    /**
     * StringUtils.trimRightZero("01234500") = "012345"
     * 
     * @param sSource
     * @return
     */
    public static String trimRightZero(String sSource) {

        if (sSource == null) {
            return "";
        }

        int iLen = sSource.length();
        int index = -1;
        // 由後至前
        for (int i = (iLen - 1); (i >= 0 && index < 0); i--) {
            char ch = sSource.charAt(i);
            if (ch != '0') {
                index = i;
            }
        }

        String s = "";
        if (index >= 0) {
            s = sSource.substring(0, index + 1);
        }

        return s;
    }

    /**
     * StringUtils.trimRight("1234500  ") = "012345"<br>
     * StringUtils.trimRight("  1234500  ") = "  012345"
     * 
     * @param sSource
     * @return
     */
    public static String trimRight(String sSource) {

        if (sSource == null) {
            return "";
        }

        int iLen = sSource.length();
        int index = -1;
        // 由後至前
        for (int i = (iLen - 1); (i >= 0 && index < 0); i--) {
            char ch = sSource.charAt(i);
            if (ch != ' ') {
                index = i;
            }
        }

        String s = "";
        if (index >= 0) {
            s = sSource.substring(0, index + 1);
        }

        return s;
    }

    public static String trimZero(String sRate) {

        if (sRate == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int iLen = sRate.length();

        // 整數
        String sInt = "";

        // 小數
        String sDecimal = "";

        // 小數點
        String sDot = "";

        int index = sRate.indexOf(".");

        if (index >= 0) {
            sDot = ".";
            sInt = sRate.substring(0, index);
            if ((index + 1) < iLen) {
                sDecimal = sRate.substring(index + 1, sRate.length());
            }
        } else {
            sInt = sRate;
        }

        // 整數
        sInt = trimLeftZero(sInt);

        // 小數
        sDecimal = trimRightZero(sDecimal);

        // 有整數部分
        if (sInt.length() > 0) {
            sb.append(sInt);

            if (sDecimal.length() > 0) {
                sb.append(sDot).append(sDecimal);
            }
        } // 沒有整數部分
        else {
            if (sDecimal.length() > 0) {
                sb.append("0.").append(sDecimal);
            } else {
                sb.append("0.0");
            }
        }

        // sb.append("%");

        return sb.toString();
    }

    /**
     * StringUtils.getRateStr("000122.00100") = "122.001"
     * StringUtils.getRateStr("00.12") = "0.12" StringUtils.getRateStr("00.00")
     * = "0.0"
     * 
     * @param sRate
     * @return
     */
    public static String getRateStr(String sRate) {
        return trimZero(sRate);
    }

    /**
     * 取得整數部分
     * 
     * StringUtils.getRateStr("000122.00100") = "122"
     * 
     * @param sRate
     * @return
     */
    public static String getIntPart(String sRate) {

        if (sRate == null) {
            return "";
        }

        // 整數
        String sInt = "";

        int index = sRate.indexOf(".");

        if (index >= 0) {
            sInt = sRate.substring(0, index);
        } else {
            sInt = sRate;
        }

        // 整數
        sInt = trimLeftZero(sInt);

        return sInt;
    }

    public static String leftPadByByteLenth(String sData, int iLength,
            String sPadStr) {

        if (sData == null) {
            return null;
        }

        if (sPadStr == null || sPadStr.length() == 0) {
            sPadStr = " ";
        }

        String sResult = sData;
        while (sResult.getBytes().length < iLength) {
            sResult = sPadStr + sResult;
        }

        return sResult;
    }

    public static String rightPadByByteLenth(String sData, int iLength,
            String sPadStr) {

        if (sData == null) {
            return null;
        }

        if (sPadStr == null || sPadStr.length() == 0) {
            sPadStr = " ";
        }
        String sResult = sData;

        while (sResult.getBytes().length < iLength) {
            sResult += sPadStr;
        }

        return sResult;
    }

    /**
     * trim 全形空白<br>
     * 
     * @param sValue
     * @return
     */
    public static String trimRightBigSpace(String sValue) {
        String sResult = sValue;

        while (sResult.endsWith("　")) {
            sResult = sResult.substring(0, sResult.length() - 1);
        }

        return sResult;
    }

    /**
     * 替換"\"為"/"
     * 
     * @param s
     *            需替換的字串
     * @return 取代完成的字串
     */
    public static String replaceBackSlash(String s) {
        String result = null;
        if (s != null) {
            result = s.replace('\\', '/');
        }
        return result;
    }

    /**
     * 將參數加入原本的 QueryString中。
     * 
     * @param queryString
     * @param params
     * @return
     */
    public static String queryStringAssembler(String queryString, Map<String, Object> params) {
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object obj = params.get(key);
            if (obj == null) {
                obj = "";
            }
            queryString = queryString.replaceAll(":" + key, "'" + (String) obj + "'");
        }
        return queryString;
    }

    /**
     * 字串比對: 避免字串因null而產生NullPointException
     * @param one 字串1
     * @param two 字串2
     * @return 
     */
    public static int nullSafeStringComparator(final String one, final String two) {
        if (one == null ^ two == null) {
            return (one == null) ? -1 : 1;
        }

        if (one == null && two == null) {
            return 0;
        }

        return one.compareToIgnoreCase(two);
    }

    /**
     * 數字字串比對: 
     * <ol>
     * <li>避免字串因null而產生NullPointException</li>
     * <li>若傳入字串全為數字，則以數字方式比對(12>2); 否則以字串方式比對</li>
     * <ol>
     * @param one 字串1
     * @param two 字串2
     * @return 
     */
    public static int nullSafeDigitalStringComparator(final String one, final String two) {
        if (one == null ^ two == null) {
            return (one == null) ? -1 : 1;
        }

        if (one == null && two == null) {
            return 0;
        }
        
        if (StringUtils.isNumeric(one)&&StringUtils.isNumeric(two)){
            return Float.valueOf(one).compareTo(Float.valueOf(two));
        }

        return one.compareToIgnoreCase(two);
    }
    
    public static boolean checkDataLength(String value, int pLength) {
        boolean valid = true;
        if(StringUtils.isBlank(value)){
            return valid;
        }
        int length =  value.length();
        if(pLength != length){
            return !valid;
        }else{
            return valid;
        }
    }
    public static boolean checkMaxLength(String value, int maxLength) {
        boolean valid = true;
        if(StringUtils.isBlank(value)){
            return valid;
        }
        int length =  value.length();
        if(maxLength < length){
            return !valid;
        }else{
            return valid;
        }
    }
    public static String checkMaxLength(String columnName ,String value, int maxLength, boolean required) {
        String chkMsg = "";

        if(isBlank(value)){
            if(required){
                chkMsg = "["+columnName+"]必填!";
                return chkMsg;
            }
        }else{
            if(false == checkMaxLength(value, maxLength)){
                chkMsg = "["+columnName+"]長度太長! 最大長度="+maxLength;
            }
        }
        return chkMsg;
    }
    
    /**
     * 安全切斷字串 (效能考量，不適合字串很長的狀況)
     * @param oriStr
     * @param encoding
     * @param stepLen 每次切掉字數 (調整控制速度與精確度)
     * @param maxByteLen
     * @return 
     */
    public static String safeTruncat(String oriStr, String encoding, int stepLen, int maxByteLen) throws UnsupportedEncodingException{
        if( oriStr==null ){ return null; }
        
        String newStr = oriStr;
        
        while( true ){
            byte[] bytes = newStr.getBytes(encoding);
            int byteLen = bytes.length;
            
            if( byteLen > maxByteLen-1 ){
                newStr = newStr.substring(0, newStr.length()-stepLen);
            }else{
                return newStr;
            }
        }
    }
    
    /**
     * 
     * @param str
     * @return 
     */
    public static List<String> strToList(String str, String del) {
        List<String> resList = null;
        if( str!=null && !str.isEmpty() ){
            String[] mailUserAry = str.split(del);
            if( mailUserAry!=null ){
                resList = new ArrayList<String>();
                for(int i=0; i<mailUserAry.length; i++){
                    resList.add(mailUserAry[i].trim());
                }
            }
        }
        
        return resList;
    }
    
    /**
     * 清除小數位多餘零
     *
     * @param str 原始字符串
     * @return
     */
    public static String trim(String str) {
        if (str.indexOf(".") != -1 && str.charAt(str.length() - 1) == '0') {
            return trim(str.substring(0, str.length() - 1));
        } else {
            return str.charAt(str.length() - 1) == '.' ? str.substring(0, str.length() - 1) : str;
        }
    }
    
    /**
     * 最長顯示字數
     *
     * @param ori
     * @return
     */
    public static String showMaxTxt(String ori, int len) {
        if (ori == null) {
            return "";
        } else if (ori.length() <= len) {
            return ori;
        } else {
            return ori.substring(0, len) + "...";
        }
    }
    
    /**
     * 反向(右至左)顯示最長顯示字數
     *
     * @param ori
     * @param len
     * @return
     */
    public static String showMaxTxtReverse(String ori, int len) {
        if (ori == null) {
            return "";
        } else if (ori.length() <= len) {
            return ori;
        } else {
            return "..." + ori.substring(ori.length()-len);
        }
    }
    public static String checkNotBlank(String value, String columnName) {
        String chkMsg = null;
        if (isBlank(value)) {
            chkMsg = "["+columnName+"]必填! ";
                return chkMsg;
        }
        
        return chkMsg;
    }
    public static String checkValue(String columnName1 ,String value1,String columnName2 , String value2) {
        String chkMsg = "";
        if(null == value1 || null == value2){
            return chkMsg;
        }
        if(!value1.equals(value2)){
            chkMsg = "["+columnName1+"]("+ value1 +")與["+columnName2+"]不符! [" + columnName2 +"]="+value2;
        }
        return chkMsg;
    }
    public static  String getDescription(String logString){
        String description = "";
        if( logString!=null && !logString.isEmpty() ){
            description = description + "\n" +logString + "";
        }
        
        try {
            description = StringUtils.safeTruncat(description, DESCRIPTION_ENCODING, 5, DESCRIPTION_BYTE_LEN);
        } catch (UnsupportedEncodingException ex) {
            logger.error("logQueryCond Exception :]\n", ex);
        }
        return description;
    }
    
    /**
     * StringUtils.getLeftZeroStr(1, 4) = "0001"
     * 指定字串長度 左邊補零
     * @param value
     * @param len
     * @return
     */
    public static String getLeftZeroStr(int value,int len) {
        String formatStr = "%0"+len+"d";
        return String.format(formatStr, value);
    }
}
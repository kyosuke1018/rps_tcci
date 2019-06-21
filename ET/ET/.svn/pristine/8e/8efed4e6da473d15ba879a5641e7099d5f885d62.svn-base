/**
 * ===========================================================================
 * IISI Confidential
 *
 * Title: 常用運算功能 例: createInteger() createBigInteger()
 *
 * (C) Copyright IISI Corp. 2006.
 * ===========================================================================
 */
package com.tcci.fc.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * version 1.0, 2006/12/14
 */
public class NumberUtils extends org.apache.commons.lang.math.NumberUtils {
    private static Logger logger = LoggerFactory.getLogger(NumberUtils.class);
    protected NumberUtils() {
        // hide from public access
    }

    /**
     * 檢核-輸入小數至x位<pre>
     * checkDecimalLength( "0", 2) = true
     * checkDecimalLength( "100.1", 2) = true
     * checkDecimalLength( "100.01", 2) = true
     * checkDecimalLength( "100.010", 2) = false
     * checkDecimalLength( "100.011", 2) = false
     *
     * @param sNumeric 數值
     * @param iDecimal 小數位數
     * @return
     */
    public static boolean checkDecimalLength(String sNumeric, int iDecimal) {
        if (NumberUtils.isNumber(sNumeric)) {

//            int iLength = sNumeric.length();

            int iNu = sNumeric.indexOf(".");

            if (iNu > 0) {
                // 有小數
                String fraction = sNumeric.substring(iNu + 1);
                if (fraction.length() > iDecimal) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @param sNumeric input value
     * @param iDecimal 小數位數
     * @param length 長度
     * @param paddChar padding char 0x30
     * @return
     */
    public static String formatNumericStr(String sNumeric, int iDecimal, int length, char paddChar) {

        boolean bRc = true;

        String sResult = sNumeric;

        if (NumberUtils.isNumber(sNumeric)) {

            int iLength = sNumeric.length();

            int iNu = sNumeric.indexOf(".");
            if (iNu > 0) {
                if (iNu + iDecimal + 1 < iLength) {
                    iLength = iNu + iDecimal + 1;
                }
            }

            StringBuffer aBuf = new StringBuffer();
            for (int i = 0; i < iLength; i++) {
                char Char = sNumeric.charAt(i);
                if (bRc) {
                    if (Char != 0x30) {
                        bRc = false;
                        aBuf.append(Char);
                    }
                } else {
                    aBuf.append(Char);
                }
            }

            sResult = aBuf.toString();

            if (sResult.length() == 0) {
                sResult = ".";
            }

            if (sResult.substring(0, 1).equals(".")) {
                sResult = "0" + sResult;
            }


            // padding right
            iLength = sResult.length();
            iNu = sResult.indexOf(".");
            if (iNu > 0) {
                if (iLength - iNu - 1 < iDecimal) {
                    sResult = StringUtils.rightPad(sResult, iDecimal + iNu + 1, paddChar);
                }
            }

            // padding left
            sResult = StringUtils.leftPad(sResult, length, paddChar);

            if (iDecimal == 0) {
                int iDecimalBegin = sResult.indexOf('.');
                if (iDecimalBegin > 0) {
                    sResult = sResult.substring(0, iDecimalBegin);
                }
            }
        }

        return sResult;
    }

    /**
     * 轉換阿拉白數字金額至中文金額
     *
     * @param String 阿拉白數字金額
     * @return String 中文金額
     */
    @SuppressWarnings("unchecked")
    public static String formatAmountNT(String number) {

        String numberStr = "";
        boolean flag = false; //是否有小數
        ArrayList bList = new ArrayList();
        bList.add("元整");
        bList.add("萬");
        bList.add("億");

        //判斷是否為浮點數，如果是則「見角進一」
        int num = number.indexOf(".");
        String substr = ""; //小數部份
        if (num != -1) { //有小數金額
            //if (Integer.parseInt(number.substring(num + 1, number.length())) > 0) {
            //number = "" + (Integer.parseInt(number.substring(0, num)) + 1);
            //}
            //else {
            substr = number.substring(num + 1, number.length());
            flag = true;
            number = "" + Integer.parseInt(number.substring(0, num));

            //}
        }
        //判斷金額是否為0元
        if (Integer.parseInt(number) == 0) {
            if (!flag) {
                return "零元整";
            } else {
                if (Integer.parseInt(substr) == 0) {
                    return "零元整";
                }
            }
        }

        num = number.length();
        ArrayList aList = new ArrayList();
        while (num > 0) {
            if (num >= 4) { //有仟位
                aList.add(number.substring(num - 4, num));
            }
            if (num < 4 && num > 0) { //沒有仟位，但是大於零元
                aList.add(number.substring(0, num));
            }
            num = num - 4; //每四位為一個處理單元
        }
        num = aList.size();
        //      System.out.println("num::" + num);
        while (num > 0) {//對每個處理單元(四位)做處理(仟、仟萬、仟億....)，如果都是0(0000)，則不顯示該Block的單位(仟、萬、億)
            String sBlock = aList.get(num - 1).toString();
            if (Integer.parseInt(sBlock) != 0 || num == 1) {
                numberStr += convert(sBlock) + bList.get(num - 1);
            }
            num--;
        }
        //處理浮點數
        if (flag == true && Integer.parseInt(substr) != 0) {
            numberStr = numberStr.substring(0, numberStr.length() - 2) + convertSmlnumber(substr);
        }
        return numberStr;
    }

    /**
     * 轉換小數部分阿拉白數字金額至小數部分中文金額
     *
     * @param String 小數部分阿拉白數字金額
     * @return String 小數部分中文金額
     */
    @SuppressWarnings("unchecked")
    private static String convertSmlnumber(String number) {
        ArrayList cList = new ArrayList(); //小數金額
        ArrayList bList = new ArrayList(); //整數金額
        bList.add("零");
        bList.add("壹");
        bList.add("貳");
        bList.add("參");
        bList.add("肆");
        bList.add("伍");
        bList.add("陸");
        bList.add("柒");
        bList.add("捌");
        bList.add("玖");

        cList.add("元");
        cList.add("角");
        cList.add("分");
        cList.add("厘");

        String strnum = "";
        String rtnstr = "";
        int num = 0;
        if (number.length() > 3) {
            strnum = number.substring(0, 3);
        } else {
            strnum = number;
        }
        for (int i = 0; i < strnum.length(); i++) {
            if (i == 0) {
                rtnstr += (String) cList.get(i);
            }
            num = Integer.parseInt(strnum.substring(i, i + 1));
            rtnstr += (String) bList.get(num);
            if (num != 0) {
                rtnstr += (String) cList.get(i + 1);
            }
        }
        //去掉末尾聯繫的"零"
        while (rtnstr.substring(rtnstr.length() - 1, rtnstr.length()).equals(bList.get(0))) {
            rtnstr = rtnstr.substring(0, rtnstr.length() - 1);
        }
        //如果中間兩個聯繫為零,則去掉一個
        for (int m = 0; m < rtnstr.length() - 1; m++) {
            if (rtnstr.substring(m, m + 1).equals(rtnstr.substring(m + 1, m + 2))) {
                rtnstr = rtnstr.substring(0, m) + rtnstr.substring(m + 1, rtnstr.length());
            }
        }
        return rtnstr;
    }

    /**
     * 判斷傳入的BigDecimal是否為整數
     * @param bd
     * @return 
     */
    public static boolean isIntegerValue(BigDecimal bd) {
        return bd.signum() == 0 || bd.scale() <= 0
                || bd.stripTrailingZeros().scale() <= 0;
    }
     
    /**
     * 傳入數字小寫，返回大寫漢字
     *
     * @param String number
     * @return String number in Chinese
     */
    @SuppressWarnings("unchecked")
    private static String convert(String number) {
        int num = 0;
        String numStr = null;
        ArrayList aList = new ArrayList();
        ArrayList bList = new ArrayList();

        int longNum = number.length();
        //      System.out.println("***number***:" + number);
        bList.add("零");
        bList.add("壹");
        bList.add("貳");
        bList.add("參");
        bList.add("肆");
        bList.add("伍");
        bList.add("陸");
        bList.add("柒");
        bList.add("捌");
        bList.add("玖");
        bList.add("萬");
        bList.add("仟");
        bList.add("佰");
        bList.add("拾");
        bList.add("元");
        bList.add("整");
        for (int i = 0; i < longNum; i++) {
            numStr = number.substring(i, i + 1);
            num = Integer.parseInt(numStr);
            numStr = (String) bList.get(num);
            aList.add(numStr);
            if (num != 0) {
                num = number.substring(i).length();
                if (num == 5) {
                    aList.add(bList.get(10));
                }
                if (num == 4) {
                    aList.add(bList.get(11));
                }
                if (num == 3) {
                    aList.add(bList.get(12));
                }
                if (num == 2) {
                    aList.add(bList.get(13));
                }
            }
        }

        longNum = aList.size();
        for (int i = longNum - 1; i >= 0; i--) {
            if (aList.get(i).equals(bList.get(0))) {
                aList.remove(i);
            } else {
                break;
            }
        }

        longNum = aList.size();
        for (int i = 0; i < longNum - 2; i++) {
            if (aList.get(i).equals(bList.get(0)) && aList.get(i + 1).equals(bList.get(0)) && aList.get(i + 2).equals(bList.get(0))) {
                aList.remove(i);
                aList.remove(i + 1);
                i++;
            }
        }


        longNum = aList.size();
        for (int i = 0; i < longNum - 1; i++) {
            if (aList.get(i).equals(bList.get(0)) && aList.get(i + 1).equals(bList.get(0))) {
                aList.remove(i);
            }
        }

        longNum = aList.size();
        numStr = "";
        for (int i = 0; i < longNum; i++) {
            numStr += (String) aList.get(i);
        }

        return numStr;
    }
    public static String getPriceDisplay(BigDecimal price, int newScale) {
        price = price.setScale(newScale, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(price);
    }
    /**
     * 依幣別
     * @param PurchaseAmountMap(String=幣別,BigDecimal=金額)
     * @param beforeString 
     * @return 
     */
    public static String getTotalPurchaseAmountString(HashMap<String,BigDecimal> PurchaseAmountMap, String beforeString){
        logger.debug("getTotalPurchaseAmountString");
        if(PurchaseAmountMap.isEmpty()){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isBlank(beforeString)){
            sb.append("採購總金額 ");
        }else{
            sb.append(beforeString).append(" ");
        }
        String[] keys = {"TWD","RMB"};//先顯示的幣別
        String key_currency = keys[0];//TWD
        if(PurchaseAmountMap.containsKey(key_currency)){
            int newScale = 2;
            BigDecimal price = PurchaseAmountMap.get(key_currency);
            String priceString = NumberUtils.getPriceDisplay(price, newScale);
            sb.append(key_currency).append(" ").append(priceString); 
        }
        key_currency = keys[1];//RMB
        if(PurchaseAmountMap.containsKey(key_currency)){
            sb.append(" ; ");
            int newScale = 2;
            BigDecimal price = PurchaseAmountMap.get(key_currency);
            String priceString = NumberUtils.getPriceDisplay(price, newScale);
            sb.append(key_currency).append(" ").append(priceString); 
        }        
        for (Map.Entry entry : PurchaseAmountMap.entrySet()) { 
            String key = (String)entry.getKey(); 
            if(key.equalsIgnoreCase(keys[0])){
                continue;
            }else if(key.equalsIgnoreCase(keys[1])){
                continue;
            }
            sb.append(" ; ");
            BigDecimal price = (BigDecimal)entry.getValue(); 
            int newScale = 2;
            String priceString = NumberUtils.getPriceDisplay(price, newScale);
            sb.append(key).append(" ").append(priceString);
        } 
        return sb.toString();
    }

}

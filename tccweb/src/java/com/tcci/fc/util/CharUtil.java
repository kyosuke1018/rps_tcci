/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gilbert.Lin
 */
public class CharUtil {
//嚴重: log4j:ERROR log4j called after unloading, see http://logging.apache.org/log4j/1.2/faq.html#unload.
//嚴重: java.lang.IllegalStateException: Class invariant violation
//    private static final Logger logger = LoggerFactory.getLogger(CharUtil.class);
    public static String  DELIMS_STRING =  " -/()\"";
    public static int REMOVED_TOKEN_LENGTH = 2;
    
    public static void main(String[] args) {
//        int removedTokenLength = 4;
//        String delimsString =  DELIMS_STRING;
        spiltString("O型圈160*5.7（耐油橡胶）", DELIMS_STRING, REMOVED_TOKEN_LENGTH);
        
        
//        String delims = getDelims(delimsString);
//        System.out.println("delims="+delims);
    }
    /**
     * 拆解邏輯集中於此method
     * @param condString 查詢條件
     * @param delimsString 拆解字元集 ex." -/()"
     * @param removedTokenLength 拆解字詞最小長度 ex. 4
     * @return 拆解查詢條件集合
     */
    public static List<String> spiltString(String condString, String delimsString, int removedTokenLength) {  
        //init
//        int removedTokenLength = 4;
        //[ ,-,/,(,)]
//        String delimsString =  " -/()";
        
        System.out.println("condString="+condString);
//        logger.debug("condString="+condString);
        //20160511:拿掉移除[雙引號,(,)]功能,不會與拆解token功能互相干擾
        //移除[雙引號,(,)]
//        condString = condString.replaceAll("[[\"]||[(]||[)]]+", "");
//        System.out.println("移除[\"()]="+condString);
        List<String> result = new ArrayList<>();
        //依空白或符號("-", "/")拆分為數個字詞(token)，依字詞當keyword進行比對
        //:delims取全形相加
        String delimsString_multi = convertToFullWidth(delimsString);
        delimsString = delimsString.concat(delimsString_multi);
//        String delims = "[[ ]||[-]||[/]||[(]||[)]]+";
        String delims = getDelims(delimsString);
        //20160511:no delims then return condString
        if("".equalsIgnoreCase(delims)){
            result.add(condString);
            return result;
        }
        List<String> condList1= spiltString(condString, delims);
//        logger.debug("condList1="+condList1);
        System.out.println("拆["+delimsString+"]="+condList1);
        //中文 與 英數字 拆分成不同的查詢條件
//        List<String> condList2 = new ArrayList<String>();
        for (int i = 0; i < condList1.size(); i++) {
            String value = condList1.get(i);
            if(org.apache.commons.lang.StringUtils.isBlank(value)){
                continue;
            }
            result.addAll(CharUtil.spiltChineseWords(value));
        }
        System.out.println("拆中文="+result);
//        a.排除→拆完後的Keyword字數最小長度(預設=4)
        Iterator<String> it = result.iterator();
        while( it.hasNext() ) {
          String token = it.next();
          //中文不依長度排除
          if(isChinese(token)){
              continue;
          }
          if(token.length() < removedTokenLength){
              it.remove();
          }
        }
//        logger.debug("result="+result);
        System.out.println("result="+result);
        return result;
    }
    /**
     * 半型轉全型
     * @param source
     * @return
     */
    public static String convertToFullWidth(final String source) {
            if (null == source) {
                    return null;
            }

            char[] charArray = source.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                    int ic = (int) charArray[i];

                    if (ic >= 33 && ic <= 126) {
                            charArray[i] = (char) (ic + 65248);
                    } else if (ic == 32) {
                            charArray[i] = (char) 12288;
                    }

            }
            return new String(charArray);
    }

    //<editor-fold defaultstate="collapsed" desc="private method">
    /**
     * call condString.split() and wrap to List 
     * @param condString
     * @param regx
     * @return 
     */
    private static List<String> spiltString(String condString, String regx) {  
        //init
        List<String> result = new ArrayList<>();
        
        String delims = regx;
        String[] tokens = condString.split(delims);
        List<String> result2= Arrays.asList(tokens);
//        logger.debug("result2="+result2);
        //
        
        Iterator<String> iterator = result2.iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            if (StringUtils.isNotBlank(token)) {
                result.add(token);
            }
        }
//        result.removeAll(Arrays.asList(null, ""));
//    Iterable<String> filterStrings = Iterables.filter(result, 
//            Predicates.());
//        logger.debug("result="+result);
        return result;
    }
    /**
     * transform " -/()"  to "[[ ]||[-]||[/]||[(]||[)]]+"
     * @param delims " -/()"
     * @return 
     */
    private static String getDelims(String delims){
        //String delims = "[[ ]||[-]||[/]||[(]||[)]]+";
        StringBuilder sb = new StringBuilder();
        //20160511:no delims then return condString
//        if(StringUtils.isBlank(delims)){
//            delims = DELIMS_STRING;
//        }
        char[] tokens = delims.toCharArray();
//        String[] tokens = delims.split(",");
        sb.append("[");
        for (int i = 0; i < tokens.length; i++) {
            char token = tokens[i];
            sb.append("[");
            sb.append(token);
            if(i == tokens.length-1){
                sb.append("]");
            }else{
                sb.append("]||");
            }
            
        }
        sb.append("]+");
        return sb.toString();
    }
    // 完整的判断中文汉字和符号
    private static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        //|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION)
                return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS;
    }
    
    private static List<String> spiltChineseWords(String str) {
        List<String> result = new ArrayList<>();
        int idx_begin = -1;
        int idx_end;
        boolean[] ischArray = new boolean[str.length()];
        
        if (isChinese(str)) {
            char[] ch = str.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                char c = ch[i];
                ischArray[i] = isChinese(c);
            }
            //            int pointer_1 = 0;
            boolean isChinese_begin = ischArray[0];
            int ischArray_length = ischArray.length;
            for (int i = 0; i < ischArray_length; i++) {
                if(1==ischArray_length){
                    result.add(str);
                    return result;
                }
                if(i==0){
                    idx_begin = 0;
                    isChinese_begin = ischArray[0];
                    continue;
                }
                
                idx_end = i;
                boolean isChinese_end = ischArray[i];
                
                
                if(isChinese_end == isChinese_begin){
                    
                }else{
//                    System.out.println("idx_begin="+idx_begin+"|idx_end="+idx_end);
                    String Str_a = str.substring(idx_begin, idx_end);
//                    System.out.println("aStr="+Str_a);
                    result.add(Str_a);
                    
                    idx_begin = i;
                    isChinese_begin = ischArray[i];
                }
                if(i+1==ischArray_length){
                    idx_end = ischArray_length;
//                    System.out.println("idx_begin="+idx_begin+"|idx_end="+idx_end);
                    String Str_a = str.substring(idx_begin, idx_end);
//                    System.out.println("aStr="+Str_a);
                    result.add(Str_a);
                }
                
                
            }
            
            
        }else{
            result.add(str);
        }
//        System.out.println("result="+result);
        return result;
    }
    //</editor-fold>
    
    
}

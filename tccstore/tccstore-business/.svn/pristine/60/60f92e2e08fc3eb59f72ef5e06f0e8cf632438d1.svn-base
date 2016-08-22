/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.rfc;

import com.tcci.sap.jco.model.RfcProxyInput;
import com.tcci.sap.jco.model.RfcProxyOutput;
import com.tcci.sap.jco.rest.RfcProxyClient;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public class RFCExec {

    private final static String CLIENT_CODE = "tccstore";
    private final static String SAP_CLIENT_CODE = "tcc_cn";
    private final static boolean DEBUG_MODE = false;

    public static List<Map<String, Object>> expCreditList(String jcoServiceUrl, String kunnr) {
        RfcProxyInput input = new InputBuilder(CLIENT_CODE, "ZSAP_JAVA_EXP_CREDIT_LIST", SAP_CLIENT_CODE)
                .inputTableEQ("ZFLD_IMP_KUNNR", kunnr)
                .outputTable("ZTAB_EXP_KNKKD")
                .debugMode(DEBUG_MODE)
                .build();

        RfcProxyClient client = new RfcProxyClient(jcoServiceUrl);
        RfcProxyOutput out = client.callRfc(input);
        client.close();

        return (null == out) ? null : out.getTables().get("ZTAB_EXP_KNKKD");
    }

    public static List<Map<String, Object>> crmQueryCredit(String jcoServiceUrl, String plant, String kunnr) {
        RfcProxyInput input = new InputBuilder(CLIENT_CODE, "Z_SD_CRM_QUERY_CREDIT", SAP_CLIENT_CODE)
                .inputValue("P_BUKRS", plant)
                .inputValue("P_KUNNR", kunnr)
                .outputTable("P_ZSSD_CREDITDATA")
                .debugMode(DEBUG_MODE)
                .build();

        RfcProxyClient client = new RfcProxyClient(jcoServiceUrl);
        RfcProxyOutput out = client.callRfc(input);
        client.close();

        return (null == out) ? null : out.getTables().get("P_ZSSD_CREDITDATA");
    }

    public static Map<String, List<Map<String, Object>>> expSdGetOrder(String jcoServiceUrl, List<String> vbelns) {
        if (null == vbelns || vbelns.isEmpty()) {
            return null;
        }

        InputBuilder builder = new InputBuilder(CLIENT_CODE, "ZSAP_JAVA_EXP_SD_GET", SAP_CLIENT_CODE)
                .inputTableEQ("ZFLD_IMP_VBTYP", "C")
                //.inputTableEQ("ZFLD_IMP_KUNNR", "13003")
                //.inputTableBT("ZFLD_IMP_AUDAT", "20150101", "20151231")
                //.outputTable("ZTAB_EXP_VBAK")
                .outputTable("ZTAB_EXP_VBUK")
                .debugMode(DEBUG_MODE);
        for (String vbeln : vbelns) {
            builder.inputTableEQ("ZFLD_IMP_VBELN", vbeln);
        }

        RfcProxyClient client = new RfcProxyClient(jcoServiceUrl);
        RfcProxyOutput out = client.callRfc(builder.build());
        client.close();

        return (null == out) ? null : out.getTables();
    }

    public static Map<String, List<Map<String, Object>>> getActiveContract(String jcoServiceUrl) {
        RfcProxyInput input = new InputBuilder(CLIENT_CODE, "ZSAP_JAVA_EXP_SD_GET", SAP_CLIENT_CODE)
                .inputTableEQ("ZFLD_IMP_VBTYP", "G") // 合約
                .inputTableNE("ZFLD_IMP_GBSTK", "C") // 'C' 完全處理(停用)
                .outputTable("ZTAB_EXP_VBAK") // 合約主檔
                .outputTable("ZTAB_EXP_VBAP") // 合約項次
                .outputTable("ZTAB_EXP_VBKD") // 銷售區, inco
                .outputTable("ZTAB_EXP_VBUP") // 項次是否完成
                .debugMode(DEBUG_MODE)
                .build();

        RfcProxyClient client = new RfcProxyClient(jcoServiceUrl);
        RfcProxyOutput out = client.callRfc(input);
        client.close();

        return (null == out) ? null : out.getTables();
    }
    
}

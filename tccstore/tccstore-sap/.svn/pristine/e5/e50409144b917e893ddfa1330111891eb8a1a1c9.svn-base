/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.sapproxy.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import com.tcci.tccstore.sapproxy.dto.SapTableDto;
import com.tcci.tccstore.sapproxy.dto.ZfldImpXxxxDto;
import com.tcci.tccstore.sapproxy.enums.SapProxyResponseEnum;
import com.tcci.tccstore.sapproxy.enums.SapSystemEnum;
import static com.tcci.tccstore.sapproxy.jco.SdProxy4JCo.logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jimmy.Lee
 */
public class CustomerCredit {
    // SAP客戶帳戶餘額(一個客戶可以有多個公司)
    // return:
    // KKBER: 公司代碼 (2000, 2500, ...)
    // OBLIG: 金額
    // WAERB: 幣別
    public static SapProxyResponseDto findCustomerCredits(JCoDestination destination, String code) throws Exception {
        SapTableDto result = null;
        try {
            String rfc = "ZSAP_JAVA_EXP_CREDIT_LIST";
            JCoFunction function = destination.getRepository().getFunction(rfc);
            if (function == null) {
                throw new RuntimeException(rfc + " not found in SAP.");
            }
            List<ZfldImpXxxxDto> inputTables = new ArrayList();
            inputTables.add(new ZfldImpXxxxDto("ZFLD_IMP_KUNNR", "I", "EQ", code, null));
            JCoParameterList tables = function.getTableParameterList(); //獲得Tables變數列表
            for (ZfldImpXxxxDto inputParam : inputTables) {
                JCoTable tValues = tables.getTable(inputParam.getTableName()); //Input Table
                tValues.appendRow();
                tValues.setValue("SCOPE", inputParam.getScope());
                tValues.setValue("OPERA", inputParam.getOpera());
                if (null != inputParam.getLow()) {
                    tValues.setValue("LOW", inputParam.getLow());
                }
                if (null != inputParam.getHigh()) {
                    tValues.setValue("HIGH", inputParam.getHigh());
                }
            }
            //Execute function
            function.execute(destination);
            JCoTable outputTable = function.getTableParameterList().getTable("ZTAB_EXP_KNKKD");
            int numRows = outputTable.getNumRows();
            logger.debug("output rows = " + numRows);
            outputTable.setRow(0);

            SapTableDto dto = new SapTableDto();
            dto.setDataMapList(JcoUtils.getJCoTableData(outputTable, null));
            result = dto;
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_CONNECTION,
                    null,
                    e.getMessage(),
                    null);
        }
    }

    // SAP客戶帳戶餘額
    // return:
    // SKFOR:
    // SSOBL:
    // SAUFT:
    // OBLIG:
    // SSOBL_IN:
    // NAME1:
    public static SapProxyResponseDto queryCREDIT(JCoDestination destination, String plant, String kunnr) throws Exception {
        SapTableDto result = null;
        try {
            String rfc = "Z_SD_CRM_QUERY_CREDIT";
            JCoFunction function = destination.getRepository().getFunction(rfc);
            if (function == null) {
                throw new RuntimeException(rfc + " not found in SAP.");
            }
            function.getImportParameterList().setValue("P_BUKRS", plant);
            function.getImportParameterList().setValue("P_KUNNR", kunnr);

            //Execute function
            try {
                function.execute(destination);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return new SapProxyResponseDto(
                        SapSystemEnum.JCO,
                        SapProxyResponseEnum.ERROR_LEVEL_APPLICATION,
                        null,
                        e.getMessage(),
                        null);
            }

            JCoTable outputTable = function.getTableParameterList().getTable("P_ZSSD_CREDITDATA");
            int numRows = outputTable.getNumRows();
            System.out.println("output P_ZSSD_CREDITDATA rows = " + numRows);
            if (numRows > 0) {
//                result = JCoUtils.getJCoTableDataAsMap(outputTable);
                SapTableDto dto = new SapTableDto();
                dto.setDataMapList(JcoUtils.getJCoTableData(outputTable, null));
                result = dto;

            }
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_CONNECTION,
                    null,
                    e.getMessage(),
                    null);
        }
    }

}

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
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class CustomerList {

    private static final Logger logger = LoggerFactory.getLogger(CustomerList.class);

    public static SapProxyResponseDto findCustomerList(JCoDestination destination, String low, String high) throws Exception {
        Map<String, SapTableDto> result = new HashMap<>();
        try {
            String rfc = "ZSAP_JAVA_EXP_CUSTOMER_LIST";
            JCoFunction function = destination.getRepository().getFunction(rfc);
            if (function == null) {
                throw new RuntimeException(rfc + " not found in SAP.");
            }
            JCoParameterList tables = function.getTableParameterList();
            // criteria
            appendImpParam(tables, new ZfldImpXxxxDto("ZFLD_IMP_KUNNR", "I", "BT", low, high));

            // execute function
            function.execute(destination);

            // get result
            getOutputDto(result, tables, "ZTAB_EXP_KNA1");
            getOutputDto(result, tables, "ZTAB_EXP_KNVV");

            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
            /*
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_APPLICATION,
                    null,
                    e.getMessage(),
                    null);
            */
        }

    }

    private static void appendImpParam(JCoParameterList tables, ZfldImpXxxxDto impParam) {
        JCoTable jcoTable = tables.getTable(impParam.getTableName()); //Input Table
        jcoTable.appendRow();
        jcoTable.setValue("SCOPE", impParam.getScope());
        jcoTable.setValue("OPERA", impParam.getOpera());
        if (null != impParam.getLow()) {
            jcoTable.setValue("LOW", impParam.getLow());
        }
        if (null != impParam.getHigh()) {
            jcoTable.setValue("HIGH", impParam.getHigh());
        }
    }

    private static void getOutputDto(Map<String, SapTableDto> result, JCoParameterList tables, String tableName) {
        JCoTable outTable = tables.getTable(tableName);
        int rows = outTable.getNumRows();
        logger.debug("{} output rows:{}", tableName, rows);
        outTable.setRow(0);
        SapTableDto dto = new SapTableDto();
        dto.setDataMapList(JcoUtils.getJCoTableData(outTable, null));
        result.put(tableName, dto);
    }

}

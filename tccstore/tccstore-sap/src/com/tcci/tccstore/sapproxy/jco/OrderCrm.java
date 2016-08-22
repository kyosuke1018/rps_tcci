package com.tcci.tccstore.sapproxy.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import com.tcci.tccstore.sapproxy.dto.SapTableDto;
import com.tcci.tccstore.sapproxy.enums.SapProxyResponseEnum;
import com.tcci.tccstore.sapproxy.enums.SapSystemEnum;
import static com.tcci.tccstore.sapproxy.jco.SdProxy4JCo.logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jimmy.Lee
 */
public class OrderCrm {
    
    // 查詢出貨單
    public static SapProxyResponseDto querySODETAIL(JCoDestination destination, String plant, String kunnr, String pdate) throws Exception {
        SapTableDto result = null;
        try {
            String rfc = "Z_SD_CRM_SODETAIL";
            JCoFunction function = destination.getRepository().getFunction(rfc);
            if (function == null) {
                throw new RuntimeException(rfc + " not found in SAP.");
            }
            function.getImportParameterList().setValue("P_BUKRS", plant);
            function.getImportParameterList().setValue("P_KUNNR", kunnr);
            function.getImportParameterList().setValue("P_DATE", pdate);
            try {
                function.execute(destination);
            } catch (Exception e) {
                System.out.print("Exception e >> " + e);
            }
            JCoTable outputTable = function.getTableParameterList().getTable("P_ZSSD_CRMSODETAIL");
            int numRows = outputTable.getNumRows();
            if (numRows > 0) {
                outputTable.setRow(0);
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

    // 查詢訂單
    public static SapProxyResponseDto queryZordercn(JCoDestination destination, String plant, String kunnr, String SHIPBDATE, String SHIPEDATE, String FLAG) throws Exception {
        SapTableDto result = null;
        try {
            String rfc = "Z_SD_CRM_SHIPDETAIL";
            JCoFunction function = destination.getRepository().getFunction(rfc);
            if (function == null) {
                throw new RuntimeException(rfc + " not found in SAP.");
            }
            function.getImportParameterList().setValue("P_BUKRS", plant);
            function.getImportParameterList().setValue("P_KUNNR", kunnr);
            function.getImportParameterList().setValue("SHIPBDATE", SHIPBDATE);
            function.getImportParameterList().setValue("SHIPEDATE", SHIPEDATE);
            function.getImportParameterList().setValue("P_FLAG", FLAG);

            //Execute function
            try {
                function.execute(destination);
            } catch (Exception e) {
                System.out.print("Exception e >> " + e);
            }
            JCoTable outputTable = function.getTableParameterList().getTable("P_ZSSD_CRMDETAIL");
            int numRows = outputTable.getNumRows();
            System.out.println("output P_ZSSD_CRMDETAIL rows = " + numRows);
            if (numRows > 0) {
                outputTable.setRow(0);
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

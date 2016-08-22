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
public class Contract {

    private static final Logger logger = LoggerFactory.getLogger(Contract.class);

    public static SapProxyResponseDto findActiveContract(JCoDestination destination) throws Exception {
        Map<String, SapTableDto> result = new HashMap<>();
        try {
            String rfc = "ZSAP_JAVA_EXP_SD_GET";
            JCoFunction function = destination.getRepository().getFunction(rfc);
            if (function == null) {
                throw new RuntimeException(rfc + " not found in SAP.");
            }
            JCoParameterList tables = function.getTableParameterList();
            
            // criteria
            appendImpParam(tables, new ZfldImpXxxxDto("ZFLD_IMP_VBTYP", "I", "EQ", "G", null)); // 合約
            appendImpParam(tables, new ZfldImpXxxxDto("ZFLD_IMP_GBSTK", "I", "NE", "C", null)); // 'C'無效
            
            // execute function
            function.execute(destination);
            
            // get result
            getOutputDto(result, tables, "ZTAB_EXP_VBAK");
            getOutputDto(result, tables, "ZTAB_EXP_VBAP");
            getOutputDto(result, tables, "ZTAB_EXP_VBKD");
            getOutputDto(result, tables, "ZTAB_EXP_VBUP");
            
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.SUCCESS,
                    result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            /*
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_APPLICATION,
                    null,
                    e.getMessage(),
                    null);
            */
            throw e;
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

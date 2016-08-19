package com.tcci.ecdemo.sapproxy.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import com.tcci.fc.util.sap.jco.JCoConnection;
import com.tcci.ecdemo.sapproxy.SdProxy;
import com.tcci.ecdemo.sapproxy.dto.SapProxyResponseDto;
import com.tcci.ecdemo.sapproxy.dto.SapTableDto;
import com.tcci.ecdemo.sapproxy.dto.ZfldImpXxxxDto;
import com.tcci.ecdemo.sapproxy.enums.SapProxyResponseEnum;
import com.tcci.ecdemo.sapproxy.enums.SapSystemEnum;
import java.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SD4JCO, JCO class for interfcae upload
 *
 * @author nEO.Fu
 */
public class SdProxy4JCo implements SdProxy {

    protected static final Logger logger = LoggerFactory.getLogger(SdProxy4JCo.class);
    /**
     * RFC功能名稱: ZSAP_JAVA_EXP_SD_GET (帶出銷售文件內容)
     */
    final static String RFC_ZSAP_JAVA_EXP_SD_GET = "ZSAP_JAVA_EXP_SD_GET";
    /**
     * JCO 執行成功之ReturnCode
     */
    final static String JCO_SUCCESS_RETURN_CODE = "";
    private Properties jcoProps = null;
    private JCoDestination destination = null;
    private JCoConnection connection;

    public SdProxy4JCo() {
    }

//    //constructor
//    public Sd4JcoProxy(Properties props) {
//        init(props);
//    }
    @Override
    public void init(Properties props) {
        connection = new JCoConnection();
        jcoProps = props;
        destination = connection.connect(jcoProps);
    }

    @Override
    public void dispose() {
        try {
            connection.closeConnection();
        } catch (Exception e) {
            logger.error("Disconnect SAP system failed!");
        }
    }

    @Override
    public SapProxyResponseDto findShippedSalesDocument(List<String> vbelnList) throws Exception {
        return findSalesDocument(vbelnList, true);
    }

    @Override
    public SapProxyResponseDto findSalesDocument(List<String> vbelnList, Boolean shipped) throws Exception {
        SapTableDto result = null;
        try {

            JCoFunction function = destination.getRepository().getFunction(RFC_ZSAP_JAVA_EXP_SD_GET);
            if (function == null) {
                throw new RuntimeException(RFC_ZSAP_JAVA_EXP_SD_GET + " not found in SAP.");
            }
            List<ZfldImpXxxxDto> inputTables = new ArrayList();
            for (String vbeln : vbelnList) {
                ZfldImpXxxxDto inputTable = new ZfldImpXxxxDto("ZFLD_IMP_VBELN", "I", "EQ", vbeln, null);
                inputTables.add(inputTable);
            }
            if (shipped != null) {
                if (shipped) {
                    ZfldImpXxxxDto inputTable = new ZfldImpXxxxDto("ZFLD_IMP_GBSTK", "I", "EQ", "C", null);
                    inputTables.add(inputTable);
                }
            }
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
            JCoTable outputTable = function.getTableParameterList().getTable("ZTAB_EXP_VBAK");
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

    /**
     * 處理回傳值，當TYPE不為S時，回傳錯誤。
     *
     * @param result
     * @return
     */
    private SapProxyResponseDto handleResult(Map<String, Object> result) {
        //若回傳的訊息類型欄位(TYPE)不等於'S'，表示執行失敗
        String rtnType = (String) result.get("TYPE");
        if (!StringUtils.equals(rtnType, "S")) {
            String errMsg = (String) result.get("MESSAGE");
            return new SapProxyResponseDto(
                    SapSystemEnum.JCO,
                    SapProxyResponseEnum.ERROR_LEVEL_APPLICATION,
                    rtnType,
                    errMsg,
                    result);
        }

        return new SapProxyResponseDto(
                SapSystemEnum.JCO,
                SapProxyResponseEnum.SUCCESS,
                result);
    }
}

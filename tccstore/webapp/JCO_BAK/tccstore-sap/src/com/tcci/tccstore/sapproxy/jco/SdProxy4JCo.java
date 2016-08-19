package com.tcci.tccstore.sapproxy.jco;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.tcci.fc.util.sap.jco.JCoConnection;
import com.tcci.tccstore.sapproxy.SdProxy;
import com.tcci.tccstore.sapproxy.dto.SapProxyResponseDto;
import java.util.*;
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
     * JCO 執行成功之ReturnCode
     */
    final static String JCO_SUCCESS_RETURN_CODE = "";
    private Properties jcoProps = null;
    private JCoDestination destination = null;
    private JCoConnection connection;

    public SdProxy4JCo() {
    }

    @Override
    public void init(Properties props) {
        connection = new JCoConnection();
        jcoProps = props;
        try {
            destination = connection.connect(jcoProps);
            destination.ping();
        } catch (JCoException ex) {
            String message = "SAP连线失败!";
            logger.error(message, ex);
            throw new RuntimeException(message);
        }
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
        return OrderTccstore.findSalesDocument(destination, vbelnList, true);
    }

    @Override
    public SapProxyResponseDto findSalesDocument(List<String> vbelnList, Boolean shipped) throws Exception {
        return OrderTccstore.findSalesDocument(destination, vbelnList, shipped);
    }

//    @Override
//    public SapProxyResponseDto findOrderPrice(Order order) throws Exception {
//        return OrderTccstore.findOrderPrice(destination, order);
//    }

    @Override
    public SapProxyResponseDto createOrder(Map<String, Object> order) throws Exception {
        return OrderTccstore.createOrder(destination, order);
    }

    @Override
    public SapProxyResponseDto cancelOrder(Map<String, Object> order) throws Exception {
        return OrderTccstore.cancelOrder(destination, order);
    }

    // SAP客戶帳戶餘額(一個客戶可以有多個公司)
    @Override
    public SapProxyResponseDto findCustomerCredits(String code) throws Exception {
        return CustomerCredit.findCustomerCredits(destination, code);
    }
    
    // SAP客戶帳戶餘額(crm)
    @Override
    public SapProxyResponseDto queryCREDIT(String plant, String kunnr) throws Exception {
        return CustomerCredit.queryCREDIT(destination, plant, kunnr);
    }    
    
    // 查詢出貨單(crm)
    @Override
    public SapProxyResponseDto querySODETAIL(String plant, String kunnr, String pdate) throws Exception{
        return OrderCrm.querySODETAIL(destination, plant, kunnr, pdate);
    }

    // 查詢訂單(crm)
    @Override
    public SapProxyResponseDto queryZordercn(String plant, String kunnr, String SHIPBDATE, String SHIPEDATE, String FLAG) throws Exception {
        return OrderCrm.queryZordercn(destination, plant, kunnr, SHIPBDATE, SHIPEDATE, FLAG);
    }

    // 取得有效合約
    @Override
    public SapProxyResponseDto findActiveContract() throws Exception {
        return Contract.findActiveContract(destination);
    }

    /*
    客戶主檔:10000 ~ 99999 (非5碼需人工排除)
    送達地點:S00000 ~ S99999 (非6碼需人工排除)
    */
    @Override
    public SapProxyResponseDto findCustomerList(String low, String high) throws Exception {
        return CustomerList.findCustomerList(destination, low, high);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

import com.tcci.tccstore.entity.EcOrder;
import com.tcci.tccstore.entity.datawarehouse.ZorderCn;
import com.tcci.tccstore.entity.datawarehouse.ZstdCreditdataVO;
import com.tcci.tccstore.entity.datawarehouse.ZstdSodetailVO;
import com.tcci.tccstore.entity.datawarehouse.ZstdSoinputVO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class ZstdFacade {

    private final static Logger logger = LoggerFactory.getLogger(ZstdFacade.class);

    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;

    private final static Map<String, String> sapErrorMapping;
    static {
        sapErrorMapping = new HashMap<>();
        sapErrorMapping.put("MV1423", "账号产生问题，请联络业务");
        sapErrorMapping.put("MV1391", "此产品不能在你选择的出货工厂出货，请联络业务");
        sapErrorMapping.put("MV1042", "订单正在变动中，请稍后查看");
        sapErrorMapping.put("MV1150", "帐上余额不足，请尽快打款");
    }

    // ----------------------------------------------
    // ST_ZTSD_SOINPUT/ST_ZTSD_CREATELOG: SAP開單/取消
    // ----------------------------------------------
    // 新增一筆建立訂單記錄
    public void soinputCreate(EcOrder ecOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String COLUMNS = "("
                + "SIGNI, " // 訂單序號 ecOrder.id
                + "ORDER_FLAG_2, " // 新增 "1" 
                + "IPC_ITEMNO, " // 手機下單 "M"
                + "EBELN, " // 合約號碼 ecOrder.contractCode
                + "EBELP, " // 合約項次 ecOrder.posnr
                + "EBELN_OLD, " // 車號 ecOrder.vehicle
                + "WERKS, " // 廠代碼 ecOrder.plantCode
                + "AUDAT, " // 開單日 ecOrder.createtime -> yyyyMMdd
                + "INCO1, " // 提貨方式代碼 ecOrder.method
                + "INCO2, " // 提貨方式說明(EXW:廠交自提, FCA:工地交自提)
                + "PERNR, " // 業務員工號 ecOrder.salesCode
                + "KMENGE, " // 訂單數量 ecOrder.quantity
                + "KUNAG, " // 客戶代碼 ecOrder.customerId.code
                + "KUNNR, " // 客戶代碼 ecOrder.customerId.code
                + "KUNNR_NAME1, " // 送達地點代碼 ecOrder.deliveryCode
                + "LZONE, " // 銷售區代碼 ecOrder.salesareaCode
                + "MATNR, " // 商品代碼 ecOrder.productCode
                + "VSART, " // 出貨類型 "01"
                + "SHIP_END" // 出貨日期 ecOrder.deliveryDate
                + ") ";
        final String VALUES = "VALUES ("
                + "#SIGNI, " // 訂單序號 ecOrder.id
                + "#ORDER_FLAG_2, " // 新增 "1" 
                + "#IPC_ITEMNO, " // 手機下單 "M"
                + "#EBELN, " // 合約號碼 ecOrder.contractCode
                + "#EBELP, " // 合約項次 ecOrder.posnr
                + "#EBELN_OLD, " // 車號 ecOrder.vehicle
                + "#WERKS, " // 廠代碼 ecOrder.plantCode
                + "#AUDAT, " // 開單日 ecOrder.createtime -> yyyyMMdd
                + "#INCO1, " // 提貨方式代碼 ecOrder.method
                + "#INCO2, " // 提貨方式說明(EXW:廠交自提, FCA:工地交自提)
                + "#PERNR, " // 業務員工號 ecOrder.salesCode
                + "#KMENGE, " // 訂單數量 ecOrder.quantity
                + "#KUNAG, " // 客戶代碼 ecOrder.customerId.code
                + "#KUNNR, " // 客戶代碼 ecOrder.customerId.code
                + "#KUNNR_NAME1, " // 送達地點代碼 ecOrder.deliveryCode
                + "#LZONE, " // 銷售區代碼 ecOrder.salesareaCode
                + "#MATNR, " // 商品代碼 ecOrder.productCode
                + "#VSART, " // 出貨類型 "01"
                + "#SHIP_END" // 出貨日期 ecOrder.deliveryDate
                + ")";
        String sql = "INSERT INTO ST_ZTSD_SOINPUT " + COLUMNS + VALUES;
        Query q = em.createNativeQuery(sql);
        q.setParameter("SIGNI", ecOrder.getId());
        q.setParameter("ORDER_FLAG_2", "1");
        q.setParameter("IPC_ITEMNO", "M");
        q.setParameter("EBELN", ecOrder.getContractCode());
        q.setParameter("EBELP", ecOrder.getPosnr());
        q.setParameter("EBELN_OLD", ecOrder.getVehicle());
        q.setParameter("WERKS", ecOrder.getPlantCode());
        q.setParameter("AUDAT", sdf.format(ecOrder.getCreatetime()));
        String inco1 = ecOrder.getMethod();
        String inco2 = "EXW".equalsIgnoreCase(inco1) ? "廠交自提"
                : "FCA".equalsIgnoreCase(inco1) ? "工地交自提" : inco1;
        q.setParameter("INCO1", inco1);
        q.setParameter("INCO2", inco2);
        q.setParameter("PERNR", ecOrder.getSalesCode());
        q.setParameter("KMENGE", ecOrder.getQuantity());
        q.setParameter("KUNAG", ecOrder.getCustomerId().getCode());
        q.setParameter("KUNNR", ecOrder.getCustomerId().getCode());
        q.setParameter("KUNNR_NAME1", ecOrder.getDeliveryCode());
        q.setParameter("LZONE", ecOrder.getSalesareaCode());
        q.setParameter("MATNR", ecOrder.getProductCode());
        q.setParameter("VSART", "01");
        q.setParameter("SHIP_END", ecOrder.getDeliveryDate());
        q.executeUpdate();
    }

    // 新增一筆取消訂單記錄
    public void soinputCancel(EcOrder ecOrder) {
        // 如果SAP已開單, 增加一筆待刪除record
        if (ecOrder.getSapOrdernum() != null) {
            final String COLUMNS = "("
                    + "SIGNI, " // 訂單序號 ecOrder.id
                    + "ORDER_FLAG_2, " // 刪除 "2" 
                    + "VBELV, " // SAP訂單號碼 ecOrder.sapOrdernum
                    + "WERKS" // 廠代碼 ecOrder.plantCode
                    + ") ";
            final String VALUES = "VALUES ("
                    + "#SIGNI, " // 訂單序號 ecOrder.id
                    + "#ORDER_FLAG_2, " // 刪除 "2" 
                    + "#VBELV, " // SAP訂單號碼 ecOrder.sapOrdernum
                    + "#WERKS" // 廠代碼 ecOrder.plantCode
                    + ") ";
            String sql = "INSERT INTO ST_ZTSD_SOINPUT " + COLUMNS + VALUES;
            Query q = em.createNativeQuery(sql);
            q.setParameter("SIGNI", ecOrder.getId());
            q.setParameter("ORDER_FLAG_2", "2");
            q.setParameter("VBELV", ecOrder.getSapOrdernum());
            q.setParameter("WERKS", ecOrder.getPlantCode());
            q.executeUpdate();
        } else {
            // SAP尚未開單, 更新該筆狀態
            String sql = "UPDATE ST_ZTSD_SOINPUT"
                    + " SET PROC_FLAG='E', EC_FLAG='C'"
                    + " WHERE SIGNI=#SIGNI"
                    + " AND ORDER_FLAG_2='1'"
                    + " AND PROC_FLAG IS NULL";
            Query q = em.createNativeQuery(sql);
            q.setParameter("SIGNI", ecOrder.getId());
            int count = q.executeUpdate();
            if (count != 1) {
                logger.warn("cancelOrder pending, order id:{}", ecOrder.getId());
            }
        }
    }

    // 找出SAP已完成的記錄
    public List<ZstdSoinputVO> soinputFindSapFinished() {
        List<ZstdSoinputVO> result = new ArrayList<>();
        String sql = "SELECT SIGNI, PROC_FLAG, ORDER_FLAG_2, VBELV"
                + " FROM ST_ZTSD_SOINPUT"
                + " WHERE (PROC_FLAG='C' OR PROC_FLAG='F')"
                + " AND EC_FLAG IS NULL";
        Query q = em.createNativeQuery(sql);
        List list = q.getResultList();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            Long signi = ((BigDecimal) columns[idx++]).longValue();
            String procFlag = (String) columns[idx++];
            String orderFlag2 = (String) columns[idx++];
            String vbelv = (String) columns[idx++];
            ZstdSoinputVO vo = new ZstdSoinputVO();
            vo.setSigni(signi);
            vo.setProcFlag(procFlag);
            vo.setOrderFlag2(orderFlag2);
            vo.setVbelv(vbelv);
            result.add(vo);
        }
        return result;
    }

    // 設定電商已完成
    public void soinputEcComplete(Long signi, String orderFlag2) {
        String sql = "UPDATE ST_ZTSD_SOINPUT"
                + " SET EC_FLAG='C'"
                + " WHERE SIGNI=#signi AND ORDER_FLAG_2=#orderFlag2";
        Query q = em.createNativeQuery(sql);
        q.setParameter("signi", signi);
        q.setParameter("orderFlag2", orderFlag2);
        q.executeUpdate();
    }

    // 找出新增/取消發生錯誤的log
    public String soinputFindError(Long signi) {
        String sql = "SELECT MESSAGE FROM ST_ZTSD_CREATELOG"
                + " WHERE SIGNI=#signi AND (MES_TYPE='E' OR MES_TYPE='F')"
                + " ORDER BY CREATETIME DESC";
        Query q = em.createNativeQuery(sql);
        q.setParameter("signi", signi);
        q.setMaxResults(1);
        List list = q.getResultList();
        String error = list.isEmpty() ? null : (String) list.get(0);
        if (error != null && error.length() > 5) {
            String mappingMessage = sapErrorMapping.get(error.substring(0, 6));
            if (mappingMessage != null) {
                error = mappingMessage;
            }
        }
        return error;
    }

    // ---------------------------
    // ST_ZTSD_CREDITDATA: 客戶餘額
    // ---------------------------
    public List<ZstdCreditdataVO> findCreditdata(String kunner) {
        List<ZstdCreditdataVO> result = new ArrayList<>();
        String sql = "SELECT BUKRS, SKFOR, SSOBL, SAUFT, OBLIG, SSOBL_IN, DATUM, UZEIT"
                + " FROM ST_ZTSD_CREDITDATA"
                + " WHERE KUNNR=#kunnr"
                + " ORDER BY BUKRS";
        Query q = em.createNativeQuery(sql);
        q.setParameter("kunnr", kunner);
        for (Object row : q.getResultList()) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            String bukrs = (String) columns[idx++];
            BigDecimal skfor = (BigDecimal) columns[idx++];
            BigDecimal ssobl = (BigDecimal) columns[idx++];
            BigDecimal sauft = (BigDecimal) columns[idx++];
            BigDecimal oblig = (BigDecimal) columns[idx++];
            BigDecimal ssoblIn = (BigDecimal) columns[idx++];
            String datum = (String) columns[idx++];
            String uzeit = (String) columns[idx++];
            ZstdCreditdataVO vo = new ZstdCreditdataVO();
            vo.setBukrs(bukrs);
            vo.setSkfor(skfor);
            vo.setSsobl(ssobl);
            vo.setSauft(sauft);
            vo.setOblig(oblig);
            vo.setSsoblIn(ssoblIn);
            vo.setDatum(datum);
            vo.setUzeit(uzeit);
            result.add(vo);
        }
        return result;
    }

    public ZstdCreditdataVO findCreditdata(String kunner, String bukrs) {
        String sql = "SELECT SKFOR, SSOBL, SAUFT, OBLIG, SSOBL_IN"
                + " FROM ST_ZTSD_CREDITDATA"
                + " WHERE KUNNR=#kunnr AND BUKRS=#bukrs";
        Query q = em.createNativeQuery(sql);
        q.setParameter("kunnr", kunner);
        q.setParameter("bukrs", bukrs);
        List list = q.getResultList();
        if (!list.isEmpty()) {
            Object[] columns = (Object[]) list.get(0);
            ZstdCreditdataVO vo = new ZstdCreditdataVO();
            int idx = 0;
            BigDecimal skfor = (BigDecimal) columns[idx++];
            BigDecimal ssobl = (BigDecimal) columns[idx++];
            BigDecimal sauft = (BigDecimal) columns[idx++];
            BigDecimal oblig = (BigDecimal) columns[idx++];
            BigDecimal ssoblIn = (BigDecimal) columns[idx++];
            vo.setSkfor(skfor);
            vo.setSsobl(ssobl);
            vo.setSauft(sauft);
            vo.setOblig(oblig);
            vo.setSsoblIn(ssoblIn);
            return vo;
        }
        return null;
    }

    // -------------------------------
    // ST_ZTSD_SHIPDETAIL: 當天出貨資料
    // -------------------------------
    public List<ZorderCn> findShipdetail(CrmFilter filter) {
        List<ZorderCn> result = new ArrayList<>();
        String sql = "SELECT KUNNR, KUNNR_TX, WERKS, WERKS_TX, AUBEL, ARKTX, INCO1_TX, BZIRK, BZTXT, XBLNR,"
                + " FKIMG, FKDAT, VGBEL, CHANGMAT2, KTEXT, UATBG, VSART, BEZEI, PERNR, ENAME,"
                + " Z5_KUNNR, Z4_DESC, Z5_DESC, Z4_KUNNR, AUDAT"
                + " FROM ST_ZTSD_SHIPDETAIL"
                + " WHERE 1=1";
        Map<String, Object> params = new HashMap<>();
        if (filter.getKunnr() != null) {
            sql += " AND KUNNR=#KUNNR";
            params.put("KUNNR", filter.getKunnr());
        }
        if (filter.getVkorg() != null) {
            sql += " AND VKORG=#VKORG";
            params.put("VKORG", filter.getVkorg());
        }
        if (filter.getWerks() != null) {
            sql += " AND WERKS=#WERKS";
            params.put("WERKS", filter.getWerks());
        }
        if (filter.getBzirk() != null) {
            sql += " AND BZIRK=#BZIRK";
            params.put("BZIRK", filter.getBzirk());
        }
        if (filter.getVsart() != null) {
            sql += " AND VSART=#VSART";
            params.put("VSART", filter.getVsart());
        }
        if (filter.getXblnr() != null) {
            sql += " AND XBLNR LIKE #XBLNR";
            params.put("XBLNR", "%" + filter.getXblnr() + "%");
        }
        if (filter.getInco1() != null) {
            sql += " AND INCO1 LIKE #INCO1";
            params.put("INCO1", "%" + filter.getInco1() + "%");
        }
        Query q = em.createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List list = q.getResultList();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            ZorderCn vo = new ZorderCn();
            vo.setKunnr((String) columns[idx++]);
            vo.setKunnrTx((String) columns[idx++]);
            vo.setWerks((String) columns[idx++]);
            vo.setWerksTx((String) columns[idx++]);
            vo.setAubel((String) columns[idx++]);
            vo.setArktx((String) columns[idx++]);
            vo.setInco1Tx((String) columns[idx++]);
            vo.setBzirk((String) columns[idx++]);
            vo.setBztxt((String) columns[idx++]);
            vo.setXblnr((String) columns[idx++]);

            vo.setFkimg((BigDecimal) columns[idx++]);
            vo.setFkdat((String) columns[idx++]);
            vo.setVgbel((String) columns[idx++]);
            vo.setChangmat2((String) columns[idx++]);
            vo.setKtext((String) columns[idx++]);
            vo.setUatbg((String) columns[idx++]);
            vo.setVsart((String) columns[idx++]);
            vo.setBezei((String) columns[idx++]);
            vo.setPernr((String) columns[idx++]);
            vo.setEname((String) columns[idx++]);

            vo.setZ5Kunnr((String) columns[idx++]);
            vo.setZ4Desc((String) columns[idx++]);
            vo.setZ5Desc((String) columns[idx++]);
            vo.setZ4Kunnr((String) columns[idx++]);
            vo.setAudat((String) columns[idx++]);
            result.add(vo);
        }
        return result;
    }
    
    // 找出已出貨的訂單(當天出貨資料)  
    public List<String> findInShipdetail(List<String> aubels) {
        return findExistOrder("ST_ZTSD_SHIPDETAIL", "AUBEL", aubels);
    }
    
    // 找出已出貨的訂單(歷史訂單)
    public List<String> findInZorderCn(List<String> vbelns) {
        return findExistOrder("ZORDER_CN", "AUBEL", vbelns);
    }
    
    private List<String> findExistOrder(String table, String column, List<String> list) {
        List<String> result = new ArrayList<>();
        if (list.isEmpty()) {
            return result;
        }
        boolean first = true;
        int count1 = 0;
        int count2 = 0;
        StringBuilder sb = new StringBuilder();
        for (String aubel : list) {
            if (first) {
                sb.append("SELECT ").append(column).append(" FROM ").append(table)
                        .append(" WHERE ").append(column).append(" IN (");
                first = false;
            } else {
                sb.append(',');
            }
            sb.append('\'').append(aubel).append('\'');
            count1++;
            count2++;
            if (count1==50 || count2==list.size()) { // 每50筆做一個query
                sb.append(')');
                logger.debug("findExistOrder sql:{}", sb.toString());
                Query q = em.createNativeQuery(sb.toString());
                result.addAll((List<String>) q.getResultList());
                sb.setLength(0);
                first = true;
                count1 = 0;
            }
        }
        return result;
    }
    
    // -----------------------------
    // ST_ZTSD_SODETAIL: 當天訂單資料
    // -----------------------------
    public List<ZstdSodetailVO> findSodetail(CrmFilter filter) {
        List<ZstdSodetailVO> result = new ArrayList<>();
        String sql = "SELECT KUNNR, KUNNR_TX, WERKS, WERKS_TX, BZTXT, ENAME, VBELN, ARKTX, INCO1_TX, KWMENG,"
                + " BEZEI, BSTKD, VGBEL, Z4_DESC, GBSTK"
                + " FROM ST_ZTSD_SODETAIL"
                + " WHERE 1=1";
        Map<String, Object> params = new HashMap<>();
        if (filter.getKunnr() != null) {
            sql += " AND KUNNR=#KUNNR";
            params.put("KUNNR", filter.getKunnr());
        }
        if (filter.getVkorg() != null) {
            sql += " AND VKORG=#VKORG";
            params.put("VKORG", filter.getVkorg());
        }
        if (filter.getWerks() != null) {
            sql += " AND WERKS=#WERKS";
            params.put("WERKS", filter.getWerks());
        }
        if (filter.getBzirk() != null) {
            sql += " AND BZIRK=#BZIRK";
            params.put("BZIRK", filter.getBzirk());
        }
        if (filter.getVsart() != null) {
            sql += " AND VSART=#VSART";
            params.put("VSART", filter.getVsart());
        }
        if (filter.getXblnr() != null) {
            sql += " AND BSTKD LIKE #BSTKD";
            params.put("BSTKD", "%" + filter.getXblnr() + "%");
        }
        if (filter.getInco1() != null) {
            sql += " AND INCO1 LIKE #INCO1";
            params.put("INCO1", "%" + filter.getInco1() + "%");
        }
        Query q = em.createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List list = q.getResultList();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            ZstdSodetailVO vo = new ZstdSodetailVO();
            vo.setKunnr((String) columns[idx++]);
            vo.setKunnrTx((String) columns[idx++]);
            vo.setWerks((String) columns[idx++]);
            vo.setWerksTx((String) columns[idx++]);
            vo.setBztxt((String) columns[idx++]);
            vo.setEname((String) columns[idx++]);
            vo.setVbeln((String) columns[idx++]);
            vo.setArktx((String) columns[idx++]);
            vo.setInco1Tx((String) columns[idx++]);
            vo.setKwmeng((BigDecimal) columns[idx++]);
            vo.setBezei((String) columns[idx++]);
            vo.setBstkd((String) columns[idx++]);
            vo.setVgbel((String) columns[idx++]);
            vo.setZ4Desc((String) columns[idx++]);
            vo.setGbstk((String) columns[idx++]);
            result.add(vo);
        }
        return result;
    }

}

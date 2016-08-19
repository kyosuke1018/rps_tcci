/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.crm;

import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.datawarehouse.ZmaraCn;
import com.tcci.tccstore.entity.datawarehouse.ZorderCn;
import com.tcci.tccstore.entity.datawarehouse.Zt001wCn;
import com.tcci.tccstore.entity.datawarehouse.Zt171Cn;
import com.tcci.tccstore.facade.datawarehouse.ZmaraCnFacade;
import com.tcci.tccstore.facade.datawarehouse.Zt001wCnFacade;
import com.tcci.tccstore.facade.datawarehouse.Zt171CnFacade;
import com.tcci.tccstore.vo.CrmCreditVo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.SelectEvent;
import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.entity.EcCompany;
import com.tcci.tccstore.entity.datawarehouse.ZstdCreditdataVO;
import com.tcci.tccstore.entity.datawarehouse.ZstdSodetailVO;
import com.tcci.tccstore.facade.company.EcCompanyFacade;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.datawarehouse.CrmFilter;
import com.tcci.tccstore.facade.datawarehouse.ZorderCnFacade;
import com.tcci.tccstore.facade.datawarehouse.ZstdFacade;
import java.util.Properties;
import javax.annotation.Resource;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;


/**
 *
 * @author carl.lin
 */
@ManagedBean(name = "zorderCnController")
@ViewScoped
public class ZorderCnController implements Serializable  {    

    private String requireCustomerMsg, requireDateMsg;
    private List<ZorderCn> items = new ArrayList<ZorderCn>();
    private ZorderCn current;
    private List<ZstdSodetailVO> itemsSo = new ArrayList<ZstdSodetailVO>();
    private List<CrmCreditVo> itemCredit =new ArrayList<CrmCreditVo>();
    private List<Zt001wCn> zt001wCnList = new ArrayList<Zt001wCn>();
    private List<Zt171Cn> zt171CnList = new ArrayList<Zt171Cn>();
    private List<ZmaraCn> zmaraCnList = new ArrayList<ZmaraCn>();
    private List<EcCustomer> customerList = new ArrayList<EcCustomer>();
    private List<EcCompany> companys;
    
    @EJB
    private ZmaraCnFacade zmaraCnListFacde;
    @EJB
    private Zt001wCnFacade zt001wCnFacade;
    @EJB
    private Zt171CnFacade zt171CnwFacade;    
    @EJB
    private ZorderCnFacade zorderCnFacade;
    @EJB
    private EcCustomerFacade customerFacade;
    @EJB
    private EcCompanyFacade companyFacade;
    @EJB
    private ZstdFacade zstdFacade;
            
    // managed bean
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    private String code, name,selectedCustomer;
    private Date dateStr, dateStr1;
    private String matnr, werks, plantId, vkorg, bzirk, xblnr, vgbel, inco1, custNAME, bezei;
    private BigDecimal sum_fkimg, sum_kwmeng, OBLIG, SSOBL, SAUFT, SKFOR, SSOBL_IN;
    private String salePerson;

    private CrmFilter crmFilter = new CrmFilter();
    
    @Resource(mappedName = "jndi/global.config")
    transient private Properties globalConfig;
    
    @PostConstruct
    public void init() {
        companys = companyFacade.findNoHideCredit();
        zt001wCnList = zt001wCnFacade.getAllZt001wCn();
        zt171CnList = zt171CnwFacade.getAllZt171Cn();
        zmaraCnList = zmaraCnListFacde.getAllZmaraCn();
        // customerList =  customerFacade.findByMember(userSession.getEcMember());
        customerList = userSession.getCustomerList();
        
        if (null == dateStr) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            dateStr = cal.getTime();
        }

        if (null == dateStr1) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            dateStr1 = cal.getTime();
        }
        if (sum_fkimg == null) {
            sum_fkimg = new BigDecimal(0D);
        }

        if (sum_kwmeng == null) {
            sum_kwmeng = new BigDecimal(0D);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }
    
    // getter &  setter

    public List<CrmCreditVo> getItemCredit() {
        return itemCredit;
    }

    public void setItemCredit(List<CrmCreditVo> itemCredit) {
        this.itemCredit = itemCredit;
    }

    
    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
    
    public List<EcCustomer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<EcCustomer> customerList) {
        this.customerList = customerList;
    }

    public List<ZmaraCn> getZmaraCnList() {
        return zmaraCnList;
    }

    public void setZmaraCnList(List<ZmaraCn> zmaraCnList) {
        this.zmaraCnList = zmaraCnList;
    }

    public List<Zt171Cn> getZt171CnList() {
        return zt171CnList;
    }

    public void setZt171CnList(List<Zt171Cn> zt171CnList) {
        this.zt171CnList = zt171CnList;
    }
    
    public List<Zt001wCn> getZt001wCnList() {
        return zt001wCnList;
    }

    public void setZt001wCnList(List<Zt001wCn> zt001wCnList) {
        this.zt001wCnList = zt001wCnList;
    }
    
    public String getSalePerson() {
        return salePerson;
    }

    public void setSalePerson(String salePerson) {
        this.salePerson = salePerson;
    }

    public String getBezei() {
        return bezei;
    }

    public void setBezei(String bezei) {
        this.bezei = bezei;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ZstdSodetailVO> getItemsSo() {
        return itemsSo;
    }

    public void setItemsSo(List<ZstdSodetailVO> itemsSo) {
        this.itemsSo = itemsSo;
    }

    public BigDecimal getOBLIG() {
        return OBLIG;
    }

    public void setOBLIG(BigDecimal OBLIG) {
        this.OBLIG = OBLIG;
    }

    public BigDecimal getSSOBL_IN() {
        return SSOBL_IN;
    }

    public void setSSOBL_IN(BigDecimal SSOBL_IN) {
        this.SSOBL_IN = SSOBL_IN;
    }

    public BigDecimal getSSOBL() {
        return SSOBL;
    }

    public void setSSOBL(BigDecimal SSOBL) {
        this.SSOBL = SSOBL;
    }

    public BigDecimal getSAUFT() {
        return SAUFT;
    }

    public void setSAUFT(BigDecimal SAUFT) {
        this.SAUFT = SAUFT;
    }

    public BigDecimal getSKFOR() {
        return SKFOR;
    }

    public void setSKFOR(BigDecimal SKFOR) {
        this.SKFOR = SKFOR;
    }

    public String getXblnr() {
        return xblnr;
    }

    public void setXblnr(String xblnr) {
        this.xblnr = xblnr;
    }

    public String getVgbel() {
        return vgbel;
    }

    public void setVgbel(String vgbel) {
        this.vgbel = vgbel;
    }

    public String getInco1() {
        return inco1;
    }

    public void setInco1(String inco1) {
        this.inco1 = inco1;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getBzirk() {
        return bzirk;
    }

    public void setBzirk(String bzirk) {
        this.bzirk = bzirk;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public BigDecimal getSum_fkimg() {
        return sum_fkimg;
    }

    public void setSum_fkimg(BigDecimal sum_fkimg) {
        this.sum_fkimg = sum_fkimg;
    }

    public List<ZorderCn> getItems() {
        return items;
    }

    public void setItems(List<ZorderCn> items) {
        this.items = items;
    }

    public String getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(String selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public List<EcCompany> getCompanys() {
        return companys;
    }

    public void setCompanys(List<EcCompany> companys) {
        this.companys = companys;
    }

   public List<String> completeMethod(String input) {
        List<String> result = new ArrayList<String>();
        HashMap<EcCustomer, EcCustomer> uniqueUsers = new HashMap<EcCustomer, EcCustomer>();
        List<EcCustomer> customersByCode = customerFacade.findByCriteria(input, "");
        for (EcCustomer customer1 : customersByCode) {
            uniqueUsers.put(customer1, customer1);
        }

        List<EcCustomer> customersByName = customerFacade.findByCriteria("", input);
        for (EcCustomer customer2 : customersByName) {
            uniqueUsers.put(customer2, customer2);
        }
        for (EcCustomer uniqueUser : uniqueUsers.values()) {
            result.add(uniqueUser.getCode());
        }
        return result;
    }    
   
    public void handleSelect(SelectEvent event) {
        String code = (String) event.getObject();
        setName(customerFacade.findByCriteria(code, "").get(0).getName());
        setSelectedCustomer(code);
    }
    
   
     public String getCustomerName(String code) {
        if (null != code) {

            try {
                EcCustomer customer = customerFacade.getCustomersByCode(code);
                if (null == customer.getName()) {
                    name = "客戶代號不存在!!!";
                } else if (customer.getName().isEmpty()) {
                    name = "客戶代號不存在!!!";
                } else {
                    name = customer.getName();
                }
            } catch (Exception e) {
                name = "客戶代號不存在!!!";
            }

        }
        return name;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public Date getDateStr() {
        return dateStr;
    }

    public void setDateStr(Date dateStr) {
        this.dateStr = dateStr;
    }

    public Date getDateStr1() {
        return dateStr1;
    }

    public void setDateStr1(Date dateStr1) {
        this.dateStr1 = dateStr1;
    }

    public String getRequireCustomerMsg() {
        return "客戶代號不得為空白，請輸入客戶代號";
    }

    public void setRequireCustomerMsg(String requireCustomerMsg) {
        this.requireCustomerMsg = requireCustomerMsg;
    }

    public String getRequireDateMsg() {
        return "日期欄位不得為空白";
    }

    public void setRequireDateMsg(String requireDateMsg) {
        this.requireDateMsg = requireDateMsg;
    }

    public BigDecimal getSum_kwmeng() {
        return sum_kwmeng;
    }

    public void setSum_kwmeng(BigDecimal sum_kwmeng) {
        this.sum_kwmeng = sum_kwmeng;
    }

    public ZorderCnController() {
    }

    public ZorderCn getSelected() {
        if (current == null) {
            current = new ZorderCn();
        }
        return current;
    }

    public void postProcessXLSZorderCn(Object document) {
        postProcessXLS(document, 10);
    }
    
    public void postProcessXLSZorderCnSo(Object document) {
        postProcessXLS(document, 8);
    }
    
    private void postProcessXLS(Object document, int numericColumn) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFCellStyle decStyle = wb.createCellStyle();
        decStyle.setDataFormat((short)2);
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            HSSFRow row = (HSSFRow) rows.next();
            if (row.getRowNum() == 0) { // header
                continue;
            }
            HSSFCell cell = row.getCell(numericColumn); // 出货吨数, 订单数量
            String strVal = cell.getStringCellValue();
            if (strVal != null) {
                strVal = strVal.replaceAll(",", "");
            }
            cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellStyle(decStyle);
            try {
                cell.setCellValue(Double.valueOf(strVal));
            } catch (Exception ex) {
            }
        }
    }
    
    public String prepareList() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -90);
        //查詢起日不可小於90天    
//        if (dateStr.before(cal.getTime())) {
//            FacesContext context = FacesContext.getCurrentInstance();
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    ResourceBundle.getBundle("/msgApp").getString("app.error.Message_beginDateCheck"), ""));
//            return null;
//        }
        current = new ZorderCn();
        current.setKunnr(code);
        current.setWerks(werks);
        current.setMatnr(matnr);
        current.setVkorg(plantId);
        current.setBzirk(bzirk);
        current.setXblnr(xblnr);
        current.setVgbel(vgbel);
        current.setInco1(inco1);
        if (bezei != null) {
            current.setVsart(bezei);
        }
        if (salePerson != null) {
            current.setPernr(salePerson);
        }

        List<ZorderCn> tmpItems = zorderCnFacade.findAll(current, dateStr, dateStr1);
        sum_fkimg = BigDecimal.ZERO;
        items.clear();

        for (ZorderCn tmpOrderCn : tmpItems) {
            String tmpAudat = tmpOrderCn.getAudat().substring(0, 4) + "/" + tmpOrderCn.getAudat().substring(4, 6) + "/" + tmpOrderCn.getAudat().substring(6, 8);
            tmpOrderCn.setAudat(tmpAudat);
            String tmpFkdat = tmpOrderCn.getFkdat().substring(0, 4) + "/" + tmpOrderCn.getFkdat().substring(4, 6) + "/" + tmpOrderCn.getFkdat().substring(6, 8);
            tmpOrderCn.setFkdat(tmpFkdat);
            if (!tmpOrderCn.getUatbg().isEmpty()) {
                String tmpString1 = tmpOrderCn.getUatbg().substring(0, 2) + ":" + tmpOrderCn.getUatbg().substring(2, 4) + ":" + tmpOrderCn.getUatbg().substring(4, 6);
                tmpOrderCn.setUatbg(tmpString1);
            }
            if (bezei != null && !bezei.equals("ALL") && !tmpOrderCn.getVsart().equals(bezei)) {
                continue;
            }
            if (salePerson != null && !salePerson.equals("ALL") && !tmpOrderCn.getPernr().equals(salePerson)) {
                continue;
            }
            sum_fkimg = tmpOrderCn.getFkimg().add(sum_fkimg);
            items.add(tmpOrderCn);
        }
        return null;
    }

    public void prepareSapRfcCall() {
        try {
            crmFilter.setKunnr(code);
            crmFilter.setVkorg(plantId);
            crmFilter.setWerks("ALL".equals(werks) ? null : werks);
            crmFilter.setBzirk("ALL".equals(bzirk) ? null : bzirk);
            crmFilter.setXblnr(StringUtils.trimToNull(xblnr));
            crmFilter.setVsart("ALL".equals(bezei) ? null : bezei);
            crmFilter.setInco1(StringUtils.trimToNull(inco1));
            getCustomerCredits();
            getZordercn();
            getSODetail();
        } catch (Exception ex) {
            System.out.println(ex);
            JsfUtil.addErrorMessage(ex, "sap exception");
        }
    }

    public void getCustomerCredits() throws Exception {
        SKFOR = BigDecimal.ZERO;
        SSOBL = BigDecimal.ZERO;
        SAUFT = BigDecimal.ZERO;
        OBLIG = BigDecimal.ZERO;
        SSOBL_IN = BigDecimal.ZERO;
        custNAME = "";
        if (code != null && plantId !=null ) {
            /*
            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
            SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
            SapProxyResponseDto result = sdProxy.queryCREDIT(plantId ,code);
            SapTableDto sapTableDto = (SapTableDto) result.getResult();
            if (sapTableDto != null && sapTableDto.getDataMapList().size() > 0) {
                Map<String, Object> dataMap = sapTableDto.getDataMapList().get(0);
                SKFOR = (BigDecimal) dataMap.get("SKFOR");
                SSOBL = (BigDecimal) dataMap.get("SSOBL");
                SAUFT = (BigDecimal) dataMap.get("SAUFT");
                OBLIG = (BigDecimal) dataMap.get("OBLIG");
                SSOBL_IN = (BigDecimal) dataMap.get("SSOBL_IN");
                custNAME = (String) dataMap.get("NAME1");
            }
            sdProxy.dispose();
            */

            ZstdCreditdataVO vo = zstdFacade.findCreditdata(code, plantId);
            if (vo != null) {
                SKFOR = vo.getSkfor();
                SSOBL = vo.getSsobl();
                SAUFT = vo.getSauft();
                OBLIG = vo.getOblig();
                SSOBL_IN = vo.getSsoblIn();
            }

            /*
            String jcoServiceUrl = globalConfig.getProperty("SAP_REST_ROOT");
            if (null == jcoServiceUrl) {
                throw new AbortProcessingException("系統設定有誤!");
            }
            List<Map<String, Object>> result = RFCExec.crmQueryCredit(jcoServiceUrl, plantId, code);
            if (result != null && !result.isEmpty()) {
                Map<String, Object> dataMap = result.get(0);
                SKFOR = new BigDecimal(dataMap.get("SKFOR").toString());
                SSOBL = new BigDecimal(dataMap.get("SSOBL").toString());
                SAUFT = new BigDecimal(dataMap.get("SAUFT").toString());
                OBLIG = new BigDecimal(dataMap.get("OBLIG").toString());
                SSOBL_IN = new BigDecimal(dataMap.get("SSOBL_IN").toString());
                custNAME = (String) dataMap.get("NAME1");
            }
            */
        }
    }

    /*
    public void getZordercn() throws Exception {
        // call queryZordercn(String plant, String kunnr, String SHIPBDATE, String SHIPEDATE)
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf1 = new SimpleDateFormat("HHmmss");
        sum_fkimg = BigDecimal.ZERO;
        items.clear();
        Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
        SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
        //及時查當天資料
        String datetmp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // test data
        //datetmp = "20141010";
        SapProxyResponseDto result = sdProxy.queryZordercn(plantId, code, datetmp, datetmp, "W");
        SapTableDto sapTableDto = (SapTableDto) result.getResult();
        if (sapTableDto != null && sapTableDto.getDataMapList().size() > 0) {
            List<Map<String, Object>> list = sapTableDto.getDataMapList();
            for (int i = 0; i < list.size(); i++) {
                ZorderCn tmpzorder = new ZorderCn();
                tmpzorder.setKunnr((String) list.get(i).get("KUNNR"));
                tmpzorder.setKunnrTx((String) list.get(i).get("NAME1"));
                tmpzorder.setWerks((String) list.get(i).get("WERKS"));
                tmpzorder.setWerksTx((String) list.get(i).get("WERKS_TX"));
                tmpzorder.setAubel((String) list.get(i).get("AUBEL"));
                tmpzorder.setArktx((String) list.get(i).get("ARKTX"));
                tmpzorder.setInco1Tx((String) list.get(i).get("INCO1_TX"));
                tmpzorder.setBzirk((String) list.get(i).get("BZIRK"));
                tmpzorder.setBztxt((String) list.get(i).get("BZTXT"));
                tmpzorder.setXblnr((String) list.get(i).get("XBLNR"));
                tmpzorder.setFkimg((BigDecimal) list.get(i).get("FKIMG"));
                tmpzorder.setFkdat(sf.format(list.get(i).get("FKDAT")));
                tmpzorder.setVgbel((String) list.get(i).get("VGBEL"));
                tmpzorder.setChangmat2((String) list.get(i).get("CHANGMAT2"));
                tmpzorder.setKtext((String) list.get(i).get("KTEXT"));
                tmpzorder.setUatbg(sf1.format(list.get(i).get("UATBG")));
                tmpzorder.setVsart((String) list.get(i).get("VSART"));
                tmpzorder.setBezei((String) list.get(i).get("BEZEI"));
                tmpzorder.setPernr((String) list.get(i).get("PERNR"));
                tmpzorder.setEname((String) list.get(i).get("ENAME"));
                tmpzorder.setZ5Kunnr((String) list.get(i).get("Z5_KUNNR"));
                tmpzorder.setZ4Desc((String) list.get(i).get("Z4_DESC"));
                tmpzorder.setZ5Desc((String) list.get(i).get("Z5_DESC"));
                tmpzorder.setZ4Kunnr((String) list.get(i).get("Z4_KUNNR"));

                tmpzorder.setFkdat(tmpzorder.getFkdat().substring(0, 4) + "/" + tmpzorder.getFkdat().substring(4, 6) + "/" + tmpzorder.getFkdat().substring(6, 8));
                if (!tmpzorder.getUatbg().isEmpty()) {
                    tmpzorder.setUatbg(tmpzorder.getUatbg().substring(0, 2) + ":" + tmpzorder.getUatbg().substring(2, 4) + ":" + tmpzorder.getUatbg().substring(4, 6));
                }

                // 廠別 fillter
                if ((werks != null) && (!tmpzorder.getWerks().equals(werks) && !werks.equals("ALL") && !werks.isEmpty())) {
                    continue;
                }

                // 銷售地區 fillter
                if ((bzirk != null) && (!tmpzorder.getBzirk().equals(bzirk) && !bzirk.equals("ALL") && !bzirk.isEmpty())) {
                    continue;
                }
                // 車牌號碼 fillter 
                if ((xblnr != null) && (tmpzorder.getXblnr().indexOf(xblnr) < 0 && !xblnr.isEmpty())) {
                    continue;
                }
                // 運輸方式 fillter
                if (bezei != null) {
                    if ((tmpzorder.getVsart() != null
                            && (tmpzorder.getVsart().indexOf(bezei) < 0
                            && !bezei.equals("ALL"))
                            || (tmpzorder.getVsart()) == null)) {
                        continue;
                    }
                }
                //國貿條件 fillter
                if ((inco1 != null) && tmpzorder.getInco1() != null
                        && (tmpzorder.getInco1().indexOf(inco1) < 0 && !inco1.isEmpty())) {
                    continue;
                }
                //業務人員
                if (salePerson != null) {
                    if ((tmpzorder.getPernr() != null
                            && !tmpzorder.getPernr().equals(salePerson)
                            && !salePerson.equals("ALL"))
                            || (tmpzorder.getPernr() == null)) {
                        continue;
                    }
                }

                if (tmpzorder.getFkimg() != null) {
                    sum_fkimg = sum_fkimg.add(tmpzorder.getFkimg());
                }

                items.add(tmpzorder);
            }
        }
        sdProxy.dispose();
    }
    */
    public void getZordercn() throws Exception {
        sum_fkimg = BigDecimal.ZERO;
        items.clear();
        List<ZorderCn> list = zstdFacade.findShipdetail(crmFilter);
        for (ZorderCn tmpzorder : list) {
            tmpzorder.setFkdat(formatDate(tmpzorder.getFkdat()));
            tmpzorder.setUatbg(formatTime(tmpzorder.getUatbg()));
            tmpzorder.setAudat(formatDate(tmpzorder.getAudat()));
            if (tmpzorder.getFkimg() != null) {
                sum_fkimg = sum_fkimg.add(tmpzorder.getFkimg());
            }
            items.add(tmpzorder);
        }
    }

    /*
    public void getSODetail() throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, "tcc_cn"); //取得相關Jco連結參數
        SdProxy sdProxy = SdProxyFactory.createProxy(jcoProp);//建立連線
        //及時查當天資料
        String pdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // for test
        // pdate = "20150131";
        SapProxyResponseDto result = sdProxy.querySODETAIL(plantId, code ,pdate);//call RFC
        SapTableDto sapTableDto = (SapTableDto) result.getResult();
        itemsSo.clear();
        sum_kwmeng = BigDecimal.ZERO;
        if (sapTableDto != null && sapTableDto.getDataMapList().size() > 0) {
            List<Map<String, Object>> listSo = sapTableDto.getDataMapList();
            for (int i = 0; i < listSo.size(); i++) {
                CrmSODetailVo tmpSodVo = new CrmSODetailVo();

                tmpSodVo.setVkorg((String) listSo.get(i).get("VKORG"));
                tmpSodVo.setVkorgTx((String) listSo.get(i).get("VKORG_TX"));
                tmpSodVo.setVtweg((String) listSo.get(i).get("VTWEG"));
                tmpSodVo.setVtwegTx((String) listSo.get(i).get("VTWEG_TX"));
                tmpSodVo.setSpart((String) listSo.get(i).get("SPART"));
                tmpSodVo.setSpartTx((String) listSo.get(i).get("SPART_TX"));
                tmpSodVo.setKunnr((String) listSo.get(i).get("KUNNR"));
                tmpSodVo.setKunnrTx((String) listSo.get(i).get("NAME1"));
                tmpSodVo.setEname((String) listSo.get(i).get("ENAME"));
                tmpSodVo.setAudat(sf.format(listSo.get(i).get("AUDAT")));
                tmpSodVo.setAudat(tmpSodVo.getAudat().substring(0, 4) + "/" + tmpSodVo.getAudat().substring(4, 6) + "/" + tmpSodVo.getAudat().substring(6, 8));
                tmpSodVo.setVbeln((String) listSo.get(i).get("VBELN"));
                tmpSodVo.setMatnr((String) listSo.get(i).get("MATNR"));
                tmpSodVo.setArktx((String) listSo.get(i).get("ARKTX"));
                tmpSodVo.setKwmeng((BigDecimal) listSo.get(i).get("KWMENG"));
                tmpSodVo.setZieme((String) listSo.get(i).get("ZIEME"));
                tmpSodVo.setCmpre((BigDecimal) listSo.get(i).get("CMPRE"));
                tmpSodVo.setUmziz((BigDecimal) listSo.get(i).get("UMZIZ"));
                tmpSodVo.setWaerk((String) listSo.get(i).get("WAERK"));
                tmpSodVo.setWerks((String) listSo.get(i).get("WERKS"));
                tmpSodVo.setWerksTx((String) listSo.get(i).get("WERKS_TX"));
                tmpSodVo.setInco1((String) listSo.get(i).get("INCO1"));
                tmpSodVo.setInco1Tx((String) listSo.get(i).get("INCO1_TX"));
                tmpSodVo.setBzirk((String) listSo.get(i).get("BZIRK"));
                tmpSodVo.setBztxt((String) listSo.get(i).get("BZTXT"));
                tmpSodVo.setKdgrp((String) listSo.get(i).get("KDGRP"));
                tmpSodVo.setKtext((String) listSo.get(i).get("KTEXT"));
                if (listSo.get(i).get("ERDAT") != null) {
                    tmpSodVo.setErdat(sf.format(listSo.get(i).get("ERDAT")));
                }
                if (listSo.get(i).get("AEDAT") != null) {
                    tmpSodVo.setAedat(sf.format(listSo.get(i).get("AEDAT")));
                }
                tmpSodVo.setVsart((String) listSo.get(i).get("VSART"));
                tmpSodVo.setBezei((String) listSo.get(i).get("BEZEI"));
                tmpSodVo.setPernr((String) listSo.get(i).get("PERNR"));
                tmpSodVo.setGbstk((String) listSo.get(i).get("GBSTK"));
                tmpSodVo.setBstkd((String) listSo.get(i).get("BSTKD"));
                tmpSodVo.setVgbel((String) listSo.get(i).get("VGBEL"));
                tmpSodVo.setZ5Kunnr((String) listSo.get(i).get("Z5_KUNNR"));
                tmpSodVo.setZ4Desc((String) listSo.get(i).get("Z4_DESC"));
                tmpSodVo.setZ5Desc((String) listSo.get(i).get("Z5_DESC"));
                tmpSodVo.setZ4Kunnr((String) listSo.get(i).get("Z4_KUNNR"));

                // 廠別 fillter
                if ((werks != null) && (!tmpSodVo.getWerks().equals(werks) && !werks.equals("ALL") && !werks.isEmpty())) {
                    continue;
                }
                // 銷售地區 fillter
                if ((bzirk != null) && (!tmpSodVo.getBzirk().equals(bzirk) && !bzirk.equals("ALL") && !bzirk.isEmpty())) {
                    continue;
                }
                // 車牌號碼 fillter 
                if ((xblnr != null) && (tmpSodVo.getBstkd().indexOf(xblnr) < 0 && !xblnr.isEmpty())) {
                    continue;
                }
                // 運輸方式 fillter
                if (bezei != null) {
                    if ((tmpSodVo.getVsart() != null
                            && (tmpSodVo.getVsart().indexOf(bezei) < 0
                            && !bezei.equals("ALL"))
                            || (tmpSodVo.getVsart()) == null)) {
                        continue;
                    }
                }
                //國貿條件 fillter
                if ((inco1 != null) && (tmpSodVo.getInco1().indexOf(inco1) < 0 && !inco1.isEmpty())) {
                    continue;
                }

                //業務人員
                if (salePerson != null) {
                    if ((tmpSodVo.getPernr() != null
                            && !tmpSodVo.getPernr().equals(salePerson)
                            && !salePerson.equals("ALL"))
                            || (tmpSodVo.getPernr() == null)) {
                        continue;
                    }
                }

                if (tmpSodVo.getKwmeng() != null) {
                    sum_kwmeng = sum_kwmeng.add(tmpSodVo.getKwmeng());
                }
                itemsSo.add(tmpSodVo);
            }
        }
        sdProxy.dispose();
    }
    */

    public void getSODetail() throws Exception {
        itemsSo.clear();
        sum_kwmeng = BigDecimal.ZERO;
        List<ZstdSodetailVO> list = zstdFacade.findSodetail(crmFilter);
        for (ZstdSodetailVO tmpSodVo : list) {
            tmpSodVo.setAudat(formatDate(tmpSodVo.getAudat()));
            tmpSodVo.setErdat(formatDate(tmpSodVo.getErdat()));
            tmpSodVo.setAedat(formatDate(tmpSodVo.getAedat()));
            if (tmpSodVo.getKwmeng() != null) {
                sum_kwmeng = sum_kwmeng.add(tmpSodVo.getKwmeng());
            }
            itemsSo.add(tmpSodVo);
        }
    }
    
    // yyyyMMdd -> yyyy/MM/dd
    private static String formatDate(String ymd) {
        if (ymd != null && ymd.length()==8) {
            return ymd.substring(0, 4) + "/" + ymd.substring(4, 6) + "/" + ymd.substring(6, 8);
        } else {
            return ymd;
        }
    }

    // HHmmss -> HH:mm:ss
    private static String formatTime(String hms) {
        if (hms != null && hms.length()==6) {
            return hms.substring(0, 2) + ":" + hms.substring(2, 4) + ":" + hms.substring(4, 6);
        } else {
            return hms;
        }
    }

}

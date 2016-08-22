/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.*;
import com.tcci.sksp.entity.ar.SkSalesDiscountLog;
import com.tcci.sksp.entity.enums.SalesAllowancesPageEnum;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkSalesDetailsFacade;
import com.tcci.sksp.facade.SkSalesDiscountLogFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import com.tcci.sksp.vo.SalesAllowanceVO;
import com.tcci.sksp.vo.SalesDetailsVO;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.event.TabChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class SalesAllowancesController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<SalesDetailsVO> salesOrderList;
    private List<SalesDetailsVO> customerList;
    private SalesDetailsVO[] selectedSalesOrderList;
    private SalesDetailDataModel salesOrderDataModel;
    private List<SalesAllowancesPageEnum> pageEnumList;
    private String currentTab = "searchCriteriaTab";
    private boolean selectAll = false;
    private int tabIndex = 0;
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;

    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }
    //@EJB
    //SkSalesOrderMasterFacade salesOrderMasterFacade;
    @EJB
    SkSalesDetailsFacade salesDetailsFacade;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    SkSalesMemberFacade salesMemberFacade;
    @EJB
    SkSalesDiscountLogFacade salesDiscountLogFacade;
    @EJB
    SkCustomerFacade skCustomerFacade;
    /*public List<SalesAllowancesVO> getSalesAllowancesVOList() {
     return salesAllowancesVOList;
     }
    
     public void setSalesAllowancesVOList(List<SalesAllowancesVO> salesAllowancesVOList) {
     this.salesAllowancesVOList = salesAllowancesVOList;
     }
     * 
     */

    public SalesDetailDataModel getSalesOrderDataModel() {
        return salesOrderDataModel;
    }

    public void setSalesOrderDataModel(SalesDetailDataModel salesOrderDataModel) {
        this.salesOrderDataModel = salesOrderDataModel;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public List<SalesDetailsVO> getSalesOrderList() {
        return salesOrderList;
    }

    public void setSalesOrderList(List<SalesDetailsVO> salesOrderList) {
        this.salesOrderList = salesOrderList;
    }

    public SalesDetailsVO[] getSelectedSalesOrderList() {
        return selectedSalesOrderList;
    }

    public void setSelectedSalesOrderList(SalesDetailsVO[] selectedSalesOrderList) {
        this.selectedSalesOrderList = selectedSalesOrderList;
    }

    private Date converterDateToStartOrEnd(Date d, boolean isStart) {
        Date date = null;
        if (d == null) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (!isStart) {
            hour = 23;
            minute = 59;
            second = 59;
        }
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c.getTime();
    }

    @PostConstruct
    public void init() {
        SalesAllowancesPageEnum[] enums = SalesAllowancesPageEnum.values();
        pageEnumList = new ArrayList<SalesAllowancesPageEnum>();
        for (SalesAllowancesPageEnum e : enums) {
            pageEnumList.add(e);
        }
    }

    public String doSearchAllowancesAction() {
        try {
            salesOrderList = null;
            boolean isCustomerRequire = false;
            boolean isOnlySalesCode = true;
            queryCriteriaController.latestStepToCheckCustomerCode(isCustomerRequire, isOnlySalesCode);
            if (isOnlySalesCode && queryCriteriaController.isInvalidCustomerRelation()) {
                return null;
            } else if (!queryCriteriaController.isWrongCustomerCode()) {
                Date startDate = converterDateToStartOrEnd(queryCriteriaController.getFilter().getInvoiceStart(), true);
                Date endDate = converterDateToStartOrEnd(queryCriteriaController.getFilter().getInvoiceEnd(), false);
                logger.debug("startDate=" + startDate + ",endDate=" + endDate);
                //List<SalesDetailsVO> salesOrderMasterList
                salesOrderList = salesDetailsFacade.findSalesAllowances(queryCriteriaController.getFilter().getSales(), queryCriteriaController.getFilter().getSkCustomer(), startDate, endDate, queryCriteriaController.getFilter().getPaymentTerm());
                initSelectableSO();
                prepareCustomer();
                setTabIndex(2);
            }
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void prepareCustomer() {
        customerList = new ArrayList<SalesDetailsVO>();
        Map<String, SalesDetailsVO> customerMap = new HashMap<String, SalesDetailsVO>();
        if (salesOrderList != null) {
            for (SalesDetailsVO vo : salesOrderList) {
                String simpleCode = vo.getCustomerSimpleCode();
                if (!customerMap.containsKey(simpleCode)) {
                    customerMap.put(simpleCode, vo);
                    customerList.add(vo);
                }
            }
        }
    }

    private void initSelectableSO() {
        if (salesOrderList != null) {
            for (SalesDetailsVO vo : salesOrderList) {
                //Owner requirement, 全部應稅,免稅者手寫
                //vo.setShippingCondition("01");                
                if ("S030".equals(vo.getPaymentTerm())) {
                    vo.setSelected(true);
                }
            }
        }
    }
    /*
     private void converterSalesOrderMaster(List<SalesDetailsVO> salesOrderMasterList) {
     if (salesOrderMasterList != null) {
     salesOrderList = new ArrayList<SalesDetailsVO>();
     for (SalesDetailsVO m : salesOrderMasterList) {
     SalesOrderVO vo = new SalesOrderVO();
     vo.setSalesOrderMaster(m);
     if ("S030".equals(vo.getSalesOrderMaster().getCustomer().getPaymentTerm())) {
     vo.setSelected(true);
     }
     List<SkSalesAllowanceLog> logList = salesAllowanceLogFacade.findBySalesOrderMaster(m);
     if (logList != null && !logList.isEmpty()) {
     vo.setPrinted(true);
     vo.setPrintNumber(logList.size());
     }
     salesOrderList.add(vo);
     }
     System.out.print("converterSalesOrderMaster salesOrderList=" + salesOrderList.size() );
     } else {
     salesOrderList = null;
     }
     }
     */

    private void printout(List<SalesAllowanceVO> list) {
        for (SalesAllowanceVO vo : list) {
            System.out.println("vo customer=" + vo.getCustomerSimpleCode() + ",page=" + vo.getPage());
            for (SalesDetailsVO m : vo.getList()) {
                System.out.println("           m invoice=" + m.getInvoiceNumber());
            }
        }
    }

    private List<SalesAllowanceVO> copyMultipleForms(List<SalesAllowanceVO> salesAllowancesList) {
        List<SalesAllowanceVO> list = null;
        if (salesAllowancesList != null) {
            list = new ArrayList<SalesAllowanceVO>(salesAllowancesList.size() * 4);
            for (SalesAllowanceVO vo : salesAllowancesList) {
                int formNumber = 4;
                if (SalesAllowancesPageEnum.PREVIOUS_TWO.equals(vo.getSalesAllowancesPage())) {
                    formNumber = 2;
                }
                for (int i = 1; i <= formNumber; i++) {
                    SalesAllowanceVO v = vo.clone();
                    v.setPage(i);
                    list.add(v);
                }
            }
        }
        return list;
    }

    public void print() {
        //logger.debug("print(), selectedSalesOrderList.length={}", selectedSalesOrderList.length);
        Calculate c = new Calculate();
        c.start();
        java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream("/report/sales_allowances.jasper");
        c.end();
        System.out.println("loader rs spent=" + c.spent());
        //java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream("/report/sub_sales_allowances.jasper");
        //logger.info("generateJasperPrint is=" + is );
        c.start();
        List<SalesAllowanceVO> salesAllowancesList = prepareSalesAllowancesList();
        c.end();
        System.out.println("prepareSalesAllowancesList spent=" + c.spent());
        c.start();
        salesAllowancesList = copyMultipleForms(salesAllowancesList);
        c.end();
        System.out.println("copyAsFourForms spent=" + c.spent());
        //printout( salesAllowancesList );
        if (salesAllowancesList == null || salesAllowancesList.isEmpty()) {
            FacesMessage message = new FacesMessage("please choose sales order to print.");
            FacesContext.getCurrentInstance().addMessage("", message);
            return;
        } else {
            logger.debug("selected size=" + salesAllowancesList.size());
        }
        try {
            c.start();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
            Map<String, Object> properties = new HashMap<String, Object>();
            HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
            OutputStream outputStream = response.getOutputStream();

            properties.clear();
            //properties.put("page", 1);
            JRBeanCollectionDataSource collectionDataSource1 = new JRBeanCollectionDataSource(salesAllowancesList);
            properties.put("subReportDataSource1", collectionDataSource1);
            /*
             JRBeanCollectionDataSource collectionDataSource2 = new JRBeanCollectionDataSource(salesAllowancesList);
             properties.put("subReportDataSource2", collectionDataSource2);
             JRBeanCollectionDataSource collectionDataSource3 = new JRBeanCollectionDataSource(salesAllowancesList);
             properties.put("subReportDataSource3", collectionDataSource3);
             JRBeanCollectionDataSource collectionDataSource4 = new JRBeanCollectionDataSource(salesAllowancesList);
             properties.put("subReportDataSource4", collectionDataSource4);
             */
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, properties, new JREmptyDataSource());
            //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, properties, new JRBeanCollectionDataSource(salesAllowancesList) );
            JRPdfExporter exporter = new JRPdfExporter();
            //List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
            //jasperPrints.add(jasperPrint);
            /*
             properties.put("page", 3);
             JRBeanCollectionDataSource collectionDataSource3 = new JRBeanCollectionDataSource(salesAllowancesList);
             properties.put("subReportDataSource1", collectionDataSource3);
             JRBeanCollectionDataSource collectionDataSource4 = new JRBeanCollectionDataSource(salesAllowancesList);
             properties.put("subReportDataSource2", collectionDataSource4);
             JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport, properties, new JREmptyDataSource());
             jasperPrints.add(jasperPrint2);
             */
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrints);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporter.exportReport();
//            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);

            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=salesAllowancesReport.pdf");
            response.setContentType("application/pdf");
            outputStream.write(byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            outputStream.flush();
            outputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
            c.end();
            System.out.println("print spent=" + c.spent());
            addLogs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLogs() {
        Date now = new Date();
        for (SalesDetailsVO vo : salesOrderList) {
            if (!vo.isSelected()) {
                continue;
            }
            SkSalesDiscountLog log = new SkSalesDiscountLog();
            log.setInvoiceNumber(vo.getInvoiceNumber());
            log.setCreator(userSession.getUser());
            log.setCreatetimestamp(now);
            salesDiscountLogFacade.create(log);
        }
    }

    private List<SalesAllowanceVO> prepareSalesAllowancesList() {
        List<SalesAllowanceVO> list = new ArrayList<SalesAllowanceVO>();
        String address = "";
        List<SalesDetailsVO> selectedList = new ArrayList<SalesDetailsVO>();
        for (SalesDetailsVO vo : salesOrderList) {
            if (vo.isSelected()) {
                selectedList.add(vo);
            }
        }
        Map<String, List<SalesDetailsVO>> map = new LinkedHashMap<String, List<SalesDetailsVO>>();
        for (SalesDetailsVO vo : selectedList) {
            String customerSimpleCode = vo.getCustomerSimpleCode();
            List<SalesDetailsVO> l = map.get(customerSimpleCode);
            if (l == null) {
                l = new ArrayList<SalesDetailsVO>();
            }
            l.add(vo);
            map.put(customerSimpleCode, l);
        }
        if (map.keySet() != null) {
            for (String customerSimpleCode : map.keySet()) {
                List<SalesDetailsVO> l = map.get(customerSimpleCode);
                int i = 0;
                int max = 10;
                List<SalesDetailsVO> l2 = new ArrayList<SalesDetailsVO>();
                BigDecimal totalSalesDiscount = BigDecimal.ZERO;
                BigDecimal totalSalesDiscountTax = BigDecimal.ZERO;
                for (SalesDetailsVO vo : l) {
                    l2.add(vo);
                    vo.getSalesAllowances();
                    totalSalesDiscount = totalSalesDiscount.add(vo.getSalesAllowancesExcludeTax());
                    totalSalesDiscountTax = totalSalesDiscountTax.add(vo.getSalesAllowancesTax());
                    i++;
                    if ((i % max) == 0) {
                        SalesAllowanceVO v = new SalesAllowanceVO();
                        v.setCity(vo.getCity());
                        v.setStreet(vo.getStreet());
                        v.setCustomerName(vo.getCustomerName());
                        v.setCustomerSimpleCode(vo.getCustomerSimpleCode());
                        v.setPaymentTerm(vo.getPaymentTerm());
                        v.setShippingCondition(vo.getShippingCondition());
                        v.setVat(vo.getVat());
                        v.setList(l2);
                        v.setTotalSalesDiscount(totalSalesDiscount);
                        v.setTotalSalesDiscountTax(totalSalesDiscountTax);
                        v.setSapid(vo.getSapid());
                        v.setSalesAllowancesPage(vo.getSalesAllowancesPage());
                        v.setDiscountRate(vo.getDiscountRate());
                        list.add(v);
                        totalSalesDiscount = BigDecimal.ZERO;
                        totalSalesDiscountTax = BigDecimal.ZERO;
                        l2 = new ArrayList<SalesDetailsVO>();
                    }
                }
                if (l2.size() < max) {
                    for (int j = l2.size(); j < 10; j++) {
                        l2.add(new SalesDetailsVO());
                    }
                    SalesAllowanceVO v = new SalesAllowanceVO();
                    v.setCity(l2.get(0).getCity());
                    v.setStreet(l2.get(0).getStreet());
                    v.setCustomerName(l2.get(0).getCustomerName());
                    v.setCustomerSimpleCode(l2.get(0).getCustomerSimpleCode());
                    v.setPaymentTerm(l2.get(0).getPaymentTerm());
                    v.setShippingCondition(l2.get(0).getShippingCondition());
                    v.setVat(l2.get(0).getVat());
                    v.setSapid(l2.get(0).getSapid());
                    v.setList(l2);
                    v.setTotalSalesDiscount(totalSalesDiscount);
                    v.setTotalSalesDiscountTax(totalSalesDiscountTax);
                    v.setSalesAllowancesPage(l2.get(0).getSalesAllowancesPage());
                    v.setDiscountRate(l2.get(0).getDiscountRate());
                    list.add(v);
                }
            }
        }
        return list;
    }
    /*
     private String formatDate(Date date) {
     String year = String.valueOf(date.getYear() - 10);
     String month = String.valueOf(date.getMonth() + 1);
     if (month.length() == 1) {
     month = "0" + month;
     }
     String day = String.valueOf(date.getDate());
     if (day.length() == 1) {
     day = "0" + 1;
     }
     logger.debug("formatDate(), date= {}", year + month + day);
     return year + month + day;
     }
     */

    public void selectAllChange(AjaxBehaviorEvent event) {
        logger.debug("selectAllChange(), selectedAll={}", selectAll);
        if (salesOrderList != null && !salesOrderList.isEmpty()) {
            for (SalesDetailsVO vo : salesOrderList) {
                vo.setSelected(selectAll);
            }
        }
    }

    public void onTabChange(TabChangeEvent event) {
        /*
         FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab: " + event.getTab().getTitle());
         System.out.println("Tab Changed Active Tab: " + event.getTab().getId() );
         FacesContext.getCurrentInstance().addMessage(null, msg);
         */
        //searchCriteriaTab,salesAllowancesPageTab,searchResultTab
        String searchCriteriaTab = "searchCriteriaTab";
        String salesAllowancesPageTab = "salesAllowancesPageTab";
        String searchResultTab = "searchResultTab";
        if (getCurrentTab() != null && getCurrentTab().equals(searchCriteriaTab)
                && !event.getTab().getId().equals(searchCriteriaTab)) {
            System.out.println("do Search");
            doSearchAllowancesAction();
        }
        //System.out.println("Tab Changed Active Tab: " + event.getTab().getId() +",previous=" + getCurrentTab() );
        setCurrentTab(event.getTab().getId());
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

    public List<SalesDetailsVO> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<SalesDetailsVO> customerList) {
        this.customerList = customerList;
    }

    public List<SalesAllowancesPageEnum> getPageEnumList() {
        return pageEnumList;
    }

    public void setPageEnumList(List<SalesAllowancesPageEnum> pageEnumList) {
        this.pageEnumList = pageEnumList;
    }

    public String saveAllowancesPageAction() {
        try {
            skCustomerFacade.saveAllowancePageAndTaxRate(customerList);
            resetPageAndTaxRate();
            setTabIndex(2);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, "已儲存資料!");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }

    private void resetPageAndTaxRate() {
        List<SalesDetailsVO> salesOrderList2 = new ArrayList<SalesDetailsVO>();
        Map<String, SalesDetailsVO> customerMap = new HashMap<String, SalesDetailsVO>();
        if (customerList != null) {
            for (SalesDetailsVO vo : customerList) {
                String simpleCode = vo.getCustomerSimpleCode();
                if (!customerMap.containsKey(simpleCode)) {
                    customerMap.put(simpleCode, vo);
                }
            }
        }
        if (salesOrderList != null) {
            for (SalesDetailsVO vo : salesOrderList) {
                String simpleCode = vo.getCustomerSimpleCode();
                SalesDetailsVO customer = customerMap.get(simpleCode);
                vo.setSalesAllowancesPage(customer.getSalesAllowancesPage());
                vo.setDiscountRate(customer.getDiscountRate());
                vo.getSalesAllowances();
                salesOrderList2.add(vo);
                //System.out.println("resetPage customerpage=" + page.getSalesAllowancesPage() + ",");
            }
        }
        salesOrderList = salesOrderList2;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}

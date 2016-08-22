/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.fc.util.report.ExportFormatEnum;
import com.tcci.fc.util.report.TCCJasperReport;
import com.tcci.sksp.controller.util.ArDataModel;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkAccountsReceivable;
import com.tcci.sksp.entity.enums.AdvanceRemitTypeEnum;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkAccountsReceivableFacade;
import com.tcci.sksp.facade.SkAdvanceRemitFacade;
import com.tcci.sksp.vo.AccountsReceivableVO;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class AccountsReceivableController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;

    private List<AccountsReceivableVO> arVOList;
    private List<SkAccountsReceivable> arList;
    private SkAccountsReceivable[] selectedARList;
    private ArDataModel arDataModel;
    private BigDecimal summaryQueryARs;
    private String summaryQueryARsString;
    
    @EJB SkAccountsReceivableFacade accountsReceivableFacade;
    @EJB SkAdvanceRemitFacade advanceRemitFacade;

    public BigDecimal getSummaryQueryARs() {
        return summaryQueryARs;
    }

    public void setSummaryQueryARs(BigDecimal summaryQueryARs) {
        this.summaryQueryARs = summaryQueryARs;
    }
    
    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    public ArDataModel getArDataModel() {
        return arDataModel;
    }

    public void setArDataModel(ArDataModel arDataModel) {
        this.arDataModel = arDataModel;
    }

    public SkAccountsReceivable[] getSelectedARList() {
        return selectedARList;
    }

    public void setSelectedARList(SkAccountsReceivable[] selectedARList) {
        this.selectedARList = selectedARList;
    }

    public List<SkAccountsReceivable> getArList() {
        return arList;
    }

    public void setArList(List<SkAccountsReceivable> arList) {
        this.arList = arList;
    }

    public List<AccountsReceivableVO> getArVOList() {
        return arVOList;
    }

    public void setArVOList(List<AccountsReceivableVO> arVOList) {
        this.arVOList = arVOList;
    }
    @PostConstruct
    public void init() {
        logger.info("AccountsReceivableController init");
        logger.info("AccountsReceivableController getInvoiceStart" + queryCriteriaController.getFilter().getInvoiceStart());
        queryCriteriaController.getFilter().setInvoiceStart(null);
        logger.info("AccountsReceivableController getInvoiceStart" + queryCriteriaController.getFilter().getInvoiceStart());
        setQueryCriteriaController(queryCriteriaController);
    }
    public String doSearchARAction(){
        /*
        if( startDate == null )
            startDate = getDefaultStartDate();
        startDate = converterDateToStartOrEnd(startDate,true);
         * 
         */
        try {
            queryCriteriaController.latestStepToCheckCustomerCode(false, true);
            SkSalesMember member = queryCriteriaController.getFilter().getSales();
            SkCustomer customer = queryCriteriaController.getFilter().getSkCustomer();
            Date startDate = queryCriteriaController.getFilter().getInvoiceStart();
            Date endDate = converterDateToStartOrEnd(queryCriteriaController.getFilter().getInvoiceEnd(),false);
            List<SkAccountsReceivable> list = accountsReceivableFacade.findARByCriteria(member, customer , startDate, endDate);
            logger.debug("list.size()={}",list.size());
            converterToVO(list);
        } catch(Exception e) {
            FacesUtil.addFacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }
    private void converterToVO(List<SkAccountsReceivable> list){
        summaryQueryARs = BigDecimal.ZERO;
        arVOList = new ArrayList<AccountsReceivableVO>();
        AccountsReceivableVO vo = null;
        for(SkAccountsReceivable ar : list ){
            if( (vo != null) && (ar.getCustomer().equals(vo.getCustomer())) ){
                
                List<SkAccountsReceivable> subList = vo.getList();
                subList.add(ar);
                vo.setList(  subList );
                vo.setTotalAmount( vo.getTotalAmount().add(ar.getAmount() ));
                if( vo.getTotalPremiumDiscount() == null )
                    vo.setTotalPremiumDiscount(ar.getPremiumDiscount());
                else{
                    if( ar.getPremiumDiscount() != null )
                    vo.setTotalPremiumDiscount( vo.getTotalPremiumDiscount().add(ar.getPremiumDiscount()));
                }
                if( ar.getPremiumDiscount() == null )
                    vo.setTotalARAmount( vo.getTotalARAmount().add( ar.getAmount()) );
                else
                    vo.setTotalARAmount( vo.getTotalARAmount().add( ar.getAmount().add(ar.getPremiumDiscount() )) );
            }else{
                if( vo != null )
                    arVOList.add(vo);
                vo = new AccountsReceivableVO();
                vo.setCustomer(ar.getCustomer());
                Long advancedReceiptsA = advanceRemitFacade.getAdvancedAmountByCustomer(ar.getCustomer(), AdvanceRemitTypeEnum.A, null) * (-1); // The advanced receipts should be displayed as positive number.
                Long advancedReceiptsJ = advanceRemitFacade.getAdvancedAmountByCustomer(ar.getCustomer(), AdvanceRemitTypeEnum.J, null) * (-1);
                vo.setAdvancedReceiptsA(advancedReceiptsA);
                vo.setAdvancedReceiptsJ(advancedReceiptsJ);
                List<SkAccountsReceivable> subList = new ArrayList<SkAccountsReceivable>();
                subList.add(ar);
                vo.setTotalAmount(ar.getAmount());
                if( ar.getPremiumDiscount() == null)
                    vo.setTotalPremiumDiscount( BigDecimal.ZERO );
                else
                    vo.setTotalPremiumDiscount(ar.getPremiumDiscount());
                if( ar.getPremiumDiscount() == null )
                    vo.setTotalARAmount(ar.getAmount() );
                else
                    vo.setTotalARAmount(ar.getAmount().add(ar.getPremiumDiscount()) );
                vo.setList(subList);
            }
        }
        if( vo != null )
            arVOList.add(vo);
        if( arVOList != null && !arVOList.isEmpty() ){
            for( AccountsReceivableVO arVO :arVOList){
                if( summaryQueryARs == null)
                    summaryQueryARs = arVO.getTotalARAmount();
                else
                    summaryQueryARs = summaryQueryARs.add(arVO.getTotalARAmount());
            }
        }else{
            arVOList = null;
            summaryQueryARs = null;
        }
    }
    /*
    private Date getDefaultStartDate(){
        Date date = null;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1900);
        return c.getTime();
    }
    */
    private Date converterDateToStartOrEnd(Date d,boolean isStart){
        Date date = null;
        if(d == null)
            return date;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int hour = 0;
        int minute = 0;
        int second = 0;
        if( !isStart ){
            hour =23;
            minute=59;
            second=59;
        }
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c.getTime();
    }
    
    public String doExportARAction1(){
        String reportName = "/report/arReport.jasper";
        java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportName);
        HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
        Map params = new HashMap();
        try {
            params.put("salesMember", queryCriteriaController.getFilter().getSales());
            params.put("invoiceStart", queryCriteriaController.getFilter().getInvoiceStart());
            params.put("invoiceEnd", queryCriteriaController.getFilter().getInvoiceEnd());
            //params.put("arSubReport", arSubReport);            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource( arVOList ));
            //JasperPrint jasperPrint = JasperFillManager.fillReport(reportName, parameters, new JRBeanCollectionDataSource(DailySalesFactory.getBeanCollection(parameters)));

            OutputStream out = response.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=arReport.pdf");
            response.setContentType("application/pdf");
            out.write(byteArrayOutputStream.toByteArray());
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();

        } catch (JRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
       
    }

    public String doExportPDFAction() {
        return doExportARAction(ExportFormatEnum.PDF);
    }
    
    public String doExportXLSAction() {
        return doExportARAction(ExportFormatEnum.XLS);
    }
     
    private String doExportARAction(ExportFormatEnum exportFormatEnum){
        TCCJasperReport report =new TCCJasperReport();
        try {
            //Logger.getLogger(this.getClass().getCanonicalName()).info("doExportARAction arVOList=" + arVOList );
            String rootPath="/report";
            //String rootPath="";
            String fileName="arReport";
            String subReportFileName ="arReport_subreport_detail";
            Map<String,Object> params= new HashMap<String,Object>();
            //Load Subreport
            JasperReport arSubReport = report.loadReport(rootPath, subReportFileName);
            params.put("salesMember", queryCriteriaController.getFilter().getSales());
            params.put("invoiceStart", queryCriteriaController.getFilter().getInvoiceStart());
            params.put("invoiceEnd", queryCriteriaController.getFilter().getInvoiceEnd());
            params.put("arSubReport", arSubReport);
            params.put("summaryQueryARs", summaryQueryARs);
            HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());           
            OutputStream outputStream = response.getOutputStream();
            report.execute( arVOList, rootPath, fileName,fileName, params, response, exportFormatEnum, outputStream);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException e) {
             String msg = " Error:" + e.getLocalizedMessage();
             FacesMessage fMsg =new FacesMessage(FacesMessage.SEVERITY_ERROR ,msg,msg);
             FacesContext.getCurrentInstance().addMessage(null, fMsg);
             e.printStackTrace();
        } catch (Exception e) {
            String msg = " Error:" + e.getLocalizedMessage();
             FacesMessage fMsg =new FacesMessage(FacesMessage.SEVERITY_ERROR ,msg,msg);
             FacesContext.getCurrentInstance().addMessage(null, fMsg);
            e.printStackTrace();
        }
        return null;
    }

    public String getSummaryQueryARsString() {
        if( summaryQueryARs == null)
            summaryQueryARsString ="0";
        else{
            summaryQueryARsString = NumberFormat.getNumberInstance().format(summaryQueryARs);
        }
        return summaryQueryARsString;
    }
    
}

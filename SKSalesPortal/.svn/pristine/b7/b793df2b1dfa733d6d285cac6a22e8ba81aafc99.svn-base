package com.tcci.sksp.controller.remit;

import com.tcci.fc.util.report.ExportFormatEnum;
import com.tcci.fc.util.report.TCCJasperReport;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.enums.BankEnum;
import com.tcci.sksp.entity.enums.PaymentTypeEnum;
import com.tcci.sksp.vo.CashSummaryVO;
import com.tcci.sksp.vo.CheckSummaryVO;
import com.tcci.sksp.vo.RemitListVO;
import com.tcci.sksp.vo.RemitMasterVO;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class RemitQueryExportToPDFController {
    final Logger logger = LoggerFactory.getLogger(RemitQueryExportToPDFController.class);
    
    // report root path 
    private final static String REPORT_ROOT = "report";
    // report name    
    private final static String REPORT_NAME = "remitQueryPrint";
    // sub-report name
    private final static String SUBREPORT_NAME1 = "remitQueryPrint_subreport1";   
    private final static String SUBREPORT_NAME1D = "remitQueryPrint_subreport1_detail";
    private final static String SUBREPORT_NAME2 = "remitQueryPrint_subreport2";
    private final static String SUBREPORT_NAME3 = "remitQueryPrint_subreport3";   
    
    private SkArRemitMaster[] remitMasterList;
    
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }    
    
    @ManagedProperty(value = "#{remitQueryController}")
    RemitQueryController remitQueryController;
    
    public void setRemitQueryController(RemitQueryController remitQueryController) {
        this.remitQueryController = remitQueryController;
    }    
            
    public void exportReportPDF() {
        remitMasterList = remitQueryController.getSelectedMasterList(); 
        if (remitMasterList.length == 0) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitQuery.msg.at.least.one.paymentnumber"));
            return;
        }
        
        TCCJasperReport report = new TCCJasperReport();
        Connection connection = null;        
        try {            
            String fileName = REPORT_NAME;
            Map<String, Object> params = new HashMap<String, Object>();       
            params.put("sales", queryCriteriaController.getFilter().getSales());
            params.put("payingStart", queryCriteriaController.getFilter().getPayingStart()); 
            params.put("payingEnd", queryCriteriaController.getFilter().getPayingEnd());
            
            List<RemitListVO> remitListVOList = new ArrayList<RemitListVO>();
            RemitListVO remitListVO = new RemitListVO();
            remitListVO.setRemitMasterList(getRemitMasterList());
            remitListVO.setCashSummaryList(getCashSummaryList());
            remitListVO.setCheckSummaryList(getCheckSummaryList());
            remitListVOList.add(remitListVO);

            //Load Subreport
            JasperReport subReport1 = report.loadReport(REPORT_ROOT, SUBREPORT_NAME1);
            params.put("SUBREPORT_1", subReport1);
            
            JasperReport subReport1D = report.loadReport(REPORT_ROOT, SUBREPORT_NAME1D);
            params.put("SUBREPORT_1D", subReport1D);            

            JasperReport subReport2 = report.loadReport(REPORT_ROOT, SUBREPORT_NAME2);
            params.put("SUBREPORT_2", subReport2);
            
            JasperReport subReport3 = report.loadReport(REPORT_ROOT, SUBREPORT_NAME3);
            params.put("SUBREPORT_3", subReport3);                

            HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
            ExportFormatEnum exportFormatEnum = ExportFormatEnum.PDF;
            OutputStream outputStream = response.getOutputStream();

            String outputFileName = REPORT_NAME;
            report.execute(remitListVOList, REPORT_ROOT, fileName, outputFileName, params, response, exportFormatEnum, outputStream);            

            FacesContext.getCurrentInstance().responseComplete();            
        } catch (JRException e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error:" + e.getLocalizedMessage());
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error:" + e.getLocalizedMessage());     
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
            }
        }
    }
    
    private List<RemitMasterVO> getRemitMasterList() {        
        List<RemitMasterVO> remitMasterVOList = new ArrayList<RemitMasterVO>();
          
        for (SkArRemitMaster master : remitMasterList) {
            RemitMasterVO vo = new RemitMasterVO();
            vo.setRemitMaster(master);
            vo.setRemitItemList(new ArrayList(master.getSkArRemitItemCollection()));
            remitMasterVOList.add(vo);            
        }        
        return remitMasterVOList;
    }
    
    private List<CashSummaryVO> getCashSummaryList() {                   
        List<CashSummaryVO> cashSummaryList = new ArrayList<CashSummaryVO>();
        
        Map<BankEnum, BigDecimal> bankMap = new EnumMap<BankEnum, BigDecimal>(BankEnum.class);               
        for (SkArRemitMaster master : remitMasterList) {
            BankEnum bank = master.getBank();
            BigDecimal remitAmount = bankMap.get(bank);
            if (remitAmount == null) {
                bankMap.put(bank, master.getRemittanceAmount());
            } else {
                bankMap.put(bank, remitAmount.add(master.getRemittanceAmount()));
            }
        }
        for (BankEnum bank : bankMap.keySet()) {
            CashSummaryVO vo = new CashSummaryVO();
            vo.setBankName(bank.getDisplayName());
            vo.setCashTotal(bankMap.get(bank));
            cashSummaryList.add(vo);
        }
        return cashSummaryList;
    }
    
    private List<CheckSummaryVO> getCheckSummaryList() {    
        List<CheckSummaryVO> checkSummaryVOList = new ArrayList<CheckSummaryVO>();
        
        List<SkArRemitItem> remitItems = new ArrayList<SkArRemitItem>();       
        for (SkArRemitMaster master : remitMasterList) {
            remitItems.addAll(master.getSkArRemitItemCollection());
        }
        Map<String, BigDecimal> checkMap = new LinkedHashMap<String, BigDecimal>();
        for (SkArRemitItem item : remitItems) {
            if (PaymentTypeEnum.CHECK.equals(item.getPaymentType())) {
                putCheckMap(item.getCheckNumber(), item.getAmount(), checkMap);
            }
            if (PaymentTypeEnum.CHECK.equals(item.getPaymentType2())) {
                putCheckMap(item.getCheckNumber2(), item.getAmount2(), checkMap);
            }         
        }
        for (String checkNumber : checkMap.keySet()) {
            CheckSummaryVO vo = new CheckSummaryVO();
            vo.setCheckNumber(checkNumber);
            vo.setCheckTotal(checkMap.get(checkNumber));
            checkSummaryVOList.add(vo);
        }
        return checkSummaryVOList;
    }            
    
    private void putCheckMap(String checkNumber, BigDecimal checkAmount, Map<String, BigDecimal> checkMap) {
        if (!StringUtils.isEmpty(checkNumber)) {
            BigDecimal amount = checkMap.get(checkNumber);
            if (amount == null) {
                checkMap.put(checkNumber, checkAmount);
            } else {
                checkMap.put(checkNumber, amount.add(checkAmount));
            }
        }
    }

}

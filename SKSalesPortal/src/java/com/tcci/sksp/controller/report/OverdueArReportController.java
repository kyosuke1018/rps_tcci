package com.tcci.sksp.controller.report;

import com.tcci.fc.util.report.ExportFormatEnum;
import com.tcci.fc.util.report.TCCJasperReport;
import com.tcci.sksp.controller.util.ArDataModel;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.ar.SkOverdueAr;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkOverdueArFacade;
import com.tcci.sksp.vo.OverdueArVO;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import javax.ejb.EJB;

@ManagedBean
@ViewScoped
public class OverdueArReportController {
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;

    private Integer overdueDaysNumber;
    private List<OverdueArVO> overdueArVOList;
    private List<SkOverdueAr> overdueArList;
    private SkOverdueAr[] selectedOverdueArList;
    private BigDecimal summaryQueryARs;
    private String summaryQueryARsString;
       
    @EJB SkOverdueArFacade skOverdueArFacade;    

    public BigDecimal getSummaryQueryARs() {
        return summaryQueryARs;
    }

    public void setSummaryQueryARs(BigDecimal summaryQueryARs) {
        this.summaryQueryARs = summaryQueryARs;
    }
    
    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }
    
    public SkOverdueAr[] getSelectedOverdueArList() {
        return selectedOverdueArList;
    }

    public void setSelectedOverdueArList(SkOverdueAr[] selectedOverdueArList) {
        this.selectedOverdueArList = selectedOverdueArList;
    }

    public List<SkOverdueAr> getOverdueArList() {
        return overdueArList;
    }

    public void setOverdueArList(List<SkOverdueAr> overdueArList) {
        this.overdueArList = overdueArList;
    }

    public List<OverdueArVO> getOverdueArVOList() {
        return overdueArVOList;
    }

    public void setOverdueArVOList(List<OverdueArVO> overdueArVOList) {
        this.overdueArVOList = overdueArVOList;
    }
    
    @PostConstruct
    public void init() {
        setQueryCriteriaController(queryCriteriaController);
    }
    public String doSearchAction(){       
        SkSalesMember sales = queryCriteriaController.getFilter().getSales();
        String date = queryCriteriaController.getFilter().getYear() +"/"+ queryCriteriaController.getFilter().getMonth();        
        try{
            List<SkOverdueAr> list = skOverdueArFacade.findByCriteria(sales, date, overdueDaysNumber);
            converterToVO(list);
        }catch(Exception e){
            FacesUtil.addFacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }
    private void converterToVO(List<SkOverdueAr> list){
        overdueArVOList = new ArrayList<OverdueArVO>();
        summaryQueryARs = null;
        OverdueArVO vo = null;
        for(SkOverdueAr ar : list ){
            if( (vo != null) && (ar.getCustomer().equals(vo.getCustomer())) ){
                
                List<SkOverdueAr> subList = vo.getList();
                subList.add(ar);
                vo.setList(  subList );
                vo.setTotalAmount( vo.getTotalAmount().add(ar.getInvoiceAmount() ));
                vo.setTotalARAmount( vo.getTotalARAmount().add( ar.getInvoiceAmount()) );
               
            }else{
                if( vo != null )
                    overdueArVOList.add(vo);
                vo = new OverdueArVO();
                vo.setCustomer(ar.getCustomer());
                
                List<SkOverdueAr> subList = new ArrayList<SkOverdueAr>();
                subList.add(ar);
                vo.setTotalAmount(ar.getInvoiceAmount());                
                vo.setList(subList);
                vo.setTotalARAmount(ar.getInvoiceAmount() );
            }
        }
        if( vo != null )
            overdueArVOList.add(vo);
        if( overdueArVOList != null && !overdueArVOList.isEmpty() ){
            for( OverdueArVO arVO :overdueArVOList){
                if( summaryQueryARs == null)
                    summaryQueryARs = arVO.getTotalARAmount();
                else
                    summaryQueryARs = summaryQueryARs.add(arVO.getTotalARAmount());
            }
        }
//        else{
//            overdueArVOList = null;
//            summaryQueryARs = null;
//        }
    }
   
    public String doExportARAction(){
        TCCJasperReport report =new TCCJasperReport();
        try {
            //Logger.getLogger(this.getClass().getCanonicalName()).info("doExportARAction arVOList=" + arVOList );
            String rootPath="/report";
            //String rootPath="";
            String fileName="overdueArReport";
            String subReportFileName ="overdueArReport_subreport_detail";
            Map<String,Object> params= new HashMap<String,Object>();
            //Load Subreport
            JasperReport subReport = report.loadReport(rootPath, subReportFileName);
            params.put("salesMember", queryCriteriaController.getFilter().getSales());
            params.put("year", queryCriteriaController.getFilter().getYear());
            params.put("month", queryCriteriaController.getFilter().getMonth());
            params.put("overdueDaysNumber", overdueDaysNumber);            
            params.put("subReport", subReport);
            params.put("summaryQueryARs", summaryQueryARs);
            HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
            ExportFormatEnum exportFormatEnum = ExportFormatEnum.XLS;
            OutputStream outputStream = response.getOutputStream();
            report.execute( overdueArVOList, rootPath, fileName,fileName, params, response, exportFormatEnum, outputStream);
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

    public Integer getOverdueDaysNumber() {
        return overdueDaysNumber;
    }

    public void setOverdueDaysNumber(Integer overdueDaysNumber) {
        this.overdueDaysNumber = overdueDaysNumber;
    }
    
}

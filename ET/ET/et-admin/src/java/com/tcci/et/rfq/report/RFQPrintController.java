package com.tcci.et.rfq.report;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.ExcelUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "rfqPrintController")
@ViewScoped
public class RFQPrintController extends SessionAwareController implements Serializable {

    // TODO change the number 
    public static final long FUNC_OPTION = 27;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    // 查詢條件
    private BaseCriteriaVO criteriaVO;
//    private List<SelectItem> factoryOptions;

    @EJB
    private RFQFacade rfqFacade;

    // 結果
    private BaseLazyDataModel<RFQPrintCtlVo> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<RFQPrintDtlVo> filterResultList; // datatable filter 後的結果

    private StreamedContent file; // 下載檔

    public void print() {

        try {
            // TODO change to relative path 
            JasperReport jasperReport = JasperCompileManager.compileReport("D:\\svn\\et\\et-admin\\src\\java\\report\\rfq\\RFQ.jrxml");

            HashMap<String, Object> test = new HashMap();

            test.put("address", "市府路1號");
            test.put("companyName", "台灣水泥");
            test.put("contactName", "馬克");
            test.put("contactNumber", "0466149316");
            test.put("contactTax", "0466149318");
            test.put("deadline", "20200101");
            test.put("RFQnumber", "1100221100");
            test.put("RFQdate", "20190701");
            test.put("buyerName", "馬克三號");
            test.put("buyerNumber", "0466123456");
            test.put("buyerTax", "0466123458");
            test.put("buyerEmail", "a@aa.a");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, test, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(jasperPrint, "temp.pdf");

            File xlsFile = new File("temp.pdf");

            StreamedContent sc = new DefaultStreamedContent(new FileInputStream(xlsFile), "application/pdf", ExcelUtils.genFileName("RFQ", ".pdf"));
            this.setFile(sc);

        } catch (Exception e) {

        }
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    @PostConstruct
    private void init() {

        criteriaVO = new BaseCriteriaVO();
        criteriaVO.setActive(Boolean.TRUE);

        doQuery();
    }

    public void doQuery() {

        List<RFQPrintCtlVo> resList = new ArrayList<>();
        resList = rfqFacade.getRFQCtlAll();
        lazyModel = new BaseLazyDataModel<>(resList);
    }

    public void doReset() {
        if (lazyModel != null) {
            lazyModel.reset();
        }
        criteriaVO = new BaseCriteriaVO();
        criteriaVO.reset();

        resetDataTable();
    }

    public void resetDataTable() {
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }

    @Override
    public String getFuncTitle() {
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }

    public BaseLazyDataModel<RFQPrintCtlVo> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<RFQPrintCtlVo> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<RFQPrintDtlVo> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<RFQPrintDtlVo> filterResultList) {
        this.filterResultList = filterResultList;
    }

}

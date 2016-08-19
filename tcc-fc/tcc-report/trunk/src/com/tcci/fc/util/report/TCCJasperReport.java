/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util.report;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
public class TCCJasperReport extends AbstractReport{
    //private String DATA_SOURCE = "DATA_SOURCE";
    //private String CONNECTION = "CONNECTION";
    private Logger logger = Logger.getLogger(TCCJasperReport.class.getName());
    private ExportSourceEnum dataType ;
    private JRDataSource dataSource;
    private Connection connection;
    private String reportName;
    private String reportTemplatePath;
    private JasperReport report;
    private JasperPrint print;
    private ExportFormatEnum exportFormatEnum;
    private HttpServletResponse reponse;
    @Override
    public void prepareDataSource(List data) {
        dataSource = new JRBeanCollectionDataSource(data);
        dataType= ExportSourceEnum.DATA_SOURCE;
    }

    @Override
    public void prepareConnection(Connection connection) {
        this.connection  = connection;
        dataType= ExportSourceEnum.CONNECTION;
    }
        
    @Override
    public void getReportTempalteFile(String rootPath, String fileName) {
        reportTemplatePath = getReportTemplateFullPath(rootPath,fileName );
    }

    @Override
    public void generateJasperPrint(Map<String, Object> params) throws JRException {
        //logger.info("generateJasperPrint reportTemplatePath=" + reportTemplatePath );
        java.io.InputStream is = TCCJasperReport.class.getClassLoader().getResourceAsStream(reportTemplatePath);
        //logger.info("generateJasperPrint is=" + is );
        report = (JasperReport) JRLoader.loadObject(is);
        //logger.info("generateJasperPrint report=" + report );
        if( ExportSourceEnum.CONNECTION.equals(dataType))
            print = JasperFillManager.fillReport(report, params, connection );
        else if( ExportSourceEnum.DATA_SOURCE.equals(dataType))
            print = JasperFillManager.fillReport(report, params, dataSource );
        //logger.info("generateJasperPrint print=" + print );
    }

    @Override
    public void setupExportFormat(HttpServletResponse response, ExportFormatEnum exportFormatEnum,String outputFileName) throws IOException {
        String outPutReportFileName = outputFileName;
        this.exportFormatEnum = exportFormatEnum;
        response.setCharacterEncoding("UTF-8");
        if( exportFormatEnum.PDF.equals(exportFormatEnum)){
             response.setContentType("application/pdf");
             outPutReportFileName = outPutReportFileName.concat(".pdf");
        }else if( exportFormatEnum.XLS.equals(exportFormatEnum)){
            response.setContentType("application/vnd.ms-excel");
            outPutReportFileName = outPutReportFileName.concat(".xls");
        }else if( exportFormatEnum.CSV.equals(exportFormatEnum)){
            response.setContentType("application/octet-stream;charset=UTF-8");
            outPutReportFileName = outPutReportFileName.concat(".csv");
        }
        response.setHeader("Content-Disposition", "attachment; filename=".concat(outPutReportFileName) );
        //logger.info("setupExportFormat outputFileName=" + outputFileName);
    }

    @Override
    public void exportReport(OutputStream outputStream) throws JRException, IOException {
        JRExporter exporter = null;
        if( exportFormatEnum.PDF.equals(exportFormatEnum)){
            exporter = new JRPdfExporter();
        }else if( exportFormatEnum.XLS.equals(exportFormatEnum)){
            exporter = new JRXlsExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            //exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
            //exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.TRUE);
            //exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,ReportsConstants.bankDetailsHtml);
            //exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.TRUE);
        }else if( exportFormatEnum.CSV.equals(exportFormatEnum)){
            exporter = new JRCsvExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
        }
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);        
        //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, this);
        exporter.exportReport();
    }

    public String getReportTemplateFullPath(String rootPath, String fileName) {
        String reportTemplateFullPath;
        if( StringUtils.isEmpty(rootPath) )
            reportTemplateFullPath = rootPath.concat(fileName).concat(".jasper");
        else
            reportTemplateFullPath = rootPath.concat("/").concat(fileName).concat(".jasper");
        return reportTemplateFullPath;
    }
    
    public JasperReport loadReport(String rootPath, String fileName) {
        try {
            String reportTemplatePath = getReportTemplateFullPath(rootPath, fileName);
            java.io.InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportTemplatePath);
            JasperReport subreport = (JasperReport) JRLoader.loadObject(is);
            return subreport;
        } catch (JRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

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
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Jason.Yu
 */
public abstract class AbstractReport {
    public abstract void prepareDataSource(List data);
    public abstract void prepareConnection(Connection connection);
    public abstract void getReportTempalteFile(String rootPath,String fileName);
    public abstract void generateJasperPrint(Map<String,Object> params) throws JRException;
    public abstract void setupExportFormat(HttpServletResponse response,ExportFormatEnum exportFormatEnum,String outputFileName) throws IOException;
    public abstract void exportReport(OutputStream outputStream) throws JRException,IOException;
    public final void execute(List data, String rootPath,String templateFileName,String outputFileName,Map<String,Object> params,HttpServletResponse response,ExportFormatEnum exportFormatEnum,OutputStream outputStream) throws JRException,IOException{
        prepareDataSource( data);
        getReportTempalteFile( rootPath, templateFileName);
        generateJasperPrint(params);
        setupExportFormat(response,exportFormatEnum,outputFileName);
        exportReport(outputStream);
    }
    public final void execute(Connection Connection, String rootPath,String templateFileName,String outputFileName,Map<String,Object> params,HttpServletResponse response,ExportFormatEnum exportFormatEnum,OutputStream outputStream) throws JRException,IOException{
        prepareConnection( Connection );
        getReportTempalteFile( rootPath, templateFileName);
        generateJasperPrint(params);
        setupExportFormat(response,exportFormatEnum,outputFileName);
        exportReport(outputStream);
    }
}

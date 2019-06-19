/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.rs;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.facade.EcFileFacade;
import java.io.File;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Peter.pan
 */
@Path("/sys")
public class SystemREST extends AbstractWebREST {
    @EJB EcFileFacade fileFacade;
    
    public SystemREST(){
        logger.debug("SystemREST init ...");
        // for 支援排序
        // sortField : PrimeUI column fields map to SQL fields
        sortFieldMap = new HashMap<String, String>();
        sortFieldMap.put("id", "S.ID");// ID
        sortFieldMap.put("starttime", "S.STARTTIME");
        sortFieldMap.put("endtime", "S.ENDTIME");
        sortFieldMap.put("createtime", "S.CREATETIME");
        
        // sortOrder : PrimeUI sortOrder 1/-1
        sortOrderMap = new HashMap<>();
        sortOrderMap.put("-1", "DESC");
        sortOrderMap.put("1", "");
    }
    

    //<editor-fold defaultstate="collapsed" desc="for Download File">
    /**
     * 下載匯出檔
     * @param request
     * @param filename
     * @return 
     */
    @GET
    @Path("/exp/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/vnd.ms-excel")
    public Response getExcelFile(@Context HttpServletRequest request, @QueryParam("filename")String filename){
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("getExcelFile ...");
        File file = new File(GlobalConstant.DIR_EXPORT + "/" + filename);
        //String expfilename = "export-" + DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_DATETIME_STR)+".xlsx";
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename="+filename);
        return response.build();
    }
    //</editor-fold>
}

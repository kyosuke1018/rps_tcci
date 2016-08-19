/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.rpt.entity.RptDValue;
import com.tcci.rpt.entity.RptDataUploadRecord;
import com.tcci.rpt.entity.RptSheetUpload;
//import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class RptSheetUploadFacade {
    private static final Logger logger = LoggerFactory.getLogger(RptSheetUploadFacade.class);
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    @EJB
    private ContentFacade contentFacade;
    @EJB
    private RptDataUploadRecordFacade rptDataUploadRecordFacade;
    @EJB
    private RptDValueFacade valueFacade;
    @EJB
    private RptTbValueFacade tbValueFacade;
    @EJB
    private ZtfiAfcsCsbaFacade ztfiAfcsCsbaFacade;
    
    public List<RptSheetUpload> findByYearmonth(String yearmonth, CompanyGroupEnum group) {
        Query q = em.createNamedQuery("RptSheetUpload.findByYearmonth");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("group", group.getCode());
        return q.getResultList();
    }
    
    public RptSheetUpload findByYearmonthCompany(String yearmonth, FcCompany company) {
        Query q = em.createNamedQuery("RptSheetUpload.findByYearmonthCompany");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("company", company);
        List<RptSheetUpload> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public void save(RptSheetUpload reportUpload, List<AttachmentVO> attachments
            , Collection<RptDValue> values, List<ReportBaseVO> tbValues, String zbukto) throws Exception {
        if (reportUpload.getId() == null) {
            em.persist(reportUpload);
        } else {
            em.merge(reportUpload);
        }
        TcUser modifier = reportUpload.getModifier();
        Date modifytimestamp = reportUpload.getModifytimestamp();
        contentFacade.saveContent(reportUpload, attachments, modifier);
        //D0202 D0204相關先註記 20160316因原提出此報表需求之user離職 暫不使用
//        if (reportUpload.getCompany().isConsolidationRevenue()) {//合併營收26家才接收D大類
//            valueFacade.saveExcelValue(reportUpload, values, modifier, reportUpload.getModifytimestamp());
//        }
        if(reportUpload.getCompany().isConsolidationRptUpload()){//TB
            //web tbValue
            tbValueFacade.saveExcelValue(reportUpload, tbValues, modifier, modifytimestamp);
            //web sap 共用table csba
            ztfiAfcsCsbaFacade.saveExcelValue(reportUpload, tbValues, zbukto);
            
            //另存一份 與SAP共用的上傳紀錄檔
//            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
//            SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
            RptDataUploadRecord rptDataUploadRecord = rptDataUploadRecordFacade.findByYearmonthCompany(reportUpload.getYearmonth(), reportUpload.getCompany());
            logger.debug("RptSheetUploadFacade rptDataUploadRecord:"+rptDataUploadRecord);
            if(rptDataUploadRecord==null){
                rptDataUploadRecord = new RptDataUploadRecord();
                rptDataUploadRecord.setCompanyCode(reportUpload.getCompany().getCode());
                rptDataUploadRecord.setYearmonth(reportUpload.getYearmonth());
            }
            rptDataUploadRecord.setModifier(modifier);
            rptDataUploadRecord.setModifytimestamp(modifytimestamp);
//            rptDataUploadRecord.setUploadDate(sdfDate.format(modifytimestamp));
//            rptDataUploadRecord.setUploadTime(sdfTime.format(modifytimestamp));
            rptDataUploadRecordFacade.save(rptDataUploadRecord);
        }
        
        logger.debug("RptSheetUploadFacade save end!");
    }
    
}

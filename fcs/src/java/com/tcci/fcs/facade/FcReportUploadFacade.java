/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcReportA0102;
import com.tcci.fcs.entity.FcReportNote;
import com.tcci.fcs.entity.FcReportUpload;
import com.tcci.fcs.entity.FcReportValue;
import com.tcci.fcs.enums.CompanyGroupEnum;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class FcReportUploadFacade {
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    @EJB
    private FcReportValueFacade valueFacade;
    @EJB
    private ContentFacade contentFacade;
    @EJB
    private FcReportA0102Facade a0102Facade;
    
    public FcReportUpload findByYearmonthCompany(String yearmonth, FcCompany company) {
        Query q = em.createNamedQuery("FcReportUpload.findByYearmonthCompany");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("company", company);
        List<FcReportUpload> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public void save(FcReportUpload reportUpload, List<AttachmentVO> attachments, Collection<FcReportValue> values, List<FcReportA0102> excelReportA0102) throws Exception {
        if (reportUpload.getId() == null) {
            em.persist(reportUpload);
        } else {
            em.merge(reportUpload);
        }
        contentFacade.saveContent(reportUpload, attachments, reportUpload.getUploader());
        valueFacade.saveExcelValue(reportUpload, values, reportUpload.getUploader(), reportUpload.getModifytimestamp());
        a0102Facade.saveExcelValue(reportUpload, excelReportA0102, reportUpload.getUploader(), reportUpload.getModifytimestamp());
    }
    
    public List<FcReportUpload> findByYearmonth(String yearmonth, CompanyGroupEnum group) {
        Query q = em.createNamedQuery("FcReportUpload.findByYearmonth");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("group", group.getCode());
        return q.getResultList();
    }
    
    public List<FcReportValue> findReportValue(FcReportUpload report) {
        Query q = em.createQuery("SELECT v FROM FcReportValue v WHERE v.report=:report");
        q.setParameter("report", report);
        return q.getResultList();
    }
    
    public List<FcReportValue> findReportValue(String yearmonth, CompanyGroupEnum group) {
        Query q = em.createQuery("SELECT v FROM FcReportValue v WHERE v.report.yearmonth=:yearmonth AND v.report.company.group.code = :group AND v.sheet IN ('D0206', 'D0208')");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("group", group.getCode());
        return q.getResultList();
    }
    
    public void updateValues(FcReportUpload report, FcCompany coid2, String sheet, Map<String, BigDecimal> values, TcUser modifier, Date modifytimestamp) {
        valueFacade.updateValues(report, coid2, sheet, values, modifier, modifytimestamp);
    }
    
    public List<FcReportNote> findNotes(String yearmonth) {
        Query q =em.createQuery("SELECT n FROM FcReportNote n WHERE n.yearmonth=:yearmonth");
        q.setParameter("yearmonth", yearmonth);
        return q.getResultList();
    }
    
    public FcReportNote findNote(String yearmonth, FcCompany coid1, FcCompany coid2) {
        Query q =em.createQuery("SELECT n FROM FcReportNote n WHERE n.yearmonth=:yearmonth AND n.coid1=:coid1 AND n.coid2=:coid2");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("coid1", coid1);
        q.setParameter("coid2", coid2);
        List<FcReportNote> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    // 更新備註，若note是空的則刪除
    public void updateNote(String yearmonth, FcCompany coid1, FcCompany coid2, String note, TcUser modifier, Date modifytimestamp) {
        if (null == note) {
            Query q = em.createQuery("DELETE FROM FcReportNote n WHERE n.yearmonth=:yearmonth AND n.coid1=:coid1 AND n.coid2=:coid2");
            q.setParameter("yearmonth", yearmonth);
            q.setParameter("coid1", coid1);
            q.setParameter("coid2", coid2);
            q.executeUpdate();
            return;
        }
        FcReportNote rptNote = findNote(yearmonth, coid1, coid2);
        if (rptNote == null) {
            rptNote = new FcReportNote(yearmonth, coid1, coid2);
        }
        rptNote.setModifier(modifier);
        rptNote.setModifytimestamp(modifytimestamp);
        rptNote.setNote(note);
        if (rptNote.getId() == null) {
            em.persist(rptNote);
        } else {
            em.merge(rptNote);
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.util.NativeSQLUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import com.tcci.irs.controller.sheetdata.IrsCompanyVO;
import com.tcci.irs.facade.IrsCompanyNoTransFacade;
import com.tcci.irs.facade.IrsTranUploadRecordFacade;
import com.tcci.irs.facade.sheetdata.IrsSheetdataMFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class FcCompanyFacade {
    
    @EJB
    protected IrsSheetdataMFacade irsSheetdataMFacade; 
    @EJB
    protected IrsTranUploadRecordFacade irsTranUploadRecordFacade;
    @EJB
    private IrsCompanyNoTransFacade irsCompanyNoTransFacade;
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public FcCompany find(Long id) {
        return em.find(FcCompany.class, id);
    }
    
    public FcCompany findByCode(String code) {
        Query q = em.createNamedQuery("FcCompany.findByCode");
        q.setParameter("code", code);
        List<FcCompany> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<FcCompany> findAll() {
        return em.createNamedQuery("FcCompany.findAll").getResultList();
    }
    
    public List<FcCompany> findAllActive() {
        Query q = em.createNamedQuery("FcCompany.findAllActive");
        return q.getResultList();
    }
    
    /*
    依公司群組查詢 有效的公司
    consolidationRevenue : 是否限合併營收　合併聯署關係
    consolidationRpt : 是否限合併報表 
    realComp : 是否限實體公司(排除for合併的虛擬公司ex:5400XX) 
    */
    public List<FcCompany> findAllActiveByGroup(CompanyGroupEnum group, boolean consolidationRevenue, boolean consolidationRpt, boolean realComp) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c FROM FcCompany c ");
        sb.append("WHERE c.active=true ");
        if(consolidationRevenue){//限合併營收　合併聯署關係
            sb.append("AND c.consolidationRevenue=true ");
        }
        if(consolidationRpt){//限合併報表 
            sb.append("AND c.consolidationRpt=true ");
        }
        if(group != null){
            sb.append("AND c.group.code = :group ");
        }
        sb.append("ORDER BY c.code ");
        
        Query q = em.createQuery(sb.toString());
        if(group != null){
            q.setParameter("group", group.getCode());
        }
        
        if(realComp){//限實體公司(排除for合併的虛擬公司ex:5400XX) 
            List<FcCompany> result = new ArrayList();
            List<FcCompany> allList = q.getResultList();
            for(FcCompany company : allList){
                if(!company.isVirtual()){
                    result.add(company);
                }
            }
            return result;
        }
        return q.getResultList();
    }
    /*
    依公司群組查詢 有效的公司
    consolidationRevenue : 是否限合併營收　合併聯署關係
    consolidationRpt : 是否限合併報表 
    不排除虛擬公司
    */
    public List<FcCompany> findAllActiveByGroup(CompanyGroupEnum group, boolean consolidationRevenue, boolean consolidationRpt) {
        return this.findAllActiveByGroup(group, consolidationRevenue, consolidationRpt, false);
    }
    
    public List<FcCompany> findAllActiveNonsap(CompanyGroupEnum group) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c FROM FcCompany c ");
        sb.append("WHERE c.active=true ");
        sb.append("AND c.nonSap=true ");
        if(group != null){
            sb.append("AND c.group.code = :group ");
        }
        sb.append("ORDER BY c.code ");
        
        Query q = em.createQuery(sb.toString());
        if(group != null){
            q.setParameter("group", group.getCode());
        }
        return q.getResultList();
//        Query q = em.createNamedQuery("FcCompany.findAllActiveNonsap");
//        q.setParameter("group", group.getCode());
//        return q.getResultList();
    }
    
    public List<FcCompany> findByUploader(TcUser uploader) {
        String sql = "SELECT c FROM FcCompany c"
                + " WHERE c.uploader=:uploader"
                + " AND c.active=true"
                + " ORDER BY c.code";
        Query q = em.createQuery(sql);
        q.setParameter("uploader", uploader);
        return q.getResultList();
    }
    
    public List<FcCompany> findByUploaderR(TcUser uploader, CompanyGroupEnum group) {
        String sql = "SELECT u.fcCompany FROM FcUploaderR u"
                + " WHERE u.tcUser=:uploader"
                + " AND u.fcCompany.active=true AND u.fcCompany.group.code = :group"
                + " ORDER BY u.fcCompany.sort";
        Query q = em.createQuery(sql);
        q.setParameter("uploader", uploader);
        q.setParameter("group", group.getCode());
        return q.getResultList();
    }
    
    public boolean isUploader(TcUser uploader, FcCompany company) {
        String sql = "SELECT u FROM FcUploaderR u"
                + " WHERE u.tcUser=:uploader"
                + " AND u.fcCompany.active=true AND u.fcCompany = :company";
        Query q = em.createQuery(sql);
        q.setParameter("uploader", uploader);
        q.setParameter("company", company);
        return CollectionUtils.isNotEmpty(q.getResultList());
    }
    
    public void save(FcCompany company) {
        if (company.getId()==null) {
            em.persist(company);
        } else {
            em.merge(company);
        }
    }
    
    public List<IrsCompanyVO> findIrsCompanyVO(CompanyGroupEnum group, String ym){
        List<IrsCompanyVO> resultList = new ArrayList<>();
        List<FcCompany> companyList = this.findAllActiveByGroup(group, false, false, true);
        for(FcCompany company : companyList){
            IrsCompanyVO vo = new IrsCompanyVO();
            vo.setCompany(company);
//            vo.setAllEdit(irsSheetdataMFacade.companyIsAllEditByYM(company, ym));
            vo.setUploadRecord(irsTranUploadRecordFacade.findByYearmonthCompany(ym, company));
            vo.setNoTrans(irsCompanyNoTransFacade.findByYearmonthCompany(ym, company));
            resultList.add(vo);
        }
        return resultList;
    }
    
    //added by david.jen
    public List<FcCompany> findByKeyword(String keyword){
        List<FcCompany> matched = null;
        //
        StringBuilder buffer = new StringBuilder();
        String keywordExpr = "%" +  keyword + "%";
        String sql = 
        buffer.append(
                "SELECT c FROM FcCompany c ").append(
                "WHERE ").append(
                "c.name like :keywordExpr or c.code like :keywordExpr").toString() ;
        
        Query query = em.createQuery(sql);
        query.setParameter("keywordExpr", keywordExpr);
        
        matched = query.getResultList();
        
        return matched;
    }
    
    //使用者對帳公司權限
    public List<FcCompany> findIrsReconcilCompany(TcUser tcUser, FcCompany company) {
        String sql = "SELECT r.reconcilCompany FROM IrsReconcilCompanyR r"
                + " WHERE r.tcUser=:tcUser"
                + " AND r.reconcilCompany.active=true AND r.company.active=true"
                + " AND r.company = :company"
                + " ORDER BY r.reconcilCompany.code";
        Query q = em.createQuery(sql);
        q.setParameter("tcUser", tcUser);
        q.setParameter("company", company);
        return q.getResultList();
    }
    //使用者對帳公司權限 個體公司list distinct
    public List<FcCompany> findIrsReconcilCompanyRE(TcUser tcUser) {
        String sql = "SELECT distinct r.company FROM IrsReconcilCompanyR r"
                + " WHERE r.tcUser=:tcUser"
                + " AND r.reconcilCompany.active=true AND r.company.active=true"
                + " ORDER BY r.company.code";
        Query q = em.createQuery(sql);
        q.setParameter("tcUser", tcUser);
        return q.getResultList();
    }
    
    //非合併聯署公司群組
    public List<ReportBaseVO> findNonConsolidation(String group) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT fncm.GUEST_COMP_CODE,c.Name,c.ABBREVIATION,c.COMP_GROUP ");
        sql.append("from FC_NON_CONSOLIDATION_MAP fncm ");
        sql.append("left outer join FC_COMPANY c on fncm.GUEST_COMP_CODE = c.CODE ");
        sql.append("where 1=1 ");
        String pColumnName1 = "fncm.COMP_GROUP";
        sql.append(NativeSQLUtils.genEqualSQL(pColumnName1, group, params));
        sql.append("order by fncm.GUEST_COMP_CODE ");
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
//            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<ReportBaseVO> resultList = new ArrayList<>();
        for (Object row : list) {
            ReportBaseVO vo = new ReportBaseVO();
            Object[] columns = (Object[]) row;
            vo.setCompCode((String) columns[0]);
            vo.setCompName((String) columns[1]);
            vo.setAbbreviation((String) columns[2]);
            //借欄位
            vo.setComp2Code((String) columns[3]);
	    resultList.add(vo);
        }
        return resultList;
    }
}

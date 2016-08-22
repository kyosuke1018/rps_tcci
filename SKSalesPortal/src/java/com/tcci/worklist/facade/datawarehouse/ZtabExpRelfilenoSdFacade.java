package com.tcci.worklist.facade.datawarehouse;

import com.tcci.worklist.facade.AbstractFacade;
import com.tcci.worklist.entity.datawarehouse.ZtabExpRelfilenoSd;
import com.tcci.worklist.entity.datawarehouse.ZtabExpVbak;
import com.tcci.worklist.entity.datawarehouse.ZtabExpVbap;
import com.tcci.worklist.entity.datawarehouse.ZtabExpVbkd;
import com.tcci.worklist.entity.datawarehouse.ZtabExpVbpa;
import com.tcci.worklist.entity.datawarehouse.ZtabExpVbuk;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@Stateless
public class ZtabExpRelfilenoSdFacade extends AbstractFacade<ZtabExpRelfilenoSd> {

    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;
    final private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ZtabExpRelfilenoSdFacade() {
        super(ZtabExpRelfilenoSd.class);
    }

    /**
     * 依sap client 刪除資料。
     *
     * @param sapclient
     */
    public void deleteBySapclient(String sapclient) {
        String queryStr = "DELETE FROM ZtabExpRelfilenoSd o WHERE o.mandt = :sapclient";
        Query query = em.createQuery(queryStr);
        query.setParameter("sapclient", sapclient);
        query.executeUpdate();
    }

    public void updateByBnameVbeln(String sapclient, String bname, String vbeln, boolean deleted) {
        String queryStr = "UPDATE ZtabExpRelfilenoSd o SET o.deleted = :deleted WHERE o.mandt = :sapclient AND o.bname = :bname AND o.vbeln = :vbeln";
        Query query = em.createQuery(queryStr);
        query.setParameter("sapclient", sapclient);
        query.setParameter("bname", bname);
        query.setParameter("vbeln", vbeln);
        query.setParameter("deleted", deleted);
        query.executeUpdate();
    }

    /**
     * 依 sap client 及 bname 刪除資料。
     *
     * @param sapclient
     * @param bname SAP使用者主檔記錄中的使用者名稱
     */
    public void deleteBySapclientAndBname(String sapclient, String bname) {
        String queryStr = "DELETE FROM ZtabExpRelfilenoSd o WHERE o.mandt = :sapclient AND o.bname=:bname";
        Query query = em.createQuery(queryStr);
        query.setParameter("sapclient", sapclient);
        query.setParameter("bname", bname);
        query.executeUpdate();
    }

    /**
     * 依sap client 及 bname及fileno刪除資料。
     *
     * @param sapclient
     * @param bname SAP使用者主檔記錄中的使用者名稱
     * @param vbeln
     */
    public void deleteByVbeln(String sapclient, String vbeln) {
        String queryStr = "DELETE FROM ZtabExpRelfilenoSd o WHERE o.mandt = :sapclient AND o.vbeln=:vbeln";
        Query query = em.createQuery(queryStr);
        query.setParameter("sapclient", sapclient);
        query.setParameter("vbeln", vbeln);
        query.executeUpdate();
    }

    /**
     * 取得代簽核筆數
     *
     * @param empCode 工號
     * @param category ['請購單','通知單','工單']
     * @return
     */
    public List<ZtabExpRelfilenoSd> getByEmpCode(String mandt, String empCode) {
        List<ZtabExpRelfilenoSd> list = null;
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select t1 from ZtabExpRelfilenoSd t1 ,RelfilenoEmp t2 ");
        queryStr.append("where t1.bname = t2.bname "
                + "and t1.mandt = :mandt "
                + "and t2.empCode = :empCode "
                + "and t1.deleted = :deleted ");
        queryStr.append("order by t1.vbeln desc");

        Query query = em.createQuery(queryStr.toString());
        query.setParameter("empCode", empCode);
        query.setParameter("mandt", mandt);
        query.setParameter("deleted", false);
        list = query.getResultList();

//        logger.info("list.size()=" + list.size());
        return list;
    }

    public Long getCountByEmpCode(String mandt, String empCode, String category) {
//        logger.info("sapclient=" + mandt + "|empCode=" + empCode + "|category=" + category);
        Long count = 0L;

        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT COUNT(t1) FROM ZtabExpRelfilenoSd t1 ,RelfilenoEmp t2 ");
        queryStr.append("WHERE t1.bname = t2.bname "
                + "AND t1.mandt = :mandt "
                + "AND t2.empCode = :empCode AND t1.category = :category "
                + "AND t1.deleted = :deleted");

        Query query = em.createQuery(queryStr.toString());
        query.setParameter("empCode", empCode);
        query.setParameter("mandt", mandt);
        query.setParameter("category", category);
        query.setParameter("deleted", false);
        count = (Long) query.getSingleResult();

        return count;
    }

    /**
     * 依 bname + bersl 查出所有 vbeln
     *
     * @param sapclient
     * @param bname
     * @return
     */
    public List<String> findVbelnByBnameAndBersl(String sapclient, String bname, String bersl) {
        String queryStr = "SELECT o.vbeln FROM ZtabExpRelfilenoSd o WHERE o.mandt = :sapclient AND o.bname=:bname  AND o.bersl = :bersl AND o.deleted = :deleted";
        Query query = em.createQuery(queryStr);
        query.setParameter("sapclient", sapclient);
        query.setParameter("bname", bname);
        query.setParameter("bersl", bersl);
        query.setParameter("deleted", false);
        return query.getResultList();
    }

    /**
     * 依 bname 及 vbeln 查出待核清單資料
     *
     * @param sapclient
     * @param bname
     * @param vbeln
     * @return
     */
    public ZtabExpRelfilenoSd getByBnameAndVbeln(String sapclient, String bname, String vbeln) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT o FROM ZtabExpRelfilenoSd o");
        sb.append(" WHERE o.mandt = :sapclient AND o.bname=:bname AND o.vbeln=:vbeln AND o.deleted=:deleted");
        Query query = em.createQuery(sb.toString());
        query.setParameter("sapclient", sapclient);
        query.setParameter("bname", bname);
        query.setParameter("vbeln", vbeln);
        query.setParameter("deleted", false);
        List<ZtabExpRelfilenoSd> results = query.getResultList();
        if (CollectionUtils.isNotEmpty(results)) {
            return results.get(0);
        }
        return null;
    }

    /**
     * 依 vbeln 查出待核清單資料，並帶出相關的 Detail Object (包含多個bname)
     *
     * @param sapclient
     * @param vbeln
     * @return
     */
    public List<ZtabExpRelfilenoSd> findByVbelnFetchAll(String sapclient, String vbeln) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct o FROM ZtabExpRelfilenoSd o");
        sb.append(" left join fetch o.ztabExpVbakList");
        sb.append(" left join fetch  o.ztabExpVbapList");
        sb.append(" left join fetch  o.ztabExpVbkdList");
        sb.append(" left join fetch  o.ztabExpVbpaList");
        sb.append(" left join fetch  o.ztabExpVbukList");
        sb.append(" WHERE o.mandt = :sapclient AND o.vbeln=:vbeln AND o.deleted=:deleted");
        Query query = em.createQuery(sb.toString());
        query.setParameter("sapclient", sapclient);
        query.setParameter("vbeln", vbeln);
        query.setParameter("deleted", false);
        List<ZtabExpRelfilenoSd> results = query.getResultList();
        if (CollectionUtils.isNotEmpty(results)) {
            return results;
        }
        return null;
    }

    /**
     * 依 sapclient 查出已註銷的 ZtabExpRelfilenoSd
     *
     * @param sapclient
     * @return
     */
    public List<ZtabExpRelfilenoSd> fildDeletedByFilenoFetchAll(String sapclient) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct o FROM ZtabExpRelfilenoSd o");
        sb.append(" left join fetch o.ztabExpVbakList");
        sb.append(" left join fetch  o.ztabExpVbapList");
        sb.append(" left join fetch  o.ztabExpVbkdList");
        sb.append(" left join fetch  o.ztabExpVbpaList");
        sb.append(" left join fetch  o.ztabExpVbukList");
        sb.append(" WHERE o.mandt = :sapclient AND o.deleted=:deleted");
        Query query = em.createQuery(sb.toString());
        query.setParameter("sapclient", sapclient);
        query.setParameter("deleted", true);
        List<ZtabExpRelfilenoSd> results = query.getResultList();
        if (CollectionUtils.isNotEmpty(results)) {
            return results;
        }
        return null;
    }

    /**
     * 依 sapclient 刪除所有已註銷(待刪除)的資料。
     *
     * @param sapclient
     */
    public void deleteAllDeletedRelateData(String sapclient) {
        List<ZtabExpRelfilenoSd> ztabExpRelfilenoSds = fildDeletedByFilenoFetchAll(sapclient);
        if (CollectionUtils.isNotEmpty(ztabExpRelfilenoSds)) {
            deleteZtabExpRelfilenoSdRelatedData(ztabExpRelfilenoSds);
        }
    }

    /**
     *
     * 依 sapclient 及 vbeln 刪除所有相關單子的資料。(包含多個bname)
     *
     * @param sapclient
     * @param vbeln
     */
    public void deleteAllRelateDataByFileNo(String sapclient, String vbeln) {
        List<ZtabExpRelfilenoSd> ztabExpRelfilenoSds = findByVbelnFetchAll(sapclient, vbeln);
        if (CollectionUtils.isNotEmpty(ztabExpRelfilenoSds)) {
            deleteZtabExpRelfilenoSdRelatedData(ztabExpRelfilenoSds);
        }
    }

    private void deleteZtabExpRelfilenoSdRelatedData(List<ZtabExpRelfilenoSd> ztabExpRelfilenoSds) {
        for (ZtabExpRelfilenoSd ztabExpRelfilenoSd : ztabExpRelfilenoSds) {
            logger.debug("deleteZtabExpRelfilenoSdRelatedData: ztabExpRelfilenoSd={}", ztabExpRelfilenoSd.getVbeln());
            if (CollectionUtils.isNotEmpty(ztabExpRelfilenoSd.getZtabExpVbakList())) {
                for (ZtabExpVbak ztabExpVbak : ztabExpRelfilenoSd.getZtabExpVbakList()) {
                    em.remove(ztabExpVbak);
                }
            }

            if (CollectionUtils.isNotEmpty(ztabExpRelfilenoSd.getZtabExpVbapList())) {
                for (ZtabExpVbap ztabExpVbap : ztabExpRelfilenoSd.getZtabExpVbapList()) {
                    em.remove(ztabExpVbap);
                }
            }

            if (CollectionUtils.isNotEmpty(ztabExpRelfilenoSd.getZtabExpVbkdList())) {
                for (ZtabExpVbkd ztabExpVbkd : ztabExpRelfilenoSd.getZtabExpVbkdList()) {
                    em.remove(ztabExpVbkd);
                }
            }

            if (CollectionUtils.isNotEmpty(ztabExpRelfilenoSd.getZtabExpVbpaList())) {
                for (ZtabExpVbpa ztabExpVbpa : ztabExpRelfilenoSd.getZtabExpVbpaList()) {
                    em.remove(ztabExpVbpa);
                }
            }

            if (CollectionUtils.isNotEmpty(ztabExpRelfilenoSd.getZtabExpVbukList())) {
                for (ZtabExpVbuk ztabExpVbuk : ztabExpRelfilenoSd.getZtabExpVbukList()) {
                    em.remove(ztabExpVbuk);
                }
            }

            em.remove(ztabExpRelfilenoSd);
            em.flush();
            logger.debug("remove ztabExpRelfilenoSd: Bname=" + ztabExpRelfilenoSd.getBname() + ";Vbeln=" + ztabExpRelfilenoSd.getVbeln());
        }
    }

    /**
     * 查出待核清單統計資料表
     *
     * @return
     */
    public List findAllRelFilenoList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT s.name as companyName, ");
        sb.append(" r.mandt as sapclient,");
        sb.append(" emp.notes as username, ");
        sb.append(" r.bname as userSapName, ");
        sb.append(" COUNT(*) as totalFileNos");
        sb.append(" FROM SD_ZTAB_EXP_RELFILENO_SD r ");
        sb.append(" LEFT JOIN MG_RELFILENO_EMP emp on emp.bname = r.bname");
        sb.append(" LEFT JOIN MG_SAPCLIENT s on s.client = r.mandt");
        sb.append(" WHERE DELETED=0");
        sb.append(" group by r.mandt, s.name, r.bname, emp.notes");
        sb.append(" order by  r.bname, r.mandt");

        Query query = em.createNativeQuery(sb.toString());
        return query.getResultList();
    }

    public long countByBname(String bname) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(ZtabExpRelfilenoSd.class);
        Root root = cq.from(ZtabExpRelfilenoSd.class);
        cq.select(cb.count(root.get("vbeln")));
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p1 = cb.equal(root.get("bname"), bname);
        predicateList.add(p1);
        Predicate p2 = cb.equal(root.get("deleted").as(Boolean.class), false);
        predicateList.add(p2);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        Object object = getEntityManager().createQuery(cq).getSingleResult();
        logger.debug("object={}", object);
        return ((Long) object).longValue();
    }

    public List<ZtabExpRelfilenoSd> findBySapclientAndBname(String sapclient, String bname) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(ZtabExpRelfilenoSd.class);
        Root root = cq.from(ZtabExpRelfilenoSd.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p1 = cb.equal(root.get("mandt"), sapclient);
        predicateList.add(p1);
        Predicate p2 = cb.equal(root.get("bname"), bname);
        predicateList.add(p2);
        Predicate p3 = cb.equal(root.get("deleted").as(Boolean.class), false);
        predicateList.add(p3);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<ZtabExpRelfilenoSd> findByCriteria(ZtabExpRelfilenoSdFilter filter) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ZtabExpRelfilenoSd> cq = cb.createQuery(ZtabExpRelfilenoSd.class);
        Root<ZtabExpRelfilenoSd> root = cq.from(ZtabExpRelfilenoSd.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        //bname
        if (StringUtils.isNotEmpty(filter.getBname())) {
            Predicate p = cb.equal(root.get("bname").as(String.class), filter.getBname());
            predicateList.add(p);
        }
        //銷售文件單號
        if (StringUtils.isNotEmpty(filter.getVbeln())) {
            Predicate p = cb.like(root.get("vbeln").as(String.class), filter.getVbeln() + "%");
            predicateList.add(p);
        }
        //銷售組織
        if (StringUtils.isNotEmpty(filter.getVkorg())) {
            Predicate p = cb.equal(root.get("vkorg").as(String.class), filter.getVkorg());
            predicateList.add(p);
        }
        //配銷通路
        if (StringUtils.isNotEmpty(filter.getVtweg())) {
            Predicate p = cb.equal(root.get("vtweg").as(String.class), filter.getVtweg());
            predicateList.add(p);
        }
        //權限碼
        if (StringUtils.isNotEmpty(filter.getBersl())) {
            Predicate p = cb.equal(root.get("bersl").as(String.class), filter.getBersl());
            predicateList.add(p);
        }

        //建立時間
        if (filter.getAudatBegin() != null && filter.getAudatEnd() != null) {
            Calendar audatEnd = new GregorianCalendar();
            audatEnd.setTime(filter.getAudatEnd());
            audatEnd.add(Calendar.DATE, 1);
            logger.debug("between {} and {}", new Object[]{filter.getAudatBegin(), audatEnd.getTime()});
            Predicate p = cb.between(root.get("audat").as(Date.class), filter.getAudatBegin(), audatEnd.getTime());
            predicateList.add(p);
        } else if (filter.getAudatBegin() != null && filter.getAudatEnd() == null) {
            logger.debug("greaterThanOrEqualTo {}", filter.getAudatBegin());
            Predicate p = cb.greaterThanOrEqualTo(root.get("audat").as(Date.class), filter.getAudatBegin());
            predicateList.add(p);
        } else if (filter.getAudatBegin() == null && filter.getAudatEnd() != null) {
            Calendar audatEnd = new GregorianCalendar();
            audatEnd.setTime(filter.getAudatEnd());
            audatEnd.add(Calendar.DATE, 1);
            logger.debug("lessThan {}", audatEnd.getTime());
            Predicate p = cb.lessThan(root.get("audat").as(Date.class), audatEnd.getTime());
            predicateList.add(p);
        }
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        return getEntityManager().createQuery(cq).getResultList();
    }
}

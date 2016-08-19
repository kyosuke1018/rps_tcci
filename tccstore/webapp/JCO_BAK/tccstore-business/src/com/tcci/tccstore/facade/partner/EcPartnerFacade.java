/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.partner;

import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.tccstore.entity.EcGoods;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcMemberPartner;
import com.tcci.tccstore.entity.EcMemberPartnerPK;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.enums.PartnerStatusEnum;
import com.tcci.tccstore.facade.AbstractFacade;
import com.tcci.tccstore.facade.util.PartnerFilter;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class EcPartnerFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @EJB
    private ContentFacade contentFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcPartner entity) {
        if (entity.getId() == null) {
            em.persist(entity);
            // 將 owner加入MemberPartner
            EcMemberPartnerPK pk = new EcMemberPartnerPK(entity.getOwner().getId(), entity.getId());
            EcMemberPartner mp = new EcMemberPartner(pk);
            mp.setEcMember(entity.getOwner());
            mp.setEcPartner(entity);
            em.persist(mp);
        } else {
            em.merge(entity);
        }
    }
    
    public boolean isStatusApply(EcPartner entity) {
        EcPartner e = em.find(EcPartner.class, entity.getId());
        return PartnerStatusEnum.APPLY == e.getStatus();
    }
    
    /*
    public EcPartner createThenReturn(EcPartner partner) {
        List<EcPartnerProduct> partnerProductList = partner.getEcPartnerProductList();
        partner.setEcPartnerProductList(null);
        logger.debug("partner={}", partner);
        em.persist(partner);
        EcPartner newEcPartner = em.merge(partner);
        logger.debug("newEcPartner={}", newEcPartner);
        for (EcPartnerProduct partnerProduct : partnerProductList) {
            EcPartnerProductPK partnerProductPK = new EcPartnerProductPK();
            partnerProductPK.setPartnerId(newEcPartner.getId());
            partnerProductPK.setProductId(partnerProduct.getEcProduct().getId());
            partnerProduct.setEcPartnerProductPK(partnerProductPK);
            logger.debug("partnerProduct={}", partnerProduct);
            partnerProduct.setEcPartner(newEcPartner);
        }
        newEcPartner.setEcPartnerProductList(partnerProductList);
        return em.merge(newEcPartner);
    }

    public EcPartner editThenReturn(EcPartner partner) {
        EcPartner original = find(partner.getId());
        if (original.getEcPartnerProductList() != null) {
            for (EcPartnerProduct partnerProduct : original.getEcPartnerProductList()) {
                if (!partner.getEcPartnerProductList().contains(partnerProduct)) {
                    em.remove(em.merge(partnerProduct));
                }
            }
        }
        for (EcPartnerProduct partnerProduct : partner.getEcPartnerProductList()) {
            if (partnerProduct.getEcPartnerProductPK() == null) {
                EcPartnerProductPK pk = new EcPartnerProductPK();
                pk.setPartnerId(partner.getId());
                pk.setProductId(partnerProduct.getEcProduct().getId());
                partnerProduct.setEcPartnerProductPK(pk);
            }
        }

        return em.merge(partner);
    }
    */

    public void removePartner(EcPartner partner) {
        em.remove(em.merge(partner));
    }

    public EcPartnerFacade() {
        super(EcPartner.class);
    }

    public EcPartner find(Long id) {
        return em.find(EcPartner.class, id);
    }

    public EcPartner findByName(String name) {
        EcPartner result = null;
        Query q = em.createNamedQuery("EcPartner.findByName");
        q.setParameter("name", name);
        List<EcPartner> list = q.getResultList();
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }
        return result;
    }

    public List<EcPartner> findByMember(EcMember ecMember) {
        Query q = em.createNamedQuery("EcMemberPartner.findPartnerByMember");
        q.setParameter("ecMember", ecMember);
        return q.getResultList();
    }

    public void updateImage(EcPartner ecPartner, String pathname) throws Exception {
        updateImage(ecPartner, new File(pathname));
    }
    
    public void updateImage(EcPartner ecPartner, File file) throws Exception {
        if (null==file || !file.exists() || !file.isFile()) {
            return;
        }
        AttachmentVO vo = new AttachmentVO();
        byte[] content = IOUtils.toByteArray(new FileInputStream(file));
        vo.setContent(content);
        vo.setContentRole(ContentRole.PRIMARY);
        String fileName = file.getName().toLowerCase();
        String contenyType = "image/jpeg";
        if (fileName.endsWith(".png")) {
            contenyType = "image/png";
        }
        vo.setContentType(contenyType);
        vo.setFileName(file.getName());
        vo.setIndex(1);
        vo.setSize(content.length);
        List<AttachmentVO> attachments = new ArrayList<>();
        attachments.add(vo);
        contentFacade.saveContent(ecPartner, attachments);
    }

    /*
    public EcPartnerProduct findPartnerProduct(EcPartner ecPartner, EcProduct ecProduct) {
        EcPartnerProductPK pk = new EcPartnerProductPK(ecPartner.getId(), ecProduct.getId());
        return em.find(EcPartnerProduct.class, pk);
    }

    public void savePartnerProduct(EcPartnerProduct entity) {
        if (entity.getEcPartnerProductPK() == null) {
            long partnerId = entity.getEcPartner().getId();
            long productId = entity.getEcProduct().getId();
            EcPartnerProductPK pk = new EcPartnerProductPK(partnerId, productId);
            entity.setEcPartnerProductPK(pk);
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    */

    // 回傳台泥夥伴的 省,市,區
    public List<String> query(String province, String city, EcGoods ecGoods) {
        String select = "SELECT DISTINCT ";
        String from = "FROM EcPartner e ";
        String where = "WHERE e.active=TRUE ";
        String orderby = "ORDER BY ";
        Map<String, String> params = new HashMap<>();
        if (province != null && city != null) {
            select += "e.district ";
            where += "AND e.province=:province AND e.city=:city ";
            orderby += "e.district";
            params.put("province", province);
            params.put("city", city);
        } else if (province != null) {
            select += "e.city ";
            where += "AND e.province=:province ";
            orderby += "e.city";
            params.put("province", province);
        } else {
            select += "e.province ";
            orderby += "e.province";
        }
        if (ecGoods != null) {
            where += " AND EXISTS (SELECT 1 FROM EcPartnerGoods g"
                    + " WHERE g.ecGoods=:ecGoods"
                    + " AND g.ecPartner=e)";
        }
        Query q = em.createQuery(select + from + where + orderby);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        if (ecGoods != null) {
            q.setParameter("ecGoods", ecGoods);
        }
        return q.getResultList();
    }

    public List<EcPartner> query(String province, String city, String district, EcGoods ecGoods) {
        String sql;
        if (null == ecGoods) {
            sql = "SELECT e FROM EcPartner e"
                    + " WHERE e.active=TRUE"
                    + " AND e.province=:province"
                    + " AND e.city=:city"
                    + " AND e.district=:district"
                    + " ORDER BY e.name";
        } else {
            sql = "SELECT e.ecPartner FROM EcPartnerGoods e"
                    + " WHERE e.ecGoods=:ecGoods"
                    + " AND e.ecPartner.active=TRUE"
                    + " AND e.ecPartner.province=:province"
                    + " AND e.ecPartner.city=:city"
                    + " AND e.ecPartner.district=:district"
                    + " ORDER BY e.ecPartner.name";
        }
        Query q = em.createQuery(sql);
        if (null != ecGoods) {
            q.setParameter("ecGoods", ecGoods);
        }
        q.setParameter("province", province);
        q.setParameter("city", city);
        q.setParameter("district", district);
        return q.getResultList();
    }

    // 回傳大陸行政區: 省,市,區
    public List<String> queryDivision(String province, String city) {
        String select = "select t1.name";
        String from = " from ec_divisions_cn t1";
        String where = " where parent is null";
        String orderby = " order by t1.id";
        if (province != null && city != null) {
            from += ", ec_divisions_cn t2, ec_divisions_cn t3";
            where = " where t1.parent=t2.id and t2.parent=t3.id and t3.name=#province and t2.name=#city";
        } else if (province != null) {
            from += ", ec_divisions_cn t2";
            where = " where t1.parent=t2.id and t2.name=#province";
        }
        Query q = em.createNativeQuery(select + from + where + orderby);
        if (province != null && city != null) {
            q.setParameter("province", province);
            q.setParameter("city", city);
        } else if (province != null) {
            q.setParameter("province", province);
        }
        return q.getResultList();
    }
    
    public List<EcPartner> findByCriteria(PartnerFilter filter) {
        List<EcPartner> result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<EcPartner> cq = cb.createQuery(EcPartner.class);
        Root<EcPartner> root = cq.from(EcPartner.class);
        cq.select(root);

        List<Predicate> predicateList = new ArrayList();

        if (filter.getId() != null) {
            predicateList.add(cb.equal(root.get("id"), filter.getId()));
        }
        if (StringUtils.isNotEmpty(filter.getName())) {
            predicateList.add(cb.like(root.get("name").as(String.class), "%".concat(filter.getName()).concat("%")));
        }
        if (filter.getStatus() != null) {
            predicateList.add(cb.equal(root.get("status").as(PartnerStatusEnum.class), filter.getStatus()));
        }
        if (filter.getActive() != null) {
            predicateList.add(cb.equal(root.get("active").as(Boolean.class), filter.getActive()));
        }
        if (filter.getLoginAccount() != null) {
            predicateList.add(cb.like(root.get("owner").get("loginAccount").as(String.class), like(filter.getLoginAccount())));
        }
        if (filter.getProvince() != null) {
            predicateList.add(cb.like(root.get("province").as(String.class), like(filter.getProvince())));
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(cb.desc(root.get("createtime")));
        result = em.createQuery(cq).getResultList();
        return result;
    }

    public List<String> findOwners() {
        String sql = "SELECT DISTINCT e.owner.loginAccount FROM EcPartner e ORDER BY e.owner.loginAccount";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
    
    public List<String> findProvinces() {
        String sql = "SELECT DISTINCT e.province FROM EcPartner e ORDER BY e.province";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
    
    private static String like(String value) {
        return "%" + value + "%";
    }

}

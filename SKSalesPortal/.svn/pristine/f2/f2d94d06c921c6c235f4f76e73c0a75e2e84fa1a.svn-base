/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@Stateless
@DeclareRoles({"Administrators"})
public class SkSalesMemberFacade extends AbstractFacade<SkSalesMember> {

    Logger logger = LoggerFactory.getLogger(SkSalesMemberFacade.class);
    @EJB
    TcUserFacade userFacade;
    @EJB
    SkSalesChannelsFacade salesChannelsFacade;
    @Resource
    SessionContext ctx;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesMemberFacade() {
        super(SkSalesMember.class);
    }

    public SkSalesMember findByMember(TcUser member) {
        List<SkSalesMember> list = findByMembers(member);
        SkSalesMember sm = null;
        if (list != null && !list.isEmpty()) {
            sm = list.get(0);
        }
        return sm;
    }

    public List<SkSalesMember> findByTgroup(String code) {
        try {
            if (code != null) {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT s FROM SkSalesMember s ");
                sql.append("WHERE s.code like :code ");
                Query q = em.createQuery(sql.toString());
                q.setParameter("code", code + '%');
                return q.getResultList();
            }
        } catch (NoResultException ne) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SkSalesMember> findByMembers(TcUser member) {
        try {
            if (member != null) {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT s FROM SkSalesMember s ");
                sql.append("WHERE s.member = :member ");
                Query q = em.createQuery(sql.toString());
                q.setParameter("member", member);
                return q.getResultList();
            }
        } catch (NoResultException ne) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public SkSalesMember findByCode(String code) {
        try {
            if (!StringUtils.isEmpty(code)) {
                return (SkSalesMember) em.createNamedQuery("SkSalesMember.findByCode").setParameter("code", code).getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    private Long[] channelsArray(List<SkSalesChannels> list) {
        Long[] ids = null;
        if (list != null && !list.isEmpty()) {
            ids = new Long[list.size()];
            int index = 0;
            for (SkSalesChannels c : list) {
                ids[index++] = c.getId();
                logger.debug("channel id" + c.getId().toString());
            }
        }
        return ids;
    }

    public List<SkSalesMember> findAllSelectable() {
        return findAllSelectable(true);
    }

    public List<SkSalesMember> findAllSelectable(boolean includeDisableUser) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery(SkSalesMember.class);
        Root<SkSalesMember> root = cq.from(SkSalesMember.class);
        Path<Object> path = root.join("member", JoinType.LEFT);
        cq.where(root.get("selectable").as(Boolean.class).in(Boolean.TRUE));
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!includeDisableUser) {
            Predicate[] predicate = new Predicate[2];
            predicate[0] = builder.equal(path.get("disabled"), false);
            predicate[1] = builder.isNull(path);
            predicateList.add(builder.or(predicate));
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("code")));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<SkSalesMember> findByChannels() {
        TcUser sessionUser = userFacade.getSessionUser();
        return findByChannels(sessionUser);
    }

    public List<SkSalesMember> findByChannels(TcUser user) {
        return findByChannels(user, false);
    }

    public List<SkSalesMember> findByChannels(TcUser user, boolean disableUserOnly) {
        List<SkSalesMember> list = null;
        logger.debug("isRole Administrators" + ctx.isCallerInRole("Administrators"));
        if (ctx.isCallerInRole("Administrators")) {
            list = findAllSelectable();
        } else {
            List<SkSalesChannels> channelsList = salesChannelsFacade.findHierarchyChannels(user);
            Long[] ids = channelsArray(channelsList);
            if (ids != null) {
                CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
                CriteriaQuery<SkSalesMember> cq = builder.createQuery(SkSalesMember.class);
                Root<SkSalesMember> root = cq.from(SkSalesMember.class);
                Path<Object> path = root.join("member", JoinType.LEFT);
                Path<Object> channelMemberPath = root.join("skSalesChannelMemberCollection");
                cq.select(root);
                List<Predicate> predicateList = new ArrayList<Predicate>();
                predicateList.add(channelMemberPath.get("salesChannel").get("id").in((Object[]) ids));
                Predicate p = builder.equal(root.get("selectable"), true);
                predicateList.add(p);
                if (disableUserOnly) {
                    //channel.substitute = ture;

                    Predicate[] predicate = new Predicate[2];
                    predicate[0] = builder.equal(channelMemberPath.get("salesChannel").get("substitute"), true);
                    //List<Predicate> predicateList2 = new ArrayList<Predicate>();

                    //channel.substitute = false;
                    p = builder.equal(channelMemberPath.get("salesChannel").get("substitute"), false);
                    //predicateList2.add(p);

                    Predicate[] predicate2 = new Predicate[2];
                    predicate2[0] = builder.equal(path.get("disabled"), true);
                    predicate2[1] = builder.isNull(path);
                    //predicateList2.add(builder.or(predicate2));

                    predicate[1] = builder.and(p, builder.or(predicate2));
                    predicateList.add(builder.or(predicate));
                }

                if (predicateList.size() > 0) {
                    Predicate[] predicates = new Predicate[predicateList.size()];
                    predicateList.toArray(predicates);
                    cq.where(predicates);
                }

                cq.orderBy(builder.asc(root.get("code")));
                list = getEntityManager().createQuery(cq).getResultList();
            }
        }
        if (list == null) {
            list = new ArrayList<SkSalesMember>();
        }
        if (!ctx.isCallerInRole("Administrators")) {
            SkSalesMember sessionMember = findByMember(user);
            if (sessionMember != null && list.contains(sessionMember)) {
                list.remove(sessionMember);
            }
            if (sessionMember != null) {
                list.add(0, sessionMember);
            }
        }
        return list;
    }

    public List<SkSalesMember> findByChannels(SkSalesChannels channel) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery(SkSalesMember.class);
        Root<SkSalesMember> root = cq.from(SkSalesMember.class);
        cq.where(root.get("selectable").as(Boolean.class).in(Boolean.TRUE));
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (channel != null) {
            Predicate predicate = builder.equal(root.get("skSalesChannelMemberCollection").get("salesChannel"), channel);
            predicateList.add(predicate);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("code")));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<SkSalesMember> findByCriteria(String input) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = builder.createQuery(SkSalesMember.class);
        Root<SkSalesMember> root = cq.from(SkSalesMember.class);
        Join salesMember = root.join("member", JoinType.LEFT);

        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p1 = builder.like(root.get("code").as(String.class), "".concat(input).concat("%"));
        Predicate p2 = builder.like(salesMember.get("loginAccount").as(String.class), "".concat(input).concat("%"));
        Predicate p3 = builder.like(salesMember.get("cname").as(String.class), "%".concat(input).concat("%"));
        Predicate p4 = builder.like(salesMember.get("email").as(String.class), "".concat(input).concat("%"));
        predicateList.add(builder.or(p1, p2, p3, p4));
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.asc(root.get("code")));
        return getEntityManager().createQuery(cq).getResultList();
    }
}

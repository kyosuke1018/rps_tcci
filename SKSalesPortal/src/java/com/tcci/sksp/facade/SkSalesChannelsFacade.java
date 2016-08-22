/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.google.gson.Gson;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.org.SkSalesChannels;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@Stateless
public class SkSalesChannelsFacade extends AbstractFacade<SkSalesChannels> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkSalesChannelsFacade() {
        super(SkSalesChannels.class);
    }

    public List<SkSalesChannels> findHierarchyChannels(TcUser user) {

        StringBuilder sb = new StringBuilder();
        sb.append("select * from sk_sales_channels t ");
        sb.append("start with manager=? ");
        sb.append("connect by prior t.id=t.parent ");
        Query query = getEntityManager().createNativeQuery(sb.toString(), SkSalesChannels.class);
        query.setParameter(1, user.getId());
        return query.getResultList();
    }

    public SkSalesChannels findByManager(TcUser manager) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkSalesChannels.class);
        Root<SkSalesChannels> root = cq.from(SkSalesChannels.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = cb.equal(root.get("manager").as(TcUser.class), manager);
        predicateList.add(p);
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        List<SkSalesChannels> channels = getEntityManager().createQuery(cq).getResultList();
        if (channels != null && channels.size() > 0) {
            return channels.get(0);
        } else {
            return null;
        }
    }

    public List<SkSalesChannels> findRootChannel() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkSalesChannels.class);
        Root<SkSalesChannels> root = cq.from(SkSalesChannels.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = cb.isNull(root.get("parent"));
        predicateList.add(p);
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<SkSalesChannels> findChildChannel(SkSalesChannels channel) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkSalesChannels.class);
        Root<SkSalesChannels> root = cq.from(SkSalesChannels.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = cb.equal(root.get("parent"), channel);
        predicateList.add(p);
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(cb.asc(root.get("code")));
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    public SkSalesChannels findByCode(String code) {
        SkSalesChannels result = null;
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkSalesChannels.class);
        Root<SkSalesChannels> root = cq.from(SkSalesChannels.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = cb.equal(root.get("code"), code);
        predicateList.add(p);

        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        List<SkSalesChannels> channelsList = getEntityManager().createQuery(cq).getResultList();
        if (!channelsList.isEmpty()) {
            result = channelsList.get(0);
        }
        return result;
    }

    public void deleteChannel(SkSalesChannels channel) {
        List<SkSalesChannels> childChannelList = findChildChannel(channel);
        if (!childChannelList.isEmpty()) {
            for (SkSalesChannels childChannel : childChannelList) {
                deleteChannel(childChannel);
            }
        }
        SkSalesChannels parentChannel = channel.getParent();
        parentChannel.getSkSalesChannelsCollection().remove(channel);
        edit(parentChannel);

        channel.setParent(null);
        remove(channel);

        //TODO: 尚需要刪除　SkSalesChannelMember & SkSalesMember , 目前當 channel 有 childChannel or member 時, 不讓它被刪除!

    }
}

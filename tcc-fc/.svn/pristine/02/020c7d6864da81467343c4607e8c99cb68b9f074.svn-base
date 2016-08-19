/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.content;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.facade.AbstractFacade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class TcApplicationdataFacade extends AbstractFacade<TcApplicationdata> {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcApplicationdataFacade() {
        super(TcApplicationdata.class);
    }

    public List<TcApplicationdata> getByContentHolder(ContentHolder container, ContentRole contentRole) throws Exception {
        if (container == null) {
            throw new Exception("container is null!");
        }
        String containerClassname = container.getClass().getCanonicalName();
        Long containerId = container.getId();
        logger.info("containerId=" + containerId);
        System.out.println("containerId=" + containerId);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TcApplicationdata> cq = cb.createQuery(TcApplicationdata.class);
        Root<TcApplicationdata> from = cq.from(TcApplicationdata.class);

        List<Predicate> predicateList = new ArrayList<Predicate>();

        Predicate p1, p2, p3;
        p1 = cb.equal(from.<String>get("containerclassname"), containerClassname);
        predicateList.add(p1);
        p2 = cb.equal(from.<String>get("containerid"), containerId);
        predicateList.add(p2);
        p3 = cb.equal(from.<Character>get("contentrole"), contentRole.toCharacter());
        predicateList.add(p3);

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        cq.where(predicates);
        return em.createQuery(cq).getResultList();
    }

    public List<TcApplicationdata> getByContentHolder(ContentHolder container) throws Exception {
        List<TcApplicationdata> attachmentList = new ArrayList<TcApplicationdata>();
        List<TcApplicationdata> applicationdatas;

        applicationdatas = getByContentHolder(container, ContentRole.PRIMARY);
        if (applicationdatas != null) {
            attachmentList.addAll(applicationdatas);
        }
        applicationdatas = getByContentHolder(container, ContentRole.SECONDARY);
        if (applicationdatas != null) {
            attachmentList.addAll(applicationdatas);
        }
        return attachmentList;
    }
}

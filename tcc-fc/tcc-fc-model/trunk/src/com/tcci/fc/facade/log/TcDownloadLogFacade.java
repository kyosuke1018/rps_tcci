/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.log;

import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.log.TcDownloadLog;
import com.tcci.fc.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author nEO.Fu
 */
@Stateless
@Named
public class TcDownloadLogFacade extends AbstractFacade<TcDownloadLog> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcDownloadLogFacade() {
        super(TcDownloadLog.class);
    }

    public void save(TcDownloadLog downloadLog) {
        if (null == downloadLog.getId()) {
            create(downloadLog);
        } else {
            edit(downloadLog);
        }
    }

    public TcDownloadLog findByApplicationdata(TcApplicationdata applicationdata) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(TcDownloadLog.class);
        Root root = cq.from(TcDownloadLog.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (applicationdata != null) {
            Predicate p1 = cb.equal(root.get("applicationdata").as(TcApplicationdata.class), applicationdata);
            predicateList.add(p1);
        }

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        List<TcDownloadLog> downloadLogList = getEntityManager().createQuery(cq).getResultList();
        TcDownloadLog downloadLog = null;
        if (downloadLogList != null && !downloadLogList.isEmpty()) {
            downloadLog = downloadLogList.get(0);
        }
        return downloadLog;
    }
}

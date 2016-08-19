package com.tcci.fc.facade.vc;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.vc.Iterated;
import com.tcci.fc.entity.vc.Mastered;
import com.tcci.fc.entity.vc.Versioned;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.util.SequenceGenerator;
import com.tcci.fc.util.SequenceGeneratorFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Named
public class VersionControlFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @Resource
    SessionContext sessionContext;
    @EJB
    private ContentFacade applicationdataFacade;
    @EJB
    TcUserFacade userFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Iterated> findAllIterations(Versioned versioned) {
        String tableName = versioned.getClass().getName().substring(versioned.getClass().getName().lastIndexOf(".") + 1);
        String sql = "select object(o) from "
                + tableName
                + " o where o.versionnumber = :versionnumber";
        System.out.println("sql=" + sql);
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("versionnumber", versioned.getVersionnumber());
        return q.getResultList();
    }

    public Versioned newVersion(Versioned versioned) {
        Versioned newVersoned = null;
        try {

            SequenceGeneratorFactory sgf = new SequenceGeneratorFactory(versioned);
            SequenceGenerator sg = sgf.createSequenceGenerator();
            TcUser creator = userFacade.findUserByLoginAccount(sessionContext.getCallerPrincipal().getName());
            Mastered mastered = (Mastered) versioned.getMaster();

            newVersoned = (Versioned) versioned.clone();
            newVersoned.setId(null);
            newVersoned.setMaster(mastered);
            newVersoned.setCreator(creator);
            newVersoned.setCreatetimestamp(new Date());
            newVersoned.setModifier(null);
            newVersoned.setModifytimestamp(null);
            newVersoned.setIterationnumber(sg.getStartValue());
            newVersoned.setIslatestiteration(Boolean.TRUE);
            newVersoned.setVersionnumber(sg.getNextValue(versioned.getVersionnumber()));

            List<Iterated> allIterations = findAllIterations(versioned);
            for (Iterated iterated : allIterations) {
                System.out.println("iterated=" + iterated);
                ((Versioned) iterated).setIslatestversion(Boolean.FALSE);
                edit(iterated);
            }
//            dispatchVetoableEvent(VersionControlEvent.NEW_VERSION, newVersoned);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newVersoned;
    }

    public Iterated newIteration(Iterated iterated) {
        Iterated newIterated = null;
        try {

            SequenceGeneratorFactory sgf = new SequenceGeneratorFactory(iterated);
            SequenceGenerator sg = sgf.createSequenceGenerator();
            TcUser creator = userFacade.findUserByLoginAccount(sessionContext.getCallerPrincipal().getName());
            newIterated = iterated.clone();

            Mastered mastered = (Mastered) iterated.getMaster();
            newIterated.setId(null);
            newIterated.setMaster(mastered);
            newIterated.setCreator(creator);
            newIterated.setCreatetimestamp(new Date());
            newIterated.setModifier(null);
            newIterated.setModifytimestamp(null);
            newIterated.setIterationnumber(sg.getNextValue(iterated.getIterationnumber()));
            newIterated.setIslatestiteration(Boolean.TRUE);

            iterated = em.find(iterated.getClass(), iterated.getId());
            iterated.setIslatestiteration(Boolean.FALSE);
            edit(iterated);

//            dispatchVetoableEvent(IterationControlEvent.NEW_ITERATION, newIteration);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newIterated;
    }

    public void edit(Iterated entity) {
        getEntityManager().merge(entity);
    }
}

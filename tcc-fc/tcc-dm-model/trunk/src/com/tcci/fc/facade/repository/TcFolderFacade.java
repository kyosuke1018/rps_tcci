/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.repository;

import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
@Named
public class TcFolderFacade extends AbstractFacade<TcFolder> {

    @PersistenceContext(unitName="Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcFolderFacade() {
        super(TcFolder.class);
    }
    
    public TcFolder findByName(String name){
        TcFolder folder = null;

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<TcFolder> cq = builder.createQuery(TcFolder.class);
        Root<TcFolder> root = cq.from(TcFolder.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = builder.equal(root.get("name"), name);
        predicateList.add(p);
        
        if( predicateList.size() > 0){
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        List<TcFolder> list = getEntityManager().createQuery(cq).getResultList();
        if( list != null && !list.isEmpty() ){
            folder = list.get(0);
        }
        return folder;
    }
    
    /**
     * A function to find all parent folders by hierarchy.
     * @param folder sub folder
     * @return parent folders.
     */
    public List<TcFolder> findHierarchyFolders(TcFolder folder){
        List<TcFolder> list = null;
        Query query = getEntityManager().createNativeQuery("SELECT * FROM tc_folder t CONNECT by prior t.folder=t.id START WITH t.id= ?",TcFolder.class);
        query.setParameter(1, folder.getId());
        list = query.getResultList();
        return list;
    }
}

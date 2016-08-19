/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.usermgmt;

import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fcs.entity.FcUserCompGroupR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class UsermgmtFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;

    /************************
     * TcUser Services:
     * save
     * findUserByLoginAccount
     * findAllUser
     * findUserByFilter
     ***********************/

    public void save(TcUser entity) {
        if (entity.getId() == null) {em.persist(entity);}
        else { em.merge(entity); }
    }

    public void save(TcUser user, List<TcGroup> listGroupNew, TcUser operator, List<String> listCompGroupNew) {
        Collection<TcUsergroup> ugColl = user.getTcUsergroupCollection();
        if (ugColl == null) {
            ugColl = new ArrayList<TcUsergroup>();
            user.setTcUsergroupCollection(ugColl);
        }
        // remove unselected group
        ArrayList<TcUsergroup> removedUG = new ArrayList<TcUsergroup>();
        for (TcUsergroup ugOld : ugColl) {
            boolean existed = false;
            for (TcGroup groupNew : listGroupNew) {
                if (groupNew.equals(ugOld.getGroupId())) {
                    existed = true;
                    listGroupNew.remove(groupNew);
                    break;
                }
            }
            if (!existed) {
                removedUG.add(ugOld);
            }
        }
        for (TcUsergroup ug : removedUG) {
            ugColl.remove(ug);
            em.remove(em.merge(ug));
        }
        // add new group
        Date now = new Date();
        for (TcGroup groupNew : listGroupNew) {
            TcUsergroup newUG = new TcUsergroup();
            newUG.setCreator(operator);
            newUG.setCreatetimestamp(now);
            newUG.setUserId(user);
            newUG.setGroupId(groupNew);
            em.persist(newUG);
            ugColl.add(newUG);
        }
        
        //20151119 增加多選公司群組
        List<FcUserCompGroupR> cgList = user.getCompGroupList();
        if (cgList == null) {
            cgList = new ArrayList<>();
            user.setCompGroupList(cgList);
        }
        // remove unselected compGroup
        List<FcUserCompGroupR> removedCG = new ArrayList<>();
        for (FcUserCompGroupR cgOld : cgList) {
            boolean existed = false;
            for (String cgNew : listCompGroupNew) {
                if (cgNew.equals(cgOld.getGroup().getCode())) {
                    existed = true;
                    listCompGroupNew.remove(cgNew);
                    break;
                }
            }
            if (!existed) {
                removedCG.add(cgOld);
            }
        }
        for (FcUserCompGroupR cg : removedCG) {
            cgList.remove(cg);
            em.remove(em.merge(cg));
        }
        // add new group
        for (String cgNew : listCompGroupNew) {
            FcUserCompGroupR newCG = new FcUserCompGroupR();
            newCG.setCreatetimestamp(now);
            newCG.setTcUser(user);
            newCG.setGroup(CompanyGroupEnum.getFromCode(cgNew));
            em.persist(newCG);
            cgList.add(newCG);
        }
        
        save(user);
    }

    public TcUser findUserByLoginAccount(String loginAccount) {
        Query q = em.createQuery("SELECT u FROM TcUser u WHERE u.loginAccount=:loginAccount");
        q.setParameter("loginAccount", loginAccount);
        List<TcUser> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<TcUser> findAllUser() {
        return em.createQuery("SELECT u FROM TcUser u").getResultList();
    }

    public List<TcUser> findUserByFilter(QueryFilter filter) {
        StringBuilder sql;
        String userTable;
        String loginAccount = StringUtils.lowerCase(filter.getLoginAccount());
        String empId = StringUtils.lowerCase(filter.getEmpId());
        String email = StringUtils.lowerCase(filter.getEmail());
        String cname = StringUtils.lowerCase(filter.getCname());
        if (filter.getGroupId() == null) {
            sql = new StringBuilder("SELECT u FROM TcUser u WHERE 1=1");
            userTable = "u";
        } else {
            sql = new StringBuilder("SELECT ug.userId FROM TcUsergroup ug WHERE ug.groupId.id=");
            sql.append(filter.getGroupId());
            userTable = "ug.userId";
        }
        // addLikeSql(sql, table, field, param, value)
        // -> AND LOWER(table.field) LIKE :param)
        addLikeSql(sql, userTable, "loginAccount", "loginAccount", loginAccount);
        addLikeSql(sql, userTable, "empId", "empId", empId);
        addLikeSql(sql, userTable, "email", "email", email);
        addLikeSql(sql, userTable, "cname", "cname", cname);
        sql.append(" ORDER BY ").append(userTable).append(".loginAccount");
        Query q = em.createQuery(sql.toString());
        addlikeParam(q, "loginAccount", loginAccount);
        addlikeParam(q, "empId", empId);
        addlikeParam(q, "email", email);
        addlikeParam(q, "cname", cname);
        return q.getResultList();
    }

    /************************
     * TcGroup Services:
     * save
     * findGroup
     * findAllGroup
     ***********************/

    public void save(TcGroup entity) {
        if (entity.getId() == null) {em.persist(entity);}
        else { em.merge(entity); }
    }

    public TcGroup findGroup(String code) {
        Query q = em.createQuery("SELECT g FROM TcGroup g WHERE g.code=:code");
        q.setParameter("code", code);
        List<TcGroup> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<TcGroup> findAllGroup() {
        return em.createQuery("SELECT g FROM TcGroup g").getResultList();
    }
    
    /************************
     * TcUsergroup Services:
     * save
     * findUsergroup
     * findAllUsergroup
     ***********************/

    public void save(TcUsergroup entity) {
        if (entity.getId() == null) {
            em.persist(entity);
            TcUser user = entity.getUserId();
            TcGroup group = entity.getGroupId();
            user.getTcUsergroupCollection().add(entity);
            group.getTcUsergroupCollection().add(entity);
            em.merge(user);
            em.merge(group);
        } else {
            em.merge(entity); 
        }
    }

    public TcUsergroup findUsergroup(TcUser tcUser, TcGroup tcGroup) {
        String sql = "SELECT o FROM TcUsergroup o WHERE o.userId=:tcUser AND o.groupId=:tcGroup";
        Query q = em.createQuery(sql);
        q.setParameter("tcUser", tcUser);
        q.setParameter("tcGroup", tcGroup);
        List<TcUsergroup> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<TcUsergroup> findAllUsergroup() {
        return em.createQuery("SELECT o FROM TcUsergroup o").getResultList();
    }

    /*
     * helper
     */

    private static void addLikeSql(StringBuilder sql, String table, String field, String param, String value) {
        if (!StringUtils.isBlank(value)) {
            sql.append(" AND LOWER(").append(table).append('.').append(field).append(") LIKE :").append(param);
        }
    }
    
    private static void addlikeParam(Query q, String param, String value) {
        if (!StringUtils.isBlank(value)) {
            String likeValue = "%" + value +"%";
            q.setParameter(param, likeValue);
        }
    }

}

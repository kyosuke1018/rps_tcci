/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.facade.admin;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcGroupFacade;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter
 */
@Stateless
@Named
public class UserGroupFacade extends AbstractFacade<TcGroup> {
    @EJB TcGroupFacade tcGroupFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public UserGroupFacade() {
        super(TcGroup.class);
    }

    @Override
    public List<TcGroup> findAll() {
        List<TcGroup> tgList = super.findAll();
        Collections.sort(tgList, new Comparator<TcGroup>() {
            public int compare(TcGroup p1, TcGroup p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });
        return tgList; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    /**
     * 新增 user 預設為 PLANT_USER
     * @param user
     * @param operator
     */
    public void defPlantUser(TcUser user, TcUser operator){
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        sql.append("    insert into tc_usergroup (id, user_id, group_id, creator, createtimestamp) \n");
        sql.append("    select SEQ_TCC.NEXTVAL, #user_id, id, #creator, sysdate \n");
        sql.append("    from tc_group where code = #PLANT_USER; \n");
        sql.append("END;");
        
        logger.debug("defPlantUser sql = \n"+sql.toString());
        
        Query q = em.createNativeQuery(sql.toString());
        
        q.setParameter("user_id", user.getId());
        q.setParameter("creator", operator.getId());
        q.setParameter("PLANT_USER", GlobalConstant.UG_PLANT_USER);
        
        q.executeUpdate();
    }
    
    /**
     * 
     * @param groupCode
     * @param groupname
     * @param loginUser 
     */
    public void createNotExists(String groupCode, String groupname, TcUser loginUser){
        TcGroup tcGroup = tcGroupFacade.findGroupByCode(groupCode);
        if( tcGroup==null ){
            tcGroup = new TcGroup();
            tcGroup.setCode(groupCode);
            tcGroup.setName(groupname);
            tcGroup.setCreatetimestamp(new Date());
            tcGroup.setCreator(loginUser);
            
            tcGroupFacade.create(tcGroup);
        }
        
    }

    /**
     * 回饋權限複製SQL
     * @param perfix
     * @param copySrc
     * @param copyDesc
     * @return 
     */
    public String copyPermissionStr(String perfix, String copySrc, String copyDesc){
        StringBuilder sql = new StringBuilder();
        
        sql.append("    insert into tc_usergroup (id, user_id, group_id, creator, createtimestamp) \n");
        sql.append("    select SEQ_TCC.NEXTVAL, b.user_id, n.id, #creator, sysdate \n");
        sql.append("    from tc_user a \n");
        sql.append("    join tc_group n on n.code='").append(perfix).append(copyDesc).append("' \n");
        sql.append("    join tc_usergroup b on b.user_id=a.id \n");
        sql.append("    join tc_group c on c.id=b.group_id and c.code='").append(perfix).append(copySrc).append("' \n");
        sql.append("    where not exists (select * from tc_usergroup where user_id=b.user_id and group_id=n.id); \n");
        sql.append("\n");
        sql.append("    insert into cm_user_factorygroup_r (id, user_id, factorygroup_id, usergroup_id, creator, createtimestamp) \n");
        sql.append("    select SEQ_TCC.NEXTVAL, d.user_id, d.factorygroup_id, n.id, #creator, sysdate \n");
        sql.append("    from tc_user a \n");
        sql.append("    join tc_group n on n.code='").append(perfix).append(copyDesc).append("' \n");
        sql.append("    join tc_usergroup b on b.user_id=a.id \n");
        sql.append("    join tc_group c on c.id=b.group_id and c.code='").append(perfix).append(copySrc).append("' \n");
        sql.append("    join cm_user_factorygroup_r d on d.user_id=a.id and d.usergroup_id=c.id \n");
        sql.append("    where not exists (select * from cm_user_factorygroup_r where user_id=d.user_id and factorygroup_id=d.factorygroup_id and usergroup_id=n.id); \n");
        
        return sql.toString();
    }
}

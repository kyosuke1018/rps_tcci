package com.tcci.sksp.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroup;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroupMember;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroupUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Neo.Fu
 */
@Stateless
public class SkQuotationMailGroupFacade extends AbstractFacade<SkQuotationMailGroup> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    private SkQuotationMailGroupUserFacade userFacade;
    @EJB
    private SkQuotationMailGroupMemberFacade memberFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SkQuotationMailGroupFacade() {
        super(SkQuotationMailGroup.class);
    }

    public SkQuotationMailGroup save(SkQuotationMailGroup group, List<SkSalesMember> memberList, List<TcUser> userList, TcUser user) {
        Date now = new Date();
        processMember(group, memberList);
        processUser(group, userList);
        group.setModifier(user);
        group.setModifytimestamp(now);
        if (group.getId() != null) {
            edit(group);
        } else {
            group.setCreator(user);
            group.setCreatetimestamp(now);
            create(group);
        }
        return em.merge(group);
    }

    private void processMember(SkQuotationMailGroup group, List<SkSalesMember> memberList) {
        List<SkQuotationMailGroupMember> removeGroupMemberList = new ArrayList();
        List<SkSalesMember> existsMemberList = new ArrayList();
        for (SkQuotationMailGroupMember existsGroupMember : group.getMemberCollection()) {
            if (!memberList.contains(existsGroupMember.getMember())) {
                removeGroupMemberList.add(existsGroupMember);
            }
            existsMemberList.add(existsGroupMember.getMember());
        }

        //移除不存在的
        group.getMemberCollection().removeAll(removeGroupMemberList);
        for (SkQuotationMailGroupMember removeGroupMember : removeGroupMemberList) {
            memberFacade.remove(removeGroupMember);
        }
        //增加新的
        memberList.removeAll(existsMemberList);
        for (SkSalesMember newMember : memberList) {
            SkQuotationMailGroupMember newGroupMember = new SkQuotationMailGroupMember();
            newGroupMember.setGroup(group);
            newGroupMember.setMember(newMember);
            group.getMemberCollection().add(newGroupMember);
        }
    }

    private void processUser(SkQuotationMailGroup group, List<TcUser> userList) {
        List<SkQuotationMailGroupUser> removeGroupUserList = new ArrayList();
        List<TcUser> existsUserList = new ArrayList();
        for (SkQuotationMailGroupUser existsGroupUser : group.getUserCollection()) {
            if (!userList.contains(existsGroupUser.getUser())) {
                removeGroupUserList.add(existsGroupUser);
            }
            existsUserList.add(existsGroupUser.getUser());
        }

        //移除不存在的
        group.getUserCollection().removeAll(removeGroupUserList);
        for (SkQuotationMailGroupUser removeGroupUser : removeGroupUserList) {
            userFacade.remove(removeGroupUser);
        }
        //增加新的
        userList.removeAll(existsUserList);
        for (TcUser newUser : userList) {
            SkQuotationMailGroupUser newGroupUser = new SkQuotationMailGroupUser();
            newGroupUser.setGroup(group);
            newGroupUser.setUser(newUser);
            group.getUserCollection().add(newGroupUser);
        }
    }
    
    public List<SkQuotationMailGroup> findByMember(SkSalesMember member) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkQuotationMailGroup.class);
        Root<SkQuotationMailGroup> root = cq.from(SkQuotationMailGroup.class);
        Join memberList = root.join("memberCollection");
        List<Predicate> predicateList = new ArrayList();
        
        Predicate p1 = cb.equal(memberList.get("member").as(SkSalesMember.class),member);
        predicateList.add(p1);
        
        if (!predicateList.isEmpty()) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        return em.createQuery(cq).getResultList();
    }
}

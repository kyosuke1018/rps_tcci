/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.member;

import com.tcci.ec.entity.EcFavoritePrd;
import com.tcci.ec.entity.EcFavoriteStore;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.vo.Member;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcMemberFacade extends AbstractFacade<EcMember> {
    private final static Logger logger = LoggerFactory.getLogger(EcMemberFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcMemberFacade() {
        super(EcMember.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcMember entity) {
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcMember> findAllActive() {
        return em.createNamedQuery("EcMember.findAllActive").getResultList();
    }
//    public List<EcMember> findAllActive() {
//        String sql = "SELECT e FROM EcMember e WHERE e.active=TRUE";
//        Query q = em.createQuery(sql);
//        
//        return q.getResultList();
//    }

    public EcMember findActiveByLoginAccount(String loginAccount) {
        Query q = em.createNamedQuery("EcMember.findActiveByLoginAccount");
        q.setParameter("loginAccount", loginAccount);
        List<EcMember> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public EcMember findByLoginAccount(String loginAccount) {
        Query q = em.createNamedQuery("EcMember.findByLoginAccount");
        q.setParameter("loginAccount", loginAccount);
        List<EcMember> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    
    public void resetPassword(EcMember ecMember, String oldPassword, String newPassword)
            throws PasswordWrongException {
        if (!StringUtils.equals(ecMember.getPassword(), DigestUtils.sha256Hex(oldPassword))) {
//            logger.debug("sha256Hex oldPassword = "+DigestUtils.sha256Hex(oldPassword));
            throw new PasswordWrongException("old password not matched!");
        }
        ecMember.setPassword(DigestUtils.sha256Hex(newPassword));
        ecMember.setModifytime(new Date());
        em.merge(ecMember);
    }

    public String forgotPassword(String account) throws AccountNotExistException {
        if (account != null) {
            account = account.toLowerCase().trim();
        }
        EcMember ecMember = findActiveByLoginAccount(account);
        if (null == ecMember) {
            throw new AccountNotExistException("account not exist or disabled!");
        }
        String newPassword = RandomStringUtils.randomAlphanumeric(6);
        ecMember.setPassword(DigestUtils.sha256Hex(newPassword));
        ecMember.setModifytime(new Date());
        em.merge(ecMember);
        return newPassword;
    }
    
    public EcMember createNewMember(String loginAccount, String name) {
        EcMember newEcMember = new EcMember();
        newEcMember.setLoginAccount(loginAccount);
        newEcMember.setName(name);
        newEcMember.setActive(true);
        create(newEcMember);
        return newEcMember;
    }
    
    public EcMember registerMember(String name, String email, String phone, String loginAccount, String password) {
        EcMember newEcMember = new EcMember();
        newEcMember.setName(name);
        newEcMember.setEmail(email);
        newEcMember.setPhone(phone);
        newEcMember.setLoginAccount(loginAccount);
        newEcMember.setPassword(DigestUtils.sha256Hex(password));
        newEcMember.setActive(true);
        newEcMember.setCreatetime(new Date());
        newEcMember.setModifytime(new Date());
        create(newEcMember);
        return newEcMember;
    }
    
    public EcMember registerMember(Member member) {
        EcMember newEcMember = new EcMember();
        newEcMember.setName(member.getName());
        newEcMember.setEmail(member.getEmail());
        newEcMember.setPhone(member.getPhone());
        newEcMember.setLoginAccount(member.getLoginAccount());
        newEcMember.setPassword(DigestUtils.sha256Hex(member.getPassword()));
        newEcMember.setActive(true);
        newEcMember.setCreatetime(new Date());
        newEcMember.setModifytime(new Date());
        create(newEcMember);
        return newEcMember;
    }
    
//    public List<EcFavoritePrd> findFavoritePrd(EcMember member) {
//        Query q = em.createNamedQuery("EcFavoritePrd.findByMember");
//        q.setParameter("member", member);
//        List<EcFavoritePrd> list = q.getResultList();
//        return list;
//    }
//    
//    public List<EcFavoriteStore> findFavoriteStore(EcMember member) {
//        Query q = em.createNamedQuery("EcFavoriteStore.findByMember");
//        q.setParameter("member", member);
//        List<EcFavoriteStore> list = q.getResultList();
//        return list;
//    }
}

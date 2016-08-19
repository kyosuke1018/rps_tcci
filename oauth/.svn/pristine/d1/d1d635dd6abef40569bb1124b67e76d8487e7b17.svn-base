/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.member;

import com.tcci.cas.oauth.profile.BaseProfile;
import com.tcci.cas.oauth.profile.QqAttributesDefinition;
import com.tcci.cas.oauth.profile.WeiboAttributesDefinition;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcLoginLog;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcMemberCustomer;
import com.tcci.tccstore.entity.EcMemberCustomerPK;
import com.tcci.tccstore.entity.EcMemberPartner;
import com.tcci.tccstore.entity.EcMemberPartnerPK;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class EcMemberFacade extends AbstractFacade<EcMember> {

    private final static Logger logger = LoggerFactory.getLogger(EcMemberFacade.class);
    private static final String CONST_CAS_OAUTH_WEIBO_PROFILE = "weiboprofile#";
    private static final String CONST_CAS_OAUTH_QQ_PROFILE = "qqprofile#";

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

    public boolean isMemberCusomerExist(EcMember ecMember, EcCustomer ecCustomer) {
        EcMemberCustomerPK pk = new EcMemberCustomerPK(ecMember.getId(), ecCustomer.getId());
        EcMemberCustomer entity = em.find(EcMemberCustomer.class, pk);
        return entity != null;
    }

    public void insertMemberCustomer(EcMember ecMember, EcCustomer ecCustomer) {
        EcMemberCustomerPK pk = new EcMemberCustomerPK(ecMember.getId(), ecCustomer.getId());
        EcMemberCustomer entity = new EcMemberCustomer(pk);
        entity.setEcMember(ecMember);
        entity.setEcCustomer(ecCustomer);
        em.persist(entity);
    }

    public void resetPassword(EcMember ecMember, String oldPassword, String newPassword)
            throws PasswordWrongException {
        if (!StringUtils.equals(ecMember.getPassword(), DigestUtils.sha256Hex(oldPassword))) {
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

    public boolean isMemberPartnerExist(EcMember ecMember, EcPartner ecPartner) {
        EcMemberPartnerPK pk = new EcMemberPartnerPK(ecMember.getId(), ecPartner.getId());
        EcMemberPartner entity = em.find(EcMemberPartner.class, pk);
        return entity != null;
    }

    public void updateMemberCustomer(EcMember ecMember, List<EcCustomer> ecCustomers) {
        if (null == ecCustomers) {
            ecCustomers = new ArrayList<>();
        }
        Query q = em.createNamedQuery("EcMemberCustomer.findByMember");
        q.setParameter("ecMember", ecMember);
        List<EcMemberCustomer> origList = q.getResultList();
        List<EcCustomer> insertList = new ArrayList<>(ecCustomers);
        for (EcMemberCustomer mc : origList) {
            EcCustomer ecCustomer = mc.getEcCustomer();
            if (insertList.contains(ecCustomer)) {
                insertList.remove(ecCustomer);
            } else {
                logger.warn("remove member({}):customer({})", ecMember.getId(), ecCustomer.getId());
                em.remove(mc);
            }
        }
        for (EcCustomer ecCustomer : insertList) {
            logger.warn("insert member({}):customer({})", ecMember.getId(), ecCustomer.getId());
            EcMemberCustomer mc = new EcMemberCustomer(ecMember, ecCustomer);
            em.persist(mc);
        }
    }

    public void addLoginLog(EcMember ecMember) {
        EcLoginLog loginLog = new EcLoginLog(ecMember);
        em.persist(loginLog);
    }

    public List<EcMember> findYesterdayLogin() {
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        now.add(Calendar.DATE, -1);
        Date yesterday = now.getTime();
        String sql = "SELECT DISTINCT e.ecMember FROM EcLoginLog e WHERE e.loginTime>=:yesterday AND e.loginTime<:today";
        Query q = em.createQuery(sql);
        q.setParameter("yesterday", yesterday, TemporalType.DATE);
        q.setParameter("today", today, TemporalType.DATE);
        return q.getResultList();
    }

    public EcMember createNewMember(String loginAccount, BaseProfile profile) {
        EcMember newEcMember = new EcMember();
        newEcMember.setLoginAccount(loginAccount);
        String name = "";
        if (loginAccount.startsWith(CONST_CAS_OAUTH_WEIBO_PROFILE)) {
            name = (String) profile.getAttribute(WeiboAttributesDefinition.SCREEN_NAME);
        } else if (loginAccount.startsWith(CONST_CAS_OAUTH_QQ_PROFILE)) {
            name = (String) profile.getAttribute(QqAttributesDefinition.NICKNAME);
        }
        newEcMember.setName(name);
        newEcMember.setActive(true);
        create(newEcMember);
        return newEcMember;
    }

}

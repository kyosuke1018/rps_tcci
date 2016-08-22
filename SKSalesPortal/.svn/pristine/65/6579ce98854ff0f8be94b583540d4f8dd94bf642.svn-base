package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroupMember;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Neo.Fu
 */
@Stateless
public class SkQuotationMailGroupMemberFacade extends AbstractFacade<SkQuotationMailGroupMember> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkQuotationMailGroupMemberFacade() {
        super(SkQuotationMailGroupMember.class);
    }
}

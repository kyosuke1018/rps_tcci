/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkAdvancePayment;
import com.tcci.sksp.entity.ar.SkAdvanceRemit;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.ar.SkCheckMaster;
import com.tcci.sksp.entity.ar.SkFiDetailInterface;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;
import com.tcci.sksp.entity.ar.SkPremiumDiscount;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkFiMasterInterfaceFacade extends AbstractFacade<SkFiMasterInterface> {

    private Logger logger = LoggerFactory.getLogger(SkFiMasterInterfaceFacade.class);
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @EJB
    SkFiDetailInterfaceFacade detailFacade;
    @EJB
    SkAdvanceRemitFacade advanceRemitFacade;
    @EJB
    SkArRemitMasterFacade remitMasterFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkFiMasterInterfaceFacade() {
        super(SkFiMasterInterface.class);
    }

    public SkFiMasterInterface editAndReturn(SkFiMasterInterface skFiMasterInterface) {
        if (skFiMasterInterface.getId() != null) {
            skFiMasterInterface = getEntityManager().merge(skFiMasterInterface);
        }
        super.edit(skFiMasterInterface);
        return getEntityManager().merge(skFiMasterInterface);
    }

    public SkFiMasterInterface findByReferenceObject(Persistable persistable) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(SkFiMasterInterface.class);
        Root<SkFiMasterInterface> root = cq.from(SkFiMasterInterface.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        logger.debug("persistable.getClass().getCanonicalName()={}", persistable.getClass().getCanonicalName());
        Predicate p1 = cb.equal(root.get("referenceclassname"), persistable.getClass().getCanonicalName());
        predicateList.add(p1);
        Predicate p2 = cb.equal(root.get("referenceid"), persistable.getId());
        predicateList.add(p2);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);
        cq.where(predicates);
        List<SkFiMasterInterface> masterInterfaces = getEntityManager().createQuery(cq).getResultList();
        logger.debug("masterInterfaces.size()={}", masterInterfaces.size());
        if (masterInterfaces.isEmpty()) {
            return null;
        } else {
            return masterInterfaces.get(0);
        }
    }

    /**
     * @param advanceRemits 因為必須動態產生 (扣除其餘繳款單已耗用),
     * 已透過AdvanceRemitFacade.sortAdvanceRemit排序, 且分為自己的預收款 (advanceRemitA or
     * advanceRemitJ) 或 parent的預收款 (groupAdvanceRemitA or groupAdvanceRemitJ)
     */
    public SkFiMasterInterface newSkFiMasterInterface(SkArRemitMaster remitMaster, HashMap<String, List<SkAdvanceRemit>> advanceRemits) throws Exception {
        int transactionItemSeq = 1;
        SkFiMasterInterface master = new SkFiMasterInterface(remitMaster.getClass().getCanonicalName(), remitMaster.getId());
        List<SkFiDetailInterface> details = new ArrayList<SkFiDetailInterface>();
        List<SkArRemitItem> negativeArItems = getSortedNegativeArItems(remitMaster);
        for (SkArRemitItem item : remitMaster.getSkArRemitItemCollection()) {
            if (item.getAmountSum() != null && item.getAmountSum().doubleValue() >= 0) {
                logger.debug("transactionItemSeq={}", transactionItemSeq);
                details.addAll(detailFacade.newSkFiDetailInterfaces(item, master, transactionItemSeq, advanceRemits, negativeArItems));
                //每筆繳款單項目會產生借貸兩筆SkFiDetailImterface.
                if (item.getAmount() != null && item.getAmount().doubleValue() > 0) {
                    transactionItemSeq += 2;
                }
                if (item.getAmount2() != null && item.getAmount2().doubleValue() > 0) {
                    transactionItemSeq += 2;
                }
                //有 -AR 則會再產生 1+n 筆 SkFiDetailInterface.
                if (item.getNegativeAr() != null && item.getNegativeAr().doubleValue() > 0) {
                    //貸方只有 1 筆.
                    logger.debug("-ar + 1");
                    transactionItemSeq += 1;
                    //借方 n 筆.
                    List<SkArRemitItem> sortedNegativeArItems = getSortedNegativeArItems(remitMaster);
                    BigDecimal totalNegativeAr = item.getNegativeAr();
                    for (SkArRemitItem negativeArItem : sortedNegativeArItems) {
                        logger.debug("totalNegativeAr={}", totalNegativeAr);
                        logger.debug("-{}", negativeArItem.getArAmount());
                        totalNegativeAr = totalNegativeAr.add(negativeArItem.getArAmount());
                        transactionItemSeq += 1;
                        logger.debug("-ar + 1");
                        if (totalNegativeAr.doubleValue() <= 0) {
                            break;
                        }
                    }
                }
                //有預收A則會再產生 n+1 筆 SkFiDetailInterface.
                if (item.getAdvanceReceiptsA() != null && item.getAdvanceReceiptsA().doubleValue() > 0) {
                    //貸方只有 1 筆.
                    transactionItemSeq += 1;
                    double totalAdvanceReceiptsA = item.getAdvanceReceiptsA().doubleValue();
                    //借方 n 筆
                    List<SkAdvanceRemit> customerAdvanceRemitAs = advanceRemits.get("advanceRemitA");
                    for (SkAdvanceRemit customerAdvanceRemitA : customerAdvanceRemitAs) {
                        totalAdvanceReceiptsA -= customerAdvanceRemitA.getAmount().abs().doubleValue();
                        transactionItemSeq++;
                        if (totalAdvanceReceiptsA <= 0) {
                            break;
                        }
                    }
                    if (totalAdvanceReceiptsA > 0) {
                        List<SkAdvanceRemit> groupAdvanceRemitAs = advanceRemits.get("groupAdvanceRemitA");
                        for (SkAdvanceRemit groupAdvanceRemitA : groupAdvanceRemitAs) {
                            totalAdvanceReceiptsA -= groupAdvanceRemitA.getAmount().abs().doubleValue();
                            transactionItemSeq++;
                            if (totalAdvanceReceiptsA <= 0) {
                                break;
                            }
                        }
                    }
                }
                //有預收J則會再產生 n+1 筆 SkFiDetailInterface.
                if (item.getAdvanceReceiptsJ() != null && item.getAdvanceReceiptsJ().doubleValue() > 0) {
                    //貸方只有 1 筆.
                    transactionItemSeq += 1;
                    //借方 n 筆.
                    double totalAdvanceReceiptsJ = item.getAdvanceReceiptsJ().doubleValue();
                    SkCustomer customer = item.getArRemitMaster().getCustomer();
                    List<SkAdvanceRemit> customerAdvanceRemitJs = advanceRemits.get("advanceRemitJ");
                    for (SkAdvanceRemit customerAdvanceRemitJ : customerAdvanceRemitJs) {
                        totalAdvanceReceiptsJ -= customerAdvanceRemitJ.getAmount().abs().doubleValue();
                        transactionItemSeq++;
                        if (totalAdvanceReceiptsJ <= 0) {
                            break;
                        }
                    }
                    if (totalAdvanceReceiptsJ > 0) {
                        List<SkAdvanceRemit> groupAdvanceRemitJs = advanceRemits.get("groupAdvanceRemitJ");
                        for (SkAdvanceRemit groupAdvanceRemitJ : groupAdvanceRemitJs) {
                            totalAdvanceReceiptsJ -= groupAdvanceRemitJ.getAmount().abs().doubleValue();
                            transactionItemSeq++;
                            if (totalAdvanceReceiptsJ <= 0) {
                                break;
                            }
                        }
                    }
                }
                advanceRemits = advanceRemitFacade.advanceRemitMinusRemitItem(advanceRemits, item);
                //如果有尾差則會再產生兩筆SkFiDetailInterface.
                if (item.getDifferenceCharge() != null && item.getDifferenceCharge() > 0) {
                    transactionItemSeq += 2;
                }
                logger.debug("transactionItemSeq={}", transactionItemSeq);
            }
            logger.debug("details.size()={}", details.size());
        }
        //把最後一筆明細的item改成999作為結尾的識別.
        if (details.size() > 0) {
            SkFiDetailInterface lastDetail = details.get(details.size() - 1);
            lastDetail.setTransactionItem("999");
            master.setSkFiDetailInterfaceCollection(details);
            return master;
        } else {
            remitMaster.setStatus(RemitMasterStatusEnum.TRANSFER_OK);
            remitMasterFacade.edit(remitMaster);
            return null;
        }
    }

    public SkFiMasterInterface newSkFiMasterInterface(SkAdvancePayment advancePayment) {
        int transactionItemSeq = 1;
        SkFiMasterInterface master = new SkFiMasterInterface(advancePayment.getClass().getCanonicalName(), advancePayment.getId());
        List<SkFiDetailInterface> details = new ArrayList<SkFiDetailInterface>();
        //create interface for cash
        if (advancePayment.getAmount().doubleValue() > 0) {
            details.addAll(detailFacade.newSkFiDetailInterfaces(advancePayment, master, transactionItemSeq));
            transactionItemSeq += 2;
        }
        for (SkCheckMaster check : advancePayment.getSkCheckMasterCollection()) {
            logger.debug("transactionItemSeq={}", transactionItemSeq);
            details.addAll(detailFacade.newSkFiDetailInterfaces(advancePayment, check, master, transactionItemSeq));
            //每筆繳款單項目會產生借貸兩筆SkFiDetailImterface.
            transactionItemSeq += 2;
            logger.debug("transactionItemSeq={}", transactionItemSeq);
        }
        logger.debug("details.size()={}", details.size());
        //把最後一筆明細的item改成999作為結尾的識別.
        if (details.size() > 0) {
            SkFiDetailInterface lastDetail = details.get(details.size() - 1);
            lastDetail.setTransactionItem("999");
        }
        master.setSkFiDetailInterfaceCollection(details);
        return master;
    }

    public SkFiMasterInterface newSkFiMasterInterface(SkPremiumDiscount discount) {
        int transactionItemSeq = 1;
        SkFiMasterInterface master = new SkFiMasterInterface(discount.getClass().getCanonicalName(), discount.getId());
        List<SkFiDetailInterface> details = new ArrayList<SkFiDetailInterface>();
        //create interface for cash
        logger.debug("discount.getDiscount().add(discount.getTax())={}", discount.getDiscount().add(discount.getTax()));
        if (discount.getDiscount().add(discount.getTax()).doubleValue() > 0) {
            details.addAll(detailFacade.newSkFiDetailInterfaces(discount, master, transactionItemSeq));
        }
        //把最後一筆明細的item改成999作為結尾的識別.
        if (details.size() > 0) {
            SkFiDetailInterface lastDetail = details.get(details.size() - 1);
            lastDetail.setTransactionItem("999");
        }
        master.setSkFiDetailInterfaceCollection(details);
        return master;
    }

    /**
     * @return 解繳單中的-AR (以發票日期排序).
     */
    public List<SkArRemitItem> getSortedNegativeArItems(SkArRemitMaster master) throws Exception {
        List<SkArRemitItem> negativeArItems = new ArrayList<SkArRemitItem>();
        //amount 小於 0 表示為 -AR
        for (SkArRemitItem i : master.getSkArRemitItemCollection()) {
            if (i.getArAmount().doubleValue() < 0) {
                SkArRemitItem newI = new SkArRemitItem();
                PropertyUtils.copyProperties(newI, i);
                negativeArItems.add(newI);
            }
        }
        Collections.sort(negativeArItems, new Comparator<SkArRemitItem>() {

            @Override
            public int compare(SkArRemitItem o1, SkArRemitItem o2) {
                return o1.getInvoiceTimestamp().compareTo(o2.getInvoiceTimestamp());
            }
        });
        return negativeArItems;
    }

    /**
     * @return 西元年2碼+六碼序號 (取自SQE_TRNNO).
     */
    public String getTransactionNextNo() {
        //TODO: reset sequence for TransactionNo by year.
        String nextNumber = new SimpleDateFormat("yyyy").format(new Date()).substring(2);
        logger.debug("prefix={}", nextNumber);
        Query query = getEntityManager().createNativeQuery("SELECT SEQ_TRNNO.NEXTVAL FROM DUAL");
        BigDecimal index = (BigDecimal) query.getSingleResult();
        logger.debug("index=" + index);
        int indexLength = String.valueOf(index.longValue()).length();
        logger.debug("indexLength={}", indexLength);
        for (int i = 6; i > indexLength; i--) {
            logger.debug("+0");
            nextNumber += "0";
        }
        nextNumber += index;
        logger.debug("nextNumber=" + nextNumber);

        return nextNumber;
    }
}

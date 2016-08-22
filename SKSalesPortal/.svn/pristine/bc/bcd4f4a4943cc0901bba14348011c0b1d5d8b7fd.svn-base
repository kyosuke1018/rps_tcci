/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.facade;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.sksp.entity.ar.SkAdvancePayment;
import com.tcci.sksp.entity.ar.SkAdvanceRemit;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.ar.SkCheckMaster;
import com.tcci.sksp.entity.ar.SkFiDetailInterface;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;
import com.tcci.sksp.entity.ar.SkPremiumDiscount;
import com.tcci.sksp.entity.enums.PaymentTypeEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@Stateless
public class SkFiDetailInterfaceFacade extends AbstractFacade<SkFiDetailInterface> {

    private Logger logger = LoggerFactory.getLogger(SkFiDetailInterfaceFacade.class);
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
    @EJB
    SkCheckMasterFacade checkFacade;
    @EJB
    SkFiMasterInterfaceFacade masterFacade;
    @EJB
    SkAdvanceRemitFacade advanceRemitFacade;
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SkFiDetailInterfaceFacade() {
        super(SkFiDetailInterface.class);
    }

    public SkFiDetailInterface editAndReturn(SkFiDetailInterface skFiDetailInterface) {
        super.edit(skFiDetailInterface);
        return getEntityManager().merge(skFiDetailInterface);
    }

    /**
     * @param transactionItemSeq 因為沒辦法回傳用掉多少號碼, 所以必須在 caller 計算會用掉多少號碼. (目前有
     * amount, advance remit A & advance remit J & difference charge
     * (optional)).
     * @param advanceRemits 因為必須動態產生 (扣除其餘繳款單已耗用),
     * 已透過AdvanceRemitFacade.sortAdvanceRemit排序, 且分為自己的預收款 (advanceRemitA or
     * advanceRemitJ) 或 parent的預收款 (groupAdvanceRemitA or groupAdvanceRemitJ)
     */
    public List<SkFiDetailInterface> newSkFiDetailInterfaces(
            SkArRemitItem item,
            SkFiMasterInterface master,
            int transactionItemSeq,
            HashMap<String, List<SkAdvanceRemit>> advanceRemits,
            List<SkArRemitItem> negativeArItems) {
        List<SkFiDetailInterface> details = new ArrayList<SkFiDetailInterface>();
        // amount
        if (item.getAmount() != null && item.getAmount().doubleValue() > 0) {
            //現金解繳
            if (PaymentTypeEnum.CASH.equals(item.getPaymentType())) {
                SkFiDetailInterface detail = newSkFiDetailInterfaceForCashRemit(item, transactionItemSeq, "DB", item.getAmount());
                detail.setMaster(master);
                details.add(detail);
                transactionItemSeq++;
                detail = newSkFiDetailInterfaceForCashRemit(item, transactionItemSeq, "CR", item.getAmount());
                detail.setMaster(master);
                details.add(detail);
                transactionItemSeq++;
                //支票解繳
            } else if (PaymentTypeEnum.CHECK.equals(item.getPaymentType())) {
                SkFiDetailInterface detail = newSkFiDetailInterfaceForCheckRemit(item, transactionItemSeq, "DB", item.getAmount(), item.getCheckNumber());
                detail.setMaster(master);
                details.add(detail);
                transactionItemSeq++;
                detail = newSkFiDetailInterfaceForCheckRemit(item, transactionItemSeq, "CR", item.getAmount(), item.getCheckNumber());
                detail.setMaster(master);
                details.add(detail);
                transactionItemSeq++;
            }
        }
        // amount2
        if (item.getAmount2() != null && item.getAmount2().doubleValue() > 0) {
            //現金解繳
            if (PaymentTypeEnum.CASH.equals(item.getPaymentType2())) {
                SkFiDetailInterface detail = newSkFiDetailInterfaceForCashRemit(item, transactionItemSeq, "DB", item.getAmount2());
                detail.setMaster(master);
                details.add(detail);
                transactionItemSeq++;
                detail = newSkFiDetailInterfaceForCashRemit(item, transactionItemSeq, "CR", item.getAmount2());
                detail.setMaster(master);
                details.add(detail);
                transactionItemSeq++;
                //支票解繳
            } else if (PaymentTypeEnum.CHECK.equals(item.getPaymentType2())) {
                SkFiDetailInterface detail = newSkFiDetailInterfaceForCheckRemit(item, transactionItemSeq, "DB", item.getAmount2(), item.getCheckNumber2());
                detail.setMaster(master);
                details.add(detail);
                transactionItemSeq++;
                detail = newSkFiDetailInterfaceForCheckRemit(item, transactionItemSeq, "CR", item.getAmount2(), item.getCheckNumber2());
                detail.setMaster(master);
                details.add(detail);
                transactionItemSeq++;
            }
        }
        //-AR
        if (item.getNegativeAr() != null && item.getNegativeAr().doubleValue() > 0) {
            SkFiDetailInterface detail = newSkFiDetailInterfaceForNegativeAr(item, transactionItemSeq, "CR");
            detail.setMaster(master);
            details.add(detail);
            transactionItemSeq++;
            //-AR 必須依日期順序產生傳票.
            BigDecimal totalNegativeAr = item.getNegativeAr();
            //List<SkArRemitItem> negativeArItems = masterFacade.getSortedNegativeArItems(arRemitMaster);
            logger.debug("totalNegativeAr={}", totalNegativeAr);
            logger.debug("negativeArItems.size()={}", negativeArItems.size());
            List<SkArRemitItem> restNegativeArItems = new ArrayList<SkArRemitItem>();
            for (SkArRemitItem negativeArItem : negativeArItems) {
                boolean added=false;
                logger.debug("negativeArItem[{}]={}", new Object[]{negativeArItem.getId(), negativeArItem.getArAmount()});
                if (negativeArItem.getArAmount().abs().compareTo(totalNegativeAr) == -1) {
                    logger.debug("item");
                    detail = newSkFiDetailInterfaceForNegativeAr(negativeArItem, transactionItemSeq, "DB", negativeArItem.getArAmount().negate()); //use negate since ArAmount is negative.
                    detail.setMaster(master);
                    details.add(detail);
                    transactionItemSeq++;
                    totalNegativeAr = totalNegativeAr.add(negativeArItem.getArAmount()); //use add because amount is negative.
                    added=true;
                } else {
                    logger.debug("total");
                    detail = newSkFiDetailInterfaceForNegativeAr(negativeArItem, transactionItemSeq, "DB", totalNegativeAr);
                    detail.setMaster(master);
                    details.add(detail);
                    transactionItemSeq++;
                    negativeArItem.setArAmount(negativeArItem.getArAmount().add(totalNegativeAr));
                    restNegativeArItems.add(negativeArItem);
                    added=true;
                }
                if(!added) {
                    restNegativeArItems.add(negativeArItem);
                }
               
            }
            negativeArItems.clear();
            negativeArItems.addAll(restNegativeArItems);
        }
        //預收A
        if (item.getAdvanceReceiptsA() != null && item.getAdvanceReceiptsA().doubleValue() > 0) {
            SkFiDetailInterface detail = null;
            double totalAdvanceReceiptsA = item.getAdvanceReceiptsA().doubleValue();
            //先沖自己的預收
            List<SkAdvanceRemit> customerAdvanceRemitAs = advanceRemits.get("advanceRemitA");
            if (customerAdvanceRemitAs != null) {
                for (SkAdvanceRemit customerAdvanceRemitA : customerAdvanceRemitAs) {
                    if (totalAdvanceReceiptsA > customerAdvanceRemitA.getAmount().abs().doubleValue()) {
                        detail = newSkFiDetailInterfaceForAdvanceReceiptsA(
                                item,
                                transactionItemSeq,
                                "DB",
                                customerAdvanceRemitA.getAmount().abs(),
                                null);
                        detail.setMaster(master);
                        details.add(detail);
                        transactionItemSeq++;
                    } else {
                        detail = newSkFiDetailInterfaceForAdvanceReceiptsA(
                                item,
                                transactionItemSeq,
                                "DB",
                                BigDecimal.valueOf(totalAdvanceReceiptsA),
                                null);
                        detail.setMaster(master);
                        details.add(detail);
                        transactionItemSeq++;
                    }
                    totalAdvanceReceiptsA -= customerAdvanceRemitA.getAmount().abs().doubleValue();
                    if (totalAdvanceReceiptsA <= 0) {
                        break;
                    }
                }
            }
            //再沖parentCustomer的預收.
            if (totalAdvanceReceiptsA > 0) {
                List<SkAdvanceRemit> groupAdvanceRemitAs = advanceRemits.get("groupAdvanceRemitA");
                if (groupAdvanceRemitAs != null) {
                    for (SkAdvanceRemit groupAdvanceRemitA : groupAdvanceRemitAs) {
                        if (totalAdvanceReceiptsA > groupAdvanceRemitA.getAmount().abs().doubleValue()) {
                            detail = newSkFiDetailInterfaceForAdvanceReceiptsA(
                                    item,
                                    transactionItemSeq,
                                    "DB",
                                    groupAdvanceRemitA.getAmount().abs(),
                                    groupAdvanceRemitA);
                            detail.setMaster(master);
                            details.add(detail);
                            transactionItemSeq++;
                        } else {
                            detail = newSkFiDetailInterfaceForAdvanceReceiptsA(
                                    item,
                                    transactionItemSeq,
                                    "DB",
                                    BigDecimal.valueOf(totalAdvanceReceiptsA),
                                    groupAdvanceRemitA);
                            detail.setMaster(master);
                            details.add(detail);
                            transactionItemSeq++;
                        }
                        totalAdvanceReceiptsA -= groupAdvanceRemitA.getAmount().abs().doubleValue();
                        if (totalAdvanceReceiptsA <= 0) {
                            break;
                        }
                    }
                }
                //沖帳至最後仍有未沖帳的金額, 則將該筆標示為預收金額不足.
                if (totalAdvanceReceiptsA > 0) {
                    detail = newSkFiDetailInterfaceForAdvanceReceiptsA(
                            item,
                            transactionItemSeq,
                            "DB",
                            BigDecimal.valueOf(totalAdvanceReceiptsA),
                            null);
                    detail.setReturnCode("003");
                    detail.setReturnMessage(rb.getString("fiinterface.error.advanceremitanotenought"));
                    detail.setMaster(master);
                    details.add(detail);
                    transactionItemSeq++;
                }
            }
            detail = newSkFiDetailInterfaceForAdvanceReceiptsA(
                    item,
                    transactionItemSeq,
                    "CR",
                    item.getAdvanceReceiptsA(),
                    null);
            detail.setMaster(master);
            details.add(detail);
            transactionItemSeq++;
        }

        //預收J
        if (item.getAdvanceReceiptsJ() != null && item.getAdvanceReceiptsJ().doubleValue() > 0) {
            SkFiDetailInterface detail = null;
            double totalAdvanceReceiptsJ = item.getAdvanceReceiptsJ().doubleValue();
            //先沖自己的預收
            List<SkAdvanceRemit> customerAdvanceRemitJs = advanceRemits.get("advanceRemitJ");
            if (customerAdvanceRemitJs != null) {
                for (SkAdvanceRemit customerAdvanceRemitJ : customerAdvanceRemitJs) {
                    if (totalAdvanceReceiptsJ > customerAdvanceRemitJ.getAmount().abs().doubleValue()) {
                        detail = newSkFiDetailInterfaceForAdvanceReceiptsJ(
                                item,
                                transactionItemSeq,
                                "DB",
                                customerAdvanceRemitJ.getAmount().abs(),
                                null);
                        detail.setMaster(master);
                        details.add(detail);
                        transactionItemSeq++;
                    } else {
                        detail = newSkFiDetailInterfaceForAdvanceReceiptsJ(
                                item,
                                transactionItemSeq,
                                "DB",
                                BigDecimal.valueOf(totalAdvanceReceiptsJ),
                                null);
                        detail.setMaster(master);
                        details.add(detail);
                        transactionItemSeq++;
                    }
                    totalAdvanceReceiptsJ -= customerAdvanceRemitJ.getAmount().abs().doubleValue();
                    if (totalAdvanceReceiptsJ <= 0) {
                        break;
                    }
                }
            }
            //再沖parentCustomer的預收.
            if (totalAdvanceReceiptsJ > 0) {
                List<SkAdvanceRemit> groupAdvanceRemitJs = advanceRemits.get("groupAdvanceRemitJ");
                if (groupAdvanceRemitJs != null) {
                    for (SkAdvanceRemit groupAdvanceRemitJ : groupAdvanceRemitJs) {
                        if (totalAdvanceReceiptsJ > groupAdvanceRemitJ.getAmount().abs().doubleValue()) {
                            detail = newSkFiDetailInterfaceForAdvanceReceiptsJ(
                                    item,
                                    transactionItemSeq,
                                    "DB",
                                    groupAdvanceRemitJ.getAmount().abs(),
                                    groupAdvanceRemitJ);
                            detail.setMaster(master);
                            details.add(detail);
                            transactionItemSeq++;
                        } else {
                            detail = newSkFiDetailInterfaceForAdvanceReceiptsJ(
                                    item,
                                    transactionItemSeq,
                                    "DB",
                                    BigDecimal.valueOf(totalAdvanceReceiptsJ),
                                    groupAdvanceRemitJ);
                            detail.setMaster(master);
                            details.add(detail);
                            transactionItemSeq++;
                        }
                        totalAdvanceReceiptsJ -= groupAdvanceRemitJ.getAmount().abs().doubleValue();
                        if (totalAdvanceReceiptsJ <= 0) {
                            break;
                        }
                    }
                }
                //沖帳至最後仍有未沖帳的金額, 則將該筆標示為預收金額不足.
                if (totalAdvanceReceiptsJ > 0) {
                    detail = newSkFiDetailInterfaceForAdvanceReceiptsJ(
                            item,
                            transactionItemSeq,
                            "DB",
                            BigDecimal.valueOf(totalAdvanceReceiptsJ),
                            null);
                    detail.setReturnCode("003");
                    detail.setReturnMessage(rb.getString("fiinterface.error.advanceremitjnotenought"));
                    detail.setMaster(master);
                    details.add(detail);
                    transactionItemSeq++;
                }
            }
            detail = newSkFiDetailInterfaceForAdvanceReceiptsJ(
                    item,
                    transactionItemSeq,
                    "CR",
                    item.getAdvanceReceiptsJ(),
                    null);
            detail.setMaster(master);
            details.add(detail);
            transactionItemSeq++;
        }

        //尾差
        if (item.getDifferenceCharge() != null && item.getDifferenceCharge() > 0) {
            SkFiDetailInterface detail = newSkFiDetailInterfaceForDifferenceCharge(item, transactionItemSeq, "DB");
            detail.setMaster(master);
            details.add(detail);
            transactionItemSeq++;
            detail = newSkFiDetailInterfaceForDifferenceCharge(item, transactionItemSeq, "CR");
            detail.setMaster(master);
            details.add(detail);
            transactionItemSeq++;
        }

        return details;
    }
    
    private SkFiDetailInterface newSkFiDetailInterfaceForCashRemit(
            SkArRemitItem item,
            int transactionItemSeq,
            String type,
            BigDecimal amount) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        detail.setTransactionDate(item.getArRemitMaster().getReviewTimestamp());
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode(item.getArRemitMaster().getBank().getGeneralLedgerCode());
            detail.setSummonsCode("DB");
            //借方不需要客戶, 發票號碼及訂單編號.
            //customer no
            //invoice no
            //order no
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("CR0001");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            detail.setInvoiceNumber(item.getInvoiceNumber());
            detail.setOrderNumber(item.getOrderNumber());
        }
        detail.setTransactionAmount(amount);
        if ("DB".equals(type)) {
            //借方不需要銷售群組資訊.
            //sales group
        } else {
            detail.setSalesGroup(item.getArRemitMaster().getSapid());
        }
        //quantity
//--Begin--Modified by nEO Fu on 20110131 no need save check information for cash remit.
//        SkCheckMaster checkMaster = checkFacade.findByCheckNumber(item.getCheckNumber());
//        detail.setCheckNumber(item.getCheckNumber());
//        detail.setCheckDueDate(checkMaster.getDueDate());
//        detail.setCheckBank(checkMaster.getBillingBank().getCode());
//        detail.setCheckAccount(checkMaster.getBillingAccount());
//---End---Modified by nEO Fu on 20110131 no need save check information for cash remit.
        detail.setOwner(item.getModifier().getEmpId());
        return detail;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForCheckRemit(
            SkArRemitItem item,
            int transactionItemSeq,
            String type,
            BigDecimal amount,
            String checkNumber) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        detail.setTransactionDate(item.getArRemitMaster().getReviewTimestamp());
        SkCheckMaster checkMaster = checkFacade.findByCheckNumber(checkNumber);
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode("CW0002");
            detail.setSummonsCode("DB");
            //借方不需要發票號碼及訂單編號.
            //--Begin--Modified by nEO Fu on 20120419 抓支票主檔的客戶.
            if (checkMaster != null) {
                detail.setCustomerNumber(checkMaster.getCustomer().getSimpleCode());
            } else {
                detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            }
            //---End--Modified by nEO Fu on 20120419 抓支票主檔的客戶.
            //invoice no
            //order no
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("CR0001");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            detail.setInvoiceNumber(item.getInvoiceNumber());
            detail.setOrderNumber(item.getOrderNumber());
        }
        detail.setTransactionAmount(amount);
        if ("DB".equals(type)) {
            //借方不需要銷售群組資訊.
            //sales group
        } else {
            detail.setSalesGroup(item.getArRemitMaster().getSapid());
        }
        //quantity
        if ("DB".equals(type)) {
            detail.setCheckNumber(checkNumber);
            if (checkMaster != null) {
                detail.setCheckDueDate(checkMaster.getDueDate());
                detail.setCheckBank(checkMaster.getBillingBank().getCode());
                detail.setCheckAccount(checkMaster.getBillingAccount());
            } else {
                detail.setReturnCode("001");
                detail.setReturnMessage(rb.getString("fiinterface.error.checkmasternotexists"));
            }
        } else if ("CR".equals(type)) {
            //貸方不需支票主檔資訊.
            //check number
            //check due date
            //check bank
            //check account
        }
        detail.setOwner(item.getModifier().getEmpId());
        return detail;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForDifferenceCharge(
            SkArRemitItem item,
            int transactionItemSeq,
            String type) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        detail.setTransactionDate(item.getArRemitMaster().getReviewTimestamp());
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode("EX0001");
            detail.setSummonsCode("DB");
            //借方不需要客戶, 發票號碼及訂單編號.
            //customer no
            //invoice no
            //order no
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("CR0001");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            detail.setInvoiceNumber(item.getInvoiceNumber());
            detail.setOrderNumber(item.getOrderNumber());
        }
        detail.setTransactionAmount(BigDecimal.valueOf(item.getDifferenceCharge().doubleValue()));
        if ("DB".equals(type)) {
            //借方不需要銷售群組資訊.
            //sales group
        } else {
            detail.setSalesGroup(item.getArRemitMaster().getSapid());
        }
        //quantity
        //費用不需要支票的資訊.
        //check number
        //check due date
        //check bank
        //check account
        detail.setOwner(item.getModifier().getEmpId());
        return detail;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForAdvanceReceiptsA(
            SkArRemitItem item,
            int transactionItemSeq,
            String type,
            BigDecimal amount,
            SkAdvanceRemit groupAdvanceRemit) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        detail.setTransactionDate(item.getArRemitMaster().getReviewTimestamp());
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode("CA0001");
            detail.setSummonsCode("DB");
            //借方不需要發票號碼及訂單編號.
            // Jimmy, issue#20120604, DB有可能來自groupAdvanceRemit(parent customer)
            // detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            String customerNumber = (null != groupAdvanceRemit) ? 
                    groupAdvanceRemit.getCustomer().getSimpleCode() : item.getArRemitMaster().getCustomer().getSimpleCode();
            detail.setCustomerNumber(customerNumber);
            //invoice no
            //order no
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("CR0001");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            detail.setInvoiceNumber(item.getInvoiceNumber());
            detail.setOrderNumber(item.getOrderNumber());
        }
        detail.setTransactionAmount(amount);
        if ("DB".equals(type)) {
            //借方不需要銷售群組資訊.
            //sales group
        } else {
            detail.setSalesGroup(item.getArRemitMaster().getSapid());
        }
        //quantity
        //費用不需要支票的資訊.
        //check number
        //check due date
        //check bank
        //check account
        detail.setOwner(item.getModifier().getEmpId());
        return detail;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForAdvanceReceiptsJ(
            SkArRemitItem item,
            int transactionItemSeq,
            String type,
            BigDecimal amount,
            SkAdvanceRemit groupAdvanceRemit) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        detail.setTransactionDate(item.getArRemitMaster().getReviewTimestamp());
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode("CA0002");
            detail.setSummonsCode("DB");
            //借方不需要發票號碼及訂單編號.
            // Jimmy, issue#20120604, DB有可能來自groupAdvanceRemit(parent customer)
            // detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            String customerNumber = (null != groupAdvanceRemit) ? 
                    groupAdvanceRemit.getCustomer().getSimpleCode() : item.getArRemitMaster().getCustomer().getSimpleCode();
            detail.setCustomerNumber(customerNumber);
            //invoice no
            //order no
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("CR0001");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            detail.setInvoiceNumber(item.getInvoiceNumber());
            detail.setOrderNumber(item.getOrderNumber());
        }
        detail.setTransactionAmount(amount);
        if ("DB".equals(type)) {
            //借方不需要銷售群組資訊.
            //sales group
        } else {
            detail.setSalesGroup(item.getArRemitMaster().getSapid());
        }
        //quantity
        //費用不需要支票的資訊.
        //check number
        //check due date
        //check bank
        //check account
        detail.setOwner(item.getModifier().getEmpId());
        return detail;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForNegativeAr(
            SkArRemitItem item,
            int transactionItemSeq,
            String type) {
        return newSkFiDetailInterfaceForNegativeAr(item, transactionItemSeq, type, item.getNegativeAr());
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForNegativeAr(
            SkArRemitItem item,
            int transactionItemSeq,
            String type,
            BigDecimal amount) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        detail.setTransactionDate(item.getArRemitMaster().getReviewTimestamp());
        if ("DB".equals(type)) {
            //TODO: 應抓負AR的資訊
            detail.setGeneralLedgerCode("CR0001");
            detail.setSummonsCode("DB");
            detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            detail.setInvoiceNumber(item.getInvoiceNumber());
            detail.setOrderNumber(item.getOrderNumber());
        } else if ("CR".equals(type)) {
            //TODO: 應抓該張繳款單的資訊
            detail.setGeneralLedgerCode("CR0001");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(item.getArRemitMaster().getCustomer().getSimpleCode());
            detail.setInvoiceNumber(item.getInvoiceNumber());
            detail.setOrderNumber(item.getOrderNumber());
        }
        detail.setTransactionAmount(amount);
        if ("DB".equals(type)) {
            //借方不需要銷售群組資訊.
            //sales group
        } else {
            detail.setSalesGroup(item.getArRemitMaster().getSapid());
        }
        //quantity
        //費用不需要支票的資訊.
        //check number
        //check due date
        //check bank
        //check account
        detail.setOwner(item.getModifier().getEmpId());
        return detail;
    }

    public List<SkFiDetailInterface> newSkFiDetailInterfaces(
            SkAdvancePayment advancePayment,
            SkFiMasterInterface master,
            int transactionItemSeq) {
        List<SkFiDetailInterface> details = new ArrayList<SkFiDetailInterface>();
        SkFiDetailInterface detailDB = newSkFiDetailInterfaceForCashAdvancePayment(advancePayment, transactionItemSeq, "DB");
        detailDB.setMaster(master);
        details.add(detailDB);
        transactionItemSeq++;
        SkFiDetailInterface detailCR = newSkFiDetailInterfaceForCashAdvancePayment(advancePayment, transactionItemSeq, "CR");
        detailCR.setMaster(master);
        details.add(detailCR);
        return details;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForCashAdvancePayment(
            SkAdvancePayment advancePayment,
            int transactionItemSeq,
            String type) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        detail.setTransactionDate(advancePayment.getModifytimestamp());
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode("BK0001");
            detail.setSummonsCode("DB");
            //借方不需要客戶.
            //customer no
            //invoice no
            //order no
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("CA0001");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(advancePayment.getCustomer().getSimpleCode());
            //invoice no
            //order no
        }
        detail.setTransactionAmount(advancePayment.getAmount());
        //sales group
        //quantity
//--Begin--Modified by nEO Fu on 20110131 no need save check information for cash remit.
//        SkCheckMaster checkMaster = checkFacade.findByCheckNumber(item.getCheckNumber());
//        detail.setCheckNumber(item.getCheckNumber());
//        detail.setCheckDueDate(checkMaster.getDueDate());
//        detail.setCheckBank(checkMaster.getBillingBank().getCode());
//        detail.setCheckAccount(checkMaster.getBillingAccount());
//---End---Modified by nEO Fu on 20110131 no need save check information for cash remit.
        detail.setOwner(advancePayment.getModifier().getEmpId());
        return detail;
    }

    public List<SkFiDetailInterface> newSkFiDetailInterfaces(
            SkAdvancePayment advancePayment,
            SkCheckMaster check,
            SkFiMasterInterface master,
            int transactionItemSeq) {
        List<SkFiDetailInterface> details = new ArrayList<SkFiDetailInterface>();
        SkFiDetailInterface detailDB = newSkFiDetailInterfaceForCheckAdvancePayment(advancePayment, check, transactionItemSeq, "DB");
        detailDB.setMaster(master);
        details.add(detailDB);
        transactionItemSeq++;
        SkFiDetailInterface detailCR = newSkFiDetailInterfaceForCheckAdvancePayment(advancePayment, check, transactionItemSeq, "CR");
        detailCR.setMaster(master);
        details.add(detailCR);
        return details;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForCheckAdvancePayment(
            SkAdvancePayment advancePayment,
            SkCheckMaster check,
            int transactionItemSeq,
            String type) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        detail.setTransactionDate(advancePayment.getModifytimestamp());
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode("CW0002");
            detail.setSummonsCode("DB");
            detail.setCustomerNumber(advancePayment.getCustomer().getSimpleCode());
            //invoice no
            //order no
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("CA0002");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(advancePayment.getCustomer().getSimpleCode());
            //invoice no
            //order no
        }
        detail.setTransactionAmount(check.getAmount());
        //sales group
        //quantity
        detail.setCheckNumber(check.getCheckNumber());
        detail.setCheckDueDate(check.getDueDate());
        detail.setCheckBank(check.getBillingBank().getCode());
        detail.setCheckAccount(check.getBillingAccount());
        detail.setOwner(advancePayment.getModifier().getEmpId());
        return detail;
    }

    public List<SkFiDetailInterface> newSkFiDetailInterfaces(
            SkPremiumDiscount premiumDiscount,
            SkFiMasterInterface master,
            int transactionItemSeq) {
        List<SkFiDetailInterface> details = new ArrayList<SkFiDetailInterface>();
        //discount
        SkFiDetailInterface detailDiscountDB = newSkFiDetailInterfaceForPremiumDiscount(premiumDiscount, transactionItemSeq, "DB");
        detailDiscountDB.setMaster(master);
        details.add(detailDiscountDB);
        transactionItemSeq++;
        SkFiDetailInterface detailDiscountCR = newSkFiDetailInterfaceForPremiumDiscount(premiumDiscount, transactionItemSeq, "CR");
        detailDiscountCR.setMaster(master);
        details.add(detailDiscountCR);
        transactionItemSeq++;

        //tax
        SkFiDetailInterface detailTaxDB = newSkFiDetailInterfaceForPremiumDiscountTax(premiumDiscount, transactionItemSeq, "DB");
        detailTaxDB.setMaster(master);
        details.add(detailTaxDB);
        transactionItemSeq++;
        SkFiDetailInterface detailTaxCR = newSkFiDetailInterfaceForPremiumDiscountTax(premiumDiscount, transactionItemSeq, "CR");
        detailTaxCR.setMaster(master);
        details.add(detailTaxCR);
        return details;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForPremiumDiscount(
            SkPremiumDiscount premiumDiscount,
            int transactionItemSeq,
            String type) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionType("CLDI");
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        //

        //detail.setTransactionDate(new Date(Date.parse(premiumDiscount.getYear() + "/" + premiumDiscount.getMonth() + "/01")));
        detail.setTransactionDate(premiumDiscount.getCreatetimestamp());
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode("CR0003");
            detail.setSummonsCode("DB");
            detail.setCustomerNumber(premiumDiscount.getCustomer().getSimpleCode());
            detail.setInvoiceNumber(premiumDiscount.getInvoiceNumber());
            detail.setOrderNumber(premiumDiscount.getOrderNumber());
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("CR0002");
            detail.setSummonsCode("CR");
            detail.setCustomerNumber(premiumDiscount.getCustomer().getSimpleCode());
            detail.setInvoiceNumber(premiumDiscount.getInvoiceNumber());
            detail.setOrderNumber(premiumDiscount.getOrderNumber());
        }
        detail.setTransactionAmount(premiumDiscount.getDiscount().add(premiumDiscount.getTax()));
        detail.setSalesGroup(premiumDiscount.getSapid());
        //quantity
        //check number
        //due date
        //billing bank
        //chck account
        detail.setOwner(premiumDiscount.getModifier().getEmpId());
        return detail;
    }

    private SkFiDetailInterface newSkFiDetailInterfaceForPremiumDiscountTax(
            SkPremiumDiscount premiumDiscount,
            int transactionItemSeq,
            String type) {
        SkFiDetailInterface detail = new SkFiDetailInterface();
        detail.setTransactionType("CLDI");
        detail.setTransactionItem(getTransactionItem(transactionItemSeq));
        //detail.setTransactionDate(new Date(Date.parse(premiumDiscount.getYear() + "/" + premiumDiscount.getMonth() + "/01")));
        detail.setTransactionDate(premiumDiscount.getCreatetimestamp());
        if ("DB".equals(type)) {
            detail.setGeneralLedgerCode("TX0001");
            detail.setSummonsCode("DB");
            //customer
            //invoice no
            //order no
        } else if ("CR".equals(type)) {
            detail.setGeneralLedgerCode("TX0002");
            detail.setSummonsCode("CR");
            //customer no
            //invoice no
            //order no
        }
        detail.setTransactionAmount(premiumDiscount.getTax());
        //sales group
        //quantity
        //check number
        //due date
        //billing bank
        //chck account
        detail.setOwner(premiumDiscount.getModifier().getEmpId());
        return detail;
    }

    private String getTransactionItem(int transactionItemSeq) {
        String item = "";
        String itemSeq = String.valueOf(transactionItemSeq);
        logger.debug("itemSeq={}", itemSeq);
        for (int i = 3; i > itemSeq.length(); i--) {
            item += "0";
        }
        item += itemSeq;
        return item;
    }
}

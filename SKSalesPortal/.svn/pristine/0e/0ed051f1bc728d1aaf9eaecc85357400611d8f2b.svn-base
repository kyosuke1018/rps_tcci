package com.tcci.sksp.controller.quotation;

import com.tcci.sksp.controller.util.*;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.quotation.SkQuotationDetail;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.entity.ar.SkProductCategory;
import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.entity.quotation.SkQuotationGift;
import com.tcci.sksp.entity.datawarehouse.TcZtabExpTvv1t;
import com.tcci.sksp.entity.enums.QuotationStatusEnum;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.entity.quotation.SkQuotationReviewHistory;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkQuotationFacade;
import com.tcci.sksp.facade.SkProductCategoryFacade;
import com.tcci.sksp.facade.SkProductMasterFacade;
import com.tcci.sksp.facade.SkSalesDetailsFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import com.tcci.sksp.facade.TcZtabExpTvv1tFacade;
import com.tcci.sksp.notification.SkQuotationCreateNotifier;
import com.tcci.sksp.vo.QuotationDetailVO;
import com.tcci.sksp.vo.QuotationGiftVO;
import com.tcci.sksp.vo.SalesDetailsVO;
import com.tcci.worklist.controller.util.JsfUtils;
import java.math.BigDecimal;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean(name = "editQuotation")
@ViewScoped
public class EditQuotationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    ResourceBundle rb = ResourceBundle.getBundle("/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    private SkQuotationMaster selected;
    private List<QuotationDetailVO> detailList;
    private List<SkProductCategory> categoryList;
    private List<SkProductMaster> productList;
    private List<TcZtabExpTvv1t> remarkList;
    private List<QuotationGiftVO> giftList;
    private int latestIndex;
    private HtmlInputHidden initSalesAndCustomerHidden = new HtmlInputHidden();
    private boolean create;
    private String title;
    //<editor-fold desc="EJBs" defaultstate="collapsed">
    @EJB
    SkQuotationFacade ejbFacade;
    @EJB
    SkSalesDetailsFacade orderDetailsFacade;
    @EJB
    SkProductMasterFacade productFacade;
    @EJB
    SkProductCategoryFacade productCategoryFacade;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    TcZtabExpTvv1tFacade remarkFacade;
    @EJB
    SkSalesMemberFacade salesMemberFacade;
    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;

    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }
    @ManagedProperty(value = "#{selectConsigneeController}")
    private SelectConsigneeController selectConsigneeController;

    public void setSelectConsigneeController(SelectConsigneeController selectConsigneeController) {
        this.selectConsigneeController = selectConsigneeController;
    }
    @ManagedProperty(value = "#{selectCustomerController}")
    private SelectCustomerController selectCustomerController;

    public void setSelectCustomerController(SelectCustomerController selectCustomerController) {
        this.selectCustomerController = selectCustomerController;
    }

    //</editor-fold>
    @PostConstruct
    public void init() {
        logger.debug("init()");
        this.remarkList = remarkFacade.findAll();
        this.categoryList = productCategoryFacade.findAll();
        Collections.sort(this.categoryList, new Comparator<SkProductCategory>() {
            @Override
            public int compare(SkProductCategory c1, SkProductCategory c2) {
                return c1.getCategory().compareTo(c2.getCategory());
            }
        });
        String oid = JsfUtils.getRequestParameter("oid");
        if (StringUtils.isEmpty(oid)) {
            this.create = true;
            this.title = rb.getString("menu.sales.quotation.create");
            this.selected = new SkQuotationMaster();
            this.selected.setStatus(QuotationStatusEnum.OPEN);
            this.selected.setQuotationDate(new Date());
            this.detailList = new ArrayList();
            this.giftList = new ArrayList();
            this.latestIndex = 0;
        } else {
            create = false;
            this.title = rb.getString("menu.sales.quotation.edit");
            try {
                SkQuotationMaster quotationMaster = (SkQuotationMaster) ejbFacade.getObject(oid);
                if (!(quotationMaster.getCreator().equals(userSession.getUser())
                        || FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Administrators"))) {
                    JsfUtils.addErrorMessage(rb.getString("quotation.noPermissionToEdit"));
                    return;
                } else {
                    this.selected = quotationMaster;
                }
                //set consignee
                SkCustomer consignee = this.selected.getConsignee();
                selectConsigneeController.setSelectedConsignee(consignee.getSimpleCode());
                selectConsigneeController.setName(consignee.getName());

                List<SkQuotationDetail> newDetailList = new ArrayList();
                this.detailList = new ArrayList();
                List<SkQuotationGift> newGiftList = new ArrayList();
                this.giftList = new ArrayList();
                for (SkQuotationDetail detail : this.selected.getDetailCollection()) {
                    SkQuotationDetail newDetail = new SkQuotationDetail(detail);
                    QuotationDetailVO vo = new QuotationDetailVO(newDetail);
                    vo.setIndex(this.latestIndex);
                    SkProductMaster product = productFacade.findByCode(detail.getProductNumber());
                    if (product != null) {
                        vo.setCategory(product.getCategory());
                        List<SkProductMaster> productList = productFacade.findByCategories(new String[]{product.getCategory()});
                        Collections.sort(productList, new Comparator<SkProductMaster>() {
                            @Override
                            public int compare(SkProductMaster p1, SkProductMaster p2) {
                                return p1.getName().compareTo(p2.getName());
                            }
                        });
                        vo.setProductList(productList);
                    }
                    this.detailList.add(vo);
                    newDetailList.add(newDetail);
                    this.latestIndex++;
                    for (SkQuotationGift gift : detail.getGiftList()) {
                        QuotationGiftVO giftVO = new QuotationGiftVO();
                        giftVO.setDetailVO(vo);
                        SkQuotationGift newGift = new SkQuotationGift(gift);
                        newGift.setQuotationDetail(newDetail);
                        giftVO.setGift(newGift);
                        SkProductMaster giftProduct = productFacade.findByCode(gift.getProductNumber());
                        if (giftProduct != null) {
                            giftVO.setCategory(giftProduct.getCategory());
                            List<SkProductMaster> productList = productFacade.findByCategories(new String[]{giftProduct.getCategory()});
                            Collections.sort(productList, new Comparator<SkProductMaster>() {
                                @Override
                                public int compare(SkProductMaster p1, SkProductMaster p2) {
                                    return p1.getName().compareTo(p2.getName());
                                }
                            });
                            giftVO.setProductList(productList);
                        }
                        this.giftList.add(giftVO);
                        vo.getGiftVOList().add(giftVO);
                        newGiftList.add(newGift);
                        this.latestIndex++;
                    }
                    newDetail.setGiftList(newGiftList);
                }
                logger.debug("this.selected.getId()={}", this.selected.getId());
                this.selected.setDetailCollection(newDetailList);

                for (SkQuotationDetail detail : this.selected.getDetailCollection()) {
                    logger.debug("detail.getId()={}", detail.getId());
                    for (SkQuotationGift gift : detail.getGiftList()) {
                        logger.debug("gift.getId()={}", gift.getId());
                    }
                }
                for (SkQuotationReviewHistory reviewHistory : this.selected.getReviewHistoryCollection()) {
                    logger.debug("reviewHisotry.getId()={}", reviewHistory.getId());
                }
            } catch (Exception e) {
                JsfUtils.addErrorMessage(e, "EditQuotationController.init()");
                logger.error("e={}", e);
            }
        }
    }

    public String save() {
        logger.debug("save()");
        boolean customerRequired = true;
        boolean onlySalesCode = true;
        queryCriteriaController.latestStepToCheckCustomerCode(customerRequired, onlySalesCode);
        logger.debug("onlySalesCode && queryCriteriaController.isInvalidCustomerRelation()={}", onlySalesCode && queryCriteriaController.isInvalidCustomerRelation());
        logger.debug("queryCriteriaController.isWrongCustomerCode()={}", queryCriteriaController.isWrongCustomerCode());
        logger.debug("queyrCriteriaController.isCustomerNotExists()={}", queryCriteriaController.isCustomerNotExists());
        if (onlySalesCode
                && queryCriteriaController.isInvalidCustomerRelation()) {
            return null;
        } else if (!(queryCriteriaController.isWrongCustomerCode()
                || queryCriteriaController.isCustomerNotExists())) {
            logger.debug("process");
            this.selected.setCustomer(queryCriteriaController.getFilter().getSkCustomer());
            if (StringUtils.isNotEmpty(selectConsigneeController.getSelectedConsignee())) {
                SkCustomer consignee = customerFacade.findBySimpleCode(selectConsigneeController.getSelectedConsignee());
                if (consignee == null) {
                    consignee = new SkCustomer();
                    consignee.setCode(selectConsigneeController.getSelectedConsignee());
                    selectConsigneeController.setName(rb.getString("quotation.consigneenotexists"));
                }
                this.selected.setConsignee(consignee);
            }
            List<SkQuotationDetail> details = new ArrayList();
            for (QuotationDetailVO vo : this.detailList) {
                SkQuotationDetail detail = vo.getDetail();
                detail.setQuotationMaster(this.selected);
                details.add(detail);
                List<SkQuotationGift> gifts = new ArrayList();
                logger.debug("vo.getGiftVOList().size()={}", vo.getGiftVOList().size());
                for (QuotationGiftVO giftVO : vo.getGiftVOList()) {
                    SkQuotationGift gift = giftVO.getGift();
                    gift.setQuotationDetail(detail);
                    gifts.add(gift);
                }
                detail.setGiftList(gifts);
            }
            this.selected.setDetailCollection(details);
            Date now = new Date();
            if (create) {
                this.selected.setCreator(userSession.getUser());
                this.selected.setCreatetimestamp(now);
            }
            this.selected.setModifier(userSession.getUser());
            this.selected.setModifytimestamp(now);
            try {
                String successfulMessage = "";
                if (create) {
                    this.selected = ejbFacade.validateThenCreateAndReturn(this.selected);
                    successfulMessage = rb.getString("quotation.createSuccess");
                } else {
                    this.selected.setStatus(QuotationStatusEnum.OPEN);
                    this.selected.setReviewHistoryCollection(null);
                    this.selected = ejbFacade.validateThenEditAndReturn(this.selected);
                    successfulMessage = rb.getString("quotation.editSuccess");
                }
                List<String> warningMessageList = getWarningMessageList(this.selected);
                if (!warningMessageList.isEmpty()) {
                    for (String warningMessage : warningMessageList) {
                        JsfUtils.addWarningMessage(warningMessage);
                    }
                }
                JsfUtils.addSuccessMessage(successfulMessage);
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.getExternalContext().getFlash().setKeepMessages(true);
                facesContext.getExternalContext().redirect("quotation.xhtml?oid=" + this.selected.toString());
            } catch (Exception e) {
                JsfUtils.addErrorMessage(e, "CreateQuotationController.save() error occurred!");
                logger.error("e={}", e);
            }
        }
        return null;
    }

    private List<String> getWarningMessageList(SkQuotationMaster master) {
        List<String> warningMessageList = new ArrayList();
        boolean hasInstruction = false;
        for (SkQuotationDetail detail : master.getDetailCollection()) {
            SkProductMaster product = productFacade.findByCode(detail.getProductNumber());
            if (!hasInstruction) {
                hasInstruction = productFacade.isInstruction(product);
            }
            for (SkQuotationGift gift : detail.getGiftList()) {
                SkProductMaster giftProduct = productFacade.findByCode(gift.getProductNumber());
                if (!hasInstruction) {
                    hasInstruction = productFacade.isInstruction(giftProduct);
                }
            }
        }
        //warning message: 無營登藥局 - 購買指示藥(3聯)請確認是否為無登記營業人!
        if (!StringUtils.isNotEmpty(master.getCustomer().getVat())) {

            if (hasInstruction) {
                warningMessageList.add(rb.getString("quotation.notVatWithInstruction"));
            }
        }
        return warningMessageList;
    }

    public void newDetail() {
        logger.debug("newDetail()");
        SkQuotationDetail detail = new SkQuotationDetail();
        detail.setQuotationMaster(this.selected);
        QuotationDetailVO vo = new QuotationDetailVO(detail);
        vo.setIndex(latestIndex);
        latestIndex++;
        this.detailList.add(vo);
    }

    public void newGift(QuotationDetailVO detailVO) {
        logger.debug("newGift()");
        SkQuotationGift gift = new SkQuotationGift();
        gift.setQuotationDetail(detailVO.getDetail());
        String productNumber = detailVO.getDetail().getProductNumber();
        SkProductMaster product = productFacade.findByCode(productNumber);
        QuotationGiftVO giftVO = new QuotationGiftVO();
        giftVO.setDetailVO(detailVO);
        giftVO.setCategory(detailVO.getCategory());
        String[] categories = new String[1];
        categories[0] = product.getCategory();
        giftVO.setProductList(productFacade.findByCategories(categories));
        logger.debug("giftVO.getProductList().size()={}", giftVO.getProductList().size());
        gift.setProductNumber(productNumber);
        giftVO.setGift(gift);
        detailVO.getGiftVOList().add(giftVO);
        this.giftList.add(giftVO);
    }

    public String searchLastTwoOrder() {
        logger.debug("searchLastTwoOrder()");
        boolean customerRequired = true;
        boolean onlySalesCode = false;
        queryCriteriaController.latestStepToCheckCustomerCode(customerRequired, onlySalesCode);
        if (onlySalesCode
                && queryCriteriaController.isInvalidCustomerRelation()) {
        } else if (!(queryCriteriaController.isWrongCustomerCode()
                || queryCriteriaController.isCustomerNotExists())) {
            if (this.detailList == null) {
                this.detailList = new ArrayList();
            }
            if (this.giftList == null) {
                this.giftList = new ArrayList();
            }
            List<SalesDetailsVO> existsOrderDetailss = orderDetailsFacade.findByCriteria(queryCriteriaController.getFilter().getSales(), queryCriteriaController.getFilter().getSkCustomer().getSimpleCode(), null, null, "");
            HashMap<String, SkQuotationDetail> uniqueQuotationMap = new HashMap();
            HashMap<String, SkQuotationGift> uniqueGiftMap = new HashMap();
            Set<String> processedOrderSet = new HashSet();
            SkQuotationDetail detail = null;
            for (SalesDetailsVO vo : existsOrderDetailss) {
                if (vo.isGift()) {
                    if (detail != null) {
                        String key = detail.getProductNumber() + vo.getProductNumber();
                        SkQuotationGift gift = new SkQuotationGift();
                        if (uniqueGiftMap.containsKey(key)) {
                            continue;
//                            gift = uniqueGiftMap.get(key);
//                            uniqueGiftMap.remove(key);
                        }
                        logger.debug("detail={}", detail.getProductNumber());
                        gift.setQuotationDetail(detail);
                        gift.setProductNumber(vo.getProductNumber());
                        gift.setQuantity(vo.getQuantity());
                        logger.debug("vo.getSellingPrice()={}", vo.getSellingPrice());
                        gift.setPrice(detail.getPrice());
                        uniqueGiftMap.put(key, gift);
                    }
                } else {
                    logger.debug("process invoice={}", vo.getInvoiceNumber());
                    logger.debug("process order={}", vo.getOrderNumber());
                    logger.debug("processedOrderSet.size()={}", processedOrderSet.size());
                    if (processedOrderSet.size() >= 2) {
                        break;
                    }
                    processedOrderSet.add(vo.getOrderNumber());
                    if (uniqueQuotationMap.containsKey(vo.getProductNumber())) {
                        continue;
                    }
                    detail = new SkQuotationDetail();
                    detail.setProductNumber(vo.getProductNumber());
                    detail.setQuantity(vo.getQuantity());
                    detail.setPrice(vo.getSellingPrice());
                    detail.setPremiumDiscount(vo.getPremiumDiscount());
                    uniqueQuotationMap.put(detail.getProductNumber(), detail);
                }
            }
            HashMap<String, QuotationDetailVO> quotationDetailMap = new HashMap();
            List<QuotationDetailVO> addDetailList = new ArrayList();
            for (SkQuotationDetail d : uniqueQuotationMap.values()) {
                QuotationDetailVO vo = new QuotationDetailVO(d);
                vo.setIndex(latestIndex);
                SkProductMaster product = productFacade.findByCode(d.getProductNumber());
                if (product != null) {
                    vo.setCategory(product.getCategory());
                    String[] categories = new String[1];
                    categories[0] = product.getCategory();
                    List<SkProductMaster> productList = productFacade.findByCategories(categories);
                    Collections.sort(productList, new Comparator<SkProductMaster>() {
                        @Override
                        public int compare(SkProductMaster p1, SkProductMaster p2) {
                            return p1.getName().compareTo(p2.getName());
                        }
                    });
                    vo.setProductList(productList);
                }
                addDetailList.add(vo);
                quotationDetailMap.put(d.getProductNumber(), vo);
                latestIndex++;
            }
            List<QuotationGiftVO> addGiftList = new ArrayList();
            for (SkQuotationGift gift : uniqueGiftMap.values()) {
                QuotationGiftVO vo = new QuotationGiftVO();
                vo.setGift(gift);
                SkProductMaster product = productFacade.findByCode(gift.getProductNumber());
                if (product != null) {
                    vo.setCategory(product.getCategory());
                    String[] categories = new String[1];
                    categories[0] = product.getCategory();
                    vo.setProductList(productFacade.findByCategories(categories));
                }
                QuotationDetailVO detailVO = quotationDetailMap.get(gift.getQuotationDetail().getProductNumber());
                detailVO.getGiftVOList().add(vo);
                vo.setDetailVO(detailVO);
                addGiftList.add(vo);
            }
            if (this.detailList.size() + this.giftList.size() + addDetailList.size() + addGiftList.size() > 6) {
                JsfUtils.addErrorMessage(rb.getString("quotation.moreThen6ItemForAddLastTwoOrders"));
            } else {
                this.detailList.addAll(addDetailList);
                this.giftList.addAll(addGiftList);
            }
            calculateTotalAmount();
            retrieveLatestRemarks();
        }
        return null;
    }

    private void retrieveLatestRemarks() {
        if (selected != null) {
            SkQuotationMaster master = ejbFacade.findLatestQuotation(customerFacade.findBySimpleCode(selectCustomerController.getSelectedCustomer()));
            if (master != null) {
                boolean dataExists = false;
                if (StringUtils.isNotEmpty(selected.getRemark())
                        || StringUtils.isNotEmpty(selected.getRemark1())
                        || StringUtils.isNotEmpty(selected.getRemark2())
                        || StringUtils.isNotEmpty(selected.getRemark3())
                        || StringUtils.isNotEmpty(selected.getNote())) {
                    dataExists = true;
                }
                boolean hasData = false;
                if (StringUtils.isNotEmpty(master.getRemark())
                        || StringUtils.isNotEmpty(master.getRemark1())
                        || StringUtils.isNotEmpty(master.getRemark2())
                        || StringUtils.isNotEmpty(master.getRemark3())
                        || StringUtils.isNotEmpty(master.getNote())) {
                    hasData = true;
                }
                if (dataExists && !hasData) {
                    return;
                }
                selected.setRemark(master.getRemark());
                selected.setRemark1(master.getRemark1());
                selected.setRemark2(master.getRemark2());
                selected.setRemark3(master.getRemark3());
                selected.setNote(master.getNote());
            }
        }
    }

    public void calculateTotalAmount() {
        this.selected.setAmount(BigDecimal.ZERO);
        this.selected.setPremiumDiscount(BigDecimal.ZERO);
        this.selected.setTotalAmount(BigDecimal.ZERO);
        if (this.detailList != null) {
            for (QuotationDetailVO vo : this.detailList) {
                BigDecimal amount = BigDecimal.ZERO;
                SkQuotationDetail detail = vo.getDetail();
                if (detail.getPrice() != null && detail.getQuantity() != null) {
                    amount = detail.getPrice().multiply(detail.getQuantity());
                }
                BigDecimal premiumDiscount = detail.getPremiumDiscount() == null ? BigDecimal.ZERO : detail.getPremiumDiscount();
                this.selected.setAmount(this.selected.getAmount().add(amount));
                this.selected.setPremiumDiscount(this.selected.getPremiumDiscount().add(premiumDiscount));
                this.selected.setTotalAmount(this.selected.getTotalAmount().add(amount).add(premiumDiscount.negate()));
            }
        }
    }

    public String getProductName(String productNumber) {
        if (StringUtils.isNotEmpty(productNumber)) {
            return productFacade.findByCode(productNumber).getName();
        } else {
            return "";
        }
    }

    public void remove(QuotationDetailVO vo) {
        if (this.detailList != null) {
            this.detailList.remove(vo);
        }
        List<QuotationGiftVO> newGiftVOList = new ArrayList();
        if (this.giftList != null) {
            for (QuotationGiftVO giftVO : this.giftList) {
                if (giftVO.getDetailVO().equals(vo)) {
                    continue;
                }
                newGiftVOList.add(giftVO);
            }
        }
        this.giftList = newGiftVOList;
        calculateTotalAmount();
    }

    public void removeGift(QuotationGiftVO giftVO) {
        logger.debug("removeGift(), giftVO = {}", giftVO);
        if (this.giftList != null) {
            this.giftList.remove(giftVO);
        }
        if (giftVO.getDetailVO() != null) {
            giftVO.getDetailVO().getGiftVOList().remove(giftVO);
        }
    }

    public List<String> completeProductNumber(String productNumber) {
        logger.debug("completeProductNumber()");
        List<String> codes = new ArrayList<String>();
        List<SkProductMaster> products = productFacade.findByCode(productNumber, true);
        logger.debug("products.size()={}", products.size());
        for (SkProductMaster product : products) {
            codes.add(product.getCode());
        }
        logger.debug("codes.size()={}", codes.size());
        return codes;
    }

    public void changeCategory(QuotationDetailVO vo) {
        String[] categories = new String[1];
        if (StringUtils.isNotEmpty(vo.getCategory())) {
            categories[0] = vo.getCategory();
            List<SkProductMaster> productMasterList = productFacade.findByCategories(categories);
            Collections.sort(productMasterList, new Comparator<SkProductMaster>() {
                @Override
                public int compare(SkProductMaster p1, SkProductMaster p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
            vo.setProductList(productMasterList);
        } else {
            vo.setProductList(null);
        }
    }

    public void changeGiftCategory(QuotationGiftVO vo) {
        String[] categories = new String[1];
        if (StringUtils.isNotEmpty(vo.getCategory())) {
            logger.debug("vo.getCategory()={}", vo.getCategory());
            categories[0] = vo.getCategory();
            vo.setProductList(productFacade.findByCategories(categories));
        } else {
            vo.setProductList(null);
        }
    }

    public void changeProduct(QuotationDetailVO vo) {
        logger.debug("changeProduct(), index={}", vo.getIndex());
        List<SalesDetailsVO> orderList = null;
        boolean customerRequired = true;
        boolean onlySalesCode = false;
        queryCriteriaController.latestStepToCheckCustomerCode(customerRequired, onlySalesCode);
        logger.debug("sales={}", queryCriteriaController.getFilter().getSales());
        logger.debug("customer={}", queryCriteriaController.getFilter().getSkCustomer());
        if (queryCriteriaController.getFilter().getSales() != null && queryCriteriaController.getFilter().getSkCustomer() != null) {
            orderList = orderDetailsFacade.findByCriteria(queryCriteriaController.getFilter().getSales(), queryCriteriaController.getFilter().getSkCustomer().getSimpleCode(), null, null, vo.getDetail().getProductNumber());
        }
        if (orderList != null && !orderList.isEmpty()) {
            SalesDetailsVO detail = orderList.get(0);
            logger.debug("price={}", detail.getSellingPrice());
            vo.getDetail().setPrice(detail.getSellingPrice());
            vo.getDetail().setQuantity(detail.getQuantity());
            BigDecimal itemAmount = detail.getSellingPrice().multiply(detail.getQuantity());
            BigDecimal amount = this.selected.getAmount();
            this.selected.setAmount(amount == null ? itemAmount : amount.add(itemAmount));
            //TODO: 是否需從訂單帶入溢價折讓? premium_discount+premium_discount_tax?
            this.selected.setPremiumDiscount(BigDecimal.ZERO);
            BigDecimal totalAmount = this.selected.getTotalAmount();
            this.selected.setTotalAmount(totalAmount == null ? itemAmount : totalAmount.add(itemAmount));
            vo.setOrderNumber(detail.getOrderNumber());
        } else {
            vo.getDetail().setPrice(BigDecimal.ZERO);
            vo.getDetail().setQuantity(BigDecimal.ZERO);
            vo.setOrderNumber("");
        }
        changeGiftList(vo);
    }

    private void changeGiftList(QuotationDetailVO detailVO) {
        logger.debug("changeGiftList(), detailVO={}", detailVO);
        List<QuotationGiftVO> newGiftList = new ArrayList();
        boolean addGift = false;
        logger.debug("this.giftList.size()={}", this.giftList.size());
        for (QuotationGiftVO giftVO : this.giftList) {
            logger.debug("giftVO.getDetailVO().getIndex()={}, detailVO.getIndex()={}", new Object[]{giftVO.getDetailVO().getIndex(), detailVO.getIndex()});
            if (giftVO.getDetailVO().getIndex() == detailVO.getIndex()) {
                addGift = true;
                logger.debug("detailVO.getOrderNumber()={}", detailVO.getOrderNumber());
                if (StringUtils.isNotEmpty(detailVO.getOrderNumber())) {
                    newGiftList.addAll(fetchGift(detailVO));
                } else {
                    detailVO.setGiftVOList(new ArrayList());
                }
            } else {
                newGiftList.add(giftVO);
            }
        }
        logger.debug("addGift={}", addGift);
        if (!addGift) {
            newGiftList.addAll(fetchGift(detailVO));
        }
        this.giftList = newGiftList;
    }

    private List<QuotationGiftVO> fetchGift(QuotationDetailVO detailVO) {
        logger.debug("fetchGift(), detailVO={}", detailVO);
        List<QuotationGiftVO> giftVOList = new ArrayList();
        List<SalesDetailsVO> details = orderDetailsFacade.findByOrderNumber(detailVO.getOrderNumber());
        logger.debug("details.size()={}", details.size());
        if (!details.isEmpty()) {
            boolean fetch = false;
            for (SalesDetailsVO detail : details) {
                logger.debug("detail.getProductNumber()={},fetch={}", new Object[]{detail.getProductNumber(), fetch});
                if (!fetch && detail.getProductNumber().equals(detailVO.getDetail().getProductNumber())) {
                    fetch = true;
                    continue; //跳過第一筆非贈品
                }
                if (fetch && detail.isGift()) {
                    SkQuotationGift gift = new SkQuotationGift();
                    gift.setProductNumber(detail.getProductNumber());
                    gift.setQuantity(detail.getQuantity());
                    QuotationGiftVO newGiftVO = new QuotationGiftVO();
                    newGiftVO.setDetailVO(detailVO);
                    newGiftVO.setGift(gift);
                    SkProductMaster product = productFacade.findByCode(detail.getProductNumber());
                    newGiftVO.setCategory(product.getCategory());
                    String[] categories = new String[]{product.getCategory()};
                    newGiftVO.setProductList(productFacade.findByCategories(categories));
                    giftVOList.add(newGiftVO);
                } else if (fetch) {
                    break;
                }
            }
        }
        logger.debug("giftVOList.size()={}", giftVOList.size());
        detailVO.setGiftVOList(giftVOList);
        logger.debug("detailVO.getGiftVOList().size()={}", detailVO.getGiftVOList().size());
        return giftVOList;
    }

    public HtmlInputHidden getInitSalesAndCustomerHidden() {
        logger.debug("getInitSalesAndCustomerHidden(),selected={}", this.selected);
        String done = "done";
        if (!done.equals((String) initSalesAndCustomerHidden.getValue())) {
            if (this.selected != null && this.selected.getCustomer() != null) {
                SkCustomer customer = this.selected.getCustomer();
                logger.debug("customer={}", customer);
                selectCustomerController.setSelectedCustomer(customer.getSimpleCode());
                selectCustomerController.setName(customer.getName());
                SkSalesMember salesMember = salesMemberFacade.findByCode(customer.getSapid());
                queryCriteriaController.getFilter().setSales(salesMember);
            }
            initSalesAndCustomerHidden.setValue(done);
        }
        return initSalesAndCustomerHidden;
    }

    public void setInitSalesAndCustomerHidden(HtmlInputHidden initSalesAndCustomerHidden) {
        this.initSalesAndCustomerHidden = initSalesAndCustomerHidden;
    }

    public void resetCustomerCosignee(AjaxBehaviorEvent event) {
        selectConsigneeController.setSelectedConsignee("");
        selectConsigneeController.setName("");
        selectCustomerController.setSelectedCustomer("");
        selectCustomerController.setName("");
    }

    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public SkQuotationMaster getSelected() {
        return selected;
    }

    public void setSelected(SkQuotationMaster selected) {
        this.selected = selected;
    }

    public List<SkProductCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<SkProductCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public List<SkProductMaster> getProductList() {
        return productList;
    }

    public void setProductList(List<SkProductMaster> productList) {
        this.productList = productList;
    }

    public List<TcZtabExpTvv1t> getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(List<TcZtabExpTvv1t> remarkList) {
        this.remarkList = remarkList;
    }

    public List<QuotationGiftVO> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<QuotationGiftVO> giftList) {
        this.giftList = giftList;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<QuotationDetailVO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<QuotationDetailVO> detailList) {
        this.detailList = detailList;
    }
    //</editor-fold>
}

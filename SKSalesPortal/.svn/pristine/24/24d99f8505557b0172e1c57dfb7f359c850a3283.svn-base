package com.tcci.sksp.controller.quotation;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.*;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.quotation.SkQuotationMaster;
import com.tcci.sksp.entity.quotation.SkQuotationReviewHistory;
import com.tcci.sksp.entity.enums.QuotationReviewOptionEnum;
import com.tcci.sksp.entity.enums.QuotationStatusEnum;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.entity.quotation.SkQuotationDetail;
import com.tcci.sksp.entity.quotation.SkQuotationGift;
import com.tcci.sksp.facade.SkCustomerFacade;
import com.tcci.sksp.facade.SkQuotationFacade;
import com.tcci.sksp.facade.SkProductMasterFacade;
import com.tcci.sksp.facade.SkSalesChannelsFacade;
import com.tcci.sksp.facade.SkSalesDetailsFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import com.tcci.sksp.facade.TcZtabExpTvv1tFacade;
import com.tcci.sksp.vo.QuotationDetailVO;
import com.tcci.sksp.vo.QuotationMasterVO;
import com.tcci.sksp.vo.SalesDetailsVO;
import com.tcci.worklist.controller.util.JsfUtils;
import java.io.IOException;
import java.lang.String;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean(name = "queryQuotation")
@ViewScoped
public class QueryQuotationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ResourceBundle rb = ResourceBundle.getBundle("/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    private List<QuotationMasterVO> items;
    private SkQuotationReviewHistory selected = new SkQuotationReviewHistory();
    private QuotationCondition condition;
    private QuotationStatusEnum[] statusList = QuotationStatusEnum.values();
    private QuotationReviewOptionEnum[] reviewOptionList = null;
    private boolean selectAll;
    private List<String> areaList;
    private String selectedProduct;
    private List<SalesDetailsVO> orderList;
    private List<QuotationDetailVO> detailVOList;
    //<editor-fold desc="EJBs" defaultstate="collapsed">
    @EJB
    SkQuotationFacade ejbFacade;
    @EJB
    TcUserFacade userFacade;
    @EJB
    SkSalesMemberFacade memberFacade;
    @EJB
    SkCustomerFacade customerFacade;
    @EJB
    SkProductMasterFacade productFacade;
    @EJB
    SkSalesChannelsFacade channelsFacade;
    @EJB
    SkSalesDetailsFacade orderDetailsFacade;
    @EJB
    TcZtabExpTvv1tFacade remarkFacade;
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
    //</editor-fold>

    @PostConstruct
    public void init() {
        this.condition = new QuotationCondition();
        this.condition.getMaster().setStatus(QuotationStatusEnum.OPEN);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        this.condition.setBeginDate(now.getTime());
        this.selectAll = false;
        initChannelsList();
        String oid = JsfUtils.getRequestParameter("oid");
        this.items = new ArrayList();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(false);
        if (StringUtils.isNotEmpty(oid)) {
            try {
                SkQuotationMaster master = (SkQuotationMaster) ejbFacade.getObject(oid);
                if (master != null) {
                    this.items = new ArrayList();
                    this.items.add(new QuotationMasterVO(master));
                }
            } catch (Exception e) {
                logger.error("e={}", e);
            }
        }

    }

    //get area from salesList
    private void initChannelsList() {
        HashSet<String> areaSet = new HashSet();
        List<SkSalesMember> salesMembers = null;
        areaList = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
        if (request.isUserInRole(ConstantsUtil.ROLE_SALES)) {
            if (isSalesManager(userFacade.getSessionUser())) {
                logger.debug("manager");
                salesMembers = memberFacade.findByChannels(userFacade.getSessionUser(), false);
            } else {
                logger.debug("sales");
                salesMembers = memberFacade.findByChannels(userFacade.getSessionUser(), false);
            }
        } else {
            salesMembers = memberFacade.findAllSelectable();
        }
        logger.debug("salesMembers.size()={}", salesMembers.size());
        for (SkSalesMember salesMember : salesMembers) {
            String area = salesMember.getCode().substring(0, 2);
            logger.debug("area={}", area);
            areaSet.add(area);
        }
        areaList.addAll(areaSet);
        Collections.sort(areaList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
    }

    private boolean isSalesManager(TcUser user) {
        boolean isManager = false;
        SkSalesChannels channel = channelsFacade.findByManager(user);
        if (channel != null) {
            isManager = true;
        }
        return isManager;
    }

    public boolean isSalesManager() {
        return isSalesManager(userSession.getUser());
    }

    public String getAreaName(String code) {
        SkSalesChannels channel = channelsFacade.findByCode(code);
        return channel == null ? "" : " - " + channel.getName();
    }

    //get area from sales channel
//    private void initChannelsList() {
//        this.salesChannelList = new ArrayList();
//        for (SkSalesChannels channel : channelsFacade.findRootChannel()) {
//            salesChannelList.add(channel);
//            addChildChannels(channel, "&nbsp;&nbsp;&nbsp;");
//        }
//    }
//
//    private void addChildChannels(SkSalesChannels channel, String totalSpacer) {
//        String spacer = "&nbsp;&nbsp;&nbsp;";
//        List<SkSalesChannels> childChannelList = channelsFacade.findChildChannel(channel);
//        for (SkSalesChannels childChannel : childChannelList) {
//            childChannel.setName(totalSpacer + childChannel.getName());
//            salesChannelList.add(childChannel);
//            addChildChannels(childChannel, totalSpacer + spacer);
//        }
//    }
    public void query() {
        logger.debug("query()");
        boolean customerRequired = false;
        boolean onlySalesCode = false;
        queryCriteriaController.latestStepToCheckCustomerCode(customerRequired, onlySalesCode);
        if (queryCriteriaController.getFilter().getSales() != null) {
            this.condition.setSapid(queryCriteriaController.getFilter().getSales().getCode());
        } else {
            this.condition.setSapid("");
        }
        this.condition.setCustomer(queryCriteriaController.getFilter().getSkCustomer());
        List<String> sapidList = new ArrayList();
        logger.debug("queryCriteriaController.getSalesList()={}", queryCriteriaController.getSalesList());
        for (SkSalesMember salesMember : queryCriteriaController.getSalesList()) {
            logger.debug("salesMember={}", salesMember);
            sapidList.add(salesMember.getCode());
        }
        this.condition.setSapidList(sapidList);
        this.items = new ArrayList();
        for (SkQuotationMaster master : ejbFacade.findByCriteria(condition)) {
            this.items.add(new QuotationMasterVO(master));
        }
    }

    public void remove(QuotationMasterVO masterVO) {
        ejbFacade.remove(masterVO.getMaster());
        this.items.remove(masterVO);
        JsfUtils.addSuccessMessage(rb.getString("quotation.deleteSuccess"));
    }

    public void close() {
        List<QuotationMasterVO> selectedMasterList = getSelectedMasterList();
        if (selectedMasterList.isEmpty()) {
            JsfUtils.addErrorMessage(rb.getString("quotation.emptyListForClose"));
            return;
        }
        List<QuotationMasterVO> newItems = new ArrayList();
        for (QuotationMasterVO masterVO : selectedMasterList) {
            if (!masterVO.getMaster().getStatus().equals(QuotationStatusEnum.CLOSED)) {//處理選取的報價單
                SkQuotationMaster master = closeQuotation(masterVO);
                newItems.add(new QuotationMasterVO(master));
                continue;
            }
            newItems.add(masterVO);
        }
        this.items = newItems;
    }

    private List<QuotationMasterVO> getSelectedMasterList() {
        List<QuotationMasterVO> selectedMasterList = new ArrayList();
        for (QuotationMasterVO masterVO : this.items) {
            if (masterVO.isSelected()) {
                selectedMasterList.add(masterVO);
            }
        }
        return selectedMasterList;
    }

    private SkQuotationMaster closeQuotation(QuotationMasterVO masterVO) {
        SkQuotationMaster master = ejbFacade.findMaster(masterVO.getMaster().getId());
        try {
            if (!master.getStatus().equals(QuotationStatusEnum.CLOSED)) {
                master = ejbFacade.createSapQuotation(master,userSession.getUser());
                master = ejbFacade.edit(master);
            }
        } catch (Exception e) {
            logger.error("e={}", e);
            JsfUtils.addErrorMessage(e, "QueryQuotationController.closeQuotation()");
        }

        return master;
    }

    public void close(QuotationMasterVO masterVO) {
        SkQuotationMaster master = closeQuotation(masterVO);
        if (QuotationStatusEnum.CLOSED.equals(master.getStatus())) {
            JsfUtils.addSuccessMessage(MessageFormat.format(rb.getString("quotation.closeSuccessfully"), master.getQuotationNo()));
        } else {
            JsfUtils.addErrorMessage(MessageFormat.format(rb.getString("quotation.closeFailed"), master.getErrorMessage()));
        }

        List<QuotationMasterVO> newMasterVOList = new ArrayList();
        for (QuotationMasterVO mVO : items) {
            if (mVO.getMaster().equals(master)) {
                newMasterVOList.add(new QuotationMasterVO(master));
                continue;
            }
            newMasterVOList.add(mVO);
        }
        this.items = newMasterVOList;
    }

    private QuotationReviewOptionEnum[] prepareReviewOptionList(QuotationMasterVO masterVO) {
        QuotationReviewOptionEnum[] reviewOptionList = QuotationReviewOptionEnum.values();
        if (masterVO.getMaster().getStatus().equals(QuotationStatusEnum.CLOSED)) {
            QuotationReviewOptionEnum[] newReviewOptionList = new QuotationReviewOptionEnum[reviewOptionList.length - 1];
            int i = 0;
            for (QuotationReviewOptionEnum reviewOption : reviewOptionList) {
                if (reviewOption.equals(QuotationReviewOptionEnum.REJECT)) {
                    continue;
                }
                newReviewOptionList[i] = reviewOption;
                i++;
            }
            reviewOptionList = newReviewOptionList;
        }
        return reviewOptionList;

    }

    public void prepareReview(QuotationMasterVO masterVO) {
        logger.debug("prepareReview(),masterVO={}", masterVO);
        this.reviewOptionList = prepareReviewOptionList(masterVO);
        this.detailVOList = masterVO.getDetailVOList();
        SkQuotationMaster master = masterVO.getMaster();
        this.selected = new SkQuotationReviewHistory();
        this.selected.setQuotationMaster(master);
        this.selected.setReviewer(userSession.getUser());
        this.selected.setReviewDate(new Date());
        this.selected.setReviewOption(QuotationReviewOptionEnum.READED);
    }

    public void save() {
        //reject時, remark必填
        String error = "";
        if (QuotationReviewOptionEnum.REJECT.equals(this.selected.getReviewOption())) {
            if (StringUtils.isEmpty(this.selected.getRemark())) {
                error = rb.getString("quotation.remarkIsRequired");
            }
        }
        if (StringUtils.isNotEmpty(error)) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("errormsg", error);

            return;
        }

        //更新未結案訂單的狀態
        SkQuotationMaster master = this.selected.getQuotationMaster();
        if (!QuotationStatusEnum.CLOSED.equals(master.getStatus())) {
            if (QuotationReviewOptionEnum.READED.equals(this.selected.getReviewOption())) {
                master.setStatus(QuotationStatusEnum.READED);
            } else if (QuotationReviewOptionEnum.APPROVED.equals(this.selected.getReviewOption())) {
                master.setStatus(QuotationStatusEnum.APPROVED);
            } else if (QuotationReviewOptionEnum.REJECT.equals(this.selected.getReviewOption())) {
                master.setStatus(QuotationStatusEnum.REJECT);
            }
        }
        master.getReviewHistoryCollection().add(this.selected);
        master = ejbFacade.edit(master);
        List<QuotationMasterVO> newMasterVOList = new ArrayList();
        for (QuotationMasterVO mVO : this.items) {
            if (mVO.getMaster().equals(master)) {
                newMasterVOList.add(new QuotationMasterVO(master));
                continue;
            }
            newMasterVOList.add(mVO);
        }
        this.items = newMasterVOList;
        JsfUtils.addSuccessMessage(MessageFormat.format(rb.getString("quotation.reviewSuccess"), this.selected.getReviewOption().getDisplayName()));
    }

    public String getProductName(String productNumber) {
        if (StringUtils.isNotEmpty(productNumber)) {
            return productFacade.findByCode(productNumber).getName();
        } else {
            return "";
        }
    }

    public String getComments(QuotationMasterVO masterVO) {
        SkQuotationMaster master = masterVO.getMaster();
        StringBuilder comment = new StringBuilder();
        List<SkQuotationReviewHistory> sortedReviewHistory = new ArrayList();
        sortedReviewHistory.addAll(master.getReviewHistoryCollection());
        Collections.sort(sortedReviewHistory, new Comparator<SkQuotationReviewHistory>() {
            @Override
            public int compare(SkQuotationReviewHistory r1, SkQuotationReviewHistory r2) {
                return r1.getReviewDate().compareTo(r2.getReviewDate());
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        for (SkQuotationReviewHistory reviewHistory : sortedReviewHistory) {
            if (StringUtils.isNotEmpty(comment.toString())) {
                comment.append("\r\n");
            }
            comment.append(reviewHistory.getReviewer().getDisplayIdentifier())
                    .append(" ")
                    .append(reviewHistory.getReviewOption().getDisplayName())
                    .append(" 在 ")
                    .append(sdf.format(reviewHistory.getReviewDate()));

            if (StringUtils.isNotEmpty(reviewHistory.getRemark())) {
                comment.append(", 備註: ")
                        .append(reviewHistory.getRemark());
            }
        }
        return comment.toString();
    }

    public void changeSelectAll() {
        for (QuotationMasterVO masterVO : this.items) {
            masterVO.setSelected(this.selectAll);
        }
    }

    private void exportExcelWithPOI(HSSFWorkbook wb, String filename) throws IOException {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        ServletOutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
        out.close();

        FacesContext faces = FacesContext.getCurrentInstance();
        faces.responseComplete();
    }

    public void exportExcel() {
        try {
            List<QuotationMasterVO> selectedMasterVOList = getSelectedMasterList();
            if (selectedMasterVOList.isEmpty()) {
                JsfUtils.addErrorMessage(rb.getString("quotation.emptyListForPrint"));
                return;
            }
            exportExcelWithPOI(generateReport(selectedMasterVOList), "quotation.xls");
        } catch (Exception e) {
            logger.debug("e={}", e);
            FacesMessage fMsg = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, fMsg);
        }

    }

    private HSSFWorkbook generateReport(List<QuotationMasterVO> selectedMasterVOList) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row;
        HSSFCell cell;
        CellRangeAddress cellRange;

        //產生表頭
        generateHeaders(sheet);

        //產生明細
        generateDetails(sheet, selectedMasterVOList);

        return wb;
    }

    private void generateHeaders(HSSFSheet sheet) {
        HSSFCell cell;
        HSSFRow firstRow = sheet.createRow(0);
        firstRow.createCell(0);
        cell = firstRow.createCell(0);
        cell.setCellValue(rb.getString("quotation"));
        HSSFCellStyle alignCenterStyle = sheet.getWorkbook().createCellStyle();
        alignCenterStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cell.setCellStyle(alignCenterStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

        HSSFRow row = sheet.createRow(1);
        int colIndex = 0;
        //焚化廠
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("customer.code"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("customer.name"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("quotation.productNumber"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("quotation.productName"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("quotation.quantity"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("quotation.price"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("quotation.amount"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("premium.discount"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("gift"));
        colIndex++;
        cell = row.createCell(colIndex);
        cell.setCellValue(rb.getString("quotation.totalAmount"));
        colIndex++;
    }

    private void generateDetails(HSSFSheet sheet, List<QuotationMasterVO> selectedMasterVOList) {
        int rowIndex = 2;
        HSSFRow row;
        HSSFCell cell;
        HSSFCellStyle wrapTextStyle = sheet.getWorkbook().createCellStyle();
        wrapTextStyle.setWrapText(true);
        for (QuotationMasterVO selectedMasterVO : selectedMasterVOList) {
            SkQuotationMaster master = selectedMasterVO.getMaster();
            for (SkQuotationDetail detail : master.getDetailCollection()) {
                row = sheet.createRow(rowIndex);
                int colIndex = 0;
                //客戶編號
                cell = row.createCell(colIndex);
                cell.setCellValue(master.getCustomer().getSimpleCode());
                colIndex++;

                //客戶名稱
                cell = row.createCell(colIndex);
                cell.setCellValue(master.getCustomer().getName());
                colIndex++;

                //藥品編號
                cell = row.createCell(colIndex);
                cell.setCellValue(detail.getProductNumber());
                colIndex++;

                //藥品名稱
                cell = row.createCell(colIndex);
                cell.setCellValue(getProductName(detail.getProductNumber()));
                colIndex++;

                //數量
                cell = row.createCell(colIndex);
                cell.setCellValue(detail.getQuantity().doubleValue());
                colIndex++;

                //單價
                cell = row.createCell(colIndex);
                cell.setCellValue(detail.getPrice().doubleValue());
                colIndex++;

                //總價
                cell = row.createCell(colIndex);
                BigDecimal amount = detail.getQuantity().multiply(detail.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP);
                cell.setCellValue(amount.doubleValue());
                colIndex++;

                //溢價折讓
                cell = row.createCell(colIndex);
                BigDecimal premiumDiscount = BigDecimal.ZERO;
                if (detail.getPremiumDiscount() != null) {
                    premiumDiscount = detail.getPremiumDiscount();
                }
                cell.setCellValue(premiumDiscount.doubleValue());
                colIndex++;

                //贈品
                if (detail.getGiftList().size() > 0) {
                    cell = row.createCell(colIndex);
                    cell.setCellValue("V");
                }
                colIndex++;

                //實際金額
                cell = row.createCell(colIndex);
                BigDecimal totalAmount = amount.add(premiumDiscount.negate()).setScale(0, BigDecimal.ROUND_HALF_UP);
                cell.setCellValue(totalAmount.doubleValue());
                colIndex++;
                rowIndex++;

                //贈品
                if (detail.getGiftList().size() > 0) {
                    colIndex = 2;
                    for (SkQuotationGift gift : detail.getGiftList()) {
                        row = sheet.createRow(rowIndex);
                        //贈品藥品編號
                        cell = row.createCell(colIndex);
                        cell.setCellValue(gift.getProductNumber());
                        colIndex++;

                        //贈品藥品名稱
                        cell = row.createCell(colIndex);
                        cell.setCellValue(getProductName(gift.getProductNumber()));
                        colIndex++;

                        //贈品數量
                        cell = row.createCell(colIndex);
                        cell.setCellValue(gift.getQuantity().doubleValue());
                        colIndex++;

                        //贈品價格
                        cell = row.createCell(colIndex);
                        cell.setCellValue(gift.getPrice().doubleValue());
                        colIndex++;

                        //贈品總價
                        cell = row.createCell(colIndex);
                        cell.setCellValue(gift.getQuantity().multiply(gift.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
                        colIndex++;

                        //贈品溢價折讓
                        cell = row.createCell(colIndex);
                        cell.setCellValue(gift.getQuantity().multiply(gift.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
                        colIndex++;

                        colIndex++;

                        //贈品實際金額
                        cell = row.createCell(colIndex);
                        cell.setCellValue(rb.getString("gift"));
                        colIndex++;

                        rowIndex++;
                    }
                }
            }

            //備註
            row = sheet.createRow(rowIndex);
            row.setHeight((short) 1500);
            int colIndex = 3;
            cell = row.createCell(colIndex);
            StringBuilder remark = new StringBuilder();
            remark.append(StringUtils.isEmpty(master.getRemark1()) ? "" : getRemark(master.getRemark1()).concat("\n"));
            remark.append(StringUtils.isEmpty(master.getRemark2()) ? "" : getRemark(master.getRemark2()).concat("\n"));
            remark.append(StringUtils.isEmpty(master.getRemark3()) ? "" : getRemark(master.getRemark3()).concat("\n"));
            remark.append(StringUtils.isEmpty(master.getRemark()) ? "" : master.getRemark());
            cell.setCellValue(remark.toString());
            cell.setCellStyle(wrapTextStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 3, 9));
            rowIndex++;

            //悄悄話
            row = sheet.createRow(rowIndex);
            row.setHeight((short) 1500);
            colIndex = 3;
            cell = row.createCell(colIndex);
            cell.setCellValue(master.getNote());
            cell.setCellStyle(wrapTextStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 3, 9));
            rowIndex++;
        }

        //set column width
        sheet.setColumnWidth(0, 2400);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 3200);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 2000);
        sheet.setColumnWidth(5, 2400);
        sheet.setColumnWidth(6, 2400);
        sheet.setColumnWidth(7, 2400);
        sheet.setColumnWidth(8, 1200);
        sheet.setColumnWidth(9, 2400);
    }

    public boolean isErrorMessageRendered(QuotationMasterVO masterVO) {
        return StringUtils.isNotEmpty(masterVO.getMaster().getErrorMessage());
    }

    public void prepareTransaction(SkQuotationDetail detail) {
        logger.debug("prepareTransaction, detail={}", detail);
        this.selectedProduct = detail.getProductNumber();
        SkCustomer customer = detail.getQuotationMaster().getCustomer();
        this.orderList = new ArrayList();
        this.orderList = orderDetailsFacade.findHistory(customer.getSapid(),customer.getSimpleCode(),null,null,new String[] {selectedProduct},true,0,3);
        logger.debug("this.orderList.size()={}", this.orderList.size());
    }

    public void edit(QuotationMasterVO masterVO) {
        String oid = masterVO.getMaster().toString();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editQuotation.xhtml?oid=" + oid);
        } catch (Exception e) {
            logger.error("e={}", e);
            JsfUtils.addErrorMessage(e, "QueryQuotationController.edit()");
        }
    }

    public String getRemark(String kvgr1) {
        return remarkFacade.findByKvgr1(kvgr1);
    }

    public boolean canEdit(QuotationMasterVO masterVO) {
        boolean canEdit = false;
        if (masterVO != null) {
            SkQuotationMaster master = masterVO.getMaster();
            if (master != null) {
                if (!QuotationStatusEnum.CLOSED.equals(master.getStatus())) {
                    if (master.getCreator().equals(userSession.getUser())
                            || FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Administrators")) {
                        canEdit = true;
                    }
                }
            }
        }
        return canEdit;
    }

    public String getSalesMember(String sapid) {
        String result = "";
        logger.debug("sapid={}",sapid);
        if(StringUtils.isNotEmpty(sapid)) {
        result = memberFacade.findByCode(sapid).getDisplayIdentifier();
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public List<QuotationMasterVO> getItems() {
        return items;
    }

    public void setItems(List<QuotationMasterVO> items) {
        this.items = items;
    }

    public QuotationCondition getCondition() {
        return condition;
    }

    public void setCondition(QuotationCondition condition) {
        this.condition = condition;
    }

    public SkQuotationReviewHistory getSelected() {
        return selected;
    }

    public void setSelected(SkQuotationReviewHistory selected) {
        this.selected = selected;
    }

    public QuotationReviewOptionEnum[] getReviewOptionList() {
        return reviewOptionList;
    }

    public void setReviewOptionList(QuotationReviewOptionEnum[] reviewOptionList) {
        this.reviewOptionList = reviewOptionList;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public List<String> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<String> areaList) {
        this.areaList = areaList;
    }

    public QuotationStatusEnum[] getStatusList() {
        return statusList;
    }

    public void setStatusList(QuotationStatusEnum[] statusList) {
        this.statusList = statusList;
    }

    public String getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(String selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<SalesDetailsVO> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<SalesDetailsVO> orderList) {
        this.orderList = orderList;
    }

    public List<QuotationDetailVO> getDetailVOList() {
        return detailVOList;
    }

    public void setDetailVOList(List<QuotationDetailVO> detailVOList) {
        this.detailVOList = detailVOList;
    }
    //</editor-fold>
}

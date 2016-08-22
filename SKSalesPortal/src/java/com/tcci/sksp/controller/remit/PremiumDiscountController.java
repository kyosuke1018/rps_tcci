package com.tcci.sksp.controller.remit;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.FiInterfaceController;
import com.tcci.sksp.controller.util.PremiumDiscountDataModel;
import com.tcci.sksp.controller.util.SelectEmployeeController;
import com.tcci.sksp.entity.ar.SkPremiumDiscount;
import com.tcci.sksp.facade.PremiumDiscountFilter;
import com.tcci.sksp.facade.SkFiMasterInterfaceFacade;
import com.tcci.sksp.facade.SkPremiumDiscountFacade;
import com.tcci.sksp.vo.PremiumDiscountVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class PremiumDiscountController {

    //<editor-fold defaultstate="collapsed" desc="parameters">
    protected final static Logger logger = LoggerFactory.getLogger(PremiumDiscountController.class);
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
    private PremiumDiscountFilter filter = new PremiumDiscountFilter();
    private PremiumDiscountVO discoutVO;
    private List<PremiumDiscountVO> discountVOs = new ArrayList<PremiumDiscountVO>();
    private boolean selectAll = false;
    private Date transactionDate;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    //@EJB
    //SkSalesOrderFacade salesOrderFacade;
    @EJB
    SkPremiumDiscountFacade facade;
    @EJB
    SkFiMasterInterfaceFacade interfaceFacade;
    @EJB
    TcUserFacade userFacade;
    @ManagedProperty(value = "#{selectEmployeeController}")
    SelectEmployeeController selectEmployeeController;

    public void setSelectEmployeeController(SelectEmployeeController selectEmployeeController) {
        this.selectEmployeeController = selectEmployeeController;
    }
    
    @ManagedProperty(value="#{fiInterfaceController}")
    private FiInterfaceController fiInterfaceController;

    public void setFiInterfaceController(FiInterfaceController fiInterfaceController) {
        this.fiInterfaceController = fiInterfaceController;
    }
    
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">

    public PremiumDiscountVO getDiscoutVO() {
        return discoutVO;
    }

    public void setDiscoutVO(PremiumDiscountVO discoutVO) {
        this.discoutVO = discoutVO;
    }

    public List<PremiumDiscountVO> getDiscountVOs() {
        return discountVOs;
    }

    public void setDiscountVOs(List<PremiumDiscountVO> discountVOs) {
        this.discountVOs = discountVOs;
    }

    public PremiumDiscountFilter getFilter() {
        return filter;
    }

    public void setFilter(PremiumDiscountFilter filter) {
        this.filter = filter;
    }

    public PremiumDiscountDataModel getPremiumDiscountDataModel() {
        return new PremiumDiscountDataModel(getDiscountVOs());
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    //</editor-fold>
    @PostConstruct
    private void init() {
        logger.debug("init()");
        filter.setBeginDate(null);
        filter.setEndDate(null);
        Date now = new Date();
        filter.setBeginModifytimestamp(now);
        filter.setEndModifytimestamp(now);
        filter.setNumber("");
        transactionDate = now;
    }

    public void query() {
        logger.debug("query()");
        //modifier
        String empId = "";
        if (selectEmployeeController.getSelectedUser() != null) {
            empId = selectEmployeeController.getSelectedUser();
        }
        logger.debug("empId={}", empId);
        if (!StringUtils.isEmpty(empId)) {
            TcUser user = userFacade.findUserByEmpId(empId);
            logger.debug("user={}", user);
            if (user == null) {
                user = new TcUser();
                user.setCname("維護者不存在!");
            }
            filter.setModifier(user);
            selectEmployeeController.setCname(user.getCname());
        } else {
            filter.setModifier(null);
            selectEmployeeController.setCname("");
        }
        //set end date & end modifytimestamp.
        if (filter.getEndDate() != null) {
            Calendar endDate = new GregorianCalendar();
            endDate.setTime(filter.getEndDate());
            endDate.add(Calendar.DATE, 1);
            filter.setEndDate(endDate.getTime());
        }
        if (filter.getEndModifytimestamp() != null) {
            Calendar endModifytimestamp = new GregorianCalendar();
            endModifytimestamp.setTime(filter.getEndModifytimestamp());
            endModifytimestamp.add(Calendar.DATE, 1);
            filter.setEndModifytimestamp(endModifytimestamp.getTime());
        }
        discountVOs.clear();
        List<SkPremiumDiscount> discounts = facade.findByCriteria(filter);
        for (SkPremiumDiscount discount : discounts) {
            PremiumDiscountVO vo = new PremiumDiscountVO();
            vo.setDiscount(discount);
            if (vo.getDiscount().getFiInterface() == null) {
                vo.setSelected(true);
            }
            discountVOs.add(vo);
        }
    }

    public void delete() {
        logger.debug("delete(), discount={}", discoutVO);
        try {
            facade.remove(discoutVO.getDiscount());
            discountVOs.remove(discoutVO);
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    rb.getString("premiumDiscount.info.deleteSuccess"),
                    rb.getString("premiumDiscount.info.deleteSuccess"));
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            logger.error("delete fail! e={}", e);
            FacesMessage errorMessage = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    rb.getString("premiumDiscount.error.deleteFail"),
                    rb.getString("premiumDiscount.error.deleteFail"));
            FacesContext.getCurrentInstance().addMessage(null, errorMessage);
        }
    }

    public void setDiscountToDelete(PremiumDiscountVO vo) {
        logger.debug("setDiscountToDelete(), discount={}", vo);
        this.discoutVO = vo;
    }

    public void selectAllChange(AjaxBehaviorEvent event) {
        for (PremiumDiscountVO discountVO : discountVOs) {
            discountVO.setSelected(selectAll);
        }
    }

    public void prepareUpload() {
        if (transactionDate == null) {
            String errorMessage = "請選擇交易日期!";
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage);
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("errormsg", errorMessage);
            return;
        }
        for(PremiumDiscountVO vo: discountVOs) {
            vo.getDiscount().setCreatetimestamp(transactionDate);
        }
        fiInterfaceController.prepareUpload(discountVOs);
    }
}

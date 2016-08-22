package com.tcci.sksp.controller.remit;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.FiInterfaceController;
import com.tcci.sksp.controller.util.RemitMasterDataModel;
import com.tcci.sksp.controller.util.SelectEmployeeController;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.enums.PaymentTypeEnum;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.facade.RemitFilter;
import com.tcci.sksp.facade.SkArRemitItemFacade;
import com.tcci.sksp.facade.SkArRemitMasterFacade;
import com.tcci.sksp.facade.SkFiDetailInterfaceFacade;
import com.tcci.sksp.facade.SkFiMasterInterfaceFacade;
import com.tcci.sksp.vo.Interfaceable;
import com.tcci.sksp.vo.RemitMasterVO;
import com.tcci.sksp.vo.Selectable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
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
public class UploadRemitController {
    //<editor-fold defaultstate="collapsed" desc="parameters">

    protected final static Logger logger = LoggerFactory.getLogger(RemitQueryController.class);
    private RemitFilter filter = new RemitFilter();
    private RemitMasterStatusEnum[] statuses = new RemitMasterStatusEnum[]{
        RemitMasterStatusEnum.REVIEWED,
        RemitMasterStatusEnum.TRANSFER_OK,
        RemitMasterStatusEnum.TRANSFER_FAILED,
        RemitMasterStatusEnum.TRANSFER_SAP
    };
    private List<SelectItem> statusList = new ArrayList<SelectItem>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
//    @EJB
//    private SkArRemitInterfaceFacade facade;
    @EJB
    SkArRemitMasterFacade facade;
    @EJB
    SkFiMasterInterfaceFacade interfaceFacade;
    @EJB
    SkFiDetailInterfaceFacade detailInterfaceFacade;
    @EJB
    private TcUserFacade userFacade;
    @EJB
    private SkArRemitItemFacade remitItemFacade;
    @ManagedProperty(value = "#{selectEmployeeController}")
    SelectEmployeeController selectEmployeeController;

    public void setSelectEmployeeController(SelectEmployeeController selectEmployeeController) {
        this.selectEmployeeController = selectEmployeeController;
    }
    @ManagedProperty(value = "#{remitQueryController}")
    RemitQueryController remitQueryController;

    public void setRemitQueryController(RemitQueryController remitQueryController) {
        this.remitQueryController = remitQueryController;
    }
    @ManagedProperty(value = "#{fiInterfaceController}")
    private FiInterfaceController fiInterfaceController;

    public void setFiInterfaceController(FiInterfaceController fiInterfaceController) {
        this.fiInterfaceController = fiInterfaceController;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">

    public RemitFilter getFilter() {
        return filter;
    }

    public void setFilter(RemitFilter filter) {
        this.filter = filter;
    }

    public List<SelectItem> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<SelectItem> statusList) {
        this.statusList = statusList;
    }
    //</editor-fold>

    @PostConstruct
    private void init() {
        logger.debug("init()");
        Date now = new Date();
        filter = new RemitFilter();
        filter.setReviewDateStart(now);
        filter.setReviewDateEnd(now);
        filter.setStatus(RemitMasterStatusEnum.REVIEWED);
        statusList = new ArrayList<SelectItem>();
        for (RemitMasterStatusEnum statusEnum : statuses) {
            statusList.add(new SelectItem(statusEnum, statusEnum.getDisplayName()));
        }
    }

    public void query() {
        logger.debug("query(), beginDate={}, endDate={}", new Object[]{filter.getReviewDateStart(), filter.getReviewDateEnd()});
        if (!StringUtils.isEmpty(selectEmployeeController.getSelectedUser())) {
            TcUser user = userFacade.findUserByEmpId(selectEmployeeController.getSelectedUser());
            if (user != null) {
            } else {
                user = new TcUser();
                user.setCname("覆核人不存在!");
            }
            selectEmployeeController.setCname(user.getCname());
            filter.setFinanceReviewer(user);
        } else {
            selectEmployeeController.setCname("");
            filter.setFinanceReviewer(null);
        }
        if (filter.getStatus() == null) {
            List<RemitMasterStatusEnum> allStatusList = new ArrayList<RemitMasterStatusEnum>();
            for (RemitMasterStatusEnum statusEnum : statuses) {
                allStatusList.add(statusEnum);
            }

            filter.setStatusList(allStatusList);
        }
        logger.debug("filter={}", filter);
        //search by Not Yet
        List<SkArRemitMaster> masters = facade.findRemitMasterByCriteria(filter);
        List<RemitMasterVO> masterVOs = new ArrayList<RemitMasterVO>();
        logger.debug("masters.size()={}", masters.size());
        if (masters.size() > 0) {
            remitQueryController.setSelectAll(true);
        }
        for (SkArRemitMaster master : masters) {
            boolean selected = true;
            if (master.getFiInterface() != null) {
                selected = false;
                remitQueryController.setSelectAll(false);
            }
            RemitMasterVO vo = new RemitMasterVO();
            vo.setRemitMaster(master);
            vo.setSelected(selected);
            masterVOs.add(vo);
        }
        logger.debug("masterVOs.size()={}", masterVOs.size());
        remitQueryController.setRemitMasterVOList(masterVOs);
        remitQueryController.setMasterDataModel(new RemitMasterDataModel(masterVOs));
    }

    public void prepareUpload(List referenceObjectVOList) {
        //check check number used by other remit but not selected.
        HashMap<Long, Long> choosedRemitMasters = new HashMap<Long, Long>();
        HashMap<String, String> checkNumbers = new HashMap<String, String>();
        if (referenceObjectVOList != null && !referenceObjectVOList.isEmpty()) {
            Object object = referenceObjectVOList.get(0);
            if (object instanceof Interfaceable) {
                //<editor-fold defaultstate="collapsed" desc="debug message">
                logger.debug("is interfaceable");
                //</editor-fold>
                for (Object referenceObject : referenceObjectVOList) {
                    if (referenceObject instanceof Selectable && ((Selectable) referenceObject).isSelected()) {
                        //<editor-fold defaultstate="collapsed" desc="debug message">
                        logger.debug("selected");
                        //</editor-fold>
                        Interfaceable interfaceable = (Interfaceable) referenceObject;
                        Persistable persistable = interfaceable.getPersistable();
                        if (persistable instanceof SkArRemitMaster) {
                            SkArRemitMaster remitMaster = (SkArRemitMaster) persistable;
                            choosedRemitMasters.put(remitMaster.getId(), remitMaster.getId());
                            for (SkArRemitItem remitItem : remitMaster.getSkArRemitItemCollection()) {
                                if (PaymentTypeEnum.CHECK.equals(remitItem.getPaymentType())) {
                                    checkNumbers.put(remitItem.getCheckNumber(), remitItem.getCheckNumber());
                                }
                                if (PaymentTypeEnum.CHECK.equals(remitItem.getPaymentType2())) {
                                    checkNumbers.put(remitItem.getCheckNumber2(), remitItem.getCheckNumber2());
                                }                                
                            }
                        }
                    }
                }
            }
        }
        HashMap<String, Set<Long>> unchooseRemitMasters = new HashMap<String, Set<Long>>();
        for (String checkNumber : checkNumbers.values()) {
            List<SkArRemitItem> remitItems = remitItemFacade.findByCheckNumber(checkNumber);
            Set<Long> unchooseRemitMasterList = new HashSet<Long>();
            for (SkArRemitItem remitItem : remitItems) {
                //{{ Jimmy, issue#20120516, issue#2012053001
                if (remitItem.getArRemitMaster().getStatus().equals(RemitMasterStatusEnum.CANCELED) ||
                    remitItem.getArRemitMaster().getStatus().equals(RemitMasterStatusEnum.TRANSFER_OK))
                    continue;
                //}}
                if (!choosedRemitMasters.containsKey(remitItem.getArRemitMaster().getId())) {
                    unchooseRemitMasterList.add(remitItem.getArRemitMaster().getId());
                }
            }
            if (!unchooseRemitMasterList.isEmpty()) {
                unchooseRemitMasters.put(checkNumber, unchooseRemitMasterList);
            }
        }
        if (unchooseRemitMasters.isEmpty()) {
            fiInterfaceController.prepareUpload(referenceObjectVOList);
        } else {
            String errorMessage = "所選擇的支票尚有解繳單尚未選擇一併上傳.<br/><br/>明細如下:<br/>";
            for (String unchooseRemitMasterKey : unchooseRemitMasters.keySet()) {
                String idString = "";
                for (Long unchooseRemitMasterId : unchooseRemitMasters.get(unchooseRemitMasterKey)) {
                    if (StringUtils.isNotEmpty(idString)) {
                        idString += ",";
                    }
                    idString += unchooseRemitMasterId.toString();
                }
                errorMessage += "支票號碼 - " + unchooseRemitMasterKey + " : 解繳單號碼 " + idString + ".\n";
            }
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage);
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("errormsg", errorMessage);
        }
    }
}
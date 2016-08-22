package com.tcci.sksp.controller.util;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.sapproxy.PpProxy;
import com.tcci.sapproxy.dto.SapProxyResponseDto;
import com.tcci.sapproxy.jco.JcoUtils;
import com.tcci.sksp.controller.remit.RemitQueryController;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.*;
import com.tcci.sksp.entity.enums.AdvanceRemitTypeEnum;
import com.tcci.sksp.entity.enums.InterfaceMasterStatusEnum;
import com.tcci.sksp.entity.enums.RemitMasterStatusEnum;
import com.tcci.sksp.facade.*;
import com.tcci.sksp.vo.AdvancePaymentVO;
import com.tcci.sksp.vo.FiInterfaceVO;
import com.tcci.sksp.vo.Interfaceable;
import com.tcci.sksp.vo.PremiumDiscountVO;
import com.tcci.sksp.vo.RemitMasterVO;
import com.tcci.sksp.vo.Selectable;
import com.tcci.worklist.controller.util.JsfUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class FiInterfaceController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    @Resource(mappedName = "jndi/sk.config")
    transient private Properties jndiConfig;
    private Logger logger = LoggerFactory.getLogger(FiInterfaceController.class);
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
    private List<FiInterfaceVO> interfaceVOList;
    Long mergeMasterInterfaceKeyIndex = null;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;

    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }
    @ManagedProperty(value = "#{remitQueryController}")
    private RemitQueryController remitQueryController;

    public void setRemitQueryController(RemitQueryController remitQueryController) {
        this.remitQueryController = remitQueryController;
    }
    @EJB
    private SkFiMasterInterfaceFacade facade;
    @EJB
    private SkFiDetailInterfaceFacade detailFacade;
    @EJB
    private SkArRemitMasterFacade remitFacade;
    @EJB
    private SkAdvanceRemitFacade advanceRemitFacade;
    @EJB
    private SkAdvancePaymentFacade advancePaymentFacade;
    @EJB
    private SkPremiumDiscountFacade premiumDiscountFacade;
    @EJB
    private SkCheckMasterFacade checkFacade;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">

    public List<FiInterfaceVO> getInterfaceVOList() {
        return interfaceVOList;
    }

    public void setInterfaceVOList(List<FiInterfaceVO> interfaceVOList) {
        this.interfaceVOList = interfaceVOList;
    }

    //</editor-fold>
    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        String[] title = {
            rb.getString("fiinterface.column.id"),
            rb.getString("fiinterface.column.item"),
            rb.getString("fiinterface.column.date"),
            rb.getString("fiinterface.column.type"),
            rb.getString("fiinterface.column.generalledgercode"),
            rb.getString("fiinterface.column.summonscode"),
            rb.getString("fiinterface.column.customernumber"),
            rb.getString("fiinterface.column.invoicenumber"),
            rb.getString("fiinterface.column.ordernumber"),
            rb.getString("fiinterface.column.salesgroup"),
            rb.getString("fiinterface.column.amount"),
            rb.getString("fiinterface.column.quantity"),
            rb.getString("fiinterface.column.checknumber"),
            rb.getString("fiinterface.column.checkduedate"),
            rb.getString("fiinterface.column.checkbank"),
            rb.getString("fiinterface.column.checkaccount"),
            rb.getString("fiinterface.column.owner"),
            rb.getString("fiinterface.column.status")
        };
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            header.getCell(i).setCellStyle(cellStyle);
            header.getCell(i).setCellValue(title[i]);
        }
    }

    public void upload() {
        try {
            //save first to get actual transaction no (SkFiMasterInterface.getId()).
            Date now = new Date();
            Map<String, SkFiMasterInterface> processedMasters = new HashMap<String, SkFiMasterInterface>();
            Object referenceObject = null;
            logger.debug("interfaceVOList={}", interfaceVOList);
            SkFiMasterInterface originalMaster = null;
            for (FiInterfaceVO vo : interfaceVOList) {
                SkFiDetailInterface detail = vo.getDetail();
                SkFiMasterInterface master = detail.getMaster();
                detail.setMaster(null);
                detail = detailFacade.editAndReturn(detail);
                String key = master.getReferenceclassname() + ":" + master.getReferenceid();

                if (!processedMasters.containsKey(key)) {
                    master.setStatus(InterfaceMasterStatusEnum.TRANSFER_SAP);
                    if (master.getId() == null) {
                        master.setTransactionNo(facade.getTransactionNextNo());
                        master.setCreator(userSession.getUser());
                        master.setCreatetimestamp(now);
                    }
                    master.setSkFiDetailInterfaceCollection(null);
                    master = facade.editAndReturn(master);
                    //update reference object.
                    //--Begin--Modified by nEO Fu on 20120419 1 interface master to n remit master.
//                    referenceObject = facade.getObject(master.getReferenceclassname() + ":" + master.getReferenceid());
//
//                    //只有繳款單需要更新狀態為 TRANSFER_SAP, 因為繳款單會多人同時存取.
//                    if (referenceObject instanceof SkArRemitMaster) {
//                        SkArRemitMaster remitMaster = (SkArRemitMaster) referenceObject;
//                        remitMaster.setStatus(RemitMasterStatusEnum.TRANSFER_SAP);
//                        remitFacade.edit(remitMaster);
//                    }
                    if (master.getGeneratedBy() != null) {
                        for (String className : master.getGeneratedBy().keySet()) {
                            List<Long> ids = master.getGeneratedBy().get(className);
                            for (Long id : ids) {
                                referenceObject = facade.getObject(className + ":" + id);
                                if (referenceObject instanceof SkArRemitMaster) {
                                    SkArRemitMaster remitMaster = (SkArRemitMaster) referenceObject;
                                    remitMaster.setStatus(RemitMasterStatusEnum.TRANSFER_SAP);
                                    remitMaster.setFiInterface(master);
                                    remitFacade.edit(remitMaster);
                                } else if (referenceObject instanceof SkAdvancePayment) {
                                    SkAdvancePayment advancePayment = (SkAdvancePayment) referenceObject;
                                    advancePayment.setFiInterface(master);
                                    advancePaymentFacade.edit(advancePayment);
                                } else if (referenceObject instanceof SkPremiumDiscount) {
                                    SkPremiumDiscount premiumDiscount = (SkPremiumDiscount) referenceObject;
                                    premiumDiscount.setFiInterface(master);
                                    premiumDiscountFacade.edit(premiumDiscount);
                                }
                            }
                        }
                    }
                    //---End---Modified by nEO Fu on 20120419 1 interface master to n remit master.
                    originalMaster = master;
                    processedMasters.put(key, master);

                }
                originalMaster.getSkFiDetailInterfaceCollection().add(detail);
                originalMaster = facade.editAndReturn(originalMaster);
                detail.setMaster(originalMaster);
                detail = detailFacade.editAndReturn(detail);
                vo.setDetail(detail);
            }

//            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig); //取得相關Jco連結參數
//            PpProxy ppProxy = PpProxyFactory.createProxy(jcoProp);//建立連線
            PpProxy ppProxy = JcoUtils.getSapProxy("sking", sessionController.getUser().getLoginAccount());
            SapProxyResponseDto returnResult = ppProxy.doUpload(interfaceVOList);
            if (returnResult != null) {
                List<Map<String, Object>> results = returnResult.getResultAsMapList();
                if (results != null) {
                    logger.debug("results.size()={}", results.size());
                    for (Map<String, Object> result : results) {
                        //update return code & message back to the detail, 
                        //but only RETCODE no equal to "000" need to update.
                        //<editor-fold defaultstate="collapsed" desc="debug message">
                        logger.debug("TRNNO={}", (String) result.get("TRNNO"));
                        logger.debug("TRNITM={}", (String) result.get("TRNITM"));
                        logger.debug("RETCODE={}", (String) result.get("RETCODE"));
                        //</editor-fold>
                        for (FiInterfaceVO vo : interfaceVOList) {
                            //<editor-fold defaultstate="collapsed" desc="debug message">
                            logger.debug("transactionNo=" + String.valueOf(vo.getDetail().getMaster().getTransactionNo()));
                            logger.debug("detail.transactionItem=" + vo.getDetail().getTransactionItem());
                            //</editor-fold>
                            if (String.valueOf(vo.getDetail().getMaster().getTransactionNo()).equals(result.get("TRNNO"))
                                    && vo.getDetail().getTransactionItem().equals(result.get("TRNITM"))) {
                                //<editor-fold defaultstate="collapsed" desc="debug message">
                                logger.debug("update {} {}={} {}", new Object[]{result.get("TRNNO"),
                                    result.get("TRNITM"),
                                    result.get("RETCODE"),
                                    result.get("RETMESG")});
                                logger.debug("vo.getDetail()={}", vo.getDetail());
                                //</editor-fold>
                                SkFiDetailInterface detailInterface = vo.getDetail();
                                SkFiMasterInterface masterInterface = detailInterface.getMaster();
                                masterInterface.getSkFiDetailInterfaceCollection().remove(detailInterface);
                                detailInterface.setReturnCode((String) result.get("RETCODE"));
                                detailInterface.setReturnMessage((String) result.get("RETMESG"));
                                detailInterface = detailFacade.editAndReturn(detailInterface);
                                vo.setDetail(detailInterface);
                                masterInterface.getSkFiDetailInterfaceCollection().add(detailInterface);
                                String key = masterInterface.getReferenceclassname() + ":" + masterInterface.getReferenceid();
                                processedMasters.put(key, masterInterface);
                                break;
                            }
                        }
                    }
                }
            }

            //update master status by detail's status.
            for (SkFiMasterInterface master : processedMasters.values()) {
                master.setReturnCode("000");
                master.setReturnMessage("成功");
                master.setStatus(InterfaceMasterStatusEnum.TRANSFER_OK);
                for (SkFiDetailInterface detail : master.getSkFiDetailInterfaceCollection()) {
                    //<editor-fold defaultstate="collapsed" desc="debug message">
                    logger.debug("detail={}", detail);
                    logger.debug("detail.getReturnCode()={}", detail.getReturnCode());
                    //</editor-fold>
                    if (detail.getReturnCode() != null && !"000".equals(detail.getReturnCode())) {
                        if (InterfaceMasterStatusEnum.TRANSFER_OK.equals(master.getStatus())) {
                            master.setReturnCode(detail.getReturnCode());
                            master.setReturnMessage(detail.getReturnMessage());
                            master.setStatus(InterfaceMasterStatusEnum.TRANSFER_FAILED);
                        } else {
                            if (!master.getReturnMessage().equals(detail.getReturnMessage())) {
                                master.setReturnCode("002");
                                master.setReturnMessage(rb.getString("fiinterface.erorr.multipleerrorindetail"));
                                master.setStatus(InterfaceMasterStatusEnum.TRANSFER_FAILED);
                            }
                        }
                    }
                }
                facade.edit(master);
            }

            //save uploader & uploadtimestamp after
            for (SkFiMasterInterface master : processedMasters.values()) {
                master.setUploader(userSession.getUser());
                master.setUploadTimestamp(now);
                facade.edit(master);
                //update reference object.
                List<SkArRemitMaster> remitMasters = remitFacade.findByInterface(master);
                for (SkArRemitMaster remitMaster : remitMasters) {
                    if (InterfaceMasterStatusEnum.TRANSFER_FAILED.equals(master.getStatus())) {
                        remitMaster.setStatus(RemitMasterStatusEnum.TRANSFER_FAILED);
                    } else if (InterfaceMasterStatusEnum.TRANSFER_OK.equals(master.getStatus())) {
                        remitMaster.setStatus(RemitMasterStatusEnum.TRANSFER_OK);
                    } else {
                        throw new Exception("not supported yet!");
                    }
                    remitFacade.edit(remitMaster);
                }
            }
            //for remit
            if (referenceObject instanceof SkArRemitMaster) {
                for (RemitMasterVO remitMasterVO : remitQueryController.getRemitMasterVOList()) {
                    remitMasterVO.setRemitMaster(remitFacade.find(remitMasterVO.getRemitMaster().getId()));
                }
            }
            if (!returnResult.isSUCCESS()) {
                throw new Exception(rb.getString("fiinterface.error.uploadfailed"));
            }
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("fiinterface.msg.uploadsuccess"));
        } catch (Exception e) {
            //FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getLocalizedMessage());
            logger.error("upload(), e={}", e);
            RequestContext context = RequestContext.getCurrentInstance();
            logger.debug("context={}", context);
            context.addCallbackParam("errormsg", e.getLocalizedMessage());
        }
    }

    public String getDisplayIcon(SkFiMasterInterface fiInterface) {
        logger.debug("getDisplayIcon(), fiInterface={}", fiInterface);
        String icon = "";
        if (fiInterface != null) {
            if (InterfaceMasterStatusEnum.TRANSFER_FAILED.equals(fiInterface.getStatus())) {
                icon = "red-ball.gif";
            } else if (InterfaceMasterStatusEnum.TRANSFER_OK.equals(fiInterface.getStatus())) {
                icon = "green-ball.gif";
            } else {
                icon = "yellow-ball.gif";
            }
        }
        logger.debug("icon={}", icon);
        return icon;
    }

    public String getDisplayIdentifier(SkFiMasterInterface fiInterface) {
        String displayIdentifier = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (fiInterface != null && fiInterface.getUploader() != null) {
            displayIdentifier = rb.getString("common.label.uploader") + ":" + fiInterface.getUploader().getDisplayIdentifier() + "\n"
                    + rb.getString("common.label.uploadTimestamp") + ":" + sdf.format(fiInterface.getUploadTimestamp()) + "\n";
            if (!"000".equals(fiInterface.getReturnCode())) {
                displayIdentifier += rb.getString("common.label.errorMessage") + ":" + fiInterface.getReturnCode() + "-" + fiInterface.getReturnMessage();
            }
        }
        return displayIdentifier;
    }

    public boolean canEdit(SkFiMasterInterface fiInterface) {
        boolean canEdit = true;
        if (fiInterface != null) {
            if (InterfaceMasterStatusEnum.TRANSFER_OK.equals(fiInterface.getStatus())) {
                canEdit = false;
            }
        }
        return canEdit;
    }

    public boolean canRemove(SkFiMasterInterface fiInterface) {
        boolean canRemove = true;
        if (fiInterface != null) {
            canRemove = false;
        }
        return canRemove;
    }

    public void prepareUpload(List referenceObjectVOList) {
        //<editor-fold defaultstate="collapsed" desc="debug message">
        logger.debug("prepareUpload(), list={}", referenceObjectVOList);
        //</editor-fold>
        this.interfaceVOList = new ArrayList<FiInterfaceVO>();
        HashMap<Long, HashMap<String, String>> keys = new HashMap<Long, HashMap<String, String>>();
        List<SkFiMasterInterface> needMergeMasterInterfaces = new ArrayList<SkFiMasterInterface>();
        mergeMasterInterfaceKeyIndex = new Long(1);
        boolean selected = false;
        boolean isArRemit = false;
        HashMap<String, List<SkAdvanceRemit>> advanceRemits = new HashMap<String, List<SkAdvanceRemit>>();
        Set processedMasterInterface = new HashSet<SkFiMasterInterface>();
        try {
            if (referenceObjectVOList != null && !referenceObjectVOList.isEmpty()) {
                Object object = referenceObjectVOList.get(0);
                if (object instanceof Interfaceable) {
                    //<editor-fold defaultstate="collapsed" desc="debug message">
                    logger.debug("is interfaceable");
                    //</editor-fold>
                    SkCustomer previousCustomer = null;
                    for (Object referenceObject : referenceObjectVOList) {
                        if (referenceObject instanceof Selectable && ((Selectable) referenceObject).isSelected()) {
                            //<editor-fold defaultstate="collapsed" desc="debug message">
                            logger.debug("selected");
                            //</editor-fold>
                            selected = true;

                            Interfaceable interfaceable = (Interfaceable) referenceObject;
                            SkFiMasterInterface masterInterface = interfaceable.getFiInterface();
                            Persistable persistable = interfaceable.getPersistable();
                            if (persistable instanceof SkArRemitMaster) {
                                isArRemit = true;
                                SkArRemitMaster arRemitMaster = (SkArRemitMaster) persistable;
                                //<editor-fold defaultstate="collapsed" desc="debug message">
                                logger.debug("previousCustomer={}", previousCustomer);
                                logger.debug("customer={}", arRemitMaster.getCustomer());
                                //</editor-fold>
                                if (previousCustomer == null || !previousCustomer.equals(arRemitMaster.getCustomer())) {
                                    previousCustomer = arRemitMaster.getCustomer();
                                    List<SkAdvanceRemit> advanceRemitAs = advanceRemitFacade.findByCustomer(previousCustomer, AdvanceRemitTypeEnum.A, "lessThan");
                                    advanceRemitAs = advanceRemitFacade.minusUsedAdvanceRemit(advanceRemitAs, previousCustomer, AdvanceRemitTypeEnum.A);
                                    advanceRemitFacade.sortAdvanceRemit(advanceRemitAs);
                                    //<editor-fold defaultstate="collapsed" desc="debug message">
                                    logger.debug("advanceRemitAs.size()={}", advanceRemitAs.size());
                                    //</editor-fold>
                                    advanceRemits.put("advanceRemitA", advanceRemitAs);
                                    List<SkAdvanceRemit> groupAdvanceRemitAs = new ArrayList<SkAdvanceRemit>();
                                    if (previousCustomer.getParentCustomer() != null) {
                                        groupAdvanceRemitAs = advanceRemitFacade.findByCustomer(previousCustomer.getParentCustomer(), AdvanceRemitTypeEnum.A, "lessThan");
                                        groupAdvanceRemitAs = advanceRemitFacade.minusUsedAdvanceRemit(groupAdvanceRemitAs, previousCustomer.getParentCustomer(), AdvanceRemitTypeEnum.A);
                                        advanceRemitFacade.sortAdvanceRemit(groupAdvanceRemitAs);
                                        //<editor-fold defaultstate="collapsed" desc="debug message">
                                        logger.debug("groupAdvanceRemitAs.size()={}", groupAdvanceRemitAs.size());
                                        //</editor-fold>
                                    }
                                    advanceRemits.put("groupAdvanceRemitA", groupAdvanceRemitAs);
                                    List<SkAdvanceRemit> advanceRemitJs = advanceRemitFacade.findByCustomer(previousCustomer, AdvanceRemitTypeEnum.J, "lessThan");
                                    advanceRemitJs = advanceRemitFacade.minusUsedAdvanceRemit(advanceRemitJs, previousCustomer, AdvanceRemitTypeEnum.J);
                                    advanceRemitFacade.sortAdvanceRemit(advanceRemitJs);
                                    //<editor-fold defaultstate="collapsed" desc="debug message">
                                    logger.debug("advanceRemitJs.size()={}", advanceRemitJs.size());
                                    //</editor-fold>
                                    advanceRemits.put("advanceRemitJ", advanceRemitJs);
                                    List<SkAdvanceRemit> groupAdvanceRemitJs = new ArrayList<SkAdvanceRemit>();
                                    if (previousCustomer.getParentCustomer() != null) {
                                        groupAdvanceRemitJs = advanceRemitFacade.findByCustomer(previousCustomer.getParentCustomer(), AdvanceRemitTypeEnum.J, "lessThan");
                                        groupAdvanceRemitJs = advanceRemitFacade.minusUsedAdvanceRemit(groupAdvanceRemitJs, previousCustomer, AdvanceRemitTypeEnum.J);
                                        advanceRemitFacade.sortAdvanceRemit(groupAdvanceRemitJs);
                                        //<editor-fold defaultstate="collapsed" desc="debug message">
                                        logger.debug("groupAdvanceRemitJs.size()={}", groupAdvanceRemitJs.size());
                                        //</editor-fold>
                                    }
                                    advanceRemits.put("groupAdvanceRemitJ", groupAdvanceRemitJs);
                                }
                            }
                            if (masterInterface == null) {
                                if (persistable instanceof SkArRemitMaster) {
                                    SkArRemitMaster arRemitMaster = (SkArRemitMaster) persistable;
                                    masterInterface = facade.newSkFiMasterInterface(arRemitMaster, advanceRemits);
//                                advanceRemits = advanceRemitFacade.advanceRemitMinusRemit(advanceRemits, arRemitMaster);
                                } else if (persistable instanceof SkPremiumDiscount) {
                                    masterInterface = facade.newSkFiMasterInterface((SkPremiumDiscount) persistable);
                                } else if (persistable instanceof SkAdvancePayment) {
                                    masterInterface = facade.newSkFiMasterInterface((SkAdvancePayment) persistable);
                                }
                            }
                            //已經存在的 master 不需 merge.
                            if (masterInterface != null) {
                                //<editor-fold defaultstate="collapsed" desc="debug message">
                                logger.debug("masterInterface={}", masterInterface);
                                logger.debug("masterInterface.getSkFiDetailInterfaceCollection().size()={}", masterInterface.getSkFiDetailInterfaceCollection().size());
                                logger.debug("mergeMasterInterfaceKeyIndex={}", mergeMasterInterfaceKeyIndex);
                                //</editor-fold>
                                keys = generateKeys(keys, masterInterface);
                                //<editor-fold defaultstate="collapsed" desc="debug message">
                                logger.debug("keys.size()={}", keys.size());
                                for (Long key : keys.keySet()) {
                                    HashMap<String, String> uniqueKeys = keys.get(key);
                                    String uniqueKeyString = "";
                                    for (String uniqueKey : uniqueKeys.values()) {
                                        if (uniqueKeyString.length() > 0) {
                                            uniqueKeyString += ",";
                                        }
                                        uniqueKeyString += uniqueKey;
                                    }
                                    logger.debug("key {} = {}", new Object[]{key, uniqueKeyString});
                                }
                                //</editor-fold>
                                needMergeMasterInterfaces.add(masterInterface);
                            } else if (masterInterface != null && masterInterface.getId() != null) {
                                if (!processedMasterInterface.contains(masterInterface)) {
                                    for (SkFiDetailInterface detail : masterInterface.getSkFiDetailInterfaceCollection()) {
                                        FiInterfaceVO interfaceVO = new FiInterfaceVO();
                                        interfaceVO.setTransactionNo(masterInterface.getTransactionNo());
                                        interfaceVO.setMasterReferenceclassname(masterInterface.getReferenceclassname());
                                        interfaceVO.setMasterReferenceid(masterInterface.getReferenceid());
                                        interfaceVO.setCreatetimestamp(masterInterface.getCreatetimestamp());
                                        interfaceVO.setDetail(detail);
                                        interfaceVOList.add(interfaceVO);
                                    }
                                    processedMasterInterface.add(masterInterface);
                                }
                            }
                        }
                    }
                    if (!needMergeMasterInterfaces.isEmpty()) {
                        List<SkFiMasterInterface> mergedMasterInterfaces = mergeMasterInterfaces(keys, needMergeMasterInterfaces);
                        for (SkFiMasterInterface mergedMasterInterface : mergedMasterInterfaces) {
                            for (SkFiDetailInterface detail : mergedMasterInterface.getSkFiDetailInterfaceCollection()) {
                                FiInterfaceVO interfaceVO = new FiInterfaceVO();
                                interfaceVO.setTransactionNo(mergedMasterInterface.getTransactionNo());
                                interfaceVO.setMasterReferenceclassname(mergedMasterInterface.getReferenceclassname());
                                interfaceVO.setMasterReferenceid(mergedMasterInterface.getReferenceid());
                                interfaceVO.setCreatetimestamp(mergedMasterInterface.getCreatetimestamp());
                                interfaceVO.setDetail(detail);
                                interfaceVOList.add(interfaceVO);
                            }
                        }
                    }
                }
                interfaceVOList = FiInterfaceVO.sortInterfaceVOList(interfaceVOList);
                logger.debug("fiInterafceVOList.size()={}", interfaceVOList.size());
                StringBuilder sb = new StringBuilder(100);
                for (FiInterfaceVO interfaceVO : interfaceVOList) {
                    logger.debug("referenceObject={}:{}", new Object[]{interfaceVO.getMasterReferenceclassname(), interfaceVO.getMasterReferenceid()});
                    logger.debug("transactionItem={}", interfaceVO.getTransactionNo());
                    // 檢查票據主檔金額是否相符
                    SkFiDetailInterface detail = interfaceVO.getDetail();
                    String checkNumber = detail.getCheckNumber();
                    boolean check = isArRemit
                            && StringUtils.isEmpty(detail.getReturnCode())
                            && !StringUtils.isEmpty(checkNumber);
                    if (check) {
                        BigDecimal detailAmount = detail.getTransactionAmount();
                        SkCheckMaster checkMaster = checkFacade.findByCheckNumber(checkNumber);
                        BigDecimal masterAmount = (null != checkMaster) ? checkMaster.getAmount() : null;
                        if (null == detailAmount
                                || null == masterAmount
                                || detailAmount.compareTo(masterAmount) != 0) {
                            if (sb.length() != 0) {
                                sb.append(",");
                            }
                            sb.append("票據號碼").append(checkNumber).append("交易金額不符");
                        }
                    }
                }
                if (sb.length() != 0) {
                    FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, sb.toString());
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.addCallbackParam("errormsg", sb.toString());
                    return;
                }
                if (!selected) {
                    String errorMessage = "";
                    if (object instanceof PremiumDiscountVO) {
                        errorMessage = rb.getString("premiumDiscount.error.empty");
                    } else if (object instanceof AdvancePaymentVO) {
                        errorMessage = rb.getString("advancepayment.error.empty");
                    } else if (object instanceof RemitMasterVO) {
                        errorMessage = rb.getString("remit.error.empty");
                    }
                    FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage);
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.addCallbackParam("errormsg", errorMessage);
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("e={}", e);
            JsfUtils.addErrorMessage(e, "prepareUpload error occur!");
        }
    }

    private HashMap<Long, HashMap<String, String>> generateKeys(HashMap<Long, HashMap<String, String>> keys, SkFiMasterInterface masterInterface) {
        logger.debug("generateKey, keys.size()={}", keys.size());
        HashMap<String, String> uniqueKeys = getUniqueKeys(masterInterface);

        boolean exists = false;
        for (HashMap<String, String> existsUniqueKeys : keys.values()) {
            for (String existsUniqueKey : existsUniqueKeys.values()) {
                if (uniqueKeys.containsKey(existsUniqueKey)) {
                    logger.debug("exists");
                    exists = true;
                    break;
                }
            }
            if (exists) {
                logger.debug("update exists item");
                existsUniqueKeys.putAll(uniqueKeys);
                break;
            }
        }
        if (!exists) {
            logger.debug("put new item");
            keys.put(mergeMasterInterfaceKeyIndex, uniqueKeys);
            mergeMasterInterfaceKeyIndex++;
        }
        return keys;
    }

    private HashMap<String, String> getUniqueKeys(SkFiMasterInterface masterInterface) {
        HashMap<String, String> uniqueKeys = new HashMap<String, String>();
        //先找出要合併的 master 的 key - check numbers
        for (SkFiDetailInterface detailInterface : masterInterface.getSkFiDetailInterfaceCollection()) {
            String checkNumber = detailInterface.getCheckNumber();
            if (StringUtils.isNotEmpty(checkNumber)) {
                logger.debug("put check number");
                if (!uniqueKeys.containsKey(checkNumber)) {
                    uniqueKeys.put(checkNumber, checkNumber);
                }
            }
        }
        //先找出要合併的 master 的 key - reference class's id
        logger.debug("uniqueKeys.size()=" + uniqueKeys.size());
        if (uniqueKeys.isEmpty()) {
            logger.debug("no check number");
            String key = String.valueOf(masterInterface.getReferenceid());
            uniqueKeys.put(key, key);
        }
        return uniqueKeys;
    }

    private List<SkFiMasterInterface> mergeMasterInterfaces(HashMap<Long, HashMap<String, String>> keys, List<SkFiMasterInterface> masterInterfaces) {
        List<SkFiMasterInterface> mergedMasterInterfaces = new ArrayList<SkFiMasterInterface>();
        HashMap<Long, List<SkFiMasterInterface>> groupedMasterInterfaces = new HashMap<Long, List<SkFiMasterInterface>>();
        try {
            //find belong to.
            for (SkFiMasterInterface masterInterface : masterInterfaces) {
                Long groupKey = findBelongTo(keys, masterInterface);
                List<SkFiMasterInterface> groupMasterInterfaces = new ArrayList<SkFiMasterInterface>();
                if (groupedMasterInterfaces.containsKey(groupKey)) {
                    groupMasterInterfaces = groupedMasterInterfaces.get(groupKey);
                }
                groupMasterInterfaces.add(masterInterface);
                groupedMasterInterfaces.put(groupKey, groupMasterInterfaces);
            }
            //do actual merge.
            for (Long groupedMasterInterfacesKey : groupedMasterInterfaces.keySet()) {
                List<SkFiMasterInterface> groupMasterInterfaces = groupedMasterInterfaces.get(groupedMasterInterfacesKey);
                SkFiMasterInterface newMasterInterface = new SkFiMasterInterface();
                List<SkFiDetailInterface> allDetailInterfaces = new ArrayList<SkFiDetailInterface>();
                HashMap<String, SkFiDetailInterface> mergedDetailInterfaces = new HashMap<String, SkFiDetailInterface>();
                HashMap<String, List<Long>> generatedBy = new HashMap<String, List<Long>>();
                if (!groupMasterInterfaces.isEmpty()) {
                    SkFiMasterInterface firstGroupMasterInterface = groupMasterInterfaces.get(0);
                    //<editor-fold defaultstate="collapsed" desc="debug message">
                    logger.debug("getReferenceclassname={}", firstGroupMasterInterface.getReferenceclassname());
                    logger.debug("getReferenceid={}", firstGroupMasterInterface.getReferenceid());
                    //</editor-fold>
                    PropertyUtils.copyProperties(newMasterInterface, firstGroupMasterInterface);
                    for (SkFiMasterInterface groupMasterInterface : groupMasterInterfaces) {
                        List<Long> ids = new ArrayList<Long>();
                        if (generatedBy.containsKey(groupMasterInterface.getReferenceclassname())) {
                            ids = generatedBy.get(groupMasterInterface.getReferenceclassname());
                        }
                        ids.add(groupMasterInterface.getReferenceid());
                        generatedBy.put(groupMasterInterface.getReferenceclassname(), ids);
                        allDetailInterfaces.addAll(groupMasterInterface.getSkFiDetailInterfaceCollection());
                    }
                }
                newMasterInterface.setGeneratedBy(generatedBy);
                for (SkFiDetailInterface detailInterface : allDetailInterfaces) {
                    detailInterface.setMaster(newMasterInterface);
                    String detailKey = generateDetailKey(detailInterface);
                    logger.debug("detailKey={}", detailKey);
                    SkFiDetailInterface newDetailInterface = new SkFiDetailInterface();
                    PropertyUtils.copyProperties(newDetailInterface, detailInterface);
                    if (mergedDetailInterfaces.containsKey(detailKey)) {
                        newDetailInterface = mergedDetailInterfaces.get(detailKey);
                        newDetailInterface.setTransactionAmount(newDetailInterface.getTransactionAmount().add(detailInterface.getTransactionAmount()));
                    }
                    mergedDetailInterfaces.put(detailKey, newDetailInterface);
                }
                //rearrange transaction sequence for merge.
                List<SkFiDetailInterface> detailInterfaces = new ArrayList<SkFiDetailInterface>();
                detailInterfaces.addAll(mergedDetailInterfaces.values());
                Collections.sort(detailInterfaces, new Comparator<SkFiDetailInterface>() {

                    @Override
                    public int compare(SkFiDetailInterface o1, SkFiDetailInterface o2) {
                        if (o1.getSummonsCode().equals(o2.getSummonsCode())) {
                            return o1.getTransactionItem().compareTo(o2.getTransactionItem());
                        } else {
                            return o2.getSummonsCode().compareTo(o1.getSummonsCode());
                        }
                    }
                });
                int itemSeq = 1;
                SkFiDetailInterface lastDetailInterface = null;
                for (SkFiDetailInterface detailInterface : detailInterfaces) {
                    detailInterface.setTransactionItem(getTransactionItem(itemSeq));
                    lastDetailInterface = detailInterface;
                    itemSeq++;
                }
                lastDetailInterface.setTransactionItem("999");
                newMasterInterface.setSkFiDetailInterfaceCollection(detailInterfaces);
                mergedMasterInterfaces.add(newMasterInterface);
            }
        } catch (Exception e) {
            logger.error("mergeMasterInterfaces(), e = {}", e);
            JsfUtils.addErrorMessage(e, "mergeMasterInterfaces occur error.");
        }
        return mergedMasterInterfaces;
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

    private String generateDetailKey(SkFiDetailInterface detailInterface) {
        String key = "";
        if (StringUtils.isNotEmpty(detailInterface.getTransactionType())) {
            key += detailInterface.getTransactionType();
        }
        if (StringUtils.isNotEmpty(detailInterface.getGeneralLedgerCode())) {
            key += detailInterface.getGeneralLedgerCode();
        }
        if (StringUtils.isNotEmpty(detailInterface.getSummonsCode())) {
            key += detailInterface.getSummonsCode();
        }
        if (StringUtils.isNotEmpty(detailInterface.getSummonsCode())) {
            key += detailInterface.getSummonsCode();
        }
        if (StringUtils.isNotEmpty(detailInterface.getCustomerNumber())) {
            key += detailInterface.getCustomerNumber();
        }
        if (StringUtils.isNotEmpty(detailInterface.getInvoiceNumber())) {
            key += detailInterface.getInvoiceNumber();
        }
        if (StringUtils.isNotEmpty(detailInterface.getOrderNumber())) {
            key += detailInterface.getOrderNumber();
        }
        if (StringUtils.isNotEmpty(detailInterface.getSalesGroup())) {
            key += detailInterface.getSalesGroup();
        }
        if (StringUtils.isNotEmpty(detailInterface.getCheckNumber())) {
            key += detailInterface.getCheckNumber();
        }
        if (StringUtils.isNotEmpty(detailInterface.getCheckBank())) {
            key += detailInterface.getCheckBank();
        }
        if (StringUtils.isNotEmpty(detailInterface.getCheckAccount())) {
            key += detailInterface.getCheckAccount();
        }
        if (StringUtils.isNotEmpty(detailInterface.getOwner())) {
            key += detailInterface.getOwner();
        }
        return key;
    }

    private Long findBelongTo(HashMap<Long, HashMap<String, String>> keys, SkFiMasterInterface masterInterface) {
        Long group = null;
        HashMap<String, String> masterInterfaceUniqueKeys = getUniqueKeys(masterInterface);
        boolean found = false;
        for (Long key : keys.keySet()) {
            HashMap<String, String> uniqueKeys = keys.get(key);
            for (String masterInterfaceUniqueKey : masterInterfaceUniqueKeys.keySet()) {
                if (uniqueKeys.containsKey(masterInterfaceUniqueKey)) {
                    group = key;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        return group;
    }
}

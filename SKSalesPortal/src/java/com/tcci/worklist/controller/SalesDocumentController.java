package com.tcci.worklist.controller;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.entity.SkProxy;
import com.tcci.sksp.facade.SkProxyFacade;
import com.tcci.sksp.facade.ZtabExpRelfilenoSdLogFacade;
import com.tcci.worklist.controller.util.ZtabExpRelfilenoSdVODataModel;
import com.tcci.worklist.entity.datawarehouse.RelfilenoEmp;
import com.tcci.worklist.entity.datawarehouse.TcSapclient;
import com.tcci.worklist.entity.datawarehouse.ZtabExpBersl;
import com.tcci.worklist.entity.datawarehouse.ZtabExpRelfilenoSd;
import com.tcci.worklist.entity.datawarehouse.ZtabExpTj10t;
import com.tcci.worklist.entity.datawarehouse.ZtabExpTvkot;
import com.tcci.worklist.entity.datawarehouse.ZtabExpTvtwt;
import com.tcci.worklist.enums.ReviewOptionEnum;
import com.tcci.worklist.enums.SdBstzdEnum;
import com.tcci.worklist.enums.SelectOptionEnum;
import com.tcci.worklist.facade.datawarehouse.RelfilenoEmpFacade;
import com.tcci.worklist.facade.datawarehouse.TcSapclientFacade;
import com.tcci.worklist.facade.datawarehouse.ZtabExpBerslFacade;
import com.tcci.worklist.facade.datawarehouse.ZtabExpRelfilenoSdFacade;
import com.tcci.worklist.facade.datawarehouse.ZtabExpRelfilenoSdFilter;
import com.tcci.worklist.facade.datawarehouse.ZtabExpTj10tFacade;
import com.tcci.worklist.facade.datawarehouse.ZtabExpTvkotFacade;
import com.tcci.worklist.facade.datawarehouse.ZtabExpTvtwtFacade;
import com.tcci.sapproxy.PpProxy;
import com.tcci.sapproxy.dto.SapProxyResponseDto;
import com.tcci.sapproxy.dto.SapTableDto;
import com.tcci.sapproxy.jco.JcoUtils;
import com.tcci.worklist.vo.RecordFailVO;
import com.tcci.worklist.vo.RelfilenoEmpVO;
import com.tcci.worklist.vo.ZtabExpRelfilenoSdVO;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean(name = "salesDocumentController")
@ViewScoped
public class SalesDocumentController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    private static final Logger logger = LoggerFactory.getLogger(SalesDocumentController.class.getName());
    private static final String TOP_MANAGER_REVIEW_CODE = "SKPCSD03";
    private static final int BSTZD_COL = 25;
    private static final int PQ_COL = 21;
    private static final int AB_COL = 20;
    private static final int VBELN_COL = 6;
    private static final int PRICE_COL = 8;
    private static final int PRICE_COL2 = 10;
    private static final int QUANTITY_COL = 11;
    private static final int SHIFT_COLS = 2;
    private static final int LEVEL_COL = 26;
    private ResourceBundle rb = ResourceBundle.getBundle("worklistMessages");
    //TODO: change jndi name.
    @Resource(mappedName = "jndi/sapclient.config")
    transient private Properties jndiConfig;
    private String berslString = "";
    private TcSapclient tcSapclient = null;
    private ReviewOptionEnum[] reviewOptions;
    private ReviewOptionEnum reviewOption;
    private String usermode;
    private static final String SPRAS = "M"; //語言碼
    private List<ZtabExpRelfilenoSdVO> items;
    private ZtabExpRelfilenoSdFilter filter;
    private boolean selectAll;
    private List<ZtabExpTvtwt> ztabExpTvtwtList;
    private List<ZtabExpTvkot> ztabExpTvkotList;
    private List<ZtabExpTj10t> ztabExpTj10tList;
    private List<ZtabExpBersl> ztabExpBesrlList;
    private List<RelfilenoEmpVO> relfilenoEmpVOList;
    private boolean exportButtonRendered;
    private SelectOptionEnum[] selectOptions;
    private SelectOptionEnum selectOption;
    //Cache: Key 為 entity的Class，Value為 Table欄位和Entity欄位Mappings: 
    private static Map<Class, Map<String, String>> fieldPropMappingInfosCache = new HashMap<Class, Map<String, String>>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    transient private TcSapclientFacade tcSapclientFacade;
    @EJB
    transient private RelfilenoEmpFacade relfilenoEmpFacade;
    @EJB
    transient private ZtabExpBerslFacade ztabExpBerslFacade;
    @EJB
    transient private ZtabExpRelfilenoSdFacade ztabExpRelfilenoSdFacade;
    @EJB
    transient private ZtabExpTvtwtFacade ztabExpTvtwtFacade;
    @EJB
    transient private ZtabExpTvkotFacade ztabExpTvkotFacade;
    @EJB
    transient private ZtabExpTj10tFacade ztabExpTj10tFacade;
    @EJB
    transient private ZtabExpRelfilenoSdLogFacade ztabExpRelfilenoSdLogFacade;
    @EJB
    transient private SkProxyFacade proxyFacade;
    @ManagedProperty(value = "#{sessionController}")
    SessionController sessionController;

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public List<ZtabExpRelfilenoSdVO> getItems() {
        return items;
    }

    public void setItems(List<ZtabExpRelfilenoSdVO> items) {
        this.items = items;
    }

    public ZtabExpRelfilenoSdFilter getFilter() {
        return filter;
    }

    public void setFilter(ZtabExpRelfilenoSdFilter filter) {
        this.filter = filter;
    }

    public List<ZtabExpTvtwt> getZtabExpTvtwtList() {
        return ztabExpTvtwtList;
    }

    public void setZtabExpTvtwtList(List<ZtabExpTvtwt> ztabExpTvtwtList) {
        this.ztabExpTvtwtList = ztabExpTvtwtList;
    }

    public List<ZtabExpTj10t> getZtabExpTj10tList() {
        return ztabExpTj10tList;
    }

    public void setZtabExpTj10tList(List<ZtabExpTj10t> ztabExpTj10tList) {
        this.ztabExpTj10tList = ztabExpTj10tList;
    }

    public List<ZtabExpTvkot> getZtabExpTvkotList() {
        return ztabExpTvkotList;
    }

    public void setZtabExpTvkotList(List<ZtabExpTvkot> ztabExpTvkotList) {
        this.ztabExpTvkotList = ztabExpTvkotList;
    }

    public List<ZtabExpBersl> getZtabExpBesrlList() {
        return ztabExpBesrlList;
    }

    public void setZtabExpBesrlList(List<ZtabExpBersl> ztabExpBesrlList) {
        this.ztabExpBesrlList = ztabExpBesrlList;
    }

    public String getBerslString() {
        return berslString;
    }

    public void setBerslString(String berslString) {
        this.berslString = berslString;
    }

    public ReviewOptionEnum[] getReviewOptions() {
        return reviewOptions;
    }

    public void setReviewOptions(ReviewOptionEnum[] reviewOptions) {
        this.reviewOptions = reviewOptions;
    }

    public ReviewOptionEnum getReviewOption() {
        return reviewOption;
    }

    public void setReviewOption(ReviewOptionEnum reviewOption) {
        this.reviewOption = reviewOption;
    }

    public String getUsermode() {
        return usermode;
    }

    public void setUsermode(String usermode) {
        this.usermode = usermode;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public List<RelfilenoEmpVO> getRelfilenoEmpVOList() {
        return relfilenoEmpVOList;
    }

    public void setRelfilenoEmpVOList(List<RelfilenoEmpVO> relfilenoEmpVOList) {
        this.relfilenoEmpVOList = relfilenoEmpVOList;
    }

    public boolean isExportButtonRendered() {
        return exportButtonRendered;
    }

    public void setExportButtonRendered(boolean exportButtonRendered) {
        this.exportButtonRendered = exportButtonRendered;
    }

    public SelectOptionEnum[] getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(SelectOptionEnum[] selectOptions) {
        this.selectOptions = selectOptions;
    }

    public SelectOptionEnum getSelectOption() {
        return selectOption;
    }

    public void setSelectOption(SelectOptionEnum selectOption) {
        this.selectOption = selectOption;
    }

    //</editor-fold>
    @PostConstruct
    private void init() {
        tcSapclient = tcSapclientFacade.getByCode("sking");
        filter = new ZtabExpRelfilenoSdFilter();
        filter.setAudatBegin(new Date());
        ztabExpTvkotList = ztabExpTvkotFacade.getTvkotBySpras(tcSapclient.getClient(), SPRAS);
        ztabExpTvtwtList = ztabExpTvtwtFacade.getTvtwtBySpras(tcSapclient.getClient(), SPRAS);
        ztabExpTj10tList = ztabExpTj10tFacade.getTj10tBySpras(tcSapclient.getClient(), SPRAS);
        RelfilenoEmp relfilenoEmp = relfilenoEmpFacade.findByEmpCode(sessionController.getUser().getEmpId());
        relfilenoEmpVOList = new ArrayList<RelfilenoEmpVO>();
        if (relfilenoEmp != null) {
            ztabExpBesrlList = ztabExpBerslFacade.getBerslsByBname(relfilenoEmp.getBname());
            filter.setBname(relfilenoEmp.getBname());
            filter.setBersl(ztabExpBesrlList.get(0).getBersl());
            RelfilenoEmpVO relfilenoEmpVO = new RelfilenoEmpVO();
            relfilenoEmpVO.setBname(relfilenoEmp.getBname());
            relfilenoEmpVO.setTcUser(sessionController.getUser());
            relfilenoEmpVOList.add(relfilenoEmpVO);
        }
        //--Begin--Modified by nEO Fu on 20130219 add proxy mechanism.
        List<SkProxy> proxies = proxyFacade.findByProxy(sessionController.getUser());
        logger.debug("proxies.size={}", proxies.size());
        if (ztabExpBesrlList == null) {
            ztabExpBesrlList = new ArrayList<ZtabExpBersl>();
        }
        if (proxies.size() > 0) {
            for (SkProxy proxy : proxies) {
                RelfilenoEmp proxyRelfilenoEmp = relfilenoEmpFacade.findByEmpCode(proxy.getUser().getEmpId());
                if (proxyRelfilenoEmp != null) {
                    List<ZtabExpBersl> proxyBesrlList = ztabExpBerslFacade.getBerslsByBname(proxyRelfilenoEmp.getBname());
                    for (ZtabExpBersl proxyBesrl : proxyBesrlList) {
                        if (!ztabExpBesrlList.contains(proxyBesrl)) {
                            ztabExpBesrlList.add(proxyBesrl);
                        }
                    }
                    RelfilenoEmpVO relfilenoEmpVO = new RelfilenoEmpVO();
                    relfilenoEmpVO.setBname(proxyRelfilenoEmp.getBname());
                    relfilenoEmpVO.setTcUser(proxy.getUser());
                    relfilenoEmpVOList.add(relfilenoEmpVO);
                }
            }
        }
        //---End---Modified by nEO Fu on 20130219 add proxy mechanism.
        reviewOptions = ReviewOptionEnum.values();
        this.reviewOption = ReviewOptionEnum.A;
        selectOptions = selectOption.values();
        this.selectOption = null;
        this.exportButtonRendered = false;
        filter.setVkorg("9600");
        if (relfilenoEmpVOList.isEmpty()) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("sd.error.bnamenotexists"));
        }
        //<editor-fold defaultstate="collapsed" desc="debug messages">
        logger.debug("bname={}", filter.getBname());
        logger.debug("bersl={}", filter.getBersl());
        //</editor-fold>
    }

    public ZtabExpRelfilenoSdVODataModel getZtabExpRelfilenoSdVODataModel() {
        return new ZtabExpRelfilenoSdVODataModel(items);
    }

    public void query() {
        usermode = reviewOption.toString();
        items = new ArrayList<ZtabExpRelfilenoSdVO>();
//一律直接透過 RFC 查詢資料, 避免復原核發狀態後, 以核發查不到資料.
//        if (ReviewOptionEnum.A.equals(reviewOption)) {
//            List<ZtabExpRelfilenoSd> ztabExpRelfilenoSds = ztabExpRelfilenoSdFacade.findByCriteria(filter);
//            logger.debug("ztabExpRelfilenoSds.size()={}", ztabExpRelfilenoSds.size());
//            for (ZtabExpRelfilenoSd ztabExpRelfilenoSd : ztabExpRelfilenoSds) {
//                ZtabExpRelfilenoSdVO vo = new ZtabExpRelfilenoSdVO(ztabExpRelfilenoSd);
//                if ("0".equals(ztabExpRelfilenoSd.getBstzd().substring(1, 2))) {
//                    vo.setReviewable(false);
//                } else {
//                    vo.setReviewable(true);
//                }
//                items.add(vo);
//            }
//        } else {
//一律直接透過 RFC 查詢資料, 避免復原核發狀態後, 以核發查不到資料.        
        try {
            //Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, tcSapclient); //取得相關Jco連結參數
            //PpProxy ppProxy = PpProxyFactory.createProxy(jcoProp);//建立連線
            PpProxy ppProxy = JcoUtils.getSapProxy(tcSapclient.getCode(), sessionController.getUser().getLoginAccount());
            SapProxyResponseDto output = ppProxy.findExpRelFileNoSDs(filter, reviewOption.toString());
            SapTableDto result = output.getResultAsSapTableDto();
            List<Map<String, Object>> dataList = result.getDataMapList();

            //取得 Table 欄位和 Entity 欄位 Mapping
            Map<String, String> ztabExpRelfilenoSdFieldsInfo = fieldPropMappingInfosCache.get(ZtabExpRelfilenoSd.class);
            if (null == ztabExpRelfilenoSdFieldsInfo) {
                ztabExpRelfilenoSdFieldsInfo = JcoUtils.getFieldsPropsMappingInfo(ZtabExpRelfilenoSd.class);
                fieldPropMappingInfosCache.put(ZtabExpRelfilenoSd.class, ztabExpRelfilenoSdFieldsInfo);
            }
            logger.info("dataList.size()={}", dataList.size());
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> relfilenoData = dataList.get(i); //由SAP取得的資料
                ZtabExpRelfilenoSd masterData = (ZtabExpRelfilenoSd) JcoUtils.generateEntity(ztabExpRelfilenoSdFieldsInfo, relfilenoData, ZtabExpRelfilenoSd.class);
                if (null == masterData) {
                    continue;
                }

                masterData.setId(new Long(System.identityHashCode(masterData)));
                masterData.setBname(filter.getBname());
                masterData.setBersl(filter.getBersl());
                ZtabExpRelfilenoSdVO vo = new ZtabExpRelfilenoSdVO(masterData);
                //平均單價(a)及前次單價(a)必須乘以100.
//                vo.getZtabExpRelfilenoSd().setAspS(vo.getZtabExpRelfilenoSd().getAspS().multiply(new BigDecimal(100)));
//                vo.getZtabExpRelfilenoSd().setPraspS(vo.getZtabExpRelfilenoSd().getPraspS().multiply(new BigDecimal(100)));
                //金額必須乘以100.
//                vo.getZtabExpRelfilenoSd().setNetwr(vo.getZtabExpRelfilenoSd().getNetwr().multiply(new BigDecimal(100)));
                //溢價折讓必須乘以100
//                vo.getZtabExpRelfilenoSd().setDiscount(vo.getZtabExpRelfilenoSd().getDiscount().multiply(new BigDecimal(100)));

                if (SelectOptionEnum.ALL.equals(selectOption)
                        || (SelectOptionEnum.TOP_MANAGER.equals(selectOption) && isTopManagerReview(vo.getZtabExpRelfilenoSd().getBstzd()))) {
                    vo.setSelected(true);
                }
                if ("0".equals(masterData.getBstzd().substring(1, 2))) {
                    vo.setReviewable(false);
                } else {
                    vo.setReviewable(true);
                }
                if (StringUtils.isNotEmpty(filter.getBname())) {
                    logger.debug("vo.getBname={}", vo.getZtabExpRelfilenoSd().getBname());
                    if (filter.getBname().equals(vo.getZtabExpRelfilenoSd().getBname())) {
                        items.add(vo);
                    }
                } else {
                    items.add(vo);
                }
            }
            ppProxy.dispose();
            if (!items.isEmpty()) {
                exportButtonRendered = true;
            }
        } catch (Exception e) {
            logger.error("query(), e={}", e);
        }
//        }
        items = sortItems(items);
    }

    public List<ZtabExpRelfilenoSdVO> sortItems(List<ZtabExpRelfilenoSdVO> items) {
        Collections.sort(items, new Comparator<ZtabExpRelfilenoSdVO>() {
            public int compare(ZtabExpRelfilenoSdVO o1, ZtabExpRelfilenoSdVO o2) {
                ZtabExpRelfilenoSd s1 = o1.getZtabExpRelfilenoSd();
                ZtabExpRelfilenoSd s2 = o2.getZtabExpRelfilenoSd();
                if (s1.getVbeln().equals(s2.getVbeln())) {
                    return s2.getPosnr().compareTo(s1.getPosnr());
                } else {
                    return s2.getVbeln().compareTo(s1.getVbeln());
                }
            }
        });
        return items;
    }

    public String getBerslDisplay(ZtabExpBersl bersl) {
        ZtabExpTj10t ztabExpTj10t = ztabExpTj10tFacade.getTj10tBySprasBersl(tcSapclient.getClient(), SPRAS, bersl.getBersl());
        if (ztabExpTj10t != null) {
            return bersl.getBersl() + " (" + ztabExpTj10t.getTxt() + ")";
        } else {
            return "";
        }
    }

    public String getBstzdDisplayName(String bstzd) {
        if ("C".equals(bstzd.substring(1, 2))) {
            return SdBstzdEnum.BSTZD_1.getDisplayName();
        } else if ("D".equals(bstzd.substring(1, 2))) {
            return SdBstzdEnum.BSTZD_2.getDisplayName();
        } else {
            return "";
        }
    }

    /**
     * 按照簽核層級回傳顏色, 簽核層級其四碼, 若第二碼為0則只有兩碼, 若第二碼不為0則有四碼 第一碼表示此份銷售文件最高簽核層級: 1=部門經理,
     * 2=總經理 第二碼表示此項目的簽核層級: C=部門經理, D=總經理 第三碼表此此項目的簽核層級由單價高低決定.
     * 第四碼表示此項目的簽核層級由數量多少低決定.
     *
     * @param bstzd 簽核層級 10 or 20 or 1CVQ 2DVQ
     * @return css color style.
     */
    public String getPriceLevelColor(String bstzd) {
        String color = "background-color:";
        if (isManagerReview(bstzd)) {
            //TODO: 檢查代碼 C 是否代表經理
            color += " yellow;";
        } else if (isTopManagerReview(bstzd)) {
            //TODO: 檢查代碼 D 是否代表總經理
            color += " red;";
        }
        //若第三碼不為V, 表示簽核階層不由價格決定, reset color.
        if (StringUtils.isEmpty(bstzd)
                || "0".equals(bstzd.substring(1, 2))
                || !(bstzd.length() > 2 && bstzd.substring(2, 3).equals("V"))) {
            color = "";
        }
        return color;
    }

    /**
     * 按照簽核層級回傳顏色, 簽核層級其四碼, 若第二碼為0則只有兩碼, 若第二碼不為0則有四碼 第一碼表示此份銷售文件最高簽核層級: 1=部門經理,
     * 2=總經理 第二碼表示此項目的簽核層級: C=部門經理, D=總經理 第三碼表此此項目的簽核層級由單價高低決定.
     * 第四碼表示此項目的簽核層級由數量多少低決定.
     *
     * @param bstzd 簽核層級 10 or 20 or 1CVQ 2DVQ
     * @return css color style.
     */
    public String getQuantityLevelColor(String bstzd) {
        String color = "background-color:";
        if (isManagerReview(bstzd)) {
            //TODO: 檢查代碼 C 是否代表經理
            color += " yellow;";
        } else if (isTopManagerReview(bstzd)) {
            //TODO: 檢查代碼 D 是否代表總經理
            color += " red;";
        }
        //若第四碼不為Q, 表示簽核階層不由價格決定, reset color.
        if (StringUtils.isEmpty(bstzd)
                || "0".equals(bstzd.substring(1, 2))
                || !(bstzd.length() > 3 && bstzd.substring(3, 4).equals("Q"))) {
            color = "";
        }
        return color;
    }

    public String getLevelColor(String bstzd) {
        if (isManagerReview(bstzd)) {
            return "background-color: yellow;";
        } else if (isTopManagerReview(bstzd)) {
            return "background-color: red;";
        } else {
            return "";
        }
    }

    public boolean isManagerReview(String bstzd) {
        if (StringUtils.isEmpty(bstzd) || bstzd.length() <= 1) {
            return false;
        } else {
            if ("C".equals(bstzd.substring(1, 2))) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isTopManagerReview(String bstzd) {
        if (StringUtils.isEmpty(bstzd) || bstzd.length() <= 1) {
            return false;
        } else {
            if ("D".equals(bstzd.substring(1, 2))) {
                return true;
            } else {
                return false;
            }
        }
    }

    public List<Exception> validate(List<ZtabExpRelfilenoSdVO> selectedItems, String usermode) {
        List<Exception> exceptions = new ArrayList<Exception>();
        if (selectedItems == null || selectedItems.isEmpty()) {
            Exception e = new Exception(rb.getString("sd.error.listEmpty"));
            exceptions.add(e);
        } else {
            for (ZtabExpRelfilenoSdVO ztabExpRelfilenoSdVO : selectedItems) {
                ZtabExpRelfilenoSd ztabExpRelfilenoSd = ztabExpRelfilenoSdVO.getZtabExpRelfilenoSd();
                //簽核層級第二碼為0表示項目不需簽核, 不需簽核則不必檢查必輸欄位
                if (!"0".equals(ztabExpRelfilenoSd.getBstzd().substring(1, 2))) {
                    if (ztabExpRelfilenoSdVO.isCommentAB()
                            && !("A".equals(ztabExpRelfilenoSd.getDbrtg())
                            || "B".equals(ztabExpRelfilenoSd.getDbrtg()))) {
                        Exception e = new Exception(
                                rb.getString("sd.dbrtg.notmatch")
                                + "[" + ztabExpRelfilenoSd.getVbeln()
                                + "/" + ztabExpRelfilenoSd.getPosnr()
                                + "]!");
                        exceptions.add(e);
                    }
                    //usermode = R 或 O 時不檢查簽核備註必填.
                    if ("A".equals(usermode)) {
                        if (ztabExpRelfilenoSdVO.isCommentAB() || ztabExpRelfilenoSdVO.isCommentVQ()) {
                            //ignore for A/B 級客戶 or 量/價升級.
                        } else {
                            if (StringUtils.isEmpty(ztabExpRelfilenoSd.getRelText())) {
                                Exception e = new Exception(
                                        rb.getString("sd.comment.isrequired")
                                        + "[" + ztabExpRelfilenoSd.getVbeln()
                                        + "/" + ztabExpRelfilenoSd.getPosnr()
                                        + "]!");
                                exceptions.add(e);
                            }
                        }
                    }
                }
            }
        }
        return exceptions;
    }

    public void batchSign() {
        List<ZtabExpRelfilenoSdVO> selectedItems = new ArrayList<ZtabExpRelfilenoSdVO>();
        for (ZtabExpRelfilenoSdVO vo : items) {
            if (vo.isReviewable() && vo.isSelected()) {
                selectedItems.add(vo);
            }
        }
        logger.debug("batchSign, selectedItems.size()={}, bname={}, bersl={}", new Object[]{selectedItems.size(), filter.getBname(), filter.getBersl()});
        List<Exception> exceptions = validate(selectedItems, usermode);
        if (exceptions.isEmpty()) {
//            Properties jcoProp = JcoUtils.getJCoProp(jndiConfig, tcSapclient); //取得相關Jco連結參數
//            PpProxy ppProxy = PpProxyFactory.createProxy(jcoProp);//建立連線
            PpProxy ppProxy = JcoUtils.getSapProxy(tcSapclient.getCode(), sessionController.getUser().getLoginAccount());
            Map<String, List<ZtabExpRelfilenoSdVO>> approveList = new HashMap<String, List<ZtabExpRelfilenoSdVO>>();
            for (ZtabExpRelfilenoSdVO selectedItem : selectedItems) {
                logger.debug("bstzd={}", selectedItem.getZtabExpRelfilenoSd().getBstzd());
                if (!"0".equals(selectedItem.getZtabExpRelfilenoSd().getBstzd().substring(1, 2))) {
                    String key = selectedItem.getZtabExpRelfilenoSd().getVbeln();
                    List<ZtabExpRelfilenoSdVO> VOs = null;
                    if (approveList.containsKey(key)) {
                        VOs = approveList.get(key);
                    } else {
                        VOs = new ArrayList<ZtabExpRelfilenoSdVO>();
                    }
                    VOs.add(selectedItem);
                    approveList.put(key, VOs);
                }
            }
            logger.debug("approveList.size()");
            try {
                List<RecordFailVO> failList = new ArrayList<RecordFailVO>();
                for (Iterator iterator = approveList.keySet().iterator(); iterator.hasNext();) {
                    String vbeln = (String) iterator.next();
                    List<ZtabExpRelfilenoSdVO> VOs = approveList.get(vbeln);
                    for (ZtabExpRelfilenoSdVO vo : VOs) {
                        String comment = "";
                        if (vo.isCommentAB()) {
                            comment += rb.getString("sd.commentAB");
                        }
                        if (vo.isCommentVQ()) {
                            if (comment.length() > 0) {
                                comment += ", ";
                            }
                            comment += rb.getString("sd.commentVQ");
                        }
                        if (StringUtils.isNotEmpty(vo.getZtabExpRelfilenoSd().getRelText())) {
                            if (comment.length() > 0) {
                                comment += ", ";
                            }
                            comment += vo.getZtabExpRelfilenoSd().getRelText();
                        }
                        vo.getZtabExpRelfilenoSd().setRelText(comment);
                    }
                    SapProxyResponseDto dto = ppProxy.doSdDocumentRelease(VOs, usermode);
                    boolean isSUCCESS = dto.isSUCCESS();//check return code
                    if (!isSUCCESS) {
                        String message = dto.getDescription();
                        RecordFailVO recordFailVO = new RecordFailVO();
                        recordFailVO.setVbeln(vbeln);
                        recordFailVO.setErrMsg(message);
                        failList.add(recordFailVO);
                        logger.error("{}-{}{} failed[{}]", new Object[]{tcSapclient.getName(), vbeln, "Approve", message});
                        continue;
                    }
                    ztabExpRelfilenoSdLogFacade.createLog(tcSapclient.getClient(), VOs, usermode, sessionController.getUser().getLoginAccount());
                }
                if (failList.isEmpty()) {
                    FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, rb.getString("batch") + ReviewOptionEnum.fromString(usermode).getDisplayName() + rb.getString("success") + "!");
                } else {
                    StringBuilder errMsg = new StringBuilder();
                    errMsg.append(rb.getString("batch"));
                    ReviewOptionEnum.fromString(usermode);
                    errMsg.append(ReviewOptionEnum.fromString(usermode).getDisplayName());
                    errMsg.append(rb.getString("fail"));
                    errMsg.append("!: ");
                    for (RecordFailVO recordFailVO : failList) {
                        errMsg.append("[");
                        errMsg.append(recordFailVO.getVbeln());
                        errMsg.append("(");
                        errMsg.append(recordFailVO.getErrMsg());
                        errMsg.append(")]");
                    }
                    FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, errMsg.toString());
                    logger.error("fail to approve task. Failed TaskId list: {}", errMsg.toString());
                }
                List<ZtabExpRelfilenoSdVO> newItems = new ArrayList<ZtabExpRelfilenoSdVO>();
                for (ZtabExpRelfilenoSdVO vo : items) {
                    if (!approveList.keySet().contains(vo.getZtabExpRelfilenoSd().getVbeln())) {
                        newItems.add(vo);
                    }
                }
                if ("A".equals(usermode) || "R".equals(usermode)) {
                    for (Iterator iterator = approveList.keySet().iterator(); iterator.hasNext();) {
                        String vbeln = (String) iterator.next();
                        List<ZtabExpRelfilenoSdVO> VOs = approveList.get(vbeln);
                        String bname = VOs.get(0).getZtabExpRelfilenoSd().getBname();
                        ztabExpRelfilenoSdFacade.updateByBnameVbeln(tcSapclient.getClient(), bname, vbeln, true);
                    }
                }
                items = newItems;
                sortItems(items);
            } catch (Exception e) {
                logger.error("batchSign, e={}", e);
            }
        } else {
            for (Exception exception : exceptions) {
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage());
            }
        }
    }

    public void selectAllChange(AjaxBehaviorEvent event) {
        for (ZtabExpRelfilenoSdVO item : items) {
            if (item.isReviewable()) {
                item.setSelected(selectAll);
            }
        }
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        //移除未選取的row.
        Set<String> selectedBerslSet = new HashSet<String>();
        List<Integer> removeRows = new ArrayList<Integer>();
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            HSSFRow row = sheet.getRow(i);
            HSSFCell cell = row.getCell(0);
            boolean selected = Boolean.parseBoolean(cell.getStringCellValue());
            if (selected) {
                String bersl = row.getCell(5).getStringCellValue();
                logger.debug("bersl={}", bersl);
                selectedBerslSet.add(bersl);
            }
        }
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            HSSFRow row = sheet.getRow(i);
            if (!selectedBerslSet.contains(row.getCell(5).getStringCellValue())) {
                removeRows.add(new Integer(i));
            }
        }

        for (Integer removeRow : removeRows) {
            removeRow(sheet, removeRow);
        }
        int lastRowNum = sheet.getLastRowNum();
        for (Integer rowIndex : removeRows) {
            shiftRow(sheet, rowIndex, lastRowNum);
        }

        //replace </br> by \r\n
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row != null) {
                //偏遠地區客戶
                HSSFCell cell3 = row.getCell(3);
                if (cell3 != null) {
                    cell3.setCellValue(cell3.getStringCellValue().replace("<br/>", ""));
                }
                //客戶
                HSSFCell cell = row.getCell(4);
                if (cell != null) {
                    cell.setCellValue(cell.getStringCellValue().replace("<br/>", " - "));
                }
                //產品
                HSSFCell cell2 = row.getCell(8);
                if (cell2 != null) {
                    cell2.setCellValue(cell2.getStringCellValue().replace("<br/>", " - "));
                }
            }
        }

        //轉換數字欄位
        Integer[] numericColumns = new Integer[]{10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        formatNumericColumns(sheet, numericColumns);

        //移除問題客戶及偏遠地區
        if (!TOP_MANAGER_REVIEW_CODE.equals(this.filter.getBersl())) {
            Integer[] removeColumns2 = new Integer[]{4, 3};
            removeUnnecessaryColumns(sheet, removeColumns2);
        }

//        //標註需審核項目
//        //紅色
//        HSSFCellStyle redCellStyle = wb.createCellStyle();
//        redCellStyle.setFillForegroundColor(HSSFColor.RED.index);
//        redCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        //黃色
//        HSSFCellStyle yellowCellStyle = wb.createCellStyle();
//        yellowCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
//        yellowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        //粗體
//        Font boldFont = wb.createFont();
//        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
//        HSSFCellStyle boldCellStyle = wb.createCellStyle();
//        boldCellStyle.setFont(boldFont);
//        //斜體
//        Font italicFont = wb.createFont();
//        italicFont.setItalic(true);
//        HSSFCellStyle italicCellStyle = wb.createCellStyle();
//        italicCellStyle.setFont(italicFont);
//
//        //底線
//        Font underlineFont = wb.createFont();
//        underlineFont.setUnderline(Font.U_SINGLE);
//        HSSFCellStyle underlineStyle = wb.createCellStyle();
//        underlineStyle.setFont(underlineFont);

        //粗體+底線
        Font boldUnderlineFont = wb.createFont();
        boldUnderlineFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        boldUnderlineFont.setUnderline(Font.U_SINGLE);
        HSSFCellStyle boldUnderlineCellStyle = wb.createCellStyle();
        boldUnderlineCellStyle.setFont(boldUnderlineFont);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            logger.debug("row={}", row);
            String bstzd = "";
            int bstzdCol = BSTZD_COL;
            if (TOP_MANAGER_REVIEW_CODE.equals(this.filter.getBersl())) {
                bstzdCol += SHIFT_COLS;
            }
            HSSFCell cell = row.getCell(bstzdCol);
            if (cell == null) {
                continue;
            }
            bstzd = cell.getStringCellValue();
            logger.debug("bstzd={}", bstzd);
            //Price
            HSSFCellStyle priceStyle = null;
            if (isManagerReview(bstzd)) {
                priceStyle = boldUnderlineCellStyle;
            } else if (isTopManagerReview(bstzd)) {
                priceStyle = boldUnderlineCellStyle;
            }
            //若第三碼不為V, 表示簽核階層不由價格決定, reset color.
            if (StringUtils.isEmpty(bstzd)
                    || "0".equals(bstzd.substring(1, 2))
                    || !(bstzd.length() > 2 && bstzd.substring(2, 3).equals("V"))) {
                priceStyle = null;
            }
            if (priceStyle != null) {
                int priceCol = PRICE_COL;
                int priceCol2 = PRICE_COL2;
                if (TOP_MANAGER_REVIEW_CODE.equals(this.filter.getBersl())) {
                    priceCol += SHIFT_COLS;
                    priceCol2 += SHIFT_COLS;
                }
                HSSFCell priceCell = row.getCell(priceCol);
                priceCell.setCellStyle(priceStyle);
                HSSFCell price2Cell = row.getCell(priceCol2);
                price2Cell.setCellStyle(priceStyle);
            }

            //Quantity
            HSSFCellStyle quantityStyle = null;
            if (isManagerReview(bstzd)) {
                quantityStyle = boldUnderlineCellStyle;
            } else if (isTopManagerReview(bstzd)) {
                quantityStyle = boldUnderlineCellStyle;
            }
            //若第四碼不為Q, 表示簽核階層不由價格決定, reset color.
            if (StringUtils.isEmpty(bstzd)
                    || "0".equals(bstzd.substring(1, 2))
                    || !(bstzd.length() > 3 && bstzd.substring(3, 4).equals("Q"))) {
                quantityStyle = null;
            }
            if (quantityStyle != null) {
                int quantityCol = QUANTITY_COL;
                if (TOP_MANAGER_REVIEW_CODE.equals(this.filter.getBersl())) {
                    quantityCol += SHIFT_COLS;
                }

                HSSFCell quantityCell = row.getCell(quantityCol);
                quantityCell.setCellStyle(quantityStyle);
            }
            //Level
            HSSFCellStyle levelStyle = null;
            if (isManagerReview(bstzd)) {
                levelStyle = boldUnderlineCellStyle;
            } else if (isTopManagerReview(bstzd)) {
                levelStyle = boldUnderlineCellStyle;
            }
            if (levelStyle != null) {
                int levelCol = LEVEL_COL;
                int vbelnCol = VBELN_COL;
                if (TOP_MANAGER_REVIEW_CODE.equals(this.filter.getBersl())) {
                    levelCol += SHIFT_COLS;
                    vbelnCol += SHIFT_COLS;
                }

                HSSFCell levelCell = row.getCell(levelCol);
                levelCell.setCellStyle(levelStyle);
                HSSFCell vbelnCell = row.getCell(vbelnCol);
                vbelnCell.setCellStyle(levelStyle);
            }
        }

        //移除不必要的欄位
        //因為POI沒有提供刪除欄的功能, 所以必須用Cell shift的方式達成, 
        //注意要移除的欄位順必須由大至小 (右至左).
        int bstzdCol = BSTZD_COL;
        int abCol = AB_COL;
        int pqCol = PQ_COL;
        if (TOP_MANAGER_REVIEW_CODE.equals(this.filter.getBersl())) {
            bstzdCol += SHIFT_COLS;
            abCol += SHIFT_COLS;
            pqCol += SHIFT_COLS;
        }
        Integer[] removeColumns = new Integer[]{bstzdCol, pqCol,abCol, 0};
        removeUnnecessaryColumns(sheet, removeColumns);

    }

    public void removeUnnecessaryColumns(HSSFSheet sheet, Integer[] removeColumns) {
        for (Iterator it = sheet.iterator(); it.hasNext();) {
            HSSFRow row = (HSSFRow) it.next();
            for (int i = 0; i < removeColumns.length; i++) {
                HSSFCell removeCell = row.getCell(removeColumns[i]);
                int lastCellNum = row.getLastCellNum();
                HSSFCell replaceCell = removeCell;
                HSSFCell shiftCell = null;
                //TODO: 移除最後一個欄位會有問題.
                for (int j = removeColumns[i] + 1; j < lastCellNum; j++) {
                    shiftCell = row.getCell(j);
                    switch (shiftCell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            replaceCell.setCellValue(shiftCell.getNumericCellValue());
                            break;
                        default:
                            replaceCell.setCellValue(shiftCell.getStringCellValue());
                    }
                    replaceCell.setCellStyle(shiftCell.getCellStyle());
                    replaceCell = shiftCell;
                }
                //remove last cell.
                if (shiftCell != null) {
                    row.removeCell(shiftCell);
                }
            }
        }
    }

    public void formatNumericColumns(HSSFSheet sheet, Integer[] numbericColumns) {
        for (Iterator it = sheet.iterator(); it.hasNext();) {
            if (it.hasNext()) {
                HSSFRow row = (HSSFRow) it.next();
                for (int i = 0; i < numbericColumns.length; i++) {
                    HSSFCell cell = row.getCell(numbericColumns[i]);
                    try {
                        String stringValue = cell.getStringCellValue().replace(",", "");
                        if (stringValue.length() > 0) {
                            double value = Double.parseDouble(stringValue);
                            row.createCell(numbericColumns[i]).setCellValue(value);
                        }
                    } catch (Exception e) {
                        logger.debug("e={}", e);
                    }
                }
            }
        }
    }

    /**
     * Remove a row by its index
     *
     * @param sheet a Excel sheet
     * @param rowIndex a 0 based index of removing row
     */
    public static void removeRow(HSSFSheet sheet, int rowIndex) {

        HSSFRow removingRow = sheet.getRow(rowIndex);
        if (removingRow != null) {
            sheet.removeRow(removingRow);
        }
    }

    /**
     * Shift a block of an index row till end of row.
     *
     * @param sheet a Excel sheet
     * @param rowIndex a 0 based index of shifting row
     * @param lastRowNum last row index
     */
    public static void shiftRow(HSSFSheet sheet, int rowIndex, int lastRowNum) {
        if (rowIndex >= 0 && rowIndex <= lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
    }
}

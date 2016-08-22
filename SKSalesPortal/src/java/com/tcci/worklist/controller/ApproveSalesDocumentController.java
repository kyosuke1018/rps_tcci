package com.tcci.worklist.controller;

import com.tcci.sksp.controller.util.SessionController;
import com.tcci.worklist.entity.datawarehouse.*;
import com.tcci.worklist.facade.datawarehouse.*;
import com.tcci.worklist.vo.ZtabExpRelfilenoSdVO;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean(name = "approveSalesDocumentController")
@ViewScoped
public class ApproveSalesDocumentController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    //TODO: change jndi name.
    @Resource(mappedName = "jndi/mygui.config")
    transient private Properties jndiConfig;
    private static final Logger logger = LoggerFactory.getLogger(ApproveSalesDocumentController.class.getName());
    private ResourceBundle rb = ResourceBundle.getBundle("worklistMessages");
    private String berslString = "";
    TcSapclient tcSapclient = null;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private ZtabExpRelfilenoSdFacade ztabExpRelfilenoSdFacade;
    @EJB
    private RelfilenoEmpFacade relfilenoEmpFacade;
    @EJB
    private ZtabExpBerslFacade ztabExpBerslFacade;
    @EJB
    private TcSapclientFacade tcSapclientFacade;
    @EJB
    private ZtabExpTj10tFacade ztabExpTj10tFacade;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;
    
    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
    @ManagedProperty(value = "#{salesDocumentController}")
    private SalesDocumentController salesDocumentController;
    
    public void setSalesDocumentController(SalesDocumentController salesDocumentController) {
        this.salesDocumentController = salesDocumentController;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    
    public String getBerslString() {
        return berslString;
    }
    
    public void setBerslString(String berslString) {
        this.berslString = berslString;
    }

    //</editor-fold>
    @PostConstruct
    private void init() {
        logger.debug("init()");
        tcSapclient = tcSapclientFacade.getByCode("sking");
        RelfilenoEmp relfilenoEmp = relfilenoEmpFacade.findByEmpCode(sessionController.getUser().getEmpId());
        logger.debug("relfilenoEmp={}", relfilenoEmp);
        List<ZtabExpBersl> bersls = ztabExpBerslFacade.getBerslsByBname(relfilenoEmp.getBname());
        logger.debug("bersls.size()={}", bersls.size());
        berslString = "";
        for (ZtabExpBersl bersl : bersls) {
            logger.debug("bersl={}", bersl.getBersl());
            if (berslString.length() > 0) {
                berslString += " / ";
            }
            berslString += bersl.getBersl();
            ZtabExpTj10t ztabExpTj10t = ztabExpTj10tFacade.getTj10tBySprasBersl(tcSapclient.getClient(), "M", bersl.getBersl());
            if (ztabExpTj10t != null) {
                String berslDesc = ztabExpTj10t.getTxt();
                berslString += ":" + berslDesc;
            }
        }
        logger.debug("relfilenoEmp={}", relfilenoEmp);
        List<ZtabExpRelfilenoSd> ztabExpRelfilenoSds = ztabExpRelfilenoSdFacade.findBySapclientAndBname(tcSapclient.getClient(), relfilenoEmp.getBname());
        List<ZtabExpRelfilenoSdVO> items = new ArrayList<ZtabExpRelfilenoSdVO>();
        for (ZtabExpRelfilenoSd ztabExpRelfilenoSd : ztabExpRelfilenoSds) {
            ZtabExpRelfilenoSdVO item = new ZtabExpRelfilenoSdVO(ztabExpRelfilenoSd);
            if ("0".equals(ztabExpRelfilenoSd.getBstzd().substring(1, 2))) {
                item.setReviewable(false);
            }else {
                item.setReviewable(true);
            }
            items.add(item);
        }
        salesDocumentController.sortItems(items);
        salesDocumentController.setItems(items);
    }
    
    public void batchApprove() {
        salesDocumentController.setUsermode("A");
        salesDocumentController.batchSign();
    }
}

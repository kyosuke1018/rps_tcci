package com.tcci.worklist.controller.cache;

import com.tcci.sksp.controller.util.SessionController;
import com.tcci.worklist.controller.util.JsfUtils;
import com.tcci.worklist.facade.datawarehouse.ZtabExpRelfilenoSdFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean(name = "cacheController")
@RequestScoped
public class CacheController {

    //<editor-fold defaultstate="clooapsed" desc="variables">
    private static final Logger logger = LoggerFactory.getLogger(CacheController.class.getName());
    private String refreshType = "0"; //0: All, //1: class //2: entity
    private String parameter;
    private List<SelectItem> refreshTypeList = new ArrayList<SelectItem>();
    //</editor-fold>
    //<editor-fold defaultstate="clooapsed" desc="EJBs">
    @EJB
    transient private ZtabExpRelfilenoSdFacade ztabExpRelfilenoSdFacade;
    @ManagedProperty(value="#{sessionController}")
    SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
    
    //</editor-fold>
    //<editor-fold defaultstate="clooapsed" desc="getter, setter">

    public String getRefreshType() {
        return refreshType;
    }

    public void setRefreshType(String refreshType) {
        this.refreshType = refreshType;
    }

    public List<SelectItem> getRefreshTypeList() {
        return refreshTypeList;
    }

    public void setRefreshTypeList(List<SelectItem> refreshTypeList) {
        this.refreshTypeList = refreshTypeList;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    //</editor-fold>

    @PostConstruct
    private void init() {
        refreshTypeList.add(new SelectItem("0", "All"));
        refreshTypeList.add(new SelectItem("1", "Class (e.g. com.tcci.fc.entity.org.TcUser)"));
        refreshTypeList.add(new SelectItem("2", "Entity (e.g. com.tcci.fc.entity.org.TcUser:1)"));
    }

    public String refreshCache() {
        logger.debug("refreshCache by " + sessionController.getUser().getDisplayIdentifier() + " start at " + new Date());
        try {
            logger.debug("this.refreshType="+this.refreshType);
            if ("0".equals(this.refreshType)) {
                ztabExpRelfilenoSdFacade.clearCache();
                JsfUtils.addSuccessMessage("Clear all cache successful!");
            } else if ("1".equals(this.refreshType)) {
                if(StringUtils.isEmpty(parameter)) {
                    JsfUtils.addErrorMessage("請輸入 Class 名稱 (範例: com.tcci.fc.entity.org.TcUser)!");
                    return null;
                }
                Class cls = Class.forName(parameter);
                try {
                    ztabExpRelfilenoSdFacade.clearClassCache(cls);
                } catch (Exception e) {
                    logger.debug("CacheController.refreshClassCache() by ztabExpRelfilenoSdFacade", e);
                    //do nothing, since cause class not found exception.
                }
                String msg = "Clear the cache for " + parameter + " successful!";
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            } else if ("2".equals(this.refreshType)) {
                if(StringUtils.isEmpty(parameter)) {
                    JsfUtils.addErrorMessage("請輸入 OID (範例: com.tcci.fc.entity.org.TcUser:1)!");
                    return null;
                }
                Object obj = null;
                try {
                    obj = ztabExpRelfilenoSdFacade.getObject(parameter);
                    if (obj != null) {
                        ztabExpRelfilenoSdFacade.clearEntityCache(obj);
                    }
                } catch (Exception e) {
                    logger.debug("CacheController.refreshEntityCache() by ztabExpRelfilenoSdFacade", e);
                }
                String msg = "Refresh " + parameter + " successful!";
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            }
        } catch (Exception e) {
            JsfUtils.addErrorMessage(e, "CacheController.refreshCache()");
        }
        logger.debug("refreshCache by " + sessionController.getUser().getDisplayIdentifier() + " end at " + new Date());
        return null;
    }
}

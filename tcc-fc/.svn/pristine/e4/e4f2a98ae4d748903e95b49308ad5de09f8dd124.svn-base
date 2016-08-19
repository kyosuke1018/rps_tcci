/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.org;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.OrgCompany;
import com.tcci.fc.facade.org.OrgManagementFacade;
import com.tcci.fc.util.ExcelUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "companyImport")
@ViewScoped
public class CompanyImport {
    private static final Logger logger = LoggerFactory.getLogger(CompanyImport.class);
    private static final int MAX_IMPORT_ROWS = 5000;

    private List<CompanyVO> result;

    @EJB
    private OrgManagementFacade orgManagementFacade;

    private Map<String, OrgCompany> mapCompany = new HashMap<String, OrgCompany>();

    private ResourceBundle rb = ResourceBundle.getBundle("msgOrgmgmt",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());

    // action
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile tfile = event.getFile();
        try {
            result = ExcelUtil.importList(tfile.getInputstream(),
                    CompanyVO.class, 1, MAX_IMPORT_ROWS);
            JsfUtil.addSuccessMessage(result.size() + " rows loaded.");
            verify();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }

    public void save() {
        if (null==result || result.isEmpty()) {
            JsfUtil.addErrorMessage("no data!");
            return;
        }
        boolean success = true;
        for (CompanyVO vo : result) {
            if (!vo.isValid() || vo.getStatus() == CompanyVO.ST_NOCHANGE) {
                continue;
            }
            if (vo.getStatus() == CompanyVO.ST_INSERT) {
                OrgCompany orgCompany = new OrgCompany(vo.getCode(), vo.getName(), vo.isActivation());
                vo.setOrgCompany(orgCompany);
                if (!saveVO(vo, "inserted.")) {
                    success = false;
                }
            }
            if (vo.getStatus() == CompanyVO.ST_UPDATE) {
                OrgCompany orgCompany = vo.getOrgCompany();
                orgCompany.setCode(vo.getCode());
                orgCompany.setName(vo.getName());
                orgCompany.setActivation(vo.isActivation());
                if (!saveVO(vo, "updated.")) {
                    success = false;
                }
            }
        }

        if (success) {
            JsfUtil.addSuccessMessage("save successed!");
        } else {
            JsfUtil.addErrorMessage("save exception happen!");
        }
    }

    // helper
    public String getPageTitle() {
        return rb.getString("orgmgmt.title.companyimport");
    }

    private void verify() {
        if (null==result || result.isEmpty()) {
            JsfUtil.addErrorMessage("no data!");
            return;
        }
        boolean invalid = false;
        for (CompanyVO vo : result) {
            if (!vo.isValid()) {
                invalid = true;
                continue; // import時資料已經有誤
            }
            vo.setOrgCompany(findCompany(vo.getCode()));
            vo.checkForUpdate();
        }
        if (invalid) {
            JsfUtil.addErrorMessage("Some data invalid!");
        }
    }

    private OrgCompany findCompany(String code) {
        if (mapCompany.containsKey(code)) {
            return mapCompany.get(code);
        }

        OrgCompany orgCompany = orgManagementFacade.findCompany(code);
        if (orgCompany != null) {
            mapCompany.put(code, orgCompany);
        }

        return orgCompany;
    }

    private boolean saveVO(CompanyVO vo, String successMsg) {
        try {
            orgManagementFacade.save(vo.getOrgCompany());
            vo.setStatus(CompanyVO.ST_NOCHANGE);
            vo.setMessage(successMsg);
            if (!mapCompany.containsKey(vo.getCode())) {
                mapCompany.put(vo.getCode(), vo.getOrgCompany());
            }
            return true;
        } catch (Exception ex) {
            vo.setValid(false);
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    // getter, setter
    public List<CompanyVO> getResult() {
        return result;
    }

    public void setResult(List<CompanyVO> result) {
        this.result = result;
    }
}

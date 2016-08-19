/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.org;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.OrgCompany;
import com.tcci.fc.entity.org.OrgDepartment;
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
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jimmy
 */
@ManagedBean(name = "departmentImport")
@ViewScoped
public class DepartmentImport {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentImport.class);
    private static final int MAX_IMPORT_ROWS = 5000;

    private List<DepartmentVO> result;

    @EJB
    private OrgManagementFacade orgManagementFacade;
    
    private Map<String, OrgCompany> mapCompany = new HashMap<String, OrgCompany>();
    private Map<String, OrgDepartment> mapDepartment = new HashMap<String, OrgDepartment>();

    private ResourceBundle rb = ResourceBundle.getBundle("msgOrgmgmt",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());

    // action
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile tfile = event.getFile();
        try {
            result = ExcelUtil.importList(tfile.getInputstream(),
                    DepartmentVO.class, 1, MAX_IMPORT_ROWS);
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
        for (DepartmentVO vo : result) {
            if (!vo.isValid() || vo.getStatus()==DepartmentVO.ST_NOCHANGE) {
                continue;
            }
            OrgDepartment parent = null;
            if (!StringUtils.isEmpty(vo.getParentCode())) {
                parent = findDepartment(vo.getCompCode(), vo.getParentCode());
                if (parent == null) {
                    vo.setMessage("parent not found, try later");
                    continue;
                }
            }
            if (vo.getStatus()==DepartmentVO.ST_INSERT) {
                OrgDepartment orgDepartment = new OrgDepartment(
                        vo.getOrgCompany(),
                        vo.getCode(),
                        vo.getName(),
                        vo.isActivation(),
                        parent);
                vo.setOrgDepartment(orgDepartment);
                if (!saveVO(vo, "inserted.")) {
                    success = false;
                }
            }
            if (vo.getStatus()==DepartmentVO.ST_UPDATE) {
                OrgDepartment orgDepartment = vo.getOrgDepartment();
                orgDepartment.setOrgCompany(vo.getOrgCompany());
                orgDepartment.setCode(vo.getCode());
                orgDepartment.setName(vo.getName());
                orgDepartment.setActivation(vo.isActivation());
                orgDepartment.setParent(parent);
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
        return rb.getString("orgmgmt.title.departmentimport");
    }

    private void verify() {
        if (null==result || result.isEmpty()) {
            JsfUtil.addErrorMessage("no data!");
            return;
        }
        boolean invalid = false;
        for (DepartmentVO vo : result) {
            if (!vo.isValid()) {
                invalid = true;
                continue; // import時資料已經有誤
            }
            OrgCompany orgCompany = findCompany(vo.getCompCode());
            if (null == orgCompany) {
                invalid = true;
                vo.setValid(false);
                vo.setMessage("company not found.");
                continue;
            }
            vo.setOrgCompany(orgCompany);
            vo.setOrgDepartment(findDepartment(vo.getCompCode(), vo.getCode()));
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

    private OrgDepartment findDepartment(String compCode, String deptCode) {
        String key = compCode + ":" + deptCode;
        if (mapDepartment.containsKey(key)) {
            return mapDepartment.get(key);
        }

        OrgCompany orgCompany = findCompany(compCode);
        if (orgCompany == null) {
            return null;
        }

        OrgDepartment orgDepartment = orgManagementFacade.findDepartment(orgCompany, deptCode);
        if (orgDepartment != null) {
            mapDepartment.put(key, orgDepartment);
        }

        return orgDepartment;
    }

    private boolean saveVO(DepartmentVO vo, String successMsg) {
        try {
            orgManagementFacade.save(vo.getOrgDepartment());
            vo.setStatus(DepartmentVO.ST_NOCHANGE);
            vo.setMessage(successMsg);
            String deptKey = vo.getCompCode() + ":" + vo.getCode();
            if (!mapDepartment.containsKey(deptKey)) {
                mapDepartment.put(deptKey, vo.getOrgDepartment());
            }
            return true;
        } catch (Exception ex) {
            vo.setValid(false);
            vo.setMessage(ex.getMessage());
            return false;
        }
    }
    
    // getter, setter
    public List<DepartmentVO> getResult() {
        return result;
    }

    public void setResult(List<DepartmentVO> result) {
        this.result = result;
    }

    
}

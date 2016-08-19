/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.org;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.org.OrgCompany;
import com.tcci.fc.entity.org.OrgDepartment;
import com.tcci.fc.entity.org.OrgEmployee;
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
@ManagedBean(name = "employeeImport")
@ViewScoped
public class EmployeeImport {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeImport.class);
    private static final int MAX_IMPORT_ROWS = 5000;

    private List<EmployeeVO> result;

    @EJB
    private OrgManagementFacade orgManagementFacade;

    private Map<String, OrgCompany> mapCompany = new HashMap<String, OrgCompany>();
    private Map<String, OrgDepartment> mapDepartment = new HashMap<String, OrgDepartment>();
    private Map<String, OrgEmployee> mapEmployee = new HashMap<String, OrgEmployee>();

    private ResourceBundle rb = ResourceBundle.getBundle("msgOrgmgmt",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());

    // action
    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile tfile = event.getFile();
        try {
            result = ExcelUtil.importList(tfile.getInputstream(),
                    EmployeeVO.class, 1, MAX_IMPORT_ROWS);
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
        for (EmployeeVO vo : result) {
            if (!vo.isValid() || vo.getStatus()==EmployeeVO.ST_NOCHANGE) {
                continue;
            }
            if (vo.getStatus()==EmployeeVO.ST_INSERT) {
                OrgEmployee orgEmployee = new OrgEmployee(
                        vo.getOrgCompany(),
                        vo.getOrgDepartment(),
                        vo.getCode(),
                        vo.getName(),
                        vo.getEmail(),
                        vo.getAdaccount(),
                        vo.isActivation());
                vo.setOrgEmployee(orgEmployee);
                if (!saveVO(vo, "inserted.")) {
                    success = false;
                }
            }
            if (vo.getStatus()==EmployeeVO.ST_UPDATE) {
                OrgEmployee orgEmployee = vo.getOrgEmployee();
                orgEmployee.setOrgCompany(vo.getOrgCompany());
                orgEmployee.setOrgDepartment(vo.getOrgDepartment());
                orgEmployee.setCode(vo.getCode());
                orgEmployee.setName(vo.getName());
                orgEmployee.setAdaccount(vo.getAdaccount());
                orgEmployee.setActivation(vo.isActivation());
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
        return rb.getString("orgmgmt.title.employeeimport");
    }

    private void verify() {
        if (null==result || result.isEmpty()) {
            JsfUtil.addErrorMessage("no data!");
            return;
        }
        boolean invalid = false;
        for (EmployeeVO vo : result) {
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
            OrgDepartment orgDepartment = findDepartment(vo.getCompCode(), vo.getDeptCode());
            if (null == orgDepartment) {
                invalid = true;
                vo.setValid(false);
                vo.setMessage("department not found.");
                continue;
            }
            vo.setOrgDepartment(orgDepartment);
            vo.setOrgEmployee(findEmployee(vo.getCode()));
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

    public OrgEmployee findEmployee(String code) {
        if (mapEmployee.containsKey(code)) {
            return mapEmployee.get(code);
        }

        OrgEmployee orgEmployee = orgManagementFacade.findEmployee(code);
        if (null != orgEmployee) {
            mapEmployee.put(code, orgEmployee);
        }

        return orgEmployee;
    }

    private boolean saveVO(EmployeeVO vo, String successMsg) {
        try {
            orgManagementFacade.save(vo.getOrgEmployee());
            vo.setStatus(DepartmentVO.ST_NOCHANGE);
            vo.setMessage(successMsg);
            if (mapEmployee.containsKey(vo.getCode())) {
                mapEmployee.put(vo.getCode(), vo.getOrgEmployee());
            }
            return true;
        } catch (Exception ex) {
            vo.setValid(false);
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    // getter, setter
    public List<EmployeeVO> getResult() {
        return result;
    }

    public void setResult(List<EmployeeVO> result) {
        this.result = result;
    }

}

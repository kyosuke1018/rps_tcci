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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "orgManagement")
@ViewScoped
public class OrgManagement {
    private static final Logger logger = LoggerFactory.getLogger(OrgManagement.class);

    // page data
    private List path;                          // 目前路徑 (可能是 OrgCompany 或 OrgDepartment)
    private List children;                      // 子資料夾 (可能是 OrgCompany 或 OrgDepartment)
    private List<OrgEmployee> listEmployee;     // 目前路徑下的員工 (含子部門)
    private String nameFilter;                  // 搜尋員工 (姓名,工號,帳號,EMAIL)
    private List<OrgEmployee> employeeFiltered; // 過濾後的員工 (顯示於頁面)

    // ejb
    @EJB
    private OrgManagementFacade orgManagementFacade;

    // internal data
    private ResourceBundle rb = ResourceBundle.getBundle("msgOrgmgmt",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());

    @PostConstruct
    private void init() {
        path = new ArrayList<Object>();
        employeeFiltered = new ArrayList<OrgEmployee>();
        gotoNode(null);
    }

    // action
    // click目前路徑或子資料夾裡的node(公司或部門), null表示最上層
    public void gotoNode(Object node) {
        if (null == node) {
            children = orgManagementFacade.findAllCompany();
            sortCompany(children);
            listEmployee = orgManagementFacade.findAllEmployee();
            sortEmployee(listEmployee);
            filterEmployee();
            path.clear();
        } else if (node instanceof OrgCompany) {
            OrgCompany comp = (OrgCompany) node;
            children = orgManagementFacade.findTopDepartment(comp);
            sortDepartment(children);
            listEmployee = orgManagementFacade.findEmployeeByCompany((OrgCompany) node);
            sortEmployee(listEmployee);
            filterEmployee();
            path.clear();
            path.add(node);
        } else if (node instanceof OrgDepartment) {
            OrgDepartment dept = (OrgDepartment) node;
            children = orgManagementFacade.findSubDepartment(dept);
            sortDepartment(children);
            listEmployee = orgManagementFacade.findEmployeeByDepartment(dept, true);
            sortEmployee(listEmployee);
            filterEmployee();
            path.clear();
            path.add(dept.getOrgCompany());
            while (dept != null) {
                path.add(1, dept);
                dept = dept.getParent();
            }
        } else {
            JsfUtil.addErrorMessage("unknown node:" + node);
        }
    }

    // 搜尋員工資料有變時(keyup時檢查)
    public void nameFilterChange() {
        filterEmployee();
    }

    // helper
    public String getPageTitle() {
        return rb.getString("orgmgmt.title");
    }

    // 公司:顯示名稱, 部門:顯示 代碼 名稱
    public String showNodeName(Object node) {
        if (node instanceof OrgCompany) {
            OrgCompany comp = (OrgCompany) node;
            return comp.getName();
        } else if (node instanceof OrgDepartment) {
            OrgDepartment dept = (OrgDepartment) node;
            return dept.getCode() + " " + dept.getName();
        } else {
            return node.toString();
        }
    }

    public boolean isNodeActive(Object node) {
        if (node instanceof OrgCompany) {
            OrgCompany comp = (OrgCompany) node;
            return comp.isActivation();
        } else if (node instanceof OrgDepartment) {
            OrgDepartment dept = (OrgDepartment) node;
            return dept.isActivation();
        } else if (node instanceof OrgEmployee) {
            OrgEmployee employee = (OrgEmployee) node;
            return employee.isActivation();
        }
        return true;
    }

    // 公司依名稱排序
    private void sortCompany(List<OrgCompany> list) {
        if (null != list && !list.isEmpty()) {
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    OrgCompany comp1 = (OrgCompany) o1;
                    OrgCompany comp2 = (OrgCompany) o2;
                    return comp1.getName().compareTo(comp2.getName());
                }
            });
        }
    }

    // 部門依代碼排序
    private void sortDepartment(List<OrgDepartment> list) {
        if (null != list && !list.isEmpty()) {
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    OrgDepartment dept1 = (OrgDepartment) o1;
                    OrgDepartment dept2 = (OrgDepartment) o2;
                    return dept1.getCode().compareTo(dept2.getCode());
                }
            });
        }
    }

    // 員工依公司,部門,員工編號排序
    private void sortEmployee(List<OrgEmployee> list) {
        if (null != list && !list.isEmpty()) {
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    OrgEmployee emp1 = (OrgEmployee) o1;
                    OrgEmployee emp2 = (OrgEmployee) o2;
                    int compare = emp1.getOrgCompany().getName().compareTo(emp2.getOrgCompany().getName());
                    if (compare == 0) {
                        compare = emp1.getOrgDepartment().getCode().compareTo(emp2.getOrgDepartment().getCode());
                        if (compare == 0) {
                            compare = emp1.getCode().compareTo(emp2.getCode());
                        }
                    }
                    return compare;
                }
            });
        }
    }

    // 過濾員工清單 (姓名,工號,帳號,EMAIL)
    private void filterEmployee() {
        employeeFiltered.clear();
        if (listEmployee==null || listEmployee.isEmpty())
            return;
        String filter = StringUtils.trimToEmpty(nameFilter).toLowerCase();
        if (filter.isEmpty()) {
            employeeFiltered.addAll(listEmployee);
        } else {
            for (OrgEmployee emp : listEmployee) {
                if (employeeContainsFilter(emp, filter)) {
                    employeeFiltered.add(emp);
                }
            }
        }
    }

    private boolean employeeContainsFilter(OrgEmployee emp, String filter) {
        return StringUtils.contains(emp.getName(), filter)
                || StringUtils.contains(emp.getCode(), filter)
                || StringUtils.contains(emp.getAdaccount(), filter)
                || StringUtils.contains(emp.getEmail(), filter)
                ;
    }

    // getter, setter
    public List getPath() {
        return path;
    }

    public void setPath(List path) {
        this.path = path;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public List<OrgEmployee> getListEmployee() {
        return listEmployee;
    }

    public void setListEmployee(List<OrgEmployee> listEmployee) {
        this.listEmployee = listEmployee;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public List<OrgEmployee> getEmployeeFiltered() {
        return employeeFiltered;
    }

    public void setEmployeeFiltered(List<OrgEmployee> employeeFiltered) {
        this.employeeFiltered = employeeFiltered;
    }
}

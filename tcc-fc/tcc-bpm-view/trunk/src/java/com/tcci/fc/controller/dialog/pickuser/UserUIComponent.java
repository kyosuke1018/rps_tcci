package com.tcci.fc.controller.dialog.pickuser;

import java.io.Serializable;

/**
 *
 * @author Greg.Chou
 */
public class UserUIComponent implements Serializable{
    private String employeeNumber;
    private String name;
    private String email;
    private String companyName;
    private GroupUIComponent department;

    public UserUIComponent() {
    }

    public UserUIComponent(String employeeNumber, String name, String email, String companyName, GroupUIComponent department) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.email = email;
        this.companyName = companyName;
        this.department = department;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public GroupUIComponent getDepartment() {
        return department;
    }

    public void setDepartment(GroupUIComponent department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

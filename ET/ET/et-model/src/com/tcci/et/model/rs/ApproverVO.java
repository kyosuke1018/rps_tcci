/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rs;

import java.util.Set;

/**
 *
 * @author Peter.pan
 */
public class ApproverVO {
    private Set<String> manager1;  // 一階主管(帳號)
    private Set<String> manager2;  // 二階主管
    private Set<String> manager3;  // 三階主管
    private Set<String> plantHR;   // 廠(公司)人資
    private Set<String> generalHR; // 總處人資
    private Set<String> hrManager; // 人資主管

    public Set<String> getManager1() {
        return manager1;
    }

    public void setManager1(Set<String> manager1) {
        this.manager1 = manager1;
    }

    public Set<String> getManager2() {
        return manager2;
    }

    public void setManager2(Set<String> manager2) {
        this.manager2 = manager2;
    }

    public Set<String> getManager3() {
        return manager3;
    }

    public void setManager3(Set<String> manager3) {
        this.manager3 = manager3;
    }

    public Set<String> getPlantHR() {
        return plantHR;
    }

    public void setPlantHR(Set<String> plantHR) {
        this.plantHR = plantHR;
    }

    public Set<String> getGeneralHR() {
        return generalHR;
    }

    public void setGeneralHR(Set<String> generalHR) {
        this.generalHR = generalHR;
    }

    public Set<String> getHrManager() {
        return hrManager;
    }

    public void setHrManager(Set<String> hrManager) {
        this.hrManager = hrManager;
    }

}

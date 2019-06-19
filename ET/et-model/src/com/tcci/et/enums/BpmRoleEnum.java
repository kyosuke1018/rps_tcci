/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jimmy.Lee
 */
public enum BpmRoleEnum {
    PLANT_MM(true, true, true), // 廠窗口
    HZ_MM(false, true, true),   // 杭州本部物料人員
    HQ_MM(true, true, true);    //總處物料人員
    
    
    private boolean bc;
    private boolean stationery;
    private boolean pcp;
    
    BpmRoleEnum(boolean bc, boolean stationery, boolean pcp){
        this.bc = bc;
        this.stationery = stationery;
        this.pcp = pcp;
    }
    

    /**
     * bc列表
     * @return
     */
    public static List<BpmRoleEnum> getBc(){
        List<BpmRoleEnum> list = new ArrayList<>();
        for (BpmRoleEnum enum1 : BpmRoleEnum.values()) {
            if (enum1.bc) {
                list.add(enum1);
            }
        }
        return list;
    }
    public static List<BpmRoleEnum> getStationery(){
        List<BpmRoleEnum> list = new ArrayList<>();
        for (BpmRoleEnum enum1 : BpmRoleEnum.values()) {
            if (enum1.stationery) {
                list.add(enum1);
            }
        }
        return list;
    }
    public static List<BpmRoleEnum> getPcp(){
        List<BpmRoleEnum> list = new ArrayList<>();
        for (BpmRoleEnum enum1 : BpmRoleEnum.values()) {
            if (enum1.pcp) {
                list.add(enum1);
            }
        }
        return list;
    }
    
}

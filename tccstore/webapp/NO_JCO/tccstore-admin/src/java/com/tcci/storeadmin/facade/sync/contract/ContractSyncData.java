/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.contract;

import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcContractProduct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Jimmy.Lee
 */
public class ContractSyncData {

    private Set<String> setPK1 = new TreeSet<>(); // code
    private Set<String> setPK2 = new TreeSet<>(); // code:posnr

    private Map<String, VBAK> mapVBAK = new HashMap<>();
    private Map<String, VBAP> mapVBAP = new HashMap<>();
    private Map<String, VBKD> mapVBKD = new HashMap<>();
    private Map<String, VBUP> mapVBUP = new HashMap<>();

    private Map<String, EcContract> mapECCO = new HashMap<>();
    private Map<String, EcContractProduct> mapECCP = new HashMap<>();

    private List<ContractVO> result1 = new ArrayList<>();
    private List<ContractProductVO> result2 = new ArrayList<>();

    //
    public void add(VBAK vbak) {
        String pk1 = vbak.getVbeln();
        setPK1.add(pk1);
        mapVBAK.put(pk1, vbak);
    }

    public void add(VBAP vbap) {
        String pk2 = vbap.getVbeln() + ":" + vbap.getPosnr();
        setPK2.add(pk2);
        mapVBAP.put(pk2, vbap);
    }

    public void add(VBKD vbkd) {
        String pk2 = vbkd.getVbeln() + ":" + vbkd.getPosnr();
        // setPK2.add(pk2);
        mapVBKD.put(pk2, vbkd);
    }

    public void add(VBUP vbup) {
        String pk2 = vbup.getVbeln() + ":" + vbup.getPosnr();
        // setPK2.add(pk2);
        mapVBUP.put(pk2, vbup);
    }

    public void add(EcContract ecco) {
        String pk1 = ecco.getCode();
        setPK1.add(pk1);
        mapECCO.put(pk1, ecco);
    }

    public void add(EcContractProduct eccp) {
        String pk2 = eccp.getEcContract().getCode() + ":" + eccp.getEcContractProductPK().getPosnr();
        setPK2.add(pk2);
        mapECCP.put(pk2, eccp);
    }
    
    public void add(ContractVO vo1) {
        result1.add(vo1);
    }
    
    public void add(ContractProductVO vo2) {
        result2.add(vo2);
    }
    
    public VBAK getVBAK(String pk1) {
        return mapVBAK.get(pk1);
    }
    
    public VBAP getVBAP(String pk2) {
        return mapVBAP.get(pk2);
    }
    
    public VBKD getVBKD(String pk2) {
        return mapVBKD.get(pk2);
    }
    
    public VBUP getVBUP(String pk2) {
        return mapVBUP.get(pk2);
    }
    
    public EcContract getECCO(String pk1) {
        return mapECCO.get(pk1);
    }
    
    public EcContractProduct getECCP(String pk2) {
        return mapECCP.get(pk2);
    }

    // getter, setter
    public Set<String> getSetPK1() {
        return setPK1;
    }

    public void setSetPK1(Set<String> setPK1) {
        this.setPK1 = setPK1;
    }

    public Set<String> getSetPK2() {
        return setPK2;
    }

    public void setSetPK2(Set<String> setPK2) {
        this.setPK2 = setPK2;
    }

    public Map<String, VBAK> getMapVBAK() {
        return mapVBAK;
    }

    public void setMapVBAK(Map<String, VBAK> mapVBAK) {
        this.mapVBAK = mapVBAK;
    }

    public Map<String, VBAP> getMapVBAP() {
        return mapVBAP;
    }

    public void setMapVBAP(Map<String, VBAP> mapVBAP) {
        this.mapVBAP = mapVBAP;
    }

    public Map<String, VBKD> getMapVBKD() {
        return mapVBKD;
    }

    public void setMapVBKD(Map<String, VBKD> mapVBKD) {
        this.mapVBKD = mapVBKD;
    }

    public Map<String, VBUP> getMapVBUP() {
        return mapVBUP;
    }

    public void setMapVBUP(Map<String, VBUP> mapVBUP) {
        this.mapVBUP = mapVBUP;
    }

    public Map<String, EcContract> getMapECCO() {
        return mapECCO;
    }

    public void setMapECCO(Map<String, EcContract> mapECCO) {
        this.mapECCO = mapECCO;
    }

    public Map<String, EcContractProduct> getMapECCP() {
        return mapECCP;
    }

    public void setMapECCP(Map<String, EcContractProduct> mapECCP) {
        this.mapECCP = mapECCP;
    }

    public List<ContractVO> getResult1() {
        return result1;
    }

    public void setResult1(List<ContractVO> result1) {
        this.result1 = result1;
    }

    public List<ContractProductVO> getResult2() {
        return result2;
    }

    public void setResult2(List<ContractProductVO> result2) {
        this.result2 = result2;
    }

}

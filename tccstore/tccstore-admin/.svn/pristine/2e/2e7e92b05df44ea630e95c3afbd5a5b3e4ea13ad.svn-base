/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.customer;

import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcCustomerSales;
import com.tcci.tccstore.entity.EcCustomerVkorg;
import com.tcci.tccstore.entity.EcSales;
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
public class CustomerSyncData {

    private Set<String> setPK1 = new TreeSet<>(); // 客戶代碼
    private Map<String, CustomerVO> mapCUVO = new HashMap<>();
    private Map<String, EcCustomer> mapECCU = new HashMap<>();

    private Set<String> setPK2 = new TreeSet<>(); // 業務代碼
    private Map<String, SalesVO> mapSAVO = new HashMap<>();
    private Map<String, EcSales> mapECSA = new HashMap<>();

    private Set<String> setPK3 = new TreeSet<>(); // 客戶業務
    private Map<String, CustomerSalesVO> mapCUSA = new HashMap<>();
    private Map<String, EcCustomerSales> mapECCS = new HashMap<>();
    
    private Set<String> setPK4 = new TreeSet<>(); // 客戶銷售組織
    private Map<String, CustomerVkorgVO> mapCUVK = new HashMap<>();
    private Map<String, EcCustomerVkorg> mapECCV = new HashMap<>();

    private List<CustomerVO> result1 = new ArrayList<>();
    private List<SalesVO> result2 = new ArrayList<>();
    private List<CustomerSalesVO> result3 = new ArrayList<>();
    private List<CustomerVkorgVO> result4 = new ArrayList<>();

    public void add(CustomerVO cuvo) {
        String pk1 = cuvo.getCode();
        setPK1.add(pk1);
        mapCUVO.put(pk1, cuvo);
    }

    public void add(SalesVO savo) {
        String pk2 = savo.getCode();
        setPK2.add(pk2);
        mapSAVO.put(pk2, savo);
    }

    public void add(CustomerSalesVO cusa) {
        String pk3 = cusa.getCustomer() + ":" + cusa.getSales();
        setPK3.add(pk3);
        mapCUSA.put(pk3, cusa);
    }
    
    public void add(CustomerVkorgVO cuvk) {
        String pk4 = cuvk.getCustomer() + ":" + cuvk.getVkorg();
        setPK4.add(pk4);
        mapCUVK.put(pk4, cuvk);
    }

    public void add(EcCustomer eccu) {
        String pk1 = eccu.getCode();
        setPK1.add(pk1);
        mapECCU.put(pk1, eccu);
    }

    void add(EcSales ecsa) {
        String pk2 = ecsa.getCode();
        setPK2.add(pk2);
        mapECSA.put(pk2, ecsa);
    }

    void add(EcCustomerSales eccs) {
        String pk3 = eccs.getEcCustomer().getCode() + ":" + eccs.getEcSales().getCode();
        setPK3.add(pk3);
        mapECCS.put(pk3, eccs);
    }
    
    void add(EcCustomerVkorg eccv) {
        String pk4 = eccv.getEcCustomer().getCode() + ":" + eccv.getEcCustomerVkorgPK().getVkorg();
        setPK4.add(pk4);
        mapECCV.put(pk4, eccv);
    }

    public CustomerVO getCUVO(String pk1) {
        return mapCUVO.get(pk1);
    }

    public EcCustomer getECCU(String pk1) {
        return mapECCU.get(pk1);
    }

    public SalesVO getSAVO(String pk2) {
        return mapSAVO.get(pk2);
    }

    public EcSales getECSA(String pk2) {
        return mapECSA.get(pk2);
    }

    public CustomerSalesVO getCUSA(String pk3) {
        return mapCUSA.get(pk3);
    }

    public EcCustomerSales getECCS(String pk3) {
        return mapECCS.get(pk3);
    }
    
    public CustomerVkorgVO getCUVK(String pk4) {
        return mapCUVK.get(pk4);
    }
    
    public EcCustomerVkorg getECCV(String pk4) {
        return mapECCV.get(pk4);
    }

    public void addResult(CustomerVO cuvo) {
        result1.add(cuvo);
    }

    public void addResult(SalesVO savo) {
        result2.add(savo);
    }

    public void addResult(CustomerSalesVO cusa) {
        result3.add(cusa);
    }
    
    public void addResult(CustomerVkorgVO cuvk) {
        result4.add(cuvk);
    }

    // getter, setter
    public Set<String> getSetPK1() {
        return setPK1;
    }

    public void setSetPK1(Set<String> setPK1) {
        this.setPK1 = setPK1;
    }

    public Map<String, CustomerVO> getMapCUVO() {
        return mapCUVO;
    }

    public void setMapCUVO(Map<String, CustomerVO> mapCUVO) {
        this.mapCUVO = mapCUVO;
    }

    public Map<String, EcCustomer> getMapECCU() {
        return mapECCU;
    }

    public void setMapECCU(Map<String, EcCustomer> mapECCU) {
        this.mapECCU = mapECCU;
    }

    public Set<String> getSetPK2() {
        return setPK2;
    }

    public void setSetPK2(Set<String> setPK2) {
        this.setPK2 = setPK2;
    }

    public Map<String, SalesVO> getMapSAVO() {
        return mapSAVO;
    }

    public void setMapSAVO(Map<String, SalesVO> mapSAVO) {
        this.mapSAVO = mapSAVO;
    }

    public Map<String, EcSales> getMapECSA() {
        return mapECSA;
    }

    public void setMapECSA(Map<String, EcSales> mapECSA) {
        this.mapECSA = mapECSA;
    }

    public Set<String> getSetPK3() {
        return setPK3;
    }

    public void setSetPK3(Set<String> setPK3) {
        this.setPK3 = setPK3;
    }

    public Map<String, CustomerSalesVO> getMapCUSA() {
        return mapCUSA;
    }

    public void setMapCUSA(Map<String, CustomerSalesVO> mapCUSA) {
        this.mapCUSA = mapCUSA;
    }

    public Map<String, EcCustomerSales> getMapECCS() {
        return mapECCS;
    }

    public void setMapECCS(Map<String, EcCustomerSales> mapECCS) {
        this.mapECCS = mapECCS;
    }

    public Set<String> getSetPK4() {
        return setPK4;
    }

    public void setSetPK4(Set<String> setPK4) {
        this.setPK4 = setPK4;
    }

    public Map<String, CustomerVkorgVO> getMapCUVK() {
        return mapCUVK;
    }

    public void setMapCUVK(Map<String, CustomerVkorgVO> mapCUVK) {
        this.mapCUVK = mapCUVK;
    }

    public Map<String, EcCustomerVkorg> getMapECCV() {
        return mapECCV;
    }

    public void setMapECCV(Map<String, EcCustomerVkorg> mapECCV) {
        this.mapECCV = mapECCV;
    }

    public List<CustomerVO> getResult1() {
        return result1;
    }

    public void setResult1(List<CustomerVO> result1) {
        this.result1 = result1;
    }

    public List<SalesVO> getResult2() {
        return result2;
    }

    public void setResult2(List<SalesVO> result2) {
        this.result2 = result2;
    }

    public List<CustomerSalesVO> getResult3() {
        return result3;
    }

    public void setResult3(List<CustomerSalesVO> result3) {
        this.result3 = result3;
    }

    public List<CustomerVkorgVO> getResult4() {
        return result4;
    }

    public void setResult4(List<CustomerVkorgVO> result4) {
        this.result4 = result4;
    }

}

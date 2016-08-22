/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync.delivery;

import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcDeliveryVkorg;
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
public class DeliverySyncData {

    private Set<String> setPK1 = new TreeSet<>(); // code
    private Set<String> setPK2 = new TreeSet<>(); // code:vkorg

    // source: SAP/CSV
    private Map<String, KNA1> mapKNA1 = new HashMap<>();
    private Map<String, KNVV> mapKNVV = new HashMap<>();

    // destination: DB
    private Map<String, EcDeliveryPlace> mapECDP = new HashMap<>();
    private Map<String, EcDeliveryVkorg> mapECDV = new HashMap<>();

    // compare result
    private List<DeliveryVO> result1 = new ArrayList<>();
    private List<DeliveryVkorgVO> result2 = new ArrayList<>();

    // public API
    public void add(KNA1 kna1) {
        String kunnr = kna1.getKunnr();
        if (kunnr.matches("S.....")) { // S開頭, 6碼
            setPK1.add(kunnr);
            mapKNA1.put(kunnr, kna1);
        }
    }

    public void add(KNVV knvv) {
        String kunnr = knvv.getKunnr();
        if (kunnr.matches("S.....")) { // S開頭, 6碼
            String key = kunnr + ":" + knvv.getVkorg();
            setPK2.add(key);
            mapKNVV.put(key, knvv);
        }
    }

    public void add(EcDeliveryPlace ecdp) {
        String key = ecdp.getCode();
        setPK1.add(key);
        mapECDP.put(key, ecdp);
    }

    public void add(EcDeliveryVkorg ecdv) {
        String key = ecdv.getEcDeliveryPlace().getCode() + ":" + ecdv.getPk().getVkorg();
        setPK2.add(key);
        mapECDV.put(key, ecdv);
    }

    public KNA1 getKNA1(String pk1) {
        return mapKNA1.get(pk1);
    }

    public KNVV getKNVV(String pk2) {
        return mapKNVV.get(pk2);
    }

    public EcDeliveryPlace getECDP(String pk1) {
        return mapECDP.get(pk1);
    }

    public EcDeliveryVkorg getECDV(String pk2) {
        return mapECDV.get(pk2);
    }

    public void add(DeliveryVO vo) {
        result1.add(vo);
    }
    
    public void add(DeliveryVkorgVO vo2) {
        result2.add(vo2);
    }

    public boolean hasAnyResult() {
        return !result1.isEmpty() || !result2.isEmpty();
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

    public Map<String, KNA1> getMapKNA1() {
        return mapKNA1;
    }

    public void setMapKNA1(Map<String, KNA1> mapKNA1) {
        this.mapKNA1 = mapKNA1;
    }

    public Map<String, KNVV> getMapKNVV() {
        return mapKNVV;
    }

    public void setMapKNVV(Map<String, KNVV> mapKNVV) {
        this.mapKNVV = mapKNVV;
    }

    public Map<String, EcDeliveryPlace> getMapECDP() {
        return mapECDP;
    }

    public void setMapECDP(Map<String, EcDeliveryPlace> mapECDP) {
        this.mapECDP = mapECDP;
    }

    public Map<String, EcDeliveryVkorg> getMapECDV() {
        return mapECDV;
    }

    public void setMapECDV(Map<String, EcDeliveryVkorg> mapECDV) {
        this.mapECDV = mapECDV;
    }

    public List<DeliveryVO> getResult1() {
        return result1;
    }

    public void setResult1(List<DeliveryVO> result1) {
        this.result1 = result1;
    }

    public List<DeliveryVkorgVO> getResult2() {
        return result2;
    }

    public void setResult2(List<DeliveryVkorgVO> result2) {
        this.result2 = result2;
    }

}

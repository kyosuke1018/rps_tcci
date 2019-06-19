/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.statistic;

/**
 *
 * @author Peter.pan
 */
public class StatisticPrdTypeVO extends StatisticVO {
    private Long typeL1;
    private String typeL1Name;
    private Long typeL2;
    private String typeL2Name;
    private Long typeId;
    private String typeName;
    
    public void genLabel(){
        StringBuilder sb = new StringBuilder();
        if( typeL1Name!=null ){
            sb.append(typeL1Name);
        }
        if( typeL2Name!=null ){
            sb.append("／").append(typeL2Name);
        }
        if( typeName!=null ){
            sb.append("／").append(typeName);
        }
        this.setLabel(sb.toString());
    }

    public Long getTypeL1() {
        return typeL1;
    }

    public void setTypeL1(Long typeL1) {
        this.typeL1 = typeL1;
    }

    public String getTypeL1Name() {
        return typeL1Name;
    }

    public void setTypeL1Name(String typeL1Name) {
        this.typeL1Name = typeL1Name;
    }

    public Long getTypeL2() {
        return typeL2;
    }

    public void setTypeL2(Long typeL2) {
        this.typeL2 = typeL2;
    }

    public String getTypeL2Name() {
        return typeL2Name;
    }

    public void setTypeL2Name(String typeL2Name) {
        this.typeL2Name = typeL2Name;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}

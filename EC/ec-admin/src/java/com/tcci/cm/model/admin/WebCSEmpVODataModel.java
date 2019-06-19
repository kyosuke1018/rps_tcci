/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.admin;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 * 
 * @author Peter
 */
public class WebCSEmpVODataModel extends ListDataModel<WebCSEmpVO> implements SelectableDataModel<WebCSEmpVO>,  Serializable {

    public  WebCSEmpVODataModel(List<WebCSEmpVO> list) {
        super(list);
    }

    @Override
    public Object getRowKey(WebCSEmpVO emp) {
        return Long.valueOf(emp.getId()).toString();
    }

    @Override
    public WebCSEmpVO getRowData(String rowKey) {
        List<WebCSEmpVO> empList = (List<WebCSEmpVO>) getWrappedData();
        for (WebCSEmpVO emp :  empList) {
            if (Long.valueOf(emp.getId()).toString().equals(rowKey)) {
                return emp;
            }
        }
        return null;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import com.tcci.sksp.vo.SalesDetailsVO;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Jason.Yu
 */
public class SalesDetailDataModel extends ListDataModel<SalesDetailsVO> implements SelectableDataModel<SalesDetailsVO>{

    public SalesDetailDataModel(List<SalesDetailsVO> salesDetailsList) {
        super(salesDetailsList);
    }
    
    @Override
    public Object getRowKey(SalesDetailsVO t) {
        return t.getOrderNumber();
    }

    @Override
    public SalesDetailsVO getRowData(String rowKey) {
        List<SalesDetailsVO> list = (List<SalesDetailsVO>)getWrappedData();
        for( SalesDetailsVO object : list){
            if(object.getOrderNumber().toString().equals(rowKey) ){
                return object;
            }
        }
        return null;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import com.tcci.sksp.vo.SalesOrderVO;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Jason.Yu
 */
public class SalesOrderDataModel extends ListDataModel<SalesOrderVO> implements SelectableDataModel<SalesOrderVO> {

    public SalesOrderDataModel(List<SalesOrderVO> salesOrderList) {
        super(salesOrderList);
    }

    @Override
    public Object getRowKey(SalesOrderVO salesOrder) {
        return salesOrder.getId();
    }

    @Override
    public SalesOrderVO getRowData(String rowKey) {
        List<SalesOrderVO> list = (List<SalesOrderVO>)getWrappedData();
        for( SalesOrderVO object : list){
            if(object.getId().toString().equals(rowKey) ){
                return object;
            }
        }
        return null;
    }
    
}

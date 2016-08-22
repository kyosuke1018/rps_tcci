/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.SkCustomer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Jason.Yu
 */
public class CustomerDataModel extends ListDataModel<SkCustomer> implements SelectableDataModel<SkCustomer> {

    public CustomerDataModel(List<SkCustomer> customerList) {
        super( customerList);
    }
    
    @Override
    public Object getRowKey(SkCustomer customer) {
        return customer.getId();
    }

    @Override
    public SkCustomer getRowData(String rowKey) {
        List<SkCustomer> list = (List<SkCustomer>)getWrappedData();
        for( SkCustomer customer : list){
            if(customer.getId().toString().equals(rowKey) ){
                return customer;
            }
        }
        return null;
    }
}

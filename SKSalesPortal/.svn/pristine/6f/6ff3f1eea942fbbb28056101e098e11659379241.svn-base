/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.SkBank;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Jason.Yu
 */
public class BankDataModel extends ListDataModel<SkBank> implements SelectableDataModel<SkBank> {

    BankDataModel(List<SkBank> bankList) {
        super( bankList );
    }

    @Override
    public Object getRowKey(SkBank bank) {
        return bank.getId();
    }

    @Override
    public SkBank getRowData(String rowKey) {
        List<SkBank> list = (List<SkBank>)getWrappedData();
        for( SkBank customer : list){
            if(customer.getId().toString().equals(rowKey) ){
                return customer;
            }
        }
        return null;
    }
    
}

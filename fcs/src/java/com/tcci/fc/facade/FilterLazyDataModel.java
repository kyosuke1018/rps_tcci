/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade;

import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;




/**
 * keep datasource to wrappedData 
 * @author gilbert
 */
public class FilterLazyDataModel<T> extends BaseLazyDataModel<T>{
    public FilterLazyDataModel() {
        super();
    }
    public FilterLazyDataModel(List<T> datasource) {
        super(datasource);
        setWrappedData(datasource);
    }    

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        setWrappedData(super.load(first, pageSize, sortField, sortOrder, filters));
        return (List<T>)getWrappedData();
    }

    
    

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade;


import com.tcci.fc.facade.util.LazySorter;
import com.tcci.fc.facade.util.NativeSQLUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
public class BaseLazyDataModel<T> extends LazyDataModel<T> implements Serializable{

    protected static final Logger logger = LoggerFactory.getLogger(BaseLazyDataModel.class);
    // Data Source for binding resultList to the DataTable
    protected List<T> datasource;// (含 filter, sort, 換頁)前查詢結果
    public List<T> afterFilterDatasource;// (含 filter, sort, 換頁)後查詢結果
    public BaseLazyDataModel() {
        super();
    }
    public BaseLazyDataModel(List<T> datasource) {
        this.datasource = datasource;
        setRowCount((datasource==null)? 0:datasource.size());
    }
 
    /**
     * 重設為初始狀態
     */
    public void reset(){
        if( datasource != null){
            datasource.clear();
        }
        this.setRowCount(0);
    }
    
    @Override
    public T getRowData(String rowKey) {
        for(T item : datasource) {
            //if(row.getId().equals(rowKey)) {
            if( new Integer(item.hashCode()).toString().equals(rowKey) ) {
                return item;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(T item) {
        //return cust.getCustomerId();
        return item.hashCode();
    }

    /**
     * datatable 載入程序 (含 filter, sort, 換頁)
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param filters　filter條件Map
     * @return 
     */
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        logger.debug("== load ==> filters = "+ ((filters==null)? "null":filters.toString()));
        logger.debug("== load ==> sortField = "+ ((sortField==null)? "null":sortField));
        logger.debug("== load ==> sortOrder = "+ ((sortOrder==null)? "null":sortOrder.toString()));
        
//        List<T> resultList = new ArrayList<T>();
        afterFilterDatasource = new ArrayList<>();

        // filter
        if( filters.isEmpty() ){
            afterFilterDatasource = datasource;
        }else{
            Map<String, String[]> filtersZFZH = new HashMap<>();//繁簡轉換後的查詢條件(簡繁體陣列)
            if (filters != null) {
                for (Entry<String, String> filter : filters.entrySet()) {
                    String[] compFilterValues = NativeSQLUtils.converterZhCode((String)filter.getValue());
                    if (compFilterValues != null) {
                        filtersZFZH.put(filter.getKey(), compFilterValues);
                    }
                }

                for (T row : datasource) {
                    if (doFilter(row, filtersZFZH)) {
                        afterFilterDatasource.add(row);
                    }
                }
            }
        }

        // sort
        if(sortField != null) {
            Collections.sort(afterFilterDatasource, new LazySorter<T>(sortField, sortOrder));
        }

        // rowCount
        int dataSize = afterFilterDatasource.size();
        this.setRowCount(dataSize);
         
        // paginate
        if(dataSize > pageSize) {
            try {
                int fromIndex = first;
                int toIndex = first + pageSize;
                if(toIndex > dataSize){
                    toIndex = dataSize;
                }
                return afterFilterDatasource.subList(fromIndex, toIndex);
            } catch (Exception e) {
                logger.debug("load subList exception ", e);
                return afterFilterDatasource.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return afterFilterDatasource;
        }
    }

    /**
     * 針對一筆資料做 Filter 檢查
     *
     * @param row 一筆資料
     * @param filters filterMap[key:;value:繁或簡字串陣列]
     * @return 
     */
    private boolean doFilter(T row, Map<String, String[]> filters) {
        boolean match = true;
        // 檢查各項 filter 條件
        for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
            try {
                String filterProperty = it.next();
                String[] filterValues = filters.get(filterProperty);
                if (filterValues != null) {
                    Object fieldValue = PropertyUtils.getProperty(row, filterProperty);
                    // 不分大小寫，前置詞相同
                    String compFieldValue = (fieldValue == null) ? "" : fieldValue.toString().toUpperCase();
                    
                    if (filterValues.length > 1) {
                        if (compFieldValue.indexOf(filterValues[0].toUpperCase()) < 0 && compFieldValue.indexOf(filterValues[1].toUpperCase()) < 0) {
                            match = false;
                            break; // 一項 Filter 不符合即 false
                        }
                    } else {
                        if (compFieldValue.indexOf(filterValues[0].toUpperCase()) < 0) {
                            match = false;
                            break; // 一項 Filter 不符合即 false
                        }
                    }
                }
            } catch(Exception e) {
                logger.debug("load exception ", e);
                match = false;
            } 
        }

        return match;
    }
    
    public List<T> getAfterFilterDatasource() {
        return afterFilterDatasource;
}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.global;

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
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 * @param <T>
 */
public class BaseLazyDataModel<T> extends LazyDataModel<T> implements SelectableDataModel<T>, Serializable{
    protected static final Logger logger = LoggerFactory.getLogger(BaseLazyDataModel.class);
    // Data Source for binding resultList to the DataTable
    protected List<T> datasource;// (含 filter, sort, 換頁)前查詢結果
    public List<T> afterFilterDatasource;// (含 filter, sort, 換頁)後查詢結果
    private Map<String, List<String>> combinedFilterFields;// 合併 filter 的欄位
    private int first = 0; // 目前第一筆index

    public BaseLazyDataModel() {
        super();
    }
    
    public BaseLazyDataModel(List<T> datasource) {
        this.datasource = datasource;
        setWrappedData(datasource);
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
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        logger.debug("== load ==> first = "+ first);
        logger.debug("== load ==> pageSize = "+ pageSize);
        logger.debug("== load ==> filters = "+ ((filters==null)? "null":filters.toString()));
        logger.debug("== load ==> sortField = "+ ((sortField==null)? "null":sortField));
        logger.debug("== load ==> sortOrder = "+ ((sortOrder==null)? "null":sortOrder.toString()));
        
        this.setFirst(first);
        afterFilterDatasource = new ArrayList<T>();

        // filter
        if( filters==null || filters.isEmpty() ){
            afterFilterDatasource = datasource;
            if( afterFilterDatasource==null ){
                afterFilterDatasource = new ArrayList<T>();
            }
        }else{
            Map<String, String[]> filtersZFZH = new HashMap<String, String[]>();//繁簡轉換後的查詢條件(簡繁體陣列)
            for (Entry<String, Object> filter : filters.entrySet()) {
                // String[] compFilterValues = SQLUtils.converterZhCode(filter.getValue()); // 繁簡
                String[] compFilterValues = new String[]{filter.getValue().toString()};
                if (compFilterValues != null) {
                    filtersZFZH.put(filter.getKey(), compFilterValues);
                }
            }

            if( combinedFilterFields==null || combinedFilterFields.isEmpty() ){// 一般 Filter
                for (T row : datasource) {
                    if (doFilter(row, filtersZFZH)) {
                        afterFilterDatasource.add(row);
                    }
                }
            }else{// 有組合欄位
                for (T row : datasource) {
                    if (doCombinedFilter(row, filtersZFZH)) {
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
            logger.debug("load subList : dataSize "+dataSize+"; first = "+first + "; pageSize = "+ pageSize +"; first + pageSize="+(first + pageSize));
            try {
                if( first + pageSize > dataSize ){
//                    setWrappedData(afterFilterDatasource.subList(first, first + (dataSize % pageSize)));
                    return afterFilterDatasource.subList(first, first + (dataSize % pageSize));
                }else{
//                    setWrappedData(afterFilterDatasource.subList(first, first + pageSize));
                    return afterFilterDatasource.subList(first, first + pageSize);
                }
            } catch (Exception e) {
                logger.debug("BaseLazyDataModel Exception:\n", e);
//                setWrappedData(afterFilterDatasource);
                return afterFilterDatasource;
            }
        } else {
//            setWrappedData(afterFilterDatasource);
            return afterFilterDatasource;
        }
        
//        return (List<T>)getWrappedData();
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
        int count = 0;
        for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
            count++;
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

//        logger.debug("count = "+count);
        return match;
    }

    /**
     * 針對一筆資料做 Filter 檢查 (有組合欄位)
     *
     * @param row 一筆資料
     * @param filters filterMap[key:;value:繁或簡字串陣列]
     * @return 
     */
    private boolean doCombinedFilter(T row, Map<String, String[]> filters) {
        boolean match = true;
        // 檢查各項 filter 條件
        int count = 0;
        try {
            for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                count++;
                String field = it.next();
                
                List<String> filterPropertys = combinedFilterFields.get(field);
                boolean tmpMatch = true;
                for(String filterProperty : filterPropertys){
                    String[] filterValues = filters.get(filterProperty);
                    if (filterValues != null) {
                        Object fieldValue = PropertyUtils.getProperty(row, filterProperty);
                        // 不分大小寫，前置詞相同
                        String compFieldValue = (fieldValue == null) ? "" : fieldValue.toString().toUpperCase();

                        if (filterValues.length > 1) {
                            if (compFieldValue.indexOf(filterValues[0].toUpperCase()) < 0 && compFieldValue.indexOf(filterValues[1].toUpperCase()) < 0) {
                                tmpMatch = false;
                            }else{
                                tmpMatch = true;
                                break; // 組合欄位符合，一欄位符合即
                            }
                        } else {
                            if (compFieldValue.indexOf(filterValues[0].toUpperCase()) < 0) {
                                tmpMatch = false;
                            }else{
                                tmpMatch = true;
                                break; // 組合欄位符合，一欄位符合即
                            }
                        }
                    }
                }
                if( !tmpMatch ){
                    match = false;
                    break; // 一項 Filter 不符合即 false
                }
            }
        } catch(Exception e) {
            logger.debug("load exception ", e);
            match = false;
        } 

        logger.debug("count = "+count);
        return match;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">    
    public List<T> getAfterFilterDatasource() {
        return afterFilterDatasource;
    }
    
    public int getFilteredRowCount(){
        return (afterFilterDatasource!=null? afterFilterDatasource.size():0);
    }

    public List<T> getDatasource() {
        return datasource;
    }

    public void setDatasource(List<T> datasource) {
        this.datasource = datasource;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public Map<String, List<String>> getCombinedFilterFields() {
        return combinedFilterFields;
    }

    public void setCombinedFilterFields(Map<String, List<String>> combinedFilterFields) {
        this.combinedFilterFields = combinedFilterFields;
    }
    //</editor-fold>
}

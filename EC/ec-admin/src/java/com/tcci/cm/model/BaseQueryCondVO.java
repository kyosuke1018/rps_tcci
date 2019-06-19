/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model;

import com.tcci.cm.model.admin.TcFactoryVO;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcUser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Datawarehouse 查詢條件Base VO，包含基本條件: 廠別、日期、查詢筆數. 
 * @author gilbert
 */
public class BaseQueryCondVO {
    private DateFormat format = new SimpleDateFormat("yyyyMMdd"); 

    private List<TcFactoryVO> queryFactories;//所選廠別 
    private List<TcFactoryVO> querySpecFactories;// ex. 跨廠查詢PO履歷已核發  
    private Date startDate;
    private Date endDate;
    private int limitQryRows;//查詢筆數(for Top)
    private int maxResultsSize;//最大查詢結果筆數限制(若超過可throw exception)

    private TcUser loginUser; // 執行者帳號
    
    public BaseQueryCondVO() {
        maxResultsSize = GlobalConstant.DEF_MAX_RESULT_SIZE;
    }
    
    public void setDefCommonCond(){
        Calendar c = Calendar.getInstance();
        endDate = c.getTime();
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 1);//系統年的1/1
        startDate = c.getTime(); 
    }
    
    public String getQueryFactoriesString(){
        StringBuilder sb = new StringBuilder();
        if(!CollectionUtils.isEmpty(queryFactories)){
            int i = 1;
            int size = queryFactories.size();
            for (TcFactoryVO tcFactoryVO : queryFactories) {
                sb.append(tcFactoryVO.getFactoryCode());
                if(i != size){
                    sb.append(",");
                }
                i++;
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 取的實際有完整權限的工廠別 (剔除包含在 queryCorssFactories 中的廠別)
     */
    public List<TcFactoryVO> getFullQueryFactories(){
        if( queryFactories==null || queryFactories.isEmpty() 
                || querySpecFactories==null || querySpecFactories.isEmpty() ){
            return queryFactories;
        }
        
        // 剔除包含在 getRealQueryFactories 中的廠別
        List<TcFactoryVO> keeps = new ArrayList<TcFactoryVO>();
        for(TcFactoryVO tcFactoryVO : queryFactories){
            boolean existed = false;
            for(TcFactoryVO tcFactoryVO2 : querySpecFactories){
                if( !existed && tcFactoryVO.equals(tcFactoryVO2) ){
                    existed = true;
                }
            }
            if( !existed ){
                keeps.add(tcFactoryVO);
            }
        }
        
        return keeps;
    }
    
    public String getStartDateString(){
        StringBuilder sb = new StringBuilder();
        if(startDate != null){
            sb.append(format.format(startDate));
        }
        return sb.toString();
    }
    
    public String getEndDateString(){
        StringBuilder sb = new StringBuilder();
        if(endDate != null){
            sb.append(format.format(endDate));
        }        
        return sb.toString();
    }    
    
    public List<String> getListByString(String aString) {
        if (StringUtils.isBlank(aString)) {
            return null;
        }
        List<String> aList = new ArrayList<String>();
        String sListString = StringUtils.trim(aString);
        String[] sStringAry = sListString.split(",");
        for (String string : sStringAry) {
            if (StringUtils.isNotBlank(string)) {
                aList.add(string);
            }
        }
        return aList;
    }    
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<TcFactoryVO> getQuerySpecFactories() {
        return querySpecFactories;
    }

    public void setQuerySpecFactories(List<TcFactoryVO> querySpecFactories) {
        this.querySpecFactories = querySpecFactories;
    }
    
    public List<TcFactoryVO> getQueryFactories() {
        return queryFactories;
    }
    
    public void setQueryFactories(List<TcFactoryVO> queryFactories) {
        this.queryFactories = queryFactories;
    }
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }    
    public int getLimitQryRows() {
        return limitQryRows;
    }

    public void setLimitQryRows(int limitQryRows) {
        this.limitQryRows = limitQryRows;
    }

//    public String getQueryYearStart() {
//        return queryYearStart;
//    }
//
//    public void setQueryYearStart(String queryYearStart) {
//        this.queryYearStart = queryYearStart;
//    }
//
//    public String getQueryMonthStart() {
//        return queryMonthStart;
//    }
//
//    public void setQueryMonthStart(String queryMonthStart) {
//        this.queryMonthStart = queryMonthStart;
//    }
//
//    public String getQueryYearEnd() {
//        return queryYearEnd;
//    }
//
//    public void setQueryYearEnd(String queryYearEnd) {
//        this.queryYearEnd = queryYearEnd;
//    }
//
//    public String getQueryMonthEnd() {
//        return queryMonthEnd;
//    }
//
//    public void setQueryMonthEnd(String queryMonthEnd) {
//        this.queryMonthEnd = queryMonthEnd;
//    }    

    public int getMaxResultsSize() {
        return maxResultsSize;
    }

    public void setMaxResultsSize(int maxResultsSize) {
        this.maxResultsSize = maxResultsSize;
    }

    public TcUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(TcUser loginUser) {
        this.loginUser = loginUser;
    }
    //</editor-fold>
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.sksp.entity.ar.SkSalesAllowances;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class SalesAllowancesListController {

    private List<SkSalesAllowances> salesAllowancesList;
    private Set<String> processedOrders = new HashSet<String>(); // 有ReFIID的訂單,不能編輯刪除
            
    @PostConstruct
    private void init() {
        salesAllowancesList = new ArrayList<SkSalesAllowances>();
    }

    public List<SkSalesAllowances> getSalesAllowancesList() {
        return salesAllowancesList;
    }

    public void setSalesAllowancesList(List<SkSalesAllowances> salesAllowancesList) {
        this.salesAllowancesList = salesAllowancesList;
        for (SkSalesAllowances sa : salesAllowancesList) {
            String orderNumber = sa.getOrderNumber();
            String returnNumber = sa.getReturnNumber();
            // Jimmy, 20120613, 增加applyDate, 表示是同一天上傳的的資料
            if (StringUtils.isNotEmpty(orderNumber) && StringUtils.isNotEmpty(returnNumber))
                processedOrders.add(orderNumber + ":" + sa.getApplyDate());
        }
    }

    public boolean isShowExportButton() {
        return !this.salesAllowancesList.isEmpty();
    }
    
    public boolean isProcessedOrderNumber(SkSalesAllowances sa) {
        String orderNumber = sa.getOrderNumber();
        return processedOrders.contains(orderNumber + ":" + sa.getApplyDate());
    }
    
}

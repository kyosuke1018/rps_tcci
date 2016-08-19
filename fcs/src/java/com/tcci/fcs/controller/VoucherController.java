/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fcs.controller.queryCondition.VoucherQueryCondition;
import com.tcci.fcs.enums.CompanyGroupEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.CloseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mendel.Lee
 */
@ManagedBean(name = "voucherController")
@ViewScoped
public class VoucherController {

    private static final Logger logger = LoggerFactory.getLogger(VoucherController.class);
    
    // Query Condition
    private VoucherQueryCondition queryCondition
	    = new VoucherQueryCondition();
    
    // 企業團選項
    private List<SelectItem> groupsOptionList;
    
    // 會計科目選項
    private List<String> accountOptionList = new ArrayList<String>();
    
    // 明細科目選項
    private List<String> detailOptionList = new ArrayList<String>();
    
    // 列表清單
    private List<VoucherMaster> dataTableList = new ArrayList<VoucherMaster>();
    
    // 選擇之物件
    private VoucherMaster selectMasterObject = new VoucherMaster();
    private int selectMasterIndex = -1;
    
    // 選擇之物件
    private VoucherDetail selectDetailObject = new VoucherDetail();
    private int selectDetailIndex = -1;
    
    private Map<String, String> accountMap = new HashMap<String, String>();
    private Map<String, String> detailMap = new HashMap<String, String>();
    
    private boolean editable = false;

    @PostConstruct
    private void init() {
	
	groupsOptionList = this.buildGroupOptions();

	this.accountOptionList.add("4110-銷貨收入");
	this.accountOptionList.add("4190-銷貨折讓");
	this.accountOptionList.add("4310-租賃收入");
	
	this.detailOptionList.add("1000-台灣水泥");
//	this.detailOptionList
	
	this.accountMap.put("4110", "銷貨收入");
	this.accountMap.put("4190", "銷貨折讓");
	this.accountMap.put("4310", "租賃收入");
	
	this.detailMap.put("1000", "台灣水泥");
	
	VoucherMaster master = new VoucherMaster();
	master.setId(2015090001L);
	master.setAccountDate("2015/09");
	master.setVoucherType("T");
	master.setVoucherStatus("輸入");
	master.setSummary("調整傳票");
	master.setDebitBalance(new BigDecimal("125000"));
	master.setCreditBalance(new BigDecimal("100000"));
	master.setDifferBalance(new BigDecimal("25000"));
	master.setPostingPerson("");
	this.dataTableList.add(master);
	
	master = new VoucherMaster();
	master.setId(2015090002L);
	master.setAccountDate("2015/09");
	master.setVoucherType("T");
	master.setVoucherStatus("過帳");
	master.setSummary("調整傳票");
	master.setDebitBalance(new BigDecimal("125000"));
	master.setCreditBalance(new BigDecimal("100000"));
	master.setDifferBalance(new BigDecimal("25000"));
	master.setPostingPerson("過帳人員");
	master.setPostingDate(new Date(114, 11, 10));
	this.dataTableList.add(master);
	
	List<VoucherDetail> detailList = new ArrayList<VoucherDetail>();
	VoucherDetail detail = new VoucherDetail();
	detail.setId(1L);
	detail.setAccount("4110");
	detail.setAccountName("銷貨收入");
	detail.setDebitAndCredit("D");
	detail.setDetail("1000");
	detail.setDetailName("台灣水泥");
	detail.setAmount(new BigDecimal("125000"));
	detail.setRemark("調整傳票");
	detailList.add(detail);	
	
	detail = new VoucherDetail();
	detail.setId(2L);
	detail.setAccount("4190");
	detail.setAccountName("銷貨折讓");
	detail.setDebitAndCredit("C");
	detail.setDetail("1000");
	detail.setDetailName("台灣水泥");
	detail.setAmount(new BigDecimal("100000"));
	detail.setRemark("調整傳票");
	detailList.add(detail);	
	master.setVoucherDetailList(detailList);
	
	this.selectMasterObject = master;
    }
    
    private List<SelectItem> buildGroupOptions() {
        List<SelectItem> options = new ArrayList();
        for (CompanyGroupEnum item : CompanyGroupEnum.values()) {
            options.add(new SelectItem(item, item.getName()));
        }
        return options;
    }
    
    public List<String> findAccountList(String keyword) {
	if (StringUtils.isNotBlank(keyword)) {
	    return this.accountOptionList;
	} else {
	    return this.accountOptionList;
	}
    }
    
    public List<String> findDetailList(String keyword) {
	if (StringUtils.isNotBlank(keyword)) {
	    return this.detailOptionList;
	} else {
	    return this.detailOptionList;
	}
    }
    
    public void changeAccount() {
	String key = this.selectDetailObject.getAccount();
	System.out.println("account = " + key);
	System.out.println("accountName = " + this.accountMap.get(key));
	this.selectDetailObject.setAccountName(this.accountMap.get(key));
    }
    
    public void changeDetail() {
	String key = this.selectDetailObject.getDetail();
	System.out.println("account = " + key);
	System.out.println("accountName = " + this.detailMap.get(key));
	this.selectDetailObject.setDetailName(this.detailMap.get(key));
    }
    
    /**
     * 新增主檔資料
     */
    public void addMaster() {
	this.selectMasterObject = new VoucherMaster();
	this.selectMasterIndex = -1;
    }
    
    /**
     * 新增明細資料
     */
    public void addDetail() {
	this.selectDetailIndex = -1;
	this.selectMasterObject.getVoucherDetailList().add(new VoucherDetail());
    }
    
    /**
     * 編輯主檔資料
     *
     * @param master
     */
    public void editMaster(VoucherMaster master) {
	this.selectMasterObject = master;
	HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();
//	response.
    }
    
    /**
     * 編輯明細資料
     *
     * @param detail
     */
    public void editDetail() {
	this.editable = true;
//	this.selectDetailObject = detail;
//	this.selectDetailIndex = this.selectMasterObject.getVoucherDetailList().indexOf(detail);
    }
    
    /**
     * 編輯明細資料
     *
     * @param detail
     */
    public void cancelDetail() {
	this.editable = false;
//	this.selectDetailObject = detail;
//	this.selectDetailIndex = this.selectMasterObject.getVoucherDetailList().indexOf(detail);
    }
    
    /**
     * 儲存主檔資料
     */
    public void saveMaster() {
	this.getDataTableList().add(this.selectMasterObject);
    }
    
    /**
     * 儲存明細資料
     */
    public void saveDetail() {
	if (this.selectDetailIndex == -1) {
	    this.selectMasterObject.getVoucherDetailList().add(this.selectDetailObject);
	} else {
	    this.selectMasterObject.getVoucherDetailList().remove(selectDetailIndex);
	    this.selectMasterObject.getVoucherDetailList().add(selectDetailIndex, this.selectDetailObject);
	}
    }
    
    /**
     * 關閉視窗
     *
     * @param event
     */
    public void handleDetailClose(CloseEvent event) {
	if (!FacesContext.getCurrentInstance().isValidationFailed()) {
	    /*當沒錯時*/
	    this.selectDetailObject = new VoucherDetail();
	}
    }
    
    public boolean isMasterEditable(VoucherMaster master) {
	if(master.getVoucherStatus().equals("輸入")) {
	    return true;
	}
	return false;
    }
    
    public boolean isDetailEditable(VoucherDetail detail) {
	
	if (detail.getId() == null || editable == true) {
	    return true;
	}
	
	return false;
    }

    //<editor-fold defaultstate="collapsed" desc="getter & setter">
    public VoucherQueryCondition getQueryCondition() {
	return queryCondition;
    }
    
    public void setQueryCondition(VoucherQueryCondition queryCondition) {
	this.queryCondition = queryCondition;
    }
    
    public List<SelectItem> getGroupsOptionList() {
	return groupsOptionList;
    }

    public void setGroupsOptionList(List<SelectItem> groupsOptionList) {
	this.groupsOptionList = groupsOptionList;
    }
    
    public List<String> getAccountOptionList() {
	return accountOptionList;
    }
    
    public void setAccountOptionList(List<String> accountOptionList) {
	this.accountOptionList = accountOptionList;
    }
    
    public List<String> getDetailOptionList() {
	return detailOptionList;
    }
    
    public void setDetailOptionList(List<String> detailOptionList) {
	this.detailOptionList = detailOptionList;
    }

    public List<VoucherMaster> getDataTableList() {
	return dataTableList;
    }

    public void setDataTableList(List<VoucherMaster> dataTableList) {
	this.dataTableList = dataTableList;
    }
    
    public VoucherMaster getSelectMasterObject() {
	return selectMasterObject;
    }
    
    public void setSelectMasterObject(VoucherMaster selectMasterObject) {
	this.selectMasterObject = selectMasterObject;
    }
    
    public VoucherDetail getSelectDetailObject() {
	return selectDetailObject;
    }
    
    public void setSelectDetailObject(VoucherDetail selectDetailObject) {
	this.selectDetailObject = selectDetailObject;
    }
//</editor-fold>
}
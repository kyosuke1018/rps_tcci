/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.sheetdata;

import com.tcci.fc.controller.dataimport.ImportExcelBase2;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.util.ExcelUtil;
import com.tcci.fc.util.time.DateUtils;
import static com.tcci.fc.util.time.DateUtils.convertStringToDate;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.sheetdata.ZtfiAfrcInvo;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcInvoFacade;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author gilbert
 */
@ManagedBean(name = "importZtcoRelaGuiController")
@ViewScoped
public class ImportZtcoRelaGuiController extends ImportExcelBase2<ZtfiAfrcInvoVO> {

    //<editor-fold defaultstate="collapsed" desc="Injects">
    @Inject
    private ZtfiAfrcInvoFacade ztfiAfrcInvoFacade;
    //</editor-fold>    

    private String yearMonth;
    private FcCompany company;

    private Map<String, ZtfiAfrcInvo> mapEntity;
    
    // 檔案上傳驗證－公司
    private Set<String> companySet;
    // 檔案上傳驗證－幣別
    private Set<String> currencySet;

    @PostConstruct
    protected void init() {

    }

    //String sheetName = "關企對帳發票記錄";
    public ImportZtcoRelaGuiController() {
	super(ZtfiAfrcInvoVO.class);
	headerRow = 1; // row 3
	sheetName = "關係人帳進銷項發票記錄";
    }

    //<editor-fold defaultstate="collapsed" desc="handleFileUpload">
    public void handleFileUpload(UploadedFile uploadFile) {
	UploadedFile tfile = uploadFile;
	fileName = truncateFilename(tfile.getFileName());
	try {
	    datalist = ExcelUtil.importList(tfile.getInputstream(),
		    clazz, headerRow, maxRows, sheetName);
	    if (null == datalist) {
		String msg = "sheet 不存在! sheetName=" + sheetName;
		JsfUtil.addErrorMessage(msg);
		return;
	    }
	    JsfUtil.addSuccessMessage(datalist.size() + " 筆資料載入.");
	    // 驗證資料並執行postInit
	    verify();
	    invalidOnlyChange();
	} catch (Exception ex) {
	    JsfUtil.addErrorMessage(ex.getMessage());
	    ex.printStackTrace();
	}
    }

    @Override
    protected void init4Verify() {
	mapEntity = new HashMap<>();
	List<ZtfiAfrcInvo> entityList = ztfiAfrcInvoFacade.find(company.getCode(), yearMonth);
	for (ZtfiAfrcInvo entity : entityList) {
	    String key = getKey(entity);;
	    mapEntity.put(key, entity);
	}
	
	Set<String> keySet = new HashSet<>();
	for (ZtfiAfrcInvoVO data : datalist) {
	    // 資料重覆
	    String checkKey = getCheckKey(data);
	    if (keySet.contains(checkKey)) {
		writeErrorMsg(data, "該筆資料重覆，請檢查！");
	    }
	    keySet.add(checkKey);
	    
	    // 關係人公司代碼與公司代碼相同
	    if (company.getCode().equals(data.getZafbuk())) {
		writeErrorMsg(data, "關係人公司代碼與公司代碼相同！");
	    }

	    // 關係人代碼
	    if (!companySet.contains(data.getZafbuk())) {
		writeErrorMsg(data, "關係人公司代碼不存在！");
	    }
	    
	    // 幣別碼
	    if (!currencySet.contains(data.getWaers())) {
		writeErrorMsg(data, "幣別碼不存在！");
	    }
	}
    }

    private void writeErrorMsg(ZtfiAfrcInvoVO data, String errorMsg) {
	data.setValid(false);
	String msg = data.getMessage();
	if (msg == null) {
	    msg = "";
	}
	StringBuilder sb = new StringBuilder(msg);
	sb.append(errorMsg).append('\n');
	data.setMessage(sb.toString());
    }

    @Override
    protected boolean postInit(ZtfiAfrcInvoVO vo) {
//待補-設預設值
//        vo.setYearMonth(yearMonth);
//        vo.setCompany(company);
//	vo.setBukrs(company.getCode());
	if (StringUtils.isNotBlank(yearMonth)) {
	    String[] string_array = DateUtils.getYearAndMonth(yearMonth);
	    String year = string_array[0];
	    String month = string_array[1];
	    vo.setZgjahr(Short.valueOf(year));
	    vo.setZmonat(Short.valueOf(month));
	}
//待補
	//setEntity
	String key = getKey(vo);
	ZtfiAfrcInvo entity = mapEntity.get(key);
	if (entity != null) {
	    vo.setEntity(entity);
	}
	//
	vo.updateStatus();
	return valid;
    }
    
    private String getCheckKey(ZtfiAfrcInvoVO vo) {
	StringBuilder key = new StringBuilder();
	key.append(":").append(company.getCode());
	key.append(":").append(vo.getZafbuk());	// 關係人公司代碼
	key.append(":").append(vo.getZafcat());	// 對帳分類
	key.append(":").append(vo.getZaftyp());	// 對帳交易別
	key.append(":").append(vo.getGjahr());		// 會計年度
	key.append(":").append(vo.getBelnr());		// 會計文件號碼
	key.append(":").append(vo.getBudat());		// 文件過帳日期
	key.append(":").append(vo.getWaers());		// 幣別碼
	key.append(":").append(vo.getGuiRegis());	// 統一編號
	key.append(":").append(vo.getGuino());		// 發票號碼
	
	return key.toString();
    }

    private String getKey(ZtfiAfrcInvoVO vo) {
	StringBuilder key = new StringBuilder();
//待補
	key.append(vo.getZgjahr());
	key.append(":").append(vo.getZmonat());
	key.append(":").append(company.getCode());
	key.append(":").append(vo.getZafbuk());
	key.append(":").append(vo.getZafcat());
	key.append(":").append(vo.getZaftyp());
	key.append(":").append(vo.getGjahr());
	key.append(":").append(vo.getBelnr());
	key.append(":").append(vo.getGuino());
//待補
	return key.toString();
    }

    private String getKey(ZtfiAfrcInvo entity) {
	StringBuilder key = new StringBuilder();
//待補
	key.append(entity.getZgjahr());
	key.append(":").append(entity.getZmonat());
	key.append(":").append(entity.getBukrs());
	key.append(":").append(entity.getZafbuk());
	key.append(":").append(entity.getZafcat());
	key.append(":").append(entity.getZaftyp());
	key.append(":").append(entity.getGjahr());
	key.append(":").append(entity.getBelnr());
	key.append(":").append(entity.getGuino());
//待補

	return key.toString();
    }

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="save()">
    @Override
    protected boolean insert(ZtfiAfrcInvoVO vo) {
	ZtfiAfrcInvo entity = new ZtfiAfrcInvo();
//待補-set PK
	entity.setZgjahr(vo.getZgjahr());
	entity.setZmonat(vo.getZmonat());
	entity.setBukrs(company.getCode());
	entity.setZafbuk(vo.getZafbuk());
	entity.setZafcat(vo.getZafcat());
	entity.setZaftyp(vo.getZaftyp());
	entity.setGjahr(Short.parseShort(vo.getGjahr()));
	entity.setBelnr(vo.getBelnr());
	entity.setGuino(vo.getGuino());
//待補

	vo.setEntity(entity);
	return saveVO(vo);
    }

    @Override
    protected boolean update(ZtfiAfrcInvoVO vo) {
	return saveVO(vo);
    }

    private boolean saveVO(ZtfiAfrcInvoVO vo) {
	ZtfiAfrcInvo entity = vo.getEntity();

	// 空白表示保留原值
//待補-set not PK column
	if (vo.getKunnr() != null) {
	    entity.setKunnr(vo.getKunnr());
	}
	if (vo.getLifnr() != null) {
	    entity.setLifnr(vo.getLifnr());
	}
	if (vo.getGuiRegis() != null) {
	    entity.setGuiRegis(vo.getGuiRegis());
	}
	String budat = vo.getBudat();
	if (budat != null) {
	    String format = "yyyyMMdd";
	    Date budat_d = convertStringToDate(format, budat);
	    entity.setBudat(budat_d);
	}
	if (vo.getWaers() != null) {
	    entity.setWaers(vo.getWaers());
	}
	if (vo.getBaseamt() != null) {
	    entity.setBaseamt(vo.getBaseamt());
	}
	if (vo.getTaxamt() != null) {
	    entity.setTaxamt(vo.getTaxamt());
	}
	if (vo.getTotalamt() != null) {
	    entity.setTotalamt(vo.getTotalamt());
	}
	if (vo.getZafbukNm() != null) {
	    entity.setZafbukNm(vo.getZafbukNm());
	}
//	if (vo.getZckflg() != null) {
//	    entity.setZckflg(vo.getZckflg());
//	}

//待補
	entity.setSyncTimeStamp(new Date());
//        entity.setCreatetimestamp(new Date());
//        entity.setCreator(userSession.getTcUser());
	try {
	    ztfiAfrcInvoFacade.save(entity);
	    return true;
	} catch (Exception ex) {
	    vo.setMessage(ex.getMessage());
	    return false;
	}
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private method">
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getYearMonth() {
	return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
	this.yearMonth = yearMonth;
    }

    public FcCompany getCompany() {
	return company;
    }

    public void setCompany(FcCompany company) {
	this.company = company;
    }

    public Set<String> getCompanySet() {
	return companySet;
    }

    public void setCompanySet(Set<String> companySet) {
	this.companySet = companySet;
    }

    public Set<String> getCurrencySet() {
	return currencySet;
    }

    public void setCurrencySet(Set<String> currencySet) {
	this.currencySet = currencySet;
    }
    //</editor-fold>    
}

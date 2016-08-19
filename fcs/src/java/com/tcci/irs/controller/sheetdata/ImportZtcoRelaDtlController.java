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
import com.tcci.irs.entity.sheetdata.ZtfiAfrcTran;
import com.tcci.irs.facade.sheetdata.ZtfiAfrcTranFacade;
import java.util.Date;
import java.util.HashMap;
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
@ManagedBean(name = "importZtcoRelaDtlController")
@ViewScoped
public class ImportZtcoRelaDtlController extends ImportExcelBase2<ZtfiAfrcTranVO> {

    //<editor-fold defaultstate="collapsed" desc="Injects">
    @Inject
    private ZtfiAfrcTranFacade ztfiAfrcTranFacade;
    //</editor-fold>    

    private String yearMonth;
    private FcCompany company;

    private Map<String, ZtfiAfrcTran> mapEntity;

    // 檔案上傳驗證－公司
    private Set<String> companySet;
    // 檔案上傳驗證－幣別
    private Set<String> currencySet;
    // 檔案上傳驗證－會科
    private Set<String> accountsSet;

    @PostConstruct
    protected void init() {

    }

    //String sheetName = "關企對帳交易記錄";
    public ImportZtcoRelaDtlController() {
	super(ZtfiAfrcTranVO.class);
	headerRow = 1; // row 3
	sheetName = "關系人對帳交易記錄";
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
	}
    }

    @Override
    protected void init4Verify() {
	mapEntity = new HashMap<>();
	List< ZtfiAfrcTran> entityList = ztfiAfrcTranFacade.find(company.getCode(), yearMonth);
	for (ZtfiAfrcTran entity : entityList) {
	    String key = getKey(entity);
	    mapEntity.put(key, entity);
	}
	
	Map<String, ZtfiAfrcTranVO> keyMap = new HashMap<>();
	for (ZtfiAfrcTranVO data : datalist) {
	    // 資料重覆
	    String checkKey = getCheckKey(data);
	    if (keyMap.containsKey(checkKey)) {
		writeErrorMsg(data, "該筆資料重覆，請檢查(或修改明細碼)！");
	    }
	    keyMap.put(checkKey, data);
	    
	    // 關係人公司代碼與公司代碼相同
	    if (company.getCode().equals(data.getZafbuk())) {
		writeErrorMsg(data, "關係人公司代碼與公司代碼相同！");
	    }

	    // 關係人公司代碼
	    if (!companySet.contains(data.getZafbuk())) {
		writeErrorMsg(data, "關係人公司代碼不存在！");
	    }
	    
	    // 幣別碼
	    if (!currencySet.contains(data.getWaers())) {
		writeErrorMsg(data, "幣別碼不存在！");
	    }
            
            // 會科代碼
	    if (!accountsSet.contains(data.getAccountCode())) {
                writeErrorMsg(data, "會科代碼有誤！");
	    }
	}
    }

    private void writeErrorMsg(ZtfiAfrcTranVO data, String errorMsg) {
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
    protected boolean postInit(ZtfiAfrcTranVO vo) {
	//設預設值
	if (StringUtils.isNotBlank(yearMonth)) {
	    String[] string_array = DateUtils.getYearAndMonth(yearMonth);
	    String year = string_array[0];
	    String month = string_array[1];
	    vo.setZgjahr(Short.valueOf(year));
	    vo.setZmonat(Short.valueOf(month));
	}
	
	//setEntity
	String key = getKey(vo);
	ZtfiAfrcTran entity = mapEntity.get(key);
	if (entity != null) {
	    vo.setEntity(entity);
	}
	
	//
	vo.updateStatus();
	return valid;
    }
    
    private String getCheckKey(ZtfiAfrcTranVO vo) {
	StringBuilder key = new StringBuilder();
	key.append(":").append(company.getCode());
	key.append(":").append(vo.getZafbuk());	// 關係人公司代碼
	key.append(":").append(vo.getZafcat());	// 對帳分類
	key.append(":").append(vo.getZaftyp());	// 對帳交易別
	key.append(":").append(vo.getGjahr());		// 會計年度
	key.append(":").append(vo.getBelnr());		// 會計文件號碼
	key.append(":").append(vo.getBudat());		// 文件過帳日期
	key.append(":").append(vo.getWaers());		// 幣別碼
	key.append(":").append(vo.getAccountCode());	// 會計科目
	key.append(":").append(vo.getBuzei());		// 明細碼
	
	return key.toString();
    }

    private String getKey(ZtfiAfrcTranVO vo) {
	StringBuilder key = new StringBuilder();
	key.append(vo.getZgjahr());
	key.append(":").append(vo.getZmonat());
	key.append(":").append(company.getCode());
	key.append(":").append(vo.getZafbuk());
	key.append(":").append(vo.getZafcat());
	key.append(":").append(vo.getZaftyp());
	key.append(":").append(vo.getGjahr());
	key.append(":").append(vo.getBelnr());
	key.append(":").append(vo.getBuzei());

	return key.toString();
    }

    private String getKey(ZtfiAfrcTran entity) {
	StringBuilder key = new StringBuilder();
	key.append(entity.getZgjahr());
	key.append(":").append(entity.getZmonat());
	key.append(":").append(company.getCode());
	key.append(":").append(entity.getZafbuk());
	key.append(":").append(entity.getZafcat());
	key.append(":").append(entity.getZaftyp());
	key.append(":").append(entity.getGjahr());
	key.append(":").append(entity.getBelnr());
	key.append(":").append(entity.getBuzei());

	return key.toString();
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="save()">
    @Override
    protected boolean insert(ZtfiAfrcTranVO vo) {
	ZtfiAfrcTran entity = new ZtfiAfrcTran();
	entity.setZgjahr(vo.getZgjahr());
	entity.setZmonat(vo.getZmonat());
	entity.setBukrs(company.getCode());
	entity.setZafbuk(vo.getZafbuk());
	entity.setZafcat(vo.getZafcat());
	entity.setZaftyp(vo.getZaftyp());
	entity.setGjahr(Short.parseShort(vo.getGjahr()));
	entity.setBelnr(vo.getBelnr());
	entity.setBuzei(vo.getBuzei());

	vo.setEntity(entity);
	return saveVO(vo);
    }

    @Override
    protected boolean update(ZtfiAfrcTranVO vo) {
	return saveVO(vo);
    }

    private boolean saveVO(ZtfiAfrcTranVO vo) {
	ZtfiAfrcTran entity = vo.getEntity();

	// 空白表示保留原值
	//待補-set not PK column
	if (vo.getKunnr() != null) {
	    entity.setKunnr(vo.getKunnr());
	}
	if (vo.getLifnr() != null) {
	    entity.setLifnr(vo.getLifnr());
	}
	if (vo.getHkont() != null) {
	    entity.setHkont(vo.getHkont());
	}
	String budat = vo.getBudat();
	if (budat != null) {
	    String format = "yyyyMMdd";
	    Date budat_d = convertStringToDate(format, budat);
	    entity.setBudat(budat_d);
	}
	if (vo.getBlart() != null) {
	    entity.setBlart(vo.getBlart());
	}
	if (vo.getWaers() != null) {
	    entity.setWaers(vo.getWaers());
	}
	if (vo.getWrbtr() != null) {
	    entity.setWrbtr(vo.getWrbtr());
	}
	if (vo.getDmbtr() != null) {
	    entity.setDmbtr(vo.getDmbtr());
	}
	if (vo.getZuonr() != null) {
	    entity.setZuonr(vo.getZuonr());
	}
	if (vo.getSgtxt() != null) {
	    entity.setSgtxt(vo.getSgtxt());
	}
//	if (vo.getZaftlSaco() != null) {
//	    entity.setZaftlSaco(vo.getZaftlSaco());
//	}
	if (vo.getZafbukNm() != null) {
	    entity.setZafbukNm(vo.getZafbukNm());
	}
	if (vo.getTxt50() != null) {
	    entity.setTxt50(vo.getTxt50());
	}
//	if (vo.getHkontIfrs() != null) {
//	    entity.setHkontIfrs(vo.getHkontIfrs());
//	}
	entity.setZlupdt(new Date());

	entity.setSyncTimeStamp(new Date());

	if (vo.getAccountCode() != null) {
	    entity.setAccountCode(vo.getAccountCode());
	}
	try {
	    ztfiAfrcTranFacade.save(entity);
	    return true;
	} catch (Exception ex) {
	    vo.setMessage(ex.getMessage());ex.printStackTrace();
	    return false;
	}
    }
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

    public Set<String> getAccountsSet() {
        return accountsSet;
    }

    public void setAccountsSet(Set<String> accountsSet) {
        this.accountsSet = accountsSet;
    }
    //</editor-fold>
}

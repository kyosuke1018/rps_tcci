/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.controller.attachment;

import com.tcci.fc.controller.BaseController;
import com.tcci.fc.controller.attachment.AttachmentController;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.facade.BaseLazyDataModel;
import com.tcci.fc.facade.FilterLazyDataModel;
import com.tcci.fc.util.ListUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.fc.util.time.DateUtils;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.entity.attachment.IrsReportUpload;
import com.tcci.irs.enums.PagecodeRoleEnum;
import com.tcci.irs.facade.attachment.IrsReportUploadFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author gilbert
 */
@ManagedBean(name = "queryAttachmentController")
@ViewScoped
public class QueryAttachmentController extends BaseController {
    //<editor-fold defaultstate="collapsed" desc="Injects">
   
    @EJB
    protected IrsReportUploadFacade tcReportUploadFacade; 
    //
    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;
    public void setAttachmentController(AttachmentController attachmentController) {
        this.attachmentController = attachmentController;
    }
    public AttachmentController getAttachmentController() {
        return attachmentController;
    }
    //</editor-fold>    


    

    private IrsReportUpload condVO = null;
    private BaseLazyDataModel<IrsReportUpload> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<IrsReportUpload> filterResultList; // datatable filter 後的結果
    private FcCompany company;
    public FcCompany getCompany() {
        return company;
    }
    public void setCompany(FcCompany company) {
        this.company = company;
    }
    //helper
    private List<SelectItem> companyOptions;//工廠
    
    @PostConstruct
    @Override
    protected void init() {    
        logger.debug("PostConstruct");

        //
        condVO = new IrsReportUpload();
        if (!checkParameter2()){
            return;
        }
        String pageCode_src = condVO.getPageCode();
        //setPageTitle
        String controllerClass = getClass().getSimpleName();
        String controllerInstance = controllerClass.substring(0,1).toLowerCase()+controllerClass.substring(1);
        setPageTitle(pageCode_src+rb.getString(controllerInstance+".pageTitle"));
        //factoryOptions
        List<FcCompany> companyList = new ArrayList<>();
        companyOptions = ListUtils.getOptions(companyList);
        //factory
        setCompany(ListUtils.getFirstElement(companyList));
        if(null != company){
            condVO.setCompanyCode(company);
        }
        //year
        String year = DateUtils.getYear(Calendar.getInstance());
        condVO.setYearMonth(year);
        
        loadData();

        logger.debug("factorycode="+condVO.getCompanyCode()+"|pageCode="+condVO.getPageCode());
    }
    
    protected boolean checkParameter2() {
        logger.debug("checkParameter");
        //setPageCode
        String pageCode_src = JsfUtil.getRequestParameter("pageCode");
        condVO.setPageCode(pageCode_src);
        
        return valid;
    }

    @Override
    protected boolean check4Query() throws Exception {
        //檢核:年月
        String yearMonth = condVO.getYearMonth();
        String columnName = "年" ;
        String format_yearMonth = "yyyy";
        String errMsg = DateUtils.checkDate(format_yearMonth, yearMonth, columnName);
        if(StringUtils.isNotBlank(errMsg)){
            JsfUtil.addErrorMessage(errMsg);
            return !valid;
        }
        //檢核:pageCode
        String pageCode_src = condVO.getPageCode();
        columnName = "pageCode";
        errMsg = StringUtils.checkNotBlank(pageCode_src, columnName);
        if(StringUtils.isNotBlank(errMsg)){
            JsfUtil.addErrorMessage(errMsg);
            return !valid;
        }
        
        return super.check4Query(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    protected boolean loadData() {
        //init
        lazyModel = null;
        
        logger.debug("companyCode="+condVO.getCompanyCode()+"|pageCode="+condVO.getPageCode());
        PagecodeRoleEnum pagecodeRoleEnum = PagecodeRoleEnum.getEnum(condVO.getPageCode());
        if(null == pagecodeRoleEnum){
            return super.loadData();
        }
        String pageCode_src = pagecodeRoleEnum.getPageCode();
        List<String> pageCodeList = new ArrayList<>();
        pageCodeList.add(pageCode_src);
        List<IrsReportUpload> entityList = tcReportUploadFacade.find(condVO.getYearMonth()
                , condVO.getCompanyCode().getCode()
                , pageCodeList
        );

        for (IrsReportUpload reportUpload : entityList) {
            boolean stopUpload = false;
            attachmentController.init(reportUpload, stopUpload);
            List<AttachmentVO> list_temp =  attachmentController.getAttachmentVOList();
            AttachmentVO vo = ListUtils.getFirstElement(list_temp);
            reportUpload.setAttachmentVO(vo);
        }

        if(CollectionUtils.isNotEmpty(entityList)){
            lazyModel = new FilterLazyDataModel<>(entityList);
        }else{
            JsfUtil.addSuccessMessage("查無資料!");

        }
        
        
        
        return super.loadData();
    }  
    

   
    
    //<editor-fold defaultstate="collapsed" desc="private method">
    
    //</editor-fold>       
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public BaseLazyDataModel<IrsReportUpload> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<IrsReportUpload> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<IrsReportUpload> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<IrsReportUpload> filterResultList) {
        this.filterResultList = filterResultList;
    }
    public IrsReportUpload getCondVO() {
        return condVO;
    }

    public void setCondVO(IrsReportUpload condVO) {
        this.condVO = condVO;
    }
    
    public List<SelectItem> getCompanyOptions() {
        return companyOptions;
    }

    public void setCompanyOptions(List<SelectItem> companyOptions) {
        this.companyOptions = companyOptions;
    }
    //</editor-fold>    

}

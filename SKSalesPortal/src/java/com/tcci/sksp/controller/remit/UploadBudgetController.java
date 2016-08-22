/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.controller.util.parser.ExcelParser;
import com.tcci.sksp.entity.ar.SkBudget;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkBudgetFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import com.tcci.sksp.vo.UploadedBudgetVO;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class UploadBudgetController {
    private UploadedFile uploadedFile;
    private List<UploadedBudgetVO> uploadedBudgetList;
    private boolean hasError;
    
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;
    
    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }    
    
    @EJB
    private SkBudgetFacade budgetFacade;   
    @EJB
    private SkSalesMemberFacade salesMemberFacade;
    
    @PostConstruct
    public void init() {
        initParams();
    }
    
    private void initParams() {
        uploadedBudgetList = new ArrayList<UploadedBudgetVO>();
        hasError = false;        
    }
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<UploadedBudgetVO> getUploadedBudgetList() {
        return uploadedBudgetList;
    }

    public void setUploadedBudgetList(List<UploadedBudgetVO> uploadedBudgetList) {
        this.uploadedBudgetList = uploadedBudgetList;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }        
    
    public void handleFileUpload(FileUploadEvent event) {     
        uploadedFile = event.getFile();
        uploadBudgetData(uploadedFile);
    }    
    
    public void doUploadBudgetData() {
        uploadBudgetData(this.uploadedFile);       
    }
    
    public void uploadBudgetData(UploadedFile uploadedFile) {
        Logger.getLogger(UploadBudgetController.class.getName()).log(Level.INFO, "uploadBudgetData() start!");
        try {
            initParams();            
            InputStream is = uploadedFile.getInputstream();
            ExcelParser parser = new ExcelParser();
            parser.setInputStream(is);
            List<List<Object>> data = parser.parse();  
            processBudgetData(data);
            if (!hasError) {
                String msg = FacesUtil.getMessage("uploadfile") + "\"" + uploadedFile.getFileName() + "\"" + FacesUtil.getMessage("complete");
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, msg);
                Logger.getLogger(UploadBudgetController.class.getName()).log(Level.INFO, msg);            
            }
        } catch (Exception ex) {            
            String msg = FacesUtil.getMessage("upload.fail") + "  "+ ex.getMessage();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, msg);            
            Logger.getLogger(UploadBudgetController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();            
        }                
    }    
    
    public void confirmUpload() {
        List<SkBudget> budgetList = new ArrayList<SkBudget>();
        for (UploadedBudgetVO vo : uploadedBudgetList) {
            SkBudget budget = null;                  
            
            Calendar c = Calendar.getInstance();
            c.set(Integer.valueOf(vo.getYear()).intValue(), Integer.valueOf(vo.getMonth()).intValue()-1, 1, 0, 0, 0);               
            
            String sapId = vo.getSapId();
            Date baseline = c.getTime();
            budget = budgetFacade.findByCriteria(sapId, baseline);
            if (budget == null) {
                budget = new SkBudget();
                budget.setSapid(vo.getSapId()); 
                budget.setBaselineTimestamp(baseline);
            }
            budget.setBudget(new BigDecimal(Double.parseDouble(vo.getBudget())));
            budgetList.add(budget);
        }
        budgetFacade.insertOrUpdate(budgetList, userSession.getUser());
        FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, FacesUtil.getMessage("update.budget.save.successfully"));
    }    
    
    private void processBudgetData(List<List<Object>> data)throws Exception {        
        HashMap<String,String> uniqueKeys = new HashMap<String,String>();
        for (int i = 0; i < data.size(); i++) {
            List<Object> list = (List<Object>)data.get(i);
            if (list.size() > 0) {
                String key = (String) list.get(0);
                if (("BUDGET").equalsIgnoreCase(key)) {
                    UploadedBudgetVO vo = new UploadedBudgetVO();
                    List<String> msg = new ArrayList<String>();
                    if (list.size() >= 4) {
                        // sapId
                        String sapId = (String)list.get(1);
                        vo.setSapId(sapId);
                        if (StringUtils.isEmpty(sapId)) {
                            msg.add(FacesUtil.getMessage("upload.budget.error.emptysales"));                           
                        } else {                         
                            SkSalesMember sales = salesMemberFacade.findByCode(sapId);
                            if (sales == null) {
                                msg.add(FacesUtil.getMessage("upload.budget.error.invalidsales"));                               
                            } else {
                                String name = "";
                                if( sales.getMember() != null)
                                    name = sales.getMember().getCname();
                                vo.setSalesName( name );
                            }
                        }
                        
                        // year
                        String yearStr = (String)list.get(2);    
                        vo.setYear(yearStr);                        
                        if (StringUtils.isEmpty(yearStr)) {
                            msg.add(FacesUtil.getMessage("upload.budget.error.emptyyear"));                                  
                        } else {
                            try {
                                int year = Integer.parseInt(yearStr);
                                if (year < 2012) {
                                    msg.add(FacesUtil.getMessage("upload.budget.error.invalidyear"));                                        
                                }
                            } catch (Exception e) {
                                msg.add(FacesUtil.getMessage("upload.budget.error.invalidyear"));                                                          
                            }
                        }
                        
                        // month
                        String monthStr = (String)list.get(3);    
                        vo.setMonth(monthStr);
                        if (StringUtils.isEmpty(monthStr)) {
                            msg.add(FacesUtil.getMessage("upload.budget.error.emptymonth"));                                                        
                        } else {
                            try {
                                int month = Integer.parseInt(monthStr);
                                if (month < 1 || month > 12) {
                                    msg.add(FacesUtil.getMessage("upload.budget.error.invalidmonth"));                                                                   
                                }
                            } catch (Exception e) {
                                msg.add(FacesUtil.getMessage("upload.budget.error.invalidmonth"));                                                    
                            }
                        }                        
                        if(uniqueKeys.containsKey(sapId)) {
                            String value = uniqueKeys.get(sapId);
                            if(value.equals(vo.getYear()+vo.getMonth())) {
                                msg.add(FacesUtil.getMessage("upload.budget.error.duplciatedata"));
                            }
                        }else {
                            uniqueKeys.put(sapId,vo.getYear()+vo.getMonth());
                        }
                        
                        // budget
                        String budgetStr = (String)list.get(4);
                        vo.setBudget(budgetStr);
                        if (StringUtils.isEmpty(budgetStr)) {
                            msg.add(FacesUtil.getMessage("upload.budget.error.emptybudget"));                                                      
                        } else {
                            try {
                                double budget = Double.parseDouble(budgetStr);
                                if (budget < 0) {
                                    msg.add(FacesUtil.getMessage("upload.budget.error.invalidbudget"));                                                               
                                }                                
                            } catch (Exception e) {
                                msg.add(FacesUtil.getMessage("upload.budget.error.invalidmonth"));                                              
                            }
                        }                        
                    } else {          
                        msg.add(FacesUtil.getMessage("upload.budget.error.emptycolumn"));                                    
                    }                   
                    hasError = !hasError ? (msg.size() > 0 ? true : false) : hasError;                    
                    vo.setErrorMsg(msg);
                    uploadedBudgetList.add(vo);                    
                }
            }
        }
        if (hasError) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("upload.budget.error.invalidfile"));           
        }
    }    
   
}

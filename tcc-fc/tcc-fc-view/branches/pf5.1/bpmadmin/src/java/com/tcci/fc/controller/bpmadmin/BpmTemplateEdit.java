/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.bpm.TcActivityroutetemplate;
import com.tcci.fc.entity.bpm.TcActivitytemplate;
import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.facade.bpmtemplate.ActivityVO;
import com.tcci.fc.facade.bpmtemplate.RouteVO;
import com.tcci.fc.facade.bpmtemplate.TcProcessTemplateFacade;
import com.tcci.fc.util.ExcelUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "bpmTemplateEdit")
@ViewScoped
public class BpmTemplateEdit {
    
    private String id;
    private TcProcesstemplate processTemplate; // 編輯中的 processTemplate
    private List<ActivityVO> activityList;
    private List<RouteVO> routeList;
    private String activityMsgs;
    private String routeMsgs;
    
    @EJB
    private TcProcessTemplateFacade templateFacade;
    
    private Map<String, ActivityVO> mapActivity;
    
    @PostConstruct
    private void init() {
        mapActivity = new HashMap<String, ActivityVO>();
        id = JsfUtil.getRequestParameter("id");
        if (null == id) {
            processTemplate = new TcProcesstemplate();
        } else {
            loadTemplate();
        }
    }
    
    // excel 檔案上傳
    public void handleFileUpload(FileUploadEvent event) {
        mapActivity.clear();
        UploadedFile tfile = event.getFile();
        try {
            Workbook workbook = WorkbookFactory.create(tfile.getInputstream());
            activityList = ExcelUtil.importList(workbook.getSheet("ACTIVITY"), ActivityVO.class, 1, 1000);
            routeList = ExcelUtil.importList(workbook.getSheet("ROUTE"), RouteVO.class, 1, 1000);
            boolean activityCorrect = verifyActivity();
            boolean routeCorrect = verifyRoute();
            if (activityCorrect && routeCorrect) {
                JsfUtil.addSuccessMessage("資料已讀取!");
            } else {
                JsfUtil.addErrorMessage("資料有誤!");
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "handleFileUpload exception!");
        }
    }
    
    public void preview() {
        RequestContext context = RequestContext.getCurrentInstance();
        String result = digraphStrig();
        context.addCallbackParam("result", result);
    }
    
    public void save() {
        try {
            templateFacade.save(processTemplate, activityList, routeList);
            JsfUtil.addSuccessMessage("資料已儲存!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "save exception!");
        }
    }

    // helper
    private boolean loadTemplate() {
        processTemplate = templateFacade.findById(Long.valueOf(id));
        if (null == processTemplate) {
            JsfUtil.addErrorMessage("資料不存在!");
            return false;
        }
        activityList = new ArrayList<ActivityVO>();
        routeList = new ArrayList<RouteVO>();
        for (TcActivitytemplate act : processTemplate.getTcActivitytemplateCollection()) {
            ActivityVO avo = new ActivityVO(act);
            activityList.add(avo);
            for (TcActivityroutetemplate route : act.getTcActivityroutetemplateCollection()) {
                RouteVO rvo = new RouteVO(route);
                routeList.add(rvo);
            }
        }
        return true;
    }
    
    private boolean verifyActivity() {
        boolean success = true;
        boolean typeStartFound = false;
        boolean typEndFound = false;
        for (ActivityVO vo : activityList) {
            String key = vo.getActivityname();
            if (mapActivity.containsKey(key)) {
                vo.setValid(false);
                vo.setMessage("ACTIVITY名稱重複!");
            } else {
                mapActivity.put(key, vo);
            }
            try {
                ActivityTypeEnum.fromValue(vo.getActivitytype());
            } catch (Exception ex) {
                vo.setValid(false);
                vo.setMessage("ACTIVITY類型不正確!");
            }
            if (ActivityTypeEnum.START.name().equals(vo.getActivitytype())) {
                if (typeStartFound) {
                    vo.setValid(false);
                    vo.setMessage("START重複!");
                } else {
                    typeStartFound = true;
                }
            }
            if (ActivityTypeEnum.END.name().equals(vo.getActivitytype())) {
                if (typEndFound) {
                    vo.setValid(false);
                    vo.setMessage("END重複!");
                } else {
                    typEndFound = true;
                }
            }
            if (!ActivityTypeEnum.TASK.name().equals(vo.getActivitytype()) && !StringUtils.isBlank(vo.getRolename())) {
                vo.setValid(false);
                vo.setMessage("非TASK類型不能有角色名稱!");
            }
            if (!vo.isValid()) {
                success = false;
            }
        }
        if (!typeStartFound) {
            success = false;
            activityMsgs += "必需有START\n";
        }
        if (!typEndFound) {
            success = false;
            activityMsgs += "必需有END\n";
        }
        return success;
    }
    
    private boolean verifyRoute() {
        boolean success = true;
        for (RouteVO vo : routeList) {
            String key = vo.getFromactivity();
            if (!mapActivity.containsKey(key)) {
                vo.setValid(false);
                vo.setMessage(key + "不存在!");
            }
            key = vo.getToactivity();
            if (!mapActivity.containsKey(key)) {
                vo.setValid(false);
                vo.setMessage(key + "不存在!");
            }
            if (!vo.isValid()) {
                success = false;
            }
        }
        return success;
    }
    
    private String digraphStrig() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {").append("\n");
        sb.append("node [fontsize=10];").append("\n");
        for (ActivityVO vo : activityList) {
            nodeString(vo, sb);
        }
        for (RouteVO vo : routeList) {
            routeString(vo, sb);
        }
        sb.append("}");
        return sb.toString();
    }
    
    private void nodeString(ActivityVO vo, StringBuilder sb) {
        String shape;
        if (ActivityTypeEnum.START.name().equals(vo.getActivitytype()) || 
            ActivityTypeEnum.END.name().equals(vo.getActivitytype())) {
            shape = "circle";
        } else if (ActivityTypeEnum.TASK.name().equals(vo.getActivitytype())) {
            shape = "box";
        } else if (ActivityTypeEnum.CONDITION.name().equals(vo.getActivitytype())) {
            shape = "diamond";
        } else if (ActivityTypeEnum.EXPRESSION_ROBOT.name().equals(vo.getActivitytype())) {
            shape = "parallelogram";
        } else {
            shape = "star";
        }
        sb.append("\"").append(vo.getActivityname()).append("\" [shape=")
                .append(shape).append("];");
    }
    
    private void routeString(RouteVO vo, StringBuilder sb) {
        sb.append("\"").append(vo.getFromactivity()).append("\" -> \"")
                .append(vo.getToactivity()).append("\" [label=\"")
                .append(vo.getRoutename()).append("\"];");
    }
    
    // getter, setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TcProcesstemplate getProcessTemplate() {
        return processTemplate;
    }

    public void setProcessTemplate(TcProcesstemplate processTemplate) {
        this.processTemplate = processTemplate;
    }

    public List<ActivityVO> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActivityVO> activityList) {
        this.activityList = activityList;
    }

    public List<RouteVO> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<RouteVO> routeList) {
        this.routeList = routeList;
    }

    public String getActivityMsgs() {
        return activityMsgs;
    }

    public void setActivityMsgs(String activityMsgs) {
        this.activityMsgs = activityMsgs;
    }

    public String getRouteMsgs() {
        return routeMsgs;
    }

    public void setRouteMsgs(String routeMsgs) {
        this.routeMsgs = routeMsgs;
    }
    
}

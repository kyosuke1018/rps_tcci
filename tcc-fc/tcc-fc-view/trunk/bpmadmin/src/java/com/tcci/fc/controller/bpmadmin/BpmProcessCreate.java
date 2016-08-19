/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.bpm.TcActivitytemplate;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.bpmtemplate.TcProcessTemplateFacade;
import com.tcci.fc.facade.essential.EssentialFacade;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "bpmProcessCreate")
@ViewScoped
public class BpmProcessCreate implements RoleUsersDialogListener {
    
    private String templateId;
    private TcProcesstemplate template;
    private TcActivitytemplate editRow;
    private String formclassname = "com.tcci.<app>.entity.<classname>";
    private String fomrId;
    private String subject;
    
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    @ManagedProperty(value = "#{roleUsersDialog}")
    private RoleUsersDialog roleUserDialog;

    @Inject
    private IBPMEngine bpmEngine;
    @EJB
    private TcProcessTemplateFacade templateFacade;
    @EJB
    private EssentialFacade essentialFacade;
    
    private Map<String, Object> mapRoleUsers = new HashMap<String, Object>();
    
    @PostConstruct
    private void init() {
        templateId = JsfUtil.getRequestParameter("templateId");
        if (null == templateId) {
            JsfUtil.addErrorMessage("參數templateId是空的!");
        } else {
            try {
                template = templateFacade.findById(Long.valueOf(templateId));
            } catch (Exception ex) {
            }
            if (null == template) {
                JsfUtil.addErrorMessage("無法找到template!");
            }
        }
    }
    
    // action
    public void editRoleUsers(TcActivitytemplate row) {
        if (null==row) {
            return;
        }
        editRow = row;
        roleUserDialog.init(this, editRow.getRolename(), mapRoleUsers.get(editRow.getRolename()));
    }
    
    public void removeRoleUsers(TcActivitytemplate row) {
        if (null==row) {
            return;
        }
        mapRoleUsers.remove(row.getRolename());
    }
    
    @Override
    public void roleUsersDialog_OK(Object result) {
        mapRoleUsers.put(editRow.getRolename(), result);
    }
    
    public void submit() {
        try {
            Persistable primaryObj = essentialFacade.getObject(formclassname, Long.valueOf(fomrId));
            TcProcess process = bpmEngine.createProcess(userSession.getTcUser(), template.getProcessname(), mapRoleUsers, subject, primaryObj);
            JsfUtil.addSuccessMessage("流程建立成功, process id=" + process.getId());
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "create process exception!");
        }
    }
    
    // helper
    public String getPageTitle() {
        String pageTitle = "建立流程";
        if (template != null) {
            pageTitle += "(" + template.getProcessname() + ")";
        }
        return pageTitle;
    }
    
    public String showRoleUsers(TcActivitytemplate row) {
        Object obj = mapRoleUsers.get(row.getRolename());
        if (null==obj) {
            return null;
        }
        if (obj instanceof TcUser) {
            TcUser user = (TcUser) obj;
            return user.getCname();
        } else if (obj instanceof TcGroup) {
            TcGroup tcGroup = (TcGroup) obj;
            return "[" + tcGroup.getName() + "]";
        } else if (obj instanceof Collection<?>) {
            Collection<TcUser> users = (Collection<TcUser>) obj;
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (TcUser user : users) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append(user.getCname());
            }
            return sb.toString();
        }
        return null;
    }
        
    // getter, setter
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public TcProcesstemplate getTemplate() {
        return template;
    }

    public void setTemplate(TcProcesstemplate template) {
        this.template = template;
    }

    public TcActivitytemplate getEditRow() {
        return editRow;
    }

    public void setEditRow(TcActivitytemplate editRow) {
        this.editRow = editRow;
    }

    public String getFormclassname() {
        return formclassname;
    }

    public void setFormclassname(String formclassname) {
        this.formclassname = formclassname;
    }

    public String getFomrId() {
        return fomrId;
    }

    public void setFomrId(String fomrId) {
        this.fomrId = fomrId;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public RoleUsersDialog getRoleUserDialog() {
        return roleUserDialog;
    }

    public void setRoleUserDialog(RoleUsersDialog roleUserDialog) {
        this.roleUserDialog = roleUserDialog;
    }

}

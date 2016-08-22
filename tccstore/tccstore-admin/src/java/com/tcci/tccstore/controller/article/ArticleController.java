/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.article;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.entity.EcArticle;
import com.tcci.tccstore.facade.article.EcArticleFacade;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "articleController")
@ViewScoped
public class ArticleController {

    private List<EcArticle> data;
    private String title;
    private Date startDate;
    private Date endDate;
    private EcArticle selected;

    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    @EJB
    private EcArticleFacade ecArticleFacade;

    @PostConstruct
    private void init() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        startDate = cal.getTime();
        query();
    }

    // action
    public void query() {
        data = ecArticleFacade.query(StringUtils.trimToNull(title), startDate, endDate);
    }

    public void remove(EcArticle row) {
        if (row != null) {
            ecArticleFacade.remove(row);
            query();
            JsfUtil.addSuccessMessage("已刪除!");
        }
    }
    
    public void publish(EcArticle row) {
        if (row != null) {
            row.setActive(true);
            ecArticleFacade.saveAndPublish(row);
            JsfUtil.addSuccessMessage("資料已發佈!");
        }
    }
    
    public void toggleActive(EcArticle row) {
        if (row != null) {
            row.setActive(!row.isActive());
            ecArticleFacade.edit(row);
            JsfUtil.addSuccessMessage("資料已儲存!");
        }
    }
    
    // helper
    public String shortContent(EcArticle row) {
        if (row == null) {
            return null;
        } else if (row.getLink() != null) {
            return row.getLink();
        }
        String content = row.getContent();
        if (content != null) {
            content = content.replaceAll("<.*?>", "");
        }
        return StringUtils.left(content, 50);
    }
    
    // getter, setter
    public List<EcArticle> getData() {
        return data;
    }

    public void setData(List<EcArticle> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public EcArticle getSelected() {
        return selected;
    }

    public void setSelected(EcArticle selected) {
        this.selected = selected;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

}

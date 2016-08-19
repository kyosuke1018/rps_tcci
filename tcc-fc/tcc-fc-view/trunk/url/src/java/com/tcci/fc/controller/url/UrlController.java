/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.url;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcURLData;
import com.tcci.fc.facade.content.ContentFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@ManagedBean(name = "urlController")
@ViewScoped
public class UrlController {

    private Logger logger = LoggerFactory.getLogger(UrlController.class);
    private boolean readonly = false;
    private List<TcURLData> items;
    @EJB
    ContentFacade contentFacade;

    public void init(ContentHolder contentHolder, boolean readonly) {
        this.readonly = readonly;
        this.items =  contentFacade.getURLData(contentHolder);
    }

    public void add() {
        if (this.items == null) {
            this.items = new ArrayList();
        }
        this.items.add(new TcURLData());
        logger.debug("this.items.size()={}", this.items.size());
    }

    public void remove(TcURLData urldata) {
        this.items.remove(urldata);
    }

    public String getUrl(String url) {
        return url.startsWith("http") ? url : "http://".concat(url);
    }

    //getter, setter
    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public List<TcURLData> getItems() {
        return items;
    }

    public void setItems(List<TcURLData> items) {
        this.items = items;
    }
}

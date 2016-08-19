/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.vaultmgmt;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.content.TcFvitemFacade;
import com.tcci.fc.facade.content.TcFvvaultFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Named
@Stateless
public class VaultmgmtController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    Logger logger = LoggerFactory.getLogger(VaultmgmtController.class);
    private List<TcFvvault> items;
    private TcFvvault selected;
    private boolean backupFileFlag = true;
    private TcFvitemDataModel recycleFvitemDataModel;
    private TcFvitem[] selectedRecycledFvitems = new TcFvitem[0];
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @Inject
    TcFvvaultFacade ejbFacade;
    @Inject
    TcDomainFacade domainFacade;
    @Inject
    TcFvitemFacade fvitemFacade;
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    //</editor-fold>
    @PostConstruct
    private void init() {
        this.items = new ArrayList();
        List<TcFvvault> allVault = ejbFacade.findAll();
        TcDomain trashCanDomain = domainFacade.getTrashCanDomain();
        for (TcFvvault vault : allVault) {
            if (vault.getDomain().equals(trashCanDomain)) {
                continue;
            }
            this.items.add(vault);
        }
    }

    public void initRecycleBin() {
        logger.debug("initRecycleBin()");
        List<TcFvitem> fvitemList = fvitemFacade.findAllRecycledTcFvitem();
        this.recycleFvitemDataModel = new TcFvitemDataModel(fvitemList);
    }

    public void removeAllUnreferenceFvitem() {
        for (TcFvvault vault : items) {
            fvitemFacade.destroyTcFvitemNoRef(vault.getDomain(), vault, backupFileFlag);
        }
    }

    public void removeUnreferenceFvitem(TcFvvault vault) {
        fvitemFacade.destroyTcFvitemNoRef(vault.getDomain(), vault, backupFileFlag);
    }

    public boolean isCleanRecycledBinEnabled() {
        return this.recycleFvitemDataModel == null ? false : this.recycleFvitemDataModel.getRowCount() > 0;
    }

    public void removeAllRecycledFvitems() {
        Iterator<TcFvitem> it = this.recycleFvitemDataModel.iterator();
        while (it.hasNext()) {
            TcFvitem recycledFvitem = it.next();
            fvitemFacade.destroyRecycledTcFvitem(recycledFvitem);
        }
    }

    public void removeSelectedRecycledFvitems() {
        for (TcFvitem selectedRecycledFvitem : this.selectedRecycledFvitems) {
            fvitemFacade.destroyRecycledTcFvitem(selectedRecycledFvitem);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public List<TcFvvault> getItems() {
        return items;
    }

    public void setItems(List<TcFvvault> items) {
        this.items = items;
    }

    public boolean isBackupFileFlag() {
        return backupFileFlag;
    }

    public void setBackupFileFlag(boolean backupFileFlag) {
        this.backupFileFlag = backupFileFlag;
    }

    public TcFvitem[] getSelectedRecycledFvitems() {
        return selectedRecycledFvitems;
    }

    public void setSelectedRecycledFvitems(TcFvitem[] selectedRecycledFvitems) {
        this.selectedRecycledFvitems = selectedRecycledFvitems;
    }

    public TcFvitemDataModel getRecycleFvitemDataModel() {
        return recycleFvitemDataModel;
    }

    public TcFvvault getSelected() {
        return selected;
    }

    public void setSelected(TcFvvault selected) {
        this.selected = selected;
    }
    //</editor-fold>
}

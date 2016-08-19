/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.vaultmgmt;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.content.TcFvitemFacade;
import com.tcci.fc.facade.content.TcFvvaultFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.vo.DomainVaultVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@ManagedBean
@ViewScoped
public class VaultmgmtController {

    //<editor-fold defaultstate="collapsed" desc="variables">
    Logger logger = LoggerFactory.getLogger(VaultmgmtController.class);
    private boolean selectAll;
    private List<DomainVaultVO> items;
    private boolean backupFileFlag = true;
    private TcFvitemDataModel recycleFvitemDataModel;
    private TcFvitem[] selectedRecycledFvitems = new TcFvitem[0];
    private ResourceBundle rb = ResourceBundle.getBundle("msgVaultmgmt",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @Inject
    TcFvvaultFacade ejbFacade;
    @Inject
    TcDomainFacade domainFacade;
    @Inject
    TcFvitemFacade fvitemFacade;
    //</editor-fold>

    @PostConstruct
    private void init() {
        this.items = new ArrayList();
        List<TcFvvault> allVault = ejbFacade.findAll();
        TcDomain trashCanDomain = domainFacade.getTrashCanDomain();
        Map<TcDomain, List<TcFvvault>> domainVaultMap = new HashMap();
        for (TcFvvault vault : allVault) {
            if (vault.getDomain().equals(trashCanDomain)) {
                continue;
            }
            TcDomain domain = vault.getDomain();
            List<TcFvvault> vaultList = new ArrayList();
            if (domainVaultMap.containsKey(domain)) {
                vaultList = domainVaultMap.get(domain);
                domainVaultMap.remove(domain);
            }
            vaultList.add(vault);
            domainVaultMap.put(domain, vaultList);
        }
        for (TcDomain domain : domainVaultMap.keySet()) {
            DomainVaultVO vo = new DomainVaultVO();
            vo.setDomain(domain);
            vo.setVaultList(domainVaultMap.get(domain));
            this.items.add(vo);
        }
    }

    public void initRecycleBin() {
        logger.debug("initRecycleBin()");
        List<TcFvitem> fvitemList = fvitemFacade.findAllRecycledTcFvitem();
        this.recycleFvitemDataModel = new TcFvitemDataModel(fvitemList);
    }

    public void changeSelectAll() {
        for (DomainVaultVO vo : this.items) {
            vo.setSelected(this.selectAll);
        }
    }

    public void selectOne() {
        boolean selectAll = true;
        for (DomainVaultVO vo : this.items) {
            if (!vo.isSelected()) {
                selectAll = false;
            }
        }
        if (selectAll) {
            this.selectAll = true;
        } else {
            this.selectAll = false;
        }
    }

    public void removeAllUnreferenceFvitem() {

        for (DomainVaultVO vo : items) {
            removeUnreferenceFvItem(vo);
        }
        JsfUtil.addSuccessMessage(rb.getString("fvvault.message.removeUnreferenceFvitemSuccess"));
    }

    public void removeUnreferenceFvitem() {
        boolean selected = false;
        for (DomainVaultVO vo : this.items) {
            if (vo.isSelected()) {
                selected = true;
                removeUnreferenceFvItem(vo);
            }
        }
        if (selected) {
            JsfUtil.addSuccessMessage(rb.getString("fvvault.message.removeUnreferenceFvitemSuccess"));
        } else {
            JsfUtil.addErrorMessage(rb.getString("fvvault.error.selectDomainFirst"));
        }
    }

    private void removeUnreferenceFvItem(DomainVaultVO vo) {
        fvitemFacade.destroyTcFvitemNoRef(vo.getDomain(), vo.getVaultList(), backupFileFlag);
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
    public List<DomainVaultVO> getItems() {
        return items;
    }

    public void setItems(List<DomainVaultVO> items) {
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

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
    //</editor-fold>
}

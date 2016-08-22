package com.tcci.sksp.vo;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
public class RemitMasterVO implements Interfaceable, Selectable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean selected;
    private SkArRemitMaster remitMaster;
    private List<SkArRemitItem> remitItemList;
    private SkFiMasterInterface fiInterface;

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<SkArRemitItem> getRemitItemList() {
        return remitItemList;
    }

    public void setRemitItemList(List<SkArRemitItem> remitItemList) {
        this.remitItemList = remitItemList;
    }

    public SkArRemitMaster getRemitMaster() {
        return remitMaster;
    }

    public void setRemitMaster(SkArRemitMaster remitMaster) {
        this.remitMaster = remitMaster;
    }

    @Override
    public Persistable getPersistable() {
        return this.remitMaster;
    }

    @Override
    public SkFiMasterInterface getFiInterface() {
        logger.debug("fiInterface={}", remitMaster.getFiInterface());
        return remitMaster.getFiInterface();
    }
}

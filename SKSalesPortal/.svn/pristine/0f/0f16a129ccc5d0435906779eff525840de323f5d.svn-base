package com.tcci.sksp.vo;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.sksp.entity.ar.SkAdvancePayment;
import com.tcci.sksp.entity.ar.SkCheckMaster;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;

/**
 *
 * @author nEO.Fu
 */
public class AdvancePaymentVO implements Interfaceable, Selectable {

    private boolean selected;
    private boolean selectable;
    private SkAdvancePayment advancePayment;
    private SkCheckMaster checkMaster;
    private SkFiMasterInterface fiInterface;

    public AdvancePaymentVO(SkAdvancePayment advancePayment) {
        this.advancePayment = advancePayment;
    }

    public SkAdvancePayment getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(SkAdvancePayment advancePayment) {
        this.advancePayment = advancePayment;
    }

    public SkCheckMaster getCheckMaster() {
        return checkMaster;
    }

    public void setCheckMaster(SkCheckMaster checkMaster) {
        this.checkMaster = checkMaster;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public Persistable getPersistable() {
        return this.advancePayment;
    }

    @Override
    public SkFiMasterInterface getFiInterface() {
        return advancePayment.getFiInterface();
    }
}

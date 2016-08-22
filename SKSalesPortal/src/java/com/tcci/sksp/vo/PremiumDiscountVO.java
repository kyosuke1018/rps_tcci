package com.tcci.sksp.vo;

import com.tcci.fc.entity.essential.Persistable;
import com.tcci.sksp.entity.ar.SkFiMasterInterface;
import com.tcci.sksp.entity.ar.SkPremiumDiscount;

/**
 *
 * @author nEO.Fu
 */
public class PremiumDiscountVO implements Interfaceable, Selectable {

    SkPremiumDiscount discount;
    SkFiMasterInterface fiInterface;
    boolean selected;

    public SkPremiumDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(SkPremiumDiscount discount) {
        this.discount = discount;
    }

    @Override
    public Persistable getPersistable() {
        return this.discount;
    }

    @Override
    public SkFiMasterInterface getFiInterface() {
        return discount.getFiInterface();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

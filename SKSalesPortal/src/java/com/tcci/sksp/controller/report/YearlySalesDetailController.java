package com.tcci.sksp.controller.report;

import com.tcci.sksp.vo.YearlySalesDetailsVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class YearlySalesDetailController {

    private boolean yearRendered;
    private List<YearlySalesDetailsVO> yearlySalesDetailsVOList;

    public boolean isYearRendered() {
        return yearRendered;
    }

    public void setYearRendered(boolean yearRendered) {
        this.yearRendered = yearRendered;
    }

    public List<YearlySalesDetailsVO> getYearlySalesDetailsVOList() {
        return yearlySalesDetailsVOList;
    }

    public void setYearlySalesDetailsVOList(List<YearlySalesDetailsVO> yearlySalesDetailsVOList) {
        this.yearlySalesDetailsVOList = yearlySalesDetailsVOList;
    }

    public BigDecimal getUnitPrice(YearlySalesDetailsVO vo, int month) {
        BigDecimal unitPrice = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal giftQuantity = BigDecimal.ZERO;
        if (month == 1) {
            amount = vo.getM1Amount();
            quantity = vo.getM1Quantity();
            giftQuantity = vo.getM1GiftQuantity();
        } else if (month == 2) {
            amount = vo.getM2Amount();
            quantity = vo.getM2Quantity();
            giftQuantity = vo.getM2GiftQuantity();
        } else if (month == 3) {
            amount = vo.getM3Amount();
            quantity = vo.getM3Quantity();
            giftQuantity = vo.getM3GiftQuantity();
        } else if (month == 4) {
            amount = vo.getM4Amount();
            quantity = vo.getM4Quantity();
            giftQuantity = vo.getM4GiftQuantity();
        } else if (month == 5) {
            amount = vo.getM5Amount();
            quantity = vo.getM5Quantity();
            giftQuantity = vo.getM5GiftQuantity();
        } else if (month == 6) {
            amount = vo.getM6Amount();
            quantity = vo.getM6Quantity();
            giftQuantity = vo.getM6GiftQuantity();
        } else if (month == 7) {
            amount = vo.getM7Amount();
            quantity = vo.getM7Quantity();
            giftQuantity = vo.getM7GiftQuantity();
        } else if (month == 8) {
            amount = vo.getM8Amount();
            quantity = vo.getM8Quantity();
            giftQuantity = vo.getM8GiftQuantity();
        } else if (month == 9) {
            amount = vo.getM9Amount();
            quantity = vo.getM9Quantity();
            giftQuantity = vo.getM9GiftQuantity();
        } else if (month == 10) {
            amount = vo.getM10Amount();
            quantity = vo.getM10Quantity();
            giftQuantity = vo.getM10GiftQuantity();
        } else if (month == 11) {
            amount = vo.getM11Amount();
            quantity = vo.getM11Quantity();
            giftQuantity = vo.getM11GiftQuantity();
        } else if (month == 12) {
            amount = vo.getM12Amount();
            quantity = vo.getM12Quantity();
            giftQuantity = vo.getM12GiftQuantity();
        }else if(month==13) {
            amount = vo.getTotalAmount();
            quantity = vo.getTotalQuantity();
            giftQuantity = vo.getTotalGiftQuantity();
        }
        if (quantity.add(giftQuantity).doubleValue() > 0) {
            unitPrice = amount.divide(quantity.add(giftQuantity), 2, BigDecimal.ROUND_HALF_UP);
        }
        return unitPrice;
    }
}

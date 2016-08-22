package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.ar.SkPremiumDiscount;
import com.tcci.sksp.vo.PremiumDiscountVO;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author nEO.Fu
 */
public class PremiumDiscountDataModel extends ListDataModel<PremiumDiscountVO> implements SelectableDataModel<PremiumDiscountVO>{

    public PremiumDiscountDataModel(List<PremiumDiscountVO> list) {
        super(list);
    }
    
    @Override
    public Object getRowKey(PremiumDiscountVO t) {
        return t.getDiscount().getId();
    }

    @Override
    public PremiumDiscountVO getRowData(String rowKey) {
        List<PremiumDiscountVO> masterList = (List<PremiumDiscountVO>) getWrappedData();
        for(PremiumDiscountVO master : masterList) {
            if(master.getDiscount().getId().toString().equals(rowKey) )
                return master;
        }
        return null;
    }
    
}

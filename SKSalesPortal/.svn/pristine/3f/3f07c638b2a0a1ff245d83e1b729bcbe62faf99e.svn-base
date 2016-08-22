package com.tcci.sksp.controller.util;

import com.tcci.sksp.vo.RemitMasterVO;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Jason.Yu
 */
public class RemitMasterDataModel extends ListDataModel<RemitMasterVO>
        implements SelectableDataModel<RemitMasterVO> {

    public RemitMasterDataModel(List<RemitMasterVO> list) {
        super(list);
    }

    @Override
    public Object getRowKey(RemitMasterVO t) {
        return t.getRemitMaster().getId();
    }

    @Override
    public RemitMasterVO getRowData(String rowKey) {
        List<RemitMasterVO> masterVOs = (List<RemitMasterVO>) getWrappedData();
        for (RemitMasterVO masterVO : masterVOs) {
            if (masterVO.getRemitMaster().getId().toString().equals(rowKey)) {
                return masterVO;
            }
        }
        return null;
    }
}

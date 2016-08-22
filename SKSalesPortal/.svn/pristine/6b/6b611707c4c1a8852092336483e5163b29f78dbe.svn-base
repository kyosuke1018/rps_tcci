/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.controller.util;

import com.tcci.worklist.vo.ZtabExpRelfilenoSdVO;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author nEO
 */
public class ZtabExpRelfilenoSdVODataModel extends ListDataModel<ZtabExpRelfilenoSdVO> implements SelectableDataModel<ZtabExpRelfilenoSdVO> {

    public ZtabExpRelfilenoSdVODataModel(List<ZtabExpRelfilenoSdVO> list) {
        super(list);
    }
    
    @Override
    public Object getRowKey(ZtabExpRelfilenoSdVO t) {
        return t.getZtabExpRelfilenoSd().getId();
    }

    @Override
    public ZtabExpRelfilenoSdVO getRowData(String rowKey) {
        List<ZtabExpRelfilenoSdVO> masterList = (List<ZtabExpRelfilenoSdVO>) getWrappedData();
        for(ZtabExpRelfilenoSdVO master : masterList) {
            if(master.getZtabExpRelfilenoSd().getId().toString().equals(rowKey) )
                return master;
        }
        return null;
    }
    
}

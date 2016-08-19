package com.tcci.fc.controller.vaultmgmt;

import com.tcci.fc.entity.content.TcFvitem;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author nEO.Fu
 */
public class TcFvitemDataModel extends ListDataModel<TcFvitem> implements SelectableDataModel<TcFvitem> {

    public TcFvitemDataModel(List<TcFvitem> list) {
        super(list);
    }

    @Override
    public Object getRowKey(TcFvitem t) {
        return t.getId().toString();
    }

    @Override
    public TcFvitem getRowData(String rowKey) {
        List<TcFvitem> fvitemList = (List<TcFvitem>) getWrappedData();
        for (TcFvitem fvitem : fvitemList) {
            if (fvitem.getId().toString().equals(rowKey)) {
                return fvitem;
            }
        }
        return null;
    }
}

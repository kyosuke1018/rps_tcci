package com.tcci.sksp.controller.util;

import com.tcci.fc.entity.org.TcUser;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author nEO.Fu
 */
public class TcUserDataModel extends ListDataModel<TcUser> implements SelectableDataModel<TcUser>{

    public TcUserDataModel(List<TcUser> list) {
        super(list);
    }
    
    @Override
    public Object getRowKey(TcUser t) {
        return t.getId();
    }

    @Override
    public TcUser getRowData(String rowKey) {
        List<TcUser> userList = (List<TcUser>) getWrappedData();
        for(TcUser user : userList) {
            if(user.getId().toString().equals(rowKey) )
                return user;
        }
        return null;
    }
    
}

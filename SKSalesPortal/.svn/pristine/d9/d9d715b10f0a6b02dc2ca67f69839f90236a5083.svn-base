package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.ar.SkAccountsReceivable;
import com.tcci.sksp.facade.SkAccountsReceivableFacade;
import java.util.List;
import javax.faces.model.ListDataModel;
import javax.naming.InitialContext;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Lynn.Huang
 */
public class ArDataModel extends ListDataModel<SkAccountsReceivable> implements SelectableDataModel<SkAccountsReceivable> {
    private SkAccountsReceivableFacade skAccountsReceivableFacade;
    
    public ArDataModel() {        
    }
    
    public ArDataModel(List<SkAccountsReceivable> arlist) {
        super(arlist);
        try {
            InitialContext ctx = new InitialContext();
            skAccountsReceivableFacade = (SkAccountsReceivableFacade)ctx.lookup("java:global/SKSalesPortal/SkAccountsReceivableFacade");        
        } catch (Exception e) {            
        }
    }
    
    @Override
    public SkAccountsReceivable getRowData(String rowKey) {
        if (skAccountsReceivableFacade != null) {
            return skAccountsReceivableFacade.find(Long.parseLong(rowKey));
        }        
        return null;
    }
    
    @Override
    public Object getRowKey(SkAccountsReceivable skAccountsReceivable) {  
        return skAccountsReceivable.getId();
    }
}

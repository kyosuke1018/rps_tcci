package com.tcci.sksp.controller.admin;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.SelectEmployeeController;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.facade.SkSalesChannelsFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class CrudChannelController {
    private SkSalesChannels selectedChannel;
    private SkSalesChannels newChannel;
    private boolean isCreate;
    
    @EJB
    private SkSalesChannelsFacade salesChannelsFacade;
    @EJB
    private TcUserFacade tcUserFacade;
    
    @ManagedProperty(value = "#{selectEmployeeController}")
    SelectEmployeeController selectEmployeeController;

    public void setSelectEmployeeController(SelectEmployeeController selectEmployeeController) {
        this.selectEmployeeController = selectEmployeeController;
    }    

    public SkSalesChannels getSelectedChannel() {
        return selectedChannel;
    }

    public void setSelectedChannel(SkSalesChannels selectedChannel) {
        this.selectedChannel = selectedChannel;
    }
    
    public SkSalesChannels getNewChannel() {
        return newChannel;
    }

    public void setNewChannel(SkSalesChannels newChannel) {
        this.newChannel = newChannel;
    }

    public boolean isIsCreate() {
        return isCreate;
    }

    public void setIsCreate(boolean isCreate) {
        this.isCreate = isCreate;
    }    
    
    public void setSelectedChannel(SkSalesChannels selectedChannel, boolean isCreate) {        
        this.selectedChannel = selectedChannel;
        this.isCreate = isCreate;       
        selectEmployeeController.setSelectedUser(null);
        selectEmployeeController.setCname(null);          
        if (isCreate) {
            newChannel = new SkSalesChannels(); 
            newChannel.setParent(selectedChannel);
        } else {
            if (selectedChannel.getManager() != null) {
                selectEmployeeController.setSelectedUser(selectedChannel.getManager().getEmpId());
                selectEmployeeController.setCname(selectedChannel.getManager().getCname());
            }       
        }
    }
    
    private void validate() throws Exception {
        if (StringUtils.isEmpty(isCreate ? newChannel.getCode() : selectedChannel.getCode())) {
            throw new Exception(FacesUtil.getMessage("sales.channel.code.required"));
        }
        if (StringUtils.isEmpty(isCreate ? newChannel.getName() : selectedChannel.getName())) {
             throw new Exception(FacesUtil.getMessage("sales.channel.name.required"));
        }       
    }
    
    public void saveChannel() {
        try {                
            validate();
            TcUser manager = null;
            if (!StringUtils.isEmpty(selectEmployeeController.getSelectedUser())) {
                manager = tcUserFacade.findUserByEmpId(selectEmployeeController.getSelectedUser());                    
            }        
            if (isCreate) {            
                newChannel.setManager(manager);
                salesChannelsFacade.create(newChannel);
            } else {
                selectedChannel.setManager(manager);    
                salesChannelsFacade.edit(selectedChannel);
            }        
            String msg = "Channel " + selectedChannel.getName() + " has beed modified or created sub.";
            Logger.getLogger(CrudChannelController.class.getName()).log(Level.INFO, msg);    
        } catch (Exception e) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("errormsg", e.getLocalizedMessage());               
        }
    }
    
}

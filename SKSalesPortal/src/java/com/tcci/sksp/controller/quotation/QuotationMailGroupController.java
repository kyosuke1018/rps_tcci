package com.tcci.sksp.controller.quotation;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.UserFacade;
import com.tcci.sksp.controller.util.SessionController;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroup;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroupMember;
import com.tcci.sksp.entity.quotation.SkQuotationMailGroupUser;
import com.tcci.sksp.facade.SkQuotationMailGroupFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import com.tcci.worklist.controller.util.JsfUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@ManagedBean(name = "quotationMailGroup")
@ViewScoped
public class QuotationMailGroupController {

    Logger logger = LoggerFactory.getLogger(QuotationMailGroupController.class);
    ResourceBundle rb = ResourceBundle.getBundle("/messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    private SkQuotationMailGroup selected;
    private List<SkQuotationMailGroup> items;
    private String selectedMember;
    private List<SkSalesMember> memberList;
    private String selectedUser;
    private List<TcUser> userList;
    @EJB
    private SkQuotationMailGroupFacade ejbFacade;
    @EJB
    private SkSalesMemberFacade memberFacade;
    @EJB
    private UserFacade userFacade;
    @ManagedProperty(value = "#{sessionController}")
    private SessionController userSession;

    public void setUserSession(SessionController userSession) {
        this.userSession = userSession;
    }

    @PostConstruct
    private void init() {
        logger.debug("ejbFacade={}", ejbFacade);
        this.items = ejbFacade.findAll();
        this.memberList = new ArrayList();
        this.userList = new ArrayList();
    }

    public void prepareCreate() {
        this.selected = new SkQuotationMailGroup();
        this.selectedUser = "";
        this.selectedMember = "";
        this.memberList = new ArrayList();
        this.userList = new ArrayList();
    }

    public void prepareEdit(SkQuotationMailGroup group) {
        this.selected = group;
        this.selectedUser = "";
        this.selectedMember = "";
        this.memberList = new ArrayList();
        for (SkQuotationMailGroupMember groupMember : group.getMemberCollection()) {
            this.memberList.add(groupMember.getMember());
        }
        this.userList = new ArrayList();
        for (SkQuotationMailGroupUser groupUser : group.getUserCollection()) {
            this.userList.add(groupUser.getUser());
        }
    }

    public void remove(SkQuotationMailGroup group) {
        ejbFacade.remove(group);
        this.items.remove(group);
        JsfUtils.addSuccessMessage(rb.getString("quotation.mailGroup.msg.deleteSuccess"));
    }

    public String getEditTitle() {
        String editTitle = "";
        if (null == this.selected || null == this.selected.getId()) {
            editTitle = rb.getString("button.add");
        } else {
            editTitle = rb.getString("button.edit");
        }
        return editTitle.concat(rb.getString("quotation.mailGroup"));
    }

    public List<String> completeMember(String input) {
        List<String> resultList = new ArrayList();
        logger.debug("completeMember,input={}", input);
        List<SkSalesMember> memberList = memberFacade.findByCriteria(input);
        logger.debug("list.size()={}", memberList.size());
        for (SkSalesMember member : memberList) {
            resultList.add(member.getCode());
        }
        selectedMember = "";

        return resultList;
    }

    public String getMemberDisplayIdentifier(String code) {
        return memberFacade.findByCode(code).getDisplayIdentifier();
    }

    public void selectMember(SelectEvent event) {
        logger.debug("selectMember,event={}", event);
        logger.debug("event.getObject()={}", event.getObject());
        SkSalesMember member = memberFacade.findByCode((String) event.getObject());
        if (!memberList.contains(member)) {
            List<SkSalesMember> newMemberList = new ArrayList();
            newMemberList.add(member);
            newMemberList.addAll(memberList);
            memberList = newMemberList;
        }
    }

    public void removeMember(SkSalesMember member) {
        logger.debug("removeMember,member={}", member);
        memberList.remove(member);
    }

    public List<String> completeUser(String input) {
        logger.debug("completeUser,input={}", input);
        List<TcUser> userList = userFacade.findByCriteria(input);
        List<String> resultList = new ArrayList();
        for (TcUser user : userList) {
            resultList.add(user.getLoginAccount());
        }
        selectedUser = "";
        logger.debug("list.size()={}", userList.size());
        return resultList;
    }

    public String getUserDisplayIdentifier(String loginAccount) {
        String displayIdentifier = "";
        logger.debug("userFacade={}", userFacade);
        TcUser user = userFacade.findUserByLoginAccount(loginAccount);
        if (user != null) {
            displayIdentifier = user.getDisplayIdentifier();
        }

        return displayIdentifier;
    }

    public void selectUser(SelectEvent event) {
        logger.debug("selectMember,event={}", event);
        logger.debug("event.getObject()={}", event.getObject());
        TcUser user = userFacade.findUserByLoginAccount((String) event.getObject());
        if (!userList.contains(user)) {
            List<TcUser> newUserList = new ArrayList();
            newUserList.add(user);
            newUserList.addAll(userList);
            userList = newUserList;
        }
    }

    public void removeUser(TcUser user) {
        logger.debug("removeUser,member={}", user);
        userList.remove(user);
    }

    public void save() {
        List<String> errorMessagList = validate();
        if (!errorMessagList.isEmpty()) {
            String error = "";
            for (String errorMessage : errorMessagList) {
                if (error.length() > 0) {
                    error += "\n";
                }
                error = error + errorMessage;
            }
            logger.debug("error");
            RequestContext.getCurrentInstance().addCallbackParam("errormsg", error);
        } else {
            SkQuotationMailGroup group = ejbFacade.save(this.selected, this.memberList, this.userList, userSession.getUser());
            List<SkQuotationMailGroup> newItems = new ArrayList();
            boolean add = false;
            if (!this.items.isEmpty()) {
                for (SkQuotationMailGroup existsGroup : items) {
                    if (group.equals(existsGroup)) {
                        newItems.add(group);
                        add = true;
                    } else {
                        newItems.add(existsGroup);
                    }
                }
            } else {
                newItems.add(group);
            }
            if (!add) {
                newItems.add(group);
            }
            this.items = newItems;
            JsfUtils.addSuccessMessage(rb.getString("quotation.mailGroup.msg.saveSuccess"));
        }
    }

    private List<String> validate() {
        List<String> errorMessageList = new ArrayList();
        if (StringUtils.isEmpty(selected.getName())) {
            errorMessageList.add(rb.getString("quotation.mailGroup.error.nameIsRequired"));
        }
        if (memberList.isEmpty()) {
            errorMessageList.add(rb.getString("quotation.mailGroup.error.memberIsRequired"));
        }
        if (userList.isEmpty()) {
            errorMessageList.add(rb.getString("quotation.mailGroup.error.userIsRequired"));
        }
        return errorMessageList;

    }
    //<editor-fold defaultstate="collapsed" desc="getter,setter">

    public SkQuotationMailGroup getSelected() {
        return selected;
    }

    public void setSelected(SkQuotationMailGroup selected) {
        this.selected = selected;
    }

    public List<SkSalesMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<SkSalesMember> memberList) {
        this.memberList = memberList;
    }

    public List<TcUser> getUserList() {
        return userList;
    }

    public void setUserList(List<TcUser> userList) {
        this.userList = userList;
    }

    public String getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(String selectedMember) {
        this.selectedMember = selectedMember;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<SkQuotationMailGroup> getItems() {
        return items;
    }

    public void setItems(List<SkQuotationMailGroup> items) {
        this.items = items;
    }
    //</editor-fold>
}

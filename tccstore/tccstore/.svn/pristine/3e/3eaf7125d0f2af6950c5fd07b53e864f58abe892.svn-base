/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.usermgmt;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.usermgmt.UsermgmtFacade;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jimmy
 */
@ManagedBean(name = "importTcUser")
@ViewScoped
public class ImportTcUser extends ImportExcelBase<TcUserVO> {

    // managed bean
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    // ejb
    @EJB
    private UsermgmtFacade usermgmtFacade;

    // internal data
    private Map<String, TcUser> mapUser;
    private Date saveDate;

    // c'tor
    public ImportTcUser() {
        super(TcUserVO.class);
        mapUser = new HashMap<String, TcUser>();
    }
    
    // base methods implement/override
    @Override
    protected boolean postInit(TcUserVO vo) {
        vo.setTcUser(findUserByLoginAccount(vo.getLoginAccount()));
        vo.updateStatus();
        return true;
    }

    @Override
    public void beforeSave() {
        saveDate = new Date();
    }
    
    @Override
    protected boolean insert(TcUserVO vo) {
        TcUser tcUser = new TcUser();
        tcUser.setCreatetimestamp(saveDate);
        tcUser.setCreator(userSession.getTcUser());
        tcUser.setDisabled(false);
        tcUser.setLoginAccount(vo.getLoginAccount());
        vo.setTcUser(tcUser);
        return saveVO(vo);
    }

    @Override
    protected boolean update(TcUserVO vo) {
        return saveVO(vo);
    }
    
    // helper
    private TcUser findUserByLoginAccount(String loginAccount) {
        if (mapUser.containsKey(loginAccount)) {
            return mapUser.get(loginAccount);
        }
        TcUser user = usermgmtFacade.findUserByLoginAccount(loginAccount);
        if (user != null) {
            mapUser.put(loginAccount, user);
        }
        return user;
    }
    
    private boolean saveVO(TcUserVO vo) {
        TcUser tcUser = vo.getTcUser();
        tcUser.setCname(vo.getCname());
        tcUser.setEmail(vo.getEmail());
        tcUser.setEmpId(vo.getEmpId());
        try {
            usermgmtFacade.save(vo.getTcUser());
            if (!mapUser.containsKey(vo.getLoginAccount())) {
                mapUser.put(vo.getLoginAccount(), vo.getTcUser());
            }
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    // getter, setter
    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

}

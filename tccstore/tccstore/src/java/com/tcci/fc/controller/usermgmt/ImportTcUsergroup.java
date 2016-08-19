/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.usermgmt;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
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
 * @author Jimmy.Lee
 */
@ManagedBean(name = "importTcUsergroup")
@ViewScoped
public class ImportTcUsergroup extends ImportExcelBase<TcUsergroupVO> {

    // managed bean
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;

    // ejb
    @EJB
    private UsermgmtFacade usermgmtFacade;

    // internal data
    private Map<String, TcUser> mapUser;
    private Map<String, TcGroup> mapGroup;
    private Map<String, TcUsergroup> mapUsergroup;
    private Date saveDate;
    
    // c'tor
    public ImportTcUsergroup() {
        super(TcUsergroupVO.class);
        mapUser = new HashMap<String, TcUser>();
        mapGroup = new HashMap<String, TcGroup>();
        mapUsergroup = new HashMap<String, TcUsergroup>();
    }
    
    @Override
    protected boolean postInit(TcUsergroupVO vo) {
        TcUser user = findUserByLoginAccount(vo.getLoginAccount());
        if (null == user) {
            vo.setMessage("loginAccount not exist");
            return false;
        }
        vo.setTcUser(user);
        TcGroup group = findGroup(vo.getGroupCode());
        if (null == group) {
            vo.setMessage("groupCode not exist");
            return false;
        }
        vo.setTcGroup(group);
        vo.setTcUsergroup(findUsergroup(user, group));
        vo.updateStatus();
        return true;
    }

    @Override
    public void beforeSave() {
        saveDate = new Date();
    }
    
    @Override
    protected boolean insert(TcUsergroupVO vo) {
        TcUsergroup ug = new TcUsergroup();
        ug.setCreatetimestamp(saveDate);
        ug.setCreator(userSession.getTcUser());
        ug.setUserId(vo.getTcUser());
        ug.setGroupId(vo.getTcGroup());
        vo.setTcUsergroup(ug);
        return saveVO(vo);
    }

    @Override
    protected boolean update(TcUsergroupVO vo) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    private TcGroup findGroup(String code) {
        if (mapGroup.containsKey(code)) {
            return mapGroup.get(code);
        }
        TcGroup group = usermgmtFacade.findGroup(code);
        if (null != group) {
            mapGroup.put(code, group);
        }
        return group;
    }

    private TcUsergroup findUsergroup(TcUser tcUser, TcGroup tcGroup) {
        String key = tcUser.getLoginAccount() + ":" + tcGroup.getCode();
        if (mapUsergroup.containsKey(key)) {
            return mapUsergroup.get(key);
        }
        TcUsergroup gu = usermgmtFacade.findUsergroup(tcUser, tcGroup);
        if (null != gu) {
            mapUsergroup.put(key, gu);
        }
        return gu;
    }

    private boolean saveVO(TcUsergroupVO vo) {
        try {
            usermgmtFacade.save(vo.getTcUsergroup());
            String key = vo.getLoginAccount() + ":" + vo.getGroupCode();
            if (!mapUsergroup.containsKey(key)) {
                mapUsergroup.put(key, vo.getTcUsergroup());
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

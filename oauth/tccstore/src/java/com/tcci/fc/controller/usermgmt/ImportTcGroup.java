/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.usermgmt;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.fc.entity.org.TcGroup;
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
@ManagedBean(name = "importTcGroup")
@ViewScoped
public class ImportTcGroup extends ImportExcelBase<TcGroupVO> {
    
    // managed bean
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    
    // ejb
    @EJB
    private UsermgmtFacade usermgmtFacade;

    // internal data
    private Map<String, TcGroup> mapGroup;
    private Date saveDate;

    // c'tor
    public ImportTcGroup() {
        super(TcGroupVO.class);
        mapGroup = new HashMap<String, TcGroup>();
    }

    // base methods implement/override
    @Override
    protected boolean postInit(TcGroupVO vo) {
        vo.setTcGroup(findGroup(vo.getCode()));
        vo.updateStatus();
        return true;
    }

    @Override
    public void beforeSave() {
        saveDate = new Date();
    }
    
    @Override
    protected boolean insert(TcGroupVO vo) {
        TcGroup group = new TcGroup();
        group.setCode(vo.getCode());
        group.setCreatetimestamp(saveDate);
        group.setCreator(userSession.getTcUser());
        vo.setTcGroup(group);
        return saveVO(vo);
    }

    @Override
    protected boolean update(TcGroupVO vo) {
        return saveVO(vo);
    }

    // helper
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

    private boolean saveVO(TcGroupVO vo) {
        TcGroup group = vo.getTcGroup();
        group.setName(vo.getName());
        try {
            usermgmtFacade.save(vo.getTcGroup());
            if (!mapGroup.containsKey(vo.getCode())) {
                mapGroup.put(vo.getCode(), vo.getTcGroup());
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

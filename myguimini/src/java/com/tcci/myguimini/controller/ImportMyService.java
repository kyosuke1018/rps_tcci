/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.controller;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.myguimini.entity.MyService;
import com.tcci.myguimini.facade.MyServiceFacade;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "importMyService")
@ViewScoped
public class ImportMyService extends ImportExcelBase<MyServiceVO> {
    
    @EJB
    private MyServiceFacade myServiceFacade;
    
    // internal data
    private Map<String, MyService> mapMyService;

    // c'tor
    public ImportMyService() {
        super(MyServiceVO.class);
        mapMyService = new HashMap<String, MyService>();
    }
    
    @Override
    protected boolean postInit(MyServiceVO vo) {
        vo.setMyService(findMyService(vo.getService()));
        vo.updateStatus();
        return true;
    }

    @Override
    protected boolean insert(MyServiceVO vo) {
        MyService myService = new MyService();
        vo.setMyService(myService);
        return saveVO(vo);
    }

    @Override
    protected boolean update(MyServiceVO vo) {
        return saveVO(vo);
    }
    
    // helper
    private MyService findMyService(String service) {
        if (mapMyService.containsKey(service)) {
            return mapMyService.get(service);
        }
        MyService myService = myServiceFacade.find(service);
        if (myService != null) {
            mapMyService.put(service, myService);
        }
        return myService;
    }
    
    private boolean saveVO(MyServiceVO vo) {
        String service = vo.getService();
        MyService myService = vo.getMyService();
        myService.setService(service);
        myService.setServiceUrl(vo.getServiceUrl());
        myService.setDescription(vo.getDescription());
        myService.setActive(vo.getActive()==1);
        try {
            myServiceFacade.save(myService);
            if (!mapMyService.containsKey(service)) {
                mapMyService.put(service, myService);
            }
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }
    
    @Override
    protected void afterSave() {
        myServiceFacade.reload();
    }
    
}

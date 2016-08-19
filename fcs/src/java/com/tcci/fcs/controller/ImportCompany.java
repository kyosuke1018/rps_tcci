/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fcs.entity.FcCompGroup;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcUploaderR;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.facade.FcCompGroupFacade;
import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.facade.FcUploaderRFacade;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "importCompany")
@ViewScoped
public class ImportCompany extends ImportExcelBase<CompanyVO> {

    @EJB
    private FcCompanyFacade companyFacade;
    @EJB
    private TcUserFacade tcUserFacade;
    @EJB
    private FcUploaderRFacade fcUploaderRFacade;
    @EJB
    private FcCompGroupFacade fcCompGroupFacade;
    
    private Map<String, FcCompany> mapCompany; // code, fcCompany
    private Map<String, TcUser> mapUser; // loginAccount, tcUser
    
    // c'tor
    public ImportCompany() {
        super(CompanyVO.class);
        mapCompany = new HashMap<String, FcCompany>();
        mapUser = new HashMap<String, TcUser>();
    }
    
    // interface implement
    @Override
    protected boolean postInit(CompanyVO vo) {
        vo.setCompany(findCompany(vo.getCode()));
        String loginAccount = vo.getLoginAccount();
        if (loginAccount != null) {
            TcUser uploader = findUserByLoginAccount(loginAccount);
            if (null == uploader) {
                vo.setMessage("loginAccount user not exist");
                return false;
            }
            vo.setUploader(uploader);
        }
        //檢查母公司欄位
        if(vo.getGroup()!=null){
            CompanyGroupEnum groupEnum = CompanyGroupEnum.getFromCode(vo.getGroup().toUpperCase());
            if(groupEnum==null){
                vo.setMessage("group not exist");
                return false;
            }
        }else{
            vo.setMessage("group not exist");
            return false;
        }
        
        vo.updateStatus();
        return true;
    }

    @Override
    protected boolean insert(CompanyVO vo) {
        boolean active = (vo.getActive()==1);
//        FcCompany company = new FcCompany(vo.getCode(), vo.getName(), active, vo.getUploader());
        
        FcCompGroup group = fcCompGroupFacade.findByCode(vo.getGroup().toUpperCase());
        
        //匯入欄位增加
        FcCompany company = new FcCompany(vo.getCode(), vo.getName(), active, vo.getUploader(), 
                vo.getSort(), group);
        try {
            companyFacade.save(company);
            if(vo.getUploader() != null){//公司匯入 寫入上傳人關聯
                //重新查得該公司
                company = companyFacade.findByCode(vo.getCode());
                FcUploaderR fcUploaderR = new FcUploaderR();
                fcUploaderR.setFcCompany(company);
                fcUploaderR.setTcUser(vo.getUploader());
                fcUploaderRFacade.save(fcUploaderR);
            }
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    @Override
    protected boolean update(CompanyVO vo) {
        boolean active = (vo.getActive()==1);
        FcCompany company = vo.getCompany();
        company.setName(vo.getName());
        company.setActive(active);
        company.setUploader(vo.getUploader());
        //匯入欄位增加
        company.setSort(vo.getSort());
//        company.setGroup(CompanyGroupEnum.getFromCode(vo.getGroup().toUpperCase()));
        FcCompGroup group = fcCompGroupFacade.findByCode(vo.getGroup().toUpperCase());
        company.setGroup(group);
        try {
            companyFacade.save(company);
            if(vo.getUploader() != null){//公司匯入 寫入上傳人關聯
                List<FcUploaderR> uploadererLiser = fcUploaderRFacade.findByCompanyCode(company.getCode());
                if (uploadererLiser != null) {
                    boolean duplicate = false;
                    for(FcUploaderR fcUploaderR:uploadererLiser){
                        if(fcUploaderR.getTcUser().getId().equals(vo.getUploader().getId())){
//                        if(fcUploaderR.getTcUser().getId() == vo.getUploader().getId()){
                            duplicate = true;
                        }
                    }
                    if(!duplicate){//排除現有的上傳人
                        FcUploaderR fcUploaderR = new FcUploaderR();
                        fcUploaderR.setFcCompany(company);
                        fcUploaderR.setTcUser(vo.getUploader());
                        fcUploaderRFacade.save(fcUploaderR);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }
    
    // helper
    private FcCompany findCompany(String code) {
        if (mapCompany.containsKey(code)) {
            return mapCompany.get(code);
        }
        FcCompany company = companyFacade.findByCode(code);
        if (company != null) {
            mapCompany.put(code, company);
        }
        return company;
    }
    
    private TcUser findUserByLoginAccount(String loginAccount) {
        if (mapUser.containsKey(loginAccount)) {
            return mapUser.get(loginAccount);
        }
        TcUser user = tcUserFacade.findUserByLoginAccount(loginAccount);
        if (user != null) {
            mapUser.put(loginAccount, user);
        }
        return user;
    }
}
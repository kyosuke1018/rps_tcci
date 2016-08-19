/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.member.EcMemberFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class MemberCustomerImport extends ImportExcelBase<MemberCustomerVO> {

    @EJB
    private EcMemberFacade ecMemberFacade;
    @EJB
    private EcCustomerFacade ecCustomerFacade;
    
    private Set<String> setPK;
    private Map<String, EcMember> mapMember;
    private Map<String, EcCustomer> mapCustomer;

    public MemberCustomerImport() {
        super(MemberCustomerVO.class);
        setPK = new HashSet<>();
        mapMember = new HashMap<>();
        mapCustomer = new HashMap<>();
    }
    
    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(MemberCustomerVO vo) {
        String loginAccount = StringUtils.lowerCase(vo.getLoginAccount());
        vo.setLoginAccount(loginAccount);
        String pk = loginAccount + ":" + vo.getCustomer();
        if (!setPK.add(pk)) {
            vo.setMessage("data duplicated!");
            return false;
        }
        EcMember ecMember = findMember(loginAccount);
        if (null == ecMember) {
            vo.setMessage(loginAccount + " loginAccount not exist!");
            return false;
        }
        vo.setEcMember(ecMember);
        if (vo.getCustomer() != null) {
            EcCustomer ecCustomer = findCustomer(vo.getCustomer());
            if (null == ecCustomer) {
                vo.setMessage(vo.getCustomer() + " customer not exist!");
                return false;
            }
            vo.setEcCustomer(ecCustomer);
            if (!ecMemberFacade.isMemberCusomerExist(ecMember, ecCustomer)) {
                vo.setStatus(ExcelVOBase.Status.ST_INSERT);
            }
        } else if (vo.getCustomers() != null ) {
            String[] codeArray = StringUtils.split(vo.getCustomers(), ",\n ");
            List<EcCustomer> list = new ArrayList<>();
            for (String code : codeArray) {
                EcCustomer ecCustomer = findCustomer(code);
                if (null == ecCustomer) {
                    vo.setMessage(code + " customer not exist!");
                    return false;
                }
                list.add(ecCustomer);
            }
            vo.setEcCustomers(list);
            vo.setStatus(ExcelVOBase.Status.ST_UPDATE);
        } else {
            vo.setStatus(ExcelVOBase.Status.ST_UPDATE); // clean member customer
        }
        return true;
    }

    @Override
    protected boolean insert(MemberCustomerVO vo) {
        try {
            ecMemberFacade.insertMemberCustomer(vo.getEcMember(), vo.getEcCustomer());
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    @Override
    protected boolean update(MemberCustomerVO vo) {
        try {
            ecMemberFacade.updateMemberCustomer(vo.getEcMember(), vo.getEcCustomers());
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    private EcMember findMember(String loginAccount) {
        if (mapMember.containsKey(loginAccount)) {
            return mapMember.get(loginAccount);
        }
        EcMember ecMember = ecMemberFacade.findByLoginAccount(loginAccount);
        mapMember.put(loginAccount, ecMember);
        return ecMember;
    }

    private EcCustomer findCustomer(String customer) {
        if (mapCustomer.containsKey(customer)) {
            return mapCustomer.get(customer);
        }
        EcCustomer ecCustomer = ecCustomerFacade.findByCode(customer);
        mapCustomer.put(customer, ecCustomer);
        return ecCustomer;
    }
    
}

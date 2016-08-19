/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.facade.member.EcMemberFacade;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class MemberImport extends ImportExcelBase<MemberVO> {

    @EJB
    private EcMemberFacade entityFacade;

    private Set<String> setPK;

    public MemberImport() {
        super(MemberVO.class);
        setPK = new HashSet<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(MemberVO vo) {
        String loginAccount = StringUtils.lowerCase(vo.getLoginAccount());
        vo.setLoginAccount(loginAccount);
        if (!setPK.add(loginAccount)) {
            vo.setMessage(loginAccount + " loginAccount duplicated!");
            return false;
        }
        EcMember ecMember = entityFacade.findByLoginAccount(loginAccount);
        vo.setEcMember(ecMember);
        if (ecMember != null && vo.getPassword() == null) {
            vo.setPassword(ecMember.getPassword());
        }
        String[] fields = {"name", "email", "phone", "password", "active"};
        updateStatus(vo, ecMember, fields);
        return true;
    }

    @Override
    protected boolean insert(MemberVO vo) {
        EcMember ecMember = new EcMember();
        ecMember.setLoginAccount(vo.getLoginAccount());
        vo.setEcMember(ecMember);
        return save(vo);
    }

    @Override
    protected boolean update(MemberVO vo) {
        return save(vo);
    }

    private boolean save(MemberVO vo) {
        EcMember ecMember = vo.getEcMember();
        ecMember.setName(vo.getName());
        ecMember.setEmail(vo.getEmail());
        ecMember.setPhone(vo.getPhone());
        String password = vo.getPassword();
        if (password != null && password.length() != 64) {
            password = DigestUtils.sha256Hex(password);
        }
        ecMember.setPassword(password);
        ecMember.setActive(vo.isActive());
        try {
            if (ecMember.getId() == null) {
                entityFacade.create(ecMember);
            } else {
                entityFacade.edit(ecMember);
            }
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }
}

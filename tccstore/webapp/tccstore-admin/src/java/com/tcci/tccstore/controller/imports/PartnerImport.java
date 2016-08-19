/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.enums.PartnerStatusEnum;
import com.tcci.tccstore.facade.member.EcMemberFacade;
import com.tcci.tccstore.facade.partner.EcPartnerFacade;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class PartnerImport extends ImportExcelBase<PartnerVO> {

    private String imageFolder;
    
    @EJB
    private EcPartnerFacade entityFacade;
    @EJB
    private EcMemberFacade ecMemberFacade;
    
    private Map<String, EcMember> mapMember;
    private Set<String> setName;
    
    public PartnerImport() {
        super(PartnerVO.class);
        mapMember = new HashMap<>();
        setName = new HashSet<>();
    }

    @Override
    protected void verify() {
        setName.clear();
        super.verify();
    }
    
    @Override
    protected boolean postInit(PartnerVO vo) {
        EcMember owner = findMember(vo.getOwnerAccount());
        if (null == owner) {
            vo.setMessage(vo.getOwnerAccount() + " owner not exist!");
            return false;
        }
        vo.setOwner(owner);
        if (!setName.add(vo.getName())) {
            vo.setMessage(vo.getName() + " name duplicated!");
            return false;
        }
        EcPartner ecPartner = entityFacade.findByName(vo.getName());
        vo.setEcPartner(ecPartner);
        if (vo.getImage() != null) {
            String pathname = imageFolder + File.separator + vo.getImage();
            File file = new File(pathname);
            if(!file.exists() || file.isDirectory()) {
                vo.setMessage(vo.getImage() + " image file not exist!");
                return false;
            }
        }
        // theme 暫時不用, image, pstatus 另外處理
        String[] fields = { "owner", "description", "address", "province", "city", "district", "town", 
            "contact", "phone", "social", "active"};
        ExcelVOBase.Status status = updateStatus(vo, ecPartner, fields);
        // 有圖檔一律update (or insert)
        if (status==ExcelVOBase.Status.ST_NOCHANGE) {
            if (vo.getImage() != null ||
                    !vo.getPstatus().equals(ecPartner.getStatus().name())) {
                vo.setStatus(ExcelVOBase.Status.ST_UPDATE);
            }
        }
        return true;
    }

    @Override
    protected boolean insert(PartnerVO vo) {
        EcPartner ecPartner = new EcPartner();
        ecPartner.setCreatetime(new Date());
        ecPartner.setName(vo.getName());
        vo.setEcPartner(ecPartner);
        return save(vo);
    }

    @Override
    protected boolean update(PartnerVO vo) {
        return save(vo);
    }

    // action
    public void dummy() {
        System.out.println(imageFolder);
    }
    
    private EcMember findMember(String ownerAccount) {
        if (mapMember.containsKey(ownerAccount)) {
            return mapMember.get(ownerAccount);
        }
        EcMember ecMember = ecMemberFacade.findActiveByLoginAccount(ownerAccount);
        mapMember.put(ownerAccount, ecMember);
        return ecMember;
    }
    
    private boolean save(PartnerVO vo) {
        EcPartner entity = vo.getEcPartner();
        entity.setOwner(vo.getOwner());
        entity.setDescription(vo.getDescription());
        entity.setAddress(vo.getAddress());
        entity.setProvince(vo.getProvince());
        entity.setCity(vo.getCity());
        entity.setDistrict(vo.getDistrict());
        entity.setTown(vo.getTown());
        entity.setContact(vo.getContact());
        entity.setPhone(vo.getPhone());
        entity.setSocial(vo.getSocial());
        // ecPartner.setTheme(vo.getTheme());
        entity.setStatus(PartnerStatusEnum.fromString(vo.getPstatus()));
        entity.setActive(vo.isActive());
        try {
            entityFacade.save(entity);
            if (vo.getImage() != null) {
                String pathname = imageFolder + File.separator + vo.getImage();
                entityFacade.updateImage(entity, pathname);
            }
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    // getter, setter
    public String getImageFolder() {
        return imageFolder;
    }

    public void setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.ImportUtils;
import com.tcci.ec.entity.EcCompany;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPerson;
import com.tcci.ec.entity.EcSeller;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.entity.EcStoreUser;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PersonTypeEnum;
import com.tcci.ec.enums.TccMemberEnum;
import com.tcci.ec.model.FileVO;
import com.tcci.ec.model.ImportTcc;
import com.tcci.ec.model.rs.ImportResultVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.criteria.MemberCriteriaVO;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.ImportTccDealerVO;
import com.tcci.ec.model.ImportTccDsVO;
import com.tcci.ec.util.AnnotationImportUtils;
import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.fc.util.StringUtils;
import com.tcci.security.AESPasswordHash;
import com.tcci.security.AESPasswordHashImpl;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcMemberFacade extends AbstractFacade<EcMember> {
    @EJB EcCompanyFacade companyFacade;
    @EJB EcPersonFacade personFacade;
    @EJB EcSellerFacade sellerFacade;
    @EJB EcStoreFacade storeFacade;
    @EJB EcFileFacade fileFacade;
    @EJB EcOptionFacade optionFacade;
    @EJB EcStoreUserFacade storeUserFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcMemberFacade() {
        super(EcMember.class);
    }
    
    public String getMemberDetailInfoSQL(){
        // EC_PERSON, EC_COMPANY 欄位聯集
        
        // EC_PERSON, EC_COMPANY 都有欄位
        StringBuilder common = new StringBuilder();
        common.append("  SELECT id, type, main_id, cname, ename, id_type, id_code, nickname, brief, \n");
        common.append("  email1, email2, tel1, tel2, tel3, fax1, country, state, addr1, addr2, \n");
        common.append("  url1, url2, CREATOR, CREATETIME, MODIFIER, MODIFYTIME, \n");

        StringBuilder sql = new StringBuilder();
        // 個人
        sql.append(common.toString());
        sql.append("  gender, birthday, age, \n");// EC_PERSON only
        
        sql.append("  NULL email3, NULL fax2, NULL owner1, NULL owner2, NULL contact1, NULL contact2, NULL contact3, \n");// EC_COMPANY only
        sql.append("  NULL web_id1, NULL web_id2, NULL longitude, NULL latitude, \n");// EC_COMPANY only
        sql.append("  NULL start_at, NULL category, NULL capital, NULL year_income, \n");// EC_COMPANY only
        sql.append("  '").append(MemberTypeEnum.PERSON.getCode()).append("' MEM_TYPE \n");
        sql.append("  FROM EC_PERSON \n");
        
        sql.append("  UNION \n");
        // 公司/組織
        sql.append(common.toString());
        sql.append("  NULL gender, NULL birthday, NULL age, \n");// EC_PERSON only
        
        sql.append("  email3, fax2, owner1, owner2, contact1, contact2, contact3, \n");// EC_COMPANY only
        sql.append("  web_id1, web_id2, longitude, latitude, start_at, category, capital, year_income, \n");// EC_COMPANY only
        sql.append("  '").append(MemberTypeEnum.COMPANY.getCode()).append("' MEM_TYPE \n");
        sql.append("  FROM EC_COMPANY \n");
        
        return sql.toString();
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcMember entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getActive()==null ){ entity.setActive(true); }
            if( entity.getAdminUser()==null ){ entity.setAdminUser(false); }
            if( entity.getSellerApply()==null ){ entity.setSellerApply(false); }
            if( entity.getSellerApprove()==null ){ entity.setSellerApprove(false); }
           
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }
    public void saveVO(MemberVO vo, EcMember operator, Locale locale, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        logger.debug("saveVO id = {}, memberId = {}, type = {}", new Object[] {vo.getId(), vo.getMemberId(), vo.getType()});
       
        // for 會員主檔 EC_MEMBER
        vo.setActive(vo.getActive()==null?true:vo.getActive());
        vo.setAdminUser(vo.getAdminUser()==null?false:vo.getAdminUser());
        vo.setSellerApply(vo.getSellerApply()==null?false:vo.getSellerApply());
        vo.setSellerApprove(vo.getSellerApprove()==null?false:vo.getSellerApprove());
        vo.setComApply(vo.getComApply()==null?false:vo.getComApply());
        vo.setComApprove(vo.getComApprove()==null?false:vo.getComApprove());
        vo.setNickname(vo.getNickname()==null?vo.getCname():vo.getNickname());// for 系統顯示

        boolean existed = (vo.getMemberId()!=null && vo.getMemberId()>0);
        EcMember member = (existed)?this.find(vo.getMemberId()):new EcMember();
        
        if( !existed ){// new
            // default password
            AESPasswordHash aes = new AESPasswordHashImpl();
            String encrypted = aes.encrypt(GlobalConstant.DEF_PWD);
            member.setPassword(encrypted);
        }
        
        Long detailId = vo.getId();// 保存 detailId (EC_COMPANY.ID or EC_PERSON.ID)
        vo.setId(vo.getMemberId());
        // 申請賣家
        boolean newApplySeller = (member.getSellerApply()==null || !member.getSellerApply()) 
                                && (vo.getSellerApply()!=null && vo.getSellerApply());
        // 核准賣家
        boolean newApproveSeller = (member.getSellerApprove()==null || !member.getSellerApprove()) 
                                && (vo.getSellerApprove()!=null && vo.getSellerApprove());
        vo.setApplytime(newApplySeller?new Date():member.getApplytime());
        vo.setApprovetime(newApproveSeller?new Date():member.getApprovetime());
        // 申請轉公司戶
        boolean newApplyCom= (member.getComApply()==null || !member.getComApply()) 
                                && (vo.getComApply()!=null && vo.getComApply());
        // 核准公司戶
        boolean newApproveCom = (member.getComApprove()==null || !member.getComApprove()) 
                                && (vo.getComApprove()!=null && vo.getComApprove());
        vo.setComApplytime(newApplyCom?new Date():member.getComApplytime());
        vo.setComApprovetime(newApproveCom?new Date():member.getComApprovetime());
        
        ExtBeanUtils.copyProperties(member, vo);
        logger.debug("saveVO MemberType="+vo.getMemberType()+", Type="+vo.getType());
        member.setType(vo.getMemberType());
        member.setDisabled(false); 
        this.save(member, operator, simulated);// save EC_MEMBER
        logger.debug("saveVO member.getId() = "+member.getId());
        vo.setMemberId(member.getId());// for new
        // for 自行註冊
        operator = (operator==null)?new EcMember(member.getId()):operator;

        // for 詳細資料 (EC_COMPANY or EC_PERSON)
        // 詳細資料
        vo.setId(detailId);// 一定要詳細資料 copyProperties 前復原
        if( MemberTypeEnum.COMPANY.getCode().equals(vo.getMemberType()) ){
            EcCompany company = (detailId!=null)?companyFacade.find(detailId):new EcCompany();
            logger.debug("saveVO company.getId()="+company.getId()+", detailId="+detailId);
            ExtBeanUtils.copyProperties(company, vo);
            company.setType(CompanyTypeEnum.MEMBER.getCode());
            company.setMainId(member.getId());
            companyFacade.save(company, operator, simulated);// save EC_COMPANY
            detailId = company.getId();
            logger.debug("saveVO company.getId() = "+company.getId());
        }else{
            EcPerson person = (detailId!=null)?personFacade.find(detailId):new EcPerson();
            logger.debug("saveVO person.getId()="+person.getId()+", detailId="+detailId);
            ExtBeanUtils.copyProperties(person, vo);
            person.setType(PersonTypeEnum.MEMBER.getCode());
            person.setMainId(member.getId());
            personFacade.save(person, operator, simulated);// save EC_PERSON
            detailId = person.getId();
            logger.debug("saveVO person.getId() = "+person.getId());
        }
        vo.setId(detailId);// for new
               
        // for 核准賣家 
        if( vo.getSellerApply()!=null && vo.getSellerApply() ){
            // for EC_SELLER
            EcSeller seller = sellerFacade.findByMemberId(member.getId());
            if( seller==null ){// 新核准
                seller = new EcSeller();
                seller.setDisabled(false);
                seller.setMemberId(member.getId());
                seller.setApproveTime(new Date());
                seller.setApprover(operator.getId());
                sellerFacade.save(seller, operator, simulated);
                logger.debug("saveVO seller.getId() = "+seller.getId());
            }else if( newApproveSeller ){// 重新核准
                seller.setDisabled(false);
                seller.setApproveTime(new Date());
                seller.setApprover(operator.getId());
                sellerFacade.save(seller, operator, simulated);
                logger.debug("saveVO seller.getId() = "+seller.getId());
            }
            vo.setSellerId(seller.getId());// for new
            
            // for EC_STORE
            EcStore store = storeFacade.findBySellerId(seller.getId());
            if( store==null ){
                // 首次建立，部分欄位自會員註冊資料 copy
                store = storeFacade.createNewStore(vo, null, operator, locale, true, sys.isTrue(vo.getTccDealer()), simulated);
                //vo.setStoreOwner(true);// 原申請人
                //vo.setFiUser(true);// 有結案權
            }
            vo.setStoreId(store!=null?store.getId():null);// for new
        }
        
        // for Store多人管理 EC_STORE_USER
        if( vo.getStoreId()!=null ){
            EcStoreUser entity = storeUserFacade.findByKey(vo.getStoreId(), vo.getMemberId());
            if( entity==null ){
                entity = new EcStoreUser();
                entity.setDisabled(false);
                entity.setMemberId(vo.getMemberId());
                entity.setStoreId(vo.getStoreId());
                entity.setOwner(vo.getStoreOwner()==null?false:vo.getStoreOwner());
                entity.setFiUser(vo.getFiUser()==null?false:vo.getFiUser());
                storeUserFacade.save(entity, operator, simulated);
            }
        }
        
        // 取消核准處理
        if( sys.isFalse(vo.getSellerApply()) && vo.getSellerId()!=null ){
            // 賣家資格取消
            EcSeller seller = sellerFacade.find(vo.getSellerId());
            seller.setDisabled(true);
            sellerFacade.save(seller, operator, simulated);
            // 其商店需 CLOSE
            if( vo.getSellerId()!=null ){
                storeFacade.closeAllBySeller(vo.getSellerId(), operator, simulated);
            }
        }
    }
    
    /**
     * for EC1.5 同公司(經銷商)匯入帳號
     * @param storeId
     * @return 
     */
    public boolean decideStoreOwner(Long storeId, EcMember operator, Locale locale, boolean simulated){
        if( simulated ){
            logger.info("decideStoreOwner simulated = "+simulated);
            return true;
        }
        if( storeId==null ){
            logger.error("decideStoreOwner storeId==null");
            return false;
        }
        EcStore store = storeFacade.find(storeId);
        if( store==null ){
            logger.error("decideStoreOwner store==null, storeId ="+storeId);
            return false;
        }
        
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setManageStoreId(storeId);
        criteriaVO.setDisabled(false);
        criteriaVO.setOrderBy("S.CREATETIME", "");// 最先立優先
        List<MemberVO> list = findByCriteria(criteriaVO, locale);
        
        if( sys.isEmpty(list) ){
            logger.error("decideStoreOwner list==null, storeId = "+storeId);
            return false;
        }
        
        // 先找出所有 StoreOwner
        List<MemberVO> storeOwnerList = new ArrayList<MemberVO>();
        for(MemberVO vo : list){
            if( sys.isTrue(vo.getStoreOwner()) ){
                storeOwnerList.add(vo);
                logger.info("decideStoreOwner owner id = "+vo.getMemberId());
            }
        }
        
        logger.info("decideStoreOwner storeId="+storeId+", storeOwnerCount=="+storeOwnerList.size());
        
        if( storeOwnerList.size()==1 ){
            return true;
        }else if( storeOwnerList.isEmpty() ){// 無 Owner，取所有管理員第一筆為 Owner
            EcStoreUser entity = new EcStoreUser();
            entity.setDisabled(false);
            entity.setMemberId(list.get(0).getMemberId());
            entity.setStoreId(list.get(0).getStoreId());
            entity.setOwner(true);
            entity.setFiUser(true);
            storeUserFacade.save(entity, operator, simulated);
            logger.info("decideStoreOwner save EcStoreUser = "+entity.getId());
            
            EcSeller seller = sellerFacade.find(store.getSellerId());
            seller.setMemberId(list.get(0).getMemberId());
            sellerFacade.save(seller, operator, simulated);
            logger.info("decideStoreOwner save EcSeller = "+seller.getId());
        }else if( storeOwnerList.size()>=2 ){
            boolean first = true;
            for(MemberVO vo : storeOwnerList){
                EcStoreUser entity = storeUserFacade.findByKey(storeId, vo.getMemberId());
                if( !first ){// 第一筆以外，改為非 store owner
                    entity.setOwner(false);
                    storeUserFacade.save(entity, operator, simulated);
                    logger.info("decideStoreOwner save EcStoreUser = "+entity.getId());
                }
                first = false;
            }
            
            EcSeller seller = sellerFacade.find(store.getSellerId());
            seller.setMemberId(storeOwnerList.get(0).getMemberId());
            sellerFacade.save(seller, operator, simulated);
            logger.info("decideStoreOwner save EcSeller = "+seller.getId());
        }
        
        return true;
    }
    
    /**
     * 查詢
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<MemberVO> findByCriteria(MemberCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.ID MEMBER_ID, S.TYPE MEMBER_TYPE, S.TYPE MEMBER_TYPE_ORI \n");
        sql.append(", S.LOGIN_ACCOUNT, S.NAME, S.EMAIL, S.PHONE, S.ACTIVE, S.DISABLED \n");
        sql.append(", S.SELLER_APPLY, S.SELLER_APPROVE, S.APPROVETIME, S.ADMIN_USER \n");
        sql.append(", S.COM_APPLY, S.COM_APPROVE, S.COM_APPROVETIME, S.COM_APPLYTIME \n");
        sql.append(", S.TCC_DEALER, S.TCC_DS \n");
        sql.append(", D.* \n"); // EC_COMPANY or EC_PERSON
        sql.append(", SR.ID SELLER_ID, SU.STORE_ID \n");
        sql.append(", ST.CNAME STORE_CNAME, ST.ENAME STORE_ENAME \n");
        sql.append(", F.ID PIC_ID \n");
        //if( criteriaVO.getStoreManager()!=null && criteriaVO.getStoreManager() ){
        sql.append(", SU.ID MANAGER_ID, SU.OWNER STORE_OWNER, SU.FI_USER \n");
        //}
        if( criteriaVO.isFullData() ){
            sql.append(", NVL(O.COUNTS,0) COUNTS, NVL(O.TOTAL_AMT,0) TOTAL_AMT, O.lastBuyDate \n");
        }
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.NAME");
        }
        
        List<MemberVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(MemberVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(MemberVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(MemberVO.class, sql.toString(), params);
        }
        
        if( list!=null ){
            for(MemberVO vo : list){
                vo.genTypeName(locale);
                if( criteriaVO.isFullData() ){
                    // picture
                    FileVO pic = fileFacade.findSingleByPrimary(FileEnum.MEMBER_PIC.getCode(), vo.getMemberId());
                    vo.setPic(pic);
                }
            }
        }
        return list;
    }
    
    /**
     * 依輸入條件查詢
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(MemberCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("FROM EC_MEMBER S \n");
        // 商店管理員
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append(getMemberDetailInfoSQL());
        sql.append(") D ON D.TYPE='").append(PersonTypeEnum.MEMBER.getCode()).append("' AND D.MEM_TYPE=S.TYPE AND D.MAIN_ID=S.ID \n");
        sql.append("LEFT OUTER JOIN EC_SELLER SR ON SR.MEMBER_ID=S.ID AND S.DISABLED=0 \n");// 關聯有效會員、核准賣家
        sql.append("LEFT OUTER JOIN EC_STORE ST ON ST.SELLER_ID=SR.ID AND ST.DEF_STORE=1 \n");// 預設商店
        
        if( criteriaVO.getManageStoreId()!=null ){// 指定管理商店
            sql.append("JOIN EC_STORE_USER SU ON SU.MEMBER_ID=S.ID AND SU.STORE_ID=#MAN_STORE_ID AND SU.DISABLED=0 \n");// 商店管理員
            params.put("MAN_STORE_ID", criteriaVO.getManageStoreId());
        }else{
            sql.append("LEFT OUTER JOIN EC_STORE_USER SU ON SU.MEMBER_ID=S.ID AND SU.STORE_ID=ST.ID AND SU.DISABLED=0 \n");
        }
        sql.append("LEFT OUTER JOIN EC_FILE F ON F.PRIMARY_TYPE=#PRIMARY_TYPE AND F.PRIMARY_ID=S.ID \n");// 執照/證件圖
        params.put("PRIMARY_TYPE", FileEnum.MEMBER_PIC.getPrimaryType());
        
        // 商店管理員
        //sql.append("LEFT OUTER JOIN EC_STORE_USER SU ON (SU.STORE_ID=ST.ID OR SU.STORE_ID=#MAN_STORE_ID) AND SU.MEMBER_ID=S.ID AND SU.DISABLED=0 \n");
        //params.put("MAN_STORE_ID", criteriaVO.getManageStoreId()!=null?criteriaVO.getManageStoreId():0L);
        
        // 已成交訂單
        if( criteriaVO.isFullData() ){
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("    SELECT MEMBER_ID, COUNT(ID) COUNTS, SUM(TOTAL) TOTAL_AMT, MAX(APPROVETIME) lastBuyDate \n"); 
            sql.append("    FROM EC_ORDER \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    AND STATUS IN (").append(OrderStatusEnum.getCanCountListStr()).append(") \n");// 賣家結案
            sql.append("    GROUP BY MEMBER_ID \n");
            sql.append(") O ON O.MEMBER_ID=S.ID \n");
        }
        
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");

        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getActive()!=null ){
            sql.append("AND S.ACTIVE=#ACTIVE \n");
            params.put("ACTIVE", criteriaVO.getActive());
        }        
        if( criteriaVO.getLoginAccount()!=null ){
            if( GlobalConstant.ACC_CASE_SENSITIVE ){
                sql.append("AND S.LOGIN_ACCOUNT=#LOGIN_ACCOUNT \n");
            }else{
                sql.append("AND LOWER(S.LOGIN_ACCOUNT)=LOWER(#LOGIN_ACCOUNT) \n");
            }
            params.put("LOGIN_ACCOUNT", criteriaVO.getLoginAccount());
        }
        if( criteriaVO.getName()!=null ){
            sql.append("AND S.NAME=#NAME \n");
            params.put("NAME", criteriaVO.getName());
        }
        if( criteriaVO.getEncryptedPwd()!=null ){
            sql.append("AND S.PASSWORD=#PASSWORD \n");
            params.put("PASSWORD", criteriaVO.getEncryptedPwd());
        }
        if( criteriaVO.getSellerId()!=null ){
            sql.append("AND SR.ID=#SELLER_ID \n");
            params.put("SELLER_ID", criteriaVO.getSellerId());
        }
        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND ST.ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        /*if( criteriaVO.getManageStoreId()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT ID FROM EC_STORE_USER WHERE MEMBER_ID=S.ID AND STORE_ID=#MAN_STORE_ID \n");
            sql.append(") \n");
            params.put("MAN_STORE_ID", criteriaVO.getManageStoreId());
        }*/
        // 身分別查詢條件 AND or OR
        if( criteriaVO.getIdentityUnion()!=null && criteriaVO.getIdentityUnion() ){// 聯集
            if( (criteriaVO.getSellerApply()!=null && criteriaVO.getSellerApply()) 
             || (criteriaVO.getSellerApprove()!=null)
             || (criteriaVO.getTccDealer()!=null)
             || (criteriaVO.getTccDs()!=null)
             || (criteriaVO.getAdminUser()!=null) ){
                sql.append("AND (1=2 \n");
                if( criteriaVO.getSellerApply()!=null && criteriaVO.getSellerApply() ){// 申請成為賣家中
                    sql.append("OR (S.SELLER_APPLY=1 AND S.SELLER_APPROVE=0) \n");
                }
                if( criteriaVO.getSellerApprove()!=null ){
                    sql.append("OR S.SELLER_APPROVE=#SELLER_APPROVE \n");
                    params.put("SELLER_APPROVE", criteriaVO.getSellerApprove());
                }
                if( criteriaVO.getTccDealer()!=null ){// TCC經銷商
                    sql.append("OR S.TCC_DEALER=#TCC_DEALER \n");
                    params.put("TCC_DEALER", criteriaVO.getTccDealer());
                }
                if( criteriaVO.getTccDs()!=null ){// TCC經銷商下游客戶
                    sql.append("OR S.TCC_DS=#TCC_DS \n");
                    params.put("TCC_DS", criteriaVO.getTccDs());
                }
                if( criteriaVO.getAdminUser()!=null ){// 系統管理員
                    sql.append("OR S.ADMIN_USER=#ADMIN_USER \n");
                    params.put("ADMIN_USER", criteriaVO.getAdminUser());
                }
                sql.append(") \n");
            }
        }else{// 交集
            if( criteriaVO.getSellerApply()!=null && criteriaVO.getSellerApply() ){// 申請成為賣家中
                sql.append("AND S.SELLER_APPLY=1 AND S.SELLER_APPROVE=0 \n");
            }
            if( criteriaVO.getSellerApprove()!=null ){
                sql.append("AND S.SELLER_APPROVE=#SELLER_APPROVE \n");
                params.put("SELLER_APPROVE", criteriaVO.getSellerApprove());
            }
            if( criteriaVO.getTccDealer()!=null ){// TCC經銷商
                sql.append("AND S.TCC_DEALER=#TCC_DEALER \n");
                params.put("TCC_DEALER", criteriaVO.getTccDealer());
            }
            if( criteriaVO.getTccDs()!=null ){// TCC經銷商下游客戶
                sql.append("AND S.TCC_DS=#TCC_DS \n");
                params.put("TCC_DS", criteriaVO.getTccDs());
            }
            if( criteriaVO.getAdminUser()!=null ){// 系統管理員
                sql.append("AND S.ADMIN_USER=#ADMIN_USER \n");
                params.put("ADMIN_USER", criteriaVO.getAdminUser());
            }
        }
        
        if( criteriaVO.getDealerId()!=null ){// 依經銷商, 找下游客戶
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT TCC.* \n");
            sql.append("    FROM EC_TCC_DEALER_DS TCC \n");
            sql.append("    WHERE TCC.DEALER_ID=#DEALER_ID AND TCC.DS_ID=S.ID \n");
            sql.append(") \n");
            params.put("DEALER_ID", criteriaVO.getDealerId());
        }
        if( criteriaVO.getDsId()!=null ){// 依下游客戶, 找經銷商
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT TCC.* \n");
            sql.append("    FROM EC_TCC_DEALER_DS TCC \n");
            sql.append("    WHERE TCC.DS_ID=#DS_ID AND TCC.DEALER_ID=S.ID \n");
            sql.append(") \n");
            params.put("DS_ID", criteriaVO.getDsId());
        }

        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("S.LOGIN_ACCOUNT LIKE #KW OR S.NAME LIKE #KW \n");
            sql.append(") \n");
            params.put("KW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getTelKeyword()) ){
            String kw = "%" + criteriaVO.getTelKeyword().trim() + "%";
            sql.append("AND S.PHONE LIKE #TELKW \n");
            params.put("TELKW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getEmailKeyword()) ){
            String kw = "%" + criteriaVO.getEmailKeyword().trim() + "%";
            sql.append("AND S.EMAIL LIKE #EMAILKW \n");
            params.put("EMAILKW", kw);
        }

        if( criteriaVO.getHasPic()!=null ){// 有無上傳照片
            if( criteriaVO.getHasPic() ){
                sql.append("AND F.ID IS NOT NULL \n");
            }else{
                sql.append("AND F.ID IS NULL \n");
            }
        }
                
        return sql.toString();
    }
    
    public int countByCriteria(MemberCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    
    /**
     * 帳/密登入驗證
     * @param account
     * @param encrypted
     * @param locale
     * @return 
     */
    public MemberVO findForLogin(String account, String encrypted, Locale locale) {
        logger.debug("findForLogin account = "+account);
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setLoginAccount(account);
        criteriaVO.setEncryptedPwd(encrypted);
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setFullData(false);
        
        List<MemberVO> list = findByCriteria(criteriaVO, locale);
        logger.info("findForLogin list ="+(list!=null?list.size():0));
        MemberVO vo = (list!=null && list.size()==1)? list.get(0):null;
        
        // 可管理商店 (包含自己註冊商店 及 被加入管理者的商店)
        if( vo!=null ){
            storeFacade.findByMemberStoreOptions(vo, true);
        }
        return vo;
    }

    public MemberVO findById(Long id, boolean fullData, Locale locale) {
        if( id==null ){
            logger.error("findById error id="+id);
            return null;
        }
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<MemberVO> list = findByCriteria(criteriaVO, locale);
        
        MemberVO vo = (list!=null && !list.isEmpty() && list.get(0).getMemberId().equals(id))? list.get(0):null;
        if( vo!=null ){
            FileVO pic = fileFacade.findSingleByPrimary(GlobalConstant.SHARE_STORE_ID, FileEnum.MEMBER_PIC.getCode(), vo.getId());
            vo.setPicture(pic);
            // 指定會員的商店 // 可管理商店 (1.包含自己註冊商店 及 2.被加入管理者的商店)
            storeFacade.findByMemberStoreOptions(vo, true);
        }
        
        return vo;
    }
    
    /**
     * 登入帳號
     * @param loginAccount
     * @return 
     */
    public EcMember findByLoginAccount(String loginAccount){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loginAccount", loginAccount);
        List<EcMember> list = this.findByNamedQuery("EcMember.findByLoginAccount", params);
        
        for(EcMember member : list){
            if( member.getDisabled()!=null && !member.getDisabled() ){
                return member;
            }
        }
        return null;
    }
    /**
     * for
     * 1. 供手機已登入賣家平台時
     * 2. 會員匯入檢查
     * 3. 新增商店管理員
     * @param account
     * @param locale
     * @return 
     */
    public MemberVO findVOByLoginAccount(String account, Locale locale) {
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setLoginAccount(account);
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setFullData(false);
        
        List<MemberVO> list = findByCriteria(criteriaVO, locale);
        logger.debug("findVOByLoginAccount list ="+(list!=null?list.size():0));
        MemberVO vo = (list!=null && list.size()==1)? list.get(0):null;
        
        // 可管理商店 (包含自己註冊商店 及 被加入管理者的商店)
        if( vo!=null ){
            storeFacade.findByMemberStoreOptions(vo, true);
        }
        return vo;
    }
    
    /**
     * for 商店管理員
     * @param account
     * @param locale
     * @return 
     */
    public MemberVO findByStoreManager(Long storeId, String account, String name, Locale locale) {
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setStoreManager(Boolean.TRUE);
        criteriaVO.setManageStoreId(storeId);
        criteriaVO.setLoginAccount(account);
        criteriaVO.setName(name);
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setDisabled(Boolean.FALSE);
        criteriaVO.setFullData(false);
        
        List<MemberVO> list = findByCriteria(criteriaVO, locale);
        logger.debug("findByStoreManager list ="+(list!=null?list.size():0));
        return (list!=null && list.size()==1)? list.get(0):null;
    }
    
    /**
     * 輸入檢查
     * @param vo
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(MemberVO vo, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, "memberId", locale, errors);

        return pass;
    }

    /**
     * TCC會員匯入
     * @param filename
     * @param contentType
     * @param autoAddClass
     * @param locale
     * @param memType
     * @param resVO
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException 
     */
    public void importTccMembers(String filename, String contentType, boolean autoAddClass, 
            Locale locale, TccMemberEnum memType, ImportResultVO resVO)
            throws FileNotFoundException, IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException{
        logger.debug("importTccMembers ...");
        
        resVO.setCanImport(false);
        // 產業別
        Map<String, Long> categoryMap = optionFacade.findForNameMap(null, OptionEnum.INDUSTRY.getCode());
        categoryMap = (categoryMap==null)?new HashMap<String, Long>():categoryMap;
        // 所在區域
        Map<String, Long> areaMap = optionFacade.findForNameMap(null, OptionEnum.SALES_AREA.getCode());
        areaMap = (areaMap==null)?new HashMap<String, Long>():areaMap;
        
        FileInputStream fileStream = new FileInputStream(filename);
        
        int sheetIndex = 0;
        String msg = "";
        if( ImportUtils.checkExcelContentType(contentType) ){
            // Excel 資料轉入 impList
            Class clazz = (TccMemberEnum.DOWNSTREAM==memType)?ImportTccDsVO.class:ImportTccDealerVO.class;
            int res = AnnotationImportUtils.importTccMembers(fileStream, contentType, sheetIndex, locale, resVO.getResList(), clazz);
            logger.info("importTccMembers res="+res+", resList="+resVO.getResList().size());
            // 下游客戶
            Map<String, Long> codeIdMap = null;
             if ( TccMemberEnum.DOWNSTREAM==memType ){
                 codeIdMap =  findDealerCodeIdMap(locale);
                 logger.info("importTccMembers codeIdMap="+(codeIdMap!=null?codeIdMap.size():0));
             }
            
            if( res>0 ){
                int total = 0, success = 0, fail = 0, noData = 0;
                // 移除最後全空欄位
                removeLastEmptyRows(resVO.getResList());
                // impList 資料檢核
                for(Object obj : resVO.getResList()){
                    ImportTcc vo = (ImportTcc)obj;
                    logger.debug("importTccMembers row = "+total+", vo.isHasError() = "+vo.isHasError());
                    //vo.setStoreId(storeId);// 必要，for key 重複判斷
                    // 產業別
                    vo.setCategoryName(vo.getCategoryName()!=null?vo.getCategoryName().trim():null);
                    vo.setCategory(vo.getCategoryName()!=null?categoryMap.get(vo.getCategoryName().toUpperCase()):null);
                    logger.debug("importTccMembers getCategory = "+vo.getCategory());
                    // 所在區域
                    vo.setStateName(vo.getStateName()!=null?vo.getStateName().trim():null);
                    vo.setState(vo.getStateName()!=null?areaMap.get(vo.getStateName().toUpperCase()):null);
                    logger.debug("importTccMembers getState = "+vo.getState());
                    
                    checkSingleImportData(vo, autoAddClass, resVO.getResList(), locale, memType);// 單筆匯入檢查

                    // 下游客戶
                    if ( TccMemberEnum.DOWNSTREAM==memType ){
                        boolean dealerError = true;
                        if( codeIdMap!=null ){
                            ImportTccDsVO dsVO = (ImportTccDsVO)obj;
                            if( StringUtils.isNotBlank(dsVO.getDealerCode()) ){
                                Long dealerId = codeIdMap.get(dsVO.getDealerCode().trim().toUpperCase());
                                if( dealerId!=null ){
                                    dsVO.setDealerId(dealerId);
                                    dealerError = false;
                                }
                            }
                            logger.debug("importTccMembers DealerCode="+dsVO.getDealerCode()+", DealerId="+dsVO.getDealerId());
                        }
                        if( dealerError ){
                            vo.setHasError(true);
                            // [所属经销商统一社会信用代码]輸入錯誤!
                            vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "err.txt001"));
                        }
                    }
                    
                    logger.debug("importTccMembers row = "+total+", vo.isHasError() = "+vo.isHasError());
                    if( vo.isHasError() ){
                        vo.genErrMsgs(locale);// 產生錯誤訊息彙總字串 for UI
                        resVO.getErrList().add(vo);
                        fail++;
                    }else if( !vo.isHasData() ){
                        noData++;
                    }else{
                        success++;
                    }
                    total++;
                }
                
                // 共 3 筆資料 (正確 3 筆資料，錯誤 0 筆。)
                StringBuilder sb = new StringBuilder()
                        .append(ResourceBundleUtils.getMessage(locale, "total.row"))
                        .append(" ").append(total).append(" ").append(ResourceBundleUtils.getMessage(locale, "row.data"))
                        .append(" (").append(ResourceBundleUtils.getMessage(locale, "right"))
                        .append(" ").append(success).append(" ").append(ResourceBundleUtils.getMessage(locale, "counts"))
                        .append("，").append(ResourceBundleUtils.getMessage(locale, "error"))
                        .append(" ").append(fail).append(" ").append(ResourceBundleUtils.getMessage(locale, "counts"));
                if( noData>0 ){
                    sb.append("，").append(ResourceBundleUtils.getMessage(locale, "nodata")).append(" ").append(noData).append(" ")
                      .append(ResourceBundleUtils.getMessage(locale, "counts")); 
                }
                sb.append("。)");
                
                msg = sb.toString();
                resVO.setCanImport(success>0 && fail<=0);
            }else{
                // 匯入檔案格式錯誤!  上傳EXCEL不可超過筆資料!
                msg = (res<0)?ResourceBundleUtils.getMessage(locale, "imp.file.err")
                        :ResourceBundleUtils.getMessage(locale, "upload.limit")
                        +GlobalConstant.MAX_IMPORT_NUM
                        +ResourceBundleUtils.getMessage(locale, "row.data");
            }
        }else{
            // 上傳檔非EXCEL格式!
            msg = ResourceBundleUtils.getMessage(locale, "upload.file.err");//+" (contentType="+contentType+")";
            logger.error("importProducts msg = "+ msg);
            logger.error("importProducts contentType = "+ contentType);
        }
        resVO.setResultMsg(msg);
    }
   
    /**
     * 移除最後全空欄位
     * @param impList
     */
    public void removeLastEmptyRows(List impList){
        if( impList==null || impList.isEmpty() ){
            logger.error("removeLastEmptyRows ... no data !");
            return;
        }
        
        List<Integer> allEmptyRows = new ArrayList<Integer>(); // 所有欄位接空白
        
        for(int i=impList.size()-1; i>=0; i--){// 反向順序刪除
            ImportTcc rowVO = (ImportTcc)impList.get(i);
            // DataKey、全部空白 檢查
            if( !rowVO.isHasData() ){
                // 避免多出的空白列 EXCEL 認定非空，但實際沒填入文字的資料列。若欄位資料全空，一律忽略，不視為錯誤。
                allEmptyRows.add(i);
                logger.debug("removeLastEmptyRows allEmptyRows id = {}", i);
            }else{
                break;
            }
        }// end of for
        
        // 移除 EXCEL 認定非空，但實際沒填入文字的資料列
        if( !allEmptyRows.isEmpty() ){
            for(int i=0; i<allEmptyRows.size(); i++){
                Integer id = allEmptyRows.get(i);
                logger.debug("removeLastEmptyRows remove id = {}", id);
                if( id == impList.size()-1 ){
                    impList.remove(id.intValue());
                }else{
                    break; // 只刪在最後的空白列
                }
            }
        }
    }
    
    /**
     * 單筆匯入檢查
     * @param vo
     * @param autoAddClass
     * @param impList
     * @param locale
     * @return 
     */
    public boolean checkSingleImportData(ImportTcc vo, boolean autoAddClass, 
            List<ImportTcc> impList, Locale locale, TccMemberEnum memType){
        logger.debug("checkSingleImportData ...");
        
        List<String> errors = new ArrayList<String>();
        
        MemberVO newVO = null;
        //if( memType==TccMemberEnum.DOWNSTREAM ){// 匯入台泥經銷商下游客戶(攪拌站、檔口)可能已存在，避免帳號重複檢核
        //    newVO = this.findVOByLoginAccount(vo.getLoginAccount(), locale);
        //}
        newVO = this.findVOByLoginAccount(vo.getLoginAccount(), locale);// 經銷商、下游客戶 可能都已存在(已存在做update)
        if( newVO==null ){
            newVO = new MemberVO();
        }
        /*else if( memType==TccMemberEnum.DOWNSTREAM || newVO.getTccDs()==null || !newVO.getTccDs() ){// 須為台泥經銷商下游客戶(攪拌站、檔口)
            // 此帳號已存在，但非台泥經銷商下游客戶
            vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "exists.not.ds"));
            vo.setHasError(true);
        }*/
        
        // 下游客戶
        if( TccMemberEnum.DOWNSTREAM==memType ){
            vo.setCname(vo.getName());// 公司名稱 同 會員名稱
            if( vo.getPhone()==null ){
                vo.setPhone(vo.getLoginAccount());// 電話當帳號
            }
        }

        ExtBeanUtils.copyProperties(newVO, vo);
        if( !checkInput(newVO, null, locale, errors) ){
            vo.getErrList().addAll(errors);
            vo.setHasError(true);
        }

        // 匯入時才有的檢查        
        // 同一份 EXCEL 出現相同 [名稱]，報錯不匯入。
        for(ImportTcc impVo : impList){
            if( vo.getName()!=null
                    && vo.getName().equals(impVo.getName())
                    && vo.getRowNum()!=impVo.getRowNum() ){
                vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "same.tcc.name")
                        +impVo.getRowNum()+ResourceBundleUtils.getMessage(locale, "rows"));
                vo.setHasError(true);
                break;
            }
        }
        // 同一份 EXCEL 出現相同 [帳號]，報錯不匯入。
        for(ImportTcc impVo : impList){
            if( vo.getLoginAccount()!=null
                    && vo.getLoginAccount().equals(impVo.getLoginAccount())
                    && vo.getRowNum()!=impVo.getRowNum() ){
                vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "same.tcc.code")
                        +impVo.getRowNum()+ResourceBundleUtils.getMessage(locale, "rows"));
                vo.setHasError(true);
                break;
            }
        }
        
        return !vo.isHasError();
    }

    /**
     * 台泥經銷商選單
     * 
     * @param locale
     * @param opLang
     * @return 
     */
    public List<LongOptionVO> findTccDealerOptions(Locale locale, String opLang) {
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setTccDealer(Boolean.TRUE);
        criteriaVO.setSellerApprove(Boolean.TRUE);// 只取是賣家的帳號 
        criteriaVO.setDisabled(Boolean.FALSE);
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setFullData(false);
        if( "E".equals(opLang) ){
            criteriaVO.setOrderBy("NVL(S.ENAME, S.NAME)");
        }else{
            criteriaVO.setOrderBy("S.NAME");
        }
        
        List<MemberVO> list = findByCriteria(criteriaVO, locale);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        for(MemberVO vo : list){
            StringBuilder label = new StringBuilder();
            // use cname or ename by opLang (C/E)
            String name = "E".equals(opLang)?(StringUtils.isBlank(vo.getEname())?vo.getName():vo.getEname()):vo.getName();
            label.append("[").append(vo.getMemberId()).append("]")
                 .append(name!=null?name:"");
            ops.add(new LongOptionVO(vo.getMemberId(), label.toString()));
        }
        return ops;
    }

    /**
     * get TCC Dealer Code Id Map
     * @return 
     */
    private Map<String, Long> findDealerCodeIdMap(Locale locale) {
        logger.debug("findDealerCodeIdMap ...");
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setTccDealer(Boolean.TRUE);// tcc dealer
        criteriaVO.setSellerApprove(Boolean.TRUE);// 賣家
        criteriaVO.setDisabled(Boolean.FALSE);
        criteriaVO.setActive(Boolean.TRUE);
        criteriaVO.setFullData(false);

        List<MemberVO> list = findByCriteria(criteriaVO, locale);
        if( list!=null ){
            Map<String, Long> map = new HashMap<String, Long>();
            for(MemberVO vo : list){
                if( StringUtils.isNotBlank(vo.getIdCode()) ){
                    map.put(vo.getIdCode().trim().toUpperCase(), vo.getMemberId());
                }
            }
            return map;
        }
        
        return null;
    }

    /**
     * 同步 EC1.0 密碼 & Active
     * @param loginAccount
     * @return 
     */
    public int syncPasswordFromEc10(String loginAccount) {
        // 不要因同步問題影響登入，使用 try catch
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            StringBuilder sql = new StringBuilder();
            sql.append("BEGIN \n");

            sql.append("MERGE INTO EC_MEMBER D \n");
            sql.append("USING ( \n");
            sql.append("     SELECT E10.ID, E10.LOGIN_ACCOUNT, E10.NAME, E10.EMAIL, E10.PHONE, E10.PASSWORD, E10.ACTIVE \n");
            sql.append("     FROM TCCSTORE_USER.EC_MEMBER E10 \n");
            // 針對經銷商
            sql.append("     JOIN EC_MEMBER M ON UPPER(M.LOGIN_ACCOUNT)=UPPER(E10.LOGIN_ACCOUNT) AND M.TCC_DEALER=1 \n");
            //sql.append("     -- WHERE M.VERIFY_CODE>1 \n");
            sql.append("     WHERE UPPER(E10.LOGIN_ACCOUNT) = UPPER(#LOGIN_ACCOUNT) \n");
            sql.append(") S ON (UPPER(S.LOGIN_ACCOUNT)=UPPER(D.LOGIN_ACCOUNT)) \n");
            sql.append("WHEN MATCHED THEN \n");
            sql.append("UPDATE SET D.PASSWORD=S.PASSWORD, D.ACTIVE=S.ACTIVE, D.VERIFY_CODE=TO_CHAR(SYSDATE, 'yyyyMMddHH24MISS'); \n");

            sql.append("END; \n");

            params.put("LOGIN_ACCOUNT", loginAccount);

            logger.debug("syncPasswordFromEc10 sql =\n"+sql.toString());
            Query q = em.createNativeQuery(sql.toString());
            setParamsToQuery("syncPasswordFromEc10", params, q);

            return q.executeUpdate();
        } catch (Exception e) {
            sys.processUnknowException(null, "syncPasswordFromEc10", e);
        }
        logger.error("syncPasswordFromEc10 error loginAccount = " + loginAccount);
        return -1;
    }

    /**
     * for ec-amin link to ec-seller
     * @param loginAccount
     * @param sessionKey
     * @param locale
     * @return 
     */
    public MemberVO findForInternalLogin(String loginAccount, String sessionKey, Locale locale) {
        logger.debug("findForInternalLogin loginAccount = "+loginAccount+", sessionKey = "+sessionKey);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) CC \n");
        sql.append("FROM EC_MEMBER M \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND M.LOGIN_ACCOUNT=#LOGIN_ACCOUNT \n");
        sql.append("AND M.DISABLED=0 \n");
        sql.append("AND M.ACTIVE=1 \n");
        sql.append("AND M.ADMIN_USER=1 \n");
        sql.append("AND EXISTS ( \n");
        sql.append("    SELECT *  \n");
        sql.append("    FROM EC_SESSION S \n");
        sql.append("    WHERE S.MEMBER_ID=M.ID \n");
        sql.append("    AND S.SESSION_KEY=#SESSION_KEY \n");
        sql.append("    AND S.EXP_TIME>=#EXP_TIME \n");
        sql.append(") \n");
        
        params.put("LOGIN_ACCOUNT", loginAccount);
        params.put("SESSION_KEY", GlobalConstant.INTERNAL_PREFIX_SK+sessionKey);
        params.put("EXP_TIME", new Date());

        int count = this.count(sql.toString(), params);
        if( count>0 ){
            return this.findVOByLoginAccount(loginAccount, locale);
        }
        return null;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.ec.model.FileVO;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcCompany;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPerson;
import com.tcci.ec.entity.EcSeller;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.entity.EcStoreUser;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.PersonTypeEnum;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.criteria.MemberCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.StringUtils;
import com.tcci.security.AESPasswordHash;
import com.tcci.security.AESPasswordHashImpl;
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
    @EJB EcMemberFactoryFacade memberFactoryFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcMemberFacade() {
        super(EcMember.class);
    }
    
    public void remove(MemberVO vo) {       
        EcMember member = findByVO(vo);
        
        if( member!=null ){
            this.remove(member);
        }
    }

    public EcMember findByVO(MemberVO vo){
        EcMember entity = this.find(vo.getId());
        return entity;
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
        
        // 注意原本 NULL 狀況
        // 申請賣家
        boolean newApplySeller = (!sys.isTrue(member.getSellerApply())) && sys.isTrue(vo.getSellerApply());
        // 核准賣家
        boolean newApproveSeller = (!sys.isTrue(member.getSellerApprove())) && sys.isTrue(vo.getSellerApprove());
        vo.setApplytime(newApplySeller?new Date():member.getApplytime());
        vo.setApprovetime(newApproveSeller?new Date():member.getApprovetime());
        // 申請轉公司戶
        boolean newApplyCom= (!sys.isTrue(member.getComApply())) && sys.isTrue(vo.getComApply());
        // 核准公司戶
        boolean newApproveCom = (!sys.isTrue(member.getComApprove())) && sys.isTrue(vo.getComApprove());

        vo.setComApplytime(newApplyCom?new Date():member.getComApplytime());
        vo.setComApprovetime(newApproveCom?new Date():member.getComApprovetime());
        
        ExtBeanUtils.copyProperties(member, vo);
        logger.debug("saveVO MemberType="+vo.getMemberType()+", Type="+vo.getType());
        member.setType(vo.getMemberType());
        member.setDisabled(false); 
        this.save(member, operator, simulated);// save EC_MEMBER
        logger.debug("saveVO member.getId() = "+member.getId());
        vo.setMemberId(member.getId());// for new
        // for 自行註冊 或 ec-admin同步
        operator = (operator==null)?new EcMember(member.getId()):operator;

        // for 詳細資料 (EC_COMPANY or EC_PERSON)
        // 詳細資料
        vo.setId(detailId);// 一定要詳細資料 copyProperties 前復原
        if( MemberTypeEnum.COMPANY.getCode().equals(vo.getMemberType()) ){
            EcCompany company = (detailId!=null && detailId>0)?companyFacade.find(detailId):new EcCompany();
            logger.debug("saveVO company.getId()="+company.getId()+", detailId="+detailId);
            ExtBeanUtils.copyProperties(company, vo);
            company.setType(CompanyTypeEnum.MEMBER.getCode());
            company.setMainId(member.getId());
            companyFacade.save(company, operator, simulated);// save EC_COMPANY
            detailId = company.getId();
            logger.debug("saveVO company.getId() = "+company.getId());
        }else{
            EcPerson person = (detailId!=null && detailId>0)?personFacade.find(detailId):new EcPerson();
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
        if( sys.isTrue(vo.getSellerApply()) ){
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
        if( sys.isValidId(vo.getStoreId()) ){
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
        if( sys.isFalse(vo.getSellerApply()) && sys.isValidId(vo.getSellerId()) ){
            // 賣家資格取消
            EcSeller seller = sellerFacade.find(vo.getSellerId());
            seller.setDisabled(true);
            sellerFacade.save(seller, operator, simulated);
            // 其商店需 CLOSE
            if( sys.isValidId(vo.getSellerId()) ){
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
        sql.append(", DEALER_NAMES \n");
        sql.append(", MF.FACTORY_ID \n");
        
        //}
        if( criteriaVO.isFullData() ){
            sql.append(", NVL(O.COUNTS,0) COUNTS, NVL(O.TOTAL_AMT,0) TOTAL_AMT, O.lastBuyDate \n");
        }
        sql.append(", MC.ID CREATOR_ID, MC.LOGIN_ACCOUNT CREATOR_ACCOUNT, MC.NAME CREATOR_NAME \n");
        sql.append(", MM.ID MODIFIER_ID, MM.LOGIN_ACCOUNT MODIFIER_ACCOUNT, MM.NAME MODIFIER_NAME \n");
        
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
        
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT R.DS_ID, LISTAGG(TO_CHAR(C.NICKNAME), ', ') WITHIN GROUP (ORDER BY C.NICKNAME) DEALER_NAMES \n");
        sql.append("    FROM EC_TCC_DEALER_DS R \n");
        sql.append("    JOIN EC_MEMBER M ON M.ID=R.DEALER_ID AND M.TCC_DEALER=1 AND M.DISABLED=0 \n");
        sql.append("    JOIN EC_COMPANY C ON C.TYPE='").append(CompanyTypeEnum.MEMBER.getCode()).append("' AND C.MAIN_ID=M.ID \n");
        sql.append("    GROUP BY R.DS_ID \n");
        sql.append(") DRS ON DRS.DS_ID=S.ID \n");

        // 商店管理員
        //sql.append("LEFT OUTER JOIN EC_STORE_USER SU ON (SU.STORE_ID=ST.ID OR SU.STORE_ID=#MAN_STORE_ID) AND SU.MEMBER_ID=S.ID AND SU.DISABLED=0 \n");
        //params.put("MAN_STORE_ID", criteriaVO.getManageStoreId()!=null?criteriaVO.getManageStoreId():0L);
        
        // 已成交訂單
        if( criteriaVO.isFullData() ){
            sql.append("LEFT OUTER JOIN ( \n");
            sql.append("    SELECT MEMBER_ID, COUNT(ID) COUNTS, SUM(TOTAL) TOTAL_AMT, MAX(CREATETIME) lastBuyDate \n"); 
            sql.append("    FROM EC_ORDER \n");
            sql.append("    WHERE 1=1 \n");
            sql.append("    AND STATUS IN ('")
               .append(OrderStatusEnum.Approve.getCode()).append("', '")// 賣家已核准
               .append(OrderStatusEnum.Close.getCode()).append("') \n");// 賣家結案
            sql.append("    GROUP BY MEMBER_ID \n");
            sql.append(") O ON O.MEMBER_ID=S.ID \n");
        }
        
        // 維護廠別 (取第一筆)
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT A.FACTORY_ID, A.MEMBER_ID \n");
        sql.append("    FROM EC_MEMBER_FACTORY A \n");
        sql.append("    JOIN (SELECT MEMBER_ID, MIN(ID) MID FROM EC_MEMBER_FACTORY GROUP BY MEMBER_ID) B \n");
        sql.append("        ON B.MEMBER_ID=A.MEMBER_ID AND B.MID=A.ID \n");
        sql.append(") MF ON MF.MEMBER_ID=S.ID \n");
        
        sql.append("LEFT OUTER JOIN EC_MEMBER MC ON MC.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER MM ON MM.ID=S.MODIFIER \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");

        // 維護廠別
        if( !sys.isEmpty(criteriaVO.getFactoryList()) ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT * FROM EC_MEMBER_FACTORY PMF WHERE PMF.MEMBER_ID=S.ID \n");
            sql.append("    ").append(NativeSQLUtils.getInSQL("PMF.FACTORY_ID", criteriaVO.getFactoryList(), params)).append(" \n");
            sql.append(") \n");
        }
        if( criteriaVO.getFactoryId()!=null ){
            sql.append("AND MF.FACTORY_ID=#FACTORY_ID \n");
            params.put("FACTORY_ID", criteriaVO.getFactoryId());
        }
            
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
        if( !StringUtils.isBlank(criteriaVO.getCompanyKeyword()) ){
            String kw = "%" + criteriaVO.getCompanyKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("D.ID_CODE LIKE #CKW OR D.CNAME LIKE #CKW OR D.ENAME LIKE #CKW OR D.NICKNAME LIKE #CKW \n");
            sql.append(") \n");
            params.put("CKW", kw);
        }
        if( !StringUtils.isBlank(criteriaVO.getFullKeyword()) ){// for ec-admin
            String kw = "%" + criteriaVO.getFullKeyword().trim() + "%";
            sql.append("AND ( \n");
            sql.append("S.LOGIN_ACCOUNT LIKE #FKW OR S.NAME LIKE #FKW \n");
            sql.append("OR S.PHONE LIKE #FKW \n");
            sql.append("OR S.EMAIL LIKE #FKW \n");
            sql.append("OR D.ID_CODE LIKE #FKW OR D.CNAME LIKE #FKW OR D.ENAME LIKE #FKW OR D.NICKNAME LIKE #FKW \n");
            sql.append(") \n");
            params.put("FKW", kw);
        }

        if( criteriaVO.getHasPic()!=null ){// 有無上傳照片
            if( criteriaVO.getHasPic() ){
                sql.append("AND F.ID IS NOT NULL \n");
            }else{
                sql.append("AND F.ID IS NULL \n");
            }
        }
        
        if( criteriaVO.getIdCode()!=null ){
            String idCode = criteriaVO.getIdCode().trim().toUpperCase();
            sql.append("AND UPPER(D.ID_CODE) = #ID_CODE \n");
            params.put("ID_CODE", idCode);
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
    
    public MemberVO fundById(Long memberId, Locale locale){
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setId(memberId);
        List<MemberVO> list = this.findByCriteria(criteriaVO, locale);
        
        return sys.isEmpty(list)?null:list.get(0);
    }
    
    /**
     * for ec-amin link to ec-seller
     * @param loginAccount
     * @return 
     */
    public EcMember fundForInternal(String loginAccount){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loginAccount", loginAccount);
        List<EcMember> list = this.findByNamedQuery("EcMember.findByLoginAccount", params);
        if( list==null ){
            logger.error("fundForInternal error list==null");
            return null;
        }
        int count = 0;
        int index  = -1;
        for(int i=0; i<list.size(); i++){
            EcMember member = list.get(i);
            if( !member.getDisabled() && member.getActive() ){
                count++;
                index = i;
            }
        }
        
        if( count==0 ){
            logger.error("fundForInternal error no active account");
            return null;
        }else if( count>1 ){
            logger.error("fundForInternal has same active accounts");
            return null;
        }else{
            return list.get(index);
        }
    }
    
    /**
     * 輸入檢查
     * @param vo
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(MemberVO vo, TcUser operator, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, "memberId", locale, errors);

        return pass;
    }
    
}

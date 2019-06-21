/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.et.facade.EtCompanyFacade;
import com.tcci.et.facade.EtOptionFacade;
import com.tcci.et.facade.EtFileFacade;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.VelocityMail;
import com.tcci.et.entity.EtMember;
import com.tcci.et.enums.FileEnum;
import com.tcci.et.enums.MemberTypeEnum;
import com.tcci.et.model.ApplyFormVO;
import com.tcci.et.model.FileVO;
import com.tcci.et.model.rs.LongOptionVO;
import com.tcci.et.model.criteria.MemberCriteriaVO;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.VenderVO;
import com.tcci.et.entity.EtMemberForm;
import com.tcci.et.entity.EtVender;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.facade.EtMemberFormFacade;
import com.tcci.et.facade.EtVenderFacade;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EtMemberFacade extends AbstractFacade<EtMember> {
    @EJB EtCompanyFacade companyFacade;
    @EJB EtFileFacade fileFacade;
    @EJB EtOptionFacade optionFacade;
    @EJB EtVenderFacade venderFacade;
    @EJB EtMemberFormFacade etMemberFormFacade;
    

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtMemberFacade() {
        super(EtMember.class);
    }
    
    public List<EtMember> findByDisabled(boolean disabled) {
        Query q = em.createNamedQuery("EtMember.findByDisabled");
        q.setParameter("disabled", disabled);
        return q.getResultList();
    }
    
    public String getMemberDetailInfoSQL(){
        StringBuilder common = new StringBuilder();
        common.append("  SELECT id, type, main_id, cname, ename, id_type, id_code, nickname, brief, \n");
//        common.append("  email1, email2, tel1, tel2, tel3, fax1, country, state, addr1, addr2, \n");
        common.append("  tel1, tel2, tel3, country, state, addr1, addr2, \n");
        common.append("  url1, url2, CREATOR, CREATETIME, MODIFIER, MODIFYTIME, \n");

        StringBuilder sql = new StringBuilder();
        // 個人
//        sql.append(common.toString());
//        sql.append("  gender, birthday, age, \n");// EC_PERSON only
//        
//        sql.append("  NULL email3, NULL fax2, NULL owner1, NULL owner2, NULL contact1, NULL contact2, NULL contact3, \n");// ET_COMPANY only
//        sql.append("  NULL web_id1, NULL web_id2, NULL longitude, NULL latitude, \n");// ET_COMPANY only
//        sql.append("  NULL start_at, NULL category, NULL capital, NULL year_income, \n");// ET_COMPANY only
//        sql.append("  '").append(MemberTypeEnum.PERSON.getCode()).append("' MEM_TYPE \n");
//        sql.append("  FROM EC_PERSON \n");
//        
//        sql.append("  UNION \n");
        // 公司/組織
        sql.append(common.toString());
        sql.append("  NULL gender, NULL birthday, NULL age, \n");// EC_PERSON only
        
//        sql.append("  email3, fax2, owner1, owner2, contact1, contact2, contact3, \n");// ET_COMPANY only
//        sql.append("  web_id1, web_id2, longitude, latitude, start_at, category, capital, year_income, \n");// ET_COMPANY only
        sql.append("  start_at, category, capital, \n");// ET_COMPANY only
        sql.append("  '").append(MemberTypeEnum.COMPANY.getCode()).append("' MEM_TYPE \n");
        sql.append("  FROM ET_COMPANY \n");
        
        return sql.toString();
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtMember entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getActive()==null ){ entity.setActive(true); }
           
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
    public void saveVO(MemberVO vo, TcUser operator, Locale locale, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        logger.debug("saveVO id = {}, memberId = {}, type = {}", new Object[] {vo.getId(), vo.getMemberId(), vo.getType()});
       
        // for 會員主檔 ET_MEMBER
        vo.setActive(vo.getActive()==null?true:vo.getActive());
//        vo.setAdminUser(vo.getAdminUser()==null?false:vo.getAdminUser());
//        vo.setSellerApply(vo.getSellerApply()==null?false:vo.getSellerApply());
//        vo.setSellerApprove(vo.getSellerApprove()==null?false:vo.getSellerApprove());
//        vo.setComApply(vo.getComApply()==null?false:vo.getComApply());
//        vo.setComApprove(vo.getComApprove()==null?false:vo.getComApprove());
//        vo.setNickname(vo.getNickname()==null?vo.getCname():vo.getNickname());// for 系統顯示

        //boolean isNew = (vo.getMemberId()==null);
        logger.info("saveVO vo.getMemberId()="+vo.getMemberId());
        EtMember member = (vo.getMemberId()!=null)?this.find(vo.getMemberId()):new EtMember();
        
        if( member.getId()==null ){// new
            // default password
            member.setPassword(vo.getPwd());
        }
        
        Long detailId = vo.getId();// 保存 detailId (ET_COMPANY.ID or EC_PERSON.ID)
        vo.setId(vo.getMemberId());
        vo.setApplytime(member.getApplytime());
        vo.setApprovetime(member.getApprovetime());
        vo.setMemberType(MemberTypeEnum.COMPANY.getCode());//def
        
        ExtBeanUtils.copyProperties(member, vo);
        logger.debug("saveVO MemberType="+vo.getMemberType()+", Type="+vo.getType());
        member.setType(vo.getMemberType());
        member.setDisabled(false); 
        this.save(member, operator, simulated);// save ET_MEMBER
        logger.debug("saveVO member.getId() = "+member.getId());
        vo.setMemberId(member.getId());// for new
        // for 自行註冊 operator==null
//        operator = (operator==null)?new EtMember(member.getId()):operator;
        
        // for ET_VENDER
        List<LongOptionVO> list = vo.getVenders();
        if(CollectionUtils.isNotEmpty(list)){
            MemberVO tempVO = new MemberVO();
            tempVO.setMemberId(member.getId());
            BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
            criteriaVO.setMemberId(member.getId());
            List<LongOptionVO> oriList = venderFacade.findByMemberVenderOptions(tempVO);
            List<LongOptionVO> removeList = new ArrayList<>(); 
            if(CollectionUtils.isNotEmpty(oriList)){
                for(LongOptionVO optionVO : oriList){
                    Long id = optionVO.getValue();
                    boolean selected = false;
                    for(LongOptionVO selectedVO : list){
                        if (id.compareTo(selectedVO.getValue())==0) {
                            selected = true;
                            break;
                        }
                    }
                    if (!selected) {
                        removeList.add(optionVO);
                    }
                }
                for (LongOptionVO optionVO : removeList) {
                    oriList.remove(optionVO);
                    EtVender entity = venderFacade.find(optionVO.getValue());
                    venderFacade.remove(optionVO.getValue(), false);
                }
                
            }
        }
        // add new
        List<VenderVO> addList = vo.getAddVenderList();
        if(CollectionUtils.isNotEmpty(addList)){
            for(VenderVO selectedVO : addList){
                EtVender entity = new EtVender();
                ExtBeanUtils.copyProperties(entity, selectedVO);
                entity.setMainId(member.getId());
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                venderFacade.save(entity, operator, simulated);
            }
        }
        
        vo.setId(detailId);// for new
    }
    
    /**
     * 查詢
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<MemberVO> findByCriteria(MemberCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.ID MEMBER_ID, S.TYPE MEMBER_TYPE, S.TYPE MEMBER_TYPE_ORI \n");
        sql.append(", S.LOGIN_ACCOUNT, S.NAME, S.EMAIL, S.PHONE, S.ACTIVE, S.DISABLED \n");
//        sql.append(", S.SELLER_APPLY, S.SELLER_APPROVE, S.APPROVETIME, S.ADMIN_USER \n");
        sql.append(", S.APPLY_VENDER_CODE,  S.APPLY_VENDER_NAME \n");
        sql.append(", S.APPROVETIME \n");
        sql.append(", V.CODES venderCodes \n");
//        sql.append(", S.COM_APPLY, S.COM_APPROVE, S.COM_APPROVETIME, S.COM_APPLYTIME \n");
//        sql.append(", S.TCC_DEALER, S.TCC_DS \n");
//        sql.append(", D.* \n"); // ET_COMPANY or EC_PERSON
//        sql.append(", SR.ID SELLER_ID, SU.STORE_ID \n");
//        sql.append(", ST.CNAME STORE_CNAME, ST.ENAME STORE_ENAME \n");
//        sql.append(", F.ID PIC_ID \n");
        //if( criteriaVO.getStoreManager()!=null && criteriaVO.getStoreManager() ){
//        sql.append(", SU.ID MANAGER_ID, SU.OWNER STORE_OWNER, SU.FI_USER \n");
        //}
//        if( criteriaVO.isFullData() ){
//            sql.append(", NVL(O.COUNTS,0) COUNTS, NVL(O.TOTAL_AMT,0) TOTAL_AMT, O.lastBuyDate \n");
//        }
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.NAME");
        }
        
        logger.debug("findMemberFullInfo sql = "+sql.toString());
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
                    
                    //vender
                    List<VenderVO> venderList = venderFacade.findByMemberId(vo.getMemberId());
                    vo.setVenderList(venderList);
                    
                    //apply form
                    List<EtMemberForm> applyList = etMemberFormFacade.findByMember(vo.getMemberId());
                    List<ApplyFormVO> applyFormList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(applyList)) {
                        ResourceBundle msgApp = ResourceBundle.getBundle("messages", locale);
                        for(EtMemberForm form : applyList){
                            ApplyFormVO formVO = new ApplyFormVO();
                            formVO.setId(form.getId());
                            formVO.setVenderCode(form.getApplyVenderCode());
                            formVO.setVenderName(form.getApplyVenderName());
                            formVO.setCreatetime(form.getCreatetime());
                            String status = msgApp.getString("status." + form.getStatus().toString());
                            formVO.setStatus(status);
                            //駁回原因
                            if(FormStatusEnum.REJECT.equals(form.getStatus())){
                                formVO.setRejectReason(form.getProcess().getTerminateReason());
                            }
                            applyFormList.add(formVO);
                        }
                        vo.setApplyFormList(applyFormList);
                    }
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
        sql.append("FROM ET_MEMBER S \n");
        
        //供應商 代碼
        // for Oracle
        sql.append("LEFT OUTER JOIN ( \n");//系統角色 groups TC_GROUP.NAME
        sql.append("  SELECT A.MAIN_ID \n");
        sql.append("  , LISTAGG(A.LIFNR_UI, ',') WITHIN GROUP (ORDER BY A.LIFNR_UI) CODES \n");
        sql.append("  FROM ET_VENDER A \n");
        sql.append("  GROUP BY A.MAIN_ID \n");
        sql.append(") v on v.MAIN_ID=S.ID \n");
        
//        sql.append("LEFT OUTER JOIN ET_FILE F ON F.PRIMARY_TYPE=#PRIMARY_TYPE AND F.PRIMARY_ID=S.ID \n");// 執照/證件圖
//        params.put("PRIMARY_TYPE", FileEnum.MEMBER_PIC.getPrimaryType());
        
        sql.append("WHERE 1=1 \n");
//        sql.append("AND S.DISABLED=0 \n");

        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getActive()!=null ){
            sql.append("AND S.ACTIVE=#ACTIVE \n");
            params.put("ACTIVE", criteriaVO.getActive());
        }       
        if( criteriaVO.getDisabled()!=null ){
            sql.append("AND S.DISABLED=#DISABLED \n");
            params.put("DISABLED", criteriaVO.getDisabled());
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
//        if( criteriaVO.getSellerId()!=null ){
//            sql.append("AND SR.ID=#SELLER_ID \n");
//            params.put("SELLER_ID", criteriaVO.getSellerId());
//        }
//        if( criteriaVO.getStoreId()!=null ){
//            sql.append("AND ST.ID=#STORE_ID \n");
//            params.put("STORE_ID", criteriaVO.getStoreId());
//        }
        /*if( criteriaVO.getManageStoreId()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT ID FROM EC_STORE_USER WHERE MEMBER_ID=S.ID AND STORE_ID=#MAN_STORE_ID \n");
            sql.append(") \n");
            params.put("MAN_STORE_ID", criteriaVO.getManageStoreId());
        }*/
        // 身分別查詢條件 AND or OR
//        if( criteriaVO.getIdentityUnion()!=null && criteriaVO.getIdentityUnion() ){// 聯集
//            if( (criteriaVO.getSellerApply()!=null && criteriaVO.getSellerApply()) 
//             || (criteriaVO.getSellerApprove()!=null)
//             || (criteriaVO.getTccDealer()!=null)
//             || (criteriaVO.getTccDs()!=null)
//             || (criteriaVO.getAdminUser()!=null) ){
//                sql.append("AND (1=2 \n");
//                if( criteriaVO.getSellerApply()!=null && criteriaVO.getSellerApply() ){// 申請成為賣家中
//                    sql.append("OR (S.SELLER_APPLY=1 AND S.SELLER_APPROVE=0) \n");
//                }
//                if( criteriaVO.getSellerApprove()!=null ){
//                    sql.append("OR S.SELLER_APPROVE=#SELLER_APPROVE \n");
//                    params.put("SELLER_APPROVE", criteriaVO.getSellerApprove());
//                }
//                if( criteriaVO.getTccDealer()!=null ){// TCC經銷商
//                    sql.append("OR S.TCC_DEALER=#TCC_DEALER \n");
//                    params.put("TCC_DEALER", criteriaVO.getTccDealer());
//                }
//                if( criteriaVO.getTccDs()!=null ){// TCC經銷商下游客戶
//                    sql.append("OR S.TCC_DS=#TCC_DS \n");
//                    params.put("TCC_DS", criteriaVO.getTccDs());
//                }
//                if( criteriaVO.getAdminUser()!=null ){// 系統管理員
//                    sql.append("OR S.ADMIN_USER=#ADMIN_USER \n");
//                    params.put("ADMIN_USER", criteriaVO.getAdminUser());
//                }
//                sql.append(") \n");
//            }
//        }else{// 交集
//            if( criteriaVO.getSellerApply()!=null && criteriaVO.getSellerApply() ){// 申請成為賣家中
//                sql.append("AND S.SELLER_APPLY=1 AND S.SELLER_APPROVE=0 \n");
//            }
//            if( criteriaVO.getSellerApprove()!=null ){
//                sql.append("AND S.SELLER_APPROVE=#SELLER_APPROVE \n");
//                params.put("SELLER_APPROVE", criteriaVO.getSellerApprove());
//            }
//            if( criteriaVO.getTccDealer()!=null ){// TCC經銷商
//                sql.append("AND S.TCC_DEALER=#TCC_DEALER \n");
//                params.put("TCC_DEALER", criteriaVO.getTccDealer());
//            }
//            if( criteriaVO.getTccDs()!=null ){// TCC經銷商下游客戶
//                sql.append("AND S.TCC_DS=#TCC_DS \n");
//                params.put("TCC_DS", criteriaVO.getTccDs());
//            }
//            if( criteriaVO.getAdminUser()!=null ){// 系統管理員
//                sql.append("AND S.ADMIN_USER=#ADMIN_USER \n");
//                params.put("ADMIN_USER", criteriaVO.getAdminUser());
//            }
//        }
        
//        if( criteriaVO.getDealerId()!=null ){// 依經銷商, 找下游客戶
//            sql.append("AND EXISTS ( \n");
//            sql.append("    SELECT TCC.* \n");
//            sql.append("    FROM EC_TCC_DEALER_DS TCC \n");
//            sql.append("    WHERE TCC.DEALER_ID=#DEALER_ID AND TCC.DS_ID=S.ID \n");
//            sql.append(") \n");
//            params.put("DEALER_ID", criteriaVO.getDealerId());
//        }
//        if( criteriaVO.getDsId()!=null ){// 依下游客戶, 找經銷商
//            sql.append("AND EXISTS ( \n");
//            sql.append("    SELECT TCC.* \n");
//            sql.append("    FROM EC_TCC_DEALER_DS TCC \n");
//            sql.append("    WHERE TCC.DS_ID=#DS_ID AND TCC.DEALER_ID=S.ID \n");
//            sql.append(") \n");
//            params.put("DS_ID", criteriaVO.getDsId());
//        }

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
//        if( !StringUtils.isBlank(criteriaVO.getEmailKeyword()) ){
//            String kw = "%" + criteriaVO.getEmailKeyword().trim() + "%";
//            sql.append("AND S.EMAIL LIKE #EMAILKW \n");
//            params.put("EMAILKW", kw);
//        }

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
        logger.info("findForLogin account = "+account);
        logger.info("findForLogin encrypted = "+encrypted);
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setLoginAccount(account);
        criteriaVO.setEncryptedPwd(encrypted);
//        criteriaVO.setActive(Boolean.TRUE);
//        criteriaVO.setDisabled(Boolean.FALSE);
        criteriaVO.setFullData(false);
        
        List<MemberVO> list = findByCriteria(criteriaVO, locale);
        logger.info("findForLogin list ="+(list!=null?list.size():0));
        MemberVO vo = (list!=null && list.size()==1)? list.get(0):null;
        
        if( vo!=null ){
            venderFacade.findByMemberVenderOptions(vo);
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
        logger.info("findMemberFullInfo list = {}", list);
        logger.info("findMemberFullInfo list = {}", list.size());
        
        MemberVO vo = (list!=null && !list.isEmpty() && list.get(0).getMemberId().equals(id))? list.get(0):null;
        if( vo!=null ){
            FileVO pic = fileFacade.findSingleByPrimary(GlobalConstant.SHARE_STORE_ID, FileEnum.MEMBER_PIC.getCode(), vo.getId());
            vo.setPicture(pic);
            // 指定會員的供應商
            venderFacade.findByMemberVenderOptions(vo);
        }
        
        return vo;
    }
    
    /**
     * 登入帳號
     * @param loginAccount
     * @return 
     */
    public EtMember findByLoginAccount(String loginAccount){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loginAccount", loginAccount);
        List<EtMember> list = this.findByNamedQuery("EtMember.findByLoginAccount", params);
        
        for(EtMember member : list){
//            if( member.getDisabled()!=null && !member.getDisabled() ){
                return member;
//            }
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
        
        if( vo!=null ){
            venderFacade.findByMemberVenderOptions(vo);
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
    public boolean checkInput(MemberVO vo, EtMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, "memberId", locale, errors);

        return pass;
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

            sql.append("MERGE INTO ET_MEMBER D \n");
            sql.append("USING ( \n");
            sql.append("     SELECT E10.ID, E10.LOGIN_ACCOUNT, E10.NAME, E10.EMAIL, E10.PHONE, E10.PASSWORD, E10.ACTIVE \n");
            sql.append("     FROM TCCSTORE_USER.ET_MEMBER E10 \n");
            // 針對經銷商
            sql.append("     JOIN ET_MEMBER M ON UPPER(M.LOGIN_ACCOUNT)=UPPER(E10.LOGIN_ACCOUNT) AND M.TCC_DEALER=1 \n");
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
    
    //BATCH
    public void batchCheckBlockVender(TcUser admin){
        //系統復用 
        List<EtMember> disabledList = this.findByDisabled(Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(disabledList)) {
            for(EtMember entity : disabledList){
                List<EtVender> list = venderFacade.findByMainId(entity.getId());
                if (CollectionUtils.isNotEmpty(list)) {
                    BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
                    criteriaVO.setDisabled(Boolean.FALSE);//排除黑名單
                    boolean reopen = true;
                    for(EtVender vender : list){
                        criteriaVO.setType(vender.getMandt());
                        criteriaVO.setCode(vender.getVenderCode());
                        List<VenderVO> result = venderFacade.findLfa1ByCriteria(criteriaVO);
                        if (CollectionUtils.isEmpty(result)) {
                            reopen = false;
                        }
                    }
                    
                    if(reopen){
                        logger.info("batchCheckBlockVender enable memberId:"+entity.getId());
                        entity.setDisabled(Boolean.FALSE);
                        this.save(entity, admin, Boolean.FALSE);
                    }
                }
            }
        }
        //系統停用
        List<EtVender> allList = venderFacade.findAll();
        if (CollectionUtils.isNotEmpty(allList)) {
            BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
            criteriaVO.setDisabled(Boolean.FALSE);//排除黑名單
            for(EtVender vender : allList){
                criteriaVO.setType(vender.getMandt());
                criteriaVO.setCode(vender.getVenderCode());
                List<VenderVO> result = venderFacade.findLfa1ByCriteria(criteriaVO);
                if (CollectionUtils.isEmpty(result)) {
                    Long memberId = vender.getMainId();
                    EtMember entity = this.find(memberId);
                    if(entity!=null && !entity.getDisabled()){
                        logger.info("batchCheckBlockVender disable memberId:"+memberId);
                        entity.setDisabled(Boolean.TRUE);
                        this.save(entity, admin, Boolean.FALSE);
                    }
                }
            }
        }
        
    }
    
    public List<MemberVO> findNoticeMemberByCategoryId(Long categoryId){
        return this.checkMappingMemberAndCategoryId(categoryId, null);
    }
    public List<MemberVO> checkMappingMemberAndCategoryId(Long categoryId, Long memberId){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  \n");
        sql.append("distinct m.ID as memberID, m.name, m.EMAIL \n");
//        sql.append(" ,v.MANDT, v.LIFNR_UI \n");
        sql.append("from ET_VENDER v \n");
        sql.append("INNER JOIN ET_VENDER_CATEGORY vc on vc.MANDT = v.MANDT and vc.LIFNR_UI=v.LIFNR_UI \n");
        sql.append("INNER JOIN ET_MEMBER m on m.ID=v.MAIN_ID \n");
        //m.DISABLED = 0 表示黑名單供應商
//        sql.append("INNER JOIN TC_ZTAB_EXP_LFA1 S on S.MANDT = v.MANDT and S.LIFNR_UI=v.LIFNR_UI \n");
        sql.append("WHERE 1=1  \n");
        sql.append("AND m.ACTIVE=1 AND m.DISABLED = 0 \n");
//        sql.append("AND S.CSPERM is null \n");
//        sql.append("AND (S.SPERQ!='99' or S.SPERQ is null ) \n");
        if( memberId!=null ){
            sql.append("AND m.ID=#memberId \n");
            params.put("memberId", memberId);
        }
        if( categoryId!=null ){
            sql.append("AND vc.CATEGORY=#categoryId \n");
            params.put("categoryId", categoryId);
        }
        sql.append("order by m.ID");
        return this.selectBySql(MemberVO.class, sql.toString(), params);
    }
    
    public void sendPwdMail(EtMember member, String pwd){
        StringBuilder sbTitle = new StringBuilder().append(GlobalConstant.EMAIL_SUBJECT_PREFIX);
        if( StringUtils.isNotBlank(pwd) ){
            sbTitle.append("預設密碼通知");
        }else{
            sbTitle.append("密碼變更通知");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(VelocityMail.SUBJECT, sbTitle.toString());
        parameters.put(VelocityMail.TO, member.getEmail());
        Map<String, Object> mailBean = new HashMap<>();
        if( StringUtils.isNotBlank(pwd) ){
            mailBean.put("type", "reset");
        }else{
            mailBean.put("type", "changePwd");
        }
        mailBean.put("msg", pwd);
        
        VelocityMail.sendMail(parameters, mailBean, "pwd.vm");
    }

}

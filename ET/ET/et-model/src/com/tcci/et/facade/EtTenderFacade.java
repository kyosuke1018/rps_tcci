/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.cm.util.NotificationUtils;
import com.tcci.cm.util.VelocityMail;
import com.tcci.et.entity.KbRichContent;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.PublicationEnum;
import com.tcci.et.enums.RichContentEnum;
import com.tcci.et.enums.SaveTypeEnum;
import com.tcci.et.model.MemberVO;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.rs.AttachmentRsVO;
import com.tcci.et.entity.EtTender;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.util.StringUtils;
import com.tcci.fc.vo.AttachmentVO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EtTenderFacade extends AbstractFacade<EtTender> {
    @EJB private TcDomainFacade domainFacade;
    @EJB private AttachmentFacade attachmentFacade;
    @EJB private KbRichContentFacade richContentFacade;
    @EJB private KbPhotoGalleryFacade photoGalleryFacade;
    @EJB private SysResourcesFacade sys;
    
    @EJB private EtMemberFacade etMemberFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtTenderFacade() {
        super(EtTender.class);
    }
    
    /**
     * PublicationVO 關聯檔案
     * @param pubVO
     * @return 
     */
//    public List<AttachmentVO> findFiles(PublicationVO pubVO){
//        KbPublication entity = new KbPublication();
//        copyVoToEntity(pubVO, entity);
//
//        List<AttachmentVO> files = attachmentFacade.loadContent(entity);// 關聯上傳檔
//        return files;
//    }
    public List<AttachmentVO> findFiles(EtTender entity){
        List<AttachmentVO> files = attachmentFacade.loadContent(entity);// 關聯上傳檔
        return files;
    }
    
    /**
     * 文章類共用查詢SQL
     * @param criteriaVO
     * @return 
     */
    public String genCommonFindSQL(TenderCriteriaVO criteriaVO){
        boolean fullData = (criteriaVO!=null)? criteriaVO.isFullData():false;
        boolean countOnly = (criteriaVO!=null)? criteriaVO.isCountOnly():false;
        
        StringBuilder sql = new StringBuilder();

//        PublicationEnum publicationEnum = (criteriaVO!=null)?PublicationEnum.getFromCode(criteriaVO.getType()):null;
        
        if( countOnly ){
            sql.append("SELECT count(S.ID) \n");
        }else{
            sql.append("SELECT S.* \n");
//            sql.append(", TL.ID LINKID, TL.URL, TL.OPENMETHOD \n");
            if( fullData ){
                sql.append(", TH.ID HTMLID, TH.CONTENTS \n");
//                sql.append(", TH.ID HTMLID, TH.CONTENT CONTENTS \n");
//                sql.append(", S.CONTENT CONTENTS \n");
            }else{
                sql.append(", TH.ID HTMLID \n");// 效能考量
            }
            sql.append(", CASE WHEN S.MODIFYTIME IS NULL THEN U1.CNAME ELSE U2.CNAME END LASTUSERNAME \n"); 
            sql.append(", CASE WHEN S.MODIFYTIME IS NULL THEN S.CREATETIME ELSE S.MODIFYTIME END LASTTIME \n");
            sql.append(", CF.ID FACTORYID, CONCAT(CF.CODE,CF.NAME) AS FACTORYNAME \n");
            sql.append(", CF.SAP_CLIENT_CODE, CF.COMPANY_ID, CF.CURRENCY \n");
            sql.append(", CM.COMPANY_NAME, CM.SAP_CLIENT, CM.LANGUAGE \n");
            sql.append(", EO.ID AREAID, EO.CNAME AREANAME, EO.SORTNUM AREASORT \n");
            sql.append(", EO2.ID CATEGORYID, EO2.CNAME CATEGORYNAME, EO2.SORTNUM CATEGORYSORT \n");
            sql.append(", rfq.ID RFQID \n");//rfq
            
            // 封面圖示
//            if( criteriaVO!=null && publicationEnum!=null && publicationEnum.getHasCoverImg() ){
//                sql.append(", F.DOMAIN as coverDomain, F.FILENAME as coverFilename, F.CONTENTTYPE as coverContenttype \n");
//            }
        }
        sql.append("FROM ET_TENDER S \n");
        
//        sql.append("LEFT OUTER JOIN KB_LINK TL ON S.DATATYPE='")
//                .append(DataTypeEnum.LINK.getCode()).append("' AND TL.PRIMARYTYPE='")
//                .append(LinkEnum.DOC.getCode()).append("' AND PRIMARYID=S.ID \n");// 網址
//        sql.append("LEFT OUTER JOIN KB_RICH_CONTENT TH ON S.DATATYPE='")
        sql.append("LEFT OUTER JOIN KB_RICH_CONTENT TH ON TH.PRIMARYTYPE='")
                .append(RichContentEnum.TENDER.getCode())
                .append("' AND TH.PRIMARYID=S.ID \n"); // 自訂網頁
        sql.append("LEFT OUTER JOIN CM_FACTORY CF ON CF.ID=S.FACTORY_ID \n");
        sql.append("LEFT OUTER JOIN CM_COMPANY CM ON CM.ID=CF.COMPANY_ID \n");
        sql.append("LEFT OUTER JOIN ET_OPTION EO ON EO.TYPE='area' AND EO.ID=S.AREA_ID \n");
        sql.append("LEFT OUTER JOIN ET_OPTION EO2 ON EO2.TYPE='tenderCategory' AND EO2.ID=S.CATEGORY_ID \n");

        // 封面圖示
//        if( criteriaVO!=null && publicationEnum!=null && publicationEnum.getHasCoverImg() ){
//            sql.append("LEFT OUTER JOIN ( \n"); 
//            sql.append("    select p.PRIMARYID, a.* \n");
//            sql.append("    from TC_FVITEM a \n");
//            sql.append("    join TC_APPLICATIONDATA b on b.FVITEM=a.ID and b.CONTAINERCLASSNAME='").append(KbPhotoGallery.class.getName()).append("' \n");
//            sql.append("    join KB_PHOTO_GALLERY p on p.ID=b.CONTAINERID and IDENTIFY_IMG=1 \n");// 利用 IDENTIFY_IMG 標示為封面圖示
//            sql.append("        and p.PRIMARYTYPE='").append(PhotoGalleryEnum.DOC.getCode()).append("' \n");
//            sql.append(") F ON F.PRIMARYID=S.ID \n");
//        }

        sql.append("LEFT OUTER JOIN TC_USER U1 ON U1.ID=S.CREATOR \n"); 
        sql.append("LEFT OUTER JOIN TC_USER U2 ON U2.ID=S.MODIFIER \n"); 
        sql.append("LEFT OUTER JOIN ET_RFQ_EKKO rfq ON rfq.TENDER_ID=S.ID \n");
        sql.append("WHERE 1=1 \n"); 
        
        return sql.toString();
    }
    
    /**
     * 依查詢條件抓取資料
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String getSqlByCriteria(TenderCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append(genCommonFindSQL(criteriaVO));
        
        if( criteriaVO!=null ){
            // type 招標方式
            if( criteriaVO.getType()!=null ){
                sql.append("AND S.TYPE=#type \n");
                params.put("type", criteriaVO.getType());
            }
            if(criteriaVO.getTypes()!=null){
                sql.append(NativeSQLUtils.getInSQL("S.TYPE", criteriaVO.getTypes(), params, 2)).append(" \n");
            }
            // DATATYPE
//            if( criteriaVO.getDataType()!=null ){
//                sql.append("AND S.DATATYPE=#datatype \n");
//                params.put("datatype", criteriaVO.getDataType());
//            }
            // Status
            if( criteriaVO.getStatus()!=null ){
                sql.append("AND S.STATUS=#status \n");
                params.put("status", criteriaVO.getStatus());
            }
            if(criteriaVO.getStatusList()!=null){
//                String cond = NativeSQLUtils.getInSQL("S.STATUS", criteriaVO.getStatusList(), params, 2);
//                sql.append(cond);
//                logger.info("findByCriteria Status cond..."+cond);
                sql.append(NativeSQLUtils.getInSQL("S.STATUS", criteriaVO.getStatusList(), params, 2)).append(" \n");
                
            }
            
            // ID
            if( criteriaVO.getId()!=null ){
                sql.append("AND S.ID=#id \n");
                params.put("id", criteriaVO.getId());
            }
            //廠別
            if( criteriaVO.getFactoryId()!=null ){
                sql.append("AND S.FACTORY_ID=#factoryId \n");
                params.put("factoryId", criteriaVO.getFactoryId());
            }else{
                if (CollectionUtils.isNotEmpty(criteriaVO.getIdList())) {
                    String pColumnName = "S.FACTORY_ID";
                    List<Long> pValueList = criteriaVO.getIdList();
                    sql.append((NativeSQLUtils.getInSQL(pColumnName, pValueList, params))).append(" \n");
                }
            }
            if( criteriaVO.getAreaId()!=null ){
                sql.append("AND S.AREA_ID=#areaId \n");
                params.put("areaId", criteriaVO.getAreaId());
            }
            if( criteriaVO.getCategoryId()!=null ){
                sql.append("AND S.CATEGORY_ID=#categoryId \n");
                params.put("categoryId", criteriaVO.getCategoryId());
            }
            // HTMLID
            if( criteriaVO.getHtmlId()!=null ){
                sql.append("AND TH.ID=#htmlId \n");
                params.put("htmlId", criteriaVO.getHtmlId());
            }
            // LINKID
            if( criteriaVO.getLinkId()!=null ){
                sql.append("AND TL.ID=#linkId \n");
                params.put("linkId", criteriaVO.getLinkId());
            }
            // Parent
//            if( criteriaVO.getParent()!=null ){
//                sql.append("AND S.PARENT=#parent \n");
//                params.put("parent", criteriaVO.getParent());
//            }
            // News
//            if( criteriaVO.getNews()!=null && criteriaVO.getNews() ){
//                sql.append("AND S.NEWS=1 \n");
//            }
            // 決標日期
            if( criteriaVO.getStartCloseDate()!=null ){
                sql.append("AND S.CLOSE_DATE>=#StartCloseDate \n");
                params.put("StartCloseDate", criteriaVO.getStartCloseDate());
            }
            if( criteriaVO.getEndCloseDate()!=null ){
                sql.append("AND S.CLOSE_DATE<=#EndCloseDate \n");
                params.put("EndCloseDate", criteriaVO.getEndCloseDate());
            }
            // 評標日期
            if( criteriaVO.getStartVerifyDate()!=null ){
                sql.append("AND S.VERIFY_DATE>=#StartVerifyDate \n");
                params.put("StartVerifyDate", criteriaVO.getStartVerifyDate());
            }
            if( criteriaVO.getEndVerifyDate()!=null ){
                sql.append("AND S.VERIFY_DATE<=#EndVerifyDate \n");
                params.put("EndVerifyDate", criteriaVO.getEndVerifyDate());
            }
            // 發佈日期
            if( criteriaVO.getStartPublishDate()!=null ){
                sql.append("AND S.DATEDATE>=#StartPublishDate \n");
                params.put("StartPublishDate", criteriaVO.getStartPublishDate());
            }
            if( criteriaVO.getEndPublishDate()!=null ){
                sql.append("AND S.DATEDATE<=#EndPublishDate \n");
                params.put("EndPublishDate", criteriaVO.getEndPublishDate());
            }
            
            //發佈通知
            if( criteriaVO.getNotice()!=null && criteriaVO.getNotice() ){
                sql.append("AND (");
//                sql.append(" (S.DATADATE<sysdate) AND (S.DATADATE>=(sysdate-1))) ");
                sql.append(" (S.DATADATE<sysdate) AND (S.DATADATE>=(sysdate-20)) ");
                sql.append(") \n");
            }
            // 排除2個月前決標案件
            if( criteriaVO.getClosed()!=null && criteriaVO.getClosed() ){
//                if( criteriaVO.getStatus()==null && criteriaVO.getStatusList()==null ){
                    sql.append("AND (S.STATUS!='E' or \n");
                    sql.append(" (S.STATUS='E'AND S.CLOSE_DATE is not null AND S.CLOSE_DATE>=(sysdate-60)) \n");
                    sql.append(") \n");
//                }else{
//                    sql.append("AND S.STATUS=#statusE \n");
//                    params.put("statusE", TenderStatusEnum.END.getCode());
//                    sql.append("AND S.CLOSE_DATE is not null AND S.CLOSE_DATE>=(sysdate-60) \n");
//                }
                
            }
            //排除停用(已暫停 已刪除)
            //排除未開始  (標書發售中 投標中 評標中 已決標)
            if( criteriaVO.getActive()!=null && criteriaVO.getActive() ){
//                sql.append("AND S.STATUS!=#statusR \n");
//                params.put("statusR", TenderStatusEnum.REMOVE.getCode());
                
                //java.lang.NoClassDefFoundError: com/tcci/fc/util/zhcoder/Zhcoder
                //fixed model admin web都要放Zhcoder jar檔
//                List<String> pValueList = TenderStatusEnum.getCodeByActive(Boolean.TRUE);
//                sql.append(NativeSQLUtils.getInSQL("S.STATUS", pValueList, params, 10)).append(" \n");
                //
                sql.append("AND S.STATUS in ('OS', 'T', 'V', 'E') \n");
            }
            
            
            //非草稿狀態
            if( criteriaVO.getNotDraft()!=null && criteriaVO.getNotDraft() ){
                sql.append("AND S.STATUS!=#statusD \n");
                params.put("statusD", TenderStatusEnum.DRAFT.getCode());
            }
            
            // Code 特殊代碼查詢 (for 固定網頁文章)
            if( criteriaVO.getCode()!=null ){
                sql.append("AND S.CODE=#code \n");
                params.put("code", criteriaVO.getCode());
            }
            // 網站語系
            if( criteriaVO.getLang()!=null ){// ex. "AC":跨語系及繁體中文
                sql.append(LanguageEnum.genLangCriteriaSQL("S.LANG", criteriaVO.getLang(), params)).append(" \n");
                /*
                if( criteriaVO.getLang().equals("AC") ){// 跨語系及繁體中文
                    sql.append("AND S.LANG IN ('").append(LanguageEnum.ALL.getShortCode()).append("','")
                            .append(LanguageEnum.TRANDITIONAL_CHINESE.getShortCode()).append("') \n");
                }else if( criteriaVO.getLang().equals("AS") ){// 跨語系及簡體中文
                    sql.append("AND S.LANG IN ('").append(LanguageEnum.ALL.getShortCode()).append("','")
                            .append(LanguageEnum.SIMPLIFIED_CHINESE.getShortCode()).append("') \n");
                }else if( criteriaVO.getLang().equals("AE") ){// 跨語系及英文
                    sql.append("AND S.LANG IN ('").append(LanguageEnum.ALL.getShortCode()).append("','")
                            .append(LanguageEnum.ENGLISH.getShortCode()).append("') \n");
                }else{
                    sql.append("AND S.LANG=#LANG \n");
                    params.put("LANG", criteriaVO.getLang());
                }
                */
            }
            
            // 關鍵字
            String keyword = criteriaVO.getKeyword();
            if( keyword!=null && !keyword.isEmpty() ){
                keyword = "%" + keyword.toUpperCase() + "%";
//                sql.append("AND (UPPER(S.TITLE) LIKE #keywork OR UPPER(S.SUMMARY) LIKE #keywork) \n");
                sql.append("AND (UPPER(S.TITLE) LIKE #keywork OR UPPER(S.CODE) LIKE #keywork) \n");
                params.put("keywork", keyword);
            }
        }
        
        return sql.toString();
    }
    public int countByCriteria(TenderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        criteriaVO.setCountOnly(true);
        sql.append(getSqlByCriteria(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public List<TenderVO> findByCriteria(TenderCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        criteriaVO.setCountOnly(false);
        sql.append(getSqlByCriteria(criteriaVO, params));
        
        // order by 
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY NVL(S.MODIFYTIME, S.CREATETIME) DESC");
        }
        
        logger.debug("findByCriteria ..."+sql);
        List<TenderVO> resList;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            resList = selectBySql(TenderVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            resList = selectBySql(TenderVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            resList = selectBySql(TenderVO.class, sql.toString(), params);
        }
        
        //準備content
        //資料型態CLOB 用native sql 會出錯java.lang.SecurityException: sealing violation: package oracle.jdbc is sealed
        //另外以JPA取得
        if( criteriaVO.isFullData() && resList!=null ){
            for(TenderVO vo:resList){
                Long htmlId = vo.getHtmlId();
                if(htmlId!=null){
                    KbRichContent kbRichContent = richContentFacade.find(htmlId);
                    vo.setContents(kbRichContent.getContents());
                }
            }
        }
        
        // 準備照片　URL
        /*
        if( resList!=null && (criteriaVO.getIgnoreCoverImage()==null || !criteriaVO.getIgnoreCoverImage()) ){
            try{
                for(PublicationVO vo : resList){
                    if( vo.getTypeEnum().getHasCoverImg() ){
                        String url = imageFacade.preparePublicUrl(GlobalConstant.DOMAIN_NAME_DOC_IMG, vo.getCoverFilename(), vo.getCoverContenttype());
                        vo.setCoverUrl(url);
                        logger.debug("findByCriteria CoverUrl = "+url);
                    }
                }
            }catch(Exception e){
                logger.error("findByCriteria CoverUrl Exception:\n", e);
            }
        }*/
        
        return resList;
    }
    public TenderVO findById(Long id, boolean withAttachment){
        if( id==null || id<=0 ){
            logger.error("findById id==null || id<=0");
            return null;
        }
        TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(true);// 單筆取所有資料
//        if( publicOnly ){
//            criteriaVO.setStatus(ContentStatusEnum.PUBLISH.getCode());
//        }
        List<TenderVO> resList = findByCriteria(criteriaVO);
        
        TenderVO vo = (resList!=null && resList.size()>0)?resList.get(0):null;
        // 取得文章內容或關聯檔
        getDocContents(vo);
        //附件
        if(vo!=null){
            List<AttachmentVO> attachmentList = findFiles(vo);
            if(CollectionUtils.isNotEmpty(attachmentList)){
                if(withAttachment){
                    vo.setDocs(attachmentList);
                }else{
                    List<AttachmentRsVO> docRsList = new ArrayList<>();
                    for(AttachmentVO attachmentVO:attachmentList){
                        AttachmentRsVO rsVO = new AttachmentRsVO();
                        rsVO.setAppId(attachmentVO.getApplicationdata().getId());
                        rsVO.setFileName(attachmentVO.getFileName());
                        rsVO.setContentType(attachmentVO.getContentType());
                        rsVO.setIndex(attachmentVO.getIndex());
                        String url = rsVO.genUrl(sys.getRestUrlPrefix(), "/services/tender/file",id , attachmentVO.getApplicationdata().getId());
                        rsVO.setUrl(url);
                        docRsList.add(rsVO);
                    }
                    vo.setDocRsList(docRsList);
                }
            }
        }
        
        return vo;
    }
    
    /**
     * 取得文章內容或關聯檔
     * @param vo 
     */
    public void getDocContents(TenderVO vo){
        if( vo!=null ){
            //只查rich content?!
//            if( pubVO.getTypeEnum().getHasCoverImg() ){
//                photoGalleryFacade.loadCoverImage(pubVO);// 取得封面圖示
//            }
            
//            if( pubVO.getDataTypeEnum()==DataTypeEnum.UPLOAD ){
//                vo.setDocs(findFiles(vo));
//            }else if( pubVO.getDataTypeEnum()==DataTypeEnum.HTML ){
                // 自 FVVAULT 讀取檔案內容
                KbRichContent kbRichContent = new KbRichContent(vo.getHtmlId());
                List<AttachmentVO> list = attachmentFacade.loadContent(kbRichContent);
                logger.debug("getDocContents pubVO.getHtmlId() ="+vo.getHtmlId());
                logger.debug("getDocContents list ="+((list!=null)?list.size():0));
                if( list!=null && !list.isEmpty() ){
                    AttachmentVO attachmentVO = list.get(0);
                    try{
                        byte[] bytes = IOUtils.toByteArray(attachmentFacade.getContentStream(attachmentVO));
                        if( bytes!=null ){
                            String contents = new String(bytes, GlobalConstant.ENCODING_DEF);
                            vo.setContents(contents);
                            logger.debug("getDocContents get Content from file.");
                        }
                    }catch(Exception e){
                        logger.error("getDocContents read rich content Exception:\n", e);
                    }
                }
//            }
        }
    }
    
    /**
     * TenderVO 關聯檔案
     * @param vo
     * @return 
     */
    public List<AttachmentVO> findFiles(TenderVO vo){
        EtTender entity = new EtTender();
        copyVoToEntity(vo, entity);

        List<AttachmentVO> files = attachmentFacade.loadContent(entity);// 關聯上傳檔
        return files;
    }
    
    /**
     * 刪除文章 
     * @param tenderVO
     * @param operator
     * @param simulated
     * @throws java.io.IOException
     */
    public void deleteDoc(TenderVO tenderVO, TcUser operator, boolean simulated) throws IOException {
        if( tenderVO==null || tenderVO.getId()==null ){
            logger.error("deleteDoc tenderVO==null || tenderVO.getId()==null");
            return;
        }else{
            EtTender entity = this.find(tenderVO.getId());
            if( entity==null ){
                logger.error("deleteDoc not exists tenderVO.getId()="+tenderVO.getId());
                return;
            }
        }
        
//        if( DataTypeEnum.FOLDER.getCode().equals(pubVO.getDataType()) ){
//            logger.debug("deleteDoc "+pubVO.getDataType()+", pubVO.getId() = "+pubVO.getId());
//            remove(pubVO.getId(), simulated);
//        }else if( DataTypeEnum.LINK.getCode().equals(pubVO.getDataType()) ){
//            logger.debug("deleteDoc "+pubVO.getDataType()+", pubVO.getLinkId() = "+pubVO.getLinkId());
//            linkFacade.remove(pubVO.getLinkId(), simulated);
//            remove(pubVO.getId(), simulated);
//        }else if( DataTypeEnum.HTML.getCode().equals(pubVO.getDataType()) ){
//            logger.debug("deleteDoc "+pubVO.getDataType()+", pubVO.getHtmlId() = "+pubVO.getHtmlId());
            logger.debug("tenderVO.getHtmlId() = "+tenderVO.getHtmlId());
            KbRichContent entity = richContentFacade.find(tenderVO.getHtmlId());
            if( entity!=null ){
                attachmentFacade.removeContent(entity, simulated);
                richContentFacade.remove(entity, simulated);
            
                // 自圖庫取圖 - 移除文章插圖關聯 & 直接上傳插圖 - 移除圖片
//                photoGalleryFacade.removeDocImagesAll(pubVO.getId(), operator, simulated);
            }
            remove(tenderVO.getId(), simulated);
//        }else if( DataTypeEnum.UPLOAD.getCode().equals(pubVO.getDataType()) ){
//            logger.debug("deleteDoc "+pubVO.getDataType()+", pubVO.getId() = "+pubVO.getId());
//            KbPublication entity = find(pubVO.getId());
//            if( entity!=null ){
//                attachmentFacade.removeContent(entity, simulated);
//            }
//            remove(pubVO.getId(), simulated);
//        }
    }
    
    /**
     * 刪除兩小時前的暫存資料
     * @param user
     * @param simulated
     */
    public void deleteTemp(TcUser user){
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ET_TENDER \n")
           .append("where TYPE='").append(PublicationEnum.TEMP.getCode()).append("' \n")
//           .append("and CREATETIMESTAMP<DATEADD(HH, -2, GETDATE()) \n");// for MSSQL
           .append("and CREATETIME<(SYSDATE - 1/12) \n");// for Oracle
        
        if( user!=null && user.getId()!=null ){
            sql.append("and CREATOR=").append(user.getId());
        }
        
        Query q = em.createNativeQuery(sql.toString());
        q.executeUpdate();
    }
    
    /**
     * save with rich content
     * @param tenderVO
     * @param rcEntity
     * @param operator 
     * @throws java.io.UnsupportedEncodingException 
     */
    public void saveRichContent(TenderVO tenderVO, KbRichContent rcEntity, TcUser operator, boolean simulated) throws UnsupportedEncodingException, Exception{
        logger.debug("saveRichContent tenderVO.getId() = "+tenderVO.getId());
        
        EtTender entity = (tenderVO.getId()!=null && tenderVO.getId()>=0)? this.find(tenderVO.getId()):new EtTender();
        copyVoToEntity(tenderVO, entity);
        save(entity, operator, simulated);
        copyEntityToVo(entity, tenderVO);
        
        rcEntity.setPrimaryType(RichContentEnum.TENDER.getCode());
        rcEntity.setPrimaryId(tenderVO.getId());
//        rcEntity.setContent(tenderVO.getContents());
        
        //rcEntity.setContents(pubVO.getContents()); Cause java.sql.DataTruncation: Data truncation
        // for admin UI query (DB 只存過濾 TAG 後的部分資料)
        String noHtml = tenderVO.getContents().replaceAll("\\<.*?>","|");
        String safeContent = StringUtils.safeTruncat(noHtml, GlobalConstant.ENCODING_DEF, 100, GlobalConstant.RICHCONTENT_SUMMARY_LEN);// 內容長度檢核
        rcEntity.setContents(safeContent);
        rcEntity.setSaveType(SaveTypeEnum.FILE.getCode());// 存至 FVVAULT
        richContentFacade.save(rcEntity, operator, simulated);
        
        // RichContent 存至 FVVAULT (完整HTML資料存至檔案)
        byte[] contents = tenderVO.getContents().getBytes(GlobalConstant.ENCODING_DEF);
        String oriFilename = GlobalConstant.RICHCONTENT_FILENAME+entity.getId()+GlobalConstant.FILE_EXT_HTML;// 自訂產生原始檔名

        TcDomain domain = domainFacade.getDefaultDomain();
        AttachmentVO attachmentVO = attachmentFacade.genAttachmentVO(oriFilename, GlobalConstant.CONTENTTYPE_HTML, contents, 0);
        attachmentFacade.saveContentSingle(domain, rcEntity, attachmentVO, simulated);// 新增關聯檔，原關聯刪除(實體檔暫不刪除)
        
        // 檢查是否有原本自圖庫取圖，又刪除的狀況。需刪除關聯資料
        // 文章插圖依最新自訂網頁內容部分移除
//        photoGalleryFacade.removeDocImagesByContent(tenderVO.getId(), tenderVO.getContents(), operator, simulated);
        
        // 回傳結果
        copyEntityToVo(entity, tenderVO);
        tenderVO.setHtmlId(rcEntity.getId());
    }
    
    /**
     * 儲存 Publication
     * @param tenderVO
     * @param operator 
     * @param simulated 
     * @throws java.lang.Exception 
     */
    public void saveAll(TenderVO tenderVO, TcUser operator, boolean simulated) throws Exception{
        //網頁 rich editor
        KbRichContent entity = (tenderVO.getHtmlId()!=null && tenderVO.getHtmlId()>0)?richContentFacade.find(tenderVO.getHtmlId()):new KbRichContent();
        saveRichContent(tenderVO, entity, operator, simulated);
    }
    
    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     * @param simulated 
     */
    public void save(EtTender entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
            }
        }
    }
    public void saveVO(TenderVO tenderVO, TcUser operator, boolean simulated){
        EtTender entity = (tenderVO.getId()!=null)?find(tenderVO.getId()):new EtTender();
        this.copyVoToEntity(tenderVO, entity);
        this.save(entity, operator, simulated);
        this.copyEntityToVo(entity, tenderVO);
    }
    
    /**
     * 複製
     * @param entity
     * @param vo
     */
    public void copyEntityToVo(EtTender entity, TenderVO vo){
        ExtBeanUtils.copyProperties(vo, entity);
        
        vo.setLastTime(vo.getLastUpdateTime());
        vo.setLastUserName((vo.getLastUpdateUser()!=null)?vo.getLastUpdateUser().getCname():null);
    }
    public void copyVoToEntity(TenderVO vo, EtTender entity){
        ExtBeanUtils.copyProperties(entity, vo);
    }
    
    /**
     * 依 autoComplete 輸入字串取得 Tender
     * @param allTenderList
     * @param txt
     * @return 
     */
    public TenderVO findTenderByTxt(List<TenderVO> allTenderList, String txt){
        int s = txt.lastIndexOf("(");
        int e = txt.lastIndexOf(")");

        String key;
        if( s<0 || e<=0 ){
            key = txt;
        }else{
            key = txt.substring(s+1, e);
        }
        
        if( allTenderList!=null ){
            for(TenderVO tender : allTenderList){
//                if(tender.getCode()!=null ){
//                    if( tender.getCode().toUpperCase().equals(key.toUpperCase()) ){
//                        return findById(tender.getId(), false);
//                    }
//                }else{
                    if(tender.getId() == Long.parseLong(key)){
                        return findById(tender.getId(), false);
                    }
//                }
            }
        }
        return null;
    }
    
    public String getTenderDisplayIdentifier(TenderVO tenderVO){
        String key = tenderVO.getId().toString();
        String code = tenderVO.getCode()!=null?tenderVO.getCode():"";
        String name = tenderVO.getTitle()!=null?tenderVO.getTitle():"";
        StringBuilder txt = new StringBuilder();
        txt.append(code).append(name).append("(").append(key).append(")");
        
        return txt.toString();
    }
    
    public void batchUpdateTenderStatus(TcUser admin){
        logger.debug("batchUpdateTenderStatus() begin");
        TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
//        criteriaVO.setClosed(Boolean.FALSE);
//        criteriaVO.setActive(Boolean.TRUE);//排除停用(已刪除)
//        criteriaVO.setNotDraft(Boolean.TRUE);//非草稿狀態
        
        //針對需要排程 依時間去更新的狀態(未開始 標書發售中 投標中 評標中)
        List<String> statusList = new ArrayList<>();
        statusList.add(TenderStatusEnum.NOT_SALE.getCode());
        statusList.add(TenderStatusEnum.ON_SALE.getCode());
        statusList.add(TenderStatusEnum.ON_TENDER.getCode());
        statusList.add(TenderStatusEnum.VERIFY.getCode());
        criteriaVO.setStatusList(statusList);
        
        List<TenderVO> notcloselist = findByCriteria(criteriaVO);
        logger.debug("updateTenderStatus not closed: {} ", notcloselist.size());
        Date now = new Date();
        for(TenderVO vo : notcloselist){
            String status = "";
            if(vo.getCloseDate()!=null && vo.getCloseDate().before(now)){//已決標
                status = TenderStatusEnum.END.getCode();
            }else if(vo.getVerifyDate()!=null && vo.getVerifyDate().before(now)){//評標中
                status = TenderStatusEnum.VERIFY.getCode();
            }else if(vo.getStenderDate()!=null && vo.getStenderDate().before(now) 
                    && vo.getEtenderDate()!=null&& now.before(vo.getEtenderDate())){//投標中
                status = TenderStatusEnum.ON_TENDER.getCode();
            }else if(vo.getSsaleDate()!=null && vo.getSsaleDate().before(now) 
                    && vo.getEsaleDate()!=null && now.before(vo.getEsaleDate())){//標書發售中
                status = TenderStatusEnum.ON_SALE.getCode();
            }else{
                logger.debug("batchUpdateTenderStatus another status");
            }
            
            if(StringUtils.isNotBlank(status) && !status.equals(vo.getStatus())){//狀態改變
                logger.debug("batchUpdateTenderStatus Tender id:{}", vo.getId());
                logger.debug("batchUpdateTenderStatus Tender status:{} to {}", vo.getStatus(), status);
                vo.setStatus(status);
                this.saveVO(vo, admin, false);
            }
        }
        
        logger.debug("batchUpdateTenderStatus() begin");
    }
    
    public void batchPublishNotification(TcUser admin){
        
        TenderCriteriaVO criteriaVO = new TenderCriteriaVO();
        criteriaVO.setNotice(Boolean.TRUE);

        //針對需要排程 依發佈時間去通知的狀態(未開始 標書發售中 投標中)
        List<String> statusList = new ArrayList<>();
        statusList.add(TenderStatusEnum.NOT_SALE.getCode());
        statusList.add(TenderStatusEnum.ON_SALE.getCode());
        statusList.add(TenderStatusEnum.ON_TENDER.getCode());
//        statusList.add(TenderStatusEnum.END.getCode());
        criteriaVO.setStatusList(statusList);
        
        
        List<TenderVO> allTender = findByCriteria(criteriaVO);
        
        Map<String, List<TenderVO>> niticeMap = new HashMap<>();//key:email,value:通知案件
        if (CollectionUtils.isNotEmpty(allTender)) {
            logger.debug("batchPublishNotification allTender{}", allTender.size());
            for(TenderVO vo : allTender){
                Long categoryId = vo.getCategoryId();
                
                //依案件類別 查通知對象
                List<MemberVO> memberList = etMemberFacade.findNoticeMemberByCategoryId(categoryId);
                
                if (CollectionUtils.isNotEmpty(memberList)) {
                    for(MemberVO member:memberList){
                        List<TenderVO> list = niticeMap.get(member.getEmail());
                        if (CollectionUtils.isNotEmpty(list)) {
                            list.add(vo);
                            niticeMap.put(member.getEmail(), list);
                        }else{
                            list = new ArrayList<>();
                            list.add(vo);
                            niticeMap.put(member.getEmail(), list);
                        }
                    }
                }
            }
            
            for (Map.Entry<String, List<TenderVO>> entry : niticeMap.entrySet()) {
                String email = entry.getKey();
                List<TenderVO> list = entry.getValue();
                logger.info("batchPublishNotification entry{} {}", email, list.size());
                StringBuilder sbTitle = new StringBuilder().append(GlobalConstant.EMAIL_SUBJECT_PREFIX).append("案件發佈通知");
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put(VelocityMail.SUBJECT, sbTitle.toString());
                NotificationUtils notificationUtils = NotificationUtils.getInstance();
                
                if(GlobalConstant.DEBUG_MODE){
                    parameters.put(VelocityMail.TO, sys.getNotifyAdmins());
                }else{
                    parameters.put(VelocityMail.TO, email);
                }
                //BCC收件人 admin 上線初期加發
                String bcc = sys.getNotifyAdmins();
                parameters.put(VelocityMail.BCC, bcc);

                // HTML 樣板內參數
                Map<String, Object> mailBean = new HashMap<>();
                //                    mailBean.put("summaryTotal1", list.size());
                mailBean.put("list", list);

                VelocityMail.sendMail(parameters, mailBean, "tenderPublish.vm");

            }
            logger.info("batchPublishNotification niticeMap{}", niticeMap.size());
        }else{
            logger.debug("batchPublishNotification no data to notify");
        }
        
        
        
    }
    
}

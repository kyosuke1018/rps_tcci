/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.fc.util.StringUtils;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.entity.KbLink;
import com.tcci.et.entity.KbPhotoGallery;
import com.tcci.et.entity.KbPublication;
import com.tcci.et.entity.KbRichContent;
import com.tcci.et.enums.ContentStatusEnum;
import com.tcci.et.enums.DataTypeEnum;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.LinkEnum;
import com.tcci.et.enums.PhotoGalleryEnum;
import com.tcci.et.enums.PublicationEnum;
import com.tcci.et.enums.RichContentEnum;
import com.tcci.et.enums.SaveTypeEnum;
import com.tcci.et.model.PublicationVO;
import com.tcci.et.model.criteria.PublicationCriteriaVO;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class KbPublicationFacade extends AbstractFacade<KbPublication> {
    @EJB private TcDomainFacade domainFacade;
    @EJB private KbRichContentFacade richContentFacade;
    @EJB private KbLinkFacade linkFacade;
    @EJB private KbPhotoGalleryFacade photoGalleryFacade;
    @EJB private AttachmentFacade attachmentFacade;
    @EJB private ImageFacade imageFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KbPublicationFacade() {
        super(KbPublication.class);
    }
    
    /**
     * 刪除兩小時前的暫存資料
     * @param user
     * @param simulated
     */
    public void deleteTemp(TcUser user){
        StringBuilder sql = new StringBuilder();
        sql.append("delete from KB_PUBLICATION \n")
           .append("where TYPE='").append(PublicationEnum.TEMP.getCode()).append("' \n")
//           .append("and CREATETIMESTAMP<DATEADD(HH, -2, GETDATE()) \n");// for MSSQL
           .append("and CREATETIMESTAMP<(SYSDATE - 1/12) \n");// for Oracle
        
        if( user!=null && user.getId()!=null ){
            sql.append("and CREATOR=").append(user.getId());
        }
        
        Query q = em.createNativeQuery(sql.toString());
        q.executeUpdate();
    }
    
    /**
     * save with rich content
     * @param pubVO
     * @param rcEntity
     * @param operator 
     * @throws java.io.UnsupportedEncodingException 
     */
    public void saveRichContent(PublicationVO pubVO, KbRichContent rcEntity, TcUser operator, boolean simulated) throws UnsupportedEncodingException, Exception{
        logger.debug("saveRichContent pubVO.getId() = "+pubVO.getId());
        
        KbPublication entity = (pubVO.getId()!=null && pubVO.getId()>=0)? this.find(pubVO.getId()):new KbPublication();
        copyVoToEntity(pubVO, entity);
        save(entity, operator, simulated);
        copyEntityToVo(entity, pubVO);
        
        rcEntity.setPrimaryType(RichContentEnum.PUBLICATION.getCode());
        rcEntity.setPrimaryId(pubVO.getId());
        //rcEntity.setContents(pubVO.getContents()); Cause java.sql.DataTruncation: Data truncation
        // for admin UI query (DB 只存過濾 TAG 後的部分資料)
        String noHtml = pubVO.getContents().replaceAll("\\<.*?>","|");
        String safeContent = StringUtils.safeTruncat(noHtml, GlobalConstant.ENCODING_DEF, 100, GlobalConstant.RICHCONTENT_SUMMARY_LEN);// 內容長度檢核
        rcEntity.setContents(safeContent);
        rcEntity.setSaveType(SaveTypeEnum.FILE.getCode());// 存至 FVVAULT
        richContentFacade.save(rcEntity, operator, simulated);
        
        // RichContent 存至 FVVAULT (完整HTML資料存至檔案)
        byte[] contents = pubVO.getContents().getBytes(GlobalConstant.ENCODING_DEF);
        String oriFilename = GlobalConstant.RICHCONTENT_FILENAME+entity.getId()+GlobalConstant.FILE_EXT_HTML;// 自訂產生原始檔名

        TcDomain domain = domainFacade.getDefaultDomain();
        AttachmentVO attachmentVO = attachmentFacade.genAttachmentVO(oriFilename, GlobalConstant.CONTENTTYPE_HTML, contents, 0);
        attachmentFacade.saveContentSingle(domain, rcEntity, attachmentVO, simulated);// 新增關聯檔，原關聯刪除(實體檔暫不刪除)
        
        // 檢查是否有原本自圖庫取圖，又刪除的狀況。需刪除關聯資料
        // 文章插圖依最新自訂網頁內容部分移除
        photoGalleryFacade.removeDocImagesByContent(pubVO.getId(), pubVO.getContents(), operator, simulated);
        
        // 回傳結果
        copyEntityToVo(entity, pubVO);
        pubVO.setHtmlId(rcEntity.getId());
    }
    
    /**
     * 儲存連結類型文章
     * @param pubVO
     * @param lkEntity
     * @param operator 
     * @param simulated 
     */
    public void saveLink(PublicationVO pubVO, KbLink lkEntity, TcUser operator, boolean simulated){
        KbPublication entity = (pubVO.getId()!=null && pubVO.getId()>=0)? this.find(pubVO.getId()):new KbPublication();
        copyVoToEntity(pubVO, entity);
        save(entity, operator, simulated);
        copyEntityToVo(entity, pubVO);
        
        logger.debug("saveLink pubVO.getId() = "+pubVO.getId());
        
        lkEntity.setPrimaryType(LinkEnum.DOC.getCode());
        lkEntity.setPrimaryid(pubVO.getId());
        lkEntity.setUrl(pubVO.getUrl());
        lkEntity.setOpenMethod(pubVO.getOpenMethod());

        linkFacade.save(lkEntity, operator, simulated);
        // 回傳結果
        copyEntityToVo(entity, pubVO);
        pubVO.setLinkId(lkEntity.getId());
    }
    
    /**
     * 儲存 Publication
     * @param pubVO
     * @param operator 
     * @param simulated 
     * @throws java.lang.Exception 
     */
    public void saveAll(PublicationVO pubVO, TcUser operator, boolean simulated) throws Exception{
        if( DataTypeEnum.FOLDER.getCode().equals(pubVO.getDataType()) ){// 資料夾
            logger.debug("saveByVO "+pubVO.getDataType()+"  pubVO.getTitle() = "+pubVO.getTitle());
            saveVO(pubVO, operator, simulated);
        }else if( DataTypeEnum.LINK.getCode().equals(pubVO.getDataType()) ){// 連結
            logger.debug("saveByVO "+pubVO.getDataType()+"  pubVO.getLinkId() = "+pubVO.getLinkId());
            KbLink entity = (pubVO.getLinkId()!=null && pubVO.getLinkId()>0)?linkFacade.find(pubVO.getLinkId()):new KbLink();
            saveLink(pubVO, entity, operator, simulated);
        }else if( DataTypeEnum.HTML.getCode().equals(pubVO.getDataType()) ){// 自訂網頁
            logger.debug("saveByVO "+pubVO.getDataType()+"  pubVO.getHtmlId() = "+pubVO.getHtmlId());
            KbRichContent entity = (pubVO.getHtmlId()!=null && pubVO.getHtmlId()>0)?richContentFacade.find(pubVO.getHtmlId()):new KbRichContent();
            saveRichContent(pubVO, entity, operator, simulated);
        }else if( DataTypeEnum.UPLOAD.getCode().equals(pubVO.getDataType()) ){// 上傳檔
            saveVO(pubVO, operator, simulated);// 先取得 pubVO.id
            logger.debug("saveByVO "+pubVO.getDataType()+" pubVO.getId() = "+pubVO.getId());
        }
    }
    
    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     * @param simulated 
     */
    public void save(KbPublication entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytimestamp(new Date());
                this.edit(entity, simulated);
            }else{
                entity.setCreator(operator);
                entity.setCreatetimestamp(new Date());
                this.create(entity, simulated);
            }
        }
    }
    public void saveVO(PublicationVO pubVO, TcUser operator, boolean simulated){
        KbPublication entity = (pubVO.getId()!=null)?find(pubVO.getId()):new KbPublication();
        this.copyVoToEntity(pubVO, entity);
        this.save(entity, operator, simulated);
        this.copyEntityToVo(entity, pubVO);
    }
    
    /**
     * 複製
     * @param entity
     * @param vo
     */
    public void copyEntityToVo(KbPublication entity, PublicationVO vo){
        ExtBeanUtils.copyProperties(vo, entity);
        
        vo.setLastTime(vo.getLastUpdateTime());
        vo.setLastUserName((vo.getLastUpdateUser()!=null)?vo.getLastUpdateUser().getCname():null);
    }
    public void copyVoToEntity(PublicationVO vo, KbPublication entity){
        ExtBeanUtils.copyProperties(entity, vo);
    }

    /**
     * 刪除文章 
     * @param pubVO
     * @param operator
     * @param simulated
     * @throws java.io.IOException
     */
    public void deleteDoc(PublicationVO pubVO, TcUser operator, boolean simulated) throws IOException {
        if( pubVO==null || pubVO.getId()==null ){
            logger.error("deleteDoc pubVO==null || pubVO.getId()==null");
            return;
        }else{
            KbPublication entity = this.find(pubVO.getId());
            if( entity==null ){
                logger.error("deleteDoc not exists pubVO.getId()="+pubVO.getId()+", pubVO.getDataType()="+pubVO.getDataType());
                return;
            }
        }
        
        if( DataTypeEnum.FOLDER.getCode().equals(pubVO.getDataType()) ){
            logger.debug("deleteDoc "+pubVO.getDataType()+", pubVO.getId() = "+pubVO.getId());
            remove(pubVO.getId(), simulated);
        }else if( DataTypeEnum.LINK.getCode().equals(pubVO.getDataType()) ){
            logger.debug("deleteDoc "+pubVO.getDataType()+", pubVO.getLinkId() = "+pubVO.getLinkId());
            linkFacade.remove(pubVO.getLinkId(), simulated);
            remove(pubVO.getId(), simulated);
        }else if( DataTypeEnum.HTML.getCode().equals(pubVO.getDataType()) ){
            logger.debug("deleteDoc "+pubVO.getDataType()+", pubVO.getHtmlId() = "+pubVO.getHtmlId());
            KbRichContent entity = richContentFacade.find(pubVO.getHtmlId());
            if( entity!=null ){
                attachmentFacade.removeContent(entity, simulated);
                richContentFacade.remove(entity, simulated);
            
                // 自圖庫取圖 - 移除文章插圖關聯 & 直接上傳插圖 - 移除圖片
                photoGalleryFacade.removeDocImagesAll(pubVO.getId(), operator, simulated);
            }
            remove(pubVO.getId(), simulated);
        }else if( DataTypeEnum.UPLOAD.getCode().equals(pubVO.getDataType()) ){
            logger.debug("deleteDoc "+pubVO.getDataType()+", pubVO.getId() = "+pubVO.getId());
            KbPublication entity = find(pubVO.getId());
            if( entity!=null ){
                attachmentFacade.removeContent(entity, simulated);
            }
            remove(pubVO.getId(), simulated);
        }
    }
    
    /**
     * PublicationVO 關聯檔案
     * @param pubVO
     * @return 
     */
    public List<AttachmentVO> findFiles(PublicationVO pubVO){
        KbPublication entity = new KbPublication();
        copyVoToEntity(pubVO, entity);

        List<AttachmentVO> files = attachmentFacade.loadContent(entity);// 關聯上傳檔
        return files;
    }
    
    /**
     * 文章類共用查詢SQL
     * @param criteriaVO
     * @return 
     */
    public String genCommonFindSQL(PublicationCriteriaVO criteriaVO){
        boolean fullData = (criteriaVO!=null)? criteriaVO.isFullData():false;
        boolean countOnly = (criteriaVO!=null)? criteriaVO.isCountOnly():false;
        
        StringBuilder sql = new StringBuilder();

        PublicationEnum publicationEnum = (criteriaVO!=null)?PublicationEnum.getFromCode(criteriaVO.getType()):null;
        
        if( countOnly ){
            sql.append("SELECT count(S.ID) \n");
        }else{
            sql.append("SELECT S.* \n");
            sql.append(", P0.TITLE FOLDERTITLE \n");
            sql.append(", TL.ID LINKID, TL.URL, TL.OPENMETHOD \n");
            if( fullData ){
                sql.append(", TH.ID HTMLID, TH.CONTENTS \n");
            }else{
                sql.append(", TH.ID HTMLID \n");// 效能考量
            }
            sql.append(", CASE WHEN S.MODIFYTIMESTAMP IS NULL THEN U1.CNAME ELSE U2.CNAME END LASTUSERNAME \n"); 
            sql.append(", CASE WHEN S.MODIFYTIMESTAMP IS NULL THEN S.CREATETIMESTAMP ELSE S.MODIFYTIMESTAMP END LASTTIME \n");
            
            // 封面圖示
            if( criteriaVO!=null && publicationEnum!=null && publicationEnum.getHasCoverImg() ){
                sql.append(", F.DOMAIN as coverDomain, F.FILENAME as coverFilename, F.CONTENTTYPE as coverContenttype \n");
            }
        }
        sql.append("FROM KB_PUBLICATION S \n");
        
        sql.append("LEFT OUTER JOIN KB_PUBLICATION P0 ON P0.ID=S.PARENT \n"); 
        sql.append("LEFT OUTER JOIN KB_LINK TL ON S.DATATYPE='")
                .append(DataTypeEnum.LINK.getCode()).append("' AND TL.PRIMARYTYPE='")
                .append(LinkEnum.DOC.getCode()).append("' AND PRIMARYID=S.ID \n");// 網址
        sql.append("LEFT OUTER JOIN KB_RICH_CONTENT TH ON S.DATATYPE='")
                .append(DataTypeEnum.HTML.getCode()).append("' AND TH.PRIMARYTYPE='")
                .append(RichContentEnum.PUBLICATION.getCode())
                .append("' AND TH.PRIMARYID=S.ID \n"); // 自訂網頁

        // 封面圖示
        if( criteriaVO!=null && publicationEnum!=null && publicationEnum.getHasCoverImg() ){
            sql.append("LEFT OUTER JOIN ( \n"); 
            sql.append("    select p.PRIMARYID, a.* \n");
            sql.append("    from TC_FVITEM a \n");
            sql.append("    join TC_APPLICATIONDATA b on b.FVITEM=a.ID and b.CONTAINERCLASSNAME='").append(KbPhotoGallery.class.getName()).append("' \n");
            sql.append("    join KB_PHOTO_GALLERY p on p.ID=b.CONTAINERID and IDENTIFY_IMG=1 \n");// 利用 IDENTIFY_IMG 標示為封面圖示
            sql.append("        and p.PRIMARYTYPE='").append(PhotoGalleryEnum.DOC.getCode()).append("' \n");
            sql.append(") F ON F.PRIMARYID=S.ID \n");
        }
        
        sql.append("LEFT OUTER JOIN TC_USER U1 ON U1.ID=S.CREATOR \n"); 
        sql.append("LEFT OUTER JOIN TC_USER U2 ON U2.ID=S.MODIFIER \n"); 
        sql.append("WHERE 1=1 \n"); 
        
        return sql.toString();
    }
    
    /**
     * 依查詢條件抓取資料
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String getSqlByCriteria(PublicationCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append(genCommonFindSQL(criteriaVO));
        
        if( criteriaVO!=null ){
            // type
            if( criteriaVO.getType()!=null ){
                sql.append("AND S.TYPE=#type \n");
                params.put("type", criteriaVO.getType());
            }
            // DATATYPE
            if( criteriaVO.getDataType()!=null ){
                sql.append("AND S.DATATYPE=#datatype \n");
                params.put("datatype", criteriaVO.getDataType());
            }
            // Doc Only
            if( criteriaVO.getDocOnly()!=null ){
                if( criteriaVO.getDocOnly() ){
                    sql.append("AND S.DATATYPE <> #DATATYPE \n");
                    params.put("DATATYPE", DataTypeEnum.FOLDER.getCode());
                }
            }
            // Status
            if( criteriaVO.getStatus()!=null ){
                sql.append("AND S.STATUS=#status \n");
                params.put("status", criteriaVO.getStatus());
            }
            // ID
            if( criteriaVO.getId()!=null ){
                sql.append("AND S.ID=#id \n");
                params.put("id", criteriaVO.getId());
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
            if( criteriaVO.getParent()!=null ){
                sql.append("AND S.PARENT=#parent \n");
                params.put("parent", criteriaVO.getParent());
            }
            // News
            if( criteriaVO.getNews()!=null && criteriaVO.getNews() ){
                sql.append("AND S.NEWS=1 \n");
            }
            // 發佈日期
            if( criteriaVO.getStartDate()!=null ){
                sql.append("AND S.DATADATE>=#StartDate \n");
                params.put("StartDate", criteriaVO.getStartDate());
            }
            if( criteriaVO.getEndDate()!=null ){
                sql.append("AND S.DATADATE<=#EndDate \n");
                params.put("EndDate", criteriaVO.getEndDate());
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
                sql.append("AND (UPPER(S.TITLE) LIKE #keywork OR UPPER(S.SUMMARY) LIKE #keywork) \n");
                params.put("keywork", keyword);
            }
        }
        
        return sql.toString();
    }
    public int countByCriteria(PublicationCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        criteriaVO.setCountOnly(true);
        sql.append(getSqlByCriteria(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public List<PublicationVO> findByCriteria(PublicationCriteriaVO criteriaVO){
        if( criteriaVO==null ){
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        criteriaVO.setCountOnly(false);
        sql.append(getSqlByCriteria(criteriaVO, params));
        
        // order by 
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY NVL(S.MODIFYTIMESTAMP, S.CREATETIMESTAMP) DESC");
        }
        
        logger.debug("findByCriteria ..."+sql);
        ResultSetHelper<PublicationVO> resultSetHelper = new ResultSetHelper(PublicationVO.class);
        List<PublicationVO> resList;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            resList = selectBySql(PublicationVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
//            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            resList = selectBySql(PublicationVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
//            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            resList = selectBySql(PublicationVO.class, sql.toString(), params);
//            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        }
        
        // 準備照片　URL
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
        }
        
        return resList;
    }
    public PublicationVO findById(Long id, boolean publicOnly){
        if( id==null || id<=0 ){
            logger.error("findById id==null || id<=0");
            return null;
        }
        PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(true);// 單筆取所有資料
        if( publicOnly ){
            criteriaVO.setStatus(ContentStatusEnum.PUBLISH.getCode());
        }
        List<PublicationVO> resList = findByCriteria(criteriaVO);
        
        PublicationVO pubVO = (resList!=null && resList.size()>0)?resList.get(0):null;
        // 取得文章內容或關聯檔
        getDocContents(pubVO);
        
        return pubVO;
    }
    // for 固定網頁、預覽暫存網頁
    public PublicationVO fundHtmlPageByCode(String pubType, String code, String status){
        if( pubType==null || pubType.isEmpty() || code==null || code.isEmpty() ){
            logger.error("fundFixPageByCode pubType==null || pubType.isEmpty() || code==null || code.isEmpty()");
            return null;
        }
        
        PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
        criteriaVO.setType(pubType);
        criteriaVO.setCode(code);
        criteriaVO.setFullData(true);// 單筆取所有資料
        if( status!=null ){ criteriaVO.setStatus(status); }
        
        List<PublicationVO> vo = findByCriteria(criteriaVO);
        
        if( vo!=null && !vo.isEmpty() ){
            PublicationVO pubVO = vo.get(0);
            // 取得文章內容或關聯檔
            getDocContents(pubVO);
            return pubVO;
        }else{
            return null;
        }
    }
    
    public List<PublicationVO> findFolders(PublicationEnum type){
        PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
        criteriaVO.setType(type!=null? type.getCode():null);
        criteriaVO.setDataType(DataTypeEnum.FOLDER.getCode());
        
        return findByCriteria(criteriaVO);
    }

    /**
     * 取得文章內容或關聯檔
     * @param pubVO 
     */
    public void getDocContents(PublicationVO pubVO){
        if( pubVO!=null ){
            if( pubVO.getTypeEnum().getHasCoverImg() ){
                photoGalleryFacade.loadCoverImage(pubVO);// 取得封面圖示
            }
            
            if( pubVO.getDataTypeEnum()==DataTypeEnum.UPLOAD ){
                pubVO.setDocs(findFiles(pubVO));
            }else if( pubVO.getDataTypeEnum()==DataTypeEnum.HTML ){
                // 自 FVVAULT 讀取檔案內容
                KbRichContent kbRichContent = new KbRichContent(pubVO.getHtmlId());
                List<AttachmentVO> list = attachmentFacade.loadContent(kbRichContent);
                logger.debug("getDocContents pubVO.getHtmlId() ="+pubVO.getHtmlId());
                logger.debug("getDocContents list ="+((list!=null)?list.size():0));
                if( list!=null && !list.isEmpty() ){
                    AttachmentVO attachmentVO = list.get(0);
                    try{
                        byte[] bytes = IOUtils.toByteArray(attachmentFacade.getContentStream(attachmentVO));
                        if( bytes!=null ){
                            String contents = new String(bytes, GlobalConstant.ENCODING_DEF);
                            pubVO.setContents(contents);
                            logger.debug("getDocContents get Content from file.");
                        }
                    }catch(Exception e){
                        logger.error("getDocContents read rich content Exception:\n", e);
                    }
                }
            }
        }
    }
    
    /**
     * HTML 格式文章，依據關聯 HTMLID 查 PublicationVO
     * @param htmlId
     * @return 
     */
    public PublicationVO findByHtmlId(Long htmlId){
        PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
        criteriaVO.setHtmlId(htmlId);
        criteriaVO.setDataType(DataTypeEnum.HTML.getCode());
        
        List<PublicationVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
}

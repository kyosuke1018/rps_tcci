/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.facade.global.ImageFileFilter;
import com.tcci.cm.model.global.ImageVO;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.util.FileUtils;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.entity.KbPhotoGallery;
import com.tcci.et.enums.ContentStatusEnum;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.PhotoGalleryEnum;
import com.tcci.et.model.PhotoGalleryVO;
import com.tcci.et.model.PublicationVO;
import com.tcci.et.model.RandomSelectVO;
import com.tcci.et.model.criteria.MediaCriteriaVO;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class KbPhotoGalleryFacade extends AbstractFacade<KbPhotoGallery> {
    @EJB AttachmentFacade attachmentFacade;
    @EJB ImageFacade imageFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KbPhotoGalleryFacade() {
        super(KbPhotoGallery.class);
    }

    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     * @param simulated 
     */
    public void save(KbPhotoGallery entity, TcUser operator, boolean simulated){
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
    public void saveVO(PhotoGalleryVO pubVO, TcUser operator, boolean simulated){
        KbPhotoGallery entity = (pubVO.getId()!=null)?find(pubVO.getId()):new KbPhotoGallery();
        this.copyVoToEntity(pubVO, entity);
        this.save(entity, operator, simulated);
        this.copyEntityToVo(entity, pubVO);
    }
    
    /**
     * 儲存圖片資訊
     * @param entity
     * @param imageVO
     * @param operator 
     * @param simulated 
     */
    public void saveImageInfo(KbPhotoGallery entity, ImageVO imageVO, TcUser operator, boolean simulated){
        entity.setWidth(imageVO.getWidth());
        entity.setHeight(imageVO.getHeight());
        entity.setCompressed(imageVO.getCompressed());
        
        save(entity, operator, simulated);
    }
    
    /**
     * 完整圖檔儲存(含檔案處理)
     * @param parent
     * @param primaryType
     * @param primaryId
     * @param subject
     * @param description
     * @param status
     * @param identifyImg
     * @param fullFileName
     * @param domain
     * @param operator
     * @param simulated
     * @return
     * @throws IOException
     * @throws Exception 
     */
    public KbPhotoGallery saveFullImageData(Long parent, 
        String primaryType, Long primaryId, 
        String subject, String description, 
        String status,
        Boolean identifyImg,
        String fullFileName,
        TcDomain domain,
        TcUser operator, boolean simulated) throws IOException, Exception{
        File fs = new File(fullFileName);
        String fileName = fs.getName();
        // 讀取檔案
        byte[] content = FileUtils.readFileToByteArray(fs);
        
        return saveFullImageData(parent, 
                    primaryType, primaryId, 
                    subject, description, 
                    status,
                    identifyImg,
                    fileName,
                    content,
                    domain,
                    operator, 
                    simulated);
    }
    public KbPhotoGallery saveFullImageData(Long parent, 
        String primaryType, Long primaryId, 
        String subject, String description, 
        String status,
        Boolean identifyImg,
        String fileName,
        byte[] content,
        TcDomain domain,
        TcUser operator, boolean simulated) throws IOException, Exception{
        //File fs = new File(fullFileName);
        //String fileName = fs.getName();
        // 讀取檔案
        //byte[] content = FileUtils.readFileToByteArray(fs);
        
        if( content!=null && content.length>10240 ){// 原圖小於10K應該有問題，不匯入
            // 建立 KbPhotoGallery
            KbPhotoGallery pgEntity = prepareImageHolder(parent, 
                    primaryType, primaryId, 
                    subject, // subject
                    description, // description
                    status, identifyImg, operator, simulated);

            boolean res = attachmentFacade.saveContentToFvVault(domain, pgEntity, fileName, content, GlobalConstant.IMG_JPG_CONTENT_TYPE, 0, operator);
            logger.info("saveFullImageData saveContent fileName = "+fileName+", res = "+res);

            // 縮圖
            if( res ){
                ImageVO retImgVO = new ImageVO();
                String retMsg = imageFacade.compressImageByHolder(pgEntity, retImgVO);
                logger.info("importImages compressImage fileName = "+fileName+", retMsg = "+retMsg);
                // 儲存寬高資訊
                saveImageInfo(pgEntity, retImgVO, operator, simulated);
            }

            return pgEntity;
        }
        return null;
    }
    
    /**
     * 刪除圖片
     * @param Id
     * @param operator 
     * @param simulated 
     */
    public void removePhotoById(Long Id, TcUser operator, boolean simulated){
        logger.debug("removePhoto Id = "+Id+", operator="+operator.getLoginAccount());
        KbPhotoGallery entity = find(Id);
        removePhoto(entity, operator, simulated);
    }
    public void removePhoto(KbPhotoGallery entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            logger.debug("removePhoto entity = "+entity+", operator="+operator.getLoginAccount());
            remove(entity, simulated);
            attachmentFacade.removeContent(entity, simulated);
        }
    }
    
    /**
     * 複製
     * @param entity
     * @param vo
     */
    public void copyEntityToVo(KbPhotoGallery entity, PhotoGalleryVO vo){
        ExtBeanUtils.copyProperties(vo, entity);
        
        vo.setLastTime(vo.getLastUpdateTime());
        vo.setLastUserName((vo.getLastUpdateUser()!=null)?vo.getLastUpdateUser().getCname():null);
    }
    public void copyVoToEntity(PhotoGalleryVO vo, KbPhotoGallery entity){
        ExtBeanUtils.copyProperties(entity, vo);
    }
        
    /**
     * 植物分類共用查詢SQL
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String genCommonFindSQL(MediaCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        if( criteriaVO!=null && criteriaVO.isCountOnly() ){
            sql.append("SELECT COUNT(*) CC \n"); 
        }else{
            sql.append("SELECT S.*, C.SUBCOUNTS, IMG.SUBCOUNTS IMGCOUNTS, FD.SUBCOUNTS FOLDERCOUNTS \n");
            if( criteriaVO!=null && criteriaVO.getLang()!=null && criteriaVO.getLang().endsWith(LanguageEnum.ENGLISH.getShortCode()) ){
                sql.append(", NVL(P.ENAME, P.CNAME) as PARENTNAME \n");
            }else{
                sql.append(", P.CNAME as PARENTNAME \n");
            }
            if( criteriaVO!=null && criteriaVO.isGetFvInfo() ){// 效能考量-直接取得 attachmentVO 資訊 (1對1適用)
                sql.append(", FV.APPID, FV.CONTAINERCLASSNAME, FV.CONTAINERID \n");
                sql.append(", FV.FVITEMID \n");
                sql.append(", FV.DOMAIN, FV.FILENAME, FV.ORIFILENAME, FV.CONTENTTYPE, FV.FILESIZE \n");
                sql.append(", FV.DOMAINNAME \n");
                sql.append(", FV.LOCATION \n");
            }

            // for 文章插圖
            if( criteriaVO!=null && PhotoGalleryEnum.DOC.getCode().equals(criteriaVO.getPrimaryType()) ){
                sql.append(", PUB.TITLE, PUB.SUMMARY \n");
            }

            sql.append(", CASE WHEN S.MODIFYTIMESTAMP IS NULL THEN U1.CNAME ELSE U2.CNAME END LASTUSERNAME \n");
            sql.append(", CASE WHEN S.MODIFYTIMESTAMP IS NULL THEN S.CREATETIMESTAMP ELSE S.MODIFYTIMESTAMP END LASTTIME \n");
        }
        
        sql.append("FROM KB_PHOTO_GALLERY S \n");
        
        if( criteriaVO!=null && criteriaVO.isGetFvInfo() ){// 效能考量-直接取得 attachmentVO 資訊 (1對1適用)
            sql.append("JOIN ( \n");// 檔案資訊
            sql.append(attachmentFacade.getPhotoFVSQL());
            sql.append(") FV on FV.CONTAINERID=S.ID \n");
        }
        
        // for 文章插圖
        if( criteriaVO!=null && PhotoGalleryEnum.DOC.getCode().equals(criteriaVO.getPrimaryType()) ){
            sql.append("JOIN KB_PUBLICATION PUB ON PUB.ID=S.PRIMARYID \n");
            
            if( criteriaVO.isUseDocCriteria() && criteriaVO.getDocCriteriaVO()!=null ){
                if( criteriaVO.getDocCriteriaVO().getType()!=null ){
                    sql.append("    AND PUB.TYPE=#DOCTYPE \n");
                    params.put("DOCTYPE", criteriaVO.getDocCriteriaVO().getType());
                }
            }
        }

        // 子項目(項目與圖片)
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT PARENT, COUNT(*) SUBCOUNTS \n");
        sql.append("    FROM KB_PHOTO_GALLERY \n");
        sql.append("    WHERE 1=1 \n");
        // 有無包含圖片
        if( criteriaVO!=null && criteriaVO.isIncMedia()!=null ){
            sql.append("    AND PRIMARYTYPE='").append(PhotoGalleryEnum.IMAGE.getCode()).append("' \n");
        }
        // status
        if( criteriaVO!=null && criteriaVO.getStatus()!=null ){
            sql.append("    AND STATUS=#STATUS_SUB \n");
            params.put("STATUS_SUB", criteriaVO.getStatus());
        }
        sql.append("    GROUP BY PARENT \n"); 
        sql.append(") C ON C.PARENT=S.ID \n");

        // 子圖片
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT PARENT, COUNT(*) SUBCOUNTS \n");
        sql.append("    FROM KB_PHOTO_GALLERY \n");
        sql.append("    WHERE 1=1 \n");
        sql.append("    AND PRIMARYTYPE='").append(PhotoGalleryEnum.IMAGE.getCode()).append("' \n");
        sql.append("    GROUP BY PARENT \n"); 
        sql.append(") IMG ON IMG.PARENT=S.ID \n");
        // 子資料夾
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT PARENT, COUNT(*) SUBCOUNTS \n");
        sql.append("    FROM KB_PHOTO_GALLERY \n");
        sql.append("    WHERE 1=1 \n");
        sql.append("    AND PRIMARYTYPE='").append(PhotoGalleryEnum.CUSTOM.getCode()).append("' \n");
        sql.append("    GROUP BY PARENT \n"); 
        sql.append(") FD ON FD.PARENT=S.ID \n");
        
        // 所在相簿
        //if( criteriaVO!=null && criteriaVO.getParents()!=null && !criteriaVO.getParents().isEmpty() ){
        sql.append("LEFT OUTER JOIN KB_PHOTO_GALLERY P ON P.ID=S.PARENT \n");
        //}
        
        sql.append("LEFT OUTER JOIN TC_USER U1 ON U1.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN TC_USER U2 ON U2.ID=S.MODIFIER \n");        
        sql.append("WHERE 1=1 \n");
        
        // 有無包含圖片
        if( criteriaVO!=null && criteriaVO.isIncMedia()!=null ){
            sql.append("AND S.CODE IS NULL \n");
            if( criteriaVO.isIncMedia() ){
                sql.append("AND C.SUBCOUNTS>0 \n");
            }else{
                sql.append("AND C.SUBCOUNTS IS NULL \n");
            }
        }
                
        return sql.toString();
    }
    
    public String genCommonCriteriaSQL(MediaCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        if( criteriaVO!=null ){
            // parent
            if( criteriaVO.getParent()!=null ){
                sql.append("AND S.PARENT=#PARENT \n");
                params.put("PARENT", criteriaVO.getParent());
            }
            // Parents
            if( criteriaVO.getParents()!=null && !criteriaVO.getParents().isEmpty() ){
                sql.append(NativeSQLUtils.getInSQL("S.PARENT", criteriaVO.getParents(), params)).append(" \n");
            }
            // exCludeParents
            if( criteriaVO.getExCludeParents()!=null && !criteriaVO.getExCludeParents().isEmpty() ){
                int startIdx = (criteriaVO.getParents()!=null && !criteriaVO.getParents().isEmpty())? criteriaVO.getParents().size()+1:1;
                sql.append(NativeSQLUtils.getNotInSQL("S.PARENT", criteriaVO.getExCludeParents(), params, startIdx)).append(" \n");
            }
            // primary
            if( criteriaVO.getPrimaryType()!=null ){
                sql.append("AND S.PRIMARYTYPE=#PRIMARYTYPE \n");
                params.put("PRIMARYTYPE", criteriaVO.getPrimaryType());
            }else if( criteriaVO.getTypes()!=null && !criteriaVO.getTypes().isEmpty() ){
                sql.append(NativeSQLUtils.getInSQL("S.PRIMARYTYPE", criteriaVO.getTypes(), params)).append(" \n");
            }
            if( criteriaVO.getPrimaryId()!=null ){
                sql.append("AND S.PRIMARYID=#PRIMARYID \n");
                params.put("PRIMARYID", criteriaVO.getPrimaryId());
            }
            // status
            if( criteriaVO.getStatus()!=null ){
                sql.append("AND S.STATUS=#STATUS \n");
                params.put("STATUS", criteriaVO.getStatus());
            }
            // 關鍵字
            String keyword = criteriaVO.getKeyword();
            if( keyword!=null && !keyword.isEmpty() ){
                keyword = "%" + keyword + "%";
                sql.append("AND (S.CNAME LIKE #keywork OR S.ENAME LIKE #keywork　OR S.SUBJECT LIKE #keywork) \n");
                params.put("keywork", keyword);
            }
            // ID
            if( criteriaVO.getId()!=null ){
                sql.append("AND S.ID=#ID \n");
                params.put("ID", criteriaVO.getId());
            }
            // IDs
            if( criteriaVO.getIds()!=null && !criteriaVO.getIds().isEmpty() ){
                sql.append(NativeSQLUtils.getInSQL("S.ID", criteriaVO.getIds(), params)).append(" \n");
            }
            // exCludeIDs
            if( criteriaVO.getExCludeIds()!=null && !criteriaVO.getExCludeIds().isEmpty() ){
                int startIdx = (criteriaVO.getIds()!=null && !criteriaVO.getIds().isEmpty())? criteriaVO.getIds().size()+1:1;
                sql.append(NativeSQLUtils.getNotInSQL("S.ID", criteriaVO.getExCludeIds(), params, startIdx)).append(" \n");
            }
            
            // 網站語系 (by 資料夾/相簿設定)
            if( criteriaVO.getLang()!=null ){
                if( criteriaVO.getPrimaryType()!=null ){
                    String keyfield = null;
                    if( PhotoGalleryEnum.CUSTOM.getCode().equals(criteriaVO.getPrimaryType()) ){
                        // 資料夾/相簿 本身
                        keyfield = "S.LANG";
                    }else if( PhotoGalleryEnum.IMAGE.getCode().equals(criteriaVO.getPrimaryType()) ){
                        // 包含圖片
                        keyfield = "P.LANG";
                    }
                    
                    if( keyfield!=null ){// ex. "AC":跨語系及繁體中文
                        sql.append(LanguageEnum.genLangCriteriaSQL(keyfield, criteriaVO.getLang(), params)).append(" \n");
                        /*if( criteriaVO.getLang().equals("AC") ){// 跨語系及繁體中文
                            sql.append("AND ").append(keyfield).append(" IN ('").append(LanguageEnum.ALL.getShortCode()).append("','")
                               .append(LanguageEnum.TRANDITIONAL_CHINESE.getShortCode()).append("') \n");
                        }else if( criteriaVO.getLang().equals("AS") ){// 跨語系及簡體中文
                            sql.append("AND ").append(keyfield).append(" IN ('").append(LanguageEnum.ALL.getShortCode()).append("','")
                               .append(LanguageEnum.SIMPLIFIED_CHINESE.getShortCode()).append("') \n");
                        }else if( criteriaVO.getLang().equals("AE") ){// 跨語系及英文
                            sql.append("AND ").append(keyfield).append(" IN ('").append(LanguageEnum.ALL.getShortCode()).append("','")
                               .append(LanguageEnum.ENGLISH.getShortCode()).append("') \n");
                        }else{
                            sql.append("AND ").append(keyfield).append("=#LANG \n");
                            params.put("LANG", criteriaVO.getLang());
                        }*/
                    }
                }
            }
        }
        
        return sql.toString();
    }
    
    /**
     * 依查詢條件抓取資料筆數
     * @param criteriaVO
     * @return 
     */
    public int countByCriteria(MediaCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        criteriaVO.setCountOnly(true);
        sql.append(genCommonFindSQL(criteriaVO, params));
        sql.append(genCommonCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
        
    /**
     * 依查詢條件抓取資料
     * @param criteriaVO
     * @return 
     */
    public List<PhotoGalleryVO> findByCriteria(MediaCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(genCommonFindSQL(criteriaVO, params));
        sql.append(genCommonCriteriaSQL(criteriaVO, params));
        
        // order by 
        if( criteriaVO!=null && criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            // sql.append("ORDER BY S.PARENT, S.CNAME");
            sql.append("ORDER BY NVL(S.MODIFYTIMESTAMP, S.CREATETIMESTAMP) DESC");
        }
        
        logger.debug("findByCriteria ...");
        ResultSetHelper<PhotoGalleryVO> resultSetHelper = new ResultSetHelper(PhotoGalleryVO.class);
        List<PhotoGalleryVO> resList;
        if( criteriaVO!=null && criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO!=null && criteriaVO.getMaxResults()!=null ){
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        }
        
        // 效能考量-直接取得 attachmentVO 資訊 (1對1適用)
        if( criteriaVO!=null && criteriaVO.isGetFvInfo() && resList!=null ){
            PhotoGalleryEnum photoGalleryEnum = PhotoGalleryEnum.getFromCode(criteriaVO.getPrimaryType());
            genPhotoAttachmentInfo(photoGalleryEnum, resList);
        }
        
        return resList;
    }
    public PhotoGalleryVO findById(Long id, boolean getFVInfo){
        if( id==null || id<=0 ){
            logger.error("findById id==null || id<=0");
            return null;
        }
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setGetFvInfo(getFVInfo);
        List<PhotoGalleryVO> resList = findByCriteria(criteriaVO);
        
        return (resList!=null && resList.size()>0)?resList.get(0):null;
    }
    public List<PhotoGalleryVO> findByPrimary(String primaryType, Long primaryId){
        if( primaryType==null || primaryId==null ){
            logger.error("findByPrimary primaryType==null || primaryId==null");
            return null;
        }
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        criteriaVO.setPrimaryType(primaryType);
        criteriaVO.setPrimaryId(primaryId);
        List<PhotoGalleryVO> resList = findByCriteria(criteriaVO);
        
        return resList;
    }

    /**
     * 依DB查詢資料產生attachmentVO 資訊
     * 效能考量-直接取得 attachmentVO 資訊 (1對1適用)
     * @param photoGalleryEnum
     * @param list 
     */
    public void genPhotoAttachmentInfo(PhotoGalleryEnum photoGalleryEnum, List<PhotoGalleryVO> list){
        TcDomain domain = null;
        if( photoGalleryEnum!=null ){
            domain = imageFacade.getDomain(photoGalleryEnum.getDomainName());
        }

        for(PhotoGalleryVO vo : list){
            AttachmentVO attachmentVO = new AttachmentVO();
            TcApplicationdata applicationdata = new TcApplicationdata();
            TcFvitem fvitem = new TcFvitem();
            if( domain==null ){
                PhotoGalleryEnum pgEnum = PhotoGalleryEnum.getFromCode(vo.getPrimaryType());
                if( pgEnum!=null ){
                    domain = imageFacade.getDomain(pgEnum.getDomainName());
                }
            }

            fvitem.setDomain(domain);
            fvitem.setContenttype(vo.getContentType());
            fvitem.setFilename(vo.getFileName());
            fvitem.setFilesize(vo.getFilesize());
            fvitem.setName(vo.getOriFileName());
            fvitem.setId(vo.getFvitemId());

            applicationdata.setFvitem(fvitem);
            applicationdata.setContainerclassname(KbPhotoGallery.class.getName());
            applicationdata.setContainerid(vo.getId());
            applicationdata.setId(vo.getAppid());

            attachmentVO.setApplicationdata(applicationdata);
            attachmentVO.setContentType(vo.getContentType());
            attachmentVO.setFileName(vo.getOriFileName());

            vo.setAttachmentVO(attachmentVO);
        }
    }
    
    /**
     * 回傳包含FVVAULT檔案儲存資訊
     * 原每筆 PhotoGalleryVO 只會關聯一個圖片 (for 控制每個圖片是否可發佈)
     * @param list
     * @return 
     */
    public List<PhotoGalleryVO> findImages(List<PhotoGalleryVO> list){
        logger.debug("findImages ...");
        if( list!=null ){
            for(PhotoGalleryVO pgVO : list){
                KbPhotoGallery entity = new KbPhotoGallery();
                copyVoToEntity(pgVO, entity);

                List<AttachmentVO> vos = attachmentFacade.loadContent(entity);// 關聯上傳檔
                // 原每筆 PhotoGalleryVO 只會關聯一個圖片 (for 控制每個圖片是否可發佈)
                AttachmentVO attachmentVO = (vos!=null && !vos.isEmpty())? vos.get(0):null;
                if( attachmentVO!=null ){
                    pgVO.setAttachmentVO(attachmentVO);
                }
            }
        }
        return list;// 可直接使用原 list
    }
    
    // for 自訂圖庫
    public List<PhotoGalleryVO> findImagesByParent(Long parentId){
        logger.debug("findImagesByParent parentId ="+parentId);
        if( parentId==null ){
            logger.error("findImagesByParent parentId==null");
            return null;
        }
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        criteriaVO.setPrimaryType(PhotoGalleryEnum.IMAGE.getCode());
        criteriaVO.setParent(parentId);
        criteriaVO.setGetFvInfo(true);// 效能考量-直接取得 attachmentVO 資訊 (1對1適用)
        List<PhotoGalleryVO> resList = findByCriteria(criteriaVO);
        
        return resList;
    }
    // for 自訂圖庫 取得 關連 AttachmentVO
    public List<PhotoGalleryVO> findCustomImages(List<PhotoGalleryVO> list){
        logger.debug("findCustomImages ...");
        return findImages(list);// 回傳包含FVVAULT檔案儲存資訊
    }
    // for 蒐藏植物團體照片 取得 關連 AttachmentVO
    public List<PhotoGalleryVO> findColRecordImages(List<PhotoGalleryVO> list){
        logger.debug("findColRecordImages ...");
        return findImages(list);// 回傳包含FVVAULT檔案儲存資訊
    }
    
    // for 文章插圖
    public List<PhotoGalleryVO> findImagesByDoc(MediaCriteriaVO criteriaVO){
        logger.debug("findImagesByDoc ...");
        
        criteriaVO.setUseDocCriteria(true);// 有使用文章專屬條件(需多JOIN 文章 TABLE)
        criteriaVO.setPrimaryType(PhotoGalleryEnum.DOC.getCode());
        criteriaVO.getDocCriteriaVO().setType(criteriaVO.getType());// 文章類別
        
        List<PhotoGalleryVO> resList = findByCriteria(criteriaVO);
        return findDocImages(resList);
    }
    // for 文章插圖 取得 關連 AttachmentVO
    public List<PhotoGalleryVO> findDocImages(List<PhotoGalleryVO> list){
        logger.debug("findDocImages ...");
        // 原每筆 PhotoGalleryVO 可能關聯多個圖片
        List<PhotoGalleryVO> retList = new ArrayList<PhotoGalleryVO>();
        if( list!=null ){
            for(PhotoGalleryVO pgVO : list){
                KbPhotoGallery entity = new KbPhotoGallery();
                copyVoToEntity(pgVO, entity);

                List<AttachmentVO> vos = attachmentFacade.loadContent(entity);// 關聯上傳檔
                if( vos!=null ){
                    for(AttachmentVO attachmentVO : vos){
                        PhotoGalleryVO photoGalleryVO = new PhotoGalleryVO();
                        ExtBeanUtils.copyProperties(photoGalleryVO, pgVO);
                        
                        photoGalleryVO.setAttachmentVO(attachmentVO);
                        retList.add(photoGalleryVO);
                    }
                }
            }
        }
        return retList;
    }
    // for 文章封面
    public KbPhotoGallery findDocCoverImageHolder(PublicationVO pubVO){
        Map<String, Object> params = new HashMap<String, Object>();
        String jpql = "SELECT k FROM KbPhotoGallery k WHERE k.primaryType=:primaryType AND k.primaryId=:primaryId AND k.identifyImg=:identifyImg";
        
        params.put("primaryType", PhotoGalleryEnum.DOC.getCode());
        params.put("primaryId", pubVO.getId());
        params.put("identifyImg", Boolean.TRUE);
        List<KbPhotoGallery> list = this.findByJPQLQuery(jpql, params);
        
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    public KbPhotoGallery prepareDocCoverImageHolder(PublicationVO pubVO, TcUser operator, boolean simulated){
        KbPhotoGallery photoGallery = findDocCoverImageHolder(pubVO);
        if( photoGallery==null ){
            photoGallery = new KbPhotoGallery();
            photoGallery.setPrimaryType(PhotoGalleryEnum.DOC.getCode());// 文章關聯
            photoGallery.setPrimaryId(pubVO.getId());// 關聯 ID
            photoGallery.setIdentifyImg(true);// 文章封面
            photoGallery.setStatus(ContentStatusEnum.PUBLISH.getCode());
            
            save(photoGallery, operator, simulated);
        }
        return photoGallery;
    }
    /**
     * 取得封面圖示
     * @param pubVO
     */
    public void loadCoverImage(PublicationVO pubVO){
        logger.debug("loadCoverImage ...");
        KbPhotoGallery photoGallery = findDocCoverImageHolder(pubVO);
        
        if( photoGallery!=null ){
            AttachmentVO attachmentVO = attachmentFacade.getLastAttachment(photoGallery);
            pubVO.setCoverImgVO(attachmentVO);
            pubVO.setCoverImg(photoGallery.getCname());
        }
    }
    
    /**
     * 取得全部 Folder
     * @return 
     */
    public List<PhotoGalleryVO> findAllFolder(){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(genCommonFindSQL(null, params));
        sql.append("AND S.PRIMARYTYPE = '").append(PhotoGalleryEnum.CUSTOM.getCode()).append("' \n"); // 自訂相簿/圖庫
        
        if( !GlobalConstant.FIX_IMG_EDITABLED ){// 固定網站圖片可後台維護
            sql.append("AND S.CODE IS NULL \n");
        }
        
        sql.append("ORDER BY S.PARENT, S.CNAME");

        logger.debug("findAllFolder ...");
        ResultSetHelper<PhotoGalleryVO> resultSetHelper = new ResultSetHelper(PhotoGalleryVO.class);
        List<PhotoGalleryVO> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        return resList;
    }
    
    /**
     * 不包含圖的資料夾
     * @param typeEnum
     * @return 
     */
    public List<PhotoGalleryVO> findEmptyFolders(PhotoGalleryEnum typeEnum){
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        Map<String, Object> params = new HashMap<String, Object>();
        criteriaVO.setIncMedia(false);// 不包含圖
        
        StringBuilder sql = new StringBuilder();
        sql.append(genCommonFindSQL(criteriaVO, params));
        if( typeEnum!=null ){
            sql.append("AND S.PRIMARYTYPE='").append(typeEnum.getCode()).append("' \n");
        }
        sql.append("ORDER BY S.PARENT, S.CNAME");

        logger.debug("findEmptyFolders ...");
        ResultSetHelper<PhotoGalleryVO> resultSetHelper = new ResultSetHelper(PhotoGalleryVO.class);
        List<PhotoGalleryVO> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        return resList;
    }
    
    /**
     * 取得下層分類
     * @param allList
     * @param parentId
     * @param typeEnum
     * @return 
     */
    public List<PhotoGalleryVO> findSubItems(List<PhotoGalleryVO> allList, Long parentId, PhotoGalleryEnum typeEnum){
        if( allList==null || allList.isEmpty() || parentId==null || parentId<0 ){
            logger.debug("findSubItems allList==null || allList.isEmpty() || parentId==null || parentId<0 ...");
            return null;
        }
        List<PhotoGalleryVO> retList = new ArrayList<PhotoGalleryVO>();
        
        for(PhotoGalleryVO vo : allList){
            if( vo.getPrimaryTypeEnum()==typeEnum && parentId.equals(vo.getParent()) ){
                retList.add(vo);
            }
        }
        return retList;
    }

    /**
     * 官網網站專用圖片
     * [首頁輪播大圖]、[我們的館藏]、[保種中心宗旨]、[保種中心位置]、[植物名錄主類群照片]
     * @param parentId 
     * @return 
     */
    public List<PhotoGalleryVO> findWebSiteImages(Long parentId){// 多圖同一用途(放在同一資料夾)
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        criteriaVO.setPrimaryType(PhotoGalleryEnum.IMAGE.getCode());
        criteriaVO.setParent(parentId);
        return findByCriteria(criteriaVO);
    }
    public PhotoGalleryVO findWebSiteImage(Long primaryId){// 單圖一用途(指定primaryId)
        List<PhotoGalleryVO> list = findByPrimary(PhotoGalleryEnum.IMAGE.getCode(), primaryId);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 準備PhotoGalleryVO List 的  image URL
     * imgList 須已包含 AttachmentVO 資訊
     * @param imgList
     * @throws java.io.UnsupportedEncodingException
     */
    public void prepareWebImagesUrl(List<PhotoGalleryVO> imgList) throws UnsupportedEncodingException{
        if( imgList!=null ){
            for(PhotoGalleryVO pgVO : imgList){
                // 設定圖片存取位置
                String domainName = pgVO.getPrimaryTypeEnum().getDomainName();
                String url = imageFacade.preparePublicUrl(domainName, pgVO.getAttachmentVO());// 小圖
                
                if( url!=null ){
                    pgVO.setUrl(url);
                    logger.debug("prepareWebImagesUrl url = "+url);
                    pgVO.setDescription(pgVO.getAttachmentVO().getDescription());// for UI顯示
                    pgVO.setDescription(pgVO.getDescription()==null? pgVO.getAttachmentVO().getFileName():null);
                }else{
                    logger.error("prepareWebImagesUrl error: not image url with pgVO = "+pgVO);
                }
            }// end of for
        }
    }

    //<editor-fold defaultstate="collapsed" desc="自訂相簿顯示">
    /**
     * 相簿隨機選出封面顯示圖片
     * @param albums
     * @return 
     */
    public List<PhotoGalleryVO> findAlbumCoverImages(List<PhotoGalleryVO> albums){
        logger.debug("findAlbumCoverImages ...");
        List<RandomSelectVO> list = findAlbumCoverImageIds(albums);
        if( list==null ){
            logger.error("findAlbumCoverImages list==null");
            return null;
        }
        
        List<Long> photoGalleryIds = new ArrayList<Long>();
        for(RandomSelectVO vo : list){
            photoGalleryIds.add(vo.getSelectedId());
        }
        
        MediaCriteriaVO imgCriteriaVO = new MediaCriteriaVO();
        imgCriteriaVO.setPrimaryType(PhotoGalleryEnum.IMAGE.getCode());
        imgCriteriaVO.setIds(photoGalleryIds);
        imgCriteriaVO.setStatus(ContentStatusEnum.PUBLISH.getCode());//封面必為已發佈
        imgCriteriaVO.setOrderBy("S.PARENT");// 依相簿排序
        return findByCriteria(imgCriteriaVO);
    }
    public List<RandomSelectVO> findAlbumCoverImageIds(List<PhotoGalleryVO> albums){
        logger.debug("findAlbumCoverImageIds ...");
        if( albums==null ){
            logger.error("findAlbumCoverImageIds albums==null");
            return null;
        }
        
        List<Long> parents = new ArrayList<Long>();
        List<RandomSelectVO> counts = new ArrayList<RandomSelectVO>();
        for(PhotoGalleryVO vo : albums){
            RandomSelectVO randomSelectVO = new RandomSelectVO();
            randomSelectVO.setId(vo.getId());// 相簿ID
            randomSelectVO.setNum(vo.getSubCounts());
            
            parents.add(vo.getId());
            counts.add(randomSelectVO);
        }
        
        // 取得所有相簿包含圖片
        MediaCriteriaVO imgCriteriaVO = new MediaCriteriaVO();
        imgCriteriaVO.setPrimaryType(PhotoGalleryEnum.IMAGE.getCode());
        imgCriteriaVO.setParents(parents);
        imgCriteriaVO.setStatus(ContentStatusEnum.PUBLISH.getCode());//封面必為已發佈
        List<PhotoGalleryVO> images = this.findByCriteria(imgCriteriaVO);
        
        if( images==null ){
            logger.error("findAlbumCoverImageIds images==null");
            return null;
        }
        
        // 隨機選出顯示圖片
        rendomSelectPhoto(PhotoGalleryEnum.CUSTOM, images, counts);
        
        return counts;
    }

    /**
     * 取得相簿封面照片 (隨機至包含照片中選取)
     * @param criteriaVO 
     * @return  
     */
    public List<PhotoGalleryVO> prepareAlbums(MediaCriteriaVO criteriaVO) {
        List<PhotoGalleryVO> albums = findByCriteria(criteriaVO);
        logger.debug("prepareAlbums ShowCoverImg = "+criteriaVO.isShowCoverImg());
        if( albums!=null && criteriaVO.isShowCoverImg() ){
            // 相簿隨機選出封面顯示圖片
            List<PhotoGalleryVO> list = findAlbumCoverImages(albums);
            
            if( list!=null ){
                // 回傳包含FVVAULT檔案儲存資訊 // 原每筆 PhotoGalleryVO 只會關聯一個圖片 (for 控制每個圖片是否可發佈)
                findImages(list);

                // 相簿封面照片
                for(PhotoGalleryVO album : albums){
                    for(PhotoGalleryVO img : list){
                        if( album.getId().equals(img.getParent()) ){
                            album.setAttachmentVO(img.getAttachmentVO());
                        }
                    }
                }
            }
        }
        
        return albums;
    }    
    //</editor-fold>
    
    /**
     * 隨機選出顯示圖片
     * @param pgEnum
     * @param images 各分類/相簿所有圖片
     * @param resList 區分分類/相簿結果
     */
    public void rendomSelectPhoto(PhotoGalleryEnum pgEnum, List<PhotoGalleryVO> images, List<RandomSelectVO> resList){
        logger.debug("rendomSelectPhoto ...");
        if( resList==null || images==null ){
            logger.error("rendomSelectPhoto counts==null || images==null");
            return;
        }
        
        // 先歸類圖片
        for(RandomSelectVO selectVO : resList){
            List<Long> containsList = new ArrayList<Long>();
            for(PhotoGalleryVO vo : images){
                if( PhotoGalleryEnum.CUSTOM==pgEnum && vo.getParent().equals(selectVO.getId()) )// 相簿圖片
                {
                    containsList.add(vo.getId());
                }
            }
            selectVO.setContainsList(containsList);
        }
        
        // by 分類亂數選出顯示的圖片
        for(RandomSelectVO selectVO : resList){
            selectVO.setSelectedId(selectVO.findSelectedId());
            logger.debug("rendomSelectPhoto id = "+selectVO.getId()+", selectVO.getNum()="+selectVO.getNum()+", selectedId = "+selectVO.getSelectedId());
        }
    }
    
    /**
     * 準備多圖上傳，個別關聯 Content Holder
     * 每筆 PhotoGalleryVO 只會關聯一個圖片 (for 控制每個圖片是否可發佈)
     * @param parent
     * @param primaryType
     * @param primaryId
     * @param subject
     * @param description
     * @param status
     * @param identifyImg
     * @param num
     * @param operator
     * @param simulated
     * @return 
     */
    public List<KbPhotoGallery> prepareImageHolders(Long parent, 
            String primaryType, Long primaryId, String subject, String description, String status, Boolean identifyImg,
            int num, TcUser operator, boolean simulated){
        List<KbPhotoGallery> pgList = new ArrayList<KbPhotoGallery>();
        for(int i=0; i<num; i++){
            KbPhotoGallery kbPhotoGallery = prepareImageHolder(parent, primaryType, primaryId, subject, description, status, identifyImg, operator, simulated);
            pgList.add(kbPhotoGallery);
        }
        return pgList;
    }
    public KbPhotoGallery prepareImageHolder(Long parent, 
        String primaryType, Long primaryId, 
        String subject, String description, String status, Boolean identifyImg, TcUser operator, boolean simulated){
        KbPhotoGallery kbPhotoGallery = new KbPhotoGallery();
        kbPhotoGallery.setParent(parent);
        kbPhotoGallery.setPrimaryType(primaryType);
        kbPhotoGallery.setPrimaryId(primaryId);
        kbPhotoGallery.setSubject(subject);
        kbPhotoGallery.setDescription(description);
        
        kbPhotoGallery.setStatus(status);// 狀態
        kbPhotoGallery.setIdentifyImg(identifyImg);// 植物蒐藏 (OLD植物圖鑑)

        save(kbPhotoGallery, operator, simulated);// 儲存 Content Holder
        return kbPhotoGallery;
    }

    /**
     * 檢查是否是文章插圖 by KbPhotoGallery.ID
     * @param imgId
     * @param ignoreDocId
     * @return 
     */
    public boolean isDocImage(Long imgId, Long ignoreDocId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) cc from KB_PHOTO_GALLERY \n");
        sql.append("where PRIMARYTYPE=#PRIMARYTYPE and PARENT=#imgId \n");
        
        params.put("PRIMARYTYPE", PhotoGalleryEnum.DOC.getCode());
        params.put("imgId", imgId);

        if( ignoreDocId!=null ){// for 原本也是文章插圖，要排除原文章ID時
            sql.append("and PRIMARYID<>#ignoreDocId ");
            params.put("ignoreDocId", ignoreDocId);
        }
        
        return this.count(sql.toString(), params)>0;
    }
    public boolean isDocImage(Long imgId){
        return isDocImage(imgId, null);
    }
    
    /**
     * 移除文章插圖 by KbPublication.ID
     * @param docId
     * @param operator
     * @param simulated
     */
    public void removeDocImagesAll(Long docId, TcUser operator, boolean simulated){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT k FROM KbPhotoGallery k WHERE k.primaryType = :primaryType AND k.primaryId = :primaryId");
        
        params.put("primaryType", PhotoGalleryEnum.DOC.getCode());
        params.put("primaryId", docId);
        
        List<KbPhotoGallery> list = findByJPQLQuery(sql.toString(), params);
        
        if( list!=null && !list.isEmpty() ){
            for(KbPhotoGallery photoGallery : list){
                // 檢查是否也是其他文章插圖
                if( !isDocImage(photoGallery.getId(), docId) ){
                    this.removePhoto(photoGallery, operator, simulated);
                }
            }
        }
    }
    
    /**
     * 檢查是否有原本自圖庫取圖，又刪除的狀況。需刪除關聯資料
     * 文章插圖依最新自訂網頁內容部分移除
     * @param docId
     * @param content
     * @param operator 
     * @param simulated 
     */
    public void removeDocImagesByContent(Long docId, String content, TcUser operator, boolean simulated){
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        criteriaVO.setPrimaryType(PhotoGalleryEnum.DOC.getCode());
        criteriaVO.setPrimaryId(docId);
        List<PhotoGalleryVO> list = this.findByCriteria(criteriaVO);
        
        List<Long> idsFromUpload = new ArrayList<Long>();// 直接上傳插圖
        
        if( list!=null && !list.isEmpty() ){
            for(PhotoGalleryVO vo : list){
                if( vo.getIdentifyImg()==null || !vo.getIdentifyImg() ){// 封面圖排除
                    if( vo.getParent()!=null && vo.getParent()>0 ){// 自圖庫取圖
                        Long imgId = vo.getParent();
                        PhotoGalleryVO photoGalleryVO = this.findById(imgId, true);
                        String filename = (photoGalleryVO!=null)?photoGalleryVO.getFileName():null;
                        logger.debug("removeDocImageByContent filename= "+filename);
                        if( filename!=null && !filename.trim().isEmpty() ){
                            if( content.indexOf(filename)>0 ){
                                logger.debug("removeDocImageByContent exist!");
                            }else{
                                logger.debug("removeDocImageByContent not exist!");
                                this.remove(vo.getId(), simulated);// 只可刪除文章關聯
                            }
                        }
                    }else{// 直接上傳插圖
                        idsFromUpload.add(vo.getId());
                    }
                }
            }
        }

        // 直接上傳插圖
        if( !idsFromUpload.isEmpty() ){
            criteriaVO = new MediaCriteriaVO();
            criteriaVO.setType(PhotoGalleryEnum.DOC.getCode());
            criteriaVO.setIds(idsFromUpload);
            criteriaVO.setGetFvInfo(true);
            List<PhotoGalleryVO> listUpload = this.findByCriteria(criteriaVO);
            
            if( listUpload!=null && !listUpload.isEmpty() ){
                for(PhotoGalleryVO vo : listUpload){
                    Long imgId = vo.getId();
                    String filename = vo.getFileName();
                    logger.debug("removeDocImageByContent Upload filename= "+filename);
                    if( filename!=null && !filename.trim().isEmpty() ){
                        if( content.indexOf(filename)>0 ){
                            logger.debug("removeDocImageByContent Upload exist!");
                        }else{
                            logger.debug("removeDocImageByContent Upload not exist!");
                            // 檢查是否也是其他文章插圖
                            if( !isDocImage(imgId, docId) ){
                                logger.debug("removeDocImageByContent Upload remove!");
                                this.removePhotoById(imgId, operator, simulated);// 連同FV一起刪除
                            }
                        }
                    }
                }
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="for import">
    /**
     * 自訂相簿 匯入
     * @param rootId
     * @param rootPath
     * @param lang
     * @param operator
     * @param simulated
     * @return 
     */
    public String impAlbumPhotos(Long rootId, String rootPath, String lang, TcUser operator, boolean simulated){
        File dirF = new File(rootPath);
        
        if( !dirF.exists() || !dirF.isDirectory() ){
            return "輸入檔案路徑錯誤!";
        }
        
        int counts = 0;
        if( dirF.listFiles()!=null ){
            for(File dir : dirF.listFiles()){
                if( dir.isDirectory() && dir.canRead() ){
                    String folderName = dir.getName();
                    String dateStr = (folderName!=null && folderName.length()>=10)? folderName.substring(0, 10):folderName;
                    Date dataDate = null;
                    logger.debug("impAlbumPhotos folderName = "+folderName);
                    logger.debug("impAlbumPhotos dateStr = "+dateStr);
                    try{
                        dataDate = DateUtils.parseDateStrictly(dateStr, new String[]{"yyyy-MM-dd"});
                    }catch(Exception e){
                        logger.error("impAlbumPhotos Exception dateStr = "+dateStr+" ("+e.toString()+")");
                    }
                    KbPhotoGallery pgDir = new KbPhotoGallery();
                    pgDir.setDataDate(dataDate);
                    pgDir.setCname(folderName);
                    // pgDir.setSubject(folderName);
                    pgDir.setDescription(dir.getAbsolutePath());
                    pgDir.setParent(rootId);
                    pgDir.setPrimaryType(PhotoGalleryEnum.CUSTOM.getCode());
                    pgDir.setStatus(ContentStatusEnum.PUBLISH.getCode());
                    pgDir.setLang(lang);
                    save(pgDir, operator, simulated);// 相簿
                    logger.debug("impAlbumPhotos pgDir = "+pgDir.getId());
                    
                    Long dirId = pgDir.getId();// 相簿ID
                    if( dir.listFiles()!=null ){
                        // 發佈圖檔
                        for(File imgF : dir.listFiles(new ImageFileFilter())){
                            logger.debug("impAlbumPhotos pub img = "+imgF.getName());
                            
                            TcDomain domain = imageFacade.getDomain(GlobalConstant.DOMAIN_NAME_CUSTOM_IMG);
                            try{
                                // 完整儲存含檔案處理
                                KbPhotoGallery pgEntity = saveFullImageData(dirId, 
                                    PhotoGalleryEnum.IMAGE.getCode(), null, 
                                    imgF.getName(), imgF.getAbsolutePath(), 
                                    ContentStatusEnum.PUBLISH.getCode(),
                                    null,
                                    imgF.getAbsolutePath(),
                                    domain,
                                    operator, simulated);

                                if( pgEntity!=null ){
                                    logger.debug("impAlbumPhotos pub pgEntity = "+pgEntity);
                                    counts++;
                                }else{
                                    logger.debug("impAlbumPhotos pub pgEntity = null ");
                                }
                            }catch(Exception e){
                                logger.error("impAlbumPhotos Exception:\n", e);
                            }
                        }
                        
                        // 草稿圖檔
                        for(File drafDir : dir.listFiles()){
                            if( drafDir.isDirectory() && drafDir.canRead() && drafDir.getName().equals("草稿") ){
                                logger.debug("impAlbumPhotos draf dir = "+drafDir.getAbsolutePath());
                                
                                if( drafDir.listFiles()!=null ){
                                    // 草稿圖檔
                                    for(File imgDF : drafDir.listFiles(new ImageFileFilter())){
                                        logger.debug("impAlbumPhotos draf img = "+imgDF.getName());
                                        
                                        TcDomain domain = imageFacade.getDomain(GlobalConstant.DOMAIN_NAME_CUSTOM_IMG);
                                        try{
                                            // 完整儲存含檔案處理
                                            KbPhotoGallery pgEntity = saveFullImageData(dirId, 
                                                PhotoGalleryEnum.IMAGE.getCode(), null, 
                                                imgDF.getName(), imgDF.getAbsolutePath(), 
                                                ContentStatusEnum.DRAFT.getCode(),
                                                null,
                                                imgDF.getAbsolutePath(),
                                                domain,
                                                operator, simulated);
                                            
                                            if( pgEntity!=null ){
                                                logger.debug("impAlbumPhotos draf pgEntity = "+pgEntity);
                                                counts++;
                                            }else{
                                                logger.debug("impAlbumPhotos draf pgEntity = null ");
                                            }
                                        }catch(Exception e){
                                            logger.error("impAlbumPhotos Exception:\n", e);
                                        }
                                        
                                        counts++;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        return "共匯入 "+counts+" 個圖檔";
    }
    //</editor-fold>

    /**
     * 儲存照片 for PDA
     * @param parent
     * @param primaryId
     * @param pgEnum
     * @param inputStream
     * @param subject
     * @param description
     * @param filename
     * @param contentType
     * @param statusCode
     * @param operator
     * @return 
     */
    public KbPhotoGallery saveImage(Long parent, 
            PhotoGalleryEnum pgEnum, Long primaryId,
            String subject, String description, 
            InputStream inputStream, 
            String filename, String contentType, 
            String statusCode, TcUser operator){
        try{
            logger.info("saveImage primaryId="+primaryId+", filename="+filename+", contentType="+contentType+", operator="+operator.getLoginAccount());
            // AccessionVO vo = accessionFacade.findVoById(id, false, false, false);
            
            // 儲存上傳檔
            // 每筆 PhotoGalleryVO 只會關聯一個圖片 (for 控制每個圖片是否可發佈)
            if( inputStream!=null ){
                byte[] content = IOUtils.toByteArray(inputStream);
                if( content!=null ){
                    TcDomain domain = imageFacade.getDomain(pgEnum.getDomainName());
                    KbPhotoGallery kbPhotoGallery = 
                            saveFullImageData(parent, // parent 
                                    pgEnum.getCode(), primaryId, // primaryType, primaryId, 
                                    subject, description, 
                                    statusCode, // status,
                                    false, // identifyImg,
                                    filename, // fileName,
                                    content, 
                                    domain,
                                    operator, 
                                    false);
                    logger.info("saveImage kbPhotoGallery = "+kbPhotoGallery);
                    return kbPhotoGallery;
                }
            }
        }catch(Exception e){
            logger.error("saveImage Exception:\n", e);
        }
        return null;
    }

}

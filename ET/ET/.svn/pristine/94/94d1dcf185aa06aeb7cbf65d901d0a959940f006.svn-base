/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.et.entity.KbLink;
import com.tcci.et.entity.KbVideo;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.enums.LinkEnum;
import com.tcci.et.enums.PhotoGalleryEnum;
import com.tcci.et.enums.VideoLibraryEnum;
import com.tcci.et.model.VideoVO;
import com.tcci.et.model.criteria.MediaCriteriaVO;
import com.tcci.cm.model.global.GlobalConstant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class KbVideoFacade extends AbstractFacade<KbVideo> {
    @EJB KbLinkFacade linkFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KbVideoFacade() {
        super(KbVideo.class);
    }

    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     * @param simulated 
     */
    public void save(KbVideo entity, TcUser operator, boolean simulated){
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
    public void saveVO(VideoVO pubVO, TcUser operator, boolean simulated){
        KbVideo entity = (pubVO.getId()!=null)?find(pubVO.getId()):new KbVideo();
        this.copyVoToEntity(pubVO, entity);
        this.save(entity, operator, simulated);
        this.copyEntityToVo(entity, pubVO);
    }
    
    /**
     * 儲存 (含網址資訊)
     * @param vo
     * @param operator 
     * @param simulated 
     */
    public void saveVideo(VideoVO vo, TcUser operator, boolean simulated){
        saveVO(vo, operator, simulated);

        KbLink link = (vo.getLinkId()!=null)?linkFacade.find(vo.getLinkId()):new KbLink();
        link.setPrimaryType(LinkEnum.VIDEO.getCode());
        link.setPrimaryid(vo.getId());
        link.setUrl(vo.getUrl());
        link.setOpenMethod(vo.getOpenMethod());
        linkFacade.save(link, operator, simulated);
    }
    
    /**
     * 複製
     * @param entity
     * @param vo
     */
    public void copyEntityToVo(KbVideo entity, VideoVO vo){
        ExtBeanUtils.copyProperties(vo, entity);
        
        vo.setLastTime(vo.getLastUpdateTime());
        vo.setLastUserName((vo.getLastUpdateUser()!=null)?vo.getLastUpdateUser().getCname():null);
    }
    public void copyVoToEntity(VideoVO vo, KbVideo entity){
        ExtBeanUtils.copyProperties(entity, vo);
    }
    
    /**
     * 刪除影片
     * @param vo 
     * @param simulated 
     */
    public void deleteVideo(VideoVO vo, boolean simulated){
        logger.debug("deleteVideo vo="+vo);
        linkFacade.remove(vo.getLinkId(), simulated);// 網址資訊
        this.remove(vo.getId(), simulated);// 影片資訊
    }
    
    /**
     * 共用查詢SQL
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String genCommonFindSQL(MediaCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        String urlRs1 = GlobalConstant.URL_VIDEO_VIEW1;
        String urlRs2 = GlobalConstant.URL_VIDEO_VIEW2;
        String urlEmbed = GlobalConstant.URL_VIDEO_EMBED;
        
        if( criteriaVO!=null && criteriaVO.isCountOnly() ){
            sql.append("SELECT count(S.ID) \n");
        }else{
            sql.append("SELECT S.*, C.SUBCOUNTS \n");
            sql.append(", V.ID LINKID, V.URL, V.OPENMETHOD \n");
            sql.append(", REPLACE(REPLACE(V.URL, '").append(urlRs1).append("', '").append(urlEmbed).append("'), '").append(urlRs2).append("', '").append(urlEmbed).append("') AS embedUrl \n");
            sql.append(", CASE WHEN S.MODIFYTIMESTAMP IS NULL THEN U1.CNAME ELSE U2.CNAME END LASTUSERNAME \n");
            sql.append(", CASE WHEN S.MODIFYTIMESTAMP IS NULL THEN S.CREATETIMESTAMP ELSE S.MODIFYTIMESTAMP END LASTTIME \n");
            sql.append(", P.CNAME as PARENTNAME \n");
            // for 文章插影片
            if( criteriaVO!=null 
                    && VideoLibraryEnum.DOC.getCode().equals(criteriaVO.getPrimaryType()) 
                    && criteriaVO.isUseDocCriteria() ){
                sql.append(", PUB.TITLE DOCTITLE \n");
            }
        }
        sql.append("FROM KB_VIDEO S \n");
        
        // for 文章插影片
        if( criteriaVO!=null 
                && PhotoGalleryEnum.DOC.getCode().equals(criteriaVO.getPrimaryType()) 
                && criteriaVO.isUseDocCriteria() ){
            sql.append("JOIN KB_PUBLICATION PUB ON PUB.ID=S.PRIMARYID \n");
            
            if( criteriaVO.getDocCriteriaVO()!=null ){
                if( criteriaVO.getDocCriteriaVO().getType()!=null ){
                    sql.append("    AND PUB.TYPE=#DOCTYPE \n");
                    params.put("DOCTYPE", criteriaVO.getDocCriteriaVO().getType());
                }
            }
        }
        
        sql.append("LEFT OUTER JOIN KB_LINK V ON V.PRIMARYTYPE='").append(LinkEnum.VIDEO.getCode()).append("' AND V.PRIMARYID=S.ID \n");
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("    SELECT PARENT, COUNT(*) SUBCOUNTS \n");
        sql.append("    FROM KB_VIDEO \n");
        sql.append("    GROUP BY PARENT \n");
        sql.append(") C ON C.PARENT=S.ID \n");
        sql.append("LEFT OUTER JOIN TC_USER U1 ON U1.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN TC_USER U2 ON U2.ID=S.MODIFIER \n");        
        // 所在資料夾
        sql.append("LEFT OUTER JOIN KB_VIDEO P ON P.ID=S.PARENT \n");

        sql.append("WHERE 1=1 \n");
        
        return sql.toString();
    }

    public String getSqlByCriteria(MediaCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append(genCommonFindSQL(criteriaVO, params));
        
        if( criteriaVO!=null ){
            // 子資料夾
            if( criteriaVO.getParent()!=null ){
                sql.append("AND S.PARENT=#PARENT \n");
                params.put("PARENT", criteriaVO.getParent());
            }
            // PRIMARYTYPE
            if( criteriaVO.getPrimaryType()!=null ){
                sql.append("AND S.PRIMARYTYPE=#PRIMARYTYPE \n");
                params.put("PRIMARYTYPE", criteriaVO.getPrimaryType());
            }
            // PRIMARYID
            if( criteriaVO.getPrimaryId()!=null ){
                sql.append("AND S.PRIMARYID=#PRIMARYID \n");
                params.put("PRIMARYID", criteriaVO.getPrimaryId());
            }
            // STATUS
            if( criteriaVO.getStatus()!=null ){
                sql.append("AND S.STATUS=#STATUS \n");
                params.put("STATUS", criteriaVO.getStatus());
            }
            // 關鍵字
            String keyword = criteriaVO.getKeyword();
            if( keyword!=null && !keyword.isEmpty() ){
                keyword = "%" + keyword + "%";
                sql.append("AND (S.CNAME LIKE #keywork OR S.ENAME LIKE #keywork");
                sql.append("　OR S.TITLE LIKE #keywork　OR S.DESCRIPTION LIKE #keywork");
                sql.append("　OR S.CHANNEL LIKE #keywork) \n");
                params.put("keywork", keyword);
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
        }
        
        return sql.toString();
    }
    /**
     * 依查詢條件抓取資料
     * @param criteriaVO
     * @return 
     */
    public List<VideoVO> findByCriteria(MediaCriteriaVO criteriaVO){
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
        
        logger.debug("findByCriteria ...");
        ResultSetHelper<VideoVO> resultSetHelper = new ResultSetHelper(VideoVO.class);
        List<VideoVO> resList;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        }
        
        return resList;
    }
    
    public int countByCriteria(MediaCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        criteriaVO.setCountOnly(true);
        sql.append(getSqlByCriteria(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    
    public VideoVO findById(Long id){
        if( id==null || id<=0 ){
            logger.error("findById id==null || id<=0");
            return null;
        }
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        criteriaVO.setId(id);
        List<VideoVO> resList = findByCriteria(criteriaVO);
        
        return (resList!=null && resList.size()>0)?resList.get(0):null;
    }
    
    /**
     * 取得全部
     * @return 
     */
    public List<VideoVO> findCustomFolder(){
        logger.debug("findCustomFolder ...");
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        criteriaVO.setPrimaryType(VideoLibraryEnum.CUSTOM.getCode());
        
        return findByCriteria(criteriaVO);
    }
    
    /**
     * 包含影片
     * @param parentId
     * @return 
     */
    public List<VideoVO> findSubVideos(Long parentId){
        logger.debug("findSubVideos parentId="+parentId);
        MediaCriteriaVO criteriaVO = new MediaCriteriaVO();
        criteriaVO.setParent(parentId);
        criteriaVO.setPrimaryType(VideoLibraryEnum.VIDEO.getCode());
        
        return findByCriteria(criteriaVO);
    }
    
    /**
     * 取得下層分類
     * @param allList
     * @param parentId
     * @return 
     */
    public List<VideoVO> findSubItems(List<VideoVO> allList, Long parentId){
        if( allList==null || allList.isEmpty() || parentId==null || parentId<0 ){
            logger.debug("findSubItems allList==null || allList.isEmpty() || parentId==null || parentId<0 ...");
            return null;
        }
        List<VideoVO> retList = new ArrayList<VideoVO>();
        
        for(VideoVO vo : allList){
            if( parentId.equals(vo.getParent()) ){
                retList.add(vo);
            }
        }
        return retList;
    }

    // for 文章插入影片
    public List<VideoVO> findVideosByDoc(MediaCriteriaVO criteriaVO){
        logger.debug("findVideosByDoc ...");

        criteriaVO.setUseDocCriteria(true);// 有使用文章專屬條件(需多JOIN 文章 TABLE)
        criteriaVO.setPrimaryType(VideoLibraryEnum.DOC.getCode());
        criteriaVO.getDocCriteriaVO().setType(criteriaVO.getType());// 文章類別
        
        List<VideoVO> resList = findByCriteria(criteriaVO);
        return resList;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.entity.content.TcFvvault;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.content.FileUploadFacade;
import com.tcci.fc.facade.content.TcFvvaultFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.entity.KbPhotoGallery;
import com.tcci.et.model.global.FvItemVO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jimmy.lee
 */
@Stateless
public class AttachmentFacade extends AbstractFacade<TcApplicationdata> {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    @EJB TcDomainFacade domainFacade;
    @EJB TcFvvaultFacade tcFvvaultFacade;
    @EJB ContentFacade contentFacade;
    @EJB FileUploadFacade fileUploadFacade;
    
    private TcDomain defaultDomain;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AttachmentFacade() {
        super(TcApplicationdata.class);
    }
    
    @PostConstruct
    private void init() {
        defaultDomain = domainFacade.getDefaultDomain();
    }
    
    /**
     * KbPhotoGallery 關聯檔案資訊
     * @return 
     */
    public String getPhotoFVSQL(){
        StringBuilder sql = new StringBuilder();
        sql.append("	select t.ID APPID, t.CONTAINERCLASSNAME, t.CONTAINERID, t.DESCRIPTION \n");
        sql.append("	, t.FVITEM FVITEMID \n");
        sql.append("	, i.ID, i.DOMAIN, i.FILENAME, i.NAME ORIFILENAME, i.CONTENTTYPE, i.FILESIZE \n");
        sql.append("	, m.NAME DOMAINNAME \n");
        sql.append("	, v.LOCATION \n");
        sql.append("	from TC_APPLICATIONDATA t \n");
        sql.append("	join TC_FVITEM i on i.ID=t.FVITEM \n");
        sql.append("	join TC_DOMAIN m on m.ID=i.DOMAIN \n");
        sql.append("	join TC_FVVAULT v on v.DOMAIN=m.ID AND v.HOSTNAME='").append(GlobalConstant.FVVAULT_HOST).append("' \n");
        sql.append("	where t.CONTAINERCLASSNAME='").append(KbPhotoGallery.class.getName()).append("' \n");
        
        return sql.toString();
    }
    
    public String getLocationByDomain(String domainName){
        TcDomain domain = domainFacade.getDomainByName(domainName);
        TcFvvault fv = tcFvvaultFacade.getTcFvvaultByHost(domain, GlobalConstant.getFvVaultHost());
        
        if( fv!=null ){
            String location = fv.getLocation();
            if( location.endsWith(File.separator) || location.endsWith("\\") ){
                location = location.substring(0, location.length()-1);
                return location;
            }
        }
        return null;
    }
    
    /**
     * 上傳至 FVAULT
     * @param domain
     * @param contentHolder
     * @param fileName
     * @param content
     * @param contentType
     * @param index
     * @param loginTcUser
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean saveContentToFvVault(TcDomain domain, ContentHolder contentHolder, String fileName, byte[] content, String contentType, int index, TcUser loginTcUser) throws Exception{
        // 讀取實體檔案
        if( content==null ){
            return false;
        }else{
            List<AttachmentVO> attachments = loadContent(contentHolder);
            if( attachments==null ){
                attachments = new ArrayList<AttachmentVO>();
            }
            // 上傳 FVVAULT
            AttachmentVO attachmentVO = genAttachmentVO(fileName, contentType, content, index);
            attachments.add(attachmentVO);
            
            // 避免單一資料夾檔案過多，動態建立目錄 yyyyMM
            //String additionalPath = DateUtils.formatDateString(new Date(), GlobalConstant.FORMAT_YM);
            //contentFacade.saveContent(contentHolder, attachments, additionalPath, loginTcUser);
            contentFacade.saveContent(domain, contentHolder, attachments, loginTcUser);
        }
        
        return true;
    }
    
    /**
     * InputStream To AttachmentVO
     * @param oriFileName
     * @param content
     * @param contentType
     * @param index
     * @return 
     */
    public AttachmentVO genAttachmentVO(String oriFileName, String contentType, byte[] content, int index){
        AttachmentVO attachmentVO = new AttachmentVO();
        if( content==null ){
            logger.error("inputStreamToAttachmentVO content = null !");
        }else{
            // 上傳 FVVAULT
            attachmentVO.setSize(content.length);
            attachmentVO.setContent(content);
            attachmentVO.setContentType(contentType);
            attachmentVO.setFileName(oriFileName);
            attachmentVO.setIndex(index);
        }
        return attachmentVO;
    }
    
    /*
     * public API:
     *   List<AttachmentVO> loadContent(ContentHolder)
     *   InputStream getContentStream(AttachmentVO)
     *   void saveContent(ContentHolder contentHolder, List<AttachmentVO> attachmentVOList)
     */
    public List<AttachmentVO> loadContent(ContentHolder container) {
        List<TcApplicationdata> applicationDataList = getApplicationdata(container);
        if (applicationDataList == null || applicationDataList.isEmpty()) {
            return null;
        }
        List<AttachmentVO> result = new ArrayList<AttachmentVO>();
        int index = 0;
        for (TcApplicationdata a : applicationDataList) {
            AttachmentVO vo = new AttachmentVO();
            vo.setApplicationdata(a);
            TcFvitem fvItem = a.getFvitem();
            if (fvItem == null) {
                continue;
            }
            // vo.setContent(content); // 下載時再讀取檔案
            vo.setContentType(fvItem.getContenttype());
            vo.setFileName(fvItem.getName());
            vo.setSize(fvItem.getFilesize());
            vo.setIndex(index++);
            result.add(vo);
        }
        return result;
    }
    
    /**
     *  依 CententHolder 取最新檔案
     * @param contentHolder
     * @return 
     */
    public AttachmentVO getLastAttachment(ContentHolder contentHolder){
        List<AttachmentVO> attachments = loadContent(contentHolder);
        AttachmentVO attachmentVO = null;
        if( attachments!=null && !attachments.isEmpty() ){
            // 取最新版本即可
            attachmentVO = attachments.get(attachments.size()-1);
        }
        
        return attachmentVO;
    }
    
    /**
     * 取得實體檔名
     * @param vo
     * @return 
     */
    public String getFullFileName(AttachmentVO vo){
        TcApplicationdata appData = vo.getApplicationdata();
        if (appData == null) {
            return null;
        }
        
        TcFvitem tcFvitem = appData.getFvitem();
        if (tcFvitem == null) {
            return null;
        }            
        TcDomain domain = tcFvitem.getDomain();
        if (domain == null) {
            return null;
        }

        String host = GlobalConstant.getFvVaultHost(); //GlobalConstant.FVVAULT_HOST;
        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);

        return getFullFileName(tcFvvault.getLocation(), tcFvitem.getFilename());
    }
    public String getFullFileName(String location, String sysFilename){
        if (!location.endsWith("/") && !location.endsWith("\\")) {
            location = location + File.separator;
        }

        return location + sysFilename;
    }
    
    /**
     * 取得FVVAULT位置
     * @param vo
     * @param host
     * @return 
     */
    public String getPathByAttachment(AttachmentVO vo, String host){
        TcApplicationdata appData = vo.getApplicationdata();
        if (appData == null) {
            return null;
        }
        TcFvitem tcFvitem = appData.getFvitem();
        if (tcFvitem == null) {
            return null;
        }            
        TcDomain domain = tcFvitem.getDomain();
        if (domain == null) {
            return null;
        }

        TcFvvault tcFvvault = tcFvvaultFacade.getTcFvvaultByHost(domain, host);
        String path = tcFvvault.getLocation();

        return path;       
    }
    public String getPathByAttachment(AttachmentVO vo){
        String host = GlobalConstant.getFvVaultHost(); //GlobalConstant.FVVAULT_HOST;
        return getPathByAttachment(vo, host);
    }
    
    /**
     * Get AttachmentVO's  Content InputStream
     * @param vo
     * @return
     * @throws FileNotFoundException 
     */
    public InputStream getContentStream(AttachmentVO vo) throws FileNotFoundException {
        byte[] bytArray = vo.getContent();
        logger.debug("getContentStream bytArray = "+(bytArray!=null?bytArray.length:null));
        
        if (bytArray != null) {
            return new ByteArrayInputStream(bytArray);
        }
        
        String fullname = getFullFileName(vo);

        logger.debug("fullname = "+fullname);
        return new FileInputStream(fullname);
    }
    
    /**
     * Save content holder and it's attachments
     * @param domain
     * @param container
     * @param attachmentVOList
     * @param simulated
     * @throws Exception 
     */
    public void saveContent(TcDomain domain, ContentHolder container, List<AttachmentVO> attachmentVOList, boolean simulated) throws Exception {
        if (domain == null) {
            domain = defaultDomain;
        }
        List<TcApplicationdata> applicationdataList = getApplicationdata(container);
        HashSet<TcApplicationdata> existedSet = new HashSet<TcApplicationdata>();
        if (attachmentVOList != null && !attachmentVOList.isEmpty()) {
            for (AttachmentVO attachment : attachmentVOList) {
                if (attachment.getApplicationdata() == null) {
                    InputStream is = new ByteArrayInputStream(attachment.getContent());
                    fileUploadFacade.uploadContent(domain, container, is, attachment.getFileName(), attachment.getSize(), attachment.getContentType(), ContentRole.SECONDARY);
                } else {
                    existedSet.add(attachment.getApplicationdata());
                }
            }
        }
        //remove attachment
        if (applicationdataList != null) {
            for (TcApplicationdata a : applicationdataList) {
                if (!existedSet.contains(a)) {
                    // 附件檔案並未跟著刪除
                    remove(a, simulated);
                    TcFvitem fvItem = a.getFvitem();
                    if (fvItem != null) {
                        fvItem.setTcApplicationdataCollection(null);
                        em.remove(em.merge(fvItem));
                    }
                }
            }
        }        
    }
    public void saveContentSingle(TcDomain domain, ContentHolder container, AttachmentVO attachmentVO, boolean simulated) throws Exception {
        List<AttachmentVO> attachmentVOList = new ArrayList<AttachmentVO>();
        attachmentVOList.add(attachmentVO);
        
        saveContent(domain, container, attachmentVOList, simulated);
    }
    
    /**
     * remove content holder (附件檔案並未跟著刪除)
     * @param container 
     * @param simulated 
     */
    public void removeContent(ContentHolder container, boolean simulated) {
        List<TcApplicationdata> applicationDataList = getApplicationdata(container);
        if (applicationDataList != null && !applicationDataList.isEmpty()) {
            for (TcApplicationdata a : applicationDataList) {
                // 附件檔案並未跟著刪除
                remove(a, simulated);
                TcFvitem fvItem = a.getFvitem();
                if (fvItem != null) {
                    fvItem.setTcApplicationdataCollection(null);
                    em.remove(em.merge(fvItem));
                }
            }
        }
    }
    
    /**
     * get TcApplicationdata by ContentHolder
     * @param container
     * @return 
     */
    public List<TcApplicationdata> getApplicationdata(ContentHolder container) {
        String containerclassname = container.getClass().getCanonicalName();
        Long containerid = container.getId();
        String sql = "SELECT t FROM TcApplicationdata t"
                + " WHERE (t.containerclassname=:containerclassname)"
                + " AND (t.containerid=:containerid)"
                ;
        Query q = em.createQuery(sql);
        q.setParameter("containerclassname", containerclassname);
        q.setParameter("containerid", containerid);
        return q.getResultList();        
    }
    
    /**
     * getFvItemLsit
     * @param containerClassName
     * @param appIds
     * @return 
     */
    public List<FvItemVO> getFvItemLsit(String containerClassName, List<Long> appIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(getFvItemSQL(GlobalConstant.FVVAULT_HOST));
        sql.append(NativeSQLUtils.getInSQL("t.ID", appIds, params));
        
        logger.debug("getFvItemLsit ...");
        ResultSetHelper<FvItemVO> resultSetHelper = new ResultSetHelper(FvItemVO.class);
        List<FvItemVO> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);

        return resList;
    }
    public FvItemVO getSingleFvItem(Long appId){// for TcApplicaiotn與FvItem一對一狀況時應用
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(getFvItemSQL(GlobalConstant.FVVAULT_HOST));
        sql.append("AND t.ID=#appId ");
        params.put("appId", appId);
        
        logger.debug("getFvItemLsit ...");
        ResultSetHelper<FvItemVO> resultSetHelper = new ResultSetHelper(FvItemVO.class);
        List<FvItemVO> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);

        return (resList!=null && resList.size()>0)?resList.get(0):null;
    }
    public String getFvItemSQL(String host) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select t.ID APPID, t.CONTAINERCLASSNAME, t.CONTAINERID, t.DESCRIPTION \n");
        sql.append(", t.FVITEM FVITEMID \n");
        sql.append(", i.ID, i.DOMAIN, i.FILENAME, i.NAME ORIFILENAME, i.CONTENTTYPE, i.FILESIZE \n");
        sql.append(", m.NAME DOMAINNAME \n");
        sql.append(", v.LOCATION \n");
        sql.append("from TC_APPLICATIONDATA t \n");
        sql.append("join TC_FVITEM i on i.ID=t.FVITEM \n");
        sql.append("join TC_DOMAIN m on m.ID=i.DOMAIN \n");
        sql.append("join TC_FVVAULT v on v.DOMAIN=m.ID AND v.HOSTNAME='").append(GlobalConstant.FVVAULT_HOST).append("' \n");
        sql.append("where 1=1 \n");
        
        return sql.toString();
    }

    /**
     * 查存在 FV DB 實體檔名
     * @param domainName
     * @param containerClazz
     * @return 
     */
    public List<String> findFvFileNameList(String domainName, Class containerClazz){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("select I.FILENAME \n");
        sql.append("from TC_APPLICATIONDATA A \n");
        sql.append("join TC_FVITEM I ON I.ID=A.FVITEM \n");
        sql.append("join TC_DOMAIN D ON D.ID=I.DOMAIN \n");
        sql.append("join TC_FVVAULT V ON V.DOMAIN=D.ID AND V.HOSTNAME='localhost' \n");
        sql.append("where 1=1 \n");
        sql.append("and D.NAME=#DOMAINNAME \n");
        sql.append("and A.CONTAINERCLASSNAME=#CONTAINERCLASSNAME \n");
        
        params.put("DOMAINNAME", domainName);
        params.put("CONTAINERCLASSNAME", containerClazz.getName());
        
        return this.findStringList(sql.toString(), params);
    }

    public boolean saveFiles(ContentHolder holder, List<AttachmentVO> newAttachments, List<Long> existedAppIds, 
            boolean removeOri, TcDomain domain, TcUser tcUser, boolean simulated) throws Exception{
        if( newAttachments==null ){
            logger.error("saveUploadedFiles error newAttachments==null");
            return false;
        }
        logger.debug("saveUploadedFiles ...");
        
        // 刪除原關聯檔
        if( removeOri ){
            logger.debug("saveUploadedFiles removeContent holder ="+holder);
            removeContent(holder, simulated);
        }
        
        // 取得並保留已存在 App Ids 至 existedAppIds，以便後續分辨哪些是本次上傳的
        List<AttachmentVO> attachmentList = findExistedAttachments(holder, existedAppIds);
        
        attachmentList.addAll(newAttachments);
        logger.debug("saveUploadedFiles attachmentList = "+attachmentList.size());

        contentFacade.saveContent(domain, holder, attachmentList, tcUser);
        return true;
    }

    /**
     * 取得並保留已存在 App Ids 至 existedAppIds，以便後續分辨哪些是本次上傳的
     * @param holder
     * @param existedAppIds
     * @return 
     */
    public List<AttachmentVO> findExistedAttachments(ContentHolder holder, List<Long> existedAppIds){
        List<AttachmentVO> attachmentList = loadContent(holder);
        
        if( attachmentList==null ){
            attachmentList = new ArrayList<AttachmentVO>();
        }else{
            // 保留已存在 App Ids，以便後續分辨哪些是本次上傳的
            if( existedAppIds!=null ){
                for(AttachmentVO vo : attachmentList){
                    existedAppIds.add(vo.getApplicationdata().getId());
                }
            }
        }
        return attachmentList;
    }
                
    /**
     * 新增附件
     * @param holder
     * @param domain
     * @param is
     * @param filename
     * @param contentType
     * @param removeOri
     * @param tcUser
     * @param simulated
     * @return
     * @throws IOException
     * @throws Exception 
     */
    public boolean addAttachment(ContentHolder holder,
        TcDomain domain, InputStream is, String filename, String contentType, 
        boolean removeOri, TcUser tcUser, boolean simulated) throws IOException, Exception{

        byte[] content = IOUtils.toByteArray(is); // bodyPartEntity.getInputStream());
        logger.debug("inputStreamToAttachmentVO content size = "+((content!=null)?content.length:0));
      
        AttachmentVO attachmentVO = genAttachmentVO(filename, contentType, content, 0);
        if( attachmentVO.getContent()==null ){
            logger.error("addAttachment attachmentVO.getContent()==null");
            return false;
        }

        List<AttachmentVO> newAttachments = new ArrayList<AttachmentVO>();
        newAttachments.add(attachmentVO);
        boolean res = saveFiles(holder, newAttachments, null, removeOri, domain, tcUser, simulated);

        return res;
    }
}

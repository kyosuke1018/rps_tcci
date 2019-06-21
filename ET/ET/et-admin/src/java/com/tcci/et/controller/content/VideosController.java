/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.content;

import com.tcci.cm.enums.ActionEnum;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.enums.ContentStatusEnum;
import com.tcci.et.enums.OpenMethodEnum;
import com.tcci.et.enums.VideoLibraryEnum;
import com.tcci.et.facade.KbLinkFacade;
import com.tcci.et.facade.KbVideoFacade;
import com.tcci.et.model.VideoVO;
import com.tcci.et.model.criteria.PublicationCriteriaVO;
import com.tcci.et.model.criteria.MediaCriteriaVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * 圖庫管理
 * @author Peter.pan
 */
@ManagedBean(name = "videos")
@ViewScoped
public class VideosController extends MediaController implements Serializable {
    private static final long FUNC_OPTION = 27;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB KbVideoFacade videoFacade;
    @EJB KbLinkFacade linkFacade;
    
    // tree
    private TreeNode root;
    private TreeNode selectedNode;
    private boolean showTree = true;
    private boolean showVideo = false;
    private Map<Long, Boolean> expandMap;
    private boolean first = true;
    
    // edit
    private String mode;
    // private ActionEnum editMode = ActionEnum.MODIFY;
    private VideoVO editVO;
    private VideoVO editSubVO;
    //private LinkVO linkVO;
    List<VideoVO> videoList;

    List<VideoVO> folderList;
    List<VideoVO> resList;
    List<VideoVO> filterResultList;
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if("kyle.cheng".equals(this.getLoginUser().getLoginAccount())){
            functionDenied = true;
        }else{
        if( functionDenied ){ return; }
        }
        // Get view Id
        viewId = JsfUtils.getViewId();
        
        if( viewId.endsWith("/videosSelect.xhtml") ){// 自影片庫選影片
            mode = ActionEnum.CHOICE.getCode();
        }else{
            mode = ActionEnum.MODIFY.getCode();
        }
        logger.debug("init mode = "+mode);
                
        criteriaVO = new MediaCriteriaVO();                
        expandMap = new HashMap<Long, Boolean>();// 記錄樹節點展開狀況
        rebuildTree();// 建立樹狀圖
    }
    
    /**
     * 編輯操作
     */
    public void changeEditMode(){
        logger.debug("changeEditMode editMode = "+this.mode);
        if( ActionEnum.MODIFY.getCode().equals(mode) ){
            prepareEditNode();// 編輯基本資料
        }else if( ActionEnum.ADDSUB.getCode().equals(mode) ){
            prepareAddSubNode();// 新增影片資料夾
        }else if( ActionEnum.UPLOAD.getCode().equals(mode) ){
            prepareAddVideo();// 新增包含影片
        }
    }
    
    /**
     * 準備包含影片
     * @param parentVO 
     */
    public void prepareIncludeVideos(VideoVO parentVO){
        logger.debug("prepareIncludeVideos parentVO = "+parentVO);
        if( parentVO.getTypeEnum()== VideoLibraryEnum.CUSTOM ){
            videoList = videoFacade.findSubVideos(parentVO.getId());
        }else if( parentVO.getTypeEnum()== VideoLibraryEnum.DOC ){
            videoList = videoFacade.findVideosByDoc(criteriaVO);
        }
        
        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT);
        //filterResultList = new ArrayList<VideoVO>();
        //copyList(filterResultList, videoList);
        //filterResultList = videoList; // filterValue 初始化 // for 列表顯示
    }
    
    /**
     * 準備編輯目錄
     */
    public void prepareEditNode(){
        //editMode = ActionEnum.MODIFY;
        if( !isChoiceOnly() ){
            this.mode = ActionEnum.MODIFY.getCode();
        }
        logger.debug("prepareEditNode editVO = "+editVO);
    }
    
    /**
     * 準備新增子目錄
     */
    public void prepareAddSubNode(){
        //editMode = ActionEnum.ADDSUB;
        if( !isChoiceOnly() ){
            this.mode = ActionEnum.ADDSUB.getCode();
        }
        editSubVO = new VideoVO();
        editSubVO.setParent(editVO.getId());// 父目錄
        editSubVO.setPrimaryType(VideoLibraryEnum.CUSTOM.getCode());// 自訂影片夾
    }
    
    /**
     * 準備新增影片
     */
    public void prepareAddVideo(){
        //editMode = ActionEnum.UPLOAD;
        if( !isChoiceOnly() ){
            this.mode = ActionEnum.UPLOAD.getCode();
        }
        // for 上傳影片
        editSubVO = new VideoVO();
        editSubVO.setParent(editVO.getId());// 父目錄
        editSubVO.setPrimaryType(VideoLibraryEnum.VIDEO.getCode());// 影片
        editSubVO.setStatus(ContentStatusEnum.PUBLISH.getCode());// 影片預設發佈(因實際已於Youtube發佈)
        editSubVO.setOpenMethod(OpenMethodEnum.ORIGIN.getCode());
    }
    
    /**
     * 準備修改影片
     * @param vo
     */
    public void prepareEditVideo(VideoVO vo){
        logger.debug("prepareEditVideo vo="+vo);
        //editMode = ActionEnum.UPLOAD;
        if( !isChoiceOnly() ){
            this.mode = ActionEnum.UPLOAD.getCode();
        }
        editSubVO = vo;
    }
    
    /**
     * 刪除上傳影片
     * @param vo
     */
    public void deleteVideos(VideoVO vo){
        logger.debug("deleteVideos ...");
        ActivityLogEnum acEnum = ActivityLogEnum.D_VIDEO;
        try{
            if( vo!=null 
                && VideoLibraryEnum.VIDEO.getCode().equals(vo.getPrimaryType()) 
                && editVO.getId().equals(vo.getParent())){// 安全檢核
                videoFacade.deleteVideo(vo, this.isSimulated());
                
                prepareIncludeVideos(editVO);// reload 該節點包含影片
                
                cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                        vo.getTitle(), null, true, this.getLoginUser(), this.isSimulated());
                
                JsfUtils.buildSuccessCallback();
                return;
            }else{
                JsfUtils.addErrorMessage("無法刪除!");
            }
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "deleteVideos", e, false);
        }
        
        JsfUtils.buildErrorCallback();
        cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), (vo!=null)?vo.getId():0,
                (vo!=null)?vo.getTitle():null, null, true, this.getLoginUser(), this.isSimulated());
    }
    
    /**
     * 輸入資料檢核
     * @param vo
     * @return 
     */
    public boolean checkInputDataBasic(VideoVO vo){
        logger.debug("checkInputDataBasic vo = "+vo);
        boolean hasErr = false;
        
        if( StringUtils.length(vo.getCname())>40 ){
            JsfUtils.addErrorMessage("[名稱]欄位輸入長度過長!(最多40個字)");
            hasErr = true;
        }
        if( StringUtils.length(vo.getTitle())>100 ){
            JsfUtils.addErrorMessage("[標題]欄位輸入長度過長!(最多100個字)");
            hasErr = true;
        }
        if( StringUtils.length(vo.getDescription())>500 ){
            JsfUtils.addErrorMessage("[簡介]欄位輸入長度過長!(最多500個字)");
            hasErr = true;
        }
        
        return !hasErr;
    }
    public boolean checkInputData(){
        logger.debug("checkInputData editMode = "+this.mode);    
        if( ActionEnum.MODIFY.getCode().equals(mode) ){ // 編輯基本資料
            if( StringUtils.isBlank(editVO.getCname()) ){// 必填
                JsfUtils.addErrorMessage("未輸入[名稱]!");
                return false;
            }
            return checkInputDataBasic(this.editVO);
        }else if( ActionEnum.ADDSUB.getCode().equals(mode) ){ // 新增子資料夾/相簿
            if( StringUtils.isBlank(editSubVO.getCname()) ){// 必填
                JsfUtils.addErrorMessage("未輸入[名稱]!");
                return false;
            }
            return checkInputDataBasic(this.editSubVO);
        }else if( ActionEnum.UPLOAD.getCode().equals(mode) ){ // 新增包含圖片
            if( StringUtils.isBlank(this.editSubVO.getTitle()) ){// 必填
                JsfUtils.addErrorMessage("未輸入[標題]!");
                return false;
            }
            if( StringUtils.isBlank(this.editSubVO.getUrl()) ){// 必填
                JsfUtils.addErrorMessage("未輸入[影片網址]!");
                return false;
            }
            if( StringUtils.length(editSubVO.getUrl())>1024 ){
                JsfUtils.addErrorMessage("[影片網址]欄位輸入長度過長!(最多1024個字元)");
                return false;
            }
            return checkInputDataBasic(this.editSubVO);
        }
        
        return true;
    }
    
    /**
     * 基本資料儲存
     */
    public void saveBasic(){
        logger.debug("saveBasic ...");
        ActivityLogEnum acEnum = ActivityLogEnum.U_VIDEO_FOLDER;    
        try{
            // 輸入資料檢核
            if( !checkInputData() ){
                return;
            }
            videoFacade.saveVO(editVO, this.getLoginUser(), this.isSimulated());
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(), 
                    editVO.getCname(), null, true, this.getLoginUser(), this.isSimulated());
            
            rebuildTree();// TODO
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "saveBasic", e, false);
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(), 
                    editVO.getCname(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }

    /**
     * 子資料夾儲存
     */
    public void saveFolder(){
        logger.debug("saveFolder ...");
        ActivityLogEnum acEnum = ActivityLogEnum.A_VIDEO_FOLDER;
        try{
            // 輸入資料檢核
            if( !checkInputData() ){
                return;
            }
            videoFacade.saveVO(editSubVO, this.getLoginUser(), this.isSimulated());
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editSubVO.getId(),
                    editSubVO.getCname(), editSubVO.getDescription(), true, this.getLoginUser(), this.isSimulated());
            rebuildTree();// TODO
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "saveFolder", e, false);
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editSubVO.getId(),
                    editSubVO.getCname(), editSubVO.getDescription(), false, this.getLoginUser(), this.isSimulated());
        }
    }

    /**
     * 儲存圖片
     */
    public void saveVideo(){
        logger.debug("saveVideo ...");
        ActivityLogEnum acEnum = ActivityLogEnum.A_VIDEO;
        try{
            // 輸入資料檢核
            if( !checkInputData() ){
                return;
            }
            videoFacade.saveVideo(editSubVO, this.getLoginUser(), this.isSimulated());
                
            prepareIncludeVideos(editVO);// reload 該節點包含影片
                        
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editSubVO.getId(),
                    editSubVO.getTitle(), null, true, this.getLoginUser(), this.isSimulated());

            prepareAddVideo();// 回到新增圖片狀態
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "saveVideo", e, false);
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editSubVO.getId(),
                    editSubVO.getTitle(), null, true, this.getLoginUser(), this.isSimulated());
        }
    }
    
    /**
     * Folder可否刪除
     * @param vo
     * @return 
     */
    public boolean canDeleteFolder(VideoVO vo){
        logger.debug("canDeleteFolder ...");
        
        return (!isChoiceOnly()) && (!vo.getId().equals(0L)) && (vo.getSubCounts()==0);
    }
    
    /**
     * 刪除資料夾
     * @param vo
     */
    public void deleteFolder(VideoVO vo){
        logger.debug("deleteFolder ...");
        ActivityLogEnum acEnum = ActivityLogEnum.D_VIDEO_FOLDER;
        try{
            if( !canDeleteFolder(vo) ){
                return;
            }
            videoFacade.remove(vo.getId(), this.isSimulated());
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getCname(), null, true, this.getLoginUser(), this.isSimulated());
            // this.selectedNode = node.getParent();
            rebuildTree();// TODO
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "deleteFolder", e, false);
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getCname(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    public void doResetUpdate(){
        logger.debug("doResetUpdate ...");
        prepareEditNode();// 回編輯基本資料模式
    }
    
    //<editor-fold defaultstate="collapsed" desc="for tree">
    /**
     * 隱藏顯示 Tree
     */
    public void displayTree(){
        showTree = !showTree;
        logger.debug("displayTree showTree = "+showTree);
    }

    /**
     * 縮小影片顯示
     */
    public void displayVideo(){
        showVideo = !showVideo;
        logger.debug("displayVideo showVideo = "+showVideo);
    }
    
    /**
     * 建立樹狀圖
     */
    public void rebuildTree(){
        folderList = videoFacade.findCustomFolder();
        buildTree(folderList);// 建立樹狀圖
    }
    
    // for 所在資料夾
    public List<SelectItem> getParentFolderOptions(Long excludeId){
        return genParentFolderOptions(folderList, excludeId);
    }
    public List<SelectItem> genParentFolderOptions(List<VideoVO> list, Long excludeId){
        List<SelectItem> ops = new ArrayList<SelectItem>();
        ops.add(new SelectItem("0", VideoLibraryEnum.CUSTOM.getDisplayName()));// 自訂影片庫
        
        if( list!=null ){
            // 找出 excludeId 所有下層 ID，皆不可設定
            List<Long> excludeList = new ArrayList<Long>();
            findSubFolder(list, excludeId, excludeList);
        
            for(VideoVO vo : list){
                if( VideoLibraryEnum.CUSTOM==vo.getTypeEnum() ){// folder
                    if( !excludeList.contains(vo.getId()) ){// 過濾不可設定節點
                        String label = (vo.getParentName()!=null)? vo.getParentName()+"/"+vo.getCname():vo.getCname();
                        ops.add(new SelectItem(vo.getId().toString(), label));
                    }
                }
            }
        }
        return ops;
    }
    
    /**
     * 遞迴找出下層節點
     * @param allList
     * @param keyId
     * @param resList 
     */
    public void findSubFolder(List<VideoVO> allList, Long keyId, List<Long> resList){
        if( keyId==null || allList==null || allList.isEmpty() ){
            return;
        }
        resList.add(keyId);
        
        for(VideoVO vo : allList){
            if( keyId.equals(vo.getParent()) ){
                findSubFolder(allList, vo.getId(), resList);
            }
        }
    }
    
    public void chooseEditVO(TreeNode node){
        editVO = (VideoVO)node.getData();
        prepareIncludeVideos(editVO);// 該節點包含影片
        prepareEditNode();// 預設編輯節點
    }
    
    public void onNodeSelect(NodeSelectEvent event) {
        logger.debug("onNodeSelect selectedNode = "+this.selectedNode);
        chooseEditVO(selectedNode);
        
        if( !isChoiceOnly() ){
            this.mode = ActionEnum.MODIFY.getCode();// default action
        }
    }
    
    public void onNodeExpand(NodeExpandEvent event) {
        logger.debug("onNodeExpand = "+event.getTreeNode().toString());
        this.expandMap.put(((VideoVO)event.getTreeNode().getData()).getId(), Boolean.TRUE);
    }
 
    public void onNodeCollapse(NodeCollapseEvent event) {
        logger.debug("onNodeCollapse = "+event.getTreeNode().toString());
        this.expandMap.put(((VideoVO)event.getTreeNode().getData()).getId(), Boolean.FALSE);
    }
    
    public void checkSelectedNode(TreeNode node){
        if( selectedNode!=null 
        && ((VideoVO)selectedNode.getData()).getId().equals(((VideoVO)node.getData()).getId()) ){
            node.setSelected(true);
        }
    }
    
    public void checkExpandNode(TreeNode node){
        Long id = ((VideoVO)node.getData()).getId();
        if( expandMap.get(id)!=null && expandMap.get(id) ){
            node.setExpanded(true);
            logger.debug("checkExpandNode "+id+"="+node.isExpanded());
        }
    }
    
    /**
     * 建立樹狀圖
     * @param rootVO 
     */
    private void buildTree(List<VideoVO> allList){
        VideoVO rootVO = new VideoVO();
        rootVO.setId(0L);
        root = new DefaultTreeNode("R", rootVO, null);

        // 目錄 (可自訂子目錄)
        List<VideoLibraryEnum> enumsD = VideoLibraryEnum.getDirectories(true);
        //boolean first = true;
        for(VideoLibraryEnum enumD : enumsD){
            VideoVO vo = new VideoVO();
            vo.setId(0L);
            vo.setCname(enumD.getDisplayName());
            vo.setPrimaryType(enumD.getCode());
            
            TreeNode nodeD = new DefaultTreeNode(enumD.getCode(), vo, root);
            
            if( VideoLibraryEnum.CUSTOM == enumD){
                nodeD.setExpanded(true);// 預設展開

                // 自訂子樹
                buildSubTree(allList, nodeD);

                if( first ){// 預設選取
                    nodeD.setSelected(true);
                    selectedNode = nodeD;
                    chooseEditVO(selectedNode);
                    first = !first;
                }else{
                    checkSelectedNode(nodeD);
                }
            }
        }
    }
    
    /**
     * 遞迴建立子樹
     * @param allList
     * @param parent 
     */
    private void buildSubTree(List<VideoVO> allList, TreeNode parent){
        if( allList==null || parent==null ){
            return;
        }
        
        // 子節點
        List<VideoVO> subList = videoFacade.findSubItems(allList, ((VideoVO)parent.getData()).getId());
        if( subList!=null ){
            for(VideoVO vo : subList){
                vo.setCanEdit(true);
                TreeNode subNode = new DefaultTreeNode(vo.getPrimaryType(), vo, parent);
                
                checkSelectedNode(subNode);// 是否選取
                checkExpandNode(subNode);// 是否展開
                
                buildSubTree(allList, subNode);
            }
        }
    }
    //</editor-fold>
    
    public void copyList(List<VideoVO> desList, List<VideoVO> srcList){
        if( srcList!=null ){
            for(VideoVO vo : srcList){
                desList.add(vo);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="for 文章插入影片選取">
    public void doQueryDoc(){
        logger.debug("doQueryDoc ...");
        videoList = videoFacade.findVideosByDoc(criteriaVO);
        // for 列表顯示
        filterResultList = new ArrayList<VideoVO>();
        copyList(filterResultList, videoList);
        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }

    public void doResetDoc(){
        logger.debug("doResetDoc ...");
        PublicationCriteriaVO docCriteriaVO = new PublicationCriteriaVO();
        this.criteriaVO.setDocCriteriaVO(docCriteriaVO);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for 控制顯示區域">
    /**
     * 控制顯示區域
     * @return 
     */
    public boolean isChoiceOnly(){
        // logger.debug("isChoiceOnly mode = "+mode);
        return (ActionEnum.getFromCode(mode) == ActionEnum.CHOICE);
    }
    public boolean canEditVideo(){// 只有[自訂影片]在此提供編輯
        return (!isChoiceOnly()) && (showCustom());
    }
    public boolean canDeleteVideo(){// 只有[自訂影片]、[文章影片]在此提供刪除
        return (!isChoiceOnly()) && (showCustom() || showIllustration());
    }
    
    public boolean showEditNodeBlock(){
        return (ActionEnum.getFromCode(mode) == ActionEnum.MODIFY);
    }
    public boolean showAddSubNodeBlock(){
        return (ActionEnum.getFromCode(mode) == ActionEnum.ADDSUB);
    }
    public boolean showUploadBlock(){
        return (ActionEnum.getFromCode(mode) == ActionEnum.UPLOAD);
    }
    
    public boolean showCustom(){
        return (VideoLibraryEnum.CUSTOM == VideoLibraryEnum.getFromCode(editVO.getPrimaryType()));
    }
    public boolean showIllustration(){
        return (VideoLibraryEnum.DOC == VideoLibraryEnum.getFromCode(editVO.getPrimaryType()));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Export">
    /**
     * 匯出
     */
    public void prepareExport() {
        logger.debug("prepareExport ...");
        if (CollectionUtils.isEmpty(filterResultList)) {
            // 無查詢結果
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("common.no.result"));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public boolean isShowTree() {
        return showTree;
    }

    public void setShowTree(boolean showTree) {
        this.showTree = showTree;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<VideoVO> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoVO> videoList) {
        this.videoList = videoList;
    }

    public List<VideoVO> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<VideoVO> folderList) {
        this.folderList = folderList;
    }

    public VideoVO getEditSubVO() {
        return editSubVO;
    }

    public void setEditSubVO(VideoVO editSubVO) {
        this.editSubVO = editSubVO;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public boolean isShowVideo() {
        return showVideo;
    }

    public void setShowVideo(boolean showVideo) {
        this.showVideo = showVideo;
    }

    public Map<Long, Boolean> getExpandMap() {
        return expandMap;
    }

    public void setExpandMap(Map<Long, Boolean> expandMap) {
        this.expandMap = expandMap;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public VideoVO getEditVO() {
        return editVO;
    }

    public void setEditVO(VideoVO editVO) {
        this.editVO = editVO;
    }

    public List<VideoVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<VideoVO> filterResultList) {
        this.filterResultList = filterResultList;
    }
    
    public List<VideoVO> getResList() {
        return resList;
    }

    public void setResList(List<VideoVO> resList) {
        this.resList = resList;
    }
    //</editor-fold>
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.facade.EcMemberFacade;
import com.tcci.ec.facade.rs.AuthREST;
import com.tcci.ec.facade.rs.MemberREST;
import com.tcci.ec.facade.rs.TenderREST;
import com.tcci.ec.model.MemberVO;
import com.tcci.ec.model.TenderConformVO;
import com.tcci.ec.model.TenderVO;
import com.tcci.ec.model.VenderVO;
import com.tcci.ec.model.rs.AttachmentRsVO;
import com.tcci.ec.model.rs.BaseListResponseVO;
import com.tcci.ec.model.rs.SubmitVO;
import com.tcci.ec.model.rs.TenderRsVO;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.et.facade.rs.BaseRESTClient;
import static com.tcci.et.facade.rs.RestClient.getClientConfig;
import com.tcci.fc.vo.AttachmentVO;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "tenderController")
@ViewScoped
public class TenderController extends SessionAwareController implements Serializable {
    
    @Inject
    private MemberREST memberREST;
    @Inject
    private TenderREST tenderREST;
    @EJB
    private EcMemberFacade memberFacade;
    
    private EcMember member;
    private MemberVO memberVO;
    private SubmitVO formVO;
    private List<TenderRsVO> tenderList = new ArrayList<>();
    private TenderVO tenderVO;
    private List<VenderVO> venderList;//會員綁定 供應商類別符合
    private TenderConformVO tenderConformVO;
    private List<AttachmentRsVO> attachmentList;
    private StreamedContent downloadFile;
    
    @PostConstruct
    private void init(){
        logger.info("init ...");
        try{
            member = this.getLoginUser();
            if(member!=null){
                memberVO = this.findMemberFullInfo();
            }
            
            tenderList = findTenderList();
            
            formVO = new SubmitVO();
        } catch (Exception e) {
            // ignore;
            logger.debug("logout exception :\n", e);
        }
    }
    
    private MemberVO findMemberFullInfo(){
        logger.debug("findMemberFullInfo = "+member.getId());
        try{
//            MemberVO vo = memberFacade.findById(member.getId(), true, GlobalConstant.DEF_LOCALE.getLocale());

//            String url = "http://localhost:8080/et-web/services/members/full" + "/" + member.getId();
            String url = sys.getRestUrlPrefix()+"/services/members/full" + "/" + member.getId();//sys.getRestUrlPrefix():http://192.168.203.51/et-web
            logger.debug("url = "+url);
            BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
            Invocation.Builder reqBuilder = restClient.getWebTarget().request();
            MemberVO vo = reqBuilder.get(MemberVO.class);
            logger.debug("name = "+vo.getName());
            
            return vo;
        } catch (Exception e) {
            // ignore;
            logger.debug("findMemberFullInfo exception :\n", e);
        }
        return null;
    }
    
    private List<TenderRsVO> findTenderList(){
        try{
            formVO = new SubmitVO();
            formVO.setType("STATUS");
            formVO.setStatus(TenderStatusEnum.END.getCode());
//            String url = "http://localhost:8080/et-web/services/tender/list";
            String url = sys.getRestUrlPrefix()+"/services/tender/list";//sys.getRestUrlPrefix():http://192.168.203.51/et-web
            logger.debug("url = "+url);
            BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
            Invocation.Builder reqBuilder = restClient.getWebTarget().request();
            BaseListResponseVO vo = reqBuilder.post(
                    Entity.entity(formVO, MediaType.APPLICATION_JSON_TYPE), BaseListResponseVO.class);
            
            logger.debug("List = "+vo.getList().size());
            return vo.getList();
        } catch (Exception e) {
            // ignore;
            logger.debug("findMemberFullInfo exception :\n", e);
        }
        return new ArrayList<>();
    }
    
    public void selectTender(Long id) {
        try{
//            String url = "http://localhost:8080/et-web/services/tender/full" + "/" + id;
            String url = sys.getRestUrlPrefix()+"/services/tender/full" + "/" + id;//sys.getRestUrlPrefix():http://192.168.203.51/et-web
            logger.debug("url = "+url);
            BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
            Invocation.Builder reqBuilder = restClient.getWebTarget().request();
            tenderVO = reqBuilder.get(TenderVO.class);
            
            
            if(memberVO != null){
                formVO = new SubmitVO();
                formVO.setMemberId(memberVO.getMemberId());
                formVO.setTenderId(tenderVO.getId());
                //會員綁定 符合的供應商類別
                venderList = findMatchVenderList(formVO);
                //會員 案件投標紀錄
                tenderConformVO = findConformLog(formVO);
                
                
                logger.debug("HtmlId = "+tenderVO.getHtmlId());
                logger.debug("Contents = "+tenderVO.getContents());
                
                //附件
                attachmentList = tenderVO.getDocRsList();
                if(CollectionUtils.isNotEmpty(attachmentList)){
                    for(AttachmentRsVO vo : attachmentList){
                        logger.debug("vo = "+vo.getIndex());
                        logger.debug("vo FileName= "+vo.getFileName());
                        logger.debug("vo ContentType= "+vo.getContentType());
                    }
                }
                
            }
            
            logger.debug("status = "+tenderVO.getStatus());
        } catch (Exception e) {
            // ignore;
            logger.debug("selectTender exception :\n", e);
        }
    }
    
    private List<VenderVO> findMatchVenderList(SubmitVO formVO){
        try{
//            String url = "http://localhost:8080/et-web/services/members/checkVenderCategory";
            String url = sys.getRestUrlPrefix()+"/services/members/checkVenderCategory";//sys.getRestUrlPrefix():http://192.168.203.51/et-web
            logger.debug("url = "+url);
            BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
            Invocation.Builder reqBuilder = restClient.getWebTarget().request();
            BaseListResponseVO vo = reqBuilder.post(
                    Entity.entity(formVO, MediaType.APPLICATION_JSON_TYPE), BaseListResponseVO.class);
            
            logger.debug("List = "+vo.getList().size());
            return vo.getList();
        } catch (Exception e) {
            // ignore;
            logger.debug("findMatchVenderList exception :\n", e);
        }
        return new ArrayList<>();
    }
    
    private TenderConformVO findConformLog(SubmitVO formVO){
        try{
//            String url = "http://localhost:8080/et-web/services/tender/findConformLog";
            String url = sys.getRestUrlPrefix()+"/services/tender/findConformLog";//sys.getRestUrlPrefix():http://192.168.203.51/et-web
            logger.debug("url = "+url);
            BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
            Invocation.Builder reqBuilder = restClient.getWebTarget().request();
            TenderConformVO vo = reqBuilder.post(
                    Entity.entity(formVO, MediaType.APPLICATION_JSON_TYPE), TenderConformVO.class);
            
            logger.debug("vo id = "+vo.getId());
            return vo;
        } catch (Exception e) {
            // ignore;
            logger.debug("findConformLog exception :\n", e);
        }
        return null;
    }
    
    public void conform(Long venderId) {
        logger.debug("conform!  tender= "+tenderVO.getId());
        logger.debug("conform!  member= "+memberVO.getId());
        logger.debug("conform!  venderId= "+venderId);
        if(tenderVO == null || memberVO == null || venderId==null){
            JsfUtils.addErrorMessage("會員或供應商不符合投標資格!");
            return;
        }
       
        //conform
        formVO = new SubmitVO();
        formVO.setMemberId(memberVO.getMemberId());
        formVO.setTenderId(tenderVO.getId());
        formVO.setVenderId(venderId);
        tenderConformVO = conform(formVO);
    } 
    
    private TenderConformVO conform(SubmitVO formVO){
        try{
//            String url = "http://localhost:8080/et-web/services/tender/conform";
            String url = sys.getRestUrlPrefix()+"/services/tender/conform";//sys.getRestUrlPrefix():http://192.168.203.51/et-web
            BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
            Invocation.Builder reqBuilder = restClient.getWebTarget().request();
            TenderConformVO vo = reqBuilder.post(
                    Entity.entity(formVO, MediaType.APPLICATION_JSON_TYPE), TenderConformVO.class);
            
            logger.debug("vo id = "+vo.getId());
            return vo;
        } catch (Exception e) {
            // ignore;
            logger.debug("conform exception :\n", e);
        }
        return null;
    }
    
    public boolean fetchDocFile(Long appId) throws FileNotFoundException, UnsupportedEncodingException{
        try{
//            String url = sys.getRestUrlPrefix()+"/services/tender/file"
//                    + "/" + tenderVO.getId()
//                    + "/" + memberVO.getMemberId()
//                    + "/" + appId;//sys.getRestUrlPrefix():http://192.168.203.51/et-web
//            logger.debug("url = "+url);
//            BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
//            Invocation.Builder reqBuilder = restClient.getWebTarget().request();
//            attachmentVO = reqBuilder.get(AttachmentRsVO.class);
            formVO = new SubmitVO();
            formVO.setMemberId(memberVO.getMemberId());
            formVO.setTenderId(tenderVO.getId());
            formVO.setAppId(appId);
            String url = sys.getRestUrlPrefix()+"/services/tender/getFile";//sys.getRestUrlPrefix():http://192.168.203.51/et-web
            BaseRESTClient restClient = new BaseRESTClient(url, getClientConfig());
            Invocation.Builder reqBuilder = restClient.getWebTarget().request();
            AttachmentRsVO attachmentVO = reqBuilder.post(
                    Entity.entity(formVO, MediaType.APPLICATION_JSON_TYPE), AttachmentRsVO.class);
            
            if( attachmentVO.getContent()!=null ){
//                String fileName = URLEncoder.encode(attachmentVO.getFileName(), GlobalConstant.ENCODING_DEF);
                String fileName = attachmentVO.getFileName();
                downloadFile = new DefaultStreamedContent(
//                        attachmentFacade.getContentStream(attachmentVO),
                        new ByteArrayInputStream(attachmentVO.getContent()),
//                        attachmentVO.getInputStream(),
                        attachmentVO.getContentType(),
                        fileName);
                return true;
            }else{
                logger.error("fetchDocFile error : attachmentVO=null");
            }
        } catch (Exception e) {
            // ignore;
            logger.debug("fetchDocFile exception :\n", e);
        }
        
        return false;
    }
    
    public MemberVO getMemberVO() {
        return memberVO;
    }

    public TenderVO getTenderVO() {
        return tenderVO;
    }

    public List<TenderRsVO> getTenderList() {
        return tenderList;
    }

    public List<VenderVO> getVenderList() {
        return venderList;
    }

    public TenderConformVO getTenderConformVO() {
        return tenderConformVO;
    }

    public void setTenderConformVO(TenderConformVO tenderConformVO) {
        this.tenderConformVO = tenderConformVO;
    }

    public List<AttachmentRsVO> getAttachmentList() {
        return attachmentList;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }
    
    
    
    
    
}

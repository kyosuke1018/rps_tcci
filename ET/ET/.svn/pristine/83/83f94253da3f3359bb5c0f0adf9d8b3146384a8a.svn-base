/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade.rs;

import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtCompany;
import com.tcci.et.entity.EtFile;
import com.tcci.et.entity.EtMember;
import com.tcci.et.enums.FileEnum;
import com.tcci.et.enums.rs.ResStatusEnum;
import com.tcci.et.facade.EtCompanyFacade;
import com.tcci.et.facade.EtFileFacade;
import com.tcci.et.model.FileVO;
import com.tcci.et.model.VenderVO;
import com.tcci.et.model.rs.SubmitVO;
import com.tcci.et.model.rs.TenderRsVO;
import com.tcci.et.entity.EtMemberForm;
import com.tcci.et.entity.EtVender;
import com.tcci.et.entity.EtVenderAll;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.enums.FormTypeEnum;
import com.tcci.et.facade.EtVenderAllFacade;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.StringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 *
 * @author Kyle.Cheng
 */
@Path("/vender")
public class VenderREST extends AbstractWebREST {
    
    @EJB private EtCompanyFacade etCompanyFacade;
    @EJB EtFileFacade fileFacade;
    @EJB private EtVenderAllFacade etVenderAllFacade;
    
    /**
     * /services/vender/count
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response count(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("find list ...");
        EtMember member = null;
        List<TenderRsVO> result = new ArrayList<>();
        try {
            if(formVO!=null){
            }else{
                BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
                criteriaVO.setDisabled(Boolean.FALSE);//排除黑名單
//                int totalRows = etVenderFacade.countLfa1ByCriteria(criteriaVO);
//                logger.debug("count vender total totalRows = "+totalRows);
//                return this.genSuccessRepsoneWithCount(request, totalRows);
                //要distinct MANDT, LIFNR_UI
                List<VenderVO> list = etVenderFacade.findLfa1ByCriteria(criteriaVO);
                logger.debug("count vender total list.size = "+list.size());
                return this.genSuccessRepsoneWithCount(request, list.size());
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**會員 申請供應商
     * /services/vender/apply
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/apply")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apply(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        EtMember member = null;
        List<String> errors = new ArrayList<>();
        try{
            member = getReqUser(request);
            if(member == null){
                member = memberFacade.find(formVO.getMemberId());
            }
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            
            if(member == null){
                logger.error("apply member==null");
                errors.add("無登入資訊");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }else if(formVO.getFactoryId()==null && StringUtils.isBlank(formVO.getApplyVenderCode())){
                errors.add("申請供應商綁定 類別有誤");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, errors);
            }
            
            FormTypeEnum formType = formVO.getFactoryId()==null?FormTypeEnum.M_V:FormTypeEnum.M_NV;
            //hardcore 之後前台網站 會分開
            String mandt = GlobalConstant.SAP_CLIENT_TCCCN;
            
            Map<String, Object> roleApprovers = null;
            EtMemberForm form = new EtMemberForm();
            String formTypeCode = formType.getCode();
            form.setType(formTypeCode);
            //檢查必填欄位
            if(FormTypeEnum.M_V.equals(formType)){
                if(StringUtils.isBlank(formVO.getApplyVenderCode()) || StringUtils.isBlank(formVO.getApplyVenderName())){
                    errors.add("無供應商資訊");
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, errors);//未輸入必要參數
                }
                
                Long factoryId = null;
                String venderCode = formVO.getApplyVenderCode();
                BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
                criteriaVO.setDisabled(Boolean.FALSE);//排除黑名單
                criteriaVO.setType(mandt);
                criteriaVO.setCode(venderCode);
                List<VenderVO> list = etVenderFacade.findLfa1ByCriteria(criteriaVO);
                CmFactory factory = etVenderCategoryFacade.findApplyFactory(criteriaVO);
                if(factory!=null){
                    factoryId = factory.getId();
                    roleApprovers = reloadApprovers(factory);//size 2
                }
                
                boolean matched = CollectionUtils.isNotEmpty(list) && factoryId!=null && roleApprovers!=null && !roleApprovers.isEmpty();
                logger.info("apply list:{}, factory:{}", list.size(), factoryId);
                if(roleApprovers!=null){
                    logger.info("apply matched:{}, roleApprovers:{}", matched, roleApprovers.size());
                }
                if( !matched ){//無符合的供應商 廠 簽核人員
                    errors.add("無符合的供應商資訊");
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);//資料不存在或已被刪除
                }
                
                form.setFactoryId(factoryId);
                form.setVenderCode(venderCode);
                form.setMandt(mandt);
                form.setApplyVenderCode(venderCode);
                form.setApplyVenderName(formVO.getApplyVenderName());
            }else if(FormTypeEnum.M_NV.equals(formType)){
                if(StringUtils.isBlank(formVO.getApplyVenderName()) || StringUtils.isBlank(formVO.getIdCode())
//                        || formVO.getFactoryId()==null || formVO.getCountry()==null || formVO.getCurrency()==null){
                        || formVO.getFactoryId()==null || StringUtils.isBlank(formVO.getCountryCode()) || StringUtils.isBlank(formVO.getCurrencyCode())){
                    return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, errors);//未輸入必要參數
                }
                //TODO 新商申請是否有其他限制
                
                
                Long factoryId = formVO.getFactoryId();
                CmFactory factory = cmFactoryFacade.find(factoryId);
                roleApprovers = reloadApprovers(factory);
                
                EtCompany company = new EtCompany();
                ExtBeanUtils.copyProperties(company, formVO);
                company.setMainId(member.getId());
                company.setCname(formVO.getApplyVenderName());
                if(CollectionUtils.isNotEmpty(formVO.getIndustryList())){
                    int size = formVO.getIndustryList().size();
                    StringBuilder sb = new StringBuilder();
                    for(String str:formVO.getIndustryList()){
                        sb.append(str).append("、");
                    }
                    sb.deleteCharAt(sb.lastIndexOf("、"));
                    String industry = sb.toString();
                    company.setIndustry(industry);
                }
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                company.setStartAt(df.parse(formVO.getDateStr()));
                
                company = etCompanyFacade.save(company, member, false);
                logger.info("apply... company ID = " + company.getId());
                
                form.setFactoryId(factoryId);
                form.setMandt(mandt);
                form.setApplyVenderName(formVO.getApplyVenderName());
                form.setCompanyId(company.getId());
            }else{
                errors.add("申請供應商綁定 類別有誤");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }
            
            //註冊 以系統管理員執行
            TcUser admin = userFacade.findUserByLoginAccount("administrator", null);
            //form submit
            form.setMemberId(member.getId());
            form.setStatus(FormStatusEnum.SIGNING);
            memberFormFacade.save(form, admin, Boolean.FALSE);
            logger.info("apply... formID = " + form.getId());
            memberFormFacade.startProcess(form, admin, roleApprovers, formTypeCode);    
            
            //寫入ET_VENDER_ALL 供報價挑選申請中的供應商
            EtVenderAll entity = new EtVenderAll();
            entity.setApplyId(form.getId());
            entity.setMandt(form.getMandt());
            entity.setName(form.getApplyVenderName());
            etVenderAllFacade.save(entity, admin, false);
            
//            return this.genSuccessRepsoneWithId(request, member.getId());// 回傳會員ID
            return this.genSuccessRepsoneWithId(request, form.getId());// 回傳申請單ID
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**會員 解除供應商
     * /services/vender/remove
     *
     * @param request
     * @param formVO
     * @return
     */
    @POST
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@Context HttpServletRequest request, SubmitVO formVO) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        EtMember member = null;
        List<String> errors = new ArrayList<>();
        try{
            member = getReqUser(request);
            if(member == null){
                member = memberFacade.find(formVO.getMemberId());
            }
            Locale locale = getLocale(request);
            logInputs(methodName, formVO, member);// log 輸入資訊
            
            if(member == null){
                logger.error("apply member==null");
                errors.add("無登入資訊");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR, errors);
            }else if(formVO.getVenderId()==null){
                errors.add("查無要解除的供應商");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_EMPTY, errors);
            }
            
            EtVender entity =  etVenderFacade.find(formVO.getVenderId());
            if(entity==null){
                errors.add("查無要解除的供應商");
                return this.genFailRepsone(request, ResStatusEnum.IN_ERROR_NOT_EXIST, errors);
            }else if(entity.getMainId().equals(member.getId())){
                etVenderFacade.remove(entity, false);
            }
            return this.genSuccessRepsoneWithId(request, member.getId());// 回傳會員ID
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
    
    /**
     * /services/vender/upload
     * @param request
     * @param imgSrc
     * @param multiPart
     * @return 
     */
    @Path("/upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@Context HttpServletRequest request, final FormDataMultiPart multiPart) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.info("upload ...");
        FileEnum fileEnum = FileEnum.IMP_VENDER_APPLY;
        String root = fileEnum.getRootDir();
        List<String> errors = new ArrayList<>();
        EtMember member = null;
        try{
            member = getReqUser(request);
            Locale locale = getLocale(request);
//            logInputs(methodName, formVO, member);// log 輸入資訊

            // 上傳檔處理
            List<FormDataBodyPart> bodyParts = multiPart.getFields("files");
            if( bodyParts==null || bodyParts.isEmpty() ){
                logger.error("upload error bodyParts==null || bodyParts.isEmpty()");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
//            Long memberId = this.getMultiPartLong(multiPart, "memberId");
            Long formId = this.getMultiPartLong(multiPart, "formId");
            if( formId == null ){
                logger.error("upload formId==null ");
                return this.genFailRepsone(request); 
            }
            
            Long id = this.getMultiPartLong(multiPart, "id");
            String fileName = this.getMultiPartValue(multiPart, "filename");
            logger.info("upload fileName = "+fileName);
            String fileContentType = this.getMultiPartValue(multiPart, "fileContentType");

            byte[] content  = null;
            for (int i = 0; i < bodyParts.size(); i++) {
                //fileName = bodyParts.get(i).getContentDisposition().getFileName();// 中文會亂碼
                BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                fileContentType = (fileContentType==null)?bodyParts.get(i).getContentDisposition().getType():fileContentType;
                //logger.debug("uploadStoreLogo file ContentType = " + fileContentType);
                content = IOUtils.toByteArray(bodyPartEntity.getInputStream());
                logger.debug("upload content size = "+((content!=null)?content.length:0));
                break; // 暫時段落，只支援一個圖檔
            }
            
            if( fileName==null || content==null ){
                logger.error("upload error fileName==null || content==null");
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }
            
            // 儲存實體檔案
            FileVO fileVO = sys.writeRealFile(fileEnum, fileName, content, true, null);
            logger.info("upload write real file finish.");
            
            // Save ET_FILE
            EtFile fileEntity = (id!=null)?fileFacade.find(id):new EtFile();
            if( fileEntity!=null && fileEnum.getPrimaryType().equals(fileEntity.getPrimaryType())
             && fileEntity.getPrimaryId()!=null && !fileEntity.getPrimaryId().equals(formId) ){
                logger.error("upload error : {} : {}", fileEntity.getPrimaryId(), formId);
                return Response.notAcceptable(null).build(); // HTTP STATUS 406
            }else if( fileEntity==null ){
                fileEntity = new EtFile();
            }
            
            fileEntity.setStoreId(null);
            fileEntity.setPrimaryType(fileEnum.getPrimaryType());
            fileEntity.setPrimaryId(formId);// 此處可能為 null 或 0，後續再 update
            fileEntity.setDescription("form doc");
            fileEntity.setContentType(fileContentType);
            
            fileEntity.setFilename(fileVO.getFilename());
            fileEntity.setName(fileVO.getName());
            fileEntity.setSavedir(fileVO.getSavedir());
            fileEntity.setSavename(fileVO.getSavename());
            fileEntity.setFileSize(fileVO.getFileSize());
            fileFacade.save(fileEntity, member, false);
            logger.info("upload save db finish.");
            
            FileVO resVO = fileFacade.findSingleByPrimary(fileEntity.getPrimaryType(), formId);
            return this.genSuccessRepsone(request, resVO);
        }catch(Exception e){
//            sys.processUnknowException(member, methodName, e);
        }
        return this.genFailRepsone(request);
    }
}

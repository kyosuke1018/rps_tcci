/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.service.partner;

import com.tcci.tccstore.entity.EcGoods;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.entity.EcPartnerComment;
import com.tcci.tccstore.service.EntityToModel;
import com.tcci.tccstore.enums.PartnerStatusEnum;
import com.tcci.tccstore.facade.form.EcFormFacade;
import com.tcci.tccstore.facade.goods.EcGoodsFacade;
import com.tcci.tccstore.facade.partner.EcPartnerCommentFacade;
import com.tcci.tccstore.facade.partner.EcPartnerFacade;
import com.tcci.tccstore.model.goods.Goods;
import com.tcci.tccstore.model.partner.Partner;
import com.tcci.tccstore.model.partner.PartnerComment;
import com.tcci.tccstore.model.partner.PartnerQueryResult;
import com.tcci.tccstore.service.MultipartRequestMap;
import com.tcci.tccstore.service.ServiceBase;
import com.tcci.tccstore.service.ServiceException;
import com.tcci.tccstore.util.MailNotify;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Path("partner")
public class PartnerService extends ServiceBase {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(PartnerService.class);

    @Inject
    private EcPartnerFacade partnerFacade;
//    @Inject
//    private EcProductFacade productFacade;
    @Inject
    private EcPartnerCommentFacade partnerCommentFacade;
//    @Inject
//    private EcPartnerProductFacade partnerProductFacade;
    @Inject
    private EcGoodsFacade goodsFacade;
    @Inject
    private EcFormFacade formFacade;

    @Resource(mappedName = "jndi/tccstore.config")
    transient private Properties tccstoreConfig;
    
    /*
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@Context HttpServletRequest request, Partner partner) throws ServiceException {
        ResourceBundle rb = ResourceBundle.getBundle("/msgPartner", request.getLocale());
        partner.setCreatetime(new Date());
        partner.setStatus("APPLY");
        partner.setActive(false);
        logger.debug("partner={}", partner);
        String result = "";
        List<String> errorList = validate(partner, rb);

        if (errorList.isEmpty()) {
            EcPartner ecPartner = convertPartnerToEcPartner(partner, null);
            logger.debug("ecPartner={}", ecPartner);
            ecPartner.setOwner(getAuthMember());
            partnerFacade.createThenReturn(ecPartner);
            result = rb.getString("partner.msg.createSuccess");
        } else {
            for (String errorMessage : errorList) {
                result += errorMessage + "\n";
            }
            throw new ServiceException(result);
        }
        return result;
    }

    @POST
    @Path("edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String edit(@Context HttpServletRequest request, Partner partner) throws ServiceException {
        String result = "";
        ResourceBundle rb = ResourceBundle.getBundle("/msgPartner", request.getLocale());
        logger.debug("partner={}", partner);
        List<String> errorList = validate(partner, rb);
        EcPartner existsEcPartner = partnerFacade.find(partner.getId());
        if (existsEcPartner != null) {
            if (errorList.isEmpty()) {
                //記錄原始的建立時間
                partner.setCreatetime(existsEcPartner.getCreatetime());
                EcPartner ecPartner = convertPartnerToEcPartner(partner, existsEcPartner);
                logger.debug("ecPartner={}", ecPartner);
                ecPartner.setOwner(getAuthMember());
                partnerFacade.editThenReturn(ecPartner);
                result = rb.getString("partner.msg.editSuccess");
            } else {
                for (String errorMessage : errorList) {
                    result += errorMessage + "\n";
                }
                throw new ServiceException(result);
            }
        } else {
            result = rb.getString("partner.err.partnerNotExists");
            throw new ServiceException(result);
        }
        return result;
    }
    
    @POST
    @Path("comment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createComment(@Context HttpServletRequest request, PartnerComment partnerComment) throws ServiceException {
        String result = "";
        logger.debug("partnerComment={}", partnerComment);
        ResourceBundle rb = ResourceBundle.getBundle("/msgPartner", request.getLocale());
        partnerComment.setCreatetime(new Date());
        List<String> errorList = validateComment(partnerComment, rb);
        if (errorList.isEmpty()) {
            EcPartnerComment ecPartnerComment = convertPartnerCommentToEcPartnerComment(partnerComment, null);
            ecPartnerComment.setMember(getAuthMember());
            ecPartnerComment.setActive(true);
            partnerCommentFacade.creatThenReturn(ecPartnerComment);
            result = rb.getString("partner.msg.createCommentSuccess");
        } else {
            for (String errorMessage : errorList) {
                result += errorMessage + "\n";
            }
            throw new ServiceException(result);
        }
        return result;
    }

    @POST
    @Path("comment/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String editComment(@Context HttpServletRequest request, PartnerComment partnerComment) throws ServiceException {
        String result = "";
        ResourceBundle rb = ResourceBundle.getBundle("/msgPartner", request.getLocale());
        EcPartnerComment ecPartnerComment = partnerCommentFacade.find(partnerComment.getId());
        if (ecPartnerComment != null) {
            List<String> errorList = validateComment(partnerComment, rb);
            //記錄原始建立時間
            Date createtime = ecPartnerComment.getCreatetime();
            ecPartnerComment = convertPartnerCommentToEcPartnerComment(partnerComment, ecPartnerComment);
            if (errorList.isEmpty()) {
                ecPartnerComment.setCreatetime(createtime);
                ecPartnerComment.setMember(getAuthMember());
                partnerCommentFacade.editThenReturn(ecPartnerComment);
                result = rb.getString("partner.msg.editCommentSuccess");
            } else {
                for (String errorMessage : errorList) {
                    result += errorMessage + "\n";
                }
                throw new ServiceException(result);
            }
        } else {
            result = rb.getString("partner.err.partnerCommentNotExists");
            throw new ServiceException(result);
        }
        return result;
    }

    @GET
    @Path("comment/remove/{comment_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String remove(@Context HttpServletRequest request, @PathParam("comment_id") Long id) throws ServiceException {
        ResourceBundle rb = ResourceBundle.getBundle("/msgPartner", request.getLocale());
        String result = "";
        EcPartnerComment partnerComment = partnerCommentFacade.find(id);
        if (partnerComment != null) {
            partnerCommentFacade.remove(partnerComment);
            result = rb.getString("partner.msg.removeCommentSuccess");
        } else {
            result = rb.getString("partner.err.partnerCommentNotExists");
            throw new ServiceException(result);
        }
        return result;
    }

    @GET
    @Path("product/{product_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Partner> findPartnerByProduct(@Context HttpServletRequest request, @PathParam(value = "product_id") Long productId, @QueryParam(value = "state") String state) throws ServiceException {
        ResourceBundle rb = ResourceBundle.getBundle("/msgProduct", request.getLocale());
        List<Partner> resultList = new ArrayList();
        EcProduct product = productFacade.find(productId);
        if (product != null) {
            PartnerProductFilter filter = new PartnerProductFilter();
            filter.setProduct(product);
            filter.setPartnerState(state);
            for (EcPartnerProduct partnerProduct : partnerProductFacade.findByCriteria(filter)) {
                EcPartner ecPartner = partnerProduct.getEcPartner();
                Partner partner = EntityToModel.buildPartnerSimple(ecPartner);
                double averageRate = 0;
                if (ecPartner.getEcPartnerCommentList() != null && !ecPartner.getEcPartnerCommentList().isEmpty()) {
                    double totalRate = 0;
                    for (EcPartnerComment comment : ecPartner.getEcPartnerCommentList()) {
                        totalRate += comment.getRate();
                    }
                    averageRate = totalRate / ecPartner.getEcPartnerCommentList().size();
                }
                partner.setAverageRate(averageRate);
                resultList.add(partner);
            }
        } else {
            throw new ServiceException(rb.getString("product.err.productNotExists"));
        }
        return resultList;
    }
    */

    // 檢視
    @GET
    @Path("view/{partner_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Partner findPartnerById(@PathParam(value = "partner_id") Long partner_id) {
        EcPartner ecPartner = partnerFacade.find(partner_id);
        if (null == ecPartner) {
            String error = "夥伴资料不存在!";
            logger.error(error);
            throw new ServiceException(error);
        }
        Partner result = EntityToModel.buildPartner(ecPartner);
        String appUrl = tccstoreConfig.getProperty("appURL");
        String imageUrl = appUrl + "/service/image?oid=partner:" + ecPartner.getId();
        result.setImageUrl(imageUrl);
        result.setGoodsList(convertToGoodsList(goodsFacade.findByPartner(ecPartner)));
        List<PartnerComment> comments = new ArrayList();
        List<EcPartnerComment> partnerCommentList = partnerCommentFacade.findByPartner(ecPartner);
        for (EcPartnerComment entity : partnerCommentList) {
            // rate, message, createtime
            comments.add(EntityToModel.buildPartnerCommentSimple(entity));
        }
        result.setComments(comments);
        return result;
    }

    // 申請
    @GET
    @Path("apply")
    @Produces(MediaType.APPLICATION_JSON)
    public Partner apply() {
        Partner result = new Partner();
        List<EcGoods> allGoods = goodsFacade.findAllActive();
        List<Goods> goods = new ArrayList<>();
        for (EcGoods entity : allGoods) {
            Goods model = EntityToModel.buildGoods(entity);
            model.setSelected(false);
            goods.add(model);
        }
        result.setGoodsList(goods);
        return result;
    }
    
    // 編輯
    @GET
    @Path("edit/{partner_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Partner edit(@PathParam(value = "partner_id") Long partner_id) {
        EcMember loginMember = getAuthMember();
        EcPartner ecPartner = partnerFacade.find(partner_id);
        if (null == ecPartner) {
            String error = "夥伴资料不存在!";
            logger.error(error);
            throw new ServiceException(error);
        }
        if (!ecMemberFacade.isMemberPartnerExist(loginMember, ecPartner)) {
            String error = "无权限编辑夥伴资料!";
            logger.error(error);
            throw new ServiceException(error);
        }
        Partner result = EntityToModel.buildPartner(ecPartner);
        String appUrl = tccstoreConfig.getProperty("appURL");
        String imageUrl = appUrl + "/service/image?oid=partner:" + ecPartner.getId();
        result.setImageUrl(imageUrl);
        List<EcGoods> allGoods = goodsFacade.findAllActive();
        List<EcGoods> partnerGoods = goodsFacade.findByPartner(ecPartner);
        List<Goods> goods = new ArrayList<>();
        for (EcGoods entity : allGoods) {
            Goods model = EntityToModel.buildGoods(entity);
            boolean selected = partnerGoods.contains(entity);
            model.setSelected(selected);
            goods.add(model);
        }
        result.setGoodsList(goods);
        return result;
    }
    
    // 儲存
    @POST
    @Path("save")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String save(@Context HttpServletRequest request) {
        EcMember loginMember = getAuthMember();
        MultipartRequestMap map = new MultipartRequestMap(request);
        Partner partner = fromJson(map.getStringParameter("info"), Partner.class);
        File file = map.getFileParameter("pic");
        ResourceBundle rb = ResourceBundle.getBundle("/msgPartner", request.getLocale());
        List<String> errorList = validate(partner, rb);
        if (!errorList.isEmpty()) {
            String result = "";
            for (String errorMessage : errorList) {
                result += errorMessage + "\n";
            }
            throw new ServiceException(result);
        }
        List<EcGoods> partnerGoods = new ArrayList<>();
        if (partner.getGoodsList() != null) {
            for (Goods g : partner.getGoodsList()) {
                if (!g.isSelected()) {
                    continue;
                }
                EcGoods eg = goodsFacade.find(g.getId());
                if (eg != null) {
                    partnerGoods.add(eg);
                }
            }
        }
        boolean applyNotify = false;
        EcPartner ecPartner;
        if (partner.getId() == null) {
            ecPartner = new EcPartner();
            ecPartner.setActive(false);
            ecPartner.setStatus(PartnerStatusEnum.APPLY);
            ecPartner.setOwner(loginMember);
            ecPartner.setCreatetime(new Date());
            applyNotify = true;
        } else {
            ecPartner = partnerFacade.find(partner.getId());
            if (null == ecPartner) {
                String error = "夥伴资料不存在!";
                logger.error(error);
                throw new ServiceException(error);
            }
            if (!ecMemberFacade.isMemberPartnerExist(loginMember, ecPartner)) {
                String error = "无权限编辑夥伴资料!";
                logger.error(error);
                throw new ServiceException(error);
            }
        }
        
        String[] fields = {"name", "description", "phone", "social", "province", "city", "district", "town", "contact", "address"};
        EntityToModel.copyFields(partner, ecPartner, fields);
        partnerFacade.save(ecPartner);
        goodsFacade.udpatePartnerGoods(ecPartner, partnerGoods);
        if (file != null) {
            try {
                partnerFacade.updateImage(ecPartner, file);
            } catch (Exception ex) {
                logger.error("partnerFacade.updateImage exception", ex);
            }
        }
        logger.warn("partner:{} saved, by member:{}", ecPartner.getId(), loginMember.getId());
        if (applyNotify) {
            // 通知審核人
            String approveLink = tccstoreConfig.getProperty("adminURL") + "/faces/sales/partner.xhtml";
            String contact = formFacade.findContact(ecPartner.getProvince());
            MailNotify.partnerApply(ecPartner, approveLink, contact);
        }
        return "OK";
    }
    
    @POST
    @Path("comment/add")
    @Produces(MediaType.TEXT_PLAIN)
    public String commentAdd(
            @FormParam(PARAM_PARTNER_ID) Long partner_id,
            @FormParam(PARAM_RATE) double rate,
            @FormParam(PARAM_MESSAGE) String message) {
        EcMember loginMember = getAuthMember();
        EcPartner ecPartner = partnerFacade.find(partner_id);
        if (null == ecPartner) {
            String error = "夥伴资料不存在!";
            logger.error(error);
            throw new ServiceException(error);
        }
        if (rate<0 || rate>5) {
            String error = "评分必须介于0~5!";
            logger.error(error);
            throw new ServiceException(error);
        }
        EcPartnerComment partnerComent = partnerCommentFacade.addComment(loginMember, ecPartner, rate, message);
        String approveLink = tccstoreConfig.getProperty("adminURL") + "/faces/sales/partnerComment.xhtml";
        String contact = formFacade.findContact(ecPartner.getProvince());
        MailNotify.partnerCommentApprove(partnerComent, approveLink, contact);
        return "OK";
    }
    
    @GET
    @Path("comment/list/{partner_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartnerComment> findPartnerComment(@PathParam(value = "partner_id") Long partner_id) {
        EcPartner ecPartner = partnerFacade.find(partner_id);
        if (null == ecPartner) {
            String error = "夥伴资料不存在!";
            logger.error(error);
            throw new ServiceException(error);
        }
        List<PartnerComment> commentList = new ArrayList();
        List<EcPartnerComment> partnerCommentList = partnerCommentFacade.findByPartner(ecPartner);
        for (EcPartnerComment entity : partnerCommentList) {
            // rate, message, createtime
            commentList.add(EntityToModel.buildPartnerCommentSimple(entity));
        }
        return commentList;
    }

    @GET
    @Path("mylist")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Partner> mylist() {
        List<EcPartner> ecPartners = partnerFacade.findByMember(getAuthMember());
        List<Partner> result = new ArrayList<>();
        for (EcPartner entity : ecPartners) {
            // id, name, averageRate, active
            Partner vo = EntityToModel.buildPartnerSimple(entity);
            // 補上狀態
            vo.setStatus(entity.getStatus().name());
            result.add(vo);
        }
        return result;
    }
    
    @POST
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    public PartnerQueryResult query(
            @FormParam(PARAM_GOODS_ID) Long goods_id,
            @FormParam(PARAM_PROVINCE) String province,
            @FormParam(PARAM_CITY) String city,
            @FormParam(PARAM_DISTRICT) String district,
            @FormParam(PARAM_DIVISION) String division,
            @FormParam(PARAM_ALLPARTNER) String allpartner) {
        PartnerQueryResult queryResult = new PartnerQueryResult();
        // 查詢大陸行政區(填寫partner地址)
        if (division != null) {
            List<String> result = partnerFacade.queryDivision(province, city);
            queryResult.setResult(result);
            return queryResult;
        }
        EcGoods ecGoods = (allpartner != null || null==goods_id) ? null : goodsFacade.find(goods_id);
        if (province != null && city != null && district != null) {
            List<EcPartner> ecParnters = partnerFacade.query(province, city, district, ecGoods);
            List<Partner> partners = new ArrayList<>();
            String appUrl = tccstoreConfig.getProperty("appURL");
            for (EcPartner entity : ecParnters) {
                // 僅需id, name, imageUrl, averageRatge
                Partner vo = EntityToModel.buildPartnerSimple(entity);
                String imageUrl = appUrl + "/service/image?oid=partner:" + vo.getId();
                vo.setImageUrl(imageUrl);
                partners.add(vo);
            }
            queryResult.setPartners(partners);
        } else {
            List<String> result = partnerFacade.query(province, city, ecGoods);
            queryResult.setResult(result);
        }
        return queryResult;
    }
    
    @GET
    @Path("goods/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Goods> goodsList() {
        return convertToGoodsList(goodsFacade.allPartnerGoods());
    }
    
    @POST
    @Path("goods/buy")
    @Produces(MediaType.APPLICATION_JSON)
    public String goodsBuy(
            @FormParam(PARAM_GOODS_ID) Long goods_id, 
            @FormParam(PARAM_QUANTITY) int quantity,
            @FormParam(PARAM_UOM) String uom) {
        EcMember loginMember = getAuthMember();
        EcGoods ecGoods = goodsFacade.find(goods_id);
        if (null == ecGoods || !ecGoods.isActive()) {
            String error = "商品不存在或已停用!";
            logger.error(error);
            throw new ServiceException(error);
        }
        goodsFacade.goodsBuy(ecGoods, loginMember, quantity, uom);
        return "OK";
    }
    
    /*
    private PartnerComment convertEcPartnerCommentToPartnerComment(EcPartnerComment ecPartnerComment) {
        PartnerComment partnerComment = new PartnerComment();
        partnerComment.setId(ecPartnerComment.getId());
        partnerComment.setRate(ecPartnerComment.getRate());
        partnerComment.setMessage(ecPartnerComment.getMessage());
        partnerComment.setCreatetime(ecPartnerComment.getCreatetime());
        partnerComment.setPartner(convertEcPartnerToPartner(ecPartnerComment.getPartner()));
        partnerComment.setMember(EntityToModel.buildMember(ecPartnerComment.getEcMember()));
        return partnerComment;
    }

    private Partner convertEcPartnerToPartner(EcPartner ecPartner) {
        Partner partner = new Partner();
        partner.setId(ecPartner.getId());
        partner.setName(ecPartner.getName());
        partner.setDescription(ecPartner.getDescription());
        partner.setPhone(ecPartner.getPhone());
        partner.setSocial(ecPartner.getSocial());
        partner.setAddress(ecPartner.getAddress());
        partner.setProvince(ecPartner.getProvince());
        partner.setCity(ecPartner.getCity());
        partner.setDistrict(ecPartner.getDistrict());
        partner.setTown(ecPartner.getTown());
        partner.setContact(ecPartner.getContact());
        partner.setTheme(ecPartner.getTheme());
        partner.setStatus(ecPartner.getStatus().toString());
        partner.setCreatetime(ecPartner.getCreatetime());
        partner.setActive(ecPartner.isActive());
        partner.setMember(ecPartner.getMember());
        partner.setMessage(ecPartner.getMessage());
        List<PartnerProduct> productList = new ArrayList();
        for (EcPartnerProduct ecPartnerProduct : ecPartner.getEcPartnerProductList()) {
            EcProduct ecProduct = ecPartnerProduct.getEcProduct();
            PartnerProduct partnerProduct = new PartnerProduct();
            partnerProduct.setProduct(EntityToModel.buildContractProduct(ecProduct));
            partnerProduct.setUnitPrice(ecPartnerProduct.getUnitPrice());
            productList.add(partnerProduct);
        }
        partner.setPartnerProductList(productList);

        //calculate average rate
        double averageRate = 0;
        if (ecPartner.getEcPartnerCommentList() != null && !ecPartner.getEcPartnerCommentList().isEmpty()) {
            double totalRate = 0;
            for (EcPartnerComment comment : ecPartner.getEcPartnerCommentList()) {
                totalRate += comment.getRate();
            }
            averageRate = totalRate / ecPartner.getEcPartnerCommentList().size();
        }
        partner.setAverageRate(averageRate);

        return partner;
    }

    private EcPartnerComment convertPartnerCommentToEcPartnerComment(PartnerComment partnerComment, EcPartnerComment ecPartnerComment) {
        if (ecPartnerComment == null) {
            ecPartnerComment = new EcPartnerComment();
        }
        ecPartnerComment.setRate(partnerComment.getRate());
        ecPartnerComment.setMessage(partnerComment.getMessage());
        ecPartnerComment.setPartner(partnerFacade.find(partnerComment.getPartner().getId()));
        ecPartnerComment.setCreatetime(partnerComment.getCreatetime());
        return ecPartnerComment;
    }

    private List<String> validateComment(PartnerComment comment, ResourceBundle rb) {
        List<String> errorList = new ArrayList();
        //評分必填
        if (comment.getRate() == 0) {
            errorList.add(rb.getString("partner.err.rateIsRequired"));
        }

        //台泥夥伴必填
        if (comment.getPartner() == null) {
            errorList.add(rb.getString("partner.err.partnerIsRequired"));
        } else {
            EcPartner partner = partnerFacade.find(comment.getPartner().getId());
            if (partner == null) {
                errorList.add(rb.getString("partner.err.commentPartnerNotExists"));
            }
        }

        return errorList;
    }

    private EcPartner convertPartnerToEcPartner(Partner partner, EcPartner ecPartner) {
        if (ecPartner == null) {
            ecPartner = new EcPartner();
        }
        ecPartner.setId(partner.getId());
        ecPartner.setName(partner.getName());
        ecPartner.setDescription(partner.getDescription());
        ecPartner.setPhone(partner.getPhone());
        ecPartner.setSocial(partner.getSocial());
        ecPartner.setAddress(partner.getAddress());
        ecPartner.setProvince(partner.getProvince());
        ecPartner.setCity(partner.getCity());
        ecPartner.setDistrict(partner.getDistrict());
        ecPartner.setTown(partner.getTown());
        ecPartner.setContact(partner.getContact());
        ecPartner.setTheme(partner.getTheme());
        ecPartner.setStatus(PartnerStatusEnum.fromString(partner.getStatus()));
        ecPartner.setCreatetime(partner.getCreatetime());
        ecPartner.setActive(partner.isActive());
        ecPartner.setMember(partner.getMember());
        ecPartner.setMessage(partner.getMessage());
        if (partner.getPartnerProductList() != null) {
            ecPartner.setEcPartnerProductList(new ArrayList());
            for (PartnerProduct partnerProduct : partner.getPartnerProductList()) {
                EcProduct ecProduct = productFacade.find(partnerProduct.getProduct().getId());
                EcPartnerProduct ecPartnerProduct = partnerProductFacade.findByPartnerAndProduct(ecPartner, ecProduct);
                if (ecPartnerProduct == null) {
                    if (ecProduct != null) {
                        ecPartnerProduct = new EcPartnerProduct();
                        ecPartnerProduct.setEcProduct(ecProduct);
                        ecPartnerProduct.setEcPartner(ecPartner);
                        ecPartnerProduct.setUnitPrice(partnerProduct.getUnitPrice());
                    }
                }
                ecPartner.getEcPartnerProductList().add(ecPartnerProduct);
            }
        }

        if (ecPartner.getId() != null) {
            ecPartner.setEcAllPartnerCommentList(partnerCommentFacade.findByPartner(ecPartner));
        }
        return ecPartner;
    }
    */

    private List<String> validate(Partner partner, ResourceBundle rb) {
        List<String> errorList = new ArrayList();

        //夥伴名稱
        if (StringUtils.isEmpty(partner.getName())) {
            errorList.add(rb.getString("partner.err.nameIsRequired"));
        } else {
            //夥伴是否已存在
            EcPartner existsPartner = partnerFacade.findByName(partner.getName());
            logger.debug("existsPartner={}", existsPartner);
            boolean exists = false;
            if (existsPartner != null) {
                logger.debug("partner.getId()={}", partner.getId());
                logger.debug("existsPartner.getId()={}", existsPartner.getId());
                if (partner.getId() == null || (partner.getId() != null && !partner.getId().equals(existsPartner.getId()))) {
                    exists = true;
                }
                if (exists) {
                    errorList.add(rb.getString("partner.err.partnerExists"));
                }
            }
        }

        //狀態
        //if (partner.getStatus() == null) {
        //    errorList.add(rb.getString("partner.err.statusIsRequired"));
        //}

        //夥伴商品
        //if (partner.getPartnerProductList() == null || partner.getPartnerProductList().isEmpty()) {
        //    errorList.add(rb.getString("partner.err.partnerProductIsRequired"));
        //} else {
            //夥伴商品不存在
        //    int index = 1;
        //    for (PartnerProduct partnerProduct : partner.getPartnerProductList()) {
        //        logger.debug("product.getId()={}", partnerProduct.getProduct().getId());
        //        EcProduct ecProduct = productFacade.find(partnerProduct.getProduct().getId());
        //        if (ecProduct == null) {
        //            errorList.add(MessageFormat.format(rb.getString("partner.err.productNotExists"), index));
        //        }
        //        index++;
        //    }
        //}

        //省必填
        if (StringUtils.isEmpty(partner.getProvince())) {
            errorList.add(rb.getString("partner.err.provinceIsRequired"));
        }
        //市必填
        if (StringUtils.isEmpty(partner.getCity())) {
            errorList.add(rb.getString("partner.err.cityIsRequired"));
        }
        //區必填
        if (StringUtils.isEmpty(partner.getDistrict())) {
            errorList.add(rb.getString("partner.err.districtIsRequired"));
        }
        //鎮必填
        if (StringUtils.isEmpty(partner.getDistrict())) {
            errorList.add(rb.getString("partner.err.townIsRequired"));
        }
        //電話必填
        if (StringUtils.isEmpty(partner.getPhone())) {
            errorList.add(rb.getString("partner.err.phoneIsRequired"));
        }
        //連絡人必填
        if (StringUtils.isEmpty(partner.getContact())) {
            errorList.add(rb.getString("partner.err.contactIsRequired"));
        }

        return errorList;
    }

    private List<Goods> convertToGoodsList(List<EcGoods> list) {
        List<Goods> result = new ArrayList<>();
        for (EcGoods entity : list) {
            Goods model = EntityToModel.buildGoods(entity);
            model.setSelected(true);
            result.add(model);
        }
        return result;
    }

}

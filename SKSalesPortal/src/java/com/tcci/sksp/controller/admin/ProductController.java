package com.tcci.sksp.controller.admin;

import com.tcci.fc.controller.util.AttachmentController;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.sksp.entity.ar.SkProductCategory;
import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.facade.SkProductCategoryFacade;
import com.tcci.sksp.facade.SkProductMasterFacade;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@ManagedBean
@ViewScoped
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    private List<SkProductCategory> categoryList;
    private SkProductCategory category;
    private SkProductMaster product;
    private List<SkProductMaster> productList;

    @EJB
    SkProductMasterFacade ejbFacade;
    @EJB
    SkProductCategoryFacade productCategoryFacade;
    @EJB
    ContentFacade contentFacade;

    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;

    public void setAttachmentController(AttachmentController attachmentController) {
        this.attachmentController = attachmentController;
    }

    @PostConstruct
    private void init() {
        this.categoryList = productCategoryFacade.findAll();
        this.productList = new ArrayList();
    }

    public void editPicture() {
        attachmentController.init(this.product, ContentRole.PICTURE);
    }

    public void editLicense() {
        attachmentController.init(this.product, ContentRole.LICENSE);
    }

    public void editCategory() {
        attachmentController.init(this.product, ContentRole.CATEGORY);
    }

    public void editBeLicense() {
        attachmentController.init(this.product, ContentRole.BE);
    }

    public void changeCategory(AjaxBehaviorEvent event) {
        logger.debug("this.category.getCategory()={}", this.category.getCategory());
        String[] categories = new String[]{this.category.getCategory()};
        this.productList = ejbFacade.findByCategories(categories);
        Collections.sort(this.productList, new Comparator<SkProductMaster>() {
            @Override
            public int compare(SkProductMaster p1, SkProductMaster p2) {
                return p1.getName().compareTo(p2.getName());

            }
        });
    }

    public List<AttachmentVO> initAttachmentVOList(ContentHolder contentHolder, String contentRoleString) {
        List<AttachmentVO> attachmentVOList = new ArrayList();
        try {
            ContentRole contentRole = ContentRole.fromCharacter(contentRoleString.charAt(0));
            attachmentVOList = initAttachmentVOList(contentHolder, contentRole);
        } catch (Exception e) {
            logger.error("e={}", e);
        }
        return attachmentVOList;
    }

    private List<AttachmentVO> initAttachmentVOList(ContentHolder contentHolder, ContentRole contentRole) {
        List<AttachmentVO> attachmentVOList = new ArrayList();
        try {
            if (contentHolder != null) {
                for (TcApplicationdata applicationdata : contentFacade.getApplicationdata(contentHolder, contentRole)) {
                    AttachmentVO vo = new AttachmentVO();
                    vo.setApplicationdata(applicationdata);
                    vo.setFileName(applicationdata.getFvitem().getName());
                    vo.setSize(applicationdata.getFvitem().getFilesize());
                    attachmentVOList.add(vo);
                }
            }
        } catch (Exception e) {
            logger.error("e={}", e);
        }
        return attachmentVOList;
    }

    //<editor-fold defaultstate="collapsed" desc="getter,setter">
    public List<SkProductCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<SkProductCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public SkProductCategory getCategory() {
        return category;
    }

    public void setCategory(SkProductCategory category) {
        this.category = category;
    }

    public SkProductMaster getProduct() {
        return product;
    }

    public void setProduct(SkProductMaster product) {
        this.product = product;
    }

    public List<SkProductMaster> getProductList() {
        return productList;
    }

    public void setProductList(List<SkProductMaster> productList) {
        this.productList = productList;
    }
    //</editor-fold>
}

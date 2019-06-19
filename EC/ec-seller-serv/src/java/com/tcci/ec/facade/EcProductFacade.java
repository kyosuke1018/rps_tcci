/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.ImportUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.enums.CompanyTypeEnum;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.ImageSizeEnum;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.enums.ProductStatusEnum;
import com.tcci.ec.enums.ProductVariantEnum;
import com.tcci.ec.model.rs.ImportProductVO;
import com.tcci.ec.model.rs.ImportResultVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.PrdAttrVO;
import com.tcci.ec.model.PrdAttrValVO;
import com.tcci.ec.model.PrdDetailVO;
import com.tcci.ec.model.PrdIntroVO;
import com.tcci.ec.model.PrdPaymentVO;
import com.tcci.ec.model.PrdShippingVO;
import com.tcci.ec.model.PrdTypeTreeNodeVO;
import com.tcci.ec.model.PrdTypeTreeVO;
import com.tcci.ec.model.PrdTypeVO;
import com.tcci.ec.model.PrdVarOptionVO;
import com.tcci.ec.model.PrdVariantVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import com.tcci.ec.model.ProductVO;
import com.tcci.ec.model.TccProductVO;
import com.tcci.ec.model.statistic.StatisticPrdStatusVO;
import com.tcci.ec.model.statistic.StatisticPrdTypeVO;
import com.tcci.ec.model.statistic.StatisticPrdVO;
import com.tcci.ec.util.AnnotationImportUtils;
import com.tcci.fc.util.CollectionUtils;
import com.tcci.fc.util.ResourceBundleUtils;
import com.tcci.fc.util.StringUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcProductFacade extends AbstractFacade<EcProduct> {
    @EJB EcPrdTypeFacade prdTypeFacade;
    @EJB EcPrdAttrFacade prdAttrFacade;
    @EJB EcPrdAttrValFacade prdAttrValFacade;
    @EJB EcPrdIntroFacade prdIntroFacade;    
    @EJB EcPrdVarOptionFacade prdVarOptionFacade;
    @EJB EcPrdVariantFacade prdVariantFacade;
    @EJB EcPrdShippingFacade prdShippingFacade;
    @EJB EcPrdPaymentFacade prdPaymentFacade;
    @EJB EcPrdDetailFacade prdDetailFacade;
    @EJB EcVendorFacade vendorFacade;
    @EJB EcOptionFacade optionFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcProductFacade() {
        super(EcProduct.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcProduct entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getStatus()==null ){ entity.setStatus(ProductStatusEnum.DRAF.getCode()); }
            if( entity.getCurrencyId()==null ){ entity.setCurrencyId(GlobalConstant.DEF_CURRENCY_ID); }
            if( entity.getPriceAmt()==null ){ entity.setPriceAmt(GlobalConstant.DEF_PRD_PRICE_AMT); }
            // 0 to NULL (UI use select input)
            entity.setTypeId((entity.getTypeId()!=null && entity.getTypeId()==0)? null:entity.getTypeId());
            entity.setVendorId((entity.getVendorId()!=null && entity.getVendorId()==0)? null:entity.getVendorId());
            entity.setBrandId((entity.getBrandId()!=null && entity.getBrandId()==0)? null:entity.getBrandId());
            entity.setWeightUnit((entity.getWeightUnit()!=null && entity.getWeightUnit()==0)? null:entity.getWeightUnit());

            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }
    public void saveVO(ProductVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcProduct entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcProduct();
            // 需保存的系統產生欄位
            //vo.setCreator(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            // 複製 UI 輸入欄位
            ExtBeanUtils.copyProperties(entity, vo);
            // DB 儲存
            save(entity, operator, simulated);
            // 回傳 VO 欄位
            vo.setId(entity.getId());
            vo.setCreatorId(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            vo.setModifierId(entity.getModifier()!=null? entity.getModifier().getId():null);
            vo.setModifytime(entity.getModifytime());
        }
    }
    
    /**
     * 商品狀態統計
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticPrdStatusVO> findGroupByStatus(ProductCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.STATUS, COUNT(DISTINCT S.ID) VALUE \n"); 
        sql.append(findByCriteriaSQL(criteriaVO, params));    
        sql.append("GROUP BY S.STATUS");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        List<StatisticPrdStatusVO> list = this.selectBySql(StatisticPrdStatusVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticPrdStatusVO vo : list){
                vo.genLabel(locale);
            }
        }
        return list;
    }
    
    /**
     * 商品狀態統計
     * @param criteriaVO
     * @return 
     */
    public List<StatisticPrdTypeVO> findGroupByType(ProductCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT T.TYPEL1, T.TYPEL1NAME, T.TYPEL2, T.TYPEL2NAME, S.TYPE_ID, T.TYPENAME, COUNT(DISTINCT S.ID) VALUE \n"); 
        sql.append(findByCriteriaSQL(criteriaVO, params));    
        sql.append("GROUP BY S.TYPE_ID, T.TYPENAME, T.TYPEL2, T.TYPEL2NAME, T.TYPEL1, T.TYPEL1NAME");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        List<StatisticPrdTypeVO> list = this.selectBySql(StatisticPrdTypeVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticPrdTypeVO vo : list){
                vo.genLabel();
            }
        }
        return list;
    }

    /**
     * 依條件查詢商品
     * @param criteriaVO
     * @return 
     */
    public int countByCriteria(ProductCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public List<ProductVO> findByCriteria(ProductCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT S.* \n");
        sql.append(", ST.CNAME STORE_NAME \n"); 
        sql.append(", CUR.NAME CUR_NAME, CUR.CODE CURRENCY \n");// 幣別
        sql.append(", T.TYPENAME, T.TYPEL2, T.TYPEL2NAME, T.TYPEL1, T.TYPEL1NAME \n");// 類別
        sql.append(", Y.CNAME VENDOR_NAME \n");// 供應商
        sql.append(", B.CNAME BRAND_NAME \n");// 品牌
        sql.append(", U.CNAME PRICE_UNIT_NAME \n");// 計量單位
        sql.append(", W.CNAME WEIGHT_UNIT_NAME \n");// 重量單位
        sql.append(", NVL(FP.CC, 0) FAV_COUNT \n");// 喜愛商品
        sql.append(", AP.LOGIN_ACCOUNT APPLICANT_ACCOUNT \n");// 申請人
        // 封面圖
        sql.append(", FI.NAME picName, FI.DESCRIPTION picDescription, FI.SAVENAME, FI.CONTENT_TYPE, FI.FILESIZE \n");
        // 庫存
        sql.append(", NVL(S.STOCK_SETTLE, 0)+ST.STOCK STOCK \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CNAME");
        }
        
        List<ProductVO> list = this.selectBySql(ProductVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());

        if( list!=null ){
            for(ProductVO vo : list){
                vo.genStatusName(locale);
                vo.setUrl(vo.genUrl("", GlobalConstant.URL_GET_IMAGE, ImageSizeEnum.SMALL.getCode()));
            }
        }
        return list;
    }
    public String findByCriteriaSQL(ProductCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM EC_PRODUCT S \n");
        sql.append("LEFT OUTER JOIN EC_STORE ST ON ST.ID=S.STORE_ID \n");
        // 幣別
        sql.append("LEFT OUTER JOIN EC_CURRENCY CUR ON CUR.ID=S.CURRENCY_ID \n");
        // 供應商
        sql.append("LEFT OUTER JOIN EC_VENDOR V ON V.ID=S.VENDOR_ID \n"); 
        sql.append("LEFT OUTER JOIN EC_COMPANY Y ON Y.MAIN_ID=V.ID AND Y.TYPE='").append(CompanyTypeEnum.VENDOR.getCode()).append("' \n");
        // 品牌
        sql.append("LEFT OUTER JOIN EC_OPTION B ON B.ID=S.BRAND_ID AND B.TYPE='").append(OptionEnum.PRD_BRAND.getCode()).append("' \n");
        // 計量單位
        sql.append("LEFT OUTER JOIN EC_OPTION U ON U.ID=S.PRICE_UNIT AND U.TYPE='").append(OptionEnum.PRD_UNIT.getCode()).append("' \n");
        // 重量單位
        sql.append("LEFT OUTER JOIN EC_OPTION W ON W.ID=S.WEIGHT_UNIT AND W.TYPE='").append(OptionEnum.WEIGHT_UNIT.getCode()).append("' \n");
        // 申請人
        sql.append("LEFT OUTER JOIN EC_MEMBER AP ON AP.ID=S.APPLICANT \n");
        // 類別
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("     SELECT L3.ID, L3.CNAME TYPENAME \n");
        sql.append("     , L2.ID TYPEL2, L2.CNAME TYPEL2NAME \n");
        sql.append("     , L1.ID TYPEL1, L1.CNAME TYPEL1NAME \n");
        sql.append("     FROM EC_PRD_TYPE L3 \n");
        sql.append("     JOIN EC_PRD_TYPE L2 ON L2.ID=L3.PARENT \n");
        sql.append("     JOIN EC_PRD_TYPE L1 ON L1.ID=L2.PARENT \n");
        sql.append(") T ON T.ID=S.TYPE_ID \n");
        // 喜愛商品
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("     SELECT F.PRODUCT_ID, COUNT(DISTINCT F.MEMBER_ID) CC \n");
        sql.append("     FROM EC_FAVORITE_PRD F \n");
        sql.append("     WHERE 1=1 \n");
        sql.append("     GROUP BY F.PRODUCT_ID \n");
        sql.append(") FP ON FP.PRODUCT_ID=S.ID \n");
        // 封面圖 (不要用 PRIMARY_ID 來 JOIN 因為，TCC匯入商品是共用圖片，存的是 EC_TCC_PRODUCT.ID)
        sql.append("LEFT OUTER JOIN EC_FILE FI ON FI.ID=S.COVER_PIC_ID \n");
        // 庫存
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("     SELECT L.STORE_ID, L.PRD_ID, SUM(L.QUANTITY) STOCK \n");
        sql.append("     FROM EC_STOCK_LOG L \n");
        sql.append("     WHERE L.DISABLED=0 \n");// 有效項目
        sql.append("     AND L.CLOSED=0 \n");// 未結帳項目
        sql.append("     GROUP BY L.STORE_ID, L.PRD_ID \n");
        sql.append(") ST ON ST.STORE_ID=S.STORE_ID AND ST.PRD_ID=S.ID \n");
        
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");

        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getTypeId()!=null ){
            sql.append("AND S.TYPE_ID=#TYPE_ID \n");
            params.put("TYPE_ID", criteriaVO.getTypeId());
        }else if( criteriaVO.getTypeL2()!=null ){
            sql.append("AND T.TYPEL2=#TYPEL2 \n");
            params.put("TYPEL2", criteriaVO.getTypeL2());
        }else if( criteriaVO.getTypeL1()!=null ){
            sql.append("AND T.TYPEL1=#TYPEL1 \n");
            params.put("TYPEL1", criteriaVO.getTypeL1());
        }
        if( !StringUtils.isBlank(criteriaVO.getStatus()) ){
            sql.append("AND S.STATUS=#STATUS \n");
            params.put("STATUS", criteriaVO.getStatus());
        }
        if( !CollectionUtils.isEmpty(criteriaVO.getStatusList()) ){
            sql.append(NativeSQLUtils.getInSQL("S.STATUS", criteriaVO.getStatusList(), params)).append("\n");
        }
        if( !StringUtils.isBlank(criteriaVO.getKeyword()) ){
            String kw = "%" + criteriaVO.getKeyword().trim() + "%";
            sql.append("AND (S.CNAME LIKE #KW OR S.CODE LIKE #KW) \n");
            params.put("KW", kw);
        }
        
        // 銷售區域
        if( criteriaVO.getAreaId()!=null ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT A.STORE_ID FROM EC_STORE_AREA A WHERE A.STORE_ID=S.STORE_ID AND A.AREA_ID=#AREA_ID \n");
            sql.append(") \n");
            params.put("AREA_ID", criteriaVO.getAreaId());
        }else if( criteriaVO.getAreaList()!=null && !criteriaVO.getAreaList().isEmpty() ){
            sql.append("AND EXISTS ( \n");
            sql.append("    SELECT A.STORE_ID FROM EC_STORE_AREA A WHERE A.STORE_ID=S.STORE_ID \n")
               .append(NativeSQLUtils.getInSQL("A.AREA_ID", criteriaVO.getAreaList(), params));
            sql.append(") \n");
        }
        
        return sql.toString();
    }
    
    public ProductVO findById(Long storeId, Long prdId, boolean fullInfo, Locale locale){
        if( storeId==null || prdId==null ){
            logger.error("findById error storeId="+storeId+", prdId="+prdId);
            return null;
        }
        
        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setId(prdId);
        List<ProductVO> list = findByCriteria(criteriaVO, locale);
        
        ProductVO prdVO = (list!=null && !list.isEmpty())? list.get(0):null;
        // 發佈狀態 for 判斷發佈時間
        if( prdVO!=null ){
            prdVO.setStatusOri(prdVO.getStatus());
        }
        if( prdVO==null || !fullInfo ){
            return prdVO;
        }
        
        // 多型別選項
        // 顏色
        List<PrdVarOptionVO> colors = prdVarOptionFacade.findByPrd(storeId, prdId, ProductVariantEnum.COLOR.getCode());
        prdVO.setColorsByVoList(colors);
        // 大小
        List<PrdVarOptionVO> sizes = prdVarOptionFacade.findByPrd(storeId, prdId, ProductVariantEnum.SIZE.getCode());
        prdVO.setSizesByVoList(sizes);
        // 多型別設定
        List<PrdVariantVO> variants = prdVariantFacade.findByPrd(storeId, prdId);
        prdVO.setVariants(variants);
        // 簡介
        List<PrdIntroVO> introductions = prdIntroFacade.findByPrd(storeId, prdId);
        prdVO.setIntroductionsByVO(introductions);

        // 運送方式
        List<PrdShippingVO> shippings = prdShippingFacade.findByPrd(storeId, prdId);
        prdVO.setShippingsByVoList(shippings);
        // 付款方式
        List<PrdPaymentVO> payments = prdPaymentFacade.findByPrd(storeId, prdId);
        prdVO.setPaymentsByVoList(payments);
        
        // 商品詳細介紹
        List<PrdDetailVO> details = prdDetailFacade.findByPrd(storeId, prdId);
        prdVO.setDetails(details);
  
        // 商品類別屬性選項
        List<PrdAttrVO> typeAttrs = prdAttrFacade.findByPrdType(storeId, prdVO.getTypeId());
        prdVO.setTypeAttrs(typeAttrs);
        // 商品屬性設定
        List<PrdAttrValVO> attrVals = prdAttrValFacade.findByPrd(storeId, prdVO.getId());
        prdVO.setAttrs(attrVals);
               
        return prdVO;
    }

    /**
     * 線上商品
     * @param storeId
     * @param locale
     * @return 
     */
    public List<LongOptionVO> findOnlinePrdOps(Long storeId, Locale locale, String opLang) {
        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();       
        List<String> statusList = new ArrayList<String>();
        statusList.add(ProductStatusEnum.PUBLISH.getCode());
        //statusList.add(ProductStatusEnum.OUT_OF_STOCK.getCode());
        criteriaVO.setStatusList(statusList);
        criteriaVO.setStoreId(storeId);
        
        criteriaVO.setOrderBy("S.CNAME");
        List<ProductVO> list = findByCriteria(criteriaVO, locale);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        for(ProductVO vo : list){
            StringBuilder label = new StringBuilder();
            // use cname or ename by opLang (C/E)
            String name = "E".equals(opLang)?(StringUtils.isBlank(vo.getEname())?vo.getCname():vo.getEname()):vo.getCname();
            label.append("[").append(vo.getId()).append("]")
                 .append(name!=null?name:"");
            ops.add(new LongOptionVO(vo.getId(), label.toString()));
        }
        return ops;
    }
    
    /**
     *  匯入
     * @param storeId
     * @param filename
     * @param contentType
     * @param autoAddClass
     * @param locale
     * @param resVO
     * @throws FileNotFoundException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException 
     */
    public void importProducts(Long storeId, String filename, String contentType, boolean autoAddClass, Locale locale, ImportResultVO resVO)
            throws FileNotFoundException, IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        logger.debug("importProducts ...");
        
        resVO.setCanImport(false);
        // 準備依名稱取ID的欄位資料
        // 商品分類
        PrdTypeTreeVO prdTypeTree = prdTypeFacade.findPrdTypeTree(storeId, true, null);
        // 供應商
        Map<String, Long> vendorMap = vendorFacade.findForNameMap(storeId);
        // 品牌
        Map<String, Long> brandMap = optionFacade.findForNameMap(storeId, OptionEnum.PRD_BRAND.getCode());
        // 計價單位
        Map<String, Long> prdUnitMap = optionFacade.findForNameMap(storeId, OptionEnum.PRD_UNIT.getCode());
        
        FileInputStream fileStream = new FileInputStream(filename);
        //InputStream fileStream = file.getInputstream();
        //String contentType = file.getContentType();
        
        int sheetIndex = 0;
        String msg = "";
        if( ImportUtils.checkExcelContentType(contentType) ){
            // Excel 資料轉入 impList
            int res = AnnotationImportUtils.importProducts(fileStream, contentType, sheetIndex, locale, resVO.getResList());
            logger.info("importProducts res="+res+", resList="+resVO.getResList().size());
            if( res>0 ){
                int total = 0, success = 0, fail = 0, noData = 0;
                // 移除最後全空欄位
                removeLastEmptyRows(resVO.getResList());
                // impList 資料檢核
                for(Object obj : resVO.getResList()){
                    boolean hasError = false;
                    ImportProductVO vo = (ImportProductVO)obj;
                    vo.setStatus(ProductStatusEnum.DRAF.getCode());// 匯入預設草稿
                    // 商品分類
                    if( prdTypeTree!=null && prdTypeTree.getNodes()!=null ){
                        for(PrdTypeTreeNodeVO nodeVO : prdTypeTree.getNodes()){// 1
                            PrdTypeVO data = nodeVO.getData();
                            vo.setTypeL1Name(vo.getTypeL1Name()!=null?vo.getTypeL1Name().trim():null);
                            if( data!=null && data.getCname()!=null && data.getCname().equalsIgnoreCase(vo.getTypeL1Name()) ){
                                vo.setTypeL1(data.getId());// found level 1
                                if( nodeVO.getChildren()!=null ){// 2
                                    for(PrdTypeTreeNodeVO nodeVO2 : nodeVO.getChildren()){
                                        PrdTypeVO data2 = nodeVO2.getData();
                                        vo.setTypeL2Name(vo.getTypeL2Name()!=null?vo.getTypeL2Name().trim():null);
                                        if( data2!=null && data2.getCname()!=null && data2.getCname().equalsIgnoreCase(vo.getTypeL2Name()) ){
                                            vo.setTypeL2(data2.getId());// found level 2
                                            if( nodeVO2.getChildren()!=null ){// 3
                                                for(PrdTypeTreeNodeVO nodeVO3 : nodeVO2.getChildren()){
                                                    PrdTypeVO data3 = nodeVO3.getData();
                                                    vo.setTypeName(vo.getTypeName()!=null?vo.getTypeName().trim():null);
                                                    if( data3!=null && data3.getCname()!=null && data3.getCname().equalsIgnoreCase(vo.getTypeName()) ){
                                                        vo.setTypeId(data3.getId());// found level 2
                                                    }
                                                }
                                            }//3
                                        }
                                    }
                                }// 2
                            }    
                        }
                    }//1
                    // 供應商
                    vo.setVendorName(vo.getVendorName()!=null?vo.getVendorName().trim():null);
                    vo.setVendorId(vo.getVendorName()!=null?vendorMap.get(vo.getVendorName().toUpperCase()):null);
                    // 品牌
                    vo.setBrandName(vo.getBrandName()!=null?vo.getBrandName().trim():null);
                    vo.setBrandId(vo.getBrandName()!=null?brandMap.get(vo.getBrandName().toUpperCase()):null);
                    logger.info("brandMap = "+brandMap.size());
                    logger.info("getBrandName = "+vo.getBrandName());
                    logger.info("getBrandId = "+vo.getBrandId());
                    // 計價單位
                    vo.setPriceUnitName(vo.getPriceUnitName()!=null?vo.getPriceUnitName().trim():null);
                    vo.setPriceUnit(vo.getPriceUnitName()!=null?prdUnitMap.get(vo.getPriceUnitName().toUpperCase()):null);
                    logger.info("prdUnitMap = "+prdUnitMap.size());
                    logger.info("getPriceUnitName = "+vo.getPriceUnitName());
                    logger.info("setPriceUnit = "+vo.getPriceUnit());
                    vo.setStoreId(storeId);// 必要，for key 重複判斷
                    checkSingleImportData(vo, autoAddClass, resVO.getResList(), locale);// 單筆匯入檢查
                    
                    if( vo.isHasError() ){
                        vo.genErrMsgs(locale);// 產生錯誤訊息彙總字串 for UI
                        resVO.getErrList().add(vo);
                        fail++;
                    }else if( !vo.isHasData() ){
                        noData++;
                    }else{
                        success++;
                    }
                    total++;
                }
                
                // 共 3 筆資料 (正確 3 筆資料，錯誤 0 筆。)
                StringBuilder sb = new StringBuilder()
                        .append(ResourceBundleUtils.getMessage(locale, "total.row"))
                        .append(" ").append(total).append(" ").append(ResourceBundleUtils.getMessage(locale, "row.data"))
                        .append(" (").append(ResourceBundleUtils.getMessage(locale, "right"))
                        .append(" ").append(success).append(" ").append(ResourceBundleUtils.getMessage(locale, "counts"))
                        .append("，").append(ResourceBundleUtils.getMessage(locale, "error"))
                        .append(" ").append(fail).append(" ").append(ResourceBundleUtils.getMessage(locale, "counts"));
                if( noData>0 ){
                    sb.append("，").append(ResourceBundleUtils.getMessage(locale, "nodata")).append(" ").append(noData).append(" ")
                      .append(ResourceBundleUtils.getMessage(locale, "counts")); 
                }
                sb.append("。)");
                
                msg = sb.toString();
                resVO.setCanImport(success>0 && fail<=0);
            }else{
                // 匯入檔案格式錯誤!  上傳EXCEL不可超過筆資料!
                msg = (res<0)?ResourceBundleUtils.getMessage(locale, "imp.file.err")
                        :ResourceBundleUtils.getMessage(locale, "upload.limit")
                        +GlobalConstant.MAX_IMPORT_NUM
                        +ResourceBundleUtils.getMessage(locale, "row.data");
            }
        }else{
            msg = ResourceBundleUtils.getMessage(locale, "upload.file.err");//+" (contentType="+contentType+")";
            logger.error("importProducts msg = "+ msg);
            logger.error("importProducts contentType = "+ contentType);
        }
        resVO.setResultMsg(msg);
    }
    
    /**
     * 單筆匯入檢查
     * @param vo
     * @param autoAddClass
     * @param impList
     * @param locale
     * @return 
     */
    public boolean checkSingleImportData(ImportProductVO vo, boolean autoAddClass, 
            List<ImportProductVO> impList, Locale locale){
        logger.debug("checkSingleImportData ...");
        
        boolean success = true;
        List<String> errors = new ArrayList<String>();
        ProductVO newVO = new ProductVO();
        ExtBeanUtils.copyProperties(newVO, vo);
        
        if( !checkInput(newVO, null, locale, errors) ){
            vo.getErrList().addAll(errors);
            success = false;
        }
        
        // 匯入時才有的檢查
        // 商品分類
        if( StringUtils.isNotBlank(vo.getTypeL1Name()) && vo.getTypeL1()==null ){
            vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "typeL1.no.exists"));
            success = false;
        }else if( StringUtils.isNotBlank(vo.getTypeL2Name()) && vo.getTypeL2()==null ){
            vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "typeL2.no.exists"));
            success = false;
        }else if( StringUtils.isNotBlank(vo.getTypeName()) && vo.getTypeId()==null ){
            vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "typeL3.no.exists"));
            success = false;
        }
        // 供應商
        if( StringUtils.isNotBlank(vo.getVendorName()) && vo.getVendorId()==null ){
            vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "vendor.no.exists"));
            success = false;
        }
        // 品牌
        if( StringUtils.isNotBlank(vo.getBrandName()) && vo.getBrandId()==null ){
            vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "brand.no.exists"));
            success = false;
        }
        // 計量單位
        if( StringUtils.isNotBlank(vo.getPriceUnitName()) && vo.getPriceUnit()==null ){
            vo.getErrList().add(ResourceBundleUtils.getMessage(locale, "priceunit.no.exists"));
            success = false;
        }
        
        // 同一份 EXCEL 出現相同 [商品中文名稱]，報錯不匯入。
        for(ImportProductVO impVo : impList){
            if( vo.getCname()!=null
                    && vo.getCname().equals(impVo.getCname())
                    && vo.getRowNum()!=impVo.getRowNum() ){
                vo.getErrList()
                  .add(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "same.prd.name"), impVo.getRowNum()));
                success = false;
                break;
            }
        }
        // 同一份 EXCEL 出現相同 [商品編號]，報錯不匯入。
        for(ImportProductVO impVo : impList){
            if( vo.getCode()!=null
                    && vo.getCode().equals(impVo.getCode())
                    && vo.getRowNum()!=impVo.getRowNum() ){
                vo.getErrList()
                  .add(MessageFormat.format(ResourceBundleUtils.getMessage(locale, "same.prd.code"), impVo.getRowNum()));
                success = false;
                break;
            }
        }
        
        vo.setHasError(!success);
        return success;
    }
    
    /**
     * 移除最後全空欄位
     * @param impList
     */
    public void removeLastEmptyRows(List<ImportProductVO> impList){
        if( impList==null || impList.isEmpty() ){
            logger.error("removeLastEmptyRows ... no data !");
            return;
        }
        
        List<Integer> allEmptyRows = new ArrayList<Integer>(); // 所有欄位接空白
        
        for(int i=impList.size()-1; i>=0; i--){// 反向順序刪除
            ImportProductVO rowVO = impList.get(i);
            // DataKey、全部空白 檢查
            if( !rowVO.isHasData() ){
                // 避免多出的空白列 EXCEL 認定非空，但實際沒填入文字的資料列。若欄位資料全空，一律忽略，不視為錯誤。
                allEmptyRows.add(i);
                logger.debug("removeLastEmptyRows allEmptyRows id = {}", i);
            }else{
                break;
            }
        }// end of for
        
        // 移除 EXCEL 認定非空，但實際沒填入文字的資料列
        if( !allEmptyRows.isEmpty() ){
            for(int i=0; i<allEmptyRows.size(); i++){
                Integer id = allEmptyRows.get(i);
                logger.debug("removeLastEmptyRows remove id = {}", id);
                if( id == impList.size()-1 ){
                    impList.remove(id.intValue());
                }else{
                    break; // 只刪在最後的空白列
                }
            }
        }
    }

    /**
     * 自動設定商品封面圖
     * 
     * @param storeId
     * @param prdId
     * @return 
     */
    public int autoSetCoverPicture(Long storeId, Long prdId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        // 原封面已刪除 => 未設定原封面
        sql.append("UPDATE EC_PRODUCT S SET COVER_PIC_ID=NULL \n");
        sql.append("WHERE S.COVER_PIC_ID IS NOT NULL \n"); 
        if( storeId!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", storeId);
        }
        if( prdId!=null ){
            sql.append("AND S.ID=#PRD_ID \n");
            params.put("PRD_ID", prdId);
        }
        sql.append("AND NOT EXISTS ( \n");
        sql.append("      SELECT * FROM EC_FILE F \n");
        sql.append("      WHERE F.PRIMARY_TYPE=#PRIMARY_TYPE AND F.PRIMARY_ID=S.ID \n"); 
        sql.append("      AND F.ID=S.COVER_PIC_ID \n");
        sql.append("); \n");
        
        sql.append("\n");
        
        // 未設定原封面 => 設定最新圖
        sql.append("MERGE INTO EC_PRODUCT S \n");
        sql.append("USING ( \n");
        sql.append("  SELECT P.ID, MAX(F.ID) FID \n");
        sql.append("  FROM EC_PRODUCT P \n");
        sql.append("  JOIN EC_FILE F ON F.PRIMARY_TYPE=#PRIMARY_TYPE AND F.PRIMARY_ID=P.ID \n");
        sql.append("  WHERE P.COVER_PIC_ID IS NULL \n");
        if( storeId!=null ){
            sql.append("  AND P.STORE_ID=#STORE_ID \n");
            //params.put("STORE_ID", storeId);// put before
        }
        if( prdId!=null ){
            sql.append("  AND P.ID=#PRD_ID \n");
            //params.put("PRD_ID", prdId);// put before
        }
        sql.append("  GROUP BY P.ID \n");
        sql.append(") W ON (W.ID=S.ID) \n");
        sql.append("WHEN MATCHED THEN \n");
        sql.append("  UPDATE SET S.COVER_PIC_ID=W.FID; \n");

        sql.append("END; \n");
        
        params.put("PRIMARY_TYPE", FileEnum.PRD_PIC.getPrimaryType());
        
        logger.debug("autoSetCoverPicture sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("autoSetCoverPicture", params, q);
        
        return q.executeUpdate();
    }    

    /**
     * 輸入檢查
     * @param vo
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(ProductVO vo, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, locale, errors);

        return pass;
    }
    
    //<editor-fold defaultstate="collapsed" desc="for Statistic">
    /**
     * 商品庫存量統計
     * @param criteriaVO
     * @param locale
     * @return 
     */
    public List<StatisticPrdVO> findGroupByInventory(ProductCriteriaVO criteriaVO, Locale locale){
        logger.debug("findGroupByPrdSales ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT P.ID, P.CNAME, P.ENAME, SUM(L.QUANTITY) VALUE \n"); 
        sql.append("FROM EC_PRODUCT P \n"); 
        sql.append("JOIN EC_STOCK_LOG L ON L.DISABLED=0 AND L.CLOSED=0 AND L.PRD_ID=P.ID \n"); 
        sql.append("WHERE 1=1 \n"); 
        sql.append("AND P.DISABLED=0 \n"); 
        sql.append("AND P.STATUS=#STATUS1 \n"); 
        sql.append("AND P.STORE_ID=#STORE_ID \n"); 
        sql.append("AND to_char(L.DATA_TIME,'yyyymm')<=to_char(#END_AT,'yyyymm') \n"); 
        sql.append("GROUP BY P.ID, P.CNAME, P.ENAME \n"); 
        sql.append("ORDER BY VALUE DESC \n"); 
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        params.put("STATUS1", ProductStatusEnum.PUBLISH.getCode());
        params.put("END_AT", criteriaVO.getEndAt());
        
        List<StatisticPrdVO> list = this.selectBySql(StatisticPrdVO.class, sql.toString(), params);
        
        if( !CollectionUtils.isEmpty(list) ){
            for(StatisticPrdVO vo : list){
                vo.genLabel(locale);
            }
        }
        
        return list;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for TCC Product">
    /**
     * 已匯入過的 TCC 商品
     * @param storeId
     * @param vo
     * @return 
     */
    public boolean existsTccProduct(Long storeId, TccProductVO vo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) \n");
        sql.append("FROM EC_PRODUCT S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        sql.append("AND S.TCC_PRD=1 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        sql.append("AND S.CODE=#CODE \n");
        
        params.put("STORE_ID", storeId);
        params.put("CODE", vo.getCode());
            
        return this.count(sql.toString(), params)>0;
    }

    /**
     * 建立 TCC 商品
     * @param storeId
     * @param vo
     * @param operator 
     */
    public void createTccProduct(Long storeId, TccProductVO vo, EcMember operator) {
        // EcProduct
        EcProduct entity = new EcProduct();
        entity.setTccPrd(Boolean.TRUE);
        entity.setDisabled(Boolean.FALSE);
        entity.setCname(vo.getName());
        entity.setCode(vo.getCode());
        entity.setCompareAtPrice(vo.getPrice());
        entity.setCurrencyId(GlobalConstant.DEF_CURRENCY_ID);
        entity.setPrice(vo.getPrice());
        entity.setPriceAmt(BigDecimal.ONE);
        entity.setPublishTime(new Date());
        entity.setStatus(ProductStatusEnum.PUBLISH.getCode());
        entity.setStoreId(storeId);
        
        entity.setCoverPicId(vo.getCoverPicId());
        entity.setTypeId(vo.getTypeId());
        entity.setBrandId(vo.getBrandId());
        entity.setPriceUnit(vo.getPriceUnit());
        entity.setVendorId(vo.getVendorId());

        this.save(entity, operator, false);
    }
    
    /**
     * 依訂單抓取商品
     * @param storeId
     * @param orderId
     * @return 
     */
    public ProductVO findProductByOrderEC10(Long storeId, Long orderId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT P.* \n");
        sql.append("FROM EC_ORDER S \n");
        sql.append("JOIN EC_ORDER_DETAIL D ON D.ORDER_ID=S.ID \n");
        sql.append("JOIN EC_PRODUCT P ON P.ID=D.PRODUCT_ID AND P.TCC_PRD=1 \n");
        sql.append("WHERE S.ID=#ORDER_ID \n");
        
        params.put("ORDER_ID", orderId);
        
        if( storeId!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", storeId);
        }
        List<ProductVO> list = this.selectBySql(ProductVO.class, sql.toString(), params);
        
        if( list!=null ){
            if( list.size()>1 ){
                logger.error("findProductByOrderEC10 error list.size > 1 : "+list.size());
            }else{
                return list.get(0);
            }
        }else{
            logger.error("findProductByOrderEC10 error list==null!");
        }
        return null;
    }
    //</editor-fold>
}

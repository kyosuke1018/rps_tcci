/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmFunction;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.model.admin.MenuFunctionVO;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcUser;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.menu.DefaultMenuColumn;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.Submenu;

/**
 *
 * @author Peter
 */
@Stateless
public class CmFunctionFacade extends AbstractFacade<CmFunction> {
    public final String NO_URL = "#";
    public final String DEF_MENU_ICON = "ui-icon-gear";
    public final String TOP_MENU_CLASS = "topmenu";
    
    public final String[] MENU_ICONS = {"ui-icon-suitcase", "ui-icon-wrench", "ui-icon-cart", "ui-icon-tag", "ui-icon-folder-open"};
    // 使用手冊 ...
    public final String[] MENU_MANUAL_CODES = {};//{"Z11","Z12","Z13","Z14","Z15","Z16"};
    //在docpub的名稱
    public final String[] MENU_MANUAL_URLS = {};//{"使用手冊-合約資訊查詢與維護","使用手冊-採購單待轉合約清單","使用手冊-合約請(付)款作業","使用手冊-B11.租賃契約申請","使用手冊-B12.租賃契約查詢","使用手冊-C11.合約通知查詢與維護"};
    // 教育訓練文件 ...
    public final String[] MENU_PPT_CODES = {};//{"Z21","Z22","Z23","Z24"};
    //在docpub的名稱
    public final String[] MENU_PPT_URLS = {};//{"油價降幅預警系統教育訓練","合約管理系統教育訓練", "租賃契約管理系統教育訓練", "採購合約到期通知系統教育訓練"};
    
// 權限申請表 ...
    public final String[] MENU_APPLYF_CODES = {};//{"Z31","Z32","Z33"};
    //在docpub的名稱
    public final String[] MENU_APPLYF_URLS = {};//{"合約管理系統權限申請表", "租賃合約管理權限申請表","採購合約到期通知權限申請表"};    
    
    @EJB SysResourcesFacade sysResourcesFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmFunctionFacade() {
        super(CmFunction.class);
    }
    
    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     */
    public void save(CmFunction entity, TcUser operator){
        if( entity!=null ){
            entity.setCreator(operator.getId());
            entity.setCreatetimestamp(new Date());
            
            if( entity.getId()!=null && entity.getId()>0 ){
                this.edit(entity);
            }else{
                this.create(entity);
            }
        }
    }
    
    /**
     * 依 RuleCode 取資料
     * @param ruleCode
     * @return 
     */
    public CmFunction findByRuleCode(String ruleCode){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", ruleCode);
        List<CmFunction> list = findByNamedQuery("CmFunction.findByRuleCode", params);
        if( list!=null && !list.isEmpty() ){
            return list.get(0);
        }else{
            return null;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="for Menu">
    /**
     * 建立功能選單
     * @param functions
     * @param model
     * @param urlPrefix
     * @param megamenu
     * @param fromInternet
     * @param administrator
     * @return 
     */
    public boolean createMenu(List<MenuFunctionVO> functions, MenuModel model, String urlPrefix, 
            boolean megamenu, boolean fromInternet, boolean administrator) {
        logger.debug("createMenu ...");
        Locale locale = JsfUtils.getRequestLocale();
        if( Locale.CHINA.equals(locale) || Locale.SIMPLIFIED_CHINESE.equals(locale) ){
            logger.debug("is CN ...");
        }else{
            logger.debug("is TW ...");
        }

        // 無權限仍可看到 Z.說明
        //if( functions==null ){
        //    return false;
        //}
        // 階層資料
        List<Long> mainList = new ArrayList<Long>();
        Map<Long, Long> subMainMap = new LinkedHashMap<Long, Long>();
        Map<Long, Long> funcSubMap = new LinkedHashMap<Long, Long>();
        Map<Long, String> mainIconMap = new HashMap<Long, String>();
        // function id and icon Map
        mainIconMap.put(new Long(1), DEF_MENU_ICON); // 1 系統管理功能
        mainIconMap.put(new Long(20), MENU_ICONS[0]); // 20 業務
        mainIconMap.put(new Long(13), MENU_ICONS[1]); // 13 工務
        mainIconMap.put(new Long(7), MENU_ICONS[2]); // 7 物料
        mainIconMap.put(new Long(14), MENU_ICONS[3]); // 14 財務
        mainIconMap.put(new Long(54), MENU_ICONS[4]); // 54 內容管理功能
        
        if( functions!=null ){
            String homePage = JsfUtils.getContextPath();//GlobalConstant.HOME_PAGE;
            // 回首頁
            MenuItem homeItem = JsfUtils.newMenuItem("home", JsfUtils.getResourceTxt("label.gohome"), homePage, "ui-icon-home", null, null);
            model.addElement(homeItem);
        
            // 有權限功能
            for(MenuFunctionVO menuFunctionVO : functions){
                if( menuFunctionVO.getUrl()!=null && !menuFunctionVO.getUrl().isEmpty() && !menuFunctionVO.getUrl().equals(NO_URL) ){
                    if( !mainList.contains(menuFunctionVO.getMid()) ) {
                        mainList.add(menuFunctionVO.getMid());
                    }
                    subMainMap.put(menuFunctionVO.getSid(), menuFunctionVO.getMid());
                    funcSubMap.put(menuFunctionVO.getId(), menuFunctionVO.getSid());
                }
            }
            
            for(long mid : mainList){
                String icon = (mainIconMap.get(mid)!=null)? mainIconMap.get(mid):DEF_MENU_ICON;
                Submenu topMenu = JsfUtils.newSubmenu("menu"+mid, showTitle(mid, functions, locale), icon, TOP_MENU_CLASS);

                if( megamenu ){
                    DefaultMenuColumn column = buildMenuColumn(mid, functions, subMainMap, funcSubMap, null, true, urlPrefix, locale);
                    ((DefaultSubMenu)topMenu).addElement(column);
                    model.addElement(topMenu);
                }else{
                    for(Long sid : subMainMap.keySet()){
                        if( subMainMap.get(sid) == mid ){// 子選單
                            if( showMenu(sid, functions) ){// 參考權限    
                                Submenu subMenu = buildSubMenu(sid, functions, funcSubMap, true, urlPrefix, locale);
                                ((DefaultSubMenu)topMenu).addElement(subMenu);
                            }
                        }
                    }
                    model.addElement(topMenu);
                }
            }
        }
            
        // administrator 專屬功能
        if( administrator ){
            //Z 說明
            Submenu topMenu = JsfUtils.newSubmenu("X", "X. Administrator Only", "ui-icon-star", "topmenu");// 說明
            Submenu subMenu11 = JsfUtils.newSubmenu("X1", "X1. Administrator Only", null, null);// 使用手冊
            String urlTxt = JsfUtils.getContextPath()+"/faces/ec-seller.xhtml";
            MenuItem item = JsfUtils.newMenuItem("X11", " ec-seller 管理員後台", urlTxt, "ui-icon-link", "NEW_WIN", null);
            if( megamenu ){
                DefaultMenuColumn column1 = new DefaultMenuColumn();
                ((DefaultSubMenu)subMenu11).addElement(item);
                column1.addElement(subMenu11);
                ((DefaultSubMenu)topMenu).addElement(column1);
                model.addElement(topMenu);
            }else{
                //((DefaultSubMenu)subMenu11).addElement(item);
                //((DefaultSubMenu)topMenu).addElement(subMenu11);
                //model.addElement(topMenu);
                model.addElement(item);
            }
        }
        
        if( !fromInternet ){// 文管系統不開放Internet
            //Z 說明
            Submenu topMenu = JsfUtils.newSubmenu("Z", "Z."+JsfUtils.getResourceTxt("label.description"), "ui-icon-help", "topmenu");// 說明
            if( megamenu ){
                DefaultMenuColumn column1 = new DefaultMenuColumn();
                //Z1使用手冊
                Submenu subMenu11 = JsfUtils.newSubmenu("Z1", "Z1."+JsfUtils.getResourceTxt("menu.manual.Z1"), null, null);// 使用手冊
                // URL 不可用 Resource Bundle
                for(int i=0; i<MENU_MANUAL_CODES.length && i<MENU_MANUAL_URLS.length; i++){
                    // subMenu11.getChildren().add(genMenuItemForManual(MENU_MANUAL_CODES[i], MENU_MANUAL_URLS[i], true));
                    ((DefaultSubMenu)subMenu11).addElement(genMenuItemForManual(MENU_MANUAL_CODES[i], MENU_MANUAL_URLS[i], true));
                }
                column1.addElement(subMenu11);

                //Z2教育訓練文件
                Submenu subMenu12 = JsfUtils.newSubmenu("Z2", "Z2."+JsfUtils.getResourceTxt("menu.manual.Z2"), null, null);// 教育訓練文件
                // URL 不可用 Resource Bundle
                for(int i=0; i<MENU_PPT_CODES.length && i<MENU_PPT_URLS.length; i++){
                    ((DefaultSubMenu)subMenu12).addElement(genMenuItemForManual(MENU_PPT_CODES[i], MENU_PPT_URLS[i], true));
                }
                column1.addElement(subMenu12);

                //Z3權限申請表
                Submenu subMenu13 = JsfUtils.newSubmenu("Z3", "Z3."+JsfUtils.getResourceTxt("menu.manual.Z3"), null, null);// 權限申請表
                // URL 不可用 Resource Bundle
                for(int i=0; i<MENU_APPLYF_CODES.length && i<MENU_APPLYF_URLS.length; i++){
                    ((DefaultSubMenu)subMenu13).addElement(genMenuItemForManual(MENU_APPLYF_CODES[i], MENU_APPLYF_URLS[i], true));
                }
                column1.addElement(subMenu13);            

                ((DefaultSubMenu)topMenu).addElement(column1);
            }else{
                //Z1使用手冊
                Submenu subMenu11 = JsfUtils.newSubmenu("Z1", "Z1."+JsfUtils.getResourceTxt("menu.manual.Z1"), null, null);// 使用手冊
                // URL 不可用 Resource Bundle
                for(int i=0; i<MENU_MANUAL_CODES.length && i<MENU_MANUAL_URLS.length; i++){
                    // subMenu11.getChildren().add(genMenuItemForManual(MENU_MANUAL_CODES[i], MENU_MANUAL_URLS[i], true));
                    ((DefaultSubMenu)subMenu11).addElement(genMenuItemForManual(MENU_MANUAL_CODES[i], MENU_MANUAL_URLS[i], true));
                }
                ((DefaultSubMenu)topMenu).addElement(subMenu11);
            
                //Z2教育訓練文件
                Submenu subMenu12 = JsfUtils.newSubmenu("Z2", "Z2."+JsfUtils.getResourceTxt("menu.manual.Z2"), null, null);// 教育訓練文件
                // URL 不可用 Resource Bundle
                for(int i=0; i<MENU_PPT_CODES.length && i<MENU_PPT_URLS.length; i++){
                    ((DefaultSubMenu)subMenu12).addElement(genMenuItemForManual(MENU_PPT_CODES[i], MENU_PPT_URLS[i], true));
                }
                ((DefaultSubMenu)topMenu).addElement(subMenu12);
  
                //Z3權限申請表
                Submenu subMenu13 = JsfUtils.newSubmenu("Z3", "Z3."+JsfUtils.getResourceTxt("menu.manual.Z3"), null, null);// 權限申請表
                // URL 不可用 Resource Bundle
                for(int i=0; i<MENU_APPLYF_CODES.length && i<MENU_APPLYF_URLS.length; i++){
                    ((DefaultSubMenu)subMenu13).addElement(genMenuItemForManual(MENU_APPLYF_CODES[i], MENU_APPLYF_URLS[i], true));
                }
                ((DefaultSubMenu)topMenu).addElement(subMenu13);
            }
            model.addElement(topMenu);
        }
        
        return true;
    }
    
    /**
     * 建立 文件 選單項目
     * @param code
     * @param urlTxt
     * @param newWin
     * @return 
     */
    public MenuItem genMenuItemForManual(String code, String urlTxt, boolean newWin){
        String label = code+"."+JsfUtils.getResourceTxtWithDef("menu.manual."+code, urlTxt);
        try {
            urlTxt = fetchDocpubUrl(URLEncoder.encode(urlTxt, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            logger.error("genMenuItemForManual exception code="+code+": \n", ex);
        }
        
        return JsfUtils.newMenuItem(code, label,
                                    urlTxt, // URL 不可用 Resource Bundle
                                    null, (newWin)?"DOC_WIN":null, null);
    }
    
    /**
     * 顯示標題
     * @param func_id
     * @param functions
     * @param locale
     * @return 
     */
    public String showTitle(long func_id, List<MenuFunctionVO> functions, Locale locale){
        if( functions!=null ){
            boolean isCN = Locale.CHINA.equals(locale) || Locale.SIMPLIFIED_CHINESE.equals(locale);

            for(MenuFunctionVO ppFunctionVO:functions){
                if( ppFunctionVO.getId() == func_id ){
                    return ((ppFunctionVO.getCode()!=null)?ppFunctionVO.getCode():"")+(isCN?ppFunctionVO.getTitleCN():ppFunctionVO.getTitle());
                }else if( ppFunctionVO.getSid() == func_id ){
                    return (isCN?ppFunctionVO.getStitleCN():ppFunctionVO.getStitle());
                }else if( ppFunctionVO.getMid() == func_id ){
                    return (isCN?ppFunctionVO.getMtitleCN():ppFunctionVO.getMtitle());
                }
            }
        }
        return "無標題"; 
    }
    
    /**
     * 功能對應內控代號
     * @param func_id
     * @param functions
     * @return
     */
    public String getMapRuleCode(long func_id, List<MenuFunctionVO> functions){
        if( functions!=null ){
            for(MenuFunctionVO ppFunctionVO:functions){
                if( ppFunctionVO.getId() == func_id ){
                    return ppFunctionVO.getCode();
                }
            }
        }
        return ""; 
    }
    
    /**
     * 取得功能對應 CommonReport 代碼
     * @param func_id
     * @param functions
     * @return 
     */
    public String getReportCode(long func_id, List<MenuFunctionVO> functions){
        if( functions!=null ){
            for(MenuFunctionVO ppFunctionVO:functions){
                if( ppFunctionVO.getId() == func_id ){
                    return ppFunctionVO.getRptCode();
                }
            }
        }
        return ""; 
    }
    
    /**
     * Build Menu Column
     * @param mid
     * @param subMainMap
     * @param funcSubMap
     * @param filterIds
     * @param checkPermission
     * @return 
     */
    private DefaultMenuColumn buildMenuColumn(long mid, List<MenuFunctionVO> functions,
            Map<Long, Long> subMainMap, Map<Long, Long> funcSubMap, List<Long> filterIds, 
            boolean checkPermission, String urlPrefix, Locale locale){
        if( subMainMap ==null || funcSubMap ==null ){
            return null;
        }
        DefaultMenuColumn column = new DefaultMenuColumn();
        ///DefaultSubMenu subMenuCol = new DefaultSubMenu();
        
        for(Long sid : subMainMap.keySet()){
            if( subMainMap.get(sid) == mid ){// 子選單
                if( !checkPermission || showMenu(sid, functions) ){// 參考權限
                    if( filterIds==null || filterIds.contains(sid) ){// 篩選
                        Submenu subMenu = buildSubMenu(sid, functions, funcSubMap, checkPermission, urlPrefix, locale);
                        //column.getChildren().add(subMenu);
                        column.addElement(subMenu);
                        ///subMenuCol.addElement(subMenu);
                    }
                }
            }
        }
        
        return column;
    }
    
    /**
     * Build Sub Menu
     * @param sid
     * @param funcSubMap
     * @param checkPermission
     * @return 
     */
    private Submenu buildSubMenu(long sid, List<MenuFunctionVO> functions, 
            Map<Long, Long> funcSubMap, boolean checkPermission, String urlPrefix, Locale locale){
        if( funcSubMap ==null ){
            return null;
        }
        Submenu subMenu = JsfUtils.newSubmenu("menu"+sid, showTitle(sid, functions, locale), null, null);

        for(Long fid : funcSubMap.keySet()){
            if( funcSubMap.get(fid) == sid ){// 子功能
                if( !checkPermission || showMenu(fid, functions) ){// 參考權限
                    MenuItem item = JsfUtils.newMenuItem("menu"+fid, showTitle(fid, functions, locale), fetchFunctionUrl(fid, functions, urlPrefix));
                    ((DefaultSubMenu)subMenu).addElement(item);
                }
            }
        }
        
        return subMenu;
    }
    
    /**
     * 取得功能網址
     * @param func_id
     * @param functions
     * @return 
     */
    public String fetchFunctionUrl(long func_id, List<MenuFunctionVO> functions, String urlPrefix){
        MenuFunctionVO menuFunctionVO = fetchFunctionById(func_id, functions);
        if( menuFunctionVO!=null ){
            return ((urlPrefix==null)?"":urlPrefix)+menuFunctionVO.getUrl();
        }
        
        return "#"; 
    }
    
    /**
     * 取得公用文件 docpub URL
     *
     * @param subPath
     * @return
     */
    public String fetchDocpubUrl(String subPath) {
        //20140103 改指向docpub
        try {
            String url = sysResourcesFacade.getDocPubUrl(); //http://docpub.taiwancement.com/docpub/faces/manual/ 
            url = (url.endsWith("/")) ? url : url + "/";

            return url + GlobalConstant.DOCPUB_PATH + URLEncoder.encode(GlobalConstant.DOC_PATH_PREFIX, "UTF-8") + subPath;
        } catch (UnsupportedEncodingException e) {
            logger.error("getManualUrl Exception : no MANUAL_URL in jndi/global.conf \n", e);
        }

        return subPath;
    }
    
    /**
     * 是否顯示選單
     * @param func_id
     * @param functions
     * @return 
     */
    public boolean showMenu(long func_id, List<MenuFunctionVO> functions){
        if( functions!=null ){
            for(MenuFunctionVO ppFunctionVO:functions){
                if( ppFunctionVO.getId() == func_id
                    || ppFunctionVO.getSid() == func_id
                    || ppFunctionVO.getMid() == func_id ){
                    return true;
                }
            }
        }
        
        return false; 
    }    
    
    /**
     * 區出特定功能資訊
     * @param func_id
     * @param functions
     * @return 
     */
    public MenuFunctionVO fetchFunctionById(long func_id, List<MenuFunctionVO> functions){
        if( functions!=null ){
            for(MenuFunctionVO ppFunctionVO:functions){
                if( ppFunctionVO.getId() == func_id ){
                    return ppFunctionVO;
                }
            }
        }
        
        return null; 
    }
    //</editor-fold>    
}

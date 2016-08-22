/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.rs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.sksp.controller.util.ConstantsUtil;
import com.tcci.sksp.entity.org.SkSalesChannels;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkSalesChannelsFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import com.tcci.sksp.vo.SalesMemberVO;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@Path("sales")
@Stateless
public class SalesREST {

    Logger logger = LoggerFactory.getLogger(SalesREST.class);
    @EJB
    TcUserFacade userFacade;
    @EJB
    SkSalesChannelsFacade channelsFacade;
    @EJB
    SkSalesMemberFacade memberFacade;

    @GET
    @Path("findsalesmember")
    @Produces("text/plain; charset=UTF-8;")
    public String findSalesMember(@Context HttpServletRequest request,
            @QueryParam("include_blank") String jsonIncludeBlank,
            @QueryParam("disable_user_only") String jsonDisableUserOnly) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Principal principal = request.getUserPrincipal();
        logger.debug("principal={}", principal);
        logger.debug("userFacade={}", userFacade);
        if (principal == null) {
            return "NO_PERMISSION";
        }
        TcUser user = userFacade.findUserByLoginAccount(principal.getName());

        boolean includeBlank = false;
        if (StringUtils.isNotEmpty(jsonIncludeBlank)) {
            gson.fromJson(jsonIncludeBlank, Boolean.class);
        }
        boolean includeDisableUserOnly = false;
        if (StringUtils.isNotEmpty(jsonDisableUserOnly)) {
            gson.fromJson(jsonDisableUserOnly, Boolean.class);
        }
        List<SkSalesMember> salesList = initSalesList(request, includeBlank, includeDisableUserOnly, user);
        //將SkSalesMember轉成SaalesMemberVO
        List<SalesMemberVO> result = new ArrayList();
        for (SkSalesMember salesMember : salesList) {
            SalesMemberVO vo = new SalesMemberVO();
            vo.setSalesMember(salesMember);
            String area = salesMember.getCode().substring(0, 2);
            vo.setAreaCode(area);
            SkSalesChannels salesChannels = channelsFacade.findByCode(area);
            String areaName = "";
            if (null != salesChannels) {
                areaName = salesChannels.getName();
            }
            vo.setAreaName(areaName);
            result.add(vo);
        }
        return gson.toJson(result);
    }

    private List<SkSalesMember> initSalesList(HttpServletRequest request, boolean includeBlank, boolean disableUserOnly, TcUser user) {

        List<SkSalesMember> salesMembers = new ArrayList<SkSalesMember>();
        if (includeBlank) {
            salesMembers.add(new SkSalesMember());
        }
        if (request.isUserInRole(ConstantsUtil.ROLE_SALES)) {
            if (isSalesManager(user)) {
                logger.debug("manager");
                salesMembers = memberFacade.findByChannels(user, disableUserOnly);
            } else {
                logger.debug("sales");
                salesMembers = memberFacade.findByChannels(user, false);
            }
        } else {
            salesMembers = memberFacade.findAllSelectable();
        }
        return salesMembers;
    }

    private boolean isSalesManager(TcUser user) {
        boolean isManager = false;
        SkSalesChannels channel = channelsFacade.findByManager(user);
        if (channel != null) {
            isManager = true;
        }
        return isManager;
    }
}

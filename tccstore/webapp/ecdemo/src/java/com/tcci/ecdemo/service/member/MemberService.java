/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.service.member;

import com.tcci.ecdemo.entity.EcMember;
import com.tcci.ecdemo.facade.member.EcMemberFacade;
import com.tcci.ecdemo.model.member.Member;
import com.tcci.ecdemo.service.AuthType;
import com.tcci.ecdemo.service.HMACAuth;
import com.tcci.ecdemo.service.HMACServiceBase;
import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jimmy.Lee
 */
@Path("member")
public class MemberService extends HMACServiceBase {

    @EJB
    private EcMemberFacade ecMemberFacade;
    
    @HMACAuth(authType = AuthType.OPTION)
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Member login(@FormParam("username") String username, @FormParam("password") String password) {
        // TODO: SSO login
        EcMember ecMember = ecMemberFacade.findActiveByLoginAccount(username);
        if (null == ecMember || !"abcd1234".equals(password)) {
            throw new WebApplicationException("login failed!", Response.Status.UNAUTHORIZED);
        }
        return new Member(ecMember.getLoginAccount(), ecMember.getName(), ecMember.getPhone());
    }

    @GET
    @Path("reload")
    @Produces(MediaType.APPLICATION_JSON)
    public Member reload() {
        EcMember authMember = this.getAuthMember();
        return new Member(authMember.getLoginAccount(), authMember.getName(), authMember.getEmail());
    }
}

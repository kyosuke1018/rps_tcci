/*
 * Copyright (c) 2017 Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.tcci.security;

import java.util.Set;
import javax.security.enterprise.credential.Credential;

public class DatabaseCredential implements Credential {
    // 驗證需要欄位
    private final String caller;
    private final String password;
    private final boolean encrypted;
    private final String from;// client IP
    // 驗證成功額外回傳資訊
    private Set<String> authorities = null;
    private Long memberId = null;
//    private Long storeId = null;
    private boolean adminUser;
//    private boolean tccDealer;
//    private boolean storeOwner;
//    private boolean fiUser;

    // for name/password login
    public DatabaseCredential(String caller, String password, boolean encrypted, String from){
       this.caller = caller;
       this.password = password;
       this.encrypted = encrypted;
       this.from = from;
    }

    // for extract from JWT
    public DatabaseCredential(String caller, Set<String> authorities, Long memberId,
            boolean adminUser, String from){
       this.caller = caller;
       this.authorities = authorities;
       this.memberId = memberId;
       this.adminUser = adminUser;
       this.password = null;
       this.encrypted = false;
       this.from = from;
    }

    public String getFrom() {
        return from;
    }
    
    public String getCaller() {
        return caller;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public boolean isAdminUser() {
        return adminUser;
    }

    public void setAdminUser(boolean adminUser) {
        this.adminUser = adminUser;
    }

}

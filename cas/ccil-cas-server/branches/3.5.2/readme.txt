installation guide.
1.download cas-server.3.5.2.zip from Jasig - CAS web site.
   http://downloads.jasig.org/cas/cas-server-3.5.2-release.zip
2.unzip to cas-server.3.5.2.zip to cas-server.3.5.2 folder.
3.copy cas-server-webapp sub folder from unzip folder.
4.open project by netbeans.
   4.1.rename project name from cas-server-webapp to cas-server.
5.configure Dependancies
     5.1.cas-server-support-ladp
          Group Id: ${project.groupId}
          Artifact Id: cas-server-support-ldap
          Version: ${project.version}
          Type: jar
     5.2.cas-server-integration-restlet
          Group Id: ${project.groupId}
          Artifact Id: cas-server-integration-restlet
          Version: ${project.version}
          Type: jar
     5.3.hibernate-entitymanager
          Group Id: org.hibernate
          Artifact Id: hibernate-entitymanager
     5.4.ojdbc14
         Group Id: com.oracle
         Artifact Id: ojdbc14
         Version: 10.2.0.4.0
         Type: pom
     5.5.commons-dbcp (不能直接輸入以下資訊, 需用搜尋, 並選擇 version[jar] - local
         Group Id: commons-dbcp
         Artifact Id: commons.dbcp
         Version: 1.4
         Type: jar
6.customize login, logout, login success page.
     6.1.WEB-INF/view/jsp/default/ui/casLoginView.jsp
     6.2.WEB-INF/view/jsp/default/ui/casLogoutView.jsp
     6.3.WEB-INF/view/jsp/default/ui/casGenericSuccess.jsp
     6.4.css/eagle.css
     6.5.js/eagle.js, js/jquery-1.8.0.min.js, js/jquery.ba-throttle-debounce.min.js
     6.6.images/eagle/*.png
     6.7.message_en.properties
     6.8.message_zh_TW.properties
     6.9.message_zh_CN.properties
7.configure authenticationHandlers.
     deployerConfigContext.xml:
     7.1.remove SimpleTestUsernamePasswordAuthenticationHandler.
          <bean class="org.jasig.cas.authentication.handler.support.SimpleTestUsernamePasswordAuthenticationHandler" />
     7.2.add BindLdapAuthenticationHandler.
                <bean class="org.jasig.cas.adaptors.ldap.BindLdapAuthenticationHandler">
                    <property name="filter" value="sAMAccountName=%u" />
                    <property name="searchBase" value="DC=Taiwancement,DC=com" />
                    <property name="ignorePartialResultException" value="true" />
                    <property name="contextSource" ref="contextSource" />
                </bean>
     7.3 add contextSource after authenticationManager bean.
    <bean id="contextSource" class="org.springframework.ldap.core.support.LdapContextSource">
        <property name="anonymousReadOnly" value="false" />
        <property name="pooled" value="false" />
        <!--property name="url" value="ldap://192.168.15.3:3268/" /-->
               
        <property name="urls">
            <list>
                <value>ldap://192.168.15.3:389/</value>
                <value>ldap://192.168.15.4:389/</value>
                <value>ldap://192.168.15.75:389/</value>
                <value>ldap://192.168.15.76:389/</value>
            </list>
        </property>
               
        <property name="userDn" value="tccicommon@taiwancement.com" />
        <property name="password" value="Tcci@7000" />               
        <property name="baseEnvironmentProperties">
            <map>
                <entry key="com.sun.jndi.ldap.connect.timeout" value="3000" />
                <entry key="com.sun.jndi.ldap.read.timeout" value="3000" />
                            <!--
                    <entry>
                            <key><value>java.naming.security.protocol</value></key>
                            <value>ssl</value>
                       </entry>
                                -->
                <entry>
                    <key>
                        <value>java.naming.security.authentication</value>
                    </key>
                    <value>simple</value>
                </entry>
            </map>
        </property>
    </bean>
8.configure web service validation.
     web.xml:
     8.1.add RestletFrameworkServlet.
<servlet>
    <servlet-name>restlet</servlet-name>
    <servlet-class>com.noelios.restlet.ext.spring.RestletFrameworkServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>restlet</servlet-name>
    <url-pattern>/v1/*</url-pattern>
</servlet-mapping>
9.configure cookie lifetime.
     ticketGrantingTicketCookieGenerator.xml:
     9.1 change properties of ticketGrantingTicketCookieGenerator
     p:cookieSecure="false"
     p:cookieMaxAge="86400"
10.configure persistence.
     ticketRegistry.xml
     10.1.remove DefaultTicketRegistry.
     <bean id="ticketRegistry" class="org.jasig.cas.ticket.registry." />

     10.2.add JpaTicketRegistry.
     <bean id="ticketRegistry" class="org.jasig.cas.ticket.registry.JpaTicketRegistry" />
     <bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor"/>
     10.2.1.add entityManagerFactory.
<bean id="entityManagerFactory"
class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
<property name="jpaVendorAdapter">
<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
<property name="generateDdl" value="true"/>
<property name="showSql" value="false" />
</bean>
</property>
<property name="jpaProperties">
<props>
<!-- Use MySQLDialect at your own risk. See MySQL section below for details. -->
<prop key="hibernate.dialect">org.hibernate.dialect.OracleDialect</prop>
<!--<prop key="hibernate.hbm2ddl.auto">create</prop>-->
<prop key="hibernate.hbm2ddl.auto">update</prop>
</props>
</property>
</bean>

     10.2.3.add transactionManager.

<bean id="transactionManager"

class="org.springframework.orm.jpa.JpaTransactionManager"

p:entityManagerFactory-ref="entityManagerFactory" />

<tx:annotation-driven transaction-manager="transactionManager" />

11.change warName from cas to cas-server.
12.Clean & Build project.
13.configure jdbc connection by glassfish connection pool.
     13.1.copy persistence.xml to 
     13.2.add proeprty of entityManagerFactory bean.
     <property name="persistenceXmlLocation" value="classpath:/META-INF/persistence.xml" />
14.delete all Test Packages.
15.clean & build project.

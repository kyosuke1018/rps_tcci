1.configure Dependancies
	1.1.pac4j-cas
		Group Id: org.pac4j
		Artifact Id: pac4j-cas
		Version: ${pac4j.version}
	1.2.pac4j-oauth
		Group Id: org.pac4j
		Artifact Id: pac4j-oauth
		Version: ${pac4j.version}
	1.3.cas-server-support-pac4j
		Group Id: ${project.groupId}
		Artifact Id: cas-server-support-pac4j
		Version: ${project.version}
		
2.register oauth service from provider (e.g. Facebook, Github, Weibo, QQ) to get key & secret (for next step).
	Weibo: http://open.weibo.com/apps
	QQ: http://connect.qq.com/manage/
	
3.add clients (applicationContext.xml).
	3.0.if client not exists, create it follow create-client.txt to create it.
	3.1.declare every oauth client:
		<bean id="weibo" class="org.pac4j.oauth.client.WeiboClient">
			<property name="key" value="${oauth.weibo.key}" />
			<property name="secret" value="${oauth.weibo.secret}" />
		</bean>        
		
		<bean id="qq" class="org.pac4j.oauth.client.QqClient">
			<property name="key" value="${oauth.qq.key}" />
			<property name="secret" value="${oauth.qq.secret}" />
		</bean>   
	3.2 add clients bean and specific callbackUrl and oauth client above declare.
	    <bean id="clients" class="org.pac4j.core.client.Clients">
			<property name="callbackUrl" value="${oauth.callbackUrl}" />
			<property name="clients">
				<list>
					<ref bean="weibo" />
					<ref bean="qq"/>
				</list>
			</property>
		</bean>
		
4.Add client action in webflow
	4.1.add clientAction after on-start tag. (login-webflow.xml)
		<action-state id="clientAction">
			<evaluate expression="clientAction" />
			<transition on="success" to="sendTicketGrantingTicket" />
			<transition on="error" to="ticketGrantingTicketCheck" />
			<transition on="stop" to="stopWebflow" />
		</action-state>
		<view-state id="stopWebflow" />
	4.2.declare ClientAction (cas-servlet.xml)
		<bean id="clientAction" class="org.jasig.cas.support.pac4j.web.flow.ClientAction"
		c:theCentralAuthenticationService-ref="centralAuthenticationService"
		c:theClients-ref="clients"/>

5.Add the handler and the metadata populator (optional) for authentication
	5.1.declare oauth authentication handler (deployerConfigContext.xml)
	    <bean id="secondaryAuthenticationHandler" class="org.jasig.cas.support.pac4j.authentication.handler.support.ClientAuthenticationHandler">
			<constructor-arg index="0" ref="clients"/>
		</bean>    
	5.2.add above handler into authenticationManager (deployerConfigContext.xml)
		<entry key-ref="secondaryAuthenticationHandler">
			<null />
		</entry>
		
6.Add link in login page.
	6.1.add screen.ecsso.otherLogin, screen.ecsso.button.weiboLogin and screen.ecsso.button.qqLogin property by locale into messages*.properties, 
	    for example: screen.ecsso.otherLogin=其他账号登录:, screen.ecsso.button.weiboLogin=微博登录, screen.ecsso.button.qqLogin=QQ登录 for messages_zh_CN.properties.
	6.2.download login icon (16x16) from provider to /src/main/webapp/themes/theme1/img/ folder.
		Weibo: weibo.png: http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9A%E6%A0%87%E8%AF%86%E4%B8%8B%E8%BD%BD?sudaref=www.google.com.tw
		QQ: qq.png: http://wiki.open.qq.com/wiki/website/%E7%BD%91%E7%AB%99%E5%89%8D%E7%AB%AF%E9%A1%B5%E9%9D%A2%E8%A7%84%E8%8C%83#1._.E4.BD.BF.E7.94.A8.E8.85.BE.E8.AE.AF.E6.8F.90.E4.BE.9B.E7.9A.84.E6.A0.87.E5.87.86.E2.80.9CQQ.E7.99.BB.E5.BD.95.E2.80.9D.E6.A0.87.E8.AF.86
	6.3.add link in login page both normal & mobile version (casLoginView.jsp).
		<spring:message code="screen.ecsso.otherLogin"/>
		<a href="${WeiboClientUrl}" title="<spring:message code="screen.ecsso.button.weiboLogin" />" style="color: blue;"><img src="<c:url value="/themes/theme1/img/weibo.png"/>"/></a>
		<a href="${QqClientUrl}" title="<spring:message code="screen.ecsso.button.qqLogin" />" style="color: blue;"><img src="<c:url value="/themes/theme1/img/qq.png"/>"/></a>

7.configure return user profile to client applications.
	7.1.add attribute for org.jasig.cas.services.RegexRegisteredService inside serviceRegistryDao.
                    <property name="attributeReleasePolicy"
                              ref="attributeReleasePolicy" />
	
	7.2.add attributeReleasePolicy bean.
	    <bean id="attributeReleasePolicy" class="org.jasig.cas.services.ReturnAllAttributeReleasePolicy"/>
8.remove all check rules in checkstyle-rules.xml & checkstyle-suppressions.xml		
	
	
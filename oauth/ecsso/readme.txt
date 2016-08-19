installation guide.
1.download cas-server.4.1.5.zip from Jasig - CAS web site.
   https://github.com/Jasig/cas/releases/tag/v4.1.5
   
2.unzip to cas-server.4.1.5.zip to ecsso folder.

3.remove other modules except cas-server-webapp module in ecsso folder.

4.rename folder naem from cas-server-webapp to ecsso.

5.open project by netbeans.
   4.1.rename project name from cas-server-webapp to ecsso.
   
6.configure Dependancies
     6.1.cas-server-support-jdbc
          Group Id: ${project.groupId}
          Artifact Id: cas-server-support-jdbc
          Version: ${project.version}
          Type: jar
     6.2.cas-server-integration-restlet
          Group Id: ${project.groupId}
          Artifact Id: cas-server-integration-restlet
          Version: ${project.version}
          Type: jar
     6.3.ojdbc14
         Group Id: com.oracle
         Artifact Id: ojdbc14
         Version: 10.2.0.4.0
         Type: pom
		 
7.customize login, logout, login success page.
     7.1.copy exists theme1 (under src/main/webapp/WEB-INF/view/jsp folder) folder from previous version of ecsso 
	     to src/main/webapp/WEB-INF/view/jsp folder.
     7.2.copy exists theme1 (under src/main/webapp/themes folder) folder from previous version of ecsso to src/main/webapp/themes folder.
     7.3.add cas.viewResolver.basename=theme1_views after cas.themeResolver.defaultThemeName property in /Web Pages/WEB-INF/cas.properties 
     7.4.change basename of viewResolver from cas_views to ${cas.viewResolver.basename} in /Web Pages/WEB-INF/cas-servlet.xml
	 7.5.copy exists theme1_view.properties (under src/main/resources folder) from previous version of ecsso to /src/main/resources folder.
	 7.7.add two properties screen.ecsso.title & screen.ecsso.link.forgotPassword by locale into messages*.properties, 
	     for example: add screen.ecsso.title=CRM-台泥电子商务 & screen.ecsso.link.forgotPassword=忘记密码? to mesasges_zn_CN.properties.
		 
8.configure authenticationHandlers.
     deployerConfigContext.xml:
     8.1.change bean class to org.jasig.cas.adaptors.jdbc.QueryDatabaseAuthenticationHandler 
	     and p:dataSource-ref, p:passwordEncoder-ref and p:sql for primaryPrincipalResolver for primaryPrincipalResolver.
		 <bean id="primaryAuthenticationHandler"
			   class="org.jasig.cas.adaptors.jdbc.QueryDatabaseAuthenticationHandler"
			  p:dataSource-ref="jndiDataSource"
			  p:passwordEncoder-ref="passwordEncoder"
			  p:sql="select password from ec_member where lower(login_account)=lower(?) and active=1"
		 />
     8.2.add passwordEncoder bean.
		 <bean id="passwordEncoder" class="org.jasig.cas.authentication.handler.DefaultPasswordEncoder" p:characterEncoding="UTF-8" >
			<constructor-arg index="0" value="SHA-256" />
		 </bean>
	 8.3.add jndiDataSource bean.
		 <bean id="jndiDataSource"
		 	   class="org.springframework.jndi.JndiObjectFactoryBean">
			<property name="jndiName" value="jdbc/tccstore"/>
			<property name="lookupOnStartup" value="false"/>
			<property name="proxyInterface" value="javax.sql.DataSource"/>
		 </bean>
	 8.4.replace serviceRegistryDao by org.jasig.cas.services.InMemoryServiceRegistryDaoImpl.
		 <bean
			id="serviceRegistryDao"
			class="org.jasig.cas.services.InMemoryServiceRegistryDaoImpl">
			<property name="registeredServices">
				<list>
					<bean class="org.jasig.cas.services.RegexRegisteredService">
						<property name="id" value="0" />
						<property name="name" value="HTTP and IMAP" />
						<property name="description" value="Allows HTTP(S) and IMAP(S) protocols" />
						<property name="serviceId" value="^(https?|imaps?)://.*" />
						<property name="evaluationOrder" value="10000001" />
					</bean>
					<!--
					Use the following definition instead of the above to further restrict access
					to services within your domain (including subdomains).
					Note that example.com must be replaced with the domain you wish to permit.
					-->
					<!--
					<bean class="org.jasig.cas.services.RegexRegisteredService">
						<property name="id" value="1" />
						<property name="name" value="HTTP and IMAP on example.com" />
						<property name="description" value="Allows HTTP(S) and IMAP(S) protocols on example.com" />
						<property name="serviceId" value="^(https?|imaps?)://([A-Za-z0-9_-]+\.)*example\.com/.*" />
						<property name="evaluationOrder" value="0" />
					</bean>
					-->
				</list>
			</property>
		</bean>
		
8.configure web service validation.
	8.1 add runtime dependency cas-server-support-rest.
		Group Id: ${project.groupId}
		Artifact Id: cas-server-support-rest
        Version: 4.1.7-SNAPSHOT
		Scope: runtime
9.configure cookie lifetime.
     ticketGrantingTicketCookieGenerator.xml:
     9.1.change properties of ticketGrantingTicketCookieGenerator
		<bean id="ticketGrantingTicketCookieGenerator" class="org.jasig.cas.web.support.CookieRetrievingCookieGenerator"
			  c:casCookieValueManager-ref="cookieValueManager"
			  p:cookieSecure="false"
			  p:cookieMaxAge="43200"
			  p:cookieName="CASTGC"
			  p:cookiePath="/ecsso/"/>
     9.2.removed cookieCipherExecutor.
	 9.3.replace cookieValueManager by org.jasig.cas.web.support.NoOpCookieValueManager
	     and remove c:cipherExecutor-ref property.
		<bean id="cookieValueManager" class="org.jasig.cas.web.support.NoOpCookieValueManager"/>
		
10.configure ticketRegistry.
	 ticketRegistry.xml
     10.1.remove DefaultTicketRegistry.
		<bean id="ticketRegistry" class="org.jasig.cas.ticket.registry.DefaultTicketRegistry" />
     10.2.add JpaTicketRegistry.
		 <bean id="ticketRegistry" class="org.jasig.cas.ticket.registry.JpaTicketRegistry" />
		 <bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor"/>
     10.3.add entityManagerFactory.
		 <bean id="entityManagerFactory"
			  class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean"
			  p:dataSource-ref="jndiDataSource2"
			  p:jpaVendorAdapter-ref="jpaVendorAdapter"
			  p:packagesToScan-ref="packagesToScan">
			<property name="jpaProperties">
				<props>
					<prop key="hibernate.dialect">${database.dialect:org.hibernate.dialect.Oracle10gDialect}</prop>
					<prop key="hibernate.hbm2ddl.auto">update</prop>
					<prop key="hibernate.jdbc.batch_size">${database.batchSize:1}</prop>
				</props>
			</property>
		 </bean>
		 
 		 <bean id="jndiDataSource2"
			  class="org.springframework.jndi.JndiObjectFactoryBean">
			<property name="jndiName" value="jdbc/ecsso"/>
			<property name="lookupOnStartup" value="false"/>
			<property name="proxyInterface" value="javax.sql.DataSource"/>
		 </bean>  		
		 
		 <util:list id="packagesToScan">
			<value>org.jasig.cas.ticket</value>
			<value>org.jasig.cas.adaptors.jdbc</value>
		 </util:list>    
		 
 		 <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"
			  id="jpaVendorAdapter"
			  p:generateDdl="true"
			  p:showSql="true" />  
			  
		 <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager"
			  p:entityManagerFactory-ref="entityManagerFactory" />
			  
		<tx:advice id="txCentralAuthenticationServiceAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<tx:method name="destroyTicketGrantingTicket" read-only="false" />
				<tx:method name="grantServiceTicket" read-only="false" />
				<tx:method name="delegateTicketGrantingTicket" read-only="false" />
				<tx:method name="validateServiceTicket" read-only="false" />
				<tx:method name="createTicketGrantingTicket" read-only="false" />

				<tx:method name="getTicket" read-only="false" />
				<tx:method name="getTickets" read-only="true" />
			</tx:attributes>
		</tx:advice>

		<tx:advice id="txRegistryAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<tx:method name="deleteTicket" read-only="false" />
				<tx:method name="addTicket" read-only="false" />
				<tx:method name="updateTicket" read-only="false" />
				<tx:method name="getTicket" read-only="true" />
				<tx:method name="getTickets" read-only="true" />
				<tx:method name="sessionCount" read-only="true" />
				<tx:method name="serviceTicketCount" read-only="true" />
			</tx:attributes>
		</tx:advice>

		<tx:advice id="txRegistryServiceTicketDelegatorAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<tx:method name="grantTicketGrantingTicket" read-only="false" />
			</tx:attributes>
		</tx:advice>

		<tx:advice id="txRegistryTicketGrantingTicketDelegatorAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<tx:method name="markTicketExpired" read-only="false" />
				<tx:method name="grantServiceTicket" read-only="false" />
			</tx:attributes>
		</tx:advice>

		<tx:advice id="txRegistryLockingAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<tx:method name="getOwner" read-only="true" />
				<tx:method name="acquire" read-only="false" />
				<tx:method name="release" read-only="false" />
			</tx:attributes>
		</tx:advice>

		<aop:config>
			<aop:pointcut id="ticketRegistryOperations" expression="execution(* org.jasig.cas.ticket.registry.JpaTicketRegistry.*(..))"/>
			<aop:pointcut id="ticketRegistryLockingOperations" expression="execution(* org.jasig.cas.ticket.registry.support.JpaLockingStrategy.*(..))"/>
			<aop:pointcut id="ticketRegistryServiceTicketDelegatorOperations" expression="execution(* org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry$ServiceTicketDelegator.*(..))"/>
			<aop:pointcut id="ticketRegistryTicketGrantingTicketDelegatorOperations" expression="execution(* org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry$TicketGrantingTicketDelegator.*(..))"/>
			<aop:pointcut id="casOperations" expression="execution(* org.jasig.cas.CentralAuthenticationServiceImpl.*(..))"/>

			<aop:advisor advice-ref="txRegistryAdvice" pointcut-ref="ticketRegistryOperations"/>
			<aop:advisor advice-ref="txRegistryLockingAdvice" pointcut-ref="ticketRegistryLockingOperations"/>
			<aop:advisor advice-ref="txRegistryTicketGrantingTicketDelegatorAdvice" pointcut-ref="ticketRegistryTicketGrantingTicketDelegatorOperations"/>
			<aop:advisor advice-ref="txRegistryServiceTicketDelegatorAdvice" pointcut-ref="ticketRegistryServiceTicketDelegatorOperations"/>
			<aop:advisor advice-ref="txCentralAuthenticationServiceAdvice" pointcut-ref="casOperations"/>
		</aop:config>
	 10.4.setup ticketRegistryCleaner.
		10.4.1.add p:lock-ref="cleanerLock" of ticketRegistryCleaner.
		10.4.2.add cleanerLock bean.
			<bean id="cleanerLock" class="org.jasig.cas.ticket.registry.support.JpaLockingStrategy"
				  p:uniqueId="${host.name}"
				  p:applicationId="cas-ticket-registry-cleaner" />
		
	ticketExpirationPolicies.xml
	 10.5.change c:numberOfUses to 2 and c:timeToKill to ${st.timeToKillInSeconds:300} of serviceTicketExpirationPolicy bean.
		 <bean id="serviceTicketExpirationPolicy" class="org.jasig.cas.ticket.support.MultiTimeUseOrTimeoutExpirationPolicy"
			   c:numberOfUses="2" c:timeToKill="${st.timeToKillInSeconds:300}" c:timeUnit-ref="SECONDS"/>
	 10.6.change class of grantingTicketExpirationPolicy bean to org.jasig.cas.ticket.support.RememberMeDelegatingExpirationPolicy
	      and add two properties (sessionExpirationPolicy and rememberMeExpirationPolicy) for it.
	     <bean id="grantingTicketExpirationPolicy" class="org.jasig.cas.ticket.support.RememberMeDelegatingExpirationPolicy">
			<property name="sessionExpirationPolicy">
				<bean class="org.jasig.cas.ticket.support.HardTimeoutExpirationPolicy">
					<constructor-arg index="0" value="32400000" />
				</bean>
			</property>
			<property name="rememberMeExpirationPolicy">
				<bean class="org.jasig.cas.ticket.support.HardTimeoutExpirationPolicy">
					<constructor-arg index="0" value="1296000000" />
				</bean>
			</property>
		 </bean>
11.remove p:defaultLocale="en" from localeResolver bean in cas-servlet.xml
12.change warName from cas to ecsso (pom.xml).
13.config log file location (log4j2.xml)
	change fileName attribute for all RollingFile element to ../logs/*.log
14.Clean & Build project.
15.configure jdbc connection by glassfish connection pool.
	 13.1 copy glassfish-resources*.xml from previous version of ecsso to /setup folder.
16.delete all Test Packages.
17.clean & build project.   
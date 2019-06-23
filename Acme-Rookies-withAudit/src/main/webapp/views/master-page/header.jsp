<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<div style="float: right">
	<a href="?language=es" id="es"><img src="images/sp.png"
		style="height: 25px; width: 50px;" /></a> <a href="?language=en" id="en"><img
		src="images/uk.png" style="height: 25px; width: 50px;" /></a>
</div>

<div>
	<a href="#"><img src="${banner}" height="180" alt="${fn:escapeXml(sysName)}" /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/show.do"><spring:message code="master.page.administrator.show" /></a></li>
					<li><a href="dashboard/administrator/show.do"><spring:message code="master.page.dashboard" /></a></li>
					<li><a href="administrator/create.do"><spring:message code="master.page.administrator.register" /></a></li>
					<li><a href="auditor/create.do"><spring:message code="master.page.auditor.register" /></a></li>
					<li><a href="configuration/administrator/edit.do"><spring:message code="master.page.administrator.configuration" /></a></li>
					<jstl:if test="${!isRebranded }">
						<li><a href="message/administrator/rebrand.do"><spring:message code="master.page.messages.rebrand" /></a></li>
					</jstl:if>
					<li><a href="dashboard/administrator/auditorScore.do"><spring:message code="master.page.messages.recalculateAudit" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('PROVIDER')">
			<li><a class="fNiv"><spring:message	code="master.page.provider" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="provider/show.do"><spring:message code="master.page.provider.show" /></a></li>
					<li><a href="item/provider/list.do"><spring:message code="master.page.my.items" /></a></li>
					<li><a href="sponsorship/provider/list.do"><spring:message code="master.page.my.sponsorships" /></a></li>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="hasRole('ROOKIE')">
			<li><a class="fNiv"><spring:message	code="master.page.rookie" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="rookie/show.do"><spring:message code="master.page.rookie.show" /></a></li>
					<li><a href="application/rookie/list.do"><spring:message code="master.page.my.application.list" /></a></li>
					<li><a href="finder/rookie/edit.do"><spring:message code="master.page.finder" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('COMPANY')">
			<li><a class="fNiv"><spring:message	code="master.page.company" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="company/show.do"><spring:message code="master.page.company.show" /></a></li>
					<li><a href="position/company/list.do"><spring:message code="master.page.my.positions" /></a></li>
					<li><a href="nyordel/company/list.do"><spring:message code="nyordel.list" /></a></li>
					<li><a href="problem/company/list.do"><spring:message code="master.page.problem" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('AUDITOR')">
			<li><a class="fNiv"><spring:message	code="master.page.auditor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="auditor/show.do"><spring:message code="master.page.auditor.show" /></a></li>
					<li><a href="audit/auditor/list.do"><spring:message code="master.page.audit.list" /></a></li>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="permitAll()">
			<li><a class="fNiv" href="position/list.do"><spring:message code="master.page.all.positions" /></a></li>
			<li><a class="fNiv" href="company/list.do"><spring:message code="master.page.all.companies" /></a></li>
			<li><a class="fNiv" href="provider/list.do"><spring:message code="master.page.all.providers" /></a></li>
			<li><a class="fNiv" href="item/list.do"><spring:message code="master.page.all.items" /></a></li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"><spring:message code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="company/create.do"><spring:message code="master.page.company.register" /></a></li>
					<li><a href="rookie/create.do"><spring:message code="master.page.rookie.register" /></a></li>
					<li><a href="provider/create.do"><spring:message code="master.page.provider.register" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.messaging" />
				</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="message/list.do"><spring:message code="master.page.boxes" /></a></li>
					<li><a href="message/send.do"><spring:message code="master.page.messages.send" /></a></li>
					<security:authorize access="hasRole('ADMINISTRATOR')">
						<li><a href="message/administrator/broadcast.do"><spring:message code="master.page.messages.broadcast" /></a></li>
						<li><a href="message/administrator/listSpammers.do"><spring:message code="master.page.messages.listSpammers" /></a></li>
						<li><a href="message/administrator/recalculateSpammer.do"><spring:message code="master.page.messages.recalculateSpammers" /></a></li>
					</security:authorize>
				</ul>
			</li>
			<li>
				<a class="fNiv">
					<spring:message code="master.page.profile" />
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="social-profile/list.do"><spring:message code="master.page.social.profiles" /></a></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

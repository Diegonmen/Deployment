<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="applications" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<jstl:if test="${!rookieLogged == true }">
		<display:column titleKey="application.curricula.personalData.fullName">
			<jstl:out value="${row.curricula.personalData.fullName}" />
		</display:column>
	</jstl:if>
	<acme:displayColumns
		attributes="problem.position.title,problem.title,status,moment,answer,answerLink,answerMoment"
		entity="application" />
	<display:column>
		<a href="curricula/show.do?id=${row.curricula.id}"><spring:message
				code="application.curricula.show" /></a>
	</display:column>
	<security:authorize access="hasRole('ROOKIE')">
		<display:column>
			<a href="application/rookie/show.do?applicationId=${row.id}"><spring:message
					code="application.show" /></a>
		</display:column>
		<display:column>
			<jstl:if test="${row.status == 'PENDING'}">
				<a href="application/rookie/edit.do?applicationId=${row.id }"><spring:message
						code="application.submit.answer" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
	<security:authorize access="hasRole('COMPANY')">
		<display:column>
			<a href="application/company/show.do?applicationId=${row.id}"><spring:message
					code="application.show" /></a>
		</display:column>
		<display:column>
			<jstl:if test="${row.status == 'SUBMITTED'}">
				<a href="application/company/accept.do?applicationId=${row.id }"><spring:message
						code="application.accept" /></a>
			</jstl:if>
		</display:column>
		<display:column>
			<jstl:if test="${row.status == 'SUBMITTED'}">
				<a href="application/company/reject.do?applicationId=${row.id }"><spring:message
						code="application.reject" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
</display:table>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="position.show" var="positionShow" />
<spring:message code="position.edit" var="positionEdit" />
<spring:message code="position.cancel" var="positionCancel" />
<spring:message code="position.companies" var="positionCompanies" />

<jstl:if test="${showSearch}">
<form action="position/search.do" method="get">
  <input type="text" name="q" value="<jstl:out value="${param['q']}" />" placeholder="<spring:message code='position.search.placeholder' />">&nbsp;
  <input type="submit" value="<spring:message code='position.search' />">
</form>

<jstl:if test="${param['q'] != null && param['q'] != ''}">
	<h3>Showing results for <jstl:out value="${param['q']}" /></h3>
</jstl:if>
</jstl:if>
<display:table name="positions" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<acme:displayColumns attributes="title,ticker,deadline,description"
		entity="position" />
	<display:column>
		<a href="position/show.do?positionId=${row.id}">${positionShow}</a>
	</display:column>
	<jstl:if test="${areMines}">
		<security:authorize access="hasRole('COMPANY')">
			<display:column>
				<jstl:if test="${row.draftMode==true}">
					<a href="position/company/edit.do?positionId=${row.id}">${positionEdit}</a>
				</jstl:if>
			</display:column>
			<display:column>
				<jstl:if test="${row.draftMode==false && row.cancelled==false }">
					<a href="application/company/list.do?positionId=${row.id }"><spring:message
							code="application.list" /></a>
				</jstl:if>
			</display:column>
		</security:authorize>
	</jstl:if>

	<security:authorize access="hasRole('ROOKIE')">
		<display:column>
			<jstl:if test="${row.draftMode == false && row.cancelled == false}">
				<jstl:if test="${!positionsApplied.contains(row)}">
					<a href="application/rookie/create.do?positionId=${row.id }"><spring:message
							code="application.create" /></a>
				</jstl:if>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<jstl:if test="${!areMines}">
			<display:column>
				<a href="company/fromPosition/show.do?positionId=${row.id}">${positionCompanies}</a>
			</display:column>
	</jstl:if>
	
	<security:authorize access="hasRole('AUDITOR')">
		<display:column>
			<a href="audit/auditor/create.do?positionId=${row.id }"><spring:message code="audit.assign" /></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('PROVIDER')">
		<display:column>
			<a href="sponsorship/provider/create.do?positionId=${row.id}"><spring:message
					code="position.sponsor" /></a>
		</display:column>
	</security:authorize>
	
	<display:column>
		<a href="audit/list.do?positionId=${row.id }"><spring:message code="position.audits" /></a>
	</display:column>

</display:table>

<security:authorize access="hasRole('COMPANY')">
	<acme:createButton entity="position" role="company" />
</security:authorize>



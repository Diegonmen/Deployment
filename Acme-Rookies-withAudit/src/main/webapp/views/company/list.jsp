<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="company.show" var="companyShow" />
<spring:message code="company.position" var="positions" />


		<display:table name="companys" id="row" requestURI="${requestURI}"
			pagesize="5" class="displaytag">
			
			<acme:displayColumns attributes="commercialName,email,address,phoneNumber"
				entity="company" />
			<display:column>
				<a href="company/show.do?companyId=${row.id}">${companyShow}</a>
			</display:column>
			<display:column>
				<a href="position/fromCompany/list.do?companyId=${row.id}">${positions}</a>
			</display:column>
		</display:table>


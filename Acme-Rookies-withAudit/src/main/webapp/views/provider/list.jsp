<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="provider.show" var="providerShow" />
<spring:message code="provider.item" var="items" />


		<display:table name="providers" id="row" requestURI="${requestURI}"
			pagesize="5" class="displaytag">
			
			<acme:displayColumns attributes="name,email,address,phoneNumber"
				entity="provider" />
			<display:column>
				<a href="provider/show.do?providerId=${row.id}">${providerShow}</a>
			</display:column>
			<display:column>
				<a href="item/list.do?providerId=${row.id}">${items}</a>
			</display:column>
		</display:table>


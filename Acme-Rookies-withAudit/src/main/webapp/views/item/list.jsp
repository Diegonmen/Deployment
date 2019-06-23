<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="item.show" var="itemShow" />
<spring:message code="item.edit" var="itemEdit" />
<spring:message code="item.cancel" var="itemCancel" />
<spring:message code="item.provider" var="itemProvider" />



<display:table name="items" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<acme:displayColumns attributes="name,description" entity="item" />
	<display:column>
		<a href="item/show.do?itemId=${row.id}">${itemShow}</a>
	</display:column>
	<jstl:if test="${mines}">
		<security:authorize access="hasRole('PROVIDER')">
			<display:column>
					<a href="item/provider/edit.do?itemId=${row.id}">${itemEdit}</a>
			</display:column>
		</security:authorize>
	</jstl:if>
	<jstl:if test="${!mines}">
			<display:column>
					<a href="provider/fromList/show.do?itemId=${row.id}">${itemProvider}</a>
			</display:column>
		
	</jstl:if>

</display:table>

<security:authorize access="hasRole('PROVIDER')">
	<acme:createButton entity="item" role="provider" />
</security:authorize>



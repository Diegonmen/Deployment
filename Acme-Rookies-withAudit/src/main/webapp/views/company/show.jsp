<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:img src="${company.picture}" />

<acme:showAttribute message="company.name" value="${company.name} ${company.surname}" />

<acme:showAttribute message="company.commercialName" value="${company.commercialName}" />

<acme:showAttribute message="company.email" value="${company.email}" />

<acme:showAttribute message="company.phoneNumber" value="${company.phoneNumber}" />

<acme:showAttribute message="company.address" value="${company.address}" />

<acme:showAttribute message="company.vat" value="${company.vat}" />

<jstl:if test="${isMe}">
	<security:authorize access="hasRole('COMPANY')">
			<input type="button" name="save" class="ui button"
				value="<spring:message code="company.edit" />"
 				onclick="javascript: relativeRedir('company/edit.do');" />
	</security:authorize>
</jstl:if>
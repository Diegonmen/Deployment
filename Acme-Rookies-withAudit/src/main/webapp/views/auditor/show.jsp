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

<acme:img src="${auditor.picture}" />

<acme:showAttribute message="auditor.name" value="${auditor.name} ${auditor.surname}" />

<acme:showAttribute message="auditor.email" value="${auditor.email}" />

<acme:showAttribute message="auditor.phoneNumber" value="${auditor.phoneNumber}" />

<acme:showAttribute message="auditor.address" value="${auditor.address}" />

<acme:showAttribute message="auditor.vat" value="${auditor.vat}" />

<jstl:if test="${isMe}">
<security:authorize access="hasRole('AUDITOR')">
		<input type="button" name="save" class="ui button"
			value="<spring:message code="auditor.edit" />"
 			onclick="javascript: relativeRedir('auditor/edit.do');" />
</security:authorize>
</jstl:if>
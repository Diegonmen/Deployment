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

<acme:img src="${provider.picture}" />

<acme:showAttribute message="provider.name" value="${provider.name} ${provider.surname}" />

<acme:showAttribute message="provider.make" value="${provider.make}" />

<acme:showAttribute message="provider.email" value="${provider.email}" />

<acme:showAttribute message="provider.phoneNumber" value="${provider.phoneNumber}" />

<acme:showAttribute message="provider.address" value="${provider.address}" />

<acme:showAttribute message="provider.vat" value="${provider.vat}" />

<jstl:if test="${isMe}">
	<security:authorize access="hasRole('PROVIDER')">
			<input type="button" name="save" class="ui button"
				value="<spring:message code="provider.edit" />"
 				onclick="javascript: relativeRedir('provider/edit.do');" />
	</security:authorize>
</jstl:if>
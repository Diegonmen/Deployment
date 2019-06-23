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

<acme:img src="${rookie.picture}" />

<acme:showAttribute message="rookie.name" value="${rookie.name} ${rookie.surname}" />

<acme:showAttribute message="rookie.email" value="${rookie.email}" />

<acme:showAttribute message="rookie.phoneNumber" value="${rookie.phoneNumber}" />

<acme:showAttribute message="rookie.address" value="${rookie.address}" />

<acme:showAttribute message="rookie.vat" value="${rookie.vat}" />

<jstl:if test="${isMe}">
	<security:authorize access="hasRole('ROOKIE')">
			<input type="button" name="save" class="ui button"
				value="<spring:message code="rookie.edit" />"
 				onclick="javascript: relativeRedir('rookie/edit.do');" />
 				
 				<input type="button" name="curricula" class="ui button"
				value="<spring:message code="curricula.list" />"
 				onclick="javascript: relativeRedir('curricula/list.do');" />
	</security:authorize>
</jstl:if>
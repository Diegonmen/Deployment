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

<acme:showAttribute message="curricula.personalData.fullName" value="${personalData.fullName}" />

<acme:showAttribute message="curricula.personalData.statement" value="${personalData.statement}" />

<acme:showAttribute message="curricula.personalData.phoneNumber" value="${personalData.phoneNumber}" />

<acme:showAttribute message="curricula.personalData.githubProfile" value="${personalData.githubProfile}" />

<acme:showAttribute message="curricula.personalData.linkedinProfile" value="${personalData.linkedinProfile}" />

<jstl:if test="${curricula.copied == false}">
<security:authorize access="hasRole('ROOKIE')">
		<input type="button" name="save" class="ui button"
			value="<spring:message code="curricula.edit" />"
 			onclick="javascript: relativeRedir('personalData/edit.do?id=${personalData.id}');" />
</security:authorize>
</jstl:if>
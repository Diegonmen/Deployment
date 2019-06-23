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

<acme:showAttribute message="socialProfile.nick" value="${socialProfile.nick}" />

<acme:showAttribute message="socialProfile.socialNetwork" value="${socialProfile.socialNetwork}" />

<acme:showAttribute message="socialProfile.link" value="${socialProfile.link}" />

<input type="button" name="edit"
	value="<spring:message code="socialProfile.edit" />"
	onclick="javascript: relativeRedir('/social-profile/edit.do?id=${socialProfile.id}');" />
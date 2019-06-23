<%--
 * datetime-local.tag
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
 
<%@ attribute name="path" required="true" %>
<%@ attribute name="code" required="true" %>

<%@ attribute name="readonly" required="false" %>
<%@ attribute name="datePlaceholder" required="false" %>
<%@ attribute name="typeNumber" required="false" %>
<%@ attribute name="size" required="false" %>

<jstl:if test="${readonly == null}">
	<jstl:set var="readonly" value="false" />
</jstl:if>

<jstl:choose>
	<jstl:when test="${typeNumber == true}">
		<jstl:set var="type" value="number" />
	</jstl:when>
	<jstl:otherwise>
		<jstl:set var="type" value="text" />
	</jstl:otherwise>
</jstl:choose>

<%-- Definition --%>

<div>
	<form:label path="${path}">
		<spring:message code="${code}" />
	</form:label>
	<jstl:if test="${datePlaceholder == true}">
		<form:input path="${path}" readonly="${readonly}" type="${type}" placeholder="dd/MM/yyyy HH:mm" />
	</jstl:if>
	<jstl:if test="${datePlaceholder == false || datePlaceholder == null}">
		<form:input path="${path}" type="${type}" readonly="${readonly}" />
	</jstl:if>
	<form:errors path="${path}" cssClass="error" />
</div>	
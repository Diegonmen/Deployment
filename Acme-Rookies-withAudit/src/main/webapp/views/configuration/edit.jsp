<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="configuration/administrator/edit.do"
	modelAttribute="configuration">
	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="configuration.sysName" path="sysName" />
	<acme:textarea code="configuration.bannerURL" path="bannerURL" />
	<acme:textarea code="configuration.welcomeMessageEng"
		path="welcomeMessageEng" />
	<acme:textarea code="configuration.welcomeMessageEsp"
		path="welcomeMessageEsp" />
	<acme:textbox code="configuration.countryCode" path="countryCode" />
	<acme:textbox code="configuration.cache" path="cache" typeNumber="true" />
	<acme:textbox code="configuration.finderResults" path="finderResults" typeNumber="true" />
	<form:label path="vat">
		<spring:message code="configuration.vat" />
	</form:label>
	<form:input path="vat" type="text" pattern="\d+([.]\d+)?" />
	<form:errors path="vat" cssClass="error" />
	<acme:textarea code="configuration.spamWords" path="spamWords" />


	<input type="submit" name="save"
		value="<spring:message code="configuration.save" />" />

	<input type="button" name="cancel"
		value="<spring:message code="configuration.cancel" />"
		onclick="javascript: relativeRedir('/');" />

</form:form>

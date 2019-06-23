<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${param['positionId'] != null}">
	<jstl:set var="actionAttributes" value="?positionId=${param['positionId']}" />
</jstl:if>

<form:form action="sponsorship/provider/edit.do${actionAttributes}" modelAttribute="sponsorship">

	<acme:hiddenInputs attributes="id"/>
	<acme:textbox code="sponsorship.banner" path="banner"/>
	<acme:textbox code="sponsorship.url" path="url"/>

	<acme:editButtons entity="sponsorship" role="provider" save="true" delete="${sponsorship.id!=0}" />

</form:form>

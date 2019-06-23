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

<form:form action="nyordel/company/edit.do" modelAttribute="nyordel">
	<acme:hiddenInputs attributes="id,audit" />
	<form:hidden path="moment" value="01/01/2001 00:00" />
	<acme:textarea code="nyordel.body" path="body" />
	<acme:textbox code="nyordel.picture" path="picture" />
	<spring:message code="nyordel.draftMode" />
	<form:radiobutton path="draftMode" value="true" />
	<spring:message code="nyordel.draftModeTrue" />
	<form:radiobutton path="draftMode" value="false" />
	<spring:message code="nyordel.draftModeFalse" />


	<acme:editButtons save="true" delete="${nyordel.id != 0 }" entity="nyordel" customCancelPath="audit/show.do?auditId=${param['auditId']}" />
</form:form>

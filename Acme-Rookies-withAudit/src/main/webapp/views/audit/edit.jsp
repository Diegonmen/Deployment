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

<security:authorize access="hasRole('AUDITOR')">
	<form:form action="audit/auditor/edit.do?positionId=${positionId}"
		modelAttribute="audit">
		<acme:hiddenInputs attributes="id" />
		<form:hidden path="moment" value="01/01/2001 00:00" />
		<acme:textarea code="audit.text" path="text" />
		<form:label path="score">
			<spring:message code="audit.score" />
		</form:label>
		<form:input path="score" type="text" pattern="\d+([.]\d+)?" />
		<form:errors path="score" cssClass="error" />
		<br/>
		<spring:message code="problem.draftMode" />
		<form:radiobutton path="draftMode" value="true" />
		<spring:message code="audit.draftModeTrue" />
		<form:radiobutton path="draftMode" value="false" />
		<spring:message code="audit.draftModeFalse" />


		<acme:editButtons save="true" delete="true" entity="audit" />
	</form:form>
</security:authorize>

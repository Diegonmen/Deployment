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

<security:authorize access="hasRole('COMPANY')">
	<form:form action="problem/company/edit.do" modelAttribute="problem">
		<acme:hiddenInputs attributes="id,version" />
		<acme:textbox code="problem.title" path="title" />
		<acme:textarea code="problem.statement" path="statement" />
		<acme:textarea code="problem.attachments" path="attachments" />
		<acme:textbox code="problem.hint" path="hint" />
		<acme:select items="${positions}" itemLabel="title" code="problem.positions" path="position"/>
		<br>
		<spring:message code="problem.draftMode" />
		<form:radiobutton path="draftMode" value="true" />
		<spring:message code="problem.draftModeTrue" />
		<form:radiobutton path="draftMode" value="false" />
		<spring:message code="problem.draftModeFalse" />

		<acme:editButtons entity="problem" role="company" save="true"
			delete="${problem.id!=0}" />

	</form:form>
</security:authorize>

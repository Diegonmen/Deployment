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
	<form:form action="position/company/edit.do" modelAttribute="position">
		<acme:hiddenInputs attributes="id" />
		<acme:textbox code="position.title" path="title" />
		<acme:textarea code="position.description" path="description" />
		<acme:textbox code="position.deadline" path="deadline"
			datePlaceholder="true" />
		<acme:textarea code="position.profile" path="profile" />
		<acme:textarea code="position.skills" path="skills" />
		<acme:textbox code="position.technologies" path="technologies"/>
		<acme:textbox code="position.salary" path="salary" typeNumber="true" />	
		<form:label path="draftMode">
		</form:label>
		<form:radiobutton path="draftMode" value="true" />
		<spring:message code="position.draftModeTrue" />
		<form:radiobutton path="draftMode" value="false" />
		<spring:message code="position.draftModeFalse" />

		<acme:editButtons entity="position" role="company" save="true" delete="${position.draftMode&&position.id!=0}"
			/>

	</form:form>
</security:authorize>

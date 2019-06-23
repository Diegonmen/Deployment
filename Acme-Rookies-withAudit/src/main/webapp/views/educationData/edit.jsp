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


<form:form action="educationData/edit.do?curriculaId=${curricula.id }" modelAttribute="educationData">

	<acme:hiddenInputs attributes="id,version"/>
	<acme:textbox code="curricula.educationData.degree" path="degree"/>
	<acme:textbox code="curricula.educationData.institution" path="institution"/>
	<form:label path="mark">
		<spring:message code="curricula.educationData.mark" />
	</form:label>
	<form:input path="mark" step="0.1" type="number" />
	<form:errors path="mark" cssClass="error" />
	<acme:textbox code="curricula.educationData.startDate" datePlaceholder="true" path="startDate" />
	<acme:textbox code="curricula.educationData.endDate" datePlaceholder="true" path="endDate" />

	<acme:editButtons entity="educationData" role="rookie" customCancelPath="curricula/show.do?id=${curricula.id }" save="true" delete="${educationData.id != 0}"/>

</form:form>

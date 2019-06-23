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


<form:form action="positionData/edit.do?curriculaId=${curricula.id }" modelAttribute="positionData">

	<acme:hiddenInputs attributes="id,version"/>
	<acme:textbox code="curricula.positionData.title" path="title"/>
	<acme:textbox code="curricula.positionData.description" path="description"/>
	<acme:textbox code="curricula.positionData.startDate" datePlaceholder="true" path="startDate" />
	<acme:textbox code="curricula.positionData.endDate" datePlaceholder="true" path="endDate" />

	<acme:editButtons entity="positionData" role="rookie" customCancelPath="curricula/show.do?id=${curricula.id }" save="true" delete="${positionData.id != 0}"/>

</form:form>

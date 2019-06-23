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


<form:form action="personalData/edit.do?curriculaId=${curricula.id }" modelAttribute="personalData">

	<acme:hiddenInputs attributes="id,version"/>
	<acme:textbox code="curricula.personalData.fullName" path="fullName"/>
	<acme:textbox code="curricula.personalData.statement" path="statement"/>
	<acme:textbox code="curricula.personalData.phoneNumber" path="phoneNumber"/>
	<acme:textbox code="curricula.personalData.githubProfile" path="githubProfile"/>
	<acme:textbox code="curricula.personalData.linkedinProfile" path="linkedinProfile"/>

	<acme:editButtons entity="personalData" role="rookie" customCancelPath="curricula/show.do?id=${curricula.id }" save="true" checkPhoneNumber = "true"/>

</form:form>

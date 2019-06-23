<%@page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="curricula/edit.do" modelAttribute="curriculaForm" id="form">

	<acme:hiddenInputs attributes="id" />

	<jstl:if test="${curriculaForm.id == 0}">
	<acme:textbox code="curricula.title" path="title" />
	<acme:textbox code="curricula.personalData.fullName" path="fullName" />
	<acme:textbox code="curricula.personalData.statement" path="statement" />
	<acme:textbox code="curricula.personalData.phoneNumber" path="phoneNumber" />
	<acme:textbox code="curricula.personalData.githubProfile" path="githubProfile" />
	<acme:textbox code="curricula.personalData.linkedinProfile" path="linkedinProfile" />
	</jstl:if>
	
	<jstl:if test="${curriculaForm.id > 0}">
		<acme:textbox code="curricula.title" path="title" />
		<acme:hiddenInputs attributes="fullName,statement,phoneNumber,githubProfile,linkedinProfile"/>
	</jstl:if>
	
	<jstl:if test="${curriculaForm.id > 0}">
		<acme:editButtons entity="curricula" role="rookie"
			save="true" customCancelPath="curricula/list.do" checkPhoneNumber = "true" />
	</jstl:if>

	<jstl:if test="${curriculaForm.id == 0}">
		<acme:editButtons entity="curricula" role="rookie"
			save="true" customCancelPath="welcome/index.do" checkPhoneNumber = "true" />

	</jstl:if>

</form:form>


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

<security:authorize access="hasRole('ROOKIE')">
	<form:form action="application/rookie/edit.do?positionId=${positionId}"
		modelAttribute="application">
		<acme:hiddenInputs attributes="id" />
		<form:hidden path="moment" value="01/01/2001 00:00"/>
		<jstl:if test="${application.id != 0 }">
			<acme:textarea code="application.answer" path="answer" />
			<acme:textbox code="application.answerLink" path="answerLink" />
			<form:hidden path="answerMoment" value="01/01/2001 00:00" />
		</jstl:if>

		<jstl:if test="${application.id == 0 }">
			<acme:select items="${curriculas }" itemLabel="title"
				code="application.curricula" path="curricula" />
		</jstl:if>

		<jstl:if test="${application.id == 0 }">
			<input type="submit" name="save"
				value="<spring:message code="application.submit"/>" />
		</jstl:if>
		<jstl:if test="${application.id != 0 }">
			<input type="submit" name="submit"
				value="<spring:message code="application.submit"/>" />
		</jstl:if>
		<acme:cancel url="application/rookie/list.do?problemId=${problemId}"
			code="application.cancel" />

	</form:form>
</security:authorize>

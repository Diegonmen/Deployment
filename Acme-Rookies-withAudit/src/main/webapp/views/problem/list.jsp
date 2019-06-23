<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="problems" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<acme:displayColumns attributes="title,statement,hint,position.title"
		entity="problem" />
	<display:column>
		<a href="problem/show.do?problemId=${row.id}"><spring:message
				code="problem.show" /></a>
	</display:column>
	<display:column>
		<jstl:if test="${row.draftMode == true}">
			<a href="problem/company/edit.do?problemId=${row.id}"><spring:message
					code="problem.edit" /></a>
		</jstl:if>
	</display:column>
</display:table>

<acme:createButton entity="problem" role="company"/>
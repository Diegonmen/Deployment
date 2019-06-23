<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="audits" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<display:column titleKey="audit.map[row].title">
		<jstl:out value="${row.position.title}"></jstl:out>
	</display:column>
	
	<acme:displayColumns
		attributes="text,score,moment" entity="audit" />
	
	<display:column>
		<a href="audit/show.do?auditId=${row.id}"><spring:message
				code="audit.show" /></a>
	</display:column>
	
	<display:column>
		<jstl:if test="${row.draftMode == true }">
			<a href="audit/auditor/edit.do?auditId=${row.id}"><spring:message
				code="audit.edit" /></a>	
		</jstl:if>
	</display:column>
</display:table>


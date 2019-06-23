<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="curricula.delete" var="curriculaDelete" />

<display:table name="curricula" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag" >
	
	<<acme:displayColumns attributes="title" entity="curricula"/>
	 <display:column>
			<a href="curricula/show.do?id=${row.id}">
				<spring:message code="curricula.show" />
			</a>
		</display:column>
		<display:column>
			<a href="curricula/delete.do?curriculaId=${row.id}">${curriculaDelete}</a>
	</display:column>
</display:table>

<security:authorize access="hasRole('ROOKIE')">
	<acme:createButton url="curricula/create.do" entity="curricula"/>
</security:authorize>
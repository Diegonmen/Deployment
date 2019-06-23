<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="view" tagdir="/WEB-INF/tags"%>


<acme:showAttribute message="position.title" value="${audit.position.title}"/>
<acme:showAttribute message="audit.moment" value="${audit.moment}"/>
<acme:showAttribute message="audit.text" value="${audit.text}"/>
<acme:showAttribute message="audit.score" value="${audit.score}"/>
<acme:showAttribute message="audit.auditor.name" value="${audit.auditor.name}  ${audit.auditor.surname}"/>

<h2><spring:message code="audit.nyordels" />:</h2>
<jsp:useBean id="now" class="java.util.Date" />

<style>
.indigo {
	color: white;
	background-color: Indigo;
}

.grey {
	color: white;
	background-color: DarkSlateGrey;
}

.papaya {
	background-color: PapayaWhip;
}
</style>

<display:table name="nyordels" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<jstl:set var="pm" value="${row.moment}" />
	
	<jstl:choose>
		<jstl:when test="${(now.time - pm.time) le 2629800000}">
						<jstl:set var="color" value="indigo" />
					</jstl:when>
					<jstl:when
						test="${(now.time - pm.time) gt 2629800000 and ((now.time - pm.time) le 5259600000)}">
						<jstl:set var="color" value="grey" />
					</jstl:when>
					<jstl:otherwise>
						<jstl:set var="color" value="papaya" />
					</jstl:otherwise>
	</jstl:choose>

	<acme:displayColumns attributes="ticker,body" entity="nyordel" stylus="${color}"/>
	
	<spring:message code="master.page.date.format" var="dateFormat" />
  	<display:column property="moment" format="{0,date,${dateFormat}}" titleKey="nyordel.moment" class="${color }"/>

	<display:column class="${color }">
		<a href="nyordel/show.do?nyordelId=${row.id}"><spring:message
				code="nyordel.show" /></a>
	</display:column>
	
	<security:authorize access="hasRole('COMPANY')">
		<display:column class="${color }">
			<jstl:if test="${row.draftMode == true }">
				<a href="nyordel/company/edit.do?nyordelId=${row.id}"><spring:message
						code="nyordel.edit" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
</display:table>

<security:authorize access="hasRole('COMPANY')">
	<input type="button" name="create"
		value="<spring:message code="nyordel.create" />"
		onclick="javascript: relativeRedir('nyordel/company/create.do?auditId=${audit.id}');" />
</security:authorize>
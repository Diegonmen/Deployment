<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

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
.white{
	background-color: white;
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
					<jstl:when test="${row.moment == null}">
						<jstl:set var="color" value="white" />
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
	<jstl:if test="${edit}">
	<display:column class="${color }">
		<jstl:if test="${row.draftMode == true }">
			<a href="nyordel/company/edit.do?nyordelId=${row.id}"><spring:message
					code="nyordel.edit" /></a>
		</jstl:if>
	</display:column>
	</jstl:if>
	</security:authorize>
</display:table>


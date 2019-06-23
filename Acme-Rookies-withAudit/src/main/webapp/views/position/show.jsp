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

<spring:message code="position.cancel" var="positionCancel" />

<acme:showAttribute message="position.title" value="${position.title}"/>
<acme:showAttribute message="position.description" value="${position.description}"/>
<acme:showAttribute message="position.deadline" value="${position.deadline}"/>
<acme:showAttribute message="position.ticker" value="${position.ticker}"/>
<acme:showAttribute message="position.draftMode" value="${position.draftMode}"/>
<acme:showAttribute message="position.profile" value="${position.profile}"/>
<acme:showAttribute message="position.skills" value="${position.skills}"/>
<acme:showAttribute message="position.technologies" value="${position.technologies}"/>
<acme:showAttribute message="position.salary" value="${position.salary}"/>
<acme:showAttribute message="position.cancelled" value="${position.cancelled}"/>

<jstl:if test="${canCancel}">	
		<a href="position/company/cancel.do?positionId=${position.id}">${positionCancel}</a>
</jstl:if>
<br>

<jstl:if test="${sponsorship != null}">
	<h2 style="margin-bottom:0px;"><spring:message code="position.sponsored.by" /></h2>
	<a href="<jstl:out value="${sponsorship.url}" />" ><img src="<jstl:out value="${sponsorship.banner}" />" height="120px" /></a>
	<br />
</jstl:if>
<h2><spring:message code="position.problems" />:</h2>
<display:table name="problems" id="row" requestURI="${requestURI}"
		pagesize="5" class="displaytag">			
		<acme:displayColumns attributes="title,statement,hint"
			entity="problem" />
		<display:column>
			<a href="problem/show.do?problemId=${row.id}"><spring:message code="position.show" /></a>
		</display:column>
</display:table>

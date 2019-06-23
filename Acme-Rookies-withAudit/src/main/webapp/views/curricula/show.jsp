<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="view" tagdir="/WEB-INF/tags"%>

<spring:message code="curricula.personalData.fullName" var="pDfullName" />
<spring:message code="curricula.positionData.title" var="pDtitle" />
<spring:message code="curricula.educationData.degree" var="pDdegree" />
<spring:message code="curricula.miscellaneousData.text" var="pDtext" />
<spring:message code="curricula.personalData.table" var="tPersonalData" />
<spring:message code="curricula.positionData.table" var="tPositionData" />
<spring:message code="curricula.educationData.table"
	var="tEducationData" />
<spring:message code="curricula.miscellaneousData.table"
	var="tMiscellaneousData" />


<h3>
	<jstl:out value="${tPersonalData}"></jstl:out>
</h3>
<display:table name="curricula.personalData" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">


	<display:column value="${row.fullName}" title="${pDfullName}"></display:column>

	<security:authorize access="hasRole('ROOKIE')">
		<display:column>
			<a href="personalData/show.do?id=${row.id}"> <spring:message
					code="curricula.show" />
			</a>
		</display:column>
	</security:authorize>
</display:table>


<h3><jstl:out value="${tPositionData}"></jstl:out></h3>
<jstl:if test="${not empty curricula.positionData }">
<display:table name="curricula.positionData" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	
		<display:column value="${row.title}" title="${pDtitle}"></display:column>
	

	<security:authorize access="hasRole('ROOKIE')">
		<display:column>
			<a href="positionData/show.do?id=${row.id}"> <spring:message
					code="curricula.show" />
			</a>
		</display:column>
		</security:authorize>

</display:table>
</jstl:if>
<jstl:if test="${curricula.copied == false}">
<security:authorize access="hasRole('ROOKIE')">
			<input type="button" class="ui button" name="create"
				value="<spring:message code="curricula.create" />"
				onclick="javascript: relativeRedir('positionData/create.do?curriculaId=${curricula.id}')">
			<br></br>
	</security:authorize>
</jstl:if>

<h3>
	<jstl:out value="${tEducationData}"></jstl:out>
</h3>
<jstl:if test="${not empty curricula.educationData }">
<display:table name="curricula.educationData" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	
		<display:column value="${row.degree}" title="${pDdegree}"></display:column>
	
	<security:authorize access="hasRole('ROOKIE')">
		<display:column>
			<a href="educationData/show.do?id=${row.id}"> <spring:message
					code="curricula.show" />
			</a>
		</display:column>
	</security:authorize>
	
</display:table>
</jstl:if>
<jstl:if test="${curricula.copied == false}">
<security:authorize access="hasRole('ROOKIE')">
			<input type="button" class="ui button" name="create"
				value="<spring:message code="curricula.create" />"
				onclick="javascript: relativeRedir('educationData/create.do?curriculaId=${curricula.id}');">
			<br></br>
	</security:authorize>
</jstl:if>
<h3>
	<jstl:out value="${tMiscellaneousData}"></jstl:out>
</h3>
<jstl:if test="${not empty curricula.miscellaneousData }">
<display:table name="curricula.miscellaneousData" id="row"
	requestURI="${requestURI}" pagesize="5" class="displaytag">

	
		<display:column value="${row.text}" title="${pDtext}"></display:column>
	

	<security:authorize access="hasRole('ROOKIE')">
		<display:column>
			<a href="miscellaneousData/show.do?id=${row.id}"> <spring:message
					code="curricula.show" />
			</a>
		</display:column>
	</security:authorize>
	
</display:table>
</jstl:if>
<jstl:if test="${curricula.copied == false}">
<security:authorize access="hasRole('ROOKIE')">
			<input type="button" class="ui button" name="create"
				value="<spring:message code="curricula.create" />"
				onclick="javascript: relativeRedir('miscellaneousData/create.do?curriculaId=${curricula.id}');">
			<br></br>
</security:authorize>

<security:authorize access="hasRole('ROOKIE')">
	<input type="button" name="save" class="ui button"
			value="<spring:message code="curricula.edit" />"
 			onclick="javascript: relativeRedir('curricula/edit.do?id=${curricula.id}');" />
</security:authorize>
</jstl:if>

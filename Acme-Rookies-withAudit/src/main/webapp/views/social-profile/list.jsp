<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="socialProfiles" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag" >
	
	<acme:displayColumns attributes="nick,socialNetwork,link" entity="socialProfile"/>
	
	<display:column>
		<a href="social-profile/edit.do?id=${row.id}">
			<spring:message code="socialProfile.edit" />
		</a>
	</display:column>
	
</display:table>

<acme:createButton entity="socialProfile" url="/social-profile/create.do"/>
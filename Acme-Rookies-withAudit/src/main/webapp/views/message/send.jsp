<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<spring:message code="message.moment" var="moment" />
<spring:message code="message.subject" var="subject" />
<spring:message code="message.body" var="body" />
<spring:message code="message.priority" var="priority" />
<spring:message code="message.saveStr" var="saveStr" />
<spring:message code="message.destinations" var="destinations" />

<form:form action="message/edit.do" modelAttribute="messageObject">
	<form:hidden path="id"/>
	
	<h3>${destinations}</h3>
	<div><form:select path="recipient" multiple="false">
	  <jstl:forEach items="${allActors}" var="actor">
	  	<form:option value="${actor}">${actor.userAccount.username}</form:option>
	  </jstl:forEach>
	</form:select></div>
	<form:errors cssClass="error" path="recipient"></form:errors>
	<br/>
	<acme:textbox code="message.subject" path="subject"/><br/>
	<acme:textarea code="message.body" path="body"/><br/>
	<spring:message code="message.pattern" var="pattern"/>
	<acme:textarea code="message.tags" path="tags" placeholder="${pattern}"/><br/>
	
	<acme:editButtons entity="message"
			save="true" customCancelPath="welcome/index.do"/>
</form:form>
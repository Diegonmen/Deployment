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

<form:form action="message/administrator/broadcast.do" modelAttribute="messageObject">

	<br/>
	<acme:textbox code="message.subject" path="subject"/><br/>
	<acme:textarea code="message.body" path="body"/><br/>
	<spring:message code="message.pattern" var="pattern"/>
	<acme:textarea code="message.tags" path="tags" value="SYSTEM" placeholder="${pattern}"/><br/>
	
	<input type="submit" name="save" value="${saveStr}">
</form:form>
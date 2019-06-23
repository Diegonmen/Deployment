<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="view" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:showAttribute message="message.moment" value="${messageObject.moment}" />
<acme:showAttribute message="message.sender" value="${messageObject.sender.userAccount.username}" />
<acme:showAttribute message="message.recipients" value="${messageObject.recipient.userAccount.username}" />
<acme:showAttribute message="message.subject" value="${messageObject.subject}" />
<br />
<acme:showAttribute message="message.body" value="${messageObject.body}" box="true" />
<acme:showAttribute message="message.tags" value="${messageObject.tags}" />
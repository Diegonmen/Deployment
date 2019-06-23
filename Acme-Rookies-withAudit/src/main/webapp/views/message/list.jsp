<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="message.view" var="messageView" />
<spring:message code="message.delete" var="messageDelete" />
<spring:message code="message.tags" var="messageTags" />
<spring:message code="message.sender" var="messageSender" />

<display:table pagesize="5" list="${messages }" class="displaytag" keepStatus="true"
	name="messages" requestURI="/message/list.do" id="row">

	<%-- <display:column value="${row.priority}" title="${messagePriority}"></display:column> --%>
	<acme:displayColumns attributes="tags,moment,subject" entity="message"/>
	<display:column value="${row.sender.userAccount.username}" title="${messageSender}"></display:column>
	<display:column>
			<a href="message/show.do?messageId=${row.id}">${messageView}</a>
	</display:column>
	<display:column>
			<a href="message/delete.do?messageId=${row.id}">${messageDelete}</a>
	</display:column>
</display:table>

<jstl:if test="${not empty messageCode}">
	<p class="error"><jstl:out value="${messageCode}"/></p>
</jstl:if>
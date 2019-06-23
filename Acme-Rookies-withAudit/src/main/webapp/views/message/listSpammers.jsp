<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


		<display:table name="spammers" id="row" requestURI="${requestURI}"
			pagesize="5" class="displaytag">
			
			<acme:displayColumns attributes="isBanned,phoneNumber,vat,userAccount.username"
				entity="actor" />
			<jstl:if test="${ not row.isBanned }">
				<display:column>
					<a href="message/administrator/ban.do?actorId=${row.id}"><spring:message code="actor.ban"/></a>
				</display:column>
			</jstl:if>
			<jstl:if test="${ row.isBanned }">
				<display:column>
					<a href="message/administrator/unban.do?actorId=${row.id}"><spring:message code="actor.unban"/></a>
				</display:column>
			</jstl:if>
		</display:table>
		

			<p class="error"><jstl:out value="${error}"/></p>
			${error}



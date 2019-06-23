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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<acme:img src="${nyordel.picture}"/>

<acme:showAttribute message="nyordel.position.title" value="${position.title}"/>
<acme:showAttribute message="nyordel.ticker" value="${nyordel.ticker}"/>
<spring:message code="master.page.date.format" var="dateFormat"/>
 <b><spring:message code="nyordel.moment"></spring:message></b>
    <fmt:formatDate value="${nyordel.moment}" pattern="${dateFormat}" />
<br>
<acme:showAttribute message="nyordel.body" value="${nyordel.body}"/>
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


<acme:showAttribute message="position.title" value="${application.problem.position.title}"/>
<acme:showAttribute message="problem.title" value="${application.problem.title }" />
<acme:showAttribute message="application.status" value="${application.status }" />
<acme:showAttribute message="application.moment" value="${application.moment }" />
<acme:showAttribute message="application.answer" value="${application.answer }" />
<acme:showAttribute message="application.answerLink" value="${application.answerLink }" />
<acme:showAttribute message="application.answerMoment" value="${application.answerMoment }" />
<a href="/curricula/rookie/show.do?curriculaId=${application.curricula.id }"><spring:message code="application.curricula.show" /></a>

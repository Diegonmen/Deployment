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


<acme:showAttribute message="problem.title" value="${problem.title}"/>
<acme:showAttribute message="problem.statement" value="${problem.statement}"/>
<acme:showAttribute message="problem.position.title" value="${problem.position.title}"/>
<acme:showAttribute message="problem.hint" value="${problem.hint}"/>
<acme:showAttribute message="problem.draftMode" value="${problem.draftMode}"/>
<acme:showAttribute message="problem.attachments" value="${problem.attachments}"/>
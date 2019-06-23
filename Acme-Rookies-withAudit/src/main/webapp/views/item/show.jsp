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


<acme:showAttribute message="item.name" value="${item.name}"/>
<jstl:forEach var="picture" items="${item.pictures}">
	<acme:img src="${picture}" />
</jstl:forEach>
<acme:showAttribute message="item.description" value="${item.description}"/>
<acme:showAttribute message="item.links" value="${item.links}"/>


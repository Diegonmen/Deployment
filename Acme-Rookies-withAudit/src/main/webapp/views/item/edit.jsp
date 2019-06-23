<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('PROVIDER')">
	<form:form action="item/provider/edit.do" modelAttribute="item">
		<acme:hiddenInputs attributes="id" />
		
		<acme:textbox code="item.name" path="name" />
		<acme:textarea code="item.description" path="description" />
		<acme:textarea code="item.links" path="links" />
		<acme:textarea code="item.pictures" path="pictures" />
		
		<acme:editButtons entity="item" role="provider" save="true" delete="${item.id!=0}"/>

	</form:form>
</security:authorize>

<%@page import="domain.Finder"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<form:form action="finder/rookie/edit.do" modelAttribute="finder">
	<acme:hiddenInputs attributes="id"/>
	<acme:textbox code="finder.keyword" path="keyword" />
	<acme:textbox code="finder.minimumSalary" path="minimumSalary" typeNumber="true" />
	<acme:textbox code="finder.maximumSalary" path="maximumSalary" typeNumber="true" />
	<acme:textbox code="finder.deadline" path="deadline" datePlaceholder="true" />
	
	<input type="submit" name="save"
		value="<spring:message code="finder.search" />" />&nbsp;
		
	<input type="button" name="clear"
		value="<spring:message code="finder.clear" />"
		onclick="javascript: relativeRedir('/finder/rookie/clear.do');" />&nbsp;
	
</form:form>


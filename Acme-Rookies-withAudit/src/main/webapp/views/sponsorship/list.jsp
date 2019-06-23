<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<style>
.grey {
	color: grey;
}
</style>

<display:table name="sponsorships" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag" >
	
	<display:column titleKey="sponsorship.banner">
		<img src="${row.banner}" style="${imgStyle}" height="40px" />
	</display:column>
	<acme:displayColumns attributes="url" entity="sponsorship"/>
	<display:column>
		<a href="sponsorship/provider/show.do?id=${row.id}">
			<spring:message code="sponsorship.show" />
		</a>
	</display:column>
	<display:column>
		<a href="sponsorship/provider/edit.do?id=${row.id}">
			<spring:message code="sponsorship.edit" />
		</a>
	</display:column>
	
</display:table>

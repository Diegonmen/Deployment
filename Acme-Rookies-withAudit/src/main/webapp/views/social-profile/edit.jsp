<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="social-profile/edit.do" modelAttribute="socialProfile">

	<acme:hiddenInputs attributes="id,version"/>
	
	<acme:textbox code="socialProfile.nick" path="nick"/>
	
	<acme:textbox code="socialProfile.socialNetwork" path="socialNetwork"/>
	
	<acme:textbox code="socialProfile.link" path="link"/>

	<acme:editButtons entity="socialProfile" save="true" delete="${socialProfile.id != 0}" customCancelPath="/social-profile/list.do" />
	
</form:form>
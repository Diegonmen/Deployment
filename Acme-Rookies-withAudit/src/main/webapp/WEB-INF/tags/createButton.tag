<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
 
<%@ attribute name="entity" required="true" %>

<%@ attribute name="role" required="false" %>

<%@ attribute name="url" required="false" %>

<jstl:if test="${url == null}">
	<jstl:set var="url" value="${entity}/${role}/create.do" />
</jstl:if>

<%-- Definition --%>

<jstl:if test="${role != null}">
	<security:authorize access="hasRole('${role.toUpperCase()}')">
		<input type="button" name="create"
			value="<spring:message code="${entity}.create" />"
			onclick="javascript: relativeRedir('${url}');" />
	</security:authorize>
</jstl:if>
<jstl:if test="${role == null}">
	<input type="button" name="create"
			value="<spring:message code="${entity}.create" />"
			onclick="javascript: relativeRedir('${url}');" />
</jstl:if>
	
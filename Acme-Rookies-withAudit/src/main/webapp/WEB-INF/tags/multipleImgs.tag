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

<%@ attribute name="urls" required="true" type="java.util.Collection" %>

<%-- Definition --%>
<jstl:if test="${urls.isEmpty()}">
	<spring:message code="master.page.no.images" />
</jstl:if>
<br />
<jstl:forEach var="url" items="${urls}" varStatus="loop">
	<a href="<jstl:out value="${url}"/>"><img src="<jstl:out value="${url}"/>" height="80"></a>
	<jstl:if test="${(loop.count) % 4 == 0}">
		<br />
	</jstl:if>
</jstl:forEach>
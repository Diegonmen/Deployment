<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
 
<%@ attribute name="entity" required="true" %>

<%@ attribute name="role" required="false" %>
<%@ attribute name="save" required="false" %>
<%@ attribute name="delete" required="false" %>

<%@ attribute name="customCancelPath" required="false" %>
<%@ attribute name="checkPhoneNumber" required="false" %>

<jstl:if test="${customCancelPath == null}">
	<jstl:if test="${role == null}">
		<jstl:set var="customCancelPath" value="${entity}/list.do" />
	</jstl:if>
	<jstl:if test="${role != null}">
		<jstl:set var="customCancelPath" value="${entity}/${role}/list.do" />
	</jstl:if>
</jstl:if>

<%-- Definition --%>

<br />

<jstl:if test="${save}">
	<jstl:if test="${checkPhoneNumber}">
	<input type="submit" name="save"
		value="<spring:message code="${entity}.save" />" onclick="if(!/^(\+[0-9]{1,3}[ ]{0,1}(\([0-9]{1,3}\)[ ]{0,1}){0,1}){0,1}[0-9]{1}[0-9 ]{3,}$/.test(document.getElementById('phoneNumber').value)) { return confirm('<spring:message code="${entity}.confirm.phoneNumber" />')}" />
	</jstl:if>
	<jstl:if test="${!checkPhoneNumber}">
	<input type="submit" name="save"
		value="<spring:message code="${entity}.save" />" />
	</jstl:if>
</jstl:if>
<jstl:if test="${delete}">
	<input type="submit" name="delete"
		value="<spring:message code="${entity}.delete" />"
		onclick="return confirm('<spring:message code="${entity}.confirm.delete" />')" />
</jstl:if>

<input type="button" name="cancel"
	value="<spring:message code="${entity}.cancel" />"
	onclick="javascript: relativeRedir('${customCancelPath}');" />
<br />
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:img src="${administrator.picture}" />

<acme:showAttribute message="administrator.name" value="${administrator.name} ${administrator.surname}" />

<acme:showAttribute message="administrator.email" value="${administrator.email}" />

<acme:showAttribute message="administrator.phoneNumber" value="${administrator.phoneNumber}" />

<acme:showAttribute message="administrator.address" value="${administrator.address}" />

<acme:showAttribute message="administrator.vat" value="${administrator.vat}" />

<acme:showAttribute message="administrator.creditCard.number" value="${administrator.creditCard.number}" />

<acme:showAttribute message="administrator.creditCard.cvv" value="${administrator.creditCard.cvv}" />

<acme:showAttribute message="administrator.creditCard.holderName" value="${administrator.creditCard.holderName}" />

<acme:showAttribute message="administrator.creditCard.make" value="${administrator.creditCard.make}" />

<acme:showAttribute message="administrator.creditCard.expirationMonth" value="${administrator.creditCard.expirationMonth}" />

<acme:showAttribute message="administrator.creditCard.expirationYear" value="${administrator.creditCard.expirationYear}" />

<security:authorize access="hasRole('ADMINISTRATOR')">
		<input type="button" name="save" class="ui button"
			value="<spring:message code="administrator.edit" />"
 			onclick="javascript: relativeRedir('administrator/edit.do');" />
</security:authorize>
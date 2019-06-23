<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<jstl:set value="<%=LocaleContextHolder.getLocale()%>" var="locale"></jstl:set>
<fmt:setLocale value="${locale}" />
<html>
<body>
	<h4>
		<spring:message code="administrator.avgMinMaxStdevPositionsPerCompany"
			var="avgMinMaxStdevPositionsPerCompanyHead" />
		<jstl:out value="${avgMinMaxStdevPositionsPerCompanyHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevPositionsPerCompany">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMaxStdevPositionsPerCompany}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	<h4>
		<spring:message code="administrator.avgMinMaxStdevApplicationsPerRookie"
			var="avgMinMaxStdevApplicationsPerRookieHead" />
		<jstl:out value="${avgMinMaxStdevApplicationsPerRookieHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevApplicationsPerRookie">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMaxStdevApplicationsPerRookie}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	<h4>
		<spring:message code="administrator.avgMinMaxStdevCurriculaPerRookie"
			var="avgMinMaxStdevCurriculaPerRookieHead" />
		<jstl:out value="${avgMinMaxStdevCurriculaPerRookieHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevCurriculaPerRookie">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMaxStdevCurriculaPerRookie}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	<h4>
		<spring:message code="administrator.avgMinMaxStdevResultsPerFinder"
			var="avgMinMaxStdevResultsPerFinderHead" />
		<jstl:out value="${avgMinMaxStdevResultsPerFinderHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevResultsPerFinder">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMaxStdevResultsPerFinder}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	<h4>
		<spring:message code="administrator.avgMinMaxStdevSalaries"
			var="avgMinMaxStdevSalariesHead" />
		<jstl:out value="${avgMinMaxStdevSalariesHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevSalaries">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMaxStdevSalaries}">
				<td><fmt:formatNumber type="currency" value="${datos}" currencySymbol="&euro;" /></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<table class="displaytag" name="biggest">
		<tr>
			<th><spring:message
					code="administrator.companiesMorePositions"
					var="bestHeader" /> <jstl:out value="${bestHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${companiesMorePositions}">
			<tr>
				<td><jstl:out value="${datos.commercialName}" /></td>
				<td>${fn:length(datos.positions)}</td>
			</tr>
			</jstl:forEach>

	</table>
	
	<table class="displaytag" name="biggest">
		<tr>
			<th><spring:message
					code="administrator.rookiesMoreRequests"
					var="bestHeader" /> <jstl:out value="${bestHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${rookiesMoreApplications}">
			<tr>
				<td><jstl:out value="${datos.userAccount.username}" /></td>
				<td>${fn:length(datos.applications)}</td>
			</tr>
			</jstl:forEach>

	</table>
	
	<table class="displaytag" name="biggest">
		<tr>
			<th><spring:message
					code="administrator.bestSalary"
					var="bestHeader" /> <jstl:out value="${bestHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${positionsGreatestSalary}">
			<tr>
				<td><jstl:out value="${datos.title}" /> (<jstl:out value="${datos.ticker}"/>)</td>
				<td><fmt:formatNumber type="currency" value="${datos.salary}" currencySymbol="&euro;" /></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<table class="displaytag" name="biggest">
		<tr>
			<th><spring:message
					code="administrator.worstSalary"
					var="bestHeader" /> <jstl:out value="${bestHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${positionsLowestSalary}">
			<tr>
				<td><jstl:out value="${datos.title}" /> (<jstl:out value="${datos.ticker}" />)</td>
				<td><fmt:formatNumber type="currency" value="${datos.salary}" currencySymbol="&euro;" /></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<h4>
		<spring:message code="administrator.ratioFinder"
			var="percentageFixUpTasksWithComplaintHead" />
		<jstl:out value="${percentageFixUpTasksWithComplaintHead}" />
		=
		<jstl:choose>
			<jstl:when test="${ratioFinder.matches('[0-9]+') }"> 
			<fmt:formatNumber value="${ratioFinders*100}"
				maxFractionDigits="2" />
			%
			</jstl:when>
			<jstl:otherwise>
				<jstl:out value="${ratioFinders }"/>
			</jstl:otherwise>
		</jstl:choose>
	</h4>
	
	<h2>D04</h2>																				<!-- AQUI EMPIEZA TODO LO DEL D04, CHAVALADA :D -->
	
	<h4>
		<spring:message code="administrator.avgMinMaxStdev6"/>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevSalaries">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMax6}">
				<td><jstl:out value="${datos}"/></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<h4>
		<spring:message code="administrator.avgMinMaxStdev7"/>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevSalaries">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMax7}">
				<td><jstl:out value="${datos}"/></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<h4>
		<spring:message code="administrator.avgMinMaxStdev8"/>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevSalaries">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMax8}">
				<td><jstl:out value="${datos}"/></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<h4>
		<spring:message code="administrator.avgMinMaxStdev9"/>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevSalaries">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMax9}">
				<td><jstl:out value="${datos}"/></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<h4>
		<spring:message code="administrator.avgMinMaxStdev10"/>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevSalaries">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMax10}">
				<td><jstl:out value="${datos}"/></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<table class="displaytag" name="biggest">
		<tr>
			<th><spring:message code="administrator.highestAudit"/></th>
		</tr>
			<jstl:forEach var="datos"
				items="${companiesMorePositions}">
			<tr>
				<td><jstl:out value="${datos.userAccount.username}" /></td>
				<td><jstl:out value="${datos.auditScore}"/></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<h3><spring:message code="administrator.avgSalaryAudit"/>: <fmt:formatNumber type="currency" value="${avgSalaryHighestScores}" currencySymbol="&euro;" /></h3>
	
	<table class="displaytag" name="biggest">
		<tr>
			<th><spring:message code="administrator.topProvider"/></th>
		</tr>
			<jstl:forEach var="datos"
				items="${top5Providers}">
			<tr>
				<td><jstl:out value="${datos.userAccount.username}" /></td>
				<td><jstl:out value="${fn:length(datos.items)}"/></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<table class="displaytag" name="biggest">
		<tr>
			<th><spring:message code="administrator.sponsorShizoodle"/></th>
		</tr>
			<jstl:forEach var="datos"
				items="${providers10Percent}">
			<tr>
				<td><jstl:out value="${datos.userAccount.username}" /></td>
				<td><jstl:out value="${fn:length(datos.sponsorships)}"/></td>
			</tr>
			</jstl:forEach>

	</table>
	
<!-- 	
	<table class="displaytag" name="biggest">
		<tr>
			<th><spring:message
					code="administrator.biggest"
					var="bestHeader" /> <jstl:out value="${bestHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${largestBrotherhoods}">
			<tr>
				<td><jstl:out value="${datos.title}" /></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<table class="displaytag" name="smallest">
		<tr>
			<th><spring:message
					code="administrator.smallest"
					var="bestHeader" /> <jstl:out value="${bestHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${smallestBrotherhoods}">
			<tr>
				<td><jstl:out value="${datos.title}" /></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<table class="displaytag" name="in30Days">
		<tr>
			<th><spring:message
					code="administrator.inThirtyDays"
					var="bestHeader" /> <jstl:out value="${bestHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${in30Days}">
			<tr>
				<td><jstl:out value="${datos.title}" /></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<h4> <spring:message
					code="administrator.status"/></h4>
	<table class="displaytag" name="status">
		<tr>
			<th><spring:message code="administrator.status.accepted"/></th>
			<th><spring:message code="administrator.status.rejected"/></th>
			<th><spring:message code="administrator.status.pending"/></th>
		</tr>
			<tr>
			<jstl:forEach var="datos"
				items="${ratioRequestsByStatus}">
				<td><jstl:out value="${datos}" /></td>
			</jstl:forEach>
			</tr>
	</table>
	
	<h4> <spring:message
					code="administrator.statusByParade"/></h4>
	<table class="displaytag" name="status">
		<tr>
			<th></th>
			<th><spring:message code="administrator.status.accepted"/></th>
			<th><spring:message code="administrator.status.rejected"/></th>
			<th><spring:message code="administrator.status.pending"/></th>
		</tr>
			
			<jstl:forEach var="datos"
				items="${ratioStatusParade}">
				<tr>
				<th><jstl:out value="${datos.key.title}" /></th>
				<td><jstl:out value="${datos.value[0] }"/>
				<td><jstl:out value="${datos.value[1] }"/>
				<td><jstl:out value="${datos.value[2] }"/>
				</tr>
			</jstl:forEach>
			
	</table>
	
	<table class="displaytag" name="status">
		<tr>
			<th><spring:message
					code="administrator.atLeast10PercentAccepted"
					var="bestHeader" /> <jstl:out value="${bestHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${atLeast10PercentAccepted}">
			<tr>
				<td><jstl:out value="${datos.userAccount.username}" /></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<h4>
		<spring:message code="administrator.ratio1"
			var="avgMinMaxStdevMembersPerBrotherhoodHead" />
		<jstl:out value="${avgMinMaxStdevMembersPerBrotherhoodHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevMembersPerBrotherhood">
		<tr>
			<th></th>
			<th><spring:message code="administrator.count"/></th>
			<th><spring:message code="administrator.ratio"/></th>
		</tr>
			<jstl:forEach var="datos" items="${byAreaCount}">
				<tr>
					<td><jstl:out value="${datos.key.name}"></jstl:out></td>
					<td><jstl:out value="${datos.value[0]}"></jstl:out></td>
					<td><jstl:out value="${datos.value[1]}"></jstl:out></td>
				</tr>
			</jstl:forEach>
	</table>
	
	<h4>
		<spring:message code="administrator.avgBluhOfBrosPerArea"
			var="avgMinMaxStdevMembersPerBrotherhoodHead" />
		<jstl:out value="${avgMinMaxStdevMembersPerBrotherhoodHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevMembersPerBrotherhood">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMaxStdevBrotherhoodsPerArea}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<h4>
		<spring:message code="administrator.avgBluhOfFinders"
			var="avgMinMaxStdevMembersPerBrotherhoodHead" />
		<jstl:out value="${avgMinMaxStdevMembersPerBrotherhoodHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevMembersPerBrotherhood">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMaxStdevResultsInFinders}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<h4>
		<spring:message code="administrator.ratioFinder"
			var="percentageFixUpTasksWithComplaintHead" />
		<jstl:out value="${percentageFixUpTasksWithComplaintHead}" />
		=
		<fmt:formatNumber value="${ratioEmptyFinders*100}"
			maxFractionDigits="2" />
		%
	</h4>
	
	<input type="button" value="<spring:message code="administrator.spammer.recalc" />"
		onclick="javascript: relativeRedir('/message/administrator/spammer.do');" />
	<br /><br />
	<br />
	<input type="button" value="<spring:message code="administrator.polarity" />"
		onclick="javascript: relativeRedir('/message/administrator/polarity.do');" />
	<br /><br />


<input type="button" name="cancel" value="<spring:message code="administrator.back" />" onclick="javascript: history.back()" />

<h2><spring:message code="administrator.d02.title"/></h2>

	<h4>
		<spring:message code="administrator.recordsPerHistoryStats"
			var="avgMinMaxStdevRecordsPerHistoryHead" />
		<jstl:out value="${avgMinMaxStdevRecordsPerHistoryHead}"></jstl:out>
	</h4>
	<table class="displaytag" name="avgMinMaxStdevRecordsPerHistory">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${avgMinMaxStdevRecordsPerHistory}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<h4>
		<spring:message code="administrator.largestHistory"
			var="largestHistoryHead" />
		<jstl:out value="${largestHistoryHead}"></jstl:out>
	</h4>
	<table>
	<jstl:forEach var="datos"
		items="${biggestHistory}">
	<tr>
		<td><a href = "brotherhood/show.do?id=${ datos.id }"/><jstl:out value="${datos.title}" /></a></td>
	</tr>
	</jstl:forEach>
	</table>
	
	<table class="displaytag" name="smallest">
		<tr>
			<th><spring:message
					code="administrator.largerHistory"
					var="largerHistoryHeader" /> <jstl:out value="${largerHistoryHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${biggerThanAverage}">
			<tr>
				<td><a href = "brotherhood/show.do?id=${ datos.id }"/><jstl:out value="${datos.title}" /></a></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<h4>
		<spring:message code="administrator.ratioNotCoordinated"
			var="ratioNotCoordinatedHead" />
		<jstl:out value="${ratioNotCoordinatedHead}"></jstl:out>: <jstl:out value="${ ratioNotCoordinated*100 }"/>%
	</h4>
	
	
	<h4>
		<spring:message code="administrator.statsCoordinated"
			var="statsCoordinatedHead" />
		<jstl:out value="${statsCoordinatedHead}"></jstl:out>
	</h4>
	
	<table class="displaytag" name="statsParadesPerChapter">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${statsParadesPerChapter}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<table class="displaytag" name="smallest">
		<tr>
			<th><spring:message
					code="administrator.chapters10Percent"
					var="chapters10PercentHeader" /> <jstl:out value="${chapters10PercentHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${moreThan10Percent}">
			<tr>
				<td><a href = "chapter/show.do?id=${ datos.id }"/><jstl:out value="${datos.title}" /></a></td>
			</tr>
			</jstl:forEach>

	</table>
	
	<h4>
		<spring:message code="administrator.ratioDraftFinal"
			var="ratioDraftFinalHead" />
			<jstl:out value="${ratioDraftFinalHead}"></jstl:out>: 
			<jstl:choose>
				<jstl:when test="${ empty ratioFinalMode }">
					0%
				</jstl:when>
				<jstl:when test="${ ratioFinalMode < 0 }">
					<spring:message code="administrator.inf"/>
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${ ratioFinalMode*100 }"/>%
				</jstl:otherwise>
			</jstl:choose>
	</h4>
	
	<h4> <spring:message
					code="administrator.paradesByStatus"/></h4>
	<table class="displaytag" name="paradesByStatus">
		<tr>
			<th><spring:message code="administrator.status.draft"/></th>
			<th><spring:message code="administrator.status.accepted"/></th>
			<th><spring:message code="administrator.status.rejected"/></th>
			<th><spring:message code="administrator.status.submitted"/></th>
		</tr>
			<tr>
			<jstl:forEach var="datos"
				items="${paradesByStatus}">
				<td><jstl:out value="${datos}" /></td>
			</jstl:forEach>
			</tr>
	</table>
	
	<h4>
		<spring:message code="administrator.ratioActive"
			var="ratioActiveHead" />
		<jstl:out value="${ratioActiveHead}"></jstl:out>: <jstl:out value="${ ratioActive*100 }"/>%
	</h4>
	
	<h4>
		<spring:message code="administrator.statsSponsor"
			var="statsSponsorHead" />
		<jstl:out value="${statsSponsorHead}"></jstl:out>
	</h4>
	
	<table class="displaytag" name="statsSponsor">
		<tr>
			<th><spring:message code="administrator.average"
					var="averageHeader" /> <jstl:out value="${averageHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.minimum"
					var="minimumHeader" /> <jstl:out value="${minimumHeader}"></jstl:out>
			</th>
			<th><spring:message code="administrator.maximum"
					var="maximumHeader" /><jstl:out value="${maximumHeader}"></jstl:out>
			</th>

			<th><spring:message code="administrator.standardDeviation"
					var="standardDeviationHeader" /> <jstl:out
					value="${standardDeviationHeader}"></jstl:out></th>
		</tr>
		<tr>
			<jstl:forEach var="datos" items="${statsSponsorshipsForSponsor}">
				<td><jstl:out value="${datos}"></jstl:out></td>
			</jstl:forEach>
		</tr>
	</table>
	
	<table class="displaytag" name="smallest">
		<tr>
			<th><spring:message
					code="administrator.top5Sponsors"
					var="top5SponsorsHeader" /> <jstl:out value="${top5SponsorsHeader}"></jstl:out></th>
		</tr>
			<jstl:forEach var="datos"
				items="${sponsorsActiveSponsorships}">
			<tr>
				<td><a href = "sponsor/show.do?id=${ datos.id }"/><jstl:out value="${datos.userAccount.username}" /></a></td>
			</tr>
			</jstl:forEach>

	</table>
 -->
</body>
</html>

<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Quarterly reporting" otherwise="/login.htm" redirect="/module/@MODULE_ID@/quarterlyReportController.form" />

<openmrs:htmlInclude file="/moduleResources/quarterlyreporting/style.css" />
<openmrs:htmlInclude file="/moduleResources/quarterlyreporting/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/quarterlyreporting/jquery.PrintArea.js" />
	
<script type="text/javascript" language="JavaScript">
	$(document).ready(function() {
		$("div#print_button").click(function() {
			$("div.quaterlyreportarea").printArea();
		});	
	});
</script>
	
<div class="quaterlyreportarea">

<div class="pageTitle">
<h2 style="margin-left: 70px"><spring:message code="quarterlyreporting.title"/></h2>
</div>

<br />
<form action="" method="post">
<div style="margin-left: 70px;">
<div class="boxHeader"><b><spring:message code="quarterlyreporting.select"/></b></div>

<div class="box">
<table>
	<tr>
		<td> <spring:message
	code="quarterlyreporting.quarter" /></td>
		<td><select name="quarter">
			<option>---<spring:message code="quarterlyreporting.chooseQter"/>---</option>
			<option value="01-01To 31-03">01/Jan <spring:message code="quarterlyreporting.To" /> 31/Mar</option>
			<option value="01-04To 30-06">01/<spring:message code="quarterlyreporting.april" /> <spring:message code="quarterlyreporting.To" /> 3O/<spring:message code="quarterlyreporting.jun" /></option>
			<option value="01-07To 30-09">01/<spring:message code="quarterlyreporting.jul" /> <spring:message code="quarterlyreporting.To" /> 30/Sep</option>
			<option value="01-10To 31-12">01/Oct <spring:message code="quarterlyreporting.To" /> 31/Dec</option>
		</select></td>
		<td><spring:message code="quarterlyreporting.year"/></td>
		<td><select name="year">
			<c:forEach var="annual" items="${years}" >
				<option <c:if test="${annual==currentYear}" >selected="selected"</c:if>	value="${annual}" >${annual}</option>
			</c:forEach>
		</select></td>
	</tr>
</table>


<input type="submit" value="<spring:message code="quarterlyreporting.findButton"/>">
</div>

<!--  hidden params  --> 
<input type="hidden" name="mGender" value="M" />
<input type="hidden" name="fGender" value="F" /> 

<input type="hidden" name="zeroMonthAge" value="0" /> 
<input type="hidden" name="one79MonAge" value="179" /> 
<input type="hidden" name="one80MonAge" value="180" /> 
<input type="hidden" name="elevMonAge" value="11" /> 
<input type="hidden" name="twlvMonAge" value="12" /> 
<input type="hidden" name="twenty3MonAge" value="23" /> 
<input type="hidden" name="twentyFoMonAge" value="24" />
<input type="hidden" name="fifty9MonAge" value="59" /> 
<input type="hidden" name="sixtyMonAge" value="60" /> 
<input type="hidden" name="one79MonAge" value="179" />  
	
	<c:if test="${fn:length(valuesCollection)!=0}">
	<br />
	<div id="print_button" style="cursor: http:pointer"><a><spring:message code="quarterlyreporting.printButton" /></a>
    </div>

	</c:if>
</div>	

<div class="alltables" style="margin-left: 70px">	



<c:if test="${fn:length(valuesCollection)!=0}">
<spring:message code="quarterlyreporting.qterBegin" /> &nbsp; <b> ${quarterFromDate} </b> &nbsp; <spring:message code="quarterlyreporting.qterEnding" /> &nbsp; <b>
	${quarterToDate}    </b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>Reported On ${today }</i>
<br />
<br />

<table width="1104" border="1">
	<tr>
		<td colspan="6" class="mainHeader">1.0 <spring:message code="quarterlyreporting.enrollmentAdult" /></td>
	</tr>
	<tr class="headers">
		<td width="163">&nbsp;</td>
		<td width="113"><spring:message code="quarterlyreporting.cumEnrolleeBegQter" /></td>
		<td width="127"><spring:message code="quarterlyreporting.newEnrolleesDuringQter" /></td>
		<td width="118"><spring:message code="quarterlyreporting.cumEnrolleesEndQter" /></td>
		<td width="173"><spring:message code="quarterlyreporting.totalReceivedHIVCareDuringQter" /></td>	
		<td width="173"><spring:message code="quarterlyreporting.totalReceivedCotrimoDuringQter" /></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.maleZeroTo179" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatUnder179EnrolledBuyTheBegOfQuarter">${cumMalePatUnder179EnrolledBuyTheBegOfQuarter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatUnder179EnrolledDuringTheQuarter">${cumMalePatUnder179EnrolledDuringTheQuarter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatUnder179EnrolledByTheEndOfQuarter">${cumMalePatUnder179EnrolledByTheEndOfQuarter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCareAge0To179">${malePatRecievedCareAge0To179}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCotrimo0To179">${malePatRecievedCotrimo0To179}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.maleOver180" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatOver180MonEnrolledBegOfQter">${cumMalePatOver180MonEnrolledBegOfQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatOver180EnrolledDuringQter">${cumMalePatOver180EnrolledDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatOver180EnrolledEndOfQter">${cumMalePatOver180EnrolledEndOfQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180ReceivedHIVCare">${maleOver180ReceivedHIVCare}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180ReceivedCotrimo">${maleOver180ReceivedCotrimo}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleZeroTo179" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPat0To179EnrolledTheBegOfQter">${cumFemPat0To179EnrolledTheBegOfQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPat0To179EnrolledDuringTheQuarter">${cumFemPat0To179EnrolledDuringTheQuarter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPat0To179EnrolledByTheEndOfQuarter">${cumFemPat0To179EnrolledByTheEndOfQuarter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCareAge0To179">${femalePatRecievedCareAge0To179}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCotrimo0To179">${femalePatRecievedCotrimo0To179}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleOver180" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPatOver180MonEnrolledBegOfQter">${cumFemPatOver180MonEnrolledBegOfQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPatOver180MonEnrolledDuringQter">${cumFemPatOver180MonEnrolledDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPatOver180MonEnrolledEndOfQter">${cumFemPatOver180MonEnrolledEndOfQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femaleOver180ReceivedHIVCare">${femaleOver180ReceivedHIVCare}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femaleOver180ReceivedCotrimo">${femaleOver180ReceivedCotrimo}</a></td>
	</tr>
	<tr>
		<td class="categories"><b>Total</b></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalEnrolledBegQter"><b>${totalEnrolledBegQter}</b></a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalEnrolledDuringQter"><b>${totalEnrolledDuringQter}</b></a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalEnrolledEndQter"><b>${totalEnrolledEndQter}</b></a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalPatientsRecievedCare"><b>${totalPatientsRecievedCare}</b></a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalPatientsRecievedCotrimo"><b>${totalPatientsRecievedCotrimo}</b></a></td>
	</tr>
	<tr>
		<td colspan="6" bgcolor="#CCCCCC" class="categories">&nbsp;</td>
	</tr>
	<tr>
	<td class="headers" colspan="3"><spring:message code="quarterlyreporting.eligible" /></td>
	<td colspan="3">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=eligiblePatients">${eligiblePatients}</a></td>
	</tr>
	
</table>

<br /></br>
<!--   table 2      -->

<table width="1104" border="1">
	<tr>
		<td colspan="6" class="mainHeader">1.1 <spring:message code="quarterlyreporting.enrollmentPediatric" /></td>
	</tr>
	<tr>
		<td width="163" class="categories"><spring:message code="quarterlyreporting.maleZeroToElevMonths" /> </td>
		<td width="113">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat0To11EnrolledByTheBegOfQter">${malePat0To11EnrolledByTheBegOfQter}</a></td>
		<td width="127">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat0To11EnrolledDuringTheQter">${malePat0To11EnrolledDuringTheQter}</a></td>
		<td width="118">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat0To11EnrolledByTheEndOfQter">${malePat0To11EnrolledByTheEndOfQter}</a></td>
		<td width="173">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCareAge0To11">${malePatRecievedCareAge0To11}</a></td>
		<td width="173">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCotrimo0To11">${malePatRecievedCotrimo0To11}</a></td>
	</tr>
	<tr>
		<td width="163" class="categories"><spring:message code="quarterlyreporting.male12To23Months"/> </td> 
		<td width="113">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat12To23BegQter">${malePat12To23BegQter}</a></td>
		<td width="127">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat12To23DuringQter">${malePat12To23DuringQter}</a></td>
		<td width="118">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat12To23EndQter">${malePat12To23EndQter}</a></td>
		<td width="173">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCareAge12To23">${malePatRecievedCareAge12To23}</a></td>
		<td width="173">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCotrimo12To23">${malePatRecievedCotrimo12To23}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.male24To59Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat24To59BegQter">${malePat24To59BegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat24To59DuringQter">${malePat24To59DuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat24To59EndQter">${malePat24To59EndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCareAge24To59">${malePatRecievedCareAge24To59}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCotrimo24To59">${malePatRecievedCotrimo24To59}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.male60To179Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat60To179BegQter">${malePat60To179BegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat60To179DuringTheQter">${malePat60To179DuringTheQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat60To179EndQter">${malePat60To179EndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCareAge60To179">${malePatRecievedCareAge60To179}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePatRecievedCotrimo60To179">${malePatRecievedCotrimo60To179}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleZeroToElevMonths" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat0To11EnrolledBegQter">${femalePat0To11EnrolledBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat0To11EnrolledDuringQter">${femalePat0To11EnrolledDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat0To11EnrolledEndQter">${femalePat0To11EnrolledEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCareAge0To11">${femalePatRecievedCareAge0To11}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCotrimo0To11">${femalePatRecievedCotrimo0To11}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female12To23Months"/></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat12To23EnrolledBegQter">${femalePat12To23EnrolledBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat12To23EnrolledDuringQter">${femalePat12To23EnrolledDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat12To23EnrolledEndQter">${femalePat12To23EnrolledEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCareAge12To23">${femalePatRecievedCareAge12To23}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCotrimo12To23">${femalePatRecievedCotrimo12To23}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female24To59Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat24To59EnrolledBegQter">${femalePat24To59EnrolledBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat24To59EnrolledDuringQter">${femalePat24To59EnrolledDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat24To59EnrolledEndQter">${femalePat24To59EnrolledEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCareAge24To59">${femalePatRecievedCareAge24To59}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCotrimo24To59">${femalePatRecievedCotrimo24To59}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female60To179Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat60To179EnrolledBegQter">${femalePat60To179EnrolledBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat60To179EnrolledDuringQter">${femalePat60To179EnrolledDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePat60To179EnrolledEndQter">${femalePat60To179EnrolledEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCareAge60To179">${femalePatRecievedCareAge60To179}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femalePatRecievedCotrimo60To179">${femalePatRecievedCotrimo60To179}</a></td>
	</tr>
</table>
<br />
<br />
<!-- Table 2.0    -->
<table width="1104" border="1">
	<tr>
		<td colspan="7" class="mainHeader">2.0 <spring:message code="quarterlyreporting.ARTCareAdult" />  </td>
	</tr>
	<tr>
		<td width="145" height="99">&nbsp;</td>
		<td width="149" class="headers"><spring:message code="quarterlyreporting.cumStartedARTBegQter" />
		
</td>
		<td width="146" class="headers"><spring:message code="quarterlyreporting.newAndTransfersDuringQter" />	
</td>
		<td width="103" class="headers"><spring:message code="quarterlyreporting.newOnARTEndeQter" />                             	
</td>
		<td width="109" class="headers"><spring:message code="quarterlyreporting.newOnARTDuringQter" /> </td>
		<td width="232" class="headers"><spring:message code="quarterlyreporting.transferredDuringQter" /></td>
		<td width="114" class="headers"><spring:message code="quarterlyreporting.totalOnARTEndQter" />	
</td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.maleZeroTo179" /></td>
		<td>&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePat0To179StartedARTBegOfQter">${cumMalePat0To179StartedARTBegOfQter}</a></td>
		<td>&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePat0To179StartedDuringQter">${cumMalePat0To179StartedDuringQter}</a></td>
		<td>&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePat0To179StartedEndQter">${cumMalePat0To179StartedEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To179NewOnArtDuringQterNoTransferIn">${male0To179NewOnArtDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To179TransfInDuringQter">${male0To179TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To179ActiveOnARTTotalEndQter">${male0To179ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.maleOver180" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatOver180StartedARTBegQter">${cumMalePatOver180StartedARTBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatOver180StartedARTDuringQter">${cumMalePatOver180StartedARTDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumMalePatOver80StartedARTEndQter">${cumMalePatOver80StartedARTEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180NewOnArtDuringQterNoTransferIn">${maleOver180NewOnArtDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180TransfInDuringQter">${maleOver180TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180ActiveOnARTTotalEndQter">${maleOver180ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleZeroTo179" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumfem0To179StartedARTBegQter">${cumfem0To179StartedARTBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumfem0To179StartedARTDuringQter">${cumfem0To179StartedARTDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumfemPat0To179StartedARTEndQter">${cumfemPat0To179StartedARTEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem0To179OnArtDuringQterNoTransferIn">${fem0To179OnArtDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femaleOTo179TransfInDuringQter">${femaleOTo179TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem0To179ActiveOnARTTotalEndQter">${fem0To179ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleOver180" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPatOver180StartedARTBegQter">${cumFemPatOver180StartedARTBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPatOver180StartedARTDuringQter">${cumFemPatOver180StartedARTDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPatOver180StartedARTEndQter">${cumFemPatOver180StartedARTEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femOver180NewOnARTDuringQterNoTransfIn">${femOver180NewOnARTDuringQterNoTransfIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femaleOver180TransfInDuringQter">${femaleOver180TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femOver180ActiveOnARTTotalEndQter">${femOver180ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><strong>Total</strong></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalCumBuyBegQter">${totalCumBuyBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalCumDuringQter">${totalCumDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalCumEndQter">${totalCumEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalActiveOnART">${totalActiveOnART}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=transferredInTotal">${transferredInTotal}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalAdultActiveInART">${totalAdultActiveInART}</a></td>
	</tr>
	<tr>
		<td colspan="7" bgcolor="#CCCCCC">&nbsp;</td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.pregnantFem" /></td>
		<td>&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumFemPregnantNewOnARTInTheBegOfQter">${cumFemPregnantNewOnARTInTheBegOfQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=newOnARTPlusTransInPregnantFem">${newOnARTPlusTransInPregnantFem}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=cumPregnantFemEndQter">${cumPregnantFemEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=newOnARTPregnant">${newOnARTPregnant}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=pregnantFemTransfIn">${pregnantFemTransfIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=activePregnants">${activePregnants}</a></td>
	</tr>
	<tr>
		<td height="61" colspan="4">&nbsp;</td>
		<td colspan="2" class="headers"><spring:message code="quarterlyreporting.USDFunded" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=usdFunded">${totalAdultActiveInART}</a></td></td>
	</tr>
</table>
<br />
<br />
<!--   Table 2.1  -->

<table width="1104" border="1">
	<tr>
		<td colspan="7" class="mainHeader">2.1 <spring:message code="quarterlyreporting.pediatricARTCare" /></td>
	</tr>

	<tr>
		<td width="145" class="categories"><spring:message code="quarterlyreporting.maleZeroToElevMonths" /></td>
		<td width="149">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat0To11StartedBegQter">${malePat0To11StartedBegQter}</a></td>
		<td width="146">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat0To11StartedDuringQter">${malePat0To11StartedDuringQter}</a></td>
		<td width="103">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat0To11StartedEndQter">${malePat0To11StartedEndQter}</a></td>
		<td width="109">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To11NewOnARTDuringQterNoTransferIn">${male0To11NewOnARTDuringQterNoTransferIn}</a></td>
		<td width="232">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To11TransfInDuringQter">${male0To11TransfInDuringQter}</a></td>
		<td width="114">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To11ActiveOnARTTotalEndQter">${male0To11ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td width="145" class="categories"><spring:message code="quarterlyreporting.male12To23Months" /></td>
		<td width="149">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat12To23StartedBegQter">${malePat12To23StartedBegQter}</a></td>
		<td width="146">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat12To23StartedDuringQter">${malePat12To23StartedDuringQter}</a></td>
		<td width="103">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat12To23StartedEndQter">${malePat12To23StartedEndQter}</a></td>
		<td width="109">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male12To23NewOnARTDuringQterNoTransferIn">${male12To23NewOnARTDuringQterNoTransferIn}</a></td>
		<td width="232">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male12To23TransfInDuringQter">${male12To23TransfInDuringQter}</a></td>
		<td width="114">&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male12To23ActiveOnARTTotalEndQter">${male12To23ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.male24To59Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat24To59StartedBegQter">${malePat24To59StartedBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat24To59StartedDuringQter">${malePat24To59StartedDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat24To59StartedEnd">${malePat24To59StartedEnd}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male24To59NewOnARTDuringQterNoTransferIn">${male24To59NewOnARTDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male24To59TransfInDuringQter">${male24To59TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male24To59ActiveOnARTTotalEndQter">${male24To59ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.male60To179Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat60To179StartedBegQter">${malePat60To179StartedBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat60To179StartedDuringQter">${malePat60To179StartedDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=malePat60To179StartedEndQter">${malePat60To179StartedEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male60To179NewOnARTDuringQterNoTransferIn">${male60To179NewOnARTDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male60To179TransfInDuringQter">${male60To179TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male5To14ActiveOnARTTotalEndQtermale5To14ActiveOnARTTotalEndQter">${male5To14ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleZeroToElevMonths" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat0To11StartedBegQter">${femPat0To11StartedBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat0To11StartedDuringQter">${femPat0To11StartedDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat0T11StartedEndQter">${femPat0T11StartedEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem0To11NewOnARTDuringQterNoTransferIn">${fem0To11NewOnARTDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female0To11TransfInDuringQter">${female0To11TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female0To11ActiveOnARTTotalEndQter">${female0To11ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female12To23Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat12To23StartedBegQter">${femPat12To23StartedBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat0To11StartedDuringQter">${femPat0To11StartedDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkIdfemPat12To23StartedEndQter">${femPat12To23StartedEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem12To23NewOnARTDuringQterNoTransferIn">${fem12To23NewOnARTDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female12To23TransfInDuringQter">${female12To23TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female12To23ActiveOnARTTotalEndQter">${female12To23ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female24To59Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat24To59StartedBegQter">${femPat24To59StartedBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat24To59StartedDuringQter">${femPat24To59StartedDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat24To59StartedEnd">${femPat24To59StartedEnd}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem24To59NewOnARTDuringQterNoTransferIn">${fem24To59NewOnARTDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female24To59TransfInDuringQter">${female24To59TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem24To59ActiveOnARTTotalEndQter">${fem24To59ActiveOnARTTotalEndQter}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female60To179Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat60To179StartedBegQter">${femPat60To179StartedBegQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat60To179StartedDuringQter">${femPat60To179StartedDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femPat60To179StartedEndQter">${femPat60To179StartedEndQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem60To179NewOnARTDuringQterNoTransferIn">${fem60To179NewOnARTDuringQterNoTransferIn}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female60To179TransfInDuringQter">${female60To179TransfInDuringQter}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem60To179ActiveOnARTTotalEndQter">${fem60To179ActiveOnARTTotalEndQter}</a></td>
	</tr>
</table>

<br />
<!--   end    -->
<table width="1104" border="1">
	<tr>
		<td colspan="6" class="mainHeader"><strong>3.0 <spring:message code="quarterlyreporting.ARTCareFollowUpAdult" />												
</strong></td>
	</tr>
	<tr style="width: 1104">
		<td width="186" class="headers"></td>
		<td width="179" class="headers"><strong><spring:message code="quarterlyreporting.stoppedART" /></strong></td>
		<td width="152" class="headers"><strong><spring:message code="quarterlyreporting.transOut" /> </strong></td>
		<td width="103" class="headers"><strong><spring:message code="quarterlyreporting.death" /></strong></td>
		<td width="149" class="headers"><strong><spring:message code="quarterlyreporting.lost" /></strong></td>
		<td width="187" class="headers"><strong>Total</strong></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.maleZeroTo179" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To179StoppedART">${male0To179StoppedART}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0to179TransfOut">${male0to179TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0to179Died">${male0to179Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To179Lost">${male0To179Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To179NotOnARTTotal">${male0To179NotOnARTTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.maleOver180" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180Stopped">${maleOver180Stopped}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180TransOut">${maleOver180TransOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180Died">${maleOver180Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180Lost">${maleOver180Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=maleOver180NotOnARTTotal">${maleOver180NotOnARTTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleZeroTo179" /> </td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female0To179StoppedART">${female0To179StoppedART}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fema0to179TransfOut">${fema0to179TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem0to179Died">${fem0to179Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem0to179Lost">${fem0to179Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female0To179NotOnARTTotal">${female0To179NotOnARTTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleOver180" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femaleOver180Stopped">${femaleOver180Stopped}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femOver180TransOut">${femOver180TransOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femOver180Died">${femOver180Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femOver180Lost">${femOver180Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=femOver180NotOnARTTotal">${femOver180NotOnARTTotal}</a></td>
	</tr>
	<tr>
		<td class="headers"><strong>Total</strong></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalStoppedART"><strong>${totalStoppedART}</strong></a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalTransfOut"><strong>${totalTransfOut}</strong></a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalDied"><strong>${totalDied}</strong></a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totalLost"><strong>${totalLost}</strong></a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=bigTotal"><strong>${bigTotal}</strong></a></td>
	</tr>
	<tr>
		<td colspan="6">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="6" class="mainHeader"><strong>3.1 <spring:message code="quarterlyreporting.ARTCareFollowUpPed" />   </strong></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.maleZeroToElevMonths" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To11StoppedARV">${male0To11StoppedARV}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0to11TransfOut">${male0to11TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To11Died">${male0To11Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To11Lost">${male0To11Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male0To11NotOnARVTotal">${male0To11NotOnARVTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.male12To23Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male12To23StoppedARV">${male12To23StoppedARV}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male12to23TransfOut">${male12to23TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male12To23Died">${male12To23Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male12To23Lost">${male12To23Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male12To23NotOnARVTotal">${male12To23NotOnARVTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.male24To59Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male24To59StoppedARV">${male24To59StoppedARV}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male24to59TransfOut">${male24to59TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male24To59Died">${male24To59Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male24To59Lost">${male24To59Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male24To59NotOnARVTTotal">${male24To59NotOnARVTTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.male60To179Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male60To179StoppedARV">${male60To179StoppedARV}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male60to179TransfOut">${male60to179TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male60To179Died">${male60To179Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male60To179Lost">${male60To179Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=male60To179NotOnARVTTotal">${male60To179NotOnARVTTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.femaleZeroToElevMonths" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female0To11StopARV">${female0To11StopARV}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem0to11TransfOut">${fem0to11TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem0To11Died">${fem0To11Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem0To11Lost">${fem0To11Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female0To11NotOnARVTotal">${female0To11NotOnARVTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female12To23Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female12To23StoppedARV">${female12To23StoppedARV}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem12to23TransfOut">${fem12to23TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem12To23Died">${fem12To23Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem12To23Lost">${fem12To23Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female12To23NotOnARVTotal">${female12To23NotOnARVTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female24To59Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem24To59StopARV">${fem24To59StopARV}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem24to59TransfOut">${fem24to59TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem24To59Died">${fem24To59Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem24To59Lost">${fem24To59Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female24To59NotOnARVTotal">${female24To59NotOnARVTotal}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.female60To179Months" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem60To179StopARV">${fem60To179StopARV}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem60to179TransfOut">${fem60to179TransfOut}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem60To179Died">${fem60To179Died}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=fem60To179Lost">${fem60To179Lost}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=female60To179NotOnARVTotal">${female60To179NotOnARVTotal}</a></td>
	</tr>
</table>


<br />
<br />
<br />
<br />
<br />
<br />
<br />
<br />
<br />
<br />
<br />
<!--   table 4  -->

<table width="1104" border="1">
	<tr>
		<td colspan="3" class="mainHeader">4.1 <spring:message code="quarterlyreporting.adherenceSix" /></td>
		<td colspan="3" class="mainHeader">4.2 <spring:message code="quarterlyreporting.adherenceTwelve" /></td>
	</tr>
	<tr>
		<td width="245" bgcolor="#CCCCCC">&nbsp;</td>
		<td width="97" class="headers"><spring:message code="quarterlyreporting.baseline" /></td>
		<td width="122" class="headers">6 <spring:message code="quarterlyreporting.months" /></td>
		<td width="278" bgcolor="#CCCCCC">&nbsp;</td>
		<td width="100" class="headers"><spring:message code="quarterlyreporting.baseline" /></td>
		<td width="119" class="headers">12 <spring:message code="quarterlyreporting.months" /></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.monthWhenCohortStarted" /></td>
		<td class="categories">&nbsp;${monthsWhenCohortStartedARTSix}</td>
		<td bgcolor="#CCCCCC">&nbsp;</td>
		<td class="categories"><spring:message code="quarterlyreporting.monthWhenCohortStarted" /></td>
		<td class="categories">&nbsp;${monthsWhenCohortStartedARTTwelve}</td>
		<td bgcolor="#CCCCCC">&nbsp;</td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.numberInCohort" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=patientsInCohortBaselineSix">${patientsInCohortBaselineSix}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=patientsInCohortAt6thMonth">${patientsInCohortAt6thMonth}</a></td>
		<td class="categories"><spring:message code="quarterlyreporting.numberInCohort" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=patientsInCohortBaselineTwelve">${patientsInCohortBaselineTwelve}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=patientsInCohortAt12thMonth">${patientsInCohortAt12thMonth}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.whoHaveCD4Count" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=patients6MonthsAgoWithCD4BaselineValue">${patients6MonthsAgoWithCD4BaselineValue}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=patientsAt6thMonthWithCD4">${patientsAt6thMonthWithCD4}</a></td>
		<td class="categories"><spring:message code="quarterlyreporting.whoHaveCD4Count" /></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=patients12MonthsAgoWithCD4BaselineValue">${patients12MonthsAgoWithCD4BaselineValue}</a></td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=patientsAt12thMonthWithCD4">${patientsAt12thMonthWithCD4}</a></td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.median" /></td>
		<td>&nbsp;${medianBaselineSixMonth}</td>
		<td>&nbsp;${medianSixthMonth}</td>
		<td class="categories"><spring:message code="quarterlyreporting.median" /></td>
		<td>&nbsp;${medianBaseline12Months}</td>
		<td>&nbsp;${medianAt12thMonth}</td>
	</tr>
	<tr>
		<td class="categories"><spring:message code="quarterlyreporting.whoReceivedARVSixOutOfSix" /></td>
		<td bgcolor="#CCCCCC">&nbsp;</td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=nberPatReceivedARVsFor6OutOf6Month">${nberPatReceivedARVsFor6OutOf6Month}</a></td>
		<td class="categories"><spring:message code="quarterlyreporting.whoReceivedARVTwelveOutOfTw" /></td>
		<td bgcolor="#CCCCCC">&nbsp;</td>
		<td>&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=nberPatReceivedARVsFor12OutOf12Month">${nberPatReceivedARVsFor12OutOf12Month}</a></td>
	</tr>
</table>

<p>&nbsp;</p>
<!--   TABLE 5   -->
<table width="1104" border="1">
	<tr>
		<td colspan="8" class="mainHeader">&nbsp;<strong>5. <spring:message code="quarterlyreporting.patientsAndRegimens" /> </strong></td>
	</tr>
	<tr>
		<td width="186" class="headers"><strong><spring:message code="quarterlyreporting.regimens" /></strong></td>
		<td width="69" class="headers"><strong>0-11</strong></td>
		<td width="62" class="headers"><strong>12-23</strong></td>
		<td width="79" class="headers"><strong>24-59</strong></td>
		<td width="158" class="headers"><strong>60-179 </strong></td>
		<td width="158" class="headers"><strong>Total 0-179 </strong></td>
		<td width="261" class="headers"><strong>+180</strong></td>
		<td width="176" class="headers"><strong style="color:  red">TOTAL</strong></td>
	</tr>

		<c:forEach var="obj" items="${objects}" varStatus="status" >
				<tr>
					<td width="190" class="headers">  &nbsp;&nbsp;${obj[0]}</td>
					<td width="190" >&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?index=position_${status.count},0">${obj[1]}</a></td>
					<td width="190" >&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?index=position_${status.count},1">${obj[2]}</a></td>
					<td width="190" >&nbsp;<a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?index=position_${status.count},2">${obj[3]}</a></td>
					<td width="190" >&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?index=position_${status.count},3">${obj[4]}</a></td>
					<td width="190" >&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?index=position_${status.count},4">${obj[5]}</a></td>
					<td width="190" >&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?index=position_${status.count},5">${obj[6]}</a></td>
					<td width="190" >&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?index=position_${status.count},6"><strong>${obj[7]}</strong></a></td>
				</tr>
	</c:forEach>
	
	<tr>
	                <td width="190" class="headers" style="color:  red">  &nbsp;&nbsp;TOTAL</td>
					<td width="190" >&nbsp; <a href="#"><strong>${total0To11 }</strong></a></td>
					<td width="190" >&nbsp; <a href="#"><strong>${total12To23 }</strong></a></td>
					<td width="190" >&nbsp;<a href="#"><strong>${total24To59 }</strong></a></td>
					<td width="190" >&nbsp; <a href="#"><strong>${total60To179 }</strong></a></td>
					<td width="190" >&nbsp; <a href="#"><strong>${total0To179 }</strong></a></td>
					<td width="190" >&nbsp; <a href="#"><strong>${total180plus }</strong></a></td>
					<td width="190" >&nbsp; <a href="${pageContext.request.contextPath}/module/quarterlyreporting/viewPatientsController.form?linkId=totOnReg"><strong>${totalTotal }</strong></a></td>
	</tr>
	
</table>
</c:if>
</div>

</form>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>
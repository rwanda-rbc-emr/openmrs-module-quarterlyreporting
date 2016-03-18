<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ taglib prefix="fn" uri="/WEB-INF/taglibs/fn.tld"%>

<openmrs:htmlInclude file="/moduleResources/quarterlyreporting/jquery.js" />
<script type="text/javascript" charset="utf-8">
		$(document).ready(function() {
			$('#example').dataTable( {
				"sPaginationType": "full_numbers"
			} );
		} );
</script>

<openmrs:htmlInclude
	file="/moduleResources/quarterlyreporting/jquery.dataTables.js" />

<openmrs:htmlInclude
	file="/moduleResources/quarterlyreporting/demo_page.css" />

<openmrs:htmlInclude
	file="/moduleResources/quarterlyreporting/demo_table.css" />
	<br />
	
	
	<div id="openmrs_msg">${title} : <b>${objectsSize}</b></div>
	
	<br />

	
	<form action="exportController.form" method="post">
	

	<openmrs:hasPrivilege privilege="Export Collective Patient Data">
		<input type="submit" value="Excel"/>
	</openmrs:hasPrivilege>
	
	
	</form>
	
	
	<table cellpadding="0" cellspacing="0" border="0" class="display"
		id="example">
		<thead>

			<tr>
				 <th>IDENTIFIER</th>
				 <openmrs:hasPrivilege privilege="View Patient Names">
				 <th>GIVEN NAME</th>
				 <th>FAMILY NAME</th>
				 </openmrs:hasPrivilege>
				 <th>AGE(Today)</th>
				 <th>GENDER</th>
				 <th>${columnTitle }</th>
				 <th>${cd4Title}</th>
				 <th></th>

			</tr>
		</thead>
		<tbody>
			<c:forEach var="obj" items="${objects}" varStatus="status">
			  <tr>
					<td>&nbsp; ${obj[0].patientIdentifier}</td>
					<openmrs:hasPrivilege privilege="View Patient Names">
					<td>&nbsp; ${obj[0].givenName}</td>
					<td>&nbsp; ${ obj[0].familyName}</td>
					</openmrs:hasPrivilege>
					<td>&nbsp; ${obj[0].age}</td>
					<td>&nbsp; <img	src="${pageContext.request.contextPath}/images/${obj[0].gender == 'M' ? 'male' : 'female'}.gif" /></td>
					<td>&nbsp;${obj[1] }</td>
					<td>&nbsp;${obj[2]}|${obj[3] }</td>
					<td>&nbsp;<a
						href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${obj[0].patientId}">View
					Dashboard</a></td>
				</tr>
				
			</c:forEach>
		</tbody>
	</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>

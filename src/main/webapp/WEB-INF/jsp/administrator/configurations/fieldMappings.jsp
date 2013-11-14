
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="main clearfix" role="main">

	<div class="col-md-12">
	<c:choose>
		<c:when test="${not empty savedStatus}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose>
					<c:when test="${savedStatus == 'updated'}">The field mappings have been successfully updated!</c:when>
				</c:choose>
			</div>
		</c:when>
	</c:choose>
	</div>

	<div class="col-md-6">
	
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Uploaded File Fields</h3>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<input type="button" id="meetsStandard" class="btn btn-primary" value="Meets Standard"/>
				</div>
				<div class="form-container scrollable">
					<form:form id="formFields" modelAttribute="transportDetails" method="post" role="form">
						<input type="hidden" id="action" name="action" value="save" />
						<table class="table table-striped table-hover table-default">
							<thead>
								<tr>
									<th scope="col" class="center-text">Field No</th>
									<th scope="col">Field Name</th>
									<th scope="col" class="center-text">Required</th>
									<th scope="col" class="center-text">Matching Field</th>
								</tr>
							</thead>
							<tbody>
								 <c:forEach var="i" begin="1" end="4">
								 	 <tr>
								 	 	<td colspan="4"><strong>Bucket ${i} <c:choose><c:when test="${i==1}"> (Sender Information)</c:when><c:when test="${i==2}"> (Recipient Information)</c:when><c:when test="${i==3}"> (Patient Information)</c:when><c:when test="${i==4}"> (Other)</c:when></c:choose></strong></td>
								 	 </tr>
									 <c:forEach items="${transportDetails.fields}" var="mappings" varStatus="field">
									 	<c:if test="${mappings.bucketNo == i}">
									 		<input type="hidden" name="fields[${field.index}].id" value="${mappings.id}" />
											<input type="hidden" name="fields[${field.index}].configId" value="${mappings.configId}" />
											<input type="hidden" name="fields[${field.index}].fieldNo" value="${mappings.fieldNo}" />
											<input type="hidden" name="fields[${field.index}].bucketNo" value="${mappings.bucketNo}" />
											<input type="hidden" name="fields[${field.index}].fieldDesc" value="${mappings.fieldDesc}" />
											<input type="hidden" name="fields[${field.index}].fieldLabel" value="${mappings.fieldLabel}" />
											<input type="hidden" name="fields[${field.index}].useField" value="${mappings.useField}" />
											<input type="hidden" name="fields[${field.index}].required" value="${mappings.required}" />
											<input type="hidden" name="fields[${field.index}].bucketDspPos" value="${mappings.bucketDspPos}" />
											<input type="hidden" name="fields[${field.index}].validationType" value="${mappings.validationType}" />
										 	<tr class="uFieldRow" rel="${mappings.fieldNo}">
										 		<td scope="row" class="center-text">${mappings.fieldNo}</td>
										 		<td>${mappings.fieldDesc}</td>
										 		<td class="center-text">
										 			<input type="checkbox" disabled="disabled" <c:if test="${mappings.required == true}">checked</c:if>  />
										 		</td>
										 		<td class="center-text">
										 			<select name="fields[${field.index}].messageTypeFieldId" id="matchingField_${mappings.fieldNo}">
										 				<option value="0">-</option>
										 				<c:forEach var="tField" items="${templateFields}">
										 					<option value="${tField.id}" <c:if test="${mappings.messageTypeFieldId == tField.id}">selected</c:if>>${tField.fieldNo}</option>
										 				</c:forEach>
										 			</select>
										 		</td>
										 	</tr>
									 	</c:if>
									 </c:forEach>
								 </c:forEach>
							</tbody>
						</table>
					</form:form>
				</div>
			</div>
		</section>
	</div>
	
	<div class="col-md-6">
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Selected Message Template Fields</h3>
			</div>
			<div class="panel-body">
				<div class="form-container scrollable" style="padding-top:46px;">
					<table class="table table-striped table-hover table-default">
						<thead>
							<tr>
								<th scope="col" class="center-text">Field No</th>
								<th scope="col">Field Name</th>
								<th scope="col" class="center-text">Required</th>
							</tr>
						</thead>
						<tbody>
							 <c:forEach var="i" begin="1" end="4">
							 	 <tr>
							 	 	<td colspan="3"><strong>Bucket ${i} <c:choose><c:when test="${i==1}"> (Sender Information)</c:when><c:when test="${i==2}"> (Recipient Information)</c:when><c:when test="${i==3}"> (Patient Information)</c:when><c:when test="${i==4}"> (Other)</c:when></c:choose></strong></td>
							 	 </tr>
								 <c:forEach var="tField" items="${templateFields}">
								 	<c:if test="${tField.bucketNo == i}">
								 	<tr>
								 		<td scope="row" class="center-text">${tField.fieldNo}</td>
								 		<td>${tField.fieldDesc}</td>
								 		<td class="center-text">
								 			<input type="checkbox" disabled="disabled" <c:if test="${tField.required == true}">checked</c:if>  />
								 		</td>
								 	</tr>
								 	</c:if>
								 </c:forEach>
							 </c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</section>
	</div>
</div>


<script type="text/javascript">

	$(document).ready(function() {
	    $("input:text,form").attr("autocomplete","off");
	});

	$(function() {
		//Fade out the updated/created message after being displayed.
		if($('.alert').length >0) {
			$('.alert').delay(2000).fadeOut(1000);
		}

		//This function will save the messgae type field mappings
		$('#saveDetails').click(function(event) {
			$('#action').val('save');
			
			//Need to make sure all required fields are marked if empty.
			var hasErrors = 0;
			//hasErrors = checkFormFields();
			
			if(hasErrors == 0) {
				$('#formFields').attr('action','saveFields');
				$('#formFields').submit();
			}
		});

		$('#next').click(function(event) {
			$('#action').val('next');
			
			var hasErrors = 0;
			//hasErrors = checkFormFields();
			
			if(hasErrors == 0) {
				$('#formFields').attr('action','saveFields');
				$('#formFields').submit();
			}
		});


		//Clicking the "Meets Standard" button will preselect the matching
		//field select box to the appropiate template field
		$(document).on('click', '#meetsStandard',function() {
			var fieldNo = null;
			
			$('.uFieldRow').each(function() {
				fieldNo = $(this).attr('rel');
				$('#matchingField_'+fieldNo+' > option').each(function() {
					if($(this).text() == fieldNo) {
						$('#matchingField_'+fieldNo).val($(this).val());
					}
				});
			});

			$(this).val("Clear Fields")
			$(this).attr('id','clearFields');
		});

		//Clicking the "Clear Fields" button will unselect the matching
		//field select box.
		$(document).on('click', '#clearFields',function() {
			$('.uFieldRow').each(function() {
				fieldNo = $(this).attr('rel');
				$('#matchingField_'+fieldNo).val("");
			});

			$(this).val("Meets Standard")
			$(this).attr('id','meetsStandard');
		});
		
	});

	function removeVariableFromURL(url_string, variable_name) {
		var URL = String(url_string);
	    var regex = new RegExp( "\\?" + variable_name + "=[^&]*&?", "gi");
	    URL = URL.replace(regex,'?');
	    regex = new RegExp( "\\&" + variable_name + "=[^&]*&?", "gi");
	    URL = URL.replace(regex,'&');
	    URL = URL.replace(/(\?|&)$/,'');
	    regex = null;
	    return URL;
	}

</script>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">
	<div class="row-fluid">
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
			<section class="panel panel-default">
				<div class="panel-heading">
                    <h3 class="panel-title">Choose a Transport Method</h3>
                </div>
				<div class="panel-body basic-clearfix">
					<div class="form-inline">
						<div id="transportMethodDiv" class="form-group half mb0 ${status.error ? 'has-error' : '' }">
                             <label class="sr-only" for="transportMethod">Transport Method *</label>
                             <select id="transportMethod" class="form-control">
                                  <option value="">- Select -</option>
                                  <c:forEach items="${transportMethods}" var="transMethod" varStatus="tStatus">
                                       <c:if test="${availTransportMethods.contains(transportMethods[tStatus.index][0])}">
                                      	 <option value="${transportMethods[tStatus.index][0]}" <c:if test="${selTransportMethod == transportMethods[tStatus.index][0]}">selected</c:if>>${transportMethods[tStatus.index][1]} </option>
                                       </c:if>
                                  </c:forEach>
                             </select>
                             <span id="transportMethodMsg" class="control-label"></span>
                        </div>
						<button class="btn btn-primary changeTransportMethod">Go</button>
					</div>
				</div>
			</section>
		</div>
	</div>
	<div class="row-fluid">
		<div class="col-md-6">
			<section class="panel panel-default">
				<div class="panel-heading">
					 <div class="pull-right">
	                    <a class="btn btn-primary btn-xs  btn-action" id="meetsStandard" data-toggle="tooltip" title="This describes what  does"><span class="glyphicon glyphicon-ok"></span> Meets Standard</a>
					</div>
					<h3 class="panel-title">Uploaded File Fields</h3>
				</div>
				<div class="panel-body">
					<div class="form-container scrollable">
						<form:form id="formFields" modelAttribute="transportDetails" method="post" role="form">
							<input type="hidden" id="action" name="action" value="save" />
							<input type="hidden" id="seltransportMethod" name="transportMethod" value="0" />
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
											 	<tr class="uFieldRow" rel="${mappings.fieldNo}">
											 		<td scope="row" class="center-text">
											 			<input type="hidden" name="fields[${field.index}].id" value="${mappings.id}" />
														<input type="hidden" name="fields[${field.index}].configId" value="${mappings.configId}" />
														<input type="hidden" name="fields[${field.index}].transportDetailId" value="${mappings.transportDetailId}" />
														<input type="hidden" name="fields[${field.index}].fieldNo" value="${mappings.fieldNo}" />
														<input type="hidden" name="fields[${field.index}].bucketNo" value="${mappings.bucketNo}" />
														<input type="hidden" name="fields[${field.index}].fieldDesc" value="${mappings.fieldDesc}" />
														<input type="hidden" name="fields[${field.index}].fieldLabel" value="${mappings.fieldLabel}" />
														<input type="hidden" name="fields[${field.index}].useField" value="${mappings.useField}" />
														<input type="hidden" name="fields[${field.index}].required" value="${mappings.required}" />
														<input type="hidden" name="fields[${field.index}].bucketDspPos" value="${mappings.bucketDspPos}" />
														<input type="hidden" name="fields[${field.index}].validationType" value="${mappings.validationType}" />
											 			${mappings.fieldNo}
											 		</td>
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
					<div class="form-container scrollable">
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

		//function that will get the field mappings for the selected transport method
		$('.changeTransportMethod').click(function() {
			var selTransportMethod = $('#transportMethod').val();

			if(selTransportMethod == "") {
				$('#transportMethodDiv').addClass("has-error");
			}
			else {
				window.location.href='mappings?i='+selTransportMethod;
			}
		});
		

		//This function will save the messgae type field mappings
		$('#saveDetails').click(function(event) {
			$('#action').val('save');
			$('#seltransportMethod').val($('#transportMethod').val());
			
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

			$(this).val("Clear Fields");
			$(this).attr('id','clearFields');
		});

		//Clicking the "Clear Fields" button will unselect the matching
		//field select box.
		$(document).on('click', '#clearFields',function() {
			$('.uFieldRow').each(function() {
				fieldNo = $(this).attr('rel');
				$('#matchingField_'+fieldNo).val("0");
			});

			$(this).val("Meets Standard");
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
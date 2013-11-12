<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Page variable to hold what dsp pos value to show -->
<c:set var="endDspPostLoop" value="1" />

<!-- Need to calculate how many fields for current section -->
<c:set var="totalFieldsBucket1" value="0" />
<c:set var="totalFieldsBucket2" value="0" />
<c:set var="totalFieldsBucket3" value="0" />
<c:set var="totalFieldsBucket4" value="0" />
<c:forEach items="${transportDetails.fields}" var="mappings" varStatus="field">
	<c:if test="${mappings.bucketNo == 1}">
		<c:set var="totalFieldsBucket1" value="${totalFieldsBucket1+1}" />
	</c:if>
	<c:if test="${mappings.bucketNo == 2}">
		<c:set var="totalFieldsBucket2" value="${totalFieldsBucket2+1}" />
	</c:if>
	<c:if test="${mappings.bucketNo == 3}">
		<c:set var="totalFieldsBucket3" value="${totalFieldsBucket3+1}" />
	</c:if>
	<c:if test="${mappings.bucketNo == 4}">
		<c:set var="totalFieldsBucket4" value="${totalFieldsBucket4+1}" />
	</c:if>
</c:forEach>

<div class="main clearfix" role="main">

	<div class="col-md-12">
		<c:if test="${not empty savedStatus}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose><c:when test="${savedStatus == 'updated'}">The configuration fields have been successfully updated!</c:when></c:choose>
			</div>
		</c:if>
		<div class="alert alert-danger" style="display:none;"><strong>Error!</strong> At least one field must be checked!</div>
		
		<div class="form-group">
			<input type="button" id="selectAllFields" class="btn btn-primary" value="Use All Fields"/>
		</div>
		
		<form:form id="formFields" modelAttribute="transportDetails" method="post" role="form">
			<input type="hidden" id="action" name="action" value="save" />
			<c:forEach var="i" begin="1" end="4">	
			<section class="panel panel-default">
				<div class="panel-heading">
					<dt>
						<dd>
							<strong>Bucket ${i} <c:choose><c:when test="${i==1}">(Sender Information)</c:when><c:when test="${i==2}">(Recipient Information)</c:when><c:when test="${i==3}">(Patient Information)</c:when><c:when test="${i==4}">(Other)</c:when></c:choose></strong>
						</dd>
					</dt>
				</div>
				<div class="panel-body">
					<div class="form-container scrollable">
						<table class="table table-striped table-hover bucketTable_${i}">
							<thead>
								<tr>
									<th scope="col" style="width:100px; min-width:100px" class="center-text">Use This Field</th>
									<th scope="col" style="width:150px; min-width:150px">Display POS</th>
									<th scope="col" style="width:200px; min-width:200px;">Field Name</th>
									<th scope="col" style="width:300px; min-width:300px;">Field Label *</th>
									<th scope="col" style="width:100px; min-width:100px;" class="center-text">Required</th>
									<th scope="col" style="width:150px; min-width:150px;">Validation Type</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${transportDetails.fields}" var="mappings" varStatus="field">
									<c:if test="${mappings.bucketNo == i}">
										<tr>
											<td scope="row" class="center-text">
												<input type="hidden" name="fields[${field.index}].id" value="${mappings.id}" />
												<input type="hidden" class="configId" name="fields[${field.index}].configId" value="${mappings.configId}" />
												<input type="hidden" class="fieldNo" name="fields[${field.index}].fieldNo" value="${mappings.fieldNo}" />
												<input type="hidden" name="fields[${field.index}].bucketNo" value="${mappings.bucketNo}" />
												<input type="hidden" name="fields[${field.index}].messageTypeFieldId" value="${mappings.messageTypeFieldId}" />
												<input type="checkbox" class="useFields" name="fields[${field.index}].useField" value="true" <c:if test="${mappings.useField == true}">checked</c:if>  />
											</td>
											<td >
												<c:if test="${mappings.bucketNo == 1}">
													<c:set var="endDspPostLoop" value="${totalFieldsBucket1}" />
												</c:if>
												<c:if test="${mappings.bucketNo == 2}">
													<c:set var="endDspPostLoop" value="${totalFieldsBucket2}" />
												</c:if>
												<c:if test="${mappings.bucketNo == 3}">
													<c:set var="endDspPostLoop" value="${totalFieldsBucket3}" />
												</c:if>
												<c:if test="${mappings.bucketNo == 4}">
													<c:set var="endDspPostLoop" value="${totalFieldsBucket4}" />
												</c:if>
												<input type="hidden" id="endDspPostLoop_${mappings.bucketNo}" value="${endDspPostLoop}" />
												<select rel="${mappings.bucketNo}" rel2="${mappings.bucketDspPos}" name="fields[${field.index}].bucketDspPos" class="form-control half dspPos dspPos_${mappings.bucketNo}">
												     <option value="" label=" - Select - " ></option>
												     <c:forEach begin="1" end="${endDspPostLoop}" var="t">
												     	<option value="${t}" <c:if test="${mappings.bucketDspPos == t}">selected</c:if>>${t}</option>
												     </c:forEach>
												</select>
											</td>
											<td>
												<input type="hidden" name="fields[${field.index}].fieldDesc" value="${mappings.fieldDesc}" />
												${mappings.fieldDesc}
											</td>
											<td>
												<div id="fieldLabel_${field.index}" class="form-group ${status.error ? 'has-error' : '' }">
													<input type="text" name="fields[${field.index}].fieldLabel"  value="${mappings.fieldLabel}" rel="${field.index}" class="form-control fieldLabel" />
													<span id="fieldLabelMsg_${field.index}" class="control-label"></span>
												</div>
											</td>
											<td class="center-text">
												<input type="checkbox" name="fields[${field.index}].required" value="true" <c:if test="${mappings.required == true}">checked</c:if>  />
											</td>
											<td class="validationTypes">
												<select name="fields[${field.index}].validationType" class="form-control half">
												     <option value="0" label=" - Select - " ></option>
												     <c:forEach items="${validationTypes}"  var="fieldvalidationtypes" varStatus="vtype">
												          <option value="${validationTypes[vtype.index][0]}" <c:if test="${mappings.validationType == validationTypes[vtype.index][0]}">selected</c:if>>${validationTypes[vtype.index][1]}</option>
													 </c:forEach>
												</select>
											</td>
										</tr>
									</c:if>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</section>
			</c:forEach>
		</form:form>
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
		};

		//Function that will check off all 'use field' checkboxes
		$(document).on('click', '#selectAllFields',function() {
			$('.useFields').attr('checked', true);
			$(this).attr('id','unselectAllFields');
			$(this).attr('value','Unselect All Fields');
		});

		//Function that will uncheck off all 'use field' checkboxes
		$(document).on('click', '#unselectAllFields',function() {
			$('.useFields').attr('checked', false);
			$(this).attr('id','selectAllFields');
			$(this).attr('value','Use All Fields');
		});

		
		//Function that will handle changing a display position and
		//making sure another field in the same bucket does not have
		//the same position selected. It will swap display position
		//values with the requested position.
		$('.dspPos').change(function() {
			//Store the current position
			var currDspPos = $(this).attr('rel2');
			var bucketVal = $(this).attr('rel');
			var newDspPos = $(this).val();

			$('.dspPos_'+bucketVal).each(function() {
				if($(this).val() == newDspPos) {
					$(this).val(currDspPos);
					$(this).attr('rel2',currDspPos);
				}
			});

			$(this).val(newDspPos);
			$(this).attr('rel2',newDspPos);
			
		});


		//This function will save the messgae type field mappings
		$('#saveDetails').click(function(event) {
			$('#action').val('save');
			
			//Need to make sure all required fields are marked if empty.
			var hasErrors = 0;
			hasErrors = checkFormFields();
			
			if(hasErrors == 0) {
				$('#formFields').attr('action','saveFields');
				$('#formFields').submit();
			}
		});

		$('#next').click(function(event) {
			$('#action').val('next');
			
			var hasErrors = 0;
			hasErrors = checkFormFields();
			
			if(hasErrors == 0) {
				$('#formFields').attr('action','saveFields');
				$('#formFields').submit();
			}
		});
	});

	function checkFormFields() {
		var errorsFound = 0;

		var row = 0;
		var usedFields = 0;

		$('.alert-danger').hide();

		//Check field labels
		$('.fieldLabel').each(function() {
			row = $(this).attr('rel');
			$('#fieldLabel_'+row).removeClass("has-error");
			$('#fieldLabelMsg_'+row).html("");
			if($(this).val() == '') {
				$('#fieldLabel_'+row).addClass("has-error");
				$('#fieldLabelMsg_'+row).html("The field label is required!");

				errorsFound = 1;
			}
		}); 

		if($('.useFields:checked').length > 0) {
			usedFields = 1;
		};

		if(usedFields == 0) {
			$('.alert-danger').show();
			errorsFound = 1;
		}

		return errorsFound;
	}

</script>
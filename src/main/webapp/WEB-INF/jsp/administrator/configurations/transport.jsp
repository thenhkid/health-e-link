
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">

	<div class="col-md-12">
	
		<c:if test="${not empty savedStatus}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose><c:when test="${savedStatus == 'updated'}">The configuration transport details have been successfully updated!</c:when></c:choose>
			</div>
		</c:if>
	
		<form:form id="transport" modelAttribute="transportDetails" method="post" enctype="multipart/form-data" role="form">
			<input type="hidden" id="action" name="action" value="save" />
			<form:hidden path="id" id="id" />
			<form:hidden path="configId" id="configId" />
			<input type="hidden" id="clearFields" name="clearFields" value="0" />
			<form:hidden path="fileName" id="currFile" />
			
			<section class="panel panel-default">
				
				<div class="panel-heading">
					<h3 class="panel-title">Transport Details</h3>
				</div>
				<div class="panel-body">
					<spring:bind path="transportMethod">
						<div id="transportMethodDiv" class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="name">Transport Method *</label>
							<form:select path="transportMethod" id="transportMethod" class="form-control half">
								<option value="">- Select -</option>
								<c:forEach items="${transportMethods}" var="transMethod" varStatus="tStatus">
									<option value="${transportMethods[tStatus.index][0]}" <c:if test="${transportDetails.transportMethod == transportMethods[tStatus.index][0]}">selected</c:if>>${transportMethods[tStatus.index][1]} </option>
								</c:forEach>
							</form:select>
							<span id="transportMethodMsg" class="control-label"></span>
						</div>
					</spring:bind>
					
					<div id="fileInfo" style="display:${transportDetails.transportMethod == 1 || transportDetails.transportMethod == 3 ? 'block' : 'none'}">
						<c:if test="${not empty transportDetails.fileName}">
							<div class="form-group">
								<label class="control-label">Current File</label>
								<div class="control-label">${transportDetails.fileName}</div>
							</div>
						</c:if>
						<spring:bind path="file">
							<div id="templateFileDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="templateFile"><c:if test="${not empty transportDetails.fileName}">New</c:if> Template File *</label>
								<form:input path="file" id="templateFile" type="file" />
								<span id="templateFileMsg" class="control-label"></span>
							</div>
						</spring:bind>
						<spring:bind path="messageTypeColNo">
							<div id="messageTypeColNoDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="messageTypeColNo">Field No. that contains the message type</label>
								<form:input path="messageTypeColNo" id="messageTypeColNo" class="form-control" type="text" maxLength="4" />
								<span id="messageTypeColNoMsg" class="control-label"></span>
							</div>
						</spring:bind>
						<spring:bind path="messageTypeCustomVal">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="messageTypeCustomVal">Custom Message Type Value for this Configuration</label>
								<form:input path="messageTypeCustomVal" id="messageTypeCustomVal" class="form-control" type="text" maxLength="45" />
								<form:errors path="messageTypeCustomVal" cssClass="control-label" element="label" />
							</div>
						</spring:bind>
						<spring:bind path="targetOrgColNo">
							<div id="targetOrgColNoDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="targetOrgColNo">Field No. that contains the target organization </label>
								<form:input path="targetOrgColNo" id="targetOrgColNo" class="form-control" type="text" maxLength="4" />
								<span id="targetOrgColNoMsg" class="control-label"></span>
							</div>
						</spring:bind>
						<spring:bind path="fileType">
							<div id="fileTypeDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="fileType">File Type *</label>
								<form:select path="fileType" id="fileType" class="form-control half formField">
									<option value="">- Select -</option>
									<c:forEach items="${fileTypes}" var="type" varStatus="fStatus">
										<option value="${fileTypes[fStatus.index][0]}" <c:if test="${transportDetails.fileType == fileTypes[fStatus.index][0]}">selected</c:if>>${fileTypes[fStatus.index][1]} </option>
									</c:forEach>
								</form:select>
								<span id="fileTypeMsg" class="control-label"></span>
							</div>
						</spring:bind>
						<spring:bind path="fileDelimiter">
							<div id="fileDelimDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="delimiter">Delimiter *</label>
								<form:select path="fileDelimiter" id="delimiter" class="form-control half formField">
									<option value="">- Select -</option>
									<c:forEach items="${delimiters}" var="delim" varStatus="dStatus">
										<option value="${delimiters[dStatus.index][0]}" <c:if test="${transportDetails.fileDelimiter == delimiters[dStatus.index][0]}">selected</c:if>>${delimiters[dStatus.index][1]} </option>
									</c:forEach>
								</form:select>
								<span id="fileDelimMsg" class="control-label"></span>
							</div>
						</spring:bind>
					</div>
				</div>
			</section>
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

		//Need to check which section to show when the transport method is selected
		$('#transportMethod').change(function(event) {
			$('#fileInfo').hide();

			//Remove all has-error class
			$('div.form-group').removeClass("has-error");
			$('span.control-label').removeClass("has-error");
			$('span.control-label').html("");
			$('select.formField').val('');
			
			//Show the file Information div
			if($(this).val() == 1 || $(this).val() == 3) {
				$('#fileInfo').show();
			}
			

			//If you change the transport method need to set the 
			//clearFields variable to a 1. This will control removing
			//existing uploaded form fields and field mappings
			$('#clearFields').val("1");
		});
		
		$('#saveDetails').click(function(event) {
			$('#action').val('save');
			var hasErrors = 0;

			hasErrors = checkFormFields();
			
			if(hasErrors == 0) {
				$("#transport").submit();
			}
			
		});
		
		$('#next').click(function(event) {
			$('#action').val('next');
			var hasErrors = 0;

			hasErrors = checkFormFields();
			
			if(hasErrors == 0) {
				$("#transport").submit();
			}
		});

		//If the file is uploaded set the clearFields to true
		$('#templateFile').change(function() {
			$('#clearFields').val("1");
		});
		
	});

	function checkFormFields() {
		var hasErrors = 0;

		//Remove all has-error class
		$('div.form-group').removeClass("has-error");
		$('span.control-label').removeClass("has-error");
		$('span.control-label').html("");

		//Check Transport method
		if($('#transportMethod').val() == "") {
			hasErrors = 1;
			$('#transportMethodDiv').addClass("has-error");
			$('#transportMethodMsg').addClass("has-error");
			$('#transportMethodMsg').html('The transport method is a required field!');
		}
		//Check fields for file upload method
		if($('#transportMethod').val() == 1 || $('#transportMethod').val() == 3) {
			//Make sure the file uploaded is an excel file.
			if($('#currFile').val() != '') {
				if($('#templateFile').val() != '' && $('#templateFile').val().indexOf('.xlsx') == -1) {
					$('#templateFileDiv').addClass("has-error");
					$('#templateFileMsg').addClass("has-error");
					$('#templateFileMsg').html('The template file must be an excel file (.xlsx format).');
					hasErrors = 1;
				}
			}
			else {
				if($('#templateFile').val() == '' || $('#templateFile').val().indexOf('.xlsx') == -1) {
					$('#templateFileDiv').addClass("has-error");
					$('#templateFileMsg').addClass("has-error");
					$('#templateFileMsg').html('The template file must be an excel file (.xlsx format).');
					hasErrors = 1;
				}

			}

			//Make sure a valid field no is entered
			if(!$.isNumeric($('#targetOrgColNo').val())) {
				$('#targetOrgColNoDiv').addClass("has-error");
				$('#targetOrgColNoMsg').addClass("has-error");
				$('#targetOrgColNoMsg').html('The target organziation field No must be a numeric value!');
				hasErrors = 1;
			}
			if(!$.isNumeric($('#messageTypeColNo').val())) {
				$('#messageTypeColNoDiv').addClass("has-error");
				$('#messageTypeColNoMsg').addClass("has-error");
				$('#messageTypeColNoMsg').html('The message type field No must be a numeric value!');
				hasErrors = 1;
			}

			//Make sure the file type and delimiter is selected
			if($('#fileType').val() == '') {
				$('#fileTypeDiv').addClass("has-error");
				$('#fileTypeMsg').addClass("has-error");
				$('#fileTypeMsg').html('The file type is a required field!');
				hasErrors = 1;
			}
			if($('#delimiter').val() == '') {
				$('#fileDelimDiv').addClass("has-error");
				$('#fileDelimMsg').addClass("has-error");
				$('#fileDelimMsg').html('The file delimiter is a required field!');
				hasErrors = 1;
			}
		}

		return hasErrors;
	}


</script>

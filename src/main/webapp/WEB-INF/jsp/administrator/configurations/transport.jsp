
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
			
			<section class="panel panel-default">
				
				<div class="panel-heading">
					<h3 class="panel-title">Transport Details</h3>
				</div>
				<div class="panel-body">
					<spring:bind path="transportMethod">
						<div id="transportMethodDiv" class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="name">Transport Method *</label>
							<form:select path="transportMethod" id="transportMethod" class="form-control half" disabled="${transportDetails.transportMethod > 0 ? 'true' : 'false'}">
								<option value="">- Select -</option>
								<c:forEach items="${transportMethods}" var="transMethod" varStatus="tStatus">
									<option value="${transportMethods[tStatus.index][0]}" <c:if test="${transportDetails.transportMethod == transportMethods[tStatus.index][0]}">selected</c:if>>${transportMethods[tStatus.index][1]} </option>
								</c:forEach>
							</form:select>
							<span id="transportMethodMsg" class="control-label"></span>
						</div>
					</spring:bind>
					
					<div id="fileInfo" style="display:none">
						<spring:bind path="file">
							<div id="templateFileDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="templateFile">Template File *</label>
								<form:input path="file" id="templateFile" type="file" />
								<span id="templateFileMsg" class="control-label"></span>
							</div>
						</spring:bind>
						<spring:bind path="messageTypeColNo">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="messageTypeColNo">Field No. that contains the message type </label>
								<form:input path="messageTypeColNo" id="messageTypeColNo" class="form-control" type="text" maxLength="4" />
								<form:errors path="messageTypeColNo" cssClass="control-label" element="label" />
							</div>
						</spring:bind>
						<spring:bind path="messageTypeColNo">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="messageTypeCustomVal">Custom Message Type Value for this Configuration</label>
								<form:input path="messageTypeCustomVal" id="messageTypeCustomVal" class="form-control" type="text" maxLength="45" />
								<form:errors path="messageTypeCustomVal" cssClass="control-label" element="label" />
							</div>
						</spring:bind>
						<spring:bind path="targetOrgColNo">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="targetOrgColNo">Field No. that contains the target organization </label>
								<form:input path="targetOrgColNo" id="targetOrgColNo" class="form-control" type="text" maxLength="4" />
								<form:errors path="targetOrgColNo" cssClass="control-label" element="label" />
							</div>
						</spring:bind>
						<spring:bind path="fileType">
							<div id="fileTypeDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="fileType">File Type *</label>
								<form:select path="fileType" id="fileType" class="form-control half">
									<option value="">- Select -</option>
									<c:forEach items="${fileTypes}" var="type" varStatus="fStatus">
										<option value="${fileTypes[fStatus.index][0]}">${fileTypes[fStatus.index][1]} </option>
									</c:forEach>
								</form:select>
								<span id="fileTypeMsg" class="control-label"></span>
							</div>
						</spring:bind>
						<spring:bind path="fileDelimiter">
							<div id="fileDelimDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="delimiter">Delimiter *</label>
								<form:select path="fileDelimiter" id="delimiter" class="form-control half">
									<option value="">- Select -</option>
									<c:forEach items="${delimiters}" var="delim" varStatus="dStatus">
										<option value="${delimiters[dStatus.index][0]}">${delimiters[dStatus.index][1]} </option>
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
			
			//Show the file Information div
			if($(this).val() == 1 || $(this).val() == 3) {
				$('#fileInfo').show();
			}
			
			
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
		
	});

	function checkFormFields() {
		var hasErrors = 0;

		//Remove all has-error class
		$('.form-group div').removeClass("has-error");
		$('.control-label span').removeClass("has-error");

		//Check Transport method
		if($('#transportMethod').val() == "") {
			hasErrors = 1;
			$('#transportMethodDiv').addClass("has-error");
			$('#transportMethodMsg').addClass("has-error");
			$('#transportMethodMsg').html('The transport method is a required field!');
		}

		return hasErrors;
	}


</script>

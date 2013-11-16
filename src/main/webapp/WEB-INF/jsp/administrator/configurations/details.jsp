<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">

	<div class="col-md-12">
	
		<c:if test="${not empty savedStatus}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose><c:when test="${savedStatus == 'updated'}">The configuration has been successfully updated!</c:when><c:otherwise>The configuration has been successfully created!</c:otherwise></c:choose>
			</div>
		</c:if>
	
		<form:form id="configuration" commandName="configurationDetails" modelAttribute="configurationDetails" method="post" enctype="multipart/form-data" role="form">
			<input type="hidden" id="action" name="action" value="save" />
			<form:hidden path="id" id="id" />
			<form:hidden path="dateCreated" />
			
			<section class="panel panel-default">
				
				<div class="panel-heading">
					<h3 class="panel-title">Details</h3>
				</div>
				<div class="panel-body">
					<div class="form-container">
						<div class="form-group">
							<label for="status">Status *</label>
							<div>
								<label class="radio-inline">
									<form:radiobutton id="status" path="status" value="1" disabled="${stepsCompleted == 5 ? 'false' : 'true' }" />Active 
								</label>
								<label class="radio-inline">
									<form:radiobutton id="status" path="status" value="0"/>Inactive
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="type">Configuration Type *</label>
							<div>
								<label class="radio-inline">
									<form:radiobutton id="type" path="type" value="1" disabled="${configurationDetails.id == 0 ? 'false' : 'true' }"/> For Source Organization 
								</label>
								<label class="radio-inline">
									<form:radiobutton id="type" path="type" value="2" disabled="${configurationDetails.id == 0 ? 'false' : 'true' }"/> For Target Organization
								</label>
							</div>
						</div>
						<spring:bind path="orgId">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="organization">Organization *</label>
								<form:select path="orgId" id="organization" class="form-control half" disabled="${configurationDetails.id == 0 ? 'false' : 'true' }">
									<option value="">- Select -</option>
									<c:forEach items="${organizations}" var="org" varStatus="oStatus">
										<option value="${organizations[oStatus.index].id}" <c:if test="${configurationDetails.orgId == organizations[oStatus.index].id}">selected</c:if>>${organizations[oStatus.index].orgName} </option>
									</c:forEach>
								</form:select>
							</div>
						</spring:bind>
						<spring:bind path="messageTypeId">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="messageTypeId">Message Type *</label>
								<form:select path="messageTypeId" id="messageTypeId" class="form-control half" disabled="${configurationDetails.id == 0 ? 'false' : 'true' }">
									<option value="">- Select -</option>
									<c:forEach items="${messageTypes}" var="msgType" varStatus="mStatus">
										<option value="${messageTypes[mStatus.index].id}" <c:if test="${configurationDetails.messageTypeId == messageTypes[mStatus.index].id}">selected</c:if>>${messageTypes[mStatus.index].name} </option>
									</c:forEach>
								</form:select>
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
		
		$('#saveDetails').click(function(event) {
			$('#action').val('save');
			var hasErrors = 0;
			if(hasErrors == 0) {
				$("#configuration").submit();
			}
			
		});
		
		$('#next').click(function(event) {
			$('#action').val('next');
			var hasErrors = 0;
			if(hasErrors == 0) {
				$("#configuration").submit();
			}
		});
		
	});


</script>

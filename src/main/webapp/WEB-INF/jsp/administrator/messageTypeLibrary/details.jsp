<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">

	<div class="col-md-12">
	
		<c:if test="${not empty savedStatus}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose><c:when test="${savedStatus == 'updated'}">The message type has been successfully updated!</c:when><c:otherwise>The message type has been successfully created!</c:otherwise></c:choose>
			</div>
		</c:if>
	
		<form:form commandName="messageTypeDetails" method="post" role="form">
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
									<form:radiobutton id="status" path="status" value="1"/>Active 
								</label>
								<label class="radio-inline">
									<form:radiobutton id="status" path="status" value="0"/>Inactive
								</label>
								<label class="radio-inline">
									<form:radiobutton id="status" path="status" value="2"/>Archived
								</label>
							</div>
						</div>
						<spring:bind path="name">
							<div class="form-group ${status.error ? 'has-error' : '' } ${not empty esistingType ? 'has-error' : ''}">
								<label class="control-label" for=name>Name *</label>
								<form:input path="name" id="name" class="form-control" type="text" maxLength="255" />
								<form:errors path="name" cssClass="control-label" element="label" />
								<c:if test="${not empty esistingType}">${esistingType}</c:if>
							</div>
						</spring:bind>
						<spring:bind path="templateFile">
							<div id="templateFileDiv" class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="templateFile">Template File *</label>
								<form:input path="templateFile" id="templateFile" type="file" class="form-control" />
								<span id="templateFileMsg" class="control-label"></span>
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
			$("#messageType").submit();
		});
		
		$('#next').click(function(event) {
			$('#action').val('next');
        	$('#messageType').submit();
		});
		
		
	});
	

</script>

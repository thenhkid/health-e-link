
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
							<form:select path="transportMethod" id="transportMethod" class="form-control half">
								<option value="">- Select -</option>
								<c:forEach items="${transportMethods}" var="transMethod" varStatus="tStatus">
									<option value="${transportMethods[tStatus.index][0]}">${transportMethods[tStatus.index][1]} </option>
								</c:forEach>
							</form:select>
							<span id="transportMethodMsg" class="control-label"></span>
						</div>
					</spring:bind>
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

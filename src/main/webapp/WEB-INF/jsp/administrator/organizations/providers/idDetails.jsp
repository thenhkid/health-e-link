
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 class="panel-title"><c:choose><c:when test="${btnValue == 'Update'}">Update</c:when><c:when test="${btnValue == 'Create'}">Add</c:when></c:choose> Provider Id ${success}</h3>
		</div>
		<div class="modal-body">
			<form:form id="providerIddetailsform" commandName="idDetails" modelAttribute="idDetails" method="post" role="form">
				<form:hidden path="id" id="id" />
				<form:hidden path="providerId" id="providerId" />
				<form:hidden path="dateCreated" />
				<div class="form-container">
					<spring:bind path="idNum">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="idNum">ID Number *</label>
							<form:input path="idNum" id="idNum" class="form-control" type="text" maxLength="45" />
							<form:errors path="idNum" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="type">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="type">Type</label>
							<form:input path="type" id="type" class="form-control" type="text" maxLength="45" />
							<form:errors path="type" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="issuedBy">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="issuedBy">Issued By *</label>
							<form:input path="issuedBy" id="issuedBy" class="form-control" type="text" maxLength="45" />
							<form:errors path="issuedBy" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<div class="form-group">
						<input type="button" id="submitIdButton" rel="${btnValue}" class="btn btn-primary" value="${btnValue}"/>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>

<script type="text/javascript">

	$(document).ready(function() {
	    $("input:text,form").attr("autocomplete","off");
	});

</script>
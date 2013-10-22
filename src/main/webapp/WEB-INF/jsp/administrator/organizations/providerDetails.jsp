
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 class="panel-title"><c:choose><c:when test="${btnValue == 'Update'}">Update</c:when><c:when test="${btnValue == 'Create'}">Add</c:when></c:choose> Provider ${success}</h3>
		</div>
		<div class="modal-body">
			<form:form id="providerdetailsform" commandName="providerdetails" modelAttribute="providerdetails"  method="post" role="form">
				<form:hidden path="id" id="id" />
				<form:hidden path="orgId" id="orgId" />
				<form:hidden path="dateCreated" />
				<div class="form-container">
					<div class="form-group">
						<label for="status">Status *</label>
						<div>
							<label class="radio-inline">
								<form:radiobutton id="status" path="status" value="true" /> Active
							</label>
							<label class="radio-inline">
								<form:radiobutton id="status" path="status" value="false" /> Inactive
							</label>
						</div>
					</div>
					<spring:bind path="firstName">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="firstName">First Name *</label>
							<form:input path="firstName" id="firstName" class="form-control" type="text" maxLength="45" />
							<form:errors path="firstName" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="lastName">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="lastName">Last Name *</label>
							<form:input path="lastName" id="lastName" class="form-control" type="text" maxLength="45" />
							<form:errors path="lastName" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="providerId">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="providerId">Provider Id *</label>
							<form:input path="providerId" id="providerId" class="form-control" type="text"  maxLength="45" />
							<form:errors path="providerId" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="email">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="email">Email</label>
							<form:input path="email" id="email" class="form-control" type="text"  maxLength="255" />
							<form:errors path="email" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="phone">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="phone">Phone *</label>
							<form:input path="phone" id="phone" class="form-control" type="text" maxLength="20" />
							<form:errors path="phone" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="fax">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="fax">Fax</label>
							<form:input path="fax" id="fax" class="form-control" type="text" maxLength="20" />
							<form:errors path="fax" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<label class="control-label" for="fax">Address Info</label>
					<c:choose>
						<c:when test="${providerdetails.providerAddresses.size() > 0}">
							<c:forEach items="${providerdetails.providerAddresses}" var="address" varStatus="pStatus">
							 	<spring:bind path="providerAddresses[${pStatus.index}].line1">
								 	<div class="form-group ${status.error ? 'has-error' : '' }">
										<label class="control-label" for="address1">Address 1 </label>
										<form:input path="providerAddresses[${pStatus.index}].line1" id="address1" class="form-control" type="text" size="20" maxLength="45" />
										<form:errors path="providerAddresses[${pStatus.index}].line1" cssClass="control-label" element="label" />
									</div>
								</spring:bind>
								<spring:bind path="providerAddresses[${pStatus.index}].city">
								 	<div class="form-group ${status.error ? 'has-error' : '' }">
										<label class="control-label" for="address1">City </label>
										<form:input path="providerAddresses[${pStatus.index}].city" id="address1" class="form-control" type="text" size="20" maxLength="45" />
										<form:errors path="providerAddresses[${pStatus.index}].city" cssClass="control-label" element="label" />
									</div>
								</spring:bind>
							</c:forEach>
						</c:when>
						<c:otherwise>
						 	<div class="form-group">
								<label class="control-label" for="providerAddresses[0].line1">Address 1</label>
								<input id="providerAddresses[0].line1" class="form-control" name="providerAddresses[0].line1" type="text" size="20" maxLength="45" />
							</div>
							<div class="form-group">
								<label class="control-label" for="providerAddresses[0].city">City</label>
								<input id="providerAddresses[0].city" class="form-control" name="providerAddresses[0].city" type="text" size="20" maxLength="45" />
							</div>
						</c:otherwise>
					</c:choose>
					
					<div class="form-group">
						<input type="button" id="submitButton" rel="${btnValue}" class="btn btn-primary" value="${btnValue}"/>
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
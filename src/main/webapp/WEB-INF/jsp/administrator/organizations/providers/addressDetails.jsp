
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 class="panel-title"><c:choose><c:when test="${btnValue == 'Update'}">Update</c:when><c:when test="${btnValue == 'Create'}">Add</c:when></c:choose> Address ${success}</h3>
		</div>
		<div class="modal-body">
			<form:form id="addressdetailsform" commandName="addressDetails" modelAttribute="addressDetails" method="post" role="form">
				<form:hidden path="id" id="id" />
				<form:hidden path="providerId" id="providerId" />
				<form:hidden path="dateCreated" />
				<div class="form-container">
					<spring:bind path="type">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="type">Type</label>
							<form:input path="type" id="type" class="form-control" type="text" maxLength="45" />
							<form:errors path="type" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="line1">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="line1">Line 1 *</label>
							<form:input path="line1" id="line1" class="form-control" type="text" maxLength="45" />
							<form:errors path="line1" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="line2">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="line2">Line 2</label>
							<form:input path="line2" id="line2" class="form-control" type="text" maxLength="45" />
							<form:errors path="line2" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="city">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="city">City *</label>
							<form:input path="city" id="city" class="form-control" type="text" maxLength="45" />
							<form:errors path="city" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="state">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="state">State *</label>
							<form:select id="state" path="state" cssClass="form-control half">
								<option value="" label=" - Select - " ></option>
								<form:options items="${stateList}"/>
							</form:select>
							<form:errors path="state" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="postalCode">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="postalCode">Postal Code *</label>
							<form:input path="postalCode" id="postalCode" class="form-control" type="text" maxLength="15" />
							<form:errors path="postalCode" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="phone1">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="phone1">Office Phone *</label>
							<form:input path="phone1" id="phone1" class="form-control" type="text" maxLength="45" />
							<form:errors path="phone1" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="phone2">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="phone2">Toll Free Phone</label>
							<form:input path="phone2" id="phone2" class="form-control" type="text" maxLength="45" />
							<form:errors path="phone2" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
					<spring:bind path="fax">
						<div class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="fax">Fax</label>
							<form:input path="fax" id="fax" class="form-control" type="text" maxLength="45" />
							<form:errors path="fax" cssClass="control-label" element="label" />
						</div>
					</spring:bind>
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
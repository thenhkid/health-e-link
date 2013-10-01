<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">

	<div class="col-md-12">
	
		<form:form commandName="organization"  method="post" role="form">
			<input type="hidden" id="action" name="action" value="save" />
			<form:hidden path="id" id="orgId" />
			<form:hidden path="cleanURL" id="cleanURL" />
			
			<div class="panel panel-default">
				<c:if test="${saved == 'success'}">
					<p class="success">User Updated Successfully</p>
				</c:if>
				<div class="panel-heading">
					<h3 class="panel-title">Details</h3>
				</div>
				<div class="panel-body">
					<div class="form-container">
						<div class="form-group">
							<label>Status</label>
							<div>
								<label class="radio-inline">
									<form:radiobutton path="status" value="true"/>Active 
								</label>
								<label class="radio-inline">
									<form:radiobutton path="status" value="false"/>Inactive
								</label>
							</div>
						</div>
						<div class="form-group">
							<label>Viewable</label>
							<div>
								<label class="radio-inline">
									<form:radiobutton path="publicOrg" value="true"/>Public
								</label>
								<label class="radio-inline">
									<form:radiobutton path="publicOrg" value="false"/>Private 
								</label>
							</div>
						</div>
						<spring:bind path="orgName">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="Name">Name</label>
								<form:input path="orgName" id="orgName" class="form-control" type="text" />
								<form:errors path="orgName" cssClass="control-label" element="label" />
								<c:if test="${not empty existingOrg}">${existingOrg}</c:if>
							</div>
						</spring:bind>
						<spring:bind path="address">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="Address">Address</label>
								<form:input path="address" id="address" class="form-control" type="text" />
								<form:errors path="address" cssClass="control-label" element="label" />
							</div>
						</spring:bind>
						<div class="form-group">
							<label for="Address 2">Address 2</label>
							<form:input path="address2" class="form-control" type="text" />
						</div>
						<spring:bind path="city">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="City">City</label>
								<form:input path="city" id="city" class="form-control" type="text" />
								<form:errors path="city" cssClass="control-label" element="label" />
							</div>
						</spring:bind>
						<div class="form-group">
							<label for="state">State</label>
							<form:select path="state" class="form-control half">
								<form:option value="NH" lable="New Hampshire" />
							    <!--<form:options items="${countryList}" />-->
							</form:select>
							<form:errors path="state" cssClass="error" />
						</div>
						<spring:bind path="postalCode">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="Postal Code">Postal Code</label>
								<form:input path="postalCode" id="postalCode" class="form-control" type="text" />
								<form:errors path="postalCode" cssClass="control-label" element="label" />
							</div>
						</spring:bind>
						<spring:bind path="phone">
							<div class="form-group ${status.error ? 'has-error' : '' }">
								<label class="control-label" for="Phone">Phone</label>
								<form:input path="phone" id="phone" class="form-control" type="text" />
								<form:errors path="phone" cssClass="control-label" element="label" />
							</div>
						</spring:bind>
						<div class="form-group">
							<label for="fieldB">Fax</label>
							<form:input path="fax" class="form-control" type="text" />
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>


<script type="text/javascript">

	$(document).ready(function() {
	    $("input:text,form").attr("autocomplete","off");
	})

	$(function() {
		$("#saveDetails").click(function(event) {
			$('#action').val('save');
			$("#organization").submit();
		});
		
		$("#saveCloseDetails").click(function(event) {
			$('#action').val('close');
        	$("#organization").submit();
		});

		//Need to set the organization clean url based off of the organization name
		$("#orgName").keyup(function(event) {
			var orgName = $(this).val();
			var strippedorgName = orgName.replace(/ +/g, '');
			$('#cleanURL').val(strippedorgName);
			$('#nameChange').val(1);
		});

	});
</script>

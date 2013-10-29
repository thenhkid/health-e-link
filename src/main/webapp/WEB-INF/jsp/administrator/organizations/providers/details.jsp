
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="main clearfix" role="main">

	<div class="col-md-12">
	<c:choose>
		<c:when test="${not empty savedStatus}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose>
					<c:when test="${savedStatus == 'updated'}">The provider has been successfully updated!</c:when>
					<c:otherwise>The provider has been successfully created!</c:otherwise></c:choose>
			</div>
		</c:when>
		<c:when test="${not empty param.msg}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose>
					<c:when test="${param.msg == 'updated'}">The address has been successfully updated!</c:when>
					<c:when test="${param.msg == 'created'}">The address has been successfully added!</c:when>
					<c:when test="${param.msg == 'deleted'}">The address has been successfully removed!</c:when>
					<c:when test="${param.msg == 'idupdated'}">The provider Id has been successfully updated!</c:when>
					<c:when test="${param.msg == 'idcreated'}">The provider Id has been successfully added!</c:when>
					<c:when test="${param.msg == 'iddeleted'}">The provider Id has been successfully removed!</c:when>
				</c:choose>
			</div>
		</c:when>
	</c:choose>
	</div>

	<div class="col-md-6">
	
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Provider Details</h3>
			</div>
			<div class="panel-body">
				<div class="form-container">
					<form:form id="providerdetailsform" commandName="providerdetails" modelAttribute="providerdetails"  method="post" role="form">
						<input type="hidden" id="action" name="action" value="save" />
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
							<spring:bind path="gender">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="control-label" for="gender">Gender *</label>
									<form:select path="gender" id="gender" class="form-control half">
										<form:option value="">- Choose Gender -</form:option>
										<form:option value="Female">Female</form:option>
										<form:option value="Male">Male</form:option>
									</form:select>
									<form:errors path="gender" cssClass="control-label" element="label" />
								</div>
							</spring:bind>
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
							<spring:bind path="email">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="control-label" for="email">Email</label>
									<form:input path="email" id="email" class="form-control" type="text"  maxLength="255" />
									<form:errors path="email" cssClass="control-label" element="label" />
								</div>
							</spring:bind>
							<spring:bind path="phone1">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="control-label" for="phone1">Main Phone *</label>
									<form:input path="phone1" id="phone1" class="form-control" type="text" maxLength="20" />
									<form:errors path="phone1" cssClass="control-label" element="label" />
								</div>
							</spring:bind>
							<spring:bind path="phone2">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="control-label" for="phone2">Cell</label>
									<form:input path="phone2" id="phone2" class="form-control" type="text" maxLength="20" />
									<form:errors path="phone2" cssClass="control-label" element="label" />
								</div>
							</spring:bind>
							<spring:bind path="fax">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="control-label" for="fax">Fax</label>
									<form:input path="fax" id="fax" class="form-control" type="text" maxLength="20" />
									<form:errors path="fax" cssClass="control-label" element="label" />
								</div>
							</spring:bind>
							<spring:bind path="specialty">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="control-label" for="specialty">Specialty</label>
									<form:input path="specialty" id="specialty" class="form-control" type="text" maxLength="100" />
									<form:errors path="specialty" cssClass="control-label" element="label" />
								</div>
							</spring:bind>
							<spring:bind path="website">
								<div class="form-group ${status.error ? 'has-error' : '' }">
									<label class="control-label" for="website">URL</label>
									<form:input path="website" id="website" class="form-control" type="text" maxLength="255" />
									<form:errors path="website" cssClass="control-label" element="label" />
								</div>
							</spring:bind>
						</div>
					</form:form>
				</div>
			</div>
		</section>
	</div>
	
	<!-- Existing Provider Addresses -->
	<c:if test="${providerId > 0}">
	<div class="col-md-6">
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Provider Addresses</h3>
			</div>
			<div class="panel-body">
				<div class="form-container scrollable">
					<a href="#systemAddressModal" id="createNewAddress" data-toggle="modal" class="btn btn-primary btn-sm pull-right" title="Create a new address">
						<span class="glyphicon glyphicon-plus"></span>
					</a>
					<table class="table table-striped table-hover responsive">
						<thead>
							<tr>
								<th scope="col">Address Info</th>
								<th scope="col">Contact Info</th>
								<th scope="col"></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${providerdetails.providerAddresses.size() > 0}">
									<c:forEach items="${providerdetails.providerAddresses}" var="address" varStatus="pStatus">
									<tr>
										<td scope="row">
											<c:if test="${not empty providerdetails.providerAddresses[pStatus.index].type}"><span style="font-style:italic">${providerdetails.providerAddresses[pStatus.index].type}</span><br /r></c:if>
											${providerdetails.providerAddresses[pStatus.index].line1} 
											<c:if test="${not empty providerdetails.providerAddresses[pStatus.index].line2}"><br />${providerdetails.providerAddresses[pStatus.index].line2}</c:if>
											<br />
											${providerdetails.providerAddresses[pStatus.index].city} ${providerdetails.providerAddresses[pStatus.index].state} ${providerdetails.providerAddresses[pStatus.index].postalCode}
										</td>
										<td>
											(o) ${providerdetails.providerAddresses[pStatus.index].phone1}
											<c:if test="${not empty providerdetails.providerAddresses[pStatus.index].phone2}"><br />(toll free) ${providerdetails.providerAddresses[pStatus.index].phone2}</c:if>
											<c:if test="${not empty providerdetails.providerAddresses[pStatus.index].fax}">(f) ${providerdetails.providerAddresses[pStatus.index].fax}</c:if>
										</td>
										<td>
											<a href="#systemAddressModal" data-toggle="modal" class="btn btn-link editAddress" rel="address?i=${providerdetails.providerAddresses[pStatus.index].id}" title="Edit this address">
											     <span class="glyphicon glyphicon-edit"></span>
											     Edit
											</a>
											<a href="javascript:void(0);"  class="btn btn-link deleteAddress" rel="${providerdetails.providerAddresses[pStatus.index].id}" title="Delete this address">
												<span class="glyphicon glyphicon-remove"></span>
												Delete
											</a>
										</td>
									</tr>
									</c:forEach>
								</c:when>
								<c:otherwise><tr><td scope="row" colspan="3" style="text-align:center">No Addresses Found</td></c:otherwise>
							</c:choose>
						</tbody>
					</table>

				</div>
			</div>
		</section>
		
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Provider Ids</h3>
			</div>
			<div class="panel-body">
				<div class="form-container scrollable">
					<a href="#providerIdModal" id="createNewId" data-toggle="modal" class="btn btn-primary btn-sm pull-right" title="Create a new provider Id">
						<span class="glyphicon glyphicon-plus"></span>
					</a>
					<table class="table table-striped table-hover responsive">
						<thead>
							<tr>
								<th scope="col">ID Number</th>
								<th scope="col">Type</th>
								<th scope="col">Issued By</th>
								<th scope="col"></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${providerdetails.providerIds.size() > 0}">
									<c:forEach items="${providerdetails.providerIds}" var="id" varStatus="pStatus">
									<tr>
										<td scope="row">
											${providerdetails.providerIds[pStatus.index].idNum} 
										</td>
										<td>
											${providerdetails.providerIds[pStatus.index].type}
										</td>
										<td>
											${providerdetails.providerIds[pStatus.index].issuedBy}
										</td>
										<td>
											<a href="#providerIdModal" data-toggle="modal" class="btn btn-link editId" rel="address?i=${providerdetails.providerIds[pStatus.index].id}" title="Edit this provider Id">
											     <span class="glyphicon glyphicon-edit"></span>
											     Edit
											</a>
											<a href="javascript:void(0);"  class="btn btn-link deleteId" rel="${providerdetails.providerIds[pStatus.index].id}" title="Delete this provider Id">
												<span class="glyphicon glyphicon-remove"></span>
												Delete
											</a>
										</td>
									</tr>
									</c:forEach>
								</c:when>
								<c:otherwise><tr><td scope="row" colspan="4" style="text-align:center">No provider Ids Found</td></c:otherwise>
							</c:choose>
						</tbody>
					</table>

				</div>
			</div>
		</section>
	</div>
	</c:if>
</div>

<!-- Provider Address modal -->
<div class="modal fade" id="systemAddressModal" role="dialog" tabindex="-1" aria-labeledby="Provider Address" aria-hidden="true" aria-describedby="Provider Address"></div>

<!-- Provider Id modal -->
<div class="modal fade" id="providerIdModal" role="dialog" tabindex="-1" aria-labeledby="Provider Id" aria-hidden="true" aria-describedby="Provider Id"></div>


<script type="text/javascript">

	$(document).ready(function() {
	    $("input:text,form").attr("autocomplete","off");
	});

	$(function() {
		//Fade out the updated/created message after being displayed.
		if($('.alert').length >0) {
			$('.alert').delay(2000).fadeOut(1000);
		}

		//The function that will be called when the "Save" button
		//is clicked
		$('#saveDetails').click(function(event) {
			$('#action').val('save');
			$("#providerdetailsform").submit();
		});

		//The function that will be called when the "Save & Close" button
		// is clicked
		$('#saveCloseDetails').click(function(event) {
			$('#action').val('close');
        	$('#providerdetailsform').submit();
		});

		//Function to delete a provider
		$('#deleteProvider').click(function(event) {
			var providerId = $(this).attr('rel');
			var confirmed = confirm("Are you sure you want to remove this provider?");

			if(confirmed) {
				window.location.href=  'providerDelete/delete?i='+providerId;
			}
			
		});

		//This function will launch the new provider Id overlay with a blank form
		$(document).on('click','#createNewId',function() {
			$.ajax({  
		        url: 'newProviderId'+'?providerId='+$('#id').val(),  
		        type: "GET",  
		        success: function(data) {  
		            $("#providerIdModal").html(data);           
		        }  
		    });  
		});

		//This function will launch the edit provider Id overlay populating the fields
		//with the data of the clicked id.
		$(document).on('click', '.editId',function() {
			var Action = 'providerId/'+$(this).attr('rel');

			$.ajax({  
		        url: Action,  
		        type: "GET",  
		        success: function(data) {  
		            $("#providerIdModal").html(data);           
		        }  
		    });  
		});

		//This function will delete the selected provider Id
		$(document).on('click', '.deleteId',function() {
			var confirmed = confirm("Are you sure you want to remove this provider Id?");

			if(confirmed) {
				var id = $(this).attr('rel');
				
				$.ajax({  
			        url: "providerIdDelete/delete?i="+id,  
			        type: "GET",  
			        async: false,
			        success: function(data) { 
			        	//Strip out any remaming msg parameters in the string
			        	var url = removeVariableFromURL($(location).attr('href'),'msg');
				        window.location.href=  url + "&msg=iddeleted";
			        }  
			    });  
			}
		});

		//Function to submit the changes to an existing provider Id or 
		//submit the new provider Id fields from the modal window.
		$(document).on('click', '#submitIdButton',function(event) {
			
			var formData = $("#providerIddetailsform").serialize();  

			var actionValue = 'providerId/'+$(this).attr('rel').toLowerCase();
			
			$.ajax({  
		        url: actionValue,  
		        data: formData,  
		        type: "POST",  
		        async: false,
		        success: function(data) { 
		        	//Strip out any remaming msg parameters in the string
		        	var url = removeVariableFromURL($(location).attr('href'),'msg');
			        if(data.indexOf('idUpdated') != -1) {
				        window.location.href= url + "&msg=idupdated";
					}
			        else if(data.indexOf('idCreated') != -1) {
			        	window.location.href=  url + "&msg=idcreated";
					}
			        else {
		        		$("#providerIdModal").html(data);
			        }
		        }  
		    });  
			event.preventDefault();
			return false;

		});

		//This function will launch the new provider address overlay with a blank form
	    $(document).on('click','#createNewAddress',function() {
	    	$.ajax({  
		        url: 'newProviderAddress'+'?providerId='+$('#id').val(),  
		        type: "GET",  
		        success: function(data) {  
		            $("#systemAddressModal").html(data);           
		        }  
		    });  
		});

		//This function will launch the edit address overlay populating the fields
		//with the data of the clicked address.
		$(document).on('click', '.editAddress',function() {
			var Action = 'providerAddress/'+$(this).attr('rel');

			$.ajax({  
		        url: Action,  
		        type: "GET",  
		        success: function(data) {  
		            $("#systemAddressModal").html(data);           
		        }  
		    });  
		});

		//This function will delete the selected address
		$(document).on('click', '.deleteAddress',function() {
			var confirmed = confirm("Are you sure you want to remove this address?");

			if(confirmed) {
				var id = $(this).attr('rel');
				
				$.ajax({  
			        url: "addressDelete/delete?i="+id,  
			        type: "GET",  
			        async: false,
			        success: function(data) { 
			        	//Strip out any remaming msg parameters in the string
			        	var url = removeVariableFromURL($(location).attr('href'),'msg');
				        window.location.href=  url + "&msg=deleted";
			        }  
			    });  
			}
		});

		//Function to submit the changes to an existing address or 
		//submit the new address fields from the modal window.
		$(document).on('click', '#submitButton',function(event) {
			
			var formData = $("#addressdetailsform").serialize();  

			var actionValue = 'address/'+$(this).attr('rel').toLowerCase();
			
			$.ajax({  
		        url: actionValue,  
		        data: formData,  
		        type: "POST",  
		        async: false,
		        success: function(data) { 
			        //Strip out any remaming msg parameters in the string
		        	var url = removeVariableFromURL($(location).attr('href'),'msg');
			        if(data.indexOf('addressUpdated') != -1) {
				        window.location.href= url + "&msg=updated";
					}
			        else if(data.indexOf('addressCreated') != -1) {
			        	window.location.href=  url + "&msg=created";
					}
			        else {
		        		$("#systemAddressModal").html(data);
			        }
		        }  
		    });  
			event.preventDefault();
			return false;

		});
		
	});

	function removeVariableFromURL(url_string, variable_name) {
		var URL = String(url_string);
	    var regex = new RegExp( "\\?" + variable_name + "=[^&]*&?", "gi");
	    URL = URL.replace(regex,'?');
	    regex = new RegExp( "\\&" + variable_name + "=[^&]*&?", "gi");
	    URL = URL.replace(regex,'&');
	    URL = URL.replace(/(\?|&)$/,'');
	    regex = null;
	    return URL;
	}

</script>
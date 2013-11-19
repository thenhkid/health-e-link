<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="main clearfix" role="main">

	<div class="col-md-12">
	
		<c:if test="${not empty savedStatus}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose><c:when test="${savedStatus == 'updated'}">The configuration connections have been successfully updated!</c:when></c:choose>
			</div>
		</c:if>
		
		<div class="row-fluid">
			<div class="col-md-4">
				<section class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">New Connection</h3>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-md-8">
								<div id="organizationDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                    <label class="sr-only" for="organization">Organization *</label>
                                    <select id="organization" class="form-control">
                                         <c:choose>
	                                         <c:when test="${organizations.size() == 0}">
	                                         	 <option value="">No Available Organizations</option>
	                                         </c:when>
	                                         <c:otherwise>
	                                         	<option value="">- Select -</option>
		                                         <c:forEach items="${organizations}" var="orgs" varStatus="oStatus">
		                                              <c:if test="${not usedOrgs.contains(organizations[oStatus.index].id)}">
		                                             	 <option value="${organizations[oStatus.index].id}">${organizations[oStatus.index].orgName} </option>
		                                              </c:if>
		                                         </c:forEach>
	                                         </c:otherwise>
                                         </c:choose>
                                    </select>
                                    <span id="organizationMsg" class="control-label"></span>
                               </div>
							</div>
							<div class="col-md-4">
								<button class="btn btn-primary addOrganization">Add</button>
							</div>
						</div>
					</div>
				</section>
			</div>

			<div class="col-md-8">
				<section class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Existing Connections</h3>
				</div>
				<div class="panel-body">
					<div class="form-container scrollable">
						<div>
							<table class="table table-striped table-hover responsive">
								<thead>
									<tr>
										<th scope="col">Organization</th>
										<th scope="col" class="center-text">Date Created</th>
										<th scope="col" class="center-text"></th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${connections.size() > 0}">
											<c:forEach items="${connections}" var="connect" varStatus="cStatus">
											<tr>
												<td scope="row">
													${connections[cStatus.index].name}
												</td>
												<td class="center-text"><fmt:formatDate value="${connections[cStatus.index].dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
												<td class="center-text">
													<c:choose><c:when test="${connections[cStatus.index].status == 1}">Disable</c:when><c:otherwise>Enable</c:otherwise></c:choose>
										    	</td>
											</tr>
											</c:forEach>
										</c:when>
										<c:otherwise><tr><td scope="row" colspan="3" style="text-align:center">No connections Found</td></c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</section>
			</div>
		</div>

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

		//Add a new transport method
		$('.addTransportMethod').click(function() {
			var configId = $('#configId').last().val();
			var selMethod = $('#transportMethod').val();

			if(selMethod == '') {
				$('#transportMethodDiv').addClass("has-error");
			}
			else {
				$.ajax({  
			        url: 'addTransportMethod.do',  
			        type: "POST",  
			        data: { 'configId' : configId, 'transportMethod' : selMethod},
			        success: function(data) {  
			           if(data == 1) {
			        	   window.location.href=  "transport";
					  }        
			        }  
			    }); 
			}
		});

		//This function will save the messgae type field mappings
		$('#saveDetails').click(function(event) {
			$('#action').val('save');
			
			//Need to make sure all required fields are marked if empty.
			var hasErrors = 0;
			hasErrors = checkFormFields();
			
			if(hasErrors == 0) {
				$('#transportMethods').submit();
			}
		});

		$('#next').click(function(event) {
			$('#action').val('next');
			
			var hasErrors = 0;
			hasErrors = checkFormFields();
			
			if(hasErrors == 0) {
				$('#transportMethods').submit();
			}
		});
		
	});

	function checkFormFields() {
		var hasErrors = 0;

		//Remove all has-error class
        $('div.form-group').removeClass("has-error");
        $('span.control-label').removeClass("has-error");
        $('span.control-label').html("");

		//Loop through each transport method chosen
		$('.transportMethod').each(function() {
			var sectionVal = $(this).attr('rel');

			//Validate the File Upload fields
			if(sectionVal == 1 || sectionVal == 3) {
				var headVal = "";
				if(sectionVal == 3) {
					headVal = "FTP";
				}

				if($('#'+headVal+'currFile').val() != '') {
                    if($('#'+headVal+'templateFile').val() != '' && $('#'+headVal+'templateFile').val().indexOf('.xlsx') == -1) {
                            $('#templateFileDiv').addClass("has-error");
                            $('#templateFileMsg').addClass("has-error");
                            $('#templateFileMsg').html('The template file must be an excel file (.xlsx format).');
                            hasErrors = 1;
                    }
	            }
	            else {
					if($('#'+headVal+'templateFile').val() == '' || $('#'+headVal+'templateFile').val().indexOf('.xlsx') == -1) {
	                    $('#'+headVal+'templateFileDiv').addClass("has-error");
	                    $('#'+headVal+'templateFileMsg').addClass("has-error");
	                    $('#'+headVal+'templateFileMsg').html('The template file must be an excel file (.xlsx format).');
	                    hasErrors = 1;
	           		 }
	            }

				//Make sure a valid field no is entered
                if(!$.isNumeric($('#'+headVal+'targetOrgColNo').val())) {
                    $('#'+headVal+'targetOrgColNoDiv').addClass("has-error");
                    $('#'+headVal+'targetOrgColNoMsg').addClass("has-error");
                    $('#'+headVal+'targetOrgColNoMsg').html('The target organziation field No must be a numeric value!');
                    hasErrors = 1;
                }
                if(!$.isNumeric($('#'+headVal+'messageTypeColNo').val())) {
                    $('#'+headVal+'messageTypeColNoDiv').addClass("has-error");
                    $('#'+headVal+'messageTypeColNoMsg').addClass("has-error");
                    $('#'+headVal+'messageTypeColNoMsg').html('The message type field No must be a numeric value!');
                    hasErrors = 1;
                }

                //Make sure the file type and delimiter is selected
                if($('#'+headVal+'fileType').val() == '') {
                    $('#'+headVal+'fileTypeDiv').addClass("has-error");
                    $('#'+headVal+'fileTypeMsg').addClass("has-error");
                    $('#'+headVal+'ileTypeMsg').html('The file type is a required field!');
                    hasErrors = 1;
                }
                if($('#'+headVal+'delimiter').val() == '') {
                    $('#'+headVal+'fileDelimDiv').addClass("has-error");
                    $('#'+headVal+'fileDelimMsg').addClass("has-error");
                    $('#'+headVal+'fileDelimMsg').html('The file delimiter is a required field!');
                    hasErrors = 1;
                }
                if(hasErrors == 1) {
                	 $('#collapse'+sectionVal).show();
                 }
			}
			
		});

		return hasErrors;
	}


</script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<div class="main clearfix" role="main" rel="listoforganizationusers">
	<div class="col-md-12">
		
		<c:if test="${not empty param.msg}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose><c:when test="${param.msg == 'updated'}">The system user has been successfully updated!</c:when><c:when test="${param.msg == 'created'}">The system user has been successfully added!</c:when></c:choose>
			</div>
		</c:if>
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">System Users</h3>
			</div>
			<div class="panel-body">
				<div class="table-actions">
					<div class="form form-inline pull-left">
						<form:form class="form form-inline" method="post">
							<div class="form-group">
								<label class="sr-only" for="search-organizations">Search</label>
								<input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-users" placeholder="Search"/>
							</div>
							<button id="searchOrgBtn" class="btn btn-primary btn-sm">
								<span class="glyphicon glyphicon-search"></span>
							</button>
						</form:form>
					</div>
					<a href="#systemUsersModal" id="createNewUser" data-toggle="modal" class="btn btn-primary btn-sm pull-right">
						<span class="glyphicon glyphicon-plus"></span>
					</a>
				</div>

				<div class="form-container scrollable">
					<table class="table table-striped table-hover table-default">
						<thead>
							<tr>
								<th>Name</th>
								<th>Created</th>
								<th>Primary</th>
								<th>Secondary</th>
								<th>Times Logged In</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								  <c:when test="${not empty userList}">
								    <c:forEach var="user" items="${userList}">
										<tr id="userRow">
											<td><a href="#systemUsersModal" data-toggle="modal" rel="${user.firstName}${user.lastName}?i=${user.id}" class="userEdit">${user.firstName} ${user.lastName}</a><br />(<c:choose><c:when test="${user.status == true}">active</c:when><c:otherwise>inactive</c:otherwise></c:choose>)</td>
											<td><fmt:formatDate value="${user.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
											<td>
												
											</td>
											<td>
												
											</td>
											<td>${userFunctions.findTotalLogins(user.id)}</td>
											<td class="actions-col">
												<a href="#systemUsersModal" data-toggle="modal" rel="${user.firstName}${user.lastName}?i=${user.id}" class="btn btn-link userEdit">
													<span class="glyphicon glyphicon-edit"></span>
													Edit	
												</a>
											</td>
										</tr>
									</c:forEach>
								  </c:when>
								  <c:otherwise>
								   	<tr><td colspan="4" style="text-align: center">There are currently no organization users set up.</td></tr>
								  </c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<ul class="pagination pull-right" role="navigation" aria-labelledby="Paging ">
						<c:if test="${currentPage > 1}"><li><a href="?page=${currentPage-1}">&laquo;</a></li></c:if>
						<c:forEach var="i" begin="1" end="${totalPages}">
						<li><a href="?page=${i}">${i}</a></li>
						</c:forEach>
						<c:if test="${currentPage < totalPages}"><li><a href="?page=${currentPage+1}">&raquo;</a></li></c:if>
					</ul>
				</div>
			</div>
		</section>
	</div>		
</div>	
<p rel="${currentPage}" id="currentPageHolder" style="display:none"></p>

<!-- Providers modal -->
<div class="modal fade" id="systemUsersModal" role="dialog" tabindex="-1" aria-labeledby="Add System Users" aria-hidden="true" aria-describedby="Add new system users"></div>

<script>
	$.ajaxSetup({
		cache:false
	});

	//var searchTimeout;

	jQuery(document).ready(function($) {

		if($('.alert').length >0) {
			$('.alert').delay(2000).fadeOut(1000);
		}
				
	    $("input:text,form").attr("autocomplete","off");

	    //This function will launch the new system user overlay with a blank screen
	    $(document).on('click','#createNewUser',function() {
	    	$.ajax({  
		        url: 'newSystemUser',  
		        type: "GET",  
		        success: function(data) {  
		            $("#systemUsersModal").html(data);           
		        }  
		    });  
		});

		//This function will launch the edit system user overlay populating the fields
		//with the data of the clicked user.
		$(document).on('click', '.userEdit',function() {
			var userDetailsAction = $(this).attr('rel');

			$.ajax({  
		        url: userDetailsAction,  
		        type: "GET",  
		        success: function(data) {  
		            $("#systemUsersModal").html(data);           
		        }  
		    });  
		});

		$('#searchOrgBtn').click(function() {
			$('#searchForm').submit();
		});

		/*$("#searchTerm").keyup(function(event) {
			var term = $(this).val();

			if(term.length >= 3 || term.length == 0) {
				if(searchTimeout) {clearTimeout(searchTimeout);}
				searchTimeout = setInterval("orglookup()",500);
			}
		});*/


		//Function to submit the changes to an existing user or 
		//submit the new user fields from the modal window.
		$(document).on('click', '#submitButton',function(event) {
			var currentPage = $('#currentPageHolder').attr('rel');
			var passwordVal = $('#password').val();
			var confirmPasswordVal = $('#confirmPassword').val();

			if(passwordVal != confirmPasswordVal) {
				$('#confirmPasswordDiv').addClass("has-error");	
				$('#passwordDiv').addClass("has-error");
				$('#confimPasswordMsg').addClass("has-error");
				$('#confimPasswordMsg').html('The two passwords do not match.');
				event.preventDefault();
			    return false;
			}
			else {
				var formData = $("#userdetailsform").serialize();  

				var actionValue = $(this).attr('rel').toLowerCase();
				
				$.ajax({  
			        url: actionValue,  
			        data: formData,  
			        type: "POST",  
			        async: false,
			        success: function(data) { 
				        if(data.indexOf('userUpdated') != -1) {
							window.location.href="users?msg=updated&page="+currentPage;
						}
				        else if(data.indexOf('userCreated') != -1) {
							window.location.href="users?msg=created&page="+currentPage;
						}
				        else {
			        		$("#systemUsersModal").html(data);
				        }
			        }  
			    });  
				event.preventDefault();
				return false;
			}

		});
		
	});

	/*function orglookup() {
		if(searchTimeout) {clearTimeout(searchTimeout);}

		var term = $('#searchTerm').val().toLowerCase();

		if(term.length >= 3 || term.length == 0) {
			$('#searchForm').submit();
		}
	}*/

	
</script>

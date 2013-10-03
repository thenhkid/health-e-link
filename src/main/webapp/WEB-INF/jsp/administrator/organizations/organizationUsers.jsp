<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<div class="main clearfix" role="main">
	<div class="col-md-12">
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">System Users</h3>
			</div>
			<div class="panel-body">
				<div class="table-actions">
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
										<tr id="userRow" rel="${user.firstName}${user.lastName}?i=${user.id}" style="cursor: pointer">
											<td>${user.firstName} ${user.lastName}<br />(<c:choose><c:when test="${user.status == true}">active</c:when><c:otherwise>inactive</c:otherwise></c:choose>)</td>
											<td><fmt:formatDate value="${user.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
											<td>
												
											</td>
											<td>
												
											</td>
											<td>${userFunctions.findTotalLogins(user.id)}</td>
											<td class="actions-col">
												<a href="javascript:void(0);" class="btn btn-link">
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
				</div>
			</div>
		</section>
	</div>		
</div>	

<script>
	$.ajaxSetup({
		cache:false
	});

	//var searchTimeout;

	jQuery(document).ready(function($) {		
	    $("input:text,form").attr("autocomplete","off");
	
		$(document).on('click','#userRow',function() {
			window.location.href= $(this).attr('rel')+'/';
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
		
	});

	/*function orglookup() {
		if(searchTimeout) {clearTimeout(searchTimeout);}

		var term = $('#searchTerm').val().toLowerCase();

		if(term.length >= 3 || term.length == 0) {
			$('#searchForm').submit();
		}
	}*/

	
</script>

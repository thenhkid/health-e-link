<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="main clearfix" role="main">
	<div class="col-md-12">
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Organizations</h3>
			</div>
			<div class="panel-body">
				<div style="overflow:hidden; margin-bottom:10px;">
					<div class="form form-search pull-left">
						<form:form id="searchForm" action="/administrator/organizations/list" method="post">
							<input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" placeholder="Search"/>
						</form:form>
					</div>
					<button class="btn btn-primary btn-sm pull-right">
						<span class="glyphicon glyphicon-plus"></span>
					</button>
				</div>

				<div class="form-container scrollable">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<th>Organization Name</th>
								<th>Contact Information</th>
								<th># of Users</th>
								<th># of Configurations</th>
								<th>Date Created</th>
								<th>Access Level</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								  <c:when test="${not empty organizationList}">
								    <c:forEach var="org" items="${organizationList}">
										<tr id="orgRow" rel="${org.cleanURL}" style="cursor: pointer">
											<td>${org.orgName}</td>
											<td>
												${org.address} <c:if test="not empty ${org.address2}">${org.address2}</c:if>
												<br />${org.city} ${org.state}, ${org.postalCode}
											</td>
											<td>1</td>
											<td>2</td>
											<td><fmt:formatDate value="${org.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
											<td>
												<c:choose>
													<c:when test="${org.publicOrg} == 0">
														Public
													</c:when>
													<c:otherwise>
														Private
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								  </c:when>
								  <c:otherwise>
								   	<tr><td colspan="4" style="text-align: center">There are currently no organizations set up.</td></tr>
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

	var searchTimeout;

	jQuery(document).ready(function($) {		
	    $("input:text,form").attr("autocomplete","off");
	
		$(document).on('click','#orgRow',function() {
			window.location.href= $(this).attr('rel')+'/';
		});

		$(document).on('click','.glyphicon-plus',function() {
			window.location.href="create";	
		});

		$("#searchTerm").keyup(function(event) {
			var term = $(this).val();

			if(term.length >= 3 || term.length == 0) {
				if(searchTimeout) {clearTimeout(searchTimeout);}
				searchTimeout = setInterval("orglookup()",500);
			}
		});
		
	});

	function orglookup() {
		if(searchTimeout) {clearTimeout(searchTimeout);}

		var term = $('#searchTerm').val().toLowerCase();

		if(term.length >= 3 || term.length == 0) {
			$('#searchForm').submit();
		}
	}

	
</script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<div class="main clearfix full-width" role="main">
	<div class="col-md-12">
		<section class="panel panel-default">
			<div class="panel-body">
				<div class="table-actions">
					<form:form class="form form-inline" action="/administrator/organizations/list" method="post">
						<div class="form-group">
							<label class="sr-only" for="search-organizations">Search</label>
							<input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-organizations" placeholder="Search"/>
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
								<th>Organization Name ${size}</th>
								<th>Contact Information</th>
								<th># of Users</th>
								<th># of Configurations</th>
								<th>Date Created</th>
								<th>Access Level</th>
								<th></th>
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
											<td>
												${orgFunctions.findTotalUsers(org.id)}
											</td>
											<td>
												#########
											</td>
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
								   	<tr><td colspan="4" style="text-align: center">There are currently no organizations set up.</td></tr>
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

<script>
	$.ajaxSetup({
		cache:false
	});

	//var searchTimeout;

	jQuery(document).ready(function($) {		
	    $("input:text,form").attr("autocomplete","off");
	
		$(document).on('click','#orgRow',function() {
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

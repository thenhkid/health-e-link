<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<div class="main clearfix full-width" role="main">
	<div class="col-md-12">
		
		<c:if test="${not empty savedStatus}" >
			<div class="alert alert-success">
				<strong>Success!</strong> 
				<c:choose>
					<c:when test="${savedStatus == 'updated'}">The field has been successfully updated!</c:when>
					<c:when test="${savedStatus == 'created'}">The field has been successfully added!</c:when>
					<c:when test="${savedStatus == 'deleted'}">The field has been successfully removed!</c:when>
				</c:choose>
			</div>
		</c:if>
	
		<section class="panel panel-default">
			<div class="panel-body">
				<div class="table-actions">
					<form:form class="form form-inline" action="/administrator/sysadmin/list" method="post">
						
						<div class="form-group">
							<label class="sr-only" for="searchTerm">Search</label>
							<input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" placeholder="Search"/>
						</div>
						<button id="searchtableTypeBtn" class="btn btn-primary btn-sm" title="Search for a look up table">
							<span class="glyphicon glyphicon-search"></span>
						</button>
					</form:form>
				</div>

				<div class="form-container scrollable">
					<table class="table table-striped table-hover table-default">
						<thead>
							<tr>
								<th scope="col">Table Name</th>
								<th scope="col" class="center-text">Number of Columns</th>
								<th scope="col" class="center-text">Number of Rows</th>
								<th scope="col">Description</th>
								<th scope="col"></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								  <c:when test="${not empty tableList}">
								    <c:forEach var="tableInfo" items="${tableList}">
										<tr id="tableInfoRow" style="cursor: pointer">
											<td scope="row">
												<a href="std/data/${tableInfo.urlId}" title="Edit this table">${tableInfo.tableName}</a>
											</td>
											<td class="center-text">${tableInfo.columnNum}</td>
											<td class="center-text">${tableInfo.rowNum}</td>
											<td class="center-text">${tableInfo.description}</td>
											<td class="actions-col">
												<a href="details?i=${tableInfo.tableName}" class="btn btn-link" title="Edit this message type">
													<span class="glyphicon glyphicon-edit"></span>
													Edit	
												</a>
											</td>
										</tr>
									</c:forEach>
								  </c:when>
								  <c:otherwise>
								   	<tr><td colspan="4" class="center-text">There are currently no look up tables set up.</td></tr>
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

		//Fade out the updated/created message after being displayed.
		if($('.alert').length >0) {
			$('.alert').delay(2000).fadeOut(1000);
		}
			
	    $("input:text,form").attr("autocomplete","off");
	
		$('#searchOrgBtn').click(function() {
			$('#searchForm').submit();
		});

	});

</script>

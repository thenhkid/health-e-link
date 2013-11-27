<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<div class="main clearfix" role="main" rel="dataForTable">
	<div class="col-md-12">
		
		<c:if test="${not empty savedStatus}" >
			<div class="alert alert-success">
			<c:if test="${savedStatus != 'notDeleted'}" >
				<strong>Success!</strong> 
			</c:if>
				<c:choose>
					<c:when test="${savedStatus == 'updated'}">Data has been successfully updated!</c:when>
					<c:when test="${savedStatus == 'created'}">Data has been successfully added!</c:when>
					<c:when test="${savedStatus == 'deleted'}">Data has been successfully removed!</c:when>
					<c:when test="${savedStatus == 'notDeleted'}">The data is in use and cannot be deleted.  Please set the status to inactive instead.</c:when>
				</c:choose>
			</div>
		</c:if>
		
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title"><c:if test="${not empty tableInfo}">Data for "${tableInfo.displayName}" Table</c:if></h3>
			</div>
			<div class="panel-body">
				<div class="table-actions">
					<div class="form form-inline pull-left">
						<form:form class="form form-inline" action="" method="post">
							<div class="form-group">
								<label class="sr-only" for="searchTerm">Search</label>
								<input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-dataItems" placeholder="Search"/>
							</div>
							<button id="searchdataItemBtn" class="btn btn-primary btn-sm">
								<span class="glyphicon glyphicon-search"></span>
							</button>
						</form:form>
					</div>
					<a href="${tableInfo.urlId}/dataItem.create"  class="btn btn-primary btn-sm pull-right" title="Add data">
						<span class="glyphicon glyphicon-plus"></span>
					</a>
				</div>

				<div class="form-container scrollable">
					<table class="table table-striped table-hover table-default">
						<thead>
							<tr>
								<th scope="col">Universal Translator<br/>Crosswalk Value</th>
								<th scope="col">Display Text</th>
								<th scope="col">Description</th>
								<th scope="col" class="center-text">Date Created</th>
								<th scope="col"></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								  <c:when test="${not empty dataList}">
								    <c:forEach var="dataItem" items="${dataList}">
										<tr id="dataRow">
											<td>${dataItem.id}</td>
											<td scope="row"><a href="${tableInfo.urlId}/tableData.${dataItem.displayText}?i=${dataItem.id}"  title="Edit this data">${dataItem.displayText}</a>
											<br />(<c:choose><c:when test="${dataItem.status == true}">active</c:when><c:otherwise>inactive</c:otherwise></c:choose><c:if test="${dataItem.custom == true}">, custom data</c:if>)</td>
											<td>
											    ${dataItem.description}
											</td>
											<td class="center-text"><fmt:formatDate value="${dataItem.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
											<td class="actions-col">
												<a href="${tableInfo.urlId}/tableData.${dataItem.displayText}?i=${dataItem.id}"  class="btn btn-link" title="Edit this data">
												     <span class="glyphicon glyphicon-edit"></span>
												     Edit	
												</a>
												<a href="javascript:void(0);" rel="${dataItem.id}" class="btn btn-link dataItemDelete" title="Delete this row">
													<span class="glyphicon glyphicon-remove"></span>
													Delete
												</a>
											</td>
										</tr>
									</c:forEach>
								  </c:when>
								  <c:otherwise>
								   	<tr><td colspan="5" class="center-text">There where no items found for this table.</td></tr>
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

<script>
	$.ajaxSetup({
		cache:false
	});

	//var searchTimeout;

	jQuery(document).ready(function($) {

		//Fade out the updated/created message after being displayed.
		if($('.alert').length >0) {
			$('.alert').delay(2000).fadeOut(5000);
		}
				
	    $("input:text,form").attr("autocomplete","off");

	    //This function will launch the new dataItem overlay with a blank screen
	    $(document).on('click','#createNewdataItem',function() {
	    	alert("here");
		   $.ajax({  
		        url: '../../std/data/${tableInfo.urlId}',  
		        type: "GET",  
		        success: function(data) {  
		            $("#systemdataItemsModal").html(data);           
		        }  
		    });  
		});

	    //this function will send them to table view
	    $('#searchdataItemBtn').click(function() {
			$('#searchForm').submit();
		});

		//This function will remove the clicked dataItem 
		$(document).on('click','.dataItemDelete',function() {

			var confirmed = confirm("Are you sure you want to remove this data?");

			if(confirmed) {
				var id = $(this).attr('rel');
				var path = window.location.href + "/delete?i="+id;
				window.location.href= path;
			}
		});

		$('#tableDetailsLink').click(function() {
			window.location.href=window.location.href.replace("data/", "");
		});


		//Function to submit the changes to an existing dataItem or 
		//submit the new dataItem fields from the modal window.
		$(document).on('click', '#submitButton',function(event) {
			var currentPage = $('#currentPageHolder').attr('rel');
			
			var formData = $("#dataItemdetailsform").serialize();  

			var actionValue = $(this).attr('rel').toLowerCase();
			
			$.ajax({  
		        url: actionValue+'dataItem',  
		        data: formData,  
		        type: "POST",  
		        async: false,
		        success: function(data) { 
			       
			        if(data.indexOf('dataItemUpdated') != -1) {
				        if(currentPage > 0) {
				        	window.location.href="dataItems?msg=updated&page="+currentPage;
						}
				        else {
				        	window.location.href="dataItems?msg=updated";
						}
						
					}
			        else if(data.indexOf('dataItemCreated') != -1) {
			        	if(currentPage > 0) {
				        	window.location.href="dataItems?msg=created&page="+currentPage;
						}
				        else {
				        	window.location.href="dataItems?msg=created";
						}
						
					}
			        else {
		        		$("#systemdataItemsModal").html(data);
			        }
		        }  
		    });  
			event.preventDefault();
			return false;

		});
		
	});

	
</script>

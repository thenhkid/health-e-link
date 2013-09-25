<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="main clearfix" role="main">
	<div class="col-md-12">
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Configurations</h3>
			</div>
			<div class="panel-body">
				<div style="overflow:hidden; margin-bottom:10px;">
					<div class="form form-search pull-left">
						<input type="text" class="form-control" placeholder="Search"/>
					</div>
					<button class="btn btn-primary btn-sm pull-right">
						<span class="glyphicon glyphicon-plus"></span>
					</button>
				</div>

				<div class="form-container scrollable">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<th>Message Type</th>
								<th>Type</th>
								<th>Date</th>
								<th>Connections</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								  <c:when test="${not empty configurationList}">
								    <c:forEach var="config" items="${configurationList}">
										<tr id="configRow" rel="${config.id}" style="cursor: pointer">
											<td>${config.configName}</td>
											<td>Target</td>
											<td>8/20/2013</td>
											<td>Nashua Primary Care<br/>St. Joes Hospital</td>
											<!--<td><a href="detail/${config.id}">Edit</a></td>-->
										</tr>
									</c:forEach>
								  </c:when>
								  <c:otherwise>
								   	<tr><td colspan="4" style="text-align: center">There are currently no configurations set up.</td></tr>
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

	jQuery(document).ready(function($) {		
		$(document).on('click','#configRow',function() {
			window.location.href="detail/"+$(this).attr('rel');
		});
	});
</script>

<%-- 
    Document   : auditReport
    Author     : gchan
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="container main-container" role="main">
	<div class="row">
		<div class="col-md-12 page-content">

			<c:choose>
				<c:when test="${hasPermission}">
					<div class="row">
						<div class="col-md-6">
							<h3>Transaction(s) for ${batch.configName}</h3>
							<dl>
								<dd>
									<strong>Date Submitted:</strong>
									<fmt:formatDate value="${batch.dateSubmitted}" type="both"
										dateStyle="long" timeStyle="long" />
								</dd>
								<dd>
									<strong>System ID:</strong> ${batch.utBatchName}
								</dd>
								<dd>
									<strong>Status:</strong> <a href="#statusModal"
										data-toggle="modal" class="btn btn-link viewStatus"
										rel="${batch.statusId}" title="View this Status">${batch.statusId}&nbsp;<span
										class="badge badge-help" data-placement="top" title=""
										data-original-title="">?</span></a>
								</dd>
								<dd>
									<strong>Total Transactions: </strong>${batch.totalRecordCount}
									<strong>Total Errors: </strong>${batch.errorRecordCount}
								</dd>
							</dl>
						</div>
						<div class="col-md-6"></div>
					</div>
					<%--
            <div class="row" style="overflow:hidden; margin-bottom:10px;">
                    <div class="col-md-3">
                        <form:form class="form form-inline" id="searchForm" action="/Health-e-Connect/auditReports" method="post">
                            <div class="form-group">
                                <label class="sr-only" for="searchTerm">Search</label>
                                <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" placeholder="Search"/>
                                <input type="hidden" name="batchId" id="batchId" value="${batch.id}"/>
                                <input type="hidden" name="transactionInId" id="transactionInId" value=""/>
                                <input type="hidden" name="page" id="page" value="${currentPage}" />
                            </div>
                            <button id="searchBatchesBtn" class="btn btn-primary btn-sm" title="Search Errors">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </form:form>
                    </div>

                    <div class="col-md-2 col-md-offset-3"></div>

                    <div class="col-md-4"></div>
            </div>
             --%>
					<div class="form-container scrollable">
						<c:forEach var="confError" items="${confErrorList}">
						<div>
						<h3>${confError.messageTypeName}</h3> 
						</div>
							<table class="table table-striped table-hover table-default">
								<thead>
									<tr>
										<%-- not all will have reportable fields --%>
										
										<th scope="col">Error Code</th>
										<th scope="col">Error Desc</th>
										<%-- doesn't apply to system errors --%>

										
										<%-- doesn't apply to system errors --%>
										<c:if test="${not empty confError.rptFieldHeadings}">
											<th scope="col">Error Field</th>
											<th scope="col">Error Value</th>
											<th scope="col">Go to ERG</th>
										</c:if>
										<%-- if system error --%>
										<c:if test="${empty confError.rptFieldHeadings}">
											<th scope="col">Error</th>
										</c:if>
										

									</tr>
								</thead>
								<tbody>
									<c:forEach var="error" items="${confError.transErrorDetails}">
										<tr>
											<td scope="row">${error.errorCode}&nbsp;</td>
											<td>${error.errorDisplayText}&nbsp;</td>
											
											<c:if test="${not empty confError.rptFieldHeadings}">
												<td>error field</td>
												<td>error value</td>
												<td class="actions-col">&nbsp; 
												<c:if
														test="${not empty error.transactionInId && canEdit}">
														<a href="javascript:void(0);"
															rel="${error.transactionInId}"
															class="btn btn-link viewLink"><span
															class="glyphicon glyphicon-edit"></span>ERG</a>
												
												</c:if>
												</td>
											</c:if>
											<c:if test="${empty confError.rptFieldHeadings}">
												<td>${error.errorData}&nbsp;</td>
											</c:if>
										</tr>
									</c:forEach>
									

								</tbody>
							</table>
						</c:forEach>
						<!--  this will be replaced -->
						<table class="table table-striped table-hover table-default">
								<thead>
									<tr>
										<%-- not all will have reportable fields --%>
										
										<th scope="col">Error Code</th>
										<th scope="col">Error Desc</th>
										<th scope="col">Error Field</th>
										<th scope="col">Error Data</th>
										<th scope="col">ERG</th>
									</tr>
									</thead><tbody>
						<c:forEach var="transError" items="${getErrorList}">
										<tr>
											<td scope="row">${transError.errorId}&nbsp;</td>
											<td>error desc&nbsp;</td>
											<td>${transError.fieldNo}</td>
												<td>error value</td>
												<td class="actions-col">&nbsp; 
												<c:if
														test="${not empty transError.transactionInId && canEdit}">
														<a href="javascript:void(0);"
															rel="${transError.transactionInId}"
															class="btn btn-link viewLink"><span
															class="glyphicon glyphicon-edit"></span>ERG</a>
												
												</c:if>
												</td>
											
											
										</tr>
									</c:forEach>
									</tbody>
						</table>
						<c:if test="${totalPages > 0}">
							<ul class="pagination pull-right" role="navigation"
								aria-labelledby="Paging">
								<c:if test="${currentPage > 1}">
									<li><a href="javascript:void(0);" rel="${currentPage-1}"
										class="changePage">&laquo;</a></li>
								</c:if>
								<c:forEach var="i" begin="1" end="${totalPages}">
									<li><a href="javascript:void(0);" rel="${i}"
										class="changePage">${i}</a></li>
								</c:forEach>
								<c:if test="${currentPage < totalPages}">
									<li><a href="javascript:void(0);" rel="${currentPage+1}"
										class="changePage">&raquo;</a></li>
								</c:if>
							</ul>
						</c:if>
					</div>
					<c:if test="${canSend}">
						<div>
							<input type="button" id="release"
								class="btn btn-primary btn-action-sm submitMessage"
								value="Release" />
						</div>
					</c:if>
					<c:if test="${canCancel}">
						<div>
							<input type="button" id="cancel"
								class="btn btn-primary btn-action-sm cancelMessage"
								value="Do Not Process" />
							<input type="button" id="reset"
								class="btn btn-primary btn-action-sm resetMessage"
								value="Reset" />
						</div>
						
					</c:if>
				</c:when>
				<c:otherwise>
                               You do not have permission to view this audit report.  Your request has been logged.
                            </c:otherwise>
			</c:choose>


		</div>
	</div>
</div>

<div class="modal fade" id="uploadFile" role="dialog" tabindex="-1"
	aria-labeledby="Upload New File" aria-hidden="true"
	aria-describedby="Upload New File"></div>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1"
	aria-labeledby="Status Details" aria-hidden="true"
	aria-describedby="Status Details"></div>
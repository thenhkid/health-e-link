<%-- 
    Document   : auditReport
    Author     : gchan
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

           
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

                    <div class="col-md-4">
                        
                    </div>
            </div>
            
            <div class="form-container scrollable">
               		<c:choose>
                            <c:when test="${hasPermission}">
                             You have permission to view this audit report!
                           
                           
                            
                             <table class="table table-striped table-hover table-default">
			                    <thead>
			                        <tr>
			                            <th scope="col">Errors go here</th>
			                            <th scope="col">Reportable fields</th>			                           
			                            <th scope="col"></th>
			                        </tr>
			                    </thead>
			                    <tbody>
			                    <c:forEach var="error" items="${getErrorList}">
                                    <tr>
                                        <td scope="row">${batch.utBatchName}</td>
                                        <td>
                                            ${batch.originalFileName}
                                        </td>
                                        <td>${error.transactionInId}</td>
                                        <c:if test="${error.transactionInId != 0}">
                                        <td class="actions-col" style="width:50px;">
                                            <a href="javascript:void(0);" rel="${error.transactionInId}" class="btn btn-link viewLink">
                                                <span class="glyphicon glyphicon-edit"></span>
                                                Go to ERG
                                               </a>   
                                        </td>
                                        </c:if>
                                    </tr>  
                                    </c:forEach>
                                  
                                     </tbody>
               					</table>                                
                                
                                <c:if test="${sendBatch}">
                               	<div>
                                 	<input type="button" id="release" class="btn btn-primary btn-action-sm submitMessage" value="Release"/>                                
                               </div>
                                </c:if>    
                            </c:when>
                            <c:otherwise>
                               You do not have permission to view this audit report.  Your request has been logged.
                            </c:otherwise>
                        </c:choose>    
                 
                <c:if test="${totalPages > 0}">            
                    <ul class="pagination pull-right" role="navigation" aria-labelledby="Paging">
                        <c:if test="${currentPage > 1}"><li><a href="javascript:void(0);" rel="${currentPage-1}" class="changePage">&laquo;</a></li></c:if>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                        <li><a href="javascript:void(0);" rel="${i}" class="changePage">${i}</a></li>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}"><li><a href="javascript:void(0);" rel="${currentPage+1}" class="changePage">&raquo;</a></li></c:if>
                    </ul>
                </c:if>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="uploadFile" role="dialog" tabindex="-1" aria-labeledby="Upload New File" aria-hidden="true" aria-describedby="Upload New File"></div>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>
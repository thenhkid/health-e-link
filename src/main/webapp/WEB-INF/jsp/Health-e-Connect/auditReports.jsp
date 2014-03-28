<%-- 
    Document   : auditReports
    Author     : Grace Chan
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
                                <input type="hidden" name="fromDate" id="fromDate" rel="<fmt:formatDate value="${fromDate}" type="date" pattern="MM/dd/yyyy" />" rel2="<fmt:formatDate value="${userDetails.dateOrgWasCreated}" type="date" pattern="MM/dd/yyyy" />" value="${fromDate}" />
                                <input type="hidden" name="toDate" id="toDate" rel="<fmt:formatDate value="${toDate}" type="date" pattern="MM/dd/yyyy" />" value="${toDate}" />
                                <input type="hidden" name="page" id="page" value="${currentPage}" />
                          		<input type="hidden" name="batchId" id="batchId" value=""/>
                            </div>
                            <button id="searchBatchesBtn" class="btn btn-primary btn-sm" title="Search Inbox">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </form:form>
                    </div>

                    <div class="col-md-2 col-md-offset-3"></div>

                    <div class="col-md-4">
                        <div class="date-range-picker-trigger form-control pull-right daterange">
                            <i class="glyphicon glyphicon-calendar"></i>
                            <span class="date-label" rel="" rel2=""><fmt:formatDate value="${fromDate}" type="date" pattern="MMMM dd, yyyy" /> - <fmt:formatDate value="${toDate}" type="date" pattern="MMMM dd, yyyy" /></span> <b class="caret"></b>
                        </div>
                    </div>
            </div>
            
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <thead>
                        <tr>
                            <th scope="col">Batch Name</th>
                            <th scope="col">File Name</th>
                            <th scope="col" class="center-text">Total Transactions</th>
                            <th scope="col" class="center-text">Transactions with Errors</th>
                            <th scope="col" class="center-text">Status</th>
                            <th scope="col" class="center-text">Status Date</th>
                            <th scope="col" class="center-text">File Submission Date</th>
                            <th scope="col" class="center-text">View Audit Report</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty uploadedBatches}">
                                <c:forEach var="batch" items="${uploadedBatches}">
                                    <tr>
                                        <td scope="row">${batch.utBatchName}</td>
                                        <td>
                                            ${batch.originalFileName}
                                        </td>
                                        <td class="center-text">${batch.totalRecordCount}</td>
                                        <td class="center-text">${batch.errorRecordCount}</td>
                                        <td class="center-text">
                                            <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${batch.statusId}" title="View this Status">${batch.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a>
                                        </td>
                                        <td class="center-text"><fmt:formatDate value="${batch.endDateTime}" type="date" pattern="M/dd/yyyy" /><br /><fmt:formatDate value="${batch.endDateTime}" type="time" pattern="h:mm:ss a" /></td>
                                        <td class="center-text"><fmt:formatDate value="${batch.dateSubmitted}" type="date" pattern="M/dd/yyyy" /><br /><fmt:formatDate value="${batch.dateSubmitted}" type="time" pattern="h:mm:ss a" /></td>
                                        <td class="actions-col" style="width:50px;">
                                            <a href="javascript:void(0);" rel="${batch.id}" class="btn btn-link viewLink">
                                                <span class="glyphicon glyphicon-edit"></span>
                                                Detail Audit Report
                                               </a>   
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="9" class="center-text">There are currently no audit reports.</td></tr>
                            </c:otherwise>
                        </c:choose>    
                    </tbody>
                </table>
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
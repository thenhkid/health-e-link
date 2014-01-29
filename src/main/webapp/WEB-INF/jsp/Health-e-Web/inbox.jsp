<%-- 
    Document   : create
    Created on : Dec 12, 2013, 1:12:54 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="urgencyVal" value="Not Reported" />
<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li><a href="#">eRG</a></li>
                <li class="active">Inbox</li>
            </ol>
                
            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    <c:choose>
                        <c:when test="${savedStatus == 'saved'}">Your message has been successfully updated!</c:when>
                    </c:choose>
                </div>
            </c:if>  
                
            <div style="display:none;" class="alert alert-danger" role="alert"></div>    
                
           <div class="" style="overflow:hidden; margin-bottom:10px;">
               <form:form class="form form-inline" id="searchForm" action="/Health-e-Web/inbox" method="post">
                    <div class="form-group">
                        <label class="sr-only" for="searchTerm">Search</label>
                        <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" placeholder="Search"/>
                    </div>
                    <button id="searchBatchesBtn" class="btn btn-primary btn-sm" title="Search Inbox">
                        <span class="glyphicon glyphicon-search"></span>
                    </button>
                </form:form>
            </div>     
            <form action="inbox/batch/Transactions" id="viewBatchTransactions" method="post">
                <input type="hidden" id="batchId" name="batchId" value="" />
            </form>    
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <thead>
                        <tr>
                            <th scope="col">Batch Name</th>
                            <th scope="col" class="center-text">Total Transactions</th>
                            <th scope="col" class="center-text">Status</th>
                            <th scope="col" class="center-text">Submitted By</th>
                            <th scope="col" class="center-text">Date Received</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty inboxBatches}">
                                <c:forEach var="batch" items="${inboxBatches}">
                                    <tr>
                                        <td scope="row">${batch.utBatchName}</td>
                                        <td class="center-text">
                                            ${batch.totalTransactions}
                                        </td>
                                        <td class="center-text">
                                            <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${batch.statusId}" title="View this Status">${batch.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a>
                                        </td>
                                        <td class="center-text">${batch.usersName}</td>
                                        <td class="center-text"><fmt:formatDate value="${batch.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                        <td class="actions-col" style="width:50px;">
                                            <a href="javascript:void(0);" rel="${batch.id}" class="btn btn-link viewLink">
                                                <span class="glyphicon glyphicon-edit"></span>
                                                View
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                           </c:when>
                           <c:otherwise>
                                <tr><td colspan="7" class="center-text">You currently have no messages</td></tr>
                            </c:otherwise>
                      </c:choose>                  
                    </tbody>
                </table>
                <c:if test="${totalPages > 0}">            
                    <ul class="pagination pull-right" role="navigation" aria-labelledby="Paging">
                        <c:if test="${currentPage > 1}"><li><a href="?page=${currentPage-1}">&laquo;</a></li></c:if>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                        <li><a href="?page=${i}">${i}</a></li>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}"><li><a href="?page=${currentPage+1}">&raquo;</a></li></c:if>
                    </ul>
                </c:if>
            </div>
        </div>
    </div>
</div>
<!-- Status Definition modal -->
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>

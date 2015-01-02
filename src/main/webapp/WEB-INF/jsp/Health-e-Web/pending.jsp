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
                <li><a href="<c:url value='/Health-e-Web/inbox'/>">eRG</a></li>
                <li class="active">Pending</li>
            </ol>
                
            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    <c:choose>
                        <c:when test="${savedStatus == 'saved'}">Your message has been successfully saved!</c:when>
                    </c:choose>
                </div>
            </c:if>  
                
            <div style="display:none;" class="alert alert-danger" role="alert"></div> 
            
            <div class="row" style="overflow:hidden;">
               
               <div class="col-md-3">
                    
                   <form:form class="form form-inline" id="searchForm" action="/Health-e-Web/pending" method="post">
                        <div class="form-group">
                            <input type="hidden" name="fromDate" id="fromDate" rel="<fmt:formatDate value="${fromDate}" type="date" pattern="MM/dd/yyyy" />" rel2="<fmt:formatDate value="${userDetails.dateOrgWasCreated}" type="date" pattern="MM/dd/yyyy" />" value="${fromDate}" />
                            <input type="hidden" name="toDate" id="toDate" rel="<fmt:formatDate value="${toDate}" type="date" pattern="MM/dd/yyyy" />" value="${toDate}" />
                            <input type="hidden" name="page" id="page" value="${currentPage}" />
                        </div>
                    </form:form>
                </div>
            </div>    
             
            <form:form action="sendBatches" id="sendMarkedBatches" method="post">
                <input type="hidden" id="batchIdList" name="batchIdList" value="" />
            </form:form>       
            <form:form action="batch/transactions" id="viewBatchTransactions" method="post">
                <input type="hidden" id="batchId" name="batchId" value="" />
                <input type="hidden" name="fromPage" value="pending" />
            </form:form>    
            <div class="form-container scrollable">
                <div class="date-range-picker-trigger form-control pull-right daterange" style="width:265px; margin-left: 10px;">
                    <i class="glyphicon glyphicon-calendar"></i>
                    <span class="date-label"  rel="" rel2=""><fmt:formatDate value="${fromDate}" type="date" pattern="MMM dd, yyyy" /> - <fmt:formatDate value="${toDate}" type="date" pattern="MMM dd, yyyy" /></span> <b class="caret"></b>
                </div>
                <table class="table table-striped table-hover table-default" <c:if test="${not empty pendingBatches}">id="dataTable"</c:if>>
                    <caption style="display:none">Pending Batches</caption>
                    <thead>
                        <tr>
                            <c:if test="${userDetails.deliverAuthority == true}"><th scope="col" class="center-text">Send?</th></c:if>
                            <th scope="col">Batch Name</th>
                            <th scope="col" class="center-text">Total Transactions</th>
                            <th scope="col" class="center-text">System Status</th>
                            <th scope="col" class="center-text">Submitted By</th>
                            <th scope="col" class="center-text">Date Submitted</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty pendingBatches}">
                                <c:forEach var="batch" items="${pendingBatches}">
                                    <tr>
                                        <c:if test="${userDetails.deliverAuthority == true}">
                                            <td class="actions-col center-text">
                                                <c:choose>
                                                    <c:when test="${batch.statusId == 5}">
                                                        <input type="checkbox" id="batchId" name="batchId" class="batchIds" value="${batch.id}" />
                                                    </c:when>
                                                    <c:otherwise>&nbsp;</c:otherwise>
                                                </c:choose>
                                            </td>
                                        </c:if>
                                        <td scope="row">${batch.utBatchName}</td>
                                        <td class="center-text">
                                            ${batch.totalTransactions}
                                        </td>
                                        <td class="center-text">
                                            <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${batch.statusId}" title="View this Status">${batch.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a>
                                        </td>
                                        <td class="center-text">${batch.usersName}</td>
                                        <td class="center-text"><fmt:formatDate value="${batch.dateSubmitted}" type="date" pattern="M/dd/yyyy h:mm:ss a" /></td>
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
                                <tr><td colspan="7" class="center-text">You currently have no pending batches</td></tr>
                            </c:otherwise>
                      </c:choose>                  
                    </tbody>
                </table>
                <div class="row pull-left" style="margin-left: 1px; margin-top: 20px">
                <c:if test="${userDetails.deliverAuthority == true && not empty pendingBatches}">
                <a href="javascript:void(0);" title="Send Batches" class="pull-right btn btn-primary sendBatches"><span class="glyphicon glyphicon-send"></span> Send Marked Batches</a> 
               </c:if>
            </div> 
            </div>
            
        </div>
    </div>
</div>
<!-- Status Definition modal -->
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>

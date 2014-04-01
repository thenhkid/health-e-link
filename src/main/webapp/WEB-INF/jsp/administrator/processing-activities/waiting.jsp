<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="main clearfix" role="main">
    
    <c:if test="${not empty param.msg}" >
        <div class="col-md-12">
            <div class="alert alert-success">
                <strong>Success!</strong> 
                The selected transaction is now being processed.
            </div>
        </div>
    </c:if>
    <div class="row-fluid">
        <div class="col-md-12">
            <section class="panel panel-default">
                <div class="panel-body">
                    <dt>
                        <dt>System Summary:</dt>
                        <dd><strong>Total Transactions to be Processed:</strong> <fmt:formatNumber value="${summaryDetails.batchesToProcess}" /></dd>
                    </dt>
                </div>
            </section>
        </div>
    </div>
    <div class="col-md-12">
         <section class="panel panel-default">
            <div class="panel-body">
                <div class="table-actions">
                    <div class="col-md-3" role="search">
                     <form:form class="form form-inline" id="searchForm" action="/administrator/processing-activity/waiting" method="post">
                            <div class="form-group">
                                <label class="sr-only" for="searchTerm">Search</label>
                                <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-batches" placeholder="Search"/>
                                <input type="hidden" name="fromDate" id="fromDate" rel="<fmt:formatDate value="${fromDate}" type="date" pattern="MM/dd/yyyy" />" rel2="<fmt:formatDate value="${originalDate}" type="date" pattern="MM/dd/yyyy" />" value="${fromDate}" />
                                <input type="hidden" name="toDate" id="toDate" rel="<fmt:formatDate value="${toDate}" type="date" pattern="MM/dd/yyyy" />" value="${toDate}" />
                                <input type="hidden" name="page" id="page" value="${currentPage}" />
                            </div>
                            <button id="searchOrgBtn" class="btn btn-primary btn-sm" title="Search Waiting Transactions" role="button">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </form:form>
                    </div>
                    
                    <div class="col-md-2 col-md-offset-3"></div>

                    <div class="col-md-4">
                        <div class="date-range-picker-trigger form-control pull-right daterange" style="width:245px;">
                            <i class="glyphicon glyphicon-calendar"></i>
                            <span class="date-label"><fmt:formatDate value="${fromDate}" type="date" pattern="MMMM dd, yyyy" /> - <fmt:formatDate value="${toDate}" type="date" pattern="MMMM dd, yyyy" /></span> <b class="caret"></b>
                        </div>
                    </div>
                </div>

                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col">Source Organization</th>
                                <th scope="col">Inbound Batch ID</th>
                                <th scope="col">Target Organization</th>
                                <th scope="col">Message Type</th>
                                <th scope="col" class="center-text">Date Created</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty transactions}">
                                    <c:forEach var="transaction" items="${transactions}">
                                        <tr  style="cursor: pointer">
                                            <td scope="row">
                                                ${transaction.sourceOrgFields[0].fieldValue}
                                                <dd class="adr">
                                                    <span class="street-address">${transaction.sourceOrgFields[1].fieldValue}</span><br/>
                                                    <c:if test="${not empty transaction.sourceOrgFields[2].fieldValue and transaction.sourceOrgFields[2].fieldValue != 'null'}"><span class="street-address">${transaction.sourceOrgFields[2].fieldValue}</span><br/></c:if>
                                                    <c:if test="${not empty transaction.sourceOrgFields[4].fieldValue and transaction.sourceOrgFields[4].fieldValue != 'null'}"><span class="region">${transaction.sourceOrgFields[3].fieldValue}&nbsp;${transaction.sourceOrgFields[4].fieldValue}</span>,&nbsp;</c:if> <span class="postal-code">${transaction.sourceOrgFields[5].fieldValue}</span>
                                                </dd>
                                                <c:if test="${not empty transaction.sourceOrgFields[6].fieldValue and transaction.sourceOrgFields[6].fieldValue != 'null'}"><dd>phone: <span class="tel">${transaction.sourceOrgFields[6].fieldValue}</span></dd></c:if>
                                                <c:if test="${not empty transaction.sourceOrgFields[7].fieldValue and transaction.sourceOrgFields[7].fieldValue != 'null'}"><dd>fax: <span class="tel">${transaction.sourceOrgFields[7].fieldValue}</span></dd></c:if>
                                            </td>
                                            <td>
                                                ${transaction.batchName}
                                            </td>
                                            <td>
                                                ${transaction.targetOrgFields[0].fieldValue}
                                                <dd class="adr">
                                                    <span class="street-address">${transaction.targetOrgFields[1].fieldValue}</span><br/>
                                                    <c:if test="${not empty transaction.targetOrgFields[2].fieldValue and transaction.targetOrgFields[2].fieldValue != 'null'}"><span class="street-address">${transaction.targetOrgFields[2].fieldValue}</span><br/></c:if>
                                                    <c:if test="${not empty transaction.targetOrgFields[4].fieldValue and transaction.targetOrgFields[4].fieldValue != 'null'}"><span class="region">${transaction.targetOrgFields[3].fieldValue}&nbsp;${transaction.targetOrgFields[4].fieldValue}</span>,&nbsp;</c:if> <span class="postal-code">${transaction.targetOrgFields[5].fieldValue}</span>
                                                </dd>
                                                <c:if test="${not empty transaction.targetOrgFields[6].fieldValue and transaction.targetOrgFields[6].fieldValue != 'null'}"><dd>phone: <span class="tel">${transaction.targetOrgFields[6].fieldValue}</span></dd></c:if>
                                                <c:if test="${not empty transaction.targetOrgFields[7].fieldValue and transaction.targetOrgFields[7].fieldValue != 'null'}"><dd>fax: <span class="tel">${transaction.targetOrgFields[7].fieldValue}</span></dd></c:if>
                                           </td>
                                           <td scope="row">${transaction.messageTypeName}</td>
                                           <td class="center-text"><fmt:formatDate value="${transaction.dateSubmitted}" type="date" pattern="M/dd/yyyy" /><br /><fmt:formatDate value="${transaction.dateSubmitted}" type="time" pattern="h:mm:ss a" /></td>
                                            <td class="actions-col">
                                                <a href="javascript:void(0);" class="btn btn-link process" rel="${transaction.transactionRecordId}" title="Process Transaction Now" role="button">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Process Now
                                                </a>
                                            </td>
                                        </tr>
                                   </c:forEach>     
                                 </c:when>   
                                 <c:otherwise>
                                    <tr><td colspan="5" class="center-text">There are currently no transactions waiting to be processed.</td></tr>
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
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>
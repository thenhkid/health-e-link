<%-- 
    Document   : create
    Created on : Dec 12, 2013, 1:12:54 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="urgencyVal" value="Not Reported" />
<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li><a href="#">eRG</a></li>
                <li>
                    <c:choose>
                        <c:when test="${fromPage == 'inbox'}"><a href="<c:url value='/Health-e-Web/inbox'/>">Inbox</a></c:when>
                        <c:when test="${fromPage == 'pending'}"><a href="<c:url value='/Health-e-Web/pending'/>">Pending Batches</a></c:when>
			<c:otherwise><a href="<c:url value='/Health-e-Web/sent'/>">Sent Batches</a></c:otherwise>
                    </c:choose>
                </li>
                <li class="active">Referral #${batchDetails.utBatchName}</li>
            </ol>

            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    <c:choose>
                        <c:when test="${savedStatus == 'saved'}">Your message has been successfully saved!</c:when>
                    </c:choose>
                </div>
            </c:if>    

            <form action="" id="viewTransactionDetails" method="post">
                <input type="hidden" id="transactionId" name="transactionId" value="" />
                <input type="hidden" id="fromPage" name="fromPage" value="${fromPage}" />
                <input type="hidden" id="configId" name="configId" value="" />
            </form>    
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <thead>
                        <tr>
                            <th scope="col">Message Type</th>
                            <th scope="col">Patient Information</th>
                            <th scope="col">Source Organization</th>
                            <th scope="col">Target Organization</th>
                            <th scope="col" class="center-text">Status</th>
                            <th scope="col" class="center-text">Date Submitted</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty transactions}">
                                <c:forEach var="transaction" items="${transactions}">
                                    <tr>
                                        <td scope="row">${transaction.messageTypeName}</td>
                                        <td>
                                            ${transaction.patientFields[0].fieldValue}&nbsp;${transaction.patientFields[1].fieldValue}
                                            <dd class="adr">
                                                <span class="street-address">${transaction.patientFields[4].fieldValue}</span><br/>
                                                <c:if test="${not empty transaction.patientFields[5].fieldValue}"><span class="street-address">${transaction.patientFields[5].fieldValue}</span><br/></c:if>
                                                <span class="region">${transaction.patientFields[6].fieldValue}&nbsp;${transaction.patientFields[7].fieldValue}</span>, <span class="postal-code">${transaction.patientFields[8].fieldValue}</span>
                                            </dd>
                                        </td>
                                        <td>
                                            ${transaction.sourceOrgFields[0].fieldValue}
                                            <dd class="adr">
                                                <span class="street-address">${transaction.sourceOrgFields[1].fieldValue}</span><br/>
                                                <c:if test="${not empty transaction.sourceOrgFields[2].fieldValue}"><span class="street-address">${transaction.sourceOrgFields[2].fieldValue}</span><br/></c:if>
                                                <span class="region">${transaction.sourceOrgFields[3].fieldValue}&nbsp;${transaction.sourceOrgFields[4].fieldValue}</span>, <span class="postal-code">${transaction.sourceOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transaction.sourceOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transaction.sourceOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transaction.sourceOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transaction.sourceOrgFields[7].fieldValue}</span></dd></c:if>
                                        </td>
                                        <td>
                                            ${transaction.targetOrgFields[0].fieldValue}
                                            <dd class="adr">
                                                <span class="street-address">${transaction.targetOrgFields[1].fieldValue}</span><br/>
                                                <c:if test="${not empty transaction.targetOrgFields[2].fieldValue}"><span class="street-address">${transaction.targetOrgFields[2].fieldValue}</span><br/></c:if>
                                                <span class="region">${transaction.targetOrgFields[3].fieldValue}&nbsp;${transaction.targetOrgFields[4].fieldValue}</span>, <span class="postal-code">${transaction.targetOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transaction.targetOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transaction.targetOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transaction.targetOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transaction.targetOrgFields[7].fieldValue}</span></dd></c:if>
                                        </td>
                                       <td class="center-text">
                                           <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${transaction.statusId}" title="View this Status">${transaction.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a>
                                       </td>
                                       <td class="center-text"><fmt:formatDate value="${transaction.dateSubmitted}" type="date" pattern="M/dd/yyyy" /></td>
                                       <td class="actions-col" style="width:50px;">
                                           <a href="javascript:void(0);" rel="${transaction.transactionRecordId}" rel3="${userDetails.editAuthority}" rel2="${transaction.configId}" class="btn btn-link viewLink">
                                            <span class="glyphicon glyphicon-edit"></span>
                                            View
                                          </a>
                                      </td>
                                  </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr><td colspan="7" class="center-text">There are currently no transactions for this batch.</td></tr>
                        </c:otherwise>
                    </c:choose>                  
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- Status Definition modal -->
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>

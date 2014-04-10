<%-- 
    Document   : auditReport
    Author     : gchan
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <div style="display:none;" class="alert alert-danger" role="alert"></div> 
            
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
                                    <strong>Batch Name:</strong> ${batch.utBatchName}
                                </dd>
                                <dd>
                                    <strong>Status:</strong> <a href="#statusModal"
                                                                data-toggle="modal" class="btn btn-link viewStatus"
                                                                rel="${batch.statusId}" title="View this Status">${batch.statusValue}&nbsp;<span
                                            class="badge badge-help" data-placement="top" title=""
                                            data-original-title="">?</span></a>
                                </dd>
                                <dd>
                                    <strong>Total Transactions: </strong>${batch.totalRecordCount}
                                </dd>
                                 <dd>
                                    <strong>Transactions with errors: </strong>${batch.errorRecordCount}
                                </dd>
                            </dl>
                        </div>
                        <div class="col-md-2 col-md-offset-3"></div>

                        <div class="col-md-4">
                            <h4>Originating Organization: </h4>
                            <dl class="vcard">
                                <dd class="fn">${org.orgName}</dd>
                                <dd class="adr">
                                    <span class="street-address">${org.address}</span><c:if test="${not empty org.address2}"><span class="street-address"> ${org.address2}</span></c:if><br/>
                                    <span class="region">${org.city}&nbsp;${org.state}</span>, <span class="postal-code">${org.postalCode}</span>
                                </dd>
                                <c:if test="${not empty org.phone}"><dd>phone: <span class="tel">${org.phone}</span></dd></c:if>
                                <c:if test="${not empty org.fax}"><dd>fax: <span class="tel">${org.fax}</span></dd></c:if>
                                </dl>

                            </div>
                        </div>


                        <div class="row" style="overflow:hidden; margin-bottom:10px;">
                            <div class="col-md-3">
                            <form:form class="form form-inline" id="transAction" action="/Health-e-Connect/edit" method="post">
                                <input type="hidden" name="transactionInId" id="transactionInId" value=""/>
                                <input type="hidden" name="batchIdERG" id="batchIdERG" value="${batch.statusId}"/>
                            </form:form>
                        </div>

                        <div class="col-md-2 col-md-offset-3"></div>

                        <div class="col-md-4"></div>
                    </div>

                    <div class="form-container scrollable">
                        <c:forEach var="confError" items="${confErrorList}">
                            <c:set var="rptField" value="${fn:trim(confError.rptFieldHeading1)}"/>
                            <div>
                                <h3>${confError.messageTypeName}</h3> 
                            </div>
                            <table class="table table-striped table-hover table-default">
                                <thead>
                                    <tr>
                                        <c:choose>

                                            <c:when test="${confError.errorViewId == 1}">
                                                <th scope="col">Error Code</th>
                                                <th scope="col">Error Desc</th>
                                                <th scope="col">Error Info</th>
                                                </c:when>

                                            <c:when test="${batch.statusId == 5 && confError.errorViewId != 2}">
                                                <th scope="col">Reject?</th>
                                                <th scope="col">Transaction Status</th>
                                                <th scope="col">${confError.rptFieldHeading1}</th>
                                                <th scope="col">${confError.rptFieldHeading2}</th>
                                                <th scope="col">${confError.rptFieldHeading3}</th>
                                                <th scope="col">${confError.rptFieldHeading4}</th>
                                                <th scope="col">Error Code</th>
                                                <th scope="col">Error Desc</th>
                                                <th scope="col">Field Label</th>
                                                <th scope="col">Error Data</th>
                                                <th scope="col" class="center-text">&nbsp;</th>		
                                                </c:when>

                                            <c:otherwise>
                                            	<th scope="col">Transaction Status</th>
                                                <th scope="col">${confError.rptFieldHeading1}</th>
                                                <th scope="col">${confError.rptFieldHeading2}</th>
                                                <th scope="col">${confError.rptFieldHeading3}</th>
                                                <th scope="col">${confError.rptFieldHeading4}</th>
                                                <th scope="col">Error Code</th>
                                                <th scope="col">Error Desc</th>
                                                    <c:if test="${confError.errorViewId != 2}">
                                                    <th scope="col">Field Label</th>
                                                    <th scope="col">Error Data</th>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="newTransId" value="0"/>
                                    <c:forEach var="error" items="${confError.transErrorDetails}">

                                        <tr>
                                            <c:choose>
                                                <c:when test="${confError.errorViewId == 1}">
                                                    <td class="center-text">${error.errorCode}</td>
                                                    <td>${error.errorDisplayText}</td>
                                                    <td>${error.errorData}</td>
                                                </c:when>

                                                <c:when test="${batch.statusId == 5 && confError.errorViewId != 2}">
                                                    <td class="center-text">
                                                        <c:if test="${error.transactionInId != newTransId}">
                                                            <c:if test="${not empty error.transactionInId && canEdit && error.transactionStatus == 14}">
                                                                <input type="checkbox" id="rejTransInId" name="rejTransInId" class="rejTransInId" value="${error.transactionInId}" />
                                                            </c:if>
                                                        </c:if>
                                                    </td>
                                                    <td><a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${error.transactionStatus}" title="View this Status">${error.transactionStatusValue}&nbsp;
                                                            <span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a></td>
                                                    <td>${error.rptField1Value}</td>
                                                    <td>${error.rptField2Value}</td>
                                                    <td>${error.rptField3Value}</td>
                                                    <td>${error.rptField4Value}</td>
                                                    <td class="center-text">${error.errorCode}</td>
                                                    <td>${error.errorDisplayText}<c:if test="${not empty error.errorInfo}">${error.errorInfo}</c:if></td>
                                                    <td>${error.errorFieldLabel}</td>
                                                    <td>${error.errorData}</td>
                                                    <td>&nbsp;
                                                        <c:if test="${error.transactionInId != newTransId}">
                                                            <c:if test="${not empty error.transactionInId && canEdit && error.transactionStatus == 14}">
                                                                <a href="javascript:void(0);"
                                                                   rel="${error.transactionInId}"
                                                                   class="btn btn-link viewLink"><span
                                                                        class="glyphicon glyphicon-edit"></span>ERG</a>
                                                                </c:if>
                                                            </c:if>
                                                    </td>
                                                </c:when>

                                                <c:otherwise>
                                                    <td><a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${error.transactionStatus}" title="View this Status">${error.transactionStatusValue}&nbsp;
                                                            <span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a></td>
                                                    <td>${error.rptField1Value}</td>
                                                    <td>${error.rptField2Value}</td>
                                                    <td>${error.rptField3Value}</td>
                                                    <td>${error.rptField4Value}</td>
                                                    <td class="center-text">${error.errorCode}</td>
                                                    <td>${error.errorDisplayText} ${error.errorInfo}</td>
                                                    <c:if test="${confError.errorViewId != 2}">
                                                        <td>${error.errorFieldLabel}</td>
                                                        <td>${error.errorData}</td>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                        <c:set var="newTransId" value="${error.transactionInId}"/> 
                                    </c:forEach>


                                </tbody>
                            </table>
                        </c:forEach>

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
                    <div>

                        <c:if test="${canEdit == true}">
                            <input type="button" id="reject"
                                   class="btn btn-primary btn-action-sm rejectMessages"
                                   value="Reject Marked Transactions" />
                        </c:if>
                        <c:if test="${canSend == true}">
                            <input type="button" id="release"
                                   class="btn btn-primary btn-action-sm releaseBatch"
                                   value="Release" />
                        </c:if>
                        <c:if test="${canCancel == true}">
                            <input type="button" id="cancel"
                                   class="btn btn-primary btn-action-sm cancelBatch"
                                   value="Do Not Process" />
                            <input type="button" id="reset"
                                   class="btn btn-primary btn-action-sm resetBatch"
                                   value="Reset" />
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    You do not have permission to view this audit report.  Your request has been logged.
                </c:otherwise>
            </c:choose>


        </div>
    </div>
</div>

<form action="batchOptions" id="batchOptions" method="post">
    <input type="hidden" id="idList" name="idList" value="" />
    <input type="hidden" name="batchId" id="batchId" value="${batch.id}"/> 
    <input type="hidden" name="batchOption" id="batchOption" value=""/>                    
</form>

<div class="modal fade" id="uploadFile" role="dialog" tabindex="-1"
     aria-labeledby="Upload New File" aria-hidden="true"
     aria-describedby="Upload New File"></div>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1"
     aria-labeledby="Status Details" aria-hidden="true"
     aria-describedby="Status Details"></div>
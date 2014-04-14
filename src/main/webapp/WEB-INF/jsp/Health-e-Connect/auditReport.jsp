<%--
    Document   : auditReport
    Author     : gchan
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="transactionCounter" value="1"/>
<c:set var="newTransId" value="0"/>
<c:set var="idList" value=""/>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <div style="display:none;" class="alert alert-danger" role="alert"></div>

            <c:choose>
                <c:when test="${hasPermission}">
                    <div class="row">
                        <div class="col-md-12" >
                            <h3>Transaction(s) for ${batch.configName}</h3>
                        </div>
                    </div>
                    <div class="row"> 
                        <div class="col-md-6" >
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
                                    <strong>Status:</strong> 
                                    <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${batch.statusId}" title="View this Status">
                                        ${batch.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span>
                                    </a>
                                </dd>
                                <dd>
                                    <strong>Total Transactions: </strong>${batch.totalRecordCount}
                                </dd>
                                <dd>
                                    <strong>Transactions with errors: </strong>${batch.errorRecordCount}
                                </dd>
                            </dl>
                        </div>
                        <div class="col-md-6">
                            <div class="pull-right">
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
                        </div>

                        <div class="row" style="overflow:hidden; margin-bottom:10px;">
                        <form:form class="form form-inline" id="transAction" action="/Health-e-Connect/edit" method="post">
                            <input type="hidden" name="transactionInId" id="transactionInId" value=""/>
                            <input type="hidden" name="batchIdERG" id="batchIdERG" value="${batch.statusId}"/>
                        </form:form>
                    </div>

                    <div class="row" style="overflow:hidden;margin-top:10px; margin-bottom:20px;">
                        <div class="col-md-12">
                            	<div class="pull-left">
	                                <c:if test="${canEdit == true}">
	                                    <input type="button" id="reject" class="btn btn-primary btn-xs rejectMessages" value="Reject All Transactions" />
	                                </c:if>   
	                                <c:if test="${canSend == true}">
                                        <input type="button" id="release" class="btn btn-primary btn-xs releaseBatch" value="Release" />
                                    </c:if>
                                    <c:if test="${canCancel == true}">
                                        <input type="button" id="cancel" class="btn btn-primary btn-xs cancelBatch" value="Do Not Process" />
                                        <input type="button" id="reset" class="btn btn-primary btn-xs resetBatch" value="Reset" />
                                    </c:if>
                                </div>
                        </div>
                    </div>

                    <c:forEach var="transactionIn" items="${errorList}">
                        <div class="col-md-12">
                            <section class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="pull-right" style="margin-top: -5px">
                                        <c:if test="${transactionIn.transactionStatus == 14 && batch.statusId == 5 && canEdit}">
                                        	<c:set var="idList" value="${idList},${transactionIn.transactionInId}"/>
                                            <a href="javascript:void(0);" id="reject" rel="${transactionIn.transactionInId}" rel2="${batch.id}" class="btn btn-primary btn-xs rejectMessage">Reject</a>
                                            <a href="javascript:void(0);" id="fixErrors"  rel="${transactionIn.transactionInId}" class="btn btn-primary btn-xs fixErrors">Fix Errors</a>
                                        </c:if>
                                    </div>
                                    <h3 class="panel-title">Transaction ${transactionCounter}</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="form-container scrollable">
                                        <div>
                                            <table class="table table-striped table-hover table-default">
                                                <thead>
                                                    <tr>
                                                        <th scope="col">Error Desc</th>
                                                        <th scope="col">${transactionIn.rptField1Label}</th>
                                                        <th scope="col">${transactionIn.rptField2Label}</th>
                                                        <th scope="col">${transactionIn.rptField3Label}</th>
                                                        <th scope="col">${transactionIn.rptField4Label}</th>
                                                        <th scope="col">Field Label</th>
                                                        <th scope="col">Error Data</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="error" items="${transactionIn.tedList}">
                                                        <tr>
                                                            <td scope="col">${error.errorDisplayText}<c:if test="${not empty error.errorInfo}">${error.errorInfo}</c:if></td>
                                                            <td scope="col">${error.rptField1Value}</td>
                                                            <td scope="col">${error.rptField2Value}</td>
                                                            <td scope="col">${error.rptField3Value}</td>
                                                            <td scope="col">${error.rptField4Value}</td>
                                                            <td>${error.errorFieldLabel}</td>
                                                            <td>${error.errorData}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </div> 
                        <c:set var="transactionCounter" value="${transactionCounter + 1}"/>
                    </c:forEach>
                                       <div class="row" style="overflow:hidden;margin-top:10px; margin-bottom:20px;">
                        <div class="col-md-12">
                            	<div class="pull-left">
	                                <c:if test="${canEdit == true}">
	                                    <input type="button" id="reject" class="btn btn-primary btn-xs rejectMessages" value="Reject All Transactions" />
	                                </c:if>   
	                                <c:if test="${canSend == true}">
                                        <input type="button" id="release" class="btn btn-primary btn-xs releaseBatch" value="Release" />
                                    </c:if>
                                    <c:if test="${canCancel == true}">
                                        <input type="button" id="cancel" class="btn btn-primary btn-xs cancelBatch" value="Do Not Process" />
                                        <input type="button" id="reset" class="btn btn-primary btn-xs resetBatch" value="Reset" />
                                    </c:if>
                                </div>
                        </div>
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
    <input type="hidden" id="idList" name="idList" value="${fn:substring(idList,1,fn:length(idList))}" />
    <input type="hidden" name="batchId" id="batchId" value="${batch.id}"/>
    <input type="hidden" name="batchOption" id="batchOption" value=""/>
</form>
<form action="auditReport" id="viewBatchAuditReport" method="post">
    <input type="hidden" id="auditbatchId" name="batchId" value="${batch.id}" />
</form>

<div class="modal fade" id="uploadFile" role="dialog" tabindex="-1"
     aria-labeledby="Upload New File" aria-hidden="true"
     aria-describedby="Upload New File"></div>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1"
     aria-labeledby="Status Details" aria-hidden="true"
     aria-describedby="Status Details"></div>
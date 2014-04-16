<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="main clearfix" role="main">
    <div class="row-fluid">
        <div class="col-md-12">
            <section class="panel panel-default">
                <div class="panel-body">
                    <dt>
                        <dt>Batch Summary:</dt>
                        <dd><strong>Batch ID:</strong> ${batchDetails.utBatchName}</dd>
                        <dd><strong>Source Organization:</strong> ${batchDetails.orgName}</dd>
                        <dd><strong>Date Submitted:</strong> <fmt:formatDate value="${batchDetails.dateSubmitted}" type="date" pattern="M/dd/yyyy" />&nbsp;&nbsp;<fmt:formatDate value="${batchDetails.dateSubmitted}" type="time" pattern="h:mm:ss a" /></dd>
                    	<dd><strong>Total Transactions:</strong> ${batchDetails.totalRecordCount}</dd>
                    	<dd><strong>Transactions with errors:</strong> ${batchDetails.errorRecordCount}</dd>
                    	<dd><strong>Batch Status:</strong><a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${batchDetails.statusId}" title="View this Status">${batchDetails.statusValue}</a></dd>
                    </dt>
                </div>
            </section>
        </div>
    </div>
    <div class="col-md-12">
              <div class="form-container scrollable">
  				<c:choose>
  				<c:when test="${fn:length(errorList) > 0}">
  				<c:set var="transactionCounter" value="1"/>
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
                                    <h3 class="panel-title">Transaction ${transactionCounter} - ${transactionIn.transactionInId}</h3>
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
                                                            <td scope="col">${error.errorDisplayText}<c:if test="${not empty error.errorInfo}">-${error.errorInfo}</c:if></td>
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
  				</c:when>
  				<c:otherwise>
  					This batch does not have any errors or the audit report it not ready.
  				</c:otherwise>
  				</c:choose>
                </div>

    </div>
</div>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>
<div class="modal fade" id="messageDetailsModal" role="dialog" tabindex="-1" aria-labeledby="Message Details" aria-hidden="true" aria-describedby="Message Details"></div>
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
              <c:if test="${empty doesNotExist}">
              	<div class="row" style="overflow:hidden;margin-left:0px;margin-right:0px;margin-top:10px; margin-bottom:20px;">
	  				 	<div class="pull-left">
	  				 		<c:if test="${batchDetails.statusId == 2}">
	  				 			<input type="button" id="processBatch" class="btn btn-primary btn-xs processBatch" rel="processBatch" rel2="${batchDetails.id}" value="Load Batch" />
	  				 		</c:if>
	  				 		<c:if test="${batchDetails.statusId == 3}">
	  				 			<input type="button" id="processBatch" class="btn btn-primary btn-xs processBatch" rel="processBatch" rel2="${batchDetails.id}" value="Process Batch" />
	  				 		</c:if>
	  				 		<c:if test="${canCancel}">
	                       		<input type="button" id="cancel" class="btn btn-primary btn-xs cancelBatch" rel="cancel"  rel2="${batchDetails.id}"value="Do Not Process" />
	                       	</c:if>
	                        <c:if test="${canReset}">
	                          	<input type="button" id="reset" class="btn btn-primary btn-xs resetBatch" rel="reset"  rel2="${batchDetails.id}" value="Reset" />
		       				</c:if>
		       			</div>
	                </div>	
             	</c:if>
              
  				<c:choose>
  				<c:when test="${fn:length(errorList) > 0}">
  				<c:set var="transactionCounter" value="1"/>
  				<c:forEach var="transactionIn" items="${errorList}">
                        <div>
                           <section class="panel panel-default">
                                <div class="panel-heading">
                                	<%-- don't think admin should be able to reject / fix messages, should show them status as some 
                                	transaction are invalid, some are pass, some are errored, some might be rejected --%>
                                    <div class="pull-right" style="margin-top: -5px">Status:
                                        <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${transactionIn.transactionStatus}" title="View this Status">${transactionIn.transactionStatusValue}</a>
                                        <a href="#messageDetailsModal" data-toggle="modal" rel="${transactionIn.transactionInId}" rel2="0" class="viewLink">
	                                     <span class="glyphicon glyphicon-edit"></span>
	                                     View
	                                    </a>
                                    </div>
                                    <h3 class="panel-title">Transaction ${transactionCounter}</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="form-container scrollable">
                                    	<!-- work out what buttons to show gsc 04152014 -->
                                    	
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
  				<c:when test="${not empty doesNotExist}">
  					<section class="panel panel-default">
                                <div class="panel-heading">
  						<strong>This batch does not exist.</strong>
  					</div>
  					</section>
  				</c:when>
  				<c:otherwise>
  					<section class="panel panel-default">
                                <div class="panel-heading">
  						<strong>There are currently no errors for this batch.</strong>
  					</div>
  					</section>
  				</c:otherwise>
  				</c:choose>
                </div>

    </div>
</div>

<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>
<div class="modal fade" id="messageDetailsModal" role="dialog" tabindex="-1" aria-labeledby="Message Details" aria-hidden="true" aria-describedby="Message Details"></div>
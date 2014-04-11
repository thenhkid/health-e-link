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
                    
                    
					 <div class="row" style="overflow:hidden;margin-top:10px; margin-bottom:20px;">
                        <c:if test="${canEdit == true}">
	                        <div class="col-md-4">
	                            <input type="button" id="reject"
	                                   class="btn btn-primary btn-action-sm rejectMessages"
	                                   value="Reject All Transactions" />
	                        </div>
                        </c:if>
                        <c:if test="${canSend == true}">
	                        <div class="col-md-4">
	                            <input type="button" id="release"
	                                   class="btn btn-primary btn-action-sm releaseBatch"
	                                   value="Release" />
	                         </div>         
                        </c:if>
                        <c:if test="${canCancel == true}">
                        <div class="col-md-3">
                            <input type="button" id="cancel"
                                   class="btn btn-primary btn-action-sm cancelBatch"
                                   value="Do Not Process" />
                         </div> 
                         <div class="col-md-3">       
                            <input type="button" id="reset"
                                   class="btn btn-primary btn-action-sm resetBatch"
                                   value="Reset" />
                          </div>         
                        </c:if>
                    </div>
                   
                   
                   
                    <c:set var="transactionCounter" value="1"/>
                    <c:set var="newTransId" value="0"/>
                    <c:set var="idList" value=""/>
                     
                     <c:forEach var="transactionIn" items="${errorList}">
                    	<div class="row" style="overflow:hidden;margin-top:10px; margin-bottom:20px;"> 
                    	   <div class="col-md-4">
	                    	   <h3>Transaction ${transactionCounter}</h3>
	                    	   <c:if test="${transactionIn.transactionStatus != 0}"> <strong>Status:</strong> <a href="#statusModal"
                                                                data-toggle="modal" class="btn btn-link viewStatus"
                                                                rel="${transactionIn.transactionStatus}" title="View this Status">${transactionIn.transactionStatusValue}&nbsp;<span
                                            class="badge badge-help" data-placement="top" title=""
                                            data-original-title="">?</span></a></c:if>
                                
	                    	   <c:set var="transactionCounter" value="${transactionCounter + 1}"/>
                    	   </div>
                    	<c:if test="${transactionIn.transactionStatus == 14 && batch.statusId == 5 && canEdit}">            
								<div class="col-md-4">
								<input type="button" id="reject" rel="${transactionIn.transactionInId}"
								rel2="${batch.id}"
				                                   class="btn btn-primary btn-action-sm rejectMessage"
				                                   value="Reject" />
				                 </div>
				                 <div class="col-md-4">                 
				               		 <input type="button" id="fixErrors"  rel="${transactionIn.transactionInId}"
						                                   class="btn btn-primary btn-action-sm fixErrors"
						                                   value="Fix Errors" />                        
								</div>
							</c:if> 
							</div>
					<div class="row">
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
		</c:forEach> 
    	              
                            
                          

                       
                   
                    
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
<form action="auditReport" id="viewBatchAuditReport" method="post">
                <input type="hidden" id="auditbatchId" name="batchId" value="${batch.id}" />
</form>

<div class="modal fade" id="uploadFile" role="dialog" tabindex="-1"
     aria-labeledby="Upload New File" aria-hidden="true"
     aria-describedby="Upload New File"></div>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1"
     aria-labeledby="Status Details" aria-hidden="true"
     aria-describedby="Status Details"></div>
<%-- 
    Document   : messageDetailsForm
    Created on : Dec 12, 2013, 2:47:34 PM
    Author     : chadmccue
    Description: 
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li><a href="<c:url value='/CareConnector/inbox'/>">CC</a></li>
                <li>
                    <c:choose>
                        <c:when test="${fromPage == 'inbox'}"><a href="<c:url value='/CareConnector/inbox'/>">Inbox</a></c:when>
                        <c:when test="${fromPage == 'pending'}"><a href="<c:url value='/CareConnector/pending'/>">Pending</a></c:when>
                        <c:otherwise><a href="<c:url value='/CareConnector/sent'/>">Sent</a></c:otherwise>
                    </c:choose>
                </li>
                <li class="active">Batch #${transactionDetails.batchName}</li>
            </ol>

            <div class="row">
                <div class="col-md-6">
                    <h3>${transactionDetails.messageTypeName} Transaction</h3>
                    <dl>
                        <dd><strong>Date Submitted:</strong> <fmt:formatDate value="${transactionDetails.dateSubmitted}" type="both" dateStyle="long" timeStyle="long" /></dd>
                        <dd><strong>Batch ID:</strong> ${transactionDetails.batchName}</dd>
                        <dd><strong>Transaction Type:</strong> <c:choose><c:when test="${transactionDetails.sourceType == 1}">Original Message</c:when><c:otherwise>Feedback Report</c:otherwise></c:choose></dd>
                        <dd><strong>System Status:</strong> <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${transactionDetails.statusId}" title="View this Status">${transactionDetails.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a></dd>
                    </dl>
                </div>
                <div class="col-md-6"></div>
            </div>
            <div class="well">
                <div class="row form-inline">
                    <div class="form-group col-sm-4">
                        <%-- If viewing an inbox message (not feedback report) need to give the ability
                        to change the internal status of the message --%>
                        <c:if test="${fromPage == 'inbox' && transactionDetails.sourceType == 1}">
                            <label class="sr-only">Select Action</label>
                            <select class="form-control" id="internalStatus" rel="${transactionDetails.transactionId}">
                                <option value="">-Select Status-</option>
                                <c:forEach items="${internalStatusCodes}"  varStatus="sCode">
                                    <option value="${internalStatusCodes[sCode.index][0]}" <c:if test="${transactionDetails.internalStatusId == internalStatusCodes[sCode.index][0]}">selected</c:if>>${internalStatusCodes[sCode.index][1]}</option>
                                </c:forEach>
                            </select>
                        </c:if>
                    </div>
                    <div class="form-group col-sm-4 col-sm-offset-4">
                        <label class="sr-only">Select Action</label>
                        <select class="form-control" id="formAction" rel="${transactionDetails.orginialTransactionId}">
                            <option value="">-Select Action-</option>
                            <option value="print">Print / Save As</option>
                            <c:if test="${transactionDetails.sourceType == 1}"><option value="feedbackReports">View Feedback Reports</option></c:if>
                            <c:if test="${feedbackConfigId > 0 and transactionDetails.messageStatus == 1}"><option value="${feedbackConfigId}">Create Feedback Report</option></c:if>
                            <c:if test="${transactionDetails.sourceType == 2}"><option value="originalReferral">View Original Referral</option></c:if>
                        </select>
                  </div>
               </div>
            </div>    
            <form:form action="/CareConnector/feedbackReport/details" id="newFeedbackForm" modelAttribute="transactionDetails" method="post">
                <form:hidden path="transactionId" id="transactionId" />
                <input type="hidden" id="feedbackConfigId" name="configId" value="" />
            </form:form>               
            <form:form action="/CareConnector/inbox/messageDetails" id="viewTransactionDetails" modelAttribute="transactionDetails" method="post">
                <form:hidden path="transactionId" class="transactionId" id="transactionId" />
                <input type="hidden" name="fromPage" id="fromPage" value="${fromPage}" />
            </form:form>               
            <form:form id="messageForm" action="/CareConnector/submitMessage" modelAttribute="transactionDetails" role="form" class="form" method="post">
                <form:hidden path="transactionId" id="transactionId" />
                <form:hidden path="transactionTargetId" id="transactionTargetId" />
                <input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
                <input type="hidden" name="sentTransactionId" id="sentTransactionId" value="${transactionInId}" />
                <div class="panel-group form-accordion" id="accordion">
                    <div class="panel panel-default panel-form">
                        <div id="collapseThree" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <div class="form-section row">
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Originating Organization: </h4>
                                        <dl class="vcard">
                                            <dd class="fn">${transactionDetails.sourceOrgFields[0].fieldValue}</dd>
                                            <dd class="adr">
                                                <span class="street-address">${transactionDetails.sourceOrgFields[1].fieldValue}</span><c:if test="${not empty transactionDetails.sourceOrgFields[2].fieldValue}"><span class="street-address"> ${transactionDetails.sourceOrgFields[2].fieldValue}</span></c:if><br/>
                                                <span class="region">${transactionDetails.sourceOrgFields[3].fieldValue}&nbsp;${transactionDetails.sourceOrgFields[4].fieldValue}</span>, <span class="postal-code">${transactionDetails.sourceOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transactionDetails.sourceOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.sourceOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transactionDetails.sourceOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.sourceOrgFields[7].fieldValue}</span></dd></c:if>
                                        </dl>

                                        <c:if test="${not empty transactionDetails.sourceProviderFields[0].fieldValue}">
                                            <h4 class="form-section-heading">Originating Organization Provider: </h4>
                                            <dl class="vcard">
                                                <dd class="fn">${transactionDetails.sourceProviderFields[0].fieldValue}&nbsp;${transactionDetails.sourceProviderFields[1].fieldValue}&nbsp;${transactionDetails.sourceProviderFields[10].fieldValue}</dd>
                                                <c:if test="${not empty transactionDetails.sourceProviderFields[2].fieldValue}"><dd class="fn">Id: ${transactionDetails.sourceProviderFields[2].fieldValue}</dd></c:if>
                                                <c:if test="${not empty transactionDetails.sourceProviderFields[3].fieldValue}">
                                                <dd class="adr">
                                                    <span class="street-address">${transactionDetails.sourceProviderFields[3].fieldValue}</span><br/>
                                                    <c:if test="${not empty transactionDetails.sourceProviderFields[4].fieldValue}"><span class="street-address">${transactionDetails.sourceProviderFields[4].fieldValue}</span><br/></c:if>
                                                    <span class="region">${transactionDetails.sourceProviderFields[5].fieldValue}&nbsp;${transactionDetails.sourceProviderFields[6].fieldValue}</span>, <span class="postal-code">${transactionDetails.sourceProviderFields[7].fieldValue}</span>
                                                </dd>
                                                </c:if>
                                                <c:if test="${not empty transactionDetails.sourceProviderFields[8].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.sourceProviderFields[8].fieldValue}</span></dd></c:if>
                                                <c:if test="${not empty transactionDetails.sourceProviderFields[9].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.sourceProviderFields[9].fieldValue}</span></dd></c:if>
                                            </dl>
                                        </c:if>
                                    </div>
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Recipient Organization:</h4>
                                        <dl class="vcard">
                                            <dd class="fn">${transactionDetails.targetOrgFields[0].fieldValue}</dd>
                                            <dd class="adr">
                                                <span class="street-address">${transactionDetails.targetOrgFields[1].fieldValue}</span><c:if test="${not empty transactionDetails.targetOrgFields[2].fieldValue}"><span class="street-address"> ${transactionDetails.targetOrgFields[2].fieldValue}</span></c:if><br/>
                                                <span class="region">${transactionDetails.targetOrgFields[3].fieldValue}&nbsp;${transactionDetails.targetOrgFields[4].fieldValue}</span>, <span class="postal-code">${transactionDetails.targetOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transactionDetails.targetOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.targetOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transactionDetails.targetOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.targetOrgFields[7].fieldValue}</span></dd></c:if>
                                        </dl>
                                        
                                        <c:if test="${not empty transactionDetails.targetProviderFields[0].fieldValue}">
                                            <h4 class="form-section-heading">Recipient Organization Provider: </h4>
                                            <dl class="vcard">
                                                <dd class="fn">${transactionDetails.targetProviderFields[0].fieldValue}&nbsp;${transactionDetails.targetProviderFields[1].fieldValue}</dd>
                                                <dd class="fn">Id: ${transactionDetails.targetProviderFields[2].fieldValue}</dd>
                                                <dd class="adr">
                                                    <span class="street-address">${transactionDetails.targetProviderFields[3].fieldValue}</span><br/>
                                                    <c:if test="${not empty transactionDetails.targetProviderFields[4].fieldValue}"><span class="street-address">${transactionDetails.targetProviderFields[4].fieldValue}</span><br/></c:if>
                                                    <span class="region">${transactionDetails.targetProviderFields[5].fieldValue}&nbsp;${transactionDetails.targetProviderFields[6].fieldValue}</span>, <span class="postal-code">${transactionDetails.targetProviderFields[7].fieldValue}</span>
                                                </dd>
                                                <c:if test="${not empty transactionDetails.targetProviderFields[8].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.targetProviderFields[8].fieldValue}</span></dd></c:if>
                                                <c:if test="${not empty transactionDetails.targetProviderFields[9].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.targetProviderFields[9].fieldValue}</span></dd></c:if>
                                            </dl>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-section row">
                                        <div class="col-md-12"><h4 class="form-section-heading">Patient Information:</h4></div>
                                    <c:forEach items="${transactionDetails.patientFields}" var="patientInfo" varStatus="pfield">
                                        <c:choose>
                                            <c:when test="${patientInfo.useField == false}">
                                                <input type="hidden" id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                     <c:when test="${patientInfo.fieldType == 5}">
                                                         <div class="col-md-12" style="clear:both;">
                                                     </c:when>
                                                     <c:otherwise>
                                                         <div class="col-md-6">
                                                     </c:otherwise>
                                                 </c:choose>
                                                <div id="fieldDiv_${patientInfo.fieldNo}" class="form-group">
                                                    <label class="control-label" for="${patientInfo.fieldNo}">${patientInfo.fieldLabel}</label>
                                                    <c:choose>
                                                        <%--
                                                            Field Type Values
                                                            1 = Text Box
                                                            2 = Drop Down
                                                            3 = Radio
                                                            4 = Date
                                                            5 = Comment Box
                                                        --%>
                                                        <c:when test="${patientInfo.fieldType == 2 || patientInfo.fieldSelectOptions.size() > 0}">
                                                            <c:choose>
                                                                <c:when test="${patientInfo.fieldSelectOptions.size() > 0}">
                                                                    <select disabled id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" class="form-control <c:if test="${patientInfo.required == true}"> required</c:if>">
                                                                            <option value=""></option>
                                                                        <c:forEach items="${patientInfo.fieldSelectOptions}" var="options">
                                                                            <option value="${options.optionValue}" <c:if test="${patientInfo.fieldValue == options.optionValue}">selected</c:if>>${options.optionDesc}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input id="${patientInfo.fieldNo}" disabled name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:when test="${patientInfo.fieldType == 5}">
                                                            <textarea disabled rows="5" maxlength="500" id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>">${patientInfo.fieldValue}</textarea>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input id="${patientInfo.fieldNo}" disabled name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                </div>
                                             </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </div>

                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Message Details: </h4></div>
                                    <c:forEach items="${transactionDetails.detailFields}" var="detailInfo" varStatus="dfield">
                                        <c:choose>
                                            <c:when test="${detailInfo.useField == false}">
                                                <input type="hidden" id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${detailInfo.fieldType == 5}">
                                                        <div class="col-md-12" style="clear:both;">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="col-md-6">
                                                    </c:otherwise>
                                                </c:choose>
                                                   <div id="fieldDiv_${detailInfo.fieldNo}" class="form-group">
                                                       <label class="control-label" for="fieldA">${detailInfo.fieldLabel} 
                                                           <c:if test="${not empty detailInfo.fieldHelp}">
                                                               <a href="#" data-toggle="tooltip" data-placement="top" data-original-title="${detailInfo.fieldHelp}">
                                                                   <span class="glyphicon glyphicon-question-sign" style="cursor:pointer"></span>
                                                               </a>
                                                           </c:if>
                                                           <c:if test="${detailInfo.required == true}">&nbsp;*</c:if> 
                                                        </label>
                                                       <c:choose>
                                                           <%--
                                                               Field Type Values
                                                               1 = Text Box
                                                               2 = Drop Down
                                                               3 = Radio
                                                               4 = Date
                                                               5 = Comment Box
                                                               6 = Checkbox
                                                           --%>
                                                           <c:when test="${detailInfo.fieldType != 6 && (detailInfo.fieldType == 2 || fn:length(detailInfo.fieldSelectOptions) > 0)}">
                                                               <c:choose>
                                                                   <c:when test="${detailInfo.fieldSelectOptions.size() > 0}">
                                                                       <select disabled id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" class="form-control <c:if test="${detailInfo.required == true}"> required</c:if>">
                                                                               <option value=""></option>
                                                                           <c:forEach items="${detailInfo.fieldSelectOptions}" var="options">
                                                                               <option value="${options.optionValue}" <c:if test="${detailInfo.fieldValue == options.optionValue}">selected</c:if>>${options.optionDesc}</option>
                                                                           </c:forEach>
                                                                       </select>
                                                                   </c:when>
                                                                   <c:otherwise>
                                                                       <input disabled id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
                                                                   </c:otherwise>
                                                               </c:choose>          
                                                           </c:when>
                                                           <c:when test="${detailInfo.fieldType == 5}">
                                                               <textarea disabled rows="5" maxlength="500" id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>">${detailInfo.fieldValue}</textarea>
                                                           </c:when>
                                                           <c:when test="${detailInfo.fieldType == 6 && detailInfo.fieldSelectOptions.size() > 0}">
                                                                <c:forEach items="${detailInfo.fieldSelectOptions}" var="options">
                                                                   <br /> <input disabled type="checkbox" name="detailFields[${dfield.index}].fieldValue" value="${options.optionValue}" <c:if test="${fn:contains(detailInfo.fieldValue, options.optionDesc)}">checked="true"</c:if> /> ${options.optionDesc}
                                                                </c:forEach>
                                                            </c:when>     
                                                           <%-- Text Box Default --%>
                                                           <c:otherwise>
                                                               <input disabled id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
                                                           </c:otherwise>
                                                       </c:choose>
                                                   </div>
                                               </div>
                                            </c:otherwise>
                                        </c:choose>
                                        
                                    </c:forEach>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-12 form-section">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Existing Attachments:</h3>
                        </div>
                        <div class="panel-body">
                            <div class="form-container scrollable">
                                <div id="existingAttachments"></div>
                            </div>
                        </div>
                    </section>
                </div>                     

                <c:if test="${fromPage == 'inbox'}">                    
                    <div class="col-md-12 form-section">
                        <section class="panel panel-default">
                            <div class="panel-heading">
                                <div class="pull-right">
                                    <a href="#messageNoteModal" data-toggle="modal" class="btn btn-primary btn-xs btn-xsaction" id="createNewNote" title="Create a new Note"  ><span class="glyphicon glyphicon-plus"></span></a>
                                </div>
                                <h3 class="panel-title">Notes:</h3>
                                <p><small>(Notes listed below will not be sent back to the <c:choose><c:when test="${transactionDetails.sourceType == 1}">Provider</c:when><c:otherwise>CBO</c:otherwise></c:choose>)</small></p>
                            </div>
                            <div class="panel-body">
                                <div class="form-container scrollable">
                                    <div id="existingNotes"></div>
                                </div>
                            </div>
                        </section>
                    </div> 
                </c:if> 
                                       
                <c:if test="${userDetails.cancelAuthority == true && transactionDetails.statusId != 32 && transactionDetails.statusId != 23}">                             
                    <div class="form-group">
                       <input type="button" id="cancel" class="btn btn-primary btn-action-sm cancelMessage" value="Cancel"/>
                    </div>   
                </c:if>
            </form:form>
        </div>

    </div>

</div>
<%-- Status Definition modal --%>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>

<%-- Note diallog --%>
<div class="modal fade" id="messageNoteModal" role="dialog" tabindex="-1" aria-labeledby="Message Notes" aria-hidden="true" aria-describedby="Message Notes">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="panel-title">Create a New Note</h3>
            </div>
            <div class="modal-body">
                <form id="messageNoteForm" method="post" role="form">
                    <input type="hidden" name="messageTransactionId" id="messagetransactionId" />
                    <input type="hidden" name="internalStatusId" id="internalStatusId" />
                    <input type="hidden" name="internalStatusText" id="internalStatusText" />
                    <div class="form-container">
                        <div id="selStatus" class="form-group" style="display:none;">
                            <h4 class="form-section-heading">Selected Status: </h4>
                            <span id="statusDisplayText" class="control-label"></span>
                        </div>
                        <div class="form-group">
                            <h4 class="form-section-heading">Note: </h4>
                            <textarea name="messageNotes" class="form-control" rows="5"></textarea>
                        </div>   
                        <div class="form-group">
                            <input type="button" id="submitMessageNote" class="btn btn-primary" value="Save Note"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
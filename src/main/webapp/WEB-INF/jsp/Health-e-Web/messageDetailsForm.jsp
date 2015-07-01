<%-- 
    Document   : messageDetailsForm
    Created on : Dec 12, 2013, 2:47:34 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li><a href="<c:url value='/Health-e-Web/inbox'/>">eRG</a></li>
                <c:choose>
                    <c:when test="${pageHeader == 'pending'}">
                        <li><a href="<c:url value='/Health-e-Web/pending'/>">Pending</a></li>
                    </c:when>
                </c:choose>
                <li class="active">
                    <c:choose>
                        <c:when test="${pageHeader == 'create'}">Create New Message</c:when>
                        <c:when test="${pageHeader == 'feedback'}">Create New Feedback Report</c:when>
                        <c:otherwise>Batch #${transaction.batchName}</c:otherwise>
                    </c:choose>
                </li>
            </ol>
            
            <c:choose>
                <c:when test="${transaction.transactionId > 0}">
                    <div class="row">
                        <div class="col-md-6">
                            <h3>${transaction.messageTypeName} Transaction</h3>
                            <dl>
                                <dd><strong>Date Submitted:</strong> <fmt:formatDate value="${transaction.dateSubmitted}" type="both" dateStyle="long" timeStyle="long" /></dd>
                                <dd><strong>Batch ID:</strong> ${transaction.batchName}</dd>
                                <dd><strong>Transaction Type:</strong> <c:choose><c:when test="${transaction.sourceType == 1}">Originating Message</c:when><c:otherwise>Feedback Report</c:otherwise></c:choose></dd>
                                <dd><strong>System Status:</strong> <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${transaction.statusId}" title="View this Status">${transaction.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a></dd>
                            </dl>
                        </div>
                        <div class="col-md-6"></div>
                    </div>
                    <div class="well">
                        <div class="row form-inline">
                            <div class="form-group col-sm-4">
                                  
                            </div>
                            <div class="form-group col-sm-4 col-sm-offset-4">
                                <label class="sr-only">Select Action</label>
                                <select class="form-control" id="formAction">
                                    <option value="">-Select Action-</option>
                                    <c:if test="${transaction.sourceType == 2}"><option value="${transaction.orginialTransactionId}">View Original Referral</option></c:if>
                                    <option value="print">Print / Save As</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:when test="${pageHeader == 'feedback'}"><h2 class="form-title">Create New Feedback Report</h2></c:when>
                <c:otherwise>
                    <h2 class="form-title">Create New Message</h2>
                </c:otherwise>
            </c:choose>
                    
            <form:form action="/Health-e-Web/inbox/messageDetails" id="viewOriginalTransaction" method="post">
                <input type="hidden" id="originalTransactionId" name="transactionId" value="" />
            </form:form> 
            <form:form id="messageForm" action="/Health-e-Web/submitMessage" modelAttribute="transaction" role="form" class="form" method="post">
                <input type="hidden" id="action" name="action" value="save" />
                <form:hidden path="orgId" />
                <form:hidden path="sourceSubOrgId" />
                <form:hidden path="configId" />
                <form:hidden path="messageTypeId" />
                <form:hidden path="batchName" />
                <form:hidden path="originalFileName" />
                <form:hidden path="transactionRecordId" />
                <form:hidden path="batchId" />
                <form:hidden path="transactionId" id="transactionId" />
                <form:hidden path="targetConfigId" />
                <form:hidden path="transactionTargetId" />
                <form:hidden path="targetOrgId" />
                <form:hidden path="targetSubOrgId" />
                <form:hidden path="orginialTransactionId" />
                <input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
                <c:forEach items="${transaction.sourceOrgFields}" varStatus="i">
                    <form:hidden path="sourceOrgFields[${i.index}].fieldValue" />
                    
                    <c:choose>
                        <c:when test="${empty sourceOrgFields[i.index].fieldNo}">
                            <form:hidden path="sourceOrgFields[${i.index}].fieldNo" value="${i.index+1}" />
                        </c:when>
                        <c:otherwise>
                            <form:hidden path="sourceOrgFields[${i.index}].fieldNo" />
                        </c:otherwise>
                    </c:choose>
                    
                </c:forEach>
                <c:forEach items="${transaction.targetOrgFields}" varStatus="t">
                    <form:hidden path="targetOrgFields[${t.index}].fieldValue" />
                    <form:hidden path="targetOrgFields[${t.index}].fieldNo" />
                </c:forEach>
                <div class="panel-group form-accordion" id="accordion">

                    <div class="panel panel-default panel-form">
                        <div id="collapseThree" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <div class="form-section row">
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Originating Organization: </h4>
                                        <dl class="vcard">
                                            <dd class="fn">${transaction.sourceOrgFields[0].fieldValue}</dd>
                                            <dd class="adr">
                                                <span class="street-address">${transaction.sourceOrgFields[1].fieldValue}</span><c:if test="${not empty transaction.sourceOrgFields[2].fieldValue}"><span class="street-address">&nbsp;${transaction.sourceOrgFields[2].fieldValue}</span></c:if><br/>
                                                <span class="region">${transaction.sourceOrgFields[3].fieldValue}&nbsp;${transaction.sourceOrgFields[4].fieldValue}</span>, <span class="postal-code">${transaction.sourceOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transaction.sourceOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transaction.sourceOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transaction.sourceOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transaction.sourceOrgFields[7].fieldValue}</span></dd></c:if>
                                       </dl>
                                        <c:choose>
                                            <c:when test="${not empty transaction.sourceProviderFields[0].fieldValue}">
                                                <h4 class="form-section-heading">Originating Organization Provider: </h4>
                                                <dl class="vcard">
                                                    <dd class="fn">${transaction.sourceProviderFields[0].fieldValue}&nbsp;${transaction.sourceProviderFields[1].fieldValue}</dd>
                                                    <c:if test="${not empty transaction.sourceProviderFields[2].fieldValue}"><dd class="fn">Id: ${transaction.sourceProviderFields[2].fieldValue}</dd></c:if>
                                                    <c:if test="${not empty transaction.sourceProviderFields[3].fieldValue}">
                                                    <dd class="adr">
                                                        <span class="street-address">${transaction.sourceProviderFields[3].fieldValue}</span><br/>
                                                        <c:if test="${not empty transaction.sourceProviderFields[4].fieldValue}"><span class="street-address">${transaction.sourceProviderFields[4].fieldValue}</span><br/></c:if>
                                                        <span class="region">${transaction.sourceProviderFields[5].fieldValue}&nbsp;${transaction.sourceProviderFields[6].fieldValue}</span>, <span class="postal-code">${transaction.sourceProviderFields[7].fieldValue}</span>
                                                    </dd>
                                                    </c:if>
                                                    <c:if test="${not empty transaction.sourceProviderFields[8].fieldValue}"><dd>phone: <span class="tel">${transaction.sourceProviderFields[8].fieldValue}</span></dd></c:if>
                                                    <c:if test="${not empty transaction.sourceProviderFields[9].fieldValue}"><dd>fax: <span class="tel">${transaction.sourceProviderFields[9].fieldValue}</span></dd></c:if>
                                               </dl>
                                            </c:when>
                                            <c:when test="${not empty providers}">
                                                <dl id="fromOrgProviderChoose">
                                                    <dd>
                                                        Choose Provider: 
                                                        <select class="form-control" id="orgProvider">
                                                            <option value="">-Choose-</option>
                                                             <c:forEach items="${providers}" var="provider">
                                                                     <option value="${provider.id}">${provider.firstName}&nbsp;${provider.lastName}</option>
                                                             </c:forEach>
                                                        </select>
                                                    </dd>
                                                </dl>
                                            </c:when>
                                        </c:choose>
                                        <c:forEach items="${transaction.sourceProviderFields}" var="providerInfo" varStatus="sp">
                                            <form:hidden id="provider_${providerInfo.fieldNo}" path="sourceProviderFields[${sp.index}].fieldValue" value="" />
                                            <form:hidden path="sourceProviderFields[${sp.index}].fieldNo" />
                                        </c:forEach>
                                       <div id="fromorgProvider" style="display:none">
                                           <h4 class="form-section-heading">Originating Organization Provider:  - <a href="javascript:void(0)" title="Change selected provider." id="fromOrgProviderChange">Change</a></h4>
                                           <dl class="vcard">
                                               <dd class="fn" id="fromOrgProviderName"></dd>
                                               <dd class="adr">
                                                <span class="street-address" id="fromOrgProviderLine1"></span>
                                                <span class="street-address" id="fromOrgProviderLine2"></span>
                                                <span class="region" id="fromOrgProviderRegion"></span><span class="postal-code" id="fromOrgProviderZip"></span>
                                               </dd>
                                               <dd id="fromOrgProviderPhone"></dd>
                                               <dd id="fromOrgProviderFax"></dd>
                                           </dl>
                                       </div>
                                    </div>
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Recipient Organization:</h4>
                                        <dl class="vcard">
                                            <dd class="fn">${transaction.targetOrgFields[0].fieldValue}</dd>
                                            <dd class="adr">
                                                <span class="street-address">${transaction.targetOrgFields[1].fieldValue}<c:if test="${not empty transaction.targetOrgFields[2].fieldValue}"><span class="street-address"> ${transaction.targetOrgFields[2].fieldValue}</span></c:if></span><br/>
                                                <span class="region">${transaction.targetOrgFields[3].fieldValue}&nbsp;${transaction.targetOrgFields[4].fieldValue}</span>, <span class="postal-code">${transaction.targetOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transaction.targetOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transaction.targetOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transaction.targetOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transaction.targetOrgFields[7].fieldValue}</span></dd></c:if>
                                        </dl>
                                        <c:if test="${pageHeader == 'feedback' || transaction.sourceType == 2}">
                                            <c:choose>
                                                <c:when test="${not empty transaction.targetProviderFields[0].fieldValue}">
                                                    <h4 class="form-section-heading">Recipient Organization Provider: </h4>
                                                    <dl class="vcard">
                                                        <dd class="fn">${transaction.targetProviderFields[0].fieldValue}&nbsp;${transaction.targetProviderFields[1].fieldValue}</dd>
                                                        <dd class="fn">Id: ${transaction.targetProviderFields[2].fieldValue}</dd>
                                                        <dd class="adr">
                                                            <span class="street-address">${transaction.targetProviderFields[3].fieldValue}</span><br/>
                                                            <c:if test="${not empty transaction.targetProviderFields[4].fieldValue}"><span class="street-address">${transaction.targetProviderFields[4].fieldValue}</span><br/></c:if>
                                                            <span class="region">${transaction.targetProviderFields[5].fieldValue}&nbsp;${transaction.targetProviderFields[6].fieldValue}</span>, <span class="postal-code">${transaction.targetProviderFields[7].fieldValue}</span>
                                                        </dd>
                                                        <c:if test="${not empty transaction.targetProviderFields[8].fieldValue}"><dd>phone: <span class="tel">${transaction.targetProviderFields[8].fieldValue}</span></dd></c:if>
                                                        <c:if test="${not empty transaction.targetProviderFields[9].fieldValue}"><dd>fax: <span class="tel">${transaction.targetProviderFields[9].fieldValue}</span></dd></c:if>
                                                   </dl>
                                                </c:when>
                                                <c:when test="${not empty providers}">
                                                    <dl id="fromOrgProviderChoose">
                                                        <dd>
                                                            Choose Provider: 
                                                            <select class="form-control" id="targetorgProvider">
                                                                <option value="">-Choose-</option>
                                                                 <c:forEach items="${providers}" var="provider">
                                                                         <option value="${provider.id}">${provider.firstName}&nbsp;${provider.lastName}</option>
                                                                 </c:forEach>
                                                            </select>
                                                        </dd>
                                                    </dl>
                                                </c:when>
                                            </c:choose>
                                        </c:if>
                                        <c:forEach items="${transaction.targetProviderFields}" var="tarproviderInfo" varStatus="tp">
                                            <form:hidden id="tgtprovider_${tarproviderInfo.fieldNo}" path="targetProviderFields[${tp.index}].fieldValue" value="" />
                                            <form:hidden path="targetProviderFields[${tp.index}].fieldNo" />
                                        </c:forEach>
                                       <div id="toorgProvider" style="display:none">
                                           <h4 class="form-section-heading">Recipient Organization Provider:  - <a href="javascript:void(0)" title="Change selected provider." id="toOrgProviderChange">Change</a></h4>
                                           <dl class="vcard">
                                               <dd class="fn" id="toOrgProviderName"></dd>
                                               <dd class="adr">
                                                <span class="street-address" id="toOrgProviderLine1"></span>
                                                <span class="street-address" id="toOrgProviderLine2"></span>
                                                <span class="region" id="toOrgProviderRegion"></span><span class="postal-code" id="toOrgProviderZip"></span>
                                               </dd>
                                               <dd id="toOrgProviderPhone"></dd>
                                               <dd id="toOrgProviderFax"></dd>
                                           </dl>
                                       </div>
                                    </div>
                                </div>
                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Patient Information:</h4></div>
                                    <c:forEach items="${transaction.patientFields}" var="patientInfo" varStatus="pfield">
                                        <input type="hidden" name="patientFields[${pfield.index}].fieldNo" value="${patientInfo.fieldNo}" />
                                        <c:if test="${patientInfo.readOnly == true}">
                                            <input type="hidden" name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" />
                                        </c:if>
                                        <c:choose>
                                             <c:when test="${patientInfo.fieldType == 5}">
                                                 <div class="col-md-12" style="clear:both;">
                                             </c:when>
                                             <c:otherwise>
                                                 <div class="col-md-6 ${(pfield.index mod 2) == 0 ? 'cb' : ''}">
                                             </c:otherwise>
                                         </c:choose>
                                            <div id="fieldDiv_${patientInfo.fieldNo}" class="form-group">
                                                <label class="control-label" for="${patientInfo.fieldNo}">${patientInfo.fieldLabel}&nbsp;<c:if test="${patientInfo.required == true}">*&nbsp;</c:if></label>
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
                                                                <select <c:if test="${patientInfo.readOnly == true}">disabled</c:if> id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" class="form-control <c:if test="${patientInfo.required == true}"> required</c:if>">
                                                                    <option value="">-Choose-</option>
                                                                    <c:forEach items="${patientInfo.fieldSelectOptions}" var="options">
                                                                        <option value="${options.optionValue}" <c:if test="${patientInfo.fieldValue == options.optionValue || options.optionValue == options.defaultValue}">selected</c:if>>${options.optionDesc}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input <c:if test="${patientInfo.readOnly == true}">disabled</c:if> id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:when test="${patientInfo.fieldType == 5}">
                                                        <textarea rows="5" maxlength="500" id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" class="form-control ${patientFields.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>">${patientInfo.fieldValue}</textarea>
                                                    </c:when>
                                                    <%-- Text Box Default --%>    
                                                    <c:otherwise>
                                                        <input <c:if test="${patientInfo.readOnly == true}">disabled</c:if> id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
                                                    </c:otherwise>
                                                </c:choose>
                                                <span id="errorMsg_${patientInfo.fieldNo}" class="control-label"></span>  
                                            </div>
                                         </div>
                                    </c:forEach>
                                </div>

                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Message Details: </h4></div>
                                    <c:forEach items="${transaction.detailFields}" var="detailInfo" varStatus="dfield">
                                         <input type="hidden" name="detailFields[${dfield.index}].fieldNo" value="${detailInfo.fieldNo}" />
                                         <c:choose>
                                             <c:when test="${detailInfo.fieldType == 5}">
                                                 <div class="col-md-12" style="clear:both;">
                                             </c:when>
                                             <c:otherwise>
                                                 <div class="col-md-6 ${(dfield.index mod 2) == 0 ? 'cb' : ''}">
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
                                                    --%>
                                                    <c:when test="${detailInfo.fieldType == 2 || detailInfo.fieldSelectOptions.size() > 0}">
                                                        <c:choose>
                                                            <c:when test="${detailInfo.fieldSelectOptions.size() > 0}">
                                                                <select id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" class="form-control <c:if test="${detailInfo.required == true}"> required</c:if>">
                                                                    <option value="">-Choose-</option>
                                                                    <c:forEach items="${detailInfo.fieldSelectOptions}" var="options">
                                                                        <option value="${options.optionValue}" <c:if test="${detailInfo.fieldValue == options.optionValue || options.optionValue == options.defaultValue}">selected</c:if>>${options.optionDesc}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:choose>
                                                                    <c:when test="${detailInfo.readOnly == true}">
                                                                        <input disabled value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} type="text">
                                                                        <input type="hidden" id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <input id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>  
                                                    <c:when test="${detailInfo.fieldType == 5}">
                                                        <textarea rows="5" maxlength="500" id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" class="form-control textarea ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>">${detailInfo.fieldValue}</textarea>
                                                    </c:when>
                                                    <%-- Text Box Default --%>                    
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${detailInfo.readOnly == true}">
                                                                <input disabled value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} type="text">
                                                                <input type="hidden" id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                                <span id="errorMsg_${detailInfo.fieldNo}" class="control-label"></span>                
                                            </div>
                                         </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                                    
                <%-- Existing Message Attachments --%>
                <c:if test="${transaction.attachmentLimit != 0}">
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
                    
                    <%-- New Message Attachment --%>
                    <div class="col-md-12 form-section attachmentUploadPanel" rel="${transaction.attachmentLimit}">
                        <section class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Upload New Attachment <c:if test="${transaction.attachmentLimit > 0}">- Limited to ${transaction.attachmentLimit} attachment<c:if test="${transaction.attachmentLimit > 1}">s</c:if></c:if></h3>
                            </div>
                            <div class="panel-body">
                                <div class="form-container scrollable">
                                   <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="control-label" for="attachmentTitle">Attachment Title</label>
                                            <input id="attachmentTitle" class="form-control" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="control-label" for="attachmentFile">Attachment File</label>
                                            <br /><div id="UploadButton" class="btn btn-primary btn-action-sm UploadButton" style="cursor:pointer">Upload File</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                </c:if>
                
                <div class="form-group">
                    <div id="translationMsgDiv"  class="alert alert-danger" style="display:none;">
                        <strong>An error has occurred in one of the above fields!</strong>
                    </div>
                    <input type="button" id="save" class="btn btn-primary btn-action-sm submitMessage" value="Save ${transaction.sourceType == 1 && (pageHeader == 'create' || pageHeader == 'pending') ? 'Referral' : 'Feedback Report'}"/>
                    <c:choose>
                        <c:when test="${transaction.autoRelease == true}">
                            <input type="button" id="send" class="btn btn-primary btn-action-sm submitMessage" value="Send ${transaction.sourceType == 1 && (pageHeader == 'create' || pageHeader == 'pending') ? 'Referral' : 'Feedback Report'}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="button" id="release" class="btn btn-primary btn-action-sm submitMessage" value="Release"/>
                        </c:otherwise>
                    </c:choose> 
                    <%-- Allow the user to delete saved messages --%>   
                    <c:choose>
                        <c:when test="${transaction.statusId == 15}"> 
                            <div class="pull-right"><input type="button" id="delete" class="btn btn-primary btn-action-sm deleteMessage" value="Delete"/></div>
                        </c:when> 
                        <c:otherwise>
                            <c:if test="${userDetails.cancelAuthority == true && transaction.statusId > 0}">                             
                                <div class="pull-right"><input type="button" id="cancel" class="btn btn-primary btn-action-sm cancelMessage" value="Cancel"/></div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>    
                </div>
           </form:form>
        </div>
    </div>
</div>
<%-- Status Definition modal --%>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>
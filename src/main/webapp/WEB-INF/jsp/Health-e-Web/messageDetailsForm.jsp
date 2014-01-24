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
                <li><a href="#">eRG</a></li>
                <c:if test="${pageHeader == 'pending'}"><li><a href="<c:url value='/Health-e-Web/pending'/>">Pending Batches</a></li></c:if>
                <li class="active"><c:choose><c:when test="${pageHeader == 'create'}">Create New Message</c:when><c:otherwise>Referral #${transaction.batchName}</c:otherwise></c:choose></li>
            </ol>
            
            <c:choose>
                <c:when test="${transaction.transactionId > 0}">
                    <div class="row">
                        <div class="col-md-6">
                            <h3>${transaction.messageTypeName} Transaction</h3>
                            <dl>
                                <dd><strong>Date Submitted:</strong> <fmt:formatDate value="${transaction.dateSubmitted}" type="both" dateStyle="long" timeStyle="long" /></dd>
                                <dd><strong>System ID:</strong> ${transaction.batchName}</dd>
                                <dd><strong>Status:</strong> <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${transaction.statusId}" title="View this Status">${transaction.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a></dd>
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
                                    <option value="">Select Action</option>
                                    <option value="print">Print / Save As</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <h2 class="form-title">Create New Message</h2>
                </c:otherwise>
            </c:choose>

            <form:form id="messageForm" action="/Health-e-Web/submitMessage" modelAttribute="transaction" role="form" class="form" method="post">
                <input type="hidden" id="action" name="action" value="save" />
                <form:hidden path="orgId" />
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
                <input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
                <c:forEach items="${transaction.sourceOrgFields}" varStatus="i">
                    <form:hidden path="sourceOrgFields[${i.index}].fieldValue" />
                    <form:hidden path="sourceOrgFields[${i.index}].fieldNo" />
                </c:forEach>
                <c:forEach items="${transaction.targetOrgFields}" varStatus="t">
                    <form:hidden path="targetOrgFields[${t.index}].fieldValue" />
                    <form:hidden path="targetOrgFields[${t.index}].fieldNo" />
                </c:forEach>
                <c:forEach items="${transaction.targetProviderFields}" varStatus="tp">
                    <form:hidden path="targetProviderFields[${tp.index}].fieldValue" />
                    <form:hidden path="targetProviderFields[${tp.index}].fieldNo" />
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
                                                <span class="street-address">${transaction.sourceOrgFields[1].fieldValue}</span><br/>
                                                <c:if test="${not empty transaction.sourceOrgFields[2].fieldValue}"><span class="street-address">${transaction.sourceOrgFields[2].fieldValue}</span><br/></c:if>
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
                                                    <dd class="fn">Id: ${transaction.sourceProviderFields[2].fieldValue}</dd>
                                                    <dd class="adr">
                                                        <span class="street-address">${transaction.sourceProviderFields[3].fieldValue}</span><br/>
                                                        <c:if test="${not empty transaction.sourceProviderFields[4].fieldValue}"><span class="street-address">${transaction.sourceProviderFields[4].fieldValue}</span><br/></c:if>
                                                        <span class="region">${transaction.sourceProviderFields[5].fieldValue}&nbsp;${transaction.sourceProviderFields[6].fieldValue}</span>, <span class="postal-code">${transaction.sourceProviderFields[7].fieldValue}</span>
                                                    </dd>
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
                                                <span class="street-address" id="fromOrgProviderLine1"></span><br/>
                                                <span class="street-address" id="fromOrgProviderLine2"></span><br/>
                                                <span class="region" id="fromOrgProviderRegion"></span>, <span class="postal-code" id="fromOrgProviderZip"></span>
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
                                                <span class="street-address">${transaction.targetOrgFields[1].fieldValue}</span><br/>
                                                <c:if test="${not empty transaction.targetOrgFields[2].fieldValue}"><span class="street-address">${transaction.targetOrgFields[2].fieldValue}</span><br/></c:if>
                                                <span class="region">${transaction.targetOrgFields[3].fieldValue}&nbsp;${transaction.targetOrgFields[4].fieldValue}</span>, <span class="postal-code">${transaction.targetOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transaction.targetOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transaction.targetOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transaction.targetOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transaction.targetOrgFields[7].fieldValue}</span></dd></c:if>
                                        </dl>
                                    </div>
                                </div>
                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Patient Information:</h4></div>
                                    <c:forEach items="${transaction.patientFields}" var="patientInfo" varStatus="pfield">
                                        <input type="hidden" name="patientFields[${pfield.index}].fieldNo" value="${patientInfo.fieldNo}" />
                                        <div class="col-md-6 ${(pfield.index mod 2) == 0 ? 'cb' : ''}">
                                            <div id="fieldDiv_${patientInfo.fieldNo}" class="form-group">
                                                <label class="control-label" for="${patientInfo.fieldNo}">
                                                   ${patientInfo.fieldLabel}&nbsp;<c:if test="${patientInfo.required == true}">*&nbsp;</c:if></label>
                                                <c:choose>
                                                    <c:when test="${patientInfo.fieldSelectOptions.size() > 0}">
                                                        <select id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" class="form-control <c:if test="${patientInfo.required == true}"> required</c:if>">
                                                            <option value="">-Choose-</option>
                                                            <c:forEach items="${patientInfo.fieldSelectOptions}" var="options">
                                                                <option value="${options.optionValue}" <c:if test="${patientInfo.fieldValue == options.optionValue}">selected</c:if>>${options.optionDesc}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
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
                                         <div class="col-md-6 ${(dfield.index mod 2) == 0 ? 'cb' : ''}">
                                            <div id="fieldDiv_${detailInfo.fieldNo}" class="form-group">
                                                <label class="control-label" for="fieldA">${detailInfo.fieldLabel} <c:if test="${detailInfo.required == true}">&nbsp;*</c:if></label>
                                                <c:choose>
                                                    <c:when test="${detailInfo.fieldSelectOptions.size() > 0}">
                                                        <select id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" class="form-control <c:if test="${detailInfo.required == true}"> required</c:if>">
                                                            <option value="">-Choose-</option>
                                                            <c:forEach items="${detailInfo.fieldSelectOptions}" var="options">
                                                                <option value="${options.optionValue}" <c:if test="${detailInfo.fieldValue == options.optionValue}">selected</c:if>>${options.optionDesc}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
                                                    </c:otherwise>
                                                </c:choose>
                                                <span id="errorMsg_${detailInfo.fieldNo}" class="control-label"></span>                
                                            </div>
                                         </div>
                                    </c:forEach>
                                </div>
                                        
                                <%-- Existing Message Attachments --%>
                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Existing Attachments:</h4></div>
                                    <div class="col-md-12" id="existingAttachments"></div>
                                </div>     
                                    
                                <%-- New Message Attachment --%>
                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Upload New Attachment:</h4></div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="control-label" for="attachmentTitle">Attachment Title</label>
                                            <input id="attachmentTitle" class="form-control" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label class="control-label" for="attachmentFile">Attachment File</label>
                                            <input name="fileUpload" id="attachmentFileUpload" type="file" class="form-control" />
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <input type="button" id="save" class="btn btn-primary btn-action submitMessage" value="Save Referral"/>
                                    <c:choose>
                                        <c:when test="${transaction.autoRelease == true}">
                                            <input type="button" id="send" class="btn btn-primary btn-action submitMessage" value="Send Referral"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="button" id="release" class="btn btn-primary btn-action submitMessage" value="Release"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
           </form:form>
        </div>
    </div>
</div>
<!-- Status Definition modal -->
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>
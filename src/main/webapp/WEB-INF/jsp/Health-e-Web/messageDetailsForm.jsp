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
                <c:if test="${pageHeader == 'pending'}"><li><a href="<c:url value='/Health-e-Web/pending'/>">Pending Messages</a></li></c:if>
                <li class="active"><c:choose><c:when test="${pageHeader == 'create'}">Create New Message</c:when><c:otherwise>Message Details</c:otherwise></c:choose></li>
            </ol>

            <h2 class="form-title"><c:choose><c:when test="${pageHeader == 'create'}">Create New Message</c:when><c:otherwise>Message Details</c:otherwise></c:choose></h2>
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
                <input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
                <c:forEach items="${transaction.sourceOrgFields}" varStatus="i">
                    <form:hidden path="sourceOrgFields[${i.index}].fieldValue" />
                    <form:hidden path="sourceOrgFields[${i.index}].fieldNo" />
                </c:forEach>
                <c:forEach items="${transaction.sourceProviderFields}" varStatus="sp">
                    <form:hidden path="sourceProviderFields[${sp.index}].fieldValue" />
                    <form:hidden path="sourceProviderFields[${sp.index}].fieldNo" />
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
                                         <div class="col-md-6">
                                            <div id="fieldDiv_${patientInfo.fieldNo}" class="form-group">
                                                <label class="control-label" for="${patientInfo.fieldNo}">
                                                    ${patientInfo.fieldLabel}&nbsp;<c:if test="${patientInfo.required == true}">*&nbsp;</c:if>
                                                    <span id="errorMsg_${patientInfo.fieldNo}" class="control-label"></span>  
                                                </label>
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
                                            </div>
                                         </div>
                                    </c:forEach>
                                </div>

                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Message Details: </h4></div>
                                    <c:forEach items="${transaction.detailFields}" var="detailInfo" varStatus="dfield">
                                         <input type="hidden" name="detailFields[${dfield.index}].fieldNo" value="${detailInfo.fieldNo}" />
                                         <div class="col-md-6">
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
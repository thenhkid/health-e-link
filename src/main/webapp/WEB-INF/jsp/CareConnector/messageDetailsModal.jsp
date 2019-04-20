<%-- 
    Document   : messageDetails
    Created on : Mar 28, 2014, 11:36:55 AM
    Author     : chadmccue
--%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />


<form:form action="/CareConnector/feedbackReport/details" id="newFeedbackForm" modelAttribute="transactionDetails" method="post">
    <form:hidden path="transactionId" id="transactionId" />
    <input type="hidden" id="feedbackConfigId" name="configId" value="" />
</form:form>               
<form:form action="/CareConnector/inbox/messageDetails" id="viewTransactionDetails" modelAttribute="transactionDetails" method="post">
    <form:hidden path="transactionId" class="transactionId" id="transactionId" />
    <input type="hidden" name="fromPage" id="fromPage" value="${fromPage}" />
</form:form>  
<div class="modal-dialog">
    <div class="modal-content" style="width:840px;">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 class="panel-title">Message Details</h3>
        </div>
        <div class="modal-body">
            <div class="container main-container" role="main">
                <div class="row">
                    <div class="col-md-8 page-content">
                            <div class="well">
                                <div class="row form-inline">
                                   <div class="form-group col-sm-4 col-sm-offset-4 pull-right">
                                        <label class="sr-only">Select Action </label>
                                        <select class="form-control" id="formAction" rel="${transactionDetails.orginialTransactionId}">
                                            <option value="">-Select Action-</option>
                                            <c:if test="${transactionDetails.sourceType == 1}"><option value="feedbackReports">View Feedback Reports</option></c:if>
                                            <c:if test="${feedbackConfigId > 0 and userDetails.createAuthority == true and transactionDetails.messageStatus == 1}"><option value="${feedbackConfigId}">Create Feedback Report</option></c:if>
                                            <c:if test="${transactionDetails.sourceType == 2}"><option value="originalReferral">View Original Referral</option></c:if>
                                        </select>
                                  </div>
                               </div>
                            </div> 
                            <div class="row">
                                <div class="col-md-6">
                                    <h3>${transactionDetails.messageTypeName} Transaction</h3>
                                    <dl>
                                        <dd><strong>Date Submitted:</strong> <fmt:formatDate value="${transactionDetails.dateSubmitted}" type="both" dateStyle="long" timeStyle="long" /></dd>
                                        <dd><strong>Batch ID:</strong> ${transactionDetails.batchName}</dd>
                                        <dd><strong>Transaction Type:</strong> <c:choose><c:when test="${transactionDetails.sourceType == 1}">Originating Message</c:when><c:otherwise>Feedback Report</c:otherwise></c:choose></dd>
                                        <dd><strong>System Status:</strong> ${transactionDetails.statusValue}</dd>
                                    </dl>
                                </div>
                                <div class="col-md-6"></div>
                            </div>
                                
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
                                                            <span class="street-address">${transactionDetails.sourceOrgFields[1].fieldValue}</span><c:if test="${not empty transactionDetails.sourceOrgFields[2].fieldValue}"><span class="street-address">&nbsp;${transactionDetails.sourceOrgFields[2].fieldValue}</span></c:if><br/>
                                                            <span class="region">${transactionDetails.sourceOrgFields[3].fieldValue}&nbsp;${transactionDetails.sourceOrgFields[4].fieldValue}</span>, <span class="postal-code">${transactionDetails.sourceOrgFields[5].fieldValue}</span>
                                                        </dd>
                                                        <c:if test="${not empty transactionDetails.sourceOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.sourceOrgFields[6].fieldValue}</span></dd></c:if>
                                                        <c:if test="${not empty transactionDetails.sourceOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.sourceOrgFields[7].fieldValue}</span></dd></c:if>
                                                   </dl>
                                                    <c:choose>
                                                        <c:when test="${not empty transactionDetails.sourceProviderFields[0].fieldValue}">
                                                            <h4 class="form-section-heading">Originating Organization Provider: </h4>
                                                            <dl class="vcard">
                                                                <dd class="fn">${transactionDetails.sourceProviderFields[0].fieldValue}&nbsp;${transactionDetails.sourceProviderFields[1].fieldValue}</dd>
                                                                <dd class="fn">Id: ${transactionDetails.sourceProviderFields[2].fieldValue}</dd>
                                                                <dd class="adr">
                                                                    <span class="street-address">${transactionDetails.sourceProviderFields[3].fieldValue}</span><br/>
                                                                    <c:if test="${not empty transactionDetails.sourceProviderFields[4].fieldValue}"><span class="street-address">${transactionDetails.sourceProviderFields[4].fieldValue}</span><br/></c:if>
                                                                    <span class="region">${transactionDetails.sourceProviderFields[5].fieldValue}&nbsp;${transactionDetails.sourceProviderFields[6].fieldValue}</span>, <span class="postal-code">${transactionDetails.sourceProviderFields[7].fieldValue}</span>
                                                                </dd>
                                                                <c:if test="${not empty transactionDetails.sourceProviderFields[8].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.sourceProviderFields[8].fieldValue}</span></dd></c:if>
                                                                <c:if test="${not empty transactionDetails.sourceProviderFields[9].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.sourceProviderFields[9].fieldValue}</span></dd></c:if>
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
                                                        <dd class="fn">${transactionDetails.targetOrgFields[0].fieldValue}</dd>
                                                        <dd class="adr">
                                                            <span class="street-address">${transactionDetails.targetOrgFields[1].fieldValue}<c:if test="${not empty transactionDetails.targetOrgFields[2].fieldValue}"><span class="street-address"> ${transactionDetails.targetOrgFields[2].fieldValue}</span></c:if></span><br/>
                                                            <span class="region">${transactionDetails.targetOrgFields[3].fieldValue}&nbsp;${transactionDetails.targetOrgFields[4].fieldValue}</span>, <span class="postal-code">${transactionDetails.targetOrgFields[5].fieldValue}</span>
                                                        </dd>
                                                        <c:if test="${not empty transactionDetails.targetOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.targetOrgFields[6].fieldValue}</span></dd></c:if>
                                                        <c:if test="${not empty transactionDetails.targetOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.targetOrgFields[7].fieldValue}</span></dd></c:if>
                                                    </dl>
                                                    <c:if test="${pageHeader == 'feedback' || transactionDetails.sourceType == 2}">
                                                        <c:choose>
                                                            <c:when test="${not empty transactionDetails.targetProviderFields[0].fieldValue}">
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
                                                <c:forEach items="${transactionDetails.patientFields}" var="patientInfo" varStatus="pfield">
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
                                                                            <select disabled id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" class="form-control <c:if test="${patientInfo.required == true}"> required</c:if>">
                                                                                <option value="">-Choose-</option>
                                                                                <c:forEach items="${patientInfo.fieldSelectOptions}" var="options">
                                                                                    <option value="${options.optionValue}" <c:if test="${patientInfo.fieldValue == options.optionValue || options.optionValue == options.defaultValue}">selected</c:if>>${options.optionDesc}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <input disabled id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:when>
                                                                <c:when test="${patientInfo.fieldType == 5}">
                                                                    <textarea rows="5" maxlength="500" id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" class="form-control ${patientFields.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>">${patientInfo.fieldValue}</textarea>
                                                                </c:when>
                                                                <%-- Text Box Default --%>    
                                                                <c:otherwise>
                                                                    <input disabled id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <span id="errorMsg_${patientInfo.fieldNo}" class="control-label"></span>  
                                                        </div>
                                                     </div>
                                                </c:forEach>
                                            </div>

                                            <div class="form-section row">
                                                <div class="col-md-12"><h4 class="form-section-heading">Message Details: </h4></div>
                                                <c:forEach items="${transactionDetails.detailFields}" var="detailInfo" varStatus="dfield">
                                                     <c:choose>
                                                         <c:when test="${detailInfo.fieldType == 5}">
                                                             <div class="col-md-12" style="clear:both;">
                                                         </c:when>
                                                         <c:otherwise>
                                                             <div class="col-md-6 ${(dfield.index mod 2) == 0 ? 'cb' : ''}">
                                                         </c:otherwise>
                                                     </c:choose>
                                                        <div id="fieldDiv_${detailInfo.fieldNo}" class="form-group">
                                                            <label class="control-label" for="fieldA">${detailInfo.fieldLabel} <c:if test="${detailInfo.required == true}">&nbsp;*</c:if></label>
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
                                                                            <select disabled id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" class="form-control">
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
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <input id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:when>  
                                                                <c:when test="${detailInfo.fieldType == 5}">
                                                                    <textarea disabled rows="5" maxlength="500" id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" class="form-control textarea ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>">${detailInfo.fieldValue}</textarea>
                                                                </c:when>
                                                                <%-- Text Box Default --%>                    
                                                                <c:otherwise>
                                                                    <c:choose>
                                                                        <c:when test="${detailInfo.readOnly == true}">
                                                                            <input disabled value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} type="text">
                                                                           </c:when>
                                                                        <c:otherwise>
                                                                            <input disabled id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:otherwise>
                                                            </c:choose>             
                                                        </div>
                                                     </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


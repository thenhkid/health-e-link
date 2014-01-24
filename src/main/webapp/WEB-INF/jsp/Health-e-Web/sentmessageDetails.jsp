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
                <li><c:choose><c:when test="${fromPage == 'pending'}"><a href="<c:url value='/Health-e-Web/pending'/>">Pending Batches</a></c:when><c:otherwise><a href="<c:url value='/Health-e-Web/sent'/>">Sent Messages</a></c:otherwise></c:choose></li>
                <li class="active">Batch #${transactionDetails.batchName}</li>
            </ol>

            <div class="row">
                <div class="col-md-6">
                    <h3>${transactionDetails.messageTypeName} Transaction</h3>
                    <dl>
                    <dd><strong>Date Submitted:</strong> <fmt:formatDate value="${transactionDetails.dateSubmitted}" type="both" dateStyle="long" timeStyle="long" /></dd>
                    <dd><strong>System ID:</strong> ${transactionDetails.batchName}</dd>
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
                            <option value="feedbackReports">View Feedback Reports</option>
                        </select>
                    </div>
                </div>
            </div>        
            <form:form id="messageForm" action="/Health-e-Web/submitMessage" modelAttribute="transactionDetails" role="form" class="form" method="post">
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
                                                <span class="street-address">${transactionDetails.sourceOrgFields[1].fieldValue}</span><br/>
                                                <c:if test="${not empty transactionDetails.sourceOrgFields[2].fieldValue}"><span class="street-address">${transactionDetails.sourceOrgFields[2].fieldValue}</span><br/></c:if>
                                                <span class="region">${transactionDetails.sourceOrgFields[3].fieldValue}&nbsp;${transactionDetails.sourceOrgFields[4].fieldValue}</span>, <span class="postal-code">${transactionDetails.sourceOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transactionDetails.sourceOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.sourceOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transactionDetails.sourceOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.sourceOrgFields[7].fieldValue}</span></dd></c:if>
                                       </dl>
                                       
                                        <c:if test="${not empty transactionDetails.sourceProviderFields[0].fieldValue}">
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
                                        </c:if>
                                    </div>
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Recipient Organization:</h4>
                                        <dl class="vcard">
                                            <dd class="fn">${transactionDetails.targetOrgFields[0].fieldValue}</dd>
                                            <dd class="adr">
                                                <span class="street-address">${transactionDetails.targetOrgFields[1].fieldValue}</span><br/>
                                                <c:if test="${not empty transactionDetails.targetOrgFields[2].fieldValue}"><span class="street-address">${transactionDetails.targetOrgFields[2].fieldValue}</span><br/></c:if>
                                                <span class="region">${transactionDetails.targetOrgFields[3].fieldValue}&nbsp;${transactionDetails.targetOrgFields[4].fieldValue}</span>, <span class="postal-code">${transactionDetails.targetOrgFields[5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transactionDetails.targetOrgFields[6].fieldValue}"><dd>phone: <span class="tel">${transactionDetails.targetOrgFields[6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transactionDetails.targetOrgFields[7].fieldValue}"><dd>fax: <span class="tel">${transactionDetails.targetOrgFields[7].fieldValue}</span></dd></c:if>
                                        </dl>
                                    </div>
                                </div>
                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Patient Information:</h4></div>
                                    <c:forEach items="${transactionDetails.patientFields}" var="patientInfo" varStatus="pfield">
                                         <div class="col-md-6">
                                            <div id="fieldDiv_${patientInfo.fieldNo}" class="form-group">
                                                <label class="control-label" for="${patientInfo.fieldNo}">${patientInfo.fieldLabel}</label>
                                                <c:choose>
                                                    <c:when test="${patientInfo.fieldSelectOptions.size() > 0}">
                                                        <select disabled id="${patientInfo.fieldNo}" name="patientFields[${pfield.index}].fieldValue" class="form-control <c:if test="${patientInfo.required == true}"> required</c:if>">
                                                            <option value="">-Choose-</option>
                                                            <c:forEach items="${patientInfo.fieldSelectOptions}" var="options">
                                                                <option value="${options.optionValue}" <c:if test="${patientInfo.fieldValue == options.optionValue}">selected</c:if>>${options.optionDesc}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input id="${patientInfo.fieldNo}" disabled name="patientFields[${pfield.index}].fieldValue" value="${patientInfo.fieldValue}" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
                                                   </c:otherwise>
                                                </c:choose>
                                            </div>
                                         </div>
                                    </c:forEach>
                                </div>

                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Message Details: </h4></div>
                                    <c:forEach items="${transactionDetails.detailFields}" var="detailInfo" varStatus="dfield">
                                         <div class="col-md-6">
                                            <div id="fieldDiv_${detailInfo.fieldNo}" class="form-group">
                                                <label class="control-label" for="fieldA">${detailInfo.fieldLabel}</label>
                                                <c:choose>
                                                    <c:when test="${detailInfo.fieldSelectOptions.size() > 0}">
                                                        <select disabled id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" class="form-control <c:if test="${detailInfo.required == true}"> required</c:if>">
                                                            <option value="">-Choose-</option>
                                                            <c:forEach items="${detailInfo.fieldSelectOptions}" var="options">
                                                                <option value="${options.optionValue}" <c:if test="${detailInfo.fieldValue == options.optionValue}">selected</c:if>>${options.optionDesc}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input disabled id="${detailInfo.fieldNo}" name="detailFields[${dfield.index}].fieldValue" value="${detailInfo.fieldValue}" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
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
           </form:form>
        </div>
    </div>
</div>
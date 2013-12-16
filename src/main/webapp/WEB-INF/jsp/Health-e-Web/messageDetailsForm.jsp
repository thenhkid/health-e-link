<%-- 
    Document   : messageDetailsForm
    Created on : Dec 12, 2013, 2:47:34 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="#">My Account</a></li>
                <li><a href="#">eRG</a></li>
                <li class="active">Create New Referral</li>
            </ol>

            <h2 class="form-title">Create New Message</h2>
            <form role="form" class="form">

                <div class="panel-group form-accordion" id="accordion">

                    <div class="panel panel-default panel-form">
                        <div id="collapseThree" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <div class="form-section row">
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Originating Organization: </h4>
                                        <dl class="vcard">
                                            <dd class="fn">${transaction.transactionRecords[0][0].fieldValue}</dd>
                                            <dd class="adr">
                                                <span class="street-address">${transaction.transactionRecords[0][1].fieldValue}</span><br/>
                                                <c:if test="${not empty transaction.transactionRecords[0][2].fieldValue}"><span class="street-address">${transaction.transactionRecords[0][2].fieldValue}</span><br/></c:if>
                                                <span class="region">${transaction.transactionRecords[0][3].fieldValue}&nbsp;${transaction.transactionRecords[0][4].fieldValue}</span>, <span class="postal-code">${transaction.transactionRecords[0][5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transaction.transactionRecords[0][6].fieldValue}"><dd>phone: <span class="tel">${transaction.transactionRecords[0][6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transaction.transactionRecords[0][7].fieldValue}"><dd>fax: <span class="tel">${transaction.transactionRecords[0][7].fieldValue}</span></dd></c:if>
                                        </dl>
                                    </div>
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Recipient Organization:</h4>
                                        <dl class="vcard">
                                            <dd class="fn">${transaction.transactionRecords[2][0].fieldValue}</dd>
                                            <dd class="adr">
                                                <span class="street-address">${transaction.transactionRecords[2][1].fieldValue}</span><br/>
                                                <c:if test="${not empty transaction.transactionRecords[2][2].fieldValue}"><span class="street-address">${transaction.transactionRecords[2][2].fieldValue}</span><br/></c:if>
                                                <span class="region">${transaction.transactionRecords[2][3].fieldValue}&nbsp;${transaction.transactionRecords[2][4].fieldValue}</span>, <span class="postal-code">${transaction.transactionRecords[2][5].fieldValue}</span>
                                            </dd>
                                            <c:if test="${not empty transaction.transactionRecords[2][6].fieldValue}"><dd>phone: <span class="tel">${transaction.transactionRecords[2][6].fieldValue}</span></dd></c:if>
                                            <c:if test="${not empty transaction.transactionRecords[2][7].fieldValue}"><dd>fax: <span class="tel">${transaction.transactionRecords[2][7].fieldValue}</span></dd></c:if>
                                        </dl>
                                    </div>
                                </div>
                                <div class="form-section row">
                                    <div class="col-md-12">
                                        <h4 class="form-section-heading">Patient Information: </h4>
                                        <c:forEach items="${transaction.transactionRecords[4]}" var="patientInfo">
                                            <div class="form-group">
                                                <label for="fieldA">${patientInfo.fieldLabel} <c:if test="${patientInfo.required == true}">&nbsp;*</c:if></label>
                                                <c:choose>
                                                    <c:when test="${patientInfo.validation.contains('Date')}">
                                                        <div class="form-inline form-inline-varient">
                                                            <div class="form-group">
                                                                <select class="form-control dateField">
                                                                    <option value="">-m-</option>
                                                                    <c:forEach begin="1" end="12" var="m"><option value="${m}">${m}</option></c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="form-group">
                                                                <select class="form-control dateField">
                                                                    <option value="">-d-</option>
                                                                    <c:forEach begin="1" end="31" var="d"><option value="${d}">${d}</option></c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="form-group">
                                                                <select class="form-control dateField">
                                                                    <option value="">-y-</option>
                                                                    <c:forEach begin="1900" end="${currentYear}" var="y">
                                                                        <option value="${y}">${y}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                    <c:when test="${patientInfo.fieldSelectOptions.size() > 0}">
                                                        <select class="form-control <c:if test="${patientInfo.required == true}"> required</c:if>">
                                                            <option value="">-Choose-</option>
                                                            <c:forEach items="${patientInfo.fieldSelectOptions}" var="options">
                                                                <option value="${options.optionValue}">${options.optionDesc}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input id="fieldA" class="form-control ${patientInfo.validation.replace(' ','-')} <c:if test="${patientInfo.required == true}"> required</c:if>" type="text">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>

                                <div class="form-section row">
                                    <div class="col-md-12">
                                        <h4 class="form-section-heading">Message Details: </h4>
                                        <c:forEach items="${transaction.transactionRecords[5]}" var="detailInfo">
                                            <div class="form-group">
                                                <label for="fieldA">${detailInfo.fieldLabel} <c:if test="${detailInfo.required == true}">&nbsp;*</c:if></label>
                                                <c:choose>
                                                    <c:when test="${detailInfo.validation.contains('Date')}">
                                                        <div class="form-inline form-inline-varient">
                                                            <div class="form-group">
                                                                <select class="form-control dateField">
                                                                    <option value="">-m-</option>
                                                                    <c:forEach begin="1" end="12" var="m"><option value="${m}">${m}</option></c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="form-group">
                                                                <select class="form-control dateField">
                                                                    <option value="">-d-</option>
                                                                    <c:forEach begin="1" end="31" var="d"><option value="${d}">${d}</option></c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="form-group">
                                                                <select class="form-control dateField">
                                                                    <option value="">-y-</option>
                                                                    <c:forEach begin="1900" end="${currentYear}" var="y">
                                                                        <option value="${y}">${y}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                    <c:when test="${detailInfo.fieldSelectOptions.size() > 0}">
                                                        <select class="form-control <c:if test="${detailInfo.required == true}"> required</c:if>">
                                                            <option value="">-Choose-</option>
                                                            <c:forEach items="${detailInfo.fieldSelectOptions}" var="options">
                                                                <option value="${options.optionValue}">${options.optionDesc}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input id="fieldA" class="form-control ${detailInfo.validation.replace(' ','-')} <c:if test="${detailInfo.required == true}"> required</c:if>" type="text">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>


                                <div class="form-group">
                                    <input type="submit" class="btn btn-primary btn-action" value="Send Referral"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
        </form>

    </div>
</div>
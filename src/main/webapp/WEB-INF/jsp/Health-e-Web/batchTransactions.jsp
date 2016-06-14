<%-- 
    Document   : create
    Created on : Dec 12, 2013, 1:12:54 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<c:set var="referralId" value=""/>
<c:set var="urgencyVal" value="Not Reported" />
<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li><a href="#">eRG</a></li>
                <li>
                    <c:choose>
                        <c:when test="${fromPage == 'inbox'}"><a href="<c:url value='/Health-e-Web/inbox'/>">Inbox</a></c:when>
                        <c:when test="${fromPage == 'pending'}"><a href="<c:url value='/Health-e-Web/pending'/>">Pending</a></c:when>
			<c:otherwise><a href="<c:url value='/Health-e-Web/sent'/>">Sent</a></c:otherwise>
                    </c:choose>
                </li>
            </ol>

            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    <c:choose>
                        <c:when test="${savedStatus == 'saved'}">Your message has been successfully saved!</c:when>
                    </c:choose>
                </div>
            </c:if>   
            
            <div class="row" style="overflow:hidden;">
               <div class="col-md-12">
                    <form:form class="form form-inline" id="searchForm" action="${fromPage == 'sent' ? '/Health-e-Web/sent' : '/Health-e-Web/inbox'}" method="post">
                        <div class="form-group">
                            <c:if test="${fromPage == 'inbox'}"><input type="hidden" name="viewOnly" id="viewOnly" value="${viewOnly}" /></c:if>
                            <input type="hidden" name="fromDate" id="fromDate" rel="<fmt:formatDate value="${fromDate}" type="date" pattern="MM/dd/yyyy" />" rel2="<fmt:formatDate value="${userDetails.dateOrgWasCreated}" type="date" pattern="MM/dd/yyyy" />" value="${fromDate}" />
                            <input type="hidden" name="toDate" id="toDate" rel="<fmt:formatDate value="${toDate}" type="date" pattern="MM/dd/yyyy" />" value="${toDate}" />
                        </div>
                    </form:form>
                </div>
            </div>    

            <form:form action="" id="viewTransactionDetails" method="post">
                <input type="hidden" id="transactionId" name="transactionId" value="" />
                <input type="hidden" id="fromPage" name="fromPage" value="${fromPage}" />
                <input type="hidden" id="configId" name="configId" value="" />
            </form:form>    
            <div class="form-container scrollable">
                <c:if test="${fromPage == 'inbox' && userDetails.orgType == 2}">
                    <div class="pull-right" style="width:265px; margin-left: 10px;">
                        <div class="form-group">
                            <select id="viewOnlySelectBox" class="form-control">
                                <option value="0" <c:if test="${viewOnly == 0}">selected</c:if>>Show Open & Closed Referrals</option>
                                <option value="1" <c:if test="${viewOnly == 1}">selected</c:if>>Show Open Referrals</option>
                                <option value="2" <c:if test="${viewOnly == 2}">selected</c:if>>Show Closed Referrals</option>
                            </select>
                            </div>
                        </div>
                    </div>
                </c:if>        
                <div class="date-range-picker-trigger form-control pull-right daterange" style="width:265px; margin-left: 10px;">
                    <i class="glyphicon glyphicon-calendar"></i>
                    <span class="date-label"  rel="${fromDate}" rel2="${toDate}"><fmt:formatDate value="${fromDate}" type="date" pattern="MMM dd, yyyy" /> - <fmt:formatDate value="${toDate}" type="date" pattern="MMM dd, yyyy" /></span> <b class="caret"></b>
                </div>
                <table class="table table-striped table-hover table-default"  <c:if test="${not empty transactions}">id="dataTable"</c:if>>
                    <caption style="display:none">Transactions</caption>
                    <thead>
                        <tr>
                            <th scope="col">Message Type</th>
                            <c:if test="${fromPage == 'inbox'}">
                                <th scope="col">Referring Site Name</th>
                            </c:if>    
                            <c:if test="${fromPage != 'inbox'}"><th scope="col">Sent To</th></c:if>    
                            <th scope="col">Patient Information</th>
                            <th scope="col" class="center-text">System Status</th>
                            <th scope="col" class="center-text">Date Submitted</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty transactions}">
                                <c:forEach var="transaction" items="${transactions}">
                                    <tr>
                                        <td scope="row">
                                            ${transaction.messageTypeName}
                                            <c:if test="${fromPage == 'inbox'}"><br />
                                                <c:forEach begin="0" var="dStatus" end="${transaction.detailFields.size()}">
                                                    <c:if test="${fn:toLowerCase(transaction.detailFields[dStatus].fieldLabel) == 'referral id' || fn:toLowerCase(transaction.detailFields[dStatus].fieldLabel) == 'order id'}">
                                                        <c:set var="referralId" value="${transaction.detailFields[dStatus].fieldValue}"/>
                                                    </c:if>
                                                </c:forEach>
                                                <c:if test="${not empty referralId && referralId != 'null'}">
                                                    (${referralId})
                                                </c:if>
                                                
                                            </c:if>
                                        </td>
                                        <c:if test="${fromPage == 'inbox'}">
                                            <td style="max-width: 180px;">
                                                ${transaction.sourceOrgFields[0].fieldValue}
                                                <dd class="adr">
                                                    <span class="street-address">${transaction.sourceOrgFields[1].fieldValue}</span> <c:if test="${not empty transaction.sourceOrgFields[2].fieldValue and transaction.sourceOrgFields[2].fieldValue != 'null'}"><span class="street-address">${transaction.sourceOrgFields[2].fieldValue}</span></c:if><br/>
                                                    <c:if test="${not empty transaction.sourceOrgFields[4].fieldValue and transaction.sourceOrgFields[4].fieldValue != 'null'}"><span class="region">${transaction.sourceOrgFields[3].fieldValue}&nbsp;${transaction.sourceOrgFields[4].fieldValue}</span>, </c:if> <span class="postal-code">${transaction.sourceOrgFields[5].fieldValue}</span>
                                                </dd>
                                                <c:if test="${not empty transaction.sourceOrgFields[6].fieldValue and transaction.sourceOrgFields[6].fieldValue != 'null'}"><dd>phone: <span class="tel">${transaction.sourceOrgFields[6].fieldValue}</span></dd></c:if>
                                                <c:if test="${not empty transaction.sourceOrgFields[7].fieldValue and transaction.sourceOrgFields[7].fieldValue != 'null'}"><dd>fax: <span class="tel">${transaction.sourceOrgFields[7].fieldValue}</span></dd></c:if>
                                            </td>
                                        </c:if>  
                                        <c:if test="${fromPage != 'inbox'}">
                                            <td style="max-width: 180px;">
                                                ${transaction.targetOrgFields[0].fieldValue}
                                                <dd class="adr">
                                                    <span class="street-address">${transaction.targetOrgFields[1].fieldValue}</span><br/>
                                                    <c:if test="${not empty transaction.targetOrgFields[2].fieldValue and transaction.targetOrgFields[2].fieldValue != 'null'}"><span class="street-address">${transaction.targetOrgFields[2].fieldValue}</span><br/></c:if>
                                                    <c:if test="${not empty transaction.targetOrgFields[4].fieldValue and transaction.targetOrgFields[4].fieldValue != 'null'}"><span class="region">${transaction.targetOrgFields[3].fieldValue}&nbsp;${transaction.targetOrgFields[4].fieldValue}</span>, </c:if> <span class="postal-code">${transaction.targetOrgFields[5].fieldValue}</span>
                                                </dd>
                                                <c:if test="${not empty transaction.targetOrgFields[6].fieldValue and transaction.targetOrgFields[6].fieldValue != 'null'}"><dd>phone: <span class="tel">${transaction.targetOrgFields[6].fieldValue}</span></dd></c:if>
                                                <c:if test="${not empty transaction.targetOrgFields[7].fieldValue and transaction.targetOrgFields[7].fieldValue != 'null'}"><dd>fax: <span class="tel">${transaction.targetOrgFields[7].fieldValue}</span></dd></c:if>
                                            </td>
                                       </c:if>     
                                        <td style="max-width: 180px;">
                                            <c:forEach var="patientField" items="${transaction.patientFields}">
                                                <c:if test="${patientField.saveToTableCol == 'firstName'}">${patientField.fieldValue}</c:if>
                                                <c:if test="${patientField.saveToTableCol == 'lastName'}">${patientField.fieldValue}<br /></c:if>
                                                <c:if test="${patientField.saveToTableCol == 'line1'}">
                                                    <dd class="adr">
                                                        ${patientField.fieldValue}
                                                </c:if>
                                                <c:if test="${patientField.saveToTableCol == 'line2'}">
                                                    ${patientField.fieldValue}<br />
                                                </c:if>      
                                                <c:if test="${patientField.saveToTableCol == 'city'}">
                                                    ${patientField.fieldValue}
                                                </c:if>  
                                                <c:if test="${patientField.saveToTableCol == 'state'}">
                                                    ${patientField.fieldValue}
                                                </c:if> 
                                                <c:if test="${patientField.saveToTableCol == 'postalCode'}">
                                                    ${patientField.fieldValue}<br />
                                                </c:if>    
                                                <c:if test="${patientField.saveToTableCol == 'phone1'}">
                                                    <span class="postal-code">${patientField.fieldValue}</span>
                                                    </dd>
                                                </c:if>
                                            </c:forEach>
                                           <%-- ${transaction.patientFields.size()}
                                            ${transaction.patientFields[0].fieldValue}&nbsp;${transaction.patientFields[1].fieldValue}
                                            <dd class="adr">
                                                <span class="street-address">${transaction.patientFields[4].fieldValue}</span><br/>
                                                <c:if test="${not empty transaction.patientFields[5].fieldValue and transaction.patientFields[5].fieldValue != 'null'}"><span class="street-address">${transaction.patientFields[5].fieldValue}</span><br/></c:if>
                                                <c:if test="${not empty transaction.patientFields[6].fieldValue and transaction.patientFields[6].fieldValue != 'null'}"><span class="region">${transaction.patientFields[6].fieldValue}&nbsp;${transaction.patientFields[7].fieldValue}</span>, </c:if> <span class="postal-code">${transaction.patientFields[8].fieldValue}</span>
                                            </dd>--%>
                                        </td>
                                       <td class="center-text">
                                           <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${transaction.statusId}" title="View this Status">${transaction.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a>
                                       </td>
                                       <td class="center-text"><fmt:formatDate value="${transaction.dateSubmitted}" type="date" pattern="M/dd/yyyy h:mm:ss a" /></td>
                                       <td class="actions-col" style="width:50px;">
                                           <a href="javascript:void(0);" rel="${transaction.transactionRecordId}" rel3="${userDetails.editAuthority}" rel2="${transaction.configId}" class="btn btn-link viewLink">
                                            <span class="glyphicon glyphicon-edit"></span>
                                            View
                                          </a>
                                      </td>
                                  </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr><td colspan="7" class="center-text">There are currently no transactions for the selected date range.</td></tr>
                        </c:otherwise>
                    </c:choose>                  
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- Status Definition modal -->
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>

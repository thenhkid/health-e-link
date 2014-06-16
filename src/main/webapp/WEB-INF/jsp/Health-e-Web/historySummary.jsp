<%-- 
    Document   : historySearch
    Created on : May 8, 2014, 8:49:35 AM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li class="active"><a href="<c:url value='/Health-e-Web/history'/>">History</a></li>
                <li class="active">History Search Results</li>
            </ol>
        </div>
    </div>
    <div class="row" style="padding-bottom:10px;">
        <div class="col-md-12 page-content">
            <div class="col-md-12">
                <a href="<c:url value='/Health-e-Web/history'/>" class="btn btn-primary btn-action-sm pull-right">Return to Search Page</a>
                <a href="javascript:void(0);" class="btn btn-primary btn-action-sm print pull-right" style="margin-right:10px;">Print</a>
            </div>
        </div> 
    </div>

    <div class="row"> 
        <div class="col-md-12 page-content">

            <div class="col-md-12 form-section">
                <section class="panel panel-default panel-collapse">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            Selected Criteria
                            <a data-toggle="collapse" href="#collapse-criteria"> - Click to Show</a>
                        </h3>
                    </div>
                    <div id="collapse-criteria" class="panel-collapse collapse">    
                        <div class="panel-body">
                            <div class="form-container scrollable">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label class="control-label" for="fromDate">Date Range</label>
                                        <div><fmt:formatDate value="${fromDate}" type="date" pattern="MM/dd/yyyy" /> - <fmt:formatDate value="${toDate}" type="date" pattern="MM/dd/yyyy" /></div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="type"></label>
                                        <div class="control-label">${typeText}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="sentTo">Affiliated Organization</label>
                                        <div>${sentToText}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="messageType"> Message Type</label>
                                        <div>${messageTypeText}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="status"> Referral Status</label>
                                        <div>${statusText}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="systemStatus">System Status</label>
                                        <div>${systemStatusText}</div>
                                    </div>
                                </div> 
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="status"> Batch Name</label>
                                        <div>${batchName}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="utBatchName">System Referral Id</label>
                                        <div>${utBatchName}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="lastName">Patient Last Name</label>
                                        <div>${lastName}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="patientId">Patient Id</label>
                                        <div>${patientId}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="firstName">Patient First Name</label>
                                        <div>${firstName}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="providerId">Provider Id</label>
                                        <div>${providerId}</div>
                                    </div>
                                </div>  
                            </div>
                        </div>
                    </div>
                </section>

                <ul class="nav nav-tabs navbar-actions">
                    <c:if test="${type == 0 || type == 1}"><li ${type == 0 || type == 1 ? 'class="active"' : ''}><a href="#sent" data-toggle="tab" class="btn btn-link">Sent Messages <span class="badge" style="border:1px solid #fff">${sentTotal}</span></a></li></c:if>
                    <c:if test="${type == 0 || type == 2}"><li ${type == 2 ? 'class="active"' : ''}><a href="#received" data-toggle="tab" class="btn btn-link">Received Messages  <span class="badge" style="border:1px solid #fff">${receivedTotal}</span></a></li></c:if>
                    </ul>       

                    <div id='content' class="tab-content">
                    <c:if test="${type == 0 || type == 1}"> ${reportType}
                        <div class="tab-pane active" id="sent">       
                            <div class="form-container scrollable" style="padding-top:20px;">
                                <table class="table table-striped table-hover table-default" <c:if test="${not empty sentMessages}">id="dataTable"</c:if>>
                                        <caption style="display:none">Sent Messages</caption>
                                        <thead>
                                            <tr>
                                            <c:choose>
                                                <c:when test="${showDetails == false}">
                                                    <th scope="col">Affiliated Organization</th>
                                                    <th scope="col">Transport Method</th>
                                                    <th scope="col">Message Type</th>
                                                    <th scope="col" class="center-text">Total Sent</th> 
                                                    </c:when>
                                                    <c:otherwise>
                                                    <th scope="col">Affiliated Organization</th>
                                                    <th scope="col">Message Type</th>
                                                    <th scope="col">Batch Name</th>
                                                    <th scope="col">Patient Name</th>
                                                    <th scope="col" class="center-text">Date Sent</th>
                                                    </c:otherwise>    
                                                </c:choose>         
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty sentMessages}">
                                                <c:choose>
                                                    <c:when test="${showDetails == false}">
                                                        <c:forEach var="result" items="${sentMessages}">
                                                            <tr>
                                                                <td scope="row">${result.orgName}</td>
                                                                <td>
                                                                    ${result.transportType}
                                                                </td>
                                                                <td>
                                                                    ${result.messageType}
                                                                </td>
                                                                <td class="center-text">
                                                                    ${result.totalSent}
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="result" items="${sentMessages}">
                                                            <tr>
                                                                <td scope="row">${result.orgName}</td>
                                                                <td>
                                                                    ${result.messageType}
                                                                </td>
                                                                <td>
                                                                    ${result.batchName}
                                                                </td>
                                                                <td>
                                                                    ${result.patientName}
                                                                    <br />
                                                                    Patient Id: ${result.patientId}
                                                                </td>
                                                                <td class="center-text">
                                                                    <fmt:formatDate value="${result.dateCreated}" type="date" pattern="M/dd/yyyy" /><br /><fmt:formatDate value="${result.dateCreated}" type="time" pattern="h:mm:ss a" />
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <tr><td colspan="${showDetails == false ? '4' : '5'}">There were no sent messages found</td></tr>
                                            </c:otherwise>                    
                                        </c:choose>   
                                    </tbody>
                                </table>
                            </div> 
                        </div>
                    </c:if>
                    <c:if test="${type == 0 || type == 2}">                    
                        <div class="tab-pane" id="received">       
                            <div class="form-container scrollable" style="padding-top:20px;">
                                <table class="table table-striped table-hover table-default" <c:if test="${not empty receivedMessages}">id="dataTableReceived"</c:if>>
                                        <caption style="display:none">Received Messages</caption>
                                        <thead>
                                            <tr>
                                            <c:choose>
                                                <c:when test="${showDetails == false}">
                                                    <th scope="col">Affiliated Organization</th>
                                                    <th scope="col">Transport Method</th>
                                                    <th scope="col">Message Type</th>
                                                    <th scope="col" class="center-text">Total Received</th> 
                                                    </c:when>
                                                    <c:otherwise>
                                                    <th scope="col">Affiliated Organization</th>
                                                    <th scope="col">Message Type</th>
                                                    <th scope="col">Batch Name</th>
                                                    <th scope="col">Patient Name</th>
                                                    <th scope="col" class="center-text">Date Received</th>
                                                    </c:otherwise>    
                                                </c:choose>         
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty receivedMessages}">
                                                <c:choose>
                                                    <c:when test="${showDetails == false}">
                                                        <c:forEach var="result" items="${receivedMessages}">
                                                            <tr>
                                                                <td scope="row">${result.orgName}</td>
                                                                <td>
                                                                    ${result.transportType}
                                                                </td>
                                                                <td>
                                                                    ${result.messageType}
                                                                </td>
                                                                <td class="center-text">
                                                                    ${result.totalSent}
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="result" items="${receivedMessages}">
                                                            <tr>
                                                                <td scope="row">${result.orgName}</td>
                                                                <td>
                                                                    ${result.messageType}
                                                                </td>
                                                                <td>
                                                                    ${result.batchName}
                                                                </td>
                                                                <td>
                                                                    ${result.patientName}
                                                                    <br />
                                                                    Patient Id: ${result.patientId}
                                                                </td>
                                                                <td class="center-text">
                                                                    <fmt:formatDate value="${result.dateCreated}" type="date" pattern="M/dd/yyyy" /><br /><fmt:formatDate value="${result.dateCreated}" type="time" pattern="h:mm:ss a" />
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <tr><td colspan="${showDetails == false ? '4' : '5'}">There were no received messages found</td></tr>
                                            </c:otherwise>                
                                        </c:choose>                  
                                    </tbody>
                                </table>
                            </div>  
                        </div>
                    </div> 
                </c:if>


            </div>
        </div>
    </div>
</div>

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
                <li class="active">History Detail Results</li>
            </ol>
        </div>
    </div>
    <div class="row" style="padding-bottom:10px;">
        <div class="col-md-12 page-content">
            <div class="col-md-12">
                <a href="javascript:void(0);" class="btn btn-primary btn-action-sm returnResults pull-right">Return to Search Results</a>
                <a href="javascript:void(0);" class="btn btn-primary btn-action-sm print pull-right" style="margin-right:10px;">Print</a>
            </div>
        </div> 
    </div>

    <div class="row"> 
        <form action="/Health-e-Web/history" id="viewHistoryResults" method="post">
            <input type="hidden" name="fromDate" value="${fromDate}" />
            <input type="hidden" name="toDate" value="${toDate}" />
            <input type="hidden" name="type" value="${type}" />
            <input type="hidden" name="sentTo" value="${sentTo}" />
            <input type="hidden" name="messageType" value="${messageType}" />
            <input type="hidden" name="receivedFrom" value="${receivedFrom}" />
            <input type="hidden" name="status" value="${status}" />
            <input type="hidden" name="systemStatus" value="${systemStatus}" />
            <input type="hidden" name="batchName" value="${batchName}" />
            <input type="hidden" name="utBatchName" value="${utBatchName}" />
            <input type="hidden" name="lastName" value="${lastName}" />
            <input type="hidden" name="firstName" value="${firstName}" />
            <input type="hidden" name="patientId" value="${patientId}" />
            <input type="hidden" name="providerId" value="${providerId}" />
            <input type="hidden" name="reportType" value="2" />
        </form>   
        <div class="col-md-12 page-content">

            <div class="col-md-12 form-section" >
                <section id="searchCriteriaSection" class="panel panel-default panel-collapse">
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
                                        <label class="control-label" for="type">Type</label>
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


                <section class="panel panel-default" >
                    <div class="panel-body" style="background-color:#f1f1f1">
                        <div class="col-md-6">
                            <div class="form-group">
                                <h3 class="panel-title">Affiliated Organization: ${orgName}</h3>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <h3 class="panel-title">Message Type: ${messageTypeName}</h3>
                            </div>
                        </div>
                    </div>
                </section>            


                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default" <c:if test="${not empty historyResults}">id="dataTable"</c:if>>
                            <thead>
                                <tr>
                                    <th scope="col">Type</th>
                                    <th scope="col">Batch Name</th>
                                    <th scope="col">Patient Name</th>
                                    <th scope="col" class="center-text">Patient Id</th>
                                    <th scope="col" class="center-text">Status</th>
                                    <th scope="col" class="center-text">Date Sent</th>
                                    <%--<th scope="col"></th>--%>
                                </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty transactions}">
                                    <c:forEach var="trans" items="${transactions}">
                                        <tr>
                                            <td scope="row">${trans.type}</td>
                                            <td>
                                                ${trans.batchName}
                                            </td>
                                            <td>
                                                ${trans.patientName}
                                            </td>
                                            <td class="center-text">
                                                ${trans.patientId}
                                            </td>
                                            <td class="center-text">
                                                ${trans.status}
                                            </td>
                                            <td class="center-text"><fmt:formatDate value="${trans.dateCreated}" type="date" pattern="M/dd/yyyy" /><br /><fmt:formatDate value="${trans.dateCreated}" type="time" pattern="h:mm:ss a" /></td>
                                            <%--<td>
                                                <a href="javascript:void(0);" class="viewDetals" >View</a>
                                            </td>--%>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                            </c:choose>                  
                        </tbody>
                    </table>
                </div> 
            </div>
        </div>
    </div>
</div>

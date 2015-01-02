<%-- 
    Document   : activityReport
    Created on : Dec 11, 2014, 9:33:19 AM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="main clearfix" role="main">
    <div class="col-md-9">
        
        <div class="row-fluid contain basic-clearfix">
             <form:form class="form form-inline" id="searchForm" action="/administrator/processing-activity/activityReport" method="post">
                <div class="form-group">
                    <input type="hidden" name="fromDate" id="fromDate" rel="<fmt:formatDate value="${fromDate}" type="date" pattern="MM/dd/yyyy" />" rel2="<fmt:formatDate value="${originalDate}" type="date" pattern="MM/dd/yyyy" />" value="${fromDate}" />
                    <input type="hidden" name="toDate" id="toDate" rel="<fmt:formatDate value="${toDate}" type="date" pattern="MM/dd/yyyy" />" value="${toDate}" />
                </div>
            </form:form>
            <div class="form-container scrollable">
                <div class="date-range-picker-trigger form-control pull-right daterange" style="width:285px; margin-left: 10px;">
                    <i class="glyphicon glyphicon-calendar"></i>
                    <span class="date-label"><fmt:formatDate value="${fromDate}" type="date" pattern="MMMM dd, yyyy" /> - <fmt:formatDate value="${toDate}" type="date" pattern="MMMM dd, yyyy" /></span> <b class="caret"></b>
                </div>
            </div>
        </div>
        
        <div class="row-fluid contain basic-clearfix">
            <div class="col-md-3 col-sm-3 col-xs-6">
                <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Number of Organizations">
                    <div class="panel-body">
                        <span class="stat-number"><a href="javascript:void(0);"><c:choose><c:when test="${totalReferrals >= 0}">${totalReferrals}</c:when><c:otherwise>0</c:otherwise></c:choose></a></span>
                        <h3>Total Referrals Sent</h3>
                    </div>
                </section>
            </div>
            <div class="col-md-3 col-sm-3 col-xs-6">
                <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Number of Organizations">
                    <div class="panel-body">
                        <span class="stat-number"><a href="javascript:void(0);"><c:choose><c:when test="${totalFBReports >= 0}">${totalFBReports}</c:when><c:otherwise>0</c:otherwise></c:choose></a></span>
                        <h3>Total Feedback Reports Sent</h3>
                    </div>
                </section>
            </div>            
        </div>
        
        <section class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Sent Referrals</h3>
            </div>
            <div class="panel-body">
                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                            <thead>
                                <tr>
                                    <th scope="col" style="width:350px;">Organization Name</th>
                                    <th scope="col" style="width:350px;">Message Type</th>
                                    <th scope="col" class="center-text">Total Referrals Sent</th>
                                </tr>
                                <c:choose>
                                    <c:when test="${not empty referralList}">
                                         <c:forEach var="item" items="${referralList}">
                                            <tr>
                                                <td scope="row">
                                                    ${item.orgName}
                                                </td>
                                                <td>
                                                    ${item.messageType}
                                                </td>
                                                <td  class="center-text">
                                                    ${item.total}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="3">
                                                There were no referrals sent for the dates selected.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </thead>
                    </table>
                </div>
            </div>
        </section>
                        
        <section class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Sent Feedback Reports</h3>
            </div>
            <div class="panel-body">
                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                            <thead>
                                <tr>
                                    <th scope="col" style="width:350px;">Organization Name</th>
                                    <th scope="col" style="width:350px;">Message Type</th>
                                    <th scope="col" class="center-text">Total Feedback Reports Sent</th>
                                    <th scope="col" class="center-text">Total Open</th>
                                    <th scope="col" class="center-text">Total Closed</th>
                                </tr>
                                <c:choose>
                                    <c:when test="${not empty feedbackReportList}">
                                         <c:forEach var="item" items="${feedbackReportList}">
                                            <tr>
                                                <td scope="row">
                                                    ${item.orgName}
                                                </td>
                                                <td>
                                                    ${item.messageType}
                                                </td>
                                                <td  class="center-text">
                                                    ${item.total}
                                                </td>
                                                <td class="center-text">
                                                   ${item.openTotal}
                                                </td>
                                                <td class="center-text">
                                                   ${item.closedTotal}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="3">
                                                There were no feedback reports sent for the dates selected.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </thead>
                    </table>
                </div>
            </div>
        </section>
                        
                        
        <div class="col-md-4">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Referral Types Sent</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container scrollable">
                        <table class="table table-striped table-hover table-default">
                            <thead>
                                <tr>
                                    <th scope="col" style="width:350px;">Message Type</th>
                                    <th scope="col" class="center-text">Total Sent</th>
                                </tr>
                                <c:choose>
                                    <c:when test="${not empty referralTypesMade}">
                                         <c:forEach var="type" items="${referralTypesMade}">
                                            <tr>
                                                <td scope="row">
                                                    ${type.key}
                                                </td>
                                                <td class="center-text">
                                                    ${type.value}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="2">
                                                There were no referrals were sent for the dates selected.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </thead>
                        </table>
                    </div>
                    
                </div>
            </section>
        </div>
        <div class="col-md-4">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Feedback Reports Sent</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container scrollable">
                        <table class="table table-striped table-hover table-default">
                            <thead>
                                <tr>
                                    <th scope="col" style="width:350px;">Message Type</th>
                                    <th scope="col" class="center-text">Total Sent</th>
                                </tr>
                                <c:choose>
                                    <c:when test="${not empty fbTypesMade}">
                                         <c:forEach var="type" items="${fbTypesMade}">
                                            <tr>
                                                <td scope="row">
                                                    ${type.key}
                                                </td>
                                                <td class="center-text">
                                                    ${type.value}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="2">
                                                There were no feedback reports sent for the dates selected.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </thead>
                        </table>
                    </div>
                </div>
            </section>
        </div>       
         <div class="col-md-4">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Activity Status Totals</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container scrollable">
                        <table class="table table-striped table-hover table-default">
                            <thead>
                                <tr>
                                    <th scope="col" style="width:350px;">Activity Status</th>
                                    <th scope="col" class="center-text">Total</th>
                                </tr>
                                <tr>
                                    <td scope="row">
                                        Program Completed
                                    </td>
                                    <td class="center-text">
                                      ${totalCompleted}
                                    </td>
                                </tr>
                                <tr>
                                    <td scope="row">
                                       Patients Enrolled
                                    </td>
                                    <td class="center-text">
                                       ${totalEnrolled}
                                    </td>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </section>
        </div>                              
    </div>
</div>

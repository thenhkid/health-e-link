<%-- 
    Document   : create
    Created on : Dec 12, 2013, 1:12:54 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li class="active">Pending Messages</li>
            </ol>
            <form action="create/details" id="createMessageForm" method="post">
                <input type="hidden" id="configId" name="configId" value="" />
                <input type="hidden" id="transactionId" name="transactionId" value="" />
            </form>    
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <thead>
                        <tr>
                            <th scope="col">Message Type</th>
                            <th scope="col">Target Organization</th>
                            <th scope="col" class="center-text">Date Submitted</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty pendingTransactions}">
                                <c:forEach var="transaction" items="${pendingTransactions}">
                                    <tr>
                                        <td scope="row">${transaction.messageTypeName}</td>
                                        <td>
                                            
                                        </td>
                                        <td class="center-text"><fmt:formatDate value="${transaction.dateSubmitted}" type="date" pattern="M/dd/yyyy" /></td>
                                        <td class="actions-col" style="width:200px;">
                                            <a href="javascript:void(0);" rel2="${transaction.configId}" rel="${transaction.transactionRecordId}" class="btn btn-link createLink">
                                                <span class="glyphicon glyphicon-edit"></span>
                                                View
                                            </a>
                                        </td>
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

<%-- 
    Document   : create
    Created on : Dec 12, 2013, 1:12:54 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li><a href="<c:url value='/Health-e-Web/inbox'/>">eRG</a></li>
                <li class="active">Available Message Types</li>
            </ol>
            <form:form action="create/details" id="createMessageForm" method="post">
                <input type="hidden" id="configId" name="configId" value="" />
                <input type="hidden" id="targetOrg" name="targetOrg" value="" />
                <input type="hidden" id="targetConfig" name="targetConfig" value="" />
            </form:form>
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <caption style="display:none">Associated Organizations</caption>
                    <thead>
                        <tr>
                            <th scope="col">Message Type</th>
                            <th scope="col">Target Organization</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty configurations}">
                                <c:forEach var="config" items="${configurations}">
                                    <tr>
                                        <td scope="row">${config.messageTypeName}</td>
                                        <td>
                                            <div id="row_${config.id}" class="form-group">
                                            <c:choose>
                                                <c:when test="${config.connections.size() > 1}">
                                                    <select id="targetOrg_${config.id}" class="form-control">
                                                        <option value="">- Select Target Organization -</option>
                                                        <c:forEach items="${config.connections}" var="connection" varStatus="cStatus">
                                                            <option value="${connection.targetConfigId}-${connection.targetOrgId}">${connection.targetOrgName}</option>
                                                        </c:forEach> 
                                                    </select>    
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${config.connections}" var="connection" varStatus="cStatus">
                                                        <input type="hidden" id="targetOrg_${config.id}" value="${connection.targetConfigId}-${connection.targetOrgId}" />
                                                        ${connection.targetOrgName}
                                                    </c:forEach> 
                                                </c:otherwise>
                                            </c:choose>
                                            <span id="rowMsg_${config.id}" class="control-label"></span>            
                                            </div>
                                        </td>
                                        <td class="actions-col" style="width:200px;">
                                            <a href="javascript:void(0);" rel="${config.id}" class="btn btn-link createLink">
                                                <span class="glyphicon glyphicon-edit"></span>
                                                Create
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                           </c:when>
                            <c:otherwise>
                                <tr><td colspan="3" class="center-text">There are no configurations set up for your account.</td>
                            </c:otherwise>
                      </c:choose>                  
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

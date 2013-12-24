<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="main clearfix full-width" role="main">
    <div class="col-md-12">
        <c:if test="${not empty param.msg}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose>
                    <c:when test="${param.msg == 'created'}">The configuration connection has been successfully created!</c:when>
                    <c:when test="${param.msg == 'updated'}">The configuration connection status has been successfully updated!</c:when>
                </c:choose>
            </div>
        </c:if>

        <section class="panel panel-default">
            <div class="panel-body">
                <div class="table-actions">
                    <div role="search">
                        <form:form class="form form-inline" action="/administrator/configuration/connections" method="post">
                            <div class="form-group">
                                <label class="sr-only" for="searchTerm">Search</label>
                                <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-organizations" placeholder="Search"/>
                            </div>
                            <button id="searchOrgBtn" class="btn btn-primary btn-sm" title="Search Connections" role="button">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </form:form>
                    </div>
                </div>
                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col">Source Organization Name</th>
                                <th scope="col">Message Type</th>
                                <th scope="col" class="center-text">Source Transport Method</th>
                                <th scope="col">Target Organization Name</th>
                                <th scope="col" class="center-text">Target Transport Method</th>
                                <th scope="col" class="center-text">Date Created</th>
                                <th scope="col" class="center-text">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty connections}">
                                    <c:forEach var="connection" items="${connections}">
                                        <tr>
                                            <td scope="row">
                                                <strong>${connection.srcConfigDetails.getOrgName()}</strong>
                                                <br />
                                                Authorized User: ${connection.srcConfigDetails.getuserName()}
                                            </td>
                                            <td>
                                               ${connection.srcConfigDetails.getMessageTypeName()}
                                            </td>
                                            <td class="center-text">
                                                ${connection.srcConfigDetails.gettransportMethod()}
                                            </td>
                                            <td scope="row">
                                                <strong>${connection.tgtConfigDetails.getOrgName()}</strong>
                                                <br />
                                                Authorized User: ${connection.tgtConfigDetails.getuserName()}
                                            </td>
                                            <td class="center-text">
                                               ${connection.tgtConfigDetails.gettransportMethod()}
                                            </td>
                                            <td class="center-text"><fmt:formatDate value="${connection.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                            <td class="center-text actions-col">
                                                <c:choose>
                                                    <c:when test="${connection.status == true}">
                                                        <a href="javascript:void(0);" class="btn btn-link changeStatus" rel2="false" rel="${connection.id}" title="Make this connection inactive" role="button">Active</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="javascript:void(0);" class="btn btn-link changeStatus" rel2="true" rel="${connection.id}" title="Make this connection active" role="button">Inactive</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="7" class="center-text">There are currently no configuration connections set up.</td></tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                    <ul class="pagination pull-right" role="navigation" aria-labelledby="Paging ">
                        <c:if test="${currentPage > 1}"><li><a href="?page=${currentPage-1}">&laquo;</a></li></c:if>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                        <li><a href="?page=${i}">${i}</a></li>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}"><li><a href="?page=${currentPage+1}">&raquo;</a></li></c:if>
                    </ul>
                </div>
            </div>
        </section>
    </div>
</div>
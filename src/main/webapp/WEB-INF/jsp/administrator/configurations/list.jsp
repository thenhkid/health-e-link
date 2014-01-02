<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="main clearfix full-width" role="main">
    <div class="col-md-12">

        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose>
                    <c:when test="${savedStatus == 'updated'}">The configuration has been successfully updated!</c:when>
                    <c:when test="${savedStatus == 'created'}">The configuration has been successfully added!</c:when>
                    <c:when test="${savedStatus == 'deleted'}">The configuration has been successfully removed!</c:when>
                </c:choose>
            </div>
        </c:if>

        <section class="panel panel-default">
            <div class="panel-body">
                <div class="table-actions">
                    <div role="search">
                        <form:form class="form form-inline" action="/administrator/configurations/list" method="post">
                            <div class="form-group">
                                <label class="sr-only" for="searchTerm">Search</label>
                                <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-configurations" placeholder="Search"/>
                            </div>
                            <button id="searchConfigBtn" class="btn btn-primary btn-sm" title="Search Configurations">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </form:form>
                    </div>
                </div>

                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col">Organization</th>
                                <th scope="col">Authorized User</th>
                                <th scope="col">Message Type</th>
                                <th scope="col">Configuration Type</th>
                                <th scope="col" class="center-text">Transport Method</th>
                                <th scope="col" class="center-text">Status</th>
                                <th scope="col" class="center-text">Date Created</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty configurationList}">
                                    <c:forEach var="config" items="${configurationList}">
                                        <tr id="configRow" rel="${config.id}" style="cursor: pointer">
                                            <td scope="row">
                                                ${config.orgName}
                                            </td>
                                            <td>
                                                ${config.userName}
                                            </td>
                                            <td>
                                                ${config.messageTypeName}
                                            </td>
                                            <td>
                                                <c:choose><c:when test="${config.type == 1}">Source Configuration</c:when><c:otherwise>Target Configuration</c:otherwise></c:choose>
                                            </td>
                                            <td class="center-text">
                                                ${config.transportMethod}
                                            </td>
                                            <td class="center-text"><c:choose><c:when test="${config.status == true}">active</c:when><c:otherwise>inactive</c:otherwise></c:choose></td>
                                            <td class="center-text"><fmt:formatDate value="${config.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                            <td class="actions-col">
                                                <a href="javascript:void(0);" class="btn btn-link" title="Edit this configuration">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Edit	
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="8" class="center-text">There are currently no configurations set up.</td></tr>
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="main clearfix" role="main">
    <div class="col-md-12">
         <section class="panel panel-default">
            <div class="panel-body">
                <div class="table-actions">
                    <form:form class="form form-inline" method="post">
                        <div class="form-group">
                            <label class="sr-only" for="searchTerm">Search</label>
                            <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-configurations" placeholder="Search"/>
                        </div>
                        <button id="searchConfigBtn" class="btn btn-primary btn-sm" title="Search Configurations">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </form:form>
                </div>

                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col">Message Type</th>
                                <th scope="col" class="center-text">Connections</th>
                                <th scope="col" class="center-text">Date Created</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty configs}">
                                    <c:forEach var="config" items="${configs}">
                                        <tr style="cursor: pointer">
                                            <td>
                                                <a href="<c:url value='/administrator/configurations/details?i=${config.id}' />" class="btn btn-link" title="Edit this configuration">${config.messageTypeName}</a>
                                            </td>
                                            <td class="center-text">
                                                ${config.totalConnections}
                                            </td>
                                            <td class="center-text"><fmt:formatDate value="${config.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                            <td class="actions-col">
                                                <a href="<c:url value='/administrator/configurations/details?i=${config.id}' />" class="btn btn-link" title="Edit this configuration">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Edit	
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="5" class="center-text">There are currently no configurations set up for this organization.</td></tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>		
</div>	

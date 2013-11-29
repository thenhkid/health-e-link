<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="main clearfix full-width" role="main">
    <div class="col-md-12">

        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose>
                    <c:when test="${savedStatus == 'updated'}">The message type has been successfully updated!</c:when>
                    <c:when test="${savedStatus == 'created'}">The message type has been successfully added!</c:when>
                    <c:when test="${savedStatus == 'deleted'}">The message type has been successfully removed!</c:when>
                </c:choose>
            </div>
        </c:if>

        <section class="panel panel-default">
            <div class="panel-body">
                <div class="table-actions">
                    <form:form class="form form-inline" action="/administrator/library/list" method="post">
                        <div class="form-group">
                            <label class="sr-only" for="searchTerm">Search</label>
                            <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" placeholder="Search"/>
                        </div>
                        <button id="searchMessageTypeBtn" class="btn btn-primary btn-sm" title="Search Message Types">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </form:form>
                </div>

                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col">Message Type Name</th>
                                <th scope="col" class="center-text">Date Created</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty messageTypesList}">
                                    <c:forEach var="messageType" items="${messageTypesList}">
                                        <tr id="messageTypeRow" style="cursor: pointer">
                                            <td scope="row">
                                                <a href="details?i=${messageType.id}" title="Edit this message type">${messageType.name}</a>
                                                <br />(<c:choose><c:when test="${messageType.status == 1}">active</c:when><c:when test="${messageType.status == 0}">inactive</c:when><c:otherwise>archived</c:otherwise></c:choose>)
                                            </td>
                                            <td class="center-text"><fmt:formatDate value="${messageType.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                            <td class="actions-col">
                                                <a href="details?i=${messageType.id}" class="btn btn-link" title="Edit this message type">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Edit	
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="4" class="center-text">There are currently no message types set up.</td></tr>
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
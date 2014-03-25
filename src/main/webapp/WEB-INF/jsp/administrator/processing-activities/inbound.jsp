<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="main clearfix full-width" role="main">
    <div class="col-md-12">
         <section class="panel panel-default">
            <div class="panel-body">
                <div class="table-actions">
                    <div role="search">
                        <form:form class="form form-inline" action="/administrator/processing-activity/inbound" method="post">
                            <div class="form-group">
                                <label class="sr-only" for="searchTerm">Search</label>
                                <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-batches" placeholder="Search"/>
                            </div>
                            <button id="searchOrgBtn" class="btn btn-primary btn-sm" title="Search Inbound Batches" role="button">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </form:form>
                    </div>
                </div>

                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col">Organization Name ${result}</th>
                                <th scope="col">Contact Information</th>
                                <th scope="col" class="center-text"># of Users</th>
                                <th scope="col" class="center-text"># of Configurations</th>
                                <th scope="col" class="center-text">Date Created</th>
                                <th scope="col" class="center-text">Access Level</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty batches}">
                                    <c:forEach var="batch" items="${batches}">
                                        <tr  style="cursor: pointer">
                                            <td scope="row">
                                               
                                            </td>
                                            <td class="center-text">
                                                
                                            </td>
                                            <td class="center-text">
                                               
                                            </td>
                                            <td class="center-text"><fmt:formatDate value="" type="date" pattern="M/dd/yyyy" /></td>
                                            <td class="center-text">
                                               
                                            </td>
                                            <td class="actions-col">
                                                <a href="javascript:void(0);" class="btn btn-link" title="View Batch Transactions" role="button">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    View Transactions
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="7" class="center-text">There are currently no outbound batches to review.</td></tr>
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
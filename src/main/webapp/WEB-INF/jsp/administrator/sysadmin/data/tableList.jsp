<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<div class="main clearfix" role="main" rel="lookUpTablesList">
    <div class="col-md-12">
        <section class="panel panel-default">
            <div class="panel-body">
                <div class="table-actions">
                    <form:form class="form form-inline" method="post">

                        <div class="form-group">
                            <label class="sr-only" for="searchTerm">Search</label>
                            <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" placeholder="Search"/>
                        </div>
                        <button id="searchtableTypeBtn" class="btn btn-primary btn-sm" title="Search for a look up table">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </form:form>
                </div>

                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col">Table Name</th>
                                <th scope="col" class="center-text">Number of Columns</th>
                                <th scope="col" class="center-text">Number of Rows</th>
                                <th scope="col">Description</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty tableList}">
                                    <c:forEach var="tableInfo" items="${tableList}">
                                        <tr id="tableInfoRow" style="cursor: pointer">
                                            <td scope="row">
                                                <a href="data/std/${tableInfo.urlId}" title="View Data">${tableInfo.displayName}</a>
                                            </td>
                                            <td class="center-text">${tableInfo.columnNum}</td>
                                            <td class="center-text">${tableInfo.rowNum}</td>
                                            <td class="center-text">${tableInfo.description}</td>
                                            <%-- not sure if we should let users edit our look up tables--%> 
                                            <td class="actions-col">
                                                <a href="data/std/${tableInfo.urlId}" class="btn btn-link" title="Edit data for this table">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    View Data	
                                                </a>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="4" class="center-text">There are currently no look up tables set up.</td></tr>
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

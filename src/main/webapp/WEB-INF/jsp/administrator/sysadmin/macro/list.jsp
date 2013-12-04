<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="main clearfix" role="main" rel="dataForTable">
    <div class="col-md-12">
			<div class="alert alert-success">
               <c:choose><c:when test="${param.msg == 'updated'}">The macro has been successfully updated!</c:when><c:when test="${param.msg == 'created'}">The macro has been successfully added!</c:when></c:choose>
            </div>
        <section class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Macros</h3>
                </div>
                <div class="panel-body">
                    <div class="table-actions">
                        <div class="form form-inline pull-left">
                        <form:form class="form form-inline" action="" method="post">
                            <div class="form-group">
                                <label class="sr-only" for="searchTerm">Search</label>
                                <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-marcos" placeholder="Search"/>
                            </div>
                            <button id="searchmarcoBtn" class="btn btn-primary btn-sm">
                                <span class="glyphicon glyphicon-search"></span>
                            </button>
                        </form:form>
                    </div>
                     <a href="#macroModal" id="createNewMacro" data-toggle="modal" class="btn btn-primary btn-sm pull-right" title="Create new macro">  
                        <span class="glyphicon glyphicon-plus"></span>
                    </a>
                </div>

                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                           		<th scope="col">Category</th>
                                <th scope="col">Macro Short Name</th>
                                <th scope="col">Macro Name</th>
                                <th scope="col">Formula</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty macroList}">
                                    <c:forEach var="macro" items="${macroList}">
                                        <tr id="dataRow">
                                            <td>${macro.category}</td>
                                            <td scope="row"><a href="#macroModal" data-toggle="modal" rel="macroDetail?i=${macro.id}" class="macroEdit" title="Edit this macro">${macro.macroShortName}</a>
                                            </td>
                                            <td>
                                             <c:choose>
                                             <c:when test="${fn:length(macro.macroName) > 45}">
                                             	${fn:substring(macro.macroName,0,44)}...
                                             </c:when>
                                             <c:otherwise>
                                                ${macro.macroName}
                                             </c:otherwise>
                                             </c:choose>
                                            </td>
                                            <td>
                                             <c:choose>
                                             <c:when test="${fn:length(macro.formula) > 45}">
                                             	${fn:substring(macro.formula,0,44)}...
                                             </c:when>
                                             <c:otherwise>
                                                ${macro.formula}
                                             </c:otherwise>
                                             </c:choose>
                                            </td>
                                            <td class="actions-col">
                                                <a href="#macroModal" data-toggle="modal" rel="macro/macroData?i=${macro.id}" class="macroEdit" title="Edit this macro">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Edit	
                                                </a>
                                                <a href="javascript:void(0);" rel="${macro.id}" class="btn btn-link marcoDelete" title="Delete this row">
                                                    <span class="glyphicon glyphicon-remove"></span>
                                                    Delete
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="5" class="center-text">There where no macros in the system.</td></tr>
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
<p rel="${currentPage}" id="currentPageHolder" style="display:none"></p>
<!-- Providers modal -->
<div class="modal fade" id="macroModal" role="dialog" tabindex="-1" aria-labeledby="Add/ Edit Macros" aria-hidden="true" aria-describedby="Add/Edit Macros"></div>



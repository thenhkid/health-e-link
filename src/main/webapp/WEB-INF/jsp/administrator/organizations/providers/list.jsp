<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>


<div class="main clearfix" role="main" rel="listoforganizationproviders">
    <div class="col-md-12">

        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose>
                    <c:when test="${savedStatus == 'updated'}">The provider has been successfully updated!</c:when>
                    <c:when test="${savedStatus == 'created'}">The provider has been successfully added!</c:when>
                    <c:when test="${savedStatus == 'deleted'}">The provider has been successfully removed!</c:when>
                </c:choose>
            </div>
        </c:if>
        <c:if test="${not empty param.msg}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose>
                    <c:when test="${param.msg == 'created'}">The provider has been successfully added!</c:when>
                </c:choose>
            </div>
        </c:if>
        <section class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Providers</h3>
            </div>
            <div class="panel-body">
                <div class="table-actions">
                    <div class="form form-inline pull-left">
                        <div role="search">
                            <form:form class="form form-inline" action="providers" method="post">
                                <div class="form-group">
                                    <label class="sr-only" for="searchTerm">Search</label>
                                    <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" id="search-providers" placeholder="Search"/>
                                </div>
                                <button id="searchProviderBtn" class="btn btn-primary btn-sm">
                                    <span class="glyphicon glyphicon-search"></span>
                                </button>
                            </form:form>
                        </div>
                    </div>
                    <a href="#providersModal" id="createNewProvider" data-toggle="modal" class="btn btn-primary btn-sm pull-right" title="Create a new provider">
                        <span class="glyphicon glyphicon-plus"></span>
                    </a>
                </div>

                <div class="form-container scrollable">
                    <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col">Name</th>
                                <th scope="col">Contact Info</th>
                                <th scope="col" class="center-text">Date Created</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty providerList}">
                                    <c:forEach var="provider" items="${providerList}">
                                        <tr id="userRow">
                                            <td scope="row"><a href="provider.${provider.firstName}${provider.lastName}?i=${provider.id}"  title="Edit this provider">${provider.firstName}&nbsp;${provider.lastName}</a><br />(<c:choose><c:when test="${provider.status == true}">active</c:when><c:otherwise>inactive</c:otherwise></c:choose>)</td>
                                                    <td>
                                                <c:if test="${not empty provider.email}">${provider.email}<br /></c:if>
                                                (p) ${provider.phone1}
                                                <c:if test="${not empty provider.phone2}"><br />(c) ${provider.phone2}</c:if>
                                                <c:if test="${not empty provider.fax}"><br />(f) ${provider.fax}</c:if>
                                                </td>
                                                <td class="center-text"><fmt:formatDate value="${provider.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                            <td class="actions-col">
                                                <a href="provider.${provider.firstName}${provider.lastName}?i=${provider.id}"  class="btn btn-link" title="Edit this provider">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Edit	
                                                </a>
                                                <a href="javascript:void(0);" rel="${provider.id}" class="btn btn-link providerDelete" title="Delete this provider">
                                                    <span class="glyphicon glyphicon-remove"></span>
                                                    Delete
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="5" class="center-text">There where no providers found</td></tr>
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
<div class="modal fade" id="providersModal" role="dialog" tabindex="-1" aria-labeledby="Add Organization Provider" aria-hidden="true" aria-describedby="Add new organization provider"></div>


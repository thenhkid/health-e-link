<%-- 
    Document   : providers
    Created on : Mar 7, 2014, 12:51:03 PM
    Author     : chadmccue
--%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li class="active">
                   Organization Brochures
                </li>
            </ol>
                
            <c:if test="${not empty param.msg}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    <c:choose>
                        <c:when test="${param.msg == 'updated'}">The brochure has been successfully updated!</c:when>
                        <c:when test="${param.msg == 'created'}">The brochure has been successfully added!</c:when>
                        <c:when test="${param.msg == 'deleted'}">The brochure has been successfully removed!</c:when>
                    </c:choose>
                </div>
            </c:if>
            
            <h2 class="form-title">Organization Brochures</h2>
            
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <thead>
                        <tr>
                            <th scope="col">Brochure Title</th>
                            <th>File Name</th>
                            <th class="center-text"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty brochures}">
                                <c:forEach var="brochure" items="${brochures}">
                                    <tr>
                                        <td scope="row">${brochure.title}</td>
                                        <td>
                                            ${brochure.fileName}
                                        </td>
                                        <td class="actions-col">
                                            <div class="pull-right">
                                                <a href="<c:url value="/FileDownload/downloadFile.do?filename=${brochure.fileName}&foldername=brochures&orgId=${brochure.orgId}"/>"  class="media-modal" title="Download this brochure">
                                                    <span class="glyphicon glyphicon-open"></span>
                                                    View	
                                                </a>
                                                 <a href="javascript:void(0);" rel="${brochure.id}" class="btn btn-link brochureDelete" title="Delete this brochure">
                                                    <span class="glyphicon glyphicon-remove"></span>
                                                    Delete
                                                </a>
                                           </div>
                                      </td>
                                    </tr>
                                </c:forEach>
                           </c:when>
                           <c:otherwise>
                                <tr><td colspan="3" class="center-text">Your organization has no uploaded brochures at this time.</td></tr>
                            </c:otherwise>
                      </c:choose>                  
                    </tbody>
                </table>
            </div>
            
        </div>
    </div>
</div>
<%-- Brochure Form modal --%>
<div class="modal fade" id="systemBrochureModal" role="dialog" tabindex="-1" aria-labeledby="Brochures" aria-hidden="true" aria-describedby="Brochures"></div>

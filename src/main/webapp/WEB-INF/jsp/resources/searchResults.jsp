<%-- 
    Document   : searchResults
    Created on : Jun 13, 2016, 2:03:19 PM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/resources/search'/>">Resources</a></li>
                <li><a href="<c:url value='/resources/search'/>">Search</a></li>
                <li class="active">Search Results</li>
            </ol>
              
            <div class="form-container scrollable">
               
                <table class="table table-hover table-default" <c:if test="${not empty resources}">id="dataTable"</c:if>>
                    <thead>
                        <tr>
                            <th scope="col" class="col-md-4">Resource</th>
                            <th scope="col" class="col-md-4">Contact Information</th>
                            <th scope="col" class="col-md-3"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty resources}">
                                <c:forEach var="resource" items="${resources}">
                                    <tr>
                                        <td scope="row">${resource.orgName}</td>
                                        <td>
                                            ${resource.address}  ${resource.address2}
                                            <br /> ${resource.city} ${resource.state}, ${resource.postalCode}
                                            <br /> p: ${resource.phone} 
                                            <c:if test="${not empty resource.fax}">
                                                 <br /> f: ${resource.fax} 
                                            </c:if>
                                        </td>
                                        <td class="center-text">
                                            <input type="button" id="activationRequest" class="btn btn-sm btn-info" value="Request to Activate Relationship"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                           </c:when>
                           <c:otherwise>
                                <tr><td colspan="3" class="center-text">There were no resources that matched your search criteria</td></tr>
                            </c:otherwise>
                      </c:choose>                  
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

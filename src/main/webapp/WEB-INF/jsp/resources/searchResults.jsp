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
        </div>
    </div>
    <div class="row" style="padding-bottom:10px;">
        <div class="col-md-12 page-content">
            <div class="col-md-12">
                <a href="javascript:void(0);" class="btn btn-primary btn-action-sm print pull-right" style="margin-right:10px;">Print</a>
            </div>
        </div> 
    </div>
    <div class="row">         
            <div class="form-container scrollable">
               
                <table class="table table-hover table-default" <c:if test="${not empty resources}">id="dataTable"</c:if>>
                    <thead>
                        <tr>
                            <th scope="col" class="col-md-3">Resource</th>
                            <th scope="col" class="col-md-3">Contact Information</th>
                            <th scope="col" class="col-md-3 center-text">Programs/Services Offered</th>
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
                                            <c:if test="${not empty resource.infoURL}">
                                                <br /> <a href="${resource.infoURL}" target="_blank">View Brochure</a>
                                            </c:if>     
                                        </td>
                                        <td>
                                            <c:if test="${not empty resource.programsOffered}">
                                                <ul>
                                                    <c:forEach items="${resource.programsOffered}" var="program">
                                                        <li>${program}</li>
                                                    </c:forEach>
                                                </ul>    
                                            </c:if>
                                        </td>
                                        <td class="center-text">
                                            <a href="#activationModal" id="activationRequest" data-toggle="modal" rel="${resource.id}">
                                            <input type="button" class="btn btn-sm btn-info" value="Request to Activate Relationship"/>
                                            </a>
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
<div class="modal fade" id="activationModal" role="dialog" tabindex="-1" aria-labeledby="Activation Request" aria-hidden="true" aria-describedby="Activation Request"></div>


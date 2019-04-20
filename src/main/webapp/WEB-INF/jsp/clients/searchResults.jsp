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
                <li><a href="<c:url value='/clients/search'/>">Clients</a></li>
                <li><a href="<c:url value='/clients/search'/>">Search</a></li>
                <li class="active">Search Results</li>
            </ol>
              
            <div class="form-container scrollable">
               
                <table class="table table-hover table-default" <c:if test="${not empty clients}">id="dataTable"</c:if>>
                    <thead>
                        <tr>
                            <th scope="col" class="col-md-4">Client</th>
                            <th scope="col" class="center-text">DOB</th>
                            <th scope="col" class="center-text">Postal Code</th>
                            <th scope="col" class="col-md-3"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty clients}">
                                <c:forEach var="client" items="${clients}">
                                    <tr>
                                        <td scope="row">${client.firstName} ${client.lastName}</td>
                                        <td class="center-text"><fmt:formatDate value="${client.DOB}" type="date" pattern="MM/dd/yyyy" /></td>
                                        <td class="center-text">${client.postalCode}</td>
                                        <td class="center-text">
                                            <select class="form-control input-small clientToDo">
                                                <option value="">- Select -</option>
                                                <option value="${client.id}-editClient">Edit Client</option>
                                                <option value="${client.id}-viewAssessments">View Assessments</option>
                                                <option value="${client.id}-viewReferrals">View Referrals</option>
                                                <option value="${client.id}-createReferrals">Create Referral</option>
                                                <option value="${client.id}-chronicDiseaseAssessment">Chronic Disease Risk Assessment</option>
                                                <option value="${client.id}-socialSupportAssessment">Social Support Needs Assessment</option> 
                                            </select>    
                                        </td>
                                    </tr>
                                </c:forEach>
                           </c:when>
                           <c:otherwise>
                                <tr><td colspan="4" class="center-text">There were no clients that matched your search criteria</td></tr>
                            </c:otherwise>
                      </c:choose>                  
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

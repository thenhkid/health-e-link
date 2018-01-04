<%-- 
    Document   : resources
    Created on : Jun 14, 2016, 10:24:27 PM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form id="organizationResources"  method="post" role="form">
       <input type="hidden" id="programIds" name="programIds" />
  </form:form>
<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li class="active">
                   Organization Resources
                </li>
            </ol>
            
           <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    <c:choose>
                        <c:when test="${savedStatus == 'save'}">The selected programs has been successfully associated to your organization!</c:when>
                    </c:choose>
                </div>
            </c:if>     
            
            <h2 class="form-title">Organization Resources</h2>
            
            <div class="col-md-12">
                 <table class="table table-striped table-hover table-default" >
                    <thead>
                        <tr>
                            <th scope="col">Resource</th>
                            <th scope="col" class="center-text">Associated</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty availPrograms}">
                                <c:forEach var="program" items="${availPrograms}">
                                    <c:if test="${!fn:contains(program.name, 'Feedback Report')}">
                                    <tr>
                                        <td>
                                            ${program.dspName}
                                        </td>
                                         <td scope="row" class="center-text">
                                            <input type="checkbox" name="programId" value="${program.id}" ${program.useProgram == true ? 'checked="true"' : ''} />
                                        </td>
                                    </tr>
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="7" class="center-text">There are currently no programs set up to associate this organization with.</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
            <c:if test="${not empty availPrograms}">
            <input type="button" id="saveResources" class="btn btn-primary" value="Save Resources"/>
            </c:if>
        </div>
    </div>
</div>

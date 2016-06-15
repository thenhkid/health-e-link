<%-- 
    Document   : deetails
    Created on : Jun 14, 2016, 9:01:59 PM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="main clearfix" role="main">
    <div class="row-fluid">
        <div class="col-md-12">
            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    <c:choose>
                        <c:when test="${savedStatus == 'updatedprograms'}">The organization programs have been successfully updated.</c:when>
                     </c:choose>
                </div>
            </c:if>
      </div>
  </div>
  <form:form id="organizationResources"  method="post" role="form">
       <input type="hidden" id="action" name="action" value="save" />
       <input type="hidden" id="programIds" name="programIds" />
  </form:form>
  <div class="row-fluid">
      <div class="col-md-12">
          <section class="panel panel-default">
              <div class="panel-body">
                <div class="form-container scrollable">
                    <p>
                        Below is a list of available programs that can be associated to this organization. Mark off the programs in which <strong>${organization.orgName}</strong> offers.
                    </p>
                   <table class="table table-striped table-hover table-default">
                        <thead>
                            <tr>
                                <th scope="col" class="center-text" style="width:10%">Use</th>
                                <th scope="col">Program Name</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty availPrograms}">
                                    <c:forEach var="program" items="${availPrograms}">
                                        <tr>
                                            <td scope="row" class="center-text">
                                                <input type="checkbox" name="programId" value="${program.id}" ${program.useProgram == true ? 'checked="true"' : ''} />
                                            </td>
                                            <td>
                                                ${program.dspName}
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="7" class="center-text">There are currently no programs set up to associate this organization with.</td></tr>
                                </c:otherwise>
                            </c:choose>
                       </tbody>
                 </table>
              </div>
          </div>
       </section>
    </div>
 </div>
</div>

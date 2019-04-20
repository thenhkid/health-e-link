<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<div class="main clearfix full-width" role="main">
    <div class="col-md-12">
        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success" role="alert">
                <strong>Success!</strong> 
                <c:choose>
                    <c:when test="${savedStatus == 'updated'}">The organization has been successfully updated!</c:when>
                    <c:when test="${savedStatus == 'created'}">The organization has been successfully added!</c:when>
                    <c:when test="${savedStatus == 'deleted'}">The organization has been successfully removed!</c:when>
                </c:choose>
            </div>
        </c:if>

        <section class="panel panel-default">
            <div class="panel-body">
                
                <div class="form-container scrollable"><br />
                    <table class="table table-striped table-hover table-default" <c:if test="${not empty organizationList}">id="dataTable"</c:if>>
                        <thead>
                            <tr>
                                <th scope="col">Organization Name ${result}</th>
                                <th scope="col">Parent Organization Name</th>
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
                                <c:when test="${not empty organizationList}">
                                    <c:forEach var="org" items="${organizationList}">
                                        <tr id="orgRow" rel="${org.cleanURL}" style="cursor: pointer">
                                            <td scope="row">
                                                <a href="javascript:void(0);" title="Edit this organization">${org.orgName}</a>
                                                <br />(<c:choose><c:when test="${org.status == true}">active</c:when><c:otherwise>inactive</c:otherwise></c:choose>)
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${org.parentId > 0}">
                                                        ${orgFunctions.getOrganizationById(org.parentId).getOrgName()}
                                                    </c:when>
                                                    <c:otherwise>N/A</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                ${org.address} <c:if test="${not empty org.address2}"><br />${org.address2}</c:if>
                                                <br />${org.city}&nbsp;${org.state},&nbsp;${org.postalCode}
                                            </td>
                                            <td class="center-text">
                                                ${orgFunctions.findTotalUsers(org.id)}
                                            </td>
                                            <td class="center-text">
                                                ${orgFunctions.findTotalConfigurations(org.id)}
                                            </td>
                                            <td class="center-text"><fmt:formatDate value="${org.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                            <td class="center-text">
                                                <c:choose>
                                                    <c:when test="${org.publicOrg == true}">
                                                        Public
                                                    </c:when>
                                                    <c:otherwise>
                                                        Private
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="actions-col">
                                                <a href="javascript:void(0);" class="btn btn-link" title="Edit this organization" role="button">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Edit
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="7" class="center-text">There are currently no organizations set up.</td></tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>
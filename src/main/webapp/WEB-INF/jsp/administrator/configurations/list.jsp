<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="main clearfix full-width" role="main">
    <div class="col-md-12">

        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose>
                    <c:when test="${savedStatus == 'updated'}">The configuration has been successfully updated!</c:when>
                    <c:when test="${savedStatus == 'created'}">The configuration has been successfully added!</c:when>
                    <c:when test="${savedStatus == 'deleted'}">The configuration has been successfully removed!</c:when>
                </c:choose>
            </div>
        </c:if>

        <section class="panel panel-default">
            <div class="panel-body">
                
                <div class="form-container scrollable"><br />
                    <table class="table table-striped table-hover table-default" <c:if test="${not empty configurationList}">id="dataTable"</c:if>>
                        <thead>
                            <tr>
                                <th scope="col">Organization</th>
                                <th scope="col">Configuration Name</th>
                                <th scope="col">Configuration Type</th>
                                <th scope="col">Message Type</th>
                                <th scope="col" class="center-text">Transport Method</th>
                                <th scope="col" class="center-text">Date Created</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty configurationList}">
                                    <c:forEach var="config" items="${configurationList}">
                                        <tr id="configRow" rel="${config.id}" style="cursor: pointer">
                                            <td scope="row">
                                                <a href="javascript:void(0);" title="Edit this configuration">${config.orgName}</a>
                                                <br />
                                                (<c:choose><c:when test="${config.status == true}">active</c:when><c:otherwise>inactive</c:otherwise></c:choose>)
                                            </td>
                                            <td>
                                                ${config.configName}
                                            </td>
                                            <td>
                                                <c:choose><c:when test="${config.type == 1}">Source Configuration</c:when><c:otherwise>Target Configuration</c:otherwise></c:choose>
                                                <br />
                                                (
                                                <c:choose><c:when test="${config.sourceType == 1}">Originating Message</c:when><c:otherwise>Feedback Report</c:otherwise></c:choose>
                                                )
                                            </td>
                                            <td>
                                                ${config.messageTypeName}
                                            </td>
                                            <td class="center-text">
                                                <c:choose><c:when test="${config.transportMethod == 'File Upload'}"><c:choose><c:when test="${config.type == 1}">File Upload</c:when><c:otherwise>File Download</c:otherwise></c:choose></c:when><c:otherwise>${config.transportMethod}</c:otherwise></c:choose>
                                            </td>
                                            <td class="center-text"><fmt:formatDate value="${config.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                            <td class="actions-col">
                                                <a href="javascript:void(0);" class="btn btn-link" title="Edit this configuration">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Edit	
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr><td colspan="7" class="center-text">There are currently no configurations set up.</td></tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>		
</div>
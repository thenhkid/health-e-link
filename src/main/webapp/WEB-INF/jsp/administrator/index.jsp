<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!-- Actions Nav -->
<nav class="navbar navbar-default actions-nav">
    <div class="contain">
        <div class="navbar-header">
            <h1 class="section-title navbar-brand"><a href="" title="Section Title" class="unstyled-link">Dashboard</a></h1>
        </div>
    </div>
</nav>

<!-- End Actions Nav -->
<div class="main clearfix full-width" role="main">

    <div class="row-fluid contain">
        <div class="col-md-12">
            <section class="panel panel-default panel-intro">
                <div class="panel-body" >
                    <h2>Welcome <c:out value="${pageContext.request.userPrincipal.name}" />!</h2>
                </div>
            </section>
        </div>
    </div>

    <div class="row-fluid contain basic-clearfix">
        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Number of Organizations">
                <div class="panel-body">
                    <span class="stat-number"><a href="administrator/organizations/list" title="Total number of organizations">${totalOrgs}</a></span>
                    <h3>Organizations</h3>
                    <a href="administrator/organizations/list" title="Organization Manager" class="btn btn-primary btn-small">View all</a>
                </div>
            </section>
        </div>

        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Number of Configurations">
                <div class="panel-body">
                    <span class="stat-number"><a href="administrator/configurations/list" title="Total number of configurations">${totalConfigs}</a></span>
                    <h3>Configurations</h3>
                    <a href="administrator/configurations/list" title="Configuration Manager" class="btn btn-primary btn-small">View all</a>
                </div>
            </section>
        </div>

        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Number of Message Types">
                <div class="panel-body">
                    <span class="stat-number"><a href="administrator/library/list" title="Total number of message types">${totalMessageTypes}</a></span>
                    <h3>Message Types</h3>
                    <a href="administrator/library/list" title="Message Type Library Manager" class="btn btn-primary btn-small">View all</a>
                </div>
            </section>
        </div>

        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Number of Files to Process">
                <div class="panel-body">
                    <span class="stat-number"><a href="" title="" title="Total number of files to process">0</a></span>
                    <h3>Processing Activity</h3>
                    <a href="" title="" class="btn btn-primary btn-small" role="button">View all</a>
                </div>
            </section>
        </div>
    </div>

    <div class="row-fluid contain basic-clearfix">
        
        <div class="col-md-6">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Organizations</h3>
                </div>
                <div class="panel-body" >
                    <h4>Latest Organizations:</h4>
                    <c:choose>
                        <c:when test="${not empty latestOrgs}">
                        <table class="table table-striped table-hover table-default">
                            <tbody>
                                <c:forEach var="org" items="${latestOrgs}">
                                    <tr>
                                        <td>
                                            <a href="administrator/organizations/${org.cleanURL}" title="Edit this Organization"><strong>${org.orgName}</strong></a>
                                            <address>
                                                ${org.address} <c:if test="${not empty org.address2}"><br />${org.address2}</c:if>
                                                <br />${org.city} ${org.state}, ${org.postalCode}
                                            </address>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${org.dateCreated}" type="date" pattern="M/dd/yyyy" />
                                        </td>
                                        <td>
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
                                            <a href="administrator/organizations/${org.cleanURL}" class="btn btn-link" title="Edit this Organization">
                                                <span class="glyphicon glyphicon-edit" role="button"></span>
                                                Edit
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        </c:when>
                        <c:otherwise><tr><td colspan="3" class="center-text">There are currently no organizations set up.</td></tr></c:otherwise>
                    </c:choose>
                </div>
            </section>
        </div>
        
        <div class="col-md-6">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Configurations</h3>
                </div>
                <div class="panel-body" >
                    <h4>Latest Configurations:</h4>
                    <c:choose>
                        <c:when test="${not empty latestConfigs}"> 
                            <table class="table table-striped table-hover table-default">
                                <tbody>
                                    <c:forEach var="config" items="${latestConfigs}">
                                        <tr>
                                            <td>
                                                <a href="administrator/configurations/details?i=${config.id}" title="Edit this Configuration"><strong>${config.orgName}</strong></a>
                                                <br/> <strong>Configuration Type:</strong> <c:choose><c:when test="${config.type == 1}">Source Configuration</c:when><c:otherwise>Target Configuration</c:otherwise></c:choose>
                                                <br/> <strong>Message Type:</strong>  ${config.messageTypeName}
                                                <br/> <strong>Transport Method:</strong> ${config.transportMethod}
                                                <br/> <strong>Status:</strong>  <c:choose><c:when test="${config.status == true}">active</c:when><c:otherwise>inactive</c:otherwise></c:choose>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${config.dateCreated}" type="date" pattern="M/dd/yyyy" />
                                            </td>
                                            <td class="actions-col">
                                                <a href="administrator/configurations/details?i=${config.id}" class="btn btn-link" title="Edit this Configuration" role="button">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                    Edit
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise><tr><td colspan="3" class="center-text">There are currently no configurations set up.</td></tr></c:otherwise>
                    </c:choose>
                </div>
            </section>
        </div>

        
    </div>

    <div class="row-fluid contain">
        <div class="col-md-6">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Message Types</h3>
                </div>
                <div class="panel-body" >
                    <h4>Latest Message Types:</h4>
                    <c:choose>
                        <c:when test="${not empty latestMessageTypes}">
                        <table class="table table-striped table-hover table-default">
                            <tbody>
                                <c:forEach var="messageType" items="${latestMessageTypes}">
                                    <tr>
                                        <td>
                                            <a href="administrator/library/details?i=${messageType.id}" title="Edit this Organization"><strong>${messageType.name}</strong></a>
                                            <br /><strong>Status:</strong> <c:choose><c:when test="${messageType.status == 1}">active</c:when><c:when test="${messageType.status == 0}">inactive</c:when><c:otherwise>archived</c:otherwise></c:choose> 
                                                </td>
                                                <td>
                                            <fmt:formatDate value="${messageType.dateCreated}" type="date" pattern="M/dd/yyyy" />
                                        </td>
                                        <td class="actions-col">
                                            <a href="administrator/library/details?i=${messageType.id}" class="btn btn-link" title="Edit this Organization">
                                                <span class="glyphicon glyphicon-edit" role="button"></span>
                                                Edit
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        </c:when>
                        <c:otherwise><tr><td colspan="3" class="center-text">There are currently no message types set up.</td></tr></c:otherwise>
                    </c:choose>
                </div>
            </section>
        </div>

        <div class="col-md-6">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Scheduler</h3>
                </div>
                <div class="panel-body" >
                    <h4>Latest Schedules:</h4>
                </div>
            </section>
        </div>
    </div>
</div>










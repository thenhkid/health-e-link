<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="main clearfix" role="main">
     <div class="row-fluid">
        <div class="col-md-12">

            <div class="alert alert-success" style="display:none;">
                <strong>Success!</strong> 
                <div id="saveStatus"></div>
            </div>
            <section class="panel panel-default">
                <div class="panel-body">
                    <dt>
                        <dt>Configuration Summary:</dt>
                        <dd><strong>Organization:</strong> ${configurationDetails.orgName}</dd>
                        <dd><strong>Message Type:</strong> ${configurationDetails.messageTypeName}</dd>
                    </dt>
                </div>
            </section>
        </div>    
     </div>

    <div class="row-fluid">
        <c:if test="${configurationDetails.type == 1}">
            <div class="col-md-4">
                <section class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">New Connection</h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-8">
                                <div id="organizationDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                    <label class="sr-only" for="organization">Organization *</label>
                                    <select id="organization" class="form-control">
                                        <c:choose>
                                            <c:when test="${organizations.size() == 0}">
                                                <option value="">No Available Organizations</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="">- Select -</option>
                                                <c:forEach items="${organizations}" var="orgs" varStatus="oStatus">
                                                    <c:if test="${not usedOrgs.contains(organizations[oStatus.index].id)}">
                                                        <option value="${organizations[oStatus.index].id}">${organizations[oStatus.index].orgName} </option>
                                                    </c:if>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                    <span id="organizationMsg" class="control-label"></span>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <button class="btn btn-primary addOrganization">Add</button>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </c:if>

        <div class="col-md-8">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Existing Connections</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container scrollable">
                        <div>
                            <table class="table table-striped table-hover responsive">
                                <thead>
                                    <tr>
                                        <th scope="col">Organization</th>
                                        <th scope="col" class="center-text">Date Created</th>
                                        <th scope="col" class="center-text"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${connections.size() > 0}">
                                            <c:forEach items="${connections}" var="connect" varStatus="cStatus">
                                                <tr>
                                                    <td scope="row">
                                                        ${connect.orgName}
                                                    </td>
                                                    <td class="center-text"><fmt:formatDate value="${connect.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                                    <td class="center-text">
                                                        <c:choose>
                                                            <c:when test="${connections[cStatus.index].status == true}">
                                                                <a href="javascript:void(0)" class="connectionStatus" rel2="${connect.id}" rel="1" title="Disable this connection!">Disable</a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <a href="javascript:void(0)" class="connectionStatus" rel2="${connect.id}" rel="0" title="Enable this connection!">Enable</a>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise><tr><td scope="row" colspan="3" style="text-align:center">No connections Found</td></c:otherwise>
                                        </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
</div>

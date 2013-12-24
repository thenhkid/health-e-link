<%-- 
    Document   : createConnection
    Created on : Dec 24, 2013, 11:22:00 AM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix full-width" role="main">
    <div class="row-fluid">
        <div class="col-md-12">		
            <section class="panel panel-default">
                <div class="panel-heading">
                   <h3 class="panel-title">Source Configuration</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container scrollable">
                        <div id="srcorgDiv" class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="organization">Organization *</label>
                            <select id="organization" class="form-control half selsrcOrganization">
                                <option value="">- Select -</option>
                                <c:forEach items="${organizations}" var="org" varStatus="oStatus">
                                    <option value="${organizations[oStatus.index].id}">${organizations[oStatus.index].orgName} </option>
                                </c:forEach>
                            </select>
                            <span id="srcOrgMsg" class="control-label"></span>
                       </div>
                       <div id="srcConfigDiv" class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="messageTypeId">Configuration *</label>
                            <select id="srcConfig" class="form-control half">
                                <option value="">- Select -</option>
                            </select>
                            <span id="srcConfigMsg" class="control-label"></span>
                        </div>         
                    </div>
                </div>
            </section>
           <section class="panel panel-default">
                <div class="panel-heading">
                   <h3 class="panel-title">Target Configuration</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container scrollable">
                        <div id="tgtorgDiv" class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="organization">Organization *</label>
                            <select id="organization" class="form-control half seltgtOrganization">
                                <option value="">- Select -</option>
                                <c:forEach items="${organizations}" var="org" varStatus="oStatus">
                                    <option value="${organizations[oStatus.index].id}">${organizations[oStatus.index].orgName} </option>
                                </c:forEach>
                            </select>
                            <span id="tgtOrgMsg" class="control-label"></span>
                       </div>
                       <div id="tgtConfigDiv" class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="messageTypeId">Configuration *</label>
                            <select id="tgtConfig" class="form-control half">
                                <option value="">- Select -</option>
                            </select>
                            <span id="tgtConfigMsg" class="control-label"></span>
                        </div>         
                    </div>
                </div>
            </section>                 
        </div>
    </div>
</div>                       

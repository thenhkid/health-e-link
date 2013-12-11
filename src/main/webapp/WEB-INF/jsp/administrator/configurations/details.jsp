<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">

    <div class="col-md-12">

        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose><c:when test="${savedStatus == 'updated'}">The configuration has been successfully updated!</c:when><c:otherwise>The configuration has been successfully created!</c:otherwise></c:choose>
                    </div>
        </c:if>
        
        <form:form id="configuration" commandName="configurationDetails" modelAttribute="configurationDetails" method="post" enctype="multipart/form-data" role="form">
            <input type="hidden" id="action" name="action" value="save" />
            <form:hidden path="id" id="id" />
            <form:hidden path="dateCreated" />
            <form:hidden path="stepsCompleted" />

            <section class="panel panel-default">

                <div class="panel-heading">
                    <h3 class="panel-title">Details</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container">
                        <div class="form-group">
                            <label for="status">Status *</label>
                            <div>
                                <label class="radio-inline">
                                  <form:radiobutton id="status" path="status" value="1" disabled="${completedSteps != 6 ? 'true' : 'false' }" />Active 
                                </label>
                                <label class="radio-inline">
                                    <form:radiobutton id="status" path="status" value="0"/>Inactive
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="type">Configuration Type * 
                            <span class="badge badge-help" data-placement="top" title="" data-original-title="Select For Source if this is for sending a message and For Target if this is for receiving a message">?</span></label>
                            <div>
                                <label class="radio-inline">
                                    <form:radiobutton id="type" path="type" value="1" disabled="${configurationDetails.id == 0 ? 'false' : 'true' }"/> For Source Organization 
                                </label>
                                <label class="radio-inline">
                                    <form:radiobutton id="type" path="type" value="2" disabled="${configurationDetails.id == 0 ? 'false' : 'true' }"/> For Target Organization
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="type">Clear fields after Delivery *</label>
                            <div>
                                <label class="radio-inline">
                                    <form:radiobutton id="clearFields" path="clearFields" value="1" /> Yes
                                </label>
                                <label class="radio-inline">
                                    <form:radiobutton id="clearFields" path="clearFields" value="0" /> No
                                </label>
                            </div>
                        </div>        
                        <spring:bind path="orgId">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="organization">Organization *</label>
                                <form:select path="orgId" id="organization" class="form-control half" disabled="${configurationDetails.id == 0 ? 'false' : 'true' }">
                                    <option value="">- Select -</option>
                                    <c:forEach items="${organizations}" var="org" varStatus="oStatus">
                                        <option value="${organizations[oStatus.index].id}" <c:if test="${configurationDetails.orgId == organizations[oStatus.index].id}">selected</c:if>>${organizations[oStatus.index].orgName} </option>
                                    </c:forEach>
                                </form:select>
                                <c:if test="${configurationDetails.id > 0}"><form:hidden path="orgId"/></c:if>     
                                </div>
                        </spring:bind>
                        <spring:bind path="messageTypeId">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="messageTypeId">Message Type *</label>
                                <form:select path="messageTypeId" id="messageTypeId" class="form-control half" disabled="${configurationDetails.id == 0 ? 'false' : 'true' }">
                                    <option value="">- Select -</option>
                                    <c:if test="${configurationDetails.id > 0}">
                                        <c:forEach items="${messageTypes}" var="messageType" varStatus="mStatus">
                                            <option value="${messageType.id}" <c:if test="${messageType.id == configurationDetails.messageTypeId}">selected</c:if>>${messageType.name}</option>
                                        </c:forEach>
                                    </c:if>
                                </form:select>
                                <c:if test="${configurationDetails.id > 0}"><form:hidden path="messageTypeId"/></c:if> 
                                </div>
                        </spring:bind>
                    </div>
                </div>
            </section>
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Error Handling</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container">
                        <div class="form-group">
                            <label for="status">If Error *</label>
                            <div>
                                <label class="radio-inline">
                                    <form:radiobutton id="rejectOnError" path="rejectOnError" value="1"/>Set Record to Pending Status
                                </label>
                                <label class="radio-inline">
                                  <form:radiobutton id="rejectOnError" path="rejectOnError" value="2" />Reject Record 
                                </label>
                                <label class="radio-inline">
                                    <form:radiobutton id="rejectOnError" path="rejectOnError" value="3"/>Reject all Records with same Message Type
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
            </section>                    
        </form:form>
                        
    </div>
</div>
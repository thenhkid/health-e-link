<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">
     <div class="row-fluid">
        <div class="col-md-12">

            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success">
                    <strong>Success!</strong> 
                    <c:choose><c:when test="${savedStatus == 'updated'}">The configuration message specs have been successfully updated!</c:when></c:choose>
                </div>
            </c:if>
            <section class="panel panel-default">
                <div class="panel-body">
                    <dt>
                        <dt>Configuration Summary:</dt>
                        <dd><strong>Organization:</strong> ${configurationDetails.orgName}</dd>
                        <dd><strong>Authorized User</strong> ${configurationDetails.userName}</dd>
                        <dd><strong>Message Type:</strong> ${configurationDetails.messageTypeName}</dd>
                    </dt>
                </div>
            </section>
        </div>
                    
        <div class="col-md-12">

            <form:form id="messageSpecs" commandName="messageSpecs" modelAttribute="messageSpecs" enctype="multipart/form-data" method="post" role="form">
                <input type="hidden" id="action" name="action" value="save" />
                <form:hidden path="id" id="id" />
                <form:hidden path="configId" />

                <section class="panel panel-default">

                    <div class="panel-heading">
                        <h3 class="panel-title">Message Specs</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-container">
                            <%-- Non ERG options only --%>
                            <c:if test="${transportType != 2}">
                                <c:if test="${not empty messageSpecs.templateFile}">
                                    <div class="form-group">
                                        <label class="control-label" for="currentFile">Current File</label>
                                        <input type="text" disabled class="form-control" value="${messageSpecs.templateFile}" />
                                    </div>
                                </c:if>
                                <spring:bind path="file">
                                    <div id="templateFileDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="templateFile"><c:if test="${not empty messageSpecs.templateFile}">New</c:if> File *</label>
                                        <form:input path="file" id="templateFile" type="file"  />
                                        <span id="templateFileMsg" class="control-label"></span>
                                    </div>
                                </spring:bind>
                            </c:if>
                            <%-- Source File Download options only --%>
                            <c:if test="${configurationDetails.type == 1}">
                                <%-- Non ERG options only --%>
                                <c:if test="${transportType != 2}">
                                    <spring:bind path="messagTypeCol">
                                        <div class="form-group ${status.error ? 'has-error' : '' }">
                                            <label class="control-label" for="messagTypeCol">Column containing the message type *</label>
                                            <form:input path="messagTypeCol" id="messagTypeCol" class="form-control" type="text" maxLength="3" />
                                            <form:errors path="messagTypeCol" cssClass="control-label" element="label" />
                                       </div>
                                    </spring:bind>
                                    <spring:bind path="messagTypeVal">
                                        <div class="form-group ${status.error ? 'has-error' : '' }">
                                            <label class="control-label" for="messagTypeVal">Message Type Value</label>
                                            <form:input path="messagTypeVal" id="messagTypeVal" class="form-control" type="text" maxLength="45" />
                                            <form:errors path="messagTypeVal" cssClass="control-label" element="label" />
                                       </div>
                                    </spring:bind>
                                    <spring:bind path="targetOrgCol">
                                        <div class="form-group ${status.error ? 'has-error' : '' }">
                                            <label class="control-label" for="targetOrgCol">Column containing the target organization *</label>
                                            <form:input path="targetOrgCol" id="targetOrgCol" class="form-control" type="text" maxLength="3" />
                                            <form:errors path="targetOrgCol" cssClass="control-label" element="label" />
                                       </div>
                                    </spring:bind>
                                    <spring:bind path="containsHeaderRow">
                                        <div class="form-group">
                                            <label class="control-label" for="containsHeaderRow">Will the submission have a header row? *</label>
                                            <div>
                                                <label class="radio-inline">
                                                  <form:radiobutton id="containsHeaderRow" path="containsHeaderRow" value="1" /> Yes 
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="containsHeaderRow" path="containsHeaderRow" value="0"/> No
                                                </label>
                                            </div>
                                        </div>
                                    </spring:bind>
                                </c:if>
                                <spring:bind path="rptField1">
                                    <div class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="rptField1">Reportable Field 1 *</label>
                                        <form:input path="rptField1" id="rptField1" class="form-control" type="text" maxLength="3" />
                                        <form:errors path="rptField1" cssClass="control-label" element="label" />
                                   </div>
                                </spring:bind>
                                <spring:bind path="rptField2">
                                    <div class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="rptField2">Reportable Field 2 *</label>
                                        <form:input path="rptField2" id="rptField2" class="form-control" type="text" maxLength="3" />
                                        <form:errors path="rptField2" cssClass="control-label" element="label" />
                                   </div>
                                </spring:bind>
                                <spring:bind path="rptField3">
                                    <div class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="rptField3">Reportable Field 3 *</label>
                                        <form:input path="rptField3" id="rptField3" class="form-control" type="text" maxLength="3" />
                                        <form:errors path="rptField3" cssClass="control-label" element="label" />
                                   </div>
                                </spring:bind>
                                <spring:bind path="rptField4">
                                    <div class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="rptField4">Reportable Field 4 *</label>
                                        <form:input path="rptField4" id="rptField4" class="form-control" type="text" maxLength="3" />
                                        <form:errors path="rptField4" cssClass="control-label" element="label" />
                                   </div>
                                </spring:bind>
                            </c:if>
                        </div>
                    </div>
                </section>   
            </form:form>
        </div>     
     </div>
</div>
                    
                    

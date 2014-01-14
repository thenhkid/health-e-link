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
                        <dd><strong>Configuration Name:</strong> ${configurationDetails.configName}</dd>
                        <dd><strong>Message Type:</strong> ${configurationDetails.messageTypeName}</dd>
                        <dd><strong>Transport Method:</strong> ${configurationDetails.transportMethod}</dd>
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
                                        <label class="control-label" for="templateFile">Current File</label>
                                        <form:hidden path="templateFile" />
                                        <br />${messageSpecs.templateFile}
                                    </div>
                                </c:if>
                                <spring:bind path="file">
                                    <div id="templateFileDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="file"><c:if test="${not empty messageSpecs.templateFile}">New</c:if> File *</label>
                                        <form:input path="file" id="file" type="file"  />
                                        <span id="templateFileMsg" class="control-label"></span>
                                    </div>
                                </spring:bind>
                            </c:if>
                            <%-- Source File Download options only --%>
                            <c:if test="${configurationDetails.type == 1}">
                                <%-- Non ERG options only --%>
                                <c:if test="${transportType != 2}">
                                    <spring:bind path="messageTypeCol">
                                        <div class="form-group ${status.error ? 'has-error' : '' }">
                                            <label class="control-label" for="messageTypeCol">Column containing the message type *</label>
                                            <form:input path="messageTypeCol" id="messageTypeCol" class="form-control" type="text" maxLength="3" />
                                            <form:errors path="messageTypeCol" cssClass="control-label" element="label" />
                                       </div>
                                    </spring:bind>
                                    <spring:bind path="messageTypeVal">
                                        <div class="form-group ${status.error ? 'has-error' : '' }">
                                            <label class="control-label" for="messageTypeVal">Message Type Value</label>
                                            <form:input path="messageTypeVal" id="messageTypeVal" class="form-control" type="text" maxLength="45" />
                                            <form:errors path="messageTypeVal" cssClass="control-label" element="label" />
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
                                    <div class="form-group rtpField ${status.error ? 'has-error' : '' }">
                                        <span id="rptFieldMsg" class="control-label"></span>
                                        <label class="control-label" for="rptField1">Reportable Field 1 *</label>
                                        <form:select path="rptField1" id="rptField1" class="form-control half">
                                            <option value="0">- Select -</option>
                                            <c:forEach items="${availableFields}" var="field" varStatus="fStatus">
                                                <c:if test="${currentBucket != availableFields[fStatus.index].bucketNo}">
                                                    <c:if test="${currentBucket > 0}"></optgroup></c:if>
                                                    <c:set var="currentBucket" value="${availableFields[fStatus.index].bucketNo}" />
                                                    <c:choose>
                                                        <c:when test="${currentBucket == 1}">
                                                        <optgroup label="(Sender Organiation Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 2}">
                                                        <optgroup label="(Sender Provider Information)">
                                                        </c:when>     
                                                        <c:when test="${currentBucket == 3}">
                                                        <optgroup label="(Recipient Organization Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 4}">
                                                        <optgroup label="(Recipient Provider Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 5}">
                                                        <optgroup label="(Patient Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 6}">
                                                        <optgroup label="(Details)">
                                                        </c:when>
                                                    </c:choose>
                                                </c:if>
                                                            <option value="${availableFields[fStatus.index].id}" <c:if test="${messageSpecs.rptField1 == availableFields[fStatus.index].id}">selected</c:if>>${availableFields[fStatus.index].fieldLabel} </option>
                                            </c:forEach>
                                        </form:select>
                                        <form:errors path="rptField1" cssClass="control-label" element="label" />
                                   </div>
                                </spring:bind>
                                <spring:bind path="rptField2">
                                    <div class="form-group rtpField ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="rptField2">Reportable Field 2 *</label>
                                        <form:select path="rptField2" id="rptField2" class="form-control half">
                                            <option value="0">- Select -</option>
                                            <c:forEach items="${availableFields}" var="field" varStatus="fStatus">
                                                <c:if test="${currentBucket != availableFields[fStatus.index].bucketNo}">
                                                    <c:if test="${currentBucket > 0}"></optgroup></c:if>
                                                    <c:set var="currentBucket" value="${availableFields[fStatus.index].bucketNo}" />
                                                    <c:choose>
                                                        <c:when test="${currentBucket == 1}">
                                                        <optgroup label="(Sender Organiation Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 2}">
                                                        <optgroup label="(Sender Provider Information)">
                                                        </c:when>     
                                                        <c:when test="${currentBucket == 3}">
                                                        <optgroup label="(Recipient Organization Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 4}">
                                                        <optgroup label="(Recipient Provider Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 5}">
                                                        <optgroup label="(Patient Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 6}">
                                                        <optgroup label="(Details)">
                                                        </c:when>
                                                    </c:choose>
                                                </c:if>
                                                <option value="${availableFields[fStatus.index].id}" <c:if test="${messageSpecs.rptField2 == availableFields[fStatus.index].id}">selected</c:if>>${availableFields[fStatus.index].fieldLabel} </option>
                                            </c:forEach>
                                        </form:select>
                                        <form:errors path="rptField2" cssClass="control-label" element="label" />
                                   </div>
                                </spring:bind>
                                <spring:bind path="rptField3">
                                    <div class="form-group rtpField ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="rptField3">Reportable Field 3 *</label>
                                        <form:select path="rptField3" id="rptField3" class="form-control half">
                                            <option value="0">- Select -</option>
                                            <c:forEach items="${availableFields}" var="field" varStatus="fStatus">
                                                <c:if test="${currentBucket != availableFields[fStatus.index].bucketNo}">
                                                    <c:if test="${currentBucket > 0}"></optgroup></c:if>
                                                    <c:set var="currentBucket" value="${availableFields[fStatus.index].bucketNo}" />
                                                    <c:choose>
                                                        <c:when test="${currentBucket == 1}">
                                                        <optgroup label="(Sender Organiation Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 2}">
                                                        <optgroup label="(Sender Provider Information)">
                                                        </c:when>     
                                                        <c:when test="${currentBucket == 3}">
                                                        <optgroup label="(Recipient Organization Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 4}">
                                                        <optgroup label="(Recipient Provider Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 5}">
                                                        <optgroup label="(Patient Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 6}">
                                                        <optgroup label="(Details)">
                                                        </c:when>
                                                    </c:choose>
                                                </c:if>
                                                <option value="${availableFields[fStatus.index].id}" <c:if test="${messageSpecs.rptField3 == availableFields[fStatus.index].id}">selected</c:if>>${availableFields[fStatus.index].fieldLabel} </option>
                                            </c:forEach>
                                        </form:select>
                                        <form:errors path="rptField3" cssClass="control-label" element="label" />
                                   </div>
                                </spring:bind>
                                <spring:bind path="rptField4">
                                    <div class="form-group rtpField ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="rptField4">Reportable Field 4 *</label>
                                        <form:select path="rptField4" id="rptField4" class="form-control half">
                                            <option value="0">- Select -</option>
                                            <c:forEach items="${availableFields}" var="field" varStatus="fStatus">
                                                <c:if test="${currentBucket != availableFields[fStatus.index].bucketNo}">
                                                    <c:if test="${currentBucket > 0}"></optgroup></c:if>
                                                    <c:set var="currentBucket" value="${availableFields[fStatus.index].bucketNo}" />
                                                    <c:choose>
                                                        <c:when test="${currentBucket == 1}">
                                                        <optgroup label="(Sender Organiation Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 2}">
                                                        <optgroup label="(Sender Provider Information)">
                                                        </c:when>     
                                                        <c:when test="${currentBucket == 3}">
                                                        <optgroup label="(Recipient Organization Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 4}">
                                                        <optgroup label="(Recipient Provider Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 5}">
                                                        <optgroup label="(Patient Information)">
                                                        </c:when>
                                                        <c:when test="${currentBucket == 6}">
                                                        <optgroup label="(Details)">
                                                        </c:when>
                                                    </c:choose>
                                                </c:if>
                                                <option value="${availableFields[fStatus.index].id}" <c:if test="${messageSpecs.rptField4 == availableFields[fStatus.index].id}">selected</c:if>>${availableFields[fStatus.index].fieldLabel} </option>
                                            </c:forEach>
                                        </form:select>
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
                    
                    

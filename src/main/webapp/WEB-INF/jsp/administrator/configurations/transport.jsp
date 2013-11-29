<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">

    <div class="col-md-12">

        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose><c:when test="${savedStatus == 'updated'}">The configuration transport details have been successfully updated!</c:when></c:choose>
                    </div>
        </c:if>

        <div class="row-fluid">
            <div class="col-md-4">
                <section class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Transport Methods</h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-8">
                                <div id="transportMethodDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                    <label class="sr-only" for="transportMethod">Transport Method *</label>
                                    <select id="transportMethod" class="form-control">
                                        <option value="">- Select -</option>
                                        <c:forEach items="${transportMethods}" var="transMethod" varStatus="tStatus">
                                            <c:if test="${not usedTransportMethods.contains(transportMethods[tStatus.index][0])}">
                                                <option value="${transportMethods[tStatus.index][0]}">${transportMethods[tStatus.index][1]} </option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                    <span id="transportMethodMsg" class="control-label"></span>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <button class="btn btn-primary addTransportMethod">Add</button>
                            </div>
                        </div>
                    </div>
                </section>
            </div>

            <div class="col-md-8">
                <div class="panel-group" id="accordion">
                    <form:form id="transportMethods" modelAttribute="configurationDetails" enctype="multipart/form-data" method="post" role="form">
                        <input type="hidden" id="action" name="action" value="save" />
                        <input type="hidden" id="configId" value="${configurationDetails.id}" />
                        <form:hidden path="stepsCompleted" />
                        <c:forEach items="${configurationDetails.transportDetails}" var="details" varStatus="tStatus">
                            <c:choose>
                                <c:when test="${details.transportMethod == 2}">
                                    <section rel="2" class="panel panel-default transportMethod">
                                        <input type="hidden" name="transportDetails[${tStatus.index}].id" value="${details.id}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].configId" value="${details.configId}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].transportMethod" value="${details.transportMethod}" />
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse2">Online Form Configuration</a></h3>
                                        </div>
                                    </section>
                                </c:when>
                                <c:when test="${details.transportMethod == 1}">
                                    <section id="method_1" rel="1" class="panel panel-default transportMethod">
                                        <input type="hidden" name="transportDetails[${tStatus.index}].id" value="${details.id}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].configId" value="${details.configId}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].transportMethod" value="${details.transportMethod}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].fileName" id="currFile" value="${details.fileName}" />
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse1">File Upload Configuration</a></h3>
                                        </div>
                                        <div id="collapse1" class="panel-collapse collapse out">
                                            <div class="panel-body">
                                                <c:if test="${not empty details.fileName}">
                                                    <div class="form-group">
                                                        <label class="control-label">Current File</label>
                                                        <div class="control-label">${details.fileName}</div>
                                                    </div>
                                                </c:if>
                                                <div id="templateFileDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="templateFile"><c:if test="${not empty details.fileName}">New</c:if> Template File *</label>
                                                    <input id="templateFile" type="file" name="transportDetails[${tStatus.index}].file" />
                                                    <span id="templateFileMsg" class="control-label"></span>
                                                </div>
                                                <div id="messageTypeColNoDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="messageTypeColNo">Column that will have the message type *</label>
                                                    <input id="messageTypeColNo" name="transportDetails[${tStatus.index}].messageTypeColNo" value="${details.messageTypeColNo}" class="form-control" maxLength="4" type="text" />
                                                    <span id="messageTypeColNoMsg" class="control-label"></span>
                                                </div>
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="messageTypeCustomVal">Message type value for this configuration *</label>
                                                    <input id="messageTypeCustomVal" name="transportDetails[${tStatus.index}].messageTypeCustomVal" value="${details.messageTypeCustomVal}" class="form-control" type="text" />
                                                </div>
                                                <div id="targetOrgColNoDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="targetOrgColNo">Column that will have the target organization *</label>
                                                    <input id="targetOrgColNo" name="transportDetails[${tStatus.index}].targetOrgColNo" value="${details.targetOrgColNo}" class="form-control" maxlength="4" type="text" />
                                                    <span id="targetOrgColNoMsg" class="control-label"></span>
                                                </div>
                                                <div id="fileTypeDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="fileType">File Type *</label>
                                                    <div>
                                                        <select id="fileType" name="transportDetails[${tStatus.index}].fileType" class="form-control half">
                                                            <option value="">- Select -</option>
                                                            <c:forEach items="${fileTypes}" var="type" varStatus="fStatus">
                                                                <option value="${fileTypes[fStatus.index][0]}" <c:if test="${details.fileType == fileTypes[fStatus.index][0]}">selected</c:if>>${fileTypes[fStatus.index][1]} </option>
                                                            </c:forEach>
                                                        </select>
                                                        <span id="fileTypeMsg" class="control-label"></span>
                                                    </div>
                                                </div>
                                                <div id="fileDelimDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="delimiter">File Delimiter *</label>
                                                    <div>
                                                        <select id="delimiter" name="transportDetails[${tStatus.index}].fileDelimiter" class="form-control half">
                                                            <option value="">- Select -</option>
                                                            <c:forEach items="${delimiters}" var="delim" varStatus="dStatus">
                                                                <option value="${delimiters[dStatus.index][0]}" <c:if test="${details.fileDelimiter == delimiters[dStatus.index][0]}">selected</c:if>>${delimiters[dStatus.index][1]} </option>
                                                            </c:forEach>
                                                        </select>
                                                        <span id="fileDelimMsg" class="control-label"></span>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label" for="header">Will the file contain a header row?</label>
                                                    <div>
                                                        <label class="radio-inline">
                                                            <input id="header" name="transportDetails[${tStatus.index}].containsHeader" value="1" type="radio" <c:if test="${details.containsHeader == true}">checked = "checked"</c:if> > Yes
                                                            </label>
                                                            <label class="radio-inline">
                                                                <input id="header" name="transportDetails[${tStatus.index}].containsHeader" value="0" type="radio" <c:if test="${details.containsHeader == false}">checked = "checked"</c:if> > No
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </section>
                                </c:when>
                                <c:when test="${details.transportMethod == 5}">
                                    <section id="method_5" rel="5" class="panel panel-default transportMethod">
                                        <input type="hidden" name="transportDetails[${tStatus.index}].id" value="${details.id}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].configId" value="${details.configId}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].transportMethod" value="${details.transportMethod}" />
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse5">API Configuration</a></h3>
                                        </div>
                                        <div id="collapse5" class="panel-collapse collapse out">
                                            <div class="panel-body">
                                                <div class="form-group">
                                                    <label for="fieldA">Template File *</label>
                                                    <input id="fieldA" class="form-control" type="text" />
                                                </div>

                                                <div class="form-group">
                                                    <label for="fieldA">Column that will have the message type *</label>
                                                    <input id="fieldA" class="form-control" type="text" />
                                                </div>

                                                <div class="form-group">
                                                    <label for="fieldA">Message type value for this configuration *</label>
                                                    <input id="fieldA" class="form-control" type="text" />
                                                </div>

                                                <div class="form-group">
                                                    <label for="fieldA">Column that will have the target organization *</label>
                                                    <input id="fieldA" class="form-control" type="text" />
                                                </div>
                                            </div>
                                        </div>
                                    </section>
                                </c:when>
                                <c:when test="${details.transportMethod == 3}">
                                    <section id="method_3" rel="3" class="panel panel-default transportMethod">
                                        <input type="hidden" name="transportDetails[${tStatus.index}].id" value="${details.id}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].configId" value="${details.configId}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].transportMethod" value="${details.transportMethod}" />
                                        <input type="hidden" id="FTPcurrFile" value="${details.fileName}" />
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse3">Secure FTP Configuration</a></h3>
                                        </div>
                                        <div id="collapse3" class="panel-collapse collapse out">
                                            <div class="panel-body">
                                                <c:if test="${not empty details.fileName}">
                                                    <div class="form-group">
                                                        <label class="control-label">Current File</label>
                                                        <div class="control-label">${details.fileName}</div>
                                                    </div>
                                                </c:if>
                                                <div id="FTPtemplateFileDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="FTPtemplateFile"><c:if test="${not empty details.fileName}">New</c:if> Template File *</label>
                                                    <input id="FTPtemplateFile" type="file" name="transportDetails[${tStatus.index}].file" />
                                                    <span id="FTPtemplateFileMsg" class="control-label"></span>
                                                </div>
                                                <div id="FTPmessageTypeColNoDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="messageTypeColNo">Column that will have the message type *</label>
                                                    <input id="FTPmessageTypeColNo" name="transportDetails[${tStatus.index}].messageTypeColNo" value="${details.messageTypeColNo}" class="form-control" maxLength="4" type="text" />
                                                    <span id="FTPmessageTypeColNoMsg" class="control-label"></span>
                                                </div>
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="messageTypeCustomVal">Message type value for this configuration *</label>
                                                    <input id="FTPmessageTypeCustomVal" name="transportDetails[${tStatus.index}].messageTypeCustomVal" value="${details.messageTypeCustomVal}" class="form-control" type="text" />
                                                </div>
                                                <div id="FTPtargetOrgColNoDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="targetOrgColNo">Column that will have the target organization *</label>
                                                    <input id="FTPtargetOrgColNo" name="transportDetails[${tStatus.index}].targetOrgColNo" value="${details.targetOrgColNo}" class="form-control" maxlength="4" type="text" />
                                                    <span id="FTPtargetOrgColNoMsg" class="control-label"></span>
                                                </div>
                                                <div id="FTPfileTypeDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="fileType">File Type *</label>
                                                    <div>
                                                        <select id="FTPfileType" name="transportDetails[${tStatus.index}].fileType" class="form-control half">
                                                            <option value="">- Select -</option>
                                                            <c:forEach items="${fileTypes}" var="type" varStatus="fStatus">
                                                                <option value="${fileTypes[fStatus.index][0]}" <c:if test="${details.fileType == fileTypes[fStatus.index][0]}">selected</c:if>>${fileTypes[fStatus.index][1]} </option>
                                                            </c:forEach>
                                                        </select>
                                                        <span id="FTPfileTypeMsg" class="control-label"></span>
                                                    </div>
                                                </div>
                                                <div id="FTPfileDelimDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="delimiter">File Delimiter *</label>
                                                    <div>
                                                        <select id="FTPdelimiter" name="transportDetails[${tStatus.index}].fileDelimiter" class="form-control half">
                                                            <option value="">- Select -</option>
                                                            <c:forEach items="${delimiters}" var="delim" varStatus="dStatus">
                                                                <option value="${delimiters[dStatus.index][0]}" <c:if test="${details.fileDelimiter == delimiters[dStatus.index][0]}">selected</c:if>>${delimiters[dStatus.index][1]} </option>
                                                            </c:forEach>
                                                        </select>
                                                        <span id="FTPfileDelimMsg" class="control-label"></span>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="control-label" for="header">Will the file contain a header row?</label>
                                                    <div>
                                                        <label class="radio-inline">
                                                            <input id="FTPheader" name="transportDetails[${tStatus.index}].containsHeader" value="1" type="radio" <c:if test="${details.containsHeader == true}">checked = "checked"</c:if> > Yes
                                                            </label>
                                                            <label class="radio-inline">
                                                                <input id="FTPheader" name="transportDetails[${tStatus.index}].containsHeader" value="0" type="radio" <c:if test="${details.containsHeader == false}">checked = "checked"</c:if> > No
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="fieldA">FTP IP *</label>
                                                        <input id="fieldA" class="form-control" type="text" />
                                                    </div>

                                                    <div class="form-group">
                                                        <label for="fieldA">FTP Directory *</label>
                                                        <input id="fieldA" class="form-control" type="text" />
                                                    </div>

                                                    <div class="form-group">
                                                        <label for="fieldA">FTP Username *</label>
                                                        <input id="fieldA" class="form-control" type="text" />
                                                    </div>

                                                    <div class="form-group">
                                                        <label for="fieldA">FTP Password *</label>
                                                        <input id="fieldA" class="form-control" type="text" />
                                                    </div>
                                                </div>
                                            </div>
                                        </section>
                                </c:when>
                                <c:when test="${details.transportMethod == 4}">
                                    <section id="method_4" rel="4" class="panel panel-default transportMethod">
                                        <input type="hidden" name="transportDetails[${tStatus.index}].id" value="${details.id}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].configId" value="${details.configId}" />
                                        <input type="hidden" name="transportDetails[${tStatus.index}].transportMethod" value="${details.transportMethod}" />
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse4">JSON Configuration</a></h3>
                                        </div>
                                    </section>
                                </c:when>
                            </c:choose>	

                        </c:forEach>
                    </form:form>
                </div>
            </div>
        </div>

    </div>
</div>

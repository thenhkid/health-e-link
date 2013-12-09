<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">
     <div class="row-fluid">
        <div class="col-md-12">

            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success">
                    <strong>Success!</strong> 
                    <c:choose><c:when test="${savedStatus == 'updated'}">The configuration transport details have been successfully updated!</c:when></c:choose>
                </div>
            </c:if>
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
                                            <option value="${transportMethods[tStatus.index][0]}"><c:choose><c:when test="${transportMethods[tStatus.index][0] == 1}"><c:choose><c:when test="${configurationDetails.type == 1}">File Upload</c:when><c:otherwise>File Download</c:otherwise></c:choose></c:when><c:otherwise>${transportMethods[tStatus.index][1]}</c:otherwise></c:choose></option>
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
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Assigned Transport Methods</h3>
                </div>
                <div class="panel-group" id="accordion">
                <form:form id="transportMethods" modelAttribute="configurationDetails" enctype="multipart/form-data" method="post" role="form">
                    <input type="hidden" id="action" name="action" value="save" />
                    <input type="hidden" id="configId" value="${configurationDetails.id}" />
                    <input type="hidden" id="configType" value="${configurationDetails.type}" />
                    <form:hidden path="stepsCompleted" />
                    <c:forEach items="${configurationDetails.transportDetails}" var="details" varStatus="tStatus">
                        <c:choose>
                            <c:when test="${details.transportMethod == 2}">
                                <section rel="2" class="panel panel-default transportMethod">
                                    <input type="hidden" name="transportDetails[${tStatus.index}].id" value="${details.id}" />
                                    <input type="hidden" name="transportDetails[${tStatus.index}].configId" value="${details.configId}" />
                                    <input type="hidden" name="transportDetails[${tStatus.index}].transportMethod" value="${details.transportMethod}" />
                                    <div class="panel-heading">
                                        <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse2"> ${tStatus.index+1}. Online Form Configuration</a></h3>
                                    </div>
                                    <div id="collapse2" class=""panel-collapse collapse out">
                                         <div class="panel-body">
                                            <div class="form-group">
                                                <label for="status">Status *</label>
                                                <div>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="transportDetails[${tStatus.index}].status" value="1" <c:if test="${details.status == true}">checked="checked"</c:if> /> Active 
                                                    </label>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="transportDetails[${tStatus.index}].status" value="0" <c:if test="${details.status == false}">checked="checked"</c:if> /> Inactive 
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="reportableField1">Reportable Field 1</label>
                                                <input id="reportableField1" name="transportDetails[${tStatus.index}].reportableField1" value="${details.reportableField1}" class="form-control" maxlength="4" type="text" />
                                            </div>  
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="reportableField2">Reportable Field 2</label>
                                                <input id="reportableField2" name="transportDetails[${tStatus.index}].reportableField2" value="${details.reportableField2}" class="form-control" maxlength="4" type="text" />
                                            </div>  
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="reportableField3">Reportable Field 3</label>
                                                <input id="reportableField3" name="transportDetails[${tStatus.index}].reportableField3" value="${details.reportableField3}" class="form-control" maxlength="4" type="text" />
                                            </div>  
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="reportableField4">Reportable Field 4</label>
                                                <input id="reportableField4" name="transportDetails[${tStatus.index}].reportableField4" value="${details.reportableField4}" class="form-control" maxlength="4" type="text" />
                                            </div>  
                                        </div>
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
                                        <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse1"> ${tStatus.index+1}. File <c:choose><c:when test="${configurationDetails.type == 1}">Upload</c:when><c:otherwise>Download</c:otherwise></c:choose> Configuration</a></h3>
                                    </div>
                                    <div id="collapse1" class="panel-collapse collapse out">
                                        <div class="panel-body">
                                            <div class="form-group">
                                                <label for="status">Status *</label>
                                                <div>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="transportDetails[${tStatus.index}].status" value="1" <c:if test="${details.status == true}">checked="checked"</c:if> /> Active 
                                                    </label>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="transportDetails[${tStatus.index}].status" value="0" <c:if test="${details.status == false}">checked="checked"</c:if> /> Inactive 
                                                    </label>
                                                </div>
                                            </div>
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
                                            <c:if test="${configurationDetails.type == 2}">
                                                <div id="targetFileNameDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="targetFileName">When created, what will the target file be called?</label>
                                                    <input id="targetFileName" name="transportDetails[${tStatus.index}].targetFileName" value="${details.targetFileName}" class="form-control" maxLength="255" type="text" />
                                                    <span id="targetFileNameMsg" class="control-label"></span>
                                                </div> 
                                                <div class="form-group">
                                                    <label for="status">Append a date/time stamp to the target file name?</label>
                                                    <div>
                                                        <label class="radio-inline">
                                                            <input type="radio" name="transportDetails[${tStatus.index}].appendDateTime" value="1" <c:if test="${details.appendDateTime == true}">checked="checked"</c:if> /> Yes 
                                                        </label>
                                                        <label class="radio-inline">
                                                            <input type="radio" name="transportDetails[${tStatus.index}].appendDateTime" value="0" <c:if test="${details.appendDateTime == false}">checked="checked"</c:if> /> No 
                                                        </label>
                                                    </div>
                                                </div>   
                                            </c:if>
                                            <c:if test="${configurationDetails.type == 1}">
                                                <div id="maxFileSizeDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="maxFileSize">Max File Size (MB) *</label>
                                                    <input id="maxFileSize" name="transportDetails[${tStatus.index}].maxFileSize" value="${details.maxFileSize}" class="form-control" maxLength="4" type="text" />
                                                    <span id="maxFileSizeMsg" class="control-label"></span>
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
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="reportableField1">Reportable Field 1</label>
                                                    <input id="reportableField1" name="transportDetails[${tStatus.index}].reportableField1" value="${details.reportableField1}" class="form-control" maxlength="4" type="text" />
                                                </div>  
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="reportableField2">Reportable Field 2</label>
                                                    <input id="reportableField2" name="transportDetails[${tStatus.index}].reportableField2" value="${details.reportableField2}" class="form-control" maxlength="4" type="text" />
                                                </div>  
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="reportableField3">Reportable Field 3</label>
                                                    <input id="reportableField3" name="transportDetails[${tStatus.index}].reportableField3" value="${details.reportableField3}" class="form-control" maxlength="4" type="text" />
                                                </div>  
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="reportableField4">Reportable Field 4</label>
                                                    <input id="reportableField4" name="transportDetails[${tStatus.index}].reportableField4" value="${details.reportableField4}" class="form-control" maxlength="4" type="text" />
                                                </div>
                                            </c:if>
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
                                        <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse5">${tStatus.index+1}. API Configuration</a></h3>
                                    </div>
                                    <div id="collapse5" class="panel-collapse collapse out">
                                        <div class="panel-body">
                                            <div class="form-group">
                                                <label for="status">Status *</label>
                                                <div>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="transportDetails[${tStatus.index}].status" value="1" <c:if test="${details.status == true}">checked="checked"</c:if> /> Active 
                                                    </label>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="transportDetails[${tStatus.index}].status" value="0" <c:if test="${details.status == false}">checked="checked"</c:if> /> Inactive 
                                                    </label>
                                                </div>
                                            </div>
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
                                        <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse3">${tStatus.index+1}. Secure FTP Configuration</a></h3>
                                    </div>
                                    <div id="collapse3" class="panel-collapse collapse out">
                                        <div class="panel-body">
                                            <div class="form-group">
                                                <label for="status">Status *</label>
                                                <div>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="transportDetails[${tStatus.index}].status" value="1" <c:if test="${details.status == true}">checked="checked"</c:if> /> Active 
                                                    </label>
                                                    <label class="radio-inline">
                                                        <input type="radio" name="transportDetails[${tStatus.index}].status" value="0" <c:if test="${details.status == false}">checked="checked"</c:if> /> Inactive 
                                                    </label>
                                                </div>
                                            </div>
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
                                           <c:if test="${configurationDetails.type == 2}">
                                                <div id="targetFileNameDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="targetFileName">When created, what will the target file be called?</label>
                                                    <input id="targetFileName" name="transportDetails[${tStatus.index}].targetFileName" value="${details.targetFileName}" class="form-control" maxLength="255" type="text" />
                                                    <span id="targetFileNameMsg" class="control-label"></span>
                                                </div> 
                                                <div class="form-group">
                                                    <label for="status">Append a date/time stamp to the target file name?</label>
                                                    <div>
                                                        <label class="radio-inline">
                                                            <input type="radio" name="transportDetails[${tStatus.index}].appendDateTime" value="1" <c:if test="${details.appendDateTime == true}">checked="checked"</c:if> /> Yes 
                                                        </label>
                                                        <label class="radio-inline">
                                                            <input type="radio" name="transportDetails[${tStatus.index}].appendDateTime" value="0" <c:if test="${details.appendDateTime == false}">checked="checked"</c:if> /> No 
                                                        </label>
                                                    </div>
                                                </div>    
                                            </c:if> 
                                            <c:if test="${configurationDetails.type == 1}">
                                                <div id="maxFileSizeDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="maxFileSize">Max File Size (MB) *</label>
                                                    <input id="maxFileSize" name="transportDetails[${tStatus.index}].maxFileSize" value="${details.maxFileSize}" class="form-control" maxLength="4" type="text" />
                                                    <span id="maxFileSizeMsg" class="control-label"></span>
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
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="reportableField1">Reportable Field 1</label>
                                                    <input id="reportableField1" name="transportDetails[${tStatus.index}].reportableField1" value="${details.reportableField1}" class="form-control" maxlength="4" type="text" />
                                                </div>  
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="reportableField2">Reportable Field 2</label>
                                                    <input id="reportableField2" name="transportDetails[${tStatus.index}].reportableField2" value="${details.reportableField2}" class="form-control" maxlength="4" type="text" />
                                                </div>  
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="reportableField3">Reportable Field 3</label>
                                                    <input id="reportableField3" name="transportDetails[${tStatus.index}].reportableField3" value="${details.reportableField3}" class="form-control" maxlength="4" type="text" />
                                                </div>  
                                                <div class="form-group ${status.error ? 'has-error' : '' }">
                                                    <label class="control-label" for="reportableField4">Reportable Field 4</label>
                                                    <input id="reportableField4" name="transportDetails[${tStatus.index}].reportableField4" value="${details.reportableField4}" class="form-control" maxlength="4" type="text" />
                                                </div>  
                                            </c:if>
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
                                                    <input id="header" name="transportDetails[${tStatus.index}].containsHeader" value="1" type="radio" <c:if test="${details.containsHeader == true}">checked = "checked"</c:if> > Yes
                                                    </label>
                                                    <label class="radio-inline">
                                                        <input id="header" name="transportDetails[${tStatus.index}].containsHeader" value="0" type="radio" <c:if test="${details.containsHeader == false}">checked = "checked"</c:if> > No
                                                    </label>
                                                </div>
                                            </div>
                                            <c:if test="${not empty details.FTPFields}">
                                                <c:forEach items="${details.FTPFields}" var="FTPInfo" varStatus="FTPStatus">
                                                    <input type="hidden" name="transportDetails[${tStatus.index}].FTPFields[${FTPStatus.index}].id" value="${FTPInfo.id}" />
                                                    <input type="hidden" name="transportDetails[${tStatus.index}].FTPFields[${FTPStatus.index}].transportId" value="${FTPInfo.transportId}" />
                                                    <input type="hidden" name="transportDetails[${tStatus.index}].FTPFields[${FTPStatus.index}].method" value="${FTPInfo.method}" />
                                                    <div class="form-group">
                                                         <label class="control-label"><c:choose><c:when test="${FTPInfo.method == 2}">FTP GET DETAILS</c:when><c:otherwise>FTP PUSH DETAILS</c:otherwise></c:choose></label>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="control-label" for="ip">FTP IP</label>
                                                        <input id="ip" name="transportDetails[${tStatus.index}].FTPFields[${FTPStatus.index}].ip" value="${FTPInfo.ip}" class="form-control" type="text" />
                                                    </div>

                                                    <div class="form-group">
                                                        <label class="control-label" for="directory">FTP Directory</label>
                                                        <input id="directory" name="transportDetails[${tStatus.index}].FTPFields[${FTPStatus.index}].directory" value="${FTPInfo.directory}" class="form-control" type="text" />
                                                    </div>

                                                    <div class="form-group">
                                                        <label class="control-label" for="username">FTP Username</label>
                                                        <input id="username" name="transportDetails[${tStatus.index}].FTPFields[${FTPStatus.index}].username" value="${FTPInfo.username}" class="form-control" type="text" />
                                                    </div>

                                                    <div class="form-group">
                                                        <label class="control-label" for="password">FTP Password</label>
                                                        <input id="password" name="transportDetails[${tStatus.index}].FTPFields[${FTPStatus.index}].password" value="${FTPInfo.password}" class="form-control" type="text" />
                                                    </div>
                                                </c:forEach>
                                            </c:if>             
                                        </div>
                                    </section>
                            </c:when>
                            <c:when test="${details.transportMethod == 4}">
                                <section id="method_4" rel="4" class="panel panel-default transportMethod">
                                    <input type="hidden" name="transportDetails[${tStatus.index}].id" value="${details.id}" />
                                    <input type="hidden" name="transportDetails[${tStatus.index}].configId" value="${details.configId}" />
                                    <input type="hidden" name="transportDetails[${tStatus.index}].transportMethod" value="${details.transportMethod}" />
                                    <div class="panel-heading">
                                        <h3 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse4">${tStatus.index+1}. JSON Configuration</a></h3>
                                    </div>
                                </section>
                            </c:when>
                        </c:choose>	

                    </c:forEach>
                </form:form>
            </div>
            </section>
        </div>
    </div>
</div>

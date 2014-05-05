<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="main clearfix" role="main">
    <div class="row-fluid">
        <div class="col-md-12">

            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success">
                    <strong>Success!</strong> 
                    <c:choose><c:when test="${savedStatus == 'updated'}">The configuration transport details have been successfully updated!</c:when><c:otherwise>The configuration initial setup has been saved!</c:otherwise></c:choose>
                </div>
            </c:if>
            <section class="panel panel-default">
                <div class="panel-body">
                    <dt>
                    <dt>Configuration Summary:</dt>
                    <dd><strong>Organization:</strong> ${configurationDetails.orgName}&nbsp;<span id="configtype" rel="${configurationDetails.type}"><c:choose><c:when test="${configurationDetails.type == 1}">(Source)</c:when><c:otherwise>(Target)</c:otherwise></c:choose></span></dd>
                    <dd><strong>Configuration Type:</strong> <span id="configType" rel="${configurationDetails.type}"><c:choose><c:when test="${configurationDetails.type == 1}">Source</c:when><c:otherwise>Target</c:otherwise></c:choose></span></dd>
                    <dd><strong>Configuration Name:</strong> ${configurationDetails.configName}</dd>
                    <dd><strong>Message Type:</strong> ${configurationDetails.messageTypeName}</dd>
                    <dd><strong>Transport Method:</strong> <c:choose><c:when test="${configurationDetails.transportMethod == 'File Upload'}"><c:choose><c:when test="${configurationDetails.type == 1}">File Upload</c:when><c:otherwise>File Download</c:otherwise></c:choose></c:when><c:otherwise>${configurationDetails.transportMethod}</c:otherwise></c:choose></dd>
                    </dt>
                </div>
            </section>
       </div>

       <div class="col-md-12">

            <form:form id="transportDetails" commandName="transportDetails" modelAttribute="transportDetails" method="post" role="form" enctype="multipart/form-data">

                <c:if test="${(transportDetails.id == 0 && fn:length(availConfigurations) > 1) || transportDetails.copiedTransportId > 0}">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Existing Transport Methods</h3>
                        </div>
                        <div class="panel-body">
                            <div class="form-container">
                                <div id="existingtransportMethodDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                    <form:select path="copiedTransportId" id="existingtransportMethod" class="form-control" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}">
                                        <option value="">- Select -</option>
                                        <c:forEach items="${availConfigurations}" var="config">
                                            <c:if test="${config.getId() != transportDetails.configId}">
                                                <option value="${config.gettransportDetailId()}" <c:if test="${config.gettransportDetailId() == transportDetails.copiedTransportId}">selected</c:if>>${config.getconfigName()}&nbsp;&#149;&nbsp;${config.getMessageTypeName()}&nbsp;&#149;&nbsp;${config.gettransportMethod()}</option>
                                            </c:if>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>  
                        </div>
                    </section>  
                </c:if>
                <form:hidden path="copiedTransportId" />     


                <input type="hidden" id="action" name="action" value="save" />
                <form:hidden path="id" id="id" />
                <form:hidden path="configId" id="configId" />

                <section class="panel panel-default">

                    <div class="panel-heading">
                        <h3 class="panel-title">Details</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-container">
                            <spring:bind path="transportMethodId">
                                <div id="transportMethodDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                    <label class="control-label" for="transportMethod">Transport Method *</label>
                                    <form:select path="transportMethodId" id="transportMethod" class="form-control" disabled="${transportDetails.id > 0 ? 'true' : 'false'}">
                                        <option value="">- Select -</option>
                                        <c:forEach items="${transportMethods}" var="transMethod" varStatus="tStatus">
                                            <option value="${transportMethods[tStatus.index][0]}" <c:if test="${transportDetails.transportMethodId == transportMethods[tStatus.index][0]}">selected</c:if>><c:choose><c:when test="${transportMethods[tStatus.index][0] == 1}"><c:choose><c:when test="${configurationDetails.type == 1}">File Upload</c:when><c:otherwise>File Download</c:otherwise></c:choose></c:when><c:otherwise>${transportMethods[tStatus.index][1]}</c:otherwise></c:choose></option>
                                        </c:forEach>
                                    </form:select>
                                    <span id="transportMethodMsg" class="control-label"></span>
                                    <c:if test="${transportDetails.id > 0}">
                                        <form:hidden path="transportMethodId" />
                                    </c:if>
                                </div>
                            </spring:bind>
                             <c:if test="${configurationDetails.type == 1}">    
                                <spring:bind path="autoRelease">
                                    <div class="form-group">
                                        <label class="control-label" for="autoRelease">Release Records *</label>
                                        <div>
                                            <label class="radio-inline">
                                                <form:radiobutton id="autoRelease" path="autoRelease" value="1" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Automatically 
                                            </label>
                                            <label class="radio-inline">
                                                <form:radiobutton id="autoRelease" path="autoRelease" value="0" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Manually
                                            </label>
                                        </div>
                                        <c:if test="${transportDetails.copiedTransportId > 0}">
                                            <form:hidden path="autoRelease" />
                                        </c:if>    
                                    </div>
                                </spring:bind>  
                            </c:if>
                            <div id="upload-downloadDiv" class="methodDiv" style="display:none">
                                <spring:bind path="clearRecords">
                                    <div class="form-group">
                                        <label class="control-label" for="status">Clear Records after Delivery *</label>
                                        <div>
                                            <label class="radio-inline">
                                                <form:radiobutton id="clearRecords" path="clearRecords" value="1" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Yes 
                                            </label>
                                            <label class="radio-inline">
                                                <form:radiobutton id="clearRecords" path="clearRecords" value="0" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}"/> No
                                            </label>
                                        </div>
                                        <c:if test="${transportDetails.copiedTransportId > 0}">
                                            <form:hidden path="clearRecords" />
                                        </c:if>
                                    </div>
                                </spring:bind>
                                <spring:bind path="fileLocation">
                                    <div class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="fileLocation">File Location *</label>
                                        <form:input path="fileLocation" id="fileLocation" class="form-control" type="text" maxLength="255" />
                                        <form:errors path="fileLocation" cssClass="control-label" element="label" />
                                    </div>
                                </spring:bind>
                                <spring:bind path="maxFileSize">
                                    <div id="maxFileSizeDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="maxFileSize">Max File Size (mb) *</label>
                                        <form:input path="maxFileSize" id="maxFileSize" class="form-control" type="text" maxLength="11" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" />
                                        <form:errors path="maxFileSize" cssClass="control-label" element="label" />
                                        <span id="maxFileSizeMsg" class="control-label"></span>
                                        <c:if test="${transportDetails.copiedTransportId > 0}">
                                            <form:hidden path="maxFileSize" />
                                        </c:if>
                                    </div>
                                </spring:bind>
                                <spring:bind path="fileType">
                                    <div id="fileTypeDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="fileType">File Type *</label>
                                        <form:select path="fileType" id="fileType" class="form-control" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}">
                                            <option value="">- Select -</option>
                                            <c:forEach items="${fileTypes}" varStatus="fStatus">
                                                <c:if test="${fileTypes[fStatus.index][0] != 1}">
                                                    <option value="${fileTypes[fStatus.index][0]}" <c:if test="${transportDetails.fileType == fileTypes[fStatus.index][0]}">selected</c:if>>${fileTypes[fStatus.index][1]}</option>
                                                </c:if>
                                            </c:forEach>
                                        </form:select>
                                        <span id="fileTypeMsg" class="control-label"></span>
                                        <c:if test="${transportDetails.copiedTransportId > 0}">
                                            <form:hidden path="fileType" />
                                        </c:if>
                                    </div>
                                </spring:bind>
                                <spring:bind path="fileExt">
                                    <div id="fileExtDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="fileExt">File Extension *</label>
                                        <form:input path="fileExt" id="fileExt" class="form-control" type="text" maxLength="4" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" />
                                        <form:errors path="fileExt" cssClass="control-label" element="label" />
                                        <span id="fileExtMsg" class="control-label"></span>
                                        <c:if test="${transportDetails.copiedTransportId > 0}">
                                            <form:hidden path="fileExt" />
                                        </c:if>
                                    </div>
                                </spring:bind>
                                <spring:bind path="fileDelimiter">
                                    <div id="fileDelimiterDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="fileDelimiter">File Delimiter *</label>
                                        <form:select path="fileDelimiter" id="fileDelimiter" class="form-control" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}">
                                            <option value="">- Select -</option>
                                            <c:forEach items="${delimiters}" varStatus="dStatus">
                                                <option value="${delimiters[dStatus.index][0]}" <c:if test="${transportDetails.fileDelimiter == delimiters[dStatus.index][0]}">selected</c:if>>${delimiters[dStatus.index][1]}</option>
                                            </c:forEach>
                                        </form:select>
                                        <span id="fileDelimiterMsg" class="control-label"></span>
                                        <c:if test="${transportDetails.copiedTransportId > 0}">
                                            <form:hidden path="fileDelimiter" />
                                        </c:if>
                                    </div>
                                </spring:bind>
                                <%-- Target File Download options only --%>
                                <c:if test="${configurationDetails.type == 2}">
                                    <spring:bind path="targetFileName">
                                        <div class="form-group ${status.error ? 'has-error' : '' }">
                                            <label class="control-label" for="targetFileName">File Name * <input id="useSource" type="checkbox"> Use Source File Name</label>
                                                <form:input path="targetFileName" id="targetFileName" class="form-control" type="text" maxLength="255" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" />
                                                <form:errors path="targetFileName" cssClass="control-label" element="label" />
                                                <c:if test="${transportDetails.copiedTransportId > 0}">
                                                    <form:hidden path="targetFileName" />
                                                </c:if>
                                        </div>
                                    </spring:bind>
                                    <spring:bind path="appendDateTime">
                                        <div class="form-group">
                                            <label class="control-label" for="appendDateTime">Append Date and Time to file Name? *</label>
                                            <div>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="appendDateTime" path="appendDateTime" value="1" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Yes 
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="appendDateTime" path="appendDateTime" value="0" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> No
                                                </label>
                                            </div>
                                            <c:if test="${transportDetails.copiedTransportId > 0}">
                                                <form:hidden path="appendDateTime" />
                                            </c:if>    
                                        </div>
                                    </spring:bind>
                                    <spring:bind path="mergeBatches">
                                        <div class="form-group">
                                            <label class="control-label" for="mergeBatches">Merge Batches? <span class="badge badge-help" data-placement="top" title="" data-original-title="If multiple batches found should they be created all in one file?">?</span> *</label>
                                            <div>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="mergeBatches" path="mergeBatches" value="1" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Yes 
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="mergeBatches" path="mergeBatches" value="0" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> No
                                                </label>
                                            </div>
                                            <c:if test="${transportDetails.copiedTransportId > 0}">
                                                <form:hidden path="mergeBatches" />
                                            </c:if>       
                                        </div>
                                    </spring:bind>
                                </c:if>
                                <%-- Source File Upload options only --%>
                                <c:if test="${configurationDetails.type == 1}">
                                    <spring:bind path="errorHandling">
                                        <div class="form-group">
                                            <label class="control-label" for="errorHandling">Error Handling *</label>
                                            <div>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="errorHandling" path="errorHandling" value="1" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Post errors to ERG 
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="errorHandling" path="errorHandling" value="2" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Reject record on error
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="errorHandling" path="errorHandling" value="3" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Reject submission on error
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="errorHandling" path="errorHandling" value="4" disabled="${transportDetails.copiedTransportId > 0 ? 'true' : 'false'}" /> Pass through errors
                                                </label>
                                            </div>
                                            <c:if test="${transportDetails.copiedTransportId > 0}">
                                                <form:hidden path="errorHandling" />
                                            </c:if>      
                                        </div>
                                    </spring:bind>
                                </c:if>
                            </div>
                            <div id="additionalFTPDiv" class="methodDiv" style="display:none">
                                <div id="FTPDanger" class="alert alert-danger" style="display:none;">
                                    At least one FTP section must be filled out!
                                </div> 
                                <div class="row">
                                    <c:forEach items="${transportDetails.FTPFields}" var="ftpDetails" varStatus="field">
                                        <div class="form-group col-md-6">
                                            <div class="form-group">
                                                <label for="status">FTP <c:choose><c:when test="${ftpDetails.method == 1}">Get</c:when><c:otherwise>Push</c:otherwise></c:choose> Details</label>
                                                <input name="FTPFields[${field.index}].method" class="form-control" type="hidden" value="${ftpDetails.method}"  />
                                                <input name="FTPFields[${field.index}].id" id="id${ftpDetail.method}" class="form-control" type="hidden" value="${ftpDetails.id}"  />
                                            </div>
                                            <div id="protocol${ftpDetails.method}Div" class="form-group">
                                                <label class="control-label" for="protocol${ftpDetails.method}">Protocol *</label>
                                                <select name="FTPFields[${field.index}].protocol" id="protocol${ftpDetails.method}" rel="${ftpDetails.method}" class="form-control ftpProtocol">
                                                    <option value="">- Select -</option>
                                                    <option value="FTP" <c:if test="${ftpDetails.protocol == 'FTP'}">selected</c:if>>FTP</option>
                                                    <option value="FTPS" <c:if test="${ftpDetails.protocol == 'FTPS'}">selected</c:if>>FTPS</option>
                                                    <option value="SFTP" <c:if test="${ftpDetails.protocol == 'SFTP'}">selected</c:if>>SFTP</option>
                                                    </select>
                                                    <span id="protocol${ftpDetails.method}Msg" class="control-label"></span>
                                            </div>
                                            <div id="ip${ftpDetails.method}Div" class="form-group">
                                                <label class="control-label" for="ip${ftpDetails.method}">Host *</label>
                                                <input name="FTPFields[${field.index}].ip" id="ip${ftpDetails.method}" class="form-control" type="text" maxLength="45" value="${ftpDetails.ip}"  />
                                                <span id="ip${ftpDetails.method}Msg" class="control-label"></span>
                                            </div>
                                            <div id="username${ftpDetails.method}Div" class="form-group">
                                                <label class="control-label" for="username${ftpDetails.method}">Username *</label>
                                                <input name="FTPFields[${field.index}].username" id="username${ftpDetails.method}" class="form-control" type="text" maxLength="45" value="${ftpDetails.username}"  />
                                                <span id="username${ftpDetails.method}Msg" class="control-label"></span>
                                            </div>
                                            <div id="password${ftpDetails.method}Div" class="form-group">
                                                <label class="control-label" for="password${ftpDetails.method}">Password *</label>
                                                <input name="FTPFields[${field.index}].password" id="password${ftpDetails.method}" class="form-control" type="text" maxLength="45" value="${ftpDetails.password}"  />
                                                <span id="password${ftpDetails.method}Msg" class="control-label"></span>
                                            </div>
                                            <div id="directory${ftpDetails.method}Div" class="form-group">
                                                <label class="control-label" for="directory${ftpDetails.method}">Directory *</label>
                                                <input name="FTPFields[${field.index}].directory" id="directory${ftpDetails.method}" class="form-control" type="text" maxLength="255" value="${ftpDetails.directory}"  />
                                                <span id="directory${ftpDetails.method}Msg" class="control-label"></span>
                                            </div>
                                            <div id="port${ftpDetails.method}Div" class="form-group">
                                                <label class="control-label" for="port${ftpDetails.method}">Port *</label>
                                                <input name="FTPFields[${field.index}].port" id="port${ftpDetails.method}" class="form-control" type="text" maxLength="45" value="${ftpDetails.port}"  />
                                                <span id="port${ftpDetails.method}Msg" class="control-label"></span>
                                            </div>
                                            <div class="form-group" <c:if test="${ftpDetails.certification == null || ftpDetails.certification == ''}">style="display:none" </c:if>>
                                                    <label class="control-label">Existing Certifciation:</label>
                                                    <input type="text" disabled value="${ftpDetails.certification}" class="form-control" />
                                                <input type="hidden" id="certification${ftpDetails.method}" name="FTPFields[${field.index}].certification" value="${ftpDetails.certification}" />
                                            </div>
                                            <div id="certificationfileDiv${ftpDetails.method}" class="form-group ${status.error ? 'has-error' : '' }" style="display:none;">
                                                <label class="control-label" for="certification"><c:if test="${ftpDetails.certification != null}">New </c:if>Certification File </label>
                                                <input type="file" id="file${ftpDetails.method}" name="FTPFields[${field.index}].file" class="form-control"  />
                                                <span id="certificationfileMsg" class="control-label"></span>
                                            </div>
                                            <c:if test="${ftpDetails.ip != ''}">
                                               <div class="pull-right">
                                                    <a href="javascript:void(0);" class="btn btn-primary btn-xs testFTP<c:choose><c:when test="${ftpDetails.method == 1}">Get</c:when><c:otherwise>Push</c:otherwise></c:choose>"  title="Test FTP Connection">Test FTP Connection</a>
                                                </div>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                           
                        </div>
                    </div>
                </section>

                <div id="messageTypeDanger" class="alert alert-danger" style="display:none;">
                    At least one message type must be selected!
                </div>                 
                <section class="panel panel-default assocMessageTypes" style="display:none;">
                    <div class="panel-heading">
                        <h3 class="panel-title">Associated Message Types</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-container">
                            <table class="table table-striped table-hover table-default">
                                <thead>
                                    <tr>
                                        <th scope="col">Use?</th>
                                        <th scope="col">Configuration Name</th>
                                        <th scope="col">Message Type</th>
                                        <th scope="col" class="center-text">Date Created</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${availConfigurations}" var="configs">
                                        <tr id="configRow" rel="${config.id}" style="cursor: pointer">
                                            <td scope="row">
                                                <form:checkbox class="availMessageTypes" path="messageTypes" value="${configs.getId()}" />
                                            </td>
                                            <td>
                                                ${configs.getconfigName()}
                                            <td>
                                                ${configs.getMessageTypeName()}
                                            </td>
                                            <td class="center-text"><fmt:formatDate value="${configs.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                        </tr>
                                    </c:forEach> 
                                </tbody>
                            </table>

                        </div>  
                    </div>
                </section>    
            </form:form>
        </div>     
    </div>
</div>



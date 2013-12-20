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
                        <dd><strong>Authorized User</strong> ${configurationDetails.userName}</dd>
                        <dd><strong>Message Type:</strong> ${configurationDetails.messageTypeName}</dd>
                        <dd><strong>Transport Method:</strong> ${configurationDetails.transportMethod}</dd>
                    </dt>
                </div>
            </section>
        </div>
                    
        <div class="col-md-12">

            <form:form id="transportDetails" commandName="transportDetails" modelAttribute="transportDetails" method="post" role="form">
                <input type="hidden" id="action" name="action" value="save" />
                <form:hidden path="id" id="id" />
                <form:hidden path="configId" />

                <section class="panel panel-default">

                    <div class="panel-heading">
                        <h3 class="panel-title">Details</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-container">
                            <spring:bind path="clearRecords">
                                <div class="form-group">
                                    <label class="control-label" for="status">Clear Records after Delivery *</label>
                                    <div>
                                        <label class="radio-inline">
                                          <form:radiobutton id="clearRecords" path="clearRecords" value="1" /> Yes 
                                        </label>
                                        <label class="radio-inline">
                                            <form:radiobutton id="clearRecords" path="clearRecords" value="0"/> No
                                        </label>
                                    </div>
                                </div>
                            </spring:bind>
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
                            <div id="upload-downloadDiv" class="methodDiv" style="display:none">
                                <%--<spring:bind path="fileLocation">
                                    <div class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="fileLocation">File Location *</label>
                                        <form:input path="fileLocation" id="fileLocation" class="form-control" type="text" maxLength="255" />
                                        <form:errors path="fileLocation" cssClass="control-label" element="label" />
                                   </div>
                                </spring:bind>--%>
                                <spring:bind path="maxFileSize">
                                    <div id="maxFileSizeDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="maxFileSize">Max File Size (mb) *</label>
                                        <form:input path="maxFileSize" id="maxFileSize" class="form-control" type="text" maxLength="11" />
                                        <form:errors path="maxFileSize" cssClass="control-label" element="label" />
                                        <span id="maxFileSizeMsg" class="control-label"></span>
                                   </div>
                                </spring:bind>
                                <spring:bind path="fileType">
                                    <div id="fileTypeDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="fileType">File Type *</label>
                                        <form:select path="fileType" id="fileType" class="form-control">
                                            <option value="">- Select -</option>
                                            <c:forEach items="${fileTypes}" varStatus="fStatus">
                                                <c:if test="${fileTypes[fStatus.index][0] != 1}">
                                                    <option value="${fileTypes[fStatus.index][0]}" <c:if test="${transportDetails.fileType == fileTypes[fStatus.index][0]}">selected</c:if>>${fileTypes[fStatus.index][1]}</option>
                                                </c:if>
                                            </c:forEach>
                                        </form:select>
                                        <span id="fileTypeMsg" class="control-label"></span>
                                    </div>
                                </spring:bind>
                                <spring:bind path="fileDelimiter">
                                    <div id="fileDelimiterDiv" class="form-group ${status.error ? 'has-error' : '' }">
                                        <label class="control-label" for="fileDelimiter">File Delimiter *</label>
                                        <form:select path="fileDelimiter" id="fileDelimiter" class="form-control">
                                            <option value="">- Select -</option>
                                            <c:forEach items="${delimiters}" varStatus="dStatus">
                                                <option value="${delimiters[dStatus.index][0]}" <c:if test="${transportDetails.fileDelimiter == delimiters[dStatus.index][0]}">selected</c:if>>${delimiters[dStatus.index][1]}</option>
                                            </c:forEach>
                                        </form:select>
                                        <span id="fileDelimiterMsg" class="control-label"></span>
                                    </div>
                                </spring:bind>
                                <%-- Target File Download options only --%>
                                <c:if test="${configurationDetails.type == 2}">
                                    <spring:bind path="targetFileName">
                                        <div class="form-group ${status.error ? 'has-error' : '' }">
                                            <label class="control-label" for="targetFileName">File Name * <input id="useSource" type="checkbox"> Use Source File Name</label>
                                            <form:input path="targetFileName" id="targetFileName" class="form-control" type="text" maxLength="255" />
                                            <form:errors path="targetFileName" cssClass="control-label" element="label" />
                                       </div>
                                    </spring:bind>
                                    <spring:bind path="appendDateTime">
                                        <div class="form-group">
                                            <label class="control-label" for="appendDateTime">Append Date and Time to file Name? *</label>
                                            <div>
                                                <label class="radio-inline">
                                                  <form:radiobutton id="appendDateTime" path="appendDateTime" value="1" /> Yes 
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="appendDateTime" path="appendDateTime" value="0"/> No
                                                </label>
                                            </div>
                                        </div>
                                    </spring:bind>
                                    <spring:bind path="mergeBatches">
                                        <div class="form-group">
                                            <label class="control-label" for="mergeBatches">Merge Batches? <span class="badge badge-help" data-placement="top" title="" data-original-title="If multiple senders provide batches, do the batches need to be delivered as they were sent?">?</span> *</label>
                                            <div>
                                                <label class="radio-inline">
                                                  <form:radiobutton id="mergeBatches" path="mergeBatches" value="1" /> Yes 
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="mergeBatches" path="mergeBatches" value="0"/> No
                                                </label>
                                            </div>
                                        </div>
                                    </spring:bind>
                                </c:if>
                            </div>
                            <div id="additionalFTPDiv" class="methodDiv" style="display:none">
                            
                            </div>
                            <spring:bind path="errorHandling">
                                <div class="form-group">
                                    <label class="control-label" for="errorHandling">Error Handling *</label>
                                    <div>
                                        <label class="radio-inline">
                                          <form:radiobutton id="errorHandling" path="errorHandling" value="1" /> Post errors to ERG 
                                        </label>
                                        <label class="radio-inline">
                                            <form:radiobutton id="errorHandling" path="errorHandling" value="2"/> Reject record on error
                                        </label>
                                        <label class="radio-inline">
                                            <form:radiobutton id="errorHandling" path="errorHandling" value="3"/> Reject submission on error
                                        </label>
                                        <label class="radio-inline">
                                            <form:radiobutton id="errorHandling" path="errorHandling" value="4"/> Pass through errors
                                        </label>
                                    </div>
                                </div>
                            </spring:bind>
                            <spring:bind path="autoRelease">
                                <div class="form-group">
                                    <label class="control-label" for="autoRelease">Release Records *</label>
                                    <div>
                                        <label class="radio-inline">
                                          <form:radiobutton id="autoRelease" path="autoRelease" value="1" /> Automatically 
                                        </label>
                                        <label class="radio-inline">
                                            <form:radiobutton id="autoRelease" path="autoRelease" value="0"/> Manually
                                        </label>
                                    </div>
                                </div>
                            </spring:bind>    
                        </div>
                    </div>
                </section>   
            </form:form>
        </div>     
     </div>
</div>
                    
                    

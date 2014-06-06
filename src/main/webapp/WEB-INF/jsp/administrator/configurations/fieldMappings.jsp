<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">
    <div class="row-fluid">
        <div class="col-md-12">
            <div class="alert alert-success fieldsUpdated" style="display:none;">
                <strong>Success!</strong> 
                The field mappings have been successfully updated!
            </div>
            <div id="saveMsgDiv" class="alert alert-danger" style="display:none;">
                <strong>You must click SAVE above to submit the mapping changes!</strong>
            </div>
            <section class="panel panel-default">
                <div class="panel-body">
                    <dt>
                    <dt>Configuration Summary:</dt>
                    <dd><strong>Organization:</strong> ${configurationDetails.orgName}&nbsp;<span id="configtype" rel="${configurationDetails.type}"><c:choose><c:when test="${configurationDetails.type == 1}">(Source)</c:when><c:otherwise>(Target)</c:otherwise></c:choose></span></dd>
                    <dd><strong>Configuration Name:</strong> ${configurationDetails.configName}</dd>
                    <dd><strong>Message Type:</strong> ${configurationDetails.messageTypeName}</dd>
                    <dd><strong>Transport Method:</strong> <c:choose><c:when test="${configurationDetails.transportMethod == 'File Upload'}"><c:choose><c:when test="${configurationDetails.type == 1}">File Upload</c:when><c:otherwise>File Download</c:otherwise></c:choose></c:when><c:otherwise>${configurationDetails.transportMethod}</c:otherwise></c:choose></dd>
                                    </dt>
                                </div>
                            </section>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="col-md-6">
                            <section class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="pull-right">
                                        <a class="btn btn-primary btn-xs  btn-action" id="meetsStandard" data-toggle="tooltip" data-original-title="Click here to match to the starndard fields.">Meets Standard</a>
                                    </div>
                                    <h3 class="panel-title">Uploaded File Fields</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="form-container scrollable">
                        <form:form id="formFields" modelAttribute="transportDetails" method="post" role="form">
                            <input type="hidden" id="action" name="action" value="save" />
                            <input type="hidden" id="seltransportMethod" name="transportMethod" value="${selTransportMethod}" />
                            <input type="hidden" name="errorHandling" value="${transportDetails.errorHandling}" />
                            <table class="table table-striped table-hover table-default">
                                <thead>
                                    <tr>
                                        <th scope="col" class="center-text">Field No</th>
                                        <th scope="col">Field Name</th>
                                        <th scope="col" class="center-text">Required</th> 
                                        <th scope="col" class="center-text">Use Field</th>
                                        <th scope="col" class="center-text">Matching Field</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="i" begin="1" end="6">
                                        <tr>
                                            <td colspan="5"><strong><c:choose><c:when test="${i==1}"> (Sender Organization Information)</c:when><c:when test="${i==2}"> (Sender Provider Information)</c:when><c:when test="${i==3}">(Recipient Organization Information)</c:when><c:when test="${i==4}">(Recipient Provider Information)</c:when><c:when test="${i==5}"> (Patient Information)</c:when><c:when test="${i==6}"> (Details)</c:when></c:choose></strong></td>
                                        </tr>
                                        <c:forEach items="${transportDetails.fields}" var="mappings" varStatus="field">
                                            <c:if test="${mappings.bucketNo == i}">
                                                <tr class="uFieldRow" rel="${mappings.fieldNo}" rel2="${field.index}">
                                                    <td scope="row" class="center-text">
                                                        <input type="hidden" name="fields[${field.index}].id" value="${mappings.id}" />
                                                        <input type="hidden" name="fields[${field.index}].configId" value="${mappings.configId}" />
                                                        <input type="hidden" name="fields[${field.index}].transportDetailId" value="${mappings.transportDetailId}" />
                                                        <input type="hidden" name="fields[${field.index}].fieldNo" value="${mappings.fieldNo}" />
                                                        <input type="hidden" name="fields[${field.index}].bucketNo" value="${mappings.bucketNo}" />
                                                        <input type="hidden" name="fields[${field.index}].fieldDesc" value="${mappings.fieldDesc}" />
                                                        <input type="hidden" name="fields[${field.index}].fieldLabel" value="${mappings.fieldLabel}" />
                                                        <input type="hidden" name="fields[${field.index}].required" value="${mappings.required}" />
                                                        <input type="hidden" name="fields[${field.index}].bucketDspPos" value="${mappings.bucketDspPos}" />
                                                        <input type="hidden" id="validationType_${field.index}" name="fields[${field.index}].validationType" value="${mappings.validationType}" />
                                                        <input type="hidden" id="saveToTableName_${field.index}" name="fields[${field.index}].saveToTableName" value="${mappings.saveToTableName}" />
                                                        <input type="hidden" id="saveToTableCol_${field.index}" name="fields[${field.index}].saveToTableCol" value="${mappings.saveToTableCol}" />
                                                        <input type="hidden" name="fields[${field.index}].autoPopulateTableName" value="${mappings.autoPopulateTableName}" />
                                                        <input type="hidden" name="fields[${field.index}].autoPopulateTableCol" value="${mappings.autoPopulateTableCol}" />
                                                        ${mappings.fieldNo}
                                                    </td>
                                                    <td>${mappings.fieldDesc}</td>
                                                    <td class="center-text">
                                                        <input type="checkbox" disabled="disabled" <c:if test="${mappings.required == true}">checked</c:if>  />
                                                    </td>
                                                    <td class="center-text">
                                                        <input type="checkbox" name="fields[${field.index}].useField" <c:if test="${mappings.useField == true}">checked</c:if> />
                                                    </td>
                                                    <td class="center-text">
                                                        <select name="fields[${field.index}].messageTypeFieldId" id="matchingField_${mappings.fieldNo}" rel="${field.index}" class="formField matchingField">
                                                        <option value="0">-</option>
                                                        <c:forEach var="tField" items="${templateFields}">
                                                            <option value="${tField.id}" <c:if test="${mappings.messageTypeFieldId == tField.id}">selected</c:if>>${tField.fieldLabel} - ${tField.fieldNo}</option>
                                                        </c:forEach>
                                                    </select>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </section>
        </div>
        <div class="col-md-6">
            <section class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Selected Message Template Fields</h3>
                </div>
                <div class="panel-body">
                    <div class="form-container scrollable">
                        <table class="table table-striped table-hover table-default">
                            <thead>
                                <tr>
                                    <th scope="col" class="center-text">Field No</th>
                                    <th scope="col">Field Name</th>
                                    <th scope="col" class="center-text">Required</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="i" begin="1" end="6">
                                    <tr>
                                        <td colspan="3"><strong><c:choose><c:when test="${i==1}"> (Sender Organization Information)</c:when><c:when test="${i==2}"> (Sender Provider Information)</c:when><c:when test="${i==3}">(Recipient Organization Information)</c:when><c:when test="${i==4}">(Recipient Provider Information)</c:when><c:when test="${i==5}"> (Patient Information)</c:when><c:when test="${i==6}"> (Details)</c:when></c:choose></strong></td>
                                            </tr>
                                    <c:forEach var="tField" items="${templateFields}">
                                        <c:if test="${tField.bucketNo == i}">
                                            <tr>
                                                <td scope="row" class="center-text">${tField.fieldNo}</td>
                                                <td>
                                                    ${tField.fieldDesc}
                                                    <input type="hidden" id="tableName_${tField.id}" value="${tField.saveToTableName}" />
                                                    <input type="hidden" id="tableCol_${tField.id}" value="${tField.saveToTableCol}" />
                                                    <input type="hidden" id="validationType_${tField.id}" value="${tField.validationType}" />
                                                </td>
                                                <td class="center-text">
                                                    <input type="checkbox" disabled="disabled" <c:if test="${tField.required == true}">checked</c:if>  />
                                                    </td>
                                                </tr>
                                        </c:if>
                                    </c:forEach>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
        </div>
    </div>
</div>
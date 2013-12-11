<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Page variable to hold what dsp pos value to show -->
<c:set var="endDspPostLoop" value="1" />

<!-- Need to calculate how many fields for current section -->
<c:set var="totalFieldsBucket1, totalFieldsBucket2, totalFieldsBucket4, totalFieldsBucket4" value="0" />
<c:forEach items="${messageTypeDetails.fields}" var="mappings" varStatus="field">
    <c:if test="${mappings.bucketNo == 1}">
        <c:set var="totalFieldsBucket1" value="${totalFieldsBucket1+1}" />
    </c:if>
    <c:if test="${mappings.bucketNo == 2}">
        <c:set var="totalFieldsBucket2" value="${totalFieldsBucket2+1}" />
    </c:if>
    <c:if test="${mappings.bucketNo == 3}">
        <c:set var="totalFieldsBucket3" value="${totalFieldsBucket3+1}" />
    </c:if>
    <c:if test="${mappings.bucketNo == 4}">
        <c:set var="totalFieldsBucket4" value="${totalFieldsBucket4+1}" />
    </c:if>
</c:forEach>

<div class="main clearfix" role="main">

    <div class="col-md-12">

        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose><c:when test="${savedStatus == 'updated'}">The field mappings have been successfully updated!</c:when><c:when test="${savedStatus == 'created'}">The message type has been successfully created!</c:when><c:when test="${savedStatus == 'fieldcreated'}">The new form field has been successfully created!</c:when></c:choose>
            </div>
        </c:if>
        <div id="mappingErrorMsgDiv" class="alert alert-danger" style="display:none;">
            <strong>There is an error with your Field Mappings, scroll to find the highlighted field!</strong>
        </div>

        <form:form id="fieldMappings" modelAttribute="messageTypeDetails" method="post" role="form">
            <c:forEach var="i" begin="1" end="4">	
                <section class="panel panel-default">
                    <div class="panel-heading">
                        <div class="pull-right">
                            <a href="#newFieldModal" data-toggle="modal" class="btn btn-primary btn-xs btn-action" id="addNewField" rel="${i}" title="Add new field to bucket ${i}">Add New Field</a>
                        </div>
                        <h3 class="panel-title"><strong><c:choose><c:when test="${i==1}"> (Sender Information)</c:when><c:when test="${i==2}"> (Recipient Information)</c:when><c:when test="${i==3}"> (Patient Information)</c:when><c:when test="${i==4}"> (Other)</c:when></c:choose></strong></h3>
                            </div>
                            <div class="panel-body">
                                <div class="form-container scrollable">
                                                <table class="table table-striped table-hover bucketTable_${i}">
                                <thead>
                                    <tr>
                                        <th scope="col" style="width:150px; min-width:150px">Display POS</th>
                                        <th scope="col" style="width:200px; min-width:200px;">Field Name</th>
                                        <th scope="col" style="width:300px; min-width:300px;">Field Label *</th>
                                        <th scope="col" style="width:100px; min-width:100px;" class="center-text">Required</th>
                                        <th scope="col" style="width:150px; min-width:150px;">Validation Type</th>
                                        <th scope="col" style="width:200px; min-width:200px;">Table Name *</th>
                                        <th scope="col" style="width:150px; min-width:150px;">Table Column Name *</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${messageTypeDetails.fields}" var="mappings" varStatus="field">
                                        <c:if test="${mappings.bucketNo == i}">
                                            <tr>
                                        <input type="hidden" name="fields[${field.index}].id" value="${mappings.id}" />
                                        <input type="hidden" class="messageTypeId" name="fields[${field.index}].messageTypeId" value="${mappings.messageTypeId}" />
                                        <input type="hidden" class="fieldNo" name="fields[${field.index}].fieldNo" value="${mappings.fieldNo}" />
                                        <input type="hidden" name="fields[${field.index}].bucketNo" value="${mappings.bucketNo}" />
                                        <td scope="row" >
                                            <c:if test="${mappings.bucketNo == 1}">
                                                <c:set var="endDspPostLoop" value="${totalFieldsBucket1}" />
                                            </c:if>
                                            <c:if test="${mappings.bucketNo == 2}">
                                                <c:set var="endDspPostLoop" value="${totalFieldsBucket2}" />
                                            </c:if>
                                            <c:if test="${mappings.bucketNo == 3}">
                                                <c:set var="endDspPostLoop" value="${totalFieldsBucket3}" />
                                            </c:if>
                                            <c:if test="${mappings.bucketNo == 4}">
                                                <c:set var="endDspPostLoop" value="${totalFieldsBucket4}" />
                                            </c:if>
                                            <input type="hidden" id="endDspPostLoop_${mappings.bucketNo}" value="${endDspPostLoop}" />
                                            <select rel="${mappings.bucketNo}" rel2="${mappings.bucketDspPos}" name="fields[${field.index}].bucketDspPos" class="form-control half dspPos dspPos_${mappings.bucketNo}">
                                                <option value="" label=" - Select - " ></option>
                                                <c:forEach begin="1" end="${endDspPostLoop}" var="t">
                                                    <option value="${t}" <c:if test="${mappings.bucketDspPos == t}">selected</c:if>>${t}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="hidden" name="fields[${field.index}].fieldDesc" value="${mappings.fieldDesc}" />
                                            ${mappings.fieldDesc}
                                        </td>
                                        <td>
                                            <div id="fieldLabel_${field.index}" class="form-group ${status.error ? 'has-error' : '' }">
                                                <input type="text" name="fields[${field.index}].fieldLabel"  value="${mappings.fieldLabel}" rel="${field.index}" class="form-control fieldLabel" />
                                                <span id="fieldLabelMsg_${field.index}" class="control-label"></span>
                                            </div>
                                        </td>
                                        <td class="center-text">
                                            <input type="checkbox" name="fields[${field.index}].required" value="true" <c:if test="${mappings.required == true}">checked</c:if>  />
                                            </td>
                                            <td class="validationTypes">
                                                <select name="fields[${field.index}].validationType" class="form-control half">
                                                <option value="0" label=" - Select - " ></option>
                                                <c:forEach items="${validationTypes}"  var="fieldvalidationtypes" varStatus="vtype">
                                                    <option value="${validationTypes[vtype.index][0]}" <c:if test="${mappings.validationType == validationTypes[vtype.index][0]}">selected</c:if>>${validationTypes[vtype.index][1]}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="tableNames">
                                            <div id="tableName_${field.index}" class="form-group ${status.error ? 'has-error' : '' }">
                                                <select name="fields[${field.index}].saveToTableName" rel="${field.index}" class="form-control half tableName">
                                                    <option value="" label=" - Select - " ></option>
                                                    <c:forEach items="${infoTables}"  var="infotablenames" varStatus="tname">
                                                        <option value="${infoTables[tname.index]}" <c:if test="${mappings.saveToTableName == infoTables[tname.index]}">selected</c:if>>${infoTables[tname.index]}</option>
                                                    </c:forEach>
                                                </select><br />
                                                <span id="tableNameMsg_${field.index}" class="control-label"></span>
                                            </div>
                                        </td>
                                        <td class="tableCols">
                                            <div id="tableCol_${field.index}" class="form-group ${status.error ? 'has-error' : '' }">
                                                <select id="tableCols${field.index}" name="fields[${field.index}].saveToTableCol" rel="${field.index}" rel2="${mappings.saveToTableCol}" class="form-control half tableCol">
                                                    <option value="" label=" - Select - " ></option>
                                                </select><br />
                                                <span id="tableColMsg_${field.index}" class="control-label" ></span>
                                            </div>
                                        </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </section>
            </c:forEach>
        </form:form>
    </div>
</div>

<!-- Brochure Form modal -->
<div class="modal fade" id="newFieldModal" role="dialog" tabindex="-1" aria-labeledby="New Field" aria-hidden="true" aria-describedby="New Field"></div>
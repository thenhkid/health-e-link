<%-- 
    Document   : ccdElement
    Created on : Jan 7, 2015, 1:20:23 PM
    Author     : chadmccue
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="currentBucket" value="0" />
<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 class="panel-title">Add New CCD Element</h3>
        </div>
        <div class="modal-body">
            <form:form id="ccdElementForm" commandName="ccdElement" modelAttribute="ccdElement"  method="post" role="form">
                <form:hidden path="id"  />
                <form:hidden path="configId" />
                <div class="form-container">
                    <spring:bind path="element">
                        <div id="elementDiv" class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="element">Element *</label>
                            <form:input path="element" id="element" class="form-control" type="text" maxLength="50" />
                            <span id="elementMsg" class="control-label"></span>
                        </div>
                    </spring:bind>
                    <spring:bind path="defaultValue">
                        <div id="fieldAppendDiv" class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="defaultValue">Default Value</label>
                            <form:input path="defaultValue" id="defaultValue" class="form-control" type="text" maxLength="50" />
                        </div>
                    </spring:bind>
                    <spring:bind path="fieldValue">
                        <div id="fieldValueDiv" class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="fieldValue">Field Value</label>
                            <form:select path="fieldValue" id="newFieldValue" class="form-control half">
                                <option value="">- Select -</option>
                                <c:forEach items="${fields}" var="field" varStatus="fStatus">
                                    <c:if test="${currentBucket != fields[fStatus.index].bucketNo}">
                                        <c:if test="${currentBucket > 0}"></optgroup></c:if>
                                        <c:set var="currentBucket" value="${fields[fStatus.index].bucketNo}" />
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
                                                <option value="${fields[fStatus.index].fieldNo}" <c:if test="${ccdElement.fieldValue == fields[fStatus.index].fieldNo}">selected</c:if>>${fields[fStatus.index].fieldLabel} </option>
                                </c:forEach>
                            </form:select>
                            <span id="fieldValueMsg" class="control-label"></span>
                        </div>
                    </spring:bind>
                    <div class="form-group">
                        <input type="button" id="submitElementButton" role="button" class="btn btn-primary" value="Save"/>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        $("input:text,form").attr("autocomplete", "off");
    });
</script>


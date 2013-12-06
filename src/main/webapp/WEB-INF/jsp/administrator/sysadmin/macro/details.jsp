<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 class="panel-title"><c:choose><c:when test="${btnValue == 'Update'}">Update</c:when><c:when test="${btnValue == 'Create'}">Add</c:when></c:choose> Macro ${success}</h3>
                </div>
                <div class="modal-body">
            <form:form id="macroform" commandName="Macros" modelAttribute="macroDetails"  method="post" role="form">
                <form:hidden path="id" id="id" />
                
                <div class="form-container">
                        <spring:bind path="category">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="category">Category</label>
                                <form:input path="category" id="category" class="form-control" type="text" maxLength="45" />
                                <form:errors path="category" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                         <spring:bind path="macroShortName">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="macroShortName">Macro Short Name*</label>
                                <form:input path="macroShortName" id="macroShortName" class="form-control" type="text" maxLength="50" />
                                <form:errors path="macroShortName" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="macroName">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="macroName">Macro Name*</label>
                                <form:input path="macroName" id="macroName" class="form-control" type="text" maxLength="100" />
                                <form:errors path="macroName" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="refNumber">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="refNumber">Reference Number</label>
                                <form:input path="refNumber" id="refNumber" class="form-control" type="text" maxLength="9" />
                                <form:errors path="refNumber" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="dateDisplay">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="dateDisplay">Date Display</label>
                                <form:input path="dateDisplay" id="dateDisplay" class="form-control" type="text" maxLength="10" />
                                <form:errors path="dateDisplay" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="formula">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="formula">Formula</label>
                                <form:input path="formula" id="formula" class="form-control" type="text" maxLength="255" />
                                <form:errors path="formula" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>                       
                        <spring:bind path="invalidWhen">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="invalidWhen">Invalid When</label>
                                <form:input path="invalidWhen" id="invalidWhen" class="form-control" type="text" maxLength="255" />
                                <form:errors path="invalidWhen" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="fieldAQuestion">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="fieldAQuestion">Field A Question</label>
                                <form:input path="fieldAQuestion" id="fieldAQuestion" class="form-control" type="text" maxLength="255" />
                                <form:errors path="fieldAQuestion" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="fieldBQuestion">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="fieldBQuestion">Field B Question</label>
                                <form:input path="fieldBQuestion" id="fieldBQuestion" class="form-control" type="text" maxLength="255" />
                                <form:errors path="fieldBQuestion" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>      
                        <spring:bind path="con1Question">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="con1Question">Constant 1 Question</label>
                                <form:input path="con1Question" id="con1Question" class="form-control" type="text" maxLength="255" />
                                <form:errors path="con1Question" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="con2Question">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="con2Question">Constant 2 Question</label>
                                <form:input path="con2Question" id="con2Question" class="form-control" type="text" maxLength="255" />
                                <form:errors path="con2Question" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>                                                                                                             
                        
                    <div class="form-group">
                        <input type="button" id="submitButton" rel="${btnValue}" class="btn btn-primary" value="${btnValue}"/>
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

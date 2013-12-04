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
                                <label class="control-label" for="macroShortName">Macro Short Name</label>
                                <form:input path="macroShortName" id="macroShortName" class="form-control" type="text" maxLength="45" />
                                <form:errors path="macroShortName" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="macroName">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="macroName">Macro Name</label>
                                <form:input path="macroName" id="macroName" class="form-control" type="text" maxLength="255" />
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

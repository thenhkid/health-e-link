<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 class="panel-title"><c:choose><c:when test="${btnValue == 'Update'}">Update</c:when><c:when test="${btnValue == 'Create'}">Add</c:when></c:choose> Look Up Data ${success}</h3>
                </div>
                <div class="modal-body">
            <form:form id="tabledataform" commandName="tableData" modelAttribute="tableDataDetails"  method="post" role="form">
                <form:hidden path="id" id="id" />
                <form:hidden path="urlId" id="urlId"/>
                <div class="form-container">
                    <div class="form-group">
                        <label for="status">Status *</label>
                        <div>
                            <label class="radio-inline">
                                <form:radiobutton id="status" path="status" value="true" /> Active
                            </label>
                            <label class="radio-inline">
                                <form:radiobutton id="status" path="status" value="false" /> Inactive
                            </label>
                        </div>
                    </div>
                   <div class="form-group">
                            <label for="custom">Viewable *</label>
                            <div>
                                <label class="radio-inline">
                                    <form:radiobutton id="custom" path="custom" value="false"/>Public
                                </label>
                                <label class="radio-inline">
                                    <form:radiobutton id="custom" path="custom" value="true"/>Private 
                                </label>
                            </div>
                        </div>
                        <spring:bind path="displayText">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="displayText">Display Text *</label>
                                <form:input path="displayText" id="displayText" class="form-control" type="text" maxLength="45" />
                                <form:errors path="displayText" cssClass="control-label" element="label" />
                            </div>
                        </spring:bind>
                        <spring:bind path="description">
                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                <label class="control-label" for="description">Description</label>
                                <form:input path="description" id="description" class="form-control" type="text" maxLength="255" />
                                <form:errors path="description" cssClass="control-label" element="label" />
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

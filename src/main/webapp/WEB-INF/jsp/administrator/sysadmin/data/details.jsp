<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="main clearfix" role="main">

    <div class="col-md-12">

        <c:if test="${not empty savedStatus}" >
            <div class="alert alert-success">
                <strong>Success!</strong> 
                <c:choose><c:when test="${savedStatus == 'updated'}">The data has been successfully updated!</c:when><c:otherwise>The organization has been successfully created!</c:otherwise></c:choose>
                    </div>
        </c:if>

        <form:form id="tableDataForm" commandName="tableData" modelAttribute="tableDataDetails"  method="post" role="form">
            <input type="hidden" id="action" name="action" value="save" />	
            <section class="panel panel-default">

                <div class="panel-heading">
                    <h3 class="panel-title"><c:if test="${tableDataDetails.id == 0}">Add </c:if>Data for "${tableInfo.displayName}" Table</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-container">
                            <div class="form-group">
                                <label for="status">Status *</label>
                                <div>
                                    <label class="radio-inline">
                                    <form:radiobutton id="status" path="status" value="true"/>Active 
                                </label>
                                <label class="radio-inline">
                                    <form:radiobutton id="status" path="status" value="false"/>Inactive
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
                    </div>
                </div>
            </section>
        </form:form>
    </div>
</div>

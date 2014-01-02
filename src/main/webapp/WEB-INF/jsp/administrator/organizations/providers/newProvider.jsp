<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 class="panel-title">Add Provider ${success}</h3>
         </div>
         <div class="modal-body">
             <form:form id="providerdetailsform" commandName="providerdetails" modelAttribute="providerdetails"  method="post" role="form">
                <input type="hidden" id="action" name="action" value="save" />
                <form:hidden path="id" id="id" />
                <form:hidden path="orgId" id="orgId" />
                <form:hidden path="dateCreated" />
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
                    <spring:bind path="gender">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="gender">Gender *</label>
                            <form:select path="gender" id="gender" class="form-control half">
                                <form:option value="">- Choose Gender -</form:option>
                                <form:option value="Female">Female</form:option>
                                <form:option value="Male">Male</form:option>
                            </form:select>
                            <form:errors path="gender" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <spring:bind path="firstName">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="firstName">First Name *</label>
                            <form:input path="firstName" id="firstName" class="form-control" type="text" maxLength="45" />
                            <form:errors path="firstName" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <spring:bind path="lastName">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="lastName">Last Name *</label>
                            <form:input path="lastName" id="lastName" class="form-control" type="text" maxLength="45" />
                            <form:errors path="lastName" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <spring:bind path="email">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="email">Email</label>
                            <form:input path="email" id="email" class="form-control" type="text"  maxLength="255" />
                            <form:errors path="email" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <spring:bind path="phone1">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="phone1">Main Phone *</label>
                            <form:input path="phone1" id="phone1" class="form-control sm-input" type="text" maxLength="20" />
                            <form:errors path="phone1" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <spring:bind path="phone2">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="phone2">Cell</label>
                            <form:input path="phone2" id="phone2" class="form-control sm-input" type="text" maxLength="20" />
                            <form:errors path="phone2" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <spring:bind path="fax">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="fax">Fax</label>
                            <form:input path="fax" id="fax" class="form-control sm-input" type="text" maxLength="20" />
                            <form:errors path="fax" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <spring:bind path="specialty">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="specialty">Specialty</label>
                            <form:input path="specialty" id="specialty" class="form-control" type="text" maxLength="100" />
                            <form:errors path="specialty" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <spring:bind path="website">
                        <div class="form-group ${status.error ? 'has-error' : '' }">
                            <label class="control-label" for="website">URL</label>
                            <form:input path="website" id="website" class="form-control" type="text" maxLength="255" />
                            <form:errors path="website" cssClass="control-label" element="label" />
                        </div>
                    </spring:bind>
                    <div class="form-group">
                        <input type="button" id="submitButton" rel="Create" role="button" class="btn btn-primary" value="Create"/>
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

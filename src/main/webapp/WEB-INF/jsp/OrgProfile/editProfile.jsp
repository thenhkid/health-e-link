<%-- 
    Document   : messageDetailsForm
    Created on : Dec 12, 2013, 2:47:34 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li class="active">
                    Edit Organization Profile
                </li>
            </ol>
                
            <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    Your organization profile has been successfully updated!
                </div>
            </c:if> 
            
            <h2 class="form-title">Edit Organization Profile</h2>
            
            <form:form id="orgProfileForm"  commandName="organization" role="form" class="form" method="post">
                <form:input type="hidden" path="cleanURL" />
                <form:input type="hidden" path="id" />
                <div class="panel-group form-accordion" id="accordion">

                    <div class="panel panel-default panel-form">
                        <div id="collapseThree" class="panel-collapse collapse in">
                            <div class="panel-body">
                                
                                <div class="form-section row">
                                    <div class="col-md-6 cb">
                                        <div class="form-group">
                                            <label for="publicOrg">Viewable *</label>
                                            <div>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="publicOrg" path="publicOrg" value="true"/>Public
                                                </label>
                                                <label class="radio-inline">
                                                    <form:radiobutton id="publicOrg" path="publicOrg" value="false"/>Private 
                                                </label>
                                            </div>
                                        </div>
                                    </div> 
                                    <div class="col-md-6 cb">
                                        <spring:bind path="orgName">
                                            <div class="form-group ${status.error ? 'has-error' : '' } ${not empty existingOrg ? 'has-error' : ''}">
                                                <label class="control-label" for="orgName">Name *</label>
                                                <form:input path="orgName" id="orgName" class="form-control" type="text" maxLength="255" />
                                                <form:errors path="orgName" cssClass="control-label" element="label" />
                                                <c:if test="${not empty existingOrg}">${existingOrg}</c:if>
                                            </div>
                                        </spring:bind>
                                    </div>       
                                    <div class="col-md-6">
                                        <spring:bind path="address">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="address">Address *</label>
                                                <form:input path="address" id="address" class="form-control" type="text" maxLength="45" />
                                                <form:errors path="address" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>  
                                     <div class="col-md-6 cb">
                                        <spring:bind path="address2">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="address2">Address 2</label>
                                                <form:input path="address2" id="address2" class="form-control" type="text" maxLength="45" />
                                                <form:errors path="address2" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div> 
                                    <div class="col-md-6">
                                        <spring:bind path="city">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="city">City *</label>
                                                <form:input path="city" id="postalCode" class="form-control xs-input" type="text" maxLength="45" />
                                                <form:errors path="city" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>    
                                     <div class="col-md-6 cb">
                                        <spring:bind path="county">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="county">County</label>
                                                <form:input path="county" id="county" class="form-control" type="text" maxLength="45" />
                                                <form:errors path="county" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div> 
                                    <div class="col-md-6">
                                        <spring:bind path="town">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="town">Town</label>
                                                <form:input path="town" id="town" class="form-control" type="text" maxLength="45" />
                                                <form:errors path="town" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>                
                                    <div class="col-md-6 cb">
                                        <spring:bind path="state">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="state">State *</label>
                                                <form:select id="state" path="state" cssClass="form-control half">
                                                    <option value="" label=" - Select - " ></option>
                                                    <form:options items="${stateList}"/>
                                                </form:select>
                                                <form:errors path="state" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>   
                                    <div class="col-md-6">
                                        <spring:bind path="postalCode">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="postalCode">Postal Code *</label>
                                                <form:input path="postalCode" id="postalCode" class="form-control xs-input" type="text" maxLength="15" />
                                                <form:errors path="postalCode" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>  
                                    <div class="col-md-6 cb">
                                        <spring:bind path="phone">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="phone">Phone *</label>
                                                <form:input path="phone" id="phone" class="form-control sm-input" type="text" maxLength="45" />
                                                <form:errors path="phone" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>       
                                    <div class="col-md-6">
                                        <spring:bind path="fax">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="fax">Fax</label>
                                                <form:input path="fax" id="fax" class="form-control sm-input" type="text" maxLength="45" />
                                                <form:errors path="fax" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>            
                                </div>
                                <input type="button" id="save" class="btn btn-primary btn-action-sm submitMessage" value="Save"/>
                            </div>           
                        </div>
                    </div>                   
                </div>
           </form:form>
        </div>
    </div>
</div>

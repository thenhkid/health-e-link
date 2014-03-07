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
                <li><a href="/OrgProfile/providers">Organization Providers</a></li>
                <li class="active">
                    Create New Provider
                </li>
            </ol>
            
            
            <h2 class="form-title"> Create New Provider</h2>
            
            <form:form id="providerForm"  commandName="providerdetails" modelAttribute="providerdetails" role="form" class="form" method="post">
                <form:hidden path="orgId" id="orgId" />
                <form:hidden path="dateCreated" />
                <div class="panel-group form-accordion" id="accordion">

                    <div class="panel panel-default panel-form">
                        <div id="collapseThree" class="panel-collapse collapse in">
                            <div class="panel-body">
                                
                                <div class="form-section row">
                                    <div class="col-md-6 cb">
                                        <spring:bind path="firstName">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="firstName">First Name *</label>
                                                <form:input path="firstName" id="firstName" class="form-control" type="text" maxLength="45" />
                                                <form:errors path="firstName" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>       
                                    <div class="col-md-6">
                                        <spring:bind path="lastName">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="lastName">Last Name *</label>
                                                <form:input path="lastName" id="lastName" class="form-control" type="text" maxLength="45" />
                                                <form:errors path="lastName" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>    
                                    <div class="col-md-6 cb">
                                        <spring:bind path="email">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="email">Email</label>
                                                <form:input path="email" id="email" class="form-control" type="text"  maxLength="255" />
                                                <form:errors path="email" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>       
                                    <div class="col-md-6">
                                        <spring:bind path="phone1">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="phone1">Main Phone *</label>
                                                <form:input path="phone1" id="phone1" class="form-control sm-input" type="text" maxLength="20" />
                                                <form:errors path="phone1" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>  
                                    <div class="col-md-6 cb">
                                        <spring:bind path="phone2">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="phone2">Cell</label>
                                                <form:input path="phone2" id="phone2" class="form-control sm-input" type="text" maxLength="20" />
                                                <form:errors path="phone2" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>       
                                    <div class="col-md-6">
                                        <spring:bind path="fax">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="fax">Fax</label>
                                                <form:input path="fax" id="fax" class="form-control sm-input" type="text" maxLength="20" />
                                                <form:errors path="fax" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>  
                                    <div class="col-md-6 cb">
                                        <spring:bind path="specialty">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="specialty">Specialty</label>
                                                <form:input path="specialty" id="specialty" class="form-control" type="text" maxLength="100" />
                                                <form:errors path="specialty" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>       
                                    <div class="col-md-6">
                                        <spring:bind path="website">
                                            <div class="form-group ${status.error ? 'has-error' : '' }">
                                                <label class="control-label" for="website">URL</label>
                                                <form:input path="website" id="website" class="form-control" type="text" maxLength="255" />
                                                <form:errors path="website" cssClass="control-label" element="label" />
                                            </div>
                                        </spring:bind>
                                    </div>  
                                    
                                    <div class="form-group">
                                        <input type="button" id="save" class="btn btn-primary btn-action submitMessage" value="Save"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                    </div>                   
                </div>
                
           </form:form>
        </div>
    </div>
</div>

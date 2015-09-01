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
                    ${providerdetails.firstName}&nbsp;${providerdetails.lastName}
                </li>
            </ol>
            
            <c:choose>
                <c:when test="${not empty savedStatus}" >
                    <div class="alert alert-success" role="alert">
                        <strong>Success!</strong> 
                        <c:choose>
                            <c:when test="${savedStatus == 'updated'}">The provider has been successfully updated!</c:when>
                            <c:when test="${savedStatus == 'created'}">The provider has been successfully created!</c:when>
                        </c:choose>
                    </div>
                </c:when> 
                <c:when test="${not empty param.msg}" >
                    <div class="alert alert-success" role="alert">
                        <strong>Success!</strong> 
                        <c:choose>
                            <c:when test="${param.msg == 'updated'}">The address has been successfully updated!</c:when>
                            <c:when test="${param.msg == 'created'}">The address has been successfully added!</c:when>
                            <c:when test="${param.msg == 'deleted'}">The address has been successfully removed!</c:when>
                            <c:when test="${param.msg == 'idupdated'}">The provider Id has been successfully updated!</c:when>
                            <c:when test="${param.msg == 'idcreated'}">The provider Id has been successfully added!</c:when>
                            <c:when test="${param.msg == 'iddeleted'}">The provider Id has been successfully removed!</c:when>
                        </c:choose>
                    </div>
                </c:when>
            </c:choose>
            
            
            <h2 class="form-title">Edit Provider Profile</h2>
            
            <form:form id="providerForm"  commandName="providerdetails" modelAttribute="providerdetails" role="form" class="form" method="post">
                <form:hidden path="id" id="id" />
                <form:hidden path="orgId" id="orgId" />
                <form:hidden path="dateCreated" />
                <div class="panel-group form-accordion" id="accordion">

                    <div class="panel panel-default panel-form">
                        <div id="collapseThree" class="panel-collapse collapse in">
                            <div class="panel-body">
                                
                                <div class="form-section row">
                                    <div class="col-md-6 cb">
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
                                    </div>  
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
                                    
                                </div>
                                
                                <input type="button" id="save" class="btn btn-primary btn-action-sm submitMessage" value="Save"/>
                            </div>
                        </div>
                        
                    </div>                   
                </div>
                
                <%-- Existing Provider Addresses --%>
                <div class="col-md-12 form-section">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Provider Addresses:</h3>
                        </div>
                        <div class="panel-body">
                            <div class="form-container scrollable">
                                 <a href="#systemAddressModal" id="createNewAddress" data-toggle="modal" class="btn btn-primary btn-xs pull-right" title="Create a new address">
                                    <span class="glyphicon glyphicon-plus"></span>
                                </a>
                                <table class="table table-striped table-hover responsive">
                                    <caption style="display:none">Provider Addresses</caption>
                                    <thead>
                                        <tr>
                                            <th scope="col">Address Info</th>
                                            <th scope="col">Contact Info</th>
                                            <th scope="col" class="center-text"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${providerdetails.providerAddresses.size() > 0}">
                                                <c:forEach items="${providerdetails.providerAddresses}" var="address" varStatus="pStatus">
                                                    <tr>
                                                        <td scope="row">
                                                            <c:if test="${not empty providerdetails.providerAddresses[pStatus.index].type}"><span style="font-style:italic">${providerdetails.providerAddresses[pStatus.index].type}</span><br /></c:if>
                                                            ${providerdetails.providerAddresses[pStatus.index].line1} 
                                                            <c:if test="${not empty providerdetails.providerAddresses[pStatus.index].line2}"><br />${providerdetails.providerAddresses[pStatus.index].line2}</c:if>
                                                                <br />
                                                            ${providerdetails.providerAddresses[pStatus.index].city} ${providerdetails.providerAddresses[pStatus.index].state} ${providerdetails.providerAddresses[pStatus.index].postalCode}
                                                        </td>
                                                        <td>
                                                            (o) ${providerdetails.providerAddresses[pStatus.index].phone1}
                                                            <c:if test="${not empty providerdetails.providerAddresses[pStatus.index].phone2}"><br />(toll free) ${providerdetails.providerAddresses[pStatus.index].phone2}</c:if>
                                                            <c:if test="${not empty providerdetails.providerAddresses[pStatus.index].fax}"><br />(f) ${providerdetails.providerAddresses[pStatus.index].fax}</c:if>
                                                        </td>
                                                        <td>
                                                            <div class="pull-right">
                                                                <a href="#systemAddressModal" data-toggle="modal" class="btn btn-link editAddress" rel="address?i=${providerdetails.providerAddresses[pStatus.index].id}" title="Edit this address">
                                                                    <span class="glyphicon glyphicon-edit"></span>
                                                                    Edit
                                                                </a>
                                                                <a href="javascript:void(0);"  class="btn btn-link deleteAddress" rel="${providerdetails.providerAddresses[pStatus.index].id}" title="Delete this address">
                                                                    <span class="glyphicon glyphicon-remove"></span>
                                                                    Delete
                                                                </a>  
                                                            </div>
                                                       </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise><tr><td scope="row" colspan="3" style="text-align:center">No Addresses Found</td></c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </section>
                </div> 
                
                
                <%-- Existing Provider Ids --%>
                <div class="col-md-12 form-section">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Provider Ids</h3>
                        </div>
                        <div class="panel-body">
                            <div class="form-container scrollable">
                                 <a href="#providerIdModal" id="createNewId" data-toggle="modal" class="btn btn-primary btn-xs pull-right" title="Create a new provider Id">
                                    <span class="glyphicon glyphicon-plus"></span>
                                </a>    
                                <table class="table table-striped table-hover responsive">
                                    <caption style="display:none">Provider Ids</caption>
                                    <thead>
                                        <tr>
                                            <th scope="col">ID Number</th>
                                            <th scope="col">Type</th>
                                            <th scope="col">Issued By</th>
                                            <th scope="col"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${providerdetails.providerIds.size() > 0}">
                                                <c:forEach items="${providerdetails.providerIds}" var="id" varStatus="pStatus">
                                                    <tr>
                                                        <td scope="row">
                                                            ${providerdetails.providerIds[pStatus.index].idNum} 
                                                        </td>
                                                        <td>
                                                            ${providerdetails.providerIds[pStatus.index].type}
                                                        </td>
                                                        <td>
                                                            ${providerdetails.providerIds[pStatus.index].issuedBy}
                                                        </td>
                                                        <td>
                                                            <div class="pull-right">
                                                                <a href="#providerIdModal" data-toggle="modal" class="btn btn-link editId" rel="address?i=${providerdetails.providerIds[pStatus.index].id}" title="Edit this provider Id">
                                                                    <span class="glyphicon glyphicon-edit"></span>
                                                                    Edit
                                                                </a>
                                                                <a href="javascript:void(0);"  class="btn btn-link deleteId" rel="${providerdetails.providerIds[pStatus.index].id}" title="Delete this provider Id">
                                                                    <span class="glyphicon glyphicon-remove"></span>
                                                                    Delete
                                                                </a>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise><tr><td scope="row" colspan="4" style="text-align:center">No provider Ids Found</td></c:otherwise>
                                            </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </section>
                </div> 
                
           </form:form>
        </div>
    </div>
</div>

<!-- Provider Address modal -->
<div class="modal fade" id="systemAddressModal" role="dialog" tabindex="-1" aria-labeledby="Provider Address" aria-hidden="true" aria-describedby="Provider Address"></div>

<!-- Provider Id modal -->
<div class="modal fade" id="providerIdModal" role="dialog" tabindex="-1" aria-labeledby="Provider Id" aria-hidden="true" aria-describedby="Provider Id"></div>

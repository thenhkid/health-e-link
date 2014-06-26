<%-- 
    Document   : orgDetails
    Created on : Mar 10, 2014, 10:52:19 AM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 



<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li><a href="/associations/">Associated Organizations</a></li>
                <li class="active">
                    ${orgDetails.orgName}
                </li>
            </ol>
                
            <div class="col-md-12">
                <section class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Organization Details:</h3>
                    </div>
                     <div class="panel-body">
                        <div class="row">
                            <div class="col-md-6 cb">
                                <div class="form-group">
                                    <label class="control-label" for="orgName">Name</label>
                                    <input disabled value="${orgDetails.orgName}" id="orgName" class="form-control" type="text" maxLength="255" />
                                </div>
                            </div>  
                             <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label" for="address">Address</label>
                                    <input disabled value="${orgDetails.address}" id="address" class="form-control" type="text" maxLength="45" />
                                </div>
                            </div> 
                           <div class="col-md-6 cb">
                                <div class="form-group">
                                    <label class="control-label" for="address2">Address 2</label>
                                    <input disabled value="${orgDetails.address2}" id="address2" class="form-control" type="text" maxLength="45" /> 
                                </div>
                            </div>   
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label" for="city">City</label>
                                    <input disabled value="${orgDetails.city}" id="postalCode" class="form-control xs-input" type="text" maxLength="45" />
                                </div>
                            </div>      
                            <div class="col-md-6 cb">
                                <div class="form-group">
                                    <label class="control-label" for="state">State</label>
                                    <select disabled id="state" class="form-control half">
                                        <option value="${orgDetails.state}" >${orgDetails.state}</option>
                                    </select>
                                </div>
                            </div>   
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label" for="postalCode">Postal Code</label>
                                    <input disabled value="${orgDetails.postalCode}" id="postalCode" class="form-control xs-input" type="text" maxLength="15" />
                                </div>
                            </div>  
                            <div class="col-md-6 cb">
                                <div class="form-group">
                                    <label class="control-label" for="phone">Phone</label>
                                    <input disabled value="${orgDetails.phone}" id="phone" class="form-control sm-input" type="text" maxLength="45" />
                                </div>
                            </div>       
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="control-label" for="fax">Fax</label>
                                    <input disabled value="${orgDetails.fax}" id="fax" class="form-control sm-input" type="text" maxLength="45" />
                                </div>
                            </div>            
                        </div>
                    </div>
                </section>
            </div>    
                                
            <div class="col-md-12">
                <section class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Organization Primary Contacts:</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-striped table-hover table-default">
                            <caption style="display:none">Organization Primary Contacts</caption>
                            <thead>
                                <tr>
                                    <th scope="col">Name</th>
                                    <th scope="col">Contact Information</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="mContact" items="${mainContacts}">
                                    <tr>
                                        <td scope="row">${mContact.firstName}&nbsp;${mContact.lastName}</td>
                                        <td>
                                            <dd class="adr">
                                                <span class="street-address">${mContact.email}</span>
                                            </dd>
                                        </td>
                                    </tr>
                                </c:forEach>      
                            </tbody>
                        </table>
                    </div>
                </section>
            </div>  
                                
            <c:if test="${not empty secondaryContacts}">
                <div class="col-md-12">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Organization Secondary Contacts:</h3>
                        </div>
                        <div class="panel-body">
                            <table class="table table-striped table-hover table-default">
                                <caption style="display:none">Organization Secondary Contacts</caption>
                                <thead>
                                    <tr>
                                        <th scope="col">Name</th>
                                        <th scope="col">Contact Information</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="sContact" items="${secondaryContacts}">
                                        <tr>
                                            <td scope="row">${sContact.firstName}&nbsp;${sContact.lastName}</td>
                                            <td>
                                                <dd class="adr">
                                                    <span class="street-address">${sContact.email}</span>
                                                </dd>
                                            </td>
                                        </tr>
                                    </c:forEach>      
                                </tbody>
                            </table>
                        </div>
                    </section>
                </div> 
            </c:if>
            
            <c:if test="${not empty providers}">
                <div class="col-md-12">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Organization Providers:</h3>
                        </div>
                        <div class="panel-body">
                            <c:forEach var="provider" varStatus="pStatus" items="${providers}">
                            <section class="panel panel-default col-md-5 ${(pStatus.index mod 2) == 0 ? 'cb' : 'pull-right'} providerBody">
                               <div class="panel-body">
                                    <div class="form-container scrollable">
                                       <div class="col-md-5" style="margin-right:30px;" >
                                            <div class="form-group">
                                                <h2 class="form-title">${provider.firstName}&nbsp;${provider.lastName}</h2>
                                                <c:if test="${provider.providerAddresses.size() > 0}">
                                                    <dd class="adr">
                                                        <span class="street-address">${provider.providerAddresses.get(0).line1}</span><br/>
                                                        <c:if test="${not empty provider.providerAddresses.get(0).line2 and provider.providerAddresses.get(0).line2 != 'null'}"><span class="street-address">${provider.providerAddresses.get(0).line2}</span><br/></c:if>
                                                        <span class="region">${provider.providerAddresses.get(0).city}&nbsp;${provider.providerAddresses.get(0).state}</span>, <span class="postal-code">${provider.providerAddresses.get(0).postalCode}</span>
                                                        <br /><br />
                                                        <c:choose><c:when test="${not empty provider.email}">${provider.email}</c:when><c:otherwise>&nbsp;</c:otherwise></c:choose>
                                                    </dd>
                                                </c:if>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                  <h2 class="form-title-sm">ID: <c:choose><c:when test="${provider.providerIds.size() > 0 && not empty provider.providerIds.get(0).idNum}">${provider.providerIds.get(0).idNum}</c:when><c:otherwise>N/A</c:otherwise></c:choose></h2>
                                                  <c:if test="${provider.providerAddresses.size() > 0}">
                                                    <dd class="adr">
                                                        <span>o: ${provider.providerAddresses.get(0).phone1}</span><br/>
                                                        <c:if test="${not empty provider.providerAddresses.get(0).phone2}"><span>o2: ${provider.providerAddresses.get(0).phone2}</span><br/></c:if>
                                                        <c:if test="${not empty provider.providerAddresses.get(0).fax}"><span>f: ${provider.providerAddresses.get(0).fax}</span></c:if>
                                                    </dd>
                                                  </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </c:forEach>
                        </div>
                    </section>
                </div>     
            </c:if>    
            
        </div>
    </div>
</div>
                

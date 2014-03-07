<%-- 
    Document   : providers
    Created on : Mar 7, 2014, 12:51:03 PM
    Author     : chadmccue
--%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li class="active">
                   Organization Providers
                </li>
            </ol>
                
            <c:if test="${not empty error}" >
                <div class="alert alert-danger" role="alert">
                    <strong>Error!</strong> 
                    The provider you selected was not found in your organization.
                </div>
            </c:if>  
           <c:if test="${not empty savedStatus}" >
                <div class="alert alert-success" role="alert">
                    <strong>Success!</strong> 
                    <c:choose>
                        <c:when test="${savedStatus == 'deleted'}">The provider has been successfully removed!</c:when>
                    </c:choose>
                </div>
            </c:if>     
            
            <h2 class="form-title">Organization Providers</h2>
            <div class="pull-left">
                <a href="/OrgProfile/providers/createProvider" class="btn btn-primary btn-xs " title="Create a new provider.">Create New Provider</a>
                <br /><br /> 
            </div>
                
            <div class="col-md-12">
                <c:forEach var="provider" varStatus="pStatus" items="${providers}">
                    <section class="panel panel-default col-md-5 ${(pStatus.index mod 2) == 0 ? 'cb' : ''} providerBody" style="margin-right:35px;">
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
                            <div><hr /></div>
                            <div>
                                <a href="/OrgProfile/providers/${provider.id}" class="btn btn-primary btn-xs" title="Edit this provider.">Edit</a>
                                <div class="pull-right">
                                    <a href="javascript:void(0);" class="btn btn-primary btn-xs providerDelete" rel="${provider.id}" title="Delete this provider.">Delete</a>
                                </div>
                            </div>
                        </div>
                    </section>
                </c:forEach>
            </div>
            
        </div>
    </div>
</div>

<%-- 
    Document   : create
    Created on : Dec 12, 2013, 1:12:54 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li class="active">
                   Associated Organizations
                </li>
            </ol>
                
           <form id="orgDetails" method="post">
               <input type="hidden" name="orgId" id="orgId" value="0" />
           </form>
           <div class="row" style="overflow:hidden; margin-bottom:10px;">
              
               <div class="col-md-3">
                    <form:form class="form form-inline" id="searchForm" action="/organizations/associations" method="post">
                        <div class="form-group">
                            <label class="sr-only" for="searchTerm">Search</label>
                            <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" placeholder="Search"/>
                            <input type="hidden" name="page" id="page" value="${currentPage}" />
                        </div>
                        <button id="searchBatchesBtn" class="btn btn-primary btn-sm" title="Search Inbox">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </form:form>
                </div>
            </div>   
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <thead>
                        <tr>
                            <th scope="col">Organization</th>
                            <th scope="col">Contact Information</th>
                            <th scope="col" class="center-text"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty organizations}">
                                <c:forEach var="org" items="${organizations}">
                                    <tr>
                                        <td scope="row">${org.orgName}</td>
                                        <td>
                                            <dd class="adr">
                                                <span class="street-address">${org.address}</span><br/>
                                                <c:if test="${not empty org.address2 and org.address2 != 'null'}"><span class="street-address">${org.address2}</span><br/></c:if>
                                                <span class="region">${org.city}&nbsp;${org.state}</span>, <span class="postal-code">${org.postalCode}</span>
                                                <c:if test="${not empty org.phone and org.phone != 'null'}"><br /><span class="street-address">(p) ${org.phone}</span><br/></c:if>
                                                <c:if test="${not empty org.fax and org.fax != 'null'}"><br /><span class="street-address">(f) ${org.fax}</span><br/></c:if>
                                            </dd>
                                        </td>
                                        <td class="actions-col" style="width:50px;">
                                           <a href="javascript:void(0);" rel="${org.id}" rel2="${org.orgName}" class="btn btn-link viewLink">
                                            <span class="glyphicon glyphicon-edit"></span>
                                            View
                                          </a>
                                      </td>
                                    </tr>
                                </c:forEach>
                           </c:when>
                           <c:otherwise>
                                <tr><td colspan="3" class="center-text">Your organization is currently not associated to any other organization.</td></tr>
                            </c:otherwise>
                      </c:choose>                  
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

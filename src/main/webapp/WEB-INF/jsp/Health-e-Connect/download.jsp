<%-- 
    Document   : download
    Created on : Feb 10, 2:28:43 PM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container main-container" role="main">
    <div class="row">
        
        <div class="col-md-12 page-content">
            
            <div class="" style="overflow:hidden; margin-bottom:10px;">
            <form:form class="form form-inline" id="searchForm" action="/Health-e-Web/inbox" method="post">
                 <div class="form-group">
                     <label class="sr-only" for="searchTerm">Search</label>
                     <input type="text" name="searchTerm" id="searchTerm" value="${searchTerm}" class="form-control" placeholder="Search"/>
                 </div>
                 <button id="searchBatchesBtn" class="btn btn-primary btn-sm" title="Search Inbox">
                     <span class="glyphicon glyphicon-search"></span>
                 </button>
             </form:form>
         </div>   
            
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <thead>
                        <tr>
                            <th scope="col">Batch Name</th>
                            <th scope="col">File Name</th>
                            <th scope="col" class="center-text">Status</th>
                            <th scope="col" class="center-text">Date Created</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty downloadableBatches}">
                                <c:forEach var="batch" items="${downloadableBatches}">
                                    <tr>
                                        <td scope="row">${batch.utBatchName}</td>
                                        <td>
                                            ${batch.outputFIleName}
                                        </td>
                                        <td class="center-text" id="statusDiv${batch.id}">
                                            <a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="${batch.statusId}" title="View this Status">${batch.statusValue}&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a>
                                        </td>
                                        <td class="center-text"><fmt:formatDate value="${batch.dateCreated}" type="date" pattern="M/dd/yyyy" /></td>
                                        <td class="actions-col" style="width:100px;">
                                             <a href="javascript:void(0);" rel="${batch.id}" rel1="${batch.outputFIleName}" rel2="${batch.orgId}" class="downloadFile" title="Download">
                                                <span class="glyphicon glyphicon-edit"></span>
                                                Download	
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="6" class="center-text">There are currently no batches ready to be downloaded</td></tr>
                            </c:otherwise>
                        </c:choose>    
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>
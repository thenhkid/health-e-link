<%-- 
    Document   : ERG
    Created on : 
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

           
            <div class="row" style="overflow:hidden; margin-bottom:10px;">
                    <div class="col-md-3">
                        
                    </div>

                    <div class="col-md-2 col-md-offset-3"></div>

                    <div class="col-md-4">
                        
                    </div>
            </div>
            
            <div class="form-container scrollable">
               		<c:choose>
                            <c:when test="${hasPermission}">
                            ERG form Goes here   
                            ${transactionInId}
                            </c:when>
                            <c:otherwise>
                               You do not have edit to fix permission to this ERG.  Your request has been logged.
                            </c:otherwise>
                        </c:choose>   
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="uploadFile" role="dialog" tabindex="-1" aria-labeledby="Upload New File" aria-hidden="true" aria-describedby="Upload New File"></div>
<div class="modal fade" id="statusModal" role="dialog" tabindex="-1" aria-labeledby="Status Details" aria-hidden="true" aria-describedby="Status Details"></div>
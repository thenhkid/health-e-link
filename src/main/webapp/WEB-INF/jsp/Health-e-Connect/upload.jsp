<%-- 
    Document   : upload
    Created on : Jan 21, 2014, 11:08:43 AM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <c:if test="${not empty errorCodes}" >
                <div class="alert alert-danger">
                    <strong>The last file uploaded failed our validation!</strong> 
                    <br />
                    <c:forEach items="${errorCodes}" var="code">
                        <c:choose>
                            <c:when test="${code == 1}">- The file uploaded was empty.</c:when>
                            <c:when test="${code == 2}">- The file uploaded exceeded the max size.</c:when>
                            <c:when test="${code == 3}">- The file uploaded was not the correct file type associated to your selected message type.</c:when>
                            <c:when test="${code == 4}">- The file uploaded did not contain the correct delimiter.</c:when>
                        </c:choose>
                        <br />
                    </c:forEach>
                </div>
            </c:if>
            <div class="" style="overflow:hidden; margin-bottom:10px;">
                <a href="#uploadFile" title="Upload File" data-toggle="modal" class="pull-right btn btn-primary uploadFile"><span class="glyphicon glyphicon-upload"></span> Upload File</a>
            </div>
            <div class="form-container scrollable">
                <table class="table table-striped table-hover table-default">
                    <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">File Type</th>
                            <th scope="col">Size</th>
                            <th scope="col">Message Type</th>
                            <th scope="col" class="center-text">Date Sent</th>
                            <th scope="col" class="center-text">Status</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty uploadedBatches}"></c:when>
                            <c:otherwise>
                                <tr><td colspan="7" class="center-text">There are currently no messages</td></tr>
                            </c:otherwise>
                        </c:choose>    
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="uploadFile" role="dialog" tabindex="-1" aria-labeledby="Upload New File" aria-hidden="true" aria-describedby="Upload New File"></div>
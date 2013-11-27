<%-- 
    Document   : index
    Created on : Nov 27, 2013, 10:33:49 AM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="product-suite" role="main">
    <div class="page-content container">
        <div class="row">
            <h2>Welcome <c:out value="${pageContext.request.userPrincipal.name}" />!</h2>
        </div>
    </div>
</div>
<%-- 
    Document   : index
    Created on : Nov 27, 2013, 10:33:49 AM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="product-suite" role="main">
    <div class="page-content container">
        <div class="row">
            <div class="col-md-12 product-summary">
                <div class="media">
                    <a href="" title="" class="icon-product icon-product icon-health-e-net pull-left media-object hidden-xs" ></a>
                    <div class="media-body">
                        <h2>Welcome <c:out value="${userDetails.firstName}" />!</h2>
                        <p>
                            CARE CONNECTOR - Your tool for collaborating with community healthcare partners! 
                            <br/>
                            Please select any of the available options below.
                        </p>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="container main-container">
    <div class="row">
        <div class="page-content">
            <div class="col-md-6 cb" style="padding-bottom:10px;">
                <h4><a href="#settingsModal" id="settings" data-toggle="modal" title="Account Settings" class="settings">Account Settings</a></h4>
                <p class="text">
                    Update your personal account settings.
                </p>
            </div>
            <c:set value="1" var="countIndex" scope="page" />
            <c:forEach var="module" items="${userDetails.userAllowedModules}">
                <c:if test="${not empty module.url && module.id != 8 && module.id != 9 && module.id != 11}">
                    <div class="col-md-6 ${(countIndex mod 2) == 0 ? 'cb' : ''}"  style="padding-bottom:10px;">
                        <h4><a href="<c:url value='${module.url}'/>" title="${module.featureName}">${module.featureName}</a></h4>
                        <p class="text">
                            ${module.featureDesc}
                        </p>
                    </div>
                   <c:set value="${countIndex + 1}" var="countIndex" scope="page" />     
                </c:if>
            </c:forEach>
            <div class="col-md-12 cb"><hr></div>
            <c:set value="0" var="countIndex" scope="page" />
            <c:forEach var="module" items="${userDetails.userAllowedModules}">
                <c:if test="${not empty module.url && (module.id == 8 || module.id == 9 || module.id == 11)}">
                    <div class="col-md-6 ${(countIndex mod 2) == 0 ? 'cb' : ''}"  style="padding-bottom:10px;">
                        <h4><a href="<c:url value='${module.url}'/>" title="${module.featureName}">${module.featureName}</a></h4>
                        <p class="text">
                            ${module.featureDesc}
                        </p>
                    </div>
                   <c:set value="${countIndex + 1}" var="countIndex" scope="page" />     
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>
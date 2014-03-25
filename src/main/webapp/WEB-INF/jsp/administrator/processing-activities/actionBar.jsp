<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default actions-nav" role="navigation">
    <div class="contain">
        <div class="navbar-header">
            <h1 class="section-title navbar-brand">
                <c:choose>
                    <c:when test="${param['page'] == 'inbound'}">
                        <a href="javascript:void(0);" title="Inbound Batches" class="unstyled-link">Inbound Batches</a>
                    </c:when>
                    <c:when test="${param['page'] == 'outbound'}">
                        <a href="javascript:void(0);" title="Outbound Batches" class="unstyled-link">Outbound Batches</a>
                    </c:when>
                </c:choose>
            </h1>
        </div>
        <ul class="nav navbar-nav navbar-right navbar-actions" role="menu">
            
        </ul>
    </div>
</nav>

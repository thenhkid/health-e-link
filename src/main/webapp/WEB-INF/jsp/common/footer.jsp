<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date" />

<footer class="footer">
    <div class="container">
        <nav>
            <ul class="nav-inline">
                <li><a href="" title="">Home</a></li>
                <li><a href="" title="">About</a></li>
                <c:if test="${not empty pageContext.request.userPrincipal.name}"><li><a href="<c:url value='/profile'/>" title="My Account">My Account</a></li></c:if>
            </ul>
        </nav>

        <p class="vcard">
            <span class="fn">Massachusetts Department of Public Health</span> |
            <span class="adr"><span class="post-office-box">250 Washington Street</span>,
                <span class="region">Boston, MA</span>
                <span class="postal-code">02108</span></span> |
            Phone: <span class="tel">(617) 624-6000</span> |
        </p>
        <p>
            Copyright <fmt:formatDate value="${date}" pattern="yyyy" /> Massachusetts Department of Public Health All rights reserved</p>
        </p>
    </div>
</footer>
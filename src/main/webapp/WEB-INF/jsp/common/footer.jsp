<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date" />

<footer class="footer">
    <div class="container">
        <nav>
            <ul class="nav-inline">
                <li><a href="<c:url value='/'/>" title="Home">Home</a></li>
                <li><a href="<c:url value='/about'/>" title="About">About</a></li>
                <li><a href="<c:url value='/contact'/>" title="Contact">Contact</a></li>
                <c:if test="${not empty pageContext.request.userPrincipal.name}"><li><a href="<c:url value='/profile'/>" title="My Account">My Account</a></li></c:if>
            </ul>
        </nav>

        <p class="vcard">
            <span class="fn">BOWlink Technologies Inc.</span> |
            <span class="adr"><span class="post-office-box">P.O. Box 275</span>,
                <span class="region">Auburn, MA</span>
                <span class="postal-code">01501</span></span> |
            Phone: <span class="tel">(508) 721-1977</span> |
        </p>
        <p>
            Copyright <fmt:formatDate value="${date}" pattern="yyyy" /> Massachusetts Department of Public Health All rights reserved</p>
        </p>
    </div>
</footer>
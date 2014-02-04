<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<table class="table table-striped table-hover responsive">
    <thead>
        <tr>
            <th scope="col">Date Created</th>
            <th scope="col">Note</th>
            <th scope="col">Created By</th>
            <th scope="col" class="center-text"></th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${notes.size() > 0}">
                <c:forEach items="${notes}" var="note" varStatus="nStatus">
                    <tr id="noteRow-${note.id}">
                        <td scope="row">
                            <fmt:formatDate value="${note.dateSubmitted}" type="date" dateStyle="medium" />
                            <br />
                            @ <fmt:formatDate value="${note.dateSubmitted}" type="time" timeStyle="short" />
                        </td>
                        <td style="white-space: normal; width:60%;">
                            ${note.note}
                        </td>
                        <td >
                            ${note.userName}
                        </td>
                        <td class="center-text">
                            <c:if test="${note.userId == userDetails.id}">
                                <a href="javascript:void(0);" class="btn btn-link removeNote" rel="${note.id}" title="Remove this Note.">
                                    <span class="glyphicon glyphicon-edit"></span>
                                    Remove
                                </a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
        <c:otherwise><tr><td scope="row" colspan="4" style="text-align:center">There have been no notes added.</td></c:otherwise>
        </c:choose>
    </tbody>
</table>



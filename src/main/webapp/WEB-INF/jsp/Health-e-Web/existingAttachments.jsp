<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table class="table table-striped table-hover responsive">
    <thead>
        <tr>
            <th scope="col">File Name</th>
            <th scope="col">File Location</th>
            <th scope="col" class="center-text"></th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${attachments.size() > 0}">
                <c:forEach items="${attachments}" var="attachment" varStatus="tStatus">
                    <tr>
                        <td scope="row">
                            ${tStatus.index+1}.&nbsp;<c:choose><c:when test="${not empty attachment.title}">${attachment.title}</c:when><c:otherwise>${attachment.fileName}</c:otherwise></c:choose>
                        </td>
                        <td>
                            ${attachment.fileLocation}
                        </td>
                        <td class="center-text">
                            <a href="javascript:void(0);" class="btn btn-link removeAttachment" rel="${attachment.id}" title="Remove this attachment.">
                                <span class="glyphicon glyphicon-edit"></span>
                                Remove
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
        <c:otherwise><tr><td scope="row" colspan="3" style="text-align:center">There have been no attachments uploaded.</td></c:otherwise>
        </c:choose>
    </tbody>
</table>



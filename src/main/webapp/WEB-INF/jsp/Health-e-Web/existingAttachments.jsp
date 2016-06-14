<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table class="table table-striped table-hover responsive">
    <caption style="display:none">Attachments</caption>
    <thead>
        <tr>
            <th scope="col" style="width:60%">File Name</th>
            <th scope="col">File Location</th>
            <th scope="col" class="center-text"></th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${attachments.size() > 0}">
                <c:forEach items="${attachments}" var="attachment" varStatus="tStatus">
                    <tr id="attachmentRow-${attachment.id}">
                        <td scope="row" id="filenameDsp-${attachment.id}">
                            ${tStatus.index+1}.&nbsp;<span id="dsptitle-${attachment.id}"><c:choose><c:when test="${not empty attachment.title}">${attachment.title}</c:when><c:otherwise>${attachment.fileName}</c:otherwise></c:choose></span>
                        </td>
                        <td scope="row" id="filenameEdit-${attachment.id}" style="display:none;">
                            ${tStatus.index+1}.&nbsp;
                            <c:choose>
                                <c:when test="${not empty attachment.title}">
                                    <input type="text"  style="width:60%" name="title" class="title-${attachment.id}" value="${attachment.title}" />
                                </c:when>
                                <c:otherwise>
                                    <input type="text" style="width:60%" name="title" class="title-${attachment.id}" value="${attachment.fileName}" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            ${attachment.fileLocation}
                        </td>
                        <td class="center-text">
                            <c:choose>
                                <c:when test="${pageFrom == 'sent'}">
                                    <a href="<c:url value="/FileDownload/downloadFile.do?filename=${attachment.downloadLink}&foldername=${attachment.fileLocation}"/>"  class="media-modal" title="Download this brochure">
                                        <span class="glyphicon glyphicon-edit"></span>
                                        View	
                                    </a>
                                </c:when>
                                <c:otherwise>
                                     <c:if test="${pageFrom == 'pending'}">
                                        <a href="<c:url value="/FileDownload/downloadFile.do?filename=${attachment.downloadLink}&foldername=${attachment.fileLocation}"/>"  class="media-modal" title="Download this brochure">
                                            <span class="glyphicon glyphicon-edit"></span>
                                            View	
                                        </a>
                                    </c:if>
                                    <a href="javascript:void(0);" class="btn btn-link editAttachment" rel="${attachment.id}" id="edit-${attachment.id}" title="Edit this attachment.">
                                        <span class="glyphicon glyphicon-edit"></span>
                                        Edit
                                    </a>
                                    <a href="javascript:void(0);" class="btn btn-link saveAttachment" rel="${attachment.id}" id="saveedit-${attachment.id}" title="Save" style="display:none;">
                                        <span class="glyphicon glyphicon-save"></span>
                                        Save
                                    </a>
                                    <a href="javascript:void(0);" class="btn btn-link cancelAttachment" rel="${attachment.id}" id="canceledit-${attachment.id}" title="Cancel" style="display:none;">
                                        <span class="glyphicon custom-icon icon-cancel"></span>
                                        Cancel
                                    </a>    
                                        
                                    <a href="javascript:void(0);" class="btn btn-link removeAttachment" rel="${attachment.id}" title="Remove this attachment.">
                                        <span class="glyphicon  glyphicon-edit"></span>
                                        Remove
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
        <c:otherwise><tr><td scope="row" colspan="3" style="text-align:center">There have been no attachments uploaded.</td></c:otherwise>
        </c:choose>
    </tbody>
</table>



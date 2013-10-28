
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 class="panel-title"></h3>
		</div>
		<div class="modal-body">
			<c:choose>
				<c:when test="${fileType == 'image'}">
					<image src="<%=request.getContextPath()%>/orgFolders/${brochureAttachment}" />
				</c:when>
				<c:when test="${fileType == 'pdf'}">
					<EMBED SRC="<%=request.getContextPath()%>/orgFolders/${brochureAttachment}" HEIGHT=480 WIDTH=530 />
				</c:when>
				<c:when test="${fileType == 'other'}">
					 <%@ page contentType="application/msword" %>
					 <EMBED SRC="<%=request.getContextPath()%>/orgFolders/${brochureAttachment}" HEIGHT=480 WIDTH=530 />
				</c:when>
			</c:choose>
		</div>
	</div>
</div>
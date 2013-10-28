
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 class="panel-title"><c:choose><c:when test="${btnValue == 'Update'}">Update</c:when><c:when test="${btnValue == 'Create'}">Add</c:when></c:choose> Brochure ${success}</h3>
		</div>
		<div class="modal-body">
			<form:form id="brochuredetailsform" commandName="brochuredetails" modelAttribute="brochuredetails" enctype="multipart/form-data" method="post" role="form">
				<form:hidden path="id" id="id" />
				<form:hidden path="orgId" id="orgId" />
				<form:hidden path="fileName" />
				<form:hidden path="dateCreated" />
				<div class="form-container">
					<spring:bind path="title">
						<div id="brochureTitleDiv" class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="brochureTitle">Brochure Title *</label>
							<form:input path="title" id="brochureTitle" class="form-control" type="text" maxLength="255" />
							<span id="brochureTitleMsg" class="control-label"></span>
						</div>
					</spring:bind>
					<c:if test="${not empty brochuredetails.getfileName()}">
						<div class="form-group">
							<label class="control-label" for="currentFile">Current File</label>
							<input type="text" disabled class="form-control" value="${brochuredetails.getfileName()}" />
						</div>
					</c:if>
					<spring:bind path="file">
						<div id="brochureFileDiv" class="form-group ${status.error ? 'has-error' : '' }">
							<label class="control-label" for="brochureFile"><c:if test="${not empty brochuredetails.getfileName()}">New</c:if> File *</label>
							<form:input path="file" id="brochureFile" type="file" class="form-control" />
							<span id="brochureFileMsg" class="control-label"></span>
						</div>
					</spring:bind>
					<div class="form-group">
						<input type="button" id="submitButton" rel="${btnValue}" class="btn btn-primary" value="${btnValue}"/>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>

<script type="text/javascript">

	$(document).ready(function() {
	    $("input:text,form").attr("autocomplete","off");
	});

</script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default actions-nav" role="navigation">
	<div class="navbar-header">
		<h1 class="section-title navbar-brand">
			<c:choose>
				<c:when test="${param['page'] == 'listConfigs'}">
					<a href="javascript:void(0);" title="Configuration List" class="unstyled-link">Configuration List</a>
				</c:when>
				<c:when test="${param['page'] == 'configDetails'}">
					<a href="javascript:void(0);" title="Configuration Details" class="unstyled-link">Configuration Details</a>
				</c:when>
			</c:choose>
		</h1>
	</div>
	<ul class="nav navbar-nav navbar-right navbar-actions">
		<c:choose>
			<c:when test="${param['page'] == 'configDetails'}">
				<li><a href="javascript:void(0);" id="saveDetails" title="Save"><span class="glyphicon glyphicon-ok icon-stacked"></span> Save</a></li>
				<li><a href="javascript:void(0);" id="saveCloseDetails" title="Save &amp; Close"><span class="glyphicon glyphicon-floppy-disk icon-stacked"></span> Save &amp; Close</a></li>
				<li><a href="" title="Delete"><span class="glyphicon glyphicon-remove icon-stacked"></span>Delete</a></li>
				<li><a href="" title="Cancel"><span class="glyphicon icon-stacked custom-icon icon-cancel"></span>Cancel</a></li>
			</c:when>
		</c:choose>
	</ul>
</nav>
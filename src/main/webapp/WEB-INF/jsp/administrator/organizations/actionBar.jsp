<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default actions-nav" role="navigation">
	<div class="contain">
		<div class="navbar-header">
			<h1 class="section-title navbar-brand">
				<c:choose>
					<c:when test="${param['page'] == 'listOrgs'}">
						<a href="javascript:void(0);" title="Organization List" class="unstyled-link">Organizations</a>
					</c:when>
					<c:when test="${param['page'] == 'orgDetails'}">
						<a href="javascript:void(0);" title="Organization Details" class="unstyled-link">
							<c:choose>
								<c:when test="${not empty id}">
									Edit Organization
								</c:when>
								<c:otherwise>
									Create New Organization
								</c:otherwise>
							</c:choose>
						</a>
					</c:when>
					<c:when test="${param['page'] == 'orgUsers'}">
						<a href="javascript:void(0);" title="Organization Users" class="unstyled-link">Organization Users</a>
					</c:when>
					<c:when test="${param['page'] == 'orgProviders'}">
						<a href="javascript:void(0);" title="Organization Providers" class="unstyled-link">Organization Providers</a>
					</c:when>
					<c:when test="${param['page'] == 'orgBrochures'}">
						<a href="javascript:void(0);" title="Organization Brochures" class="unstyled-link">Organization Brochures</a>
					</c:when>
				</c:choose>
			</h1>
		</div>
		<ul class="nav navbar-nav navbar-right navbar-actions">
			<c:choose>
				<c:when test="${param['page'] == 'orgDetails'}">
					<li><a href="javascript:void(0);" id="saveDetails" title="Save this Organization"><span class="glyphicon glyphicon-ok icon-stacked"></span> Save </a></li>
					<li><a href="javascript:void(0);" id="saveCloseDetails" title="Save &amp; Close"><span class="glyphicon glyphicon-floppy-disk icon-stacked"></span> Save &amp; Close</a></li>
					<c:if test="${not empty id}"><li><a href="#confirmationOrgDelete" data-toggle="modal" rel="${id}" title="Delete this Organization"><span class="glyphicon glyphicon-remove icon-stacked"></span>Delete</a></li></c:if>
					<li><a href="<c:url value='/administrator/organizations/list' />" title="Cancel"><span class="glyphicon icon-stacked custom-icon icon-cancel"></span>Cancel</a></li>
				</c:when>
				<c:when test="${param['page'] == 'listOrgs'}">
					<li><a href="create" title="Create New Organization"><span class="glyphicon icon-stacked glyphicon glyphicon-plus"></span>Create New</a></li>
				</c:when>
			</c:choose>
		</ul>
	</div>
</nav>


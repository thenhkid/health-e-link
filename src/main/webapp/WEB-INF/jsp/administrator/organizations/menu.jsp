<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
	<nav class="secondary-nav" role="navigation">
		<ul class="nav nav-pills nav-stacked">
			<li>
				<c:choose>
					<c:when test="${param['page'] == 'details'}">
						<c:choose>
							<c:when test="${id > 0}">
								<a href="">Edit Organization</a>
							</c:when>
							<c:otherwise>
								<a href="">New Organization</a>
							</c:otherwise>
						</c:choose>
						<ul class="nav nav-pills nav-stacked">
							<li class="active"><a href="" title="">Details</a></li>
							<li class="${id > 0 ? '' : 'disabled'}"><a href="${id > 0 ? 'users' : 'javascript:void(0);'}" title="">Users</a></li>
							<li class="${id > 0 ? '' : 'disabled'}"><a href="${id > 0 ? 'providers' : 'javascript:void(0);'}" title="">Providers</a></li>
							<li class="${id > 0 ? '' : 'disabled'}"><a href="${id > 0 ? 'brochures' : 'javascript:void(0);'}" title="">Brochures</a></li>
						</ul>	
					</c:when>
					<c:otherwise>
						<ul class="nav nav-pills nav-stacked">
							<li class="active"><a href="<c:url value='/administrator/organizations/list' />" title="Organizations">Organizations</a></li>
							<li><a href="<c:url value='/administrator/configurations/list' />" title="Configurations">Configurations</a></li>
							<li><a href="<c:url value='/administrator/library/list' />" title="Message Type Library">Library</a></li>
							<li><a href="<c:url value='/administrator/scheduler' />" title="Scheduler">Scheduler</a></li>
						</ul>
					</c:otherwise>
				</c:choose>
			</li>
		</ul>
	</nav>
</aside>
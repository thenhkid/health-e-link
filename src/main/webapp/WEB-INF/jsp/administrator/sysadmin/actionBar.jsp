<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default actions-nav" role="navigation">
	<div class="contain">
		<div class="navbar-header">
			<h1 class="section-title navbar-brand">
				<c:choose>
					<c:when test="${param['page'] == 'list'}">
						<a href="javascript:void(0);" title="Look Up Tables" class="unstyled-link">Look Up Tables</a>
					</c:when>
					<c:when test="${param['page'] == 'data'}">
						<a href="javascript:void(0);" title="Table Data" class="unstyled-link">Look Up Table Data</a>
					</c:when>
					<c:when test="${param['page'] == 'macros'}">
						<a href="javascript:void(0);" title="Table Data" class="unstyled-link">Manage System Macros</a>
					</c:when>
				</c:choose>
			</h1>
		</div>
		<ul class="nav navbar-nav navbar-right navbar-actions">
		</ul>
	</div>
</nav>


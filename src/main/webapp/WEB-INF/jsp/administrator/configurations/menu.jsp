<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
	<nav class="secondary-nav" role="navigation">
		<ul class="nav nav-pills nav-stacked">
			<li>
				<c:choose>
					<c:when test="${param['page'] == 'details'}">
						<a href="">Edit Configuration</a>
						<ul class="nav nav-pills nav-stacked">
							<li class="active"><a href="" title="Details">Details</a></li>
							<li><a href="" title="Transpot Details">Transport Details ${id}</a></li>
							<li><a href="" title="Field Mappings">Field Mappings</a></li>
							<li><a href="" title="Data Translations">Data Translations</a></li>
							<li><a href="" title="Connections">Connections</a></li>
							<li><a href="" title="Scheduling">Scheduling</a></li>
							<li><a href="" title="Processing History">Processing History</a></li>
						</ul>
					</c:when>
					<c:when test="${param['page'] == 'create'}">
						<a href="">Create new Configuration</a>
						<ul class="nav nav-pills nav-stacked">
							<li class="active"><a href="" title=""><strong>Step 1:</strong><br/>Details</a></li>
							<li><a href="" title=""><strong>Step 2:</strong><br/>Field Mappings</a></li>
							<li><a href="" title=""><strong>Step 3:</strong><br/>Data Translations</a></li>
							<li><a href="" title=""><strong>Step 4:</strong><br/>Connections</a></li>
							<li><a href="" title=""><strong>Step 5:</strong><br/>Scheduling</a></li>
						</ul>
					</c:when>
				</c:choose>
			</li>
		</ul>
	</nav>
</aside>





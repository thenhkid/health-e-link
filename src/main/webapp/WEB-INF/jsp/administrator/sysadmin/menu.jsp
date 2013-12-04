<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
	<nav class="secondary-nav" role="navigation">
		<ul class="nav nav-pills nav-stacked">
			<li ${param['page'] == 'table' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/data'/>" title="Table Data">Look Up Tables</a></li>
			<li ${param['page'] == 'macroList' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/macros'/>" title="Macros">Macros</a></li>
			<li ${param['page'] == 'logo' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/logo'/>" title="Logo">Logo</a></li>
		</ul>
	</nav>
</aside>
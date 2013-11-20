<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
	<nav class="secondary-nav" role="navigation">
		<ul class="nav nav-pills nav-stacked">
			<li ${param['page'] == 'data' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${id > 0 ? 'data' : 'javascript:void(0);'}" title="Table Data">Data</a></li>
		</ul>
	</nav>
</aside>
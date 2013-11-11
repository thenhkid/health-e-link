<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
	<nav class="secondary-nav" role="navigation">
		<ul class="nav nav-pills nav-stacked">
			<li ${param['page'] == 'details' ? 'class="active"' : ''}><a href="${param['page'] != 'details' ? 'details' : 'javascript:void(0);'}" title="Step 1: Configuration Details"><span class="badge">1</span> Details</a></li>
			<li ${param['page'] == 'transport' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${param['page'] != 'transport' && id > 0 ? 'transport' : 'javascript:void(0);'}" title="Step 2: Transport Details"><span class="badge">2</span> Transport Details</a></li>
			<li ${param['page'] == 'mappings' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${param['page'] != 'mappings' && id > 0 ? 'mappings' : 'javascript:void(0);'}" title="Step 3: Field Mappings"><span class="badge">3</span> Field Mappings</a></li>
			<li ${param['page'] == 'translations' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${param['page'] != 'translations' && id > 0 ? 'translations' : 'javascript:void(0);'}" title="Step 4: Data Translations"><span class="badge">4</span> Data Translations</a></li>
			<li ${param['page'] == 'connections' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${param['page'] != 'connections' && id > 0 ? 'connections' : 'javascript:void(0);'}" title="Step 5: Connections"><span class="badge">5</span> Connections</a></li>
			<li ${param['page'] == 'scheduling' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${param['page'] != 'scheduling' && id > 0 ? 'scheduling' : 'javascript:void(0);'}" title="Step 6: Scheduling"><span class="badge">6</span> Scheduling</a></li>
		</ul>
	</nav>
</aside>






<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
    <nav class="secondary-nav" role="navigation">
        <ul class="nav nav-pills nav-stacked" role="menu">
            <li role="menuitem" ${param['page'] == 'details' ? 'class="active"' : ''}><a href="${param['page'] != 'details' ? './' : 'javascript:void(0);'}" title="Organization Details">Details</a></li>
            <li role="menuitem" ${param['page'] == 'config' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${param['page'] != 'config' && id > 0 ? 'configurations' : 'javascript:void(0);'}" title="Organization Configurations">Configurations</a></li>
            <li role="menuitem" ${param['page'] == 'providers' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${id > 0 ? 'providers' : 'javascript:void(0);'}" title="Organization Providers">Providers</a></li>
            <li role="menuitem" ${param['page'] == 'users' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}><a href="${param['page'] != 'users' &&  id > 0 ? 'users' : 'javascript:void(0);'}" title="Organization System Users">System Users</a></li>
            <li role="menuitem" ${param['page'] == 'brochures' ? 'class="active"' : ''} ${id > 0 ? '' : 'class="disabled"'}"><a href="${param['page'] != 'brochures' && id > 0 ? 'brochures' : 'javascript:void(0);'}" title="Organization Brochures">Brochures</a></li>
        </ul>
    </nav>
</aside>
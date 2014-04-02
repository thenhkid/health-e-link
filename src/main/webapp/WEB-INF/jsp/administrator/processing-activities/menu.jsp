<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
    <nav class="secondary-nav" role="navigation">
        <ul class="nav nav-pills nav-stacked" role="menu">
            <li role="menuitem" ${param['page'] == 'waiting' ? 'class="active"' : ''}><a href="/administrator/processing-activity/waiting" title="Transactions Waiting to be Processed">Waiting to be Processed</a></li>
            <li role="menuitem" ${param['page'] == 'inbound' ? 'class="active"' : ''}><a href="/administrator/processing-activity/inbound" title="Inbound Processing Activities">Inbound Batches</a></li>
            <li role="menuitem" ${param['page'] == 'outbound' ? 'class="active"' : ''}><a href="/administrator/processing-activity/outbound" title="Outbound Processing Activities">Outbound Batches</a></li>
           </ul>
    </nav>
</aside>
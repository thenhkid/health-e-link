<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
    <nav class="secondary-nav" role="navigation">
        <ul class="nav nav-pills nav-stacked nav-steps" role="menu" >
            <li role="menuitem" ${param['page'] == 'details' ? 'class="active"' : ''}><a href="${param['page'] != 'details' ? 'details' : 'javascript:void(0);'}" title="Initial Setup">Initial Setup</a></li>
            <li role="menuitem" ${param['page'] == 'transport' ? 'class="active"' : ''} ${id > 0 and stepsCompleted >= 1 ? '' : 'class="disabled"'}><a href="${param['page'] != 'transport' and id > 0 and stepsCompleted >= 1 ? 'transport' : 'javascript:void(0);'}" title="Transport Method">Transport Method</a></li>
            <li role="menuitem" ${param['page'] == 'specs' ? 'class="active"' : ''} ${id > 0 and stepsCompleted >= 2 ? '' : 'class="disabled"'}><a href="${param['page'] != 'specs' and id > 0 and stepsCompleted >= 2 ? 'messagespecs' : 'javascript:void(0);'}" title="Detail Message Specs">Message Specs</a></li>
            <li role="menuitem" ${param['page'] == 'mappings' ? 'class="active"' : ''} ${id > 0 and stepsCompleted >= 3 and (mappings == 1 or mappings == 3) ? '' : 'class="disabled"'}><a href="${param['page'] != 'mappings' and id > 0 and stepsCompleted >= 3 and (mappings == 1 or mappings == 3) ? 'mappings' : 'javascript:void(0);'}" title="Field Mappings">Field Mappings</a></li>
            <li role="menuitem" ${param['page'] == 'ERGCustomize' ? 'class="active"' : ''} ${id > 0 and stepsCompleted >= 3 and (mappings == 2 or mappings == 3) ? '' : 'class="disabled"'}><a href="${param['page'] != 'ERGCustomize' and id > 0 and stepsCompleted >= 3 and (mappings == 2 or mappings == 3) ? 'ERGCustomize' : 'javascript:void(0);'}" title="ERG Customization">ERG Customization</a></li>
            <li role="menuitem" ${param['page'] == 'translations' ? 'class="active"' : ''} ${id > 0 and stepsCompleted >= 4 ? '' : 'class="disabled"'}><a href="${param['page'] != 'translations' and id > 0 and stepsCompleted >= 4 ? 'translations' : 'javascript:void(0);'}" title="Data Translations">Data Translations</a></li>
            <li role="menuitem" ${param['page'] == 'schedule' ? 'class="active"' : ''} ${id > 0 and stepsCompleted >= 5 and (mappings == 1 or mappings == 3) ? '' : 'class="disabled"'}><a href="${param['page'] != 'schedule' and id > 0 and stepsCompleted >= 5 ? 'scheduling' : 'javascript:void(0);'}" title="Scheduling">Scheduling</a></li>
        </ul>
    </nav>
</aside>
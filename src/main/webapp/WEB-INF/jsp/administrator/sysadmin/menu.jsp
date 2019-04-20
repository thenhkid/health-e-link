<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<aside class="secondary">
    <nav class="secondary-nav" role="navigation">
        <ul class="nav nav-pills nav-stacked">
            <li ${param['page'] == 'table' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/data'/>" title="Table Data">Look Up Tables</a></li>
            <li ${param['page'] == 'macroList' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/macros'/>" title="Macros">Macros</a></li>
            <li ${param['page'] == 'hl7List' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/hl7'/>" title="HL7 Specs">HL7 Specs</a></li>
            <li ${param['page'] == 'news' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/news'/>" title="News Articles">News Articles</a></li>
			<li ${param['page'] == 'loginAs' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/loginAs'/>" title="Login As">Login As</a></li>
            <li ${param['page'] == 'getLog' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/getLog'/>" title="Download Log">Download Log</a></li>
            <li ${param['page'] == 'moveFilePaths' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/moveFilePaths'/>" title="Move File Paths">Move File Paths</a></li>
                <%-- <li ${param['page'] == 'logos' ? 'class="active"' : ''}><a href="<c:url value='/administrator/sysadmin/logos'/>" title="Logo">Logos</a></li>
                --%>
          </ul>
    </nav>
</aside>
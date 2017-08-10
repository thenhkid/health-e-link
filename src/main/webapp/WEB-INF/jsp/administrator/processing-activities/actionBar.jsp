<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default actions-nav" role="navigation">
    <div class="contain">
        <div class="navbar-header">
            <h1 class="section-title navbar-brand">
                <c:choose>
                    <c:when test="${param['page'] == 'waiting'}">
                        <a href="javascript:void(0);" title="Transactions Waiting to be Processed" class="unstyled-link">Transactions Waiting to be Processed</a>
                    </c:when>
                    <c:when test="${param['page'] == 'inbound' || page == 'inbound'}">
                        <a href="javascript:void(0);" title="Inbound Batches" class="unstyled-link">Inbound Batches</a>
                    </c:when>
                    <c:when test="${param['page'] == 'rejected' || page == 'rejected'}">
                        <a href="javascript:void(0);" title="Rejected Batches" class="unstyled-link">Batches with Rejected Transactions</a>
                    </c:when>    
                    <c:when test="${param['page'] == 'outbound'}">
                        <a href="javascript:void(0);" title="Outbound Batches" class="unstyled-link">Outbound Batches</a>
                    </c:when>
                    <c:when test="${param['page'] == 'edit'}">
                        <a href="javascript:void(0);" title="Edit Batch Transaction" class="unstyled-link">Edit Batch Transaction</a>
                    </c:when>
                    <c:when test="${param['page'] == 'refActivityExport'}">
                        <a href="javascript:void(0);" title="Referral Activity Export" class="unstyled-link">Referral Activity Export</a>
                    </c:when>  
                    <c:when test="${param['page'] == 'getLog'}">
                        <a href="/administrator/sysadmin/getLog" title="Download Tomcat Log" class="unstyled-link">Download Tomcat Log</a>
                    </c:when>   
                </c:choose>
            </h1>
        </div>
        <ul class="nav navbar-nav navbar-right navbar-actions" role="menu">
            <c:choose>
                <c:when test="${param['page'] == 'edit'}">
                    <li role="menuitem"><a href="javascript:void(0);" id="saveCloseDetails" class="submitMessage" title="Save &amp; Close" role="button"><span class="glyphicon glyphicon-floppy-disk icon-stacked"></span> Save &amp; Close</a></li>
                 </c:when>
            </c:choose>
        </ul>
    </div>
</nav>

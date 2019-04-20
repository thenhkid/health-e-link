<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!-- Actions Nav -->
<nav class="navbar navbar-default actions-nav">
    <div class="contain">
        <div class="navbar-header">
            <h1 class="section-title navbar-brand"><a href="" title="Section Title" class="unstyled-link">System Admin Dashboard</a></h1>
        </div>
    </div>
</nav>

<!-- End Actions Nav -->
<div class="main clearfix full-width" role="main">

    <div class="row-fluid contain">
        <div class="col-md-12">
            <section class="panel panel-default panel-intro">
                <div class="panel-body" >
                    <h2>Welcome to system administration <c:out value="${pageContext.request.userPrincipal.name}" />!</h2>
                </div>
            </section>
        </div>
    </div>

    <div class="row-fluid contain basic-clearfix">
        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="data" title="Total number of look up tables">${totalLookUpTables}</a></span>
                    <h3>Look Up Tables</h3>
                    <a href="<c:url value='data' />" title="Look Up Tables" class="btn btn-primary btn-small" role="button">View all</a>
                </div>
            </section>
        </div>

        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="macros" title="Total number of macros">${totalMacroRows}</a></span>
                    <h3>Macros</h3>
                    <a href="macros" title="Macros" class="btn btn-primary btn-small" role="button">View all</a>
                </div>
            </section>
        </div>
                    
       <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="hl7" title="Total number of Specs">${totalHL7Entries}</a></span>
                    <h3>HL7 Specs</h3>
                    <a href="hl7" title="HL7 Specs" class="btn btn-primary btn-small" role="button">View all</a>
                </div>
            </section>
        </div>    
                    
        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="news" title="Total number of News Articles">${totalNewsArticles}</a></span>
                    <h3>News Articles</h3>
                    <a href="news" title="News Articles" class="btn btn-primary btn-small" role="button">View all</a>
                </div>
            </section>
        </div>                
		<%--
        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="logos" title="Configure Logos">2</a></span>
                    <h3>Logos</h3>
                    <a href="logos" title="Configure Logos" class="btn btn-primary btn-small" role="button">View Logos</a>
                </div>
            </section>
        </div>
		--%>
	 <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="#profileModal" data-toggle="modal" class="updateprofile" title="Update my profile">1</a></span>
                    <h3>Update my profile</h3>
                         <a href="#profileModal" id="profileButton" class="btn btn-primary btn-small" class="updateprofile1"  data-toggle="modal" title="Update my profile">
                         Update my profile</a>               
                </div>
            </section>
        </div>     
         <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="loginAs" class="loginAs" title="Login As">${totalUsers}</a></span>
                    <h3>Total Users</h3>
                         <a href="loginAs" id="loginAs" class="btn btn-primary btn-small" class="loginAs" title="Login As User">
                         Login As</a>               
                </div>
            </section>
        </div>   	
		<div class="col-md-3 col-sm-3 col-xs-3">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="getLog" class="getLog" title="Download Latest Tomcat Log">1</a></span>
                    <h3>Log</h3>
                         <a href="getLog" id="getLog" class="btn btn-primary btn-small" class="getLog" title="Download Latest Tomcat Log">
                         Download Latest Tomcat Log</a>               
                </div>
            </section>
        </div> 
        
        <div class="col-md-3 col-sm-3 col-xs-3">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="moveFilePaths" class="moveFilePaths" title="View File Paths">${filePaths}&nbsp;</a></span>
                    <h3>View Errored File Paths</h3>
                         <a href="moveFilePaths" id="moveFilePaths" class="btn btn-primary btn-small" class="moveFilePaths" title="View File Paths">
                         File Paths</a>               
                </div>
            </section>
        </div>

    </div>
</div>

<!-- my profile modal -->
<div class="modal fade" id="profileModal" role="dialog" tabindex="-1" aria-labeledby="Modify My Profile" aria-hidden="true" aria-describedby="Modify My Profile"></div>









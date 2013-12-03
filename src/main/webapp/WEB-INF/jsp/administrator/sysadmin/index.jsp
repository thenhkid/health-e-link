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
                    <h2>Welcome <c:out value="${pageContext.request.userPrincipal.name}" />!</h2>
                </div>
            </section>
        </div>
    </div>

    <div class="row-fluid contain basic-clearfix">
        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="data" title="Total number of look up tables">#</a></span>
                    <h3>Look Up Tables</h3>
                    <a href="<c:url value='data' />" title="Look Up Tables" class="btn btn-primary btn-small" role="button">View all</a>
                </div>
            </section>
        </div>

        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="macros" title="Total number of macros">#</a></span>
                    <h3>Macros</h3>
                    <a href="macros" title="Macros" class="btn btn-primary btn-small" role="button">View all</a>
                </div>
            </section>
        </div>

        <div class="col-md-3 col-sm-3 col-xs-6">
            <section class="panel panel-default panel-stats">
                <div class="panel-body">
                    <span class="stat-number"><a href="logo" title="Configure Logo">#</a></span>
                    <h3>Logo</h3>
                    <a href="logo" title="Configure Logo" class="btn btn-primary btn-small" role="button">View Logo</a>
                </div>
            </section>
        </div>


    </div>
</div>










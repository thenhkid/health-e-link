<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header id="header" class="header" role="banner">
	<!-- Primary Nav -->
	<!--role="navigation" used for accessibility -->
		<nav class="navbar primary-nav" role="navigation">
			<div class="contain">
				<div class="navbar-header">
					<a href="" class="navbar-brand" title="{company name}">
					<!--
						<img src="<%=request.getContextPath()%>/dspResources/img/health-e-link/img-health-e-link-logo.png" class="logo" alt="Health-e-Link Logo"/>
						Required logo specs:
						logo width: 125px
						logo height: 30px
	
						Plain text can be used without image logo
	
						sprite can be used with class="logo":
					-->
						<span class="logo" alt="{Company Name Logo}"></span>
					</a>
				</div>
				<ul class="nav navbar-nav">
					<li class="${param['sect'] == 'org' ? 'active' : 'none'}"><a href="<c:url value='/administrator/organizations/list' />" title="Organization Manager">Organizations</a><c:if test="${param['sect'] == 'org'}"><span class="indicator-active arrow-up"></span></c:if></li>
					<li class="${param['sect'] == 'config' ? 'active' : 'none'}"><a href="<c:url value='/administrator/configurations/list' />" title="Configuration Manager">Configurations</a><c:if test="${param['sect'] == 'config'}"><span class="indicator-active arrow-up"></span></c:if></li>
					<li class="${param['sect'] == 'lib' ? 'active' : 'none'}"><a href="<c:url value='/administrator/library/list' />" title="Message Type Library Manager">Message Type Library</a><c:if test="${param['sect'] == 'lib'}"><span class="indicator-active arrow-up"></span></c:if></li>
					<li class="${param['sect'] == 'sched' ? 'active' : 'none'}"><a href="" title="Scheduler">Scheduler</a><c:if test="${param['sect'] == 'sched'}"><span class="indicator-active arrow-up"></span></c:if></li>
				</ul>
				<ul class="nav navbar-nav navbar-right" id="secondary-nav">
					<li><a title="log out" href="<c:url value='/logout' />">Log out</a></li>
				</ul>
			</div>
		</nav>
	<!-- // End Primary Nav -->
</header>

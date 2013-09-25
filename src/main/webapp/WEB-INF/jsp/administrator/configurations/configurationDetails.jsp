<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="main clearfix" role="main">

	<!-- Subnav (if needed) -->
	<nav class="sub-nav" role="navigation">
		<ul class="nav nav-pills" >
			<li class="active">
				<a href="" title="">
					<span class="badge">1</span>
					Details
				</a>
			</li>
			<li>
				<a href="" title="">
					<span class="badge">2</span>
					Field Mappings
				</a>
			</li>
			<li class="disabled">
				<a href="" title="">
					<span class="badge">3</span>
					Data Translations
				</a>
			</li>
			<li class="disabled">
				<a href="" title="">
					<span class="badge">4</span>
					Connections
				</a>
			</li>
			<li class="disabled">
				<a href="" title="">
					<span class="badge">5</span>
					Scheduling
				</a>
			</li>
		</ul>
	</nav>
	
	<div class="col-md-6">
		<section class="panel panel-default">
			<c:if test="${saved == 'success'}">
				<p class="success">User Updated Successfully</p>
			</c:if>
			<div class="panel-heading">
				<h3 class="panel-title">Configuration Detail</h3>
			</div>
			<div class="panel-body">
				<div class="form-container">
					<form:form commandName="configuration"  method="post" role="form">
						<input type="hidden" id="action" name="action" value="save" />
						<form:hidden path="id" id="configId" />
						<form:hidden path="orgId" />
						<div class="form-group">
							<label for="fieldA">Configuration Name</label>
							<form:input path="configName" class="form-control" type="text" />
							<form:errors path="configName" cssClass="error" />
							<!-- <input id="fieldA" class="form-control" type="text" />-->
						</div>
						<%--<input type="submit" alt="Save" title="Save" value="Save" class="btn btn-primary"/>--%>
						
					</form:form>
				</div>
			</div>
		</section>
	</div>
	
</div>




<script type="text/javascript">

	$(function() {
		$("#saveDetails").click(function(event) {
			//$("#configuration").attr("action", "saveDetails");
			$('#action').val('save');
			$("#configuration").submit();
		});
		
		$("#saveCloseDetails").click(function(event) {
			$('#action').val('close');
        	$("#configuration").submit();
		});

	});
</script>

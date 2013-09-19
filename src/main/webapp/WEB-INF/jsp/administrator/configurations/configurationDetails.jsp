<%@ include file="../../common/header.jsp" %>

	<div class="link">
		<h2>Update Configurations</h2>
		<c:if test="${saved == 'success'}">
			<p class="success">User Updated Successfully</p>
		</c:if>
		<c:if test="${saved == 'error'}">
			<p>User Not Updated</p>
		</c:if>
		<form:form commandName="configuration" method="post">
			<form:hidden path="id" />
			<form:hidden path="orgId" />
			<form:label path="configName">User Name</form:label><form:input path="configName" />
			<form:errors path="configName" cssClass="error" />
			<button type="submit" id="save">Save Configuration</button>
		</form:form>
	</div>


<%@ include file="../../common/footer.jsp" %>
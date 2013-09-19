<%@ include file="../../common/header.jsp" %>

<c:if test="${not empty message}">
	<div class="errorblock">
		${message}
	</div>
</c:if>

<c:if test="${not empty savedStatus}">
	<div class="successblock">
		${savedStatus}
	</div>
</c:if>

<c:if test="${not empty configurationList}">
	<table>	
	<tr>
	<th>Configuration Name</th>
	<th colspan="2">Actions</th>
	</tr>
	<c:forEach var="config" items="${configurationList}">
		<tr>
		<td>${config.configName}</td>
		<td><a href="detail/${config.id}">Edit</a></td>
		<td></td>
		</tr>
	</c:forEach>
	</table>
</c:if>

<%@ include file="../../common/footer.jsp" %>
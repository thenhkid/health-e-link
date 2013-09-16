<%@ include file="../../common/header.jsp" %>

<c:if test="${not empty message}">
	<div class="errorblock">
		${message}
	</div>
</c:if>


<c:if test="${not empty configurationList}" >
	<c:forEach items="${configurationList}" var="config">		
		 ${config.configName}<br />
	</c:forEach>	
</c:if>


<%@ include file="../../common/footer.jsp" %>
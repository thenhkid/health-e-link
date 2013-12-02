<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default actions-nav" role="navigation">
	<div class="contain">
		<div class="navbar-header">
			<h1 class="section-title navbar-brand">
				<c:choose>
					<c:when test="${param['page'] == 'list'}">
						<a href="javascript:void(0);" title="Look Up Tables" class="unstyled-link">Look Up Tables</a>
					</c:when>
					<c:when test="${param['page'] == 'dataDetails'}">
						<a href="javascript:void(0);" title="Table Details" class="unstyled-link">
							<c:choose>
								<c:when test="${tableDataDetails.id > 0}">
									Edit Look Up Table Data
								</c:when>
								<c:otherwise>
									Add Look Up Table Data
								</c:otherwise>
							</c:choose>
						</a>
					</c:when>
					<c:when test="${param['page'] == 'data'}">
						<a href="javascript:void(0);" title="Table Data" class="unstyled-link">Look Up Table Data</a>
					</c:when>
					
				</c:choose>
			</h1>
		</div>
		<ul class="nav navbar-nav navbar-right navbar-actions">
			<c:choose>
				<c:when test="${param['page'] == 'dataDetails'}">
				<li><a href="javascript:void(0);" id="saveDetails" title="Save Data"><span class="glyphicon glyphicon-ok icon-stacked"></span> Save </a></li>
				<li><a href="javascript:void(0);" id="saveCloseDetails" title="Save And Close"><span class="glyphicon glyphicon-floppy-disk icon-stacked"></span> Save &amp; Close</a></li>
				<c:if test="${tableDataDetails.id > 0}"><li><a href="javascript:void(0);" id=detailDataDelete rel="${tableDataDetails.id}" title="Delete this data item"><span class="glyphicon glyphicon-remove icon-stacked"></span>Delete</a></li></c:if>
				<li><a href="<c:url value='/administrator/sysadmin/std/data/${tableInfo.urlId}' />" title="Cancel"><span class="glyphicon icon-stacked custom-icon icon-cancel"></span>Cancel</a></li>
				
				</c:when>
			</c:choose>
		</ul>
	</div>
</nav>


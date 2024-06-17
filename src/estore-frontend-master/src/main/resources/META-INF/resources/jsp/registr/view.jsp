<%@ include file="/jsp/init.jsp"%>
<%@ include file="/jsp/error.jsp"%>
<portlet:renderURL var="viewURL"/>
<liferay-ui:tabs names="tab.purchase,tab.electronic,tab.employee" tabsValues="tPurch,tElectro,tEmp" param="tab" url="${viewURL}">
	<c:choose>
	    <c:when test="${'tElectro' eq param.tab}">
			<jsp:include page="/jsp/registr/electronic/view.jsp" flush="true"/>
		</c:when>
		<c:when test="${'tEmp' eq param.tab}">
			<jsp:include page="/jsp/registr/employee/view.jsp" flush="true"/>
		</c:when>
		<c:otherwise>
			<jsp:include page="/jsp/registr/purchase/view.jsp" flush="true"/>
		</c:otherwise>
	</c:choose>
</liferay-ui:tabs>
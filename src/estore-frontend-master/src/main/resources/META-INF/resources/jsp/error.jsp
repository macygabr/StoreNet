<div id="errorContainer"></div>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<c:if test="${not empty sessionErrors}">
	<c:forEach var="error" items="${sessionErrors}">
		<liferay-ui:error key="${error}" message="${error}"/>
	</c:forEach>
</c:if>

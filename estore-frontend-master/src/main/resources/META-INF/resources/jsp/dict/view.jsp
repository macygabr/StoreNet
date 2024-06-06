<%@ include file="/jsp/init.jsp"%>
<%@ include file="/jsp/error.jsp"%>
<portlet:renderURL var="viewURL"/>
<div class="container-fluid">
	<clay:row>
	  <clay:col md="2">
	     <liferay-ui:tabs type="pills" cssClass="nav flex-column" names="tab.shop,tab.electrotype,tab.purchasetype,tab.positiontype" 
	        param="tab" tabsValues="tShop,tElectro,tPurch,tPos" url="${viewURL}">
		</liferay-ui:tabs>
	  </clay:col>
	  <clay:col md="10">
	  	<c:choose>
	      <c:when test="${'tElectro' eq param.tab}">
	        <jsp:include page="/jsp/dict/electrotype/view.jsp"/>
	      </c:when>
	      <c:when test="${'tPurch' eq param.tab}">
	        <jsp:include page="/jsp/dict/purchasetype/view.jsp"/>
	      </c:when>
	      <c:when test="${'tPos' eq param.tab}">
	        <jsp:include page="/jsp/dict/positiontype/view.jsp"/>
	      </c:when>
	      <c:otherwise>
	        <jsp:include page="/jsp/dict/shop/view.jsp"/>
	      </c:otherwise>
	    </c:choose>
	  </clay:col>
	</clay:row>
</div>
<portlet:renderURL var="reloadURL">
	<portlet:param name="tab" value="${param.tab}"/>
</portlet:renderURL>
<script>
Liferay.provide(window,'reloadItem', function() {
	var dialog = Liferay.Util.getTop().Liferay.Util.Window.getById('entityItem');
	dialog.destroy();
	submitForm(document.hrefFm,'${reloadURL}');
}, ['liferay-util-window']);
</script>
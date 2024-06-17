<%@ include file="/jsp/init.jsp"%>
<portlet:renderURL var="viewFormURL" windowState="pop_up">
	<portlet:param name="view" value="viewForm"/>
</portlet:renderURL>
<portlet:renderURL var="electroURL" windowState="pop_up">
	<portlet:param name="view" value="viewElectroShop"/>
</portlet:renderURL>
<aui:button-row>
	<aui:a href="javascript:;" cssClass="btn btn-primary" title="action.add" onClick="javascript:editForm('tShop',0, '50%', '350px')">
		<liferay-ui:icon icon="plus" markupView="lexicon"/>
		<liferay-ui:message key="action.add"/>
	</aui:a>
</aui:button-row>
<liferay-ui:search-container searchContainer="${searchContainerList}" curParam="cur" deltaParam="delta">
	<liferay-ui:search-container-row modelVar="shopItem" className="ru.isands.test.estore.model.Shop">
		<liferay-ui:search-container-column-text name="shop.name" value="${shopItem.name}"/>
		<liferay-ui:search-container-column-text name="shop.address" value="${shopItem.address}"/>
		<liferay-ui:search-container-column-text>
			<liferay-ui:icon-menu message="actions">
				<c:if test="${shopItem.id>0}">
					<liferay-ui:icon image="edit" message="action.edit" url="javascript:;" onClick="javascript:editForm('tShop', ${shopItem.id}, '50%', '350px')" markupView="lexicon"/>
					<liferay-ui:icon icon="control-panel" message="show.electro" url="javascript:;" onClick="javascript:showElectro(${shopItem.id},'50%', '350px')" markupView="lexicon"/>
				</c:if>
			</liferay-ui:icon-menu>
		</liferay-ui:search-container-column-text>
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
<script>
	function editForm(tab, id, w, h) {
		var url = '${viewFormURL}&<portlet:namespace/>tab='+tab;
		if (id || id>0)
			url += '&<portlet:namespace/>id='+id;
		Liferay.Util.openWindow(
			{
				dialog: {
					destroyOnHide: true,
					height: h ? h : '50%',
					width: w ? w : '50%'
				},
				id: 'entityItem',
				dialogIframe: {},
				title: id ? '<liferay-ui:message key="title.edit"/>' : '<liferay-ui:message key="title.add"/>',
				uri: url
			}
		);
	}
	
	function showElectro(id, w, h) {
		Liferay.Util.openWindow({
			dialog: {
				destroyOnHide: true,
				height: h ? h : '50%',
				width: w ? w : '50%'
			},
			id: 'entityItem',
			dialogIframe: {},
			title: '<liferay-ui:message key="title.view"/>',
			uri: '${electroURL}&<portlet:namespace/>id='+id
		});
	}
</script>
<%@ include file="/jsp/init.jsp"%>
<%@ include file="/jsp/error.jsp"%>
<aui:form name="itemForm" autocomplete="off">
	<aui:input type="hidden" name="id" value="${item.id}"/>
	<clay:row>
		<clay:col sm="12">
			<aui:input type="text" name="name" label="shop.name" required="true" value="${item.name}"/>
		</clay:col>
	</clay:row>
	<clay:row>
		<clay:col sm="12">
			<aui:input type="text" name="address" label="shop.address" required="true" value="${item.address}"/>
		</clay:col>
	</clay:row>
	<aui:button-row>
		<aui:a href="javascript:;" cssClass="btn btn-primary" title="action.save" onClick="javascript:saveForm(this)">
			<liferay-ui:icon image="export"/>
			<liferay-ui:message key="action.save"/>
		</aui:a>
		<aui:a href="javascript:;" cssClass="btn btn-secondary" title="action.back" onClick="javascript:closeForm()">
			<liferay-ui:icon icon="undo" markupView="lexicon"/>
			<liferay-ui:message key="action.back"/>
		</aui:a>
	</aui:button-row>
</aui:form>
<liferay-portlet:resourceURL copyCurrentRenderParameters="false" var="saveFormURL" id="saveItem">
	<liferay-portlet:param name="tab" value="tShop"/>
</liferay-portlet:resourceURL>
<script>
	function saveForm(saveBtn) {
		saveBtn.className += ' disabled';
		sendFormByAjax('<portlet:namespace/>itemForm', '${saveFormURL}', 'errorContainer', 
				'<liferay-ui:message key="error.internal.request"/>', function(response) {
			if (response.errors) {
				viewErrors('errorContainer', response.errors);
				saveBtn.className = 'btn btn-primary';
			}
			else
				Liferay.Util.getOpener().reloadItem();
		});
	}
	
	function closeForm() {
		var dialog = Liferay.Util.getTop().Liferay.Util.Window.getById('entityItem');
		dialog.destroy();
	}
</script>
<%@ include file="/jsp/init.jsp"%>
<%@ include file="/jsp/error.jsp"%>
<jsp:useBean id="languageUtil" class="com.liferay.portal.kernel.language.LanguageUtil"/>
<jsp:useBean id="dateUtil" class="com.liferay.portal.kernel.util.DateUtil"/>
<jsp:useBean id="hintsUtil" class="com.liferay.portal.kernel.model.ModelHintsUtil"/>
<c:set var="className" value="ru.isands.test.estore.model.Employee"/>
<aui:fieldset label="${item.id>0 ? 'employee.edit' : 'employee.add'}">
<c:if test="${item.id>0}">
	<b>${item.getFullName()}</b>		
</c:if>
<aui:form name="itemForm" autocomplete="off">
	<aui:input type="hidden" name="id" value="${item.id}"/>
	<clay:row id="workAddress" cssClass="${item.id>0 ? '' : 'd-none'}">
		<clay:col sm="12">
			<div class="alert alert-info"><b><liferay-ui:message key="employee.workaddress"/>: </b><p id="addrValue" style="display:inline"><liferay-ui:message key="loading"/></p></div>
		</clay:col>
	</clay:row>
	<clay:row>
		<clay:col sm="4">
			<aui:input type="text" name="lastName" label="employee.lastname" required="true" value="${item.lastName}"
			maxlength="${hintsUtil.getMaxLength(className, 'lastName')}"/>
		</clay:col>
		<clay:col sm="4">
			<aui:input type="text" name="firstName" label="employee.firstname" required="true" value="${item.firstName}"
			maxlength="${hintsUtil.getMaxLength(className, 'firstName')}"/>
		</clay:col>
		<clay:col sm="4">
			<aui:input type="text" name="patronymic" label="employee.patronymic" required="true" value="${item.patronymic}"
			maxlength="${hintsUtil.getMaxLength(className, 'patronymic')}"/>
		</clay:col>
	</clay:row>
	<clay:row>
		<clay:col sm="2">
			<aui:input type="text" name="birthDate" label="employee.birthdate" required="true" value="${empty item.birthDate ? '' : dateUtil.getDate(item.birthDate, 'dd-MM-yyyy', themeDisplay.getLocale())}"
			placeholder="${languageUtil.get(pageContext.getRequest(), 'dd_MM_yyyy')}"/>
			<script>
				AUI().use('aui-datepicker',function(A) {
					new A.DatePicker({
						trigger: '#<portlet:namespace/>birthDate',
						popover: { zIndex: 1 },
						mask: '%d-%m-%Y',
						calendar: {maximumDate: new Date()}
					});
				});
			</script>
		</clay:col>
		<clay:col sm="2">
			<label for="<portlet:namespace/>genderMale"><liferay-ui:message key="employee.gender"/></label>
		    <aui:input type="radio" name="gender" id="genderMale" label="gender.male" value="male"/>
		    <aui:input type="radio" name="gender" id="genderFemale" label="gender.female" value="female"/>
		</clay:col>
		<script>
			document.getElementById('<portlet:namespace/>${item.gender eq true ? "genderFemale" : "genderMale"}').setAttribute('checked', 'checked');
		</script>
		<clay:col sm="4">
			<aui:select name="positionId" label="employee.position">
			<c:forEach var="position" items="${positionList}">
				<aui:option value="${position.id}" selected="${item.positionId eq position.id}">
					<c:out value="${position.name}"/>
				</aui:option>
			</c:forEach>
			</aui:select>
		</clay:col>
		<clay:col sm="4">
			<aui:select name="shopId" label="employee.shop" onChange="handleAddress(this)">
			</aui:select>
		</clay:col>
	</clay:row>
	<portlet:actionURL var="saveItemUrl" name="saveItem">
		<portlet:param name="id" value="${item.id}"/>
		<portlet:param name="tab" value="tEmp"/>
	</portlet:actionURL>
	<portlet:renderURL var="backUrl">
		<portlet:param name="tab" value="tEmp"/>
	</portlet:renderURL>
	<aui:button-row>
		<aui:a href="javascript:;" cssClass="btn btn-primary" title="action.save" onClick="javascript:saveForm(this)">
			<liferay-ui:icon image="export"/>
			<liferay-ui:message key="action.save"/>
		</aui:a>
		<aui:a href="${backUrl}" cssClass="btn btn-secondary" title="action.back">
			<liferay-ui:icon icon="undo" markupView="lexicon"/>
			<liferay-ui:message key="action.back"/>
		</aui:a>
	</aui:button-row>
</aui:form>
</aui:fieldset>
<liferay-portlet:resourceURL copyCurrentRenderParameters="false" var="getShopsURL" id="shops"/>
<script>
	var shops = [];
	getContentByAjax('${getShopsURL}', 'errorContainer', '<liferay-ui:message key="error.internal.request"/>', function(arr) {
		var select = document.getElementById('<portlet:namespace/>shopId');
		shops = arr;
		for (var i=0;i<arr.length;i++) {
			var elem = arr[i];
			select.innerHTML+='<option value="'+elem['id']+'" '+(${item.shopId}==elem['id'] ? 'selected="true"' : '')+'>'+elem['value']+'</option>';
			if (${item.shopId}==elem['id'])
				document.getElementById('addrValue').innerHTML = elem.address;
		}
	});
	
	function handleAddress(element) {
		var workAddress = document.getElementById('workAddress');
		workAddress.className = 'row';
		shops.forEach(function(shop) {
			if (element.value == shop.id) {
				document.getElementById('addrValue').innerHTML = shop.address;
				return;
			}
		});
	}
	
	function saveForm(saveBtn) {
		if (checkFormOnRequired('<portlet:namespace/>itemForm', 'errorContainer', '<liferay-ui:message key="error.check.required"/>')) {
			saveBtn.className += ' disabled';
			var itemForm = document.forms['<portlet:namespace/>itemForm'];
			itemForm.action = '${saveItemUrl}';
			itemForm.submit();
		}
	}
</script>
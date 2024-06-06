<%@ include file="/jsp/init.jsp"%>
<portlet:renderURL var="viewFormURL">
	<portlet:param name="view" value="viewForm"/>
	<portlet:param name="tab" value="${param.tab}"/>
</portlet:renderURL>
<aui:button-row>
	<aui:a href="${viewFormURL}" cssClass="btn btn-primary" title="action.add">
		<liferay-ui:icon icon="plus" markupView="lexicon"/>
		<liferay-ui:message key="action.add"/>
	</aui:a>
</aui:button-row>
<liferay-ui:search-container searchContainer="${searchContainerList}" curParam="cur" deltaParam="delta">
	<liferay-ui:search-container-row modelVar="empItem" className="ru.isands.test.estore.item.EmployeeItem">
		<liferay-ui:search-container-column-text name="employee.lastname" value="${empItem.getLastName()}"/>
		<liferay-ui:search-container-column-text name="employee.firstname" value="${empItem.getFirstName()}"/>
		<liferay-ui:search-container-column-text name="employee.patronymic" value="${empItem.getPatronymic()}"/>
		<liferay-ui:search-container-column-text name="employee.birthdate" value="${empItem.getBirthDate()}"/>
		<liferay-ui:search-container-column-text name="employee.position" value="${empItem.getPosition()}"/>
		<liferay-ui:search-container-column-text name="employee.shop" value="${empItem.getShop()}"/>
		<liferay-ui:search-container-column-text>
			<liferay-ui:icon-menu message="actions">
				<c:if test="${empItem.id>0}">
					<portlet:renderURL var="editFormURL">
						<portlet:param name="view" value="viewForm"/>
						<portlet:param name="tab" value="${param.tab}"/>
						<portlet:param name="id" value="${empItem.id}"/>
					</portlet:renderURL>
					<liferay-ui:icon image="edit" message="action.edit" url="${editFormURL}" markupView="lexicon"/>
				</c:if>
			</liferay-ui:icon-menu>
		</liferay-ui:search-container-column-text>
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
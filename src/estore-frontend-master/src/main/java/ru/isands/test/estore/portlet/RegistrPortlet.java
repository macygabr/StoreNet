package ru.isands.test.estore.portlet;

import ru.isands.test.estore.constants.EStoreKeys;
import ru.isands.test.estore.dto.EmpItemDTO;
import ru.isands.test.estore.dto.EmployeeDTO;
import ru.isands.test.estore.handler.Handler;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.util.ArrayList;

import javax.portlet.*;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;

/**
 * @author isands
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=estore",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.footer-portlet-javascript=/js/main.js",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.version=3.0",
		"javax.portlet.display-name=Реестры записей",
		"javax.portlet.name=" + EStoreKeys.REGISTR_PORTLET,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class RegistrPortlet extends CommonPortlet {
	
	/**
	 * Переопределение метода рендера страницы
	 * @param renderRequest - рендер-объект запроса
	 * @param renderResponse - рендер-объект ответа
	 * @exception IOException - исключение при выполнении операции ввода/вывода
	 * @exception PortletException - исключение при обращении к портлету
	 */
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		final String view = ParamUtil.getString(renderRequest, VIEW);
		if (!Validator.isBlank(view)) {
			if ("viewForm".equals(view))
				viewEditForm(renderRequest, renderResponse);
			return;
		}
		final String tab = ParamUtil.getString(renderRequest, TAB);
		if ("tEmp".equals(tab))
			viewListEmployee(renderRequest, renderResponse);
		include("/jsp/registr/view.jsp", renderRequest, renderResponse);
	}
	
	/**
	 * Рендер формы редактирования записи
	 * @param renderRequest - рендер-объект запроса
	 * @param renderResponse - рендер-объект ответа
	 * @exception IOException - исключение при выполнении операции ввода/вывода
	 * @exception PortletException - исключение при обращении к портлету
	 */
	private void viewEditForm(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		final String tab = ParamUtil.getString(renderRequest, TAB);
		if ("tEmp".equals(tab)) {
			final long id = ParamUtil.getLong(renderRequest, "id");
			renderRequest.setAttribute("item", new EmployeeDTO());
			include("/jsp/registr/employee/edit.jsp", renderRequest, renderResponse);
		}
	}
	
	/**
	 * Переопределение метода запроса ресурсов
	 * @param renderRequest - ресурс-объект запроса
	 * @param renderResponse - ресурс-объект ответа
	 * @exception IOException - исключение при выполнении операции ввода/вывода
	 * @exception PortletException - исключение при обращении к портлету 
	 */
	@Override
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws IOException, PortletException {
		super.serveResource(resourceRequest, resourceResponse);
		final String resourceId = resourceRequest.getResourceID();
		if ("shops".equals(resourceId)) {
			JSONArray arr = JSONFactoryUtil.createJSONArray();
			JSONObject obj = JSONFactoryUtil.createJSONObject();
			obj.put("id", 1);
			obj.put("value", "shop");
			obj.put("address", "address");
			arr.put(obj);
			writeJSON(resourceRequest, resourceResponse, arr);
		}
	}
	
	/**
	 * Метод сохранения записи
	 * @param actionRequest - объект запроса
	 * @param actionResponse - объект ответа
	 * @throws InvalidSyntaxException 
	 */
	public void saveItem(ActionRequest actionRequest, ActionResponse actionResponse) throws InvalidSyntaxException {
		final String tab = ParamUtil.getString(actionRequest, TAB);
		try  {
			final Bundle bundle = FrameworkUtil.getBundle(getClass());
			final BundleContext bundleContext = bundle.getBundleContext();
			ServiceReference<?>[] serviceRefs = bundleContext.getServiceReferences(Handler.class.getName(), "(handler.type="+tab+")");
			if (serviceRefs == null || serviceRefs.length<=0)
				throw new Exception("Не найден обработчик сохранения записи");
			final Handler handler = (Handler) bundleContext.getService(serviceRefs[0]);
			handler.readRequest(actionRequest);
			final String sessionId = actionRequest.getRequestedSessionId();
			if (!handler.hasErrors(sessionId)) {
				handler.validate(actionRequest, actionResponse);
				if (!handler.hasErrors(sessionId)) {
					handler.save(actionRequest, actionResponse);
					PortletURL url = createRedirectURL(actionRequest);
					url.getRenderParameters().setValue(VIEW, "viewForm");
					url.getRenderParameters().setValue(TAB, tab);
					url.getRenderParameters().setValue("id", handler.getIdValue(actionRequest));
					actionResponse.sendRedirect(url.toString());
				}
			}
			if (handler.hasErrors(sessionId)) {
				actionResponse.getRenderParameters().setValue(VIEW, "viewForm");
				actionResponse.getRenderParameters().setValue(TAB, tab);
				saveErrors(actionRequest, handler.getErrors(sessionId));
			}
		} catch (Exception e) {
			saveError(actionRequest, "Ошибка выполнения действия. Описание: "+e.getMessage());
			log.error(e);
		}
	}

	/**
	 * Метод поиска списка сотрудников и добавления в атрибут searchContainerList
	 * TODO: возможно сделать для гибкого использования для вывода разных реестров
	 * @param renderRequest - рендер-объект запроса
	 * @param renderResponse - рендер-объект ответа
	 */
	private void viewListEmployee(RenderRequest renderRequest, RenderResponse renderResponse) {
		Integer cur = ParamUtil.getInteger(renderRequest, CUR, SearchContainer.DEFAULT_CUR);
		Integer delta = ParamUtil.getInteger(renderRequest, DELTA, SearchContainer.DEFAULT_DELTA);
		PortletURL url = createRenderURL(renderRequest, renderResponse);
		url.getRenderParameters().setValue(TAB, ParamUtil.getString(renderRequest,TAB));
		url.getRenderParameters().setValue(CUR, ParamUtil.getString(renderRequest,CUR));
		ThemeDisplay themeDisplay = getThemeDisplay(renderRequest);
		SearchContainer<EmpItemDTO> searchContainer = new SearchContainer<EmpItemDTO>(renderRequest, null, null, CUR, cur, delta, url, null, LanguageUtil.get(themeDisplay.getLocale(),"emptyResultMessage"));
		searchContainer.setDeltaConfigurable(true);
		searchContainer.setDeltaParam(DELTA);
		searchContainer.setTotal(1);
		searchContainer.setResults(new ArrayList<EmpItemDTO>() {
			private static final long serialVersionUID = 1L;

			{
				add(new EmpItemDTO(1, "testov", "test", "testovich", "21-04-1992", "main shop", "director"));
			}
		});
		searchContainer.setIteratorURL(url);
		renderRequest.setAttribute("searchContainerList", searchContainer);
	}
}
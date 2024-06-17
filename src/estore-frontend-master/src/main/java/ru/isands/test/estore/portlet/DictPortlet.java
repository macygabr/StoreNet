package ru.isands.test.estore.portlet;

import ru.isands.test.estore.constants.EStoreKeys;
import ru.isands.test.estore.dto.ShopDTO;
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

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

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
		"javax.portlet.display-name=Справочники записей",
		"javax.portlet.name=" + EStoreKeys.DICT_PORTLET,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class DictPortlet extends CommonPortlet {
	
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
			else if ("viewElectroShop".equals(view))
				include("/jsp/dict/shop/electroview.jsp", renderRequest, renderResponse);
			return;
		}
		final String tab = ParamUtil.getString(renderRequest, TAB);
		if (Validator.isBlank(tab) || "tShop".equals(tab))
			viewListShop(renderRequest, renderResponse);
		include("/jsp/dict/view.jsp", renderRequest, renderResponse);
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
		if ("saveItem".equals(resourceId)) {
			writeJSON(resourceRequest, resourceResponse, saveItem(resourceRequest, resourceResponse));
		}
	}
	
	/**
	 * Метод сохранения записи
	 * @param resourceRequest - ресурс-объект запроса
	 * @param resourceResponse - ресурс-объект ответа
	 * @throws InvalidSyntaxException 
	 */
	private Object saveItem(ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
		JSONObject response = JSONFactoryUtil.createJSONObject();
		JSONArray errors = JSONFactoryUtil.createJSONArray();
		final String tab = ParamUtil.getString(resourceRequest, TAB);
		try  {
			final Bundle bundle = FrameworkUtil.getBundle(getClass());
			final BundleContext bundleContext = bundle.getBundleContext();
			ServiceReference<?>[] serviceRefs = bundleContext.getServiceReferences(Handler.class.getName(), "(handler.type="+tab+")");
			if (serviceRefs == null || serviceRefs.length<=0)
				throw new Exception("Не найден обработчик сохранения записи");
			final Handler handler = (Handler) bundleContext.getService(serviceRefs[0]);
			handler.readRequest(resourceRequest);
			final String sessionId = resourceRequest.getRequestedSessionId();
			if (!handler.hasErrors(sessionId)) {
				handler.validate(resourceRequest, resourceResponse);
				if (!handler.hasErrors(sessionId))
					handler.save(resourceRequest, resourceResponse);
			}
			if (handler.hasErrors(sessionId)) {
				handler.getErrors(sessionId).forEach(error -> errors.put(error));
			}
		} catch (Exception e) {
			errors.put("Ошибка выполнения действия. Описание: "+e.getMessage());
			log.error(e);
		}
		if (errors.length()>0)
			response.put("errors", errors);
		return response;
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
		if (Validator.isBlank(tab) || "tShop".equals(tab)) {
			final long id = ParamUtil.getLong(renderRequest, "id");
			ShopDTO shop = new ShopDTO();
			renderRequest.setAttribute("item", shop);
			include("/jsp/dict/shop/edit.jsp", renderRequest, renderResponse);
		}
	}
	
	/**
	 * Метод поиска списка магазинов и добавления в атрибут searchContainerList
	 * TODO: возможно сделать для гибкого использования для вывода разных реестров
	 * @param renderRequest - рендер-объект запроса
	 * @param renderResponse - рендер-объект ответа
	 */
	private void viewListShop(RenderRequest renderRequest, RenderResponse renderResponse) {
		Integer cur = ParamUtil.getInteger(renderRequest, CUR, SearchContainer.DEFAULT_CUR);
		Integer delta = ParamUtil.getInteger(renderRequest, DELTA, SearchContainer.DEFAULT_DELTA);
		PortletURL url = createRenderURL(renderRequest, renderResponse);
		url.getRenderParameters().setValue(TAB, ParamUtil.getString(renderRequest,TAB));
		url.getRenderParameters().setValue(CUR, ParamUtil.getString(renderRequest,CUR));
		ThemeDisplay themeDisplay = getThemeDisplay(renderRequest);
		SearchContainer<ShopDTO> searchContainer = new SearchContainer<ShopDTO>(renderRequest, null, null, CUR, cur, delta, url, null, LanguageUtil.get(themeDisplay.getLocale(),"emptyResultMessage"));
		searchContainer.setDeltaConfigurable(true);
		searchContainer.setDeltaParam(DELTA);
		searchContainer.setTotal(1);
		searchContainer.setResults(new ArrayList<ShopDTO>() {
			private static final long serialVersionUID = 1L;

			{
				add(new ShopDTO(1, "test shop", "address test"));
			}
		});
		searchContainer.setIteratorURL(url);
		renderRequest.setAttribute("searchContainerList", searchContainer);
	}

}
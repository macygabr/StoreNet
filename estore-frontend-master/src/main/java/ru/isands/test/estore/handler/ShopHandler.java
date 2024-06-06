package ru.isands.test.estore.handler;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.*;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import ru.isands.test.estore.dto.ShopDTO;

/**
 * @author isands
 */
@Component(
	property = { "handler.type=tShop"}, service = Handler.class
)
public class ShopHandler extends Handler {
	
	// можно использовать для генерации ид (инкрементом счетчика)
	@Reference
	CounterLocalService counterService;
	
	@Override
	public void read(PortletRequest portletRequest) {
		final ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		final String sessionId = portletRequest.getRequestedSessionId();
		if (themeDisplay == null) {
			addError("Внутренняя ошибка с порталом. Попробуйте отправить запрос позднее", sessionId, themeDisplay);
			return;
		}
		final long id = ParamUtil.getLong(portletRequest, "id");
		ShopDTO shop = new ShopDTO();
		shop.setId(id);
		shop.setName(ParamUtil.getString(portletRequest, "name").trim());
		shop.setAddress(ParamUtil.getString(portletRequest, "address").trim());
		portletRequest.setAttribute("item", shop);
	}

	@Override
	public void validate(PortletRequest portletRequest, PortletResponse portletResponse) {
		final ShopDTO item = (ShopDTO) portletRequest.getAttribute("item");
		final ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		final String sessionId = portletRequest.getRequestedSessionId();
		if (themeDisplay == null || item == null) {
			addError("Внутренняя ошибка с порталом. Попробуйте отправить запрос позднее", sessionId, themeDisplay);
			return;
		}
		if (Validator.isBlank(item.getName()))
			addError("shop.name.error.null", sessionId, themeDisplay);
		if (Validator.isBlank(item.getAddress()))
			addError("shop.address.error.null", sessionId, themeDisplay);
	}
	
	@Override
	public void save(PortletRequest portletRequest, PortletResponse portletResponse) {
		// здесь идет запрос к микросервису, либо можно изменить структуру интерфейса и сделать отправку данных на более раннем этапе
	}

	@Override
	public String getIdValue(ActionRequest actionRequest) {
		final ShopDTO item = (ShopDTO) actionRequest.getAttribute("item");
		return String.valueOf(item.getId());
	}
	
}
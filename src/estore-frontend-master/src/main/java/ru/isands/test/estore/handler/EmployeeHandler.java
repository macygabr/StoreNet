package ru.isands.test.estore.handler;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.*;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import ru.isands.test.estore.dto.EmployeeDTO;


/**
 * @author isands
 */
@Component(
	property = { "handler.type=tEmp"}, service = Handler.class
)
public class EmployeeHandler extends Handler {
	
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
		EmployeeDTO employee = new EmployeeDTO();
		employee.setId(id);
		employee.setLastName(ParamUtil.getString(portletRequest, "lastName"));
		employee.setFirstName(ParamUtil.getString(portletRequest, "firstName"));
		employee.setPatronymic(ParamUtil.getString(portletRequest, "patronymic"));
		employee.setBirthDate(ParamUtil.getString(portletRequest, "birthDate"));
		employee.setGender("genderMale".equals(ParamUtil.getString(portletRequest, "gender")));
		employee.setPositionId(ParamUtil.getLong(portletRequest, "positionId"));
		employee.setShopId(ParamUtil.getLong(portletRequest, "shopId"));
		portletRequest.setAttribute("item", employee);
	}

	@Override
	public void validate(PortletRequest portletRequest, PortletResponse portletResponse) {
		final EmployeeDTO item = (EmployeeDTO) portletRequest.getAttribute("item");
		final ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		final String sessionId = portletRequest.getRequestedSessionId();
		if (themeDisplay == null || item == null) {
			addError("Внутренняя ошибка с порталом. Попробуйте отправить запрос позднее", sessionId, themeDisplay);
			return;
		}
		if (Validator.isBlank(item.getLastName()))
			addError("employee.lastname.error.null", sessionId, themeDisplay);
		if (Validator.isBlank(item.getFirstName()))
			addError("employee.firstname.error.null", sessionId, themeDisplay);
		if (Validator.isBlank(item.getPatronymic()))
			addError("employee.patronymic.error.null", sessionId, themeDisplay);
		if (Validator.isBlank(item.getBirthDate()))
			addError("employee.birthdate.error.null", sessionId, themeDisplay);
		if (item.getShopId()<=0)
			addError("employee.shop.error.null", sessionId, themeDisplay);
	}
	
	@Override
	public void save(PortletRequest portletRequest, PortletResponse portletResponse) {
		// здесь идет запрос к микросервису, либо можно изменить структуру интерфейса и сделать отправку данных на более раннем этапе
	}

	@Override
	public String getIdValue(ActionRequest actionRequest) {
		final EmployeeDTO item = (EmployeeDTO) actionRequest.getAttribute("item");
		return String.valueOf(item.getId());
	}
	
}
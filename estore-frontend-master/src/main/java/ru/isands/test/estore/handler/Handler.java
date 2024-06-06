package ru.isands.test.estore.handler;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public abstract class Handler {
	
	protected static Log log = LogFactoryUtil.getLog(Handler.class);
	
	Map<String, List<String>> errorMap;
	
	/**
	 * Чтение данных из формы и запись объекта в атрибут (с очисткой ошибок в сессии)
	 * @param portletRequest - объект запроса
	 */
	public void readRequest(PortletRequest portletRequest) {
		cleanSessionErrors(portletRequest.getRequestedSessionId());
		read(portletRequest);
	}
	
	/**
	 * Чтение данных из формы и запись объекта в атрибут
	 * @param portletRequest - объект запроса
	 */
	public abstract void read(PortletRequest portletRequest);
	
	/**
	 * Проверка записанного атрибута критериям соостветствия
	 * @param portletRequest - объект запроса
	 * @param portletResponse - объект ответа
	 */
	public abstract void validate(PortletRequest portletRequest, PortletResponse portletResponse);
	
	/**
	 * Сохранение объекта в базу данных
	 * @param portletRequest - объект запроса
	 * @param portletResponse - объект ответа
	 */
	public abstract void save(PortletRequest portletRequest, PortletResponse portletResponse);
	
	/**
	 * Признак присутствия ошибок
	 * @return true/false
	 */
	public boolean hasErrors(String sessionId) {
		return errorMap != null && errorMap.containsKey(sessionId);
	}
	
	/**
	 * Возврат списка ошибок
	 * @return список
	 */
	public List<String> getErrors(String sessionId) {
		return errorMap.getOrDefault(sessionId, new ArrayList<String>());
	}
	
	/**
	 * Добавление ошибки в список обработчика
	 * @param error - код ошибки
	 * @param sessionId - идентификатор сессии
	 * @param themeDisplay - объект для перевода текста ошибки
	 */
	public void addError(String error, String sessionId, ThemeDisplay themeDisplay) {
		if (errorMap == null)
			errorMap = new HashMap<String, List<String>>();
		List<String> sessionErrors = errorMap.getOrDefault(sessionId, new ArrayList<String>());
		sessionErrors.add(themeDisplay != null ? LanguageUtil.get(themeDisplay.getRequest(), error) : error);
		errorMap.put(sessionId, sessionErrors);
	}

	/**
	 * Возвращает значение идентификатора записи
	 * @param actionRequest - объект запроса
	 * @return строка
	 */
	public abstract String getIdValue(ActionRequest actionRequest);
	
	protected void cleanSessionErrors(String sessionId) {
		if (hasErrors(sessionId))
			errorMap.remove(sessionId);
	}
}

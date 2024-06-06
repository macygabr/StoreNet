package ru.isands.test.estore.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.*;

import java.io.IOException;
import java.util.*;
import javax.portlet.*;

public class CommonPortlet extends MVCPortlet {
	
	protected Log log = LogFactoryUtil.getLog(CommonPortlet.class);

	public static final String ERROR_PAGE = "/jsp/error.jsp";

	public static final String VIEW = "view";
	public static final String ACTION = "action";
	public static final String RESOURCE = "resource";
	public static final String IDENTIFIER = "identifier";
	public static final String VIEW_LIST = "viewList";
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String REDIRECT_VIEW = "redirectView";
	public static final String VIEW_BUILDER = "viewBuilder";
	public static final String BLANK = "";

	public static final String SESSION_ERRORS = "sessionErrors";
	public static final String EMPTY_RESULT_MESSAGE = "emptyResultMessage";

	public static final String SEARCH_CONTAINER = "searchContainer";
	public static final String SEARCH_CONTAINER_TOTAL = "searchContainerTotal";
	public static final String SEARCH_CONTAINER_RESULTS = "searchContainerResults";
	public static final String SEARCH_CONTAINER_ITERATOR_URL = "searchContainerIteratorURL";

	public static final String CUR = "cur";
	public static final String DELTA = "delta";
	public static final String TAB = "tab";
	public static final String ORDER_BY_COL = "orderByCol";
	public static final String ORDER_BY_TYPE = "orderByType";

	public static final String CUR1 = "cur1";
	public static final String DELTA1 = "delta1";
	public static final String ORDER_BY_COL1 = "orderByCol1";
	public static final String ORDER_BY_TYPE1 = "orderByType1";

	public static final String ASC = "asc";
	public static final String DESC = "desc";
	public static final String SEARCH = "search";
	public static final String LIMIT = "limit";
	public static final String OFFSET = "offset";

	public static final int DEFAULT_LIMIT = 20;
	public static final int DEFAULT_OFFSET = 0;
	public static final String EMPTY_STRING = "";

	private static final String ERROR_THEME_DISPLAY_NOT_FOUND = "error.portlet.themeDisplay.notFound";

	private final Set<String> transitParameters = Collections.synchronizedSet(new HashSet<String>());

	@Override
	public final void init() throws PortletException {
		super.init();
		addTransitParameters(CUR, DELTA, ORDER_BY_COL, ORDER_BY_TYPE);
		initialize();
	}

	void initialize() {
	}

	@Override
	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
			throws IOException, PortletException {
		updateRenderParameters(actionRequest, actionResponse, transitParameters);
		super.processAction(actionRequest, actionResponse);
	}

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		copyParametersToAttributes(renderRequest, renderResponse, transitParameters);
		SessionMessages.add(renderRequest,
				PortalUtil.getPortletId(renderRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		renderRequest.setAttribute(SESSION_ERRORS, SessionErrors.keySet(renderRequest));
		super.render(renderRequest, renderResponse);
	}

	void addTransitParameters(String... parameters) {
		for (String parameter : parameters) {
			transitParameters.add(parameter);
		}
	}

	protected ThemeDisplay getThemeDisplay(PortletRequest portletRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		if (themeDisplay == null)
			throw new RuntimeException(ERROR_THEME_DISPLAY_NOT_FOUND);
		return themeDisplay;
	}

	void copyParametersToAttributes(RenderRequest renderRequest, RenderResponse portletResponse, Set<String> names) {
		RenderParameters params = renderRequest.getRenderParameters();
		if (params == null)
			return;
		for (String name : names) {
			if (renderRequest.getAttribute(name) != null)
				continue;
			String value = params.getValue(name);
			if (!Validator.isBlank(value))
				renderRequest.setAttribute(name, value);
		}
	}

	void updateRenderParameters(ActionRequest actionRequest, ActionResponse actionResponse, Set<String> names) {
		for (String name : names) {
			String value = actionRequest.getActionParameters().getValue(name);
			if (!Validator.isBlank(value))
				actionResponse.getRenderParameters().setValue(name, value);
		}
	}

	protected PortletURL createRenderURL(RenderRequest renderRequest, RenderResponse renderResponse) {
		PortletURL renderURL = renderResponse.createRenderURL();
		for (String transitParameter : transitParameters) {
			String parameterValue = GetterUtil.getString(renderRequest.getAttribute(transitParameter));
			if (!Validator.isBlank(parameterValue))
				renderURL.getRenderParameters().setValue(transitParameter, parameterValue);
		}
		return renderURL;
	}

	protected PortletURL createRedirectURL(ActionRequest actionRequest) {
		String portletName = (String) actionRequest.getAttribute(WebKeys.PORTLET_ID);
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		PortletURL redirectURL = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(actionRequest),
				portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
		return redirectURL;
	}

	protected void saveMessage(PortletRequest portletRequest, String message) {
		if (message == null) {
			return;
		}
		SessionMessages.add(portletRequest, message);
	}

	protected void saveError(PortletRequest portletRequest, String errorMessage) {
		if (errorMessage == null || errorMessage.isEmpty()) {
			return;
		}
		SessionErrors.add(portletRequest, errorMessage);
	}

	protected void saveErrors(PortletRequest portletRequest, List<String> errors) {
		if (errors == null || errors.isEmpty()) {
			return;
		}
		for (String error : errors) {
			saveError(portletRequest, error);
		}
	}

	protected void saveError(PortletRequest portletRequest, Exception exception) {
		saveError(portletRequest, exception.getMessage());
	}

	protected void doViewError(RenderRequest renderRequest, RenderResponse renderResponse, Exception exception) {
		try {
			renderResponse.resetBuffer();
			saveError(renderRequest, exception);
			include(ERROR_PAGE, renderRequest, renderResponse);

		} catch (Exception error) {
			saveError(renderRequest, error);
		}
	}

	protected void hideDefaultErrorMessage(ActionRequest actionRequest) {
		PortletConfig portletConfig = (PortletConfig) actionRequest.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(actionRequest,
				portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
	}

}

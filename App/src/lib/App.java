package lib;

import java.util.ArrayList;
import java.util.Arrays;

public class App {
	private static class AppData {
		String component;
		boolean isApiRequest;
	}

	private static AppData data = new AppData();

	private static Request processRequest() throws ClassNotFoundException {
		boolean isApiRequest = false;
		String requestUrl = Helpers.getRequestURL();
		if (requestUrl.length() > 0) {
			requestUrl = StringFilters.sanitizeUrl(Helpers.chopIff(requestUrl, "/"));
			if (requestUrl.substring(0, 3).equals("api")) {
				if (requestUrl.length() == 3) {
					isApiRequest = true;
					requestUrl = "";
				} else if (requestUrl.charAt(3) == '/') {
					isApiRequest = true;
					requestUrl = requestUrl.substring(4);
				}
			}
		} else requestUrl = "";

		String[] requestParts = requestUrl.split("/");

		char[] componentName;
		if (requestParts.length > 0) {
			componentName = requestParts[0].toCharArray();
			componentName[0] = Character.toUpperCase(componentName[0]);
		} else componentName = "Pages".toCharArray();
		Class.forName(Arrays.toString(componentName));

		char[] methodName;
		if (requestParts.length > 1) {
			methodName = requestParts[1].toCharArray();
			if (!Arrays.toString(methodName).equals("index")) {
				methodName[0] = Character.toUpperCase(methodName[0]);
				componentName[0] = Character.toLowerCase(componentName[0]);
				Class.forName("java." + Arrays.toString(componentName) + "." + Arrays.toString(methodName));
				componentName[0] = Character.toUpperCase(componentName[0]);
			}
		} else methodName = "index".toCharArray();

		ArrayList<String> params;
		if (requestParts.length > 2)
			params = new ArrayList<>(Arrays.asList(requestParts).subList(2, requestParts.length));
		else params = null;

		return new Request(requestUrl, isApiRequest, Arrays.toString(componentName), Arrays.toString(methodName), params);
	}

	public static void start() throws ClassNotFoundException {
		Request request = App.processRequest();

		data.component = request.getComponent();
		data.isApiRequest = request.getApiRequest();

		Session.create();

		LoginSessions.init();

		App.dispatchMethod(request.getMethod(), request.getParams());
	}

	public static void dispatchMethod(String method, ArrayList<String> params) {

	}
}

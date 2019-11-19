package lib;

import helpers.Filters;
import helpers.Requests;
import helpers.Strings;

import java.util.ArrayList;
import java.util.Arrays;

public class App {
	private static class AppData {
		Request request;
	}

	private static class Request {
		String url;
		boolean isApiRequest;
		String component;
		String method;
		ArrayList<String> params;

		Request(String url, Boolean isAPIRequest, String component, String method, ArrayList<String> params) {
			this.url = url;
			this.isApiRequest = isAPIRequest;
			this.component = component;
			this.method = method;
			this.params = params;
		}
	}

	public static AppData data = new AppData();

	private static String getFQComponentName(char[] componentName) {
		componentName[0] = Character.toUpperCase(componentName[0]);
		return "component." + Arrays.toString(componentName);
	}

	private static String getFQMethodName(char[] componentName, char[] methodName) {
		componentName[0] = Character.toLowerCase(componentName[0]);
		methodName[0] = Character.toUpperCase(methodName[0]);
		return "component." + Arrays.toString(componentName) + ".methods." + Arrays.toString(methodName);
	}

	private static Request processRequest() throws Exception {
		boolean isApiRequest = false;
		String requestUrl = Requests.getRequestURL();
		if (requestUrl.length() > 0) {
			requestUrl = Filters.sanitizeUrl(Strings.chopIff(requestUrl, "/"));
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

		/*
		  Component name could be
		  empty (whole request is empty)
		  name of one of the base classes
		 */
		char[] componentName;
		if (requestParts.length > 0)
			componentName = requestParts[0].toCharArray();
		else componentName = "Pages".toCharArray();
		Class.forName(getFQComponentName(componentName));

		/*
		  Method name could be:
		  index (declared in base classes)
		  names of child classes
		 */
		char[] methodName;
		if (requestParts.length > 1) {
			methodName = requestParts[1].toCharArray();
			if (!Arrays.toString(methodName).equals("index"))
				Class.forName(getFQMethodName(componentName, methodName));
		} else methodName = "index".toCharArray();

		ArrayList<String> params;
		if (requestParts.length > 2)
			params = new ArrayList<>(Arrays.asList(requestParts).subList(2, requestParts.length));
		else params = null;

		return new Request(requestUrl, isApiRequest, getFQComponentName(componentName), getFQMethodName(componentName, methodName), params);
	}

	public static void start() throws Exception {
		data.request = App.processRequest();

		Session.create();

		LoginSessions.init();

		App.dispatchMethod();
	}

	public static void dispatchMethod() throws Exception {
		dispatchMethod(data.request.component, data.request.method, data.request.params);
	}

	public static void dispatchMethod(String componentName, String methodName, ArrayList<String> params) throws Exception {
		if (methodName.equals("index")) {
			if (params == null)
				Class.forName(componentName).getMethod("index").invoke(
						Class.forName(componentName).getConstructor().newInstance());
			else
				Class.forName(componentName).getMethod("index", ArrayList.class).invoke(
						Class.forName(componentName).getConstructor().newInstance(), params);
		} else {
			if (params == null)
				Class.forName(methodName).getConstructor().newInstance();
			else
				Class.forName(methodName).getConstructor(ArrayList.class).newInstance(params);
		}
		Response.render();
	}
}

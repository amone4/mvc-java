import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class App {
	private static HashMap<String, String> data;

	private static void processRequest() throws ClassNotFoundException {
		Request request = new Request(
				"",
				false,
				"Pages",
				"index",
				new ArrayList<>()
		);

		String requestUrl = Helpers.getRequestURL();
		if (requestUrl.length() > 0) {
			requestUrl = StringFilters.sanitizeUrl(Helpers.chopIff(request.getUrl(), "/"));
			if (requestUrl.substring(0, 3).equals("api")) {
				if (requestUrl.length() == 3) {
					request.setAPIRequest(true);
					requestUrl = "";
				} else if (requestUrl.charAt(3) == '/') {
					request.setAPIRequest(false);
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
			}
		} else methodName = "index".toCharArray();

		ArrayList<String> params;
		if (requestParts.length > 2)
			params = new ArrayList<>(Arrays.asList(requestParts).subList(2, requestParts.length));
		else params = null;
	}

	public static void start() throws ClassNotFoundException {
		App.processRequest();
	}

	public static void stop() {}
}

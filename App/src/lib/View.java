package lib;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

abstract public class View {
	private boolean htmlTypeChecker(Object... args) {
		try {
			for (Method method : Class.forName(String.valueOf(this.getClass())).getDeclaredMethods())
				if (method.getName().equals("html")) {
					Type[] parameters = method.getGenericParameterTypes();
					if (parameters.length == args.length) {
						boolean isCorrectMethod = true;
						for (int i = 0; isCorrectMethod && i < parameters.length; i++)
							if (!parameters[i].getTypeName().equals(String.valueOf(args[i].getClass())))
								isCorrectMethod = false;
						if (isCorrectMethod)
							return true;
					}
				}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected abstract String htmlCaller(Object... args);

	public String getHtml(Object... args) {
		if (this.htmlTypeChecker(args))
			return this.htmlCaller(args);
		return "";
	}

	protected String htmlAttr(String key, String val) {
		return key + "=\"" + val + "\"";
	}
}

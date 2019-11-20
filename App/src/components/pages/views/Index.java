package components.pages.views;

import lib.View;

import java.util.ArrayList;

public class Index extends View {
	private String html(ArrayList<String> list, int ip) {
		return "";
	}

	@Override
	protected String htmlCaller(Object... args) {
		return this.html(
				(ArrayList<String>) args[0],
				(int) args[1]
		);
	}
}

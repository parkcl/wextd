package org.wextd.wp.config_parsers;

public class ParserFactory {

	public static ParseStrategy create(String type) {
		if (type.equals(ParseStrategy.JSON)) {
			return new GsonJsonParse();
		}

		return null;
	}
}

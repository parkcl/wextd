package org.wextd.wp.config_parsers;

import java.io.File;
import java.util.Map;

import org.wextd.wp.exceptions.NoConfigFileFoundException;

public interface ParseStrategy {
	public static final String JSON = "json";

	public Map<String, Object> parse(File toParse);
}

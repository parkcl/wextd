package org.wextd.wp.daemon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wextd.wp.config_parsers.ParseStrategy;
import org.wextd.wp.config_parsers.ParserFactory;
import org.wextd.wp.exceptions.NoWatchPathsAvailableException;
import org.wextd.wp.util.FileUtil;

public class KeyRenderer {
	public static final String WATCH_KEY = "watchPath";
	protected Map<String, Object> configMappings;
	private static KeyRenderer kr = null;
	private final Logger log = LoggerFactory.getLogger(DefaultFileWatcher.class);
	private final static String CONFIG_FILE_NAME = "config.json";

	private KeyRenderer() {
		init();
	}

	private void init() {
		ParseStrategy ps = ParserFactory.create(ParseStrategy.JSON);
		configMappings = ps.parse(new File(CONFIG_FILE_NAME));
	}

	public static KeyRenderer getInstance() {
		if (kr == null) {
			kr = new KeyRenderer();
		}

		return kr;
	}

	public List<File> getWatchFiles() throws NoWatchPathsAvailableException {
		ArrayList<File> al = new ArrayList<File>();

		if (configMappings.get(WATCH_KEY) == null) {
			throw new NoWatchPathsAvailableException();
		}

		@SuppressWarnings("unchecked")
		ArrayList<String> tmp = (ArrayList<String>) configMappings.get(WATCH_KEY);

		for (String path : tmp) {
			if (FileUtil.isFile(path)) {
				al.add(new File(path));
			} else {
				log.warn("Found invalid path -> `" + path + "'.");
			}
		}

		return al;
	}

	public Map<String, Object> getMappings() {
		return configMappings;
	}

	public Map<String, Object> getMappings(String key) {
		return null;
	}

}

package org.wextd.wp.plugins.arch;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.util.Map;

public abstract class PluginProcessor {
	public abstract void runPlugins(Path path, Kind<Path> entryCreate, Map<String, Object> mappings);
}

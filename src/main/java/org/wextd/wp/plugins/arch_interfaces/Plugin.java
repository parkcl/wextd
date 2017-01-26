package org.wextd.wp.plugins.arch_interfaces;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.util.Map;

public interface Plugin {
	public PluginValue execute(Path path, Kind<Path> event, Map<String, Object> mappings);
}

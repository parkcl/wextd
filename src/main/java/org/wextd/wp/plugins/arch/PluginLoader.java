package org.wextd.wp.plugins.arch;

import java.util.List;
import java.util.Map;

import org.wextd.wp.plugins.arch_interfaces.Plugin;

public interface PluginLoader {
	public static final String PLUGIN_DIR = "plugins";
	public Map<String, List<Plugin>> loadPlugins();
}

package org.wextd.wp.plugins.arch;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.util.List;
import java.util.Map;

import org.wextd.wp.plugins.arch_interfaces.Plugin;
import org.wextd.wp.plugins.arch_interfaces.PluginBooleanValue;
import org.wextd.wp.plugins.arch_interfaces.PostPlugin;
import org.wextd.wp.plugins.arch_interfaces.PreConditionedPlugin;

public class DefaultPluginProcessor extends PluginProcessor {

	private PluginLoader pl;

	public DefaultPluginProcessor() {
		pl = new DefaultPluginLoader();
	}

	@Override
	public void runPlugins(Path path, Kind<Path> event, Map<String, Object> mappings) {
		byte preconditionsMet = 1;
		Map<String, List<Plugin>> plugins = pl.loadPlugins();
		System.out.println(plugins);
		List<Plugin> preconditionals = plugins.get(PreConditionedPlugin.class.getSimpleName()),
				postList = plugins.get(PostPlugin.class.getSimpleName());

		if (preconditionals != null) {
			for (Plugin prePlugin : preconditionals) {
				preconditionsMet &= PluginBooleanValue
						.toInt((PluginBooleanValue) prePlugin.execute(path, event, mappings));
			}
		}

		if (postList != null && preconditionsMet == 1) {
			for (Plugin plugin : postList) {
				try {
					plugin.execute(path, event, mappings);
				} catch (Exception e) {
				}
			}
		}

	}

}

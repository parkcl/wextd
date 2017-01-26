package org.wextd.wp.plugins.arch;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wextd.wp.plugins.arch_interfaces.Plugin;
import org.wextd.wp.plugins.arch_interfaces.PostPlugin;
import org.wextd.wp.plugins.arch_interfaces.PreConditionedPlugin;
import org.wextd.wp.readers.ManifestReader;

public class DefaultPluginLoader implements PluginLoader {

	private final Logger log = LoggerFactory.getLogger(DefaultPluginLoader.class);
	public static final String PLUGIN_KEY = "plugins";
	public static final String PRE_PLGIN_KEY = "pre";
	protected static final String JAR_SUFFIX = ".jar";

	public Object loadInstance(URL[] urls, String className) {
		try {
			log.info("attempting to retrieve class " + className + " from ");

			URLClassLoader child = new URLClassLoader(urls, this.getClass().getClassLoader());
			Class<?> classToLoad = Class.forName(className, true, child);
			return classToLoad.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, List<Plugin>> loadPlugins() {
		Map<String, List<Plugin>> plugins = new HashMap<>();
		File plugs = new File(PLUGIN_DIR);
		File[] jars = plugs.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.toString().endsWith(JAR_SUFFIX);
			}
		});

		ManifestReader mr = new ManifestReader();

		for (File jar : jars) {
			URL jarUrl;
			try {

				if (jar != null) {
					jarUrl = jar.toURI().toURL();
					URL[] urls = { jarUrl };

					Plugin result = (Plugin) loadInstance(urls, mr.getAttribute(jarUrl, ManifestReader.IMPL_CLAS_ATTR));

					log.info("loaded Plugin '" + result.getClass() + "'");

					if (isPreConditionedPlugin(result)) {
						if (plugins.get(PreConditionedPlugin.class.getSimpleName()) == null) {
							plugins.put(PreConditionedPlugin.class.getSimpleName(), new ArrayList<Plugin>());
						}

						plugins.get(PreConditionedPlugin.class.getSimpleName()).add((Plugin) result);
					} else if (isPostPlugin(result)) {
						if (plugins.get(PostPlugin.class.getSimpleName()) == null) {
							plugins.put(PostPlugin.class.getSimpleName(), new ArrayList<Plugin>());
						}

						plugins.get(PostPlugin.class.getSimpleName()).add((Plugin) result);
						log.info("added " + result + " to list");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return plugins;
	}

	private boolean isPostPlugin(Plugin result) {
		System.out.println(result.getClass().getSuperclass());
		for (Class<?> cl : result.getClass().getInterfaces()) {
			System.out.println(result + ", " + cl);
			if (cl.equals(PostPlugin.class.getSimpleName())) {
				return true;
			}
		}

		return true;
	}

	private boolean isPreConditionedPlugin(Plugin result) {
		for (Class<?> cl : result.getClass().getInterfaces()) {
			System.out.println(result + ", " + cl);
			if (cl.equals(PreConditionedPlugin.class)) {
				return true;
			}
		}

		return false;
	}

}

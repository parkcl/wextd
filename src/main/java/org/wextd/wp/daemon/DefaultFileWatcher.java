package org.wextd.wp.daemon;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wextd.wp.exceptions.NoWatchPathsAvailableException;
import org.wextd.wp.plugins.arch.DefaultPluginProcessor;
import org.wextd.wp.plugins.arch.PluginProcessor;

public class DefaultFileWatcher extends AbstractFileWatcher {

	private List<File> watchFiles;
	private WatchService dwatch;
	private Thread watchThread;
	private final Logger log = LoggerFactory.getLogger(DefaultFileWatcher.class);
	private PluginProcessor proc;
	private Map<String, Object> configMappings;
	private boolean stopped = false;

	public DefaultFileWatcher() {
		init();
	}

	public void init() {
		configMappings = KeyRenderer.getInstance().getMappings();

		proc = new DefaultPluginProcessor();
		try {
			watchFiles = KeyRenderer.getInstance().getWatchFiles();
			dwatch = FileSystems.getDefault().newWatchService();

			watchThread = new Thread() {
				@Override
				public synchronized void start() {
					DefaultFileWatcher.this.stopped = false;
					super.start();
				}

				@Override
				public void run() {
					while (!stopped) {
						for (File file : watchFiles) {
							if (file.isDirectory()) {
								registerItem(file);
							}
						}

						watch();
					}
				}
			};
		} catch (NoWatchPathsAvailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		watchThread.start();
	}

	@SuppressWarnings("unchecked")
	private void watch() {
		while (true) {
			WatchKey key;

			try {
				key = dwatch.take();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();

				Path dir = (Path) key.watchable();
				Path fileName = dir.resolve((Path) event.context());

				log.info("New event, " + kind + " from " + fileName);

				try {
					proc.runPlugins(fileName, (Kind<Path>) kind, configMappings);
				} catch (Exception e) {

				}
			}

			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}

	private void registerItem(File file) {
		try {
			Paths.get(file.getAbsolutePath()).register(dwatch, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			log.info("registered " + file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addWatchItem(File toWatch) {
		watchFiles.add(toWatch);
		registerItem(toWatch);
	}

	@Override
	public void restart() throws Exception {
		try {
			stop();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		start();
		log.info("restarted service");
	}

	@Override
	public void stop() throws Exception {
		stopped = true;
		try {
			watchThread.join(1000);
			log.info("service interrupted");
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			throw e;
		}
	}

	@Override
	public void destroy() {
		watchThread = null;
	}

}

package org.wextd.wp.daemon;

public class WatchDaemonFactory {

	public static AbstractFileWatcher getWatcher() {
		return new DefaultFileWatcher();
	}

}

package org.wextd.wp.daemon;

public class DaemonLauncher {

	public static void main(String[] args) {
		WatchDaemonFactory.getWatcher().start();
	}
}

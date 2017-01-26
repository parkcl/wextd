package org.wextd.wp.daemon;

import java.io.File;
import java.util.Observable;

public abstract class AbstractFileWatcher extends Observable  {

	public abstract void start();

	public abstract void addWatchItem(File toWatch);

	public abstract void restart() throws Exception;

	public abstract void stop() throws Exception;

	public abstract void destroy();
}

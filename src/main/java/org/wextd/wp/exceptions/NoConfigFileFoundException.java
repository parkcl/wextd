package org.wextd.wp.exceptions;

public class NoConfigFileFoundException extends Exception {

	private static final long serialVersionUID = 3516987072703165149L;
	private static final String ERR_TEXT = "Failed to find a configuration file. "
			+ "Please ensure you have a configuration file [config.json] in the directory of the daemon.";

	public NoConfigFileFoundException() {
		super(ERR_TEXT);
	}
}

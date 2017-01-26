package org.wextd.wp.exceptions;

public class JsonConfigFileEmptyException extends Exception {
	private static final long serialVersionUID = -4628641028184574015L;

	public JsonConfigFileEmptyException() {
		super("Empty file. Please populate the file manually or via config.py");
	}
}

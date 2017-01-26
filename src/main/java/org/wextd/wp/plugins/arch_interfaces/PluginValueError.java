package org.wextd.wp.plugins.arch_interfaces;

public class PluginValueError implements PluginValue {

	private String msg;
	public PluginValueError(String msg) {
		this.msg = msg;
	}

	public String getValue() {
		return msg;
	}
}

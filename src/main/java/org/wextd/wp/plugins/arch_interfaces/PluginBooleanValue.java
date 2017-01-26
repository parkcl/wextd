package org.wextd.wp.plugins.arch_interfaces;

public final class PluginBooleanValue implements PluginValue {
	private boolean v;

	public PluginBooleanValue(boolean v) {
		this.v = v;
	}

	public boolean getValue() {
		return v;
	}

	public void setValue(boolean v) {
		this.v = v;
	}

	@Override
	public String toString() {
		return "PluginBooleanValue -> (" + v + ")";
	}

	public static int toInt(PluginBooleanValue b) {
		return b.getValue() == true ? 1 : 0;
	}
}

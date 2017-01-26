package org.wextd.wp.util;

import java.io.File;

public class FileUtil {

	public static boolean isFile(String path) {
		return new File(path).exists();
	}

}

package org.wextd.wp.readers;

import java.io.FileInputStream;
import java.net.URL;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wextd.wp.daemon.DefaultFileWatcher;

public class ManifestReader implements ConfigReader {

	private static final Logger log = LoggerFactory.getLogger(DefaultFileWatcher.class);
	public static final String DEFAULT_MAN_PATH = "META-INF/MANIFEST.MF";
	public static final String PLGIN_ORDER = "Plugin-order", IMPL_CLAS_ATTR = "Plugin-impl-class";

	public static Manifest read(URL u) {
		try {
			log.info("attempting to read " + u);

			JarInputStream jarStream = new JarInputStream(new FileInputStream(u.getFile()));
			Manifest mf = jarStream.getManifest();

			jarStream.close();
			return mf;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getAttribute(URL jarUrl, String attr) {
		Manifest m = read(jarUrl);

		return m.getMainAttributes().getValue(attr);
	}

}

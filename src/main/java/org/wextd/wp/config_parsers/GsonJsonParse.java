package org.wextd.wp.config_parsers;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wextd.wp.exceptions.JsonConfigFileEmptyException;
import org.wextd.wp.exceptions.NoConfigFileFoundException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GsonJsonParse implements JsonParseStrategy {
	private Gson gson;
	private JsonObject root;
	private final Logger log = LoggerFactory.getLogger(GsonJsonParse.class);

	public GsonJsonParse() {
		gson = new Gson();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> parse(File toParse) {
		try {
			init(toParse);
		} catch (NoConfigFileFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return (HashMap<String, Object>) gson.fromJson(root, HashMap.class);
	}

	private void init(File toParse) throws NoConfigFileFoundException {
		try {
			root = (JsonObject) gson.fromJson(new FileReader(toParse), JsonObject.class);
			if (root == null) {
				throw new JsonConfigFileEmptyException();
			}
		} catch (java.io.FileNotFoundException fne) {
			throw new NoConfigFileFoundException();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getRoot() {
		return root;
	}

}

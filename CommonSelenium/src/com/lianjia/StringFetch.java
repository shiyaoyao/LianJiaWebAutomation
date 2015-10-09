package com.lianjia;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

public class StringFetch {
	protected static HashMap<String, Properties> propcache = new HashMap<String, Properties>();
	protected Logger log;
	private  static Properties labelStringProp;
	
	public StringFetch() {
		labelStringProp = getProperties("data/strings/label.properties");
	}	

	/**
	 * Populates the cached language strings property object with the contents
	 * of the specified LianJiaWeb strings property file
	 * 
	 * @param file
	 *            LianJiaWeb string property file name (e.g. dwa_s_de.properties)
	 * @return
	 */
	private Properties getProperties(String file) {
		Properties properties = propcache.get(file);
		if (properties == null) {
			try {
				propcache.put(file, properties = new Properties());
				InputStream in = null;
				if (file.startsWith("file:"))
					in = new FileInputStream(new File(new URI(file)));
				else
					in = getClass().getClassLoader().getResourceAsStream(file);
				properties.load(in);
			} catch (Throwable t) {
				t.printStackTrace();
				propcache.remove(file);
				properties = null;
				if (log != null)
					log.warn("Error reading properties file: " + file, t);
			}
		}

		return properties;
	}

	public static final String getString(String label) {
		try {
			new StringFetch();
			String prop =  labelStringProp.getProperty(label);
			return prop==null ? "" : prop;
		} catch (Throwable t) {
		}
		return "";
	}

	public static Properties getStringProps() {
		return labelStringProp;
	}

	public static Properties getLabelsContainingValue(String sValue) {
		Properties subProps = new Properties();
		subProps.putAll(labelStringProp);
		String key;
		String value;
		Enumeration e = subProps.keys();
		while (e.hasMoreElements()) {
			key = e.nextElement().toString();
			value = labelStringProp.getProperty(key);
			if (!value.contains(sValue))
				subProps.remove(key);
		}
		return subProps;
	}

	public static Properties getLabelsContainingKey(String sKeySubstring) {
		Properties subProps = new Properties();
		subProps.putAll(labelStringProp);
		String key;
		Enumeration e = subProps.keys();
		while (e.hasMoreElements()) {
			key = e.nextElement().toString();
			if (!key.contains(sKeySubstring))
				subProps.remove(key);
		}
		return subProps;
	}
}

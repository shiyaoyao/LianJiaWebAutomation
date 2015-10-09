package com.lianjia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import com.lianjia.shared.WebUserDirectory;

public class RunConfig {
	protected static HashMap<String, List<String>> jarPathMap = new HashMap<String, List<String>>(50);
	protected static HashMap<String, Properties> propcache = new HashMap<String, Properties>();
	protected static final String CONFIG_FILE_NAME = "Selenium.properties";
	protected Logger log;
	protected Properties configprops;
	protected WebUserDirectory directory;

	public RunConfig() {
		super();
	}

	public String getProperty(String property) {
	    return configprops.getProperty(property);
	}

	public Properties getProperties(String file) {
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
	        }
	        catch (Throwable t) {
	            t.printStackTrace();
	        	propcache.remove(file);
	        	properties = null;
	        	if (log != null)
	        	    log.warn("Error reading properties file: " + file, t);
	        }
	    }
	    
	    return properties;
	}

	/**
	 * @return List of files in the directory specified by {@link LJTestCase#getDataPath()}
	 */
	public List<String> getPropertiesFiles(String path) {
	    List<String> ret = null;
	    String dataPath = path.replaceAll("[/]$", ""); 
	    URL dir = getClass().getClassLoader().getResource(dataPath);
	    if (dir != null && dir.getProtocol().equals("file")) {
	        try {
	            ret = Arrays.asList(new File(dir.toURI()).list(new FilenameFilter() {
	                public boolean accept(File dir, String name) {
	                    return name.endsWith(".properties");
	                }
	            }));
	        } catch (URISyntaxException e) {
	            throw new RuntimeException(e);
	        }
	    }
	    else {
	        log.debug(getClass().getSimpleName() + ".class");
	        dir = getClass().getResource(getClass().getSimpleName() + ".class");
	        
	        if (dir.getProtocol().equals("jar")) {
	            if (jarPathMap.isEmpty()) {
	                try {
	                    jarPathMap = new HashMap<String, List<String>>(50);
	                    String jarpath = dir.getPath().substring(5, dir.getPath().indexOf("!"));
	                    JarFile jar = new JarFile(URLDecoder.decode(jarpath, "UTF-8"));
	                    
	                    Enumeration<JarEntry> entries = jar.entries();
	                    JarEntry entry = null;
	                    while ( entries.hasMoreElements() && (entry = entries.nextElement()) != null ) {
	                        String resource = entry.getName();
	                        int index = resource.lastIndexOf('/');
	                        String respath = index == -1 ? "" : resource.substring(0, index);
	                        String resname = index == -1 ? resource : resource.substring(index + 1);
	                        
	                        List<String> list = jarPathMap.get(respath);
	                        if (list == null)
	                            jarPathMap.put(respath, list = new ArrayList<String>());
	                        list.add(resname);
	                        
	                        log.debug(dataPath + " - " + respath + " - " + resname);
	                    }
	                } catch (IOException ioe) {
	                    throw new RuntimeException(ioe);
	                }
	            }
	            
	            return jarPathMap.get(dataPath);
	        }
	    }
	    
	    return ret;
	}

}
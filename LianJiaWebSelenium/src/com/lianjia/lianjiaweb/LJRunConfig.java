package com.lianjia.lianjiaweb;

import static org.openqa.selenium.server.ClassPathResource.getSeleniumResourceAsStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import com.lianjia.HasProperties;
import com.lianjia.automation.CoreAutomation.Platform;
import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.lianjiaweb.users.WebUserDirectory;

public class LJRunConfig implements HasProperties {
    private static HashMap<String, List<String>> jarPathMap = new HashMap<String, List<String>>(50);
    private static HashMap<String, Properties> propcache = new HashMap<String, Properties>();
    private static final String CONFIG_FILE_NAME = "Selenium.properties";
    
    private Logger log;
    private Properties configprops;
    private WebUserDirectory directory;
    private Properties localeprops;
    
    public LJRunConfig() {
    	CoreLogger.info("Starting Core to override logger");
    	Logger.getLogger(LJRunConfig.class.getName()).setLevel(Level.INFO);
    	SeleniumCore.disableActionLogging(); 
    	 
        // fall back to Selenium.properties in project resources
        configprops = getProperties("data/" + CONFIG_FILE_NAME);
        
    	
        if (configprops == null){
        	 throw new RuntimeException("Unable to read Selenium.properties!");  
        }
                 
        getSeleniumVersionInfo();
        
        // Set up logging config
        String logConfigLocation = configprops.getProperty("logconfig");
        
        if (logConfigLocation == null)
            throw new RuntimeException("'logconfig' file location not specified in Selenium.properties.");
        
        URL logconfig = null;
        if(!new File(logConfigLocation).exists()){
        	logconfig = getClass().getClassLoader().getResource(logConfigLocation);
        }else{
			try {
				logconfig = new File(logConfigLocation).toURI().toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if (logconfig == null)
            throw new RuntimeException("Unable to read log config file!");
        
        if (logconfig.getFile().endsWith(".properties"))
            PropertyConfigurator.configure(logconfig);
        else if (logconfig.getFile().endsWith(".xml"))
            DOMConfigurator.configure(logconfig);
        
        // We may log now...
        log = Logger.getLogger(getClass());
        
        log.info("JUNITREPORT;true");
        
        /////////////////////////////////////
        // CHANGE IE REG SETTINGS 
        /////////////////////////////////////
        //setRegistryForIE();
        setWebDriverProperty();
        
        // Read in target properties.
        String target = configprops.getProperty("target");
        if (target == null || target.equals(""))
            throw new RuntimeException("'target' not specified in Selenium.properties.");
        
        Properties targetprops = getProperties(target + "/target.properties");
        
        String chkProp = "";
        if (targetprops != null) {
            for (Object prop : targetprops.keySet()) {
            	chkProp = configprops.getProperty((String)prop);
            	
                configprops.put(prop, targetprops.getProperty((String)prop));
            }
        } else {
        	RuntimeException rte =  new RuntimeException("Unable to read "+target+"/target.properties!");
        	rte.printStackTrace();
        	throw rte;
        }
        
        directory = new WebUserDirectory(this);         
    }
    
    public String getProperty(String property) {
        String prop = configprops.getProperty(property);
        return prop==null ? "" : prop;
    }
    
    public void setProperty(String prop, String value) {
    	if(configprops != null) {
    		configprops.setProperty(prop, value);
    	}
    }
    
    public Properties getProperties(String file) {
    	 Properties properties = propcache.get(file);
        if (properties == null) {
			try {
				// LOOK FOR THE FILE IN THE RESOURCE AREA (FILE SYSTEM OR JAR)
				propcache.put(file, properties = new Properties());
				InputStream in = null;
				if(file.startsWith("file:")) {
					in = new FileInputStream(new File(new URI(file)));
				} else {
					in = getClass().getClassLoader().getResourceAsStream(file);
				}
				// IF NOTHING FOUND IN RESOURCES, LOOK IN THE RUNTIME DIRECTORY
				if(in == null) {
					File localTarget = new File(file);
					if(!localTarget.exists()) {
						throw new FileNotFoundException();
					} else {
						in = new FileInputStream(localTarget);
					}
				}
				// Changing to UTF-8 to support accented Latin characters
				properties.load(new InputStreamReader(in, "UTF8"));
			} catch (FileNotFoundException fnf) {
				System.out.println(fnf.getMessage());
				propcache.remove(file);
				properties = null;
				if(log != null)
					log.warn("Properties file not found: " + file);
			} catch (Throwable t) {
				System.out.println(t.getMessage());
				propcache.remove(file);
				properties = null;
				if(log != null)
					log.warn("Error reading properties file: " + file);
			}
		}
        
        return properties;
    }
    
    private void getSeleniumVersionInfo() {
    	 if (configprops == null)
             throw new RuntimeException("Unable to read Selenium.properties!");
         
    	final Properties p = new Properties();
		try {
			p.load(getSeleniumResourceAsStream("/VERSION.txt"));
			if(p.isEmpty())
				 throw new RuntimeException("Unable to read Selenium Version information!");
			String rcVersion = p.getProperty("selenium.rc.version");
			String rcRevision = p.getProperty("selenium.rc.revision");
			String coreVersion = p.getProperty("selenium.core.version");
			String coreRevision = p.getProperty("selenium.core.revision");
			configprops.put("rcversion",(rcVersion != null ? rcVersion : "")+(rcRevision != null ? rcRevision : ""));
			configprops.put("coreversion",(coreVersion != null ? coreVersion : "")+(coreRevision != null ? coreRevision : ""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * @return List of files in the directory specified by {@link LJTestCase#getDataPath()}
     */
    public List<String> getPropertiesFiles(String path) {
        // TODO: handle test modes?
        List<String> ret = null;
        String dataPath = path.replaceAll("[/]$", ""); 
        URL dir = getClass().getClassLoader().getResource(dataPath);
        // IF NO RESOURCE PATH IS FOUND, TRY THE RUNTIME DIRECTORY
        if (dir == null){
        	File f = new File(dataPath);
        	try {
				dir = f.toURI().toURL();
			} catch (MalformedURLException e) {
				 dir = null;
			}
        }
        // IF A FILE OBJECT IS FOUND (IN RESOURCES OR RUNTIME DIRECTORY...
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
        // ...OTHERWISE LOOK IN THE JAR RESOURCES
        else {
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
                            if (resname.endsWith(".properties"))
                            		list.add(resname);
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
    
    public WebUserDirectory getUserDirectory() {
        return directory;
    }
    
    public Properties getConfigProps() {
    	return configprops;
    }
    
    public Properties getStrings(){
    	return localeprops;
    }
    
	private boolean setRegistryForIE() {
		boolean success = true;
		if (!configprops.getProperty("browser").toLowerCase().contains("explorer"))
			return false;
		log.info("Set Windows Registry settings");
		String workingdir = System.getProperty("user.dir")
				+ Platform.getFileSeparator() + "lib" + Platform.getFileSeparator();
		
		Platform.runCommandLineAndWait(new String[] {"regedit","/s","IESeleniumRequirements_LianJiaWeb.reg"}, null, new File(workingdir));
		Platform.sleep(0.5);
		return success;
	}
	
	private void setWebDriverProperty() {
		boolean bLocalWD = Boolean.getBoolean(configprops.getProperty("useLocalWebDriver", "false").toLowerCase());
		String wdIE = Platform.getCurrentProjectPath() + "/lib/IEDriverServer.exe";
		String wdChrome = Platform.getCurrentProjectPath() + "/lib/chromedriver.exe";
		boolean wdChromeSet = !(System.getProperty("webdriver.chrome.driver") == null);

		if (Platform.isMac())
			wdChrome = Platform.getCurrentProjectPath() + "/lib/chromedriver_mac";
		if (Platform.isLinux())
			wdChrome = Platform.getCurrentProjectPath() + "/lib/chromedriver_linux32";

		if (!wdChromeSet || bLocalWD)
			System.setProperty("webdriver.chrome.driver", wdChrome);
		System.setProperty("webdriver.ie.driver", wdIE);

		System.out.println("useLocalWebDriver: (" + bLocalWD + ")");
		System.out.println("(webdriver.chrome.driver): " + System.getProperty("webdriver.chrome.driver"));
		System.out.println("(webdriver.ie.driver): " + System.getProperty("webdriver.ie.driver"));
		
	}
}


package com.lianjia.automation.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
 
public class AutomationLoggerAppender implements Appender, OptionHandler {
    private static final String TEMP_LOG_DIR = "temp";
	public static final String NOT_SET = "[not set]";
	public static final String CONFIG = "CONFIG;";
	private static final String TESTFAIL = "TESTFAIL;";
	public static final String TESTIGNORE = "TESTIGNORE;";
	public static final String TESTPASS = "TESTPASS;";
	public static final String SUITESTART = "SUITESTART;";
	public static final String SUITEEND = "SUITEEND;";
	public static final String TESTSTART = "TESTSTART;";
	public static final String FOUNDTEST = "FOUNDTEST;";
	public static final String SERVERINFO = "SERVERINFO";
	public static final String CLIENTINFO = "CLIENTINFO";
	
	private static final Logger log = Logger.getLogger(AutomationLoggerAppender.class);
    public static final String IMAGES_DIR = "images";
    
    private String _currentTestSuiteName;
    private String _name;
    private Layout _layout;
    private ErrorHandler _handler;
    private Filter _filter;
    
    // Appender options
    private String _opt_logdir;
    
    // other
    private File _logDir;
    private AutomationLogger _htmllog;
    private Vector<String> _foundTests = new Vector<String>();
    private boolean isStarted = false;
	private HashMap<String, String> configAttr;
	private String archiveName;
    
    public void doAppend(LoggingEvent event) {    	
    	StringBuffer message = new StringBuffer();
    	if(event.getMessage() != null)
    		message.append(event.getMessage().toString());
        Level level = event.getLevel();
        
        if (event.getThrowableInformation() != null) {
        	message.append("\n");
            String[] throwstr = event.getThrowableInformation().getThrowableStrRep();
            for (String str : throwstr)
                message.append("\n").append(str);
        }
        
        if (message.toString().startsWith(CONFIG))
            parseConfig(event);
        else if (message.toString().startsWith(FOUNDTEST))
            _foundTests.add(message.substring(FOUNDTEST.length()).trim());
        else if (message.toString().startsWith(TESTSTART)) {
            parseTestStart(event);
            _htmllog.logIt(level, message.toString());
        }
        else if (message.toString().startsWith(SUITESTART)) {
        	// message contain suite name
        	String sSuiteName = message.toString().replace(SUITESTART, "");
        	this.initTestSuiteLog(sSuiteName);
            _htmllog.logIt(level, message.toString());
        }
        else if (message.toString().startsWith(CLIENTINFO)) {
        	parseClientInfo(event);
        }
        else if (message.toString().startsWith(TESTPASS)) {
            _htmllog.logIt(level, message.toString());
            _htmllog.passCurrentTest();
        }
        else if (message.toString().startsWith(TESTIGNORE)) {
            _htmllog.logIt(level, message.toString());
            _htmllog.skipCurrentTest();
        }
        else if (message.toString().startsWith(TESTFAIL)) {
            _htmllog.logIt(level, message.toString());
            _htmllog.failCurrentTest();
        }
        else if (message.toString().startsWith(SUITEEND)) {
        	//Archive the HTML log
        	_htmllog.endLogging();
        	copyToArchive();
        	isStarted = false;
        }
        else if (message.toString().startsWith("RERUNMODE;")){
        	_htmllog.setRerunMode(Boolean.parseBoolean(message.toString().replaceFirst("RERUNMODE;", "")));
        }
        else if(message.toString().startsWith("JUNITREPORT;")){
        	_htmllog.setJUnitReport(Boolean.parseBoolean(message.toString().replaceFirst("JUNITREPORT;", "")));
        }
        else if(message.toString().startsWith("JUNITRETEST;")){
        	_htmllog.setJUnitReport(true);
        	_htmllog.setJUnitRetestReplace(Boolean.parseBoolean(message.toString().replaceFirst("JUNITRETEST;", "")));
        }
        else if (!event.getLoggerName().equals(AutomationLogger.class.getName()))
            _htmllog.logIt(level, message.toString());
    }
    
    private void copyToArchive() {    	
    	File currentDir = new File(getLogDir(),"current");
    	File archiveDir = new File(getLogDir(),"archive");
    	// Make sure archive directory exists, if not create it.
    	if(!archiveDir.exists()){
    		archiveDir.mkdir();
    	}
		if(currentDir.exists()){
			// Create directory for Log files
	    	File logDir = new File(archiveDir,archiveName);
	    	logDir.mkdir();
			copyWithSubDir(currentDir, logDir);
		}
	}

	private void copyWithSubDir(File copyFrom, File copyTo) {
		File files[] = copyFrom.listFiles();
		for (File file : files){
			File copy = new File(copyTo,file.getName());
			if(file.isDirectory()){
				copy.mkdir();
				copyWithSubDir(file, copy);
			}else{
				try {
					FileInputStream in = new FileInputStream(file);
					FileOutputStream out = new FileOutputStream(copy);
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0){
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
					//There should be no reason for these catches to be reached
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setLogDir(String path) {
        _opt_logdir = path;
    }
    public String getLogDir() {
        return _opt_logdir;
    }
    
    public void addFilter(Filter filter) {
        _filter = filter;
    }

    public void clearFilters() {
        _filter = null;
    }

    public void close() {}

    public ErrorHandler getErrorHandler() {
        return _handler;
    }

    public Filter getFilter() {
        return _filter;
    }

    public Layout getLayout() {
        return _layout;
    }

    public String getName() {
        return _name;
    }

    public boolean requiresLayout() {
        return false;
    }

    public void setErrorHandler(ErrorHandler handler) {
        _handler = handler;
    }

    public void setLayout(Layout layout) {
        _layout = layout;
    }
    public void setName(String name) {
        _name = name;
    }

    public void activateOptions() {
    	initTestSuiteLog(null);
    }
    
    private void initTestSuiteLog(String sSuiteName){
    	if(sSuiteName == null){
    		sSuiteName = "Non-Suited Tests";
    	}if(sSuiteName.equals(_currentTestSuiteName)){
    		// do nothing when test suite is not changed
    		return;
    	}else{
    		// remove special chars that are not suitable for both path and html
    		sSuiteName = sSuiteName.trim();
    		sSuiteName = sSuiteName.replaceAll("[\\/|?*:<>&+%#= \"']", "_");
    		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
    		archiveName =sSuiteName + "_"+format.format(new Date());
    		initLogDir("current");
    		// remove temp dir
    		File tempDir = new File(getLogDir(),TEMP_LOG_DIR);
    		if(tempDir.exists()){
                File files[] = tempDir.listFiles();
                for (File file : files)
                    file.delete();  
    			tempDir.delete();
    		}
    	}
        HashMap<String, String> vars = new HashMap<String, String>();
        vars.put("platform", Platform.getOSNameAndVersion());
        vars.put("description", NOT_SET);
        vars.put("title", sSuiteName);
        vars.put("browser", NOT_SET);
        vars.put("url", NOT_SET);
        _htmllog = new AutomationLogger(_logDir, vars);
        if(configAttr!= null)
        	_htmllog.setAttributes(configAttr);
        
        _currentTestSuiteName = sSuiteName;
    }
    
    /**
     * Initialized the logging directory defined in the appender options.
     * Copies over needed resources to view the log and creates the necessary folder structure.
     */
    private void initLogDir(String logname) {
        // clean logdir
        _logDir = new File(getLogDir(),logname);
        if (!_logDir.exists())
            _logDir.mkdirs();
        else {
            File files[] = _logDir.listFiles();
            for (File file : files)
                file.delete();  
        } 
        
        // clean images dir
        File images = new File(_logDir, IMAGES_DIR);
        if (!images.exists())
            images.mkdir();
        else {
            File files[] = images.listFiles();
            for (File file : files)
                file.delete();  
        } 

        // copy over files needed for log viewing
        String[] filepaths = {
            "log.css",
            "log.js",
            "logViewer.html",
            "testViewer.html",
            "icon_apple.png",
            "icon_ff.png",
            "icon_chrome.png",
            "icon_ie.png",
            "icon_linux.png",
            "icon_safari.png",
            "icon_win.png"
        };
        for (String filepath : filepaths) {
        	
            InputStream in = getClass().getResourceAsStream(filepath);
            try {
                FileOutputStream out = new FileOutputStream(new File(_logDir, filepath));
                byte[] buf = new byte[1024];
                int read = -1;
                while ((read = in.read(buf)) > 0)
                    out.write(buf, 0, read);
                in.close();
                out.close();              
            } catch (Exception e) {
                log.error("Error copying log dependency file.", e);
            }
        }        
    }

    private void parseConfig(LoggingEvent event) {
        configAttr = new HashMap<String, String>();
        
        String config = event.getMessage().toString().substring(CONFIG.length());
        String[] values = config.trim().split(";");
        for (String value : values) {
            String[] pair = value.split("=");
            configAttr.put(pair[0], pair.length > 1 ? pair[1] : "true");
        }
        
        _htmllog.setAttributes(configAttr);
    }
    
    private void parseTestStart(LoggingEvent event) {
        String name = NOT_SET;
        String desc = NOT_SET;
        String data = NOT_SET;
        boolean rerun = false;
        
        String config = event.getMessage().toString().substring(CONFIG.length());
        String[] values = config.trim().split(";");
        for (String value : values) {
            String[] pair = value.split("=");
            if (pair.length > 1 && pair[0].trim().equals("name"))
                name = pair[1];
            else if (pair.length > 1 && pair[0].trim().equals("datafile"))
                data = pair[1];
            else if (pair.length > 1 && pair[0].trim().equals("description"))
                desc = pair[1];
            else if (pair[0].trim().equals("rerun"))
                rerun = true;
        }
        
        if (!isStarted) {
            isStarted = true;
            _htmllog.startLogging(_foundTests);
        }
        _htmllog.startNewTest(name, desc, data, rerun);
        parseClientInfo(event);
    }
    
    private void parseClientInfo(LoggingEvent event) {
    	
    	Properties clientInfo = new Properties();
    	clientInfo.put("client-name", NOT_SET);
    	clientInfo.put("client-os", NOT_SET);
    	clientInfo.put("browser-name", NOT_SET);
    	clientInfo.put("automation", NOT_SET);
    	clientInfo.put("engine", NOT_SET);
    	clientInfo.put("selenium", NOT_SET);
    	
    	 String[] values = event.getMessage().toString().trim().split(";");
         for (String value : values) {
             String[] pair = value.split("=");
             if (pair.length > 1 && pair[0].trim().equals("client-name"))
            	 clientInfo.put("client-name", pair[1]);
             else if (pair.length > 1 && pair[0].trim().equals("client-os"))
            	 clientInfo.put("client-os", pair[1]);
             else if (pair.length > 1 && pair[0].trim().equals("browser-name"))
            	 clientInfo.put("browser-name", pair[1]);
             else if (pair.length > 1 && pair[0].trim().equals("automation"))
            	 clientInfo.put("automation", pair[1]);
             else if (pair.length > 1 && pair[0].trim().equals("engine"))
            	 clientInfo.put("engine", pair[1]);
             else if (pair.length > 1 && pair[0].trim().equals("selenium"))
            	 clientInfo.put("selenium", pair[1]);
         }
    	_htmllog.logClientInfo(clientInfo);
    }
}

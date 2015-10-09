package tasks.base;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.lianjia.BaseAssert;
import com.lianjia.automation.core.Platform;

public class BaseTask extends BaseAssert {	
	private static String autoIt = new File("lib"+Platform.getFileSeparator()+"AutoIt.exe").getAbsolutePath();
	
	/**
	 * @return Returns a string to an executable script which can be executed using Runtime.getRuntime().exec(...);
	 * 
	 * Reads in the script template identified by scriptHandle. Replaces keys with values specified in variables if they exist in the template.
	 * Example Script:  doSomething("${myMessage}") HashMap.put("${myMessage}","This is my message") results in doSomething("This is my message")
	 */
	public static String prepareExternalScript(File scriptHandle,HashMap<String,String> variables){
		StringBuffer path = new StringBuffer();
		if (Platform.isWindows())
			path.append(autoIt+" ");
		
		//Read in the script
		if (scriptHandle.exists()){

            try {
            	String extension = scriptHandle.getAbsolutePath();
            	int period = extension.lastIndexOf(".");
            	extension = extension.substring(period+1);
            	
            	//Create temporary version of script
            	File tmpScript = File.createTempFile("script", "."+extension);
            	tmpScript.deleteOnExit();
            	
				BufferedInputStream input = new BufferedInputStream(new FileInputStream(scriptHandle));
				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tmpScript));
				
	            // Write file contents to response.
	            byte[] buffer = new byte[1024];
	            while (input.read(buffer) > 0) {
	            	
	            	//Data has been read in, replace any variables specified in hashmap
	            	String data = new String(buffer);
	            	Iterator<String> itr = variables.keySet().iterator();
	            	while(itr.hasNext()){
	            		String key = itr.next();
	            		String value = variables.get(key);
	            		if (key!=null && value!=null)
	            			data = data.replace(key, value);
	            	}
	            	
	                output.write(data.getBytes());
	            }
	            
	            //Close template file and newly created instance
			    output.close();
			    input.close();	
			    
			    //On a unix platform? Have to allow script access
				if (Platform.isMac())
		        	Runtime.getRuntime().exec("chmod 755 "+tmpScript.getAbsolutePath());
			    
			    path.append(tmpScript.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		
		return path.toString();
	}	
}


package com.lianjia.lianjiaweb.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import tasks.web.util.SimpleFileIO;
import appobjects.user.WebUser;

import com.lianjia.BaseAssert;
import com.lianjia.lianjiaweb.LJRunConfig;

public class WebUserDirectory {

	private Logger log;
	private LJRunConfig config;
	private List<WebUser> users = new ArrayList<WebUser>(10);
	
	public WebUserDirectory(LJRunConfig runConfig) {
		log = Logger.getLogger(getClass());
		config = runConfig;
	}

	/**
	 * @return List of non-admin users in the directory
	 */
	public List<WebUser> getUsers() {
		if (users.isEmpty())
			load();
		return users;
	}

	/**
	 * Replaces the global user list with the specified list of users
	 * 
	 * @param userList
	 */
	public void setUsers(List<WebUser> userList) {
		users.clear();
		for (WebUser usr : userList)
			users.add(usr);
	}

	public WebUser getByUserName(String userName) {

		if (users.isEmpty())
			load();

		Iterator<WebUser> iter = users.iterator();
		while (iter.hasNext()) {
			WebUser user = iter.next();
			if (user.getUsername().equals(userName)) {
				return user;
			}
		}

		return null;
	}
	
	private void load() {
		String dirpath = config.getProperty("target") + "/users";
		log.debug("Loading users from: " + dirpath);
		if(SimpleFileIO.getListOfFiles(dirpath, ".properties") == null){
			dirpath = dirpath.substring(0, dirpath.lastIndexOf("/users"));
			log.debug("Didn't find properties files in "+dirpath+"/users ; Try path: "+dirpath);
			if(SimpleFileIO.getListOfFiles(dirpath, ".properties") == null){
				BaseAssert.assertNotNull("Found user set properties in "+dirpath, null);
			}
		}
		Properties props = new Properties();
		// Get all user property files
		List<String> files = config.getPropertiesFiles(dirpath);
				
		// If a CSV user set doesn't exist, look for individual user .properties files
		// Process all the individual user property files and populate the WebUserDirectory
		for (String file : files) {
			// If this isn't a Company property file, then assume a user
			// and add it to the list.
			if (file.endsWith(".properties")) {
				props = config.getProperties(dirpath + "/" + file);
				WebUser user = WebUser.getUserFromProperties(props);
				
				users.add(user);
			}
		}
		
	}
	
	public void reset() {
		users.clear();		
		load();
	}

	/**
	 * Attempts to get all the WebUser property values for the give user. <br>
	 * 
	 * @param user a WebUser object
	 * @return An ArrayList of Strings. Each entry is in the form:<br>
	 * <code>&nbsp;&nbsp;&nbsp; property-name :: property-value</code>
	 * <p>The list is sorted by property-name, but grouped such that all defined properties are<br>
	 * at the top of the list, followed by any undefined properties.
	 */
	public ArrayList<String> getListOfProperties(WebUser user) {
		ArrayList<String> allProps = new ArrayList<String>();
		ArrayList<String> und = new ArrayList<String>();
		String temp;
		Properties userProps = new Properties();
		userProps.put("Username".toLowerCase(), user.getUsername());
		userProps.put("Password".toLowerCase(), user.getPassword());
		userProps.put("Phone".toLowerCase(), user.getPhone());
		
		for(Object p : userProps.keySet()) {
			temp = userProps.get(p.toString()).toString();
			if(temp.startsWith("["))
				temp = Arrays.toString((Object[])userProps.get(p.toString()));
			if(temp != null && !temp.isEmpty())
				allProps.add(p.toString() + " :: " + temp);
		}
		Collections.sort(allProps);
		
		for(Object p : userProps.keySet()) {
			temp = userProps.get(p.toString()).toString();
			if(temp == null || temp.isEmpty())
				und.add(p.toString() + " :: ------");
		}
		Collections.sort(und);
		allProps.addAll(und);

		return allProps;
	}	
}

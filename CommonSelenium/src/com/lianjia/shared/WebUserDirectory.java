package com.lianjia.shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import appobjects.user.WebUser;

import com.lianjia.RunConfig;

public class WebUserDirectory {

	private Logger log;
	private RunConfig config;
	private List<WebUser> users = new ArrayList<WebUser>(10);
	
	public WebUserDirectory(RunConfig runConfig) {
		log = Logger.getLogger(getClass());
		config = runConfig;
	}

	public List<WebUser> getUsers() {
		if (users.isEmpty())
			load();
		return users;
	}

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
		Properties props;
		// Get all user property files
		List<String> files = config.getPropertiesFiles(dirpath);

		for (String file : files) {
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
}

package tasks.web.authentication;

import appobjects.user.WebUser;

public interface LoginTasksInterface {

	/**
	 * Authenticates a user into hosted or standalone instance (Auto-detected)
	 * @param username
	 * @param password
	 * @return
	 */
	public abstract void login(WebUser user);

	public abstract boolean logout();

}
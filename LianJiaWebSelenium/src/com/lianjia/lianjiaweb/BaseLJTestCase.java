package com.lianjia.lianjiaweb;
import java.util.List;

import appobjects.user.WebUser;

import com.lianjia.lianjiaweb.users.WebUserDirectory;

public abstract class BaseLJTestCase extends LJTestCase {

	protected WebUserDirectory _userDirectory;
	protected List<WebUser> _users = null;
	protected WebUser _user1 = null;
	
	protected String _user1Name = null;

	@Override
	public void testSetUp() throws Throwable {

		super.testSetUp();

		_userDirectory = getUserDirectory();
		_users = _userDirectory.getUsers();
		_user1 = _users.get(0);
		_user1Name = _user1.getUsername();
		
	}

	@Override
	public void testTearDown() throws Throwable {
		super.testTearDown();
	}	
}

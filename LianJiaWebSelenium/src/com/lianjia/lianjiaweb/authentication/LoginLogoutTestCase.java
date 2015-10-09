package com.lianjia.lianjiaweb.authentication;
import appobjects.user.WebUser;

public class LoginLogoutTestCase extends AbstractAuthenticationCategory {

    @Override
	public void testMain() throws Throwable {	
        WebUser user = getUserDirectory().getUsers().get(0);
		logTestPlan("Log in as user <"+user.getUsername()+">");
        logIn(user);
        
        logTestPlan("Log out");
        logOut();
        
       		
	}

    @Override
    public String getAuthor() {
        return "SYJ";
    }

    @Override
    public String getDescription() {
        return "LianJiaWeb authentication methods";
    }

    @Override
    public String getDataPath() {
        return "data";
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}

package com.lianjia.lianjiaweb.homepage;

import com.lianjia.lianjiaweb.LJTestCase;

public class AbstractHomePageCategory extends LJTestCase {

	@Override
	public void testMain() throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCategory() {
		return "HomePage";
	}

	@Override
	public String getAuthor() {
		return "SYJ";
	}

	@Override
	public String getDataPath() {
		return "data";
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Home page functions verification";
	}

}

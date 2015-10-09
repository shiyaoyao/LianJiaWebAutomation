package com.lianjia.lianjiaweb.commons;

import com.lianjia.lianjiaweb.LJTestCase;

public class AbstractCommonsCategory extends LJTestCase {

	@Override
	public void testMain() throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCategory() {
		return "Common";
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
		return "Common functions verification";
	}

}

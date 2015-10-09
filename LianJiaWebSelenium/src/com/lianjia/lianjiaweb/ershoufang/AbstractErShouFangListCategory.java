package com.lianjia.lianjiaweb.ershoufang;

import com.lianjia.lianjiaweb.LJTestCase;

public class AbstractErShouFangListCategory extends LJTestCase {

	@Override
	public void testMain() throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCategory() {
		return "ErShouFang List view";
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
		return "ErShouFang List view functions verification";
	}

}

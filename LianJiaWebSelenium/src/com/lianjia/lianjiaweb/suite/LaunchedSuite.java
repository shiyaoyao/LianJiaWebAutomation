package com.lianjia.lianjiaweb.suite;

import com.lianjia.lianjiaweb.LJTestSuite;

/**
 * Specialized test suite which reads a text file called CustomSuite.properties
 * from the local machines home directory and reads in the full test case names
 * and loads them into a suite runner.
 */
public class LaunchedSuite extends LJTestSuite {
	public static String[] testList;
	public static String name;
	public LaunchedSuite(){
		super();
		this.addToTestList(testList);
	}
	
	@Override
	public void testMain() throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getName(){
		return name;
	}
}

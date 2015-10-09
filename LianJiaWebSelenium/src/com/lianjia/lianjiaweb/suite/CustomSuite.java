package com.lianjia.lianjiaweb.suite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lianjia.automation.core.Platform;
import com.lianjia.lianjiaweb.LJTestSuite;

/**
 *
 * Specialized test suite which reads a text file called CustomSuite.properties
 * from the local machines home directory and reads in the full test case names
 * and loads them into a suite runner.
 */
public class CustomSuite extends LJTestSuite {

	public CustomSuite(){
		super();
		String fileLoc = Platform.getUserHome()+File.separatorChar+"CustomSuite.properties";
		List<String> testNames = new ArrayList<String>(20);
		try {
			FileReader in = new FileReader(new File(fileLoc));
			BufferedReader read = new BufferedReader(in);
			String line = null;
			while ((line = read.readLine()) != null){
				testNames.add(line);
			}
			this.addToTestList(testNames.toArray(new String[testNames.size()]));
		} catch (FileNotFoundException e) {
			log.error("CustomSuite.properties was not found in "+Platform.getUserHome(),e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.error("An IO Error Occured", e);
			throw new RuntimeException(e);
		}
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

}

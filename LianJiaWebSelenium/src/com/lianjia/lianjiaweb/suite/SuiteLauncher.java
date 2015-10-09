package com.lianjia.lianjiaweb.suite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.runner.JUnitCore;

import com.lianjia.automation.core.Platform;

/**
 * <p>SuiteLauncher executes a list of test case classes through JUnit.
 * </p>
 * <p>SuiteLauncher.main() takes one or more arguments representing a test suite file name 
 * or a series of test case class names.
 * </p>
 * <p>If the first argument is a test case class name in the form com.lianjia.test...,
 * SuiteLauncher assumes all arguments are test case class names. These
 * arguments are reformatted into a String array and used as a dynamic test
 * suite.
 * </p>
 * <p>Otherwise, the first argument is assumed to be a test suite file and any
 * further arguments are ignored. The test suite file is parsed to produce a
 * list of test cases. The resulting list of test case is then executed as a suite. 
 * </p>
 * <p>SuiteLauncher also support various switch options:</p>
 * 
 * <blockquote><p><b> -l, -s</b><br>
 * &nbsp;&nbsp;&nbsp; &nbsp; Lists all test suites included in the project, as well as any local, custom test suites </p>
 * <p><b> -t </b><br>
 *  &nbsp;&nbsp;&nbsp; &nbsp; Lists all server targets defined in the project as well as any local, custom server targets defined </p>
 * <p><b> -c </b>test_suite <br>
 *  &nbsp;&nbsp;&nbsp; &nbsp; Lists all test cases included in the specified test suite file </p>
 *  </blockquote>
 */
public class SuiteLauncher {
	private static String resolvedPath = "";
	
	public static void main(String[] args) {
		String _args = "";
		if (args.length == 0){
			System.out.println("SuiteRunner --help\n for usage information.\n");
			return;
		}
		
		for (String s : args) {
			_args += s + ", ";
		}
		_args = _args.substring(0, _args.lastIndexOf(','));

		if (args[0].equals("-l") || args[0].equals("-s")) {
			new SuiteLauncher().listTestSuiteFiles();
			return;
		}

		if (args[0].equals("-t")) {
			new SuiteLauncher().listTestTargets();
			return;
		}
		
		if(args[0].equals("--help")) {
			new SuiteLauncher().help();
			return;
		}

		if (args[0].equals("-c")) {

			if (args.length < 2) {
				System.out.println("ERROR: requires two arguments: -c <suite_file_name>");
				System.out.println("You only provided " + args.length + " arguments: " + _args);
				return;
			}
			try {
				listTestCasesInSuite(args[1]);
			} catch (FileNotFoundException e) {
				System.out.println(args[1] + " was not found");
				System.out.println(e.toString());
				System.exit(1);
			} catch (IOException e) {
				System.out.println("An IO Error Occured");
				System.out.println(e.toString());
				System.exit(1);
			}
			return;
		}

		// If the first argument is a test case class reference, assume the
		// arguments
		// are a list of enumerated test cases
		if (args[0].startsWith("com.lianjia.test.")) {
			System.out.println("Creating dynamic suite from individual test case classes");
			LaunchedSuite.testList = args;
			LaunchedSuite.name = "dynamic.suite";
			JUnitCore.main(LaunchedSuite.class.getName());
			return;
		}

		List<String> testNames = new ArrayList<String>();
		String[] tests = { "" };
		try {
			// Extract the test case class names from the appropriate file or
			// resource
			//fileLocation = getTestSuitePath(args[0]);
			testNames = getTestCasesFromResource(args[0], false);
	
			if (testNames.size() == 0) {
				System.out.println("There were no test cases found in '" + args[0] + "'");
				System.out.println("Suite not executed");
				return;
			}
			// Define the LaunchesSuite object and begin testing.
			LaunchedSuite.testList = testNames.toArray(tests);
			int startIndex = Math.max(args[0].lastIndexOf(Platform.getFileSeparator()), args[0].lastIndexOf('/')) + 1;
			String name = args[0].substring(startIndex);
			LaunchedSuite.name = name;
			JUnitCore.main(LaunchedSuite.class.getName());
		} catch (FileNotFoundException e) {
			System.out.println(args[0] + " was not found");
			System.out.println(e.toString());
			System.exit(1);
		} catch (IOException e) {
			System.out.println("An IO Error Occured");
			System.out.println(e.toString());
			System.exit(1);
		}
	}

	/**
	 * Extract the test case class names from the specified resource path,
	 * either a file or a jar resource
	 * 
	 * @param resourcePath
	 *            file name path or jar resource path
	 * @return An ArrayList of test case class names found in the test suite
	 *         file/resource
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static ArrayList<String> getTestCasesFromResource(String resourcePath, boolean bPrintList) throws IOException,
			FileNotFoundException {
		
		//System.out.println("initial resourcePath: "+resourcePath);
		InputStream inStream = getTestSuiteStream(resourcePath);
		resourcePath = resolvedPath.isEmpty() ? resourcePath : resolvedPath;
		//System.out.println("resolved resourcePath: "+resourcePath);
		ArrayList<String> allTests = new ArrayList<String>();
		allTests.clear();

		if (inStream == null) {
			throw new FileNotFoundException("Resource not found: '" + resourcePath + "'");
		}

		BufferedReader buff = new BufferedReader(new InputStreamReader(inStream));
		String sTestCase = "";
		try {
			while ((sTestCase = buff.readLine()) != null) {
				if (!sTestCase.trim().isEmpty() && sTestCase.startsWith("com.lianjia.test"))
					allTests.add(sTestCase);
				else if(!sTestCase.trim().startsWith("#") && sTestCase.endsWith(".suite"))
					allTests.addAll(getTestCasesFromResource(sTestCase, bPrintList));
			}
		} catch (Exception e) {
			throw new IOException();
		}
		
		System.out.println("The test suite contains " + allTests.size() + " test cases.");
		
		if (bPrintList) {
			System.out.println("\n" + resourcePath + " will execute: ");
			for (String s : allTests) {
				System.out.println("\t" + s);
			}
			System.out.println();
		}
		
		return allTests;
	}

	private void listTestSuiteFiles() {
		System.out.println();
		String fs = Platform.getFileSeparator();
		String path = "testsuites";
		List<String> ret = null;
		String dataPath = path.replaceAll("[/]$", "");
		URL dir = getClass().getClassLoader().getResource(dataPath);
		// LOOK FOR TEST SUITES RESOURCES IN A FILE SYSTEM
		if (dir != null && dir.getProtocol().equals("file")) {
			try {
				System.out.println("Looking for suite files in '" + dir.toString() + "'\n");
				ret = Arrays.asList(new File(dir.toURI()).list(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(".suite");
					}
				}));
				if (ret != null && ret.size() > 0) {
					System.out.println("LianJiaWeb Test Suites included in the package:");
					for (String suite : ret) {
						System.out.println("\t" + suite);
					}
				} else {
					System.out.println("No LianJiaWeb test suites found in this build");
				}

			} catch (URISyntaxException e) {
				System.out.println("Exception: " + e.getMessage());
				throw new RuntimeException(e);
			}
		} else {
			// LOOK FOR TEST SUITES RESOURCES IN A JAR FILE
			dir = getClass().getResource(getClass().getSimpleName() + ".class");
			List<String> list = null;
			if (dir.getProtocol().equals("jar")) {
				try {
					HashMap<String, List<String>> jarPathMap = new HashMap<String, List<String>>(150);
					String jarpath = dir.getPath().substring(5, dir.getPath().indexOf("!"));
					String respath = "";
					String resname = "";
					JarFile jar = new JarFile(URLDecoder.decode(jarpath, "UTF-8"));
					// System.out.println("jarpath: "+jarpath.substring(jarpath.lastIndexOf('/')+1)+"\n");
					Enumeration<JarEntry> entries = jar.entries();
					JarEntry entry = null;
					// System.out.println("jarpath has entries: "+entries.hasMoreElements());
					while (entries.hasMoreElements() && (entry = entries.nextElement()) != null) {
						String resource = entry.getName();
						int index = resource.lastIndexOf('/');
						respath = index == -1 ? "" : resource.substring(0, index);
						resname = index == -1 ? resource : resource.substring(index + 1);
						if (!respath.equals(dataPath))
							continue;
						list = jarPathMap.get(respath);
						if (list == null)
							jarPathMap.put(respath, list = new ArrayList<String>());
						if (resname.endsWith(".suite"))
							list.add(resname);

					}
					List<String> outlist = jarPathMap.get(dataPath);
					if (outlist != null && outlist.size() > 0) {
						System.out.println("LianJiaWeb Test Suites included in the package: "
								+ jarpath.substring(jarpath.lastIndexOf('/') + 1));
						for (String suite : outlist) {
							System.out.println("\t" + suite);
						}
					} else {
						System.out.println("No LianJiaWeb test suites found in this jar path");
					}

				} catch (IOException ioe) {
					throw new RuntimeException(ioe);
				}
			}
		}
		// CHECKING THE RUN-TIME FILE SYSTEM FOR USER DEFINED TEST SUITES
		File localDir = new File(dataPath);
		List<String> localSuites = null;
		if (localDir.exists()) {
			System.out.println("\nUser Defined test suites are located in ./testsuites");
			try {
				localSuites = Arrays.asList(localDir.list(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.endsWith(".suite");
					}
				}));
				if (localSuites != null && localSuites.size() > 0) {
					for (String s1 : localSuites) {
						System.out.println("\t" + s1);
					}
					System.out
							.println("\nNOTE: Local test suites take precedence over resource testsuites of the same name!\n");
				} else {
					System.out.println("\nNo test suites found in the local testsuites folder ." + fs + dataPath + "\n");
				}

			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
				throw new RuntimeException(e);
			}
		} else {
			System.out.println("\nA local test suite folder (." + fs + dataPath + ") isn't found");
		}
	}

	private void listTestTargets() {
		System.out.println();
		String dataPath = "targets";
		ArrayList<String> ret = new ArrayList<String>();
		URL dir = getClass().getClassLoader().getResource(dataPath);
		// CHECKING THE FILE SYSTEM RESOURCE AREA FOR TARGETS
		if (dir != null && dir.getProtocol().equals("file")) {
			try {
				File[] contents = (new File(dir.toURI())).listFiles();
				for (File nm : contents) {

					if (nm.isDirectory()) {
						List<String> tl = Arrays.asList(nm.list(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.endsWith("target.properties");
							}
						}));
						if (tl != null && tl.size() > 0)
							ret.add(nm.getName());
					}
				}
				if (ret != null && ret.size() > 0) {
					System.out.println("Defined test targets");
					System.out.println("Not all targets will resolve to live servers!");
					for (String s1 : ret) {
						System.out.println("\t" + s1);
					}
				} else {
					System.out.println("No defined test target found");
				}

			} catch (URISyntaxException e) {
				System.out.println("Exception: " + e.getMessage());
				throw new RuntimeException(e);
			}
		} else {
			// CHECKING THE JAR FILE RESOURCES FOR TARGETS
			dir = getClass().getResource(getClass().getSimpleName() + ".class");
			List<String> list = new ArrayList<String>();
			if (dir.getProtocol().equals("jar")) {
				try {
					String jarpath = dir.getPath().substring(5, dir.getPath().indexOf("!"));
					JarFile jar = new JarFile(URLDecoder.decode(jarpath, "UTF-8"));
					Enumeration<JarEntry> entries = jar.entries();
					JarEntry entry = null;
					while (entries.hasMoreElements() && (entry = entries.nextElement()) != null) {
						String resource = entry.getName();
						int index = resource.lastIndexOf('/');
						String respath = index == -1 ? "" : resource.substring(0, index);
						String resname = index == -1 ? resource : resource.substring(index + 1);
						if (!respath.startsWith(dataPath) || respath.split("/").length > 2)
							continue;
						if (resname.startsWith("target.prop"))
							list.add(respath.substring(respath.lastIndexOf('/') + 1));
					}
					if (list != null && list.size() > 0) {
						System.out.println("Defined test targets");
						System.out.println("Not all targets will resolve to live servers!");
						for (String suite : list) {
							System.out.println("\t" + suite);
						}
					} else {
						System.out.println("No LianJiaWeb test suites found in this jar path");
					}
				} catch (IOException ioe) {
					throw new RuntimeException(ioe);
				}
			}
		}
		// CHECKING THE RUN-TIME FILE SYSTEM FOR USER DEFINED TARGETS
		File f = new File(dataPath);
		if (f.exists()) {
			System.out.println("\nUser Defined test targets are located in ./targets");
			try {
				File[] localContents = (f).listFiles();
				for (File nm : localContents) {

					if (nm.isDirectory()) {
						List<String> tl = Arrays.asList(nm.list(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.endsWith("target.properties");
							}
						}));
						if (tl != null && tl.size() > 0)
							ret.add(nm.getName());
					}
				}
				if (ret != null && ret.size() > 0) {
					for (String s1 : ret) {
						System.out.println("\t" + s1);
					}
					System.out
							.println("\nNOTE: Resource targets take precedence over user defined targets of the same name!\n");
				} else {
					System.out.println("No defined test target found");
				}

			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
				throw new RuntimeException(e);
			}
		}
	}

	private static void listTestCasesInSuite(String sSuiteName) throws FileNotFoundException, IOException {
		List<String> testNames;
		//sSuiteName = getTestSuitePath(sSuiteName);
		testNames = getTestCasesFromResource(sSuiteName, true);
		if (testNames.size() == 0) {
			System.out.println("There were no test cases found in '" + sSuiteName + "'");
			System.out.println();
			return;
		}
	}

	private static InputStream getTestSuiteStream(String suite) throws FileNotFoundException {
		boolean bLocal = false;
		InputStream inStream = null;
		String sLocal = "localtestsuites";
		String sResource = "testsuites";
		String fs = "/";
		
		String sSuite = suite.replace(Platform.getFileSeparator(),fs);
		sSuite = sSuite.endsWith(".suite") ? sSuite : sSuite+".suite";
		
		resolvedPath = sResource + "/" + sSuite;
		String localSuite = sSuite;
		//System.out.println("look for local file: "+localSuite);
		File locfile = new File(localSuite);
		if (!locfile.exists() && !sSuite.startsWith("localtestsuites")) {
			localSuite = sLocal + "/" + localSuite;
			locfile = new File(localSuite);
		}

		if (locfile.exists()) {
			bLocal = true;
			inStream = new FileInputStream(locfile);
		} else {
			//System.out.println("local file doesn't exist. Try resource path ("+resolvedPath+") and check jar resource");
			inStream = new SuiteLauncher().getClass().getClassLoader().getResourceAsStream(resolvedPath);
		}
		if (inStream == null) {
			System.out.println("Specified suite name could not be found: " + sSuite);
			throw new FileNotFoundException();
		} else {
			if(bLocal)
				System.out.println("Found '"+localSuite+"' as a local file");
			else
				System.out.println("Found "+resolvedPath+ " as a java resource");
		}
		return inStream;
	}
	
	private void help() {
		String path = "com/lianjia/test/lianjiaweb/suite/suitelauncher.help";
		System.out.println("HELP "+path);
		
		byte[] buffer = new byte[2048];
		
		InputStream in = getClass().getClassLoader().getResourceAsStream(path);
		if( in != null ) {
			try {
				for (int len = 0; (len = in.read(buffer)) != -1; ){
					System.out.write(buffer,0,len);
				} 
				in.close();
			} catch (Exception e) {
					System.out.println("Crap.\n"+e.getMessage());
			}
		} else {
			System.out.println("input stream is null");
		}
	}
}

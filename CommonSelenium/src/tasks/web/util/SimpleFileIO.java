package tasks.web.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import com.lianjia.automation.core.Platform;

public class SimpleFileIO {
	private static final Logger log = Logger.getLogger(SimpleFileIO.class);
	
	public static String _eol = System.getProperty("line.separator");
	public static String _slash = System.getProperty("file.separator");
	
	/**
	 * Append a string to the end of a text file
	 * 
	 * @param sFile
	 *            File name
	 * @param sLine
	 *            String to append
	 * @throws IOException
	 */
	public static void appendLine(String sFile, String sLine) throws IOException {
		try {
			Writer output = new BufferedWriter(new FileWriter(sFile, true));
			output.append(_eol + sLine);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates or overwrites a file with new contents
	 * 
	 * @param sFile
	 *            File name
	 * @param sContents
	 *            Contents to write
	 * @throws IOException
	 */
	public static void writeFile(String sFile, String sContents) throws IOException {
		Writer output;
		File fFile = new File(sFile);

		if(!fFile.exists())
			fFile.createNewFile();

		output = new BufferedWriter(new FileWriter(fFile));
		output.append(sContents);
		output.close();
	}
	
	public static void writeFile(String sFile, ArrayList<String> contents) throws IOException {
		StringBuilder sb = new StringBuilder();
		for(String s : contents){
			sb.append(s+_eol);
		}
		writeFile(sFile, sb.toString());
	}

	/**
	 * Get the contents of a text file as a StringBuilder. StringBuilder is
	 * functionally equivalent to StringBuilder
	 * 
	 * @param sFile
	 *            File name
	 * @return Contents
	 * @throws IOException
	 */
	public static String getFileContents(String sFile) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		String sLine;
		sFile = getAbsolutePath(sFile);
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(sFile), "UTF8"));
		} catch (FileNotFoundException fnf) {
			System.out.println("File not found (" + sFile + ")  Try as resource.");
			try {
				reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(getResourceAsFile(sFile)), "UTF8"));
			} catch (Exception r_fnf) {
				System.out.println(r_fnf.getMessage());
				return "";
			}
			
		} catch (UnsupportedEncodingException unsup) {
			return "";
		} 
	
		try {
			sb.append(reader.readLine());
			sLine = reader.readLine();
			while(sLine != null) {
				sb.append(_eol + sLine);
				sLine = reader.readLine();
			}
		} catch (Exception fnf) {
			fnf.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				//
			}
		}
		return sb.toString();
	}

	public static ArrayList<String> getLinesInFile(String sFile) {
		ArrayList<String> lines = new ArrayList<String>();	
		BufferedReader reader = null;
		String sLine;
		sFile = getAbsolutePath(sFile);
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(sFile), "UTF8"));
		} catch (FileNotFoundException fnf) {
			System.out.println("File not found (" + sFile + ")  Try as resource.");
			try {
				reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(getResourceAsFile(sFile)), "UTF8"));
			} catch (Exception r_fnf) {
				System.out.println(r_fnf.getMessage());
				return lines;
			}
			
		} catch (UnsupportedEncodingException unsup) {
			log.warn(unsup.getMessage());
			return lines;
		} 
	
		try {
			lines.add(reader.readLine());
			sLine = reader.readLine();
			while(sLine != null) {
				lines.add(sLine);
				sLine = reader.readLine();
			}
		} catch (Exception fnf) {
			fnf.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				//
			}
		}
		
		return lines;
	}
	
	public static ArrayList<String> getNonEmptyLinesInFile(String sFile) {
		ArrayList<String> lines = getLinesInFile(sFile);
		for (int i=0; i<lines.size(); i++){
			if(lines.get(i).isEmpty())
				lines.remove(i);
		}
		return lines;
	}
	
	/**
	 * Returns a list of all lines in a file
	 * @param sFileName path to file
	 * @param bDataOnly <br>If <code>false</code>, will return each line in the file
	 * preserving all white space.
	 * <br><br>
	 * If <code>true</code> will trim each line and will <b>not</b> return empty lines, or comments.<br>
	 * A comment is any line beginning with the following characters:<br>
	 * <code>
	 * &nbsp;&nbsp; # &nbsp;&nbsp;(pound-sign)<br>
	 * &nbsp;&nbsp; ; &nbsp;&nbsp;(semicolon)<br>
	 * &nbsp;&nbsp; // &nbsp;(double-slash)<br>
	 * </code>
	 * @return an ArrayList of strings
	 */
	public static ArrayList<String> getLinesInFile(String sFileName, boolean bDataOnly) {
		ArrayList<String> lines = new ArrayList<String>();
		
		if(bDataOnly){
			for(String line : getLinesInFile(sFileName)){
				line = line.trim();
				if(!(line.startsWith("#") || line.startsWith(";") || line.startsWith("//") || line.isEmpty()))
					lines.add(line);
					
			}
			return lines;
		}
		
		return getLinesInFile(sFileName);
	}
	/**
	 * Gets the contents of file sFile and returns the line that begins with
	 * sBeginsWith
	 * 
	 * @param sFile
	 *            file name including path
	 * @param sBeginswith
	 *            text to look for
	 * @return
	 */
	public static String getLineStartingWith(String sFile, String sBeginswith) {

		try {
			for(String sLine : getFileContents(sFile).split(_eol)) {
				if(sLine.startsWith(sBeginswith)) {
					return sLine;
				}
			}
		} catch (Exception fnf) {
			fnf.printStackTrace();
		}
		System.out.println("Line starting with '" + sBeginswith + "' not found in file " + sFile);
		return "";
	}

	/**
	 * Gets the contents of file sFile and returns the first line that contains
	 * sContains
	 * 
	 * @param sFile
	 *            file name including path
	 * @param sContains
	 *            text to look for
	 * @return
	 */
	public static String getLineContaining(String sFile, String sContains) throws IOException {
		try {
			for(String sLine : getFileContents(sFile).split(_eol)) {
				if(sLine.contains(sContains)) {
					return sLine;
				}
			}
		} catch (Exception fnf) {
			fnf.printStackTrace();
		}
		System.out.println("'" + sContains + "' not found in file " + sFile);
		return "";
	}

	/**
	 * Replaces the line beginning with sOld with the string sNew.<br>
	 * Useful for updating property or log files.
	 * 
	 * @param sFile
	 *            File name
	 * @param sOld
	 *            Starting text of target line
	 * @param sNew
	 *            Replacement text
	 * @throws IOException
	 */
	public static void updateLine(String sFile, String sOld, String sNew) throws IOException {
		StringBuilder contents = new StringBuilder(getFileContents(sFile));
		int istart = contents.indexOf(sOld);
		if(istart < 1) {
			System.out.println("String '" + sOld + "' not found in file");
			return;
		}
		int iend = contents.indexOf(_eol, istart);
		iend = iend < istart ? contents.length() : iend;
		contents = contents.replace(istart, iend, sNew);
		try {
			Writer output = new BufferedWriter(new FileWriter(sFile));
			output.write(contents.toString());
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static File getResourceAsFile(String fileResource) {
		// Get the file resource.
		URL dir = SimpleFileIO.class.getClassLoader().getResource(fileResource);

		// If it exists in the as a resource in the files system, return it.
		if(dir != null && dir.getProtocol().equals("file")) {
			try {
				return new File(dir.toURI());
			} catch (URISyntaxException e) {
				log.error("File I/O exception");
				return null;
			}
		}

		// If the file name isn't a project resource, treat it as a direct
		// file system name.
		if(dir == null) {
			File f = new File(fileResource);
			if(f.exists())
				return f;
			else
				return null;
		}

		// Resource file is in a jar. Copy it to the a subfolder in the working
		// directory
		String autotemp = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ "auto-temp";

		// Extract the path name, if any, form the fileResource string
		String resourcePath = "";
		int i = fileResource.lastIndexOf('/');
		if(i >= 0) {
			resourcePath = fileResource.substring(0, i);
			resourcePath = resourcePath.replaceAll("[/]$", "");
		}
		// Create the resource path under auto-temp
		autotemp = resourcePath.isEmpty() ? autotemp : autotemp + "/" + resourcePath;
		File destDir = new File(autotemp);
		destDir.mkdirs();

		// Get the file name from the fileResource string
		String filename = fileResource.replace(resourcePath + "/", "");
		File destFile = new File(destDir, filename);

		// If the file already exists in the filesystem, don't copy it again
		if(destFile.exists()) {
			return destFile;
		}

		// Copy the resource file to the filesystem
		InputStream in = SimpleFileIO.class.getClassLoader().getResourceAsStream(fileResource);

		byte[] bytebuf = new byte[1024];
		int length = 0;

		try {
			FileOutputStream os = new FileOutputStream(destFile);
			while((length = in.read(bytebuf)) > 0) {
				os.write(bytebuf, 0, length);
			}
			os.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// ************

		if(destFile.exists() && !Platform.isWindows()) {
			String cmd = "chmod 777 " + destFile.getPath().replace(" ", "\\ ");
			System.out.println("change mode: " + cmd);
			Platform.runCommandAndWait(cmd);
		} else {
			System.out.println("FILE DOESN'T EXIST: " + destFile.getPath());
		}
		return destFile.exists() ? destFile : null;
	}

	/**
	 * @return List of files with the given fileExtension in the directory
	 *         specified
	 */
	public static List<String> getListOfFiles(String path, String fileExtension) {
		List<String> ret = null;
		String dataPath = getAbsolutePath(path);
		dataPath = dataPath.replaceAll("[/]$", "");
		
		final String ext = fileExtension;
		@SuppressWarnings("rawtypes")
		Class thisClass = new SimpleFileIO().getClass();
		URL dir = thisClass.getClassLoader().getResource(dataPath);
		// IF NO RESOURCE PATH IS FOUND, TRY THE RUNTIME DIRECTORY
		if(dir == null) {
			File f = new File(dataPath);
			try {
				dir = f.toURI().toURL();
			} catch (MalformedURLException e) {
				dir = null;
			}
		}
		// IF A FILE OBJECT IS FOUND (IN RESOURCES OR RUNTIME DIRECTORY...
		if(dir != null && dir.getProtocol().equals("file")) {
			File localFile = new File("foo");
			try {
				localFile = new File(dir.toURI());
			} catch (URISyntaxException e1) {
				//
			}
			if (localFile.exists()) {
				try {
					ret = Arrays.asList(new File(dir.toURI()).list(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.endsWith(ext);
						}
					}));
				} catch (URISyntaxException e) {
					return null;
				}
			}
		}
		// ...OTHERWISE LOOK IN THE JAR RESOURCES
		else {
			HashMap<String, List<String>> jarPathMap = new HashMap<String, List<String>>(50);
			dir = thisClass.getResource(thisClass.getSimpleName() + ".class");

			if(dir.getProtocol().equals("jar")) {
				if(jarPathMap.isEmpty()) {
					try {
						jarPathMap = new HashMap<String, List<String>>(50);
						String jarpath = dir.getPath().substring(5, dir.getPath().indexOf("!"));
						JarFile jar = new JarFile(URLDecoder.decode(jarpath, "UTF-8"));

						Enumeration<JarEntry> entries = jar.entries();
						JarEntry entry = null;
						while(entries.hasMoreElements() && (entry = entries.nextElement()) != null) {
							String resource = entry.getName();
							int index = resource.lastIndexOf('/');
							String respath = index == -1 ? "" : resource.substring(0, index);
							String resname = index == -1 ? resource : resource.substring(index + 1);

							List<String> list = jarPathMap.get(respath);
							if(list == null)
								jarPathMap.put(respath, list = new ArrayList<String>());
							if(resname.endsWith(ext))
								list.add(resname);
						}
					} catch (IOException ioe) {
						return null;
					}
				}

				return jarPathMap.get(dataPath);
			}
		}

		return ret;
	}
	
	public static boolean copyFileToDirectory(String sourceFile, String destDir, boolean bCreateDestFolder) throws IOException {
		File source = new File(sourceFile);
		if(!source.exists()){
			source = getResourceAsFile(sourceFile);
			if(source == null) {
				log.error(sourceFile + " doesn't exist");
				return false;
			}
		}
		if (!source.exists()) {
			log.error(source.getPath() + " doesn't exist");
			return false;
		}
		File dest = new File(destDir);
		if (!dest.exists() && !bCreateDestFolder) {
			log.error("Destination path (" + destDir + ") doesn't exist");
			return false;
		}

		FileUtils.copyFileToDirectory(source, dest, bCreateDestFolder);
		String name = source.getName();
		File newFile = new File(destDir, name);
		return newFile.exists();
	}
	

	public static Properties getProperties(String file) {
		Properties properties = new Properties();

		try {
			// LOOK FOR THE FILE IN THE RESOURCE AREA (FILE SYSTEM OR JAR)
			InputStream in = null;
			if (file.startsWith("file:")) {
				in = new FileInputStream(new File(new URI(file)));
			} else {
				in = new SimpleFileIO().getClass().getClassLoader().getResourceAsStream(file);
			}
			// IF NOTHING FOUND IN RESOURCES, LOOK IN THE RUNTIME DIRECTORY
			if (in == null) {
				File localTarget = new File(file);
				if (!localTarget.exists()) {
					throw new FileNotFoundException();
				} else {
					in = new FileInputStream(localTarget);
				}
			}
			// Changing to UTF-8 to support accented Latin characters
			properties.load(new InputStreamReader(in, "UTF8"));
			in.close();
		} catch (FileNotFoundException fnf) {
			System.out.println(fnf.getMessage());
			properties = null;
			if (log != null)
				log.warn("Properties file not found: " + file);
		} catch (Throwable t) {
			System.out.println(t.getMessage());
			properties = null;
			if (log != null)
				log.warn("Error reading properties file: " + file);
		}

		return properties;
	}
	
	public static String getAbsolutePath(String sFilePath){
		File f = new File(sFilePath);
		if(f.exists())
			return f.getAbsolutePath();
		return sFilePath;
	}
}

package com.lianjia.automation.core.util;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.lianjia.automation.CoreAutomation;
import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.loggers.control.CoreLogger;

/**
 * Script Name   : <b>FileHelper</b>
 */
public class FileHelper
{
	/**Buffer size (32KB) for file manipulation methods.*/
	 public static int FILE_BUFFER_SIZE = 32 * 1024;
	 public static String slash = "/";
	 public static String backslash = "\\";
	 
	/**
	 * Deletes specified file or directory (if directory is specified the method 
	 * will recursively delete all files and or subdirectories within the specified parent directory. 
	 * (i.e. deleteFile("c:\\my test folder\\") will delete the directory "my test folder" and all files
	 * and subdirectories within that directory. Be extremely careful using this function<p>
	 * @param filename = path & filename of file or directory to delete
	 */    
	public static void deleteFile(String filename)
	{
		try
		{
			// Create a File object to represent the filename
			File f = new File(filename);

			// Make sure the file or directory exists and isn't write protected
			if (!f.exists())
			{
				return;
			}

			if (!f.canWrite())
			{
				f.setWritable(true);
			}

			// If it is a directory, recursively delete all files in the directory
			if (f.isDirectory())
			{
				String[] files = f.list();

				if (files.length > 0)
				{
					for (int x = 0; x < files.length; x++)
					{
						deleteFile(f.getAbsolutePath() + File.separator + files[x]);
					}
				}
			}

			// If we passed all the tests, then attempt to delete it
			boolean success = f.delete();
			if (!success)
			{
				return;
			}
		}
		catch (IllegalArgumentException iae)
		{
			CoreLogger.error("deleting file", iae);
			return;
		}
	}
	
	/**
	 * getFilename returns the filename from a full path
	 * @param fullPathToFile
	 * @return String - name of file
	 */
	public static String getFilename(String fullPathToFile)
	{
		try {
			return new File(fullPathToFile).getName();
		} catch (Exception e) {
			return "";
		}
	}
       
	/**
	 * Creates specified directories <p>
	 * @param sDirName = path &  directory name to create. example - MakeDir("c:\\First\\Second\\Third");
	 * this will create all three directories nested within the parent directory
	 * @return boolean true if directory was created false if it was not
	 */
	public static boolean makeDirs(String sDirName)
	{
		// Create a File object to represent the filename
		File dir = new File(sDirName);

		// If the directory doesn't already exists create a new directory
		if (!dir.exists())
		{
			return dir.mkdirs();
		}
		else
		{
			return true;
		}

	} 
	
	/**
	 * Copies all files under source folder to destination folder. If destination folder does
	 * not exist, it will be created.
	 *
	 * @param fromDirectory source
	 * @param toDirectory destination
	 *
	 * @return true if success, false otherwise
	 */       
	public static boolean copyDir(String fromDirectory, String toDirectory) {
		return copyDir(new File(fromDirectory), new File(toDirectory));
	}
	
	private static boolean copyDir(File fromDirectory, File toDirectory) {
		if (toDirectory.getAbsolutePath().indexOf(fromDirectory.getAbsolutePath())==0)
			return false;
		if (fromDirectory.isDirectory()) {
			if (!toDirectory.exists()) {
				toDirectory.mkdir();
			}
			String[] files = fromDirectory.list();
			for (int i = 0; i < files.length; i++) {
				if (copyDir(new File(fromDirectory, files[i]), new File(toDirectory, files[i])) == false) {
					return false;
				}
			}
			return true;
		}
		return copyFile(fromDirectory, toDirectory);
	}

	/**
	 * Copies one file to another without any checkings.
	 *
	 * @param fileIn  source
	 * @param fileOut destination
	 *
	 * @return true if operation was successful, false otherwise
	 */       
	public static boolean copyFile(String fileIn, String fileOut) {
		return copyFile(new File(fileIn), new File(fileOut), FILE_BUFFER_SIZE);
	}
	
	/**
	 * Copies one file to another without any checkings.
	 *
	 * @param fileIn  source
	 * @param fileOut destination
	 *
	 * @return true if operation was successful, false otherwise
	 */
	private static boolean copyFile(File fileIn, File fileOut) {
		return copyFile(fileIn, fileOut, FILE_BUFFER_SIZE);
	}
	
	
	/**
	 * Copies one file to another without any checkings.
	 *
	 * @param fileIn  source
	 * @param fileOut destination
	 * @param bufsize buffer size
	 *
	 * @return true if operation was successful, false otherwise
	 */
	public static boolean copyFile(String fileIn, String fileOut, int bufsize) {
		return copyFile(new File(fileIn), new File(fileOut), bufsize);
	}

	/**
	 * Copies one file to another without any checkings.
	 *
	 * @param fileIn  source
	 * @param fileOut destination
	 * @param bufsize buffer size
	 *
	 * @return true if operation was successful, false otherwise
	 */
	private static boolean copyFile(File fileIn, File fileOut, int bufsize) {
		FileInputStream in = null;
		FileOutputStream out = null;
		boolean result = false;
		if(!fileIn.exists()) 
			return result;
		try {
			if(!fileOut.exists()){
				File parent = new File(fileOut.getParent());
				if(!parent.exists()){
					parent.mkdirs();
				}
				fileOut.createNewFile();
			}

			in = new FileInputStream(fileIn);
			out = new FileOutputStream(fileOut);
			copyPipe(in, out, bufsize);
			result = true;
		} catch(IOException ioex) {
			CoreAutomation.Log.error("Error copying '" + fileIn + "' to '" + fileOut + "'", ioex);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch(IOException ioex) {
					CoreAutomation.Log.error("Error closing file '" + fileOut + "'", ioex);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch(IOException ioex) {
					CoreAutomation.Log.error("Error closing file '" + fileIn + "'", ioex);
				}
			}
		}
		return result;
	}
       
	/**
	 * Reads from input and writes read data to the output, until the stream ends.
	 *
	 * @param in
	 * @param out
	 * @param bufSizeHint
	 *
	 * @throws IOException
	 */
    public static void copyPipe(InputStream in, OutputStream out, int bufSizeHint) throws IOException {
		int read = -1;
		byte[] buf = new byte[bufSizeHint];
		while ((read = in.read(buf, 0, bufSizeHint)) >= 0) {
			out.write(buf, 0, read);
		}
		out.flush();
	}
    
	/**
	 * Unpacks a zip file to the target directory.
	 *
	 * @param zipFile zip file
	 * @param destDir destination directory
	 *
	 * @throws IOException
	 */       
	public static void unzip(String sZipFile, String sDestDir) throws IOException {
		unzip(new File(sZipFile), new File(sDestDir));	
	}
	
	public static void unzip(File zipFile, File destDir) throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		Enumeration en = zip.entries();
		int bufSize = 8196;

		while (en.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) en.nextElement();
			File file = (destDir != null) ? new File(destDir, entry.getName()) : new File(entry.getName());
			if (entry.isDirectory()) {
				if (!file.mkdirs()) {
					if (file.isDirectory() == false) {
						throw new IOException("Error creating directory: " + file);
					}
				}
			} else {
				File parent = file.getParentFile();
				if (parent != null && !parent.exists()) {
					if (!parent.mkdirs()) {
						if (file.isDirectory() == false) {
							throw new IOException("Error creating directory: " + parent);
						}
					}
				}

				InputStream in = zip.getInputStream(entry);
				try {
					OutputStream out = new BufferedOutputStream(new FileOutputStream(file), bufSize);
					try {
						copyPipe(in, out, bufSize);
					} finally {
						out.close();
					}
				} finally {
					in.close();
				}
			}
		}
	}
	
	/**
	 * Writes information to a temporary file on disk.
	 * @param String sFilename
	 * @param String sKey
	 * @param String sValue
	 */
	public static void setTempVarToDisk(String sFilename, String sKey, String sValue)
	{
		try
		{
			FileOutputStream out = new FileOutputStream(sFilename);
			Properties settings = new Properties();
	
			settings.put(sKey, sValue);
			
			//Close out properties file
			settings.store(out, "");
			out.close();
		}
		catch (IOException ioe)
		{
			CoreLogger.error("Error saving temporary variable from disk: " + sFilename,ioe);		
		}
	}

	/**
	 * Gets information from a temporary file on disk.
	 * @param String sFilename
	 * @param String sKey
	 * @return String sValue 
	 */
	public static String getTempVarToDisk(String sFilename, String sKey)
	{
		try
		{
			FileInputStream in = new FileInputStream(sFilename);
			Properties settings = new Properties();
			settings.load(in);
	
			String sTmp = settings.getProperty(sKey);
			in.close();
			
			return sTmp;
			
		}
		catch (IOException ioe)
		{
			CoreLogger.error("Error getting temporary variable from disk: " + sFilename,ioe);
			return null;
		}	
	}
	
	/**
	 * Gets information from a temporary file on disk withotu logging the error
	 * @param String sFilename
	 * @param String sKey
	 * @return String sValue 
	 */
	public static String getTempVarToDisk(String sFilename, String sKey, boolean bIgnoreError)
	{
		try
		{
			FileInputStream in = new FileInputStream(sFilename);
			Properties settings = new Properties();
			settings.load(in);
	
			String sTmp = settings.getProperty(sKey);
			in.close();
			
			return sTmp;			
		}
		catch (IOException ioe)
		{
			if (bIgnoreError){return null;}
			else
				{				
					CoreLogger.error("Error getting temporary variable from disk: " + sFilename,ioe);
					return null;
				}
		}	
	}	
	
	/**
	* Returns a file extension for a file name
	* @param - String sFile - file name or path i.e. c:\\temp\\myfle.java
	*/
	public static String getFileExtension(String sFile)
	{
		try {
			int extensionMark = sFile.lastIndexOf(".");
			return sFile.substring(extensionMark + 1);			
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	* Removes specified file extension from a given file name or path name
	* @param - String sFile - file name or path i.e. c:\\temp\\myfle.java
	* @param - String sExt - extension to be removed i.e. ".java"
	* @return  file name string without extension i.e. "c:\\temp\\myfile"
	*/
	public static String removeFileExtension(String sFile, String sExt)
	{
		String sOut = "";
		sOut = StringHelper.replace(sFile,sExt,"");
		return sOut;
	}
	
	/**
	 * updates the specified file by replacing the sSearchFor string with the sReplaceWith string
	 * and re-writing the file out with updated contents
	 * @param String sFile - the file to be read in and updated
	 * @param String sSearchFor - the string to be replaced
	 * @param String sSearchFor - the string to be added
	 */
	public static void updateFile(String sFile, String sSearchFor, String sReplaceWith)
	{		
		//display error if specified file does not exist
		File f = new File(sFile);
		if (!f.exists()){
			CoreLogger.warn("Could not find file: " + sFile);
			return;
		}
		
		String sOut = FileHelper.getFileContents(sFile);
		
		if (sOut.indexOf(sSearchFor) == -1){
			return;
		}
		
		//replace search string with replace string
		sOut = StringHelper.replace(sOut,sSearchFor,sReplaceWith);
		
		//write out updated file
		FileHelper.writeFileContents(sFile,sOut);		
	}
	
	/**
	 * updates the specified file by replacing the sSearchFor string with the sReplaceWith string
	 * and re-writing the file out with updated contents
	 * @param String sFile - the file to be read in and updated
	 * @param String sSearchFor - the string to be replaced
	 * @param String sReplaceWith - the string to be added
	 * @param String charset - charset such as "UTF-8"
	 */
	public static void updateFile(String sFile, String sSearchFor, String sReplaceWith, String charset){
		//display error if specified file does not exist
		File f = new File(sFile);
		if (!f.exists()){
			CoreLogger.error("Could not find file: " + sFile);
			return;
		}
		
		String sOut = FileHelper.getFileContents(sFile,charset);
		
		if (sOut.indexOf(sSearchFor) == -1){
			CoreLogger.error("Could not find search string: " + sSearchFor + " in the file " + sFile);
			return;
		}
		
		//replace search string with replace string
		sOut = StringHelper.replace(sOut,sSearchFor,sReplaceWith);
		
		//write out updated file
		FileHelper.writeFileContents(sFile,sOut,charset);
	}
	
	/**
	 * updates the specified file by replacing the sSearchFor string with the sReplaceWith string. Use charset "UTF-8".
	 * and re-writing the file out with updated contents
	 * @param String sFile - the file to be read in and updated
	 * @param String sSearchFor - the string to be replaced
	 * @param String sReplaceWith - the string to be added
	 */
	public static void updateFileByUTF8(String sFile, String sSearchFor, String sReplaceWith){
		updateFile(sFile, sSearchFor, sReplaceWith, "UTF-8");
	}
	
	/**
	 * updates the specified property file by replacing the sSearchkey value with a sNewVal
	 * and re-writes the file with updated contents
	 * @param String sPropFile - the property file (complete path and filename) to be read in and updated
	 * @param String sSearchKey - the property key to search for
	 * @param String sNewVal - the new value for the search key property
	 */
	public static void updatePropertyFile(String sPropFile, String sSearchKey, String sNewVal)
	{		
		//display error if specified prop file does not exist
		File f = new File(sPropFile);
		if (!f.exists()){
			CoreLogger.error("Could not find property file: " + sPropFile);
			return;
		}
		
		//get property file content as string array
		String sTmp[] = FileHelper.getFileContentsAsList(sPropFile);
		
		Pattern key = Pattern.compile("\\s*" + sSearchKey + "\\s*=.*");
		String sLine = ""; 
		String s = "";
		
		//cycle through property file contents line by line
		for (int x =0;x < sTmp.length;x++)
			{
			
			//ignore empty or null lines
			if (sTmp[x] == null || sTmp[x].toString().equals("")) {
				continue;
			}
			
			//ignore comment lines
			if (sTmp[x].toString().indexOf("#") == 0)
				continue;
			
			//assign line content to string s
			s = sTmp[x].toString();
			
			//compare line s to the searchkey
			if (key.matcher(s).matches())
				{
					//if searchkey is found get line content and ssign it to sLine and break loop
					sLine = sTmp[x].toString();
					break;
				}
			}
		
		if (sLine.equals("")){
			CoreLogger.error("Could not find property key: " + sSearchKey + " in property file " + sPropFile);
			return;
		}
		
		
		//get property file content as string
		String sOut = FileHelper.getFileContents(sPropFile);	
		
		//replace original property key and value with new value
		sOut = StringHelper.replace(sOut,sLine,sSearchKey + "=" + sNewVal);
		
		//write out updated property file
		FileHelper.writeFileContents(sPropFile,sOut);
	}	
		
	/** 	
	* Reads specified file contents and returns file contents as a string <p>
	* @param filename		Path and filename of file to read
	* @return String of specified file contents
	*/
	public static String readFile(String source) {
		String file = "";
		try {
			FileInputStream fstream = new FileInputStream(source);
			BufferedReader in =
				new BufferedReader(new InputStreamReader(fstream));
			String line;

			while ((line = in.readLine()) != null) {
				file = file + line + "\n";
			}
			in.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#readFile: " + e.getMessage());
		}
		return file;
	}

	/** 	
	* Reads specified file contents and returns file contents as a string <p>
	* @param filename 		Path and filename of file to read
	* @return   String of specified file contents
	*/
	public static String getFileContents(String filename) {
		if(!fileExists(filename))
		{
			return "";
		}
		try {
			File file = new File(filename);
			FileReader in = new FileReader(file);
			char c[] = new char[(int) file.length()];
			in.read(c);
			String fileContentsString = new String(c);
			in.close();
			return fileContentsString;
		} catch (Exception e) {
			e.printStackTrace();
			CoreLogger.error("Error in FileHelper#getFileContents: " + e.getMessage());
			return "";
		}
	}
	
	/**
	 * Get file contents from specified file by given charset.
	 * @param filename - absolute path of target file
	 * @param charset - charset such as "UTF-8"
	 * @return String - content of target file or null if there is any exception occurred
	 */
	public static String getFileContents(String filename, String charset){
		//create reader - if charset is not specified or not supported, use default charset;
		InputStreamReader reader = null;
		if(charset==null){
			try {
				reader = new InputStreamReader(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + e.getMessage());
				return "";
			}
		}else{
			try {
				try {
					reader = new InputStreamReader(new FileInputStream(filename),charset);
				} catch (FileNotFoundException e) {
					CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + e.getMessage());
					return "";
				}
			} catch (UnsupportedEncodingException e) {
				try {
					reader = new InputStreamReader(new FileInputStream(filename));
				} catch (FileNotFoundException ee) {
					CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + ee.getMessage());
					return "";
				}
			}
		}
		
		//read content
		char[] c = new char[1024];
		int count = 0;
		StringBuffer buffer = new StringBuffer();
		try {
			while((count=reader.read(c))!=-1){
				buffer.append(c,0,count);
			}
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + e.getMessage());
			return "";
		} finally{
			try{
				reader.close();
			} catch (IOException e) {
				CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + e.getMessage());
				return "";
			}
		}		
		
		//return
		return buffer.toString();
	}
	
	/**
	 * Get file contents from specified file by charste "UTF-8".
	 * @param filename - absolute path of target file
	 * @return String - content of target file or null if there is any exception occurred
	 */
	public static String getFileContentsByUTF8(String filename){
		return getFileContents(filename, "UTF-8");
	}
	
	/** 	
	* Returns File array of files contained within a given directory<P>
	* @param  sDir	Directory to read
	* @return   Returns File array of files contained within a given directory
	*/
	public static File[] getDirContents(String sDir) {

		try {
			File fDir = new File(sDir);
			
			return fDir.listFiles();
		}
		catch (Exception e) {
			CoreLogger.error("Error in FileHelper#getDirContents: " + e.getMessage());
			return null;
		}
	}
	
	/** 	
	* Returns file content as a String Array
	* @param filename 		Path and filename of file to read
	* @return file content as a String Array
	*/
	public static String[] getFileContentsAsList(String filename) {
		int z = 0;
		int n = getNumberOfLinesInFile(filename);
		String lsLines[] = new String[n];

		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = in.readLine()) != null) {
				if (z >= n)
					break;
				
				lsLines[z] = line;
				z++;
			}
			in.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#getFileContentsAsList: " + e.getMessage());
		}
		return lsLines;
	}
	
	/**
	 * Returns file content as a String array.
	 * @param filename - absolute path of target file
	 * @param charset - charset such as "UTF-8"
	 * @return String[] - String array which contain file content one line by one element
	 */
	public static String[] getFileContentsAsList(String filename, String charset) {
		int z = 0;
		int n = getNumberOfLinesInFile(filename);
		String lsLines[] = new String[n];
		
		// create reader - if charset is not specified or not supported, use default charset;
		BufferedReader reader = null;
		if(charset==null){
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			} catch (FileNotFoundException e) {
				CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + e.getMessage());
				return lsLines;
			}
		}else{
			try {
				try {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename),charset));
				} catch (FileNotFoundException e) {
					CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + e.getMessage());
					return lsLines;
				}
			} catch (UnsupportedEncodingException e) {
				try {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
				} catch (FileNotFoundException ee) {
					CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + ee.getMessage());
					return lsLines;
				}
			}
		}		
		
		//read content
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (z >= n)
					break;
				lsLines[z] = line;
				z++;
			}
			reader.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#getFileContents(String filename, String charset): " + e.getMessage());
			return lsLines;
		}
		
		//return
		return lsLines;
	}
	
	/**
	 * Returns file content as a String array. Use charset "UTF-8".
	 * @param filename - absolute path of target file
	 * @param charset - charset such as "UTF-8"
	 * @return String[] - String array which contain file content one line by one element
	 * 
	 */
	public static String[] getFileContentsAsListByUTF8(String filename) {
		return getFileContentsAsList( filename, "UTF-8");
	}
	
	/** 	
	* Returns file content as a String Array
	* @param filename 		Path and filename of file to read
	* @return file content as a String Array
	*/
	public static String[] getFileContentsFromFileInJar(String filename) {
		int z = 0;
		int n = getNumberOfLinesInFileInJar(filename);
		String lsLines[] = new String[n];

		try {
			InputStream myInputStream = FileHelper.class.getResourceAsStream(filename);
			BufferedReader in = new BufferedReader(new InputStreamReader(myInputStream));
			String line;
			while ((line = in.readLine()) != null) {
				if (z >= n)
					break;
				
				lsLines[z] = line;
				z++;
			}
			in.close();
		} catch (IOException e) {
			
			CoreLogger.error("Error in FileHelper#getFileContentsFromJar: " + e.getMessage());
		}
		return lsLines;
	}
	
	/** 	
	* Returns number of lines in a specified file <p>
	* @param filename		Path and filename of file to read
	* @return number of lines in specified file
	*   
	*/
	public static int getNumberOfLinesInFile(String filename) {
		int i = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while (in.readLine() != null)
				i++;
			in.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#getNumberOfLinesInFile: " + e.getMessage());
		}
		return i;
	}

	/** 	
	* Returns number of lines in a specified file <p>
	* @param filename		Path and filename of file to read
	* @return number of lines in specified file
	*/
	public static int getNumberOfLinesInFileInJar(String filename) {
		int i = 0;
		try {
			InputStream myInputStream = FileHelper.class.getResourceAsStream(filename);
			BufferedReader in = new BufferedReader(new InputStreamReader(myInputStream));
			while (in.readLine() != null)
				i++;
			in.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#getNumberOfLinesInFileInJar: " + e.getMessage());
		}
		return i;
	}

	/** 
	* Writes specified string content to file <p>
	* @param filename		path and filename of file to write string to
	* @param sContents		String to write to file
	*   
	*/
	public static void writeFileContents(String filename, String sContents) {
		//write specified string content to file	
		try {
			FileWriter out = new FileWriter(filename);
			out.write(sContents);
			out.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#writeFileContents: " + e.getMessage());
		}
	}
	
	/**
	 * Write specified string content into given file with specified charset. If the charset is null or not supported, will use default charset.
	 * @param filename - absolute path of file
	 * @param sContents - content to be written
	 * @param charset - charset such as "UTF-8"
	 * 
	 */
	public static void writeFileContents(String filename, String sContents, String charset) {
		//	write specified string content to file
		OutputStreamWriter out = null;
		
		// if charset is null, use default charset
		if(charset==null){
			try {
				out = new OutputStreamWriter(new FileOutputStream(filename));
			} catch (FileNotFoundException e) {
				CoreLogger.error("Error in FileHelper#writeFileContents(String filename, String sContents, String charset): " + e.getMessage());
			}
		}else{
			// if charset is specified, try use it; if it is not support, use default charset
			try {
				out = new OutputStreamWriter(new FileOutputStream(filename),charset);
			} catch (UnsupportedEncodingException e) {
				CoreLogger.warn("FileHelper#writeFileContents(String filename, String sContents, String charset): '"+charset+"' is NOT SUPPORTED! Default charset will be used!");
				try {
					out = new OutputStreamWriter(new FileOutputStream(filename));
				} catch (FileNotFoundException ee) {
					CoreLogger.error("Error in FileHelper#writeFileContents(String filename, String sContents, String charset): " + ee.getMessage());
				}
			} catch (FileNotFoundException e) {
				CoreLogger.error("Error in FileHelper#writeFileContents(String filename, String sContents, String charset): " + e.getMessage());
			}
		}
		try {
			try {
				out.write(sContents);				
			} catch (Exception e) {
				// TODO: handle exception
			}
			out.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#writeFileContents(String filename, String sContents, String charset): " + e.getMessage());
		}
	}
	
	/**
	 * Write specified string content into give file with UTF-8 encoding.
	 * @param filename - absolute path of file
	 * @param sContents - content to be written
	 * 
	 */
	public static void writeFileContentsByUTF8(String filename, String sContents){
		writeFileContents(filename, sContents, "UTF-8");
	}
	
	/** 
	* Appends specified string content to a file <p>
	* @param filename		path and filename of file to append string to
	* @param sContents		String to append to file
	*   
	*/
	public static void appendStringToFile_Old(String filename, String sContents) {
		try {
			FileWriter out = new FileWriter(filename, true); //tells FileWriter to append
			out.write(System.getProperty("line.separator") + sContents);
			out.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#appendStringToFile: " + e.getMessage());
		}
	}
	
	/**
	 * write contents to given file except .html file using UTF-8 encoding.
	 * @param filename - name of target file 
	 * @param sContents - content will be written to 
	 */
	public static void appendStringToFile(String filename, String sContents) {
		try {
			FileOutputStream out = new FileOutputStream(filename,true);
			byte[] bytes=getbyteString(sContents,"UTF-8");
			out.write(bytes);
			out.close();
		} catch (Exception e) {
			CoreLogger.error("Error in FileHelper#appendStringToFile: " + e.getMessage());
		}
	}
	
	/**
	 * This method is just used to write content to log html file
	 * @param filename - name of html log
	 * @param sContents - content will be written to html file.
	 */
	public static void appendStringToHtmlFile(String filename, String sContents) {
		try {
			FileOutputStream out = new FileOutputStream(filename,true);
			byte[] bytes=getbyteString(System.getProperty("line.separator") + sContents,null);
			out.write(bytes);//contentBytes
			out.close();
		} catch (IOException e) {
			CoreLogger.error("Error in FileHelper#appendStringToFile: " + e.getMessage());
		}
	}
	
	/**
	 * print given byte array
	 * @param banner
	 * @param abc
	 */
	public static void printBytes(String banner,byte[] abc){
		System.out.print(banner+"=");
		for(int i=0; i<abc.length; i++){
			System.out.print(java.lang.Integer.toHexString(abc[i])+"  ");
		}
		System.out.println();
	}
	
	/**
	 * 
	 * @param s - string
	 * @param coding - encoding way. like UTF-8, UTF-16, GB2312 and so on. if coding equals null, it will use default encoding way.
	 * @return bytes of given string using specific encoding
	 */
	public static byte[] getbyteString(String s, String coding){
		if (s == null) {
			return null;
		}
		byte[] result;
		try{
			if(null==coding){
				result=s.getBytes();
			}else
				result=s.getBytes(coding);
			return result;
		}catch(Exception e){
			result=null;
		}
		return result;
	}
	
	/**
     * This method takes a path and returns the parent.  Works with either
     * backslash or slash delimiter. Examples: c:\test1\blah  returns
     * c:\test1\ <br>
     * /usr/local/bin/bash returns /usr/local/bin/
     *
     * @param path The path to get the parent of
     *
     * @return The parent path
     */
    public static String getParentPath(String path)
    {
        //break up the filename and the path
        int i = path.lastIndexOf("\\");
        int j = path.lastIndexOf("/");
        int last = (i > j) ? i : j;

        return path.substring(0, last + 1);
    }

    /**
     * This method takes a path and strips the path info.  Works with either
     * backslash or slash delimiter. Examples: c:\test1\blah  returns blah <br>
     * /usr/local/bin/bash returns bash
     *
     * @param path The path to strip
     *
     * @return The final filename or directory name without path info
     */
    public static String stripPath(String path)
    {
        //break up the filename and the path
        int i = path.lastIndexOf("\\");
        int j = path.lastIndexOf("/");
        int last = (i > j) ? i : j;

        return path.substring(last + 1, path.length());
    }

    /**
     * Strips trailing slash from specified path string
     * @param String path - (i.e. "c:\\auto\\test\\one\\")
     */
    public static String stripTrailingSlash(String path)
    {
        if (path.equals(""))
        {
            return path;
        }

        int i = path.lastIndexOf("\\");
        int j = path.lastIndexOf("/");
        int last = (i > j) ? i : j;

        if (last == (path.length() - 1))
        {
            return path.substring(0, path.length() - 1);
        }
        else
        {
            return path;
        }
    }
    
    /**
     * Strips leading slash from specified path string
     * @param String path - (i.e. "c:\\auto\\test\\one\\")
     */
    public static String stripLeadingSlash(String path){
    	if (path.startsWith("/") || path.startsWith("\\")) return path.substring(1);
    	return path;
    }

    /**
     * Returns the first part of a path.  For instance, if the input is  "/My
     * Portal/blah2/blah4", it returns "My Portal".  It does not matter if the
     * path starts with a slash or not.  Works with either backslash or slash.
     *
     * @param path The path to operate on.
     *
     * @return The first part of the path.
     */
    public static String getTopParent(String path)
    {    	
        path = normalizePath(path);

        int i = path.indexOf(backslash);
        int j = path.indexOf(slash);
        String sep;

        if (i < 0)
        {
            sep = slash;
        }
        else if (j < 0)
        {
            sep = backslash;
        }
        else
        {
            sep = (i < j) ? backslash : slash;
        }

        StringTokenizer st = new StringTokenizer(path, sep);

        try
        {
            return st.nextToken();
        }
        catch (Exception nsee)
        {
            return "";
        }
    }
		
    /**
     * Strips off the first part of a path. For instance, if the input is  "/My
     * Portal/blah2/blah4", it returns "/blah2/blah4".  Backslash is not
     * supported.
     *
     * @param path The path to operate on
     *
     * @return The path with the first part stripped off.
     */
    public static String stripTopParent(String path)
    {
        if (path.length() <= 1)
        {
            return "";
        }

        path = normalizePath(path).substring(1);

        int first = path.indexOf(slash);
        path = path.substring((first == -1) ? path.length() : first,
                path.length()).trim();

        return path.equals("/") ? "" : path;
    }

    /**
     * Adds a leading slash, if necessary, to a path name, and removes any
     * multiple  consecutive slashes "///" if necessary.
     *
     */
    public static String normalizePath(String path)
    {
        if (!path.startsWith("/"))
        {
            path = "/" + path;
        }

        while (path.indexOf("//") != -1)
        {
            path = StringHelper.replace(path, "//", "/");
        }

        return stripTrailingSlash(path);
    }

    /**
     * Deletes specified directories
     * <p>
     * 
     * @param sDirName =
     *            path & directory name to delete. example -
     *            deleteDirs("c:\\First\\Second\\Third"); 
     */

    public static void deleteDirs(String sDirName) {
    	// Create a File object to represent the filename
    	File dir = new File(sDirName);
    	try {
    		// If the directory doesn't already exists, return
    		if (!dir.exists()) {
    			return;
    		}
    		boolean success = dir.delete();
    		if (!success) {
    			return;
    		}
    	} catch (IllegalArgumentException iae) {
    		CoreLogger.error("deleting directory", iae);
    		return;
    	}
    }

    public static boolean fileExists(String sFileName)
    {
    	File fileToCheck = new File(sFileName);
		return fileToCheck.exists();
    }
    
    public static boolean pathExists(String sPathName)
    {
    	return fileExists(sPathName);
    }
    
    /**
	 * Returns the specified property from the given properties file
	 *
	 * @param String sKey - Property Key for value to return
	 * @param fileName properties file name to load
	 *
	 * @exception IOException
	 */
	public static String getPropertyFromXMLFile(String propertyName, String fileName) {
		return getPropertyFromXMLFile(propertyName, fileName, "parameter");
	}
	
	public static String getPropertyFromXMLFile(String propertyName, String fileName, String propertyType) {
		CoreLogger.debug("getPropertyFromXMLFile() - fileName = '" + fileName + "'");
		
		CoreLogger.debug("getPropertyFromXMLFile() - propertyName = '" + propertyName + "'");
		String value = "";
		if (!FileHelper.fileExists(fileName))
		{
			System.err.println("FATAL ERROR: " + fileName + " does not exist...");
			return value;
		}
		
		String[] myXMLFileContents = getFileContentsAsList(fileName);
		
		for (int currentLine = 0; currentLine < myXMLFileContents.length; currentLine++) {
			if(myXMLFileContents[currentLine].contains(propertyName) && myXMLFileContents[currentLine].toLowerCase().contains(propertyType.toLowerCase()))
			{
				CoreLogger.debug("getPropertyFromXMLFile() - currentLine = '" + myXMLFileContents[currentLine] + "'");
				try {
					CoreLogger.debug("getPropertyFromXMLFile() - currentLine +1 = '" + myXMLFileContents[currentLine + 1] + "'");
					value = myXMLFileContents[currentLine + 1].replace("</value>", "").split(">")[1];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}		
		return value;
	}	
	
    /**
	 * Returns the specified property from the given properties file
	 *
	 * @param String sKey - Property Key for value to return
	 * @param fileName properties file name to load
	 *
	 * @exception IOException
	 */
	public static String getPropertyFromFile(String sKey, String fileName) {
		Properties prop = new Properties();
		File file = new File(fileName);
		String sPropVal = "";
		if (!file.exists())
		{
			System.err.println("FATAL ERROR: " + fileName + " does not exist...");
		}
			
		//assign property value
		try{
			loadFromFile(prop, file);
			sPropVal = prop.getProperty(sKey);
			
			}
		catch(Exception e){sPropVal = "";}
		return sPropVal;		
	}

	/**
	 * Loads properties from the file. Properties are appended to the existing
	 * properties object.
	 *
	 * @param p      properties to fill in
	 * @param file   file to read properties from
	 *
	 * @exception IOException
	 */
	private static void loadFromFile(Properties p, File file) throws IOException {
		if (p == null) {
			return;
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			p.load(fis);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}	
	
	/**
	 * Writes properties to a file.
	 *
	 * @param fileNameInJar      FileName in the jar - assumes extraction from the jar your code is running in
	 * @param extractedFileName	FileName on the filesystem
	 *
	 * @exception IOException
	 */
	public static void extractFileFromJar(String fileToExtractFromJar, String extractToDirectory, String extractedFileName)  throws IOException
	{
		extractToDirectory = extractToDirectory.replace("\\", "/");
		if(!extractToDirectory.endsWith("/")){
			extractToDirectory = extractToDirectory + "/";
		}
		makeDirs(extractToDirectory);
		InputStream myInputStream = null;
		try {
			myInputStream = FileHelper.class.getResourceAsStream(fileToExtractFromJar);	
		} catch (Exception e) {
			myInputStream =  FileHelper.class.getClass().getResourceAsStream(fileToExtractFromJar);
		}
		File myOutputFile = new File(extractToDirectory + extractedFileName);
		OutputStream myOutputStream = new FileOutputStream(myOutputFile);						
		IOUtils.copy(myInputStream, myOutputStream);	
		myOutputFile.setExecutable(true);
		myInputStream.close();
		myOutputStream.close();					
	}
	
	/**
	 * Joins two or more path components using the platform-specific file separator.
	 * 
	 * @param paths
	 * @return
	 */
	public static String join(String... paths)
	{
		return StringUtils.join(paths, Platform.getFileSeparator());
	}
	
	/**
	 * Create and return the path of a temporary directory with a path that is
	 * guaranteed to exist and be unique. Caller is responsible for deleting
	 * after use.
	 * 
	 * @return The absolute path of the created temp directory
	 * @throws IOException
	 */
	public static String getTempDirectory() throws IOException {
		return getTempDirectory(Platform.getTempDirectory(), "tmp");
	}
	
	/**
	 * Create and return the path of a temporary directory in a specified root
	 * directory, with a specified prefix. A unique suffix will be appended to
	 * the prefix to avoid collisions.
	 * 
	 * @param rootDirectory
	 *            The root directory where the temporary directory should be
	 *            created
	 * @param tempDirectoryName
	 *            A prefix to use when naming the temporary directory within the
	 *            root
	 * @return The absolute path of the created temp directory
	 * @throws IOException
	 */
	public static String getTempDirectory(String rootDirectory, String tempDirectoryName) throws IOException {
		File f = File.createTempFile(tempDirectoryName, "", new File(rootDirectory));
		f.delete();
		f.mkdir();
		return f.getAbsolutePath();
	}
}

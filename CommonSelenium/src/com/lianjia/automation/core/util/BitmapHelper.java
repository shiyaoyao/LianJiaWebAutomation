package com.lianjia.automation.core.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.lianjia.automation.CoreAutomation;
import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.loggers.control.CoreLogger;

/**
* Script Name   : <b>BitmapHelper</b>
* Description   : Date and time method library
*/

public class BitmapHelper {
	private static boolean baseline = false;

	public static void captureScreen(String scriptName,
		String description, String fileName, boolean error) {
		Dimension myScreen = Toolkit.getDefaultToolkit().getScreenSize();
	    Rectangle myBounds = new Rectangle(0, 0, myScreen.width, myScreen.height);
	    BufferedImage myImage = null;
	    Robot robot;
		try {
			robot = new Robot();
		    myImage =  robot.createScreenCapture(myBounds);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	    File myScreenshot = new File(fileName);
	    try {
			ImageIO.write(myImage, "jpeg", myScreenshot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * generates a screenshot
	 * 
	 */
	public static String captureScreenshotDoNotLog()
	{
		Dimension myScreen = Toolkit.getDefaultToolkit().getScreenSize();
	    return captureScreenshot("", 0, 0, myScreen.width, myScreen.height,"jpeg", false);
	}
	
	public static String captureScreenshot()
	{
		return captureScreenshot("");
	}
	
	/**
	 * generates a screenshot
	 * @param description		description (for logging)
	 */
	public static String captureScreenshot(String description){
		Dimension myScreen = Toolkit.getDefaultToolkit().getScreenSize();
	    return captureScreenshot("", 0, 0, myScreen.width, myScreen.height);
	}
	
	/**
	 * generates a screenshot
	 * @param description		description (for logging)
	 */
	public static String captureScreenshot(String description, int startingX, int startingY, int totalWidth, int totalHeight){
		Dimension myScreen = Toolkit.getDefaultToolkit().getScreenSize();
	    return captureScreenshot("", 0, 0, myScreen.width, myScreen.height,"jpeg");
	}

	public static String captureScreenshot(String description, int startingX, int startingY, int totalWidth, int totalHeight, String format)
	{
		return captureScreenshot( description,  startingX,  startingY,  totalWidth,  totalHeight,  format,true);
	}
	
	public static String captureScreenshot(String description, int startingX, int startingY, int totalWidth, int totalHeight, String format, boolean logScreenshot){
		String myFileName = DateHelper.genDateBasedRandVal() + "." + format;
		
		if(shouldGenerateBaselines() && !description.equals(""))
		{
			myFileName = "baseline_" + Platform.getOSNameAndVersion().toLowerCase().replace(" ", "_").replace("(", "").replace(")", "") + "_" + description.replace(" ", "_") + "." + format;	
		}
		
	    Rectangle myBounds = new Rectangle(startingX, startingY, totalWidth, totalHeight);
	    BufferedImage myImage = null;
	    Robot robot;
		try {
			robot = new Robot();
		    myImage =  robot.createScreenCapture(myBounds);
		} catch (AWTException e) {
			e.printStackTrace();
		}

		CoreAutomation.Log.debug("WRITING SCREENSHOT TO: '" + myFileName + "'");
	    File myScreenshot = new File(CoreLogger.getLogDirectory() + myFileName);
	    try {
			ImageIO.write(myImage, format, myScreenshot);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(logScreenshot)
		{
			String logmessage = String.format("<a href=%s target=_blank><img src=%s width=128, height=90  border='1'></a>", myFileName, myFileName);
			CoreLogger.info("SCREENSHOT:  " + logmessage);			
		}
		return myFileName;			
	}
	
	public static BufferedImage getScreenAsImage(int startingX, int startingY, int totalWidth, int totalHeight, String format){
	    Rectangle myBounds = new Rectangle(startingX, startingY, totalWidth, totalHeight);
	    BufferedImage myImage = null;
	    Robot robot;
		try {
			robot = new Robot();
		    myImage =  robot.createScreenCapture(myBounds);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	    return myImage;		
	}
	
	public static void cropImage(String imageName, int startingX, int startingY, int width, int height, String croppedImageName)
	{
		CoreLogger.debug("cropImage imageName = '" +imageName+ "'");
		CoreLogger.debug("cropImage startingX = '" +startingX+ "'");
		CoreLogger.debug("cropImage startingY = '" +startingY+ "'");
		CoreLogger.debug("cropImage width = '" +width+ "'");
		CoreLogger.debug("cropImage height = '" +height+ "'");
		CoreLogger.debug("cropImage croppedImageName = '" +croppedImageName+ "'");		
		String imageFormat = FileHelper.getFileExtension(imageName);
		CoreLogger.debug("cropImage imageFormat = '" + imageFormat+"'");				
		
		BufferedImage image;
		try {
			image = ImageIO.read(new File(imageName));
			ImageIO.write(image.getSubimage(startingX, startingY, width, height), imageFormat, new File(croppedImageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public static Image getImageFromFile(String myImageFileName)
	{
		try {
			return ImageIO.read(new File(myImageFileName));
		} catch (IOException e2) {
			CoreLogger.error("File Not Found: " + myImageFileName);
			return null;
		}
	}
	
	public static int[] getPixelArrayFromImage(Image myImageName, int startingX, int startingY, int width, int height)
	{
		PixelGrabber mySmallImageGrabber = new PixelGrabber(myImageName, startingX, startingY, width, height, true);
		try {
			mySmallImageGrabber.grabPixels();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		return (int[]) mySmallImageGrabber.getPixels();

	}
	
	public static boolean waitForImage(String mySmallImageFileName, int startingX, int startingY, int width, int height)
	{
		boolean foundImage = false;
		String imageFormat = FileHelper.getFileExtension(mySmallImageFileName);
		for(int searchIterator = 0; searchIterator < Platform.giLongTO; searchIterator++)
		{
			Platform.sleep(1);
			
			if(subImageExistsOnScreen(mySmallImageFileName, getScreenAsImage(startingX, startingY, width, height, imageFormat)))
			{
				foundImage = true;
				break;
			} else {
				Platform.sleep(1);
			}
		}
		return foundImage;
	}

	public static boolean subImageExistsOnScreen(String mySmallImageFileName, BufferedImage screenAsImage) //, int approximateXOffset, int approximateYOffset, boolean searchEntireImage)
	{

		Image mySmallImage = getImageFromFile(mySmallImageFileName);
		Image myBigImage = screenAsImage; 

		int smallWidth = mySmallImage.getWidth(null);
		int smallHeight = mySmallImage.getHeight(null);

		int bigWidth = myBigImage.getWidth(null);
		int bigHeight = myBigImage.getHeight(null);
		CoreLogger.debug("subImageExistsOnScreen file = '" +mySmallImageFileName+ "'");
		
		CoreLogger.debug("subImageExistsOnScreen smallWidth = '" +smallWidth+ "'");
		CoreLogger.debug("subImageExistsOnScreen smallHeight = '" +smallHeight+ "'");
		CoreLogger.debug("subImageExistsOnScreen bigWidth = '" + bigWidth+"'");
		CoreLogger.debug("subImageExistsOnScreen bigHeight = '" + bigHeight+ "'");

		int mySmallImagePixels[] = getPixelArrayFromImage(mySmallImage, 0, 0, smallWidth, smallHeight);
		int myBigImagePixels[] = getPixelArrayFromImage(myBigImage, 0, 0, bigWidth, bigHeight);
		Arrays.sort(mySmallImagePixels);
		Arrays.sort(myBigImagePixels);
		for(int searchIterator : mySmallImagePixels)
		{
			if (Arrays.binarySearch(myBigImagePixels, searchIterator) < 0) 
			{
				CoreLogger.debug("subImageExistsOnScreen result < 0");
				return false;
			}
		}
		return true;
	}	
	
	public static boolean subImageExists(String mySmallImageFileName, String myBigImageFileName) //, int approximateXOffset, int approximateYOffset, boolean searchEntireImage)
	{
		Image mySmallImage = getImageFromFile(mySmallImageFileName);
		Image myBigImage = getImageFromFile(myBigImageFileName);
		
		int smallWidth = mySmallImage.getWidth(null);
		int smallHeight = mySmallImage.getHeight(null);

		int bigWidth = myBigImage.getWidth(null);
		int bigHeight = myBigImage.getHeight(null);

		CoreLogger.debug("subItemExists smallWidth = '" +smallWidth+ "'");
		CoreLogger.debug("subItemExists smallHeight = '" +smallHeight+ "'");
		CoreLogger.debug("subItemExists bigWidth = '" + bigWidth+"'");
		CoreLogger.debug("subItemExists bigHeight = '" + bigHeight+ "'");

		int mySmallImagePixels[] = getPixelArrayFromImage(mySmallImage, 0, 0, smallWidth, smallHeight);
		int myBigImagePixels[] = getPixelArrayFromImage(myBigImage, 0, 0, bigWidth, bigHeight);
		
		Arrays.sort(myBigImagePixels);
		for(int searchIterator : mySmallImagePixels)
		{
			if (Arrays.binarySearch(myBigImagePixels, searchIterator) < 0) 
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean subImageExistsOnScreen(String mySmallImageFileName, BufferedImage screenAsImage, int approximateXOffset, int approximateYOffset)
	{
		Point myPoint = getSubImagePointOnScreen(mySmallImageFileName, screenAsImage, approximateXOffset, approximateYOffset, false);
		return (myPoint.x == 0 && myPoint.y ==0);
	}
	
	public static Point getSubImagePointOnScreen(String mySmallImageFileName, BufferedImage screenAsImage, int approximateXOffset, int approximateYOffset, boolean searchEntireImage)
	{
		boolean foundMatch = false;
		Image mySmallImage = getImageFromFile(mySmallImageFileName);
		Image myBigImage = screenAsImage; 
		
		int smallWidth = mySmallImage.getWidth(null);
		int smallHeight = mySmallImage.getHeight(null);

		int bigWidth = myBigImage.getWidth(null);
		int bigHeight = myBigImage.getHeight(null);

		CoreLogger.debug("subItemExists smallWidth = '" +smallWidth+ "'");
		CoreLogger.debug("subItemExists smallHeight = '" +smallHeight+ "'");
		CoreLogger.debug("subItemExists bigWidth = '" + bigWidth+"'");
		CoreLogger.debug("subItemExists bigHeight = '" + bigHeight+ "'");

		int mySmallImagePixels[] = getPixelArrayFromImage(mySmallImage, 0, 0, smallWidth, smallHeight);
		int myCurrentY = approximateYOffset;
		int myCurrentX = approximateXOffset;
		for(myCurrentX = approximateXOffset; myCurrentX < (bigWidth - smallWidth); myCurrentX++)
		{			
			CoreLogger.debug("subItemExists  X=" + myCurrentX + " Y=" + myCurrentY);
			
			if(java.util.Arrays.equals(getPixelArrayFromImage(myBigImage, myCurrentX, myCurrentY, smallWidth, smallHeight), mySmallImagePixels))
			{
				CoreLogger.debug("WOW, THEY MATCHED!!!!");
				foundMatch = true;
				break;
			}
			
			if(!searchEntireImage)
			{
				if (myCurrentY > (approximateYOffset + 5) && myCurrentX > (approximateXOffset + 5))
				{
					CoreLogger.debug("COULD NOT MATCH");
					break;
				}
				
				if (myCurrentX > (approximateXOffset + 5))
				{
					myCurrentX = approximateXOffset;
					myCurrentY++;
				}				
			}			
			
			if(myCurrentX + 1 == (bigWidth - smallWidth))
			{
				myCurrentX = approximateXOffset;
				myCurrentY++;
			}
			
			if(myCurrentY > bigHeight)
			{
				break;
			}			
		}
		
		if(foundMatch)
		{
			return new Point(myCurrentX,myCurrentY);
		} else{
			return new Point(0,0);			
		}
	}

	public static void generateBaselines()
	{
		baseline = true;
	}

	public static boolean shouldGenerateBaselines()
	{
		return baseline;
	}
}

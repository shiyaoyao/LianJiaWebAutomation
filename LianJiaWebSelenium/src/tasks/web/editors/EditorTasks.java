package tasks.web.editors;

import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import tasks.base.BaseTask;
import tasks.web.util.SeleniumUtils;
import tasks.web.util.SimpleFileIO;
import tasks.web.util.StringUtils;
import tasks.web.util.WindowManagement;
import appobjects.web.WebFrame;
import appobjects.web.editors.CKEditor;
import appobjects.web.editors.ClipboardImage;
import appobjects.web.editors.ClipboardText;
import appobjects.web.editors.Editor;
import appobjects.web.editors.PTEditor;
import appobjects.web.editors.RTEditor;

import com.lianjia.automation.core.web.WebBrowser;

/**
 * General purpose Task class for all editor types: 
 * <ul>
 * <li>{@link CKEditor.java}</li>
 * <li> {@link RTEditor.java} (LianJiaWeb RTE)</li>
 * <li> {@link DLEditor.java} (dialog editor)</li>
 * <li> {@link PTEditor.java}</li> 
 * </ul>
 */
public class EditorTasks extends BaseTask {
	private static final Logger log = Logger.getLogger(EditorTasks.class);

	private static CKEditor cke = new CKEditor();
	private static RTEditor rte = new RTEditor();
	private static PTEditor pte = new PTEditor();
	private static String eol = System.getProperty("line.separator");
	
	protected static Editor editor() {
		if(rte.exists()) {
			log.debug("Editor: RTE");
			return rte;
		} else if(cke.exists()) {
			log.debug("Editor: CKE");
			return cke;
		} else if(pte.exists()) {
			log.debug("Editor: Plain text");
			return pte;
		} 
			
		assertNotNull("Found Editor", null);
		return null;
	}
	/**
	 * <p>Set the content of the current editor to the specified content string</p>
	 * <p>content can contain HTML formatting tags.</p>
	 * @param content
	 */
	public static void setContent(String content) {
		editor().setEditorContent(content);
	}
	
	/**
	 * Gets the content of the current editor, including all HTML formatting tags.<br><br>
	 * Returns HTML rich text.
	 * @return
	 */
	public static String getContent() { 
		return editor().getEditorContent();
	}

	/**
	 * Get the content of the current editor as plain text<br>
	 * HTML tags and EOL characters are removed.
	 * @return
	 */
	public static String getContentPlainText() {
		String plain = editor().getEditorPlainText();
//		plain = plain.replace(eol,"").replace("\n", "");
		return plain;
	}
	
	/**
	 * Checks the contents of the editor to see if it's empty. <br>
	 * It's considered empty if it doesn't contain visible characters. That is,
	 * if it contains <i>only</i> white space it's considered empty.
	 * @return
	 */
	public static boolean isEmpty() {
		String plain = getContentPlainText();
		if(plain.matches("^\\s*$")){
			log.warn("Editor content contains just white space");
			plain = plain.trim();
		}
		return plain.isEmpty();
	}
	
	/**
	 * Checks the content of the current editor for occurrences of sText<br>
	 * sText can contain HTML formatting tags (e.g. "rich text") as part of the content test.<br><br>
	 * Return 
	 * @param sText String to find in the editor
	 * @return 
	 * the number of occurrences of <b>sText</b> found. Zero if no match is found.
	 */
	public static int hasContent(String sText) {
		String sBody = sText.contains("</") || sText.contains("<br>") ? getContent() : getContentPlainText();
		log.debug("Needle: <code>"+StringUtils.escapeHtmlTags(sText)+"</code>");
		log.debug("Haystack: <code>"+StringUtils.escapeHtmlTags(sBody)+"</code>");
		int count = StringUtils.countMatches(sBody, sText);
		if(count == 0) {
			log.warn("Didn't find exact match. Try lowercase HTML tags");
			count = StringUtils.countMatches(StringUtils.cleanUpHtml(sBody), (StringUtils.cleanUpHtml(sText)));
		}
		return count;
	}
	/**
	 * Compares content of the current editor to the expected text (sText)<br>
	 * sText can contain HTML formatting tags (e.g. "rich text") as part of the content test.<br><br>
	 * @param sText
	 * @return
	 */
	public static boolean verifyContents(String sText) {
		String expectedContent = StringUtils.cleanUpHtml(sText);
		String actualContent = sText.contains("</") || sText.contains("<br>") ? getContent() : getContentPlainText();
		actualContent = StringUtils.cleanUpHtml(actualContent);
		
		return logCompare(Pattern.compile(expectedContent), actualContent,"Editor content match expected");
	}
	/**
	 * Checks the content of the current editor for plain text occurrences of sText<br>
	 * All HTML tags and end-of-line characters are removed from sText before 
	 * searching the plain text editor content
	 * @param sText
	 * @return
	 * the number of occurrences of <b>sText</b> found. Zero if no match is found.
	 */
	public static int hasContentPlainText(String sText) {
		String sBody = StringUtils.removeHtmlTags(sText).replace(eol,"").replace("\n", "");
		return hasContent(sBody);
	}
	
	private static void pasteClipboard() throws AWTException {
		if (!WebBrowser.isInternetExplorer()) {
			// this blurs window focus on IE
			try {
				WindowManagement.setBrowserFocus();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		editor().setFocus();
		SeleniumUtils.pressCtrlEnd();
		
		SeleniumUtils.pressCtrlV();
	}
	
	/**
	 * Copies the specified text to the system clipboard and  pastes it
	 * into the editor content.<br><br>
	 * The method does not specify how the pasted text will affect existing 
	 * editor content.
	 * @param sText
	 * @throws Throwable
	 */
	public static void pasteText(String sText) throws Throwable {
		ClipboardText clip = new ClipboardText();
		clip.copy(sText);
		pasteClipboard();
	}
	
	/**
	 * Copies the specified image file to the clipboard and pastes it
	 * into the editor content.<br><br>
	 * The method attempts to paste the image at the end of current content.
	 * @param sFileName
	 * @throws IOException
	 */
	public static void pasteImage(String sFileName) throws IOException {
		loadImageToClipboard(sFileName);
		try {
			pasteClipboard();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebFrame.selectMainFrame();
	}
	
	/**
	 * Copies the specified image file to the clipboard.
	 * @param sFileName
	 * @throws IOException
	 */
	public static void loadImageToClipboard(String sFileName) throws IOException {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		File imageFile = SimpleFileIO.getResourceAsFile(sFileName);
		ClipboardImage clipbdImage = new ClipboardImage(ImageIO.read(imageFile));
		clip.setContents(clipbdImage, null);
	}
	
	/**
	 * Counts the occurrences of the HTML image tag found in the editor.
	 * @return
	 */
	public static int getImageCount() {
		int count = 0;
		String content = editor().getEditorContent();
		Pattern p = Pattern.compile("<img .*?>|<IMG .*?>");
		Matcher m = p.matcher(content);
		while(m.find())
			count++;
		return count;
	}
	
	/**
	 * Counts the occurrences of the HTML Button tags found in the editor.
	 * @return
	 * TODO:
	 */
	public static int getButtonCount() {
		int count = 0;
		String content = editor().getEditorContent();
		Pattern p = Pattern.compile("<input .*? type=\"button\"(.*?)");
		Matcher m = p.matcher(content);
		while(m.find())
			count++;			
		return count;
	}
	
	public static String getButtonTagfromEditor(){
		String sTag="";
		String content = editor().getEditorContent();
		Pattern p = Pattern.compile("<input .*? type=\"button\"(.*?)>");
		Matcher m = p.matcher(content);
		while(m.find())
			sTag = m.group();
		return sTag;
	}
	public static boolean isButtonOnEditor(){
		return getButtonCount()>0;
	}
	public static String getbuttonTitle(){
		String sTitle="";
		if(isButtonOnEditor()){
			if(editor().getButtonOnEditor().exists())
				sTitle = editor().getButtonOnEditor().getText();
			else{
				String sTag = getButtonTagfromEditor();
				System.out.println(sTag);
				Pattern p = Pattern.compile("value=\"(.*?)\"");
				Matcher m = p.matcher(sTag);
				if(m.find()){
					sTitle = m.group();
					sTitle = sTitle.substring(sTitle.indexOf("\"")+1,sTitle.lastIndexOf("\""));
				}
					
					
			}
		}
		return sTitle;
	}
	
	public static boolean isButtonOnEditorDisabled() {
		boolean bDisabled = false;
		if (isButtonOnEditor()) {
			if (editor().getButtonOnEditor().exists())
				bDisabled = !editor().getButtonOnEditor().isEnabled();
			else {
				String sTag = getButtonTagfromEditor();
				bDisabled = sTag.contains("disabled");
			}
		}
		return bDisabled;
	}
	
	public static HashMap<String, String> getButtonInfo() {
		return editor().getButtonInfo();
	}
	
	/**
	 * Gets the src attribute of the specified image
	 * 
	 * @param whichImage the n-th image (1-indexed)
	 * @return src attribute string
	 */
	public static String getImageSource(int whichImage) {
		ArrayList<String> matches = new ArrayList<String>();
		String content = editor().getEditorContent();
		Pattern p = Pattern.compile("<img .*?>|<IMG .*?>");
		Matcher m = p.matcher(content);
		while(m.find()){
			matches.add(m.group());
		}
		if(whichImage > matches.size())
			return "";
		String source = matches.get(whichImage-1);
		int start =source.indexOf("src=\"")+5;
		 source = source.substring(start,source.indexOf("\"", start+1));
		return source;
	}
	
	
	/**
	 * Appends text to the current contents of the rich text editor body.
	 * 
	 * @param sText
	 */
	public static void appendText(String sText){
		String existingText = getContent();
		//The existing text contains a "\n", even before anything's been put in the editor.
		//The js eval in setEditorContent chokes if there is a "\n" in the string.
		existingText = existingText.replaceAll("\n","");
		if (SeleniumUtils.isInternetExplorer()){
			existingText = existingText.replaceAll("\r","");
		}

		setContent(existingText+sText);
	}
	/**
	 * Prepends text to the current contents of the rich text editor body.
	 * Support HTML tags.
	 * 
	 * @param sText
	 */
	public static void prependText(String sText){
		String existingText = getContent();
		//The existing text contains a "\n", even before anything's been put in the editor.
		//The js eval in setEditorContent chokes if there is a "\n" in the string.
		existingText = existingText.replaceAll("\n","");
		if (SeleniumUtils.isInternetExplorer()){
			existingText = existingText.replaceAll("\r","");
		}
		setContent(sText+existingText);
	}
	
	/**
	 * Checks for existence of an object with the specified Xpath
	 * @param sXpath An Xpath locator relative to the editor body.<br>
	 * The string should begin with a tagName and NOT with a '/' or '//'<br><br>
	 * Example: <code>isObjectInEditor("a[contains(@href,'google.com')]")</code>
	 *@return true if object found
	 */
	public static boolean isObjectInEditor(String sXpath){
		return editor().isObjectInEditor(sXpath);
	}
	
	/**
	 * Clicks an object in the editor body with the specified Xpath
	 * @param sXpath An Xpath locator relative to the editor body.<br>
	 * The string should begin with a tagName and NOT with a '/' or '//'<br><br>
	 * Example: <code>isObjectInEditor("a[contains(@href,'google.com')]")</code>
	 * 
	 */
	public static void clickObjectInEditor(String sXpath) {
		editor().clickObjectInEditor(sXpath);
	}
}
